package com.mercurio.lms.expedicao.model.service;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO_PARCIAL;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.IE_CONSIGNATARIO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.IE_REDESPACHO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.NR_FROTA_BALCAO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.TP_FRETE_FOB;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.SynchronizedPriorityQueue;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Contingencia;
import com.mercurio.lms.expedicao.model.CtoCtoCooperada;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DoctoServicoSeguros;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.LiberacaoDocServ;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.TipoCusto;
import com.mercurio.lms.expedicao.model.ValorCusto;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tributos.model.ExcecaoICMSCliente;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.tributos.model.service.CalcularIcmsService;
import com.mercurio.lms.tributos.model.service.ExcecaoICMSClienteService;
import com.mercurio.lms.tributos.model.service.ImpostoCalculadoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.EqualsHelper;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.WarningCollector;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.DocumentoCliente;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.LiberacaoEmbarque;
import com.mercurio.lms.vendas.model.MunicipioDestinoCalculo;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.SegmentoMercado;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.DocumentoClienteService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;
import com.mercurio.lms.vendas.model.service.LiberacaoEmbarqueService;
import com.mercurio.lms.vendas.model.service.MunicipioDestinoCalculoService;
import com.mercurio.lms.vendas.model.service.ProibidoEmbarqueService;
import com.mercurio.lms.vendas.model.service.SeguroClienteService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 */
public abstract class AbstractConhecimentoNormalService extends AbstractConhecimentoService {

	private static final String REMETENTE = "remetente";
	private static final String DESTINATARIO = "destinatário";
    private static final String LMS_04065 = "LMS-04065";
	protected MonitoramentoDescargaService monitoramentoDescargaService;
	protected CotacaoService cotacaoService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	protected FilialService filialService;
	private CtoCtoCooperadaService ctoCtoCooperadaService;
	private PedidoColetaService pedidoColetaService;
	private MunicipioFilialService municipioFilialService;
	private LiberacaoDocServService liberacaoDocServService;
	private LiberacaoEmbarqueService liberacaoEmbarqueService;
	private ProibidoEmbarqueService proibidoEmbarqueService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private SeguroClienteService seguroClienteService;
	private ServAdicionalDocServService servAdicionalDocServService;
	private DocumentoClienteService documentoClienteService;
	private ExcecaoICMSClienteService excecaoICMSClienteService;
	private InscricaoEstadualService inscricaoEstadualService;
	private FluxoFilialService fluxoFilialService;
	private InformacaoDocServicoService informacaoDocServicoService;
	private MunicipioDestinoCalculoService municipioDestinoCalculoService;
	private ObservacaoDoctoServicoService observacaoDoctoServicoService;
	private RecursoMensagemService recursoMensagemService;
	private ImpostoCalculadoService impostoCalculadoService;
	private ParametroGeralService parametroGeralService;
	private ContingenciaService contingenciaService;
	private Long idMonitoramentoDescarga;
	protected DoctoServicoService doctoServicoService;
	private ConteudoParametroFilialService parametroFilialService;
	private EmbarqueValidationService embarqueValidationService;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private CalcularIcmsService calcularIcmsService;

    private static final Logger LOGGER = LogManager.getLogger(AbstractConhecimentoNormalService.class);


	private static String[] tpConhBlAferido = new String[]{"DE","DP","NO","RF"};

	protected void validatePrimeiraFase(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		//Estes 2 testes devem ser repetidos para evitar erro na tela
		validateClienteResposavel(conhecimento);
		validateIntegrantesIguais(conhecimento);

		setClienteResponsavel(conhecimento);
		setFilialAtendimentoMunicipioDestino(conhecimento);
		setLocalEntrega(conhecimento);

		validateSituacaoTributariaFreteFob(
		    conhecimento.getTpFrete().getValue(),
		    Long.valueOf(conhecimento.getInscricaoEstadualDestinatario().getIdInscricaoEstadual()));
		setRotaColetaEntregaSugerida(conhecimento);

		setClienteBaseCalculoFrete(calculoFrete, conhecimento);
		setLocalColeta(conhecimento);
	}

	protected void validateSegundaFase(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		validateClienteResposavel(conhecimento);
		validateIntegrantesIguais(conhecimento);
		validateAtendimentoMunicipioDestino(conhecimento, calculoFrete);
		validateDensidadeClienteDevedorEspecial(conhecimento);
		validateCamposAdicionais(conhecimento);
		validateInscricacaoEstadualTomadorServico(conhecimento);
		validateTipoFreteCliente(conhecimento);
		validateAeroporto(conhecimento);
		validateQuantidadeVolumes(conhecimento);
		validateDensidadePesoCubado(conhecimento);
		validateDoctoServicoOriginal(conhecimento);
		validateAtendimentoEmpresaTipoFrete(conhecimento);
		validateCotacao(conhecimento);
		validateCtrcCooperada(conhecimento);
		validateCotacaoRemententeNaoInformada(conhecimento);

		List<LiberacaoDocServ> liberacoesCotacao = null;
		if (conhecimento.getCotacoes() != null) {
			Cotacao cotacao = (Cotacao) conhecimento.getCotacoes().get(0);
			liberacoesCotacao = liberacaoDocServService.getLiberacaoDocServByIdCotacao(cotacao.getIdCotacao());
		}
		//Se existe Liberação na Cotacao, nao valida os seguintes itens.
		if (liberacoesCotacao != null && !liberacoesCotacao.isEmpty()) {
			for(LiberacaoDocServ liberacaoDocServ : liberacoesCotacao) {
				liberacaoDocServ.setDoctoServico(conhecimento);
				conhecimento.addLiberacaoEmbarque(liberacaoDocServ);
			}
			
		} else if (!conhecimento.getBlIndicadorEdi()) {
			validateRegras(conhecimento);
		}
		validateNotaFiscalConhecimento(conhecimento);
	}

	/**
	 * @param conhecimento
	 */
	private void validateRegras(Conhecimento conhecimento) {
			// Quando for EDI, não validar regras abaixo 
			validatePesoReal(conhecimento);
			
		validateClienteInadimplente(conhecimento);
		
		if (hasToValidateEmbarqueProibido(conhecimento)) {
				validateEmbarqueProibido(conhecimento);
		}
			
			validateFreteManual(conhecimento);
			validateFreteCortesia(conhecimento);
	}
		
	private boolean hasToValidateEmbarqueProibido(Conhecimento conhecimento) {
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
				
		return embarqueValidationService.hasToValidateEmbarqueProibido(	conhecimento.getServico(),	clienteResponsavel,		conhecimento.getTpFrete());
		}
		 

	private void validateClienteInadimplente(Conhecimento conhecimento) {
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
		validateEmbarqueProibidoCliente(conhecimento, clienteResponsavel);
		
	}

	/**
	 * Verifica se existe liberação na cotação, caso existir adiciona ao conhecimento 
	 */
	protected void validarLiberacaoCotacao(Conhecimento conhecimento){
		
		List<LiberacaoDocServ> liberacoesCotacao = null;
		if (conhecimento.getCotacoes() != null && !conhecimento.getCotacoes().isEmpty()) {
			Cotacao cotacao = (Cotacao) conhecimento.getCotacoes().get(0);
			liberacoesCotacao = liberacaoDocServService.getLiberacaoDocServByIdCotacao(cotacao.getIdCotacao());
		}
		
		/*Se existe Liberação na Cotacao, nao valida os seguintes itens.*/
		if (liberacoesCotacao != null && !liberacoesCotacao.isEmpty()) {
			for(LiberacaoDocServ liberacaoDocServ : liberacoesCotacao) {
				liberacaoDocServ.setDoctoServico(conhecimento);
				conhecimento.addLiberacaoEmbarque(liberacaoDocServ);
			}
		} else if (!conhecimento.getBlIndicadorEdi()) {
			validateRegras(conhecimento);
		}		
	}

		
	/**
	 * Complementação das informações do CTRC.
	 * Atenção: Esse método só dever ser utilizado quando se está inserindo (insert) um novo conhecimento, 
	 * nunca para atualizar (update) um conhecimento.
	 * Atribuição dos valores padrões do DoctoServico e do Conhecimento.
	 */
	private Conhecimento beforeInsert(Conhecimento conhecimento) {
		conhecimento.setBlBloqueado(Boolean.FALSE);

		BigDecimal vlImpostoDifal = conhecimento.getVlImpostoDifal();

		if(vlImpostoDifal != null && vlImpostoDifal.compareTo(new BigDecimal(0)) > 0){
			conhecimento.setBlBloqueado(Boolean.TRUE);
		}
		
		if (conhecimento.getIdDoctoServico() == null) { 								
		// ao achar o tipo do conhecimento dentro do array, seta false se nao true
		conhecimento.setBlPesoAferido(Arrays.binarySearch(tpConhBlAferido, conhecimento.getTpConhecimento().getValue()) <= 0);
																						// Feito essa tratativa  
			conhecimento.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());// para contornar conhecimentos 
		}																				// salvos pelo CheckWeight onde estava colocando usuario logado do CW.
																						// Deve ser o usuario da criação do CTO
		conhecimento.setDsEspecieVolume(ConstantesExpedicao.DS_ESPECIE_VOLUME);
		conhecimento.setDsCalculadoAte(ConstantesExpedicao.CALCULADO_ATE_DESTINO);
        if (conhecimento.getBlIndicadorEdi() == null) {
            conhecimento.setBlIndicadorEdi(Boolean.FALSE);
        }
		if (!conhecimento.getBlIndicadorEdi() && conhecimento.getDhInclusao() == null) { 
			conhecimento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		}
		conhecimento.setTpSituacaoConhecimento(new DomainValue("P"));
		conhecimento.setBlPermiteTransferencia(Boolean.TRUE);
		conhecimento.setBlSeguroRr(Boolean.FALSE);
		//Define o Fluxo de Carga
		setFluxoCarga(conhecimento, Boolean.FALSE);
		//Gera observacao geral
		if(conhecimento.getIdDoctoServico() == null && (conhecimento.getTpConhecimento() == null || !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())) ) {
			addObservacaoGeral(conhecimento);
		}
		prepareItems(conhecimento);
		
		if(conhecimento.getTpCargaDocumento() == null){
		    conhecimento.setTpCargaDocumento(getTpCargaDocumento(conhecimento));
		}
		
		return conhecimento;
	}

	/**
	 * Complementação das informações do CTRC.
	 * Atenção: Esse método só dever ser utilizado quando se está inserindo (insert) um novo conhecimento, 
	 * nunca para atualizar (update) um conhecimento.
	 * Atribuição dos valores padrões do DoctoServico e do Conhecimento.
	 */
	private Conhecimento beforeInsertSemCalculoFrete(Conhecimento conhecimento, Boolean blBloqueiaSubcontratada) {
		conhecimento.setBlBloqueado(Boolean.FALSE);
		conhecimento.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		conhecimento.setDhEmissao(null);
		conhecimento.setDhAlteracao(null);
        if (!conhecimento.getBlIndicadorEdi()) {
            conhecimento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
        }
		conhecimento.setDsEspecieVolume(ConstantesExpedicao.DS_ESPECIE_VOLUME);
		conhecimento.setDsCalculadoAte(ConstantesExpedicao.CALCULADO_ATE_DESTINO);
        if (conhecimento.getBlIndicadorEdi() == null) {
            conhecimento.setBlIndicadorEdi(Boolean.FALSE);
        }
		conhecimento.setTpSituacaoConhecimento(new DomainValue("P"));
		conhecimento.setBlPermiteTransferencia(Boolean.TRUE);
		conhecimento.setBlSeguroRr(Boolean.FALSE);
		conhecimento.setTarifaPreco(null);
		conhecimento.setTipoTributacaoIcms(null);
		conhecimento.setBlIndicadorFretePercentual(Boolean.FALSE);
		conhecimento.setBlIncideIcmsPedagio(Boolean.FALSE);
		conhecimento.setVlLiquido(BigDecimal.ZERO);
		conhecimento.setVlIcmsSubstituicaoTributaria(BigDecimal.ZERO);
		conhecimento.setVlIcmsSubstituicaoTributariaPesoDeclarado(null);
		conhecimento.setVlTotalDocServico(BigDecimal.ZERO);
		conhecimento.setVlDesconto(BigDecimal.ZERO);
		conhecimento.setVlImposto(BigDecimal.ZERO);
		conhecimento.setVlImpostoPesoDeclarado(BigDecimal.ZERO);
		conhecimento.setVlBaseCalcImposto(BigDecimal.ZERO);
		conhecimento.setNrCfop(null);
		conhecimento.setPsReferenciaCalculo(null);
		conhecimento.setTabelaPreco(null);
		conhecimento.setBlPesoAferido(Boolean.FALSE);
		conhecimento.setNrFatorDensidade(null);
		
		//Define o Fluxo de Carga
		setFluxoCarga(conhecimento, blBloqueiaSubcontratada);
		//Gera observacao geral
		if(conhecimento.getIdDoctoServico() == null && (conhecimento.getTpConhecimento() == null || !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())) ) {
		addObservacaoGeral(conhecimento);
		}

		prepareItems(conhecimento);

		conhecimento.setTpCargaDocumento(getTpCargaDocumento(conhecimento));
		
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioOrigem = new TipoLocalizacaoMunicipio();
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino = new TipoLocalizacaoMunicipio();
		tipoLocalizacaoMunicipioOrigem.setIdTipoLocalizacaoMunicipio(conhecimento.getDadosCliente().getIdTipoLocalizacaoOrigem());
		tipoLocalizacaoMunicipioDestino.setIdTipoLocalizacaoMunicipio(conhecimento.getDadosCliente().getIdTipoLocalizacaoDestino());
		conhecimento.setLocalizacaoOrigem(tipoLocalizacaoMunicipioOrigem);
		conhecimento.setLocalizacaoDestino(tipoLocalizacaoMunicipioDestino);
		return conhecimento;
	}

	private void prepareItems(Conhecimento conhecimento) {
		if(conhecimento.getEnderecoPessoa() != null && conhecimento.getEnderecoPessoa().getIdEnderecoPessoa() == null) {
			conhecimento.setEnderecoPessoa(null);
		}
		List<Dimensao> dimensoes = conhecimento.getDimensoes();
		if(dimensoes != null) {
			for (Dimensao dimensao : dimensoes) {
				if (dimensao.getIdDimensao() < 0) {
				dimensao.setIdDimensao(null);
				}
				dimensao.setConhecimento(conhecimento);
			}
		}

		List<NotaFiscalConhecimento> notasFiscaisConhecimento = conhecimento.getNotaFiscalConhecimentos();
		Cliente clienteRemetente = conhecimento.getClienteByIdClienteRemetente();
		for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {
			notaFiscalConhecimento.setCliente(clienteRemetente);
			notaFiscalConhecimento.setConhecimento(conhecimento);
			if(notaFiscalConhecimento.getPsMercadoria() == null) {
				notaFiscalConhecimento.setPsMercadoria(BigDecimal.ZERO);
			}
		}

		List<ObservacaoDoctoServico> observacoesDoctoServico = conhecimento.getObservacaoDoctoServicos();
		for (ObservacaoDoctoServico observacaoDoctoServico : observacoesDoctoServico) {
			observacaoDoctoServico.setDoctoServico(conhecimento);
			if(conhecimento.getIdDoctoServico() == null) {
			observacaoDoctoServico.setIdObservacaoDoctoServico(null);
		}
		}

		List<DevedorDocServ> devedoresDoctoServico = conhecimento.getDevedorDocServs();
		if(devedoresDoctoServico != null) {
			
			boolean findDevedorDocServFat = false;
			List<DevedorDocServFat> devedoresFat = conhecimento.getDevedorDocServFats();
			DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
            if (devedoresFat == null) {
				devedoresFat = new ArrayList<DevedorDocServFat>(devedoresDoctoServico.size());
            }
			
			for (DevedorDocServ devedorDoctoServico : devedoresDoctoServico) {
				//para evitar org.hibernate.NonUniqueObjectException já que os objetos vem da sessão
				Cliente cliente = clienteService.findById(devedorDoctoServico.getCliente().getIdCliente());

				// Define filial de cobrança
				Filial filialCobranca = null;
				if (ConstantesExpedicao.TP_FRETE_CIF.equals(conhecimento.getTpFrete().getValue())) {

                    filialCobranca = cliente.getBlCobrancaCentralizada() != null && cliente.getBlCobrancaCentralizada() ?
                            cliente.getFilialByIdFilialCobranca() : conhecimento.getFilialByIdFilialOrigem();

				} else if (ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimento.getTpFrete().getValue()) && conhecimento.getClienteByIdClienteConsignatario() != null) {
					filialCobranca = conhecimento.getFilialByIdFilialOrigem();

				} else if (ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimento.getTpFrete().getValue()) && conhecimento.getClienteByIdClienteConsignatario() == null) {
					filialCobranca = conhecimento.getFilialByIdFilialDestino().getFilialByIdFilialResponsavel();
				}
				
				devedorDoctoServico.setFilial(filialCobranca);

				DivisaoCliente divisaoCliente = getDivisaoClienteDevedor(conhecimento.getClienteByIdClienteBaseCalculo(), cliente, conhecimento.getDivisaoCliente(), conhecimento.getServico());

				for (DevedorDocServFat devedorDSFat : devedoresFat) {
					if (devedorDSFat.getDoctoServico() != null && devedorDSFat.getDoctoServico().getIdDoctoServico() != null  
							&& devedorDSFat.getDoctoServico().getIdDoctoServico().equals(devedorDoctoServico.getDoctoServico().getIdDoctoServico())) {
						devedorDocServFat = devedorDSFat;
						findDevedorDocServFat = true;
						break;
					}					
				}
				
				devedorDocServFat.setCliente(cliente);
				devedorDocServFat.setDoctoServico(conhecimento);
				devedorDocServFat.setDivisaoCliente(divisaoCliente);
				devedorDocServFat.setFilial(devedorDoctoServico.getFilial());
				devedorDocServFat.setTpSituacaoCobranca(new DomainValue("P"));
				devedorDocServFat.setVlDevido(conhecimento.getVlLiquido());
				
                if (!findDevedorDocServFat) {
                    devedoresFat.add(devedorDocServFat);
                }
				
				devedorDoctoServico.setDoctoServico(conhecimento);
				devedorDoctoServico.setVlDevido(conhecimento.getVlLiquido());
			}
			conhecimento.setDevedorDocServFats(devedoresFat);
		}

		List<ServAdicionalDocServ> servicosAdicionaisDoctoServico = conhecimento.getServAdicionalDocServs();
		if(servicosAdicionaisDoctoServico != null) {
			for (ServAdicionalDocServ servicoAdicionalDoctoServico : servicosAdicionaisDoctoServico) {
				servicoAdicionalDoctoServico.setDoctoServico(conhecimento);
			}
		}

		List<DadosComplemento> dadosComplementos = conhecimento.getDadosComplementos();
		if(dadosComplementos != null) {
			for (DadosComplemento dadosComplemento : dadosComplementos) {
				dadosComplemento.setConhecimento(conhecimento);
					}
				}
			}

	/**
	 * Regra ValidaCTRC.1
	 * Validação do Tipo de Frete X Cliente Responsável:
	 *	-	Se o Tipo de Frete Selecionado for "CIF" (C) o campo Responsável só poderá 
	 *		ser preenchido com "Remetente" (R), "Redespacho" ou "Outro Responsável" (O), 
	 *		caso contrário visualizar mensagem LMS-04058;
	 *	-	Se o Tipo de Frete Selecionado for "FOB" (F) o campo Redespacho não poderá
	 *		ser preenchido, caso contrário visualizar mensagem LMS-04204;
	 *	-	Se o Tipo de Frete Selecionado for "FOB" (F) o campo Responsável não poderá 
	 *		ser preenchido com "Remetente" (R), caso contrário visualizar mensagem LMS-04058.
	 *
	 * @param conhecimento @see{Conhecimento}
	 */
	protected void validateTipoFreteCliente(Conhecimento conhecimento) {
		String tpFrete = conhecimento.getTpFrete().getValue();		
		String tpDevedorFrete = conhecimento.getTpDevedorFrete().getValue();
		if(ConstantesExpedicao.TP_FRETE_CIF.equals(tpFrete)){
			if(!ConstantesExpedicao.TP_DEVEDOR_REMETENTE.equals(tpDevedorFrete) 
				&& !ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equals(tpDevedorFrete) 
				&& !ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(tpDevedorFrete)) {
				throw new BusinessException("LMS-04058");
			}
		} else if(ConstantesExpedicao.TP_FRETE_FOB.equals(tpFrete)){
			Cliente clienteRedespacho = conhecimento.getClienteByIdClienteRedespacho();
			if(clienteRedespacho != null && clienteRedespacho.getIdCliente() != null) {
				throw new BusinessException("LMS-04204");
			}

			if(!ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(tpDevedorFrete) 
					&& !ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO.equals(tpDevedorFrete) 
					&& !ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(tpDevedorFrete)) {
				throw new BusinessException("LMS-04058");
			}
		}
	}

	/**
	 * Regra ValidaCTRC.2
	 * Validação do Cliente Responsável:
	 * - Se o CTRC da cooperada (CtoCtoCooperada) for informado, o responsável pelo frete deverá ter sido informado, 
	 * se não informado visualizar mensagem LMS-04059;
	 * - Se o Cliente responsável for Outro Responsável (O), deverá obrigatoriamente,
	 * ter sido informado um cliente na popup Responsável pelo frete,
	 * este cliente será o Responsável do Frete, caso não tenha sido informado visualizar
	 * mensagem LMS-04060.
	 * - Se o Cliente responsável for Outro Responsável e o cliente informado na popup
	 * Responsável pelo frete tiver seu CNPJ iniciado por “95591723”, visualizar
	 * mensagem LMS-04288, retornando à aba Dados Gerais.
	 * autor Julio Cesar Fernandes Corrêa
	 * 02/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateClienteResposavel(Conhecimento conhecimento) {
		if(conhecimento.getCtoCtoCooperadas() != null 
			&& !conhecimento.getCtoCtoCooperadas().isEmpty() 
			&& (conhecimento.getDevedorDocServs() == null || conhecimento.getDevedorDocServs().isEmpty())) {
			throw new BusinessException("LMS-04059");
		}
		String responsavel = conhecimento.getTpDevedorFrete().getValue();
		if(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(responsavel)
			&& (conhecimento.getDevedorDocServs() == null || conhecimento.getDevedorDocServs().isEmpty())) {			
			throw new BusinessException("LMS-04060");
		}

		if(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(responsavel)) {
			for (DevedorDocServ devedorDocServ : conhecimento.getDevedorDocServs()) {
				if ("95.591.723".equals(devedorDocServ.getCliente().getPessoa().getNrIdentificacao().substring(0, 10))) {
					throw new BusinessException("LMS-04288");
				}
			}
		}

		if(ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO.equals(responsavel) 
			&& conhecimento.getClienteByIdClienteConsignatario() == null) {
			throw new BusinessException("LMS-04060");
		}
		if(ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equals(responsavel) 
			&& conhecimento.getClienteByIdClienteRedespacho() == null) {
			throw new BusinessException("LMS-04060");
		}
	}

	/**
	 * Regra ValidaCTRC.3
	 * Definição do Cliente Responsável:
	 * - Se informado "Remetente" (R) no campo Responsável, o Responsável do Frete será o Remetente;
	 * - Se informado "Destinatário" (D) no campo Responsável, o Responsável do Frete será o Destinatário;
	 * - Se informado "Consignatário" (C) no campo Responsável, o Responsável do Frete será o Consignatário;
	 * - Se informado "Redespacho" (P) no campo Responsável, o Responsável do Frete será o Redespacho;
	 * - Se informado "Outro Responsável" (O) no campo Responsável, o Responsável do Frete 
	 * será o cliente informado na popup Responsável do frete.
	 * Verificar se o cliente selecionado como Responsável do Frete, exceção quando for selecionado 
	 * "Outro responsável" (O) no campo Responsável, não possui um Responsável do Frete pré-definido 
	 * (o campo Cliente.cliente.idCliente estiver preenchido com um ID diferente do ID do cliente selecionado), 
	 * se possuir utilizar o cliente vinculado a este ID e preencher o campo Responsável com o 
	 * conteúdo "Outro Responsável".
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 02/12/2005
     *
	 * @param conhecimento
	 */
	protected void setClienteResponsavel(Conhecimento conhecimento) {
		String responsavel = conhecimento.getTpDevedorFrete().getValue();
		Cliente cliente = null;
		DevedorDocServ devedorDoctoServico = null;
		if(!ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(responsavel)) {
			InscricaoEstadual inscricaoEstadual = null;
			
			if(ConstantesExpedicao.TP_DEVEDOR_REMETENTE.equals(responsavel)) {
				cliente = conhecimento.getClienteByIdClienteRemetente();
				inscricaoEstadual = conhecimento.getInscricaoEstadualRemetente();
			} else if(ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(responsavel)) {
				cliente = conhecimento.getClienteByIdClienteDestinatario();
				inscricaoEstadual = conhecimento.getInscricaoEstadualDestinatario();
			} else if(ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO.equals(responsavel)) {
				cliente = conhecimento.getClienteByIdClienteConsignatario();
                if (conhecimento.getDadosComplementos() != null && !conhecimento.getDadosComplementos().isEmpty()) {
					for (DadosComplemento dadosComplemento : conhecimento.getDadosComplementos()) {
						InformacaoDocServico informacaoDocServico = dadosComplemento.getInformacaoDocServico();
						if (cliente != null && informacaoDocServico != null && dadosComplemento != null && IE_CONSIGNATARIO.equals(informacaoDocServico.getDsCampo())) {
							inscricaoEstadual = inscricaoEstadualService.findByNrInscricaoEstadual(cliente.getIdCliente(), dadosComplemento.getDsValorCampo());
							break;
						}
					}
				}
			} else if(ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equals(responsavel)) {
				cliente = conhecimento.getClienteByIdClienteRedespacho();
                if (conhecimento.getDadosComplementos() != null && !conhecimento.getDadosComplementos().isEmpty()) {
					for (DadosComplemento dadosComplemento : conhecimento.getDadosComplementos()) {
						InformacaoDocServico informacaoDocServico = dadosComplemento.getInformacaoDocServico();
						if (cliente != null && dadosComplemento != null && informacaoDocServico != null && IE_REDESPACHO.equals(informacaoDocServico.getDsCampo())) {
							inscricaoEstadual = inscricaoEstadualService.findByNrInscricaoEstadual(cliente.getIdCliente(), dadosComplemento.getDsValorCampo());
							break;
						}
					}
				}
			} 
			
			if(cliente != null) {
				int blResponsavelDiferente = clienteService.getRowCountResponsavelDiferente(cliente.getIdCliente()).intValue();
				
				if(blResponsavelDiferente > 0) {
					cliente = clienteService.findClienteResponsavelByIdCliente(cliente.getIdCliente());
					inscricaoEstadual = inscricaoEstadualService.findByPessoaIndicadorPadrao(cliente.getIdCliente(), true);
				}

				if(conhecimento.getDevedorDocServs() != null && !conhecimento.getDevedorDocServs().isEmpty()) {
					devedorDoctoServico = conhecimento.getDevedorDocServs().get(0); 	
				} else {
    				List<DevedorDocServ> devedores = new ArrayList<DevedorDocServ>(1);
    				devedorDoctoServico = new DevedorDocServ();
    				devedorDoctoServico.setCliente(cliente);
    				devedorDoctoServico.setInscricaoEstadual(inscricaoEstadual);
    				devedores.add(devedorDoctoServico);
    				conhecimento.setDevedorDocServs(devedores);
				}

				if(blResponsavelDiferente > 0) {
                    validarEPreencherConhecimentoComResponsaveisDiferentes(conhecimento, cliente);
							}
						}
		} else {
			devedorDoctoServico = (DevedorDocServ)conhecimento.getDevedorDocServs().get(0);
			if(devedorDoctoServico == null){
				throw new BusinessException("LMS-04243");
			}else{
				
				/*Obtem o objeto cliente do DevedorDocServ*/				
				cliente = clienteService.findByIdInitLazyProperties(devedorDoctoServico.getCliente().getIdCliente(), false);
				if(cliente == null){
					throw new BusinessException("LMS-04244");
		}
				
				
			}
		}
		if(StringUtils.isBlank(cliente.getPessoa().getNrIdentificacao())) {
			throw new BusinessException("LMS-04108");
		}
		
		this.validateClienteResponsavelCadastroCompleto(cliente);
		
		/*Obtem a situacao tributaria do responsavel*/
		conhecimento.getDadosCliente().setTpSituacaoTributariaResponsavel(this.getTpSitTributariaResponsavel(devedorDoctoServico));

	}
	
	public void validateSituacaoTributariaFreteFob(String tpFrete, Long idInscricaoEstadualDestinatario) {
	    if (TP_FRETE_FOB.equals(tpFrete)) {
	    	ConteudoParametroFilial conteudoParametroFilial = parametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "BLQ_FOB_TP_SIT_TRIBU", false, true);
	        if (conteudoParametroFilial == null || conteudoParametroFilial.getVlConteudoParametroFilial().equals("S")) {
	            if(tipoTributacaoIEService.validateTipoTributacaoFOB(idInscricaoEstadualDestinatario)){
	                throw new BusinessException("LMS-02128");
	            }
	        }
	    }
    }
	
    private void validarEPreencherConhecimentoComResponsaveisDiferentes(Conhecimento conhecimento, Cliente cliente) {
        try {
            Cliente clienteBaseCalculo = this.getClienteBaseCalculo(conhecimento);
            conhecimento.setTpDevedorFrete(new DomainValue(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL));
            conhecimento.setClienteByIdClienteBaseCalculo(clienteBaseCalculo);
            validateCamposAdicionais(conhecimento);
        } catch (BusinessException e) {
            if (LMS_04065.equals(e.getMessageKey())) {
                String nrIdent = FormatUtils.formatCNPJ(cliente.getPessoa().getNrIdentificacao());
                throw new BusinessException("LMS-04298", new Object[]{nrIdent});
            } else {
                LOGGER.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

   private void validateClienteResponsavelCadastroCompleto(Cliente cliente) {
	   cliente = clienteService.findById(cliente.getIdCliente());
	   List<Object> l = new ArrayList<Object>();
	   l.add(FormatUtils.formatCpfCnpj(cliente.getPessoa().getNrIdentificacao()));
	   
	   if (!ConstantesVendas.SITUACAO_ATIVO.equals(cliente.getTpSituacao().getValue())) {
		   throw new BusinessException("LMS-04539", l.toArray());
	   }
	}

   protected Cliente getClienteResponsavel(Conhecimento conhecimento) {
        String responsavel = conhecimento.getTpDevedorFrete().getValue();
        Cliente cliente = null;
        DevedorDocServ devedorDoctoServico = null;
        if(!ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(responsavel)) {
            
            if(ConstantesExpedicao.TP_DEVEDOR_REMETENTE.equals(responsavel)) {
                cliente = conhecimento.getClienteByIdClienteRemetente();
            } else if(ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(responsavel)) {
                cliente = conhecimento.getClienteByIdClienteDestinatario();
            } else if(ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO.equals(responsavel)) {
                cliente = conhecimento.getClienteByIdClienteConsignatario();
            } else if(ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equals(responsavel)) {
                cliente = conhecimento.getClienteByIdClienteRedespacho();
            } 
            
            if(cliente != null) {
                int blResponsavelDiferente = clienteService.getRowCountResponsavelDiferente(cliente.getIdCliente()).intValue();
                
                if(blResponsavelDiferente > 0) {
                    cliente = clienteService.findClienteResponsavelByIdCliente(cliente.getIdCliente());
                }

            }
        } else {
            devedorDoctoServico = (DevedorDocServ)conhecimento.getDevedorDocServs().get(0);
            if(devedorDoctoServico == null){
                throw new BusinessException("LMS-04243");
            }else{
                
                /*Obtem o objeto cliente do DevedorDocServ*/                
                cliente = devedorDoctoServico.getCliente();
            }
        }
        
        return cliente;

    }
	
	/**
	 * Obtem o tipo de situação tributária do responsável
	 * 
     * @param devedorDoctoServico Conhecimento
	 */
	protected String getTpSitTributariaResponsavel(DevedorDocServ devedorDoctoServico){
		TipoTributacaoIE tpTributacaoIE = null;
		if (devedorDoctoServico == null || devedorDoctoServico.getInscricaoEstadual() == null || devedorDoctoServico.getInscricaoEstadual().getIdInscricaoEstadual() == null) {
			throw new BusinessException("LMS-04245");
		}
		
		/*Obtem a IE do DevedorDocServ*/
				InscricaoEstadual ie = inscricaoEstadualService.findById(devedorDoctoServico.getInscricaoEstadual().getIdInscricaoEstadual());
				if(ie == null){
					throw new BusinessException("LMS-04245");
				}				
				
		LocalDate dtVerificacaoVigencia = getDataTpSitTributariaVigenciaDoctoServico(devedorDoctoServico);
		
		
				/*Obtem a TipoTributacaoIE relacionados a IE*/				
				List<TipoTributacaoIE> list =  ie.getTiposTributacaoIe();				
		
		if (CollectionUtils.isNotEmpty(list)) {
			for (TipoTributacaoIE tipoTributacaoIE : list) {
				if (tipoTributacaoIE.getDtVigenciaFinal() == null || tipoTributacaoIE.getDtVigenciaFinal().isAfter(dtVerificacaoVigencia) 
						|| tipoTributacaoIE.getDtVigenciaFinal().isEqual(dtVerificacaoVigencia)) {
					tpTributacaoIE = tipoTributacaoIE;
				}
			}
		}
		if (tpTributacaoIE == null) {
					throw new BusinessException("LMS-04246");
				}				
		return tpTributacaoIE.getTpSituacaoTributaria().getValue();				
			}

	private LocalDate getDataTpSitTributariaVigenciaDoctoServico(DevedorDocServ devedorDoctoServico) {
		LocalDate result = JTDateTimeUtils.getDataAtual().toLocalDate();
		if (devedorDoctoServico.getDoctoServico() != null && devedorDoctoServico.getDoctoServico().getDhEmissao() != null) {
			result = devedorDoctoServico.getDoctoServico().getDhEmissao().toLocalDate();
		}
		return result;
	}

	/**
	 * Se for informada alguma dimensão, a soma dos valores da quantidade de volumes das "Notas Fiscais" e "Dimensões" deverá ser igual.
	 * Caso contrário apresentar mensagem:
	 * LMS-04201 – "Quantidades de volumes informadas nas abas Notas Fiscais e Dimensões não estão fechando."
     *
	 * @param conhecimento
	 */
	protected void validateQuantidadeVolumes(Conhecimento conhecimento) {
		List<Dimensao> dimensoes = conhecimento.getDimensoes();
		if(dimensoes == null || dimensoes.isEmpty()) {
			return;
		}
		Integer nrQtdDimensoes = IntegerUtils.ZERO;
		for (Dimensao dimensao : dimensoes) {
			//Soma quantidade das Dimensões
			nrQtdDimensoes = IntegerUtils.addNull(nrQtdDimensoes, dimensao.getNrQuantidade());
		}

		List<NotaFiscalConhecimento> notasFiscais = conhecimento.getNotaFiscalConhecimentos();
		if(notasFiscais == null || notasFiscais.isEmpty()) {
			return;
		}
		Integer nrQtdNotasFiscais = IntegerUtils.ZERO;
		for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscais) {
			//Soma quantidade das Notas Fiscais
			nrQtdNotasFiscais = IntegerUtils.addNull(nrQtdNotasFiscais, Integer.valueOf(notaFiscalConhecimento.getQtVolumes()));
		}

		if(!nrQtdDimensoes.equals(nrQtdNotasFiscais)) {
			throw new BusinessException("LMS-04201");
		}
	}

	
	/**
	 * Verificar se uma das filiais do fluxo definido (DS_FLUXO_FILIAL) possui o ID da 
	 * sua UF de endereço parametrizado no parâmetro geral “UFS_EXIGE_NFE”, 
	 * se possuir verificar se todas as notas fiscais que compõe o documento de serviço 
	 * possuem o campo NR_CHAVE preenchido, se uma ou mais notas não possuirem esta 
	 * informação visualizar a mensagem LMS-04387 retornando à aba Dados Gerais
     *
	 * @param conhecimento
	 */
	protected void validateUFPassagemDestino(Conhecimento conhecimento,CalculoFrete calculoFrete) {
		final YearMonthDay sysdate = JTDateTimeUtils.getDataAtual();
		FluxoFilial fluxo = mcdService.findFluxoEntreFiliais(conhecimento.getFilialByIdFilialOrigem().getIdFilial(), 
															conhecimento.getFilialByIdFilialDestino().getIdFilial(), 
															calculoFrete.getIdServico(),sysdate);
		
		if(fluxo!=null && fluxo.getDsFluxoFilial()!=null) {
			String[] filiais = fluxo.getDsFluxoFilial().split("-");
			String parametroGeral = this.parametroGeralService.findSimpleConteudoByNomeParametro("UFS_EXIGE_NFE");
			String idEmpresaMercurio = this.parametroGeralService.findSimpleConteudoByNomeParametro("ID_EMPRESA_MERCURIO");
			String[] idsUFs=null;
			
			if(parametroGeral!=null) {
				idsUFs = parametroGeral.split(";");
			}
			boolean validado = false;
			if(filiais!=null && idsUFs!=null) {
				Filial filial = null;
				
				for(String item : filiais) {
					filial = this.filialService.findFilial(Long.valueOf(idEmpresaMercurio), item.trim());
					filial = this.filialService.findByIdJoinPessoa(filial.getIdFilial());
					
					if(filial!=null){
						EnderecoPessoa endereco = filial.getPessoa().getEnderecoPessoa();
						endereco = this.enderecoPessoaService.findById(endereco.getIdEnderecoPessoa());
						
						for(String id : idsUFs) {
							if(id.trim().equals(endereco.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa().toString())) {
								
								if(!this.validateNrChaveNotasDocumento(conhecimento)) {
									throw new BusinessException("LMS-04387");
								}
								
								validado = true;
								break;
								
							}
							
						}
					}
					if(validado) {
						break;
					}
				}
			}
		}
	}


	private boolean validateNrChaveNotasDocumento(Conhecimento conhecimento) {
		List<NotaFiscalConhecimento> nfs = conhecimento.getNotaFiscalConhecimentos();

		if(nfs!=null && !nfs.isEmpty()) {

			for(NotaFiscalConhecimento nf : nfs) {
				if(nf.getNrChave()==null || "".equals(nf.getNrChave().trim())) {
					return false;
				}
			}

		}

		return true;

	}

	/**
	 * Regra ValidaCTRC.6
	 * Validação da informação dos Serviços Adicionais:
	 * - Se o campo "Serviços Adicionais" (DoctoServico.blServicosAdicionais) for informado é preciso que
	 * seja informado pelo menos um serviço adicional (DoctoServico.servAdicionalDocServs),
	 * se nenhum serviço estiver informado visualizar mensagem LMS-04062;
	 * - Pelos menos um dos campos "Frete" (DoctoServico.blParcelas)
	 * e "Serviços Adicionais" (DoctoServico.blServicosAdicionais) deve estar preenchido,
	 * caso contrário visualizar mensagem LMS-04063.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 02/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateServicosAdicionais(Conhecimento conhecimento) {
		if(Boolean.TRUE.equals(conhecimento.getBlServicosAdicionais())
                && (conhecimento.getServAdicionalDocServs() == null || conhecimento.getServAdicionalDocServs().isEmpty())) {
			throw new BusinessException("LMS-04062");
        }
		if(Boolean.FALSE.equals(conhecimento.getBlServicosAdicionais())
                && Boolean.FALSE.equals(conhecimento.getBlParcelas())) {
			throw new BusinessException("LMS-04063");
	}
    }

	/**
	 * Regra ValidaCTRC.7 e ValidaCTRC.11
	 * Validação e Geração do Local de Entrega:
	 * - Se o Redespacho foi informado e este possuir mais de um endereço de entrega,
	 * algum local de entrega deve ter sido selecionado na popup Local de Entrega,
	 * caso nenhum local tenha sido selecionado visualizar mensagem LMS-04064;
	 * - Se o Redespacho foi informado e este possuir apenas um endereço de entrega e nenhum outro endereço
	 * de entrega for informado, os dados do endereço de entrega cadastrado no cliente Redespacho
	 * devem ser considerados como Local de Entrega;
	 * - Caso o Redespacho não tenha sido informado, verificar se o Destinatário possui mais de um endereço
	 * de entrega. Se ele possuir, algum local de entrega deve ter sido selecionado na popup Local de Entrega,
	 * caso nenhum local tenha sido selecionado visualizar mensagem LMS-04064;
	 * - Se o Redespacho não foi informado, o Destinatário possuir apenas um endereço de entrega
	 * e nenhum outro endereço de entrega for informado, os dados do endereço de entrega cadastrado
	 * no cliente Destinatário devem ser considerados como Local de Entrega.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 05/12/2005
     *
	 * @param conhecimento
	 */
	protected void setLocalEntrega(Conhecimento conhecimento) {
		/** Verifica se foi informado um endereco de entrega pela Popup */
		if(conhecimento.getDsEnderecoEntrega() == null) {
			Cliente cliente = null;
			if(conhecimento.getClienteByIdClienteConsignatario() != null) {
				cliente = conhecimento.getClienteByIdClienteConsignatario();
			} else {
				cliente = conhecimento.getClienteByIdClienteDestinatario();
			}

			List<Long> ids = new ArrayList<Long>(1);
			ids.add(cliente.getIdCliente());
			List enderecos = enderecoPessoaService.findByIdPessoaTpEnderecoLocalEntrega(ids, "ENT");

			/** Caso exista mais de um endereco de Entrega deve ser informado um */

//			11 - Se o destinatário, ou o Redespacho quando este for informado, tiverem mais que
//			um endereço de entrega, deverá obrigatoriamente, ter sido informado um local de
//			entrega na popup Local de Entrega, caso não tenha sido informado visualizar
//			mensagem LMS-04068, retornando à aba Dados Gerais.

			if(enderecos.size() > 1) {
				throw new BusinessException("LMS-04064");
			}

			/** Caso cliente so possua um endereco, seta o mesmo */
			if(enderecos.size() == 1) {
				Map endereco = (Map)enderecos.get(0);

				EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
				enderecoPessoa.setIdEnderecoPessoa(MapUtils.getLong(endereco, "idEnderecoPessoa"));
				conhecimento.setEnderecoPessoa(enderecoPessoa);

				UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
				unidadeFederativa.setIdUnidadeFederativa(MapUtils.getLong(endereco, "idUnidadeFederativa"));
				unidadeFederativa.setSgUnidadeFederativa(MapUtils.getString(endereco, "sgUnidadeFederativa"));

				Municipio municipio = new Municipio();
				municipio.setIdMunicipio(MapUtils.getLong(endereco, "idMunicipio"));
				municipio.setNmMunicipio(MapUtils.getString(endereco, "nmMunicipio"));
				municipio.setUnidadeFederativa(unidadeFederativa);
				conhecimento.setMunicipioByIdMunicipioEntrega(municipio);

				conhecimento.setDsBairroEntrega(MapUtils.getString(endereco, "dsBairro"));
				conhecimento.setDsComplementoEntrega(MapUtils.getString(endereco, "dsComplemento"));

				TipoLogradouro tipoLogradouro = new TipoLogradouro();
				tipoLogradouro.setIdTipoLogradouro(MapUtils.getLong(endereco, "idTipoLogradouro"));
				tipoLogradouro.setDsTipoLogradouro(new VarcharI18n(MapUtils.getString(endereco, "dsTipoLogradouro")));
				conhecimento.setTipoLogradouroEntrega(tipoLogradouro);

				conhecimento.setDsEnderecoEntrega(MapUtils.getString(endereco, "dsEndereco"));
				conhecimento.setNrEntrega(MapUtils.getString(endereco, "nrEndereco"));
				String nrCep = MapUtils.getString(endereco, "nrCep");
				if(StringUtils.isNotBlank(nrCep)) {
					conhecimento.setNrCepEntrega(nrCep);
				}
			}
		}
		/** Seta Endereco de Entrega Real do Conhecimneto */
		if (StringUtils.isNotBlank(conhecimento.getDsEnderecoEntrega())) {
			ConhecimentoUtils.setEnderecoEntregaReal(conhecimento);
		} else {
			 //Caso Endereco nao informado, busca o endereco padrao do Redespacho ou Destinatario
			 //para preencher o Endereco de Entrega Real

			// CJS - 17/07/2009 - Nova Regra conforme Especificação Técnica:
			/* 	Se foi informado um cliente de redespacho consignatário na popup Redespacho
				Consignatário, o município de destino será o município do endereço padrão do
				cliente de redespacho consignatário informado na popup
				(CONHECIMENTO.ID_MUNICIPIO_ENTREGA =
				ENDERECO_PESSOA.ID_MUNICIPIO) e o CEP de entrega será o CEP informado na popup
			*/
			Cliente cliente = null;
			if(conhecimento.getClienteByIdClienteConsignatario() != null) {
				cliente = conhecimento.getClienteByIdClienteConsignatario();
			} else {
				cliente = conhecimento.getClienteByIdClienteDestinatario();
			}
			EnderecoPessoa enderecoEntrega = enderecoPessoaService.findByIdPessoa(cliente.getIdCliente());
			cliente.getPessoa().setEnderecoPessoa(enderecoEntrega);
			conhecimento.setMunicipioByIdMunicipioEntrega(enderecoEntrega.getMunicipio());
			conhecimento.setNrCepEntrega(enderecoEntrega.getNrCep());
			ConhecimentoUtils.setEnderecoEntregaReal(conhecimento, enderecoEntrega);
		}
	}

	/**
	 * Regra ValidaCTRC.8
	 * Verifica a informação dos Campos Adicionais obrigatórios:
	 * - Se existirem registros na tabela INFORMACAO_DOCTO_CLIENTE pertencentes ao Remetente
	 * informado (CLIENTE.ID_CLIENTE = INFORMACAO_DOCTO_CLIENTE.ID_ CLIENTE), que tenham o mesmo modal
	 * e abrangência do tipo do serviço que está sendo executado
	 * (SERVIVO.TP_MODAL = INFORMACAO_DOCTO_CLIENTE.TP_MODAL
	 * e SERVICO.TP_ABRANGENCIA = INFORMACAO_DOCTO_CLIENTE.TP_ ABRANGENCIA) ou estes estejam em branco,
	 * verificar para cada linha gerada na aba Campos Adicionais (Conhecimento.dadosCoplementos)
	 * a obrigatoriedade da informação (INFORMACAO_DOCTO_ CLIENTE.BL_OPCIONAL = "N").
	 * Se for obrigatório e a informação não estiver digitada visualizar mensagem LMS-04065.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 05/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateCamposAdicionais(Conhecimento conhecimento) {

		//LMS-4068 / Conhecimento Substituto não deve validar campos adicionais / ET 04.01.01.01N4 Digitar conhecimento substituto.docx
		if(conhecimento.getTpConhecimento() != null && ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equals( conhecimento.getTpConhecimento().getValue()) ){
			return;
		}

		Cliente remetente = conhecimento.getClienteByIdClienteRemetente();
		Cliente destinatario = conhecimento.getClienteByIdClienteDestinatario();
		Cliente responsavelFrete = conhecimento.getClienteByIdClienteBaseCalculo();

		List<Long> idObrigatorios = new ArrayList<Long>();

		
		if(!BooleanUtils.isTrue(conhecimento.getBlProcessamentoTomador()) && remetente.getTpCliente() != null && ClienteUtils.isParametroClienteEspecial(remetente.getTpCliente().getValue())) {
			List list = informacaoDoctoClienteService.findDadosByClienteTpModalTpAbrangencia(
				remetente.getIdCliente(),
				conhecimento.getServico().getTpModal().getValue(),
				conhecimento.getServico().getTpAbrangencia().getValue());
			if(list != null && !list.isEmpty()){
                addInformacaoObrigatoria(idObrigatorios, list);
			}
		}

		if(destinatario.getTpCliente() != null
				&& ClienteUtils.isParametroClienteEspecial(destinatario.getTpCliente().getValue())){
			List list = informacaoDoctoClienteService.findDadosByClienteTpModalTpAbrangencia(
					destinatario.getIdCliente(),
					conhecimento.getServico().getTpModal().getValue(),
					conhecimento.getServico().getTpAbrangencia().getValue());
			if(list != null && !list.isEmpty()){
				for(int i = 0; i < list.size(); i++){
					Map map = (Map)list.get(i);
					Boolean blDestinatario = (Boolean)map.get("blDestinatario");
					Boolean blOpcional = (Boolean)map.get("blOpcional");
					if(blDestinatario && !blOpcional){
						idObrigatorios.add((Long)map.get("idInformacaoDoctoCliente"));
					}
				}
			}
		}

		if(responsavelFrete.getTpCliente() != null
				&& ClienteUtils.isParametroClienteEspecial(responsavelFrete.getTpCliente().getValue())){
			List list = informacaoDoctoClienteService.findDadosByClienteTpModalTpAbrangencia(
					responsavelFrete.getIdCliente(),
					conhecimento.getServico().getTpModal().getValue(),
					conhecimento.getServico().getTpAbrangencia().getValue());
			if(list != null && !list.isEmpty()){
				for(int i = 0; i < list.size(); i++){
					Map map = (Map)list.get(i);
					Boolean blDevedor = (Boolean)map.get("blDevedor");
					Boolean blOpcional = (Boolean)map.get("blOpcional");
					if(blDevedor && !blOpcional){
						idObrigatorios.add((Long)map.get("idInformacaoDoctoCliente"));
					}
				}
			}
		}

		if(idObrigatorios.size() > 0){
			List<DadosComplemento> dados = conhecimento.getDadosComplementos();
			if(!idObrigatorios.isEmpty() && (dados == null || dados.isEmpty())){
                throw new BusinessException(LMS_04065);
			}
			for(Long idInformacaoObrigatorio : idObrigatorios){
				boolean blFound = false;
				for(DadosComplemento dadosComplemento : dados){
					if(dadosComplemento.getInformacaoDoctoCliente() != null
						&& dadosComplemento.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente().equals(idInformacaoObrigatorio)
						&& StringUtils.isNotBlank(dadosComplemento.getDsValorCampo())){
						blFound = true;
						break;
					}
				}
				if(!blFound){
                    throw new BusinessException(LMS_04065);
				}
			}
		}
	}

    private void addInformacaoObrigatoria(List<Long> idObrigatorios, List list) {
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            Boolean blRemetente = (Boolean) map.get("blRemetente");
            Boolean blOpcional = (Boolean) map.get("blOpcional");
            if (blRemetente && !blOpcional) {
                idObrigatorios.add((Long) map.get("idInformacaoDoctoCliente"));
            }
        }
    }

	/**
	 * Regra ValidaCTRC.9
	 * Validação da densidade e peso cubado:
	 * - Se o campo densidade estiver preenchido com valor diferente de "A",
	 * o campo peso cubado não poderá estar preenchido, se estiver preenchido visualizar mensagem LMS-04066.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 05/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateDensidadePesoCubado(Conhecimento conhecimento) {
		BigDecimal psCubado = conhecimento.getPsAforado();
		if(!"A".equals(conhecimento.getDensidade().getTpDensidade().getValue())
			&& psCubado != null) {
			throw new BusinessException("LMS-04066");
		}
		//Se nao foi informado peso cubado utilizar o mesmo valor do peso real
		if(psCubado == null) {
			conhecimento.setPsAforado(conhecimento.getPsReal());
		}
	}

	/**
	 * Regra ValidaCTRC.10
	 * - Se o campo Tipo de CTRC (Conhecimento.tpConhecimento) for preenchido com
	 * Refaturamento (RF) ou Devolução Parcial (DP, deverá ter sido, obrigatoriamente, informado o CTRC original,
	 * caso o mesmo não tenha sido informado visualizar mensagem LMS-04067.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 06/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateDoctoServicoOriginal(Conhecimento conhecimento) {
		String tpConhecimento = conhecimento.getTpConhecimento().getValue();
		if(("RF".equals(tpConhecimento) || "DP".equals(tpConhecimento))
			&& conhecimento.getDoctoServicoOriginal() == null) {
			throw new BusinessException("LMS-04067");
		}
	}

	/**
	 * Regra ValidaCTRC.30
	 * Validação do Contratante:
	 * - Se o existir "ExcecaoICMSCliente" e (ExcecaoICMSCliente.blSubcontratacao) for TRUE
	 * é preciso que seja informado o (Conhecimento.nrCtrcSubcontratante),
	 * caso nao tenha sido informado visualizar a mensagem LMS-04197;
	 * - Se nao existir "ExcecaoICMSCliente" ou (ExcecaoICMSCliente.blSubcontratacao) for FALSE
	 * nao dese ser informado o (Conhecimento.nrCtrcSubcontratante),
	 * caso tenha sido informado visualizar a mensagem LMS-04198;
     * <p>
     * autor Andre Valadas
	 *
	 * @param conhecimento
	 */
	protected void validateCTRCSubcontratante(Conhecimento conhecimento) {

		/*Obtem o cliente responsavel através da lista de DevedorDocServ*/
		Cliente clienteResponsavel = null;
		Boolean isMultioperacao = null;
		if(conhecimento.getDevedorDocServs() != null && !conhecimento.getDevedorDocServs().isEmpty()){
			clienteResponsavel = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();			
		}

		/*Tenta obter a Exceção Icms Cliente, se existir*/
		ExcecaoICMSCliente excecaoICMSCliente = null;
		if(clienteResponsavel != null && clienteResponsavel.getPessoa() != null
				&& conhecimento.getClienteByIdClienteRemetente() != null && conhecimento.getClienteByIdClienteRemetente().getPessoa() != null
								&& conhecimento.getFilialByIdFilialOrigem() != null && conhecimento.getFilialByIdFilialOrigem().getPessoa() != null){

			Long idUfOrigem = conhecimento.getFilialByIdFilialOrigem().getPessoa()
				.getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();

			String nrIdRemetente   = conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao();
			String nrIdResponsavel = clienteResponsavel.getPessoa().getNrIdentificacao();

			if(LongUtils.hasValue(idUfOrigem)
					&& StringUtils.isNotBlank(nrIdRemetente) && StringUtils.isNotBlank(nrIdResponsavel)){
				excecaoICMSCliente = excecaoICMSClienteService.findByUK(nrIdResponsavel,nrIdRemetente,conhecimento.getTpFrete().getValue(),idUfOrigem,JTDateTimeUtils.getDataAtual());
			}
			
			isMultioperacao = calcularIcmsService.isTomadorSubcontratacaoMultioperacao(nrIdResponsavel);
		}
		
		if (excecaoICMSCliente != null && Boolean.TRUE.equals(excecaoICMSCliente.getBlSubcontratacao())) {
	        if (StringUtils.isBlank(conhecimento.getNrCtrcSubcontratante()) && !isMultioperacao) {
	            throw new BusinessException("LMS-04197");
	        }
		} else {
			if (StringUtils.isNotBlank(conhecimento.getNrCtrcSubcontratante())) {
				throw new BusinessException("LMS-04198");
			}
		}
	}

	/**
	 * Regra ValidaCTRC.13
	 * Identificação do município de coleta:
	 * - Se o município do remetente é atendido pela filial do usuário logado,
	 * o município de origem é o próprio município do cliente remetente
	 * (CONHECIMENTO.ID_MUNICIPIO_COLETA = ENDERECO_PESSOA.ID_MUNICIPIO para o
	 * município do remetente);
	 * - Se o município do remetente não é atendido pela filial do usuário logado,
	 * o município de origem é o município onde está localizada esta filial
	 * (CONHECIMENTO.ID_MUNICIPIO_COLETA = ENDERECO_PESSOA.ID_MUNICIPIO
	 * da filial do usuário logado).
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 06/12/2005
     *
	 * @param conhecimento
	 */
	protected void setLocalColeta(Conhecimento conhecimento) {
		Long idServico = conhecimento.getServico().getIdServico();
		String tpModal = conhecimento.getServico().getTpModal().getValue();
		Long idMunicipioColeta = conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio();

		Filial filial = obtemFilial(conhecimento.getDadosCliente());
		Long idFilialSessao = filial.getIdFilial();
		Municipio municipioFilialOrigem = filial.getPessoa().getEnderecoPessoa().getMunicipio();


		String tpModoPedidoColeta = conhecimento.getPedidoColeta() != null ? conhecimento.getPedidoColeta().getTpModoPedidoColeta().getValue() : null;
		/**
		 * LMS-5597
		 * Se o campo Tipo de CTRC for preenchido com Refaturamento:
		 * OU
		 * A coleta for do tipo “Balcão” (PEDIDO_COLETA.TP_MODO_PEDIDO_ COLETA = “BA”) então:
		 * 		O município de origem é o município de endereço da filial do usuário logado (ENDERECO_PESSOA.ID_MUNICIPIO para PESSOA.ID_PESSOA = Id da filial do usuário logado e ENDERECO_PESSOA.ID_ENDERECO_PESSOA = PESSOA.ID_ENDERECO_PESSOA)
		*/
		if(CONHECIMENTO_REFATURAMENTO.equals(conhecimento.getTpConhecimento().getValue()) || ( tpModoPedidoColeta != null && "BA".equals(tpModoPedidoColeta))) {
		    String nrIdentificacao = conhecimento.getDevedorDocServs().get(0).getCliente().getPessoa().getNrIdentificacao().substring(0, 8);
		    boolean isCnpjSubcontratacaoFedex = isCnpjSubcontratacaoFedex(nrIdentificacao);
		    
		    if(!isCnpjSubcontratacaoFedex || (isCnpjSubcontratacaoFedex && conhecimento.getNrCtrcSubcontratante() == null)){
			    municipioFilialOrigem = filial.getPessoa().getEnderecoPessoa().getMunicipio();
			    conhecimento.setMunicipioByIdMunicipioColeta(municipioFilialOrigem);
			}
		} else {
			/**
			 * LMS-5597
			 *	Caso a coleta for do tipo “Telefone” (PEDIDO_COLETA.TP_MODO_PEDIDO_ COLETA = “TE”) ou se a coleta for do tipo
			 *	“Automática” (PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA = “AU”), o município de origem é o muncipio do pedido de coleta (PEDIDO_COLETA.ID_MUNICIPIO).
			 */
			if ("TE".equals(tpModoPedidoColeta) || "AU".equals(tpModoPedidoColeta)){
				municipioFilialOrigem = conhecimento.getPedidoColeta().getMunicipio();
				conhecimento.setMunicipioByIdMunicipioColeta(municipioFilialOrigem);
			}
		}

		if(isColetaOutOfOrigem(conhecimento, filial)){
			throw new BusinessException("LMS-04499");
		}

		// Requisito adicional: Gravar o país da filial no docto de servico
		conhecimento.setPaisOrigem(municipioFilialOrigem.getUnidadeFederativa().getPais());

		Long idClienteAtendido = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
		String nrCepColeta = conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getNrCep();

		Map<String, Object> atendimento = ppeService.findAtendimentoMunicipio(
			idMunicipioColeta,
			idServico,
			Boolean.TRUE,
			JTDateTimeUtils.getDataAtual(),
			nrCepColeta,
			idClienteAtendido,
			null,
			null,
			null,
			null,
			"N" ,
			conhecimento.getNaturezaProduto().getIdNaturezaProduto(),
			null
		);
		Long idFilialAtendida = (Long)atendimento.get("idFilial");
		Long idTipoLocalizacao = (Long)atendimento.get("idTipoLocalizacaoMunicipio");

		if(!idFilialSessao.equals(idFilialAtendida) && ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal)) {
			new WarningCollector(configuracoesFacade.getMensagem("LMS-04203"));
		}

		conhecimento.setNrCepColeta(nrCepColeta);
		conhecimento.getDadosCliente().setIdTipoLocalizacaoOrigem(idTipoLocalizacao);
	}

    private boolean isCnpjSubcontratacaoFedex(String nrIdentificacao) {
        String cnpjSubcontratacaoFedex = (String)configuracoesFacade.getValorParametro("CNPJ_SUBCONTRATACAO_FDX");
        
        if (nrIdentificacao != null && cnpjSubcontratacaoFedex != null && nrIdentificacao.equals(cnpjSubcontratacaoFedex)){
            return true;
        }
        return false;
    }

	/**
	 * @param conhecimento
	 * @param filialLogada
	 * @return
	 */
	private boolean isColetaOutOfOrigem(Conhecimento conhecimento, Filial filialLogada){
		Long idServico = conhecimento.getServico().getIdServico();
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		Long idClienteRemetente = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
		Long idMunicipioRemetente = conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
		String nrCepRemetente = conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getNrCep();

		ConteudoParametroFilial blqColetaForaOrigem = parametroFilialService.findByNomeParametro(filialLogada.getIdFilial(),ConstantesExpedicao.NM_PARAMETRO_BLQ_COLETA_OUT_ORIG,false,true);


		if((conhecimento.getTpConhecimento().equals(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NORMAL)) && conhecimento.getClienteByIdClienteRedespacho() == null)
			&& (blqColetaForaOrigem != null && blqColetaForaOrigem.getVlConteudoParametroFilial().equals("S"))){

			Map<String, Object> atendimentoRemetente = ppeService.findAtendimentoMunicipio(
					idMunicipioRemetente,
					idServico,
					Boolean.TRUE,
					dataAtual,
					nrCepRemetente,
					idClienteRemetente,
					null,
					null,
					null,
					null,
					null,
					null,
					null
				);

			Long idFilialRemetente = (Long)atendimentoRemetente.get("idFilial");
			if(filialLogada.getIdFilial().longValue() != idFilialRemetente.longValue()){
				return true;
			}
		}

		return false;
	}

	/**
	 * Regra ValidaCTRC.14
	 * Identificação da filial de atendimento do município de destino:
	 *	- Chamar a rotina find Filial de Atendimento do Município (PpeService) passando os seguintes parâmetros:
	 *		• Município = Município definido no item acima
	 *		• Serviço = conforme campo Tipo de serviço no DoctoServico
	 *		• Indicador de coleta = Boolean.FALSE
	 *		• Data de consulta = data corrente
	 *		• CEP = CEP de entrega
	 *		• Cliente Atendido = conforme campo do Redespacho ou Destinatário no DoctoServico
	 *		• Segmento = CLIENTE.ID_SEGMENTO_MERCADO do cliente acima
	 *		• Cliente = conforme campo Remetente no DoctoServico
	 *		• UF de origem = UF da filial do usuário logado
	 *		• Filial de origem = Filial do usuário logado
	 *	- Se a rotina retornar algum erro visualizar mensagem LMS-04083;
	 *	- Deverá ser visualizada, no campo Filial de Destino, a sigla da filial de atendimento do
	 *	município definido como município de destino;
	 *	- Para definir se a filial obtida tem atendimento pela Mercúrio verificar se o tipo de empresa
	 *	(EMPRESA.TP_EMPRESA = "M") para a empresa da filial (EMPRESA.ID_EMPRESA = FILIAL.ID_EMPRESA) é Mercúrio.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 06/12/2005
     *
	 * @param conhecimento
	 */
	protected void setFilialAtendimentoMunicipioDestino(Conhecimento conhecimento) {

		Municipio municipioFilialOrigem = obtemFilial(conhecimento.getDadosCliente()).getPessoa().getEnderecoPessoa().getMunicipio();
		conhecimento.getDadosCliente().setIdUfFilialUsuario(municipioFilialOrigem.getUnidadeFederativa().getIdUnidadeFederativa());

		Map<String, Object> atendimento = getAtendimentoMunicipio(conhecimento);

		Long idFilialDestino = (Long)atendimento.get("idFilial");
		Long idTipoLocalizacao = (Long)atendimento.get("idTipoLocalizacaoMunicipio");

		Filial filialDestino = filialService.findByIdInitLazyProperties(idFilialDestino, false);
		filialService.validateExisteCodFilial(filialDestino);
		conhecimento.setFilialByIdFilialDestino(filialDestino);
		conhecimento.setFilialDestinoOperacional(filialDestino);
		conhecimento.getDadosCliente().setIdTipoLocalizacaoDestino(idTipoLocalizacao);
	}

	/**
	 * Regra ValidaCTRC.15
	 * - Se o cliente remetente estiver marcado com BL_ACEITA_FOB_GERAL não
	 * realizar as validações deste item.
	 * - Se o município de destino acima definido não for um município atendido pela empresa
	 * (verificar rotina de verificação de município atendido - Regra ValidaCTRC.14),
	 * o sistema só poderá aceitar o tipo frete "FOB" (F) para o modal aéreo (A).
	 * - Caso seja informado frete "FOB" para um modal diferente do aéreo, visualizar mensagem LMS-04069.
	 * - Caso o município de destino acima definido seja um município atendido pela
	 * empresa, o sistema só poderá aceitar o tipo frete “FOB” para filiais diferentes das
	 * filiais definidas no PARAMETRO_GERAL = “FILIAL_SEM_FOB”, caso contrário
	 * visualizar mensagem LMS-04289, retornando à aba Dados Gerais.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 08/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateAtendimentoEmpresaTipoFrete(Conhecimento conhecimento) {
		Cliente clienteRemetente = conhecimento.getClienteByIdClienteRemetente();
		if (Boolean.TRUE.equals(clienteRemetente.getBlAceitaFobGeral())) {
			return;
		}

		if(ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimento.getTpFrete().getValue())){
			String valorParametro = (String)configuracoesFacade.getValorParametro("FILIAL_SEM_FOB");
			String strIdFilial = conhecimento.getFilialByIdFilialDestino().getIdFilial().toString();
			String[] strIdsFilial = valorParametro.split(";");

        	for (int i = 0; i < strIdsFilial.length; i++) {
				if ( strIdsFilial[i].trim().equals(strIdFilial) ) {
					throw new BusinessException("LMS-04289");
	}
        	}
		}
	}

	/**
	 * Regra ValidaCTRC.16
	 * Verificar atendimento do Município de destino:
	 * - Quando município de destino não for atendido pela Mercúrio
	 * (filial que atende não é do tipo 'M' – Mercúrio), e o cliente (responsável pelo frete) não for
	 * parametrizado com Parceira de Redespacho e o modal for Rodoviário (R), deverá ser
	 * informado um Redespacho, caso contrário visualizar mensagem LMS-04070;
	 * - Quando município de destino não for atendido pela Mercúrio
	 * (filial que atende não é do tipo 'M' – Mercúrio), o cliente (responsável pelo frete)
	 * não for parametrizado com 'Parceira de Redespacho' (conforme regra acima), e o modal for Aéreo (A),
	 * deverá ser informado um Redespacho ou um Consignatário caso contrário visualizar mensagem LMS-04071.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 17/03/2006
     *
	 * @param conhecimento
	 */
	protected void validateAtendimentoMunicipioDestino(Conhecimento conhecimento, CalculoFrete calculoFrete) {

		EnderecoPessoa enderecoPessoa = null;
		Pessoa pessoa = conhecimento.getClienteByIdClienteRemetente().getPessoa();
		Long idPessoa = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
		if(pessoa != null) {
			enderecoPessoa = pessoa.getEnderecoPessoa();
		} else {
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
		}

		//*** Restrição Rota Origem
		RestricaoRota restricaoRotaOrigem = calculoFrete.getRestricaoRotaOrigem();
		restricaoRotaOrigem.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		if(conhecimento.getAeroportoByIdAeroportoOrigem() != null) {
			restricaoRotaOrigem.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}
		if (conhecimento.getFilialByIdFilialOrigem() != null) {
			restricaoRotaOrigem.setIdFilial(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
		}
		Municipio municipioOrigem  = conhecimento.getMunicipioByIdMunicipioColeta();
		if (municipioOrigem == null) {
			municipioOrigem = enderecoPessoa.getMunicipio();
		}
		restricaoRotaOrigem.setIdMunicipio(municipioOrigem.getIdMunicipio());
		restricaoRotaOrigem.setIdPais(municipioOrigem.getUnidadeFederativa().getPais().getIdPais());
		restricaoRotaOrigem.setIdUnidadeFederativa(municipioOrigem.getUnidadeFederativa().getIdUnidadeFederativa());
		restricaoRotaOrigem.setIdZona(municipioOrigem.getUnidadeFederativa().getPais().getZona().getIdZona());


		//*** Restrição Rota Destino
		Cliente clienteDestinatario = conhecimento.getClienteByIdClienteDestinatario();
		enderecoPessoa = clienteDestinatario.getPessoa().getEnderecoPessoa();
		RestricaoRota restricaoRotaDestino = calculoFrete.getRestricaoRotaDestino();
		if (conhecimento.getFilialByIdFilialDestino() != null) {
			restricaoRotaDestino.setIdFilial(conhecimento.getFilialByIdFilialDestino().getIdFilial());
		}
		Municipio municipioDestino  = conhecimento.getMunicipioByIdMunicipioEntrega();
		if (municipioDestino == null) {
			municipioDestino = enderecoPessoa.getMunicipio();
		}
		restricaoRotaDestino.setIdMunicipio(municipioDestino.getIdMunicipio());
		UnidadeFederativa ufDestino = unidadeFederativaService.findById(municipioDestino.getUnidadeFederativa().getIdUnidadeFederativa());
		restricaoRotaDestino.setIdUnidadeFederativa(ufDestino.getIdUnidadeFederativa());
		restricaoRotaDestino.setIdPais(ufDestino.getPais().getIdPais());
		restricaoRotaDestino.setIdZona(ufDestino.getPais().getZona().getIdZona());

		if(conhecimento.getAeroportoByIdAeroportoDestino() != null) {
			restricaoRotaDestino.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}

		/*
		 * Colocado essa validação pois ainda não ha divisão nessa fase e essa validação é chamada
		 * na validação da segunda fase também, onde já existe uma divisão selecionada.
		 */
		if( !BooleanUtils.isTrue(conhecimento.getBlIndicadorEdi())){
		validateCotMac(conhecimento, calculoFrete);
	}
	}

	protected void validateCotMac(Conhecimento conhecimento, CalculoFrete calculoFrete) {

		/*
		Para verificar se um cliente tem COT/MAC executar a rotina “Obter Rota Preço
		Cliente”, enviando os parâmetros necessários, se a rotina retornar nenhum
		ID, o cliente não tem Cot/Mac. Caso a rotina retorne algum id, acessar a tabela
		PARAMETRO_CLIENTE pelo id retornado, se o campo ID_CLIENTE_REDESPACHO
		ou o campo ID_FILIAL_MERCURIO_REDESPACHO não estiver preenchido, o
		cliente não tem Cot/Mac.
		*/
		if(conhecimento.getClienteByIdClienteConsignatario() == null) {
		String tpEmpresa = conhecimento.getFilialByIdFilialDestino().getEmpresa().getTpEmpresa().getValue();

			ParametroCliente parametroCliente = null;
			conhecimento.getDivisaoCliente();
			parametroCliente = calculoFreteService.findParametroCliente(
					calculoFrete.getIdDivisaoCliente(),
					calculoFrete.getIdServico(),
					calculoFrete.getRestricaoRotaOrigem(),
					calculoFrete.getRestricaoRotaDestino());

		if(!ConstantesExpedicao.TP_EMPRESA_MERCURIO.equals(tpEmpresa)) {
				if(parametroCliente == null || parametroCliente.getClienteByIdClienteRedespacho() == null || parametroCliente.getFilialByIdFilialMercurioRedespacho() ==null) {
				String tpModal = conhecimento.getServico().getTpModal().getValue();

					final YearMonthDay sysdate = JTDateTimeUtils.getDataAtual();
					FluxoFilial fluxoFilial = fluxoFilialService.findFluxoFilial(conhecimento.getFilialByIdFilialOrigem().getIdFilial(), conhecimento.getFilialByIdFilialDestino().getIdFilial(), sysdate, calculoFrete.getIdServico());

					while(fluxoFilial.getFilialByIdFilialParceira() == null && fluxoFilial.getFilialByIdFilialReembarcadora() != null){
						fluxoFilial = fluxoFilialService.findFluxoFilial(fluxoFilial.getFilialByIdFilialReembarcadora().getIdFilial(), conhecimento.getFilialByIdFilialDestino().getIdFilial(), sysdate, calculoFrete.getIdServico());
					}

					if((fluxoFilial == null || fluxoFilial.getFilialByIdFilialParceira() == null)
						&& ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal) ) {
					throw new BusinessException("LMS-04070");
					} else if((fluxoFilial == null || fluxoFilial.getFilialByIdFilialParceira() == null)
							&& ConstantesExpedicao.MODAL_AEREO.equals(tpModal) ) {
					throw new BusinessException("LMS-04071");
					} else {
						Cliente cliente = clienteService.findById(fluxoFilial.getFilialByIdFilialParceira().getPessoa().getIdPessoa());
						if(cliente != null) {
						conhecimento.setClienteByIdClienteConsignatario(cliente);
							conhecimento.setNrCepEntrega(cliente.getPessoa().getEnderecoPessoa().getNrCep());

							InformacaoDocServico informacaoDocServico = informacaoDocServicoService.findInformacaoDoctoServico(IE_CONSIGNATARIO, IE_CONSIGNATARIO);
							informacaoDocServico.setDsCampo(IE_CONSIGNATARIO);
							DadosComplemento dadosComplemento = new DadosComplemento();
							dadosComplemento.setInformacaoDocServico(informacaoDocServico);
							String ie = getInscricaoEstadualPessoa(cliente.getPessoa());
							dadosComplemento.setDsValorCampo(ie);
							conhecimento.addDadoComplemento(dadosComplemento);
				}
					}
				} else {
					Cliente cliente = clienteService.findById(parametroCliente.getClienteByIdClienteRedespacho().getIdCliente());
					if(cliente != null) {
						conhecimento.setClienteByIdClienteConsignatario(cliente);
						InformacaoDocServico informacaoDocServico = informacaoDocServicoService.findInformacaoDoctoServico(IE_CONSIGNATARIO, IE_CONSIGNATARIO);
						informacaoDocServico.setDsCampo(IE_CONSIGNATARIO);
						DadosComplemento dadosComplemento = new DadosComplemento();
						dadosComplemento.setInformacaoDocServico(informacaoDocServico);
						String ie = getInscricaoEstadualPessoa(cliente.getPessoa());
						dadosComplemento.setDsValorCampo(ie);
						conhecimento.addDadoComplemento(dadosComplemento);
			}
		}
	}
	}
	}

	private String getInscricaoEstadualPessoa(Pessoa pessoa) {
		List<InscricaoEstadual> inscricoes = pessoa.getInscricaoEstaduais();
		if(inscricoes != null) {
			for (InscricaoEstadual inscricao : inscricoes) {
				if(inscricao.getBlIndicadorPadrao()) {
					return inscricao.getNrInscricaoEstadual();
				}
			}
		}
		return null;
	}

	/**
	 * Regra ValidaCTRC.17
	 * Validação dos dados da cotação (Quando tipo de cálculo igual à Cotação (C)):
	 *	- Se o campo No.da cotação não for informado visualizar mensagem LMS-04072;
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 07/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateCotacao(Conhecimento conhecimento) {
        if (ConstantesExpedicao.CALCULO_COTACAO.equals(conhecimento.getTpCalculoPreco().getValue()) &&
                (conhecimento.getCotacoes() == null || conhecimento.getCotacoes().isEmpty())) {
				throw new BusinessException("LMS-04072");
			}
		}

	/**
	 * Regra ValidaCTRC.18
	 * - Se o cliente devedor do frete, definido no item ValidaCTRC.3, for especial (CLIENTE.TP_CLIENTE = "S"),
	 * não aceitar densidades que tenham o fator de multiplicação (DENSIDADE.VL_FATOR) menor que 1 (um),
	 * caso isto aconteça visualizar mensagem LMS-04074;
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 08/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateDensidadeClienteDevedorEspecial(Conhecimento conhecimento) {
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
		String tpCliente = clienteResponsavel.getTpCliente().getValue();
		if(ClienteUtils.isParametroClienteEspecial(tpCliente)
			&& conhecimento.getDensidade().getVlFator().doubleValue() < 1
		) {
			throw new BusinessException("LMS-04074");
		}
	}

	/**
	 * Regra ValidaCTRC.19
	 * - Se no campo CTRC da cooperada for informado um CTRC da cooperada que já esteja cadastrado na
	 * tabela CTO_CTO_COOPERADA (O conteúdo da segunda caixa de digitação = CTO_CTO_COOPERADA.ID_FILIAL e
	 * o conteúdo da segunda caixa de digitação = CTO_CTO_COOPERADA.NR_CTO_COOPERADA,
	 * visualizar mensagem LMS-04075;
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 08/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateCtrcCooperada(Conhecimento conhecimento) {
		List<CtoCtoCooperada> ctos = conhecimento.getCtoCtoCooperadas();
		if(ctos != null && !ctos.isEmpty()) {
			CtoCtoCooperada cto = (CtoCtoCooperada)ctos.get(0);
			Integer rowCount = ctoCtoCooperadaService.getRowCountByIdFilialNrCooperada(
				cto.getFilialByIdFilial().getIdFilial(),
				cto.getNrCtoCooperada()
			);
			if(rowCount.intValue() > 0) {
				throw new BusinessException("LMS-04075");
			}
		}
	}

	public void validateNotaFiscalConhecimento(Conhecimento conhecimento) {
		List<NotaFiscalConhecimento> notasFiscaisConhecimentos = conhecimento.getNotaFiscalConhecimentos();
		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

		if(notasFiscaisConhecimentos != null && !notasFiscaisConhecimentos.isEmpty()){
		for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimentos) {
			if(JTDateTimeUtils.comparaData(notaFiscalConhecimento.getDtEmissao(), dtAtual) > 0) {
				throw new BusinessException("LMS-04019", new Object[]{notaFiscalConhecimento.getNrNotaFiscal()});
			}
		}
		}
		validateLimitePesoFreteCortesia(conhecimento);
	}

	/**
	 * Regra ValidaCTRC.20
	 * Verificação do limite de peso para Frete Cortesia:
	 * - Se foi informado que o tipo de cálculo é Cortesia (G), verificar se existe
	 * um parâmetro geral indicando o limite de peso para Frete Cortesia
	 * (PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "LIMITE_PESO_CORTESIA").
	 * Se existir, verificar se a soma dos pesos informados nas Notas Fiscais (Conhecimento.psReal)
	 * não é maior que o valor cadastrado em PARAMETRO_GERAL.DS_CONTEUDO.
	 * Se for maior visualizar mensagem LMS-04076.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 08/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateLimitePesoFreteCortesia(Conhecimento conhecimento) {
		if(ConstantesExpedicao.CALCULO_CORTESIA.equals(conhecimento.getTpCalculoPreco().getValue())) {
			BigDecimal limite = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_CORTESIA);
            if (limite != null && CompareUtils.gt(conhecimento.getPsReal(), limite)) {
					throw new BusinessException("LMS-04076");
				}
			}
		}

	/**
	 * Regra ValidaCTRC.21
	 * Verificação de cotação para o Remetente não informada:
	 *	- Para definir a filial de atendimento do município da cotação coleta chamar a rotina find
	 *	Filial de Atendimento do Município passando os seguintes parâmetros:
	 *	• Município = Id do município do cliente remetente informado no campo remetente
	 *	• Serviço = conforme campo Tipo de serviço da aba Dados Gerais
	 *	• Indicador de coleta = Boolean.TRUE
	 *	• Data de consulta = data corrente
	 *	• CEP = CEP do cliente remetente informado no campo remetente
	 *	• Cliente atendido = Id do cliente Remetente
     * <p>
	 *	- Se o Tipo de Cálculo informado não for "Cotação", verificar se existe para o
	 *	cliente Remetente uma cotação (CLIENTE.ID_CLIENTE = ID_CLIENTE), onde o município de origem seja
	 *	atendido pela filial do usuário logado, esteja com COTACAO.TP_SITUACAO = "T" (Cotada),
	 *	que tenha a sua Nota Fiscal (COTACAO.NR_NOTA_FISCAL) igual a uma das Notas Fiscais informadas
	 *	no Conhecimento e ainda esteja válida (COTACAO.DT_VALIDADE >= data corrente).
	 *	Se existir, visualizar mensagem de confirmação LMS-04045.
	 *	Se for seleciona "SIM" cancelar o processo de validação do CTRC.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 09/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateCotacaoRemententeNaoInformada(Conhecimento conhecimento) {
		if(ConstantesExpedicao.CALCULO_COTACAO.equals(conhecimento.getTpCalculoPreco().getValue())
				|| Boolean.TRUE.equals(conhecimento.getDadosCliente().getBlCotacaoRemetente())
		) {
			return;
		}
		List<Integer> nrNotas = new ArrayList<Integer>();
		List<NotaFiscalConhecimento> notasFiscaisConhecimento = conhecimento.getNotaFiscalConhecimentos();
		for (NotaFiscalConhecimento notaFiscalConhecimento : notasFiscaisConhecimento) {
			nrNotas.add(notaFiscalConhecimento.getNrNotaFiscal());
		}

		Long idCliente = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
		List cotacoes = new ArrayList<>();
		while(nrNotas.size() > 999){
    		List sublist = new ArrayList(nrNotas.subList(0, 999));
    		cotacoes.addAll(cotacaoService.findCotacaoByIdClienteTpSituacaoNotas(idCliente, "A", sublist));
    		nrNotas.removeAll(sublist);
    	}
		 cotacoes.addAll(cotacaoService.findCotacaoByIdClienteTpSituacaoNotas(idCliente, "A", nrNotas));
			
		if(!cotacoes.isEmpty()) {
			Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
			Long idServico = conhecimento.getServico().getIdServico();
			String nrCepRemetente = conhecimento.getDadosCliente().getNrCepRemetente();
			for (Iterator iter = cotacoes.iterator(); iter.hasNext();) {
				Map map = (Map) iter.next();
				Long idMunicipio = (Long)map.get("idMunicipio");
				Long idFilialAtendida = ppeService.findFilialAtendimentoMunicipio(
					idMunicipio,
					idServico,
					Boolean.TRUE,
					JTDateTimeUtils.getDataAtual(),
					nrCepRemetente,
					idCliente,
					null,
					null,
					null,
					null,
					null
				);
				if(idFilial.equals(idFilialAtendida)) {
					throw new BusinessException("LMS-04045");
				}
			}
		}
	}

	/**
	 * Regra ValidaCTRC.22
	 * Verificação de integrantes
	 * Verificação de integrantes iguais (Mesmo ID):
	 *	- Se o Responsável for "Outro" verificar se o Cliente Responsável informado é igual a um dos
	 *	outros quatro integrantes ("Remetente", "Destinatário", "Redespacho" e "Consignatário"),
	 *	se for igual visualizar mensagem LMS-04077.
	 *	- Verificar se o Remetente é igual ao Consignatário ou ao Redespacho,
	 *	se for igual visualizar mensagem LMS-04077.
	 *	- Verificar se o Destinatário é igual ao Consignatário ou ao Redespacho,
	 *	se for igual visualizar mensagem LMS-04077.
	 *	- Verificar se o Consignatário e o Redespacho são iguais entre si,
	 *	se forem iguais visualizar mensagem LMS-04077.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 09/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateIntegrantesIguais(Conhecimento conhecimento) {
		Cliente remetente = conhecimento.getClienteByIdClienteRemetente();
		if( (remetente == null) || (remetente.getIdCliente() == null) ) {
			throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem(REMETENTE)});
		}

		Cliente destinatario = conhecimento.getClienteByIdClienteDestinatario();
		if( (destinatario == null) || (destinatario.getIdCliente() == null) ) {
			throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem(DESTINATARIO)});
		}

		if (conhecimento.getBlRedespachoIntermediario() == null || Boolean.FALSE.equals(conhecimento.getBlRedespachoIntermediario())){
		    
		    Cliente redespacho = conhecimento.getClienteByIdClienteRedespacho();
		    Cliente consignatario = conhecimento.getClienteByIdClienteConsignatario();
		    String tpDevedorFrete = conhecimento.getTpDevedorFrete().getValue();
		    if (ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(tpDevedorFrete)) {
		        Cliente responsavel = ((DevedorDocServ) conhecimento.getDevedorDocServs().get(0)).getCliente();
		        if (responsavel.equals(remetente) || responsavel.equals(destinatario) || responsavel.equals(redespacho)
		                || responsavel.equals(consignatario)) {
		            throw new BusinessException("LMS-04077");
		        }
		    }
		    
		    
		    if (remetente.equals(consignatario)
		            || (destinatario.equals(redespacho) || destinatario.equals(consignatario))
		            || (redespacho != null && redespacho.equals(consignatario))) {
		        throw new BusinessException("LMS-04077");
		    }
		}
		
	}

	/**
	 * Regra ValidaCTRC.23
	 * Se o tomador do serviço (será o cliente remetente para o frete do tipo CIF ou o
	 * cliente destinatário para o frete do tipo FOB) não tiver uma inscrição estadual cadastrada
	 * visualizar mensagem LMS-04107.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 12/01/2006
     *
	 * @param conhecimento
	 */
	protected void validateInscricacaoEstadualTomadorServico(Conhecimento conhecimento){
		String tpFrete = conhecimento.getTpFrete().getValue();
		if( (ConstantesExpedicao.TP_FRETE_CIF.equals(tpFrete) && conhecimento.getInscricaoEstadualRemetente() == null)
			|| (ConstantesExpedicao.TP_FRETE_FOB.equals(tpFrete) && conhecimento.getInscricaoEstadualDestinatario() == null)
		) {
			throw new BusinessException("LMS-04107");
		}

		if ((ConstantesExpedicao.TP_FRETE_CIF.equals(tpFrete) && conhecimento.getInscricaoEstadualDestinatario() == null)
				|| (ConstantesExpedicao.TP_FRETE_FOB.equals(tpFrete) && conhecimento.getInscricaoEstadualRemetente() == null)
		) {
			throw new BusinessException("LMS-04407");
	}

	}

	/**
	 * Regra ValidaCTRC.24
	 * Validação para peso real acima do parâmetro:
	 *	- Verificar se existe um parâmetro cadastrado na tabela PARAMETRO_GERAL para limite de peso
	 *	por CTRC (PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "PESO_LIMITE_CTRC", se o mesmo existir,
	 *	verificar se o somatório dos pesos informados na aba Notas Fiscais é maior que
	 *	o valor do parâmetro (PARAMETRO_ GERAL.DS_CONTEUDO).
	 *	Se for maior apresentar a mensagem LMS-04046 solicitando confirmação.
	 *	Se a resposta do usuário for "Sim" chamar a popup Liberação de Embarque,
	 *	se a resposta for "Não", cancelar validação dos dados.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 09/12/2005
     *
	 * @param conhecimento
	 */
	protected void validatePesoReal(Conhecimento conhecimento) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_PESO_LIMITE, false)) {
			return;
		}
		BigDecimal limite = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_CTRC);
		if(limite != null) {
            if (CompareUtils.gt(conhecimento.getPsReal(), limite) || CompareUtils.gt(conhecimento.getPsAforado(), limite)) {
				throw new BusinessException("LMS-04046");
			}

				BigDecimal totalNota = BigDecimal.ZERO;

				for(NotaFiscalConhecimento nota : conhecimento.getNotaFiscalConhecimentos()){
					totalNota = totalNota.add(nota.getPsCubado());
				}

				if(CompareUtils.gt(totalNota ,conhecimento.getPsAforado())){
					conhecimento.setPsAforado(totalNota);

                if (CompareUtils.gt(conhecimento.getPsReal(), limite) || CompareUtils.gt(conhecimento.getPsAforado(), limite)) {
						throw new BusinessException("LMS-04335");
					}
				}
		}
	}

	/**
	 * Regra ValidaCTRC.25
	 * Validação para valor de mercadoria acima do parâmetro:
	 *	- Verificar se existe um parâmetro cadastrado na tabela PARAMETRO_GERAL para limite de valor
	 *	da mercadoria por CTRC (PARAMETRO_GERAL.NM_PARAMETRO_ GERAL = "VLR_LIMITE_MERCADORIA",
	 *	se o mesmo existir, verificar se o somatório dos valores das mercadorias informados na
	 *	aba Notas Fiscais é maior que o valor do parâmetro (PARAMETRO_ GERAL.DS_CONTEUDO).
	 *	Se for maior apresentar a mensagem LMS-04047 solicitando confirmação.
	 *	Se a resposta do usuário for "Sim" chamar a popup Liberação de Embarque,
	 *	se a resposta for "Não", cancelar a validação do CTRC.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 09/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateValorMercadoria(Conhecimento conhecimento) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_VALOR_MERCADORIA)) {
			return;
		}
		BigDecimal limite = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_VALOR_MERCADORIA);
        if (limite != null && CompareUtils.gt(conhecimento.getVlMercadoria(), limite)) {
				throw new BusinessException("LMS-04047");
			}
		}

	/**
	 * Regra ValidaCTRC.26 - Verifica a necessidade de validar o embarque
	 * Verifica se é necessário executar a validação de embarque proibido da seguinte forma:
	 * - Se o conhecimento for do tipo "Normal" e se para o pedido de coleta
	 * informado (campo DoctoServico.pedidoColeta) não existe uma
	 * liberação de embarque (PEDIDO_COLETA.BL_CLIENTE_ LIBERADO_MANUAL = "N", é preciso
	 * validar o embarque, caso contrário, não validar.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 05/01/2006
     *
	 * @param conhecimento
	 */
	public void validateEmbarqueProibido(Conhecimento conhecimento) {
		if(ConstantesExpedicao.CONHECIMENTO_NORMAL.equals(conhecimento.getTpConhecimento().getValue())) {
			if(conhecimento.getPedidoColeta() != null) {
				Integer nrRowCountClienteLiberadoManual = pedidoColetaService.getRowCountByIdBlClienteLiberadoManual(conhecimento.getPedidoColeta().getIdPedidoColeta(), Boolean.TRUE);
				if(nrRowCountClienteLiberadoManual.intValue() == 0) {
					validateEmbarqueProibidoClienteMunicipio(conhecimento);
				}
			} else {
				validateEmbarqueProibidoClienteMunicipio(conhecimento);
			}
		}
	}

	private void validateEmbarqueProibidoClienteMunicipio(Conhecimento conhecimento) {
					Cliente clienteResponsavel = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
					validateEmbarqueProibidoMunicipio(conhecimento, clienteResponsavel);
				}

	/**
	 * Valida Frete Manual
     *
	 * @param conhecimento
	 */
	private void validateFreteManual(Conhecimento conhecimento) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_CALCULO_MANUAL)) {
			return;
		}
		//LMS-4068 / Não deve bloquear conhecimento substituto
		if( ConstantesExpedicao.CALCULO_MANUAL.equals(conhecimento.getTpCalculoPreco().getValue())
				&& (conhecimento.getTpConhecimento() == null || !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())) ) {
			throw new BusinessException("LMS-04048");
		}
	}

	/**
	 * Valida Frete Cortesia
     *
	 * @param conhecimento
	 */
	private void validateFreteCortesia(Conhecimento conhecimento) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_CALCULO_CORTESIA)) {
			return;
		}
		if(ConstantesExpedicao.CALCULO_CORTESIA.equals(conhecimento.getTpCalculoPreco().getValue())) {
			throw new BusinessException("LMS-04050");
		}
	}

	/**
	 * Regra ValidaCTRC.26 - Embarque proibido para município
	 * Rotina para verificar embarques proibidos
	 * Esta rotina verifica se há embarque proibido para o município de destino do conhecimento.
	 * Caso haja embarque proibido, deverá haver a liberação deste embarque,
	 * caso contrário o conhecimento não poderá ser feito para o cliente ou destino bloqueados.
	 *
	 * @param conhecimento
	 */
	private void validateEmbarqueProibidoMunicipio(Conhecimento conhecimento, Cliente clienteResponsavel) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_MUNICIPIO_EMBARQUE_PROIBIDO)) {
			return;
		}
		Municipio municipioEntrega = conhecimento.getMunicipioByIdMunicipioEntrega();
        if (municipioFilialService.getRowCountByIdMunicipioBlRecebeColeta(municipioEntrega.getIdMunicipio(), Boolean.FALSE) > 0) {
			Filial filialOrigem = conhecimento.getFilialByIdFilialOrigem();
			Filial filialDestino = conhecimento.getFilialByIdFilialDestino();
			
            if (filialOrigem.getIdFilial().equals(filialDestino.getIdFilial())) {
				return;
			}
			
			if(conhecimento.getTpFrete().getValue().equalsIgnoreCase(ConstantesExpedicao.TP_FRETE_FOB) && 
					liberacaoEmbarqueService.checkLiberacaoFOBDirigido(conhecimento)){
				return;
			}
			
            Integer nrRowCountMunicipioEmbarqueProibido = liberacaoEmbarqueService.getRowCountByIdMunicipioIdClienteModalAferido(
					municipioEntrega.getIdMunicipio(),
					clienteResponsavel.getIdCliente(),
					conhecimento.getServico().getTpModal().getValue()
			);
			/* Caso não exista liberação, refaz a consulta com MODAL NULO */
            if (nrRowCountMunicipioEmbarqueProibido < 1) {
                validarLiberacaoEmbarqueMunicipioAferido(conhecimento,clienteResponsavel);
            }
        }


    }

    private void validarLiberacaoEmbarqueMunicipioAferido(Conhecimento conhecimento, Cliente clienteResponsavel) {
        Municipio municipio = null;
        if (conhecimento.getEnderecoPessoa() != null) {
            municipio = conhecimento.getEnderecoPessoa().getMunicipio();
        }

        LiberacaoEmbarque liberacaoEmbarque = liberacaoEmbarqueService.findLiberacaoClienteByMunicipio(
                clienteResponsavel,
                conhecimento.getClienteByIdClienteDestinatario(),
                municipio

				);

        if (liberacaoEmbarque != null && !liberacaoEmbarque.getBlEfetivado()) {
					ConteudoParametroFilial conteudoParametroFilial = parametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "bloqCredEmbProib", false, true);

					 if (conteudoParametroFilial != null && "S".equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
						 throw new BusinessException("LMS-04508");
					 }

					throw new BusinessException("LMS-04054");
				}
			}
		
	/**
	 * Regra ValidaCTRC.26 - Embarque proibido para cliente
	 * Rotina para verificar embarques proibidos
	 * Esta rotina verifica se há embarque proibido para o Cliente.
	 * Caso haja embarque proibido, deverá haver a liberação deste embarque,
	 * caso contrário o conhecimento não poderá ser feito para o cliente ou destino bloqueados.
	 *
	 * @param conhecimento
	 */
	private void validateEmbarqueProibidoCliente(Conhecimento conhecimento, Cliente clienteResponsavel) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_CLIENTE_EMBARQUE_PROIBIDO)) {
			return;
		}
		Integer nrRowCountClienteEmbarqueProibido = proibidoEmbarqueService.getRowCountProibidoByIdCliente(clienteResponsavel.getIdCliente());
		if (nrRowCountClienteEmbarqueProibido.intValue() > 0) {
			throw new BusinessException("LMS-04511",new String[]{getClienteBloqueado(conhecimento,clienteResponsavel)});
		}
		validadeEmbarProibidoPorMotivoTributarioRemetente(conhecimento);
		validadeEmbarProibidoPorMotivoTributarioDestinatario(conhecimento);
	}

	protected void validadeEmbarProibidoPorMotivoTributarioDestinatario( Conhecimento conhecimento) {
		if ( conhecimento.getClienteByIdClienteDestinatario() == null ) {
			return;
		}
		Integer nrRowCountClienteEmbarqueProibidoTributario = proibidoEmbarqueService.getRowCountProibidoBloqueioTributarioByIdCliente(conhecimento.getClienteByIdClienteDestinatario().getIdCliente());
		if (nrRowCountClienteEmbarqueProibidoTributario.intValue() > 0) {
			throw new BusinessException("LMS-04511",new String[]{DESTINATARIO});
		}
	}

	protected void validadeEmbarProibidoPorMotivoTributarioRemetente(Conhecimento conhecimento) {
		if ( conhecimento.getClienteByIdClienteRemetente() == null ){
			return;
		}
		Integer nrRowCountClienteEmbarqueProibidoTributario = proibidoEmbarqueService.getRowCountProibidoBloqueioTributarioByIdCliente(conhecimento.getClienteByIdClienteRemetente().getIdCliente());
		if (nrRowCountClienteEmbarqueProibidoTributario.intValue() > 0) {
			throw new BusinessException("LMS-04511",new String[]{REMETENTE});
		}
	}
	
	protected String getClienteBloqueado(Conhecimento conhecimento, Cliente clienteResponsavel) {
		Long idDestinario = conhecimento.getClienteByIdClienteDestinatario().getIdCliente();
        if (clienteResponsavel.getIdCliente().equals(idDestinario)) {
            return DESTINATARIO;
        }
		
		return REMETENTE;
	}

	/*************************************************************************************************
	 * Métodos utilizados para validação dos dados para o calculo do frete.
	 * Todos os dados utilizados para a validação estão no conhecimento que será
	 * persistido.
	 * Após a validação, serão chamadas os métodos que calculam o frete do conhecimento.
	 * ***********************************************************************************************/

	/**
	 * Esta rotina tem por objetivo chamar as rotinas de cálculo do preço frete
	 * e dos serviços adicionais, efetuando o cálculo do valor do frete para CTRC e Nota Fiscal de Transporte.
	 *
	 * @param conhecimento
	 * @param calculoFrete
	 */
	public void executeCalculoFretePrimeiraFase(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		calculaPesoAforadoDimensoes(conhecimento, calculoFrete);

		//Calcula Frete
		executeCalculo(conhecimento, calculoFrete);
	}

	/**
	 * Validações pós Calculo do Frete
     *
	 * @param conhecimento
	 */
	public void executeCalculoFreteSegundaFase(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		setTipoConhecimentoCooperacao(conhecimento);
	}

	@Override
	protected void executeCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		if(!ConstantesExpedicao.CALCULO_COTACAO.equals(conhecimento.getTpCalculoPreco().getValue())) {
			configureCalculoFrete(conhecimento, calculoFrete);
			documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(calculoFrete);
		} else {
			calculaFreteCotacao(conhecimento, calculoFrete);
		}
		CalculoFreteUtils.copyResult(conhecimento, calculoFrete);
	}

	@Override
	protected void validateCotacaoByCTRC(Conhecimento conhecimento, CalculoFrete calculo) {
		if(ConstantesExpedicao.CALCULO_COTACAO.equals(conhecimento.getTpCalculoPreco().getValue())) {
			List<Cotacao> cotacoes = conhecimento.getCotacoes();
			if(cotacoes != null && !cotacoes.isEmpty()) {
				Cotacao cotacao = cotacoes.get(0);
				if(comparaCotacao(calculo, conhecimento, cotacao)) {
					if(!CompareUtils.eq(calculo.getVlTotal(), cotacao.getVlTotalCotacao())){
						ObservacaoDoctoServico ods = new ObservacaoDoctoServico();
						ods.setBlPrioridade(Boolean.FALSE);
						ods.setDsObservacaoDoctoServico((String)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_COTACAO_ERRO));
						conhecimento.addObservacaoDoctoServico(ods);
						ods.setDoctoServico(conhecimento);
					}
					cotacao.setDoctoServico(conhecimento);
					cotacao.setTpSituacao(new DomainValue("E"));
                } else {
					throw new BusinessException("LMS-04073");
			}
		}
	}
    }

	/**
	 * Regra CalculaCTRC.6
	 * Se o tipo de frete for cotação, acessar as informações da tabela Cotação pelo ID informado no campo
	 * No.da cotação, e compará-las com as informações do doctoservico.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 21/12/2005
     *
	 * @param conhecimento
     * @param calculo
	 * @return
	 */
	private CalculoFrete calculaFreteCotacao(Conhecimento conhecimento, CalculoFrete calculo) {
		List<Cotacao> cotacoes = conhecimento.getCotacoes();
		if(cotacoes != null && !cotacoes.isEmpty()) {
			Cotacao cotacao = cotacoes.get(0);
			if(comparaCotacao(calculo, conhecimento, cotacao)) {
				// Executa a Regra CalculaCTRC.6
				configureCalculoFrete(conhecimento, calculo);
				calculo.setIdCotacao(cotacao.getIdCotacao());
				documentoServicoFacade.executeCalculoConhecimentoNacionalNormal(calculo);
				if(!CompareUtils.eq(calculo.getVlTotal(), cotacao.getVlTotalCotacao())){
					ObservacaoDoctoServico ods = new ObservacaoDoctoServico();
					ods.setBlPrioridade(Boolean.FALSE);
					ods.setDsObservacaoDoctoServico((String)configuracoesFacade
							.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_COTACAO_ERRO));
					conhecimento.addObservacaoDoctoServico(ods);
				}
				cotacao.setDoctoServico(conhecimento);
				cotacao.setTpSituacao(new DomainValue("E"));
            } else {
				throw new BusinessException("LMS-04073");
		}
        }
		return calculo;
	}

	private boolean isColetaBalcao(Conhecimento conhecimento) {
		DomainValue tpModoPedidoColeta = conhecimento.getPedidoColeta() != null ? conhecimento.getPedidoColeta().getTpModoPedidoColeta() : null;
		return conhecimento.getPedidoColeta() != null && tpModoPedidoColeta != null &&	"BA".equals(tpModoPedidoColeta.getValue());
	}

	private boolean isFilialLogadaEqualsFilialOrigemCotacao(Conhecimento conhecimento, Cotacao cotacao) {
		Long idFilialLogada = SessionUtils.getFilialSessao().getIdFilial();
		Long idFilialOrigemCotacao = cotacao.getFilialByIdFilialOrigem() != null ? cotacao.getFilialByIdFilialOrigem().getIdFilial() : null;

		if (idFilialLogada.equals(idFilialOrigemCotacao)) {
			return true;
		}
		return false;
	}

	/**
	 * Compara cotação
     * <p>
	 * São usado os campos abaixo para a comparação:
	 * - COTACAO.ID_MUNICIPIO_ORIGEM x ID do município definido no item ValidaCTRC.12
	 * - COTACAO.ID_MUNICIPIO_DESTINO x ID do município definido no item ValidaCTRC.13
	 * - Servicos adicionais da cotação x serviços adicionais do docto servico.
	 *
	 * @param calculoFrete
	 * @param conhecimento
     * @param cotacao
	 * @return true se a cotação possui os valore do doctoservico, false caso contrário
	 */
	private boolean comparaCotacao(CalculoFrete calculoFrete, Conhecimento conhecimento, Cotacao cotacao) {

		if (isColetaBalcao(conhecimento)) {
			if (!isFilialLogadaEqualsFilialOrigemCotacao(conhecimento, cotacao)) {
			return false;
		}
		} else if(!isMunicipioColetaEqualsMunicipioOrigemCotacao(conhecimento, cotacao)) {
			return false;
		}

		List<ServAdicionalDocServ> servicosCotacao = servAdicionalDocServService.findByCotacao(cotacao.getIdCotacao());
		Boolean isServicoAdicional = servicosCotacao != null && conhecimento.getServAdicionalDocServs() != null
			&& BooleanUtils.isTrue(calculoFrete.getBlCalculaServicosAdicionais()) && servicosCotacao.size() != conhecimento.getServAdicionalDocServs().size();
		if( isServicoAdicional || (!BooleanUtils.isTrue(calculoFrete.getBlCalculaServicosAdicionais()) && servicosCotacao != null && !servicosCotacao.isEmpty())){
			return false;
		}
		if( servicosCotacao != null ){
			for(ServAdicionalDocServ servicoCotacao : servicosCotacao) {
				Long idServ = servicoCotacao.getServicoAdicional().getIdServicoAdicional();
				boolean achou = false;
				for(ServAdicionalDocServ servicoConhecimento : conhecimento.getServAdicionalDocServs()) {
					if(idServ.equals(servicoConhecimento.getServicoAdicional().getIdServicoAdicional())) {
                        if (!equalsServDocServico(servicoConhecimento, servicoCotacao)) {
							return false;
                        }
						achou = true;
						break;
					}
				}
                if (!achou) {
					return false;
			}
		}
        }
		return true;
	}

	private boolean isMunicipioColetaEqualsMunicipioOrigemCotacao(Conhecimento conhecimento, Cotacao cotacao) {
		return conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio().equals(cotacao.getMunicipioByIdMunicipioOrigem().getIdMunicipio())
				|| conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio().equals(cotacao.getMunicipioByIdMunicipioDestino().getIdMunicipio());
	}

	/**
	 * Compara todos os campos do ServAdicDocServ.
	 * autor Julio Cesar Fernandes Corrêa
	 * 10/01/2006
     *
	 * @param servDoc
	 * @param servCot
	 * @return
	 */
	private boolean equalsServDocServico(ServAdicionalDocServ servDoc, ServAdicionalDocServ servCot) {
        if (!EqualsHelper.equals(servDoc.getDtPrimeiroCheque(), servCot.getDtPrimeiroCheque())) {
			return false;
        }
        if (!EqualsHelper.equals(servDoc.getNrKmRodado(), servCot.getNrKmRodado())) {
			return false;
        }
        if (!EqualsHelper.equals(servDoc.getQtCheques(), servCot.getQtCheques())) {
			return false;
        }
        if (!EqualsHelper.equals(servDoc.getQtColetas(), servCot.getQtColetas())) {
			return false;
        }
        if (!EqualsHelper.equals(servDoc.getQtDias(), servCot.getQtDias())) {
			return false;
        }
        if (!EqualsHelper.equals(servDoc.getQtPaletes(), servCot.getQtPaletes())) {
			return false;
        }
        if (!EqualsHelper.equals(servDoc.getQtSegurancasAdicionais(), servCot.getQtSegurancasAdicionais())) {
			return false;
        }
        if (!EqualsHelper.equals(servDoc.getVlMercadoria(), servCot.getVlMercadoria())) {
			return false;
        }
		return true;

	}


	/**
	 * Configura os parâmetros de entrada para o calculo do frete.
	 * Os parâmetros são baseados no doctoservico e no conhecimento já validados.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 03/01/2006
     *
	 * @param conhecimento
     * @param calculo
	 */
	protected CalculoFrete configureCalculoFrete(Conhecimento conhecimento, CalculoFrete calculo) {
		String tpCalculo = conhecimento.getTpCalculoPreco().getValue();
		calculo.setDadosCliente(conhecimento.getDadosCliente());
		calculo.setTpConhecimento(conhecimento.getTpConhecimento().getValue());
		calculo.setTpCalculo(tpCalculo);
		calculo.setPsRealInformado(conhecimento.getPsReal());
		calculo.setPsCubadoInformado(conhecimento.getPsAforado());
		calculo.setQtVolumes(conhecimento.getQtVolumes());
		calculo.setIdDensidade(conhecimento.getDensidade().getIdDensidade());
		calculo.setVlMercadoria(conhecimento.getVlMercadoria());
		calculo.setIdServico(conhecimento.getServico().getIdServico());
		calculo.setServAdicionalDoctoServico(conhecimento.getServAdicionalDocServs());
		calculo.setTpAbrangencia(conhecimento.getServico().getTpAbrangencia().getValue());
		calculo.setTpModal(conhecimento.getServico().getTpModal().getValue());
		calculo.setTpFrete(conhecimento.getTpFrete().getValue());
        if (conhecimento.getProdutoEspecifico() != null) {
			calculo.setIdProdutoEspecifico(conhecimento.getProdutoEspecifico().getIdProdutoEspecifico());
        }
		calculo.setBlColetaEmergencia(conhecimento.getBlColetaEmergencia());
		calculo.setBlEntregaEmergencia(conhecimento.getBlEntregaEmergencia());
		calculo.setIdTarifa(findTarifaPreco(conhecimento));
		if(conhecimento.getCtoCtoCooperadas() == null) {
			calculo.setBlRedespacho(Boolean.FALSE);
		} else {
			calculo.setBlRedespacho(Boolean.TRUE);
		}
		calculo.setNrCepColeta(conhecimento.getNrCepColeta());
		calculo.setNrCepEntrega(conhecimento.getNrCepEntrega());

		RestricaoRota restricaoRotaOrigem = calculo.getRestricaoRotaOrigem();
		restricaoRotaOrigem.setIdFilial(obtemFilial(conhecimento.getDadosCliente()).getIdFilial());
		restricaoRotaOrigem.setIdMunicipio(conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio());
		if(conhecimento.getAeroportoByIdAeroportoOrigem() != null) {
			restricaoRotaOrigem.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}

		RestricaoRota restricaoRotaDestino = calculo.getRestricaoRotaDestino();
		restricaoRotaDestino.setIdFilial(conhecimento.getFilialByIdFilialDestino().getIdFilial());
		restricaoRotaDestino.setIdMunicipio(conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio());
		if(conhecimento.getAeroportoByIdAeroportoDestino() != null) {
			restricaoRotaDestino.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}
		validateDadosCliente(conhecimento, calculo);

		return calculo;
	}

	protected CalculoFrete configurarFreteCotacao(Conhecimento conhecimento, CalculoFrete calculo) {

		String tpCalculo = conhecimento.getTpCalculoPreco().getValue();
		calculo.setDadosCliente(conhecimento.getDadosCliente());
		calculo.setTpConhecimento(conhecimento.getTpConhecimento().getValue());
		calculo.setTpCalculo(tpCalculo);
		calculo.setPsRealInformado(conhecimento.getPsReal());
		calculo.setPsCubadoInformado(conhecimento.getPsAforado());
		calculo.setQtVolumes(conhecimento.getQtVolumes());
		calculo.setIdDensidade(conhecimento.getDensidade().getIdDensidade());
		calculo.setVlMercadoria(conhecimento.getVlMercadoria());
		calculo.setIdServico(conhecimento.getServico().getIdServico());
		calculo.setServAdicionalDoctoServico(conhecimento.getServAdicionalDocServs());
		calculo.setTpAbrangencia(conhecimento.getServico().getTpAbrangencia().getValue());
		calculo.setTpModal(conhecimento.getServico().getTpModal().getValue());
		calculo.setTpFrete(conhecimento.getTpFrete().getValue());
        if (conhecimento.getProdutoEspecifico() != null) {
			calculo.setIdProdutoEspecifico(conhecimento.getProdutoEspecifico().getIdProdutoEspecifico());
        }
		calculo.setBlColetaEmergencia(conhecimento.getBlColetaEmergencia());
		calculo.setBlEntregaEmergencia(conhecimento.getBlEntregaEmergencia());
		calculo.setIdTarifa(findTarifaPreco(conhecimento));
		if(conhecimento.getCtoCtoCooperadas() == null) {
			calculo.setBlRedespacho(Boolean.FALSE);
		} else {
			calculo.setBlRedespacho(Boolean.TRUE);
		}
		calculo.setNrCepColeta(conhecimento.getNrCepColeta());
		calculo.setNrCepEntrega(conhecimento.getNrCepEntrega());

		RestricaoRota restricaoRotaOrigem = calculo.getRestricaoRotaOrigem();
		restricaoRotaOrigem.setIdFilial(obtemFilial(conhecimento.getDadosCliente()).getIdFilial());
		restricaoRotaOrigem.setIdMunicipio(conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio());
		if(conhecimento.getAeroportoByIdAeroportoOrigem() != null) {
			restricaoRotaOrigem.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}
		calculo.setRestricaoRotaOrigem(restricaoRotaOrigem);

		RestricaoRota restricaoRotaDestino = calculo.getRestricaoRotaDestino();
		restricaoRotaDestino.setIdFilial(conhecimento.getFilialByIdFilialDestino().getIdFilial());
		restricaoRotaDestino.setIdMunicipio(conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio());
		if(conhecimento.getAeroportoByIdAeroportoDestino() != null) {
			restricaoRotaDestino.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}
		calculo.setRestricaoRotaDestino(restricaoRotaDestino);

		calculo.setDoctoServico(conhecimento);

		if( conhecimento.getCotacoes() != null && !conhecimento.getCotacoes().isEmpty()){
			Cotacao cotacao = conhecimento.getCotacoes().get(0);
			calculo.setIdCotacao(cotacao.getIdCotacao());
			validateDadosCliente(conhecimento, calculo);
		}


		return calculo;
	}

	/**
	 * Busca a tarifa preço baseada no município de coleta, município de entrega e no serviço.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 03/01/2006
     *
	 * @param conhecimento
	 * @return idTarifaPreco ID da tarifa preço
	 */
	private Long findTarifaPreco(Conhecimento conhecimento) {
		Long idTarifaPreco = null;
		Long idMunicipioDestino = conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio();
		Long idFilialDestino = conhecimento.getFilialByIdFilialDestino().getIdFilial();
		List<MunicipioFilial> municipiosFiliaisDestino = municipioFilialService.findMunicipioFilialByIdMunicipioByIdFilial(idMunicipioDestino, idFilialDestino);
		if( (municipiosFiliaisDestino != null) && (municipiosFiliaisDestino.size() == 1) ) {
			MunicipioFilial municipioFilial = municipiosFiliaisDestino.get(0);
			if(Boolean.TRUE.equals(municipioFilial.getBlPadraoMcd())) {
				idTarifaPreco = mcdService.findTarifaMunicipios(
						conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio(),
						idMunicipioDestino,
						conhecimento.getServico().getIdServico());
			}
		}
		return idTarifaPreco;
	}

	/**
	 * Regra CalculaCTRC.2
	 * Calculo do peso aforado do doctoservico, baseado nas dimensões:
	 *	- Somar as Multiplicações dos valores informados (Altura, Largura, Comprimento e Quantidade)
	 *	nas dimensões do Conhecimento;
	 *	- Se o modal do serviço que foi selecionado no campo Serviço for Rodoviário (R) dividir
	 *	o valor obtido PARAMETRO_GERAL.DS_CONTEUDO para o fator peso x metragem cúbica
	 *	(PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "PESO_METRAGEM_ CUBICA_RODOVIARIO").
	 *	- Se o modal do serviço que foi selecionado no campo Serviço for Aéreo (A) dividir
	 *	o valor obtido PARAMETRO_GERAL.DS_CONTEUDO para o fator peso x metragem cúbica
	 *	(PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "PESO_METRAGEM_ CUBICA_AEREO").
	 *	- O resultado das operações acima será o peso aforado.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 12/12/2005
     *
	 * @param conhecimento
	 */
	protected void calculaPesoAforadoDimensoes(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		BigDecimal psAforado = conhecimentoService.calculaPsAforado(conhecimento.getServico().getTpModal().getValue(), conhecimento.getDimensoes(), calculoFrete);
        if (psAforado != null) {
			conhecimento.setPsAforado(psAforado);
	}
    }

	/**
	 * Regra CalculaCTRC.7
	 * Validação para valor de frete acima do parâmetro:
	 *	- Verificar se existe um parâmetro cadastrado na tabela PARAMETRO_GERAL para limite de valor
	 *	de frete por CTRC (PARAMETRO_GERAL.NM_PARAMETRO_ GERAL = "VLR_LIMITE_FRETE", se o mesmo existir,
	 *	verificar se o frete calculado acima é maior que o valor do parâmetro (PARAMETRO_ GERAL.DS_CONTEUDO).
	 *	Se for maior apresentar a mensagem LMS-04051 solicitando confirmação.
	 *	Se a resposta do usuário for "Sim" chamar a popup Liberação de Embarque, se a resposta for "Não",
	 *	interromper o calculo do ctrc.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 12/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateLimiteValorFrete(Conhecimento conhecimento) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_VALOR_FRETE_LIMITE)) {
			return;
		}
		BigDecimal vlrLimite = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VLR_LIMITE_FRETE);
        if (vlrLimite != null && !CompareUtils.lt(conhecimento.getVlTotalDocServico(), vlrLimite)) {
				throw new BusinessException("LMS-04051");
			}
		}

	/**
	 * Regra CalculaCTRC.8
	 * Validação para valor de mercadoria acima do parâmetro:
	 *	- Verificar se existe um parâmetro cadastrado na tabela PARAMETRO_GERAL para limite
	 *	de percentual do valor de frete em relação ao valor da
	 *	mercadoria (PARAMETRO_GERAL.NM_PARAMETRO_ GERAL = "RELACAO_FRETE_MERCADORIA",
	 *	se o mesmo existir, verificar se o valor da fórmula abaixo é maior que o valor
	 *	do parâmetro (PARAMETRO_GERAL.DS_CONTEUDO). Se for maior apresentar a mensagem LMS-04052
	 *	solicitando confirmação. Se a resposta do usuário for "Sim" chamar a
	 *	popup Liberação de Embarque, se a resposta for "Não", interromper o cálculo do frete.
     * <p>
	 *	Valor do frete calculado * 100 / Conhecimento.vlMercadoria
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 12/12/2005
     *
	 * @param conhecimento
	 */
	protected void validateValorMercadoriaRelacaoFrete(Conhecimento conhecimento) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(conhecimento.getLiberacaoDocServs(), ConstantesExpedicao.LIBERACAO_VALOR_FRETE_MERCADORIA)) {
			return;
		}
		BigDecimal vlMaxRelacaoFreteMercadoria = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_RELACAO_FRETE_MERCADORIA);
		if(vlMaxRelacaoFreteMercadoria != null) {
			BigDecimal vlTotalDocServico = conhecimento.getVlTotalDocServico();
			BigDecimal vlMercadoria = conhecimento.getVlMercadoria();
			BigDecimal vlRelacaoFreteMercadoria = BigDecimalUtils.divide(vlTotalDocServico.multiply(BigDecimal.valueOf(100)), vlMercadoria);
			if (CompareUtils.gt(vlRelacaoFreteMercadoria, vlMaxRelacaoFreteMercadoria)) {
				String dsMoeda = conhecimento.getMoeda().getDsSimbolo();
				String dsVlTotalDocServico = dsMoeda + " " + FormatUtils.formatDecimal("###,###,###,##0.00", vlTotalDocServico);
				String dsVlMercadoria = dsMoeda + " " + FormatUtils.formatDecimal("###,###,###,##0.00", vlMercadoria);
				throw new BusinessException("LMS-04052", new Object[]{dsVlTotalDocServico, dsVlMercadoria});
			}
		}
	}

	/**
	 * Regra CalculaCTRC.9
	 * Geração do tipo do conhecimento da cooperação:
	 *	- Se o campo CTRC da cooperada estiver preenchido o tipo de Cto Parceria será "Segundo Percurso" (S);
	 *	- Se o município de destino não for atendido pela Mercúrio e a filial de destino pertencer a uma
	 *	empresa do tipo parceira (EMPRESA.TIPO_EMPRESA = "P" e EMPRESA.ID_EMPRESA = FILIAL.ID_EMPRESA)
	 *	o tipo de Cto Parceria será "Master" (M);
	 *	- Caso contrário o tipo de Cto Parceria será nulo;
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 12/12/2005
     *
	 * @param conhecimento
	 */
	protected void setTipoConhecimentoCooperacao(Conhecimento conhecimento) {
		if(conhecimento.getCtoCtoCooperadas() != null && !conhecimento.getCtoCtoCooperadas().isEmpty()) {
			conhecimento.setTpCtrcParceria(new DomainValue("S"));
		} else if("P".equals(conhecimento.getFilialByIdFilialDestino().getEmpresa().getTpEmpresa().getValue())) {
			conhecimento.setTpCtrcParceria(new DomainValue("M"));
		} else {
			conhecimento.setTpCtrcParceria(null);
		}
	}

	/**
	 * Gravar Conhecimento
	 * Verificação de existência de PCE .
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 13/12/2005
     *
	 * @param conhecimento
	 * @return
	 */
	public HashMap<String, Object> validateExistenciaPCE(Conhecimento conhecimento) {
		HashMap<String, Object> retorno = null;
			Cliente cliente = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
			versaoDescritivoPceService.validateifExistPceByCriteria(cliente.getIdCliente(),
				Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_EXPEDICAO), Long.valueOf(EventoPce.ID_EVENTO_PCE_EMISS_DOC_SERVICO),
				Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_NO_TERMINAL_EMISS_DOC_SERV));
		return retorno;
	}

	/**
	 * Valida a obrigatoriedade dos aeroportos para o modal aéreo.
	 *
	 * @param conhecimento
	 */
	public void validateAeroporto(Conhecimento conhecimento) {
		if(ConstantesExpedicao.MODAL_AEREO.equals(conhecimento.getServico().getTpModal().getValue())) {
			if(conhecimento.getAeroportoByIdAeroportoOrigem() == null) {
				throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("aeroportoDeOrigem")});
			}
			if(conhecimento.getAeroportoByIdAeroportoDestino() == null) {
				throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("aeroportoDeDestino")});
			}
		}
	}

	/**
	 * GravarCTRC
	 * Ajustes finais para a gravação (persistência) do CTRC.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 13/12/2005
     *
	 * @param conhecimento
	 * @param cdTipoCusto
	 * @return
	 */
	public Serializable storeConhecimento(Conhecimento conhecimento, String cdTipoCusto) {
		return this.storeConhecimento(conhecimento, null, cdTipoCusto, null);
	}

	/**
	 * GravarCTRC
	 * Ajustes finais para a gravação (persistência) do CTRC.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 13/12/2005
     *
	 * @param conhecimento
     * @param meioTransporte
	 * @param cdTipoCusto
     * @param tpSituacaoDescarga
	 * @return
	 */
	public Serializable storeConhecimento(Conhecimento conhecimento, MeioTransporte meioTransporte, String cdTipoCusto, String tpSituacaoDescarga) {
		CalculoFrete calculoFrete = ExpedicaoUtils.getCalculoFreteInSession(false);
		if( calculoFrete.isCalculoNotaTransporte() ){
			CalculoNFT calculoNFT = (CalculoNFT)calculoFrete;
			if( calculoNFT.getImpostosCalculados() != null){
				impostoCalculadoService.storeAll(calculoNFT.getImpostosCalculados());
			}
		}
		defineValorCusto(conhecimento, cdTipoCusto);
		setNrCfop(conhecimento);
		gerarSeguroCliente(conhecimento);
		//LMS-4068 / Observações já geradas para conhecimento substituto
		if(conhecimento.getTpConhecimento() == null || !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())){
		gerarObsDoctoClienteDevedor(conhecimento);
		//Verifica se o cliente responsável possui observação na tabela OBSERVACAO_CONHECIMENTO.
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
		generateObsDoctoClienteByObsConhecimento(clienteResponsavel, conhecimento);
		}

		Conhecimento ctoBean = beforeInsert(conhecimento);

		//LMS-4068 / Conhecimento substituto não deve gerar monitoramentoDescarga e volumeNotaFiscal neste momento.
		if (meioTransporte != null && ( conhecimento.getTpConhecimento() == null
				|| !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())) ) {

			String dsInfColeta = null;
			if (conhecimento.getPedidoColeta() != null){
				dsInfColeta = conhecimento.getPedidoColeta().getDsInfColeta();
			}

			MonitoramentoDescarga monitoramentoDescarga = (MonitoramentoDescarga)storeMonitoramentoDescarga(meioTransporte, conhecimento.getDtColeta(), tpSituacaoDescarga, conhecimento.calculaQtVolumesTotalFrota(), ctoBean.getBlIndicadorEdi(), dsInfColeta);

			for (NotaFiscalConhecimento notafiscalConhecimento : ctoBean.getNotaFiscalConhecimentos()) {
				List volumeNotaFiscais = notafiscalConhecimento.getVolumeNotaFiscais();
				for (int i = 0; i < volumeNotaFiscais.size(); i++) {
					VolumeNotaFiscal volumeNotaFiscal = (VolumeNotaFiscal)volumeNotaFiscais.get(i);
					volumeNotaFiscal.setMonitoramentoDescarga(monitoramentoDescarga);
	}
			}

			/**
			 * LMS-2498 (09/07/12)
			 * salvar o id do monitoramento descarga para fazer a validação de contingência posteriormente
			 */
			setIdMonitoramentoDescarga(monitoramentoDescarga.getIdMonitoramentoDescarga());
		}

		return conhecimentoService.store(ctoBean);
	}

	/**
	 * GravarCTRC
	 * Ajustes finais para a gravação (persistência) do CTRC.
	 * Sem realizar o cálculo de frete.
     * <p>
	 * autor Cleveland Junior Soares
	 * 07/05/2009
     *
	 * @param conhecimento
	 * @param cdTipoCusto
	 * @return
	 */
	public Serializable storeConhecimentoSemCalculoFrete(Conhecimento conhecimento, CalculoFrete calculoFrete, MeioTransporte meioTransporte, String cdTipoCusto, String tpSituacaoDescarga, Long qtVolumesTotalFrota, Boolean blBloqueiaSubcontratada) {
 		validateDadosCliente(conhecimento);
		setNrCfop(conhecimento);
		Conhecimento ctoBean = beforeInsertSemCalculoFrete(conhecimento, blBloqueiaSubcontratada);

		Long idClienteAtendido = ctoBean.getClienteByIdClienteDestinatario().getIdCliente();
		SegmentoMercado segmentoMercado = clienteService.findSegmentoMercadoByIdCliente(idClienteAtendido);
		Filial filial = obtemFilial(ctoBean.getDadosCliente());
		Municipio municipioFilialOrigem = filial.getPessoa().getEnderecoPessoa().getMunicipio();
		Map atendimento = ppeService.findAtendimentoMunicipio(
			ctoBean.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
			ctoBean.getServico().getIdServico(),
			Boolean.FALSE,
			JTDateTimeUtils.getDataAtual(),
			ctoBean.getNrCepEntrega(),
			idClienteAtendido,
			ctoBean.getClienteByIdClienteRemetente().getIdCliente(),
			segmentoMercado.getIdSegmentoMercado(),
			municipioFilialOrigem.getUnidadeFederativa().getIdUnidadeFederativa(),
			filial.getIdFilial(),
			"N",
			ctoBean.getNaturezaProduto().getIdNaturezaProduto(),
			true
		);

		Filial filialDestino = filialService.findById(MapUtils.getLong(atendimento, "idFilial"));
		ctoBean.setFilialByIdFilialDestino(filialDestino);
		ctoBean.setFilialDestinoOperacional(filialDestino);
		String dsInfColeta = null;
		if (ctoBean.getPedidoColeta() != null ){
			dsInfColeta = ctoBean.getPedidoColeta().getDsInfColeta();
		}

		//LMS-4068 / Conhecimento substituto não deve gerar monitoramentoDescarga e volumeNotaFiscal neste momento.
		if(conhecimento.getTpConhecimento() == null || !ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())){
			//LMSA-3331
			String nrProcessamento = conhecimento.getNrProcessamento(); 
			MonitoramentoDescarga monitoramentoDescarga = null;
			if (null != nrProcessamento && conhecimento.getBlIndicadorEdi()) {
				monitoramentoDescarga = (MonitoramentoDescarga) storeMonitoramentoDescarga(meioTransporte, conhecimento.getDtColeta(), tpSituacaoDescarga, qtVolumesTotalFrota, conhecimento.getBlIndicadorEdi(), dsInfColeta, nrProcessamento);
				conhecimento.setDsSerie(null);

			} else {
				monitoramentoDescarga = (MonitoramentoDescarga) storeMonitoramentoDescarga(meioTransporte, conhecimento.getDtColeta(), tpSituacaoDescarga, qtVolumesTotalFrota, conhecimento.getBlIndicadorEdi(), dsInfColeta);
				
			}
			
		for (NotaFiscalConhecimento notafiscalConhecimento : ctoBean.getNotaFiscalConhecimentos()) {
			List volumeNotaFiscais = notafiscalConhecimento.getVolumeNotaFiscais();
			for (int i = 0; i < volumeNotaFiscais.size(); i++) {
				VolumeNotaFiscal volumeNotaFiscal = (VolumeNotaFiscal)volumeNotaFiscais.get(i);
				volumeNotaFiscal.setMonitoramentoDescarga(monitoramentoDescarga);
			}
		}
		/**
		 * LMS-2498 (06/07/12)
		 * salvar o id do monitoramento descarga para fazer a validação de contingência posteriormente
		 */
		setIdMonitoramentoDescarga(monitoramentoDescarga.getIdMonitoramentoDescarga());
		}


		return storeConhecimentoObservacao(ctoBean);
	}


	/**
	 * Busca parametrização de municipio destino e insere a observação do conhecimento
     *
     * @param ctoBean Conhecimento
	 */
	private Serializable storeConhecimentoObservacao(Conhecimento ctoBean) {
		ObservacaoDoctoServico observacao = null;
		MunicipioDestinoCalculo municipio = municipioDestinoCalculoService.findDestinoVigenteByOrigem(ctoBean.getMunicipioByIdMunicipioEntrega().getIdMunicipio());
		if (municipio != null) {
			int truncStr = 30;
			String municipioObs = municipio.getMunicipioDestino().getUnidadeFederativa().getSgUnidadeFederativa() + "-" + municipio.getMunicipioDestino().getNmMunicipio();
			String strObs = recursoMensagemService.findByChave("calculadoAte", new Object[]{municipioObs});
			observacao = new ObservacaoDoctoServico();
			observacao.setDoctoServico(ctoBean);
			observacao.setDsObservacaoDoctoServico(strObs);
			observacao.setBlPrioridade(Boolean.FALSE);
			ctoBean.setDsCalculadoAte(municipioObs.length() > truncStr ? municipioObs.substring(0, truncStr) : municipioObs);
		}

		Serializable result = conhecimentoService.store(ctoBean);

        if (observacao != null) {
			observacaoDoctoServicoService.store(observacao);
        }

		return result;
	}


	/**
	 * Cria o Meio de Transporte de acordo com o Tipo de Conhecimento
	 *
	 * @param conhecimento
	 * @param mapMeioTransporte
	 * @param mapSituacaoDescarga
	 * @return
     * @author André Valadas
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MeioTransporte createMeioTransporte(final Conhecimento conhecimento, Map mapMeioTransporte, final Map<String, String> mapSituacaoDescarga) {
		/** Verifica se tem Pedido de Coleta */
		if (mapMeioTransporte != null && mapMeioTransporte.containsKey("idPedidoColeta")){
			final Long idPedidoColeta = MapUtilsPlus.getLong(mapMeioTransporte, "idPedidoColeta");
			if(LongUtils.hasValue(idPedidoColeta)) {
				mapMeioTransporte = pedidoColetaService.findMeioTransporteByIdPedidoColeta(idPedidoColeta);
				if(conhecimento.getPedidoColeta() == null) {
					conhecimento.setPedidoColeta(pedidoColetaService.findByIdInitLazyProperties(idPedidoColeta, false));
				}
			}
		}

		/** Cria Meio de Transporte */
		final MeioTransporte meioTransporte = new MeioTransporte();
		meioTransporte.setIdMeioTransporte(MapUtilsPlus.getLong(mapMeioTransporte, "idMeioTransporte"));
		meioTransporte.setNrFrota(MapUtilsPlus.getString(mapMeioTransporte, "nrFrota"));
		meioTransporte.setNrIdentificador(MapUtilsPlus.getString(mapMeioTransporte, "nrIdentificador"));

		/** Classifica o Meio de Transporte de acordo com o tipo de Conhecimento */
		final DomainValue tpModoPedidoColeta = conhecimento.getPedidoColeta() != null ? conhecimento.getPedidoColeta().getTpModoPedidoColeta() : null;
		boolean coletaBalcao = conhecimento.getPedidoColeta() != null && tpModoPedidoColeta != null &&	"BA".equals(tpModoPedidoColeta.getValue());
		if(conhecimento.getTpConhecimento() == null) {
			conhecimento.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NORMAL));
		}
		boolean ctrcDevolucao = CONHECIMENTO_DEVOLUCAO_PARCIAL.equals(conhecimento.getTpConhecimento().getValue());
		boolean ctrcRefaturamento = CONHECIMENTO_REFATURAMENTO.equals(conhecimento.getTpConhecimento().getValue());
		mapSituacaoDescarga.put("tpSituacaoDescarga", ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_INICIADA);
		if(coletaBalcao || ctrcDevolucao || ctrcRefaturamento){
			mapSituacaoDescarga.put("tpSituacaoDescarga", ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA);
			if(ctrcDevolucao) {
				meioTransporte.setNrFrota("DEVP");
				meioTransporte.setNrIdentificador("DEVP");
			}
			if(ctrcRefaturamento){
				meioTransporte.setNrFrota("REF");
				meioTransporte.setNrIdentificador("REF");
			}
			if(coletaBalcao) {
				meioTransporte.setNrFrota(NR_FROTA_BALCAO);
				meioTransporte.setNrIdentificador(NR_FROTA_BALCAO);
			}
		}
		return meioTransporte;
	}


	public MonitoramentoDescarga storeMonitoramentoDescarga(final MeioTransporte meioTransporte, final DateTime dtColeta, final String tpSituacaoDescarga, final Long qtVolumesTotalFrota, final Boolean blIndicadorEdi) {
		return storeMonitoramentoDescarga(meioTransporte, dtColeta, tpSituacaoDescarga, qtVolumesTotalFrota, blIndicadorEdi, null, null);
	}

	public MonitoramentoDescarga storeMonitoramentoDescarga(final MeioTransporte meioTransporte, final DateTime dtColeta, final String tpSituacaoDescarga, final Long qtVolumesTotalFrota, final Boolean blIndicadorEdi, String dsInfColeta) {
		return storeMonitoramentoDescarga(meioTransporte, dtColeta, tpSituacaoDescarga, qtVolumesTotalFrota, blIndicadorEdi, dsInfColeta, null);
	}
	
	public MonitoramentoDescarga storeMonitoramentoDescarga(final MeioTransporte meioTransporte, final DateTime dtColeta, final String tpSituacaoDescarga, final Long qtVolumesTotalFrota, final Boolean blIndicadorEdi, String dsInfColeta, String nrProcessamento) {
		final String tpSituacaoDescargaFilter = Boolean.TRUE.equals(blIndicadorEdi) ? ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO : ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_INICIADA;

		MonitoramentoDescarga monitoramentoDescarga = monitoramentoDescargaService.find(
				meioTransporte.getIdMeioTransporte(),
				meioTransporte.getNrFrota(),
				tpSituacaoDescargaFilter,
				SessionUtils.getFilialSessao().getIdFilial(),
				nrProcessamento);

		/* Cria monitoramento caso nao exista */
		if (monitoramentoDescarga == null) {
			monitoramentoDescarga = new MonitoramentoDescarga();
			monitoramentoDescarga.setDhChegadaVeiculo(dtColeta);
			monitoramentoDescarga.setFilial(SessionUtils.getFilialSessao());
			monitoramentoDescarga.setMeioTransporte(meioTransporte.getIdMeioTransporte() != null ? meioTransporte : null);
			monitoramentoDescarga.setNrFrota(meioTransporte.getNrFrota());
			monitoramentoDescarga.setNrPlaca(meioTransporte.getNrIdentificador());
			monitoramentoDescarga.setDsInfColeta(dsInfColeta);
			monitoramentoDescarga.setNrProcessamento(nrProcessamento);

			if(Boolean.TRUE.equals(blIndicadorEdi)) {
				monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO));
            } else {
				monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(tpSituacaoDescarga));
            }
			monitoramentoDescarga.setQtVolumesTotal(qtVolumesTotalFrota);
			monitoramentoDescarga.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());

			monitoramentoDescarga = (MonitoramentoDescarga) monitoramentoDescargaService.store(monitoramentoDescarga);
		}


		return monitoramentoDescarga;
	}

	private void gerarObsDoctoClienteDevedor(Conhecimento conhecimento) {
		Long idCliente = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente().getIdCliente();
		Servico servico = conhecimento.getServico();

		List<DocumentoCliente> documentosCliente = documentoClienteService.findByCliente(idCliente, servico.getTpModal().getValue(), servico.getTpAbrangencia().getValue());
		String dsObservacao;
		ObservacaoDoctoServico observacaoDoctoServico;
		for(DocumentoCliente documentoCliente : documentosCliente) {
			dsObservacao = configuracoesFacade.getMensagem("LMS-04154", new Object[]{documentoCliente.getTipoDocumentoEntrega().getDsTipoDocumentoEntrega().getValue()});
			observacaoDoctoServico = new ObservacaoDoctoServico(Boolean.FALSE, dsObservacao);
			observacaoDoctoServico.setDoctoServico(conhecimento);
			conhecimento.addObservacaoDoctoServico(observacaoDoctoServico);
		}
	}

	/**
	 * GravarCTRC
	 * Gerar um registro para cada registro de SEGURO_CLIENTE pertencente ao
	 * Cliente Responsável pelo Frete que tenham o mesmo modal e abrangência do serviço que
	 * está sendo executado. Além disso atualiza o valor total do docto servico no devedor do frete.
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 13/12/2005
     *
	 * @param conhecimento
	 */
	private void gerarSeguroCliente(Conhecimento conhecimento) {
		DevedorDocServ devedorDocServ = (DevedorDocServ)conhecimento.getDevedorDocServs().get(0);
		Servico servico = conhecimento.getServico();
		List seguros = seguroClienteService.findByClienteTpModalTpAbrangencia(
				devedorDocServ.getCliente().getIdCliente(),
				servico.getTpModal().getValue(),
				servico.getTpAbrangencia().getValue()
		);
		List<DoctoServicoSeguros> doctoSeguros = new ArrayList<DoctoServicoSeguros>(seguros.size());
		for (Iterator iter = seguros.iterator(); iter.hasNext();) {
			Map map = (Map) iter.next();
			TipoSeguro tipoSeguro = new TipoSeguro();
			tipoSeguro.setIdTipoSeguro((Long)map.get("idTipoSeguro"));
			DoctoServicoSeguros doctoServicoSeguros = new DoctoServicoSeguros();
			doctoServicoSeguros.setConhecimento(conhecimento);
			doctoServicoSeguros.setTipoSeguro(tipoSeguro);
			doctoSeguros.add(doctoServicoSeguros);
		}
		conhecimento.setDoctoServicoSeguros(doctoSeguros);
	}

	/**
	 * GravarCTRC
	 * Definição de ValorCusto para o valor do imposto do CTRC após o cálculo.
	 * O tipo do custo é definido como "ICMS".
     * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 * 13/12/2005
     *
	 * @param conhecimento
	 * @param cdTipoCusto
	 */
	private void defineValorCusto(Conhecimento conhecimento, String cdTipoCusto) {
		TipoCusto tipoCusto = tipoCustoService.findByDsTipoCusto(cdTipoCusto);
		if (tipoCusto == null) {
			return;
		}

		if (conhecimento.getValorCustos() == null){
			conhecimento.setValorCustos(new ArrayList<ValorCusto>(1));
		}

		if(conhecimento.getValorCustos().isEmpty()){
			ValorCusto valorCusto = new ValorCusto();
			valorCusto.setDoctoServico(conhecimento);
			valorCusto.setVlCusto(conhecimento.getVlImposto());
			valorCusto.setTipoCusto(tipoCusto);
			conhecimento.getValorCustos().add(valorCusto);
		}
	}

	public void validateUsuarioLiberacaoEmbarqueClienteMunicipio(final Usuario usuario) {
		if(!conhecimentoService.validateLiberacaoEmbarque(usuario)){
			throw new BusinessException("LMS-40005");
		}
	}

	/**
	 * Processa o conhecimento sem etiqueta
	 */
	public void executeAtualizaCTRCSemEtiqueta(Conhecimento conhecimento, Long idMonitoramentoDescarga) {
		if (idMonitoramentoDescarga != null) {
			monitoramentoDescargaService.executeFechaConhecimentoEMonitoramentoDescarga(conhecimento, Boolean.TRUE, idMonitoramentoDescarga);
			doctoServicoService.executeValidacoesParaBloqueioValores(conhecimento.getIdDoctoServico());
		}
	}

	/**
	 * Validação de contingência
	 */
	public void validateContingencia(Conhecimento conhecimento) {
		Filial filial = SessionUtils.getFilialSessao();

		if (filial.getDtImplantacaoLMS() != null && filial.getDtImplantacaoLMS().isBefore(JTDateTimeUtils.getDataAtual())) {
			PedidoColeta pedidoColeta = conhecimento.getPedidoColeta();

			if ((pedidoColeta != null && "BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())) ||
				"DP".equals(conhecimento.getTpConhecimento().getValue()) ||
				"RF".equals(conhecimento.getTpConhecimento().getValue())) {

				Contingencia contingencia = contingenciaService.findByFilial(filial.getIdFilial(), "A", "E");
				if (contingencia != null) {
					executeAtualizaCTRCSemEtiqueta(conhecimento, getIdMonitoramentoDescarga());
				}
			}
		}
	}

	//LMS-2672
	public void validateContingenciaSemLabelingSemEDI(Conhecimento conhecimento) {
		Filial filial = SessionUtils.getFilialSessao();

		if (filial.getDtImplantacaoLMS() != null && filial.getDtImplantacaoLMS().isBefore(JTDateTimeUtils.getDataAtual())) {
			PedidoColeta pedidoColeta = conhecimento.getPedidoColeta();

			if ((pedidoColeta != null && "BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())) ||
					"DP".equals(conhecimento.getTpConhecimento().getValue()) ||
					"RF".equals(conhecimento.getTpConhecimento().getValue())) {

				if(ConhecimentoUtils.isLiberaEtiquetaEdi(conhecimento)){
					executeAtualizaCTRCSemEtiqueta(conhecimento, getIdMonitoramentoDescarga());
				}else{
					Contingencia contingencia = contingenciaService.findByFilial(filial.getIdFilial(), "A", "E");
					if (contingencia != null) {
						executeAtualizaCTRCSemEtiqueta(conhecimento, getIdMonitoramentoDescarga());
					}
				}
			}
		}
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public void setExcecaoICMSClienteService(ExcecaoICMSClienteService excecaoICMSClienteService) {
		this.excecaoICMSClienteService = excecaoICMSClienteService;
	}

	public void setServAdicionalDocServService(ServAdicionalDocServService servAdicionalDocServService) {
		this.servAdicionalDocServService = servAdicionalDocServService;
	}

	public void setSeguroClienteService(SeguroClienteService seguroClienteService) {
		this.seguroClienteService = seguroClienteService;
	}

	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}

	public void setProibidoEmbarqueService(ProibidoEmbarqueService proibidoEmbarqueService) {
		this.proibidoEmbarqueService = proibidoEmbarqueService;
	}

	public void setLiberacaoEmbarqueService(LiberacaoEmbarqueService liberacaoEmbarqueService) {
		this.liberacaoEmbarqueService = liberacaoEmbarqueService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setLiberacaoDocServService(LiberacaoDocServService liberacaoDocServService) {
		this.liberacaoDocServService = liberacaoDocServService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}

	public void setCtoCtoCooperadaService(CtoCtoCooperadaService ctoCtoCooperadaService) {
		this.ctoCtoCooperadaService = ctoCtoCooperadaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}

	public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public void setDocumentoClienteService(DocumentoClienteService documentoClienteService) {
		this.documentoClienteService = documentoClienteService;
	}

	public Long getIdMonitoramentoDescarga() {
		return idMonitoramentoDescarga;
	}

	public void setIdMonitoramentoDescarga(Long idMonitoramentoDescarga) {
		this.idMonitoramentoDescarga = idMonitoramentoDescarga;
	}

	public FluxoFilialService getFluxoFilialService() {
		return fluxoFilialService;
	}

	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}

	public void setInformacaoDocServicoService(InformacaoDocServicoService informacaoDocServicoService) {
		this.informacaoDocServicoService = informacaoDocServicoService;
	}

	public void setMunicipioDestinoCalculoService(MunicipioDestinoCalculoService municipioDestinoCalculoService) {
		this.municipioDestinoCalculoService = municipioDestinoCalculoService;
	}

	public void setObservacaoDoctoServicoService(ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}

	public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	public void setImpostoCalculadoService(
			ImpostoCalculadoService impostoCalculadoService) {
		this.impostoCalculadoService = impostoCalculadoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ContingenciaService getContingenciaService() {
		return contingenciaService;
	}

	public void setContingenciaService(ContingenciaService contingenciaService) {
		this.contingenciaService = contingenciaService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setParametroFilialService(
			ConteudoParametroFilialService parametroFilialService) {
		this.parametroFilialService = parametroFilialService;
	}
	
	public void setEmbarqueValidationService(EmbarqueValidationService embarqueValidationService) {
		this.embarqueValidationService = embarqueValidationService;
	}

    public void setTipoTributacaoIEService(
            TipoTributacaoIEService tipoTributacaoIEService) {
        this.tipoTributacaoIEService = tipoTributacaoIEService;
    }

    public void setCalcularIcmsService(CalcularIcmsService calcularIcmsService) {
        this.calcularIcmsService = calcularIcmsService;
    }


}
