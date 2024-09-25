package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.CptFuncionario;
import com.mercurio.lms.vendas.model.dao.CptFuncionarioDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.cptFuncionarioService"
 */
public class CptFuncionarioService extends CrudService<CptFuncionario, Long> {
	
	@Override
	public Serializable findById(Long id) {		
		return super.findById(id);
	}
		
	/**
	 * Salva a entidade
	 */
	public Serializable store(CptFuncionario bean) {		
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
	 * Metodo utilizado pela rotina automatica AtualizacaoAutomaticaTabelasCPTService
	 * @return
	 */	
	public List<CptFuncionario> findVigencias() {		
		return getCptFuncionarioDAO().findVigencias();
	}	
	
    public void setCptFuncionarioDAO(CptFuncionarioDAO dao) {
        setDao( dao );
    }
	
	private CptFuncionarioDAO getCptFuncionarioDAO(){
		return (CptFuncionarioDAO) getDao();
	}

	/**
	 * Verifica se ja existe registro cadastrado, através dos 
	 * dados passodos como parametro 
	 * 
	 * @param idCliente
	 * @param nrMatricula
	 * @return
	 */
	public Boolean findFuncionarioCliente(Long idCliente, String nrMatricula) {
		List list = getCptFuncionarioDAO().findFuncionarioCliente(idCliente,nrMatricula);
		return !list.isEmpty();
	}

	
}