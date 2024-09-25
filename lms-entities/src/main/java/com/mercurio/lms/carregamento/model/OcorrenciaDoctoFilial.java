package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;

public class OcorrenciaDoctoFilial implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idOcorrenciaDoctoFilial;
	
	private LocalizacaoMercadoria localizacaoMercadoria;
	
	private Filial filialDoctoDestino;
	
	private Filial filialDoctoLocalizacao;
	
	private Filial filialOcorrencia;
	
	private Filial filialDoctoOrigem;
	
	private DoctoServico doctoServico;
	
	private UsuarioLMS usuarioOcorrencia;
	
	private DateTime dhOcorrencia;
	
	private String obOcorrencia;
	
	public OcorrenciaDoctoFilial(){
		
	}

	public Long getIdOcorrenciaDoctoFilial() {
		return idOcorrenciaDoctoFilial;
	}

	public void setIdOcorrenciaDoctoFilial(Long idOcorrenciaDoctoFilial) {
		this.idOcorrenciaDoctoFilial = idOcorrenciaDoctoFilial;
	}

	public LocalizacaoMercadoria getLocalizacaoMercadoria() {
		return localizacaoMercadoria;
	}

	public void setLocalizacaoMercadoria(LocalizacaoMercadoria localizacaoMercadoria) {
		this.localizacaoMercadoria = localizacaoMercadoria;
	}

	public Filial getFilialDoctoDestino() {
		return filialDoctoDestino;
	}

	public void setFilialDoctoDestino(Filial filialDoctoDestino) {
		this.filialDoctoDestino = filialDoctoDestino;
	}

	public Filial getFilialDoctoLocalizacao() {
		return filialDoctoLocalizacao;
	}

	public void setFilialDoctoLocalizacao(Filial filialDoctoLocalizacao) {
		this.filialDoctoLocalizacao = filialDoctoLocalizacao;
	}

	public Filial getFilialOcorrencia() {
		return filialOcorrencia;
	}

	public void setFilialOcorrencia(Filial filialOcorrencia) {
		this.filialOcorrencia = filialOcorrencia;
	}

	public Filial getFilialDoctoOrigem() {
		return filialDoctoOrigem;
	}

	public void setFilialDoctoOrigem(Filial filialDoctoOrigem) {
		this.filialDoctoOrigem = filialDoctoOrigem;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public UsuarioLMS getUsuarioOcorrencia() {
		return usuarioOcorrencia;
	}

	public void setUsuarioOcorrencia(UsuarioLMS usuarioOcorrencia) {
		this.usuarioOcorrencia = usuarioOcorrencia;
	}

	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

	public String getObOcorrencia() {
		return obOcorrencia;
	}

	public void setObOcorrencia(String obOcorrencia) {
		this.obOcorrencia = obOcorrencia;
	}
}
