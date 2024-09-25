package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.rest.configuracoes.ServicoChosenDTO;
import com.mercurio.lms.rest.expedicao.DoctoServicoSuggestDTO;
import com.mercurio.lms.rest.expedicao.dto.AwbSuggestDTO;
import com.mercurio.lms.rest.expedicao.dto.NaturezaProdutoChosenDTO;
import com.mercurio.lms.rest.municipios.dto.LocalidadeEspecialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.MunicipioFilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class PedidoColetaDetalheColetaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private String vigentes;
	private boolean aforado;
	private int qtVolumes;
	private Long idDoctoServico;
	private String tpDoctoSgFilial;
	private String sgPaisDoctoServico;
	private Long nrDoctoServico;
	private String nrCrtDoctoServico;
	private String tpFreteValue;
	private boolean tpFreteStatus;
	private Long idCotacao;
	private String doctoAwb;
	private String sgUnidadeFederativa;
	private CiaFilialMercurio ciaFilialMercurio;
	private BigDecimal vlMercadoria;
	private String nmDestinatario;
	private Long idNaturezaProduto;
	private Long idLocalidadeEspecial;
	private String dsLocalidadeEspecial;
	private List<NotaFiscalColetaDTO> notaFiscalColetas;
	private List<NotaFiscalColetaDTO> nrChaveNfe;
	private BigDecimal psMercadoria;
	private Long idServico;
	private String dsServico;
	private boolean blEntregaDireta;
	private Pessoa pessoa;
	private String awbDs;
	private boolean inclusaoParcial;
	private EventoColeta eventoColeta;
	private Long idMunicipio;
	private String nmMunicipio;
	private String uf;
	private CtoInternacional ctoInternacional;
	private Long idDoctoServicoCtoInternacional;
	private String sgPaisCtoInternacional;
	private Long nrCrtCtoInternacional;
	private String obDetalheColeta;
	private Long idFilial;
	private String sgFilial;
	private String nmFantasiaFilial;
	private BigDecimal psAforado;
	private String tpPedidoColeta;
	private Long idCliente;
	private String nrIdentificacaoCliente;
	private String nmDestinatarioCliente;
	private Long idMoeda;
	private String dsSimboloMoeda;
	private String sgMoeda;
	private String origem;
	private Long tpFreteId;
	private String tpFreteDescription;
	private String dsNaturezaProduto;
	private Long idEmpresa;
	private Long idPreAwb;
	private String nmAeroporto;
	private Long idAwb;
	private Long nrAwb;
	private String tpDocumentoServico;
	private ServicoChosenDTO servico;
	private NaturezaProdutoChosenDTO naturezaProduto;
	private DomainValue tpFrete;
	private DomainValue destino;
	private MunicipioFilialSuggestDTO municipioFilial;
	private LocalidadeEspecialSuggestDTO localidadeEspecial;
	private ClienteSuggestDTO destinatario;
	private DomainValue tpStatusAwb;
	private AwbSuggestDTO awb;
	private DoctoServicoSuggestDTO doctoServico;

	public NaturezaProdutoChosenDTO getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProdutoChosenDTO naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public DomainValue getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public DomainValue getDestino() {
		return destino;
	}

	public void setDestino(DomainValue destino) {
		this.destino = destino;
	}

	public MunicipioFilialSuggestDTO getMunicipioFilial() {
		return municipioFilial;
	}

	public void setMunicipioFilial(MunicipioFilialSuggestDTO municipioFilial) {
		this.municipioFilial = municipioFilial;
	}

	public LocalidadeEspecialSuggestDTO getLocalidadeEspecial() {
		return localidadeEspecial;
	}

	public void setLocalidadeEspecial(LocalidadeEspecialSuggestDTO localidadeEspecial) {
		this.localidadeEspecial = localidadeEspecial;
	}

	public ClienteSuggestDTO getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(ClienteSuggestDTO destinatario) {
		this.destinatario = destinatario;
	}

	public DomainValue getTpStatusAwb() {
		return tpStatusAwb;
	}

	public void setTpStatusAwb(DomainValue tpStatusAwb) {
		this.tpStatusAwb = tpStatusAwb;
	}

	public AwbSuggestDTO getAwb() {
		return awb;
	}

	public void setAwb(AwbSuggestDTO awb) {
		this.awb = awb;
	}

	public DoctoServicoSuggestDTO getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServicoSuggestDTO doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Long getIdPreAwb() {
		return idPreAwb;
	}

	public void setIdPreAwb(Long idPreAwb) {
		this.idPreAwb = idPreAwb;
	}

	public String getNmAeroporto() {
		return nmAeroporto;
	}

	public void setNmAeroporto(String nmAeroporto) {
		this.nmAeroporto = nmAeroporto;
	}

	public Long getIdAwb() {
		return idAwb;
	}

	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}

	public Long getNrAwb() {
		return nrAwb;
	}

	public void setNrAwb(Long nrAwb) {
		this.nrAwb = nrAwb;
	}

	public Long getIdDoctoServicoCtoInternacional() {
		return idDoctoServicoCtoInternacional;
	}

	public void setIdDoctoServicoCtoInternacional(Long idDoctoServicoCtoInternacional) {
		this.idDoctoServicoCtoInternacional = idDoctoServicoCtoInternacional;
	}

	public String getSgPaisCtoInternacional() {
		return sgPaisCtoInternacional;
	}

	public void setSgPaisCtoInternacional(String sgPaisCtoInternacional) {
		this.sgPaisCtoInternacional = sgPaisCtoInternacional;
	}

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public String getSgPaisDoctoServico() {
		return sgPaisDoctoServico;
	}

	public void setSgPaisDoctoServico(String sgPaisDoctoServico) {
		this.sgPaisDoctoServico = sgPaisDoctoServico;
	}

	public String getNrCrtDoctoServico() {
		return nrCrtDoctoServico;
	}

	public void setNrCrtDoctoServico(String nrCrtDoctoServico) {
		this.nrCrtDoctoServico = nrCrtDoctoServico;
	}

	public Long getIdCotacao() {
		return idCotacao;
	}

	public void setIdCotacao(Long idCotacao) {
		this.idCotacao = idCotacao;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getNrIdentificacaoCliente() {
		return nrIdentificacaoCliente;
	}

	public void setNrIdentificacaoCliente(String nrIdentificacaoCliente) {
		this.nrIdentificacaoCliente = nrIdentificacaoCliente;
	}

	public String getNmDestinatarioCliente() {
		return nmDestinatarioCliente;
	}

	public void setNmDestinatarioCliente(String nmDestinatarioCliente) {
		this.nmDestinatarioCliente = nmDestinatarioCliente;
	}

	public Long getIdMoeda() {
		return idMoeda;
	}

	public void setIdMoeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public String getDsSimboloMoeda() {
		return dsSimboloMoeda;
	}

	public void setDsSimboloMoeda(String dsSimboloMoeda) {
		this.dsSimboloMoeda = dsSimboloMoeda;
	}

	public String getSgMoeda() {
		return sgMoeda;
	}

	public void setSgMoeda(String sgMoeda) {
		this.sgMoeda = sgMoeda;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public String getNmFantasiaFilial() {
		return nmFantasiaFilial;
	}

	public void setNmFantasiaFilial(String nmFantasiaFilial) {
		this.nmFantasiaFilial = nmFantasiaFilial;
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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getVigentes() {
		return vigentes;
	}

	public void setVigentes(String vigentes) {
		this.vigentes = vigentes;
	}

	public boolean isAforado() {
		return aforado;
	}

	public void setAforado(boolean aforado) {
		this.aforado = aforado;
	}

	public int getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(int qtVolumes) {
		this.qtVolumes = qtVolumes;
	}


	public String getTpFreteValue() {
		return tpFreteValue;
	}

	public void setTpFreteValue(String tpFreteValue) {
		this.tpFreteValue = tpFreteValue;
	}


	public String getDoctoAwb() {
		return doctoAwb;
	}

	public void setDoctoAwb(String doctoAwb) {
		this.doctoAwb = doctoAwb;
	}

	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}

	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}

	public CiaFilialMercurio getCiaFilialMercurio() {
		return ciaFilialMercurio;
	}

	public void setCiaFilialMercurio(CiaFilialMercurio ciaFilialMercurio) {
		this.ciaFilialMercurio = ciaFilialMercurio;
	}

	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	public String getNmDestinatario() {
		return nmDestinatario;
	}

	public void setNmDestinatario(String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

	public Long getIdNaturezaProduto() {
		return idNaturezaProduto;
	}

	public void setIdNaturezaProduto(Long idNaturezaProduto) {
		this.idNaturezaProduto = idNaturezaProduto;
	}

	public List<NotaFiscalColetaDTO> getNotaFiscalColetas() {
		return notaFiscalColetas;
	}

	public void setNotaFiscalColetas(List<NotaFiscalColetaDTO> notaFiscalColetas) {
		this.notaFiscalColetas = notaFiscalColetas;
	}

	public BigDecimal getPsMercadoria() {
		return psMercadoria;
	}

	public void setPsMercadoria(BigDecimal psMercadoria) {
		this.psMercadoria = psMercadoria;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public boolean isBlEntregaDireta() {
		return blEntregaDireta;
	}

	public void setBlEntregaDireta(boolean blEntregaDireta) {
		this.blEntregaDireta = blEntregaDireta;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public boolean isInclusaoParcial() {
		return inclusaoParcial;
	}

	public void setInclusaoParcial(boolean inclusaoParcial) {
		this.inclusaoParcial = inclusaoParcial;
	}

	public EventoColeta getEventoColeta() {
		return eventoColeta;
	}

	public void setEventoColeta(EventoColeta eventoColeta) {
		this.eventoColeta = eventoColeta;
	}

	public CtoInternacional getCtoInternacional() {
		return ctoInternacional;
	}

	public void setCtoInternacional(CtoInternacional ctoInternacional) {
		this.ctoInternacional = ctoInternacional;
	}

	public String getObDetalheColeta() {
		return obDetalheColeta;
	}

	public void setObDetalheColeta(String obDetalheColeta) {
		this.obDetalheColeta = obDetalheColeta;
	}

	public BigDecimal getPsAforado() {
		return psAforado;
	}

	public void setPsAforado(BigDecimal psAforado) {
		this.psAforado = psAforado;
	}

	public String getTpPedidoColeta() {
		return tpPedidoColeta;
	}

	public void setTpPedidoColeta(String tpPedidoColeta) {
		this.tpPedidoColeta = tpPedidoColeta;
	}
	
	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getTpFreteDescription() {
		return tpFreteDescription;
	}

	public void setTpFreteDescription(String tpFreteDescription) {
		this.tpFreteDescription = tpFreteDescription;
	}

	public boolean getTpFreteStatus() {
		return tpFreteStatus;
	}

	public void setTpFreteStatus(boolean tpFreteStatus) {
		this.tpFreteStatus = tpFreteStatus;
	}

	public String getDsServico() {
		return dsServico;
	}

	public void setDsServico(String dsServico) {
		this.dsServico = dsServico;
	}

	public Long getTpFreteId() {
		return tpFreteId;
	}

	public void setTpFreteId(Long tpFreteId) {
		this.tpFreteId = tpFreteId;
	}

	public String getDsNaturezaProduto() {
		return dsNaturezaProduto;
	}

	public void setDsNaturezaProduto(String dsNaturezaProduto) {
		this.dsNaturezaProduto = dsNaturezaProduto;
	}

	public Long getIdLocalidadeEspecial() {
		return idLocalidadeEspecial;
	}

	public void setIdLocalidadeEspecial(Long idLocalidadeEspecial) {
		this.idLocalidadeEspecial = idLocalidadeEspecial;
	}

	public String getDsLocalidadeEspecial() {
		return dsLocalidadeEspecial;
	}

	public void setDsLocalidadeEspecial(String dsLocalidadeEspecial) {
		this.dsLocalidadeEspecial = dsLocalidadeEspecial;
	}

	public String getTpDoctoSgFilial() {
		return tpDoctoSgFilial;
	}

	public void setTpDoctoSgFilial(String tpDoctoSgFilial) {
		this.tpDoctoSgFilial = tpDoctoSgFilial;
	}

	public Long getNrDoctoServico() {
		return nrDoctoServico;
	}

	public void setNrDoctoServico(Long nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}

	public Long getNrCrtCtoInternacional() {
		return nrCrtCtoInternacional;
	}

	public void setNrCrtCtoInternacional(Long nrCrtCtoInternacional) {
		this.nrCrtCtoInternacional = nrCrtCtoInternacional;
	}

	public List<NotaFiscalColetaDTO> getNrChaveNfe() {
		return nrChaveNfe;
	}

	public void setNrChaveNfe(List<NotaFiscalColetaDTO> nrChaveNfe) {
		this.nrChaveNfe = nrChaveNfe;
	}

	public String getTpDocumentoServico() {
		return tpDocumentoServico;
	}

	public void setTpDocumentoServico(String tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public ServicoChosenDTO getServico() {
		return servico;
	}

	public void setServico(ServicoChosenDTO servico) {
		this.servico = servico;
	}

	public String getAwbDs() {
		return awbDs;
	}

	public void setAwbDs(String awbDs) {
		this.awbDs = awbDs;
	}

}
