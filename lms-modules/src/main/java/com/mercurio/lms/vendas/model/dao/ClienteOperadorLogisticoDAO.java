package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ClienteOperadorLogistico;

/**
 * @author vagnerh
 *
 */
public class ClienteOperadorLogisticoDAO extends BaseCrudDao<ClienteOperadorLogistico, Long> {

	@Override
    protected Class getPersistentClass() {
	    return ClienteOperadorLogistico.class;
    }
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("clienteOperador", FetchMode.JOIN);
		lazyFindPaginated.put("clienteOperador.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("clienteOperado", FetchMode.JOIN);
		lazyFindPaginated.put("clienteOperado.pessoa", FetchMode.JOIN);
	    super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("clienteOperador", FetchMode.JOIN);
		lazyFindById.put("clienteOperador.pessoa", FetchMode.JOIN);
		lazyFindById.put("clienteOperado", FetchMode.JOIN);
		lazyFindById.put("clienteOperado.pessoa", FetchMode.JOIN);
	    super.initFindByIdLazyProperties(lazyFindById);
	}

	public List<ClienteOperadorLogistico> findByIdClienteOperado(
            Long idClienteOperado) {
		StringBuilder sb = new StringBuilder();
		sb.append("select col from "+ClienteOperadorLogistico.class.getName()+" col ");
		sb.append("join fetch col.clienteOperador operador ");
		sb.append("join fetch operador.pessoa pOperador ");
		sb.append("join fetch col.clienteOperado operado ");
		sb.append("join fetch operado.pessoa pOperado ");
		sb.append(" where col.clienteOperado.idCliente = ?");
		
		return getHibernateTemplate().find(sb.toString(), new Object[]{idClienteOperado});
    }
	
	public void removeByIdClienteOperador(Long idClienteOperador){
		StringBuilder sb = new StringBuilder();
		sb.append("delete from "+ClienteOperadorLogistico.class.getName()+" col ");
		sb.append(" where col.clienteOperador.idCliente = ?");
		
		List parameters = new ArrayList<Object>();
		parameters.add(idClienteOperador);
		super.executeHql(sb.toString(), parameters);
	}
}
