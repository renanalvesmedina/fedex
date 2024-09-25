package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalClienteDestinatario;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ServicoAdicionalClienteDestinatarioDAO extends BaseCrudDao<ServicoAdicionalClienteDestinatario, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<?> getPersistentClass() {
		return ServicoAdicionalClienteDestinatario.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<ServicoAdicionalClienteDestinatario> findByIdServicoAdicionalCliente(Long id) {		
		StringBuilder query = new StringBuilder()
		.append("from " + getPersistentClass().getName() + " as master ")	
		.append("	inner join fetch master.clienteDestinatario as clienteDestinatario ")
		.append("	inner join fetch clienteDestinatario.pessoa as pessoa ")
		.append("	inner join fetch master.servicoAdicionalCliente as servicoAdicionalCliente ")			
		.append("where ")
		.append("  servicoAdicionalCliente.idServicoAdicionalCliente = ? ");
				
		return getAdsmHibernateTemplate().find(query.toString(), new Object[]{id});			
	}
	
	public ServicoAdicionalClienteDestinatario findByIdServicoAdicionalClienteByIdClienteDestinatario(Long idServicoAdicionalCliente, Long idClienteDestinatario) {		
		StringBuilder query = new StringBuilder()
		.append("from " + getPersistentClass().getName() + " as master ")	
		.append("	inner join fetch master.clienteDestinatario as clienteDestinatario ")
		.append("	inner join fetch clienteDestinatario.pessoa as pessoa ")
		.append("	inner join fetch master.servicoAdicionalCliente as servicoAdicionalCliente ")			
		.append("where ")
		.append("  servicoAdicionalCliente.idServicoAdicionalCliente = ? ")
		.append(" and clienteDestinatario.idCliente = ? ");
				
		return (ServicoAdicionalClienteDestinatario) getAdsmHibernateTemplate().findUniqueResult(query.toString(), new Object[]{idServicoAdicionalCliente, idClienteDestinatario});			
	}
}