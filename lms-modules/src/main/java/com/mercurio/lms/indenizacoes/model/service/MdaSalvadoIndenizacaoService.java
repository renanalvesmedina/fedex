package com.mercurio.lms.indenizacoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.indenizacoes.model.MdaSalvadoIndenizacao;
import com.mercurio.lms.indenizacoes.model.dao.MdaSalvadoIndenizacaoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.mdaSalvadoIndenizacaoService"
 */
public class MdaSalvadoIndenizacaoService extends CrudService<MdaSalvadoIndenizacao, Long> {
	
	/**
	 * Recupera uma instância de <code>MdaSalvadoIndenizacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public MdaSalvadoIndenizacao findById(java.lang.Long id) {
        return (MdaSalvadoIndenizacao)super.findById(id);
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
    public java.io.Serializable store(MdaSalvadoIndenizacao bean) {
    	// verifica se o mda já não foi salvo
    	
    	MdaSalvadoIndenizacao mdaSalvadoIndenizacao = findByIdMdaAndIdReciboIndenizacao(bean.getMda().getIdDoctoServico(), bean.getReciboIndenizacao().getIdReciboIndenizacao());
    	if (mdaSalvadoIndenizacao!=null && mdaSalvadoIndenizacao.getIdMdaSalvadoIndenizacao()!= bean.getIdMdaSalvadoIndenizacao()) {
    		throw new BusinessException("LMS-21024");
    	}
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMdaSalvadoIndenizacaoDAO(MdaSalvadoIndenizacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MdaSalvadoIndenizacaoDAO getMdaSalvadoIndenizacaoDAO() {
        return (MdaSalvadoIndenizacaoDAO) getDao();
    }
    
    public MdaSalvadoIndenizacao findByIdMdaAndIdReciboIndenizacao(Long idMda, Long idReciboIndenizacao) {
    	return getMdaSalvadoIndenizacaoDAO().findByIdMdaAndIdReciboIndenizacao(idMda, idReciboIndenizacao);
    }
    
    public List findItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getMdaSalvadoIndenizacaoDAO().findItensByIdReciboIndenizacao(idReciboIndenizacao);
    }
    
    public Integer getRowCountItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getMdaSalvadoIndenizacaoDAO().getRowCountItensByIdReciboIndenizacao(idReciboIndenizacao);
    }

   }