package com.mercurio.lms.workflow.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.workflow.model.Integrante;
import com.mercurio.lms.workflow.model.dao.IntegranteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.integranteService"
 */
public class IntegranteService extends CrudService<Integrante, Long> {


	/**
	 * Recupera uma inst�ncia de <code>Integrante</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Integrante findById(java.lang.Long id) {
        return (Integrante)super.findById(id);
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
    
    protected Integrante beforeStore(Integrante bean) {
    	Integrante integrante = (Integrante)bean;
    	
		//N�o pode ter um usu�rio e um perfil vehiculado a um integrante no mesmo tempo
    	final Perfil perfil = integrante.getPerfil();
		Usuario usuario = integrante.getUsuario();
		if (usuario != null && usuario.getIdUsuario() == null) {
			integrante.setUsuario(null);
			usuario = null; // define como null o usuario caso ele n�o tenha sido
							// buscado na tela, workaround para lidar com dados 
							// enviados incorretamente da tela
		}
    	if ((perfil != null && usuario != null) || 
    			(perfil == null && usuario == null)){    		
    		throw new BusinessException("LMS-39002");
    	}
    	
    	return super.beforeStore(bean);
    }
    
    public List findIntegrantesByComite(Long idComite, Long idFilial, Long idEmpresa){
    	return this.getIntegranteDAO().findIntegrantesByComite(idComite, idFilial, idEmpresa);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Integrante bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setIntegranteDAO(IntegranteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private IntegranteDAO getIntegranteDAO() {
        return (IntegranteDAO) getDao();
    }
   }