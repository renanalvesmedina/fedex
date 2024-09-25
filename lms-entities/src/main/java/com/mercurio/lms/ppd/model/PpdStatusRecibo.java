package com.mercurio.lms.ppd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "PPD_STATUS_RECIBOS")
@SequenceGenerator(name = "STATUS_RECIBOS_SEQ", sequenceName = "PPD_STATUS_RECIBOS_SQ")
public class PpdStatusRecibo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long idStatusRecibo;
	private DateTime dhStatusRecibo;	
	private DomainValue tpStatusRecibo;
	private String obStatusRecibo;	
	private UsuarioLMS usuario;
	private PpdRecibo recibo;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_RECIBOS_SEQ")
	@Column(name = "ID_STATUS_RECIBO", nullable = false)
	public Long getIdStatusRecibo() {
		return idStatusRecibo;
	}

	public void setIdStatusRecibo(Long idStatusRecibo) {
		this.idStatusRecibo = idStatusRecibo;
	}	

	@Columns(columns = { @Column(name = "DH_STATUS_RECIBO", nullable = false),
			@Column(name = "DH_STATUS_RECIBO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhStatusRecibo() {
		return dhStatusRecibo;
	}

	public void setDhStatusRecibo(DateTime dhStatusRecibo) {
		this.dhStatusRecibo = dhStatusRecibo;
	}

	@Column(name = "TP_STATUS_RECIBO", nullable = false)	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_PPD_STATUS_INDENIZACAO") })
	public DomainValue getTpStatusRecibo() {
		return tpStatusRecibo;
	}

	public void setTpStatusRecibo(DomainValue tpStatusRecibo) {
		this.tpStatusRecibo = tpStatusRecibo;
	}
	
	@Column(name = "OB_STATUS_RECIBO", length = 100)
	public String getObStatusRecibo() {
		return obStatusRecibo;
	}

	public void setObStatusRecibo(String obStatusRecibo) {
		this.obStatusRecibo = obStatusRecibo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RECIBO", nullable = false)
	public PpdRecibo getRecibo() {
		return recibo;
	}

	public void setRecibo(PpdRecibo recibo) {
		this.recibo = recibo;
	}
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idStatusRecibo", this.getIdStatusRecibo());
		bean.put("dhStatusRecibo", this.getDhStatusRecibo());
		bean.put("tpStatusRecibo", this.getTpStatusRecibo().getValue());
		bean.put("dsTpStatusRecibo", this.getTpStatusRecibo().getDescription());
		bean.put("obStatusRecibo", this.getObStatusRecibo());
		
		if(usuario != null) {
			bean.put("idUsuario", this.getUsuario().getIdUsuario());			
			bean.put("nmUsuario", this.getUsuario().getUsuarioADSM()
					.getNmUsuario());
		}
		
		return bean;
	}
}
