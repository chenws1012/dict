package com.lvmama.tnt.dict;

import com.lvmama.tnt.dict.dto.DictDataDto;
import com.lvmama.tnt.dict.service.IDictDubboService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by chenwenshun on 2017/1/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = "classpath:application.xml")
public class DictDubboSerivceTest extends AbstractJUnit4SpringContextTests{
    @Autowired
    private IDictDubboService dictDubboService;

    @Test
    public void loadDatasByTypeCdTest(){
        List<DictDataDto> dataDtoList = dictDubboService.loadDatasByTypeCd("XXXSWITCH");
        Assert.notEmpty(dataDtoList);
    }

    @Test
    public void loadDataMapTest(){
        Map<String,String> datamap = dictDubboService.loadDataMap("LTS_DECIDER");
        Assert.notEmpty(datamap);
    }
    @Test
    public void getDictDataByCdTest(){

        DictDataDto dictDataDto = dictDubboService.getDictDataByCd("XXXSWITCH","descriptor");
        Assert.isTrue(dictDataDto.getEnableFlg());
    }
}
