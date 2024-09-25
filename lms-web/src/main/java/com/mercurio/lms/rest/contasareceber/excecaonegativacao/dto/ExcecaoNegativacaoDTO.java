package com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.contasreceber.model.ExcecaoNegativacaoSerasa;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.util.session.SessionUtils;
 
public class ExcecaoNegativacaoDTO extends BaseDTO { 

	private static final long serialVersionUID = 1L;
	private FaturaDTO fatura;
	private FilialSuggestDTO filial;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private String dtVigenciaInicialFormatada;
	private String dtVigenciaFinalFormatada;
	private String obExcecaoNegativacaoSerasa;
	private UsuarioDTO usuario;
	private DateTime dataAlteracao;
	private String dataAlteracaoFormatada;
	
	public FaturaDTO getFatura() {
		return fatura;
	}
	public void setFatura(FaturaDTO fatura) {
		this.fatura = fatura;
	}
	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	public String getObExcecaoNegativacaoSerasa() {
		return obExcecaoNegativacaoSerasa;
	}
	public void setObExcecaoNegativacaoSerasa(String obExcecaoNegativacaoSerasa) {
		this.obExcecaoNegativacaoSerasa = obExcecaoNegativacaoSerasa;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public DateTime getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(DateTime dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public String getDataAlteracaoFormatada() {
		return dataAlteracaoFormatada;
	}
	public void setDataAlteracaoFormatada(String dataAlteracaoFormatada) {
		this.dataAlteracaoFormatada = dataAlteracaoFormatada;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDtVigenciaInicialFormatada() {
		return dtVigenciaInicialFormatada;
	}
	public void setDtVigenciaInicialFormatada(String dtVigenciaInicialFormatada) {
		this.dtVigenciaInicialFormatada = dtVigenciaInicialFormatada;
	}
	public String getDtVigenciaFinalFormatada() {
		return dtVigenciaFinalFormatada;
	}
	public void setDtVigenciaFinalFormatada(String dtVigenciaFinalFormatada) {
		this.dtVigenciaFinalFormatada = dtVigenciaFinalFormatada;
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	
	public ExcecaoNegativacaoSerasa build(ExcecaoNegativacaoSerasa current){
		ExcecaoNegativacaoSerasa excecaoNegativacaoSerasa = current;
		excecaoNegativacaoSerasa.setUsuario(SessionUtils.getUsuarioLogado());
		excecaoNegativacaoSerasa.setDtVigenciaInicial(getDtVigenciaInicial());
		excecaoNegativacaoSerasa.setDtVigenciaFinal(getDtVigenciaFinal());
		excecaoNegativacaoSerasa.setObExcecaoNegativacaoSerasa(getObExcecaoNegativacaoSerasa());
		
		return excecaoNegativacaoSerasa;
	}

} 
