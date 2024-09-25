package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;

public abstract class CalculoServico<T extends DoctoServico> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String tpFrete = null;
	private String tpModal = null;
	private String tpAbrangencia = null;
	private String tpConhecimento = null;
	private String tpCalculo = null;
	private Boolean blCalculaParcelas = null;
	private Boolean blCalculaServicosAdicionais = null;
	private Boolean blCotacao = Boolean.FALSE;
	private Boolean blParametroCotacao = Boolean.FALSE;
	private Boolean blRecalculoCotacao = Boolean.FALSE;
	private Boolean blEditaCotacao = Boolean.FALSE;

	private Boolean blCalculaImpostoServico = Boolean.FALSE;
	private Boolean blDescontoTotal = null;
	//Se CRTC da cooperada estiver preenchido
	private Boolean blRedespacho = null;

	private BigDecimal psReferencia = null;
	private BigDecimal vlMercadoria = null;
	private Long idDivisaoCliente = null;
	private Long idServico = null;
	private Long idParcelaPreco = null;
	private Long idCotacao = null;
	private List<ServAdicionalDocServ> servAdicionalDoctoServico;
	private RestricaoRota restricaoRotaOrigem;
	private RestricaoRota restricaoRotaDestino;
	private MunicipioFilial municipioFilialDestino;
	private MunicipioFilial municipioFilialOrigem;

	private Long idDoctoServico = null;
	private List<String> observacoes;
	private List<String> embLegaisMastersaf;
	private Cliente clienteBase;
	private TabelaPreco tabelaPreco;
	private ParametroCliente parametroCliente;
	private List<ParcelaServico> parcelasGerais;
	private List<ParcelaServico> taxas;
	private List<ParcelaServico> generalidades;
	private List<ParcelaServicoAdicional> servicosAdicionais;
	private List<ParcelaServicoAdicional> servicosAdicionaisAuxiliares;
	private List<ParcelaServico> parcelas;
	//Valores dos totaid
	private BigDecimal vlTotalParcelas = null;
	private BigDecimal vlTotalServicosAdicionais = null;
	private BigDecimal vlDesconto = BigDecimal.ZERO;
	private BigDecimal pcDesconto = null;
	private BigDecimal vlTotal = null;
	private BigDecimal vlDevido = null;
	private BigDecimal vlTotalTributos = null;
	private BigDecimal psReferenciaCalculo = null;

	//Valor calculo cotação
	private BigDecimal vlFretePesoCotacao;	
	
	private Boolean recalculoFrete;
	
	protected T doctoServico;

	private TabelaPreco tabelaPrecoRecalculo;

	private BigDecimal vlEmbutidoParcelas;

	public abstract void setDoctoServico(T doctoServico);

	public abstract T getDoctoServico();

	public abstract String getTpDocumentoServico();

	private Boolean blRecalculoFreteSorter = Boolean.FALSE;

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public String getTpModal() {
		return tpModal;
	}

	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}

	public String getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(String tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public String getTpConhecimento() {
		return tpConhecimento;
	}

	public void setTpConhecimento(String tpConhecimento) {
		this.tpConhecimento = tpConhecimento;
	}

	public String getTpCalculo() {
		return tpCalculo;
	}

	public void setTpCalculo(String tpCalculo) {
		this.tpCalculo = tpCalculo;
	}

	public Boolean getBlCalculaParcelas() {
		return blCalculaParcelas;
	}

	public void setBlCalculaParcelas(Boolean blCalculaParcelas) {
		this.blCalculaParcelas = blCalculaParcelas;
	}

	public Boolean getBlCalculaServicosAdicionais() {
		return blCalculaServicosAdicionais;
	}

	public void setBlCalculaServicosAdicionais(
			Boolean blCalculaServicosAdicionais) {
		this.blCalculaServicosAdicionais = blCalculaServicosAdicionais;
	}

	public Boolean getBlCotacao() {
		return blCotacao;
	}

	public void setBlCotacao(Boolean blCotacao) {
		this.blCotacao = blCotacao;
	}

	public Boolean getBlRecalculoCotacao() {
		return blRecalculoCotacao;
	}

	public void setBlRecalculoCotacao(Boolean blRecalculoCotacao) {
		this.blRecalculoCotacao = blRecalculoCotacao;
	}

	public Boolean getBlDescontoTotal() {
		return blDescontoTotal;
	}

	public void setBlDescontoTotal(Boolean blDescontoTotal) {
		this.blDescontoTotal = blDescontoTotal;
	}

	public BigDecimal getPcDesconto() {
		return pcDesconto;
	}

	public void setPcDesconto(BigDecimal pcDesconto) {
		this.pcDesconto = pcDesconto;
	}

	public Boolean getBlRedespacho() {
		return blRedespacho;
	}

	public void setBlRedespacho(Boolean blRedespacho) {
		this.blRedespacho = blRedespacho;
	}

	public BigDecimal getPsReferencia() {
		if(getRecalculoFrete() != null && getRecalculoFrete()){
			return psReferenciaCalculo; 
		}
		return psReferencia;
	}

	public void setPsReferencia(BigDecimal psReferencia) {
		this.psReferencia = psReferencia;
		if( Boolean.TRUE.equals(getBlCotacao()) || Boolean.TRUE.equals(getBlRecalculoFreteSorter())) {
			this.psReferenciaCalculo = psReferencia; 
		}
	}

	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		if(vlMercadoria != null){
			this.vlMercadoria = vlMercadoria.setScale(2, RoundingMode.HALF_UP);
		}else{
		this.vlMercadoria = vlMercadoria;
	}
	}

	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}

	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}

	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}

	public Long getIdCotacao() {
		return idCotacao;
	}

	public void setIdCotacao(Long idCotacao) {
		this.idCotacao = idCotacao;
	}

	public List<ServAdicionalDocServ> getServAdicionalDoctoServico() {
		return servAdicionalDoctoServico;
	}

	public void setServAdicionalDoctoServico(
			List<ServAdicionalDocServ> servAdicionalDoctoServico) {
		this.servAdicionalDoctoServico = servAdicionalDoctoServico;
	}

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public List<String> getObservacoes() {
		if(this.observacoes == null) {
			this.observacoes = new ArrayList<String>();
		}
		return this.observacoes;
	}
		
	public void clearObservacoes() {
		this.observacoes = new ArrayList<String>();
	}
	
	public void setObservacoes(List<String> observacoes) {
		this.observacoes = observacoes;
	}

	public List<String> getEmbLegaisMastersaf() {
		if(this.embLegaisMastersaf == null) {
			this.embLegaisMastersaf = new ArrayList<String>();
		}
		return this.embLegaisMastersaf;
	}

	public void clearEmbLegaisMastersaf() {
		this.embLegaisMastersaf = new ArrayList<String>();
	}

	public void setEmbLegaisMastersaf(List<String> embLegaisMastersaf) {
		this.embLegaisMastersaf = embLegaisMastersaf;
	}

	public Cliente getClienteBase() {
		return clienteBase;
	}

	public void setClienteBase(Cliente clienteBase) {
		this.clienteBase = clienteBase;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public ParametroCliente getParametroCliente() {
		return parametroCliente;
	}

	public void setParametroCliente(ParametroCliente parametroCliente) {
		this.parametroCliente = parametroCliente;
	}

	public ParcelaServico getParcelaGeral(String cdParcelaPreco) {
		return getParcela(cdParcelaPreco, getParcelasGerais());
	}

	public List<ParcelaServico> getParcelasGerais() {
		return parcelasGerais;
	}

	public void setParcelasGerais(List<ParcelaServico> parcelasGerais) {
		this.parcelasGerais = parcelasGerais;
	}

	public void resetParcelasGerais() {
		this.parcelasGerais = null;
	}

	public void addParcelaGeral(ParcelaServico parcela) {
		if(parcela != null) {
			if(this.parcelasGerais == null) {
				this.parcelasGerais = new ArrayList<ParcelaServico>();
			}
			this.parcelasGerais.add(parcela);
			this.addParcela(parcela);
		}
	}

	public GeneralidadeCliente getGeneralidadeCliente(String cdParcelaPreco){
		
		List<GeneralidadeCliente> list = getParametroCliente()
				.getGeneralidadeClientes();
		if(CollectionUtils.isNotEmpty(list)){
			for (GeneralidadeCliente gen : list) {
				if (cdParcelaPreco.equals(gen.getParcelaPreco()
						.getCdParcelaPreco())) {
					return gen;
				}
			}
		}
		return null;
	}
	
	public ParcelaServico getGeneralidade(String cdParcelaPreco) {
		return getParcela(cdParcelaPreco, getGeneralidades());
	}

	public List<ParcelaServico> getGeneralidades() {
		return generalidades;
	}

	public void addGeneralidade(ParcelaServico generalidade) {
		if(generalidade != null) {
			if(this.generalidades == null) {
				this.generalidades = new ArrayList<ParcelaServico>();
			}
			this.generalidades.add(generalidade);
			this.addParcelaGeral(generalidade);
		}
	}

	public ParcelaServico getTaxa(String cdParcelaPreco) {
		return getParcela(cdParcelaPreco, getTaxas());
	}

	public List<ParcelaServico> getTaxas() {
		return taxas;
	}

	public void addTaxa(ParcelaServico taxa) {
		if(taxa != null) {
			if(this.taxas == null) {
				this.taxas = new ArrayList<ParcelaServico>();
			}
			this.taxas.add(taxa);
			this.addParcelaGeral(taxa);
		}
	}

	public ParcelaServico getServicoAdicional(String cdParcelaPreco) {
		return getParcela(cdParcelaPreco, getServicosAdicionais());
	}

	public void resetServicosAdicionais() {
		this.servicosAdicionais = null;
	}

	public List<ParcelaServicoAdicional> getServicosAdicionais() {
		return servicosAdicionais;
	}

	public void addServicoAdicional(ParcelaServicoAdicional servicoAdicional) {
		if(servicoAdicional != null) {
			if(this.servicosAdicionais == null) {
				this.servicosAdicionais = new ArrayList<ParcelaServicoAdicional>();
			}
			this.servicosAdicionais.add(servicoAdicional);
		}
	}

	public List<ParcelaServicoAdicional> getTodosServicosAdicionais() {
		List<ParcelaServicoAdicional> result = null;
		if(this.servicosAdicionais != null) {
			result = new ArrayList<ParcelaServicoAdicional>();
			result.addAll(this.servicosAdicionais);
			if(this.servicosAdicionaisAuxiliares != null) {
				result.addAll(this.servicosAdicionaisAuxiliares);
			}
		} else {
			result = new ArrayList<ParcelaServicoAdicional>();
			if(this.servicosAdicionaisAuxiliares != null) {
				result.addAll(this.servicosAdicionaisAuxiliares);
			}
		}
		return result;
	}

	public ParcelaServico getServicoAdicionalAuxiliar(String cdParcelaPreco) {
		return getParcela(cdParcelaPreco, getServicosAdicionaisAuxiliares());
	}

	public List<ParcelaServicoAdicional> getServicosAdicionaisAuxiliares() {
		return servicosAdicionaisAuxiliares;
	}

	public void addServicoAdicionalAuxiliar(
			ParcelaServicoAdicional servicoAdicionalAuxiliar) {
		if(servicoAdicionalAuxiliar != null) {
			if(this.servicosAdicionaisAuxiliares == null) {
				this.servicosAdicionaisAuxiliares = new ArrayList<ParcelaServicoAdicional>();
			}
			this.servicosAdicionaisAuxiliares.add(servicoAdicionalAuxiliar);
		}
	}

	public BigDecimal getVlTotalParcelas() {
		return vlTotalParcelas;
	}

	public void setVlTotalParcelas(BigDecimal vlTotalParcelas) {
		if(vlTotalParcelas != null){
			this.vlTotalParcelas = vlTotalParcelas.setScale(2,
					RoundingMode.HALF_UP);
		}else{
		this.vlTotalParcelas = vlTotalParcelas;
	}
	}

	public void setVlTotalParcelas() {
		this.setVlTotalParcelas(Collections.EMPTY_LIST);
	}

	public void setVlTotalParcelas(List<String> cdParcelas) {
		BigDecimal vlTotal = BigDecimal.ZERO;
		if(Boolean.TRUE.equals(getBlCalculaParcelas())) {
			List<ParcelaServico> parcelas = getParcelas();
			if(parcelas != null) {
				for (ParcelaServico parcelaServico : parcelas) {
					if(parcelaServico.getVlParcela() == null) {
						parcelaServico.setVlParcela(parcelaServico
								.getVlBrutoParcela());
					}
					/** Verifica se foram passadas parcelas definidas */
					if(!cdParcelas.isEmpty()) {
						if (cdParcelas.contains(parcelaServico
								.getParcelaPreco().getCdParcelaPreco())) {
							vlTotal = vlTotal.add(parcelaServico
									.getVlBrutoParcela());
						}
					} else {
						if( parcelaServico.getVlParcela() != null ){
							vlTotal = vlTotal
									.add(parcelaServico.getVlParcela());
						}
					}
				}
			}
		}
		setVlTotalParcelas(vlTotal);
	}

	public BigDecimal getVlTotalServicosAdicionais() {
		return vlTotalServicosAdicionais;
	}

	public void setVlTotalServicosAdicionais(
			BigDecimal vlTotalServicosAdicionais) {
		if( vlTotalServicosAdicionais != null ){
			this.vlTotalServicosAdicionais = vlTotalServicosAdicionais
					.setScale(2, RoundingMode.HALF_UP);
		}else{
		this.vlTotalServicosAdicionais = vlTotalServicosAdicionais;
	}
	}

	public void setVlTotalServicosAdicionais() {
		BigDecimal vlTotal = BigDecimal.ZERO;
		if(Boolean.TRUE.equals(getBlCalculaServicosAdicionais())) {
			List<ParcelaServicoAdicional> parcelas = getServicosAdicionais();
			if(parcelas != null) {
				for (ParcelaServicoAdicional servicoAdicional : parcelas) {
					if(servicoAdicional.getVlParcela() == null) {
						servicoAdicional.setVlParcela(servicoAdicional
								.getVlBrutoParcela());
					}
					vlTotal = vlTotal.add(servicoAdicional.getVlParcela());
				}
			}
		}
		setVlTotalServicosAdicionais(vlTotal);
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		if(vlDesconto != null){
			this.vlDesconto = vlDesconto.setScale(2, RoundingMode.HALF_UP);
		}else{
		this.vlDesconto = vlDesconto;
	}
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		if(vlTotal != null){
			this.vlTotal = vlTotal.setScale(2, RoundingMode.HALF_UP);
		}else{
		this.vlTotal = vlTotal;
	}
	}

	public BigDecimal getVlDevido() {
		return vlDevido;
	}

	public void setVlDevido(BigDecimal vlDevido) {
		if(vlDevido != null){
			this.vlDevido = vlDevido.setScale(2, RoundingMode.HALF_UP);
		}else{
		this.vlDevido = vlDevido;
	}
	}

	public BigDecimal getVlTotalTributos() {
		return vlTotalTributos;
	}

	public void setVlTotalTributos(BigDecimal vlTotalTributos) {
		if(vlTotalTributos != null){
			this.vlTotalTributos = vlTotalTributos.setScale(2,
					RoundingMode.HALF_UP);
		}else{
		this.vlTotalTributos = vlTotalTributos;
	}
	}

	private void addParcela(ParcelaServico parcelaServico) {
		this.getParcelas().add(parcelaServico);
	}

	public List<ParcelaServico> getParcelas() {
		if(this.parcelas == null) {
			this.parcelas = new ArrayList<ParcelaServico>();
		}
		return this.parcelas;
	}

	public void resetParcelas() {
		this.parcelas = null;
		this.parcelasGerais = null;
		this.taxas = null;
		this.generalidades = null;
		this.setVlTotalParcelas(BigDecimal.ZERO);
	}

	public void resetParcelasRecalculoCotacao() {
		this.parcelas = null;
		this.parcelasGerais = null;
		this.generalidades = null;
		this.setVlTotalParcelas(BigDecimal.ZERO);
	}

	public void resetValores() {
		setVlTotalParcelas(BigDecimal.ZERO);
		setVlTotalServicosAdicionais(BigDecimal.ZERO);
		setVlDesconto(BigDecimal.ZERO);
		setPcDesconto(BigDecimal.ZERO);
		setVlTotal(BigDecimal.ZERO);
	}

	private ParcelaServico getParcela(String cdParcelaPreco,
			List<? extends ParcelaServico> parcelasServico) {
		if(parcelasServico != null) {
			for (ParcelaServico parcelaServico : parcelasServico) {
				if (cdParcelaPreco.equals(parcelaServico.getParcelaPreco()
						.getCdParcelaPreco())) {
					return parcelaServico;
				}
			}
		}
		return null;
	}

	public RestricaoRota getRestricaoRotaDestino() {
		if(restricaoRotaDestino == null) {
			restricaoRotaDestino = new RestricaoRota();
		}
		return restricaoRotaDestino;
	}

	public void setRestricaoRotaDestino(RestricaoRota restricaoRotaDestino) {
		this.restricaoRotaDestino = restricaoRotaDestino;
	}

	public RestricaoRota getRestricaoRotaOrigem() {
		if(restricaoRotaOrigem == null) {
			restricaoRotaOrigem = new RestricaoRota();
		}
		return restricaoRotaOrigem;
	}

	public void setRestricaoRotaOrigem(RestricaoRota restricaoRotaOrigem) {
		this.restricaoRotaOrigem = restricaoRotaOrigem;
	}

	public MunicipioFilial getMunicipioFilialDestino() {
		return municipioFilialDestino;
	}

	public void setMunicipioFilialDestino(MunicipioFilial municipioFilialDestino) {
		this.municipioFilialDestino = municipioFilialDestino;
	}

	public MunicipioFilial getMunicipioFilialOrigem() {
		return municipioFilialOrigem;
	}

	public void setMunicipioFilialOrigem(MunicipioFilial municipioFilialOrigem) {
		this.municipioFilialOrigem = municipioFilialOrigem;
	}

	public Boolean getRecalculoFrete() {
		return recalculoFrete;
	}

	public void setRecalculoFrete(Boolean recalculoFrete) {
		this.recalculoFrete = recalculoFrete;
	}

	public TabelaPreco getTabelaPrecoRecalculo() {
		return tabelaPrecoRecalculo;
	}

	public void setTabelaPrecoRecalculo(TabelaPreco tabelaPrecoRecalculo) {
		this.tabelaPrecoRecalculo = tabelaPrecoRecalculo;
	}

	public BigDecimal getVlFretePesoCotacao() {
		return vlFretePesoCotacao;
	}

	public void setVlFretePesoCotacao(BigDecimal vlFretePesoCotacao) {
		if(vlFretePesoCotacao != null){
			this.vlFretePesoCotacao = vlFretePesoCotacao.setScale(2,
					RoundingMode.HALF_UP);
		}else{
		this.vlFretePesoCotacao = vlFretePesoCotacao;
	}
	
	}

	public BigDecimal getVlEmbutidoParcelas() {
		
		vlEmbutidoParcelas = BigDecimal.ZERO;
		
		if(CollectionUtils.isNotEmpty(getGeneralidades())){
			for(ParcelaServico generalidade : getGeneralidades()){
				if(generalidade.getVlEmbuteParcela() != null){
					vlEmbutidoParcelas = vlEmbutidoParcelas.add(generalidade
							.getVlEmbuteParcela());
				}
			}
		}
		
		return vlEmbutidoParcelas;
	}

	public void setVlEmbutidoParcelas(BigDecimal vlEmbutidoParcelas) {
		if(vlEmbutidoParcelas != null){
			this.vlEmbutidoParcelas = vlEmbutidoParcelas.setScale(2,
					RoundingMode.HALF_UP);
		}else{
		this.vlEmbutidoParcelas = vlEmbutidoParcelas;
	}
	}

	public void addVlEmbutidoParcela(BigDecimal vlEmbutido) {
		this.vlEmbutidoParcelas = getVlEmbutidoParcelas().add(vlEmbutido);
	}

	public BigDecimal getPsReferenciaCalculo() {
		return psReferenciaCalculo;
}

	public void setPsReferenciaCalculo(BigDecimal psReferenciaCalculo) {
		this.psReferenciaCalculo = psReferenciaCalculo;
	}

	public Boolean getBlParametroCotacao() {
		return blParametroCotacao;
	}

	public void setBlParametroCotacao(Boolean blParametroCotacao) {
		this.blParametroCotacao = blParametroCotacao;
	}

	public Boolean getBlEditaCotacao() {
		return blEditaCotacao;
	}

	public void setBlEditaCotacao(Boolean blEditaCotacao) {
		this.blEditaCotacao = blEditaCotacao;
	}

	public Boolean getBlCalculaImpostoServico() {
		return blCalculaImpostoServico;
}

	public void setBlCalculaImpostoServico(Boolean blCalculaImpostoServico) {
		this.blCalculaImpostoServico = blCalculaImpostoServico;
	}

	public Boolean getBlRecalculoFreteSorter() {
		return blRecalculoFreteSorter;
}

	public void setBlRecalculoFreteSorter(Boolean blRecalculoFreteSorter) {
		this.blRecalculoFreteSorter = blRecalculoFreteSorter;
	}
}
