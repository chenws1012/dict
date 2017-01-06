/**
 * 
 */
package com.lvmama.tnt.dict.service;

import com.lvmama.tnt.dict.dto.DictDataDto;
import com.lvmama.tnt.dict.dto.DictTypeDto;
import com.lvmama.tnt.dict.dto.Page;
import com.lvmama.tnt.dict.entity.DictData;
import com.lvmama.tnt.dict.entity.DictType;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 * 
 * @author chenwenshun
 * 
 */
public interface IDictTypeService {
	/** 根据id取得数据字典 */
	public DictType loadDictById(long dictTypeId);

	/** 根据cd取得数据字典 */
	public DictType loadDictByCd(String dictTypeCd);

	/** 根据cd取得数据字典 */
	public DictData loadDictDataByCd(String dictTypeCd, String dictCd, boolean onlyEnable);
	
	/** 根据cd更新数据字典 */
	public void updateDictDataByCd(String dictTypeCd, String dictCd, String dictName);
	public void saveOrUpdateDictDataByCd(String dictTypeCd, String dictCd, String dictName);
	/** 保存 */
	public DictType saveDict(DictTypeDto dto);

	/** 批量保存字典列表 */
	public void saveDatas(List<DictDataDto> lstDeleted, List<DictDataDto> lstInsert, List<DictDataDto> lstUpdated,
						  Long dictTypeId);

	/** 删除 */
	public void deleteDict(long dictTypeId);
	/** 删除子表记录 */
	public void deleteDictData(long dictDataId);

	/** 翻页查询 */
	public Page<DictType> findPageDict(Page<DictType> page, Map<String, Object> param, Map<String, String> mapOrder);

	/** 根据主表id获取数据列表 */
	public List<DictData> loadDatasByTypeCd(String dictTypeCd);
	public List<DictData> loadDatasByTypeCd(String dictTypeCd, String dictRemark);
	/**获取所有的子表，包含无需*/
	public List<DictData> loadAllDatasByTypeCd(String dictTypeCd);
	/** 判断dictTypeCd是否存在 */
	public boolean existDictTypeCd(String dictTypeCd, String dictTypeOld);

	/** 判断dataCd是否存在 */
	public boolean existDataCd(long dictTypeId, String dataCd, String dataCdOld);

	/** 根据dictTypeCd取得数据map，用于填充下拉框 */
	public Map<String, String> loadDataMap(String dictTypeCd);
	public  void saveDictData(DictData dictData);
	/** 根据field取得数据字典 */
	public  DictData getDictDataByField(String dictTypeCd, String val, String field);
	/** 根据field取得所有满足数据字典 */
	public List<DictData> getDictDataListByField(String dictTypeCd, String val, String field);
}
