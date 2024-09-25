package com.mercurio.lms.expedicao.swt.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.OrdemServico;
import com.mercurio.lms.expedicao.model.OrdemServicoAnexo;
import com.mercurio.lms.expedicao.model.OrdemServicoDocumento;
import com.mercurio.lms.expedicao.model.OrdemServicoItem;
import com.mercurio.lms.expedicao.model.ServicoAdicionalPrecificado;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.OrdemServicoAnexoService;
import com.mercurio.lms.expedicao.model.service.OrdemServicoItemService;
import com.mercurio.lms.expedicao.model.service.OrdemServicoService;
import com.mercurio.lms.expedicao.model.service.TabelaServicoAdicionalService;
import com.mercurio.lms.expedicao.report.ImprimirOrdemServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.ArquivoUtils;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManterOrdemServicoAction extends MasterDetailAction {
	private Logger log = LogManager.getLogger(this.getClass());
	private ClienteService clienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private ConhecimentoService conhecimentoService;
	private DevedorDocServService devedorDocServService;
	private DivisaoClienteService divisaoClienteService;
	private FilialService filialService;
	private InscricaoEstadualService inscricaoEstadualService;
	private ManifestoService manifestoService;
	private MunicipioService municipioService;
	private OrdemServicoAnexoService ordemServicoAnexoService;
	private OrdemServicoItemService ordemServicoItemService;
	private ParametroGeralService parametroGeralService;
	private ParcelaPrecoService parcelaPrecoService; 
	private PpeService ppeService;	
	private UsuarioService usuarioService;
	private TabelaServicoAdicionalService tabelaServicoAdicionalService;
	private DoctoServicoService doctoServicoService;
	private ReportExecutionManager reportExecutionManager;
	private ImprimirOrdemServicoService imprimirOrdemServicoService;
	
	@Override
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		//Declaracao da classe pai
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(OrdemServico.class, true);
	
		/* Ordenação dos registros da grid da aba "Anexos". */
		BeanComparator anexoComparator = new BeanComparator("dhInclusao");
				
		ItemListConfig itemListConfigAnexos = new ItemListConfig() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override
			public List initialize(Long masterId, Map parameters) {
				return ordemServicoAnexoService.findByOrdemServico(masterId);
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			public Integer getRowCount(Long masterId, Map parameters) {
				return ordemServicoAnexoService.findByOrdemServico(masterId).size();
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap map = new TypedFlatMap(parameters);
				OrdemServicoAnexo ordemServicoAnexo = (OrdemServicoAnexo) bean;
				
				ordemServicoAnexo.setIdOrdemServicoAnexo(map.getLong("idOrdemServicoAnexo"));
	
				ordemServicoAnexo.setDsAnexo(map.getString("dsAnexo"));
				
				if (map.getLong("idParcelaPreco") != null && map.getLong("idParcelaPreco").intValue() > 0) {
					ParcelaPreco parcelaPreco = parcelaPrecoService.findById(map.getLong("idParcelaPreco"));
					ordemServicoAnexo.setParcelaPreco(parcelaPreco);
				}
				
				if(map.getLong("idOrdemServicoAnexo") == null || map.getLong("idOrdemServicoAnexo").intValue() < 0){
					ordemServicoAnexo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				} else {
					ordemServicoAnexo.setDhInclusao(map.getDateTime("dhInclusao"));
				}
				
				try {
					ordemServicoAnexo.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
				} catch (IOException e) {
					log.error(e);
					throw new InfrastructureException(e.getMessage());
				}
				
				return ordemServicoAnexo;
			}
		};

		Comparator<OrdemServicoItem> itemComparator = new Comparator<OrdemServicoItem>() {
			@Override
			public int compare(OrdemServicoItem o1, OrdemServicoItem o2) {
				String str1 = o1.getParcelaPreco().getDsParcelaPreco().getValue(getUserLocale());
				String str2 = o2.getParcelaPreco().getDsParcelaPreco().getValue(getUserLocale());
				return str1.compareTo(str2);
			}
		};
		
		ItemListConfig itemListConfigItens = new ItemListConfig() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override
			public List initialize(Long masterId, Map parameters) {
				return ordemServicoItemService.findByOrdemServico(masterId);
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			public Integer getRowCount(Long masterId, Map parameters) {
				return ordemServicoItemService.findByOrdemServico(masterId).size();
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			public Object populateNewItemInstance(Map parameters, Object bean) {
				OrdemServicoItem item = (OrdemServicoItem) bean;
				TypedFlatMap map = new TypedFlatMap(parameters);
				ParcelaPreco parcelaPreco = parcelaPrecoService.findById(map.getLong("idParcelaPreco"));															
				
				item.setIdOrdemServicoItem(map.getLong("idOrdemServicoItem"));
				item.setParcelaPreco(parcelaPreco);
				item.setDtExecucao(map.getYearMonthDay("dtExecucao"));
				item.setVlTabela(map.getBigDecimal("vlTabela"));
				item.setVlNegociado(map.getBigDecimal("vlNegociado"));
				item.setVlCusto(map.getBigDecimal("vlCusto"));
				item.setNrKmRodado(map.getInteger("nrKmRodado"));								
				item.setQtVolume(map.getInteger("qtVolume"));
				item.setQtHomem(map.getInteger("qtHomem"));
				item.setQtPalete(map.getInteger("qtPalete"));
				item.setBlRetornaPalete(map.getBoolean("blRetornaPalete"));
				item.setDhPeriodoInicial(map.getDateTime("dhPeriodoInicial"));
				item.setDhPeriodoFinal(map.getDateTime("dhPeriodoFinal"));
				item.setDsServico(map.getString("dsServico"));
				item.setTpEscolta(map.getDomainValue("tpEscolta"));
				item.setTpModeloPalete(map.getDomainValue("tpModeloPalete"));
				
				if(map.getBoolean("blFaturado") == null) {
					item.setBlFaturado(Boolean.FALSE);
				} else {
					item.setBlFaturado(map.getBoolean("blFaturado"));
				}
				
				if(map.getBoolean("blSemCobranca") == null) {
					item.setBlSemCobranca(Boolean.FALSE);
				} else {
					item.setBlSemCobranca(map.getBoolean("blSemCobranca"));
				}
								
				return item;
			}
			
			@Override
			public void modifyItemValues(Object newBean, Object bean) {
				OrdemServicoItem newItem = (OrdemServicoItem)newBean;
				OrdemServicoItem oldItem = (OrdemServicoItem)bean;
				oldItem.copyValuesFrom(newItem);
			}
		};
		
		config.addItemConfig("listaAnexos", OrdemServicoAnexo.class, itemListConfigAnexos, anexoComparator);
		config.addItemConfig("listaItens", OrdemServicoItem.class, itemListConfigItens, itemComparator);
		return config;
	}
	
	@SuppressWarnings("unchecked")
	public TypedFlatMap store(TypedFlatMap typedFlatMap) {
		MasterEntry entry = getMasterFromSession(typedFlatMap.getLong("idOrdemServico"), false);
   		
		OrdemServico ordemServico = (OrdemServico) entry.getMaster();		   	
   		populateStoreBean(typedFlatMap, ordemServico);
		   		
		ItemList listaAnexos = entry.getItems("listaAnexos");
		ItemList listaItens = entry.getItems("listaItens");

		if(!listaAnexos.isInitialized()) {
			listaAnexos.initialize(ordemServicoAnexoService.findByOrdemServico(ordemServico.getIdOrdemServico()));
		}
		if(!listaItens.isInitialized()) {
			listaItens.initialize(ordemServicoItemService.findByOrdemServico(ordemServico.getIdOrdemServico()));
		}
				
		validateAnexos(listaItens.getItems(), listaAnexos.getItems());
		validateDoctos(listaItens.getItems(), ordemServico);
				
		getService().store(
				ordemServico,				
				listaAnexos.getRemovedItems(),
				listaAnexos.getItems(),				
				listaItens.getRemovedItems(),
				listaItens.getItems());
		
		listaAnexos.resetItemsState();
		listaItens.resetItemsState();
		updateMasterInSession(entry);
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idOrdemServico", ordemServico.getIdOrdemServico());
		retorno.put("dtSolicitacao", ordemServico.getDtSolicitacao());
		retorno.put("tpSituacao", ordemServico.getTpSituacao().getValue());
		retorno.put("nrOrdemServico", ordemServico.getNrOrdemServico());
		retorno.put("listaIdsDoctoServico", generateMapFromOSDoctos(ordemServico.getOrdemServicoDocumentos()));		
		return retorno;		
	}
	
	private void validateAnexos(List<OrdemServicoItem> itensToStore, List<OrdemServicoAnexo> anexosToStore) {
		for (OrdemServicoItem ordemServicoItem : itensToStore) {
			Integer totalAnexo = 0;
			for (OrdemServicoAnexo ordemServicoAnexo : anexosToStore) {				
				if (ordemServicoAnexo.getParcelaPreco() != null && 
						ordemServicoItem.getParcelaPreco().getCdParcelaPreco().equals(ordemServicoAnexo.getParcelaPreco().getCdParcelaPreco())) {
					totalAnexo++;
				}
			}
			Integer totalNecessario = 1;
			if (ConstantesExpedicao.CD_CAPATAZIA.equals(ordemServicoItem.getParcelaPreco().getCdParcelaPreco())) {
				totalNecessario = 2;
			}
			if (totalAnexo.compareTo(totalNecessario) < 0) {
				throw new BusinessException("LMS-04467");
			}
		}
	}
	
	private void validateDoctos(List<OrdemServicoItem> itens, OrdemServico ordemServico) {
		for(OrdemServicoItem item : itens) {
			if((!ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS_NO_CLIENTE.equals(item.getParcelaPreco().getCdParcelaPreco()) &&
					!ConstantesExpedicao.CD_GESTAO_OPERACOES_LOGISTICAS.equals(item.getParcelaPreco().getCdParcelaPreco())) ||
					ordemServico.getTpDocumento() != null) {
				validateDocumentoRequired(ordemServico);
			}		
		}
	}
	
	private void validateDocumentoRequired(OrdemServico ordemServico) {
		Boolean isValid = Boolean.TRUE;
		
		if(ordemServico.getTpDocumento() == null) {
			isValid = false;
		} else {
			String tpDocumento = ordemServico.getTpDocumento().getValue();
			if(ConstantesExpedicao.TP_DOCUMENTO_DOCTO_SERVICO.equals(tpDocumento) && ( 
					ordemServico.getOrdemServicoDocumentos() == null ||
					ordemServico.getOrdemServicoDocumentos().size() == 0)) {
				isValid = false;
			} else if(ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_COLETA.equals(tpDocumento) && 
					ordemServico.getManifestoColeta() == null) {
				isValid = false;
			} else if((ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_ENTREGA.equals(tpDocumento) ||
					ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_VIAGEM.equals(tpDocumento)) &&
					ordemServico.getManifesto() == null) {
				isValid = false;
			}
		}
		
		if(!isValid) {
			throw new BusinessException("LMS-04468");
		}
	}
	
	private List<Map<String, Object>> generateMapFromOSDoctos(List<OrdemServicoDocumento> doctos) {
		List<Map<String, Object>> ordemServicoDocumentos = new ArrayList<Map<String,Object>>();
		
		if(doctos != null) {
			for (OrdemServicoDocumento ordemServicoDocumento : doctos) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("idOrdemServicoDocumento", ordemServicoDocumento.getIdOrdemServicoDocumento());
				map.put("idDoctoServico", ordemServicoDocumento.getDoctoServico().getIdDoctoServico());
				DoctoServico doctoServico = ordemServicoDocumento.getDoctoServico();
				DomainValue dv = configuracoesFacade.getDomainValue("DM_TIPO_DOCUMENTO_SERVICO", doctoServico.getTpDocumentoServico().getValue());
				map.put("tpDocumentoServico", dv.getValue());
				Filial filialOrigem = doctoServico.getFilialByIdFilialOrigem();
				map.put("sgFilial", filialOrigem.getSgFilial());
				map.put("idFilialOrigem", filialOrigem.getIdFilial());
				map.put("sgFilialOrigem", filialOrigem.getSgFilial());
				map.put("nrDoctoServico", doctoServico.getNrDoctoServico());
				map.put("dsDoctoServico", dv.getDescription()
								+ " - "	+ filialOrigem.getSgFilial() 
								+ " - "	+ doctoServico.getNrDoctoServico());
				Servico servico = doctoServico.getServico();
				map.put("idServico", servico.getIdServico());
				ordemServicoDocumentos.add(map);
			}
		}
		
		return ordemServicoDocumentos;
	}
		
	public TypedFlatMap findById(Long id) {
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		TypedFlatMap map = getService().findByIdWithPreFatura(id);
		
		//LMS-7113
		OrdemServico ordemServico = (OrdemServico) map.get("ordemServico");
		TypedFlatMap  preFaturaServico = (TypedFlatMap) map.get("preFaturaServico");
		putMasterInSession(map.get("ordemServico"));
		
		typedFlatMap.put("dtSolicitacao", ordemServico.getDtSolicitacao());		
		typedFlatMap.put("nrOrdemServico", ordemServico.getNrOrdemServico());
		typedFlatMap.put("idOrdemServico", ordemServico.getIdOrdemServico());
		
		typedFlatMap.put("idCliente", ordemServico.getClienteTomador().getIdCliente());
		Pessoa pessoa = ordemServico.getClienteTomador().getPessoa();
		typedFlatMap.put("nrIdentificacao", pessoa.getNrIdentificacao());
		typedFlatMap.put("nmPessoa", pessoa.getNmPessoa());
		
		if (ordemServico.getDivisaoCliente() != null) {
			typedFlatMap.put("idDivisaoCliente", ordemServico.getDivisaoCliente().getIdDivisaoCliente());
		}
		
		if (ordemServico.getIeTomador() != null) {
			typedFlatMap.put("idInscricaoEstadual", ordemServico.getIeTomador().getIdInscricaoEstadual());
		}
		
		Filial filialRegistro = ordemServico.getFilialRegistro();
		typedFlatMap.put("idFilialRegistro", filialRegistro.getIdFilial());
		typedFlatMap.put("sgFilialRegistro", filialRegistro.getSgFilial());
		typedFlatMap.put("nmFantasiaRegistro", filialRegistro.getPessoa().getNmFantasia());
		
		Filial filialExecucao = ordemServico.getFilialExecucao();
		typedFlatMap.put("idFilialExecucao", filialExecucao.getIdFilial());
		typedFlatMap.put("sgFilialExecucao", filialExecucao.getSgFilial());
		typedFlatMap.put("nmFantasiaExecucao", filialExecucao.getPessoa().getNmFantasia());
		
		typedFlatMap.put("nmSolicitante", ordemServico.getNmSolicitante());
		
		typedFlatMap.put("tpSituacaoOrdemServico", ordemServico.getTpSituacao().getValue());
		typedFlatMap.put("tpSolicitante", ordemServico.getTpSolicitante().getValue());
		
		Municipio municipio = municipioService.findById(ordemServico.getMunicipioExecucao().getIdMunicipio());
		typedFlatMap.put("idMunicipio", municipio.getIdMunicipio());
		typedFlatMap.put("nmMunicipio", municipio.getNmMunicipio());
		
		Usuario usuario = usuarioService.findById(ordemServico.getUsuarioRegistrante().getIdUsuario());
		if (usuario != null) {
			typedFlatMap.put("idUsuarioRegistrante", usuario.getIdUsuario());
			typedFlatMap.put("usuarioRegistrante", usuario.getNmUsuario());
		}
		
		typedFlatMap.put("dsMotivoRejeicao", ordemServico.getDsMotivoRejeicao());
				
		if(ordemServico.getTpDocumento() != null) {
			String tpDocumento = ordemServico.getTpDocumento().getValue();
			typedFlatMap.put("tpDocumento", tpDocumento);
			
			if (ConstantesExpedicao.TP_DOCUMENTO_DOCTO_SERVICO.equals(tpDocumento)) {			
				typedFlatMap.put("listaIdsDoctoServico", generateMapFromOSDoctos(ordemServico.getOrdemServicoDocumentos()));
			} else {
				Long idManifesto;
				Integer nrManifesto;
				Long idFilial;
				String sgFilial;
				if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_COLETA.equals(tpDocumento)) {
					ManifestoColeta manifestoColeta = ordemServico.getManifestoColeta();
					idManifesto = manifestoColeta.getIdManifestoColeta();
					nrManifesto = manifestoColeta.getNrManifesto();
					idFilial = manifestoColeta.getFilial().getIdFilial();
					sgFilial = manifestoColeta.getFilial().getSgFilial();
				} else {
					Manifesto manifesto = ordemServico.getManifesto();
					idManifesto = manifesto.getIdManifesto();
					if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_ENTREGA.equals(tpDocumento)) {
						nrManifesto = manifesto.getManifestoEntrega().getNrManifestoEntrega();
					} else {
						nrManifesto = manifesto.getManifestoViagemNacional().getNrManifestoOrigem();
					}
					idFilial = manifesto.getFilialByIdFilialOrigem().getIdFilial();
					sgFilial = manifesto.getFilialByIdFilialOrigem().getSgFilial();
				}
				
				typedFlatMap.put("idManifesto", idManifesto);
				typedFlatMap.put("nrManifesto", nrManifesto);
				typedFlatMap.put("idFilial", idFilial);
				typedFlatMap.put("sgFilial", sgFilial);
			}
		}
		
		//LMS-7113
		if(preFaturaServico!= null && !preFaturaServico.isEmpty()){
			typedFlatMap.put("sgFilialCobranca", preFaturaServico.get("sgFilialCobranca"));
			typedFlatMap.put("nrPreFatura", FormatUtils.completaDados(preFaturaServico.get("nrPreFatura"),"0",9,0,FormatUtils.ESQUERDA));
			typedFlatMap.put("tpSituacaoPreFatura", ((DomainValue)preFaturaServico.get("tpSituacaoPreFatura")).getValue());
		}
				
		return typedFlatMap;
	}



	@SuppressWarnings("unchecked")
	public void populateStoreBean(TypedFlatMap typedFlatMap, OrdemServico ordemServico) {
		ordemServico.setIdOrdemServico(typedFlatMap.getLong("idOrdemServico"));
		Cliente clienteTomador = new Cliente();
		clienteTomador.setIdCliente(typedFlatMap.getLong("idCliente"));
		ordemServico.setClienteTomador(clienteTomador);
		ordemServico.setDivisaoCliente(null);
		ordemServico.setIeTomador(null);
		
		if (typedFlatMap.getLong("idDivisaoCliente") != null)  {
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(typedFlatMap.getLong("idDivisaoCliente"));
			ordemServico.setDivisaoCliente(divisaoCliente);
		}
		if (typedFlatMap.getLong("idInscricaoEstadual") != null)  {
			InscricaoEstadual ieTomador = new InscricaoEstadual();
			ieTomador.setIdInscricaoEstadual(typedFlatMap.getLong("idInscricaoEstadual"));
			ordemServico.setIeTomador(ieTomador);
		}
		
		if (typedFlatMap.getYearMonthDay("dtSolicitacao") != null) {
			ordemServico.setDtSolicitacao(typedFlatMap.getYearMonthDay("dtSolicitacao"));
		} else {
			ordemServico.setDtSolicitacao(JTDateTimeUtils.getDataAtual());
		}
		
		if(ordemServico.getIdOrdemServico() != null) {
			ordemServico.setNrOrdemServico(typedFlatMap.getLong("nrOrdemServico"));
		}
		
		Filial filialExecucao = new Filial();
		filialExecucao.setIdFilial(typedFlatMap.getLong("idFilialExecucao"));
		ordemServico.setFilialExecucao(filialExecucao);
		
		Filial filialRegistro = new Filial();
		filialRegistro.setIdFilial(typedFlatMap.getLong("idFilialRegistro"));
		ordemServico.setFilialRegistro(filialRegistro);
		
		ordemServico.setTpDocumento(typedFlatMap.getDomainValue("tpDocumento"));
		
		if(ordemServico.getTpDocumento() != null) {
			if (ConstantesExpedicao.TP_DOCUMENTO_DOCTO_SERVICO.equals(ordemServico.getTpDocumento().getValue())) {
				List<TypedFlatMap> listaIdsDoctoServico = typedFlatMap.getList("listaIdsDoctoServico");
				List<OrdemServicoDocumento> ordemServicoDocumentoLista = new ArrayList<OrdemServicoDocumento>();
				if (listaIdsDoctoServico != null) {				
					for (TypedFlatMap map : listaIdsDoctoServico) {
						OrdemServicoDocumento ordemServicoDocumento = new OrdemServicoDocumento();
						if (map.get("idOrdemServicoDocumento") != null) {
							ordemServicoDocumento.setIdOrdemServicoDocumento((Long) map.get("idOrdemServicoDocumento"));
						}
						DoctoServico doctoServico = new DoctoServico();
						doctoServico.setIdDoctoServico(map.getLong("idDoctoServico"));
						doctoServico.setTpDocumentoServico(map.getDomainValue("tpDocumentoServico"));
						doctoServico.setNrDoctoServico(map.getLong("nrDoctoServico"));
						Filial filial = new Filial();
						filial.setIdFilial(map.getLong("idFilialOrigem"));
						filial.setSgFilial(map.getString("sgFilialOrigem"));
						doctoServico.setFilialByIdFilialOrigem(filial);
						Servico servico = new Servico();
						servico.setIdServico(map.getLong("idServico"));
						doctoServico.setServico(servico);
						ordemServicoDocumento.setDoctoServico(doctoServico);
						ordemServicoDocumento.setOrdemServico(ordemServico);
						ordemServicoDocumentoLista.add(ordemServicoDocumento);
					}
				}
				ordemServico.setOrdemServicoDocumentos(ordemServicoDocumentoLista);
			} else {				
				if(typedFlatMap.getLong("idManifesto") != null) {
					if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_COLETA.equals(ordemServico.getTpDocumento().getValue())) {
						ManifestoColeta manifestoColeta = new ManifestoColeta();
						manifestoColeta.setIdManifestoColeta(typedFlatMap.getLong("idManifesto"));
						ordemServico.setManifestoColeta(manifestoColeta);
					} else {
						Manifesto manifesto = manifestoService.findById(typedFlatMap.getLong("idManifesto"));
						manifesto.setIdManifesto(typedFlatMap.getLong("idManifesto"));
						ordemServico.setManifesto(manifesto);
					}
				} else {
					ordemServico.setManifesto(null);
					ordemServico.setManifestoColeta(null);
				}
				
				ordemServico.setOrdemServicoDocumentos(new ArrayList<OrdemServicoDocumento>());
			}
		}
		
		Municipio municipioExecucao = new Municipio();
		municipioExecucao.setIdMunicipio(typedFlatMap.getLong("idMunicipio"));
		ordemServico.setMunicipioExecucao(municipioExecucao);
		ordemServico.setNmSolicitante(typedFlatMap.getString("nmSolicitante"));					
		ordemServico.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DIGITADA));
		ordemServico.setTpSolicitante(typedFlatMap.getDomainValue("tpSolicitante"));
		
		UsuarioLMS usuarioRegistrante = new UsuarioLMS();
		usuarioRegistrante.setIdUsuario(typedFlatMap.getLong("idUsuarioRegistrante"));
		ordemServico.setUsuarioRegistrante(usuarioRegistrante);
	}
	
	@SuppressWarnings("rawtypes")
	public void storeItem(TypedFlatMap parameters) {
		Long idParcelaPreco = parameters.getLong("idParcelaPreco");
		Long idOrdemServicoItem = parameters.getLong("idOrdemServicoItem");
		
		MasterEntry masterObj = getMasterFromSession(parameters.getLong("idMaster"), false);    	   
    	ItemList itens = getItemsFromSession(masterObj, "listaItens");
		ItemListConfig config = getMasterConfig().getItemListConfig("listaItens");
    	
    	for (Iterator iter = itens.iterator((Long)masterObj.getMasterId(), config); iter.hasNext();) {
    		OrdemServicoItem item = (OrdemServicoItem) iter.next();
    		if((item.getParcelaPreco().getIdParcelaPreco().equals(idParcelaPreco)) && (idOrdemServicoItem == null || item.getIdOrdemServicoItem() == null || idOrdemServicoItem.compareTo(item.getIdOrdemServicoItem()) != 0)) {
				throw new BusinessException("LMS-04466");
			}	
    	}
		saveItemInstance(parameters, "listaItens");
	}
	
	public void storeAnexo(TypedFlatMap parameters) {
		saveItemInstance(parameters, "listaAnexos");
	}
	
	public Object findByIdOrdemServicoAnexo(MasterDetailKey key) { 
		return findItemById(key, "listaAnexos");
	}
	
	public Object findByIdOrdemServicoItem(MasterDetailKey key) {		
		OrdemServicoItem ordemServicoItem = (OrdemServicoItem) findItemById(key, "listaItens");
		return converterItemParaMap(ordemServicoItem);
	}
	
	private Map<String, Object> converterItemParaMap(OrdemServicoItem ordemServicoItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idOrdemServicoItem", ordemServicoItem.getIdOrdemServicoItem());
		map.put("idParcelaPreco", ordemServicoItem.getParcelaPreco().getIdParcelaPreco());
		map.put("dtExecucao", ordemServicoItem.getDtExecucao());
		map.put("vlTabela", ordemServicoItem.getVlTabela());
		map.put("vlNegociado", ordemServicoItem.getVlNegociado());
		map.put("vlCusto", ordemServicoItem.getVlCusto());
		map.put("nrKmRodado", ordemServicoItem.getNrKmRodado());
		map.put("qtHomem", ordemServicoItem.getQtHomem());
		map.put("qtVolume", ordemServicoItem.getQtVolume());
		map.put("qtPalete", ordemServicoItem.getQtPalete());
		if (ordemServicoItem.getTpModeloPalete() != null) {
			map.put("tpModeloPalete", ordemServicoItem.getTpModeloPalete().getValue());
		}
		map.put("blRetornaPalete", ordemServicoItem.getBlRetornaPalete());
		if (ordemServicoItem.getTpEscolta() != null) {
			map.put("tpEscolta", ordemServicoItem.getTpEscolta().getValue());
		}
		map.put("dhPeriodoInicial", ordemServicoItem.getDhPeriodoInicial());
		map.put("dhPeriodoFinal", ordemServicoItem.getDhPeriodoFinal());
		map.put("dsServico", ordemServicoItem.getDsServico());
		map.put("blFaturado", ordemServicoItem.getBlFaturado());
		map.put("blSemCobranca", ordemServicoItem.getBlSemCobranca());
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedAnexos(Map parameters) {
		ResultSetPage rs = findPaginatedItemList(parameters, "listaAnexos");
		
		FilterResultSetPage filter = new FilterResultSetPage(rs) {
			@Override
			public Map filterItem(Object item) {		 	
				OrdemServicoAnexo ordemServicoAnexo = (OrdemServicoAnexo) item;
				TypedFlatMap tfm = new TypedFlatMap();
								
				tfm.put("idOrdemServicoAnexo", ordemServicoAnexo.getIdOrdemServicoAnexo());
				try {
					tfm.put("nmArquivo",ArquivoUtils.getNomeArquivo(ordemServicoAnexo.getDcArquivo()));
				} catch(Exception e) {
					/* TODO Corrigir problemas com anexos em sessão
					 *  ERRO "ORA-22275: localizador de LOB especificado é inválido" ESPERADO
					 *  POIS AINDA NÃO EXISTE O ARQUIVO / BLOB NO BANCO DE DADOS.
					 */
				}
				tfm.put("dsAnexo",ordemServicoAnexo.getDsAnexo());
				if (ordemServicoAnexo.getParcelaPreco() != null) {
					tfm.put("dsServicoAdicional", ordemServicoAnexo.getParcelaPreco().getDsParcelaPreco());
				}
				tfm.put("dhInclusao", ordemServicoAnexo.getDhInclusao());
				
				if(ordemServicoAnexo.getIdOrdemServicoAnexo() == null ||  ordemServicoAnexo.getIdOrdemServicoAnexo() < 0) {
					tfm.put("arquivo", false);
				}
				
				return tfm;
		 	};
		 };
		
		return (ResultSetPage) filter.doFilter();
	}
		
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedItens(TypedFlatMap criteria) {
		ResultSetPage rs = findPaginatedItemList(criteria, "listaItens");
		
		FilterResultSetPage filter = new FilterResultSetPage(rs) {
			@Override
			public Map filterItem(Object item) {
				OrdemServicoItem itemOS = (OrdemServicoItem) item;
				
				TypedFlatMap tfm = new TypedFlatMap();
				tfm.put("idOrdemServicoItem", itemOS.getIdOrdemServicoItem());
				tfm.put("dsServicoAdicional", itemOS.getParcelaPreco().getDsParcelaPreco().getValue(getUserLocale()));
				tfm.put("dtExecucao", itemOS.getDtExecucao());
				tfm.put("vlNegociado", itemOS.getVlNegociado());
				tfm.put("vlCusto", itemOS.getVlCusto());
				
				return tfm;
		 	};
		 };
		
		return (ResultSetPage) filter.doFilter();		    
	}
	
	public Integer getRowCountAnexos(TypedFlatMap parameters) {
		return getRowCountItemList(parameters, "listaAnexos");
	}
	
	public Integer getRowCountItens(TypedFlatMap parameters) {
		return getRowCountItemList(parameters, "listaItens");
	}
	
	@SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeAnexosByIds (List ids) {
		super.removeItemByIds(ids, "listaAnexos");
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeItensByIds (List ids) {
		MasterEntry entry = getMasterFromSession(null, false);		
		ItemList itensConfig = entry.getItems("listaItens");
		ItemList anexosConfig = entry.getItems("listaAnexos");
		List<Long> anexosToRemove = new ArrayList<Long>();
		
		if(!anexosConfig.isInitialized()) {
			anexosConfig.initialize(ordemServicoAnexoService.findByOrdemServico((Long)entry.getMasterId()));
		}
		
		for(OrdemServicoItem item : (List<OrdemServicoItem>)itensConfig.getItems()) {
			if(ids.indexOf(item.getIdOrdemServicoItem()) >= 0) {
				for(OrdemServicoAnexo anexo : (List<OrdemServicoAnexo>)anexosConfig.getItems()) {
					if(anexo.getParcelaPreco() != null && 
							anexo.getParcelaPreco().getIdParcelaPreco().equals(item.getParcelaPreco().getIdParcelaPreco())) {
						anexosToRemove.add(anexo.getIdOrdemServicoAnexo());
					}
				}
			}
		}
				
		super.removeItemByIds(anexosToRemove, "listaAnexos");
		super.removeItemByIds(ids, "listaItens");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map executeDownloadArquivo(TypedFlatMap parameters) {
		Long idOrdemServicoAnexo = parameters.getLong("idOrdemServicoAnexo");
		OrdemServicoAnexo anexo = null;

		if(idOrdemServicoAnexo != null) {
			anexo = ordemServicoAnexoService.findById(idOrdemServicoAnexo);
		}

		Map retorno = new HashMap();
		if(anexo != null) {
			retorno.put("dcArquivo", Base64Util.encode(anexo.getDcArquivo()));
		}

		return retorno;
	}

	public TypedFlatMap executeAprovar(TypedFlatMap parameters) {
		Long idOrdemServicoAnexo = parameters.getLong("masterId");
		OrdemServico ordemServico = getService().executeAprovar(idOrdemServicoAnexo);
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("tpSituacao", ordemServico.getTpSituacao().getValue());
		return retorno;
	}
	
	public TypedFlatMap executeReprovar(TypedFlatMap parameters) {
		Long idOrdemServicoAnexo = parameters.getLong("masterId");
		String motivo = parameters.getString("dsMotivoRejeicao");
		OrdemServico ordemServico = getService().executeReprovar(idOrdemServicoAnexo, motivo);
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("tpSituacao", ordemServico.getTpSituacao().getValue());
		retorno.put("dsMotivoRejeicao", ordemServico.getDsMotivoRejeicao());
		return retorno;
	}
	
	public void removeById(java.lang.Long id) {
		getService().removeById(id);
	}
		
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
		getService().removeByIds(ids);
    }
	
	public Map<String, Object> findDefaultParameters(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		Usuario usuario = SessionUtils.getUsuarioLogado();
		result.put("idUsuarioRegistrante", usuario.getIdUsuario());
		result.put("usuarioRegistrante", usuario.getNmUsuario());

		Filial filial = SessionUtils.getFilialSessao();
		result.put("idFilialRegistro", filial.getIdFilial());
		result.put("sgFilialRegistro", filial.getSgFilial());
		result.put("nmFantasiaRegistro", filial.getPessoa().getNmFantasia());

		result.put("idFilialExecucao", filial.getIdFilial());
		result.put("sgFilialExecucao", filial.getSgFilial());
		result.put("nmFantasiaExecucao", filial.getPessoa().getNmFantasia());

		Municipio municipio = filial.getPessoa().getEnderecoPessoa().getMunicipio();
		result.put("idMunicipio", municipio.getIdMunicipio());
		result.put("nmMunicipio", municipio.getNmMunicipio());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupMunicipio(TypedFlatMap criteria) {
		
		Map mapPais = new HashMap();
		mapPais.put("idPais", SessionUtils.getPaisSessao().getIdPais());
		Map mapUf = new HashMap();
		mapUf.put("pais", mapPais);
		Map mapCriteria = new HashMap();
		criteria.put("unidadeFederativa", mapUf);
		
		List<Municipio> municipios = municipioService.findLookup(criteria);
		List<Map<String, Object>> result = null;
		if (municipios != null && !municipios.isEmpty()) {
			result = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < municipios.size(); i++) {
				Municipio municipio =  municipios.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("idMunicipio", municipio.getIdMunicipio());
				map.put("nmMunicipio", municipio.getNmMunicipio());
				
				BigDecimal idServico = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("SERVICO_PADRAO", false);
				
				if (idServico != null) {
					Long idFilial = ppeService.findFilialAtendimentoMunicipio(
							municipio.getIdMunicipio(),
							Long.valueOf(idServico.toString()),
							Boolean.TRUE,
							null,
							null,
							null,
							null,
							null,
							null,
							null,
							null
					);
					
					if (idFilial != null) {					
						Filial filial = filialService.findById(idFilial);
						map.put("idFilialExecucao", filial.getIdFilial());
						map.put("sgFilialExecucao", filial.getSgFilial());
						map.put("nmFantasiaExecucao", filial.getPessoa().getNmFantasia());
					}
				}

				result.add(map);
			}
		}
		return result;
	}
	
	//LMS - 6896
	public Map<String, Object> findVerificaFilialExecucao(){
		
		Filial filialLogado = SessionUtils.getFilialSessao();
		String parametroGeral = (String) parametroGeralService.findConteudoByNomeParametro("FILIAL_EXECUCAO", false);						
		Map<String, Object> map = null;
		
		if(parametroGeral.contains(filialLogado.getIdFilial().toString())){
			map = new HashMap<String, Object>();
			map.put("isFilialExecucao", true);	
		}
		
		return map;		
	}

	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupFilial(TypedFlatMap criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public List<Map<String, Object>> findLookupUsuario(TypedFlatMap criteria) {
		List<Map<String, Object>> usuarios = usuarioService.findLookupUsuarioFuncionario(
				null, criteria.getString("login"), null, criteria.getLong("idFilial"), null, null, null, null, true);
		return usuarios;			
	}	
	
	@SuppressWarnings("unchecked" )
	public List<Map<String, Object>> findLookupCliente(Map<String, Object> criteria){
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));
		criteria.put("pessoa", pessoa);
		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		List<Cliente> clientes = clienteService.findLookup(criteria);
		for (Cliente cliente : clientes) {
			Map<String, Object> cli = new HashMap<String, Object>(); 
			cli.put("idCliente", cliente.getIdCliente());
			if (cliente.getPessoa() != null) {
				cli.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				cli.put("nmPessoa", cliente.getPessoa().getNmPessoa());
			}
			retorno.add(cli);
		}
		return retorno;
	}
		
	public List<Map<String, Object>> findComboServicosAdicionais(TypedFlatMap criteria) {
		return parcelaPrecoService.findServicosAdicionaisParcela();
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> findComboServicosAdicionaisItens(TypedFlatMap criteria) {
		MasterEntry masterObj = getMasterFromSession(criteria.getLong("idMaster"), false);    	   
    	ItemList itens = getItemsFromSession(masterObj, "listaItens");
		ItemListConfig config = getMasterConfig().getItemListConfig("listaItens");
    	
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		
    	for (Iterator iter = itens.iterator((Long)masterObj.getMasterId(), config); iter.hasNext();) {
    		OrdemServicoItem item = (OrdemServicoItem) iter.next();
    		
    		Map<String, Object> parcelaMapped = new HashMap<String, Object>();
    		parcelaMapped.put("idParcelaPreco", item.getParcelaPreco().getIdParcelaPreco());
    		parcelaMapped.put("dsServicoAdicional", item.getParcelaPreco().getDsParcelaPreco().getValue(getUserLocale()));
    		retorno.add(parcelaMapped);
    	}
		
    	if(retorno.size() > 1) {
	    	Collections.sort(retorno, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					String str1 = (String)o1.get("dsServicoAdicional");
					String str2 = (String)o2.get("dsServicoAdicional");
					return str1.compareTo(str2);
				}
			});
    	}
    	
		return retorno;
	}
	
	public void validateDoctoServico(Map<String, Object> criteria) {
		Long idDoctoServico = (Long)criteria.get("idDoctoServico");
		Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
		DoctoServico doctoServicoNovo = doctoServicoService.findById(idDoctoServico);
		List<Map<String, Object>> lista = (List)criteria.get("listDoctoServicoAssociados");
		
		DoctoServico doctoServicoAntigo;
		
		if (lista != null) {
			for (Map<String, Object> map : lista) {
				doctoServicoAntigo = doctoServicoService.findById((Long)map.get("idDoctoServico"));
				
				if (doctoServicoNovo.getDivisaoCliente() != null && 
						doctoServicoAntigo.getDivisaoCliente() != null && 
						!doctoServicoNovo.getDivisaoCliente().getIdDivisaoCliente().equals(doctoServicoAntigo.getDivisaoCliente().getIdDivisaoCliente())) {
					throw new BusinessException("LMS-04514");
				}
			}
		}
		
		if (!ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO.equals(conhecimento.getTpSituacaoConhecimento().getValue())) {
			throw new BusinessException("LMS-04463");
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultSetPage<Map<String, Object>> findPaginated(Map criteria) {
		return getService().findPaginated(new PaginatedQuery(criteria));
    }	
	
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getRowCount(Map filtros) {		
		return getService().getRowCount(new TypedFlatMap(filtros));
	}
	
	public Map<String, Object> findDadosClienteByDoctoServico(Map<String, Object> criteria) {
		Long idDoctoServico = (Long) criteria.get("idDoctoServico");
		DevedorDocServ devedorDocServ = devedorDocServService.findDevedorByDoctoServico(idDoctoServico);
		Cliente cliente =  devedorDocServ.getCliente();
		Map<String, Object> cli = new HashMap<String, Object>();
		if (cliente != null) {
			cli.put("idCliente", cliente.getIdCliente());
			if (cliente.getPessoa() != null) {
				cli.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				cli.put("nmPessoa", cliente.getPessoa().getNmPessoa());
			}
			return cli;
		}
		return null;
	}
	
	public Map<String, Object> findManifesto(Map<String, Object> map) {
		String tpDocumento = (String) map.get("tpDocumento");
		Long idFilial = (Long) map.get("idFilial");
		Integer nrManifesto = (Integer) map.get("nrManifesto");
		return manifestoService.findManifestosEntregaColetaViagem(tpDocumento, idFilial, nrManifesto);
	}
	
	public Map<String, Object> findDadosByCliente(Map<String, Object> map) {
		Long idCliente = (Long) map.get("idCliente");
		Map<String, Object> retorno = new HashMap<String, Object>();
		Cliente cliente = clienteService.findById(idCliente);
		retorno.put("divisoes", findDivisoesCliente(idCliente));
		retorno.put("ies", findInscricoesEstadoaisCliente(cliente));
		return retorno;
	}

	public List<Map<String, Object>> findInscricoesEstadoaisCliente(
			Cliente cliente) {
		List<Map<String, Object>>  iesList = new ArrayList<Map<String,Object>>();
		List<InscricaoEstadual> inscricaoEstadualList = inscricaoEstadualService.findInscricaoEstadualAtivaByPessoa(cliente.getPessoa().getIdPessoa());
		
		for (InscricaoEstadual inscricaoEstadual : inscricaoEstadualList) {
			Map<String, Object> ieMap = new HashMap<String, Object>();
			ieMap.put("idInscricaoEstadual", inscricaoEstadual.getIdInscricaoEstadual());
			ieMap.put("nrInscricaoEstadual", inscricaoEstadual.getNrInscricaoEstadual());
			iesList.add(ieMap);
		}
		return iesList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findDivisoesCliente(Long idCliente) {
		List<Map<String, Object>> divisoes = divisaoClienteService.findByIdCliente(idCliente);
		return divisoes;
	}
	
	// LMS-6708
	public Map<String, Object> findDivisaoDoctoServico(Map<String, Object> map) {
		Long idDoctoServico = (Long) map.get("idDoctoServico");
		Map<String, Object> retorno = new HashMap<String, Object>();
		DivisaoCliente divisaoCliente = doctoServicoService.findDivisaoClienteById(idDoctoServico);
		
		Map<String, Object> retornoMap = new HashMap<String, Object>();
		
		retornoMap.put("idDivisaoCliente", divisaoCliente.getIdDivisaoCliente());
		retornoMap.put("dsDivisaoCliente", divisaoCliente.getDsDivisaoCliente());
		
		retorno.put("divisaoDoctoServico", retornoMap);
		
		return retorno;
	}
	
	// LMS-8050
	public Map<String, Object> findDivisaoByDoctoServico(Map<String, Object> map) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		DoctoServico doctoServico = doctoServicoService.findById((Long)map.get("idDoctoServico"));
		DivisaoCliente divisaoCliente =  divisaoClienteService.findById(doctoServico.getDivisaoCliente().getIdDivisaoCliente());
		
		Map<String, Object> retornoMap = new HashMap<String, Object>();
		
		retornoMap.put("idDivisaoCliente", divisaoCliente.getIdDivisaoCliente());
		retornoMap.put("dsDivisaoCliente", divisaoCliente.getDsDivisaoCliente());
		
		retorno.put("divisaoDoctoServico", retornoMap);
		return retorno;
	}
	
	
	public Map<String, Object> validateValorNegocioado(TypedFlatMap criteria) {
		Map<String, Object> result = new HashMap<String, Object>();
		Boolean disableValorNegociado = Boolean.TRUE;
		
		List<ServicoAdicionalPrecificado> servicos = tabelaServicoAdicionalService.findByTabelaCliente(criteria.getLong("idServico"), criteria.getLong("idDivisaoCliente"));
		for (ServicoAdicionalPrecificado servico : servicos) {
			if(ConstantesExpedicao.CD_PALETIZACAO.equals(servico.getCdParcela()) && BigDecimalUtils.isZero(servico.getVlServico())){
				disableValorNegociado = Boolean.FALSE;
				break;
			}
		}
		
		result.put("disableValorNegociado",	disableValorNegociado);
		return result;
	}
	
	public String executeImprimirOrdemServico(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(imprimirOrdemServicoService, parameters);
	}
	
	private Locale getUserLocale() {
		return SessionUtils.getUsuarioLogado().getLocale();
	}
	
	public void setService(OrdemServicoService service) {
		this.setMasterService(service);
	}
	public OrdemServicoService getService() {
		return (OrdemServicoService) this.getMasterService();
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setOrdemServicoAnexoService(
			OrdemServicoAnexoService ordemServicoAnexoService) {
		this.ordemServicoAnexoService = ordemServicoAnexoService;
	}
	public void setOrdemServicoItemService(
			OrdemServicoItemService ordemServicoItemService) {
		this.ordemServicoItemService = ordemServicoItemService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}	
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setTabelaServicoAdicionalService(TabelaServicoAdicionalService tabelaServicoAdicionalService) {
		this.tabelaServicoAdicionalService = tabelaServicoAdicionalService;
	}
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setImprimirOrdemServicoService(ImprimirOrdemServicoService imprimirOrdemServicoService) {
		this.imprimirOrdemServicoService = imprimirOrdemServicoService;
	}	
	
}
