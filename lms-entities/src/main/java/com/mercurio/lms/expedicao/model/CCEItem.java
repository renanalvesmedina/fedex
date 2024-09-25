package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "CONTROLE_CONF_ELET_ITEM")
@SequenceGenerator(name = "controle_conf_elet_item_sq", sequenceName = "controle_conf_elet_item_sq", allocationSize = 1)
public class CCEItem implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_CONTROLE_CONF_ELET_ITEM", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "controle_conf_elet_item_sq")
	private Long idControleConfEletItem;
	
	@Column(name="NR_CHAVE")
	private String nrChave;
	
	@Column(name="NR_CAE")
	private String nrCae;
	
	@Column(name="NR_UNITIZACAO")
	private String nrUnitizacao;
	
	@Column(name="BL_PALETIZADO", nullable = true)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")	
	private Boolean blPaletizado; 
	
	@Columns(columns = { @Column(name = "DH_EMISSAO", nullable = false),
		@Column(name = "DH_EMISSAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEmissao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CONTROLE_CONF_ELET", nullable = false)
	private CCE cce;

	public Long getIdControleConfEletItem() {
		return idControleConfEletItem;
	}

	public void setIdControleConfEletItem(Long idControleConfEletItem) {
		this.idControleConfEletItem = idControleConfEletItem;
	}

	public String getNrCae() {
		return nrCae;
	}

	public void setNrCae(String nrCae) {
		this.nrCae = nrCae;
	}
	
	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public DateTime getDhEmisao() {
		return dhEmissao;
	}

	public void setDhEmisao(DateTime dhEmisao) {
		this.dhEmissao = dhEmisao;
	}

	public CCE getCce() {
		return cce;
	}

	public void setCce(CCE cce) {
		this.cce = cce;
	}	
	
	public String getNrUnitizacao() {
		return nrUnitizacao;
	}

	public void setNrUnitizacao(String nrUnitizacao) {
		this.nrUnitizacao = nrUnitizacao;
	}

	public Boolean getBlPaletizado() {
		return blPaletizado;
	}

	public void setBlPaletizado(Boolean blPaletizado) {
		this.blPaletizado = blPaletizado;
	}	
		
}
