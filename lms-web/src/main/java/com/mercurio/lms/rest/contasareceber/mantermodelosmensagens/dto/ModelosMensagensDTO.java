package com.mercurio.lms.rest.contasareceber.mantermodelosmensagens.dto;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.contasreceber.model.DadoModeloMensagem;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.util.JTFormatUtils;

public class ModelosMensagensDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	private Long idModeloMensagem;
	private UsuarioDTO usuario;
	private String dsModeloMensagem;
	private DomainValue tpModeloMensagem;
	private String dtVigenciaInicial;
	private String dtVigenciaFinal;

	private YearMonthDay dtVigenciaInicialY;
	private YearMonthDay dtVigenciaFinalY;

	private String dcModeloAssunto;
	private String dcModeloCorpo;
	private DateTime dhAlteracao;
	private List<DadoModeloMensagemDTO> dadosModeloMensagem;

	public Long getIdModeloMensagem() {
		return idModeloMensagem;
	}

	public void setIdModeloMensagem(Long idModeloMensagem) {
		this.idModeloMensagem = idModeloMensagem;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public String getDsModeloMensagem() {
		return dsModeloMensagem;
	}

	public void setDsModeloMensagem(String dsModeloMensagem) {
		this.dsModeloMensagem = dsModeloMensagem;
	}

	public DomainValue getTpModeloMensagem() {
		return tpModeloMensagem;
	}

	public void setTpModeloMensagem(DomainValue tpModeloMensagem) {
		this.tpModeloMensagem = tpModeloMensagem;
	}

	public String getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(String dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public String getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(String dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public String getDcModeloAssunto() {
		return dcModeloAssunto;
	}

	public void setDcModeloAssunto(String dcModeloAssunto) {
		this.dcModeloAssunto = dcModeloAssunto;
	}

	public String getDcModeloCorpo() {
		return dcModeloCorpo;
	}

	public void setDcModeloCorpo(String dcModeloCorpo) {
		this.dcModeloCorpo = dcModeloCorpo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public void setDadosModeloMensagem(
			List<DadoModeloMensagemDTO> dadosModeloMensagem) {
		this.dadosModeloMensagem = dadosModeloMensagem;
	}

	public List<DadoModeloMensagemDTO> getDadosModeloMensagem() {
		return dadosModeloMensagem;
	}

	public void setDtVigenciaFinalY(YearMonthDay dtVigenciaFinalY) {
		this.dtVigenciaFinalY = dtVigenciaFinalY;
	}

	public void setDtVigenciaInicialY(YearMonthDay dtVigenciaInicialY) {
		this.dtVigenciaInicialY = dtVigenciaInicialY;
	}

	public YearMonthDay getDtVigenciaFinalY() {
		return dtVigenciaFinalY;
	}

	public YearMonthDay getDtVigenciaInicialY() {
		return dtVigenciaInicialY;
	}

	public ModeloMensagem build() {
		ModeloMensagem modeloMensagem = new ModeloMensagem();
		modeloMensagem.setIdModeloMensagem(getIdModeloMensagem());
		modeloMensagem.setDcModeloAssunto(getDcModeloAssunto());
		modeloMensagem.setDcModeloCorpo(getDcModeloCorpo());
		modeloMensagem.setDhAlteracao(getDhAlteracao());
		modeloMensagem.setDsModeloMensagem(getDsModeloMensagem());
		if (getDtVigenciaFinalY() != null) {
			modeloMensagem.setDtVigenciaFinal(getDtVigenciaFinalY());
		}
		if (getDtVigenciaInicialY() != null) {
			modeloMensagem.setDtVigenciaInicial(getDtVigenciaInicialY());
		}
		modeloMensagem.setTpModeloMensagem(getTpModeloMensagem());
		modeloMensagem.setUsuario(getUsuario().build());
		return modeloMensagem;
	}
}
