package com.mercurio.lms.rest.contasareceber.lotecobranca.dto;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
 
public class HistoricoMensagemDTO extends BaseDTO { 
	
	private static final long serialVersionUID = 1L;
	private DomainValue tpMensagem;
	private DomainValue tpEnvio;
	private String para;
	private String dhInclusao;
	private String dhProcessamento;
	private String dhEnvio;
	private String dhRecebimento;
	private String dhDevolucao;
	private String dhErro;
	private Long idMonitoramentoMensagemEvento;
	private Long idMonitoramentoMensagemConteudo;
	
	public DomainValue getTpMensagem() {
		return tpMensagem;
	}
	public void setTpMensagem(DomainValue tpMensagem) {
		this.tpMensagem = tpMensagem;
	}
	public DomainValue getTpEnvio() {
		return tpEnvio;
	}
	public void setTpEnvio(DomainValue tpEnvio) {
		this.tpEnvio = tpEnvio;
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
	public String getDhInclusao() {
		return dhInclusao;
	}
	public void setDhInclusao(String dhInclusao) {
		this.dhInclusao = dhInclusao;
	}
	public String getDhProcessamento() {
		return dhProcessamento;
	}
	public void setDhProcessamento(String dhProcessamento) {
		this.dhProcessamento = dhProcessamento;
	}
	public String getDhEnvio() {
		return dhEnvio;
	}
	public void setDhEnvio(String dhEnvio) {
		this.dhEnvio = dhEnvio;
	}
	public String getDhRecebimento() {
		return dhRecebimento;
	}
	public void setDhRecebimento(String dhRecebimento) {
		this.dhRecebimento = dhRecebimento;
	}
	public String getDhDevolucao() {
		return dhDevolucao;
	}
	public void setDhDevolucao(String dhDevolucao) {
		this.dhDevolucao = dhDevolucao;
	}
	public String getDhErro() {
		return dhErro;
	}
	public void setDhErro(String dhErro) {
		this.dhErro = dhErro;
	}
	public Long getIdMonitoramentoMensagemEvento() {
		return idMonitoramentoMensagemEvento;
	}
	public void setIdMonitoramentoMensagemEvento(Long idMonitoramentoMensagemEvento) {
		this.idMonitoramentoMensagemEvento = idMonitoramentoMensagemEvento;
	}
	public Long getIdMonitoramentoMensagemConteudo() {
		return idMonitoramentoMensagemConteudo;
	}
	public void setIdMonitoramentoMensagemConteudo(
			Long idMonitoramentoMensagemConteudo) {
		this.idMonitoramentoMensagemConteudo = idMonitoramentoMensagemConteudo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
} 
