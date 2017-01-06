package com.lvmama.tnt.dict.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 数据字典
 * 
 *
 */
@Entity
@Table(name = "T_DICT_DATA")
public class DictData implements java.io.Serializable {
	private static final long serialVersionUID = -310589120875675917L;
	private long id;
	private DictType dictType;
	private String dictCd;
	private String dictName;
	private BigDecimal dispOrderNo;
	private String remark;
	private String i18n;
	private Date createdDate;
	private Date updatedDate;
	/** 是否有效 */
	private Boolean enableFlg = true;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dict_type_id")
	public DictType getDictType() {
		return this.dictType;
	}

	public void setDictType(DictType dictType) {
		this.dictType = dictType;
	}

	@Column(name = "DICT_CD",columnDefinition="",length = 50)
	public String getDictCd() {
		return this.dictCd;
	}

	public void setDictCd(String dictCd) {
		this.dictCd = dictCd;
	}

	@Column(name = "DICT_NAME", length = 16776000)
	public String getDictName() {
		return this.dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	@Column(name = "DISP_ORDER_NO", precision = 38, scale = 0)
	public BigDecimal getDispOrderNo() {
		return this.dispOrderNo;
	}

	public void setDispOrderNo(BigDecimal dispOrderNo) {
		this.dispOrderNo = dispOrderNo;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", length = 7, updatable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", length = 7)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Id
	@GeneratedValue(generator = "DictData")
	@GenericGenerator(name = "DictData", strategy = "native")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "I18N", length = 200)
	public String getI18n() {
		return i18n;
	}

	public void setI18n(String i18n) {
		this.i18n = i18n;
	}
	@Column(precision = 1, scale = 0, name = "ENABLE_FLG")
	public Boolean getEnableFlg() {

		return enableFlg;
	}

	public void setEnableFlg(Boolean enableFlg) {

		this.enableFlg = enableFlg;
	}
}
