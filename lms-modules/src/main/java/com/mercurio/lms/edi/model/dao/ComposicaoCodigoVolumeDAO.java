package com.mercurio.lms.edi.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.ComposicaoCodigoVolume;
import com.mercurio.lms.edi.model.CampoLayoutEDI;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.DeParaEDI;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class ComposicaoCodigoVolumeDAO extends BaseCrudDao<ComposicaoCodigoVolume, Long>{

	@Override
	public Class getPersistentClass() {		
		return ComposicaoCodigoVolume.class;
	}

	public ResultSetPage<ComposicaoCodigoVolume> findPaginated(PaginatedQuery paginatedQuery) {
		
		StringBuilder query = new StringBuilder()
			.append(" from ComposicaoCodigoVolume as composicao ")
			.append(" join fetch composicao.composicaoLayoutEDI clayout ")
			.append(" join fetch composicao.composicaoCampoEDI  ccampo ")
			.append(" join fetch ccampo.layout clay ")
			.append(" join fetch clay.tipoLayoutDocumento tpLayout ")
			.append(" join fetch ccampo.registroLayout reglay ")
			.append(" join fetch ccampo.campoLayout cpLayout ")
			.append(" where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
 		if(criteria.get("idComposicao") != null){
			query.append(" and clayout.id = :idComposicao ");
		}
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}
	
	public List findByComposicaoLayoutId(Long idComposicaoLayoutEDI) {
		StringBuilder hql = new StringBuilder()
		.append(" SELECT ccv ")
		.append(" FROM "+ComposicaoCodigoVolume.class.getName()+" as ccv ")		
		.append(" WHERE ccv.composicaoLayoutEDI.idComposicaoLayout = :idComposicaoLayoutEDI ")
		.append(" ORDER BY ccv.ordem ");
		;
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idComposicaoLayoutEDI", idComposicaoLayoutEDI);
	}
	
	public List<ComposicaoCodigoVolume> findByIdClienteLayoutEdi(Long idClienteLayoutEdi) {
		StringBuilder hql = new StringBuilder()
		.append("select cdv ")
		.append(" from ").append(ComposicaoLayoutEDI.class.getName()).append(" as co, ")
		.append(ComposicaoCodigoVolume.class.getName()).append(" as cdv, ")
		.append(CampoLayoutEDI.class.getName()).append(" as ca ")
		.append(" where co.campoLayout.idCampo = ca.idCampo ")
		.append(" and co.idComposicaoLayout = cdv.composicaoCampoEDI.idComposicaoLayout ")
		.append(" and co.idComposicaoLayout in ( select cdv.composicaoCampoEDI.idComposicaoLayout ")
		.append("                                from ").append(ComposicaoCodigoVolume.class.getName()).append(" as cdv, ")
		                                                .append(ComposicaoLayoutEDI.class.getName()).append(" as co ")
		.append("                                where cdv.composicaoLayoutEDI.idComposicaoLayout = co.idComposicaoLayout ")
		.append("                                and   co.clienteLayoutEDI.idClienteLayoutEDI = :idClienteLayoutEdi ) ")
		.append(" and cdv.composicaoLayoutEDI.idComposicaoLayout in ( select cdv.composicaoLayoutEDI.idComposicaoLayout ")
		.append("                                                     from ").append(ComposicaoCodigoVolume.class.getName()).append(" as cdv, ")
				                                                             .append(ComposicaoLayoutEDI.class.getName()).append(" as co ")
		.append("                                                     where cdv.composicaoLayoutEDI.idComposicaoLayout = co.idComposicaoLayout ")
		.append("                                                     and   co.clienteLayoutEDI.idClienteLayoutEDI = :idClienteLayoutEdi ) ")		
		.append(" order by cdv.ordem ");
		
		return (List<ComposicaoCodigoVolume>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idClienteLayoutEdi", idClienteLayoutEdi);
}
	
	public List<Object[]> findNomeByIdClienteLayoutEdi(Long idClienteLayoutEdi) {
		StringBuilder hql = new StringBuilder()
		.append("select ca.campoTabela, ca.nmComplemento ")
		.append(" from ").append(ComposicaoLayoutEDI.class.getName()).append(" as co, ")
		.append(ComposicaoCodigoVolume.class.getName()).append(" as cdv, ")
		.append(CampoLayoutEDI.class.getName()).append(" as ca ")
		.append(" where co.campoLayout.idCampo = ca.idCampo ")
		.append(" and co.idComposicaoLayout = cdv.composicaoCampoEDI.idComposicaoLayout ")
		.append(" and co.idComposicaoLayout in ( select cdv.composicaoCampoEDI.idComposicaoLayout ")
		.append("                                from ").append(ComposicaoCodigoVolume.class.getName()).append(" as cdv, ")
		                                                .append(ComposicaoLayoutEDI.class.getName()).append(" as co ")
		.append("                                where cdv.composicaoLayoutEDI.idComposicaoLayout = co.idComposicaoLayout ")
		.append("                                and   co.clienteLayoutEDI.idClienteLayoutEDI = :idClienteLayoutEdi ) ")
		.append(" and cdv.composicaoLayoutEDI.idComposicaoLayout in ( select cdv.composicaoLayoutEDI.idComposicaoLayout ")
		.append("                                                     from ").append(ComposicaoCodigoVolume.class.getName()).append(" as cdv, ")
				                                                             .append(ComposicaoLayoutEDI.class.getName()).append(" as co ")
		.append("                                                     where cdv.composicaoLayoutEDI.idComposicaoLayout = co.idComposicaoLayout ")
		.append("                                                     and   co.clienteLayoutEDI.idClienteLayoutEDI = :idClienteLayoutEdi ) ")		
		.append(" order by cdv.ordem ");
		
				
		return (List<Object[]>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idClienteLayoutEdi", idClienteLayoutEdi);
	}
	
	
	public List findNomeCampoVolumeByIdClienteLayoutEdi(Long idClienteLayoutEdi) {
		StringBuilder hql = new StringBuilder()
		.append("select distinct ca.campoTabela ")
		.append(" from ").append(ComposicaoLayoutEDI.class.getName()).append(" as co, ")
		.append(ComposicaoCodigoVolume.class.getName()).append(" as cdv, ")
		.append(CampoLayoutEDI.class.getName()).append(" as ca ")
		.append(" where cdv.composicaoLayoutEDI.idComposicaoLayout = co.idComposicaoLayout ")
		.append(" and co.campoLayout.idCampo = ca.idCampo ")
		.append(" and co.clienteLayoutEDI.idClienteLayoutEDI = :idClienteLayoutEdi");
		
		return (List)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idClienteLayoutEdi", idClienteLayoutEdi);
	}	
	
}
