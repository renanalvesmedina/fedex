package com.mercurio.lms.sim.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.ContatoComunicacao;
import com.mercurio.lms.sim.model.dao.ContatoComunicacaoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.contatoComunicacaoService"
 */
public class ContatoComunicacaoService extends CrudService<ContatoComunicacao, Long> {

	private VigenciaService vigenciaService;
	/**
	 * Recupera uma inst�ncia de <code>ContatoComunicacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public ContatoComunicacao findById(java.lang.Long id) {
        return (ContatoComunicacao)super.findById(id);
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
    public java.io.Serializable store(ContatoComunicacao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setContatoComunicacaoDAO(ContatoComunicacaoDAO dao) {
        setDao( dao );
    }
    
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ContatoComunicacaoDAO getContatoComunicacaoDAO() {
        return (ContatoComunicacaoDAO) getDao();
    }

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getContatoComunicacaoDAO().findPaginatedCustom(criteria);
	}

	public ResultSetPage findByIdDetalhamento(Long id) {
		return getContatoComunicacaoDAO().findByIdDetalhamento(id);
	}
	/**
	 * �damo B. Azambuja
	 * Vigencia*/
    protected void beforeRemoveById(Long id) {
    	validaRemoveById((Long) id);
		super.beforeRemoveById(id);
	}
	protected void beforeRemoveByIds(List ids) {
		for (Iterator i = ids.iterator() ; i.hasNext() ;)
	        validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
	}
	private void validaRemoveById(Long id) {
		ContatoComunicacao contatoComunicacao   = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(contatoComunicacao);
	}
    protected ContatoComunicacao beforeStore(ContatoComunicacao bean) {
    	ContatoComunicacao contatoComunicacao = (ContatoComunicacao)super.beforeStore(bean);
    	vigenciaService.validaVigenciaBeforeStore(contatoComunicacao);
    	return contatoComunicacao;
    }

	public VigenciaService getVigenciaService() {
		return vigenciaService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
   }