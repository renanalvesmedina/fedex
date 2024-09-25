package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.CptMedida;
import com.mercurio.lms.vendas.model.dao.CptMedidaDAO;

/**
 * Classe de servi�o para CRUD:
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.cptMedidaService"
 */
public class CptMedidaService extends CrudService<CptMedida, Long> {
	
	@Override
	public Serializable findById(Long id) {		
		return super.findById(id);
	}
		
	/**
	 * Salva a entidade
	 */
	public Serializable store(CptMedida bean) {		
		return super.store(bean);
	}
	
	/**
	 * Remove uma entidade
	 */
	@Override
	public void removeById(Long id) {		
		super.removeById(id);
	}
	
	/**
	 * Remove uma lista de entidade
	 */
	@Override
	public void removeByIds(List<Long> ids) {		
		super.removeByIds(ids);
	}
	
	/**
	 * Atrav�s do idCliente e idComplexidade � verificado na tabela CPT_MEDIDA se
	 * j� existe o registro
	 *  
	 * @param idCliente
	 * @param idComplexidade
	 * @return
	 */
	public Boolean findComplexidadeCliente(Long idCliente, Long idCptTipoValor) {
		List list = getCptMedidaDAO().findComplexidadeCliente(idCliente, idCptTipoValor);
		return !list.isEmpty();
	}
	
    public void setCptMedidaDAO(CptMedidaDAO dao) {
        setDao( dao );
    }
	
	private CptMedidaDAO getCptMedidaDAO(){
		return (CptMedidaDAO) getDao();
	}

	
}