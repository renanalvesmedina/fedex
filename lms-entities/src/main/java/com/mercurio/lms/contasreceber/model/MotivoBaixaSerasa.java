package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;


/**
 * The persistent class for the LOTE_SERASA database table.
 * 
 */
@Entity
@Table(name="MOTIVO_BAIXA_SERASA")
public class MotivoBaixaSerasa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_MOTIVO_BAIXA_SERASA")
    @SequenceGenerator(name = "MOTIVO_BAIXA_SERASA_SQ", sequenceName = "MOTIVO_BAIXA_SERASA_SQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOTIVO_BAIXA_SERASA_SQ")
	private Long idMotivoBaixaSerasa;
	
	@Column(name="CD_MOTIVO_BAIXA_SERASA")
	private String cdMotivoBaixaSerasa;
	
	@Column(name="DS_MOTIVO_BAIXA_SERASA")
	private String dsMotivoBaixaSerasa;

	@Column(name="OB_MOTIVO_BAIXA_SERASA")
	private String obMotivoBaixaSerasa;
	
	public MotivoBaixaSerasa() {
	}

	public Long getIdMotivoBaixaSerasa() {
		return idMotivoBaixaSerasa;
	}

	public void setIdMotivoBaixaSerasa(Long idMotivoBaixaSerasa) {
		this.idMotivoBaixaSerasa = idMotivoBaixaSerasa;
	}

	public String getCdMotivoBaixaSerasa() {
		return cdMotivoBaixaSerasa;
	}

	public void setCdMotivoBaixaSerasa(String cdMotivoBaixaSerasa) {
		this.cdMotivoBaixaSerasa = cdMotivoBaixaSerasa;
	}

	public String getDsMotivoBaixaSerasa() {
		return dsMotivoBaixaSerasa;
	}

	public void setDsMotivoBaixaSerasa(String dsMotivoBaixaSerasa) {
		this.dsMotivoBaixaSerasa = dsMotivoBaixaSerasa;
	}

	public String getObMotivoBaixaSerasa() {
		return obMotivoBaixaSerasa;
	}

	public void setObMotivoBaixaSerasa(String obMotivoBaixaSerasa) {
		this.obMotivoBaixaSerasa = obMotivoBaixaSerasa;
	}
	
	public String getDsCompleto(){
		return cdMotivoBaixaSerasa +" - "+dsMotivoBaixaSerasa;
	}

}