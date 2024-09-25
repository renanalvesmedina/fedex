package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Setor;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.dao.SetorDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.setorService"
 */
public class SetorService extends CrudService<Setor, Long> {


	/**
	 * Recupera uma inst�ncia de <code>Setor</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Setor findById(java.lang.Long id) {
        return (Setor)super.findById(id);
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
    public java.io.Serializable store(Setor bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setSetorDAO(SetorDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private SetorDAO getSetorDAO() {
        return (SetorDAO) getDao();
    }
    
    /**
     * M�todo para retornar uma cole��o de Setores ordenados por dsSetor.
     * Utilizado em combobox.
     * @author Rodrigo Antunes
     * @param criteria
     * @return List ordenada pela descri��o do setor
     */
    public List findSetorOrderByDsSetor(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsSetor");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
    
    /**
     * Busca o setor de um usu�rio.
     * @param usuario
     * @return
     * @deprecated Setor do usu�rio n�o � mais utilizado.
     */
    public Setor findSetorByUsuario(Usuario usuario) {    	
    	return getSetorDAO().findSetorByUsuario(usuario);
    }
}