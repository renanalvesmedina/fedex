package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.DescrTributIcmsPessoa;
import com.mercurio.lms.configuracoes.model.dao.DescrTributIcmsPessoaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.descrTributIcmsPessoaService"
 */
public class DescrTributIcmsPessoaService extends CrudService<DescrTributIcmsPessoa, Long> {

	private InscricaoEstadualService inscricaoEstadualService;

	/**
	 * Recupera uma instância de <code>DescrTributIcmsPessoa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public DescrTributIcmsPessoa findById(java.lang.Long id) {
        return (DescrTributIcmsPessoa)super.findById(id);
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
    public java.io.Serializable store(DescrTributIcmsPessoa bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDescrTributIcmsPessoaDAO(DescrTributIcmsPessoaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DescrTributIcmsPessoaDAO getDescrTributIcmsPessoaDAO() {
        return (DescrTributIcmsPessoaDAO) getDao();
    }
    
    /**
     * Validar DescricaoTributacaoIcms antes de efetuar o cadastramento.
     * @author Alexandre Menezes
     * @param bean entidade a ser armazenada.
     * @return entidade que será armazenada. 
     */
    protected DescrTributIcmsPessoa beforeStore(DescrTributIcmsPessoa bean) {
    	DescrTributIcmsPessoa descrTributIcmsPessoa = (DescrTributIcmsPessoa) bean;

    	// Validar Vigencia
    	if (!getDescrTributIcmsPessoaDAO().validarVigencia(descrTributIcmsPessoa.getInscricaoEstadual().getIdInscricaoEstadual(),
    			                                           descrTributIcmsPessoa.getIdDescrTributIcmsPessoa(),
    			                                           descrTributIcmsPessoa.getDtVigenciaInicial(),
    			                                           descrTributIcmsPessoa.getDtVigenciaFinal()) ) { 
    		throw new BusinessException("LMS-00047");
    	}
    	return super.beforeStore(bean);
    }

	public InscricaoEstadualService getInscricaoEstadualService() {
		return inscricaoEstadualService;
	}

	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
   }
