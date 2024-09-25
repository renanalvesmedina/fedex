package com.mercurio.lms.rest.fretecarreteiroviagem.manterrecibosdeoutrasempresas;

import java.math.BigDecimal;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class RecibosDeOutrasEmpresasDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private ProprietarioDTO proprietario;
	
	private FilialSuggestDTO filial;
	
	private String nrRecibo;
	
	private BigDecimal vlInss;
	
	private BigDecimal vlRemuneracao;
	
	private String dsEmpregador;
	
	private Map<String, Object> tpIdentificacaoEmpregador;
	
	private String nrIdentificacaoEmpregador;
	
	private DateTime dhInclusao;
	
	private UsuarioDTO usuario;
	
	private YearMonthDay dtEmissaoRecibo;

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}
	
	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public String getNrRecibo() {
		return nrRecibo;
	}

	public void setNrRecibo(String nrRecibo) {
		this.nrRecibo = nrRecibo;
	}

	public BigDecimal getVlInss() {
		return vlInss;
	}

	public void setVlInss(BigDecimal vlInss) {
		this.vlInss = vlInss;
	}

	public BigDecimal getVlRemuneracao() {
		return vlRemuneracao;
	}

	public void setVlRemuneracao(BigDecimal vlRemuneracao) {
		this.vlRemuneracao = vlRemuneracao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dateTime) {
		this.dhInclusao = dateTime;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public YearMonthDay getDtEmissaoRecibo() {
		return dtEmissaoRecibo;
	}

	public void setDtEmissaoRecibo(YearMonthDay yearMonthDay) {
		this.dtEmissaoRecibo = yearMonthDay;
	}

	public String getDsEmpregador() {
		return dsEmpregador;
	}

	public void setDsEmpregador(String dsEmpregador) {
		this.dsEmpregador = dsEmpregador;
	}

	public Map<String, Object> getTpIdentificacaoEmpregador() {
		return tpIdentificacaoEmpregador;
	}

	public void setTpIdentificacaoEmpregador(
			Map<String, Object> tpIdentificacaoEmpregador) {
		this.tpIdentificacaoEmpregador = tpIdentificacaoEmpregador;
	}

	public String getNrIdentificacaoEmpregador() {
		return nrIdentificacaoEmpregador;
	}

	public void setNrIdentificacaoEmpregador(String nrIdentificacaoEmpregador) {
		this.nrIdentificacaoEmpregador = nrIdentificacaoEmpregador;
	}
}
