package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.CentralizadoraFaturamento;
import com.mercurio.lms.configuracoes.model.dao.CentralizadoraFaturamentoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.centralizadoraFaturamentoService"
 */
public class CentralizadoraFaturamentoService extends CrudService<CentralizadoraFaturamento, Long> {

	/**
	 * Recupera uma inst�ncia de <code>CentralizadoraFaturamento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public CentralizadoraFaturamento findById(java.lang.Long id) {
    	CentralizadoraFaturamento c = (CentralizadoraFaturamento)super.findById(id);
    	return c;
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
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(CentralizadoraFaturamento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCentralizadoraFaturamentoDAO(CentralizadoraFaturamentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CentralizadoraFaturamentoDAO getCentralizadoraFaturamentoDAO() {
        return (CentralizadoraFaturamentoDAO) getDao();
    }

    @Override
    protected CentralizadoraFaturamento beforeStore (CentralizadoraFaturamento bean) {
    	 //A centralizadora n�o pode ser igual � centralizada.
    	if (bean.getFilialByIdFilialCentralizada().equals(bean.getFilialByIdFilialCentralizadora())) {
    		throw new BusinessException("LMS-27016");
    	}

    	/**
    	 * Verificar se a centralizadora j� existe 
    	 * com a mesma modal e abreg�ncia
    	 */
    	List<CentralizadoraFaturamento> filialInvalido = getCentralizadoraFaturamentoDAO().findInvalidFilial(bean, 1);
    	if (filialInvalido.size() > 0){    		
    		throw new BusinessException("LMS-27014");
    	}

    	/**
    	 * Verificar se a centralizadora j� existe 
    	 * com a mesma modal e abreg�ncia
    	 */
    	filialInvalido = getCentralizadoraFaturamentoDAO().findInvalidFilial(bean, 2);
    	if (filialInvalido.size() > 0){    		
    		throw new BusinessException("LMS-27020");
    	}    	

    	/**
    	 * Verificar se a centralizada j� existe 
    	 * com a mesma modal e abreg�ncia
    	 * */    	
    	filialInvalido = getCentralizadoraFaturamentoDAO().findInvalidFilial(bean, 3);    	
    	if (filialInvalido.size() > 0){
    		throw new BusinessException("LMS-27015");
    	}

    	return bean;
    }

}