package com.mercurio.lms.sim.model.service;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.expedicao.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.sim.model.EventoMonitoramentoCCT;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.sim.model.dao.MonitoramentoNotasFiscaisCCTDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SpringBeanFactory;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ManterParametrizacaoEnvioService;

@Assynchronous(name="MonitoramentoNotasFiscaisCCTService")
public class MonitoramentoNotasFiscaisCCTService extends CrudService<MonitoramentoCCT, Long> {

	private ClienteService clienteService;
	private DomainValueService domainValueService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private EventoMonitoramentoCCTService eventoMonitoramentoCCTService;
	private UsuarioLMSService usuarioLMSService;
	private PedidoColetaService pedidoColetaService;
	private NotaFiscalEDIService notaFiscalEDIService;
	private SpringBeanFactory springBeanFactory;
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;
	private static final Log LOG = LogFactory.getLog(MonitoramentoNotasFiscaisCCTService.class);	
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ManterParametrizacaoEnvioService parametrizacaoEnvioService;
	private AgendamentoDoctoServicoService agendamentoDoctoServicoService;

	private static final String EM_PROCESSO_DEVOLUCAO = "PD";

	public ManterParametrizacaoEnvioService getParametrizacaoEnvioService() {
		return parametrizacaoEnvioService;
	}

	public List<Map<String, Object>> findDatasAgendamentoRelatorioFUP(Long idMonitoramentoCCT) {
		return getMonitoramentoNotasFiscaisCCTDAO().findDatasAgendamentoRelatorioFUP(idMonitoramentoCCT);
	}
	
	public List<Map<String, Object>> findDatasEventoMonitoramentoRelatorioFUP(Long idMonitoramentoCCT, String tpSituacaoNFCCT) {
		return getMonitoramentoNotasFiscaisCCTDAO().findDatasEventoMonitoramentoRelatorioFUP(idMonitoramentoCCT, tpSituacaoNFCCT);
	}
	
	public List<Map<String, Object>> findDadosRelatorioFUP(Long idEnvioCteCliente, Long idClienteCCT) {
		return getMonitoramentoNotasFiscaisCCTDAO().findDadosRelatorioFUP(idEnvioCteCliente, idClienteCCT);
	}
	
	public List<Map<String, Object>> findDadosRelatorioDevolucao(Long idEnvioCteCliente, Long idClienteCCT) {
		return getMonitoramentoNotasFiscaisCCTDAO().findDadosRelatorioDevolucao(idEnvioCteCliente, idClienteCCT);
	}
	
	public List<Map> findMonitoramentosEnvioAgendamentoAutomatico(){
		return getMonitoramentoNotasFiscaisCCTDAO().findMonitoramentosEnvioAgendamentoAutomatico();
	}
	
	public List<Map> findMonitoramentosEnvioSolicitacaoPagtoICMS(){
		return getMonitoramentoNotasFiscaisCCTDAO().findMonitoramentosEnvioSolicitacaoPagtoICMS();
	}
	
	/**
	 * Retorna uma lista para montagem de um combobox, referente ao domínio DM_SITUACAO_NF_CCT, ordenado alfabeticamente pela descrição do valor.
	 * 
	 * @param dominiosNotIn Array contendo os valores que não serão retornados.
	 * @param onlyActive Indicador para considerar os domínios ativos ou não.
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> findComboTpSituacao(String[] dominiosNotIn, Boolean onlyActive) {
		List<Map> retorno = new ArrayList<Map>();
		List<DomainValue> dominios = domainValueService.findDomainValues("DM_SITUACAO_NF_CCT", onlyActive);
		
		if (dominios != null && !dominios.isEmpty()) {
			// Sem este tratamento as 'maiúsculas' eram listadas primeiro na combo.
			Transformer lowerCaseTransformer = new Transformer() {
				public Object transform(Object arg) {
					if(arg != null){
						return ((String) arg).toLowerCase();
					} else {
						return "";
					}
				}
			};
			Comparator comparator = new TransformingComparator(lowerCaseTransformer);
			
			Collections.sort(dominios, new BeanComparator("descriptionAsString", comparator));
		
		
			for (DomainValue dominio : dominios) {
				if(dominiosNotIn != null && ArrayUtils.contains(dominiosNotIn, dominio.getValue())){
					continue;
				}
				
				Map map = new HashMap();
				map.put("vlValorDominioSituacaoCCT", dominio.getValue());
				map.put("dsValorDominioSituacaoCCT", dominio.getDescriptionAsString());
				retorno.add(map);
			}
		}
		return retorno;
	}
	
	public void setParametrizacaoEnvioService(
			ManterParametrizacaoEnvioService parametrizacaoEnvioService) {
		this.parametrizacaoEnvioService = parametrizacaoEnvioService;
	}

	public void storeMonitoramento(MonitoramentoCCT monitoramentoCCT){
		this.store(monitoramentoCCT);
	}

	public void setMonitoramentoNotasFiscaisCCTDAO(MonitoramentoNotasFiscaisCCTDAO dao) {
		setDao(dao);
	}

	private MonitoramentoNotasFiscaisCCTDAO getMonitoramentoNotasFiscaisCCTDAO() {
		return (MonitoramentoNotasFiscaisCCTDAO) getDao();
	}

	@Override
	public Serializable store(MonitoramentoCCT bean) {
		return super.store(bean);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDefinition) {
		return getMonitoramentoNotasFiscaisCCTDAO().findPaginated(criteria , findDefinition);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getMonitoramentoNotasFiscaisCCTDAO().getRowCount(criteria);
	}

	public List findManifestoEntregaByMonitoramentoCCT(TypedFlatMap criteria) {
		return getMonitoramentoNotasFiscaisCCTDAO().findManifestoEntregaByMonitoramentoCCT(criteria);
	}

	public MonitoramentoCCT findMonitoramentoCCTByNrChave(String nrChave){
		return getMonitoramentoNotasFiscaisCCTDAO().findByNrChave(nrChave);		
	}

	public MonitoramentoCCT findById(Long id){
		return getMonitoramentoNotasFiscaisCCTDAO().findById(id);
	}

	public MonitoramentoCCT findById(Long id, String[] fetches) {
		return getMonitoramentoNotasFiscaisCCTDAO().findById(id, fetches);
	}

	public List findChavesMonitoramentoCCTByIds(TypedFlatMap criteria){
		return getMonitoramentoNotasFiscaisCCTDAO().findChavesMonitoramentoCCTByIds(criteria);
	}
	
	/**
	 * 
	 * @param parameters
	 */
	public void validateMonitoramentosSelecionados(TypedFlatMap parameters) {
		
		boolean valid = true;
		Cliente remetente = new Cliente();
		Cliente destinatario = new Cliente();
		Long[] idsmonitoramento = (Long[]) parameters.get("idsMonitoramentoCCT");
		
		for (int x=0; x < idsmonitoramento.length; x++){
			MonitoramentoCCT monitoramentoCCT =  getMonitoramentoNotasFiscaisCCTDAO().findById(idsmonitoramento[x]);
			
			if(!monitoramentoCCT.getTpSituacaoNotaFiscalCCT().equals(new DomainValue("PA")) && !monitoramentoCCT.getTpSituacaoNotaFiscalCCT().equals(new DomainValue("AA")) ){
				valid = false;
				break;
			}
			
			if(x == 0){
				remetente = monitoramentoCCT.getClienteRemetente();
				destinatario = monitoramentoCCT.getClienteDestinatario();
			}
			
			if (!(destinatario.equals(monitoramentoCCT.getClienteDestinatario())) || !(remetente.equals(monitoramentoCCT.getClienteRemetente())) ){
				valid = false;
				break;
			}
			
		}
		if(!valid){
			throw new BusinessException("LMS-10065");
		}
	}
	
	/**
	 * 
	 * @param nrChave
	 */
	public void executeNfeAtualizaProcessamento(String nrChave){
		getMonitoramentoNotasFiscaisCCTDAO().executeNfeAtualizaProcessamento(nrChave);
	}
	
	@SuppressWarnings("unchecked")
	@AssynchronousMethod(name="sim.executeImportNFeFromEDI", type=BatchType.BATCH_SERVICE, feedback=BatchFeedbackType.ON_ERROR)
	public void executeImportNFeFromEDI() {
		Integer qtLimiteNotas = 300;
		Map<String, Cliente> cacheClientes = new HashMap<String, Cliente>();
		List<Object[]> notasNaoProcessadasEDI = new ArrayList<Object[]>();
		BigDecimal qtLimiteDias = (BigDecimal) configuracoesFacade.getValorParametro("LIMITE_DIAS_NF_NAO_PROC_CCT");
		
		do {
			//Como registros estão ordenados por remetente e destinatário, maximiza a utilização do "cache" de clientes.
			notasNaoProcessadasEDI = notaFiscalEletronicaService.findNotasEDINaoProcessadas(qtLimiteNotas, qtLimiteDias.intValue());

			for (Object[] notaValues : notasNaoProcessadasEDI) {
				String XML_CHAVE_NFE = getValor(notaValues[0]);
				String NF_EMITENTE = getValor(notaValues[1]);
				String NF_DESTINATARIO = getValor(notaValues[2]);

				Cliente clienteRemetente = null;
				if (cacheClientes.containsKey(NF_EMITENTE)) {
					clienteRemetente = cacheClientes.get(NF_EMITENTE);
				} else {
					clienteRemetente = NF_EMITENTE != null ? clienteService.findByNumeroIdentificacaoParaCCT(NF_EMITENTE) : null;
					cacheClientes.put(NF_EMITENTE, clienteRemetente);
				}

				Cliente clienteDestinatario = null;
				if (cacheClientes.containsKey(NF_DESTINATARIO)) {
					clienteDestinatario = cacheClientes.get(NF_DESTINATARIO);
				} else {
					clienteDestinatario = NF_DESTINATARIO != null ? clienteService.findByNumeroIdentificacaoParaCCT(NF_DESTINATARIO) : null;
					cacheClientes.put(NF_DESTINATARIO, clienteDestinatario);
				}
				
				if(clienteDestinatario != null){
					clienteDestinatario.setBlConfAgendamento(parametrizacaoEnvioService.findBlConfAgendamentoByTpParametrizacaoEnvioCliente(clienteDestinatario));
					clienteDestinatario.setBlRecolheICMS(parametrizacaoEnvioService.findBlRecolheICMSByTpParametrizacaoEnvioCliente(clienteDestinatario));
				}
				
				try{	
					getServiceParaNovaTransacao().executeImportacaoNFeComNovaTransacao(XML_CHAVE_NFE, clienteRemetente, clienteDestinatario);
				} catch (DataIntegrityViolationException e){
					/* Caso aconteça a concorrência de agendamentos para este mesmo BATCH, ou seja, este método estaria sendo executado simultaneamente por mais de um JOB,
					 * pode ser que aconteça a criação de mais de um MonitoramentoCCT por chave, o que está errado. 
					 * Para resolver isto foi acrescentada uma UK no MonitoramentoCCT para o campo chave.
					 * No momento do commit da transação, do segundo monitoramento, será disparada a exceção que esta sendo tratada aqui. 
					 * Para estes casos não é necessário fazer nada no processo, pois o que deveria ser feito na já foi feito pelo primeiro BATCH.
					 */
					LOG.error(e);
				}
			}

			// Esvazia o "cache" de cliente para evitar estouro de memória.
			cacheClientes.clear();

		} while (!notasNaoProcessadasEDI.isEmpty());

		// 2ª Parte monitoramentos que faltam XML.
		String[] situacoes = { "XD", "XM" };
		cacheClientes = new HashMap<String, Cliente>();
		List<MonitoramentoCCT> monitoramentos = getMonitoramentoNotasFiscaisCCTDAO().findByTpSituacao(situacoes);

		for (MonitoramentoCCT mCCT : monitoramentos) {
			// Esvazia o "cache" de cliente para evitar estouro de memória.
			if (cacheClientes.size() > qtLimiteNotas) {
				cacheClientes.clear();
			}

			NotaFiscalEdi notaFiscalEdi = notaFiscalEletronicaService.findNfeByNrChave(mCCT.getNrChave());
			if (notaFiscalEdi != null) {

				Cliente clienteDestinatario = null;
				if (notaFiscalEdi.getCnpjDest() != null) {
					String CNPJ_DESTINATARIO = notaFiscalEDIService.getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString());

					if (cacheClientes.containsKey(CNPJ_DESTINATARIO)) {
						clienteDestinatario = cacheClientes.get(CNPJ_DESTINATARIO);
					} else {
						clienteDestinatario = clienteService.findByNumeroIdentificacaoParaCCT(CNPJ_DESTINATARIO);
						cacheClientes.put(CNPJ_DESTINATARIO, clienteDestinatario);
					}
					if(clienteDestinatario != null){
						clienteDestinatario.setBlConfAgendamento(parametrizacaoEnvioService.findBlConfAgendamentoByTpParametrizacaoEnvioCliente(clienteDestinatario));
						clienteDestinatario.setBlRecolheICMS(parametrizacaoEnvioService.findBlRecolheICMSByTpParametrizacaoEnvioCliente(clienteDestinatario));
					}
				}

				if (clienteDestinatario == null) {
					continue;
				}

				getServiceParaNovaTransacao().executeAtualizacaoMonitoramentoComNovaTransacao(mCCT, clienteDestinatario);
			}
		}
	}
	
	private MonitoramentoNotasFiscaisCCTService getServiceParaNovaTransacao(){
		return (MonitoramentoNotasFiscaisCCTService) springBeanFactory.getBean("lms.sim.monitoramentoNotasFiscaisCCTService");
	}
	
	/**
	 * Executa atualização dos monitoramentos abrindo nova transação e
	 * comitando.
	 * Método não pode ser private, pois não ganha nova transação.
	 * 
	 * @param monitoramentoCCT
	 * @param clienteDestinatario
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void executeAtualizacaoMonitoramentoComNovaTransacao(MonitoramentoCCT monitoramentoCCT, Cliente clienteDestinatario) {
		Session session = getDao().getAdsmHibernateTemplate().getSessionFactory().openSession();
		session.beginTransaction();
		
		monitoramentoCCT.setClienteDestinatario(clienteDestinatario);
		monitoramentoCCT.setBlConfiguraAgendamento(clienteDestinatario.getBlConfAgendamento());
		monitoramentoCCT.setBlRecolheICMS(clienteDestinatario.getBlRecolheICMS());

		this.store(monitoramentoCCT);

		storeEvento(monitoramentoCCT, "IO", null, SessionUtils.getUsuarioLogado(), true);
		storeEvento("GE", monitoramentoCCT.getNrChave(), null, null, null, null, monitoramentoCCT, SessionUtils.getUsuarioLogado());

		session.flush();
		session.getTransaction().commit();
		session.close();
	}
	
	
	/**
	 * Executa a importação propriamente dita, das notas do Edi, abrindo nova
	 * transação e comitando a mesma no final do método.
	 * Método não pode ser private, pois não ganha nova transação.
	 * 
	 * @param XML_CHAVE_NFE
	 * @param clienteRemetente
	 * @param clienteDestinatario
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void executeImportacaoNFeComNovaTransacao(String XML_CHAVE_NFE, Cliente clienteRemetente, Cliente clienteDestinatario) {
		Session session = getDao().getAdsmHibernateTemplate().getSessionFactory().openSession();
		session.beginTransaction();

		if (clienteRemetente == null || clienteDestinatario == null) {
			getMonitoramentoNotasFiscaisCCTDAO().executeNfeAtualizaProcessamento(XML_CHAVE_NFE);
			return;
		}

		Boolean blAlteraMonitoramento = null;
		MonitoramentoCCT monitoramentoCCT = getMonitoramentoNotasFiscaisCCTDAO().findByNrChave(XML_CHAVE_NFE);
		if (monitoramentoCCT == null) {
			blAlteraMonitoramento = true;

			monitoramentoCCT = new MonitoramentoCCT();
			monitoramentoCCT.setNrChave(XML_CHAVE_NFE);
			monitoramentoCCT.setClienteRemetente(clienteRemetente);
			monitoramentoCCT.setClienteDestinatario(clienteDestinatario);
			monitoramentoCCT.setBlConfiguraAgendamento(clienteDestinatario.getBlConfAgendamento());
			monitoramentoCCT.setBlRecolheICMS(clienteDestinatario.getBlRecolheICMS());
			monitoramentoCCT.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());

			this.store(monitoramentoCCT);

		} else {
			if (monitoramentoCCT.getTpSituacaoNotaFiscalCCT() != null && "XM".equals(monitoramentoCCT.getTpSituacaoNotaFiscalCCT().getValue())) {
				blAlteraMonitoramento = true;
			} else {
				blAlteraMonitoramento = false;
			}

			monitoramentoCCT.setClienteDestinatario(clienteDestinatario);
			monitoramentoCCT.setBlConfiguraAgendamento(clienteDestinatario.getBlConfAgendamento());
			monitoramentoCCT.setBlRecolheICMS(clienteDestinatario.getBlRecolheICMS());

			this.store(monitoramentoCCT);
		}

		storeEvento(monitoramentoCCT, "IO", null, SessionUtils.getUsuarioLogado(), blAlteraMonitoramento);
		storeEvento("GE", XML_CHAVE_NFE, null, null, null, null, monitoramentoCCT, SessionUtils.getUsuarioLogado());
		getMonitoramentoNotasFiscaisCCTDAO().executeNfeAtualizaProcessamento(XML_CHAVE_NFE);

		session.flush();
		session.getTransaction().commit();
		session.close();
	}

	private String getValor(Object valor) {
		if (valor != null) {
			return valor.toString();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void executeVincularDocumentoComMonitoramento(DoctoServico doctoServico, String tpOrigem) {
		MonitoramentoCCT monitoramentoCCT = null;
		List<NotaFiscalConhecimento> nfcs = notaFiscalConhecimentoService.findByConhecimento(doctoServico.getIdDoctoServico());

		for (NotaFiscalConhecimento nfc : nfcs) {
			monitoramentoCCT = findMonitoramentoCCTByNrChave(nfc.getNrChave());
			if (monitoramentoCCT != null) {
				storeEvento(tpOrigem, monitoramentoCCT.getNrChave(), null, null, doctoServico, null, null, SessionUtils.getUsuarioLogado());
			}
		}
	}

	public void storeEvento(String tpOrigem , String nrChave , Cliente cRemetente , PedidoColeta pColeta , DoctoServico dServico, String pObservacao, MonitoramentoCCT monitoramento, Usuario usuarioLogado ) {

		if(monitoramento == null){
			monitoramento = getMonitoramentoNotasFiscaisCCTDAO().findByNrChave(nrChave);
		}
		
		if ("GE".equals(tpOrigem)) {

			if (monitoramento != null) {
				PedidoColeta pedidoColeta = pedidoColetaService.findByNrChave(nrChave);
				if (pedidoColeta != null) {
					monitoramento.setPedidoColeta(pedidoColeta);
					this.store(monitoramento);
				}
				
				if (monitoramento.getClienteDestinatario() != null
						&& !parametrizacaoEnvioService.findBlRecolheICMSByTpParametrizacaoEnvioCliente(monitoramento.getClienteDestinatario())) {
					storeEvento(monitoramento, "PA", null, SessionUtils.getUsuarioLogado(), true);
				}
			}
		}else if("CO".equals(tpOrigem)){

			if (monitoramento == null) {
				Cliente dest = null;
				NotaFiscalEdi notaFiscalEdi = notaFiscalEletronicaService.findNfeByNrChave(nrChave);

				if (notaFiscalEdi != null && notaFiscalEdi.getCnpjDest() != null) {
					String CNPJ_DESTINATARIO = notaFiscalEDIService.getValidatedNrIdentificacao(notaFiscalEdi.getCnpjDest().toString());
					dest = clienteService.findByNumeroIdentificacao(CNPJ_DESTINATARIO);
				}
				
				MonitoramentoCCT monitoramentoToStore = new MonitoramentoCCT();
				monitoramentoToStore.setNrChave(nrChave);
				monitoramentoToStore.setClienteRemetente(cRemetente);
				monitoramentoToStore.setClienteDestinatario(dest);
				monitoramentoToStore.setTpSituacaoNotaFiscalCCT(new DomainValue("XM"));
				monitoramentoToStore.setPedidoColeta(pColeta);
				monitoramentoToStore.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				this.store(monitoramentoToStore);

				storeEvento(monitoramentoToStore , "XM" , null, SessionUtils.getUsuarioLogado() , true);

			} else {
				
				monitoramento.setPedidoColeta(pColeta);
				this.store(monitoramento);

			}
		}else if("PA".equals(tpOrigem)){
			storeEvento(monitoramento , "PA" , pObservacao , SessionUtils.getUsuarioLogado() , true);
		}else if ("AG".equals(tpOrigem)) {
			if(monitoramento == null){
				monitoramento = new MonitoramentoCCT();
				monitoramento.setNrChave(nrChave);
				monitoramento.setClienteRemetente(cRemetente);
				monitoramento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				this.store(monitoramento);
			}
			
			storeEvento(monitoramento , "AG" , pObservacao , SessionUtils.getUsuarioLogado() , true);
			
		}else if ("TA".equals(tpOrigem)) {
			if(monitoramento == null){
				monitoramento = new MonitoramentoCCT();
				monitoramento.setNrChave(nrChave);
				monitoramento.setClienteRemetente(cRemetente);
				monitoramento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				this.store(monitoramento);
			}
			
			storeEvento(monitoramento , "AA" , pObservacao , SessionUtils.getUsuarioLogado() , true);
			
		}else if("AC".equals(tpOrigem)){ 	
			if(monitoramento == null){
				monitoramento = new MonitoramentoCCT();
				monitoramento.setNrChave(nrChave);
				monitoramento.setClienteRemetente(cRemetente);
				monitoramento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				this.store(monitoramento);
			}
			storeEvento(monitoramento , "AC" , pObservacao , SessionUtils.getUsuarioLogado() , true);
		}else if("RA".equals(tpOrigem)){
			if(monitoramento == null){
				monitoramento = new MonitoramentoCCT();
				monitoramento.setNrChave(nrChave);
				monitoramento.setClienteRemetente(cRemetente);
				monitoramento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
				this.store(monitoramento);
			}
			
			if(monitoramento != null){
				
				boolean blConfirmaAgendamento = false; 
				if(monitoramento.getClienteDestinatario() != null){
					blConfirmaAgendamento = parametrizacaoEnvioService.findBlConfAgendamentoByTpParametrizacaoEnvioCliente(monitoramento.getClienteDestinatario());
				}
				
				if(blConfirmaAgendamento){
					storeEvento(monitoramento , "AR" , pObservacao , SessionUtils.getUsuarioLogado() , true);
				}else{
					storeEvento(monitoramento , "RA" , pObservacao , SessionUtils.getUsuarioLogado() , true);
				}
			}
		}else if("SI".equals(tpOrigem)){
			storeEvento(monitoramento , "AI" , null, SessionUtils.getUsuarioLogado() , true);
		}else if("FE".equals(tpOrigem)){
			storeEvento(monitoramento , "PI" , null, SessionUtils.getUsuarioLogado() , true);
		}else if("FA".equals(tpOrigem)){
			storeEvento(monitoramento , "PE" , null, SessionUtils.getUsuarioLogado() , true);	
		}else if("RN".equals(tpOrigem)){
			storeEvento(monitoramento , "SE" , null, SessionUtils.getUsuarioLogado() , true);
		}else if("EM".equals(tpOrigem)){
			if(monitoramento!=null){
				monitoramento.setDoctoServico(dServico);
				this.store(monitoramento);
				storeEvento(monitoramento , "CD" , configuracoesFacade.getMensagem("LMS-10087"), SessionUtils.getUsuarioLogado() , false);
				agendamentoDoctoServicoService.storeOrUpdate(monitoramento, Boolean.TRUE);
			}
		}else if("RE".equals(tpOrigem)){
			storeEvento(monitoramento , "EN" , null, usuarioLogado , true);
		}else if("RR".equals(tpOrigem)){
			storeEvento(monitoramento , "RF" , null, SessionUtils.getUsuarioLogado() , true);
		}else if("RC".equals(tpOrigem)){
			storeEvento(monitoramento , "CA" , null, SessionUtils.getUsuarioLogado() , true);
			agendamentoDoctoServicoService.storeOrUpdate(monitoramento, Boolean.FALSE);
		}else if("RD".equals(tpOrigem)){
			storeEvento(monitoramento , "IN" , null, SessionUtils.getUsuarioLogado() , true);
		}else if("CA".equals(tpOrigem)){
			if(dServico != null && monitoramento!=null){
				storeEvento(monitoramento , "DC" , null, SessionUtils.getUsuarioLogado() , false);
				agendamentoDoctoServicoService.storeOrUpdate(monitoramento, Boolean.FALSE);
				monitoramento.setDoctoServico(null);					
				this.store(monitoramento);
			}
		}
	}

	private void storeEvento(MonitoramentoCCT monitoramento, String domainValue, String dsEvento, Usuario usuario, boolean blAlteraMonitoramento) {
		EventoMonitoramentoCCT evento = new EventoMonitoramentoCCT();
		if(monitoramento==null){
			return;
		}
		if(blAlteraMonitoramento){
			if("AC".equals(domainValue)){
				monitoramento.setTpSituacaoNotaFiscalCCT(new DomainValue("PR"));
			}else{
				monitoramento.setTpSituacaoNotaFiscalCCT(new DomainValue(domainValue));
			}
		}
		evento.setMonitoramentoCCT(monitoramento);
		
		evento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		evento.setDtEvento(JTDateTimeUtils.getDataAtual());
		evento.setUsuario(usuarioLMSService.findById(usuario.getIdUsuario()));
		evento.setTpSituacaoNotaFiscalCCT(new DomainValue(domainValue));
		
		if(dsEvento == null){
			dsEvento = configuracoesFacade.getDomainValue("DM_SITUACAO_NF_CCT", domainValue).getDescriptionAsString();
		}
		evento.setDsComentario(dsEvento);

		eventoMonitoramentoCCTService.store(evento);
	}

	public void storeTabDevolucao(Map parameters) {
		Long idMonitoramentoCCT = Long.valueOf(parameters.get("idMonitoramentoCCT").toString());
		Long idUsuario = Long.valueOf(parameters.get("idUsuario").toString());

		MonitoramentoCCT monitoramentoOld = this.findById(idMonitoramentoCCT);
		
		EventoMonitoramentoCCT evento = setEventoMonitormaentoCCT(parameters, monitoramentoOld, idUsuario);
		
		if(monitoramentoOld.getNrChave().equals(parameters.get("nrChaveNFeDevolucao").toString())){
			Cliente remetente = monitoramentoOld.getClienteRemetente();
			Cliente destinatario = monitoramentoOld.getClienteDestinatario();
			monitoramentoOld.setClienteRemetente(destinatario);
			monitoramentoOld.setClienteDestinatario(remetente);
			monitoramentoOld.setTpSituacaoNotaFiscalCCT(new DomainValue(EM_PROCESSO_DEVOLUCAO));
			monitoramentoOld.setNrChaveDevolucao(parameters.get("nrChaveNFeDevolucao").toString());
			this.store(monitoramentoOld);
			
			evento.setTpSituacaoNotaFiscalCCT(new DomainValue(EM_PROCESSO_DEVOLUCAO));
			eventoMonitoramentoCCTService.store(evento);
		}else{
			evento.setTpSituacaoNotaFiscalCCT(monitoramentoOld.getTpSituacaoNotaFiscalCCT());
			eventoMonitoramentoCCTService.store(evento);
			monitoramentoOld.setNrChaveDevolucao(parameters.get("nrChaveNFeDevolucao").toString());
			MonitoramentoCCT monitoramentoNFeDevolucao = this.findMonitoramentoCCTByNrChave(parameters.get("nrChaveNFeDevolucao").toString());
			
			EventoMonitoramentoCCT eventoNovoMonitoramento = new EventoMonitoramentoCCT();
			
			if(monitoramentoNFeDevolucao != null){
				monitoramentoNFeDevolucao.setTpSituacaoNotaFiscalCCT(new DomainValue(EM_PROCESSO_DEVOLUCAO));
				this.store(monitoramentoNFeDevolucao);
				eventoNovoMonitoramento = setEventoMonitormaentoCCT(parameters, monitoramentoNFeDevolucao, idUsuario);
			}else{
				MonitoramentoCCT monitoramentoNovo = new MonitoramentoCCT();
				monitoramentoNovo.setNrChave(parameters.get("nrChaveNFeDevolucao").toString());
				monitoramentoNovo.setClienteRemetente(monitoramentoOld.getClienteDestinatario());
				monitoramentoNovo.setTpSituacaoNotaFiscalCCT(new DomainValue(EM_PROCESSO_DEVOLUCAO));
				this.store(monitoramentoNovo);
				
				eventoNovoMonitoramento = setEventoMonitormaentoCCT(parameters, monitoramentoNovo, idUsuario);
			}
			eventoNovoMonitoramento.setTpSituacaoNotaFiscalCCT(new DomainValue(EM_PROCESSO_DEVOLUCAO));
			eventoMonitoramentoCCTService.store(eventoNovoMonitoramento);
		}

	}
	
	private EventoMonitoramentoCCT setEventoMonitormaentoCCT(Map parameters, MonitoramentoCCT monitoramento, Long idUsuario) {
		EventoMonitoramentoCCT evento = new EventoMonitoramentoCCT();
		evento.setMonitoramentoCCT(monitoramento);
		evento.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		evento.setDtEvento(JTDateTimeUtils.getDataAtual());
		evento.setUsuario(usuarioLMSService.findById(idUsuario));
		evento.setDsComentario(parameters.get("txtComentario").toString());
		return evento;
	}

	public void updateDataSolicitacaoICMS(List<String> chaves){
		 getMonitoramentoNotasFiscaisCCTDAO().updateDataSolicitacaoICMS(chaves);
	}

	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public NotaFiscalEletronicaService getNotaFiscalEletronicaService() {
		return notaFiscalEletronicaService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public EventoMonitoramentoCCTService getEventoMonitoramentoCCTService() {
		return eventoMonitoramentoCCTService;
	}

	public void setEventoMonitoramentoCCTService(
			EventoMonitoramentoCCTService eventoMonitoramentoCCTService) {
		this.eventoMonitoramentoCCTService = eventoMonitoramentoCCTService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setNotaFiscalEDIService(NotaFiscalEDIService notaFiscalEDIService) {
		this.notaFiscalEDIService = notaFiscalEDIService;
	}

	public void setSpringBeanFactory(SpringBeanFactory springBeanFactory) {
		this.springBeanFactory = springBeanFactory;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public List<Map> findInformacoesAwbByDoctoServico(Long idDoctoServico) {
		return getMonitoramentoNotasFiscaisCCTDAO().findInformacoesAwbByDoctoServico(idDoctoServico);
	}

	public String findInfoPedido(String nrChave) {
		return getMonitoramentoNotasFiscaisCCTDAO().findInfoPedido(nrChave);
	}

	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	
	public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}

}
