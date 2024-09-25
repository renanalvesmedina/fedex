package com.mercurio.lms.vol.model.service;


import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolContatos;
import com.mercurio.lms.vol.model.dao.ContatosVolDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vol.contatosVolService"
 */
public class ContatosVolService extends CrudService<VolContatos, Long> {
		
	/**
	 * Recupera uma inst�ncia de <code>VolContatos</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public VolContatos findById(java.lang.Long id) {
        return (VolContatos)super.findById(id);
    }
    
        
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
     public java.io.Serializable store(VolContatos bean) {
        return super.store(bean);
    }
    
    
	/**
	 * Recupera recusa
	 * @param criteria
	 * @return volRecusa
	 */
    public ResultSetPage findPaginatedContatos(TypedFlatMap criteria){  
    	FindDefinition fd = FindDefinition.createFindDefinition(criteria);
    	return getContatosVolDAO().findPaginatedContatos(criteria, fd);
    }
    
    
    public Integer getRowCountContatos(TypedFlatMap criteria){
    	return getContatosVolDAO().getRowCountContatos(criteria);
    }
    
    /**
     * retorna ContatoVol pelo nome do contato, levando em considera��o a empresa do contato
     * @param criteria
     * @return
     */
    public List findContatoVolByNomeContato(TypedFlatMap criteria){
    	return getContatosVolDAO().findContatoVolByNomeContato(criteria);
    	
    }
 
    
    /**
     * retorna ContatoVol pelo e-mail exato do contato
     * @param criteria
     * @return
     */
    public List findContatoVolByEmailContato(TypedFlatMap criteria){
    	return getContatosVolDAO().findContatoVolByEmailContato(criteria);
    	
    }
    
    /**
     * retorna ContatoVol pelo e-mail parcial do contato
     * @param criteria
     * @return
     */
    public List findContatoVolByEmailParcial(TypedFlatMap criteria){
    	return getContatosVolDAO().findContatoVolByEmailParcial(criteria);
    	
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
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
     public void setContatosVolDAO(ContatosVolDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
     private ContatosVolDAO getContatosVolDAO() {
        return (ContatosVolDAO) getDao();
    }
     
    public String findNomeContatoByEmail(String emailContato){
    	List nomeContatoList = this.getContatosVolDAO().findNomeContatoByEmail(emailContato); 
    	Map map = (Map)nomeContatoList.get(0);
    	String nomeContato = (String)map.get("nmContato");
    	
    	return nomeContato;
    }

}
