package com.lvmama.tnt.dict.service;


import com.lvmama.tnt.dict.dto.DictDataDto;

import java.util.List;
import java.util.Map;

/**
 * Created by chenwenshun on 2017/1/4.
 */
public interface IDictDubboService {

    /** 根据主表cd获取数据列表 */
    public List<DictDataDto> loadDatasByTypeCd(String dictTypeCd);

    /** 根据dictTypeCd取得数据map，用于填充下拉框 */
    public Map<String, String> loadDataMap(String dictTypeCd);

    /** 根据cd取得数据字典
     * @param dictTypeCd
     * @param dictCd
     * */
    public DictDataDto getDictDataByCd(String dictTypeCd, String dictCd);

}
