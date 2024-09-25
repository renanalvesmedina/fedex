package com.mercurio.lms.rest.portaria.chegadasaida.dto;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;

public class InformarChegadaSaidaDTO extends FrotaPlacaChegadaSaidaSuggestDTO implements Serializable {

	private boolean blInformaKmPortaria;
	private DateTime dhSaida;
	private DateTime dhChegada;
	private String tpSaida;
	private String tpChegada;
	private String dsRota;
	private String dsTipoMeioTransporte;
	private Long idControleTrecho;
	private Long idReboque;
	private List<LacreDTO> lacres;
	private String nmPessoa;
	private Long nrControleCarga;
	private String nrFrotaReboque;
	private String nrFrotaTransportado;
	private String nrIdentificacao;
	private String nrIdentificadorReboque;
	private String nrIdentificadorTransportado;
	private Long nrRota;
	private FrotaPlacaChegadaSaidaSuggestDTO resultSuggest;
	private String sgFilial;
	private String tpControleCarga;
	private DomainValue tpIdentificacao;
	private String versao;
	private Long idOrdemSaida;
	private Long idOrdemServico;
	private Long idMotorista;
	private Long idControleEntSaidaTerceiro;
	private boolean blVirouHodometro;
	private Integer nrQuilometragem;
	private String observacao;
	private String dsRotaControleCarga;
	private String nrRotaIdaVolta;
	private String sgFilialOrigem;

	public TypedFlatMap toMapSaida() {
		TypedFlatMap parametros = toMapDefaultAttributes();
		parametros.put("obSaida", getObservacao());
		return parametros;
	}

	public TypedFlatMap toMapChegada() {
		TypedFlatMap parametros = toMapDefaultAttributes();
		parametros.put("obChegada", getObservacao());
		return parametros;
	}
	
	private TypedFlatMap toMapDefaultAttributes() {
		TypedFlatMap parametros = new TypedFlatMap();
		
		parametros.put("blInformaKmPortaria", isBlInformaKmPortaria());
		parametros.put("dhSaida", getDhSaida());
		parametros.put("dhChegada", getDhChegada());
		parametros.put("tpSaida", getTpSaida());
		parametros.put("tpChegada", getTpChegada());
		parametros.put("dsRota", getDsRota());
		parametros.put("dsTipoMeioTransporte", getDsTipoMeioTransporte());
		parametros.put("idControleTrecho", getIdControleTrecho());
		parametros.put("idReboque", getIdReboque());
		parametros.put("lacres", getLacres());
		parametros.put("nmPessoa", getNmPessoa());
		parametros.put("nrControleCarga", getNrControleCarga());
		parametros.put("nrFrotaReboque", getNrFrotaReboque());
		parametros.put("nrFrotaTransportado", getNrFrotaTransportado());
		parametros.put("nrIdentificacao", getNrIdentificacao());
		parametros.put("nrIdentificadorReboque", getNrIdentificadorReboque());
		parametros.put("nrIdentificadorTransportado", getNrIdentificadorTransportado());
		parametros.put("nrRota", getNrRota());
		parametros.put("sgFilial", getSgFilial());
		parametros.put("tpControleCarga", getTpControleCarga());
		parametros.put("tpIdentificacao", getTpIdentificacao());
		parametros.put("versao", getVersao());
		parametros.put("idOrdemSaida", getIdOrdemSaida());
		parametros.put("idOrdemServico", getIdOrdemServico());
		parametros.put("idMotorista", getIdMotorista());
		parametros.put("idControleEntSaidaTerceiro", getIdControleEntSaidaTerceiro());
		parametros.put("blVirouHodometro", isBlVirouHodometro());
		parametros.put("nrQuilometragem", getNrQuilometragem());
		parametros.putAll(super.toMap());
		
		return new TypedFlatMap(parametros);
	}

	public boolean isBlInformaKmPortaria() {
		return blInformaKmPortaria;
	}

	public void setBlInformaKmPortaria(boolean blInformaKmPortaria) {
		this.blInformaKmPortaria = blInformaKmPortaria;
	}

	public String getDsRota() {
		return dsRota;
	}

	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}

	public String getDsTipoMeioTransporte() {
		return dsTipoMeioTransporte;
	}

	public void setDsTipoMeioTransporte(String dsTipoMeioTransporte) {
		this.dsTipoMeioTransporte = dsTipoMeioTransporte;
	}

	public Long getIdControleTrecho() {
		return idControleTrecho;
	}

	public void setIdControleTrecho(Long idControleTrecho) {
		this.idControleTrecho = idControleTrecho;
	}

	public Long getIdReboque() {
		return idReboque;
	}

	public void setIdReboque(Long idReboque) {
		this.idReboque = idReboque;
	}

	public List<LacreDTO> getLacres() {
		return lacres;
	}

	public void setLacres(List<LacreDTO> lacres) {
		this.lacres = lacres;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public Long getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public String getNrFrotaReboque() {
		return nrFrotaReboque;
	}

	public void setNrFrotaReboque(String nrFrotaReboque) {
		this.nrFrotaReboque = nrFrotaReboque;
	}

	public String getNrFrotaTransportado() {
		return nrFrotaTransportado;
	}

	public void setNrFrotaTransportado(String nrFrotaTransportado) {
		this.nrFrotaTransportado = nrFrotaTransportado;
	}

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public String getNrIdentificadorReboque() {
		return nrIdentificadorReboque;
	}

	public void setNrIdentificadorReboque(String nrIdentificadorReboque) {
		this.nrIdentificadorReboque = nrIdentificadorReboque;
	}

	public String getNrIdentificadorTransportado() {
		return nrIdentificadorTransportado;
	}

	public void setNrIdentificadorTransportado(String nrIdentificadorTransportado) {
		this.nrIdentificadorTransportado = nrIdentificadorTransportado;
	}

	public Long getNrRota() {
		return nrRota;
	}

	public void setNrRota(Long nrRota) {
		this.nrRota = nrRota;
	}

	public FrotaPlacaChegadaSaidaSuggestDTO getResultSuggest() {
		return resultSuggest;
	}

	public void setResultSuggest(FrotaPlacaChegadaSaidaSuggestDTO resultSuggest) {
		this.resultSuggest = resultSuggest;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public String getTpControleCarga() {
		return tpControleCarga;
	}

	public void setTpControleCarga(String tpControleCarga) {
		this.tpControleCarga = tpControleCarga;
	}

	public DomainValue getTpIdentificacao() {
		return tpIdentificacao;
	}

	public void setTpIdentificacao(DomainValue tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getTpSaida() {
		return tpSaida;
	}

	public void setTpSaida(String tpSaida) {
		this.tpSaida = tpSaida;
	}

	public String getTpChegada() {
		return tpChegada;
	}

	public void setTpChegada(String tpChegada) {
		this.tpChegada = tpChegada;
	}

	public Long getIdOrdemSaida() {
		return idOrdemSaida;
	}

	public void setIdOrdemSaida(Long idOrdemSaida) {
		this.idOrdemSaida = idOrdemSaida;
	}

	public Long getIdOrdemServico() {
		return idOrdemServico;
	}

	public void setIdOrdemServico(Long idOrdemServico) {
		this.idOrdemServico = idOrdemServico;
	}

	public Long getIdMotorista() {
		return idMotorista;
	}

	public void setIdMotorista(Long idMotorista) {
		this.idMotorista = idMotorista;
	}

	public Long getIdControleEntSaidaTerceiro() {
		return idControleEntSaidaTerceiro;
	}

	public void setIdControleEntSaidaTerceiro(Long idControleEntSaidaTerceiro) {
		this.idControleEntSaidaTerceiro = idControleEntSaidaTerceiro;
	}

	public boolean isBlVirouHodometro() {
		return blVirouHodometro;
	}

	public void setBlVirouHodometro(boolean blVirouHodometro) {
		this.blVirouHodometro = blVirouHodometro;
	}

	public Integer getNrQuilometragem() {
	    return nrQuilometragem;
	}

	public void setNrQuilometragem(Integer nrQuilometragem) {
	    this.nrQuilometragem = nrQuilometragem;
	}

	public DateTime getDhSaida() {
	    return dhSaida;
	}

	public void setDhSaida(DateTime dhSaida) {
	    this.dhSaida = dhSaida;
	}

	public DateTime getDhChegada() {
	    return dhChegada;
	}

	public void setDhChegada(DateTime dhChegada) {
	    this.dhChegada = dhChegada;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getDsRotaControleCarga() {
		return dsRotaControleCarga;
	}

	public void setDsRotaControleCarga(String dsRotaControleCarga) {
		this.dsRotaControleCarga = dsRotaControleCarga;
	}

	public String getNrRotaIdaVolta() {
		return nrRotaIdaVolta;
	}

	public void setNrRotaIdaVolta(String nrRotaIdaVolta) {
		this.nrRotaIdaVolta = nrRotaIdaVolta;
	}

	public String getSgFilialOrigem() {
		return sgFilialOrigem;
	}

	public void setSgFilialOrigem(String sgFilialOrigem) {
		this.sgFilialOrigem = sgFilialOrigem;
	}
}