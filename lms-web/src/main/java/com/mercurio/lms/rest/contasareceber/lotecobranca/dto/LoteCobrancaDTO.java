package com.mercurio.lms.rest.contasareceber.lotecobranca.dto;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contasreceber.model.LoteCobrancaTerceira;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.util.session.SessionUtils;
 
public class LoteCobrancaDTO extends BaseDTO { 
	
	private static final long serialVersionUID = 1L;
	private Long idLoteCobranca;
	private String nrLote;
	private String descricao;
	private DomainValue tpLote;
	private DateTime dtEnvio;
	private DateTime dtAlteracao;
	private String dtEnvioFormatada;
	private String dtAlteracaoFormatada;
	private UsuarioDTO usuario;
	
	public Long getIdLoteCobranca() {
		return idLoteCobranca;
	}
	public void setIdLoteCobranca(Long idLoteCobranca) {
		this.idLoteCobranca = idLoteCobranca;
	}
	public String getNrLote() {
		return nrLote;
	}
	public void setNrLote(String nrLote) {
		this.nrLote = nrLote;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public DomainValue getTpLote() {
		return tpLote;
	}
	public void setTpLote(DomainValue tpLote) {
		this.tpLote = tpLote;
	}
	public DateTime getDtEnvio() {
		return dtEnvio;
	}
	public void setDtEnvio(DateTime dtEnvio) {
		this.dtEnvio = dtEnvio;
	}
	public DateTime getDtAlteracao() {
		return dtAlteracao;
	}
	public void setDtAlteracao(DateTime dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	public String getDtEnvioFormatada() {
		return dtEnvioFormatada;
	}
	public void setDtEnvioFormatada(String dtEnvioFormatada) {
		this.dtEnvioFormatada = dtEnvioFormatada;
	}
	public String getDtAlteracaoFormatada() {
		return dtAlteracaoFormatada;
	}
	public void setDtAlteracaoFormatada(String dtAlteracaoFormatada) {
		this.dtAlteracaoFormatada = dtAlteracaoFormatada;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public LoteCobrancaTerceira build(LoteCobrancaTerceira current){
		LoteCobrancaTerceira loteCobrancaTerceira = current;
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		loteCobrancaTerceira.setUsuario(usuario);
		loteCobrancaTerceira.setDsLote(getDescricao());
		loteCobrancaTerceira.setTpLote(getTpLote());
		
		return loteCobrancaTerceira;
	}
	
} 
