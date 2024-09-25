package com.mercurio.lms.carregamento.model;

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

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

/**
 * Entidade para atender a demanda LMS-2788
 * 
 * @author pfernandes@voiza.com.br
 * 
 */
@Entity
@Table(name = "HISTORICO_MPC")
@SequenceGenerator(name = "HISTORICO_MPC_SQ", sequenceName = "HISTORICO_MPC_SQ")
public class HistoricoMpc implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORICO_MPC_SQ")
	@Column(name = "ID_HISTORICO_MPC", nullable = false)
	private Long idHistoricoMPC;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CABECALHO_CARREGAMENTO", nullable = true)
	private CabecalhoCarregamento cabecalhoCarregamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CARREGAMENTO", nullable = false)
	private Carregamento carregamento;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_REJEITO_MPC", nullable = true)
	private RejeitoMpc rejeitoMPC;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_LIBERA_MPC", nullable = true)
	private LiberaMpc liberaMPC;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RESPONSAVEL", nullable = true)
	private UsuarioLMS responsavel;
	
	@Columns(columns = { @Column(name = "DH_HISTORICO_MPC", nullable = true),
			@Column(name = "DH_HISTORICO_MPC_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhHistoricoMPC;

	public Long getIdHistoricoMPC() {
		return idHistoricoMPC;
	}

	public void setIdHistoricoMPC(Long idHistoricoMPC) {
		this.idHistoricoMPC = idHistoricoMPC;
	}

	public CabecalhoCarregamento getCabecalhoCarregamento() {
		return cabecalhoCarregamento;
	}

	public void setCabecalhoCarregamento(CabecalhoCarregamento cabecalhoCarregamento) {
		this.cabecalhoCarregamento = cabecalhoCarregamento;
	}

	public Carregamento getCarregamento() {
		return carregamento;
	}

	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}

	public RejeitoMpc getRejeitoMPC() {
		return rejeitoMPC;
	}

	public void setRejeitoMPC(RejeitoMpc rejeitoMPC) {
		this.rejeitoMPC = rejeitoMPC;
	}

	public UsuarioLMS getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(UsuarioLMS responsavel) {
		this.responsavel = responsavel;
	}

	public DateTime getDhHistoricoMPC() {
		return dhHistoricoMPC;
	}

	public void setDhHistoricoMPC(DateTime dhHistoricoMPC) {
		this.dhHistoricoMPC = dhHistoricoMPC;
	}

	public LiberaMpc getLiberaMPC() {
		return liberaMPC;
	}

	public void setLiberaMPC(LiberaMpc liberaMPC) {
		this.liberaMPC = liberaMPC;
	}	
}

