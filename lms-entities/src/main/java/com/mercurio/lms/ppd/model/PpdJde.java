package com.mercurio.lms.ppd.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "F557657I")
@IdClass(PpdJdePK.class) 
public class PpdJde implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private String bimid;
	private Long bilnid;
	private Long bian8;
	private String bitytn;
	private String bidrin;
	private String biapta;
	private String biedsp;
	private String bipid;
	private String biuser;
	private String bijobn;
	private Long biupmj;
	private Long biupmt;	
						
	@Id
	@Column(name="BIMID", nullable=false, length=10)
	public String getBimid() {
		return bimid;
	}

	public void setBimid(String bimid) {
		this.bimid = bimid;
	}

	@Id
	@Column(name="BILNID", nullable=false)
	public Long getBilnid() {
		return bilnid;
	}

	public void setBilnid(Long bilnid) {
		this.bilnid = bilnid;
	}

	@Column(name="BIAN8")
	public Long getBian8() {
		return bian8;
	}

	public void setBian8(Long bian8) {
		this.bian8 = bian8;
	}

	@Column(name="BITYTN", length=8)
	public String getBitytn() {
		return bitytn;
	}

	public void setBitytn(String bitytn) {
		this.bitytn = bitytn;
	}

	@Column(name="BIDRIN", length=1)
	public String getBidrin() {
		return bidrin;
	}

	public void setBidrin(String bidrin) {
		this.bidrin = bidrin;
	}		

	@Column(name="BIAPTA", length=1500)
	public String getBiapta() {
		return biapta;
	}

	public void setBiapta(String biapta) {
		this.biapta = biapta;
	}

	@Column(name="BIEDSP", length=1)
	public String getBiedsp() {
		return biedsp;
	}

	public void setBiedsp(String biedsp) {
		this.biedsp = biedsp;
	}			

	@Column(name="BIPID",length=10)
	public String getBipid() {
		return bipid;
	}

	public void setBipid(String bipid) {
		this.bipid = bipid;
	}

	@Column(name="BIUSER",length=10)
	public String getBiuser() {
		return biuser;
	}

	public void setBiuser(String biuser) {
		this.biuser = biuser;
	}			

	@Column(name="BIJOBN", length=10)
	public String getBijobn() {
		return bijobn;
	}

	public void setBijobn(String bijobn) {
		this.bijobn = bijobn;
	}

	@Column(name="BIUPMJ", length=6)
	public Long getBiupmj() {
		return biupmj;
	}

	public void setBiupmj(Long biupmj) {
		this.biupmj = biupmj;
	}

	@Column(name="BIUPMT")
	public Long getBiupmt() {
		return biupmt;
	}

	public void setBiupmt(Long biupmt) {
		this.biupmt = biupmt;
	}
}
