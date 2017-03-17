/**
 * 
 */
package com.lvmama.tnt.dict.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lvmama.tnt.dict.dto.DictDataDto;
import com.lvmama.tnt.dict.dto.DictTypeDto;
import com.lvmama.tnt.dict.dto.Page;
import com.lvmama.tnt.dict.entity.DictData;
import com.lvmama.tnt.dict.entity.DictType;
import com.lvmama.tnt.dict.service.IDictTypeService;
import com.lvmama.tnt.dict.util.JsonUtil;
import com.lvmama.tnt.dict.util.RenderUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


/**
 * 数据字典
 * 
 * @author chenwenshun
 * 
 */
@Controller
@RequestMapping("/dict")
public class DictTypeController extends MultiActionController {

	public static final String RESULT = "RESULT";
	@Autowired
	private IDictTypeService dictTypeService;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView model = new ModelAndView("/other/dict");
		return model;
	}

	@RequestMapping("save")
	public ModelAndView save(HttpServletRequest req, HttpServletResponse resp, DictTypeDto dto) {
		try {
			DictType type = dictTypeService.saveDict(dto);
			saveDatas(req, type.getId());
		} catch (Exception e) {
			log.error("dict save error!", e);
			RenderUtil.renderText("failure", resp);
			return null;
		}
		RenderUtil.renderText("success", resp);
		return null;
	}

	@RequestMapping("delete")
	public void delete(HttpServletRequest req, HttpServletResponse resp, DictTypeDto dto) {
		dictTypeService.deleteDict(dto.getId());
	}

	@RequestMapping("/deleteSub")
	public void deleteSub(HttpServletRequest req, HttpServletResponse resp) {
		Long dictDataId = Long.valueOf(req.getParameter("subId"));
		dictTypeService.deleteDictData(dictDataId);
	}

	private void saveDatas(HttpServletRequest request, Long dictTypeId) {
//		try {
			List<DictDataDto> lstDeleted = JsonUtil.getDeletedRecords(DictDataDto.class, request);
			List<DictDataDto> lstInsert = JsonUtil.getInsertRecords(DictDataDto.class, request);
			List<DictDataDto> lstUpdated = JsonUtil.getUpdatedRecords(DictDataDto.class, request);
			dictTypeService.saveDatas(lstDeleted, lstInsert, lstUpdated, dictTypeId);
//		} catch (Exception e) {
//			log.error("dictData save error!", e);
//		}
	}

	@RequestMapping("/detail")
	public ModelAndView detail(HttpServletRequest req, HttpServletResponse resp, DictTypeDto dto) throws Exception {

		ModelAndView model = new ModelAndView("/other/dict_detail");
		DictType dictType;
		if ( dto.getId() != null && dto.getId()!=0 ) {
			dictType = dictTypeService.loadDictById(dto.getId());
		}else {
			dictType = new DictType();
		}
		model.addObject(RESULT, dictType);
		return model;
	}

	@RequestMapping("/list")
	public void list(HttpServletRequest req, HttpServletResponse resp, DictTypeDto dto) {
		Page<DictType> page = new Page<DictType>(20);
		String sortField = req.getParameter("sort");
		String order = req.getParameter("order");
		String pageNo = req.getParameter("page");
		String rows = req.getParameter("rows");
		if (StringUtils.isNotBlank(pageNo)) {
			page.setPageNo(Integer.valueOf(pageNo));
		}
		if (StringUtils.isNotBlank(rows)) {
			page.setPageSize(Integer.valueOf(rows));
		}
		// 设置默认排序
		Map<String, String> mapOrder = new LinkedHashMap<String, String>();
		if (StringUtils.isNotBlank(sortField)) {
			mapOrder.put(sortField, order);
		}
		Map<String, Object> pram = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dto.getDictTypeName())) {
			pram.put("dictTypeName", "%" + dto.getDictTypeName() + "%");
		}
		if (StringUtils.isNotBlank(dto.getDictTypeCd())) {
			pram.put("dictTypeCd", "%" + dto.getDictTypeCd() + "%");
		}
		if ( dto.getEnableFlg() != null ) {
			pram.put("enableFlg", dto.getEnableFlg());
		}
		// 设置查询参数
		page = dictTypeService.findPageDict(page, pram, mapOrder);
		JsonUtil.renderJson(resp, page, new String[] { "dictDatas" });

	}

	@RequestMapping("/listData")
	public void listData(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String dictTypeId = req.getParameter("dictTypeId");
		List<DictData> appDictDatas = new ArrayList<DictData>();
		if (StringUtils.isNotBlank(dictTypeId)) {
			DictType dictType = dictTypeService.loadDictById(Long.valueOf(dictTypeId));
			if (dictType!=null){
				appDictDatas = dictType.getDictDatas();
			}
		}
		JsonUtil.renderListJson(resp, appDictDatas, new String[] { "dictType" });
	}

	@RequestMapping("isTypeExists")
	public void isTypeExists(HttpServletRequest req, HttpServletResponse resp) {
		String newDictTypeCd = req.getParameter("dictTypeCd").trim();
		String oldDictTypeCd = req.getParameter("oldDictTypeCd").trim();

		if (dictTypeService.existDictTypeCd(newDictTypeCd, oldDictTypeCd)) {
			RenderUtil.renderText("false", resp);
		} else {
			RenderUtil.renderText("true", resp);
		}
	}

	@RequestMapping("/isDataCdExists")
	public void isDataCdExists(HttpServletRequest req, HttpServletResponse resp) {
		String dictTypeId = req.getParameter("dictTypeId").trim();
		String dataCd = req.getParameter("dataCd").trim();
		String oldDataCd = req.getParameter("oldDataCd").trim();

		if (dictTypeService.existDataCd(Long.valueOf(dictTypeId), dataCd, oldDataCd)) {
			RenderUtil.renderText("true", resp);
		} else {
			RenderUtil.renderText("false", resp);
		}
	}

	public void setDictTypeService(IDictTypeService dictTypeService) {
		this.dictTypeService = dictTypeService;
	}
}
