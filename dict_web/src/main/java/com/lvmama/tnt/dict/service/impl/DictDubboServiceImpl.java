package com.lvmama.tnt.dict.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.lvmama.tnt.dict.dao.DictDao;
import com.lvmama.tnt.dict.dto.DictDataDto;
import com.lvmama.tnt.dict.entity.DictData;
import com.lvmama.tnt.dict.service.IDictDubboService;
import com.lvmama.tnt.dict.service.IDictTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenwenshun on 2017/1/4.
 */
@Service
@Transactional
public class DictDubboServiceImpl implements IDictDubboService{
    @Autowired
    private DictDao dictDao;

    @Autowired
    private IDictTypeService dictTypeService;
    public List<DictDataDto> loadDatasByTypeCd(String dictTypeCd) {
        StringBuilder hql = new StringBuilder("from DictData d where d.dictType.dictTypeCd=:dictTypeCd ");
        hql.append(" and d.enableFlg=true and d.dictType.enableFlg=true ");
        HashMap<String,Object> param = new HashMap<>();
        param.put("dictTypeCd",dictTypeCd);
        List<DictData> list = this.dictDao.queryByParameter(hql.toString(),param,false);
        List<DictDataDto> dataDtoList = Lists.newArrayList(FluentIterable.from(list).
                transform(new Function<DictData, DictDataDto>() {
            private DictDataDto dictDataDto;
            public DictDataDto apply(DictData dictData) {
                dictDataDto = new DictDataDto();
                BeanUtils.copyProperties(dictData,dictDataDto);
                dictDataDto.setDictTypeId(dictData.getDictType().getId());
                return dictDataDto;
            }
        }));

        return dataDtoList;
    }

    public Map<String, String> loadDataMap(String dictTypeCd) {
        return this.dictTypeService.loadDataMap(dictTypeCd);
    }

    public DictDataDto getDictDataByCd(String dictTypeCd, String dictCd) {
        StringBuilder hql = new StringBuilder("from DictData d where d.dictType.dictTypeCd=:dictTypeCd ");
        hql.append(" and d.enableFlg=true and d.dictType.enableFlg=true ");
        hql.append(" and d.dictCd = :dictCd ");
        HashMap<String,Object> param = new HashMap<>();
        param.put("dictTypeCd",dictTypeCd);
        param.put("dictCd",dictCd);
        DictData dictData = this.dictDao.findUnique(hql.toString(),param);
        if(dictData==null)
            return null;
        DictDataDto dictDataDto = new DictDataDto();
        BeanUtils.copyProperties(dictData,dictDataDto);
        dictDataDto.setDictTypeId(dictData.getDictType().getId());
        return dictDataDto;
    }

//    public List<DictDataDto> getDictDataListByDictTypeCd(String dictTypeCd) {
//        StringBuilder hql = new StringBuilder("from DictData d where d.dictType.dictTypeCd=:dictTypeCd ");
//        hql.append(" and d.enableFlg=true and d.dictType.enableFlg=true ");
//        HashMap<String,Object> param = new HashMap<>();
//        param.put("dictTypeCd",dictTypeCd);
//        List<DictData> list = this.dictDao.queryByParameter(hql.toString(),param,false);
//        List<DictDataDto> dataDtoList = Lists.newArrayList(FluentIterable.from(list).transform(new Function<DictData, DictDataDto>() {
//            private DictDataDto dictDataDto;
//            @Override
//            public DictDataDto apply(DictData dictData) {
//                dictDataDto = new DictDataDto();
//                BeanUtils.copyProperties(dictData,dictDataDto);
//                dictDataDto.setDictTypeId(dictData.getDictType().getId());
//                return dictDataDto;
//            }
//        }));
//        return dataDtoList;
//    }
}
