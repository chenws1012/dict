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
public class DictDataDto implements Serializable{

	private static final long serialVersionUID = -6379623981425035725L;
	private Long id;
	private Long dictTypeId;
	private String dictCd;
	private String dictName;
	private BigDecimal dispOrderNo;
	private String remark;
	private String i18n;
	private Boolean enableFlg = true;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDictTypeId() {
		return dictTypeId;
	}
	public void setDictTypeId(Long dictTypeId) {
		this.dictTypeId = dictTypeId;
	}
	public String getDictCd() {
		return dictCd;
	}
	public void setDictCd(String dictCd) {
		this.dictCd = dictCd;
	}
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
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
	public String getI18n() {
		return i18n;
	}
	public void setI18n(String i18n) {
		this.i18n = i18n;
	}
	
	public Boolean getEnableFlg() {
	
		return enableFlg;
	}
	
	public void setEnableFlg(Boolean enableFlg) {
	
		this.enableFlg = enableFlg;
	}
}
