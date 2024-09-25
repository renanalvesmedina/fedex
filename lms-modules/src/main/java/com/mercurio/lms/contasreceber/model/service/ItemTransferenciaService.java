package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.AgendaTransferencia;
import com.mercurio.lms.contasreceber.model.ItemTransferencia;
import com.mercurio.lms.contasreceber.model.Transferencia;
import com.mercurio.lms.contasreceber.model.dao.ItemTransferenciaDAO;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.itemTransferenciaService"
 */
public class ItemTransferenciaService extends CrudService<ItemTransferencia, Long> {


	/**
	 * Recupera uma instância de <code>ItemTransferencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ItemTransferencia findById(java.lang.Long id) {
        return (ItemTransferencia)super.findById(id);
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
	// FIXME corrigir para retornar o ID
    public ItemTransferencia store(ItemTransferencia bean) {
    	super.store(bean);
    	getItemTransferenciaDAO().getSessionFactory().getCurrentSession().flush();
        return bean;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setItemTransferenciaDAO(ItemTransferenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ItemTransferenciaDAO getItemTransferenciaDAO() {
        return (ItemTransferenciaDAO) getDao();
    }
    
    /**
	 * Busca as transferências que possuam itens de transferência e estejam pendentes de recebimento
	 * @param tfm Parâmetros de pesquisa Pode ser passado o id do DevedorDocServFat pra filtrar por devedor
	 * @return Lista de transferências
	 */
    public List findTransferenciasPendentesWithItemTransferencia(TypedFlatMap tfm){
    	return getItemTransferenciaDAO().findTransferenciasPendentesWithItemTransferencia(tfm);
    }
    
    /**
     * Busca todas as Transferências de um determinado Devedor
     * @param idDevedorDocServFat Identificador do Devedor
     * @return Lista de Transferências desse devedor
     */
     public List findTransferenciasByDevedorDocServFat(Long idDevedorDocServFat){
    	 return getItemTransferenciaDAO().findTransferenciasByDevedorDocServFat(idDevedorDocServFat);
     }
     
     /**
      * Retorna o número de transferencia que tem o devedor informado com a situacao de transferencia informada.
      * 
      * @author Mickaël Jalbert
      * @since 11/01/2007
      * 
      * @param idDevedorDocServFat
      * @param tpSituacaoTransferencia
      * @return
      */
      public Integer findRowCountByDevedorDocServFatTpSituacao(Long idDevedorDocServFat, String tpSituacaoTransferencia){
     	 return getItemTransferenciaDAO().findRowCountByDevedorDocServFatTpSituacao(idDevedorDocServFat, tpSituacaoTransferencia);
      }     
     
     /**
      * Método responsável por salvar um ItemTransferencia
      * 
      * @author Hector Julian Esnaola Junior
	  * 21/03/2006
	  * 
      * @param agendaTransferencia
      * @param transferencia
      * @return Serializable
      */
     public ItemTransferencia storeItemTransferenciaByAgendaTransferencia(AgendaTransferencia agendaTransferencia, Transferencia transferencia){
    	 
    	 ItemTransferencia itemTransferencia = new ItemTransferencia();
    	 
    	 itemTransferencia.setDevedorDocServFat(agendaTransferencia.getDevedorDocServFat());
    	 itemTransferencia.setTransferencia(transferencia);
    	 itemTransferencia.setClienteByIdNovoResponsavel(agendaTransferencia.getCliente());
    	 itemTransferencia.setMotivoTransferencia(agendaTransferencia.getMotivoTransferencia());
    	 itemTransferencia.setClienteByIdAntigoResponsavel(agendaTransferencia.getDevedorDocServFat().getCliente());
    	 itemTransferencia.setObItemTransferencia(agendaTransferencia.getObAgendaTransferencia());
    	 itemTransferencia.setDivisaoClienteNovo(agendaTransferencia.getDivisaoCliente());
    	 itemTransferencia.setDivisaoClienteAntigo(agendaTransferencia.getDevedorDocServFat().getDivisaoCliente());
    	 
    	 this.store(itemTransferencia);
    	 
    	 //getItemTransferenciaDAO().getSessionFactory().getCurrentSession().flush();
    	 
    	 return itemTransferencia;
     }
     
     /**
      * 
      * @param idItemTransferencia
      * @return Map
      */
     public List findItemTransferenciaByDevedorDocServFat(Long idDevedorDocServFat){
    	 
    	 List list = getItemTransferenciaDAO().findItemTransferenciaByDevedorDocServFat(idDevedorDocServFat);
    	 
    	 for (Iterator iter = list.iterator(); iter.hasNext();) {
		
    		 Map element = (Map) iter.next();
			
			String nrIdentificacao = (String)element.remove("nrIdentificacaoNova");
			String tpIdentificacao = ((DomainValue)element.remove("tpIdentificacaoNova")).getValue();
			String nmPessoa = (String)element.remove("nmPessoaNova");
			
			element.put("identificacao", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao) + " - " + nmPessoa);
			
		}
    	
    	return list;
     }
     
     /**
      * Exclui os elementos da List
      *
      * @author Hector Julian Esnaola Junior
      * @since 21/05/2007
      *
      */
     public void removeItensTransferencia(List<ItemTransferencia> itensTransferencia){
     	getItemTransferenciaDAO().removeItensTransferencia(itensTransferencia);
     }
}