package org.rebate.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.rebate.entity.base.BaseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 每日地区消费/交易额统计
 * 
 *
 */
@Entity
@Table(name = "rm_area_consume_report", 
	indexes = {
	@Index(name = "reportDateIndex", columnList = "reportDate"),
    @Index(name = "areaIndex", columnList = "area"),
    @Index(name = "sellerDiscountIndex", columnList = "sellerDiscount")
	})
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "rm_area_consume_report_sequence")
public class AreaConsumeReport extends BaseEntity {


	private static final long serialVersionUID = 1L;
  
  	/** 地区 区县 */
  	private Area area;
  	/** 地区 市 */
  	private Area city;
  	/** 地区 省 */
  	private Area province;
  	/** 该折扣总消费金额 */
  	private BigDecimal totalAmount; 
  	/** 消费折扣 */
  	private BigDecimal sellerDiscount;
  	/** 统计日期 */
  	private Date reportDate;
//    /** 代理级别 */
//    private AgencyLevel agencyLevel;
  	
  	@ManyToOne(fetch = FetchType.LAZY)
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	@Column(scale = 4, precision = 12)
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	@JsonProperty
	@Column(scale = 1)
	public BigDecimal getSellerDiscount() {
		return sellerDiscount;
	}
	public void setSellerDiscount(BigDecimal sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}
	@JsonProperty
	@Temporal(TemporalType.DATE)
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getCity() {
		return city;
	}
	public void setCity(Area city) {
		this.city = city;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	public Area getProvince() {
		return province;
	}
	public void setProvince(Area province) {
		this.province = province;
	}

  
}
