package com.mercurio.lms.fretecarreteirocoletaentrega.model;

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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "EVENTO_NOTA_CREDITO")
@SequenceGenerator(name = "EVENTO_NOTA_CREDITO_SQ", sequenceName = "EVENTO_NOTA_CREDITO_SQ", allocationSize=1)
public class EventoNotaCredito implements Serializable {
	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENTO_NOTA_CREDITO_SQ")
	@Column(name = "ID_EVENTO_NOTA_CREDITO", nullable = false)
	private Long idEventoNotaCredito;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NOTA_CREDITO", nullable = true)	
	private NotaCredito notaCredito;
	
	
	@Columns(columns = { @Column(name = "DH_EVENTO", nullable = false), @Column(name = "DH_EVENTO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEvento;
	
	
	@Column(name = "TP_ORIGEM_EVENTO", nullable = false, length = 1)	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		parameters = { @Parameter(name = "domainName", value = "DM_TIPO_ORIGEM_EVENTO_NOTA_CREDITO") })
	private DomainValue tpOrigemEvento;
	
	
	@Column(name = "TP_COMPLEMENTO_EVENTO", nullable = false, length = 1)	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		parameters = { @Parameter(name = "domainName", value = "DM_TIPO_COMPLEMENTO_EVENTO_NOTA_CREDITO") })
	private DomainValue tpComplementoEvento;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;
	
	
	public Long getIdEventoNotaCredito() {
		return idEventoNotaCredito;
	}
	
	public void setIdEventoNotaCredito(Long idEventoNotaCredito) {
		this.idEventoNotaCredito = idEventoNotaCredito;
	}
	
	public NotaCredito getNotaCredito() {
		return notaCredito;
	}
	
	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}
	
	public DateTime getDhEvento() {
		return dhEvento;
	}
	
	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}
	
	public DomainValue getTpOrigemEvento() {
		return tpOrigemEvento;
	}
	
	public void setTpOrigemEvento(DomainValue tpOrigemEvento) {
		this.tpOrigemEvento = tpOrigemEvento;
	}
	
	public DomainValue getTpComplementoEvento() {
		return tpComplementoEvento;
	}
	
	public void setTpComplementoEvento(DomainValue tpComplementoEvento) {
		this.tpComplementoEvento = tpComplementoEvento;
	}
	
	public UsuarioLMS getUsuario() {
		return usuario;
	}
	
	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
}
