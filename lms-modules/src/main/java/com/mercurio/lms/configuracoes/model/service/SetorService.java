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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.setorService"
 */
public class SetorService extends CrudService<Setor, Long> {


	/**
	 * Recupera uma instância de <code>Setor</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Setor findById(java.lang.Long id) {
        return (Setor)super.findById(id);
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
    public java.io.Serializable store(Setor bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSetorDAO(SetorDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SetorDAO getSetorDAO() {
        return (SetorDAO) getDao();
    }
    
    /**
     * Método para retornar uma coleção de Setores ordenados por dsSetor.
     * Utilizado em combobox.
     * @author Rodrigo Antunes
     * @param criteria
     * @return List ordenada pela descrição do setor
     */
    public List findSetorOrderByDsSetor(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsSetor");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
    
    /**
     * Busca o setor de um usuário.
     * @param usuario
     * @return
     * @deprecated Setor do usuário não é mais utilizado.
     */
    public Setor findSetorByUsuario(Usuario usuario) {    	
    	return getSetorDAO().findSetorByUsuario(usuario);
    }
}