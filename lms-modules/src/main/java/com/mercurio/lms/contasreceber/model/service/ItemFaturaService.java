package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.dao.ItemFaturaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.itemFaturaService"
 */
public class ItemFaturaService extends CrudService<ItemFatura, Long> {


	public void flush() {
        getItemFaturaDAO().flush();
    }
	
	public void clear() {
		getItemFaturaDAO().clear();
	}
	
	/**
	 * Recupera uma inst�ncia de <code>ItemFatura</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ItemFatura findById(java.lang.Long id) {
        return (ItemFatura)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
	
	/**
	 * Remove uma cole��o de itens fatura
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 27/06/2007
	 *
	 * @param ids
	 *
	 */
    public void removeItemFaturas(List itens) {
        getItemFaturaDAO().removeItemFaturas(itens);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ItemFatura bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setItemFaturaDAO(ItemFaturaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ItemFaturaDAO getItemFaturaDAO() {
        return (ItemFaturaDAO) getDao();
    }
    
    /**
     * Retorna o itemFatura ativo do devedorDocServFat informado.
     * 
     * @author Micka�l Jalbert
     * @since 15/05/2006
     * 
     * @param Long idDevedorDocServFat
     * @return ItemFatura
     * */
    public ItemFatura findByDevedorDocServFat(Long idDevedorDocServFat){
    	return getItemFaturaDAO().findByDevedorDocServFat(idDevedorDocServFat);
    }
    
	/**
	 * Gera um item fatura para um boleto a partir de um devedorDocServFat e de uma fatura
	 * 
	 * @author Micka�l Jalbert
	 * @since 18/04/2006
	 * 
	 * @param DevedorDocServFat devedorDocServFat
	 * @param Fatura fatura
	 * 
	 * @return ItemFatura
	 * */    
    public ItemFatura generateItemFaturaDeBoleto(DevedorDocServFat devedorDocServFat, Fatura fatura){
    	ItemFatura itemFatura = new ItemFatura();
    	
    	itemFatura.setDevedorDocServFat(devedorDocServFat);
    	itemFatura.setFatura(fatura);
    	
    	store(itemFatura);
    	
    	return itemFatura;
    }
    
    /**
     * Obtem a lista de tpDocumentoServico atrav�s do ItemFatura
     * @param idFatura
     * @return
     */
	public List<TypedFlatMap>  findTpDocumentoItemFatura(Long idFatura) {
		return getItemFaturaDAO().findTpDocumentoItemFatura(idFatura);
	}
	        
	
	@SuppressWarnings("rawtypes")
	public List findItemsManutenidos(Long nrFatura, Long idFilial) {
		return getItemFaturaDAO().findItemsManutenidos(nrFatura,idFilial);
	}

	public ItemFatura findByIdInitializeDoctoServico(Long id) {
        ItemFatura itemFatura = (ItemFatura)super.findById(id);
        itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue();
        
        return itemFatura;
	}
	        
   }