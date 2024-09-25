package com.mercurio.lms.edi.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class ComposicaoLayoutEDIDAO extends BaseCrudDao<ComposicaoLayoutEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return ComposicaoLayoutEDI.class;
	}
	
	public ComposicaoLayoutEDI findById(Long id){
						
		DetachedCriteria dc = createDetachedCriteria()
		.setFetchMode("campoLayout", FetchMode.JOIN)
		.setFetchMode("registroLayout", FetchMode.JOIN)
		.setFetchMode("layout", FetchMode.JOIN)
		.setFetchMode("clienteLayoutEDI", FetchMode.JOIN)
		.setFetchMode("deParaEDI", FetchMode.JOIN)
		.setFetchMode("layout.tipoArquivoEDI", FetchMode.JOIN)
		.add(Restrictions.eq("idComposicaoLayout", id));
		
		return (ComposicaoLayoutEDI)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
		
	@SuppressWarnings("unchecked")
	public ResultSetPage<ComposicaoLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> criteria = paginatedQuery.getCriteria();

		boolean blFindAllComposicaoCliente = false; // CQPRO00024407 
		if(MapUtils.getObject(criteria, "blFindAllComposicaoCliente") != null){
			blFindAllComposicaoCliente = (Boolean)MapUtils.getObject(criteria, "blFindAllComposicaoCliente");
		}
			
		StringBuilder query = new StringBuilder()
			.append("from " + getPersistentClass().getName() + " as composicao ")
			.append(" join fetch composicao.layout as lay ")
			.append(" join fetch composicao.registroLayout as reglay ")
			.append(" join fetch composicao.campoLayout as camlay ")			
			.append(" left join fetch composicao.clienteLayoutEDI as clilay ")			
			.append(" left join fetch composicao.deParaEDI as depara ")			
			.append(" left join fetch clilay.embarcadora as embarcadora ")
			.append(" left join fetch embarcadora.clienteEmbarcador as clienteEmbarcador ")
			.append(" left join fetch clienteEmbarcador.pessoa as pessoaEmbarcador ")
			;
		
		query.append("where 1=1 ");
		
		if(MapUtils.getObject(criteria, "idLayoutEdi") != null) {
			query.append("  and lay.id like :idLayoutEdi ");
		}
		if(MapUtils.getObject(criteria, "idRegistro") != null) {
			
			query.append("  and reglay.id = :idRegistro ");
		}
		if(MapUtils.getObject(criteria, "idCampo") != null) {
			query.append("  and camlay.id = :idCampo ");
		}
		if(blFindAllComposicaoCliente){
			if(MapUtils.getObject(criteria, "idClienteEDIFilialEmbarcadora") != null && MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and (clilay.clienteEDI.id = :idCliente or clilay.embarcadora.id = :idClienteEDIFilialEmbarcadora or composicao.clienteLayoutEDI = null ) ");
			}
		} else {
		if(MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and clilay.clienteEDI.id = :idCliente ");
		}
		}
		
		if(!blFindAllComposicaoCliente){
			if(MapUtils.getObject(criteria, "idClienteLayoutEDI") != null) {
				query.append("  and clilay.idClienteLayoutEDI = :idClienteLayoutEDI ");
			}else{
				query.append("  and clilay.clienteEDI is null ");			
			}
		}
		
		query.append(" order by lay.nmLayoutEdi,lay.tpLayoutEdi ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}	
	
	
	public ResultSetPage<ComposicaoLayoutEDI> findPaginatedReport(PaginatedQuery paginatedQuery) {
		
		StringBuilder query = new StringBuilder()
		
		.append(" from ComposicaoLayoutEDI comp ")
		.append(" join fetch comp.layout as ledi ")
		.append(" join fetch comp.campoLayout as campo ")
		.append(" join fetch comp.registroLayout  as registro  ")
		.append(" left join fetch comp.deParaEDI as depara ")
		.append(" left join fetch comp.clienteLayoutEDI as cliente  ")
		.append(" WHERE 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();				
		if(MapUtils.getObject(criteria, "idLayoutEdi") != null) {
			query.append(" and ledi.id = :idLayoutEdi and cliente is null");
		} else if(MapUtils.getObject(criteria, "idClienteEdi") != null) {
			query.append(" and (cliente.clienteEDI.idClienteEDI = :idClienteEdi");
			query.append(" or (cliente is null and ledi.id in (select cli.layoutEDI.id from ClienteLayoutEDI cli where cli.clienteEDI.id = :idClienteEdi)))");
		}
		
		query.append(" order by registro.identificador,comp.posicao ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	
	public ResultSetPage<ComposicaoLayoutEDI> findPaginatedCriteria(PaginatedQuery paginatedQuery) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.setFetchMode("layout", FetchMode.JOIN)
		.setFetchMode("registroLayout", FetchMode.JOIN)
		.setFetchMode("campoLayout", FetchMode.JOIN)
		.setFetchMode("clienteLayoutEDI", FetchMode.JOIN)
		.setFetchMode("clienteLayoutEDI.clienteEDI", FetchMode.JOIN);
				
		Map<String, Object> criteria = paginatedQuery.getCriteria();		
		dc.add(Restrictions.eq("clienteLayoutEDI.clienteEDI.id", MapUtils.getLong(criteria, "idCliente")));
		
		return findPaginatedByDetachedCriteria(dc, paginatedQuery.getCurrentPage(), paginatedQuery.getPageSize());
	}
	
	public List<ComposicaoLayoutEDI> find(ComposicaoLayoutEDI composicao){
		
		DetachedCriteria dc = createDetachedCriteria();
		
		if(composicao.getClienteLayoutEDI() != null && composicao.getClienteLayoutEDI().getIdClienteLayoutEDI() != null){
			dc.add(Restrictions.eq("clienteLayoutEDI.id", composicao.getClienteLayoutEDI().getIdClienteLayoutEDI()));
		}
		
		dc.add(Restrictions.eq("layout.id", composicao.getLayout().getIdLayoutEdi()))
		  .add(Restrictions.eq("registroLayout.id", composicao.getRegistroLayout().getIdRegistroLayoutEdi()))
		  .add(Restrictions.eq("campoLayout.id", composicao.getCampoLayout().getIdCampo()));
		
		dc.add(Restrictions.ge("dtVigenciaInicial", composicao.getDtVigenciaInicial()));		
		dc.add(Restrictions.le("dtVigenciaFinal", composicao.getDtVigenciaFinal()));
		
		return findByDetachedCriteria(dc);
	}
	
	public List<ComposicaoLayoutEDI> findComposicaoCliente(ComposicaoLayoutEDI composicao){
		
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("layout.id", composicao.getLayout().getIdLayoutEdi()))
		.add(Restrictions.eq("registroLayout.id", composicao.getRegistroLayout().getIdRegistroLayoutEdi()))
		.add(Restrictions.eq("clienteLayoutEDI.id", composicao.getClienteLayoutEDI().getIdClienteLayoutEDI()));
		
		return findByDetachedCriteria(dc);
	}
	
	public List<ComposicaoLayoutEDI> findComposicaoClienteTipo(Map crit){
		Long idRegistroLayoutEdi = (Long)crit.get("idRegistroLayoutEdi");
		Long idLayoutEdi = (Long)crit.get("idLayoutEdi");
		Long idClienteLayoutEdi = (Long)crit.get("idClienteLayoutEdi");		
		
		StringBuilder query = new StringBuilder()
		.append(" select co ")
		.append(" from   ").append(ComposicaoLayoutEDI.class.getName()).append(" as co ")
		.append(" where  co.layout.idLayoutEdi = :idLayoutEdi ")
		.append(" and    co.registroLayout.idRegistroLayoutEdi = :idRegistroLayoutEdi ")
		.append(" and    ( co.clienteLayoutEDI.idClienteLayoutEDI = :idClienteLayoutEdi ")
		.append(" or     ( co.clienteLayoutEDI.idClienteLayoutEDI is null")
		.append("          and co.campoLayout.idCampo not in ( select co2.campoLayout.idCampo ")
		.append("                                        from   ").append(ComposicaoLayoutEDI.class.getName()).append(" as co2 ")
		.append("                                        where  co2.layout.idLayoutEdi = co.layout.idLayoutEdi")
		.append("                                        and    co2.registroLayout.idRegistroLayoutEdi = co.registroLayout.idRegistroLayoutEdi")
		.append("                                        and    co2.clienteLayoutEDI.idClienteLayoutEDI = :idClienteLayoutEdi ))) ");
		
		List<ComposicaoLayoutEDI> rleList = getAdsmHibernateTemplate().findByNamedParam(query.toString(), crit);
		
		return 	rleList;		
}
	
	
}
