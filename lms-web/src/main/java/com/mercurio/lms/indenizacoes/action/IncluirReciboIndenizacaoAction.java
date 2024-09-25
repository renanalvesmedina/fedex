package com.mercurio.lms.indenizacoes.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.AgenciaBancariaService;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.CotacaoIndicadorFinanceiroService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.ProdutoService;
import com.mercurio.lms.indenizacoes.model.AnexoRim;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.MdaSalvadoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ParcelaReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoNf;
import com.mercurio.lms.indenizacoes.model.service.AnexoRimService;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.FilialDebitadaService;
import com.mercurio.lms.indenizacoes.model.service.ParcelaReciboIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.ReciboIndenizacaoService;
import com.mercurio.lms.indenizacoes.report.EmitirReciboRIMService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.OrdemFilialFluxoService;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.MotivoAberturaNcService;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.NegociacaoService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.seguros.model.service.ProcessoSinistroService;
import com.mercurio.lms.seguros.model.service.SinistroDoctoServicoService;
import com.mercurio.lms.util.ArquivoUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.WarningCollector;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.incluirReciboIndenizacaoAction"
 */

public class IncluirReciboIndenizacaoAction extends MasterDetailAction {

	private Logger log = LogManager.getLogger(this.getClass());
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	private SinistroDoctoServicoService sinistroDoctoServicoService;
	private ParcelaReciboIndenizacaoService parcelaReciboIndenizacaoService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private DomainValueService domainValueService;
	private ConhecimentoService conhecimentoService;
	private DoctoServicoService doctoServicoService;
	private ContaBancariaService contaBancariaService;
	private ConversaoMoedaService conversaoMoedaService;
	private FilialDebitadaService filialDebitadaService;
	private AgenciaBancariaService agenciaBancariaService;
	private NaoConformidadeService naoConformidadeService;
	private RecursoMensagemService recursoMensagemService;	
	private ProcessoSinistroService processoSinistroService;
	private CtoInternacionalService ctoInternacionalService;
	private MotivoAberturaNcService motivoAberturaNcService;
	private EmitirReciboRIMService emitirReciboRIMService;
	private MdaService mdaService;
	private ReciboReembolsoService reciboReembolsoService;
	private BancoService bancoService;	
	private MoedaService moedaService;
	private PessoaService pessoaService;
	private FilialService filialService;
	private ProdutoService produtoService;
	private ClienteService clienteService;
	private NegociacaoService negociacaoService;
	private DevedorDocServService devedorDocServService;
	private VersaoDescritivoPceService versaoDescritivoPceService; 
	private HistoricoFilialService historicoFilialService; 
	private CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService;
	private OrdemFilialFluxoService ordemFilialFluxoService;
	private ConfiguracoesFacade configuracoesFacade;
	private AnexoRimService anexoRimService;
	private UsuarioLMSService usuarioLMSService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	
	static final String CONSIGNATARIO = "C";
	
	static final String DESTINATARIO = "D";
	static final String REMETENTE = "R";
	static final String TERCEIRO = "T";
	static final String DEVEDORDOCUMENTO = "V";
	
	
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
    	/********************************************************************
    	 * MasterEntryConfig
    	 ********************************************************************/
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(ReciboIndenizacao.class, true);
    	Comparator comparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				return 0;
			}    		
    	};
    	
    	/********************************************************************
    	 * Documentos do Processo ItemListConfig
    	 ********************************************************************/
    	ItemListConfig doctoServicoIndenizacaoProcessoConfig = new ItemListConfig() {
			public List initialize(Long masterId, Map parameters) {
				return null;
			}
			public Integer getRowCount(Long masterId, Map parameters) {
				return null;
			}
			public Map configItemDomainProperties() {
				return null;
			}
			
			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) bean;

				// seta o pai
				if(getMasterId(tfm) != null) {
					doctoServicoIndenizacao.setReciboIndenizacao(getReciboIndenizacaoFromSession(getMasterId(tfm)));										
				}

				if ("selecao".equals(tfm.getString("myScreen"))) {
					DoctoServico doctoServico = doctoServicoService.findByIdJoinProcessoSinistro(tfm.getLong("idDoctoServico"));
					SinistroDoctoServico sinistroDoctoServico = (SinistroDoctoServico)doctoServico.getSinistroDoctoServicos().get(0);
					doctoServicoIndenizacao.setDoctoServico(doctoServico);
					doctoServicoIndenizacao.setTpPrejuizo(sinistroDoctoServico.getTpPrejuizo());
					Integer qtVolumes = doctoServico.getQtVolumes();
					if (qtVolumes==null)
						qtVolumes = Integer.valueOf(0);
					doctoServicoIndenizacao.setQtVolumes(qtVolumes);
					doctoServicoIndenizacao.setVlIndenizado(sinistroDoctoServico.getVlPrejuizo());
					doctoServicoIndenizacao.setMoeda(sinistroDoctoServico.getMoeda());
					
				} else if ("documentos".equals(tfm.getString("myScreen"))) {
					
					DoctoServico doctoServico = doctoServicoService.findByIdJoinProcessoSinistro(tfm.getLong("doctoServico.idDoctoServico"));
					BigDecimal vlDocumentoServico = doctoServico.getVlMercadoria()!=null ? doctoServico.getVlMercadoria() : new BigDecimal(0.00);
					Moeda moedaDocto = doctoServico.getMoeda();
					BigDecimal vlIndenizacao = tfm.getBigDecimal("vlIndenizacao");
					Moeda moedaIndenizacao = moedaService.findById(tfm.getLong("moeda.idMoeda"));
					BigDecimal vlDocumentoConvertido = conversaoMoedaService.converteValor(moedaDocto.getIdMoeda(), moedaIndenizacao.getIdMoeda(), vlDocumentoServico, JTDateTimeUtils.getDataAtual());
					doctoServicoIndenizacao.setDoctoServico(doctoServico);
					// dados gerais
					Filial filial = SessionUtils.getFilialSessao();
					boolean isFilialMatriz = historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial());
					// verifica se nao for Matriz eo valor da indenização é maior que o valor do documento de servico
					if (!isFilialMatriz && CompareUtils.gt(vlIndenizacao, vlDocumentoConvertido)) {
						throw new BusinessException("LMS-21016");
					}

					doctoServicoIndenizacao.setMoeda(moedaIndenizacao);
					doctoServicoIndenizacao.setVlIndenizado(vlIndenizacao);
					doctoServicoIndenizacao.setIdDoctoServicoIndenizacao(tfm.getLong("idDoctoServicoIndenizacao"));
					ReciboIndenizacao reciboIndenizacao = new ReciboIndenizacao();
					reciboIndenizacao.setIdReciboIndenizacao(getMasterId(tfm));
					doctoServicoIndenizacao.setReciboIndenizacao(reciboIndenizacao);
					
					Integer qtVolumes = tfm.getInteger("qtVolumesIndenizados");
					if (qtVolumes==null){
						qtVolumes = Integer.valueOf(0);
					}
					Integer qtVolumesDocto = doctoServico.getQtVolumes();
					if (qtVolumesDocto==null){
						qtVolumesDocto = Integer.valueOf(0);
					}
					if (qtVolumes.intValue() > qtVolumesDocto.intValue()){
						throw new BusinessException("LMS-21039");
					}
					doctoServicoIndenizacao.setQtVolumes(qtVolumes);
					
					if (tfm.getLong("produto.idProduto")!=null) {
						Produto produto = produtoService.findById(tfm.getLong("produto.idProduto"));
						doctoServicoIndenizacao.setProduto(produto);
					}
										
					List listbox = tfm.getList("notasFiscais");
					if (listbox!=null) {
				    	List result = new ArrayList();
				    	for (Iterator it = listbox.iterator(); it.hasNext(); ) {
				    		TypedFlatMap item = (TypedFlatMap)it.next();
				    		Long idNotaFiscalConhecimento = item.getLong("idNotaFiscalConhecimento");
				    		NotaFiscalConhecimento notaFiscalConhecimento = notaFiscalConhecimentoService.findById(idNotaFiscalConhecimento);
				    		ReciboIndenizacaoNf reciboIndenizacaoNf = new ReciboIndenizacaoNf();
				    		reciboIndenizacaoNf.setNotaFiscalConhecimento(notaFiscalConhecimento);
				    		reciboIndenizacaoNf.setDoctoServicoIndenizacao(doctoServicoIndenizacao);
			    			result.add(reciboIndenizacaoNf);
				    	}
						doctoServicoIndenizacao.setReciboIndenizacaoNfs(result);
					}					
				}
				
				return doctoServicoIndenizacao;
			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				
				DoctoServicoIndenizacao newObject = (DoctoServicoIndenizacao)newBean;
				DoctoServicoIndenizacao oldObject = (DoctoServicoIndenizacao)bean;
				
				oldObject.setProduto(newObject.getProduto());
				oldObject.setMoeda(newObject.getMoeda());
				oldObject.setVlIndenizado(newObject.getVlIndenizado());
				oldObject.setQtVolumes(newObject.getQtVolumes());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());
				oldObject.setDoctoServico(newObject.getDoctoServico());
				
				List list = new ArrayList();
				if (newObject.getReciboIndenizacaoNfs()!=null) {
					for(int i=0; i<newObject.getReciboIndenizacaoNfs().size(); i++) {
						ReciboIndenizacaoNf reciboIndenizacaoNf = (ReciboIndenizacaoNf) newObject.getReciboIndenizacaoNfs().get(i);
						reciboIndenizacaoNf.setDoctoServicoIndenizacao(oldObject);
						reciboIndenizacaoNf.setIdReciboIndenizacaoNf(null);
						list.add(reciboIndenizacaoNf);
					}
					if (oldObject.getReciboIndenizacaoNfs()!=null)
						oldObject.getReciboIndenizacaoNfs().clear();
				}
					oldObject.setReciboIndenizacaoNfs(list);
		    }	
    	};
    	
    	
    	/********************************************************************
    	 * Documentos do RNC ItemListConfig
    	 ********************************************************************/
    	ItemListConfig doctoServicoIndenizacaoRNCConfig = new ItemListConfig() {
			public List initialize(Long masterId, Map parameters) {
				return null;
			}
			public Integer getRowCount(Long masterId, Map parameters) {
				return null;
			}
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) bean;
				doctoServicoIndenizacao.setReciboIndenizacao(getReciboIndenizacaoFromSession(getMasterId(tfm)));										

				/************************* Validações Básicas ao Salvar *****************************
				 * Estas validações poderiam estar no método saveItemDocumentosRNC, mas por questões 
				 * de otimização, optou-se por realizá-las neste método, buscando aproveitar os finds.
				 */				
		    	Long idDoctoServicoIndenizacao = tfm.getLong("idDoctoServicoIndenizacao");
		    	Long idDoctoServico = tfm.getLong("doctoServico.idDoctoServico");
		    	Long idCliente = tfm.getLong("idCliente");
		    	
				// documento de servico a incluir 
		    	DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);
		    	Long idFilialOrigemIncluido = doctoServico.getFilialByIdFilialOrigem().getIdFilial();
		    	Long idFilialDestinoIncluido = doctoServico.getFilialByIdFilialDestino() == null ? null : doctoServico.getFilialByIdFilialDestino().getIdFilial();

		    	// verifica se o cliente não é devedor, destinatário, consignatário ou remetente do documento
		    	// CQPRO00024250 - Adicionada nova validação: erro se o tipo benificiário for Terceiro mas a pessoa tiver relação com o documento.
		    	boolean isDevedorDocumento = devedorDocServService.getRowCountByIdDoctoServicoIdCliente(idDoctoServico, idCliente).intValue() > 0;    	
		    	boolean isRemetente = (doctoServico.getClienteByIdClienteRemetente()!=null && doctoServico.getClienteByIdClienteRemetente().getIdCliente().equals(idCliente));
		    	boolean isDestinatario = (doctoServico.getClienteByIdClienteDestinatario()!=null && doctoServico.getClienteByIdClienteDestinatario().getIdCliente().equals(idCliente));
		    	boolean isConsignatario = (doctoServico.getClienteByIdClienteConsignatario()!=null && doctoServico.getClienteByIdClienteConsignatario().getIdCliente().equals(idCliente));
		    	
		    	String escolhido = tfm.getDomainValue("tpBeneficiarioIndenizacao").getValue();
		    	
		    	String preenchido  = isDevedorDocumento 		? DEVEDORDOCUMENTO 	: "";
		    		   preenchido += isDestinatario 			? DESTINATARIO 		: "";
		    		   preenchido += isRemetente 				? REMETENTE 		: "";
		    		   preenchido += isConsignatario 			? CONSIGNATARIO		: "";
		    		   preenchido += preenchido.length() == 0 	? TERCEIRO 			: ""; 


		    	if(preenchido.equals(TERCEIRO)){
		    		if(!escolhido.equals(TERCEIRO)){
		    		throw new BusinessException("LMS-21046");
		    	}
		    	}else{
		    		if(escolhido.equals(TERCEIRO)){
		    			throw new BusinessException("LMS-21058");	
		    		}
		    		if(!preenchido.contains(escolhido)){
						throw new BusinessException("LMS-21059", new Object[] { domainValueService.findDomainValueByValue("DM_BENEFICIARIO_INDENIZACAO", preenchido.substring(0, 1)).getDescriptionAsString() });
		    		}
		    	}
		    	
		    	Filial filial = SessionUtils.getFilialSessao();
		    	Cliente cliente = clienteService.findById(idCliente);    	
		    	boolean isFilialResponsavel = cliente.getFilialByIdFilialAtendeOperacional().getIdFilial().equals(filial.getIdFilial());    	
				boolean isFilialOrigemDestino = filial.getIdFilial().equals(doctoServico.getFilialByIdFilialOrigem().getIdFilial()) || filial.getIdFilial().equals(doctoServico.getFilialByIdFilialDestino().getIdFilial());
				boolean isFilialMatriz = historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial());
				
				// verifica se a filial do usuario é diferente da filial responsavel operacional
				// e se a filial do usuario é diferente da filial origem ou filial destino
				if (!isFilialMatriz){ //Teste adicionado conforme CQPRO00024183
				if (!isFilialResponsavel && !isFilialOrigemDestino) {
					throw new BusinessException("LMS-21040");
				}
				}
				
		    	MasterEntry master      = getMasterFromSession(getMasterId(tfm), false);
				ItemList itens          = getItemsFromSession(master, "doctoServicoIndenizacaoRNC");
				
				ItemListConfig config   = getMasterConfig().getItemListConfig("doctoServicoIndenizacaoRNC");
				
				if (itens.hasItems()) {
					int itemIndex = 0; //Otimização para os casos em que só precisa fazer comparação com o primeiro item. 
					for (Iterator it=itens.iterator(null, config) ; it.hasNext(); itemIndex++) {
						DoctoServicoIndenizacao item = (DoctoServicoIndenizacao)it.next();
						
						// no caso de inclusão, não permite documentos de servico repetidos
						if (idDoctoServicoIndenizacao==null && idDoctoServico.equals(item.getDoctoServico().getIdDoctoServico())) {
							throw new BusinessException("LMS-21031");
						}
				
						if (itemIndex == 0){
							// documento de servico da itemList
							DoctoServico doctoServicoItemList = item.getDoctoServico();
							Long idFilialOrigemItemList = doctoServicoItemList.getFilialByIdFilialOrigem().getIdFilial();
							Long idFilialDestinoItemList = doctoServicoItemList.getFilialByIdFilialDestino() == null ? null : doctoServicoItemList.getFilialByIdFilialDestino().getIdFilial();
			        	
							// se a filial origem recebida for diferente da filial origem do docto da itemList ou 
							// se a filial destino recebida for diferente da filial destino do docto da itemList
							// ou ainda de uma se uma das filiais destino for nula e a outra não,
							// então dispara uma exceção
							if (!idFilialOrigemIncluido.equals(idFilialOrigemItemList) 
									|| (idFilialDestinoIncluido==null | idFilialDestinoItemList==null) // sim, isso é um xor
									|| (idFilialDestinoIncluido!=null && !idFilialDestinoIncluido.equals(idFilialDestinoItemList)) ) {
								throw new BusinessException("LMS-21030");
							}
		    			
							// para todos os documentos de serviço incluídos, as filiais responsáveis por suas respectivas RNCs devem ser iguais, isto é:
							// ao selecionar um documento de serviço, verificar se já existe algum outro documento para esse RIM
							// caso existir, dispara a exceção.
							List filiaisResponsaveisDoctoNovo = ocorrenciaNaoConformidadeService.findFiliaisResponsaveisOcorrenciasNaoConformidade(doctoServicoItemList.getIdDoctoServico());
							List filiaisResponsaveisDoctoExistente = ocorrenciaNaoConformidadeService.findFiliaisResponsaveisOcorrenciasNaoConformidade(idDoctoServico);
		    				if (!filiaisResponsaveisDoctoExistente.containsAll(filiaisResponsaveisDoctoNovo)){
		    					throw new BusinessException("LMS-21056");
		    				}
						}
					}
				}
				
				DevedorDocServ devedorDocServ = devedorDocServService.findDevedorByDoctoServico(doctoServico.getIdDoctoServico());
				String cnpjRaizTomador = devedorDocServ.getCliente().getPessoa().getNrIdentificacao().substring(0, 8);
				String paramBloqPagtoIndenizacao = (String) configuracoesFacade.getValorParametro("BLOQ_PAGTO_INDENIZACAO");
		    	List<String> cnpjsBloqPagtoIndenizacao = Arrays.asList(paramBloqPagtoIndenizacao.split(";"));
				if (conhecimentoService.validateDoctoServicoIsTpSubContratacaoIdDoctoServico(doctoServico.getIdDoctoServico())
						&& cnpjsBloqPagtoIndenizacao.contains(cnpjRaizTomador)) {
					throw new BusinessException("LMS-21096");
				}
				
				/****************************************************************************************************************/
								
				Integer qtVolumes = tfm.getInteger("qtVolumesIndenizados");
				if (qtVolumes==null){
					qtVolumes = Integer.valueOf(0);
				}
				Integer qtVolumesDocto = doctoServico.getQtVolumes();
				if (qtVolumesDocto==null){
					qtVolumesDocto = Integer.valueOf(0);
				}
				if (qtVolumes.intValue() > qtVolumesDocto.intValue()){
					throw new BusinessException("LMS-21039");
				}
				doctoServicoIndenizacao.setQtVolumes(qtVolumes);

				// obtém a nao conformidade do documento de servico
				NaoConformidade naoConformidade = naoConformidadeService.findNaoConformidadeByIdDoctoServico(idDoctoServico);
				if (naoConformidade == null) {
					//o documento de servico deve estar associado a um rnc
		    		throw new BusinessException("LMS-21018");
				}
				
				verifyNaoConformidade(naoConformidade.getIdNaoConformidade());

				// obtém primeira ocorrencia de nao conformidade da nao conformidade
				OcorrenciaNaoConformidade ocorrenciaNaoConformidade = ocorrenciaNaoConformidadeService.findFirstOcorrenciaByIdNaoConformidade(naoConformidade.getIdNaoConformidade());

				// obtém controle de carga e manifesto da ocorrencia, caso existam
				if (ocorrenciaNaoConformidade!=null) {
					// setando a ocorrencia (menor delas) na nao conformidade
					List ocorrencias = new ArrayList();
					ocorrencias.add(ocorrenciaNaoConformidade);
					naoConformidade.setOcorrenciaNaoConformidades(ocorrencias);
				}

				// setando a não conformidade (eh única) no documento de servico
				List naoConformidades = new ArrayList();
				naoConformidades.add(naoConformidade);
				doctoServico.setNaoConformidades(naoConformidades);
				
				// setando o documento de servico
				doctoServicoIndenizacao.setDoctoServico(doctoServico);
				
				BigDecimal vlDocumentoServico = doctoServico.getVlMercadoria()!=null ? doctoServico.getVlMercadoria() : new BigDecimal(0.00);
				Moeda moedaDocto = doctoServico.getMoeda();
				BigDecimal vlIndenizacao = tfm.getBigDecimal("vlIndenizacao");
				Moeda moedaIndenizacao = moedaService.findById(tfm.getLong("moeda.idMoeda"));
				BigDecimal vlDocumentoConvertido = conversaoMoedaService.converteValor(moedaDocto.getIdMoeda(), moedaIndenizacao.getIdMoeda(), vlDocumentoServico, JTDateTimeUtils.getDataAtual());
				
				// verifica se nao for Matriz eo valor da indenização é maior que o valor do documento de servico
				if (!isFilialMatriz && CompareUtils.gt(vlIndenizacao, vlDocumentoConvertido)) {
					throw new BusinessException("LMS-21016");
				}
				
				doctoServicoIndenizacao.setOcorrenciaNaoConformidade(ocorrenciaNaoConformidadeService.findById(tfm.getLong("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade")));
						
				if (tfm.getLong("filialSinistro.idFilial") != null) {
					doctoServicoIndenizacao.setFilialSinistro(filialService.findById(tfm.getLong("filialSinistro.idFilial")));
					doctoServicoIndenizacao.setRotaSinistro(filialService.findById(tfm.getLong("rotaSinistro.idFilial")));
				}
						
				doctoServicoIndenizacao.setMoeda(moedaIndenizacao);
				doctoServicoIndenizacao.setVlIndenizado(vlIndenizacao);
				doctoServicoIndenizacao.setIdDoctoServicoIndenizacao(tfm.getLong("idDoctoServicoIndenizacao"));
				ReciboIndenizacao reciboIndenizacao = new ReciboIndenizacao();
				reciboIndenizacao.setIdReciboIndenizacao(getMasterId(tfm));
				doctoServicoIndenizacao.setReciboIndenizacao(reciboIndenizacao);
				doctoServicoIndenizacao.setTpPrejuizo(new DomainValue("T"));
				
				if (tfm.getLong("produto.idProduto")!=null) {
					Produto produto = produtoService.findById(tfm.getLong("produto.idProduto"));
					doctoServicoIndenizacao.setProduto(produto);
				}
									
				List listbox = tfm.getList("notasFiscais");
				if (listbox!=null) {
			    	List result = new ArrayList();
			    	for (Iterator it = listbox.iterator(); it.hasNext(); ) {
			    		TypedFlatMap item = (TypedFlatMap)it.next();
			    		Long idNotaFiscalConhecimento = item.getLong("idNotaFiscalConhecimento");
			    		NotaFiscalConhecimento notaFiscalConhecimento = notaFiscalConhecimentoService.findById(idNotaFiscalConhecimento);
			    		ReciboIndenizacaoNf reciboIndenizacaoNf = new ReciboIndenizacaoNf();
			    		reciboIndenizacaoNf.setNotaFiscalConhecimento(notaFiscalConhecimento);
			    		reciboIndenizacaoNf.setDoctoServicoIndenizacao(doctoServicoIndenizacao);
			    		
			    		for (Object object : result) {
							ReciboIndenizacaoNf nf = (ReciboIndenizacaoNf)object;
							if(nf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento().equals(reciboIndenizacaoNf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento())) {
								throw new BusinessException("LMS-28004");
							}
							
						}
			    		
			    		//
			    		
			    		if (reciboIndenizacaoNf!=null)
			    			result.add(reciboIndenizacaoNf);
			    	}
					doctoServicoIndenizacao.setReciboIndenizacaoNfs(result);
				}					
				
				return doctoServicoIndenizacao;

			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				
				DoctoServicoIndenizacao newObject = (DoctoServicoIndenizacao)newBean;
				DoctoServicoIndenizacao oldObject = (DoctoServicoIndenizacao)bean;
								
				oldObject.setIdDoctoServicoIndenizacao(newObject.getIdDoctoServicoIndenizacao());
				oldObject.setProduto(newObject.getProduto());
				oldObject.setMoeda(newObject.getMoeda());
				oldObject.setVlIndenizado(newObject.getVlIndenizado());
				oldObject.setQtVolumes(newObject.getQtVolumes());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());

				
				oldObject.setOcorrenciaNaoConformidade(newObject.getOcorrenciaNaoConformidade());
				oldObject.setFilialSinistro(newObject.getFilialSinistro());
				oldObject.setRotaSinistro(newObject.getRotaSinistro());
				oldObject.setDoctoServico(newObject.getDoctoServico());
				
				List list = new ArrayList();
				if (newObject.getReciboIndenizacaoNfs()!=null) {
					for(int i=0; i<newObject.getReciboIndenizacaoNfs().size(); i++) {
						ReciboIndenizacaoNf reciboIndenizacaoNf = (ReciboIndenizacaoNf) newObject.getReciboIndenizacaoNfs().get(i);
						reciboIndenizacaoNf.setDoctoServicoIndenizacao(oldObject);
						reciboIndenizacaoNf.setIdReciboIndenizacaoNf(null);
						
						for (Object object : list) {
							ReciboIndenizacaoNf nf = (ReciboIndenizacaoNf)object;
							if(nf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento().equals(reciboIndenizacaoNf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento())) {
								throw new BusinessException("LMS-28004");
							}
							
						}
						
						list.add(reciboIndenizacaoNf);
					}
					if (oldObject.getReciboIndenizacaoNfs()!=null)
						oldObject.getReciboIndenizacaoNfs().clear();
				}
					oldObject.setReciboIndenizacaoNfs(list);
		    }	
    	};

    	
    	/********************************************************************
    	 * MDA ItemListConfig
    	 ********************************************************************/
    	ItemListConfig mdaConfig = new ItemListConfig() {
			public List initialize(Long masterId) {
				return null;
			}
			public Integer getRowCount(Long masterId) {
				return null;
			}
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				MdaSalvadoIndenizacao mdaSalvadoIndenizacao = (MdaSalvadoIndenizacao) bean;
		    	Long idMda = tfm.getLong("mda.idDoctoServico");
		    	mdaSalvadoIndenizacao.setIdMdaSalvadoIndenizacao(tfm.getLong("idMdaSalvadoIndenizacao"));
		    	
		    	Mda mda = mdaService.findById(idMda);
		    	Filial filialOrigem = filialService.findById(mda.getFilialByIdFilialOrigem().getIdFilial());
		    	mda.setFilialByIdFilialOrigem(filialOrigem);
		    	mdaSalvadoIndenizacao.setMda(mda);    
		    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);
		    	ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		    	mdaSalvadoIndenizacao.setReciboIndenizacao(reciboIndenizacao);

				// seta o pai
				if(getMasterId(tfm) != null) {
					mdaSalvadoIndenizacao.setReciboIndenizacao(getReciboIndenizacaoFromSession(getMasterId(tfm)));										
				}
								
				return mdaSalvadoIndenizacao;
			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				MdaSalvadoIndenizacao newObject = (MdaSalvadoIndenizacao)newBean;
				MdaSalvadoIndenizacao oldObject = (MdaSalvadoIndenizacao)bean;
				oldObject.setIdMdaSalvadoIndenizacao(newObject.getIdMdaSalvadoIndenizacao());
				oldObject.setMda(newObject.getMda());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());
		    }	
    	};
    	
    	/********************************************************************
    	 * Parcela ItemListConfig
    	 ********************************************************************/
    	ItemListConfig parcelaConfig = new ItemListConfig() {
			public List initialize(Long masterId) {
				return null;
			}
			public Integer getRowCount(Long masterId, Map parameters) {
				return null;
			}
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao) bean;

		    	parcelaReciboIndenizacao.setIdParcelaReciboIndenizacao(tfm.getLong("idParcelaReciboIndenizacao"));
		    	parcelaReciboIndenizacao.setDtVencimento(tfm.getYearMonthDay("dtVencimento"));    	
		    	parcelaReciboIndenizacao.setNrBoleto(tfm.getString("nrBoleto"));
		    	Moeda moeda = moedaService.findById(tfm.getLong("moeda.idMoeda"));
		    	parcelaReciboIndenizacao.setMoeda(moeda);
		    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);
		    	ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		    	parcelaReciboIndenizacao.setReciboIndenizacao(reciboIndenizacao);
		    	parcelaReciboIndenizacao.setTpStatusParcelaIndenizacao(new DomainValue("A"));
		    	parcelaReciboIndenizacao.setVlPagamento(tfm.getBigDecimal("vlPagamento"));		    	
				return parcelaReciboIndenizacao;
			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				ParcelaReciboIndenizacao newObject = (ParcelaReciboIndenizacao)newBean;
				ParcelaReciboIndenizacao oldObject = (ParcelaReciboIndenizacao)bean;
				
				oldObject.setIdParcelaReciboIndenizacao(newObject.getIdParcelaReciboIndenizacao());
				oldObject.setDtVencimento(newObject.getDtVencimento());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());
				oldObject.setMoeda(newObject.getMoeda());
				oldObject.setNrBoleto(newObject.getNrBoleto());
				oldObject.setTpStatusParcelaIndenizacao(newObject.getTpStatusParcelaIndenizacao());
				oldObject.setVlPagamento(newObject.getVlPagamento());
		    }	
    	};
    	
    	/********************************************************************
    	 * Anexo ItemListConfig
    	 ********************************************************************/
    	ItemListConfig anexoConfig = new ItemListConfig() {
    		
			public List initialize(Long masterId) {
				return new ArrayList();
			}
			
			public Integer getRowCount(Long masterId, Map parameters) {				
				return 0;
			}
			
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				
				AnexoRim anexoRim = (AnexoRim) bean;
				anexoRim.setIdAnexoRim(tfm.getLong("idAnexoRim"));
				anexoRim.setDescAnexo((String) tfm.get("descAnexo"));
				anexoRim.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
				anexoRim.setUsuarioLMS(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));				
				try {
					anexoRim.setDcArquivo(Base64Util.decode(tfm.getString("dcArquivo")));
				} catch (IOException e) {
					throw new InfrastructureException(e.getMessage());
				}
				
				MasterEntry master = getMasterFromSession(getMasterId(tfm), true);
		    	ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		    	anexoRim.setReciboIndenizacao(reciboIndenizacao);
		    	
				return anexoRim;
			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				AnexoRim newObject = (AnexoRim) newBean;
				AnexoRim oldObject = (AnexoRim)bean;
				
				oldObject.setIdAnexoRim(newObject.getIdAnexoRim());
				oldObject.setDhCriacao(newObject.getDhCriacao());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());
				oldObject.setDcArquivo(newObject.getDcArquivo());
				oldObject.setDescAnexo(newObject.getDescAnexo());
				oldObject.setUsuarioLMS(newObject.getUsuarioLMS());
		    }	
    	};
    	
		config.addItemConfig("doctoServicoIndenizacaoProcesso", DoctoServicoIndenizacao.class, doctoServicoIndenizacaoProcessoConfig, comparator);    	
		config.addItemConfig("doctoServicoIndenizacaoRNC",      DoctoServicoIndenizacao.class, doctoServicoIndenizacaoRNCConfig, comparator);
		config.addItemConfig("mda",                             MdaSalvadoIndenizacao.class, mdaConfig, comparator);
		config.addItemConfig("parcela",                         ParcelaReciboIndenizacao.class, parcelaConfig, comparator);
		config.addItemConfig("anexo", AnexoRim.class, anexoConfig, comparator);
		
		return config;
	}
	
	
	
    /*******************************************************************************
     * Store
     *******************************************************************************/
	public Serializable storeRIM(TypedFlatMap tfm) {

		YearMonthDay dtVencimento = tfm.getYearMonthDay("dtVencimento");
		YearMonthDay dtProgramadaPagto = tfm.getYearMonthDay("dtProgramadaPagamento"); 
		
		if (dtVencimento!=null && dtVencimento.compareTo(JTDateTimeUtils.getDataAtual()) < 0) {
			throw new BusinessException("LMS-21033");
		}

		if (dtProgramadaPagto!=null && dtProgramadaPagto.compareTo(JTDateTimeUtils.getDataAtual()) < 0) {
			throw new BusinessException("LMS-21034");
		}

		// tipo de indenizacao 
		DomainValue tpIndenizacao = tfm.getDomainValue("tpIndenizacao");

		// dados do master
		Long idReciboIndenizacao = tfm.getLong("idReciboIndenizacao");
		MasterEntry master = getMasterFromSession(idReciboIndenizacao, true);
		ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		Moeda moeda = moedaService.findById(tfm.getLong("moeda.idMoeda"));
		reciboIndenizacao.setMoeda(moeda);
		reciboIndenizacao.setDtGeracao(JTDateTimeUtils.getDataAtual());
		
		// se blSalvados=true então exige o itens de mda   
		Boolean blSalvados = ("S".equals(tfm.getString("blSalvados")) ? Boolean.TRUE : Boolean.FALSE);
		ItemList itensMda = getItemsFromSession(master, "mda");
		if (blSalvados.booleanValue() && !itensMda.hasItems()) {
			throw new BusinessException("LMS-21049");
		}
		
		// carregando itens de documentos
		ItemList itensDocto = null;
		ItemListConfig configDocto = null;
		
		// se processo de sinistro, carrega a item list de documentos do processo
		if (tpIndenizacao.getValue().equals("PS")) {
			itensDocto = getItemsFromSession(master, "doctoServicoIndenizacaoProcesso");
			configDocto = getMasterConfig().getItemListConfig("doctoServicoIndenizacaoProcesso");
			if (!itensDocto.hasItems()) {
				throw new BusinessException("LMS-21026");
			}
			
	    // se indenizacao é rnc, carrega a item list de documentos do rnc
		} else {
			itensDocto = getItemsFromSession(master, "doctoServicoIndenizacaoRNC");
			configDocto = getMasterConfig().getItemListConfig("doctoServicoIndenizacaoRNC");
			if (!itensDocto.hasItems()) {
				throw new BusinessException("LMS-21025");
			}
			
		}

		// iterando sobre a item list de documentos
		BigDecimal vlTotalIndenizado = new BigDecimal(0);
		int qtVolumes = 0;
		
		// itera sobre a item list de documentos
		for (Iterator it = itensDocto.iterator((Long)master.getMasterId(), configDocto); it.hasNext(); ) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
			
			// zerando as notas fiscais do documento para nova inserção
			List list = doctoServicoIndenizacao.getReciboIndenizacaoNfs();
			if (list!=null) {
				for(Iterator i=list.iterator(); i.hasNext(); ) {
					ReciboIndenizacaoNf reciboIndenizacaoNf = (ReciboIndenizacaoNf)i.next();
					reciboIndenizacaoNf.setIdReciboIndenizacaoNf(null);
				}
			}

			// verificando os produtos do cliente e validando sua obrigatoriedade
			List produtos = produtoService.findProdutoClienteRemetenteByIdDoctoServico(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico());
			boolean hasProdutos = (produtos.size()>0);

			// validando a obrigatoriedade do produto
			if (doctoServicoIndenizacao.getMoeda()==null || doctoServicoIndenizacao.getVlIndenizado()==null || (hasProdutos && doctoServicoIndenizacao.getProduto()==null)) {
				// se indenizacao = processo sinistro
				if (tpIndenizacao.getValue().equals("PS")) {
					throw new BusinessException("LMS-21014");
					
				// senao, se rnc e docto servico == null
				} else if (doctoServicoIndenizacao.getDoctoServico()==null) {
					throw new BusinessException("LMS-21015");
				}
			}

			// efetuando a contagem de volumes
			if (doctoServicoIndenizacao.getQtVolumes()!=null) {
				qtVolumes += doctoServicoIndenizacao.getQtVolumes().intValue();
			}
			
			// somando o valor total indenizado
			vlTotalIndenizado = vlTotalIndenizado.add(conversaoMoedaService.converteValor(doctoServicoIndenizacao.getMoeda().getIdMoeda(), reciboIndenizacao.getMoeda().getIdMoeda(), doctoServicoIndenizacao.getVlIndenizado(), reciboIndenizacao.getDtGeracao()));
			
		}
				
		// se formaPagamento=boleto e parcelas>1 exige itens de parcelas
		ItemList itensParcela = getItemsFromSession(master, "parcela");
		DomainValue tpFormaPagamento = tfm.getDomainValue("tpFormaPagamento");
		Byte qtParcelas = tfm.getByte("qtParcelasBoletoBancario");
		this.validarFormaPagamento(master, reciboIndenizacao, vlTotalIndenizado,
				tpFormaPagamento, qtParcelas, itensParcela);
		
		ItemList itensAnexoRim = getItemsFromSession(master, "anexo");
		
		// dados gerais
		Filial filial = SessionUtils.getFilialSessao();
		Usuario usuario = SessionUtils.getUsuarioLogado();
		
		/*
		 * Validações referentes ao campo VL_INDENIZACAO.
		 */
		this.validarValorTotalIndenizacao(tpFormaPagamento, vlTotalIndenizado, filial, moeda);
		
			
		ProcessoSinistro processoSinistro = null;
		if (tfm.getLong("processoSinistro.idProcessoSinistro")!=null) {
			processoSinistro = processoSinistroService.findById(tfm.getLong("processoSinistro.idProcessoSinistro"));
			reciboIndenizacao.setProcessoSinistro(processoSinistro);
		}
		Pessoa beneficiario = new Pessoa();
		beneficiario.setIdPessoa(tfm.getLong("clienteBeneficiario.idCliente"));
		reciboIndenizacao.setTpIndenizacao(tpIndenizacao);		
		reciboIndenizacao.setPessoaByIdBeneficiario(beneficiario);
		reciboIndenizacao.setTpBeneficiarioIndenizacao(tfm.getDomainValue("tpBeneficiarioIndenizacao"));
		reciboIndenizacao.setNrNotaFiscalDebitoCliente(tfm.getLong("nrNotaFiscalDebitoCliente"));
		reciboIndenizacao.setBlSalvados(blSalvados);
		reciboIndenizacao.setObReciboIndenizacao(tfm.getString("obReciboIndenizacao"));
		reciboIndenizacao.setTpFormaPagamento(tpFormaPagamento);
		reciboIndenizacao.setBlMaisUmaOcorrencia(Boolean.FALSE);
		reciboIndenizacao.setNrContaCorrente(tfm.getLong("nrContaCorrente"));
		reciboIndenizacao.setNrDigitoContaCorrente(tfm.getString("nrDigitoContaCorrente"));
		reciboIndenizacao.setQtParcelasBoletoBancario(qtParcelas);
		reciboIndenizacao.setFilial(filial);
		reciboIndenizacao.setUsuario(usuario);
		reciboIndenizacao.setTpStatusIndenizacao(new DomainValue("G"));
		reciboIndenizacao.setQtVolumesIndenizados(Integer.valueOf(qtVolumes));
		reciboIndenizacao.setVlIndenizacao(vlTotalIndenizado);
		reciboIndenizacao.setDtEmissao(JTDateTimeUtils.getDataAtual());
		
		reciboIndenizacao.setBlEmailPgto(Boolean.FALSE);
		//LMS-428 REQ001 Segurado com seguro próprio
		reciboIndenizacao.setBlSegurado(tfm.getBoolean("blSegurado"));
		Long idFavorecido = tfm.getLong("favorecido.idPessoa");
		if (idFavorecido!=null) {
			Pessoa favorecido = new Pessoa();
			favorecido.setIdPessoa(idFavorecido);
			reciboIndenizacao.setPessoaByIdFavorecido(favorecido);
			//LMS-4378
			reciboIndenizacao.setTpFavorecidoIndenizacao(reciboIndenizacao.getTpBeneficiarioIndenizacao());
		}
		reciboIndenizacao.setDtProgramadaPagamento(dtProgramadaPagto);
		Long idBanco = tfm.getLong("banco.idBanco");
		if (idBanco!=null) {
			Banco banco = new Banco();
			banco.setIdBanco(idBanco);
			reciboIndenizacao.setBanco(banco);
		}
		Long idAgencia = tfm.getLong("agenciaBancaria.idAgenciaBancaria");
		if (idAgencia!=null) {
			AgenciaBancaria agenciaBancaria = new AgenciaBancaria();
			agenciaBancaria.setIdAgenciaBancaria(idAgencia);
			reciboIndenizacao.setAgenciaBancaria(agenciaBancaria);	
		}
		
		/*********************************************************************/
		getMaster().storeIncluir(reciboIndenizacao, itensDocto, itensMda, itensParcela, itensAnexoRim, configDocto, tfm);		
		itensDocto.resetItemsState();
		itensMda.resetItemsState();
		itensParcela.resetItemsState();
		itensAnexoRim.resetItemsState();
		updateMasterInSession(master);
		/*********************************************************************/

		// dados retornados à tela após salvar
		TypedFlatMap result = new TypedFlatMap();
		result.put("idReciboIndenizacao", reciboIndenizacao.getIdReciboIndenizacao());
		result.put("nrReciboIndenizacao", reciboIndenizacao.getNrReciboIndenizacao());
		result.put("sgSimboloMoeda", reciboIndenizacao.getMoeda().getSiglaSimbolo());
		result.put("vlIndenizacao", reciboIndenizacao.getVlIndenizacao());
		result.put("qtVolumesIndenizados", reciboIndenizacao.getQtVolumesIndenizados());
		
		return result;
	}
	

	/**
	 * Valida se a forma de pagamento é boleto e a quantidade de parcelas esta correta juntamente com o valor.
	 * 
	 * @param master
	 * @param reciboIndenizacao
	 * @param vlTotalIndenizado
	 * @param tpFormaPagamento
	 * @param qtParcelas
	 * @param itensParcela
	 */
	private void validarFormaPagamento(MasterEntry master,
			ReciboIndenizacao reciboIndenizacao, BigDecimal vlTotalIndenizado,
			DomainValue tpFormaPagamento, Byte qtParcelas, ItemList itensParcela) {
		if ((tpFormaPagamento.getValue().equals("BO")) && (qtParcelas.intValue() > 1)) {
			
			// se nao possui parcelas
			if (!itensParcela.hasItems()) {
				throw new BusinessException("LMS-21050");			
			} 
			ItemListConfig configParcela  = getMasterConfig().getItemListConfig("parcela");
			// itera sobre a item list de parcelas
			BigDecimal vlTotalParcelas = BigDecimal.ZERO;
			for (Iterator it = itensParcela.iterator((Long)master.getMasterId(), configParcela); it.hasNext(); ) {
				ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao)it.next();
				vlTotalParcelas = vlTotalParcelas.add(conversaoMoedaService.converteValor(parcelaReciboIndenizacao.getMoeda().getIdMoeda(), reciboIndenizacao.getMoeda().getIdMoeda(), parcelaReciboIndenizacao.getVlPagamento(), reciboIndenizacao.getDtGeracao()));
			}
			
			// se a quantidade de parcelas é diferente do informado
			if (qtParcelas.intValue() != itensParcela.size()) {
				throw new BusinessException("LMS-21052");
			// se o total de parcelas é diferente do total dos documentos
			} else if (vlTotalParcelas.compareTo(vlTotalIndenizado) != 0) {
				throw new BusinessException("LMS-21087");
			}		
		}
	}



	/**
	 * Validações referentes ao campo VL_INDENIZACAO.
	 * 
	 * @param tpFormaPagamento
	 * @param vlTotalIndenizado
	 * @param filial
	 */
	private void validarValorTotalIndenizacao(DomainValue tpFormaPagamento,	BigDecimal vlTotalIndenizado, Filial filial, Moeda moeda) {
		BigDecimal vlSalarioMinimo = cotacaoIndicadorFinanceiroService.findVlCotacaoIndFinanceiro("SALMIN", SessionUtils.getPaisSessao().getIdPais(), JTDateTimeUtils.getDataAtual());
		// se nao há salário mínimo ou cotacao para ele, então dispara excecao
    	if (vlSalarioMinimo==null) { 
			throw new BusinessException("LMS-21028");
		}
		
		/*
		 * Se campo VL_INDENIZACAO for maior que salário mínimo x valor do parâmetro VL_LIMITE_SM_INDENIZ_FF 
		 * e filial do usuário logado for diferente de Matriz 
		 * e campo Forma de Pagamento (TP_FORMA_PAGAMENTO) = “PU”,
		 * exibir a mensagem LMS-21088, passando como parâmetro a moeda selecionada e o valor de salário mínimo x valor do parâmetro VL_LIMITE_SM_INDEZ_FF.
		 */
		BigDecimal vlLimiteSmIindenizFF = (BigDecimal)configuracoesFacade.getValorParametro("VL_LIMITE_SM_INDENIZ_FF");						
		
		BigDecimal salarioMinimoXLimit = vlSalarioMinimo.multiply(vlLimiteSmIindenizFF);
		if( (vlTotalIndenizado.compareTo(salarioMinimoXLimit) > 0)
			&& isUsuarioFilial(filial.getIdFilial())
			&& tpFormaPagamento.getValue().equals("PU")) {
			
				throw new BusinessException("LMS-21088", new Object[]{ moeda.getDsSimbolo(), salarioMinimoXLimit });
		}
		
	}
	
	private boolean isUsuarioFilial(Long idFilial) {
		
		return !historicoFilialService.validateFilialUsuarioMatriz(idFilial);
	}
	/*******************************************************************************
     * Main Screen Methods
     *******************************************************************************/
	public ResultSetPage findPaginatedFilialDebitada(TypedFlatMap tfm) {
		if (tfm.getLong("idReciboIndenizacao")==null)
			return null;
		
		List list = new ArrayList();
		ResultSetPage rsp = filialDebitadaService.findPaginatedFilialDebitadaReciboIndenizacao(tfm);
		
		for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			FilialDebitada filialDebitada = (FilialDebitada)it.next();
			
			TypedFlatMap result = new TypedFlatMap();
			result.put("idFilialDebitada", filialDebitada.getIdFilialDebitada());
			result.put("sgFilial", filialDebitada.getFilial().getSgFilial());
			result.put("nmFantasia", filialDebitada.getFilial().getPessoa().getNmFantasia());
			result.put("pcDebitado", filialDebitada.getPcDebitado());
			result.put("vlReembolso", filialDebitada.getVlReembolso());
			if (filialDebitada.getMoeda()!=null)
				result.put("sgSimboloVlReembolso", filialDebitada.getMoeda().getSiglaSimbolo());
			list.add(result);
		}
		
		rsp.setList(list);
		return rsp;
	}
	
	public Integer getRowCountFilialDebitada(TypedFlatMap tfm) {
		if (tfm.getLong("idReciboIndenizacao")==null)
			return Integer.valueOf(0);
		return filialDebitadaService.getRowCountFilialDebitadaReciboIndenizacao(tfm);
	}

    /*******************************************************************************
     * Documentos do Processo Methods
     *******************************************************************************/
	
	/**
	 * FindPaginated da tela de documentos do processo
	 * @param tfm
	 * @return
	 */
	public ResultSetPage findPaginatedDocumentosProcesso(TypedFlatMap tfm) {
		 ResultSetPage rsp = findPaginatedItemList(tfm, "doctoServicoIndenizacaoProcesso");
		 
		 List list = new ArrayList();
		 
		 for(Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			 DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
			 Map map = new HashMap();
			 map.put("idDoctoServicoIndenizacao", doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
			 map.put("tpDoctoServico", doctoServicoIndenizacao.getDoctoServico().getTpDocumentoServico());
			 map.put("sgFilialOrigemDocto", doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
			 map.put("nrDoctoServico", doctoServicoIndenizacao.getDoctoServico().getNrDoctoServico());
			 map.put("nmClienteRemetente", doctoServicoIndenizacao.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
			 map.put("sgSimboloTotalDocto", doctoServicoIndenizacao.getDoctoServico().getMoeda().getSiglaSimbolo());
			 map.put("vlTotalDoctoServico", doctoServicoIndenizacao.getDoctoServico().getVlMercadoria());
			 map.put("vlIndenizado", doctoServicoIndenizacao.getVlIndenizado());
			 map.put("qtVolumes", doctoServicoIndenizacao.getQtVolumes());
			 
			 if (doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialDestino()!=null)
				 map.put("sgFilialDestinoDocto", doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialDestino().getSgFilial());
			
			 if (doctoServicoIndenizacao.getDoctoServico().getClienteByIdClienteDestinatario()!=null)
				 map.put("nmClienteDestinatario", doctoServicoIndenizacao.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
			 
			 if (doctoServicoIndenizacao.getMoeda()!=null)
				 map.put("sgSimboloVlIndenizado", doctoServicoIndenizacao.getMoeda().getSiglaSimbolo());
			 
			 if (doctoServicoIndenizacao.getProduto()!=null)
				 map.put("dsProduto", doctoServicoIndenizacao.getProduto().getDsProduto());

			 // nao conformidade
			 NaoConformidade nc = naoConformidadeService.findByIdDoctoServicoJoinOcorrencias(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico());
			 if (nc !=null) {
				 map.put("sgFilialNaoConformidade", nc.getFilial().getSgFilial());
				 map.put("nrNaoConformidade", nc.getNrNaoConformidade());
			 }
			 list.add(map);
		 }
		 rsp.setList(list);
		 return rsp;
	}
	
	/**
	 * GetRowCount da tela de documentos do processo
	 * @param tfm
	 * @return
	 */
	public Integer getRowCountDocumentosProcesso(TypedFlatMap tfm) {
		return getRowCountItemList(tfm, "doctoServicoIndenizacaoProcesso");
	}
	
	/**
	 * Detalhamento da tela de documentos do processo
	 * @param key
	 * @return
	 */
    public TypedFlatMap findDocumentoProcessoByKey(MasterDetailKey key) {
    	DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) findItemById(key, "doctoServicoIndenizacaoProcesso");
    	DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
    	
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("idDoctoServicoIndenizacao", doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
    	tfm.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
    	tfm.put("doctoServico.tpDocumentoServico", doctoServico.getTpDocumentoServico().getValue());
    	tfm.put("doctoServico.filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
    	tfm.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
    	if (doctoServicoIndenizacao.getProduto()!=null) 
    		tfm.put("produto.idProduto", doctoServicoIndenizacao.getProduto().getIdProduto());
    	if (doctoServicoIndenizacao.getTpPrejuizo()!=null) {
    		tfm.put("tpPrejuizo.description", doctoServicoIndenizacao.getTpPrejuizo().getDescription().getValue());
    		tfm.put("tpPrejuizo.value", doctoServicoIndenizacao.getTpPrejuizo().getValue());
    	}
    	
    	if (doctoServicoIndenizacao.getMoeda()!=null) 
    		tfm.put("moeda.idMoeda", doctoServicoIndenizacao.getMoeda().getIdMoeda());
    	tfm.put("qtVolumesIndenizados", doctoServicoIndenizacao.getQtVolumes());
    	tfm.put("vlIndenizacao", doctoServicoIndenizacao.getVlIndenizado());
    	putNotasFiscaisIntoTypedFlatMap(doctoServico.getIdDoctoServico(), doctoServico.getTpDocumentoServico().getValue(), doctoServicoIndenizacao.getTpPrejuizo().getValue(), tfm, doctoServicoIndenizacao.getReciboIndenizacaoNfs());
    	return tfm;
    }	
	
	/**
	 * Método que busca uma NaoConformidade a partir de um ID de DoctoServico.
	 * 
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap getDadosNaoConformidade(TypedFlatMap criteria) {
		TypedFlatMap mapNC = new TypedFlatMap();
		DoctoServicoIndenizacao doctoServico = doctoServicoIndenizacaoService.findById(criteria.getLong("idDoctoServico"));	   
		NaoConformidade naoConformidade = naoConformidadeService.findNaoConformidadeByIdDoctoServico(doctoServico.getDoctoServico().getIdDoctoServico());
		if (naoConformidade != null) {
			mapNC.put("naoConformidade.idNaoConformidade", naoConformidade.getIdNaoConformidade());
			mapNC.put("naoConformidade.filial.sgFilial", naoConformidade.getFilial().getSgFilial());
			mapNC.put("naoConformidade.nrRnc", naoConformidade.getNrNaoConformidade());
			mapNC.put("idDoctoServico", criteria.getLong("idDoctoServico"));
		}
		return mapNC;
	}

    /**
     * FindPaginated da popup de seleção de  documentos do processo
     * @param tfm
     * @return
     */
	public ResultSetPage findPaginatedSelecaoDocumentos(TypedFlatMap tfm) {
		Long masterId = getMasterId(tfm);
		Long idCliente = tfm.getLong("idCliente");
		Long idProcessoSinistro = tfm.getLong("idProcessoSinistro"); 
		List idsDoctoServico = getIdsDoctoServicoFromDocumentosProcessoSession(masterId); 
		String tpBeneficiarioIndenizacao = tfm.getString("tpBeneficiarioIndenizacao");
		Boolean isFilialMatriz = SessionUtils.isFilialSessaoMatriz();
		
		ResultSetPage rsp = sinistroDoctoServicoService.findPaginatedByIdProcessoSinistro(idProcessoSinistro, tfm, idsDoctoServico, tpBeneficiarioIndenizacao, idCliente, isFilialMatriz);
		List list = new ArrayList();
		for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			Map result = (Map)it.next();
			Map map = new HashMap(9);
			map.put("idDoctoServico",     result.get("idDoctoServico"));
			map.put("tpDocumentoServico", result.get("tpDocumentoServico"));
			map.put("sgFilialOrigem",     result.get("sgFilialOrigem"));
			map.put("nrDocumentoServico", result.get("nrDoctoServico"));
			map.put("sgFilialDestino",    result.get("sgFilialDestino"));
			map.put("nmRemetente",        result.get("nmRemetente"));
			map.put("nmDestinatario",     result.get("nmDestinatario"));
  		
    		list.add(map);			
		}
		rsp.setList(list);
		return rsp;
	}
	
	/**
	 * GetRowCount da popup de seleção de documentos do processo
	 * @param tfm
	 * @return
	 */
	public Integer getRowCountSelecaoDocumentos(TypedFlatMap tfm) {
		return sinistroDoctoServicoService.getRowCountByIdProcessoSinistro(tfm.getLong("idProcessoSinistro"), getIdsDoctoServicoFromDocumentosProcessoSession(getMasterId(tfm)), tfm.getString("tpBeneficiarioIndenizacao"), tfm.getLong("idCliente"), SessionUtils.isFilialSessaoMatriz());
	}
	
	/**
	 * Store da tela de documentos do processo
	 * @param tfm
	 */
	public void saveItemDocumentosProcesso(TypedFlatMap tfm) {
		
		validarQuantidadeDocumentosPorRecibo(tfm, "doctoServicoIndenizacaoProcesso");
		
		// se chamado da popup de selecao, então salva na sessão os documentos informados 
		if ("selecao".equals(tfm.getString("myScreen"))) {
			List ids = tfm.getList("ids");
			for(Iterator it = ids.iterator(); it.hasNext(); ) {
				Long idDoctoServico = Long.valueOf((String)it.next());
				TypedFlatMap map = new TypedFlatMap();
				map.put("idDoctoServico", idDoctoServico);
				map.put("masterId", tfm.getString("masterId"));
				map.put("myScreen", tfm.getString("myScreen"));
				saveItemInstance(map, "doctoServicoIndenizacaoProcesso");
			}
		// se veio da tela de documentos do processo, então salva o item
		} else if ("documentos".equals(tfm.getString("myScreen"))) {
			saveItemInstance(tfm, "doctoServicoIndenizacaoProcesso");
		}
	}	
	
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeDocumentosByIds(List ids) {
		super.removeItemByIds(ids, "doctoServicoIndenizacaoProcesso");
	}

    /*******************************************************************************
     * Documentos do RNC Methods
     *******************************************************************************/
	
	/**
	 * Find paginated da tela de documentos do RNC
	 * @param tfm
	 * @return
	 */
	public ResultSetPage findPaginatedDocumentosRNC(TypedFlatMap tfm) {
		 ResultSetPage rsp = findPaginatedItemList(tfm, "doctoServicoIndenizacaoRNC");
		 
		 List list = new ArrayList();
		 
		 for(Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			 DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
			 DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
			 
			 Map map = new HashMap();
			 map.put("idDoctoServicoIndenizacao", doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
			 map.put("tpDoctoServico", doctoServico.getTpDocumentoServico());
			 map.put("sgFilialOrigemDocto", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
			 map.put("nrDoctoServico", doctoServico.getNrDoctoServico());
			 map.put("nmClienteRemetente", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
			 map.put("sgSimboloTotalDocto", doctoServico.getMoeda().getSiglaSimbolo());
			 map.put("vlTotalDoctoServico", doctoServico.getVlMercadoria());
			 map.put("vlIndenizado", doctoServicoIndenizacao.getVlIndenizado());
			 map.put("qtVolumes", doctoServicoIndenizacao.getQtVolumes());
			 
			 if (doctoServico.getFilialByIdFilialDestino()!=null)
				 map.put("sgFilialDestinoDocto", doctoServico.getFilialByIdFilialDestino().getSgFilial());
			 
			 if (doctoServico.getClienteByIdClienteDestinatario()!=null)
				 map.put("nmClienteDestinatario", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
			 
			 if (doctoServicoIndenizacao.getMoeda()!=null)
				 map.put("sgSimboloVlIndenizado", doctoServicoIndenizacao.getMoeda().getSiglaSimbolo());
			 
			 // relacionamento one-to-one com o documento
			 NaoConformidade naoConformidade = (NaoConformidade)doctoServico.getNaoConformidades().get(0);
			 
			 if (naoConformidade!=null && naoConformidade.getOcorrenciaNaoConformidades()!=null) {

				 // seta dados da nao conformidade
				 map.put("sgFilialNaoConformidade", naoConformidade.getFilial().getSgFilial());
				 map.put("nrNaoConformidade", naoConformidade.getNrNaoConformidade());
				 
				 // lista apenas a primeira ocorrencia de nao conformidade da nao conformidade
				 OcorrenciaNaoConformidade ocorrenciaNaoConformidade = (OcorrenciaNaoConformidade)naoConformidade.getOcorrenciaNaoConformidades().get(0);

				 // tudo isso para listar dados do controle de cargas
				 if (ocorrenciaNaoConformidade.getControleCarga()!=null) {
					 map.put("sgFilialControleCarga", ocorrenciaNaoConformidade.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
					 map.put("nrControleCarga", ocorrenciaNaoConformidade.getControleCarga().getNrControleCarga());
				 }
				 
				 // e do manifesto
				 if (ocorrenciaNaoConformidade.getManifesto()!=null) {
					 map.put("sgFilialManifesto", ocorrenciaNaoConformidade.getManifesto().getFilialByIdFilialOrigem().getSgFilial());
					 // se manifesto viagem nacional
					 if (ocorrenciaNaoConformidade.getManifesto().getManifestoViagemNacional()!=null) {
						 map.put("nrManifesto", ocorrenciaNaoConformidade.getManifesto().getManifestoViagemNacional().getNrManifestoOrigem());
					 // se manifesto internacional
					 } else if (ocorrenciaNaoConformidade.getManifesto().getManifestoInternacional()!=null) {
						 map.put("nrManifesto", ocorrenciaNaoConformidade.getManifesto().getManifestoInternacional().getNrManifestoInt());
					 // se manifesto de entrega
					 } else if (ocorrenciaNaoConformidade.getManifesto().getManifestoEntrega()!=null) {
						 map.put("nrManifesto", ocorrenciaNaoConformidade.getManifesto().getManifestoEntrega().getNrManifestoEntrega());
					 }					 
				 }
			 }
			 
			 if (doctoServicoIndenizacao.getProduto()!=null)
				 map.put("dsProduto", doctoServicoIndenizacao.getProduto().getDsProduto());

			 list.add(map);
		 }
		 rsp.setList(list);
		 return rsp;
	}
	
	public Integer getRowCountDocumentosRNC(TypedFlatMap tfm) {
		return getRowCountItemList(tfm, "doctoServicoIndenizacaoRNC");
	}

	/**
	 * Detalhamento da tela de documentos do RNC
	 * @param key
	 * @return
	 */
    public TypedFlatMap findDocumentoRNCByKey(MasterDetailKey key) {
    	DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) findItemById(key, "doctoServicoIndenizacaoRNC");
    	TypedFlatMap tfm = new TypedFlatMap();
    	DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
    	tfm.put("idDoctoServicoIndenizacao", doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
    	tfm.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
    	tfm.put("doctoServico.tpDocumentoServico", doctoServico.getTpDocumentoServico().getValue());
    	tfm.put("doctoServico.filialByIdFilialOrigem.idFilial", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
    	tfm.put("doctoServico.filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
    	tfm.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
    	tfm.put("qtVolumesIndenizados", doctoServicoIndenizacao.getQtVolumes());
    	tfm.put("vlIndenizacao", doctoServicoIndenizacao.getVlIndenizado());
    	
    	if (doctoServicoIndenizacao.getProduto()!=null) 
    		tfm.put("produto.idProduto", doctoServicoIndenizacao.getProduto().getIdProduto());
    	if (doctoServicoIndenizacao.getMoeda()!=null) 
    		tfm.put("moeda.idMoeda", doctoServicoIndenizacao.getMoeda().getIdMoeda());
    	
    	NaoConformidade naoConformidade = (NaoConformidade)doctoServico.getNaoConformidades().get(0);
    	
    	tfm.put("naoConformidade.filial.sgFilial", naoConformidade.getFilial().getSgFilial());
    	tfm.put("naoConformidade.idNaoConformidade", naoConformidade.getIdNaoConformidade());
    	tfm.put("naoConformidade.nrRnc", new DecimalFormat("00000000").format(naoConformidade.getNrNaoConformidade()));
    	tfm.put("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", doctoServicoIndenizacao.getOcorrenciaNaoConformidade().getIdOcorrenciaNaoConformidade());
    	
    	if (doctoServicoIndenizacao.getFilialSinistro() != null && doctoServicoIndenizacao.getRotaSinistro() != null) {
    		String idRotas = doctoServicoIndenizacao.getFilialSinistro().getIdFilial()+"|"+doctoServicoIndenizacao.getRotaSinistro().getIdFilial();
    		tfm.put("rotasId", idRotas);    	
    	}
    	findDoctoServicoRelateds(tfm);
    	putNotasFiscaisIntoTypedFlatMap(doctoServico.getIdDoctoServico(), doctoServico.getTpDocumentoServico().getValue(), null, tfm, doctoServicoIndenizacao.getReciboIndenizacaoNfs());
    	//LMS-666 Criacao da combo de rotas de sinistro
    	
    	List ordensFluxoFilial = null;
    	if (doctoServico.getFluxoFilial() != null) {
    		ordensFluxoFilial = ordemFilialFluxoService.findByIdFluxoFilial(doctoServico.getFluxoFilial().getIdFluxoFilial());
    	}
    	
    	// monta lista de combo de rotas
    	List rotas = new ArrayList();
		
		
    	if(ordensFluxoFilial!=null){
			for(int i=0; i<ordensFluxoFilial.size()-1; i++) {
				Map map = new HashMap();
				OrdemFilialFluxo ordemFilialOrigem = (OrdemFilialFluxo)ordensFluxoFilial.get(i);
				OrdemFilialFluxo ordemFilialDestino = (OrdemFilialFluxo)ordensFluxoFilial.get(i+1);
				String idFilialOrigemDestino = ordemFilialOrigem.getFilial().getIdFilial()+"|"+ordemFilialDestino.getFilial().getIdFilial();
				String labelFilialOrigemDestino = ordemFilialOrigem.getNrOrdem() + " - "+ ordemFilialOrigem.getFilial().getSgFilial()+" - "
				+" --> "+  ordemFilialDestino.getNrOrdem() + " - "+ ordemFilialDestino.getFilial().getSgFilial();
				//label ORDEM - FILIAL --> ORDEM - FILIAL
				map.put("idFilialOrigemDestino", idFilialOrigemDestino);
				map.put("labelFilialOrigemDestino", labelFilialOrigemDestino);
				rotas.add(map);
			}
    	}
    	tfm.put("rotas", rotas);
    	return tfm;
    }	


	public TypedFlatMap saveItemDocumentosRNC(TypedFlatMap tfm) {
	
		// LMS-6154
		// 3. Se existir registro em SINISTRO_DOCTO_SERVICO.ID_DOCTO_SERVICO = “ID do documento” E  SINISTRO_DOCTO_SERVICO.TP_PREJUIZO <> “S” Então
		List idsSinistroDoctoServico = sinistroDoctoServicoService.findIdsSinistroDoctoServicoByIdDoctoServicoComPrejuizo(tfm.getLong("doctoServico.idDoctoServico"));
		
		if(idsSinistroDoctoServico != null && !idsSinistroDoctoServico.isEmpty()){
			
			// Se filial do usuário logado for Matriz Então
			if(SessionUtils.isFilialSessaoMatriz()){
				new WarningCollector("LMS-21092 - " + configuracoesFacade.getMensagem("LMS-21092"));
			}else{
				throw new BusinessException("LMS-21092");
			}
		}
		
		if(!validaDocumentoServico(tfm)){
			throw new BusinessException("LMS-21027"); 
		} else if(!validaDocumentoServicoMatriz(tfm)){
			new WarningCollector("LMS-21027 - " + configuracoesFacade.getMensagem("LMS-21027"));
		}

		validarQuantidadeDocumentosPorRecibo(tfm, "doctoServicoIndenizacaoRNC");
		
		String[] rotas = tfm.getString("rotasId").split("\\|");
		if(rotas!=null && rotas.length == 2){
			tfm.put("filialSinistro.idFilial", Long.valueOf(rotas[0]));
			tfm.put("rotaSinistro.idFilial",Long.valueOf(rotas[1]));
		}
		saveItemInstance(tfm, "doctoServicoIndenizacaoRNC");			
		TypedFlatMap mapRetorno = new TypedFlatMap();
		WarningCollectorUtils.putAll(mapRetorno);
		return mapRetorno;
	}
	
	private void validarQuantidadeDocumentosPorRecibo(TypedFlatMap tfm, String identificadorAba) {
		boolean validado = true;
		MasterEntry master = getMasterFromSession(getMasterId(tfm), false);
		ItemList itens = getItemsFromSession(master, identificadorAba);
		 
		if (itens != null && itens.size() >= 1) {
			/* Caso já exista um documento de serviço adicionado, verifica se é uma edição do mesmo. */
			Long idDoctoServico = tfm.getLong("doctoServico.idDoctoServico");
			if (idDoctoServico != null) {
				DoctoServicoIndenizacao item = (DoctoServicoIndenizacao) itens.iterator(null, getMasterConfig().getItemListConfig(identificadorAba)).next();
				if(!idDoctoServico.equals(item.getDoctoServico().getIdDoctoServico())){
					validado = false;
				}
			} else {
				validado = false;
			}
		}
		
		if (validado && tfm.getList("ids") != null && tfm.getList("ids").size() > 1){
			validado = false;
		}
		
		if(!validado){
			throw new BusinessException("LMS-21089");
		}
	}
	
	private boolean validaDocumentoServico(TypedFlatMap tfm) {
		//LMS-666 Incluir validação do documento serviço.
		/*
		 * Apenas um documento com o mesmo identificador deve ser informado em DoctoServicoIndenizacao, 
		 * caso já tenha sido informado e ReciboIndenizacao.tpStatusIndenizacao != ‘C’, exibir a mensagem LMS-21027 
		 * “O Documento de Serviço já foi indenizado”, caso filial logada seja igual a Matriz, o processo deve prosseguir, 
		 * caso seja filial o processo deve ser impedido de continuar
		 */   
		Long idDoctoServico = (tfm.getLong("idDoctoServico")!= null ? tfm.getLong("idDoctoServico") : tfm.getLong("doctoServico.idDoctoServico") );
		if(idDoctoServico == null){
			return true;
		}
		
		if (!isUsuarioFilial(SessionUtils.getFilialSessao().getIdFilial())) {
			return true;
		}
		//Retorna documentos indenizados que não estao cancelados
		List<DoctoServicoIndenizacao> documentosIndenizados = doctoServicoIndenizacaoService.findByIdDoctoServico(idDoctoServico);
		if (documentosIndenizados == null || documentosIndenizados.isEmpty()) {
			return true;
		} 

		return false;
	}
	
	private boolean validaDocumentoServicoMatriz(TypedFlatMap tfm) {
		//LMS-666 Incluir validação do documento serviço.
		/*
		 * Apenas um documento com o mesmo identificador deve ser informado em DoctoServicoIndenizacao, 
		 * caso já tenha sido informado e ReciboIndenizacao.tpStatusIndenizacao != ‘C’, exibir a mensagem LMS-21027 
		 * “O Documento de Serviço já foi indenizado”, caso filial logada seja igual a Matriz, o processo deve prosseguir, 
		 * caso seja filial o processo deve ser impedido de continuar
		 */   
		Long idDoctoServico = (tfm.getLong("idDoctoServico")!= null ? tfm.getLong("idDoctoServico") : tfm.getLong("doctoServico.idDoctoServico") );
		if(idDoctoServico == null){
			return true;
		}
		
		if (!historicoFilialService.validateFilialUsuarioMatriz(SessionUtils.getFilialSessao().getIdFilial())) {
			return true;
		}
		//Retorna documentos indenizados que não estao cancelados
		List<DoctoServicoIndenizacao> documentosIndenizados = doctoServicoIndenizacaoService.findByIdDoctoServico(idDoctoServico);
		if (documentosIndenizados == null || documentosIndenizados.isEmpty()) {
			return true;
		} 

		return false;
	}
	
	/**
	 *-
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeDocumentosRNCByIds(List ids) {
		super.removeItemByIds(ids, "doctoServicoIndenizacaoRNC");
	}

    /*******************************************************************************
     * General Finders
     *******************************************************************************/
	
	/**
	 * Obtém as notas fiscais do conhecimento
	 * @param idConhecimento
	 * @return
	 */
	private List findNotasFiscaisByIdConhecimento(Long idConhecimento) {
		List notas = new ArrayList();
		for (Iterator it = notaFiscalConhecimentoService.findByConhecimento(idConhecimento).iterator(); it.hasNext(); ) {
			NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento)it.next();
			Map map = new HashMap();
			map.put("idNotaFiscalConhecimento", notaFiscalConhecimento.getIdNotaFiscalConhecimento());
			map.put("nrNotaFiscal", notaFiscalConhecimento.getNrNotaFiscal());
			notas.add(map);
		} 
		return notas;
	}
	
	public List findComboMoeda(Map map) {
		return this.moedaService.findMoedaByPais(SessionUtils.getPaisSessao().getIdPais(), true);
    }
    
    public List findLookupBanco(TypedFlatMap tfm) {
    	return this.bancoService.findLookup(tfm);
    }
    
	public List findComboTpBeneficiarioIndenizacao(TypedFlatMap tfm) {
		List<DomainValue> retorno = new ArrayList();
		List<DomainValue> dominios = this.domainValueService.findDomainValues("DM_BENEFICIARIO_INDENIZACAO_INCLUIR");

		/* LMS - 3590: Exibir o tipo "T" = Terceiro somente se filial do usuário logado for igual à Matriz (MTZ). */
		for (DomainValue domainValue : dominios) {
			if ("T".equals(domainValue.getValue())) {
				if (SessionUtils.isFilialSessaoMatriz()) {
					retorno.add(domainValue);
				}
			} else {
				retorno.add(domainValue);
			}
		}

		return retorno;
	}
    
    public List findLookupCliente(Map criteria){    	
    	List<Map> list = new ArrayList<Map>(); 
    	for (Iterator it = clienteService.findLookup(criteria).iterator(); it.hasNext(); ) {
    		Cliente cliente = (Cliente)it.next();
    		TypedFlatMap map = new TypedFlatMap();
    		map.put("idCliente", cliente.getIdCliente());
    		map.put("pessoa.nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
    		map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
    		map.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
    		list.add(map);
    	}
    	return list;
    }

    public List findLookupAgencia(Map map) {
   		return agenciaBancariaService.findLookup(map);
    }

    public List findLookupPessoa(TypedFlatMap tfm) {
    	tfm.put("nrIdentificacao", tfm.getString("pessoa.nrIdentificacao"));
    	return pessoaService.findLookup(tfm);
    }

	public List findLookupMDA(TypedFlatMap criteria) {
		Long nrDoctoServico = criteria.getLong("nrDoctoServico");
		Long idFilialOrigem = criteria.getLong("filialByIdFilialOrigem.idFilial");
		List result = mdaService.findMdaByNrDoctoServicoByIdFilialOrigem(nrDoctoServico, idFilialOrigem);
    	List listMda = new ArrayList();
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
			Mda mda = (Mda) iter.next();			
    		TypedFlatMap mapMda = new TypedFlatMap();    		
    		mapMda.put("idDoctoServico", mda.getIdDoctoServico());
    		mapMda.put("nrDoctoServico", mda.getNrDoctoServico());
    		mapMda.put("dhInclusao", mda.getDhInclusao());
    		mapMda.put("filialByIdFilialOrigem.sgFilial", mda.getFilialByIdFilialOrigem().getSgFilial());
    		listMda.add(mapMda);    		
    	}
    	return listMda;		
	}

    /**
     * Obtém o processo de sinistro.<br>
     * @param tfm
     * @return
     */
    public List findLookupProcessoSinistro(TypedFlatMap tfm) {
    	TypedFlatMap resultData = new TypedFlatMap();
    		List result =  this.processoSinistroService.findLookup(tfm);
			if (result.size()==1) {
				ProcessoSinistro processoSinistro = (ProcessoSinistro) result.get(0);
				resultData.put("idProcessoSinistro", processoSinistro.getIdProcessoSinistro());
			resultData.put("nrProcessoSinistro", processoSinistro.getNrProcessoSinistro());
				resultData.put("processoSinistro.tipoSeguro.sgTipo", processoSinistro.getTipoSeguro().getSgTipo());			
				result.clear();
				result.add(resultData);
				return result;
			}

    	throw new BusinessException("LMS-00061");
    }

    public TypedFlatMap findQtde(TypedFlatMap tfm) {
    	Long idNaoConformidade = tfm.getLong("idNaoConformidade");
    	Long idOcorrencia = tfm.getLong("idOcorrenciaNaoConformidade");
    	Integer totalOcorrencias = 0;
    	if (idOcorrencia != null) {
    		OcorrenciaNaoConformidade ocorrenciaNaoConformidade = ocorrenciaNaoConformidadeService.findById(idOcorrencia);
    		totalOcorrencias = ocorrenciaNaoConformidade.getQtVolumes();	
    	}
    	tfm.put("qtVolumesIndenizados", totalOcorrencias);
    	return tfm;
    }
    
    /**
     * Obtém valores para a combo de Produtos
     * @param tfm
     * @return
     */
    public TypedFlatMap findComboProdutoByIdDoctoServico(TypedFlatMap tfm) {
    	Long idDoctoServico = tfm.getLong("idDoctoServico");
    	List produtosOut = new ArrayList();
    	List produtosIn = produtoService.findProdutoClienteRemetenteByIdDoctoServico(idDoctoServico);
    	for (Iterator it = produtosIn.iterator(); it.hasNext(); ) {
    		Produto produto = (Produto)it.next();
    		Map map = new HashMap();
    		map.put("idProduto", produto.getIdProduto());
    		map.put("dsProduto", produto.getDsProduto());
    		produtosOut.add(map);
    	}
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idProduto", tfm.getLong("idProduto"));
    	result.put("produtos", produtosOut);
    	return result;
    }
    
    /**
     * Obtém dados bancarios da pessoa
     * @param tfm
     * @return
     */
    public TypedFlatMap findDadosBancariosPessoa(TypedFlatMap tfm) {
    	Long idBeneficiario = tfm.getLong("idPessoa");
    	TypedFlatMap result = new TypedFlatMap();
    	if (idBeneficiario!=null) {
    		List list = contaBancariaService.findContasBancariasByPessoa(idBeneficiario);
    		if (list.size()>0) {
    			ContaBancaria contaBancaria = (ContaBancaria)list.get(0);
	    		result.put("favorecido.idPessoa", contaBancaria.getPessoa().getIdPessoa());
	    		result.put("favorecido.nmPessoa", contaBancaria.getPessoa().getNmPessoa());
	    		result.put("favorecido.nrIdentificacao", FormatUtils.formatIdentificacao(contaBancaria.getPessoa()));
	    		result.put("banco.idBanco", contaBancaria.getAgenciaBancaria().getBanco().getIdBanco());
	    		result.put("banco.nrBanco", contaBancaria.getAgenciaBancaria().getBanco().getNrBanco());
	    		result.put("banco.nmBanco", contaBancaria.getAgenciaBancaria().getBanco().getNmBanco());
	    		result.put("agenciaBancaria.idAgenciaBancaria", contaBancaria.getAgenciaBancaria().getIdAgenciaBancaria());
	    		result.put("agenciaBancaria.nrAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNrAgenciaBancaria());
	    		result.put("agenciaBancaria.nmAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNmAgenciaBancaria());
	    		result.put("conta.nrContaCorrente", contaBancaria.getNrContaBancaria());
	    		result.put("conta.nrDigitoContaCorrente", contaBancaria.getDvContaBancaria());
    		}
    	}
    	return result;
    }

    /**
     * Gera o evento de RIM do processo que emite o 
     * relatório de recibos de indenização por mercadorias
     * @param tfm
     */
    public void executeEmitirReciboRimGeraEventoRIM(TypedFlatMap tfm) {
    	Long idRim = tfm.getLong("idReciboIndenizacao");
    	MasterEntry masterEntry = getMasterFromSession(idRim, true);
    	ReciboIndenizacao master = (ReciboIndenizacao)masterEntry.getMaster();
    	getMaster().executeEmitirReciboRIM(master);
    	updateMasterInSession(masterEntry);
    }
    
    /**
     * Emite o relatório de recibos de indenizacao por mercadorias
     * @param tfm
     * @return
     */
    public File executeEmitirReciboRimReport(TypedFlatMap tfm) {
    	tfm.put("reciboIndenizacao.idReciboIndenizacao", tfm.getLong("idReciboIndenizacao"));
    	File result = null;
    	try {
			 result = emitirReciboRIMService.executeReport(tfm);
		} catch (Exception e) {
			// TODO avaliar este tratamento de Exception ver se é realmente necessário
			log.error(e);
		}
		return result;
    }
    
    
    /***************************************************************************************
     * Utilities
     ***************************************************************************************/
    
	/**
	 * Obtém os ids dos documentos do processo
	 * @param masterId
	 * @return
	 */
	private List getIdsDoctoServicoFromDocumentosProcessoSession(Long masterId) {
		MasterEntry master            = getMasterFromSession(masterId, true);
		ItemList itensProcesso        = getItemsFromSession(master, "doctoServicoIndenizacaoProcesso");
		ItemListConfig configProcesso = getMasterConfig().getItemListConfig("doctoServicoIndenizacaoProcesso");

		List list = new ArrayList();
		if (itensProcesso.isInitialized()) {
			for (Iterator it = itensProcesso.iterator(masterId, configProcesso); it.hasNext(); ) {
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
				list.add(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico());
			} 
		}
		return list;
	}
    


    /**
     * Verificações da não conformidade na aba de documentos do rnc
     * @param idNaoConformidade
     */
    private void verifyNaoConformidade(Long idNaoConformidade) {
    	// verifica pelo menos uma negociacao para a nao conformidade
    	if (Integer.valueOf(0).equals(negociacaoService.getRowCountNegociacoesByIdNaoConformidade(idNaoConformidade))) {
    		throw new BusinessException("LMS-21019");
    	}
    	// verifica pelo menos um motivo de abertura com blPermiteIndenizacao=S
    	if (Integer.valueOf(0).equals(motivoAberturaNcService.getRowCountMotivosAberturaByIdNaoConformidadeBlPermissao(idNaoConformidade, true))) {
    		throw new BusinessException("LMS-21020");
    	}
    }
    
    /**
     * Põe notas fiscais no TypedFlatMap a retornar para a tela.  
     * @param idDoctoServico
     * @param tpDocumentoServico
     * @param tpPrejuizo
     * @param tfm
     * @param reciboIndenizacaoNfs
     */
    private void putNotasFiscaisIntoTypedFlatMap(Long idDoctoServico, String tpDocumentoServico, String tpPrejuizo, TypedFlatMap tfm, List reciboIndenizacaoNfs) {
    	// se documento de servico for conhecimento ou nota fiscal de transporte, então apresenta as notas fiscais
    	List notasFiscais = new ArrayList();
    	if (tpDocumentoServico.equals("CTR") || tpDocumentoServico.equals("NFT") || tpDocumentoServico.equals("CRT")
    			 || tpDocumentoServico.equals("CTE") || tpDocumentoServico.equals("NFS") || tpDocumentoServico.equals("NTE")
    			 || tpDocumentoServico.equals("NSE")) {
    		// listbox source de notas fiscais
    		List notasFiscaisSource = findNotasFiscaisByIdConhecimento(idDoctoServico);
    		tfm.put("notasFiscaisSource", notasFiscaisSource);
    		    		
    		// se prejuizo for total e lista de notas do doctoServicoIndenizacao estiver vazia,
    		// então preenche as duas listboxes com todas as notas fiscais
    		if ("T".equals(tpPrejuizo) && reciboIndenizacaoNfs==null) {
    			tfm.put("notasFiscais", notasFiscaisSource);
    			
    		// se prejuizo nao for total ou se houver notas na lista de doctoServicoIndenizacao 
    		} else if (reciboIndenizacaoNfs!=null) { 

    			// listbox destino de notas fiscais    		
        		for (Iterator it = reciboIndenizacaoNfs.iterator(); it.hasNext(); ) {
	    			ReciboIndenizacaoNf reciboIndenizacaoNf = (ReciboIndenizacaoNf)it.next();
	    			Map map = new HashMap();
	    			map.put("idNotaFiscalConhecimento", reciboIndenizacaoNf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento());
	    			map.put("nrNotaFiscal", reciboIndenizacaoNf.getNotaFiscalConhecimento().getNrNotaFiscal());
	    			notasFiscais.add(map);
	    		} tfm.put("notasFiscais", notasFiscais);
    		}else{
    			
    			//LMS-666 Item 1.4 – Aba Documentos RNC: Alterar os lists de notas fiscais.
        		//Popular a segunda listbox com as notas que ja estiverem em não conformidade
    			Long idNaoConformidade = tfm.getLong("naoConformidade.idNaoConformidade");
    			List notaFiscalConhecimentoNC = null;
    			if (idNaoConformidade != null) {
    				notaFiscalConhecimentoNC = naoConformidadeService.findNotaFiscalConhecimentoByIdNaoConformidade(idNaoConformidade);
    		}
    			
        		
        		if(notaFiscalConhecimentoNC!=null && notaFiscalConhecimentoNC.size() > 0){
        			
	        		for(Iterator it = notaFiscalConhecimentoNC.iterator(); it.hasNext(); ) {
	        			
	        			NotaFiscalConhecimento notaOcorrenciaNc = (NotaFiscalConhecimento)it.next();
	        			
	        			Map map = new HashMap();
	        			map.put("idNotaFiscalConhecimento", notaOcorrenciaNc.getIdNotaFiscalConhecimento());
	        			map.put("nrNotaFiscal", notaOcorrenciaNc.getNrNotaFiscal());
	        			notasFiscais.add(map);
    	} 
        		tfm.put("notasFiscais", notasFiscais);
    }
    		}
    	} 
    }
    
    /**
     * Busca os dados relacionados ao documento de servico.
     * São eles: RNC, Notas Fiscais, Ocorrencias
     * @param tfm
     * @return
     */
    private TypedFlatMap findDoctoServicoRelateds(TypedFlatMap tfm) {
    	Long idDoctoServico = tfm.getLong("idDoctoServico");
    	if (idDoctoServico==null)
    		idDoctoServico = tfm.getLong("doctoServico.idDoctoServico");

    	
    	// busca da nao conformidade e ocorrencias do docto servico
    	NaoConformidade naoConformidade = naoConformidadeService.findByIdDoctoServicoJoinOcorrencias(idDoctoServico);
    	if (naoConformidade!=null) {
    		Long idNaoConformidade = naoConformidade.getIdNaoConformidade();
    		verifyNaoConformidade(idNaoConformidade);
	    	tfm.put("naoConformidade.filial.sgFilial", naoConformidade.getFilial().getSgFilial());
	    	tfm.put("naoConformidade.idNaoConformidade", idNaoConformidade);
	    	tfm.put("naoConformidade.nrRnc", new DecimalFormat("00000000").format(naoConformidade.getNrNaoConformidade()));
	    	
    		List ocorrenciasIn = naoConformidade.getOcorrenciaNaoConformidades();
    		
	    	if (ocorrenciasIn!=null) {
	    		List ocorrenciasOut = new ArrayList();
	    		Integer totalOcorrencias = Integer.valueOf(0);
	    		for(Iterator it = ocorrenciasIn.iterator(); it.hasNext(); ) {
	    			OcorrenciaNaoConformidade ocorrenciaNaoConformidade = (OcorrenciaNaoConformidade)it.next();
	    			if (ocorrenciaNaoConformidade.getMotivoAberturaNc() != null && ocorrenciaNaoConformidade.getMotivoAberturaNc().getBlPermiteIndenizacao()) {
	    			Map map = new HashMap();
	    			map.put("idOcorrencia", ocorrenciaNaoConformidade.getIdOcorrenciaNaoConformidade());
	    	    	map.put("dsMotivo", ocorrenciaNaoConformidade.getNrOcorrenciaNc()+" - "+ocorrenciaNaoConformidade.getMotivoAberturaNc().getDsMotivoAbertura());
	    	    	ocorrenciasOut.add(map);
	    	    	
	    	    	Long idOcorrencia = tfm.getLong("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade");
	    	    	if (idOcorrencia != null && idOcorrencia.equals(ocorrenciaNaoConformidade.getIdOcorrenciaNaoConformidade())) {
	    	    		totalOcorrencias = ocorrenciaNaoConformidade.getQtVolumes();	
	    		}
	    		}
	    		}
	    		tfm.put("motivosOcorrencia", ocorrenciasOut);
	    		tfm.put("qtVolumesIndenizados", totalOcorrencias);
	    	}
	    	
	    	//LMS-666 Criacao da combo de rotas de sinistro
	    	DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
	    	// monta lista de combo de rotas
	    	List rotas = new ArrayList();
	    	
	    	if(doctoServico.getFluxoFilial()!=null){
	    		List ordensFluxoFilial = ordemFilialFluxoService.findByIdFluxoFilial(doctoServico.getFluxoFilial().getIdFluxoFilial());
		    	
		    	if(ordensFluxoFilial!=null){
					for(int i=0; i<ordensFluxoFilial.size()-1; i++) {
						Map map = new HashMap();
						OrdemFilialFluxo ordemFilialOrigem = (OrdemFilialFluxo)ordensFluxoFilial.get(i);
						OrdemFilialFluxo ordemFilialDestino = (OrdemFilialFluxo)ordensFluxoFilial.get(i+1);
						String idFilialOrigemDestino = ordemFilialOrigem.getFilial().getIdFilial()+"|"+ordemFilialDestino.getFilial().getIdFilial();
						String labelFilialOrigemDestino = ordemFilialOrigem.getNrOrdem() + " - "+ ordemFilialOrigem.getFilial().getSgFilial()+" --> "+
						ordemFilialDestino.getNrOrdem() + " - "+ ordemFilialDestino.getFilial().getSgFilial();
						//label ORDEM - FILIAL --> ORDEM - FILIAL
						map.put("idFilialOrigemDestino", idFilialOrigemDestino);
						map.put("labelFilialOrigemDestino", labelFilialOrigemDestino);
						rotas.add(map);
					}
	    	}	    	
	    	}
	    	
	    	tfm.put("rotas", rotas);
    	} else {
    		// o documento de servico deve ser associado a um rnc
    		throw new BusinessException("LMS-21018");
    	}    	
    	return tfm;
    }
    
    public TypedFlatMap findMoedaPadrao(TypedFlatMap tfm) {
    	TypedFlatMap result = new TypedFlatMap();
    	
    	Moeda moeda = SessionUtils.getMoedaSessao();
    	result.put("idMoeda", moeda.getIdMoeda());
    	result.put("sgSimboloMoeda", moeda.getSiglaSimbolo());
    	result.put("sgFilial", SessionUtils.getFilialSessao().getSgFilial());
    	return result;
    }
   
    /***************************************************************************************
     * Documento Servico Methods
     ***************************************************************************************/
    
    /**
     * FindLookup para filial do tipo de DoctoServico Escolhido.
     */ 
    public List findLookupServiceDocumentFilialCTR(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCRT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialRRE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFS(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialNSE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    /**
     * FindLookup para a tag DoctoServico.
     */  
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNSE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNFS(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria); 
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberCRT(Map criteria) {    	
    	List retorno = ctoInternacionalService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }    
    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	List retorno = mdaService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	List retorno = reciboReembolsoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	List retorno = conhecimentoService.findLookup(criteria);
    	return findLookupDoctoServico(retorno);
    }

    public List findLookupDoctoServico(List list) {
    	
    	if (list!=null && list.size()==1) {
    		DoctoServico doctoServico = (DoctoServico)list.remove(0);
    		Filial filial = filialService.findById(doctoServico.getFilialByIdFilialOrigem().getIdFilial());
    		TypedFlatMap result = new TypedFlatMap();
    		result.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
    		result.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
    		result.put("doctoServico.filialByIdFilialOrigem.idFilial", filial.getIdFilial());    		
    		result.put("doctoServico.filialByIdFilialOrigem.sgFilial", filial.getSgFilial());
    		result.put("qtVolumesIndenizados", doctoServico.getQtVolumes());
    		findDoctoServicoRelateds(result);
    		putNotasFiscaisIntoTypedFlatMap(doctoServico.getIdDoctoServico(), doctoServico.getTpDocumentoServico().getValue(), "P", result, null);
    		list.add(result);
    	}    	
    	return list;    	
    }
    
    
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	List list = this.filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
    
    public List findTipoDocumentoServico(Map criteria) {
    	List dominiosValidos = new ArrayList();
    	dominiosValidos.add("CTR");
    	dominiosValidos.add("CRT");
    	dominiosValidos.add("NFT");
    	dominiosValidos.add("NFS");
    	dominiosValidos.add("NTE");
    	dominiosValidos.add("NSE");
    	dominiosValidos.add("CTE");
    	List retorno = this.domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
    	return retorno;
    }

    /*******************************************************************************
     * MDA Methods
     *******************************************************************************/
    public Serializable saveMDA(TypedFlatMap tfm) {
    	return saveItemInstance(tfm, "mda");
    }
    
    public Serializable saveAnexoRim(TypedFlatMap tfm) {
    	return saveItemInstance(tfm, "anexo");
    }
    
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeMdaByIds(List ids) {
    	super.removeItemByIds(ids, "mda");
    }
    
    public ResultSetPage findPaginatedMda(TypedFlatMap tfm) {
    	ResultSetPage rsp = super.findPaginatedItemList(tfm, "mda");
    	List list = new ArrayList();
    	for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		MdaSalvadoIndenizacao mdaSalvadoIndenizacao = (MdaSalvadoIndenizacao)it.next();
    		Map map = new HashMap(4);
    		map.put("idMdaSalvadoIndenizacao", mdaSalvadoIndenizacao.getIdMdaSalvadoIndenizacao());
    		map.put("sgFilial", mdaSalvadoIndenizacao.getMda().getFilialByIdFilialOrigem().getSgFilial());
    		map.put("nrMda", mdaSalvadoIndenizacao.getMda().getNrDoctoServico());
    		map.put("dhInclusao", mdaSalvadoIndenizacao.getMda().getDhInclusao());
    		list.add(map);
    	}
    	rsp.setList(list);
    	return rsp;
    }
    
    public Integer getRowCountMda(TypedFlatMap tfm) {
    	return super.getRowCountItemList(tfm, "mda");
    }
    
    public TypedFlatMap findMdaById(MasterDetailKey key) {
    	MdaSalvadoIndenizacao mdaSalvadoIndenizacao = (MdaSalvadoIndenizacao) super.findItemById(key, "mda"); 
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idMdaSalvadoIndenizacao", mdaSalvadoIndenizacao.getIdMdaSalvadoIndenizacao());
    	result.put("filialByIdFilialOrigem.idFilial", mdaSalvadoIndenizacao.getMda().getFilialByIdFilialOrigem().getIdFilial());
    	result.put("filialByIdFilialOrigem.sgFilial", mdaSalvadoIndenizacao.getMda().getFilialByIdFilialOrigem().getSgFilial());
    	result.put("mda.idDoctoServico", mdaSalvadoIndenizacao.getMda().getIdDoctoServico());
    	result.put("mda.nrDoctoServico", mdaSalvadoIndenizacao.getMda().getNrDoctoServico());
    	result.put("dhInclusao", mdaSalvadoIndenizacao.getMda().getDhInclusao());
    	return result;
    }
    

    /*******************************************************************************
     * Parcelas Methods
     *******************************************************************************/
    public Serializable saveParcelas(TypedFlatMap tfm) {
    	Long idParcelaReciboIndenizacao = (Long) super.saveItemInstance(tfm, "parcela");
    	
    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);		
		ItemList itemList = getItemsFromSession(master, "parcela");		    	
    	
    	TypedFlatMap result = new TypedFlatMap();
   		result.put("idParcelaReciboIndenizacao", idParcelaReciboIndenizacao);
    	result.put("qtParcelasBoletoBancario", Integer.valueOf(itemList.size()));
    	result.put("nrBoleto", tfm.getString("nrBoleto"));
    	result.put("dtVencimento", tfm.getYearMonthDay("dtVencimento"));
    	return result;
    }
    
    public TypedFlatMap removeParcelasByIds(TypedFlatMap tfm) {

    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);		
		ItemList itemList = getItemsFromSession(master, "parcela");
		
		List ids = new ArrayList();
		for (Iterator it = tfm.getList("ids").iterator(); it.hasNext(); ) {
			ids.add(Long.valueOf((String)it.next()));
		}
		
		super.removeItemByIds(ids, "parcela");
		
		TypedFlatMap result = new TypedFlatMap();
    	result.put("rowCount", Integer.valueOf(itemList.size()));    	
    	return result;
    }
    
    
    public ResultSetPage findPaginatedParcelas(TypedFlatMap tfm) {
    	ResultSetPage rsp = super.findPaginatedItemList(tfm, "parcela");
    	List list = new ArrayList();
    	for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao)it.next();
    		Map map = new HashMap(4);
    		map.put("idParcelaReciboIndenizacao", parcelaReciboIndenizacao.getIdParcelaReciboIndenizacao());
    		map.put("nrBoleto", parcelaReciboIndenizacao.getNrBoleto());
    		map.put("dtVencimento", parcelaReciboIndenizacao.getDtVencimento());
    		map.put("sgSimboloParcela", parcelaReciboIndenizacao.getMoeda().getSiglaSimbolo());
    		map.put("vlPagamento", parcelaReciboIndenizacao.getVlPagamento()); 
    		list.add(map);
    	}
    	rsp.setList(list);
    	return rsp;
    }
    
    public Integer getRowCountParcelas(TypedFlatMap tfm) {
    	return super.getRowCountItemList(tfm, "parcela");
    }
    
    public TypedFlatMap findParcelasById(MasterDetailKey key) {
    	ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao) super.findItemById(key, "parcela");
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idParcelaReciboIndenizacao", parcelaReciboIndenizacao.getIdParcelaReciboIndenizacao());
    	result.put("nrBoleto", parcelaReciboIndenizacao.getNrBoleto());
    	result.put("dtVencimento", parcelaReciboIndenizacao.getDtVencimento());
    	result.put("moeda.idMoeda", parcelaReciboIndenizacao.getMoeda().getIdMoeda());
    	result.put("vlPagamento", parcelaReciboIndenizacao.getVlPagamento());
    	return result;
    }
    
    public TypedFlatMap atualizaValoresParcelas(Long idReciboIndenizacao) {
    	List list = parcelaReciboIndenizacaoService.findItensByIdReciboIndenizacao(idReciboIndenizacao);    	
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("qtParcelas", Integer.valueOf(list.size()));
    	if (list.size() == 1) {
    		result.put("nrBoleto", ((ParcelaReciboIndenizacao)list.get(0)).getNrBoleto());
    		result.put("dtVencimento", ((ParcelaReciboIndenizacao)list.get(0)).getDtVencimento());
    	}
    	return result;
    }
    
    public TypedFlatMap findSomatorioParcialIndenizacao(TypedFlatMap tfm) {
    	
    	Long idReciboIndenizacao = tfm.getLong("idReciboIndenizacao");
    	String tpIndenizacao = tfm.getString("tpIndenizacao");
    	Long idMoeda = tfm.getLong("idMoeda");
    	
    	if (idMoeda==null) {
    		throw new BusinessException("LMS-21048");
    	}
    	    	
		MasterEntry master = getMasterFromSession(idReciboIndenizacao, true);		
		ItemList itemList = null;
		ItemListConfig config = null;
		
		if ("PS".equals(tpIndenizacao)) {
	        itemList = getItemsFromSession(master, "doctoServicoIndenizacaoProcesso");
	        config = getMasterConfig().getItemListConfig("doctoServicoIndenizacaoProcesso");
	        
		} else {
	        itemList = getItemsFromSession(master, "doctoServicoIndenizacaoRNC");
	        config = getMasterConfig().getItemListConfig("doctoServicoIndenizacaoRNC");
		}

		if (!itemList.hasItems()) {
			throw new BusinessException("LMS-21047");
		}
    	
    	BigDecimal vlParcialIndenizado = new BigDecimal(0);
		for(Iterator iter = itemList.iterator(idReciboIndenizacao, config); iter.hasNext();) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) iter.next();
			vlParcialIndenizado = vlParcialIndenizado.add(conversaoMoedaService.converteValor(doctoServicoIndenizacao.getMoeda().getIdMoeda(), idMoeda, doctoServicoIndenizacao.getVlIndenizado(), JTDateTimeUtils.getDataAtual()));
		}
    	
		Moeda moeda = moedaService.findById(idMoeda);		
		String msg = recursoMensagemService.findByChave("valorParcialIndenizado", new Object[]{vlParcialIndenizado});
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("msg", msg);
		result.put("sgSimbolo", moeda.getSiglaSimbolo());
		result.put("vlParcialIndenizado", vlParcialIndenizado);		
    	return result;
    }
    
    private ReciboIndenizacao getReciboIndenizacaoFromSession(Long idReciboIndenizacao) {
		MasterEntry master = getMasterFromSession(idReciboIndenizacao, true);
		return (ReciboIndenizacao) master.getMaster(); 
    }
    
    /**
	 * Faz a validacao do PCE.
	 *  
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validatePCE(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		
		return this.getVersaoDescritivoPceService()
			.validatePCE(idCliente,
				Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_INDENIZACAO), 
    			Long.valueOf(EventoPce.ID_EVENTO_PCE_EMITIR_CARTA_OCORRENCIA),
    			Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_CARGA_REAGENDADA_REG_AGENDAMENTO));
	}

	
	/**
	 * 
	 * @param parameters
	 */
	public void validateDtVencimento(TypedFlatMap parameters) {
		YearMonthDay dtVencimento = parameters.getYearMonthDay("dtVencimento");
    	if (dtVencimento != null && JTDateTimeUtils.comparaData(dtVencimento, JTDateTimeUtils.getDataAtual()) < 0) {
    		throw new BusinessException("LMS-00009");
    	}
	}
	
	/**
     * Método responsável por localizar o registro da tabela ANEXO_RIM pelo campo ID_ANEXO_RIM
     */
	public TypedFlatMap findByIdAnexoRim(MasterDetailKey key) {
		TypedFlatMap result = new TypedFlatMap();
		AnexoRim anexoRim = (AnexoRim) super.findItemById(key, "anexo");
		
    	result.put("idAnexoRim", anexoRim.getIdAnexoRim());
		result.put("nomeArquivo", ArquivoUtils.findNomeArquivoSubString(anexoRim.getDcArquivo()));
    	result.put("nomeUsuario", anexoRim.getUsuarioLMS().getUsuarioADSM().getNmUsuario());
    	result.put("dtHoraCriacao", anexoRim.getDhCriacao());
    	result.put("descAnexo", anexoRim.getDescAnexo());
    	result.put("dcArquivo", Base64Util.encode(anexoRim.getDcArquivo()));
    	result.put("idReciboIndenizacao", anexoRim.getReciboIndenizacao().getIdReciboIndenizacao());
    	
    	return result;
	}
		
	/**
	 * Método responsável por buscar os registros da tabela ANEXO_RIM para exibir na grid da aba Anexos. 
	 */
	public ResultSetPage findPaginatedAnexoRim(TypedFlatMap tfm) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetPage rsp = super.findPaginatedItemList(tfm, "anexo");
		
		for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		AnexoRim anexoRim = (AnexoRim) it.next();
    		TypedFlatMap map = new TypedFlatMap();
    		
    		map.put("nomeArquivo", ArquivoUtils.findNomeArquivoSubString(anexoRim.getDcArquivo()));
    		map.put("descricaoAnexo", anexoRim.getDescAnexo());
    		map.put("dtHoraCriacao", anexoRim.getDhCriacao());
    		map.put("nomeUsuario", anexoRim.getUsuarioLMS().getUsuarioADSM().getNmUsuario());
    		map.put("idAnexoRim", anexoRim.getIdAnexoRim());
    		
    		list.add(map);
    	}
    	
    	rsp.setList(list);
    	
    	return rsp;
	}
	
	/**
	 * Método responsável por salvar o registro na tabela ANEXO_RIM
	 */
	public Serializable storeAnexoRim(TypedFlatMap tfm) {
		Long idReciboIndenizacao = Long.parseLong((String) tfm.get("masterId"));
    	ReciboIndenizacao reciboIndenizacao = reciboIndenizacaoService.findReciboIndenizacaoById(idReciboIndenizacao);
    	
    	AnexoRim anexoRim = new AnexoRim();
    	anexoRim.setReciboIndenizacao(reciboIndenizacao);
    	anexoRim.setUsuarioLMS(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
    	
    	anexoRim.setDescAnexo((String) tfm.get("descAnexo"));
    	anexoRim.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	
    	try {
			anexoRim.setDcArquivo(Base64Util.decode((String) tfm.getString("dcArquivo")));
		} catch (IOException e) {
			throw new InfrastructureException(e.getMessage());
		}
		
		Long retorno = (Long) anexoRimService.store(anexoRim);
	
    	return retorno;    	
	}
	
	/**
     * LMS-4886
     * 
     * Busca as formas de pagamento conforme definido na ET 21.01.01.01 Incluir Recibo de Indenização (RIM) item 1.2
     * 
     * @param criteria
     * @return List
     */
    public List findTipoFormaPagamento(Map criteria) {
    	
    	String valorParametro = (String)configuracoesFacade.getValorParametro("TP_FORMA_PAGTO_INDENIZ_INC");
    	
    	List<String> dominiosValidos = Arrays.asList(valorParametro.split(";"));
        
        List retorno = this.domainValueService.findByDomainNameAndValues("DM_FORMA_PAGAMENTO_INDENIZACAO", dominiosValidos);
        
        return retorno;
    }
	
	/**
	 * Método responsável por buscar a quantidade de registros da tabela ANEXO_RIM para exibição na grid.
	 */
	public Integer getRowCountAnexoRim(TypedFlatMap tfm) {
		return getRowCountItemList(tfm, "anexo");
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeAnexoRimByIds(List<Long> ids){
		super.removeItemByIds(ids, "anexo");		
	}
	
	public void clearSessionItens(){
		super.removeMasterFromSession();
	}
	
	public void setOrdemFilialFluxoService(
			OrdemFilialFluxoService ordemFilialFluxoService) {
		this.ordemFilialFluxoService = ordemFilialFluxoService;
	}

	  public void setMaster(ReciboIndenizacaoService reciboIndenizacaoService) {
	    	super.setMasterService(reciboIndenizacaoService);
	    }
	    public ReciboIndenizacaoService getMaster() {
	    	return (ReciboIndenizacaoService)super.getMasterService();
	    }
		public void setBancoService(BancoService bancoService) {
			this.bancoService = bancoService;
		}
		public void setAgenciaBancariaService(AgenciaBancariaService agenciaBancariaService) {
			this.agenciaBancariaService = agenciaBancariaService;
		}
		public void setPessoaService(PessoaService pessoaService) {
			this.pessoaService = pessoaService;
		}
		public void setMoedaService(MoedaService moedaService) {
			this.moedaService = moedaService;
		}
		public void setProcessoSinistroService(ProcessoSinistroService processoSinistroService) {
			this.processoSinistroService = processoSinistroService;
		}
		public void setMdaService(MdaService mdaService) {
			this.mdaService = mdaService;
		}
		public void setConhecimentoService(ConhecimentoService conhecimentoService) {
			this.conhecimentoService = conhecimentoService;
		}
		public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
			this.ctoInternacionalService = ctoInternacionalService;
		}
		public void setDomainValueService(DomainValueService domainValueService) {
			this.domainValueService = domainValueService;
		}
		public void setFilialService(FilialService filialService) {
			this.filialService = filialService;
		}
		public ContaBancariaService getContaBancariaService() {
			return contaBancariaService;
		}
		public void setContaBancariaService(ContaBancariaService contaBancariaService) {
			this.contaBancariaService = contaBancariaService;
		}
		public void setDoctoServicoIndenizacaoService(DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
			this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
		}
		public void setFilialDebitadaService(FilialDebitadaService filialDebitadaService) {
			this.filialDebitadaService = filialDebitadaService;
		}
		public void setSinistroDoctoServicoService(SinistroDoctoServicoService sinistroDoctoServicoService) {
			this.sinistroDoctoServicoService = sinistroDoctoServicoService;
		}
		public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
			this.doctoServicoService = doctoServicoService;
		}
		public void setProdutoService(ProdutoService produtoService) {
			this.produtoService = produtoService;
		}
		public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
			this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
		}
		public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
			this.conversaoMoedaService = conversaoMoedaService;
		}
		public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
			this.naoConformidadeService = naoConformidadeService;
		}
		public void setClienteService(ClienteService clienteService) {
			this.clienteService = clienteService;
		}
		public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
			this.recursoMensagemService = recursoMensagemService;
		}
		public void setNegociacaoService(NegociacaoService negociacaoService) {
			this.negociacaoService = negociacaoService;
		}
		public void setMotivoAberturaNcService(MotivoAberturaNcService motivoAberturaNcService) {
			this.motivoAberturaNcService = motivoAberturaNcService;
		}
		public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
			this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
		}
		public void setParcelaReciboIndenizacaoService(ParcelaReciboIndenizacaoService parcelaReciboIndenizacaoService) {
			this.parcelaReciboIndenizacaoService = parcelaReciboIndenizacaoService;
		}
		public void setEmitirReciboRIMService(EmitirReciboRIMService emitirReciboRIMService) {
			this.emitirReciboRIMService = emitirReciboRIMService;
		}
		public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
			this.devedorDocServService = devedorDocServService;
		}
		public VersaoDescritivoPceService getVersaoDescritivoPceService() {
			return versaoDescritivoPceService;
		}
		public void setVersaoDescritivoPceService(
				VersaoDescritivoPceService versaoDescritivoPceService) {
			this.versaoDescritivoPceService = versaoDescritivoPceService;
		}
		public void setHistoricoFilialService(
				HistoricoFilialService historicoFilialService) {
			this.historicoFilialService = historicoFilialService;
		}
		
		public CotacaoIndicadorFinanceiroService getCotacaoIndicadorFinanceiroService() {
			return cotacaoIndicadorFinanceiroService;
		}
		public void setCotacaoIndicadorFinanceiroService(
				CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService) {
			this.cotacaoIndicadorFinanceiroService = cotacaoIndicadorFinanceiroService;
		}
		public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
			this.configuracoesFacade = configuracoesFacade;
		}



		public void setAnexoRimService(AnexoRimService anexoRimService) {
			this.anexoRimService = anexoRimService;
		}



		public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
			this.usuarioLMSService = usuarioLMSService;
		}



		public void setReciboIndenizacaoService(
				ReciboIndenizacaoService reciboIndenizacaoService) {
			this.reciboIndenizacaoService = reciboIndenizacaoService;
		}
}
