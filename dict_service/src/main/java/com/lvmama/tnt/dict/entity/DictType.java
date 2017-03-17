package com.lvmama.tnt.dict.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

/**
 * 数据字典
 * 
 *
 */
@Entity
@Table(name = "T_DICT_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DictType implements java.io.Serializable {

	private static final long serialVersionUID = -1310300433105549587L;
	private long id;
	private String dictTypeCd;
	private String dictTypeName;
	private BigDecimal dispOrderNo;
	private String remark;
	private Date createdDate;
	private Date updatedDate;
	private List<DictData> dictDatas = new ArrayList<DictData>();
	/** 是否有效 */
	private Boolean enableFlg = true;
	@Column(name = "DICT_TYPE_CD", length = 20)
	public String getDictTypeCd() {
		return this.dictTypeCd;
	}

	public void setDictTypeCd(String dictTypeCd) {
		this.dictTypeCd = dictTypeCd;
	}

	@Column(name = "DICT_TYPE_NAME", length = 50)
	public String getDictTypeName() {
		return this.dictTypeName;
	}

	public void setDictTypeName(String dictTypeName) {
		this.dictTypeName = dictTypeName;
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

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "dictType")
	@BatchSize(size = 10)
	@OrderBy(clause = "disp_Order_No asc,dict_cd asc ")
	public List<DictData> getDictDatas() {
		return this.dictDatas;
	}

	public void setDictDatas(List<DictData> dictDatas) {
		this.dictDatas = dictDatas;
	}

	@Id
	@GeneratedValue(generator = "DictType")
	@GenericGenerator(name = "DictType", strategy = "native")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Column(precision = 1, scale = 0, name = "ENABLE_FLG")
	public Boolean getEnableFlg() {

		return enableFlg;
	}

	public void setEnableFlg(Boolean enableFlg) {

		this.enableFlg = enableFlg;
	}

}
