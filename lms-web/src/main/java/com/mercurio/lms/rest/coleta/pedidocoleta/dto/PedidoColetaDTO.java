package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.ServicoAdicionalChosenDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.util.BigDecimalUtils;

public class PedidoColetaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idPedidoColeta;
	private Long nrColeta;
	private String nrDddCliente;
	private String nrTelefoneCliente;
	private String nrNovoDddCliente;
	private String nrNovoTelefoneCliente;
	private String nrCep;
	private DateTime dhPedidoColeta;
	private DateTime dhColetaDisponivel;
	private YearMonthDay dtPrevisaoColeta;
	private String hrLimiteColeta;
	private DomainValue tpModoPedidoColeta;
	private DomainValue tpPedidoColeta;
	private DomainValue tpStatusColeta;
	private String edColeta;
	private String dsBairro;
	private String nmSolicitante;
	private String nmContatoCliente;
	private BigDecimal vlTotalInformado;
	private BigDecimal vlTotalVerificado = BigDecimalUtils.ZERO;;
	private Integer qtTotalVolumesInformado;
	private Integer qtTotalVolumesVerificado = Integer.valueOf(0);;
	private BigDecimal psTotalInformado;
	private BigDecimal psTotalVerificado = BigDecimalUtils.ZERO;;
	private BigDecimal psTotalAforadoInformado;
	private BigDecimal psTotalAforadoVerificado = BigDecimalUtils.ZERO;;
	private Boolean blClienteLiberadoManual;
	private Boolean blAlteradoPosProgramacao;
	private String nrEndereco;
	private String dsComplementoEndereco;
	private String obPedidoColeta;
	private Boolean blProdutoDiferenciado;
	private String dsInfColeta;
	private DomainValue situacaoAprovacao;
	private List<ProdutoDiferenciadoDTO> produtosDiferenciados;
	private Long idUsuario;
	private Long idFilialByIdFilialSolicitante;
	private ClienteSuggestDTO cliente;
	private Long idEnderecoPessoa;
	private Long idMoeda;
	private String siglaSimbolo;
	private Long idMunicipio;
	private String nmMunicipio;
	private String sgUnidadeFederativaMunicipio;
	private String sgFilialColeta;
	private FilialSuggestDTO filialByIdFilialResponsavel;
	private FilialSuggestDTO filialByIdFilialSolicitante;
	private Long idRotaIntervaloCep;
	private RotaColetaEntregaDTO rotaColetaEntrega;
	private CotacaoDTO cotacao;
	private String horarioCorte;
	private List<ServicoAdicionalChosenDTO> servicosAdicionaisColetas;
	private String endereco;
	private TelefoneEnderecoDTO telefoneEndereco;
	private String dsTipoLogradouro;
	private Long eventoColetaidUsuario;	
	private Long eventoColetaidOcorrenciaColeta;
	private String eventoColetadsDescricao;	
	private Boolean buttonBloqueioCredito;
	private String nmUnidadeFederativa;
	private PedidoColetaDetalheColetaDTO detalheColeta;
	private List<PedidoColetaDetalheColetaDTO> detalheColetasDTO;

	public Long getIdPedidoColeta() {
		return idPedidoColeta;
	}

	public void setIdPedidoColeta(Long idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}

	public Long getNrColeta() {
		return nrColeta;
	}

	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}

	public String getNrDddCliente() {
		return nrDddCliente;
	}

	public void setNrDddCliente(String nrDddCliente) {
		this.nrDddCliente = nrDddCliente;
	}

	public String getNrTelefoneCliente() {
		return nrTelefoneCliente;
	}

	public void setNrTelefoneCliente(String nrTelefoneCliente) {
		this.nrTelefoneCliente = nrTelefoneCliente;
	}

	public String getNrCep() {
		return nrCep;
	}

	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public DateTime getDhPedidoColeta() {
		return dhPedidoColeta;
	}

	public void setDhPedidoColeta(DateTime dhPedidoColeta) {
		this.dhPedidoColeta = dhPedidoColeta;
	}

	public DateTime getDhColetaDisponivel() {
		return dhColetaDisponivel;
	}

	public void setDhColetaDisponivel(DateTime dhColetaDisponivel) {
		this.dhColetaDisponivel = dhColetaDisponivel;
	}

	public YearMonthDay getDtPrevisaoColeta() {
		return dtPrevisaoColeta;
	}

	public void setDtPrevisaoColeta(YearMonthDay dtPrevisaoColeta) {
		this.dtPrevisaoColeta = dtPrevisaoColeta;
	}

	public String getHrLimiteColeta() {
		return hrLimiteColeta;
	}

	public void setHrLimiteColeta(String hrLimiteColeta) {
		this.hrLimiteColeta = hrLimiteColeta;
	}

	public DomainValue getTpModoPedidoColeta() {
		return tpModoPedidoColeta;
	}

	public void setTpModoPedidoColeta(DomainValue tpModoPedidoColeta) {
		this.tpModoPedidoColeta = tpModoPedidoColeta;
	}

	public DomainValue getTpPedidoColeta() {
		return tpPedidoColeta;
	}

	public void setTpPedidoColeta(DomainValue tpPedidoColeta) {
		this.tpPedidoColeta = tpPedidoColeta;
	}

	public DomainValue getTpStatusColeta() {
		return tpStatusColeta;
	}

	public void setTpStatusColeta(DomainValue tpStatusColeta) {
		this.tpStatusColeta = tpStatusColeta;
	}

	public String getEdColeta() {
		return edColeta;
	}

	public void setEdColeta(String edColeta) {
		this.edColeta = edColeta;
	}

	public String getDsBairro() {
		return dsBairro;
	}

	public void setDsBairro(String dsBairro) {
		this.dsBairro = dsBairro;
	}

	public String getNmSolicitante() {
		return nmSolicitante;
	}

	public void setNmSolicitante(String nmSolicitante) {
		this.nmSolicitante = nmSolicitante;
	}

	public String getNmContatoCliente() {
		return nmContatoCliente;
	}

	public void setNmContatoCliente(String nmContatoCliente) {
		this.nmContatoCliente = nmContatoCliente;
	}

	public BigDecimal getVlTotalInformado() {
		return vlTotalInformado;
	}

	public void setVlTotalInformado(BigDecimal vlTotalInformado) {
		this.vlTotalInformado = vlTotalInformado;
	}

	public BigDecimal getVlTotalVerificado() {
		return vlTotalVerificado;
	}

	public void setVlTotalVerificado(BigDecimal vlTotalVerificado) {
		this.vlTotalVerificado = vlTotalVerificado;
	}

	public Integer getQtTotalVolumesInformado() {
		return qtTotalVolumesInformado;
	}

	public void setQtTotalVolumesInformado(Integer qtTotalVolumesInformado) {
		this.qtTotalVolumesInformado = qtTotalVolumesInformado;
	}

	public Integer getQtTotalVolumesVerificado() {
		return qtTotalVolumesVerificado;
	}

	public void setQtTotalVolumesVerificado(Integer qtTotalVolumesVerificado) {
		this.qtTotalVolumesVerificado = qtTotalVolumesVerificado;
	}

	public BigDecimal getPsTotalInformado() {
		return psTotalInformado;
	}

	public void setPsTotalInformado(BigDecimal psTotalInformado) {
		this.psTotalInformado = psTotalInformado;
	}

	public BigDecimal getPsTotalVerificado() {
		return psTotalVerificado;
	}

	public void setPsTotalVerificado(BigDecimal psTotalVerificado) {
		this.psTotalVerificado = psTotalVerificado;
	}

	public BigDecimal getPsTotalAforadoInformado() {
		return psTotalAforadoInformado;
	}

	public void setPsTotalAforadoInformado(BigDecimal psTotalAforadoInformado) {
		this.psTotalAforadoInformado = psTotalAforadoInformado;
	}

	public BigDecimal getPsTotalAforadoVerificado() {
		return psTotalAforadoVerificado;
	}

	public void setPsTotalAforadoVerificado(BigDecimal psTotalAforadoVerificado) {
		this.psTotalAforadoVerificado = psTotalAforadoVerificado;
	}

	public Boolean getBlClienteLiberadoManual() {
		return blClienteLiberadoManual;
	}

	public void setBlClienteLiberadoManual(Boolean blClienteLiberadoManual) {
		this.blClienteLiberadoManual = blClienteLiberadoManual;
	}

	public Boolean getBlAlteradoPosProgramacao() {
		return blAlteradoPosProgramacao;
	}

	public void setBlAlteradoPosProgramacao(Boolean blAlteradoPosProgramacao) {
		this.blAlteradoPosProgramacao = blAlteradoPosProgramacao;
	}

	public String getNrEndereco() {
		return nrEndereco;
	}

	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}

	public String getDsComplementoEndereco() {
		return dsComplementoEndereco;
	}

	public void setDsComplementoEndereco(String dsComplementoEndereco) {
		this.dsComplementoEndereco = dsComplementoEndereco;
	}

	public String getObPedidoColeta() {
		return obPedidoColeta;
	}

	public void setObPedidoColeta(String obPedidoColeta) {
		this.obPedidoColeta = obPedidoColeta;
	}

	public Boolean getBlProdutoDiferenciado() {
		return blProdutoDiferenciado;
	}

	public void setBlProdutoDiferenciado(Boolean blProdutoDiferenciado) {
		this.blProdutoDiferenciado = blProdutoDiferenciado;
	}

	public String getDsInfColeta() {
		return dsInfColeta;
	}

	public void setDsInfColeta(String dsInfColeta) {
		this.dsInfColeta = dsInfColeta;
	}

	public DomainValue getSituacaoAprovacao() {
		return situacaoAprovacao;
	}

	public void setSituacaoAprovacao(DomainValue situacaoAprovacao) {
		this.situacaoAprovacao = situacaoAprovacao;
	}

	public List<ProdutoDiferenciadoDTO> getProdutosDiferenciados() {
		return produtosDiferenciados;
	}

	public void setProdutosDiferenciados(
			List<ProdutoDiferenciadoDTO> produtosDiferenciados) {
		this.produtosDiferenciados = produtosDiferenciados;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getIdFilialByIdFilialSolicitante() {
		return idFilialByIdFilialSolicitante;
	}

	public void setIdFilialByIdFilialSolicitante(
			Long idFilialByIdFilialSolicitante) {
		this.idFilialByIdFilialSolicitante = idFilialByIdFilialSolicitante;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public Long getIdEnderecoPessoa() {
		return idEnderecoPessoa;
	}

	public void setIdEnderecoPessoa(Long idEnderecoPessoa) {
		this.idEnderecoPessoa = idEnderecoPessoa;
	}

	public Long getIdMoeda() {
		return idMoeda;
	}

	public void setIdMoeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public String getSiglaSimbolo() {
		return siglaSimbolo;
	}

	public void setSiglaSimbolo(String siglaSimbolo) {
		this.siglaSimbolo = siglaSimbolo;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getNmMunicipio() {
		return nmMunicipio;
	}

	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}

	public String getSgUnidadeFederativaMunicipio() {
		return sgUnidadeFederativaMunicipio;
	}

	public void setSgUnidadeFederativaMunicipio(
			String sgUnidadeFederativaMunicipio) {
		this.sgUnidadeFederativaMunicipio = sgUnidadeFederativaMunicipio;
	}

	public String getSgFilialColeta() {
		return sgFilialColeta;
	}

	public void setSgFilialColeta(String sgFilialColeta) {
		this.sgFilialColeta = sgFilialColeta;
	}

	public FilialSuggestDTO getFilialByIdFilialResponsavel() {
		return filialByIdFilialResponsavel;
	}

	public void setFilialByIdFilialResponsavel(
			FilialSuggestDTO filialByIdFilialResponsavel) {
		this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
	}

	public Long getIdRotaIntervaloCep() {
		return idRotaIntervaloCep;
	}

	public void setIdRotaIntervaloCep(Long idRotaIntervaloCep) {
		this.idRotaIntervaloCep = idRotaIntervaloCep;
	}

	public RotaColetaEntregaDTO getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(RotaColetaEntregaDTO rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public CotacaoDTO getCotacao() {
		return cotacao;
	}

	public void setCotacao(CotacaoDTO cotacao) {
		this.cotacao = cotacao;
	}

	public String getHorarioCorte() {
		return horarioCorte;
	}

	public void setHorarioCorte(String horarioCorte) {
		this.horarioCorte = horarioCorte;
	}

	public List<ServicoAdicionalChosenDTO> getServicosAdicionaisColetas() {
		return servicosAdicionaisColetas;
	}

	public void setServicosAdicionaisColetas(List<ServicoAdicionalChosenDTO> servicosAdicionaisColetas) {
		this.servicosAdicionaisColetas = servicosAdicionaisColetas;
	}

	public String getNrNovoTelefoneCliente() {
		return nrNovoTelefoneCliente;
	}

	public void setNrNovoTelefoneCliente(String nrNovoTelefoneCliente) {
		this.nrNovoTelefoneCliente = nrNovoTelefoneCliente;
	}

	public String getNrNovoDddCliente() {
		return nrNovoDddCliente;
	}

	public void setNrNovoDddCliente(String nrNovoDddCliente) {
		this.nrNovoDddCliente = nrNovoDddCliente;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public TelefoneEnderecoDTO getTelefoneEndereco() {
		return telefoneEndereco;
	}

	public void setTelefoneEndereco(TelefoneEnderecoDTO telefoneEndereco) {
		this.telefoneEndereco = telefoneEndereco;
	}

	public String getDsTipoLogradouro() {
		return dsTipoLogradouro;
	}

	public void setDsTipoLogradouro(String dsTipoLogradouro) {
		this.dsTipoLogradouro = dsTipoLogradouro;
	}

	public FilialSuggestDTO getFilialByIdFilialSolicitante() {
		return filialByIdFilialSolicitante;
	}

	public void setFilialByIdFilialSolicitante(FilialSuggestDTO filialByIdFilialSolicitante) {
		this.filialByIdFilialSolicitante = filialByIdFilialSolicitante;
	}

	public Long getEventoColetaidUsuario() {
		return eventoColetaidUsuario;
	}

	public void setEventoColetaidUsuario(Long eventoColetaidUsuario) {
		this.eventoColetaidUsuario = eventoColetaidUsuario;
	}

	public Long getEventoColetaidOcorrenciaColeta() {
		return eventoColetaidOcorrenciaColeta;
	}

	public void setEventoColetaidOcorrenciaColeta(
			Long eventoColetaidOcorrenciaColeta) {
		this.eventoColetaidOcorrenciaColeta = eventoColetaidOcorrenciaColeta;
	}

	public String getEventoColetadsDescricao() {
		return eventoColetadsDescricao;
	}

	public void setEventoColetadsDescricao(String eventoColetadsDescricao) {
		this.eventoColetadsDescricao = eventoColetadsDescricao;
	}

	public Boolean getButtonBloqueioCredito() {
		return buttonBloqueioCredito;
	}

	public void setButtonBloqueioCredito(Boolean buttonBloqueioCredito) {
		this.buttonBloqueioCredito = buttonBloqueioCredito;
	}
	
	public String getNmUnidadeFederativa() {
		return nmUnidadeFederativa;
	}

	public void setNmUnidadeFederativaMunicipio(String nmUnidadeFederativa) {
		this.nmUnidadeFederativa = nmUnidadeFederativa;
	}

	public void setNmUnidadeFederativa(String nmUnidadeFederativa) {
		this.nmUnidadeFederativa = nmUnidadeFederativa;
	}

	public List<PedidoColetaDetalheColetaDTO> getDetalheColetasDTO() {
		return detalheColetasDTO;
	}

	public void setDetalheColetasDTO(List<PedidoColetaDetalheColetaDTO> detalheColetasDTO) {
		this.detalheColetasDTO = detalheColetasDTO;
	}

	public PedidoColetaDetalheColetaDTO getDetalheColeta() {
		return detalheColeta;
	}

	public void setDetalheColeta(PedidoColetaDetalheColetaDTO detalheColeta) {
		this.detalheColeta = detalheColeta;
	}
	
}