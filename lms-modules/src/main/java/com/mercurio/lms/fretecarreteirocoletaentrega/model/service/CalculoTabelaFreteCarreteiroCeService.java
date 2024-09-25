package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mercurio.lms.fretecarreteirocoletaentrega.dto.NotaCreditoCalcPadraoDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.SolicitacaoContratacaoService;
import com.mercurio.lms.entrega.model.service.EntregaNotaFiscalService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadrao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadraoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcFaixaPeso;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.CalculoTabelaFreteCarreteiroCeDAO;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.TabelaFreteCarreteiroCeDAO;
import com.mercurio.lms.fretecarreteirocoletaentrega.util.CalculoTabelaFreteCarreteiroCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.util.Chave;
import com.mercurio.lms.fretecarreteirocoletaentrega.util.ParametrosCalculoDiariaNotaCreditoPadrao;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.ConstantesEventosNotaCredito;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Service respons�vel por calculos de tabela de frete carreteiro de
 * coleta/entrega.
 */
public class CalculoTabelaFreteCarreteiroCeService extends CrudService<TabelaFreteCarreteiroCe, Long> {
    private static final String LMS_25117 = "LMS-25117";
    private static final int PRECISAO = 4;
    private static final String NR_NOTA_CREDITO = "NR_NOTA_CREDITO";
    private static final String SIM = "S";
    private static final String ATIVA_PREMIO = "ATIVA_PREMIO";
    private static final String PARAMETRO_TOTAL_HORAS_DIARIA = "TOTAL_HORAS_DIARIA";
    private static final String[] TIPO_OPERACAO_ENTREGA = new String[]{"E", "D"};
    private static final String[] TIPO_OPERACAO_B2C = new String[]{"B", "D"};
    private static final Long HOUR_IN_MILLIS = 3600000L;
    private static final int PORCENTAGEM_DIVISAO = 100;
    private static final double QUANTIDADE = 0.5;
    private static final int INTCOLETA_POS_6 = 6;
    private static final int PRECISAO_MENOR = 2;
    private static final int HORA_EM_MINUTOS = 60;
    private static final int ARR_POS_0 = 0;
    private static final int ARR_POS_2 = 2;
    private static final int ARR_POS_3 = 3;
    private static final int ARR_POS_4 = 4;
    private static final int ARR_POS_5 = 5;
    private static final int ARR_POS_6 = 6;
    private static final int ARR_POS_7 = 7;
    private static final int ARR_POS_8 = 8;
    private static final int VL_CONTEUDO_PARAMETRO_FILIAL_LENGTH = 8;
    private static final int INT_HORAS = 24;
    private static final int MEIO_DIA_HORAS = 12;
    private static final int HORAS_MAXIMA_VIAGEM = 18;
    private static final String TP_PESSOA_FISICA = "F";
    private static final Short CD_OCORRENCIA_ENTREGA_PARCIAL = Short.valueOf("102");
    private static final Short CD_OCORRENCIA_ENTREGA_REALIZADA = Short.valueOf("1");
    private static final int PAGINAR_IN = 250;
    private static final int PAGINAR_ACIMA = 999;

    private ConfiguracoesFacade configuracoesFacade;

    private MeioTransporteService meioTransporteService;

    private DoctoServicoService doctoServicoService;
    private MoedaPaisService moedaPaisService;
    private NotaCreditoPadraoService notaCreditoPadraoService;

    private CalculoTabelaFreteCarreteiroCeDAO calculoTabelaFreteCarreteiroCeDAO;
    private ManifestoEntregaService manifestoEntregaService;

    private TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService;
    private ControleCargaService controleCargaService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private PedidoColetaService pedidoColetaService;
    private EnderecoPessoaService enderecoPessoaService;
    private ParametroGeralService parametroGeralService;
    private EventoNotaCreditoService eventoNotaCreditoService;
    private SolicitacaoContratacaoService solicitacaoContratacaoService;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private EntregaNotaFiscalService entregaNotaFiscalService;

    private FilialService filialService;
    private PessoaService pessoaService;


    public NotaCredito executar(ControleCarga cc, NotaCredito nc) {
        NotaCredito notaCredito = null;

        ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao = startCalculo();

        if (nc.getNotaCreditoDoctoItens() != null && !nc.getNotaCreditoDoctoItens().isEmpty()) {
            notaCredito = iniciarReferencias(nc);
        } else {
            notaCredito = nc;
        }
        notaCredito.setVlTotal(BigDecimal.ZERO);
        notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() != null ? notaCredito.getObNotaCredito() : "");

        Long idControleCarga = cc.getIdControleCarga();
        CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();

        if (cc.getDhSaidaColetaEntrega() == null) {
            return notaCredito;
        }

        notaCredito.setControleCarga(cc);

        List<TabelaFcValores> tabelasEntrega = tabelaFreteCarreteiroCeService.findByIdFilial(cc.getFilialByIdFilialOrigem().getIdFilial(), TIPO_OPERACAO_ENTREGA);
        List<TabelaFcValores> tabelasB2C = tabelaFreteCarreteiroCeService.findByIdFilial(cc.getFilialByIdFilialOrigem().getIdFilial(), TIPO_OPERACAO_B2C);
        
        List<Object[]> documentos = calculoTabelaFreteCarreteiroCeDAO.findParametrosDocumentos(idControleCarga);
        List<Object[]> documentosFiltrados = filtrarDocEntregaParcialSemOcorrenciaEntrega(documentos, idControleCarga);
        List<Object[]> documentosFiltradosNaoEntregues =  (List<Object[]>) CollectionUtils.subtract(documentos, documentosFiltrados);

        List<DoctoServico> doctosControleCarga = doctoServicoService.findByIdControleCarga(idControleCarga);
        List<Long> documentosNaoEntregues = calculoTabelaFreteCarreteiroCeDAO.findConhecimentoNaoEntrgues(cc.getIdControleCarga());
        List<DoctoServico> docsNaoEntregues = new ArrayList<DoctoServico>();

        for (Long documento : documentosNaoEntregues) {
            docsNaoEntregues.add(doctoServicoService.findById(documento));
        }
  
        for (Object[] documento : documentosFiltradosNaoEntregues) {
            docsNaoEntregues.add(doctoServicoService.findById((Long) documento[ARR_POS_0]));
        }
        
        setNotaCreditoCalcPadraoDocto(docsNaoEntregues, notaCredito, null, false);

        Map<Long, List<DoctoServico>> agrupador = new HashMap<Long, List<DoctoServico>>();
        Map<Long, TabelaFcValores> tabelasCalculo = new HashMap<Long, TabelaFcValores>();

        for (Object[] ids : documentosFiltrados) {

            Long idRota = cc.getRotaColetaEntrega().getIdRotaColetaEntrega();
            Long idDoctoServico = (Long) ids[ARR_POS_0];
            Long idProprietario = (Long) ids[ARR_POS_2];
            Long idClienteDestinatario = (Long) ids[ARR_POS_3];
            Long idCliente = (Long) ids[ARR_POS_4];
            Long idMunicipio = (Long) ids[ARR_POS_5];
            Long idVeiculo = (Long) ids[ARR_POS_6];
            Long idMunicpioPessoa = (Long) ids[ARR_POS_7];

            if (idMunicipio == null) {
                idMunicipio = idMunicpioPessoa;
            }
            
            TabelaFcValores tabela = null;

            List<Chave> variacoes = calculoTabelaFreteCarreteiroCe.getEstrategia(new Long[]{idRota, idProprietario, idCliente, idClienteDestinatario, idMunicipio, idVeiculo});

        	if (isPessoaFisica(idClienteDestinatario) && CollectionUtils.isNotEmpty(tabelasB2C)) {
        		tabela = getTabelaPreferencial(cc, tabelasB2C);
        		
        		if (tabela == null) {
        			tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(variacoes, tabelasB2C);
        		
	        		if (tabela == null) {
	        			tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(variacoes, tabelasEntrega);
	        		}
        		}
        		
        	} else {
        		tabela = getTabelaPreferencial(cc, tabelasEntrega);
        		if (tabela == null) {
        			tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(variacoes, tabelasEntrega);
        		}
        	}
            
            if (tabela == null) {
            	throw new BusinessException(LMS_25117);
            }

            tabelasCalculo.put(tabela.getIdTabelaFcValores(), tabela);

            if (!agrupador.containsKey(tabela.getIdTabelaFcValores())) {
                agrupador.put(tabela.getIdTabelaFcValores(), new ArrayList<DoctoServico>());
            }

            DoctoServico doc = getDoctoServico(idDoctoServico, doctosControleCarga);

            if (doc != null) {
                agrupador.get(tabela.getIdTabelaFcValores()).add(doc);
            }

        }

        boolean isDescontaFrete = false;
        
        if (!hasManifestoEntregaParceira(cc)) {
            notaCredito.setTpNotaCredito(new DomainValue("E"));
        } else {
            notaCredito.setTpNotaCredito(new DomainValue("EP"));
        }
        

        for (Long idTabela : agrupador.keySet()) {

            TabelaFcValores tabela = tabelasCalculo.get(idTabela);

            DomainValue domain = tabela.getTabelaFreteCarreteiroCe().getBlDescontaFrete();

            if ("S".equalsIgnoreCase(domain.getValue())) {
                isDescontaFrete = true;
            }

            List<DoctoServico> docs = agrupador.get(idTabela);
            setNotaCreditoCalcPadraoDocto(docs, notaCredito, tabela, true);

            BigDecimal qtdDcos = getQdtDocs(docs, parcelaNotaCreditoCalcPadrao);
            BigDecimal qtdEventos = getQtdEventos(docs, parcelaNotaCreditoCalcPadrao);
            BigDecimal qtdVolumes = getQtdVolumes(docs, new ArrayList<Long>(), parcelaNotaCreditoCalcPadrao, notaCredito);

            Map<String, BigDecimal> valores = getValoresTabelaDocumento(tabela, docs, new ArrayList<Long>(), notaCredito);
            
            calculaParcelaDocumento(tabela, qtdDcos, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaEvento(tabela, qtdEventos, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaVolume(tabela, qtdVolumes, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaCapataziaCliente(tabela, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaValorMercadoria(tabela, valores.get("valorMercadoria"), notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaFreteBruto(tabela, docs, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaFreteLiquido(tabela, docs, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaFaixaPeso
                (tabela, docs, notaCredito, false, new ArrayList<Long>(), parcelaNotaCreditoCalcPadrao, qtdVolumes);
            guardaTabelaCalculoPorControleCarga(tabela, parcelaNotaCreditoCalcPadrao);
        }

        if (!documentosFiltrados.isEmpty()) {
            calculaPorControleCarga(cc, notaCredito, parcelaNotaCreditoCalcPadrao);
            aplicaCalculoParcelas(notaCredito, isDescontaFrete, parcelaNotaCreditoCalcPadrao);
        }

        
        if (nc.getNotaCreditoDoctoItens() != null && !nc.getNotaCreditoDoctoItens().isEmpty()) {
            getTabelaFreteCarreteiroCeDAO().getAdsmHibernateTemplate().evict(nc);
        }


        notaCreditoPadraoService.storeCalculoNotaCredito(notaCredito);

        eventoNotaCreditoService.storeEventoNotaCredito(notaCredito, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_GERADO);

        return notaCredito;

    }

	private List<Object[]> filtrarDocEntregaParcialSemOcorrenciaEntrega(List<Object[]> documentos, Long idControleCarga) {
		List<Object[]> documentosFiltrados = new ArrayList<Object[]>();
        
        for (Object[] ids : documentos) {
        	Long idDoctoServico = (Long) ids[ARR_POS_0];        	
        	Short cdOcorrencia = (Short) ids[ARR_POS_8];
        	
        	Boolean isEntregaParcialFinalizada = false;
        	
        	if(CD_OCORRENCIA_ENTREGA_REALIZADA.equals(cdOcorrencia)){
        	    isEntregaParcialFinalizada = eventoDocumentoServicoService.existsEventoFinalizacaoEntregaParcialByIdDoctoServico(idDoctoServico);
        	}
        	
        	if(CD_OCORRENCIA_ENTREGA_PARCIAL.equals(cdOcorrencia) || isEntregaParcialFinalizada){
        		List<NotaFiscalConhecimento> nfcList = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoSemOcorrenciaEntrega(idDoctoServico, idControleCarga);
    			
        		if(CollectionUtils.isEmpty(nfcList)){
        		    documentosFiltrados.add(ids);
        		}
        		
        	}else{
        	    documentosFiltrados.add(ids);
        	}
        }
        
        return documentosFiltrados;
	}

    /**
     * @param cc
     * @param tabelas
     * @return
     */
    private TabelaFcValores getTabelaPreferencial(ControleCarga cc, List<TabelaFcValores> tabelas) {
    	String vinculo = getVinculo(cc);

		/*Procurando tabelas especificas*/
        TabelaFcValores tabelaSelecionada = null;
        for (TabelaFcValores tabelaFcValores : tabelas) {
            if (new DomainValue("D").equals(tabelaFcValores.getTabelaFreteCarreteiroCe().getTpOperacao()) && tabelaFcValores.getRotaColetaEntrega() != null && cc.getRotaColetaEntrega().equals(tabelaFcValores.getRotaColetaEntrega())) {
                tabelaSelecionada = tabelaFcValores;
                break;

            }
            if (tabelaFcValores.getMeioTransporte() != null && tabelaFcValores.getMeioTransporte().equals(cc.getMeioTransporteByIdTransportado())) {
                if (tabelaSelecionada == null) {
                    tabelaSelecionada = tabelaFcValores;
                } else if (vinculo.equals(tabelaFcValores.getTabelaFreteCarreteiroCe().getTpVinculo().getValue())) {
                    tabelaSelecionada = tabelaFcValores;
                }

            }
        }
        return tabelaSelecionada;
    }
    
	private boolean isPessoaFisica(Long idClienteDestinatario) {
		return idClienteDestinatario != null && pessoaService.validateTipoPessoa(idClienteDestinatario, TP_PESSOA_FISICA);
	}
    
    private String getVinculo(ControleCarga cc) {
        List<Object[]> solicitacoes = solicitacaoContratacaoService.findUltimaSolicitacaoValida(cc.getFilialByIdFilialOrigem().getIdFilial(), cc.getMeioTransporteByIdTransportado().getNrIdentificador(), cc.getDhSaidaColetaEntrega());
        if (solicitacoes.isEmpty()) {
            return "A";
        }
        return String.valueOf(solicitacoes.get(0));
    }

    private ParcelaNotaCreditoCalcPadrao startCalculo() {
        ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao = new ParcelaNotaCreditoCalcPadrao();
        parcelaNotaCreditoCalcPadrao.parcelas = new ArrayList<NotaCreditoCalcPadrao>();
        parcelaNotaCreditoCalcPadrao.parcelasPremio = new ArrayList<NotaCreditoCalcPadrao>();

        parcelaNotaCreditoCalcPadrao.tabelaKmExcedente = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaDiaria = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaHora = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPreDiaria = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaDedicado = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPernoite = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaLocacaoCarreta = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaTransferencia = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaValorAjudante = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioCTE = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioEvento = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioDiaria = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioVolume = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioSaida = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioFreteBruto = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioFreteLiquido = new TabelaFcValores();
        parcelaNotaCreditoCalcPadrao.tabelaPremioMercadoria = new TabelaFcValores();

        parcelaNotaCreditoCalcPadrao.qtdDocsControleCarga = BigDecimal.ZERO;
        parcelaNotaCreditoCalcPadrao.qtdEventosControleCarga = BigDecimal.ZERO;
        parcelaNotaCreditoCalcPadrao.qtdVolumeControlecarga = BigDecimal.ZERO;

        return parcelaNotaCreditoCalcPadrao;
    }

    private boolean hasManifestoEntregaParceira(ControleCarga controleCarga) {
        return manifestoEntregaService.isManifestoEntregaParceira(controleCarga);
    }

    public NotaCredito executeNotaColeta(Long idControleCarga) {
        ControleCarga cc = controleCargaService.findById(idControleCarga);

        if (cc.getDhSaidaColetaEntrega() == null) {
            return new NotaCredito();
        }

        NotaCredito nc = getNotaCreditoColeta(idControleCarga);
        NotaCredito notaColeta = null;

        if (nc.getNotaCreditoDoctoItens() != null && !nc.getNotaCreditoDoctoItens().isEmpty()) {
            notaColeta = iniciarReferencias(nc);
        } else {
            notaColeta = nc;
        }

        if (nc.getIdNotaCredito() == null) {
            notaColeta.setNrNotaCredito(configuracoesFacade.incrementaParametroSequencial(cc.getFilialByIdFilialOrigem().getIdFilial(), NR_NOTA_CREDITO, true));
            notaColeta.setFilial(cc.getFilialByIdFilialOrigem());
            notaColeta.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
            EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(cc.getFilialByIdFilialOrigem().getIdFilial());
            MoedaPais moedaPais = moedaPaisService.findByPaisAndMoeda(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getIdPais(), cc.getFilialByIdFilialOrigem().getMoeda().getIdMoeda());
            notaColeta.setMoedaPais(moedaPais);
            notaColeta.setControleCarga(cc);

        } else {
            notaCreditoPadraoService.removeListaNotaCredito(nc);
            getTabelaFreteCarreteiroCeDAO().getAdsmHibernateTemplate().evict(nc);
        }


        String[] tipoOperacao = new String[]{"C", "CE", "D"};

        List<TabelaFcValores> tabelas = tabelaFreteCarreteiroCeService.findByIdFilial(cc.getFilialByIdFilialOrigem().getIdFilial(), tipoOperacao);

        List<Object[]> coletas = calculoTabelaFreteCarreteiroCeDAO.findParametrosColetas(idControleCarga);

        notaColeta.setVlTotal(BigDecimal.ZERO);
        notaColeta.setObNotaCredito(notaColeta.getObNotaCredito() != null ? notaColeta.getObNotaCredito() : "");

        if (!hasManifestoEntregaParceira(cc)) {
            notaColeta.setTpNotaCredito(new DomainValue("C"));
        } else {
            notaColeta.setTpNotaCredito(new DomainValue("CP"));
        }

        NotaCredito notaRetorno = gerarNotaColeta(notaColeta, coletas, tabelas, cc);

        notaCreditoPadraoService.storeCalculoNotaCredito(notaRetorno);

        eventoNotaCreditoService.storeEventoNotaCredito(notaRetorno, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_GERADO);

        return nc;
    }


    /**
     * @param controleCarga
     * @return
     */
    private NotaCredito getNotaCreditoColeta(Long controleCarga) {
        List<NotaCredito> notasCredito = notaCreditoPadraoService.findByIdControleCarga(controleCarga);

        if (notasCredito != null && !notasCredito.isEmpty()) {
            for (NotaCredito notaCredito : notasCredito) {
                if (notaCredito.getTpNotaCredito() != null && "C".equals(notaCredito.getTpNotaCredito().getValue()) || "CP".equals(notaCredito.getTpNotaCredito().getValue())) {
                    return notaCredito;
                }
            }

        }
        return new NotaCredito();
    }


    private NotaCredito gerarNotaColeta(NotaCredito nc, List<Object[]> coletas, List<TabelaFcValores> tabelas, ControleCarga cc) {
        NotaCredito notaColeta = null;
        ControleCarga controleCarga = cc;

        ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao = startCalculo();

        notaColeta = nc;

        Map<Long, List<PedidoColeta>> agrupador = new HashMap<Long, List<PedidoColeta>>();
        Map<Long, TabelaFcValores> tabelasCalculo = new HashMap<Long, TabelaFcValores>();

        Set<Long> idPedidos = new HashSet<Long>();
        List<Long> idsDoctoServico = new ArrayList<Long>();
        List<Long> complementoId = new ArrayList<Long>();

        for (Object[] ids : coletas) {
            if (controleCarga == null) {
                controleCarga = controleCargaService.findById((Long) ids[INTCOLETA_POS_6]);
            }
            idPedidos.add((Long) ids[ARR_POS_0]);

            PedidoColeta pedido = pedidoColetaService.findById((Long) ids[ARR_POS_0]);

            Long idRota = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
            Long idProprietario = controleCarga.getProprietario().getIdProprietario();
            Long idCliente = pedido.getCliente().getIdCliente();
            Long idMunicipio = pedido.getMunicipio().getIdMunicipio();
            if (idMunicipio == null) {
                idMunicipio = pedido.getEnderecoPessoa().getMunicipio().getIdMunicipio();
            }
            Long idVeiculo = (Long) ids[ARR_POS_5];
            String tpConhecimento = (String) ids[ARR_POS_7];
            Long idConhecimento = (Long) ids[ARR_POS_8];

            CalculoTabelaFreteCarreteiroCe calculoTabelaFreteCarreteiroCe = new CalculoTabelaFreteCarreteiroCe();
            List<Chave> variacoes = calculoTabelaFreteCarreteiroCe.getEstrategia(new Long[]{idRota, idProprietario, idCliente, null, idMunicipio, idVeiculo});

            TabelaFcValores tabela = getTabelaPreferencial(controleCarga, tabelas);

            if (tabela == null) {
                tabela = calculoTabelaFreteCarreteiroCe.buscaTabela(variacoes, tabelas);
                if (tabela == null) {
                    throw new BusinessException(LMS_25117);
                }
            }

            if (ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(tpConhecimento)) {
                complementoId.add(idConhecimento);
            }


            tabelasCalculo.put(tabela.getIdTabelaFcValores(), tabela);

            if (!agrupador.containsKey(tabela.getIdTabelaFcValores())) {
                agrupador.put(tabela.getIdTabelaFcValores(), new ArrayList<PedidoColeta>());
            }
            if (!agrupador.get(tabela.getIdTabelaFcValores()).contains(pedido)) {
                agrupador.get(tabela.getIdTabelaFcValores()).add(pedido);
            }

            idsDoctoServico.add(idConhecimento);
        }

        List<Long> coletasSemConhecimento = calculoTabelaFreteCarreteiroCeDAO.findColetasSemConhecimento(controleCarga.getIdControleCarga(), new ArrayList<Long>(idPedidos));
        List<PedidoColeta> pedidos = new ArrayList<PedidoColeta>();
        if(coletasSemConhecimento != null && !coletasSemConhecimento.isEmpty()) {
            if(PAGINAR_ACIMA > coletasSemConhecimento.size()){
                pedidos = pedidoColetaService.findPedidoColetaByIds(coletasSemConhecimento);
            }else{
                pedidos = paginaConsulta(coletasSemConhecimento);
            }
        }

        setNotaCreditoCalcPadraoDoctoColeta(pedidos, notaColeta, null, false);

        boolean isDescontaFrete = false;

        for (Long idTabela : agrupador.keySet()) {
            TabelaFcValores tabela = tabelasCalculo.get(idTabela);

            DomainValue domain = tabela.getTabelaFreteCarreteiroCe().getBlDescontaFrete();

            if ("S".equalsIgnoreCase(domain.getValue())) {
                isDescontaFrete = true;
            }

            Set<Long> pedidosId = new HashSet<Long>();
            for (PedidoColeta pedido : agrupador.get(idTabela)) {
                pedidosId.add(pedido.getIdPedidoColeta());
            }
            calculaParcelasColeta(notaColeta, agrupador, new ArrayList<Long>(pedidosId), tabela, complementoId, parcelaNotaCreditoCalcPadrao);
        }

        calculaPorControleCarga(controleCarga, notaColeta, parcelaNotaCreditoCalcPadrao);

        aplicaCalculoParcelas(notaColeta, isDescontaFrete, parcelaNotaCreditoCalcPadrao);

        return notaColeta;
    }

    /**
    * @param coletasSemConhecimento
    * Pagina a consulta IN do sql para nao ultrapassar o valor configurado na constante PAGINAR_IN
    * */
    private List<PedidoColeta> paginaConsulta(List<Long> coletasSemConhecimento){
        List<PedidoColeta> pedidos = new ArrayList<>();
        int fim=PAGINAR_IN;
        List<Long> ids = new ArrayList<Long>();
        for(int i=0;i<coletasSemConhecimento.size();i++){
            if(i<fim) {
                ids.add(coletasSemConhecimento.get(i));
            }else{
                pedidos.addAll(pedidoColetaService.findPedidoColetaByIds(ids));
                ids = new ArrayList<Long>();
                --i;
                if(PAGINAR_IN > (coletasSemConhecimento.size()-fim)){
                   fim = coletasSemConhecimento.size();
                }else {
                    fim += PAGINAR_IN;
                }
            }
        }
        pedidos.addAll(pedidoColetaService.findPedidoColetaByIds(ids));
        return pedidos;
    }

    /**
     * @param notaColeta
     * @param agrupador
     * @param idPedidos
     * @param tabela
     * @param complementoId
     * @param parcelaNotaCreditoCalcPadrao
     */
    private void calculaParcelasColeta(NotaCredito notaColeta, Map<Long, List<PedidoColeta>> agrupador, List<Long> idPedidos, TabelaFcValores tabela, List<Long> complementoId, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        List<PedidoColeta> pedidosA = agrupador.get(tabela.getIdTabelaFcValores());
        BigDecimal qtdDcos = getQdtPedidos(pedidosA, parcelaNotaCreditoCalcPadrao);

        BigDecimal qtdEventos = getQtdEventosPedido(pedidosA, parcelaNotaCreditoCalcPadrao);

        List<DoctoServico> doctos = fidDoctoServicoByPeditoColeta(notaColeta.getControleCarga().getIdControleCarga(), idPedidos);

        setNotaCreditoCalcPadraoDoctoColeta(pedidosA, notaColeta, tabela, true);

        BigDecimal qtdVolumes = getQtdVolumes(doctos, complementoId, parcelaNotaCreditoCalcPadrao, notaColeta);
        Map<String, BigDecimal> valores = getValoresTabelaDocumento(tabela, doctos, complementoId, notaColeta);

        calculaParcelaDocumento(tabela, qtdDcos, notaColeta, parcelaNotaCreditoCalcPadrao);
        calculaParcelaEvento(tabela, qtdEventos, notaColeta, parcelaNotaCreditoCalcPadrao);
        calculaParcelaVolume(tabela, qtdVolumes, notaColeta, parcelaNotaCreditoCalcPadrao);
        calculaParcelaCapataziaCliente(tabela, notaColeta, parcelaNotaCreditoCalcPadrao);
        calculaParcelaValorMercadoria(tabela, valores.get("valorMercadoria"), notaColeta, parcelaNotaCreditoCalcPadrao);
        calculaParcelaFreteBruto(tabela, doctos, notaColeta, parcelaNotaCreditoCalcPadrao);
        calculaParcelaFreteLiquido(tabela, doctos, notaColeta, parcelaNotaCreditoCalcPadrao);
        calculaParcelaFaixaPeso
            (tabela, doctos, notaColeta, true, complementoId, parcelaNotaCreditoCalcPadrao, qtdVolumes);
        guardaTabelaCalculoPorControleCarga(tabela, parcelaNotaCreditoCalcPadrao);
    }


    private List<DoctoServico> fidDoctoServicoByPeditoColeta(Long idControleCarga, List<Long> idPedidoColeta) {
        return doctoServicoService.findDoctosServicoByControleCargaNCPadrao(idControleCarga, idPedidoColeta);
    }


    private NotaCredito iniciarReferencias(NotaCredito nc) {
        Long nr = nc.getNrNotaCredito();
        Filial filial = nc.getFilial();
        Long idNotaCredito = nc.getIdNotaCredito();

        NotaCredito notaCredito = new NotaCredito();
        notaCredito.setNrNotaCredito(nr);
        notaCredito.setFilial(filial);
        notaCredito.setIdNotaCredito(idNotaCredito);
        notaCredito.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
        notaCredito.setMoedaPais(nc.getMoedaPais());
        notaCredito.setTpNotaCredito(nc.getTpNotaCredito());
        notaCredito.setControleCarga(nc.getControleCarga());


        return notaCredito;
    }

    private void calculaParcelaFaixaPeso
    (TabelaFcValores tabela, List<DoctoServico> docs, NotaCredito notaCredito, Boolean coleta,
     List<Long> complementoId, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao, BigDecimal qtdVolumes) {

        List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso = tabela.getListTabelaFcFaixaPeso();

        if (listTabelaFcFaixaPeso == null || listTabelaFcFaixaPeso.isEmpty()) {
            return;
        }

        Map<Long, NotaCreditoCalcPadrao> faixas = new HashMap<>();
        Map<Long, List<NotaCreditoCalcPadrao>> faixaUnica = new HashMap<>();

        for (TabelaFcFaixaPeso faixa : tabela.getListTabelaFcFaixaPeso()) {
            faixas.put(faixa.getIdTabelaFcFaixaPeso(), null);
            faixaUnica.put(faixa.getIdTabelaFcFaixaPeso(), null);
        }

        boolean existCalculoFaixaUnica = listTabelaFcFaixaPeso.stream().anyMatch(tfp -> tfp.getBlCalculoFaixaUnica());
       if (existCalculoFaixaUnica){
           calcularParcelaFretePadraoFaixaUnica(tabela, docs, notaCredito, complementoId, faixaUnica);
           Map<Long, List<NotaCreditoCalcPadrao>> faixaUnicaValid = faixaUnica.entrySet()
                   .stream().filter(fu -> !fu.getValue().isEmpty())
                   .collect(Collectors.toMap(keyMapper-> keyMapper.getKey(), valueMapper -> valueMapper.getValue()));
           geraParcelasFaixaUnica(tabela, faixaUnicaValid, parcelaNotaCreditoCalcPadrao);
       } else {
           calcularParcelaFretePadrao(tabela, docs, notaCredito, coleta, complementoId, faixas);
           geraParcelas(tabela, notaCredito, faixas, parcelaNotaCreditoCalcPadrao);
       }


    }

    private void geraParcelasFaixaUnica(TabelaFcValores tabela, Map<Long, List<NotaCreditoCalcPadrao>> faixas, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        for (TabelaFcFaixaPeso f : tabela.getListTabelaFcFaixaPeso()) {
            if(faixas.containsKey(f.getIdTabelaFcFaixaPeso())) {
                List<NotaCreditoCalcPadrao> notaCreditoCalcPadraoList = faixas.get(f.getIdTabelaFcFaixaPeso());
                notaCreditoCalcPadraoList.stream().forEach(parcela -> addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao));
            }
        }
    }

    private NotaCreditoCalcPadrao gerarNotaCreditoCalcPadrao
    (NotaCredito notaCredito, BigDecimal qtTotal, TabelaFcValores tabela, DomainValue tpValor, BigDecimal valor) {
        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(qtTotal);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(tpValor);
        parcela.setVlValor(valor);
        return parcela;
    }

    private void calcularParcelaFretePadraoFaixaUnica
    (TabelaFcValores tabela, List<DoctoServico> docs, NotaCredito notaCredito,
     List<Long> complementoId, Map<Long, List<NotaCreditoCalcPadrao>> faixaUnica){
        List<NotaCreditoCalcPadraoDto> notaCreditoCalcPadraoDtoList = new ArrayList<>();

        for (DoctoServico doctoServico : docs) {
            if (complementoId.contains(doctoServico.getIdDoctoServico())) {
                continue;
            }

            BigDecimal qtdPeso = getPesoCalculo(tabela.getTabelaFreteCarreteiroCe(), doctoServico, notaCredito);
            double intervalo = qtdPeso.doubleValue();
            TabelaFcFaixaPeso tabelaFcFaixaPeso = tabela.getListTabelaFcFaixaPeso().stream()
                .filter
                    (tfp ->
                            intervalo >= tfp.getPsInicial().doubleValue() && intervalo <= tfp.getPsFinal().doubleValue()
                    ).findAny().orElse(null);

            if (tabelaFcFaixaPeso != null) {
                if(new DomainValue("D").equals(tabelaFcFaixaPeso.getTpFator())){
                    notaCreditoCalcPadraoDtoList = agruparPorFaixaPeso(notaCreditoCalcPadraoDtoList, tabelaFcFaixaPeso.getIdTabelaFcFaixaPeso(),
                                notaCredito, BigDecimal.ONE, tabela, new DomainValue("FPD"), tabelaFcFaixaPeso.getVlValor());
                } else if(new DomainValue("K").equals(tabelaFcFaixaPeso.getTpFator())){
                    notaCreditoCalcPadraoDtoList = agruparPorFaixaPeso(notaCreditoCalcPadraoDtoList, tabelaFcFaixaPeso.getIdTabelaFcFaixaPeso(),
                            notaCredito, qtdPeso, tabela, new DomainValue("FPK"), tabelaFcFaixaPeso.getVlValor());
                } else if(new DomainValue("V").equals(tabelaFcFaixaPeso.getTpFator())){
                    notaCreditoCalcPadraoDtoList = agruparPorFaixaPeso(notaCreditoCalcPadraoDtoList, tabelaFcFaixaPeso.getIdTabelaFcFaixaPeso(),
                            notaCredito, BigDecimal.valueOf(doctoServico.getQtVolumes()), tabela, new DomainValue("FPV"), tabelaFcFaixaPeso.getVlValor());
                }

                if(new DomainValue("D").equals(tabelaFcFaixaPeso.getTpFatorSegundo())){
                    if (tabelaFcFaixaPeso.getVlValorSegundo() != null) {

                        notaCreditoCalcPadraoDtoList = agruparPorFaixaPeso(notaCreditoCalcPadraoDtoList, tabelaFcFaixaPeso.getIdTabelaFcFaixaPeso(),
                                    notaCredito, BigDecimal.ONE, tabela, new DomainValue("FPD"), tabelaFcFaixaPeso.getVlValorSegundo());
                    }
                } else if(new DomainValue("K").equals(tabelaFcFaixaPeso.getTpFatorSegundo())){
                    if (tabelaFcFaixaPeso.getVlValorSegundo() != null) {
                        notaCreditoCalcPadraoDtoList = agruparPorFaixaPeso(notaCreditoCalcPadraoDtoList, tabelaFcFaixaPeso.getIdTabelaFcFaixaPeso(),
                                notaCredito, qtdPeso, tabela, new DomainValue("FPK"), tabelaFcFaixaPeso.getVlValorSegundo());
                    }
                } else if(new DomainValue("V").equals(tabelaFcFaixaPeso.getTpFatorSegundo())) {
                    if(tabelaFcFaixaPeso.getVlValorSegundo() != null) {
                        notaCreditoCalcPadraoDtoList = agruparPorFaixaPeso(notaCreditoCalcPadraoDtoList, tabelaFcFaixaPeso.getIdTabelaFcFaixaPeso(),
                                notaCredito, BigDecimal.valueOf(doctoServico.getQtVolumes()), tabela, new DomainValue("FPV"), tabelaFcFaixaPeso.getVlValorSegundo());
                    }
                }

            }
        }

        List<NotaCreditoCalcPadrao> notaCreditoCalcPadraoList = null;
        for(Long idTabelaFcFaixaPeso : faixaUnica.keySet()) {
            notaCreditoCalcPadraoList = notaCreditoCalcPadraoDtoList.stream()
                .filter(nccp -> nccp.getIdTabelaFcFaixaPeso().equals(idTabelaFcFaixaPeso))
                    .map(nccpDto -> nccpDto.getNotaCreditoCalcPadrao())
                    .collect(Collectors.toList());
            faixaUnica.put(idTabelaFcFaixaPeso, notaCreditoCalcPadraoList);

        }
    }

    private List<NotaCreditoCalcPadraoDto> agruparPorFaixaPeso(List<NotaCreditoCalcPadraoDto> notaCreditoCalcPadraoDtoList,
                                                                    Long idTabelaFcFaixaPeso, NotaCredito notaCredito, BigDecimal qtTotal,
                                                                    TabelaFcValores tabela, DomainValue tpValor, BigDecimal valor){
        boolean gerar = true;
        if(!notaCreditoCalcPadraoDtoList.isEmpty()) {
            for (NotaCreditoCalcPadraoDto nota : notaCreditoCalcPadraoDtoList) {
                if (nota.getIdTabelaFcFaixaPeso().equals(idTabelaFcFaixaPeso) && nota.getNotaCreditoCalcPadrao().getTpValor().equals(tpValor)) {
                    nota.getNotaCreditoCalcPadrao().setQtTotal(nota.getNotaCreditoCalcPadrao().getQtTotal().add(qtTotal));
                    gerar = false;
                }
            }
        }
        if(gerar){
            notaCreditoCalcPadraoDtoList.add(gerarNota(idTabelaFcFaixaPeso, notaCredito, qtTotal, tabela, tpValor, valor));
        }
        return notaCreditoCalcPadraoDtoList;
    }

    private NotaCreditoCalcPadraoDto gerarNota
    (Long idTabelaFcFaixaPeso, NotaCredito notaCredito, BigDecimal qtTotal,
     TabelaFcValores tabela, DomainValue tpValor, BigDecimal valor) {
        return new NotaCreditoCalcPadraoDto
            (idTabelaFcFaixaPeso, gerarNotaCreditoCalcPadrao(notaCredito, qtTotal , tabela,tpValor,valor));

    }

    private void calcularParcelaFretePadrao
        (TabelaFcValores tabela, List<DoctoServico> docs, NotaCredito notaCredito,
         Boolean coleta, List<Long> complementoId, Map<Long, NotaCreditoCalcPadrao> faixas){

        Map<Long, Boolean> coletasContabilizadas = new HashMap<>();
        for (DoctoServico doctoServico : docs) {
            if (complementoId.contains(doctoServico.getIdDoctoServico())) {
                continue;
            }

            boolean doc = true;
            boolean ultima = false;
            BigDecimal qtdPeso = getPesoCalculo(tabela.getTabelaFreteCarreteiroCe(), doctoServico, notaCredito);
            for (TabelaFcFaixaPeso faixa : tabela.getListTabelaFcFaixaPeso()) {
                if (ultima) {
                    break;
                }
                BigDecimal diferenca = faixa.getPsFinal().subtract(faixa.getPsInicial());
                BigDecimal valor = BigDecimal.ZERO;

                if (qtdPeso.subtract(diferenca).compareTo(BigDecimal.ZERO) > 0) {
                    qtdPeso = qtdPeso.subtract(diferenca);
                } else {
                    ultima = true;
                }

                if (doc) {
                    doc = false;
                    if (coleta) {
                        Long idPedidoColeta = doctoServico.getPedidoColeta().getIdPedidoColeta();

                        if (coletasContabilizadas.containsKey(idPedidoColeta)) {
                            continue;
                        } else {
                            coletasContabilizadas.put(idPedidoColeta, true);
                        }
                    }

                    valor = faixa.getVlValor();

                    NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                    parcela.setNotaCredito(notaCredito);
                    parcela.setQtTotal(BigDecimal.ONE);
                    parcela.setTabelaFcValores(tabela);
                    parcela.setTpValor(new DomainValue("FPD"));
                    parcela.setVlValor(valor);

                    unificaParcela(faixas, faixa, parcela);

                } else {
                    if (qtdPeso.compareTo(BigDecimal.ZERO) > 0) {
                        valor = faixa.getVlValor();

                        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                        parcela.setNotaCredito(notaCredito);
                        parcela.setTabelaFcValores(tabela);
                        parcela.setTpValor(new DomainValue("FPK"));
                        parcela.setVlValor(valor);

                        if (ultima) {
                            parcela.setQtTotal(qtdPeso);
                        } else {
                            parcela.setQtTotal(diferenca);
                        }

                        unificaParcela(faixas, faixa, parcela);
                    }
                }
            }
        }
    }

    private BigDecimal getPesoCalculo(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe, DoctoServico doctoServico, NotaCredito notaCredito) {
        boolean isEntregaParcialFinalizada = eventoDocumentoServicoService.existsEventoFinalizacaoEntregaParcialByIdDoctoServico(doctoServico.getIdDoctoServico());
        
    	if (eventoDocumentoServicoService.validateEntregaParcial(doctoServico.getIdDoctoServico()) || isEntregaParcialFinalizada) {
    		return getPesoCalculoEntregaParcial(tabelaFreteCarreteiroCe, doctoServico, notaCredito);
    	}
    	
    	if (new DomainValue("F").equals(tabelaFreteCarreteiroCe.getTpPeso())){
    		return doctoServico.getPsReferenciaCalculo();
    		
    	} else if (new DomainValue("D").equals(tabelaFreteCarreteiroCe.getTpPeso())){
    		return doctoServico.getPsReal();
    		
    	} else if (new DomainValue("C").equals(tabelaFreteCarreteiroCe.getTpPeso())){
    		if (doctoServico.getPsAforado() != null && doctoServico.getPsAforado().compareTo(BigDecimal.ZERO) > 0) {
    			return doctoServico.getPsAforado();
    		} else {
    			if (doctoServico.getPsAferido() != null && doctoServico.getPsAferido().compareTo(BigDecimal.ZERO) > 0) {
    				return doctoServico.getPsAferido();
    			} else {
    				return doctoServico.getPsReal();
    			}
    		}
    		
    	} else {
    		if (doctoServico.getPsAferido() != null && doctoServico.getPsAferido().compareTo(BigDecimal.ZERO) > 0) {
    			return doctoServico.getPsAferido();
    		} else {
    			return doctoServico.getPsReal();
    		}
    	}
    	
    }
    
	private BigDecimal getPesoCalculoEntregaParcial(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe, DoctoServico doctoServico, NotaCredito notaCredito) {

		if (new DomainValue("F").equals(tabelaFreteCarreteiroCe.getTpPeso())) {
			return doctoServico.getPsReferenciaCalculo();

		} else if (new DomainValue("D").equals(tabelaFreteCarreteiroCe.getTpPeso())) {
			return calculaDeclarado(doctoServico, notaCredito);

		} else if (new DomainValue("A").equals(tabelaFreteCarreteiroCe.getTpPeso())) {
			return calculaAferido(doctoServico, notaCredito);

		} else if (new DomainValue("C").equals(tabelaFreteCarreteiroCe.getTpPeso())) {
			return calculaCubado(doctoServico, notaCredito);

		} else {
			return calculaTpPesoNull(doctoServico, notaCredito);
		}
	}
    
    private BigDecimal calculaTpPesoNull(DoctoServico doctoServico, NotaCredito notaCredito) {
		List<VolumeNotaFiscal> vnfList = volumeNotaFiscalService.findVolumeNotaFiscalNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
		BigDecimal psCalculo = BigDecimal.ZERO;
		for (VolumeNotaFiscal volumeNotaFiscal : vnfList) {
		    BigDecimal psAferidoVolume = volumeNotaFiscal.getPsAferido() != null ? volumeNotaFiscal.getPsAferido() : BigDecimal.ZERO;
			psCalculo = psCalculo.add(psAferidoVolume);
		}
		
		if (psCalculo.compareTo(BigDecimal.ZERO) > 0) {
			return psCalculo;
		} else {
			List<NotaFiscalConhecimento> nfcList = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
			BigDecimal psMercadoria = BigDecimal.ZERO;
			for (NotaFiscalConhecimento notaFiscalConhecimento : nfcList) {
				psMercadoria = psMercadoria.add(notaFiscalConhecimento.getPsMercadoria());
			}
			if(!BigDecimalUtils.hasValue(psMercadoria)){
                if (doctoServico.getPsAferido() != null && doctoServico.getPsAferido().compareTo(BigDecimal.ZERO) > 0) {
                    return doctoServico.getPsAferido();
                } else {
                    return doctoServico.getPsReal();
                }
            }else{
                return psMercadoria;
            }
		}
		
	}
	
	private BigDecimal calculaAferido(DoctoServico doctoServico, NotaCredito notaCredito) {
		List<VolumeNotaFiscal> vnfList = volumeNotaFiscalService.findVolumeNotaFiscalNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
		BigDecimal psAferido = BigDecimal.ZERO;
		for (VolumeNotaFiscal volumeNotaFiscal : vnfList) {
		    BigDecimal psAferidoVolume = volumeNotaFiscal.getPsAferido() != null ? volumeNotaFiscal.getPsAferido() : BigDecimal.ZERO;
			psAferido = psAferido.add(psAferidoVolume);
		}
		
		if(!BigDecimalUtils.hasValue(psAferido)){
		    if (doctoServico.getPsAferido() != null && doctoServico.getPsAferido().compareTo(BigDecimal.ZERO) > 0) {
		        return doctoServico.getPsAferido();
		    } else {
		        return doctoServico.getPsReal();
		    }
		}
		
		return psAferido;
	}
	
	private BigDecimal calculaCubado(DoctoServico doctoServico, NotaCredito notaCredito) {
		if (doctoServico.getPsAforado() != null && doctoServico.getPsAforado().compareTo(BigDecimal.ZERO) > 0) {
            return doctoServico.getPsAforado();
        } else {
            
            List<VolumeNotaFiscal> vnfList = volumeNotaFiscalService.findVolumeNotaFiscalNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
            BigDecimal psCalculo = BigDecimal.ZERO;
            for (VolumeNotaFiscal volumeNotaFiscal : vnfList) {
                BigDecimal psAferidoVolume = volumeNotaFiscal.getPsAferido() != null ? volumeNotaFiscal.getPsAferido() : BigDecimal.ZERO;
                psCalculo = psCalculo.add(psAferidoVolume);
            }
            
            if (psCalculo.compareTo(BigDecimal.ZERO) > 0) {
                return psCalculo;
            } else {
                List<NotaFiscalConhecimento> nfcList = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
                BigDecimal psMercadoria = BigDecimal.ZERO;
                for (NotaFiscalConhecimento notaFiscalConhecimento : nfcList) {
                    psMercadoria = psMercadoria.add(notaFiscalConhecimento.getPsMercadoria());
                }
                
                if(!BigDecimalUtils.hasValue(psMercadoria)){
                    if (doctoServico.getPsAferido() != null && doctoServico.getPsAferido().compareTo(BigDecimal.ZERO) > 0) {
                        return doctoServico.getPsAferido();
                    } else {
                        return doctoServico.getPsReal();
                    }
                }else{
                    return psMercadoria;
                }
            }
        }
	}
	
	private BigDecimal calculaDeclarado(DoctoServico doctoServico, NotaCredito notaCredito) {
		List<NotaFiscalConhecimento> nfcList = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
		BigDecimal psMercadoria = BigDecimal.ZERO;
		for (NotaFiscalConhecimento notaFiscalConhecimento : nfcList) {
			psMercadoria = psMercadoria.add(notaFiscalConhecimento.getPsMercadoria());
		}
		if(!BigDecimalUtils.hasValue(psMercadoria)){
		    psMercadoria = doctoServico.getPsReal();
		}
		return psMercadoria;
	}

    /**
     * @param faixas
     * @param faixa
     * @param parcela
     */
    private void unificaParcela(Map<Long, NotaCreditoCalcPadrao> faixas, TabelaFcFaixaPeso faixa, NotaCreditoCalcPadrao parcela) {
        NotaCreditoCalcPadrao p = faixas.get(faixa.getIdTabelaFcFaixaPeso());
        if (p == null) {
            faixas.put(faixa.getIdTabelaFcFaixaPeso(), parcela);
        } else {
            parcela.setQtTotal(parcela.getQtTotal().add(p.getQtTotal()));
            faixas.put(faixa.getIdTabelaFcFaixaPeso(), parcela);
        }
    }

    /**
     * @param tabela
     * @param notaCredito
     * @param faixas
     * @param parcelaNotaCreditoCalcPadrao
     */
    private void geraParcelas(TabelaFcValores tabela, NotaCredito notaCredito, Map<Long, NotaCreditoCalcPadrao> faixas, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        for (TabelaFcFaixaPeso f : tabela.getListTabelaFcFaixaPeso()) {
            NotaCreditoCalcPadrao parcela = faixas.get(f.getIdTabelaFcFaixaPeso());
            if (parcela == null) {
                continue;
            }
            addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
        }
    }

    private void setNotaCreditoCalcPadraoDocto(List<DoctoServico> docs, NotaCredito notaCredito, TabelaFcValores tabela, boolean participaCalculo) {
        if (notaCredito.getNotaCreditoDoctoItens() == null) {
            notaCredito.setNotaCreditoDoctoItens(new ArrayList<NotaCreditoCalcPadraoDocto>());
        }

        for (DoctoServico doctoServico : docs) {
            if (doctoServico == null) {
                continue;
            }
            NotaCreditoCalcPadraoDocto docTabela = new NotaCreditoCalcPadraoDocto();
            docTabela.setDoctoServico(doctoServico);
            docTabela.setNotaCredito(notaCredito);
            docTabela.setTabelaFcValores(tabela);
            if (participaCalculo) {
                docTabela.setBlCalculado(new DomainValue("S"));
            } else {
                docTabela.setBlCalculado(new DomainValue("N"));
            }
            notaCredito.getNotaCreditoDoctoItens().add(docTabela);
        }

    }

    private void setNotaCreditoCalcPadraoDoctoColeta(List<PedidoColeta> docs, NotaCredito notaCredito, TabelaFcValores tabela, boolean participaCalculo) {
        if (notaCredito.getNotaCreditoDoctoItens() == null) {
            notaCredito.setNotaCreditoDoctoItens(new ArrayList<NotaCreditoCalcPadraoDocto>());
        }

        for (PedidoColeta pedido : docs) {
            if (pedido == null) {
                continue;
            }
            NotaCreditoCalcPadraoDocto docTabela = new NotaCreditoCalcPadraoDocto();
            docTabela.setPedidoColeta(pedido);
            docTabela.setNotaCredito(notaCredito);
            docTabela.setTabelaFcValores(tabela);
            if (participaCalculo) {
                docTabela.setBlCalculado(new DomainValue("S"));
            } else {
                docTabela.setBlCalculado(new DomainValue("N"));
            }
            notaCredito.getNotaCreditoDoctoItens().add(docTabela);
        }

    }

    public BigDecimal aplicaCalculoParcelas(NotaCredito notaCredito, boolean isDescontaFrete, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        BigDecimal somatorio = BigDecimal.ZERO;

        if (parcelaNotaCreditoCalcPadrao.parcelas == null || parcelaNotaCreditoCalcPadrao.parcelas.isEmpty()) {
            notaCredito.setVlTotal(somatorio);
            notaCredito.setObNotaCredito("Nota de credito n�o possui parcelas");
            return somatorio;
        }
        parcelaNotaCreditoCalcPadrao.parcelas.addAll(parcelaNotaCreditoCalcPadrao.parcelasPremio);

        for (NotaCreditoCalcPadrao parcela : parcelaNotaCreditoCalcPadrao.parcelas) {
            somatorio = somatorio.add(parcela.getVlValor().multiply(parcela.getQtTotal()));
        }

        notaCredito.setNotaCreditoCalculoPadraoItens(parcelaNotaCreditoCalcPadrao.parcelas);
        notaCredito.setVlTotal(somatorio.setScale(PRECISAO_MENOR, RoundingMode.FLOOR));

        if (hasDescontoUsoEquipamento(notaCredito) && isDescontaFrete) {
            BigDecimal pcDescontoUsoEquipamento = BigDecimalUtils.getBigDecimal(parametroGeralService.findConteudoByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_DESCONTO_USO_EQUIPAMENTO, false));
            if (pcDescontoUsoEquipamento != null) {
                notaCredito.setVlDescUsoEquipamento(notaCredito.getVlTotal().multiply(pcDescontoUsoEquipamento.divide(BigDecimal.valueOf(PORCENTAGEM_DIVISAO), PRECISAO, BigDecimal.ROUND_HALF_UP)));
                notaCredito.setVlTotal(notaCredito.getVlTotal().subtract(notaCredito.getVlDescUsoEquipamento()));
            }
        }

        aplicaDeParaCorporativo(notaCredito);

        return notaCredito.getVlTotal();

    }


    /**
     * Criado para manter a compatibilidade com as rotinas do corporativo.
     * Jira LMS-
     *
     * @param notaCredito
     */
    private void aplicaDeParaCorporativo(NotaCredito notaCredito) {
        BigDecimal valorParcelaUnica = notaCredito.getVlTotal();
        NotaCreditoParcela parcela = new NotaCreditoParcela();
        BigDecimal quantidade = BigDecimal.ONE;
        parcela.setNotaCredito(notaCredito);
        parcela.setQtNotaCreditoParcela(quantidade);
        parcela.setVlNotaCreditoParcela(valorParcelaUnica);

        List<NotaCreditoParcela> notaCreditoParcelas = new ArrayList<NotaCreditoParcela>();
        notaCreditoParcelas.add(parcela);
        notaCredito.setNotaCreditoParcelas(notaCreditoParcelas);

    }

    protected boolean hasDescontoUsoEquipamento(NotaCredito notaCredito) {
        return !"P".equals(notaCredito.getControleCarga().getProprietario().getTpProprietario())
                && notaCredito.getControleCarga().getMeioTransporteByIdSemiRebocado() != null
                && "P".equals(notaCredito.getControleCarga().getMeioTransporteByIdSemiRebocado().getTpVinculo().getValue());
    }

    private Map<String, BigDecimal> getValoresTabelaDocumento(TabelaFcValores tabela, List<DoctoServico> docs, List<Long> complementoId, NotaCredito notaCredito) {
        BigDecimal valorMercadoria = BigDecimal.ZERO;
        BigDecimal valorLiquido = BigDecimal.ZERO;
        BigDecimal valorBruto = BigDecimal.ZERO;
        BigDecimal peso = BigDecimal.ZERO;
        boolean isEntregaParcialFinalizada = false;
        
        for (DoctoServico doctoServico : docs) {
            BigDecimal valorMercadoriaDocto = BigDecimal.ZERO;
            BigDecimal pesoDocto = BigDecimal.ZERO;
            
            if (doctoServico == null) {
                continue;
            }

            valorLiquido = valorLiquido.add(doctoServico.getVlLiquido());
            valorBruto = valorBruto.add(doctoServico.getVlTotalDocServico());

            if (complementoId.contains(doctoServico.getIdDoctoServico())) {
                continue;
            }
            pesoDocto = getPesoCalculo(tabela.getTabelaFreteCarreteiroCe(), doctoServico, notaCredito);
            peso = peso.add(pesoDocto);
            
            isEntregaParcialFinalizada = eventoDocumentoServicoService.existsEventoFinalizacaoEntregaParcialByIdDoctoServico(doctoServico.getIdDoctoServico());
            
            if (eventoDocumentoServicoService.validateEntregaParcial(doctoServico.getIdDoctoServico()) || isEntregaParcialFinalizada) {
            	List<NotaFiscalConhecimento> nfcList = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
        		for (NotaFiscalConhecimento notaFiscalConhecimento : nfcList) {
        		    valorMercadoriaDocto = valorMercadoriaDocto.add(notaFiscalConhecimento.getVlTotal());
        		}
        		valorMercadoria = valorMercadoria.add(valorMercadoriaDocto);
            	
            } else {
                valorMercadoriaDocto = valorMercadoriaDocto.add(doctoServico.getVlMercadoria());
                valorMercadoria = valorMercadoria.add(valorMercadoriaDocto);
            }
            
            if ("E".equals(notaCredito.getTpNotaCredito().getValue()) || "EP".equals(notaCredito.getTpNotaCredito().getValue())){
            	for(NotaCreditoCalcPadraoDocto docTabela : notaCredito.getNotaCreditoDoctoItens()){
            		if(doctoServico.getIdDoctoServico().equals(docTabela.getDoctoServico().getIdDoctoServico())){
            			docTabela.setPsCalculado(pesoDocto);
            			docTabela.setVlMercadCalculado(valorMercadoriaDocto);
            		}
            	}
            }
            
        }

        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        result.put("valorMercadoria", valorMercadoria);
        result.put("valorLiquido", valorLiquido);
        result.put("valorBruto", valorBruto);
        result.put("peso", peso);

        return result;

    }

    private void guardaTabelaCalculoPorControleCarga(TabelaFcValores tabela, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
    	
    	if(parcelaNotaCreditoCalcPadrao.tabelaDiaria.getTabelaFreteCarreteiroCe() != null && "B".equals(parcelaNotaCreditoCalcPadrao.tabelaDiaria.getTabelaFreteCarreteiroCe().getTpOperacao().getValue())){
    		if("B".equals(tabela.getTabelaFreteCarreteiroCe().getTpOperacao().getValue())){
    			parcelaNotaCreditoCalcPadrao.tabelaDiaria = getTabelaCalculoCC(tabela, "DIA", parcelaNotaCreditoCalcPadrao.tabelaDiaria, tabela.getVlDiaria(), false);
    			parcelaNotaCreditoCalcPadrao.tabelaPreDiaria = getTabelaCalculoCC(tabela, "PRE", parcelaNotaCreditoCalcPadrao.tabelaPreDiaria, tabela.getVlPreDiaria(), false);
    			parcelaNotaCreditoCalcPadrao.tabelaDedicado = getTabelaCalculoCC(tabela, "DED", parcelaNotaCreditoCalcPadrao.tabelaDedicado, tabela.getVlDedicado(), false);
		 	}
    	} else {
    		parcelaNotaCreditoCalcPadrao.tabelaDiaria = getTabelaCalculoCC(tabela, "DIA", parcelaNotaCreditoCalcPadrao.tabelaDiaria, tabela.getVlDiaria(), false);
    		parcelaNotaCreditoCalcPadrao.tabelaPreDiaria = getTabelaCalculoCC(tabela, "PRE", parcelaNotaCreditoCalcPadrao.tabelaPreDiaria, tabela.getVlPreDiaria(), false);
    		parcelaNotaCreditoCalcPadrao.tabelaDedicado = getTabelaCalculoCC(tabela, "DED", parcelaNotaCreditoCalcPadrao.tabelaDedicado, tabela.getVlDedicado(), false);
    	}
    	
        parcelaNotaCreditoCalcPadrao.tabelaKmExcedente = getTabelaCalculoCC(tabela, "KME", parcelaNotaCreditoCalcPadrao.tabelaKmExcedente, tabela.getVlKmExcedente(), false);
        parcelaNotaCreditoCalcPadrao.tabelaPernoite = getTabelaCalculoCC(tabela, "PNO", parcelaNotaCreditoCalcPadrao.tabelaPernoite, tabela.getVlPernoite(), false);
        parcelaNotaCreditoCalcPadrao.tabelaLocacaoCarreta = getTabelaCalculoCC(tabela, "LOC", parcelaNotaCreditoCalcPadrao.tabelaLocacaoCarreta, tabela.getVlLocacaoCarreta(), false);
        parcelaNotaCreditoCalcPadrao.tabelaTransferencia = getTabelaCalculoCC(tabela, "TRA", parcelaNotaCreditoCalcPadrao.tabelaTransferencia, tabela.getVlTransferencia(), false);
        parcelaNotaCreditoCalcPadrao.tabelaValorAjudante = getTabelaCalculoCC(tabela, "VAJ", parcelaNotaCreditoCalcPadrao.tabelaValorAjudante, tabela.getVlAjudante(), false);
        parcelaNotaCreditoCalcPadrao.tabelaHora = getTabelaCalculoCC(tabela, "HOR", parcelaNotaCreditoCalcPadrao.tabelaHora, tabela.getVlHora(), false);

        parcelaNotaCreditoCalcPadrao.tabelaPremioCTE = getTabelaCalculoCC(tabela, "PCT", parcelaNotaCreditoCalcPadrao.tabelaPremioCTE, tabela.getTabelaFreteCarreteiroCe().getPcPremioCte(), true);
        parcelaNotaCreditoCalcPadrao.tabelaPremioEvento = getTabelaCalculoCC(tabela, "PEV", parcelaNotaCreditoCalcPadrao.tabelaPremioEvento, tabela.getTabelaFreteCarreteiroCe().getPcPremioEvento(), true);
        parcelaNotaCreditoCalcPadrao.tabelaPremioDiaria = getTabelaCalculoCC(tabela, "PDI", parcelaNotaCreditoCalcPadrao.tabelaPremioDiaria, tabela.getTabelaFreteCarreteiroCe().getPcPremioDiaria(), true);
        parcelaNotaCreditoCalcPadrao.tabelaPremioVolume = getTabelaCalculoCC(tabela, "PVO", parcelaNotaCreditoCalcPadrao.tabelaPremioVolume, tabela.getTabelaFreteCarreteiroCe().getPcPremioVolume(), true);
        parcelaNotaCreditoCalcPadrao.tabelaPremioSaida = getTabelaCalculoCC(tabela, "PSA", parcelaNotaCreditoCalcPadrao.tabelaPremioSaida, tabela.getTabelaFreteCarreteiroCe().getPcPremioSaida(), true);
        parcelaNotaCreditoCalcPadrao.tabelaPremioFreteBruto = getTabelaCalculoCC(tabela, "PFB", parcelaNotaCreditoCalcPadrao.tabelaPremioFreteBruto, tabela.getTabelaFreteCarreteiroCe().getPcPremioFreteBruto(), true);
        parcelaNotaCreditoCalcPadrao.tabelaPremioFreteLiquido = getTabelaCalculoCC(tabela, "PFL", parcelaNotaCreditoCalcPadrao.tabelaPremioFreteLiquido, tabela.getTabelaFreteCarreteiroCe().getPcPremioFreteLiq(), true);
        parcelaNotaCreditoCalcPadrao.tabelaPremioMercadoria = getTabelaCalculoCC(tabela, "PME", parcelaNotaCreditoCalcPadrao.tabelaPremioMercadoria, tabela.getTabelaFreteCarreteiroCe().getPcPremioMercadoria(), true);
    }

    /**
     * Se o valor para a tabela especifica j� tiver valor lanca exception
     * <p>
     * Tabelas de Frete Padr�o conflitantes: Par�metro {0} Tabela(s) {1} Detalhamento(s) {2}
     *
     * @param tabela
     * @param domain
     * @param tabelaReferencia
     * @param valor
     * @param premio
     * @return
     */
    private TabelaFcValores getTabelaCalculoCC(TabelaFcValores tabela, String domain, TabelaFcValores tabelaReferencia, BigDecimal valor, boolean premio) {
        if (tabela != null && !BigDecimal.ZERO.equals(valor)) {
            if (tabelaReferencia != null && tabelaReferencia.getIdTabelaFcValores() != null && !tabela.getIdTabelaFcValores().equals(tabelaReferencia.getIdTabelaFcValores())
            		&& tabelaReferencia.getTabelaFreteCarreteiroCe().getTpOperacao().getValue().equals(tabela.getTabelaFreteCarreteiroCe().getTpOperacao().getValue())) {
                DomainValue d = configuracoesFacade.getDomainValue("DM_TP_VALOR_TABELA_PADRAO", domain);

                //se for do tipo premio e a tabela principal � a mesma n�o � erro.
                if (premio && tabelaReferencia.getTabelaFreteCarreteiroCe().equals(tabela.getTabelaFreteCarreteiroCe())) {
                    return tabelaReferencia;
                }

                String texto1 = formataMensagem(tabela.getTabelaFreteCarreteiroCe().getFilial().getSgFilial(),
                        tabela.getTabelaFreteCarreteiroCe().getNrTabelaFreteCarreteiroCe(),
                        tabelaReferencia.getTabelaFreteCarreteiroCe().getFilial().getSgFilial(),
                        tabelaReferencia.getTabelaFreteCarreteiroCe().getNrTabelaFreteCarreteiroCe());


                String texto2 = formataMensagemTipo(tabela.getBlTipo().getDescriptionAsString(), tabelaReferencia.getBlTipo().getDescriptionAsString());

                throw new BusinessException("LMS-25118", new Object[]{d.getDescriptionAsString(), texto1, texto2});
            } else {
                return tabela;
            }
        }
        return tabelaReferencia;
    }
    
    private String formataMensagem(String fililaTabela1, Long numeroTabela1, String fililaTabela2, Long numeroTabela2) {
        return String.format("%s %s - %s %s ", fililaTabela1, numeroTabela1, fililaTabela2, numeroTabela2);
    }

    private String formataMensagemTipo(String tipo1, String tipo2) {
        return String.format("%s - %s  ", tipo1, tipo2);
    }


    private void calculaPorControleCarga(ControleCarga cc, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        List<Object[]> result = calculoTabelaFreteCarreteiroCeDAO.findPagamentoPorControleCarga(cc.getIdControleCarga());

        String notaKmExcedente = getNotaPaga("KME", result, notaCredito.getNrNotaCredito());
        String notaDiaria = getNotaPaga("DIA", result, notaCredito.getNrNotaCredito());
        String notaPreDiaria = getNotaPaga("PRE", result, notaCredito.getNrNotaCredito());
        String notaPernoite = getNotaPaga("PNO", result, notaCredito.getNrNotaCredito());
        String notaTransferencia = getNotaPaga("TRA", result, notaCredito.getNrNotaCredito());
        String notaValorAjudante = getNotaPaga("VAJ", result, notaCredito.getNrNotaCredito());
        String notaPremioDiaria = getNotaPaga("PDI", result, notaCredito.getNrNotaCredito());
        String notaPremioSaida = getNotaPaga("PSA", result, notaCredito.getNrNotaCredito());
        String notaHora = getNotaPaga("HOR", result, notaCredito.getNrNotaCredito());
        String notaDedicado = getNotaPaga("DED", result, notaCredito.getNrNotaCredito());

        calculaParcelaKmExcedente(parcelaNotaCreditoCalcPadrao.tabelaKmExcedente, cc, notaCredito, notaKmExcedente, parcelaNotaCreditoCalcPadrao);
        calculaParcelaDiaria(parcelaNotaCreditoCalcPadrao.tabelaDiaria, notaCredito, notaDiaria, null, parcelaNotaCreditoCalcPadrao);
        calculaParcelaHora(parcelaNotaCreditoCalcPadrao.tabelaHora, cc, notaCredito, notaHora, parcelaNotaCreditoCalcPadrao);
        calculaParcelaPreDiaria(parcelaNotaCreditoCalcPadrao.tabelaPreDiaria, notaCredito, notaPreDiaria, parcelaNotaCreditoCalcPadrao);
        calculaParcelaDedicado(parcelaNotaCreditoCalcPadrao.tabelaDedicado, notaCredito, notaDedicado, parcelaNotaCreditoCalcPadrao);
        calculaParcelaPernoite(parcelaNotaCreditoCalcPadrao.tabelaPernoite, cc, notaCredito, notaPernoite, parcelaNotaCreditoCalcPadrao);
        calculaParcelaTransferencia(parcelaNotaCreditoCalcPadrao.tabelaTransferencia, notaCredito, notaTransferencia, parcelaNotaCreditoCalcPadrao);
        calculaParcelaValorAjudante(parcelaNotaCreditoCalcPadrao.tabelaValorAjudante, notaCredito, notaValorAjudante, parcelaNotaCreditoCalcPadrao);

        //Premio por cc
        if (isPagaPremio(cc)) {
            calculaParcelaPremioCTE(parcelaNotaCreditoCalcPadrao.tabelaPremioCTE, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaPremioEvento(parcelaNotaCreditoCalcPadrao.tabelaPremioEvento, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaPremioDiaria(parcelaNotaCreditoCalcPadrao.tabelaPremioDiaria, notaCredito, notaPremioDiaria, parcelaNotaCreditoCalcPadrao);
            calculaParcelaPremioVolume(parcelaNotaCreditoCalcPadrao.tabelaPremioVolume, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaPremioSaida(parcelaNotaCreditoCalcPadrao.tabelaPremioSaida, notaCredito, notaPremioSaida, parcelaNotaCreditoCalcPadrao);
            calculaParcelaPremioFreteBruto(parcelaNotaCreditoCalcPadrao.tabelaPremioFreteBruto, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaPremioFreteLiquido(parcelaNotaCreditoCalcPadrao.tabelaPremioFreteLiquido, notaCredito, parcelaNotaCreditoCalcPadrao);
            calculaParcelaPremioMercadoria(parcelaNotaCreditoCalcPadrao.tabelaPremioMercadoria, notaCredito, parcelaNotaCreditoCalcPadrao);
        }

        ajustaObservacao(notaCredito, result);

    }

    /**
     * @param notaCredito
     * @param result
     */
    private void ajustaObservacao(NotaCredito notaCredito, List<Object[]> result) {
        if (!result.isEmpty() && notaCredito.getObNotaCredito() != null && notaCredito.getObNotaCredito().length() > PRECISAO_MENOR) {
            notaCredito.setObNotaCredito(notaCredito.getObNotaCredito().substring(0, notaCredito.getObNotaCredito().length() - PRECISAO_MENOR).concat("."));
        }
    }


    private String getNotaPaga(String chave, List<Object[]> result, Long nrNota) {
        for (Object[] strings : result) {
            if (chave.equals(String.valueOf(strings[ARR_POS_0])) && !Long.valueOf(String.valueOf(strings[1])).equals(nrNota)) {
                return String.valueOf(strings[1]);
            }
        }
        return null;
    }

    private boolean isPagaPremio(ControleCarga cc) {
        ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(cc.getFilialByIdFilialOrigem().getIdFilial(), ATIVA_PREMIO, false, true);
        if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
            return true;
        }
        return false;
    }


    private BigDecimal getQtdVolumes(List<DoctoServico> docs, List<Long> complementoId, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao, NotaCredito notaCredito) {
        Integer volumes = 0;
        boolean isEntregaParcialFinalizada = false;
        for (DoctoServico doctoServico : docs) {
            Integer qtVolDoc = 0;
            
            if (doctoServico == null) {
                continue;
            }
            if (complementoId.contains(doctoServico.getIdDoctoServico())) {
                continue;
            }
            
            isEntregaParcialFinalizada = eventoDocumentoServicoService.existsEventoFinalizacaoEntregaParcialByIdDoctoServico(doctoServico.getIdDoctoServico());
            
            if (eventoDocumentoServicoService.validateEntregaParcial(doctoServico.getIdDoctoServico()) || isEntregaParcialFinalizada) {
            	List<NotaFiscalConhecimento> nfcList = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoNotaCreditoPadrao(doctoServico.getIdDoctoServico(), notaCredito.getControleCarga().getIdControleCarga());
        		for (NotaFiscalConhecimento notaFiscalConhecimento : nfcList) {
        		    qtVolDoc += notaFiscalConhecimento.getQtVolumes().intValue();
        		}
        		volumes += qtVolDoc;
            } else {
                qtVolDoc = doctoServico.getQtVolumes();
                volumes += qtVolDoc;
            }
            
            if ("E".equals(notaCredito.getTpNotaCredito().getValue()) || "EP".equals(notaCredito.getTpNotaCredito().getValue())){
            	for(NotaCreditoCalcPadraoDocto docTabela : notaCredito.getNotaCreditoDoctoItens()){
            		if(doctoServico.getIdDoctoServico().equals(docTabela.getDoctoServico().getIdDoctoServico())){
            			BigDecimal qtVolumesDocto = new BigDecimal(qtVolDoc);
            			docTabela.setQtVolumesCalculado(qtVolumesDocto);
            		}
            	}
            }
            
        }

        BigDecimal vol = new BigDecimal(volumes);
        parcelaNotaCreditoCalcPadrao.qtdVolumeControlecarga = parcelaNotaCreditoCalcPadrao.qtdVolumeControlecarga.add(vol);
        return vol;
    }


    private BigDecimal getQtdEventos(List<DoctoServico> docs, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        Set<String> enderecos = new HashSet<String>();
        for (DoctoServico doctoServico : docs) {
            if (doctoServico == null) {
                continue;
            }

            String endereco = doctoServico.getDsEnderecoEntregaReal();
            enderecos.add(endereco);
        }

        BigDecimal qdtEventos = new BigDecimal(enderecos.size());
        parcelaNotaCreditoCalcPadrao.qtdEventosControleCarga = parcelaNotaCreditoCalcPadrao.qtdEventosControleCarga.add(qdtEventos);

        return qdtEventos;
    }

    private BigDecimal getQtdEventosPedido(List<PedidoColeta> docs, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        Set<String> enderecos = new HashSet<String>();
        for (PedidoColeta pedidos : docs) {
            if (pedidos == null) {
                continue;
            }

            String endereco = pedidos.getEnderecoComComplemento();
            enderecos.add(endereco);
        }

        BigDecimal qdtEventos = new BigDecimal(enderecos.size());
        parcelaNotaCreditoCalcPadrao.qtdEventosControleCarga = parcelaNotaCreditoCalcPadrao.qtdEventosControleCarga.add(qdtEventos);

        return qdtEventos;
    }


    private BigDecimal getQdtDocs(List<DoctoServico> docs, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        Integer qtdDcos = docs.size();
        parcelaNotaCreditoCalcPadrao.qtdDocsControleCarga = parcelaNotaCreditoCalcPadrao.qtdDocsControleCarga.add(new BigDecimal(qtdDcos));
        return new BigDecimal(qtdDcos);
    }

    private BigDecimal getQdtPedidos(List<PedidoColeta> docs, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        Integer qtdDcos = docs.size();
        parcelaNotaCreditoCalcPadrao.qtdDocsControleCarga = parcelaNotaCreditoCalcPadrao.qtdDocsControleCarga.add(new BigDecimal(qtdDcos));
        return new BigDecimal(qtdDcos);
    }


    private DoctoServico getDoctoServico(Long idDoctoServico, List<DoctoServico> doctosControleCarga) {
        for (DoctoServico doctoServico : doctosControleCarga) {
            if (doctoServico.getIdDoctoServico().equals(idDoctoServico)) {
                return doctoServico;
            }
        }
        return null;
    }

	/*calculos*/


    /**
     * Cria parcela do tipo documento para entrega docto-servico, para coleta pedido de coleta
     *
     * @param tabela
     * @param quantidade
     * @param notaCredito
     */
    public void calculaParcelaDocumento(TabelaFcValores tabela, BigDecimal quantidade, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (BigDecimal.ZERO.equals(tabela.getVlConhecimento())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(quantidade);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("CON"));
        parcela.setVlValor(tabela.getVlConhecimento());

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaEvento(TabelaFcValores tabela, BigDecimal quantidade, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (BigDecimal.ZERO.equals(tabela.getVlEvento())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(quantidade);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("EVE"));
        parcela.setVlValor(tabela.getVlEvento());

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaVolume(TabelaFcValores tabela, BigDecimal quantidade, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (BigDecimal.ZERO.equals(tabela.getVlVolume())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(quantidade);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("VOL"));
        parcela.setVlValor(tabela.getVlVolume());

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public NotaCreditoCalcPadrao calculaParcelaDiaria(TabelaFcValores tabela, NotaCredito notaCredito, String notaDiaria, ParametrosCalculoDiariaNotaCreditoPadrao parametros, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlDiaria())) {
            return null;
        }

        parametros = getParametrosDiaria(parametros, notaCredito);

        BigDecimal parametro = parametros.getParametro();
        List<ControleCarga> controlesCargaNoPeriodo = parametros.getControlesCargaNoPeriodo();
        Boolean filialPagaDiariaExcedente = parametros.getFilialPagaDiariaExcedente();
        List<Map<String, Object>> notas = parametros.getNotas();

        if (parametros.isDiariaPaga()) {
            return addParcelaDiaria(tabela, notaCredito, notaDiaria, BigDecimal.ZERO, notas, parcelaNotaCreditoCalcPadrao);
        }

        if (!isPagaDiaria(controlesCargaNoPeriodo, parametro)) {
            return addParcelaDiaria(tabela, notaCredito, notaDiaria, new BigDecimal(QUANTIDADE), notas, parcelaNotaCreditoCalcPadrao);
        } else {
            if (BooleanUtils.isFalse(filialPagaDiariaExcedente)) {
                return addParcelaDiaria(tabela, notaCredito, notaDiaria, BigDecimal.ONE, notas, parcelaNotaCreditoCalcPadrao);
            }
            return addParcelaDiariaExcedente(tabela, notaCredito, notaDiaria, controlesCargaNoPeriodo, notas, parcelaNotaCreditoCalcPadrao);
        }
    }

    private ParametrosCalculoDiariaNotaCreditoPadrao getParametrosDiaria(ParametrosCalculoDiariaNotaCreditoPadrao parametros, NotaCredito notaCredito) {
        if (parametros == null) {
            parametros = new ParametrosCalculoDiariaNotaCreditoPadrao();
            parametros.setControlesCargaNoPeriodo(controleCargaService.findControlesCargaProprietarioNoPeriodo(notaCredito.getControleCarga()));
            parametros.setFilialPagaDiariaExcedente(filialService.findById(notaCredito.getFilial().getIdFilial()).getBlPagaDiariaExcedente());
            parametros.setIdFilial(notaCredito.getFilial().getIdFilial());
            parametros.setNotas(calculoTabelaFreteCarreteiroCeDAO.findPagamentoDiariaControleCargas(notaCredito.getControleCarga().getIdControleCarga()));
            parametros.setDiariaPaga(calculoTabelaFreteCarreteiroCeDAO.findExistePagamentoDiaria(notaCredito.getControleCarga().getIdControleCarga()));
            parametros.setParametro((BigDecimal) parametroGeralService.findConteudoByNomeParametro(PARAMETRO_TOTAL_HORAS_DIARIA, false));
        }
        return parametros;
    }

    /**
     * @param tabela
     * @param notaCredito
     * @param notaDiaria
     * @param controlesCargaNoPeriodo
     * @param notas
     * @param parcelaNotaCreditoCalcPadrao
     * @return
     */
    private NotaCreditoCalcPadrao addParcelaDiariaExcedente(TabelaFcValores tabela, NotaCredito notaCredito, String notaDiaria, List<ControleCarga> controlesCargaNoPeriodo, List<Map<String, Object>> notas, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        BigDecimal horasViagem = new BigDecimal(getTotalHoras(controlesCargaNoPeriodo)).setScale(PRECISAO_MENOR, RoundingMode.HALF_UP);

        BigDecimal numeroDiarias = getNumeroDiarias(horasViagem);

		/*fim diaria*/

		/*inicio excedente*/
        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("DIA"));
        parcela.setVlValor(tabela.getVlDiaria());

        if (horasViagem.compareTo(new BigDecimal(HORAS_MAXIMA_VIAGEM)) >= 0) {
            horasViagem = horasViagem.subtract(new BigDecimal(MEIO_DIA_HORAS));
            numeroDiarias = BigDecimal.ONE.add(getNumeroDiarias(horasViagem).multiply(new BigDecimal(QUANTIDADE)));
        }
        BigDecimal totalDiaria = BigDecimal.ZERO;
        for (Map<String, Object> n : notas) {
            if (notaDiaria == null) {
                notaDiaria = (String) n.get("nr_nota_credito");
            }
            totalDiaria = totalDiaria.add((BigDecimal) n.get("qt_total"));
        }


        if (numeroDiarias.compareTo(BigDecimal.ZERO) > -1) {

            BigDecimal num = numeroDiarias.subtract(totalDiaria);
            if (num.compareTo(BigDecimal.ZERO) > -1) {
                parcela.setQtTotal(num);
            } else {
                parcela.setQtTotal(BigDecimal.ZERO);
            }

        } else {
            parcela.setQtTotal(numeroDiarias);
        }

        setObservacao(notaCredito, notaDiaria);


        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
        return parcela;
    }

    /**
     * @param horasViagem
     * @return
     */
    private BigDecimal getNumeroDiarias(BigDecimal horasViagem) {
        BigDecimal numeroDiarias = BigDecimal.ZERO;
        Integer numDiarias = horasViagem.divide(new BigDecimal(INTCOLETA_POS_6), PRECISAO_MENOR, RoundingMode.HALF_UP).intValue();

        if (numDiarias < 1) {
            numeroDiarias = new BigDecimal(QUANTIDADE);
        }
        if (numDiarias >= 1) {
            numeroDiarias = new BigDecimal(numDiarias);
        }
        return numeroDiarias;
    }

    /**
     * @param notaCredito
     * @param notaDiaria
     */
    private void setObservacao(NotaCredito notaCredito, String notaDiaria) {
        if (notaDiaria != null) {
            notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() == null ? "" : notaCredito.getObNotaCredito() + " Di�ria pago n� " + notaDiaria + ", ");
        }
    }


    /**
     * @param tabela
     * @param notaCredito
     * @param notaDiaria
     * @param qtd
     * @param notas
     * @param parcelaNotaCreditoCalcPadrao
     * @return
     */
    private NotaCreditoCalcPadrao addParcelaDiaria(TabelaFcValores tabela, NotaCredito notaCredito, String notaDiaria, BigDecimal qtd, List<Map<String, Object>> notas, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(qtd);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("DIA"));
        parcela.setVlValor(tabela.getVlDiaria());


        BigDecimal totalDiaria = BigDecimal.ZERO;

        for (Map<String, Object> n : notas) {
            if (notaDiaria == null) {
                notaDiaria = (String) n.get("nr_nota_credito");
            }
            totalDiaria = totalDiaria.add((BigDecimal) n.get("qt_total"));
        }

        if (qtd.compareTo(BigDecimal.ZERO) == 0) {
            parcela.setQtTotal(BigDecimal.ZERO);
        } else {
            if (totalDiaria.compareTo(BigDecimal.ONE) > -1) {
                parcela.setQtTotal(BigDecimal.ZERO);
            } else if (totalDiaria.compareTo(new BigDecimal(QUANTIDADE)) == 0) {
                parcela.setQtTotal(BigDecimal.ZERO);
            }
        }

        setObservacao(notaCredito, notaDiaria);

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
        return parcela;
    }


    public void calculaParcelaKmExcedente(TabelaFcValores tabela, ControleCarga cc, NotaCredito notaCredito, String notaKmExcedente, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlKmExcedente())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        BigDecimal totalKM = BigDecimal.valueOf(controleCargaService.findQuilometrosPercorridosControleCarga(cc));
        Integer kmFranquia = IntegerUtils.hasValue(cc.getFilialByIdFilialOrigem().getNrFranquiaKm()) ? cc.getFilialByIdFilialOrigem().getNrFranquiaKm() : IntegerUtils.ZERO;

        BigDecimal kmRotaCorreto = new BigDecimal(kmFranquia);

        if (totalKM.compareTo(kmRotaCorreto) > 0) {
            BigDecimal pagar = totalKM.subtract(kmRotaCorreto);
            parcela.setQtTotal(pagar);
            parcela.setTabelaFcValores(tabela);
            parcela.setTpValor(new DomainValue("KME"));
            if (notaKmExcedente != null) {
                parcela.setVlValor(BigDecimal.ZERO);
                notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Km excedente pago n� " + notaKmExcedente + ", ");
            } else {
                parcela.setVlValor(tabela.getVlKmExcedente());
            }

            addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
        }
    }


    public void calculaParcelaHora(TabelaFcValores tabela, ControleCarga cc, NotaCredito notaCredito, String notaHora, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlHora())) {
            return;
        }

        String[] diferenca = JTDateTimeUtils.calculaDiferencaEmHoras(cc.getDhSaidaColetaEntrega(), cc.getDhChegadaColetaEntrega()).split("h");
        BigDecimal minutos = new BigDecimal(diferenca[1].replace("min", "").trim());

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(new BigDecimal(diferenca[ARR_POS_0]).add(minutos.divide(new BigDecimal(HORA_EM_MINUTOS), PRECISAO_MENOR, RoundingMode.HALF_EVEN)));
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("HOR"));
        if (notaHora != null) {
            parcela.setVlValor(BigDecimal.ZERO);
            notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Hora pago n� " + notaHora + ", ");
        } else {
            parcela.setVlValor(tabela.getVlHora());
        }


        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaPreDiaria(TabelaFcValores tabela, NotaCredito notaCredito, String notaPreDiaria, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlPreDiaria())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(BigDecimal.ONE);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("PRE"));

        if (notaPreDiaria != null) {
            parcela.setVlValor(BigDecimal.ZERO);
            notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Sa�da pago n� " + notaPreDiaria + ", ");
        } else {
            parcela.setVlValor(tabela.getVlPreDiaria());
        }

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaDedicado(TabelaFcValores tabela, NotaCredito notaCredito, String notaDedicado, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlDedicado())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(BigDecimal.ONE);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("DED"));

        if (notaDedicado != null) {
            parcela.setVlValor(BigDecimal.ZERO);
            notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Dedicado pago n� " + notaDedicado + ", ");
        } else {
            parcela.setVlValor(tabela.getVlDedicado());
        }

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaPernoite(TabelaFcValores tabela, ControleCarga cc, NotaCredito notaCredito, String notaPernoite, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlPernoite())) {
            return;
        }
        boolean paga = pagaPernoite(cc);
        if (paga) {

            NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
            parcela.setNotaCredito(notaCredito);
            parcela.setQtTotal(BigDecimal.ONE);
            parcela.setTabelaFcValores(tabela);
            parcela.setTpValor(new DomainValue("PNO"));
            if (notaPernoite != null) {
                parcela.setVlValor(BigDecimal.ZERO);
                notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Pernoite pago n� " + notaPernoite + ", ");
            } else {
                parcela.setVlValor(tabela.getVlPernoite());
            }
            addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
        }
    }

    private boolean pagaPernoite(ControleCarga cc) {
        String parametroInicio = "HR_PERNOITE_INI";
        String parametroFim = "HR_PERNOITE_FIM";

        boolean paga = false;

        ConteudoParametroFilial conteudoParametroFilialInicio = getParametroFilialPernoite(cc, parametroInicio);
        ConteudoParametroFilial conteudoParametroFilialFim = getParametroFilialPernoite(cc, parametroFim);
        if (conteudoParametroFilialInicio == null || conteudoParametroFilialFim == null) {
            return paga;
        }

        DateTime parametroInicioPernoite = JTDateTimeUtils.convertDataStringToDateTime(conteudoParametroFilialInicio.getVlConteudoParametroFilial());
        DateTime parametroFimPernoite = JTDateTimeUtils.convertDataStringToDateTime(conteudoParametroFilialFim.getVlConteudoParametroFilial());

        int horas = JTDateTimeUtils.getIntervalInHours(cc.getDhSaidaColetaEntrega(), cc.getDhChegadaColetaEntrega());


        DateTime horaChegada = cc.getDhChegadaColetaEntrega();
        DateTime horaSaida = cc.getDhSaidaColetaEntrega();


        DateTime pernoiteInicio = JTDateTimeUtils.getFirstHourOfDay(horaSaida);
        pernoiteInicio = pernoiteInicio.plusHours(parametroInicioPernoite.getHourOfDay());
        pernoiteInicio = pernoiteInicio.plusMinutes(parametroInicioPernoite.getMinuteOfHour());
        pernoiteInicio = pernoiteInicio.plusSeconds(parametroInicioPernoite.getSecondOfMinute());

        DateTime pernoiteFim = JTDateTimeUtils.getFirstHourOfDay(horaChegada);
        pernoiteFim = pernoiteFim.plusHours(parametroFimPernoite.getHourOfDay());
        pernoiteFim = pernoiteFim.plusMinutes(parametroFimPernoite.getMinuteOfHour());
        pernoiteFim = pernoiteFim.plusSeconds(parametroFimPernoite.getSecondOfMinute());

        if (horas >= INT_HORAS) {
            return true;
        }


        return isSaidaMaiorInicioPernoite(horaSaida, pernoiteInicio) ||
                isChegadaMaiorInicioPernoite(horaChegada, pernoiteInicio) ||
                isFimPernoiteMaiorChegada(pernoiteFim, horaChegada) ||
                isFimPernoiteMaiorSaida(pernoiteFim, horaSaida);

    }

    /**
     * @param cc
     * @param nomeParametro
     * @return
     */
    private ConteudoParametroFilial getParametroFilialPernoite(ControleCarga cc, String nomeParametro) {
        ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(cc.getFilialByIdFilialOrigem().getIdFilial(), nomeParametro, false, true);

        if (conteudoParametroFilial != null) {
            validaParametroPernoite(nomeParametro, conteudoParametroFilial);
        }

        return conteudoParametroFilial;
    }

    /**
     * @param nomeParametro
     * @param conteudoParametroFilial
     * @throws BusinessException
     */
    private void validaParametroPernoite(String nomeParametro,
                                         ConteudoParametroFilial conteudoParametroFilial)
            throws BusinessException {
        if (conteudoParametroFilial.getVlConteudoParametroFilial().length() < VL_CONTEUDO_PARAMETRO_FILIAL_LENGTH) {
            throw new BusinessException("LMS-25132", new Object[]{nomeParametro, "HH:mm:ss"});
        }
    }

    /**
     * @param horaSaida
     * @param pernoiteFim
     */
    private boolean isFimPernoiteMaiorSaida(DateTime pernoiteFim, DateTime horaSaida) {
        return JTDateTimeUtils.comparaData(pernoiteFim, horaSaida) >= 0;

    }

    /**
     * @param horaChegada
     * @param pernoiteFim
     */
    private boolean isFimPernoiteMaiorChegada(DateTime pernoiteFim, DateTime horaChegada) {
        return JTDateTimeUtils.comparaData(pernoiteFim, horaChegada) >= 0;

    }

    /**
     * @param horaChegada
     * @param pernoiteInicio
     */
    private boolean isChegadaMaiorInicioPernoite(DateTime horaChegada, DateTime pernoiteInicio) {
        return JTDateTimeUtils.comparaData(horaChegada, pernoiteInicio) >= 0;
    }

    /**
     * @param horaSaida
     * @param pernoiteInicio
     */
    private boolean isSaidaMaiorInicioPernoite(DateTime horaSaida, DateTime pernoiteInicio) {
        return JTDateTimeUtils.comparaData(horaSaida, pernoiteInicio) >= 0;
    }


    public void calculaParcelaCapataziaCliente(TabelaFcValores tabela, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (BigDecimal.ZERO.equals(tabela.getVlCapataziaCliente())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(BigDecimal.ONE);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("CCL"));
        parcela.setVlValor(tabela.getVlCapataziaCliente());

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }

    public void calculaParcelaTransferencia(TabelaFcValores tabela, NotaCredito notaCredito, String notaTransferencia, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlTransferencia())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(BigDecimal.ONE);
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("TRA"));
        if (notaTransferencia != null) {
            parcela.setVlValor(BigDecimal.ZERO);
            notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Transfer�ncia pago n� " + notaTransferencia + ", ");
        } else {
            parcela.setVlValor(tabela.getVlTransferencia());
        }

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaValorAjudante(TabelaFcValores tabela, NotaCredito notaCredito, String notaValorAjudante, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getVlAjudante())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setQtTotal(new BigDecimal(tabela.getQtAjudante()));
        parcela.setTabelaFcValores(tabela);
        parcela.setTpValor(new DomainValue("VAJ"));

        if (notaValorAjudante != null) {
            parcela.setVlValor(BigDecimal.ZERO);
            notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Valor ajudante pago n� " + notaValorAjudante + ", ");
        } else {
            parcela.setVlValor(tabela.getVlAjudante());
        }

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaValorMercadoria(TabelaFcValores tabela, BigDecimal valorMercadoria, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (BigDecimal.ZERO.equals(tabela.getPcMercadoria())) {
            return;
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setTabelaFcValores(tabela);

        BigDecimal valorPercentual = tabela.getPcMercadoria().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO,  RoundingMode.HALF_UP);
        
        if (valorPercentual.multiply(valorMercadoria).compareTo(tabela.getVlmercadoriaMinimo()) > 0) {
            parcela.setTpValor(new DomainValue("MEP"));
            parcela.setVlValor(valorPercentual);
            parcela.setQtTotal(valorMercadoria);
        } else {
            parcela.setTpValor(new DomainValue("MEV"));
            parcela.setVlValor(tabela.getVlmercadoriaMinimo());
            parcela.setQtTotal(BigDecimal.ONE);
        }

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaFreteBruto(TabelaFcValores tabela, List<DoctoServico> doctos, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (BigDecimal.ZERO.equals(tabela.getPcFrete())) {
            return;
        }

        List<NotaCreditoCalcPadrao> parcelasPercentuais = new ArrayList<NotaCreditoCalcPadrao>();
        List<NotaCreditoCalcPadrao> parcelasValorMinimo = new ArrayList<NotaCreditoCalcPadrao>();


        BigDecimal valorPercentual = tabela.getPcFrete().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO_MENOR, RoundingMode.HALF_UP);

        for (DoctoServico doctoServico : doctos) {
            BigDecimal valor = doctoServico.getVlTotalDocServico();

            BigDecimal valorP = valorPercentual.multiply(valor);

            if (valorP.compareTo(tabela.getVlFreteMinimo()) > 0) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setVlValor(valorP);
                parcela.setQtTotal(valor);
                parcelasPercentuais.add(parcela);
            } else {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setVlValor(tabela.getVlFreteMinimo());
                parcela.setQtTotal(valor);
                parcelasValorMinimo.add(parcela);
            }
        }

        unificaPracelaValor(tabela, notaCredito, parcelasPercentuais, valorPercentual, "FBP", false, parcelaNotaCreditoCalcPadrao);

        unificaPracelaValor(tabela, notaCredito, parcelasValorMinimo, tabela.getVlFreteMinimo(), "FBV", true, parcelaNotaCreditoCalcPadrao);
    }


    public void calculaParcelaFreteLiquido(TabelaFcValores tabela, List<DoctoServico> doctos, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {

        if (BigDecimal.ZERO.equals(tabela.getPcFreteLiq())) {
            return;
        }

        List<NotaCreditoCalcPadrao> parcelasPercentuais = new ArrayList<NotaCreditoCalcPadrao>();
        List<NotaCreditoCalcPadrao> parcelasValorMinimo = new ArrayList<NotaCreditoCalcPadrao>();

        BigDecimal valorPercentual = tabela.getPcFreteLiq().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP);

        for (DoctoServico doctoServico : doctos) {
            BigDecimal valor = doctoServico.getVlLiquido();
            BigDecimal valorP = doctoServico.getVlLiquido().multiply(valorPercentual);

            if (valorP.compareTo(tabela.getVlFreteMinimoLiq()) > 0) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setVlValor(valorP);
                parcela.setQtTotal(valor);
                parcelasPercentuais.add(parcela);
            } else {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setVlValor(tabela.getVlFreteMinimoLiq());
                parcela.setQtTotal(valor);
                parcelasValorMinimo.add(parcela);
            }
        }
        unificaPracelaValor(tabela, notaCredito, parcelasPercentuais, valorPercentual, "FLB", false, parcelaNotaCreditoCalcPadrao);
        unificaPracelaValor(tabela, notaCredito, parcelasValorMinimo, tabela.getVlFreteMinimoLiq(), "FLV", true, parcelaNotaCreditoCalcPadrao);
    }

    /**
     * @param tabela
     * @param notaCredito
     * @param list
     * @param valor
     */
    private void unificaPracelaValor(TabelaFcValores tabela, NotaCredito notaCredito, List<NotaCreditoCalcPadrao> list, BigDecimal valor, String tipo, boolean minimo, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        BigDecimal total = BigDecimal.ZERO;

        if (list.isEmpty()) {
            return;
        }
        int count = 0;
        for (NotaCreditoCalcPadrao notaCreditoCalcPadrao : list) {
            count++;
            total = total.add(notaCreditoCalcPadrao.getQtTotal());
        }

        NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
        parcela.setNotaCredito(notaCredito);
        parcela.setTabelaFcValores(tabela);
        parcela.setQtTotal(minimo ? new BigDecimal(count) : total);
        parcela.setTpValor(new DomainValue(tipo));
        parcela.setVlValor(valor);

        addParcelaNotaCredito(parcela, parcelaNotaCreditoCalcPadrao);
    }


	
/*Premio*/

    public void calculaParcelaPremioCTE(TabelaFcValores tabela, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioCte())) {
            return;
        }

        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("CON".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PCT"));
                parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioCte().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));
                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }
    }


    public void calculaParcelaPremioEvento(TabelaFcValores tabela, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioEvento())) {
            return;
        }

        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("EVE".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PEV"));
                parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioEvento().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));
                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }
    }

    public void calculaParcelaPremioDiaria(TabelaFcValores tabela, NotaCredito notaCredito, String notaPremioDiaria, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioDiaria())) {
            return;
        }

        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("DIA".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PDI"));

                if (notaPremioDiaria != null) {
                    parcela.setVlValor(BigDecimal.ZERO);
                    notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + " Pr�mio di�ria pago n� " + notaPremioDiaria + ", ");
                } else {
                    parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioDiaria().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));
                }

                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }

    }


    public void calculaParcelaPremioVolume(TabelaFcValores tabela, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioVolume())) {
            return;
        }
        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("VOL".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PVO"));
                parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioVolume().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));

                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }
    }


    public void calculaParcelaPremioSaida(TabelaFcValores tabela, NotaCredito notaCredito, String notaPremioSaida, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioSaida())) {
            return;
        }
        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("PRE".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PSA"));
                if (notaPremioSaida != null) {
                    parcela.setVlValor(BigDecimal.ZERO);
                    notaCredito.setObNotaCredito(notaCredito.getObNotaCredito() + "Pr�mio sa�da pago n� " + notaPremioSaida + ", ");
                } else {
                    parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioSaida().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));
                }

                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }
    }


    public void calculaParcelaPremioFreteBruto(TabelaFcValores tabela, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioFreteBruto())) {
            return;
        }
        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("FBP".equals(p.getTpValor().getValue()) || "FBV".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PFB"));
                parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioFreteBruto().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));

                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }
    }


    public void calculaParcelaPremioFreteLiquido(TabelaFcValores tabela, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioFreteLiq())) {
            return;
        }


        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("FLB".equals(p.getTpValor().getValue()) || "FLV".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PFL"));
                parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioFreteLiq().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));

                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }
    }


    public void calculaParcelaPremioMercadoria(TabelaFcValores tabela, NotaCredito notaCredito, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        if (tabela == null || tabela.getTabelaFreteCarreteiroCe() == null || BigDecimal.ZERO.equals(tabela.getTabelaFreteCarreteiroCe().getPcPremioMercadoria())) {
            return;
        }


        for (NotaCreditoCalcPadrao p : parcelaNotaCreditoCalcPadrao.parcelas) {
            if ("MEP".equals(p.getTpValor().getValue()) || "MEV".equals(p.getTpValor().getValue())) {
                NotaCreditoCalcPadrao parcela = new NotaCreditoCalcPadrao();
                parcela.setNotaCredito(notaCredito);
                parcela.setQtTotal(p.getVlValor().multiply(p.getQtTotal()));
                parcela.setTabelaFcValores(p.getTabelaFcValores());
                parcela.setTpValor(new DomainValue("PME"));
                parcela.setVlValor(tabela.getTabelaFreteCarreteiroCe().getPcPremioMercadoria().divide(new BigDecimal(PORCENTAGEM_DIVISAO), PRECISAO, RoundingMode.HALF_UP));
                addParcelaNotaCreditoPremio(parcela, parcelaNotaCreditoCalcPadrao);
            }
        }

    }


    private void addParcelaNotaCreditoPremio(NotaCreditoCalcPadrao parcela, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        parcela.setTabelaFreteCarreteiroCe(parcela.getTabelaFcValores().getTabelaFreteCarreteiroCe());
        parcelaNotaCreditoCalcPadrao.parcelasPremio.add(parcela);
    }

	/*fim calculos*/

	/*Diaria paga diaria*/

    private boolean isPagaDiaria(List<ControleCarga> controlesCargaNoPeriodo, BigDecimal parametro) {
        Double horasViagem = getTotalHoras(controlesCargaNoPeriodo);

        return horasViagem.compareTo(findTotalHorasDiaria(parametro)) >= 0;
    }

    /**
     * @param controlesCargaNoPeriodo
     * @return
     */

    private Double getTotalHoras(List<ControleCarga> controlesCargaNoPeriodo) {
        Double horasViagem = 0D;

        for (ControleCarga controleCarga : controlesCargaNoPeriodo) {
            horasViagem += getTempoViagemEmHoras(controleCarga);
        }
        return horasViagem;
    }

    private Double getTempoViagemEmHoras(ControleCarga controleCarga) {
        return getTempoViagem(controleCarga) / HOUR_IN_MILLIS;
    }

    private Double getTempoViagem(ControleCarga controleCarga) {
        if (controleCarga.getDhChegadaColetaEntrega() != null) {
            return Double.valueOf(controleCarga.getDhChegadaColetaEntrega().getMillis() - controleCarga.getDhSaidaColetaEntrega().getMillis());
        }
        return 0D;
    }

    private Double findTotalHorasDiaria(BigDecimal horas) {

        if (horas == null || BigDecimal.ZERO.compareTo(horas) > 0) {
            horas = BigDecimal.ZERO;
        }

        return horas.doubleValue();
    }


    private void addParcelaNotaCredito(NotaCreditoCalcPadrao parcela, ParcelaNotaCreditoCalcPadrao parcelaNotaCreditoCalcPadrao) {
        parcela.setTabelaFreteCarreteiroCe(parcela.getTabelaFcValores().getTabelaFreteCarreteiroCe());
        if (parcelaNotaCreditoCalcPadrao.parcelas == null) {
            parcelaNotaCreditoCalcPadrao.parcelas = new ArrayList<NotaCreditoCalcPadrao>();
        }
        parcelaNotaCreditoCalcPadrao.parcelas.add(parcela);
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    private TabelaFreteCarreteiroCeDAO getTabelaFreteCarreteiroCeDAO() {
        return (TabelaFreteCarreteiroCeDAO) getDao();
    }

    public void setTabelaFreteCarreteiroCeDAO(TabelaFreteCarreteiroCeDAO dao) {
        setDao(dao);
    }

    @Override
    protected Serializable store(TabelaFreteCarreteiroCe bean) {
        return super.store(bean);
    }

    public TabelaFreteCarreteiroCe findById(java.lang.Long id) {
        return (TabelaFreteCarreteiroCe) super.findById(id);
    }

    public MeioTransporteService getMeioTransporteService() {
        return meioTransporteService;
    }

    public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
        this.meioTransporteService = meioTransporteService;
    }

    public CalculoTabelaFreteCarreteiroCeDAO getCalculoTabelaFreteCarreteiroCeDAO() {
        return calculoTabelaFreteCarreteiroCeDAO;
    }

    public void setCalculoTabelaFreteCarreteiroCeDAO(
            CalculoTabelaFreteCarreteiroCeDAO calculoTabelaFreteCarreteiroCeDAO) {
        this.calculoTabelaFreteCarreteiroCeDAO = calculoTabelaFreteCarreteiroCeDAO;
    }

    public TabelaFreteCarreteiroCeService getTabelaFreteCarreteiroCeService() {
        return tabelaFreteCarreteiroCeService;
    }


    public void setTabelaFreteCarreteiroCeService(
            TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService) {
        this.tabelaFreteCarreteiroCeService = tabelaFreteCarreteiroCeService;
    }


    public DoctoServicoService getDoctoServicoService() {
        return doctoServicoService;
    }


    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setConteudoParametroFilialService(
            ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }


    public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
        this.moedaPaisService = moedaPaisService;
    }


    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setManifestoEntregaService(
            ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setEventoNotaCreditoService(
            EventoNotaCreditoService eventoNotaCreditoService) {
        this.eventoNotaCreditoService = eventoNotaCreditoService;
    }

    public void setNotaCreditoPadraoService(
            NotaCreditoPadraoService notaCreditoPadraoService) {
        this.notaCreditoPadraoService = notaCreditoPadraoService;
    }

    public void setSolicitacaoContratacaoService(
            SolicitacaoContratacaoService solicitacaoContratacaoService) {
        this.solicitacaoContratacaoService = solicitacaoContratacaoService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }


    public class ParcelaNotaCreditoCalcPadrao {
        private TabelaFcValores tabelaKmExcedente = null;
        private TabelaFcValores tabelaDiaria = null;
        private TabelaFcValores tabelaHora = null;
        private TabelaFcValores tabelaPreDiaria = null;
        private TabelaFcValores tabelaDedicado = null;
        private TabelaFcValores tabelaPernoite = null;
        private TabelaFcValores tabelaLocacaoCarreta = null;
        private TabelaFcValores tabelaTransferencia = null;
        private TabelaFcValores tabelaValorAjudante = null;
        private TabelaFcValores tabelaPremioCTE = null;
        private TabelaFcValores tabelaPremioEvento = null;
        private TabelaFcValores tabelaPremioDiaria = null;
        private TabelaFcValores tabelaPremioVolume = null;
        private TabelaFcValores tabelaPremioSaida = null;
        private TabelaFcValores tabelaPremioFreteBruto = null;
        private TabelaFcValores tabelaPremioFreteLiquido = null;
        private TabelaFcValores tabelaPremioMercadoria = null;

        private BigDecimal qtdDocsControleCarga = BigDecimal.ZERO;
        private BigDecimal qtdEventosControleCarga = BigDecimal.ZERO;
        private BigDecimal qtdVolumeControlecarga = BigDecimal.ZERO;

        private List<NotaCreditoCalcPadrao> parcelas;
        private List<NotaCreditoCalcPadrao> parcelasPremio;
    }


	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setEntregaNotaFiscalService(
			EntregaNotaFiscalService entregaNotaFiscalService) {
		this.entregaNotaFiscalService = entregaNotaFiscalService;
	}

}
