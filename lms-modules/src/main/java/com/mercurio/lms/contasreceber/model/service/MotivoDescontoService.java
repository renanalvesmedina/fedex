package com.mercurio.lms.contasreceber.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.dao.MotivoDescontoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.motivoDescontoService"
 */
public class MotivoDescontoService extends CrudService<MotivoDesconto, Long> {


	/**
	 * Recupera uma instância de <code>MotivoDesconto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public MotivoDesconto findById(java.lang.Long id) {
        return (MotivoDesconto)super.findById(id);
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
    public java.io.Serializable store(MotivoDesconto bean) {
        return super.store(bean);
    }
    
    /**
     * Carrega o motivo de desconto de acordo com o tpMotivoDesconto
     *
     * @author Hector Julian Esnaola Junior
     * @since 25/09/2007
     *
     * @param tpDesconto
     * @return
     *
     */
    public MotivoDesconto findByTpMotivoDesconto(String tpMotivoDesconto){
    	return getMotivoDescontoDAO().findByTpMotivoDesconto(tpMotivoDesconto);
    }


    /**
     * Carrega uma lista de motivoDesconto de acordo 
     * com a situacao e o idDesconto
     * 
     * Hector Julian Esnaola Junior
     * 18/01/2008
     *
     * @param tpSituacao
     * @param idDesconto
     * @return
     *
     * List<MotivoDesconto>
     *
     */
    public List<MotivoDesconto> findMotivoDescontoByTpSituacaoAndIdDesconto(String tpSituacao, Long idDesconto){
    	return getMotivoDescontoDAO().findMotivoDescontoByTpSituacaoAndIdDesconto(tpSituacao, idDesconto);
    }

    public List<MotivoDesconto> findMotivoDescontoByTpSituacao(String tpSituacao) {
    	return getMotivoDescontoDAO().findMotivoDescontoByTpSituacaoAndIdDesconto(tpSituacao, null);
    }

    public List<MotivoDesconto> findMotivoDescontoByTpMotivoDesconto(Map criteria) {
    	return getMotivoDescontoDAO().findMotivoDescontoByTpMotivoDesconto(criteria.get("idSetorCausadorAbatimento").toString());
    }
    
    public MotivoDesconto findByIntegracao(String cdMotivoDesconto) {
    	return getMotivoDescontoDAO().findByIntegracao(cdMotivoDesconto);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMotivoDescontoDAO(MotivoDescontoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MotivoDescontoDAO getMotivoDescontoDAO() {
        return (MotivoDescontoDAO) getDao();
    }
    
   }