package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.CentralizadoraFaturamento;
import com.mercurio.lms.configuracoes.model.dao.CentralizadoraFaturamentoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.centralizadoraFaturamentoService"
 */
public class CentralizadoraFaturamentoService extends CrudService<CentralizadoraFaturamento, Long> {

	/**
	 * Recupera uma instância de <code>CentralizadoraFaturamento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public CentralizadoraFaturamento findById(java.lang.Long id) {
    	CentralizadoraFaturamento c = (CentralizadoraFaturamento)super.findById(id);
    	return c;
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
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(CentralizadoraFaturamento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setCentralizadoraFaturamentoDAO(CentralizadoraFaturamentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private CentralizadoraFaturamentoDAO getCentralizadoraFaturamentoDAO() {
        return (CentralizadoraFaturamentoDAO) getDao();
    }

    @Override
    protected CentralizadoraFaturamento beforeStore (CentralizadoraFaturamento bean) {
    	 //A centralizadora não pode ser igual à centralizada.
    	if (bean.getFilialByIdFilialCentralizada().equals(bean.getFilialByIdFilialCentralizadora())) {
    		throw new BusinessException("LMS-27016");
    	}

    	/**
    	 * Verificar se a centralizadora já existe 
    	 * com a mesma modal e abregência
    	 */
    	List<CentralizadoraFaturamento> filialInvalido = getCentralizadoraFaturamentoDAO().findInvalidFilial(bean, 1);
    	if (filialInvalido.size() > 0){    		
    		throw new BusinessException("LMS-27014");
    	}

    	/**
    	 * Verificar se a centralizadora já existe 
    	 * com a mesma modal e abregência
    	 */
    	filialInvalido = getCentralizadoraFaturamentoDAO().findInvalidFilial(bean, 2);
    	if (filialInvalido.size() > 0){    		
    		throw new BusinessException("LMS-27020");
    	}    	

    	/**
    	 * Verificar se a centralizada já existe 
    	 * com a mesma modal e abregência
    	 * */    	
    	filialInvalido = getCentralizadoraFaturamentoDAO().findInvalidFilial(bean, 3);    	
    	if (filialInvalido.size() > 0){
    		throw new BusinessException("LMS-27015");
    	}

    	return bean;
    }

}