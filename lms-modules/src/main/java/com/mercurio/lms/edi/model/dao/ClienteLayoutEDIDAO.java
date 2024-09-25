package com.mercurio.lms.edi.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.mapping.Array;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.TipoTransmissaoEDI;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.TipoArquivoEDI;
import com.mercurio.lms.vendas.model.Cliente;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class ClienteLayoutEDIDAO extends BaseCrudDao<ClienteLayoutEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return ClienteLayoutEDI.class;
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map map) {		
		map.put("layoutEDI",  FetchMode.JOIN);
		map.put("clienteEDI", FetchMode.JOIN);		
	}
	
	@Override
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("layoutEDI",  FetchMode.JOIN);
		map.put("clienteEDI", FetchMode.JOIN);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<ClienteLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		
		int currentPage = paginatedQuery.getCurrentPage();
		int pageSize = paginatedQuery.getPageSize();
		Map criteria = paginatedQuery.getCriteria();
		
		StringBuilder hql = new StringBuilder()
			.append("select cle from " + getPersistentClass().getName() + " as cle, "+ Cliente.class.getName() +" as cli ")
			.append(" join fetch cle.clienteEDI as clienteedi ")
			.append(" join fetch cle.layoutEDI as layoutedi ")
			.append(" where cli.id = clienteedi.id ");

		if(MapUtils.getObject(criteria, "idClienteEdi") != null) {			
			hql.append("  and cle.clienteEDI.id =:idClienteEdi ");
		}
		hql.append(" order by cli.pessoa.nmPessoa ");
		
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), getRowCountQuery(criteria), currentPage, pageSize, criteria);
	}
	
	private String getRowCountQuery(Map criteria){
		StringBuilder hql = new StringBuilder()
			.append("select cle.id from " + getPersistentClass().getName() + " as cle, "+ Cliente.class.getName() +" as cli ")
			.append(" join cle.clienteEDI as clienteedi ")
			.append(" join cle.layoutEDI as layoutedi ")
			.append(" where cli.id = clienteedi.id ");
	
		if(MapUtils.getObject(criteria, "idClienteEdi") != null) {			
			hql.append("  and cle.clienteEDI.id =:idClienteEdi ");
		}
		return hql.toString();
	}

	public ClienteLayoutEDI findByIdClienteEdi(Long idClienteEdi){
		List<Object> parameters = new ArrayList<Object>();
		String tpProcesso = "2";
		StringBuilder hql = new StringBuilder()
			.append("select cle from " + getPersistentClass().getName() + " as cle ")
			.append(" join fetch cle.transmissaoEDI as transmissaoEDI ");
		hql.append(" where cle.tpProcesso = ? ");

		parameters.add(tpProcesso);
		
		if(idClienteEdi != null) {			
			hql.append(" and cle.clienteEDI.id = ? ");
			parameters.add(idClienteEdi);
		}
		 
		return (ClienteLayoutEDI) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameters.toArray());
	}
	
	public List<TipoTransmissaoEDI> findListTpTransmissao() {
		StringBuilder hql = new StringBuilder();
		hql.append("from TipoTransmissaoEDI ");
		
		return getAdsmHibernateTemplate().find(hql.toString());
	}	
	
	public List<ClienteLayoutEDI> findClientesComAlteracaoLayout(){
		StringBuilder hql = new StringBuilder()
		.append(" select cli ")
		.append(" from ").append(ClienteLayoutEDI.class.getName()).append(" as cli, ")
		.append(ComposicaoLayoutEDI.class.getName()).append(" as co ")
		.append(" where cli.idClienteLayoutEDI = co.clienteLayoutEDI.idClienteLayoutEDI ")
		.append(" and  (TO_CHAR(co.dtVigenciaInicial, 'dd/MM/yyyy') = TO_CHAR(SYSDATE, 'dd/MM/yyyy') OR ") 
		.append("       TO_CHAR(co.dtVigenciaFinal, 'dd/MM/yyyy') = TO_CHAR(sysdate-1, 'dd/MM/yyyy')) ");
		
		List<ClienteLayoutEDI> cliEdiList = getAdsmHibernateTemplate().find(hql.toString());
		
		return cliEdiList;
		
	}
	
	public List<ClienteLayoutEDI> findClientesLayoutPorTipo(){
		
		StringBuilder hql = new StringBuilder()
		.append("select  cli ")
		.append(" from ").append(ClienteLayoutEDI.class.getName()).append(" as cli ");
		
		List<ClienteLayoutEDI> rleList = getAdsmHibernateTemplate().find(hql.toString());
		
		return 	rleList;
}
	
	public List<ClienteLayoutEDI> findPorTipo(Map params){
		
		StringBuilder hql = new StringBuilder()
		.append("select  cli ")
		.append(" from ").append(ClienteLayoutEDI.class.getName()).append(" as cli, ")
						 .append(LayoutEDI.class.getName()).append(" as la, ")
						 .append(TipoArquivoEDI.class.getName()).append(" as tpe ")
		.append(" where cli.layoutEDI.idLayoutEdi = la.idLayoutEdi ")
		.append(" and la.tipoArquivoEDI.idTipoArquivoEDI = tpe.idTipoArquivoEDI ")
		.append(" and cli.nmPasta is not null ");		
		
		if(params != null && !params.isEmpty()){
			hql.append(" and upper(tpe.extTipoArquivoEDI) = 'XML'"); 
		} else {
			hql.append(" and upper(tpe.extTipoArquivoEDI) <> 'XML'");
		}								
		
		List<ClienteLayoutEDI> rleList = getAdsmHibernateTemplate().find(hql.toString());
		
		return 	rleList;
	}
}