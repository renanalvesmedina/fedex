package com.mercurio.lms.workflow.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dao.PendenciaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.pendenciaService"
 */
public class PendenciaService extends CrudService<Pendencia, Long> {
	/**
	 * Recupera uma inst�ncia de <code>Pendencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public Pendencia findById(java.lang.Long id) {
        return (Pendencia)super.findById(id);
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
    public java.io.Serializable store(Pendencia bean) {
        return super.store(bean);
    }
    
    public List findPendenciasAbertasByOcorrencia(Long idOcorrencia) {
    	return this.getPendenciaDAO().findPendenciasAbertasByOcorrencia(idOcorrencia);	
    }
    
    public List findPendenciasByOcorrencia(Long idOcorrencia) {
    	return this.getPendenciaDAO().findPendenciasByOcorrencia(idOcorrencia);	
    }    

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setPendenciaDAO(PendenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private PendenciaDAO getPendenciaDAO() {
        return (PendenciaDAO) getDao();
    }
    
	/**
	 * @author Micka�l Jalbert
	 * @since 10/11/2006
	 * 
	 * Retorna o usuario da pendencia informada
	 * 
	 * @param Long idPendencia
	 * @return List
	 */
	public Usuario findSolicitanteByPendencia(Long idPendencia) {
		List<Usuario> lstUsuarios = getPendenciaDAO().findSolicitanteByPendencia(idPendencia);

		if (lstUsuarios.size() == 1) {
			return lstUsuarios.get(0);
		} else {
			return null;
		}
	}	
        
	public List<Pendencia> findPendenciaByEvento(Long idProcesso, Long idEvento, String ... tpSituacao) {
		return getPendenciaDAO().findPendenciaByEvento(idProcesso, idEvento, tpSituacao);
	}
}