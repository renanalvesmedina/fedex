package com.mercurio.lms.entrega.model.service;

import java.io.Serializable;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.edi.model.service.LogEDIComplementoService;
import com.mercurio.lms.entrega.model.AgendamentoAnexo;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.dao.AgendamentoAnexoDAO;
import com.mercurio.lms.entrega.model.dao.AgendamentoEntregaDAO;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalEletronicaDAO;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.agendamentoEntregaService"
 */
public class AgendamentoEntregaService extends CrudService<AgendamentoEntrega, Long> {

	private WorkflowPendenciaService workflowPendenciaService;	
	private ConfiguracoesFacade configuracoesFacade;
	private AgendamentoAnexoDAO agendamentoAnexoDao;
	private LogEDIComplementoService logEDIComplementoService;
	private NotaFiscalEletronicaDAO notaFiscalEletronicaDAO;


	public NotaFiscalEletronicaDAO getNotaFiscalEletronicaDAO() {
		return notaFiscalEletronicaDAO;
	}

	public void setNotaFiscalEletronicaDAO(
			NotaFiscalEletronicaDAO notaFiscalEletronicaDAO) {
		this.notaFiscalEletronicaDAO = notaFiscalEletronicaDAO;
	}

	public void setAgendamentoAnexoDao(AgendamentoAnexoDAO agendamentoAnexoDao) {
		this.agendamentoAnexoDao = agendamentoAnexoDao;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	
	private LMComplementoDAO lmComplementoDao;
	
	public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
		this.lmComplementoDao = lmComplementoDao;
	}

	/**
	 * Recupera uma instância de <code>AgendamentoEntrega</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public AgendamentoEntrega findById(java.lang.Long id) {
        return (AgendamentoEntrega)getAgendamentoEntregaDAO().findById(id);
    }
    
    public AgendamentoEntrega findByIdDefault(Long id) {
    	return (AgendamentoEntrega) super.findById(id);
    }
    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	public Pessoa findClienteaRemetenteByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		return getAgendamentoEntregaDAO().findClienteRemetenteByIdAgendamentoEntrega(idAgendamentoEntrega);
	}
    
	public Pessoa findClienteDestinatarioByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		return getAgendamentoEntregaDAO().findClienteDestinatarioByIdAgendamentoEntrega(idAgendamentoEntrega);
	}
    
	public Cliente findClienteRemetByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		return getAgendamentoEntregaDAO().findClienteRemetByIdAgendamentoEntrega(idAgendamentoEntrega);
	}
    
	public Cliente findClienteDestByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		return getAgendamentoEntregaDAO().findClienteDestByIdAgendamentoEntrega(idAgendamentoEntrega);
	}
    
    private String getDescricaoAgendamentoEntrega(String sgFilial) {
    	return MessageFormat.format(configuracoesFacade.getMensagem("0"), new Object[]{sgFilial});
    }
    

	protected AgendamentoEntrega beforeInsert(AgendamentoEntrega bean) {
		
		if ( !((AgendamentoEntrega)bean).getTpAgendamento().getValue().equals("TA") ) {
			if(!SessionUtils.isIntegrationRunning()){
			if ( JTDateTimeUtils.comparaData( ((AgendamentoEntrega)bean).getDtAgendamento(), JTDateTimeUtils.getDataAtual()) < 0 ) {
				throw new BusinessException("LMS-09067");
			}
		}
		}
		
		return super.beforeInsert(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(AgendamentoEntrega bean) {
    	Serializable idAgendamentoEntrega = super.store(bean); 
		
    	// LMS 4599 -- Se o usuário logado não for uma empresa parceira (EMPRESA.tp_empresa != 'P'), 
    	// Chamar rotina Verifica Informação Filial Origem Agendamento passando o agendamento 		
	    	if ( !SessionUtils.getFilialSessao().getEmpresa().getTpEmpresa().getValue().equals("P") &&
	    		 !bean.getFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial()) && 
    		 !bean.getTpAgendamento().getValue().equals("TA") && 
    		 (bean.getTpSituacaoAgendamento().getValue().equals("R") || bean.getTpSituacaoAgendamento().getValue().equals("C")) ) {
    		if(!SessionUtils.isIntegrationRunning()){
    		String dsProcesso = getDescricaoAgendamentoEntrega(bean.getFilial().getSgFilial());
    		getWorkflowPendenciaService().generatePendencia(bean.getFilial().getIdFilial(), ConstantesWorkflow.NR902_AGED_CANC_REAG, (Long)idAgendamentoEntrega, dsProcesso, JTDateTimeUtils.getDataHoraAtual());
    	}
    	}
    	 
        return idAgendamentoEntrega; 
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAgendamentoEntregaDAO(AgendamentoEntregaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AgendamentoEntregaDAO getAgendamentoEntregaDAO() {
        return (AgendamentoEntregaDAO) getDao();
    }
    
    public List findAgendamentosByDoctoServico(Long idDoctoServico){
    	return lmComplementoDao.findAgendamentosByDoctoServico(idDoctoServico);
    }
    
    public List findPaginatedAgendamentosByDoctoServico(Long idDoctoServico){
    	TypedFlatMap criteria = new TypedFlatMap();
    	criteria.put("idDoctoServico", idDoctoServico);
    	
    	ResultSetPage rsp = findPaginatedAgendamentosByDoctoServico(criteria);
    	
    	return rsp.getList();
    }
    
    public ResultSetPage findPaginatedAgendamentosByDoctoServico(TypedFlatMap criteria){
    	ResultSetPage lista = lmComplementoDao.findPaginatedAgendamentosByDoctoServico(criteria.getLong("idDoctoServico"), FindDefinition.createFindDefinition(criteria));
    	for(Iterator iter = lista.getList().iterator();iter.hasNext();){
    		Map map = (Map)iter.next();
    		
    		String progrDtTurno= "";
    		if(map.get("dtAgendamento")!= null){
    			YearMonthDay data = (YearMonthDay)map.get("dtAgendamento");
    			progrDtTurno = JTFormatUtils.format(data);
    			if(map.get("dsTurno")!= null)
    				progrDtTurno = progrDtTurno+" - "+map.get("dsTurno").toString();
    		}else if(map.get("dsTurno")!= null)
    			progrDtTurno = map.get("dsTurno").toString();
    		
    		String progrHorario= "";
    		if(map.get("hrPreferenciaInicial")!= null){
    			TimeOfDay hrPreferenciaInicial = (TimeOfDay)map.get("hrPreferenciaInicial");
    			progrHorario = JTFormatUtils.format(hrPreferenciaInicial);
    			if(map.get("hrPreferenciaFinal")!= null){
    				TimeOfDay hrPreferenciaFinal = (TimeOfDay)map.get("hrPreferenciaFinal");
    				progrHorario = progrHorario+" às "+JTFormatUtils.format(hrPreferenciaFinal);
    			}	
    		}else if(map.get("hrPreferenciaFinal")!= null){
    			TimeOfDay hrPreferenciaFinal = (TimeOfDay)map.get("hrPreferenciaFinal");
    			progrHorario = JTFormatUtils.format(hrPreferenciaFinal);
    		}
    		if (!progrHorario.equalsIgnoreCase(""))
    			map.put("agendamento",progrDtTurno +" "+ progrHorario);
    		else 
    			map.put("agendamento",progrDtTurno);
    	}
    	
    	return lista;
    }
    
    public Integer getRowCountAgendamentosByDoctoServico(TypedFlatMap criteria){
    	return lmComplementoDao.getRowCountAgendamentosByDoctoServico(criteria);
    }
    
    /**
	 * Busca um Agendamento de Entrega aberto para o documento de serviço informado.
	 * 
	 * @param idDoctoServico id de documento de serviço
	 * @return instância de AgendamentoDoctoServico
	 */
	public AgendamentoEntrega findAgendamentoAberto(Long idDoctoServico) {
		return getAgendamentoEntregaDAO().findAgendamentoAberto(idDoctoServico);
	}
    
	/**
     * Método que retorna uma lista de map com dados dos agendamentos de entrega relacionados ao documento de serviço o id igual ao
     * passado como parametro   
     * 
     * @param idDoctoServico
     * @return
     */
	public List<Map<String,Object>> findAgendamentoEntregaByIdDoctoServico(Long idDoctoServico) {
		return getAgendamentoEntregaDAO().findListMapAgendamentoEntregaByIdDoctoServico(idDoctoServico);
	}
    
    public List findAgendamentoByIdAgendamento(Long idAgendamento){
    	return lmComplementoDao.findAgendamentoByIdAgendamento(idAgendamento);
    }
    
    public boolean findAgendamentosAba(Long idDoctoServico){
    	return lmComplementoDao.findAgendamentosAba(idDoctoServico);
    }
    
    public AgendamentoEntrega  findAgendamentoAbertoDoctoServico(Long idDoctoServico){
    	return getAgendamentoEntregaDAO().findAgendamentoAbertoDoctoServico(idDoctoServico);
    }
    	
    public AgendamentoEntrega findAgendamentoEntregaByIdDoctoServicoDhEnvio(Long idDoctoServico, DateTime dhEnvio){
    	return getAgendamentoEntregaDAO().findAgendamentoEntregaByIdDoctoServicoDhEnvio(idDoctoServico, dhEnvio);
    }
    	
    public AgendamentoEntrega findAgendamentoEntregaByIdDoctoServicoTpSituacaoAgendamento(Long idDoctoServico, String tpSituacaoAgendamento){
        return getAgendamentoEntregaDAO().findAgendamentoEntregaByIdDoctoServicoTpSituacaoAgendamento(idDoctoServico, tpSituacaoAgendamento);
    }
    	
    public AgendamentoEntrega findAgendamentoEntregaByIdDoctoServicoTpSituacaoAgendamentoDhContato(Long idDoctoServico, String tpSituacaoAgendamento, DateTime dhContato){
    	return getAgendamentoEntregaDAO().findAgendamentoEntregaByIdDoctoServicoTpSituacaoAgendamentoDhContato(idDoctoServico, tpSituacaoAgendamento, dhContato);
    }
    
    public List findAgendamentoAnexoByEntrega(Long idAgendamentoEntrega){
    	return agendamentoAnexoDao.findAgendamentoAnexosByIdAgendamentoEntrega(idAgendamentoEntrega);
    }
    
    public ResultSetPage findPaginatedAgendamentoAnexo(TypedFlatMap criteria){
    	return agendamentoAnexoDao.findPaginatedAgendamentoAnexo(Long.valueOf(criteria.getInteger("idAgendamentoEntrega")), FindDefinition.createFindDefinition(criteria));
    }
    
    public Integer getRowCountAgendamentoAnexo(Long idAgendamentoEntrega){
    	return agendamentoAnexoDao.getRowCountAgendamentoAnexo(idAgendamentoEntrega);
    }
    
    public AgendamentoAnexo storeBasicAgendamentoAnexo(AgendamentoAnexo agendamentoAnexo){
    	return agendamentoAnexoDao.storeBasic(agendamentoAnexo);
    }

    public int removeByIdAgendamentoAnexo(List<Long> ids){
    	return agendamentoAnexoDao.removeByIds(ids);
    }
    
    public void excluiAnexos(Long idAgendamentoEntrega){
    	getAgendamentoEntregaDAO().excluiAnexos(idAgendamentoEntrega);
    }
    
    public List<AgendamentoAnexo> findAllAgendamentoAnexoByEntrega(Long idAgendamentoEntrega){
    	return agendamentoAnexoDao.findAllAgendamentoAnexoByEntrega(idAgendamentoEntrega);
    }
    
    public String findNotasMonitoramentoCCTSendEmailByIdAgendamentoEntrega(Long idAgendamentoEntrega){
		List<String> lista =  getAgendamentoEntregaDAO().findNotasMonitoramentoCCTByIdAgendamentoEntrega(idAgendamentoEntrega);
    	String notas = "";
				
		if(lista != null  && !lista.isEmpty()){
			for(int x =0; x < lista.size(); x++){
				if(x == 0){
					notas = lista.get(x).substring(25,34);
				}else{
					notas = notas + ", " + lista.get(x).substring(25,34); 
				}
			}
		}
		return notas;
    }
    
    public List<String> findNotasMonitoramentoCCTByIdAgendamentoEntrega(Long idAgendamentoEntrega){
    	return getAgendamentoEntregaDAO().findNotasMonitoramentoCCTByIdAgendamentoEntrega(idAgendamentoEntrega);
    }
    
    public String findDataAgendamentoByIdAgendamento(Long id){
		AgendamentoEntrega agendamentoEntrega = findById(id);
		if(agendamentoEntrega.getDtAgendamento() != null){
			return JTDateTimeUtils.formatDateYearMonthDayToString(agendamentoEntrega.getDtAgendamento());
}
		return "";
	}

	public Map findDadosGeraisRelatorioAgendamento(Long idAgendamentoEntrega) {
		return getAgendamentoEntregaDAO().findDadosGeraisRelatorioAgendamento(idAgendamentoEntrega);
	}

	public List<Map<String, Object>> findDadosItensRelatorioAgendamento(Long idAgendamentoEntrega) {
		List<String> chavesNFE = findNotasMonitoramentoCCTByIdAgendamentoEntrega(idAgendamentoEntrega);
		return findDadosItensRelatoriosCCT(chavesNFE);
	}

	public List<Map<String, Object>> findDadosItensRelatoriosCCT(List<String> chavesNFE) {
		List<Map<String, Object>> itensMap = new ArrayList<Map<String, Object>>();
		
		if (chavesNFE != null) {
			String nomeComplementoPedido = (String) configuracoesFacade.getValorParametro("CAMPO_PEDIDO_CCT");
			String nomeComplementoDelivery = (String) configuracoesFacade.getValorParametro("CAMPO_NR_DELIVERY_CCT");

		for (String chave : chavesNFE) {
			Object xmlId = notaFiscalEletronicaDAO.findNfeItensByNrChave(chave);

			if (xmlId != null) {
					String nrPedido = logEDIComplementoService.findVlComplementoByNmComplmentoEChaveNfe(nomeComplementoPedido, chave);
					String nrDelivery = logEDIComplementoService.findVlComplementoByNmComplmentoEChaveNfe(nomeComplementoDelivery, chave);
				
				String xml = notaFiscalEletronicaDAO.findNfeItensByID(xmlId);
				if (xml != null) {
					StringReader sr = new StringReader(xml);

					try {
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = dbf.newDocumentBuilder();
						Document doc = docBuilder.parse(new InputSource(sr));

						// Nodo vol.
						NodeList nList = doc.getElementsByTagName("vol");
						Node nNode = nList.item(0);
						Object nrVolume = null;
						Object vlPesoBruto = null;
						if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) nNode;
							nrVolume = getElementValue(element, "qVol");
							vlPesoBruto = getElementValue(element, "pesoB");
						}
						
						// Nodo ICMSTot.
						nList = doc.getElementsByTagName("ICMSTot");
						nNode = nList.item(0);
						Object vlTotalNf = null;
						if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) nNode;
							vlTotalNf = getElementValue(element, "vNF");
						}
						
						// Nodo ide.
						nList = doc.getElementsByTagName("ide");
						nNode = nList.item(0);
						Object nrNotaFiscal = null;
						Object dtEmissaoNf = null;
							Object nrSerie = null;
						if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) nNode;
							nrNotaFiscal = getElementValue(element, "nNF");
							nrSerie = getElementValue(element, "serie");
							dtEmissaoNf = getElementValue(element, "dEmi");
							if(dtEmissaoNf == null){
								dtEmissaoNf = getElementValue(element, "dhEmi");
							}
						}
						
						// Nodo emit.
						nList = doc.getElementsByTagName("emit");
						nNode = nList.item(0);
						Object dsOrigem = null;
						if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) nNode;
							dsOrigem = getElementValue(element, "xMun");
						}
						
						// Nodo dest.
						nList = doc.getElementsByTagName("dest");
						nNode = nList.item(0);
						Object dsDestino = null;
						Object dsUfDestino = null;
							Object nrIeDest = null;
						if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) nNode;
							dsDestino = getElementValue(element, "xMun");
							dsUfDestino = getElementValue(element, "UF");
								nrIeDest = getElementValue(element, "IE");
						}
						
						// Nodo det.
						nList = doc.getElementsByTagName("det");
						for (int temp = 0; temp < nList.getLength(); temp++) {
							nNode = nList.item(temp);

							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								TypedFlatMap mapNodoDet = new TypedFlatMap();

								Element element = (Element) nNode;
								mapNodoDet.put("nr_item", element.getAttribute("nItem"));
								mapNodoDet.put("vl_unit_item", getElementValue(element, "vUnCom"));
								mapNodoDet.put("vl_total_item", getElementValue(element, "vProd"));
								mapNodoDet.put("nr_ean", getElementValue(element, "cEAN"));
								mapNodoDet.put("ds_cod_produto", getElementValue(element, "cProd"));
								mapNodoDet.put("ds_produto", getElementValue(element, "xProd"));
								mapNodoDet.put("vl_icms", getElementValue(element, "vICMS"));
								mapNodoDet.put("tx_icms", getElementValue(element, "pICMS"));
								mapNodoDet.put("vl_ipi", getElementValue(element, "vIPI"));
								mapNodoDet.put("tx_ipi", getElementValue(element, "pIPI"));
								mapNodoDet.put("vl_qtde_item", getElementValue(element, "qCom"));
									mapNodoDet.put("nr_cfop", getElementValue(element, "CFOP"));
								
								// Campos do nodo vol
								mapNodoDet.put("nr_volume", nrVolume);
								mapNodoDet.put("vl_peso_bruto", vlPesoBruto);
								
								// Campos do nodo ICMSTot
								mapNodoDet.put("vl_total_nf", vlTotalNf);
								
								// Campos do nodo ide
								mapNodoDet.put("nr_nota_fiscal", nrNotaFiscal);
								mapNodoDet.put("dt_emissao_nf", dtEmissaoNf);
									mapNodoDet.put("nr_serie", nrSerie);
								

								// Campos do emit
								mapNodoDet.put("ds_origem", dsOrigem);
								
								// Campos do dest
								mapNodoDet.put("ds_destino", dsDestino);
								mapNodoDet.put("ds_uf_destino", dsUfDestino);
									mapNodoDet.put("nr_ie_dest", nrIeDest);
								
									// Campos EDI
								mapNodoDet.put("nr_pedido", nrPedido);
									mapNodoDet.put("nr_delivery", nrDelivery);
								
								itensMap.add(mapNodoDet);
							}
						}

					} catch (Exception e) {
						throw new InfrastructureException("Ocorreu um erro ao montar os dados do xml id="+xmlId+": "+e.getMessage());
					} finally {
						if (sr != null) {
							sr.close();
						}
					}

				}
			}
		}
		}
		
		return itensMap;
	}

	/**
	 * 
	 * @param element
	 * @param tagName
	 * @return
	 */
	private Object getElementValue(Element element, String tagName){
		if(element != null && element.getElementsByTagName(tagName) != null && element.getElementsByTagName(tagName).item(0) != null){
			return element.getElementsByTagName(tagName).item(0).getTextContent();
		}
		return null;
	}

	public void setLogEDIComplementoService(LogEDIComplementoService logEDIComplementoService) {
		this.logEDIComplementoService = logEDIComplementoService;
	}
}
