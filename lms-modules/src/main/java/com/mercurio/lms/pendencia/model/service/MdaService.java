package com.mercurio.lms.pendencia.model.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.pendencia.model.ItemMda;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.pendencia.model.dao.MdaDAO;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.pendencia.mdaService"
 */
public class MdaService extends CrudService<Mda, Long> {
	
	private ConfiguracoesFacade configuracoesFacade;
	private ConversaoMoedaService conversaoMoedaService;
	private NfItemMdaService nfItemMdaService;
	private UsuarioService usuarioService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public NfItemMdaService getNfItemMdaService() {
		return nfItemMdaService;
	}
	public void setNfItemMdaService(NfItemMdaService nfItemMdaService) {
		this.nfItemMdaService = nfItemMdaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	/**
	 * Recupera uma instância de <code>Mda</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public Mda findById(java.lang.Long id) {
        return (Mda)super.findById(id);
    }
    
	/**
	 * Recupera uma instância de <code>Mda</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public Mda findMdaByIdDoctoServico(Long idDoctoServico) {
        return this.getMdaDAO().findMdaByIdDoctoServico(idDoctoServico);
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

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Mda bean) {
        return super.store(bean);
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @param items entidade filha a ser armazenada
	 * @param config classe de configuração da entidade filha
	 * @return entidade que foi armazenada.
	 */
    public TypedFlatMap storeAll(Mda bean, ItemList items, ItemListConfig config) {
    	
    	try {
			if (items == null || !items.hasItems()) {
				throw new BusinessException("LMS-17002");
			}
			if(items.getRemovedItems() != null && !items.getRemovedItems().isEmpty()) {
				throw new BusinessException("LMS-17003");
			}
			
			// Soma o total de valores e remove as notas fiscais dos itens de MDA caso exista o ID.
			BigDecimal vlTotalMercadoria = BigDecimalUtils.ZERO;
			BigDecimal psTotalReal = BigDecimalUtils.ZERO;
			int qtTotalVolumes = 0;
			for(Iterator iter = items.iterator(bean.getIdDoctoServico(), config); iter.hasNext();) {
				ItemMda itemMda = (ItemMda) iter.next();
				
				BigDecimal valorMercadoriaConvertido = conversaoMoedaService.findConversaoMoeda(
																					 SessionUtils.getPaisSessao().getIdPais(),
																					 itemMda.getMoeda().getIdMoeda(),
																					 SessionUtils.getPaisSessao().getIdPais(),
																					 SessionUtils.getMoedaSessao().getIdMoeda(),
																					 JTDateTimeUtils.getDataAtual(),
																					 itemMda.getVlMercadoria());				
				vlTotalMercadoria = vlTotalMercadoria.add(valorMercadoriaConvertido);
				
				if(itemMda.getIdItemMda().compareTo(Long.valueOf(0)) > 0) {
					this.getNfItemMdaService().removeByIdItemMda(itemMda.getIdItemMda());
				}
				psTotalReal = psTotalReal.add( itemMda.getPsItem() );
				qtTotalVolumes += itemMda.getQtVolumes().intValue();
			}
			
			bean.setVlMercadoria(vlTotalMercadoria);
			bean.setPsReal(psTotalReal);
			bean.setPsAforado(psTotalReal);
			bean.setQtVolumes(Integer.valueOf(qtTotalVolumes));
			
	    	// Pega o ultimo numero de MDA
	    	Long nrDoctoServicoFinal = null;
	    	if(bean.getNrDoctoServico() == null) {
				nrDoctoServicoFinal = configuracoesFacade.
					incrementaParametroSequencial(bean.getFilialByIdFilialOrigem().getIdFilial(), "NR_MDA", true);
				
				bean.setNrDoctoServico(nrDoctoServicoFinal);
	    	} else {
	    		nrDoctoServicoFinal = bean.getNrDoctoServico();
	    	}
	    	
	    	bean = this.getMdaDAO().storeAll(bean, items, config);
			
			TypedFlatMap mapBeanStored = new TypedFlatMap();
			mapBeanStored.put("idDoctoServico",  bean.getIdDoctoServico());
			mapBeanStored.put("sgFilial",  bean.getFilialByIdFilialOrigem().getSgFilial());
			mapBeanStored.put("nrDoctoServico",  nrDoctoServicoFinal);
			mapBeanStored.put("vlTotalMercadoria",  vlTotalMercadoria);			
			
			return mapBeanStored;
		
		} catch (RuntimeException e) {
			if (items != null) {
				items.rollbackItemsState();
			}
            
            throw e;
		}    	
    }
    
    /**
     * Método que retorna um list com um objeto de MDA usando como filtro o nrDoctoServico
     * e o idFilialOrigem.
     * 
     * @param Long nrDoctoServico
     * @param Long idFilialOrigem
     * @return List
     */
    public List findMdaByNrDoctoServicoByIdFilialOrigem(Long nrDoctoServico, Long idFilialOrigem) {
    	return this.getMdaDAO().findMdaByNrDoctoServicoByIdFilialOrigem(nrDoctoServico, idFilialOrigem);
    }
    
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMdaDAO(MdaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MdaDAO getMdaDAO() {
        return (MdaDAO) getDao();
    }
    
    public Map findSomaQtdVolumesItens(Long idDoctoServico) {
    	Map map = new HashMap();
    	map.put("sumQtVolumes", Integer.valueOf(0));
    	map.put("sumPsItem", new BigDecimal(0));
    	List lista = getMdaDAO().findSomaQtdVolumesItens(idDoctoServico);
    	if (lista != null && !lista.isEmpty()) {
    		Map mapMda = (Map)lista.get(0);
    		if (mapMda != null) {
    			map.put("sumQtVolumes", mapMda.get("sumQtVolumes"));
    			map.put("sumPsItem", mapMda.get("sumPsItem"));
    		}
    	}
    	return map;
    }
    
    /**
     * Registra o recebimento de um MDA
     * @param idMda
     * @param idUsuarioRecebidoPor
     * @param nmRecebedorCliente
     * @return retorna a data do recebimento
     */
    public DateTime executeRecebimentoMda(Long idMda, Long idUsuarioRecebidoPor, String nmRecebedorCliente){
    	DateTime dataHoraRecebimento = JTDateTimeUtils.getDataHoraAtual();
    	Mda mda = this.findMdaByIdDoctoServico(idMda);
    	mda.setTpStatusMda(new DomainValue("N"));
    	mda.setDhRecebimento(dataHoraRecebimento);
    	if (!nmRecebedorCliente.equals("")) {
    		mda.setNmRecebedorCliente(nmRecebedorCliente);
    	}
    	if (idUsuarioRecebidoPor != null) {
			mda.setUsuarioByIdUsuarioRecebidoPor(usuarioService.findById(idUsuarioRecebidoPor));
		}
		this.store(mda);
		
		//Gerando evento de recebimento.
    	String nrDoctoServico = mda.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(mda.getNrDoctoServico().toString(), 8, '0');
		this.getIncluirEventosRastreabilidadeInternacionalService().generateEventoDocumento(
				ConstantesSim.EVENTO_REGISTRAR_RECEBIMENTO_MDA, idMda, 
				SessionUtils.getFilialSessao().getIdFilial(),
				nrDoctoServico, dataHoraRecebimento, null, SessionUtils.getFilialSessao().getSiglaNomeFilial(), mda.getTpDocumentoServico().getValue());
		
		return dataHoraRecebimento;
    }
    
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
     * @param TypedFlatMap criteria
     * @return
     */
    public ResultSetPage findPaginatedMda(TypedFlatMap criteria) {
    	return this.getMdaDAO().findPaginatedMda(criteria, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados 
     * para determinados parametros.
     * 
     * @param TypedFlatMap criteria
     * @return
     */
    public Integer getRowCountMda(TypedFlatMap criteria) {
    	return this.getMdaDAO().getRowCountMda(criteria, FindDefinition.createFindDefinition(criteria));
    }    
    
    public List findByNrMdaByFilialOrigem(Long nrMda, Long idFilial){
    	return getMdaDAO().findByNrMdaByFilialOrigem(nrMda, idFilial);
    }

    public List findDocumentosServico(Long idMda){
    	return getMdaDAO().findDocumentosServico(idMda);
    }
    
	/**
	 * Busca os documentos de servico (Mdas - MDA)
	 * a partir do id do Manifesto de entrega.
	 */
    public List findMdasByIdManifestoEntrega(Long idManifesto){
    	return getMdaDAO().findMdasByIdManifestoEntrega(idManifesto);
    }
    
	/**
	 * Busca os documentos de servico (Mdas - MDA)
	 * a partir do id do Manifesto de viagem nacional.
	 */
    public List findMdasByIdManifestoViagemNacional(Long idManifesto){
    	return getMdaDAO().findMdasByIdManifestoViagemNacional(idManifesto);
    }
    
	public TypedFlatMap findDocumentoCancelado(TypedFlatMap criteria) {
		TypedFlatMap dados = new TypedFlatMap();
		boolean documentoCanceladoRetorno = getMdaDAO().findDocumentoCancelado(criteria);
	
		dados.put("isDocumentoCancelado", documentoCanceladoRetorno);
		dados.put("nrDoctoServico", criteria.get("nrDoctoServico"));
		
		return dados;
	}
}