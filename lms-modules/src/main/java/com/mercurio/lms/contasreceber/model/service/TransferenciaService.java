package com.mercurio.lms.contasreceber.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.AgendaTransferencia;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Transferencia;
import com.mercurio.lms.contasreceber.model.dao.TransferenciaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.transferenciaService"
 */
public class TransferenciaService extends CrudService<Transferencia, Long> {

	private ConfiguracoesFacade configuracoesFacade;
	
	private ItemTransferenciaService itemTransferenciaService;
	
	private DevedorDocServFatService devedorDocServFatService;
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}

	public Map findByIdWithFiliais(Long id) {
		return (Map) getTransferenciaDAO().findByIdWithFiliais(id);
	}
	
	public ResultSetPage findPaginatedTransferenciaDebito(Map map){
		return getTransferenciaDAO().findPaginatedTransferenciaDebito(map, FindDefinition.createFindDefinition(map));
	}
	
	public Integer getRowCountTransferenciaDebito(Map map){
		return getTransferenciaDAO().getRowCountTransferenciaDebito(map);
	}

	/**
	 * Verifica se a quantidade de Itens de uma Transferencia é superior a quantidade informada.<BR>
	 * @param idTransferencia Transferencia
	 * @param count quantidade mínima
	 * @return true se ouver mais Itens do que a quantidade informada; false, igual o menos itens
	 */
	public boolean validateCountItensByMin(Long idTransferencia, Integer count){
		Integer countItens = getTransferenciaDAO().findCountItens(idTransferencia);
		return (countItens.intValue() > count.intValue());
	}
	
	/**
	 * 
	 * @param idTransferencia
	 * @return
	 */
	public ResultSetPage findItensByTransferencia(Map map){
		return getTransferenciaDAO().findItensByTransferencia(map, FindDefinition.createFindDefinition(map));
	}
	
	public Integer getRowCountFromItensByTransferencia(Map map){
		return getTransferenciaDAO().getRowCountFromItensByTransferencia(map);
	}

	/**
	 * Recupera uma instância de <code>Transferencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Transferencia findById(java.lang.Long id) {
    	return (Transferencia)super.findById(id);
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
    public java.io.Serializable store(Transferencia bean) {
        return super.store(bean);
    }
    
    /**
     * Retorna a lista de transferancia, com os itens, a partir do id do devedor.
     * 
     * @author Mickaël Jalbert
     * 06/03/2006
     * 
     * @param Long idDevedor
     * @return List
     * */
    public List findByIdDevedorDocServFat(Long idDevedor){
    	return this.getTransferenciaDAO().findByIdDevedorDocServFat(idDevedor);
    }
    
    /**
     * Valida a Data/Hora da Transmissão.<BR>
     * @param idTransmissao
     * @return true se for NULL; false, diferente de NULL. 
     */
    public boolean validateDhTransmissaoIsNull(Long idTransferencia){
    	return getTransferenciaDAO().validateDhTransmissaoIsNull(idTransferencia);
    }
    

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTransferenciaDAO(TransferenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TransferenciaDAO getTransferenciaDAO() {
        return (TransferenciaDAO) getDao();
    }
    
    /**
     * Método responsável por salvar uma Transferencia na base 
     * 
     * @author Hector Julian Esnaola Junior
	 * 20/03/2006
	 * 
     * @param filialOrigem
     * @param filialDestino
     * @param tpOrigem
     * @return Transferencia salva no banco
     */
    public Transferencia storeTransferenciaByAgendaTransferencia(AgendaTransferencia agenda){
    	
    	Transferencia transferencia = new Transferencia();
    	
    	transferencia.setNrTransferencia(this.configuracoesFacade
    			.incrementaParametroSequencial(agenda.getFilialByIdFilialOrigem().getIdFilial(), "ULT_TRANSFERENCIA", true));
    	
    	transferencia.setFilialByIdFilialOrigem(agenda.getFilialByIdFilialOrigem());
    	transferencia.setFilialByIdFilialDestino(agenda.getFilialByIdFilialDestino());
    	transferencia.setDtEmissao(JTDateTimeUtils.getDataAtual());
    	
    	/** Se a filial destino não possui data de implantação no LMS ou a data de implantação é futura, receber automaticamente */
    	if(agenda.getFilialByIdFilialDestino().getDtImplantacaoLMS() == null || agenda.getFilialByIdFilialDestino().getDtImplantacaoLMS().compareTo(JTDateTimeUtils.getDataAtual()) > 0){
    		transferencia.setTpSituacaoTransferencia(new DomainValue("RE"));
    		transferencia.setDtRecebimento(JTDateTimeUtils.getDataAtual());
    	}else{
    		transferencia.setTpSituacaoTransferencia(new DomainValue("PR"));
    		transferencia.setDtRecebimento(null);
    	}
    	
    	transferencia.setTpOrigem(agenda.getTpOrigem());
    	
    	transferencia.setBlIndicadorImpressao(Boolean.FALSE);
    	transferencia.setDhTransmissao(null);
    	
    	this.store(transferencia);
    	
    	getTransferenciaDAO().getSessionFactory().getCurrentSession().flush();
    	
    	return transferencia;
    }
    
    /**
     * 
     * @param idItemTransferencia
     * @return Map
     */
    public Map findDadosTransferencia(Long idItemTransferencia){
    	Map element = getTransferenciaDAO().findDadosTransferencia(idItemTransferencia);
    	
    	//Seta a descricao do dominio
    	DomainValue situacao = (DomainValue)element.remove("tpSituacaoTransferencia");
    	element.put("tpSituacaoTransferencia", situacao.getDescription());
    	
    	
    	
    	return element;
    }
    
    /**
     * @see validateDevedorDocServFat(DevedorDocServFat devedorDocServFat)
     * */
    public void validateDevedorDocServFat(Long idDevedorDocServFat){
    	DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByIdInitLazyProperties(idDevedorDocServFat, false);
    	validateDevedorDocServFat(devedorDocServFat);
    }       
    
    /**
     * Verifica se existe transferencia pendente no devedorDocServFat pendente, se tem lança uma exception.
     * 
     * @author Mickaël Jalbert
     * @since 19/04/2006
     * 
     * @param Long idDevedorDocServFat
     * @return Boolean
     * 
     * */
    public Boolean validateDevedorDocServFat(DevedorDocServFat devedorDocServFat){
		Integer nrTransferencia = itemTransferenciaService.findRowCountByDevedorDocServFatTpSituacao(devedorDocServFat.getIdDevedorDocServFat(), "PR");
		
		if (nrTransferencia > 0){
			throw new BusinessException("LMS-36019");
		}
    	
		return Boolean.TRUE;
    }

	public void setItemTransferenciaService(
			ItemTransferenciaService itemTransferenciaService) {
		this.itemTransferenciaService = itemTransferenciaService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	/**
	 * Executa a função do botão Retransmitir
	 * Atualiza a tabela Transferencia com dh_transmissao com um valor nulo
	 * @param idTransferencia Identificador da transferencia a ser atualizada
	 * @return transferencia atualizada
	 */
	public java.io.Serializable storeRetransmitirTransferenciaDebito(Long idTransferencia) {
		// Foi feito um evict do objeto para que o Hibernate dispare um update mesmo
		// que o campo DhTransmissao nao tenha alteração de valores.
		// Isso é necessário para que seja disparada uma trigger no banco de dados.
		Transferencia transferencia = findById(idTransferencia);
		getTransferenciaDAO().getAdsmHibernateTemplate().evict(transferencia);
		transferencia.setDhTransmissao(null);
		store(transferencia);
		getTransferenciaDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().flush();
		return transferencia;
	}
	
	/**
     * Retorna a lista de transferancia de acordo com o idDoctoServico e o tpSituacaoTransferencia
     * 
     * @author Hector Julian Esnaola Junior
     * 29/06/2006
     * 
     * @param Long idDoctoServico
     * @param String tpSituacaoTransferencia
     *  
     * @return List
     * */
    public List findByIdDoctoServicoAndTpSituacaoTransferencia(Long idDoctoServico, String tpSituacaoTransferencia){
    	return getTransferenciaDAO().findByIdDoctoServicoAndTpSituacaoTransferencia(idDoctoServico, tpSituacaoTransferencia);
    }
	
    /**
     * Retorna a lista de transferancia de acordo com a tpSituacaoTransferencia
     * 
     * @author Hector Julian Esnaola Junior
     * @since 16/05/2007
     * 
     * @param Long nrTransferencia
     * @param Long idFilialOrigem
     *  
     * @return List
     * 
     */
    public Transferencia findByNrTransfereciaAndIdFilialOrigem(Long nrTransferencia, Long idFilialOrigem){
    	return getTransferenciaDAO().findByNrTransfereciaAndIdFilialOrigem(nrTransferencia, idFilialOrigem);
    }
    
}