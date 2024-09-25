package com.mercurio.lms.configuracoes.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.dao.TelefoneContatoDAO;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.telefoneContatoService"
 */
public class TelefoneContatoService extends CrudService<TelefoneContato, Long> {

    
    /**
     * Retorna um objeto do tipo <code>TelefoneContato</code> para o TelefoneEndereco caso exista.
     * Caso n�o encontre, retorna null.
     * Moacir Zardo Junior - 28-11-2005 
     * @param telefoneEndereco
     * @return TelefoneContato
     */
    public TelefoneContato getTelefoneContato(TelefoneEndereco telefoneEndereco){
        List telefoneContatos = telefoneEndereco.getTelefoneContatos();
        TelefoneContato telefoneContato = null;
        if (telefoneContatos!=null){
            for (Iterator it = telefoneContatos.iterator(); it.hasNext();) {
                telefoneContato = (TelefoneContato) it.next();
                return telefoneContato;
            }
        } 
        return telefoneContato;
    }
    

	/**
	 * Recupera uma inst�ncia de <code>TelefoneContato</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public TelefoneContato findById(java.lang.Long id) {
        return (TelefoneContato)super.findById(id);
    }
    
    /**
     * Criado para buscar o TelefoneContato sem os dados j� recebidos da tela que o chamou
     *
     * @author Jos� Rodrigo Moraes
     * @since 04/12/2006
     *
     * @param id
     * @return
     */
    public TelefoneContato findByIdCustomized(Long id){
    	return getTelefoneContatoDAO().findByIdCustomized(id);    	
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TelefoneContato bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTelefoneContatoDAO(TelefoneContatoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TelefoneContatoDAO getTelefoneContatoDAO() {
        return (TelefoneContatoDAO) getDao();
    }
    
    public List findTelefoneByContatoAndTpUso(Long idContato,String[] tpUso) {
    	return getTelefoneContatoDAO().findTelefoneByContatoAndTpUso(idContato,tpUso);
    }
    
    /**
     * �damo B. Azambuja
     * 
     * */
	public List findTelefoneByContato(TypedFlatMap criteria) {
		 return getTelefoneContatoDAO().findTelefoneByContato(criteria);
	}


	public List<TelefoneContato> findByIdTelefoneEndereco(Long idTelefoneEndereco) {
		return getTelefoneContatoDAO().findByIdTelefoneEndereco(idTelefoneEndereco);	
	}
	
	

   }