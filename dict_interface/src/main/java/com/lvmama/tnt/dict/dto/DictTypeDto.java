/**
 * 
 */
package com.lvmama.tnt.dict.dto;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author jianhuang
 * 
 */
public class DictTypeDto implements Serializable{
	private static final long serialVersionUID = 2797285843251217167L;
	private Long id;
	private String dictTypeCd;
	private String dictTypeName;
	private BigDecimal dispOrderNo;

	private Boolean enableFlg;
	private String remark;


	public String getDictTypeCd() {
		return dictTypeCd;
	}

	public void setDictTypeCd(String dictTypeCd) {
//		if (StringUtils.isNotBlank(dictTypeCd)){
//			this.dictTypeCd = dictTypeCd.toUpperCase();
//		}
		this.dictTypeCd = dictTypeCd;
	}

	public String getDictTypeName() {
		return dictTypeName;
	}

	public void setDictTypeName(String dictTypeName) {
		this.dictTypeName = dictTypeName;
	}

	public BigDecimal getDispOrderNo() {
		return dispOrderNo;
	}

	public void setDispOrderNo(BigDecimal dispOrderNo) {
		this.dispOrderNo = dispOrderNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public Boolean getEnableFlg() {
	
		return enableFlg;
	}

	
	public void setEnableFlg(Boolean enableFlg) {
	
		this.enableFlg = enableFlg;
	}
}
