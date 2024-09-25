package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoColetaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoDoctoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoParcelaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ParcelaTabelaCeCCService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.SpringBeanFactory;
import com.mercurio.lms.vendas.model.Cliente;

public abstract class NotaCreditoCalculoStrategy {

    private static final String STRATEGY_NAME_PATTERN = "lms.fretecarreteirocoletaentrega.calculoParcela?Strategy";
    private static final Logger LOGGER = LogManager.getLogger(NotaCreditoCalculoStrategy.class);

    protected final List<NotaCreditoParcela> parcelas = new ArrayList<NotaCreditoParcela>();
    protected final List<DocumentoFrete<?>> documentosFrete = new ArrayList<DocumentoFrete<?>>();

    private SpringBeanFactory notaCreditoStrategyFactory;
    protected NotaCredito notaCredito;
    protected Integer quantidadeColetas;
    protected Integer quantidadeEntregas;
    protected NotaCreditoService notaCreditoService;
    protected NotaCreditoParcelaService notaCreditoParcelaService;
    protected ParcelaTabelaCeCCService parcelaTabelaCeCCService;
    protected DoctoServicoService doctoServicoService;
    protected PedidoColetaService pedidoColetaService;
    protected NotaCreditoDoctoService notaCreditoDoctoService;
    protected NotaCreditoColetaService notaCreditoColetaService;
    protected ParametroGeralService parametroGeralService;
    protected ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    protected List<ParcelaTabelaCeCC> parcelasDiaria = new ArrayList<ParcelaTabelaCeCC>();
    protected List<ParcelaTabelaCeCC> parcelasFretePeso = new ArrayList<ParcelaTabelaCeCC>();
    protected List<ParcelaTabelaCeCC> parcelasQuilometragem = new ArrayList<ParcelaTabelaCeCC>();
    protected List<ParcelaTabelaCeCC> parcelasEvento = new ArrayList<ParcelaTabelaCeCC>();
    protected List<ParcelaTabelaCeCC> parcelasTabelaCeCCGeral = new ArrayList<ParcelaTabelaCeCC>();

	abstract public BigDecimal executeCalculo();
    abstract public DomainValue getTipoCalculo();
	abstract protected List<ParcelaTabelaCe> findParcelasTabelas();
	abstract protected TabelaColetaEntrega getTabelaCliente(Cliente cliente);
	abstract protected void executeCalculoDesconto(BigDecimal pcDescontoUsoEquipamento);
	abstract public BigDecimal findValorTotalNotaCredito(NotaCredito notaCredito);

	public void setup(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
		
		storeNotasCreditoDocumentos(notaCredito.getControleCarga().getIdControleCarga());
        storeNotasCreditoColetas(notaCredito.getControleCarga().getIdControleCarga());
        
        removeNotasCreditoParcelas();

        List<DocumentoFrete<NotaCreditoDocto>> entregas = getNotaCreditoDoctos();
        List<DocumentoFrete<NotaCreditoColeta>> coletas = getNotaCreditoColetas();

        quantidadeColetas = coletas.size();
        quantidadeEntregas = entregas.size();

        documentosFrete.addAll(entregas);
        documentosFrete.addAll(coletas);
	}

	public void generateNotaCredito() {
	    generateParcelasNotaCredito();
	}

    public NotaCredito getNotaCredito() {
        return notaCredito;
    }

    protected void generateParcelasNotaCredito() {
        for (ParcelaTabelaCe parcelaTabela : findParcelasTabelas()) {
        	if (parcelaTabela.getVlDefinido() != null) {
                CalculoParcelaStrategy calculoParcela = generateCalculoParcelaStrategy(parcelaTabela);

                if (calculoParcela != null) {
                    calculoParcela.executeCalculo();
                }
        	}
        }
        storeParcelaTabelaCeCC();
        storeParcelasNotaCredito();
    }

    protected List<NotaCredito> findNotasCreditoControleCarga(Long idControleCarga) {
        return notaCreditoService.findByIdControleCarga(idControleCarga);
    }

	protected void storeParcelasNotaCredito() {
	    List<NotaCreditoParcela> parcelasNotaCredito = new ArrayList<NotaCreditoParcela>();

        for (NotaCreditoParcela parcela : parcelas) {
            if (parcela.getQtNotaCreditoParcela() != null
                    && BigDecimal.ZERO.compareTo(parcela.getQtNotaCreditoParcela()) < 0) {
                if (notaCredito.getFilial().getFranqueado() != null) {
                    parcela.setVlNotaCreditoParcela(new BigDecimal("0.01"));
                }

                parcela.setIdNotaCreditoParcela(notaCreditoParcelaService.store(parcela));
                parcelasNotaCredito.add(parcela);
            }
        }

        notaCredito.setNotaCreditoParcelas(parcelasNotaCredito);
        
        if (notaCredito.getFilial().getFranqueado() == null) {
        	BigDecimal pcDescontoUsoEquipamento = BigDecimalUtils.getBigDecimal(parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_DESCONTO_USO_EQUIPAMENTO, false));
        	if (hasDescontoUsoEquipamento() && pcDescontoUsoEquipamento != null){        		
        		executeCalculoDesconto(pcDescontoUsoEquipamento);
        	}
        }
        	
    }
	
	protected void storeParcelaTabelaCeCC() {
		if(!parcelasTabelaCeCCGeral.isEmpty()){
			for (ParcelaTabelaCeCC parcela : parcelasTabelaCeCCGeral) {
				if (parcela.getQtParcela() != null
						&& BigDecimal.ZERO.compareTo(parcela.getQtParcela()) < 0) {
					if (notaCredito.getFilial().getFranqueado() != null) {
						parcela.setVlParcela(new BigDecimal("0.01"));
					}
					parcelaTabelaCeCCService.store(parcela);
				}
			}
		}
    }

	protected void addNotaCreditoParcela(NotaCreditoParcela parcela) {
	    parcelas.add(parcela);
	}
	
	protected List<NotaCreditoParcela> getNotaCreditoParcela() {
	    return parcelas;
	}
	
	protected List<ParcelaTabelaCeCC> getParcelaTabelaCeCC() {
	    return parcelasFretePeso;
	}
	
	
	protected void validateNotaCreditoParcelaFP(FaixaPeso faixaPeso, ParcelaTabelaCe tabela, BigDecimal quantidade,
            BigDecimal valor) {
		for(NotaCreditoParcela ncp : parcelas){
			if(ncp.getFaixaPesoParcelaTabelaCE().getIdFaixaPesoParcelaTabelaCE().equals(faixaPeso.getFaixa().getIdFaixaPesoParcelaTabelaCE())){
		        ncp.setQtNotaCreditoParcela(ncp.getQtNotaCreditoParcela().add(quantidade));
		        ncp.setVlNotaCreditoParcela(ncp.getVlNotaCreditoParcela().add(valor));
			}
		} 
	}
	

    protected CalculoParcelaStrategy generateCalculoParcelaStrategy(ParcelaTabelaCe parcelaTabela) {
	    String strategyType = getTipoCalculo().getValue() + parcelaTabela.getTpParcela().getValue();
	    String beanName = STRATEGY_NAME_PATTERN.replaceAll("\\?", strategyType.toUpperCase());

	    try {
	        CalculoParcelaStrategy calculo = (CalculoParcelaStrategy) notaCreditoStrategyFactory.getBean(beanName);
	        calculo.setup(this, parcelaTabela);

	        return calculo;
	    } catch (Throwable e) {
	        LOGGER.error(e.getMessage());
            return null;
	    }
	}

    protected void validateTotalNotaCredito(BigDecimal total) {
        notaCreditoService.validateValorTotalNotaCredito(total);
    }

    protected boolean isParcelaMaior(NotaCreditoParcela parcela, NotaCreditoParcela candidate) {
        return getTotalParcela(parcela).compareTo(getTotalParcela(candidate)) > 0;
    }

    protected boolean isParcelaPercentualSobreValor(NotaCreditoParcela parcela) {
        return TipoParcela.PERCENTUAL_SOBRE_VALOR.isEqualTo(parcela.getParcelaTabelaCe().getTpParcela());
    }

    protected boolean isParcelaPercentualSobreFrete(NotaCreditoParcela parcela) {
        return TipoParcela.PERCENTUAL_SOBRE_FRETE.isEqualTo(parcela.getParcelaTabelaCe().getTpParcela());
    }

    protected BigDecimal getTotalParcela(NotaCreditoParcela parcela) {
    	if (parcela == null){
    		return BigDecimal.ZERO;
    	}

        Double totalBruto = parcela.getQtNotaCreditoParcela().multiply(parcela.getVlNotaCreditoParcela()).doubleValue();
        BigDecimal total = new BigDecimal(totalBruto, new MathContext(16));

        return total.setScale(2, RoundingMode.HALF_UP);
    }
    
    protected boolean hasDescontoUsoEquipamento(){
    	if (!notaCredito.getControleCarga().getProprietario().getTpProprietario().equals("P")
    			&& notaCredito.getControleCarga().getMeioTransporteByIdSemiRebocado() != null
    			&& notaCredito.getControleCarga().getMeioTransporteByIdSemiRebocado().getTpVinculo().getValue().equals("P")){
    		return true;
    	}
    	return false;
    }

    protected BigDecimal getValorFinalNotaCredito(BigDecimal total, NotaCredito nota) {
        return total.add(BigDecimalUtils.defaultBigDecimal(nota.getVlAcrescimo()))
        	.subtract(BigDecimalUtils.defaultBigDecimal(nota.getVlDesconto()))
        	.subtract(BigDecimalUtils.defaultBigDecimal(nota.getVlDescUsoEquipamento()));
    }

    protected BigDecimal getMaiorValor(BigDecimal... valores) {
        if (valores != null) {
            List<BigDecimal> compare = new ArrayList<BigDecimal>(valores.length);

            for (BigDecimal valor : valores) {
                if (valor != null && BigDecimalUtils.gtZero(valor)) {
                    compare.add(valor);
                }
            }

            if (!compare.isEmpty()) {
                return Collections.max(compare);
            }
        }

        return BigDecimal.ZERO;
    }

    protected Integer getQuantidadeColetas() {
        return quantidadeColetas;
    }

    protected Integer getQuantidadeEntregas() {
        return quantidadeEntregas;
    }

    protected void addParcelaTabelaDiaria(ParcelaTabelaCeCC parcelaTabela) {
        parcelasDiaria.add(parcelaTabela);
        parcelasTabelaCeCCGeral.add(parcelaTabela);
    }

    protected void addParcelaTabelaQuilometragem(ParcelaTabelaCeCC parcelaTabela) {
        parcelasQuilometragem.add(parcelaTabela);
        parcelasTabelaCeCCGeral.add(parcelaTabela);
    }

    protected void addParcelaTabelaFretePeso(ParcelaTabelaCeCC parcelaTabela) {
        parcelasFretePeso.add(parcelaTabela);
        parcelasTabelaCeCCGeral.add(parcelaTabela);
    }

    protected void addParcelaTabelaEvento(ParcelaTabelaCeCC parcelaTabela) {
        parcelasEvento.add(parcelaTabela);
        parcelasTabelaCeCCGeral.add(parcelaTabela);
    }

    private List<DocumentoFrete<NotaCreditoColeta>> getNotaCreditoColetas() {
        List<DocumentoFrete<NotaCreditoColeta>> documentosFrete = new ArrayList<DocumentoFrete<NotaCreditoColeta>>();

        for (NotaCreditoColeta coleta : notaCreditoColetaService.findByIdNotaCredito(notaCredito.getIdNotaCredito())) {
            DocumentoFrete<NotaCreditoColeta> documentoFrete = new DocumentoFrete<NotaCreditoColeta>();
            documentoFrete.setDsEndereco(ConhecimentoUtils.getEnderecoEntregaReal((coleta.getPedidoColeta().getEnderecoPessoa())));
            documentoFrete.setDocumento(coleta);
            documentoFrete.setPeso(getMaiorValor(
                    coleta.getPedidoColeta().getPsTotalAforadoVerificado(),
                    coleta.getPedidoColeta().getPsTotalVerificado()));

            documentosFrete.add(documentoFrete);
        }

        return documentosFrete;
    }

    private List<DocumentoFrete<NotaCreditoDocto>> getNotaCreditoDoctos() {
        List<DocumentoFrete<NotaCreditoDocto>> documentosFrete = new ArrayList<DocumentoFrete<NotaCreditoDocto>>();

        for (NotaCreditoDocto documento : notaCreditoDoctoService.findByIdNotaCredito(notaCredito.getIdNotaCredito())) {
            DocumentoFrete<NotaCreditoDocto> documentoFrete = new DocumentoFrete<NotaCreditoDocto>();
            documentoFrete.setDsEndereco(documento.getDoctoServico().getDsEnderecoEntregaReal());
            documentoFrete.setDocumento(documento);
            documentoFrete.setPeso(getMaiorValor(
                    documento.getDoctoServico().getPsAferido(),
                    documento.getDoctoServico().getPsAforado(),
                    documento.getDoctoServico().getPsReal()));

            documentosFrete.add(documentoFrete);
        }

        return documentosFrete;
    }
    
    private void removeNotasCreditoParcelas() {
        List<NotaCreditoParcela> parcelasNotaCredito = notaCredito.getNotaCreditoParcelas();
    	
        if(parcelasNotaCredito != null){
        	for(NotaCreditoParcela ncp: parcelasNotaCredito){
        		notaCreditoParcelaService.removeById(ncp.getIdNotaCreditoParcela());
        	}
        }
    }

    private void storeNotasCreditoDocumentos(Long idControleCarga) {
        List<NotaCreditoDocto> documentosNotaCredito = notaCredito.getNotaCreditoDoctos();
    	
        if (documentosNotaCredito!= null){
        	for(NotaCreditoDocto ncd: documentosNotaCredito){
        		notaCreditoDoctoService.removeById(ncd.getIdNotaCreditoDocto());
        	}
        }
    	
        documentosNotaCredito = new ArrayList<NotaCreditoDocto>();
        for (DoctoServico documento : doctoServicoService.findByIdControleCarga(idControleCarga)) {
            NotaCreditoDocto notaCreditoDocto = storeNotaCreditoDocto(documento, idControleCarga);

            if (notaCreditoDocto != null) {
                documentosNotaCredito.add(notaCreditoDocto);
            }
        }

        notaCredito.setNotaCreditoDoctos(documentosNotaCredito);
    }

    private void storeNotasCreditoColetas(Long idControleCarga) {
        List<NotaCreditoColeta> coletasNotaCredito = notaCredito.getNotaCreditoColetas();
        
        if(coletasNotaCredito != null){
        	for(NotaCreditoColeta ncc: coletasNotaCredito){
        		notaCreditoColetaService.removeById(ncc.getIdNotaCreditoColeta());
        	}
        }

    	coletasNotaCredito = new ArrayList<NotaCreditoColeta>();
        for (PedidoColeta coleta : pedidoColetaService.findByIdControleCarga(idControleCarga)) {
            NotaCreditoColeta notaCreditoColeta = storeNotaCreditoColeta(coleta);

            if (coleta != null) {
                coletasNotaCredito.add(notaCreditoColeta);
            }
        }

        notaCredito.setNotaCreditoColetas(coletasNotaCredito);
    }

    private NotaCreditoDocto storeNotaCreditoDocto(DoctoServico documento, Long idControleCarga) {
        if (documento.getNotaCreditoDoctos() == null || documento.getNotaCreditoDoctos().isEmpty()) {
            NotaCreditoDocto notaCreditoDocumento = new NotaCreditoDocto();
            notaCreditoDocumento.setNotaCredito(notaCredito);
            notaCreditoDocumento.setDoctoServico(documento);

            if (hasManifestoEntregaDoctoServico(documento)) {
            	ManifestoEntregaDocumento manifestoEntregaDocumento = manifestoEntregaDocumentoService.findManifestoByIdDoctoServico(documento.getIdDoctoServico(), idControleCarga);
                notaCreditoDocumento.setManifestoEntrega(manifestoEntregaDocumento.getManifestoEntrega());
            }

            notaCreditoDocumento.setTabelaColetaEntrega(getTabelaCliente(getResponsavelFinanceiro(documento)));
            notaCreditoDocumento.setIdNotaCreditoDocto(notaCreditoDoctoService.store(notaCreditoDocumento));

            return notaCreditoDocumento;
        }

        return null;
    }

    private NotaCreditoColeta storeNotaCreditoColeta(PedidoColeta pedidoColeta) {
        if (pedidoColeta.getNotaCreditoColetas() == null || pedidoColeta.getNotaCreditoColetas().isEmpty()) {
            NotaCreditoColeta notaCreditoColeta = new NotaCreditoColeta();
            notaCreditoColeta.setNotaCredito(notaCredito);
            notaCreditoColeta.setPedidoColeta(pedidoColeta);
            notaCreditoColeta.setTabelaColetaEntrega(getTabelaCliente(pedidoColeta.getCliente()));
            notaCreditoColeta.setIdNotaCreditoColeta(notaCreditoColetaService.store(notaCreditoColeta));

            return notaCreditoColeta;
        }

        return null;
    }

    private boolean hasManifestoEntregaDoctoServico(DoctoServico documento) {
        return documento.getManifestoEntregaDocumentos() != null
                && !documento.getManifestoEntregaDocumentos().isEmpty();
    }

    private Cliente getResponsavelFinanceiro(DoctoServico documento) {
        if (documento.getDevedorDocServs() != null && !documento.getDevedorDocServs().isEmpty()) {
            return documento.getDevedorDocServs().get(0).getCliente();
        }

        throw new BusinessException("LMS-25066",
                new Object[]{ "Não existe cliente vinculado ao documento de serviço." });
    }

    public void setNotaCreditoParcelaService(NotaCreditoParcelaService notaCreditoParcelaService) {
        this.notaCreditoParcelaService = notaCreditoParcelaService;
    }
    
	public void setParcelaTabelaCeCCService(ParcelaTabelaCeCCService parcelaTabelaCeCCService) {
		this.parcelaTabelaCeCCService = parcelaTabelaCeCCService;
	}

    public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
        this.notaCreditoService = notaCreditoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public void setNotaCreditoDoctoService(NotaCreditoDoctoService notaCreditoDoctoService) {
        this.notaCreditoDoctoService = notaCreditoDoctoService;
    }

    public void setNotaCreditoColetaService(NotaCreditoColetaService notaCreditoColetaService) {
        this.notaCreditoColetaService = notaCreditoColetaService;
    }

    public void setNotaCreditoStrategyFactory(SpringBeanFactory notaCreditoStrategyFactory) {
        this.notaCreditoStrategyFactory = notaCreditoStrategyFactory;
    }
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setManifestoEntregaDocumentoService(
            ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
    	this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }
    
}
