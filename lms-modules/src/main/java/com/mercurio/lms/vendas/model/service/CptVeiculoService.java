package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.CptVeiculo;
import com.mercurio.lms.vendas.model.dao.CptVeiculoDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.cptVeiculoService"
 */
public class CptVeiculoService extends CrudService<CptVeiculo, Long> {
	
	@Override
	public Serializable findById(Long id) {		
		return super.findById(id);
	}
		
	/**
	 * Salva a entidade
	 */
	public Serializable store(CptVeiculo bean) {		
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
	 * Metodo utilizado pela rotina automatica AtualizacaoAutomaticaTabelasCPTService
	 * @return
	 */
	public List findVigencias() {		
		return getCptVeiculoDAO().findVigencias();
	}	
	
	/**
	 * Remove uma lista de entidade
	 */
	@Override
	public void removeByIds(List<Long> ids) {		
		super.removeByIds(ids);
	}
	
	/**
	 * Verifica na tabela CPT_VEICULO se já existe um cliente com 
	 * a mesma frota cadastrado
	 * 
	 * @param idCliente
	 * @param nrFrota
	 * @return
	 */
	public Boolean findFrotaCliente(Long idCliente, String nrFrota) {
		List list = getCptVeiculoDAO().findFrotaCliente(idCliente,nrFrota);
		return 	!list.isEmpty();
	}	
	
    public void setCptVeiculoDAO(CptVeiculoDAO dao) {
        setDao( dao );
    }
	
	private CptVeiculoDAO getCptVeiculoDAO(){
		return (CptVeiculoDAO) getDao();
	}

	
}