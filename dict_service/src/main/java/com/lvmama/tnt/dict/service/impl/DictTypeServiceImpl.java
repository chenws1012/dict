/**
 * 
 */
package com.lvmama.tnt.dict.service.impl;

import com.lvmama.tnt.dict.dao.DictDao;
import com.lvmama.tnt.dict.dto.DictDataDto;
import com.lvmama.tnt.dict.dto.DictTypeDto;
import com.lvmama.tnt.dict.dto.Page;
import com.lvmama.tnt.dict.entity.DictData;
import com.lvmama.tnt.dict.entity.DictType;
import com.lvmama.tnt.dict.service.IDictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 数据字典
 * 
 * @author chenwenshun
 * 
 */
@Service
@Transactional
public class DictTypeServiceImpl implements IDictTypeService {

	@Autowired
	private DictDao tempDao;

	public DictType loadDictById(long dictTypeId) {

		return tempDao.findById(DictType.class, dictTypeId);
	}

	public DictType loadDictByCd(String dictTypeCd) {

		DictType dictType = tempDao.queryUniqueByParam(DictType.class, "dictTypeCd", dictTypeCd);
		return dictType;
	}

	public DictData loadDictDataByCd(String dictTypeCd, String dictCd, boolean onlyEnable) {

		if ( StringUtils.isBlank(dictCd) ) {
			return null;
		}
		StringBuffer hql = new StringBuffer(
				"from DictData d where d.dictType.dictTypeCd=:dictTypeCd and d.dictCd=:dictCd ");
		if ( onlyEnable ) {
			hql.append("  and d.enableFlg=true and d.dictType.enableFlg=true  ");
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dictTypeCd", dictTypeCd);
		param.put("dictCd", dictCd);
		List<DictData> list = tempDao.queryByParameter(hql.toString(), param, true);
		DictData dictData = null;
		if ( list.size() > 0 ) {
			dictData = list.get(0);
		}
		return dictData;
	}

	public void saveOrUpdateDictDataByCd(String dictTypeCd, String dictCd, String dictName) {

		DictType dictType = loadDictByCd(dictTypeCd);
		if ( dictType != null ) {
			StringBuffer hql = new StringBuffer(
					"from DictData d where d.dictType.dictTypeCd=:dictTypeCd and d.dictCd=:dictCd");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("dictTypeCd", dictTypeCd);
			param.put("dictCd", dictCd);
			DictData dictData = tempDao.findUnique(hql.toString(), param);
			if ( dictData == null ) {

				dictData = new DictData();
				dictData.setDictCd(dictCd);
				dictData.setDictType(dictType);
			}
			dictData.setDictName(dictName);
			tempDao.merge(dictData);
		}
	}

	public void updateDictDataByCd(String dictTypeCd, String dictCd, String dictName) {

		StringBuffer hql = new StringBuffer(
				"from DictData d where d.dictType.dictTypeCd=:dictTypeCd and d.dictCd=:dictCd");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dictTypeCd", dictTypeCd);
		param.put("dictCd", dictCd);
		DictData dictData = tempDao.findUnique(hql.toString(), param);
		dictData.setDictName(dictName);
		tempDao.merge(dictData);
	}

	public DictType saveDict(DictTypeDto dto) {

		String dictTypeCd_old = "";
		DictType dictType;
		if ( dto.getId() != null && dto.getId() != 0 ) {
			dictType = loadDictById(dto.getId());
			dictTypeCd_old = dictType.getDictTypeCd();
		} else {
			dictType = new DictType();
		}
		if ( existDictTypeCd(dto.getDictTypeCd(), dictTypeCd_old) ) {
			throw new RuntimeException("dictTypeCd already exists! ");
		}
		dictType.setDictTypeCd(dto.getDictTypeCd());
		dictType.setDictTypeName(dto.getDictTypeName());
		dictType.setDispOrderNo(dto.getDispOrderNo());
		dictType.setRemark(dto.getRemark());
		dictType.setCreatedDate(new Date());
		dictType.setUpdatedDate(new Date());
		dictType.setEnableFlg(dto.getEnableFlg());
		dictType = tempDao.save(dictType);
		return dictType;
	}

	private void saveDictData(List<DictDataDto> datas, DictType dictType) {

		for ( DictDataDto dto : datas ) {
			String dictCd_old = "";
			DictData dictData;
			if ( dto.getId() != null && dto.getId() != 0 ) {
				dictData = tempDao.findById(DictData.class, dto.getId());
				dictCd_old = dictData.getDictCd();
			} else {
				if ( existDataCd(dictType.getId(), dto.getDictCd(), dictCd_old) ) {
					throw new RuntimeException("dictCd already exists! ");
				}
				dictData = new DictData();
			}

			dictData.setDictCd(dto.getDictCd());
			dictData.setDictName(dto.getDictName());
			dictData.setRemark(dto.getRemark());
			dictData.setDispOrderNo(dto.getDispOrderNo());
			dictData.setCreatedDate(new Date());
			dictData.setUpdatedDate(new Date());
			dictData.setEnableFlg(dto.getEnableFlg());
			dictData.setI18n(dto.getI18n());
			dictData.setDictType(dictType);
			tempDao.save(dictData);

		}
	}

	public void saveDatas(List<DictDataDto> lstDeleted, List<DictDataDto> lstInsert, List<DictDataDto> lstUpdated,
			Long dictTypeId) {

		DictType dictType = loadDictById(dictTypeId);

		for ( DictDataDto dictData : lstDeleted ) {
			deleteDictData(dictData.getId());
		}
		saveDictData(lstInsert, dictType);
		saveDictData(lstUpdated, dictType);
	}

	public void deleteDict(long dictTypeId) {

		DictType dictType = loadDictById(dictTypeId);
		for ( DictData dictData : dictType.getDictDatas() ) {
			deleteDictData(dictData.getId());
		}
		tempDao.deleteById(DictType.class, dictTypeId);
	}

	public void deleteDictData(long dictDataId) {

		tempDao.deleteById(DictData.class, dictDataId);
	}

	
	public Page<DictType> findPageDict(Page<DictType> page, Map<String, Object> param, Map<String, String> mapOrder) {

		StringBuffer hql = new StringBuffer("from DictType t where 1=1 ");
		for ( String key : param.keySet() ) {
			Object val = param.get(key);
			if ( val != null ) {
				hql.append(" and ").append(key);
				if ( val instanceof String && ((String) val).indexOf("%") != -1 ) {
					hql.append(" like :");
				} else {
					hql.append(" = :");
				}
				hql.append(key);
			}
		}
		hql.append(" order by ");
		for ( String key : mapOrder.keySet() ) {
			hql.append(key).append(" ").append(mapOrder.get(key)).append(",");
		}
		hql.append(" dispOrderNo asc");
		page = tempDao.findPage(page, hql.toString(), param);
		return page;
	}
	public List<DictData> loadDatasByTypeCd(String dictTypeCd) {
		return loadDatasByTypeCd(dictTypeCd, null);
	}
	
	public List<DictData> loadDatasByTypeCd(String dictTypeCd,String dictRemark) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from DictData d where d.dictType.dictTypeCd=:dictTypeCd ");
		if (StringUtils.isNotBlank(dictRemark)){
			hql.append(" and d.remark =:dictRemark ");
			param.put("dictRemark", dictRemark);
		}
		hql.append(" and d.enableFlg=true and d.dictType.enableFlg=true order by dispOrderNo,d.dictCd ");
		
		param.put("dictTypeCd", dictTypeCd);
		List<DictData> list = tempDao.queryByParameter(hql.toString(), param, true);
		return list;
	}
	public List<DictData> loadAllDatasByTypeCd(String dictTypeCd) {
		
		StringBuffer hql = new StringBuffer(
				"from DictData d where d.dictType.dictTypeCd=:dictTypeCd order by dispOrderNo,d.dictCd ");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dictTypeCd", dictTypeCd);
		List<DictData> list = tempDao.queryByParameter(hql.toString(), param, true);
		return list;
	}

	
	public boolean existDictTypeCd(String dictTypeCd, String dictTypeOld) {

		StringBuffer hql = new StringBuffer(
				"from DictType m where dictTypeCd=:dictTypeCd and dictTypeCd <> :dictTypeCdOld ");
		Map<String, Object> pram = new HashMap<String, Object>();
		pram.put("dictTypeCd", dictTypeCd);
		pram.put("dictTypeCdOld", dictTypeOld);
		long cnt = tempDao.countHqlResult(hql.toString(), pram);
		if ( cnt == 0 ) {
			return false;
		}
		return true;
	}

	
	public boolean existDataCd(long dictTypeId, String dataCd, String dataCdOld) {

		StringBuffer hql = new StringBuffer(
				"from DictData d where dictCd=:dataCd and  dictCd <> :dataCdOld and d.dictType.id=:dictTypeId ");
		Map<String, Object> pram = new HashMap<String, Object>();
		pram.put("dataCd", dataCd);
		pram.put("dataCdOld", dataCdOld);
		pram.put("dictTypeId", dictTypeId);
		long cnt = tempDao.countHqlResult(hql.toString(), pram);
		if ( cnt == 0 ) {
			return false;
		}
		return true;
	}

	
	public Map<String, String> loadDataMap(String dictTypeCd) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		DictType dictType = loadDictByCd(dictTypeCd);
		for ( DictData data : dictType.getDictDatas() ) {
			map.put(data.getDictCd(), data.getDictName());
		}
		return map;
	}


	
	public void saveDictData(DictData dictData) {
		// TODO Auto-generated method stub
		try{
			tempDao.merge(dictData);
		}catch(Exception ee){
			tempDao.save(dictData);
		}
	}

	
	public DictData getDictDataByField(String dictTypeCd, String val, String field) {
		StringBuffer hql = new StringBuffer(
				"from DictData d where d.dictType.dictTypeCd=:dictTypeCd and ");
		hql.append( field+" =:field");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dictTypeCd", dictTypeCd);
		param.put("field", val);
		hql.append(" order by d.id asc");
		List<DictData> list = tempDao.queryByParameter(hql.toString(), param, true);
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public List<DictData> getDictDataListByField(String dictTypeCd, String val, String field) {
		StringBuffer hql = new StringBuffer(
				"from DictData d where d.dictType.dictTypeCd=:dictTypeCd and ");
		hql.append( field+" =:field");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("dictTypeCd", dictTypeCd);
		param.put("field", val);
		hql.append(" order by d.id asc");
		List<DictData> list = tempDao.queryByParameter(hql.toString(), param, true);
		return list;
	}
}
