package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.municipios.dto.MunicipioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.TipoMeioTransporteDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.PaisDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class TabelaFreteCarreteiroCeFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private Long idTabelaFreteCarreteiroCe;
	private Long nrTabelaFreteCarreteiroCe;

	private DomainValue tpVinculo;
	private DomainValue tpOperacao;
	private DomainValue tpVigente;

	private DateTime dhVigenciaInicial;
	private DateTime dhVigenciaFinal;
	private YearMonthDay dtAtualizacaoInicial;
	private YearMonthDay dtAtualizacaoFinal;

	private FilialSuggestDTO filial;
	private FilialSuggestDTO filialRotaColetaEntrega;
	private ClienteSuggestDTO cliente;
	private ProprietarioDTO proprietario;
	private RotaColetaEntregaSuggestDTO rotaColetaEntrega;
	private MeioTransporteSuggestDTO meioTransporte;
	private TipoMeioTransporteDTO tipoMeioTransporte;
	private PaisDTO pais;
	private UnidadeFederativaDTO unidadeFederativa;
	private MunicipioDTO municipio;

	private DomainValue blTipo;

	public Long getIdTabelaFreteCarreteiroCe() {
		return idTabelaFreteCarreteiroCe;
	}

	public void setIdTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe) {
		this.idTabelaFreteCarreteiroCe = idTabelaFreteCarreteiroCe;
	}

	public Long getNrTabelaFreteCarreteiroCe() {
		return nrTabelaFreteCarreteiroCe;
	}

	public void setNrTabelaFreteCarreteiroCe(Long nrTabelaFreteCarreteiroCe) {
		this.nrTabelaFreteCarreteiroCe = nrTabelaFreteCarreteiroCe;
	}

	public DomainValue getTpVinculo() {
		return tpVinculo;
	}

	public void setTpVinculo(DomainValue tpVinculo) {
		this.tpVinculo = tpVinculo;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public DomainValue getTpVigente() {
		return tpVigente;
	}

	public void setTpVigente(DomainValue tpVigente) {
		this.tpVigente = tpVigente;
	}

	public YearMonthDay getDtAtualizacaoInicial() {
		return dtAtualizacaoInicial;
	}

	public void setDtAtualizacaoInicial(YearMonthDay dtAtualizacaoInicial) {
		this.dtAtualizacaoInicial = dtAtualizacaoInicial;
	}

	public YearMonthDay getDtAtualizacaoFinal() {
		return dtAtualizacaoFinal;
	}

	public void setDtAtualizacaoFinal(YearMonthDay dtAtualizacaoFinal) {
		this.dtAtualizacaoFinal = dtAtualizacaoFinal;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public DateTime getDhVigenciaInicial() {
		return dhVigenciaInicial;
	}

	public void setDhVigenciaInicial(DateTime dhVigenciaInicial) {
		this.dhVigenciaInicial = dhVigenciaInicial;
	}

	public DateTime getDhVigenciaFinal() {
		return dhVigenciaFinal;
	}

	public void setDhVigenciaFinal(DateTime dhVigenciaFinal) {
		this.dhVigenciaFinal = dhVigenciaFinal;
	}

	public DomainValue getBlTipo() {
		return blTipo;
	}

	public void setBlTipo(DomainValue blTipo) {
		this.blTipo = blTipo;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public ProprietarioDTO getProprietario() {
		return proprietario;
	}

	public void setProprietario(ProprietarioDTO proprietario) {
		this.proprietario = proprietario;
	}

	public RotaColetaEntregaSuggestDTO getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(
			RotaColetaEntregaSuggestDTO rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public MeioTransporteSuggestDTO getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporteSuggestDTO meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public TipoMeioTransporteDTO getTipoMeioTransporte() {
		return tipoMeioTransporte;
	}

	public void setTipoMeioTransporte(TipoMeioTransporteDTO tipoMeioTransporte) {
		this.tipoMeioTransporte = tipoMeioTransporte;
	}

	public PaisDTO getPais() {
		return pais;
	}

	public void setPais(PaisDTO pais) {
		this.pais = pais;
	}

	public UnidadeFederativaDTO getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativaDTO unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public MunicipioDTO getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioDTO municipio) {
		this.municipio = municipio;
	}

	public FilialSuggestDTO getFilialRotaColetaEntrega() {
		return filialRotaColetaEntrega;
	}

	public void setFilialRotaColetaEntrega(FilialSuggestDTO filialRotaColetaEntrega) {
		this.filialRotaColetaEntrega = filialRotaColetaEntrega;
	}
}