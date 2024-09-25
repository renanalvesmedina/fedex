package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.municipios.dto.MunicipioDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.TipoMeioTransporteDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.PaisDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class TabelaFcValoresRestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String type;

	private Long idTabelaFcValores;
	private Long idTabelaFreteCarreteiroCe;
	private Boolean destinatario;

	private ClienteSuggestDTO cliente;
	private ProprietarioDTO proprietario;
	private FilialSuggestDTO filialRotaColetaEntrega;
	private RotaColetaEntregaSuggestDTO rotaColetaEntrega;
	private MeioTransporteSuggestDTO meioTransporte;
	private TipoMeioTransporteDTO tipoMeioTransporte;
	private PaisDTO pais;
	private UnidadeFederativaDTO unidadeFederativa;
	private MunicipioDTO municipio;

	private UsuarioDTO usuarioCriacao;
	private UsuarioDTO usuarioExclusao;

	private YearMonthDay dtCriacao;
	private YearMonthDay dtExclusao;

	private BigDecimal vlConhecimento;
	private BigDecimal vlEvento;
	private BigDecimal vlVolume;
	private BigDecimal vlPalete;
	private BigDecimal vlTransferencia;
	private BigDecimal vlAjudante;
	private BigDecimal vlHora;
	private BigDecimal vlDiaria;
	private BigDecimal vlPreDiaria;
	private BigDecimal vlDedicado;
	private BigDecimal vlPernoite;
	private BigDecimal vlCapataziaCliente;
	private BigDecimal vlLocacaoCarreta;
	private BigDecimal vlPremio;
	private BigDecimal vlKmExcedente;
	private BigDecimal vlFreteMinimo;
	private BigDecimal pcFrete;
	private BigDecimal vlMercadoriaMinimo;
	private BigDecimal pcMercadoria;
	private Integer qtAjudante;
	private BigDecimal vlFreteMinimoLiq;
	private BigDecimal pcFreteLiq;

	private DomainValue blGeral;
	private DomainValue blTipo;

	private List<TabelaFcFaixaPesoRestDTO> listTabelaFcFaixaPeso;

	private List<FatorCalculoDTO> listFatorCalculo;

	public Long getIdTabelaFcValores() {
		return idTabelaFcValores;
	}

	public void setIdTabelaFcValores(Long idTabelaFcValores) {
		this.idTabelaFcValores = idTabelaFcValores;
	}

	public Long getIdTabelaFreteCarreteiroCe() {
		return idTabelaFreteCarreteiroCe;
	}

	public void setIdTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe) {
		this.idTabelaFreteCarreteiroCe = idTabelaFreteCarreteiroCe;
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

	public MunicipioDTO getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioDTO municipio) {
		this.municipio = municipio;
	}

	public UsuarioDTO getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(UsuarioDTO usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public UsuarioDTO getUsuarioExclusao() {
		return usuarioExclusao;
	}

	public void setUsuarioExclusao(UsuarioDTO usuarioExclusao) {
		this.usuarioExclusao = usuarioExclusao;
	}

	public YearMonthDay getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(YearMonthDay dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public YearMonthDay getDtExclusao() {
		return dtExclusao;
	}

	public void setDtExclusao(YearMonthDay dtExclusao) {
		this.dtExclusao = dtExclusao;
	}

	public BigDecimal getVlConhecimento() {
		return vlConhecimento;
	}

	public void setVlConhecimento(BigDecimal vlConhecimento) {
		this.vlConhecimento = vlConhecimento;
	}

	public BigDecimal getVlEvento() {
		return vlEvento;
	}

	public void setVlEvento(BigDecimal vlEvento) {
		this.vlEvento = vlEvento;
	}

	public BigDecimal getVlVolume() {
		return vlVolume;
	}

	public void setVlVolume(BigDecimal vlVolume) {
		this.vlVolume = vlVolume;
	}

	public BigDecimal getVlPalete() {
		return vlPalete;
	}

	public void setVlPalete(BigDecimal vlPalete) {
		this.vlPalete = vlPalete;
	}

	public BigDecimal getVlTransferencia() {
		return vlTransferencia;
	}

	public void setVlTransferencia(BigDecimal vlTransferencia) {
		this.vlTransferencia = vlTransferencia;
	}

	public BigDecimal getVlAjudante() {
		return vlAjudante;
	}

	public void setVlAjudante(BigDecimal vlAjudante) {
		this.vlAjudante = vlAjudante;
	}

	public BigDecimal getVlHora() {
		return vlHora;
	}

	public void setVlHora(BigDecimal vlHora) {
		this.vlHora = vlHora;
	}

	public BigDecimal getVlDiaria() {
		return vlDiaria;
	}

	public void setVlDiaria(BigDecimal vlDiaria) {
		this.vlDiaria = vlDiaria;
	}

	public BigDecimal getVlPreDiaria() {
		return vlPreDiaria;
	}

	public void setVlPreDiaria(BigDecimal vlPreDiaria) {
		this.vlPreDiaria = vlPreDiaria;
	}

	public BigDecimal getVlDedicado() {
		return vlDedicado;
	}

	public void setVlDedicado(BigDecimal vlDedicado) {
		this.vlDedicado = vlDedicado;
	}

	public BigDecimal getVlPernoite() {
		return vlPernoite;
	}

	public void setVlPernoite(BigDecimal vlPernoite) {
		this.vlPernoite = vlPernoite;
	}

	public BigDecimal getVlCapataziaCliente() {
		return vlCapataziaCliente;
	}

	public void setVlCapataziaCliente(BigDecimal vlCapataziaCliente) {
		this.vlCapataziaCliente = vlCapataziaCliente;
	}

	public BigDecimal getVlLocacaoCarreta() {
		return vlLocacaoCarreta;
	}

	public void setVlLocacaoCarreta(BigDecimal vlLocacaoCarreta) {
		this.vlLocacaoCarreta = vlLocacaoCarreta;
	}

	public BigDecimal getVlPremio() {
		return vlPremio;
	}

	public void setVlPremio(BigDecimal vlPremio) {
		this.vlPremio = vlPremio;
	}

	public BigDecimal getVlKmExcedente() {
		return vlKmExcedente;
	}

	public void setVlKmExcedente(BigDecimal vlKmExcedente) {
		this.vlKmExcedente = vlKmExcedente;
	}

	public BigDecimal getVlFreteMinimo() {
		return vlFreteMinimo;
	}

	public void setVlFreteMinimo(BigDecimal vlFreteMinimo) {
		this.vlFreteMinimo = vlFreteMinimo;
	}

	public BigDecimal getPcFrete() {
		return pcFrete;
	}

	public void setPcFrete(BigDecimal pcFrete) {
		this.pcFrete = pcFrete;
	}

	public BigDecimal getVlMercadoriaMinimo() {
		return vlMercadoriaMinimo;
	}

	public void setVlMercadoriaMinimo(BigDecimal vlMercadoriaMinimo) {
		this.vlMercadoriaMinimo = vlMercadoriaMinimo;
	}

	public BigDecimal getPcMercadoria() {
		return pcMercadoria;
	}

	public void setPcMercadoria(BigDecimal pcMercadoria) {
		this.pcMercadoria = pcMercadoria;
	}

	public List<TabelaFcFaixaPesoRestDTO> getListTabelaFcFaixaPeso() {
		return listTabelaFcFaixaPeso;
	}

	public void setListTabelaFcFaixaPeso(
			List<TabelaFcFaixaPesoRestDTO> listTabelaFcFaixaPeso) {
		this.listTabelaFcFaixaPeso = listTabelaFcFaixaPeso;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DomainValue getBlGeral() {
		return blGeral;
	}

	public void setBlGeral(DomainValue blGeral) {
		this.blGeral = blGeral;
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

	public RotaColetaEntregaSuggestDTO getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(
			RotaColetaEntregaSuggestDTO rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public FilialSuggestDTO getFilialRotaColetaEntrega() {
		return filialRotaColetaEntrega;
	}

	public void setFilialRotaColetaEntrega(FilialSuggestDTO filialRotaColetaEntrega) {
		this.filialRotaColetaEntrega = filialRotaColetaEntrega;
	}

	public Integer getQtAjudante() {
		return qtAjudante;
	}

	public void setQtAjudante(Integer qtAjudante) {
		this.qtAjudante = qtAjudante;
	}

	public DomainValue getBlTipo() {
		return blTipo;
	}

	public void setBlTipo(DomainValue blTipo) {
		this.blTipo = blTipo;
	}

	public BigDecimal getVlFreteMinimoLiq() {
		return vlFreteMinimoLiq;
	}

	public void setVlFreteMinimoLiq(BigDecimal vlFreteMinimoLiq) {
		this.vlFreteMinimoLiq = vlFreteMinimoLiq;
	}

	public BigDecimal getPcFreteLiq() {
		return pcFreteLiq;
	}

	public void setPcFreteLiq(BigDecimal pcFreteLiq) {
		this.pcFreteLiq = pcFreteLiq;
	}

	public List<FatorCalculoDTO> getListFatorCalculo() {
		return listFatorCalculo;
	}

	public void setListFatorCalculo(List<FatorCalculoDTO> listFatorCalculo) {
		this.listFatorCalculo = listFatorCalculo;
	}

	public Boolean getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Boolean destinatario) {
		this.destinatario = destinatario;
	}

}