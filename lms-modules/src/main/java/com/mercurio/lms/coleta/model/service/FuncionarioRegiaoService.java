package com.mercurio.lms.coleta.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.FuncionarioRegiao;
import com.mercurio.lms.coleta.model.dao.FuncionarioRegiaoDAO;
import com.mercurio.lms.configuracoes.model.dao.UsuarioDAO;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.RegiaoColetaEntregaFilService;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.funcionarioRegiaoService"
 */
public class FuncionarioRegiaoService extends CrudService<FuncionarioRegiao, Long> {

	private RegiaoColetaEntregaFilService regiaoColetaEntregaFilService;
	private UsuarioDAO usuarioDAO;
	private FuncionarioService funcionarioService;
	
	public RegiaoColetaEntregaFilService getRegiaoColetaEntregaFilService() {
		return regiaoColetaEntregaFilService;
	}

	public void setRegiaoColetaEntregaFilService(RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
		this.regiaoColetaEntregaFilService = regiaoColetaEntregaFilService;
	}
	
	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	/**
	 * Recupera uma instância de <code>FuncionarioRegiao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public FuncionarioRegiao findById(java.lang.Long id) {
        return (FuncionarioRegiao)super.findById(id);
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
    public java.io.Serializable store(FuncionarioRegiao funcionarioRegiao) {    	
        return super.store(funcionarioRegiao);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFuncionarioRegiaoDAO(FuncionarioRegiaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FuncionarioRegiaoDAO getFuncionarioRegiaoDAO() {
        return (FuncionarioRegiaoDAO) getDao();
    }
    
    /**
     * Busca todas a Regioes de Coleta Entrega que estao relacionadas com a filial do usuário.
     * 
     * @param criteria
     * @return
     */
    public List findRegiaoColetaEntregaByFilial(TypedFlatMap criteria) {
    	
    	criteria = new TypedFlatMap();
    	criteria.put("filial.idFilial", SessionUtils.getFilialSessao().getIdFilial());
    	
    	return this.getRegiaoColetaEntregaFilService().findListRegiaoVigente(criteria);
    }
    
    public Map findFilialUsuarioLogado() {
    	
    	Filial filial = SessionUtils.getFilialSessao();
    	
    	Map mapFilial = new HashMap();
    	mapFilial.put("idFilial", filial.getIdFilial());
    	mapFilial.put("sgFilial", filial.getSgFilial());
    	
    	Map mapPessoa = new HashMap();
    	mapPessoa.put("nmFantasia", filial.getPessoa().getNmFantasia());
    	mapFilial.put("pessoa", mapPessoa);
    	
    	Map mapSessionObjects = new HashMap();
    	mapSessionObjects.put("filial", mapFilial);
    	
    	return mapSessionObjects;
    }
}