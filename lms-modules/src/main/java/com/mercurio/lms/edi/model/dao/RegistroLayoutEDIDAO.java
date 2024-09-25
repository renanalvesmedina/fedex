package com.mercurio.lms.edi.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.RegistroLayoutEDI;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class RegistroLayoutEDIDAO extends BaseCrudDao<RegistroLayoutEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return RegistroLayoutEDI.class;
	}
		
	@SuppressWarnings("unchecked")
	public ResultSetPage<RegistroLayoutEDI> findPaginated(PaginatedQuery paginatedQuery) {
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		
		StringBuilder hql = new StringBuilder();
		hql.append("from RegistroLayoutEDI where 1=1 ");
		
		if(MapUtils.getString(criteria, "identificador") != null){
			hql.append(" and identificador like :identificador");
		}
		if(MapUtils.getString(criteria, "descricao") != null){
			hql.append(" and descricao like :descricao");
		}
		hql.append(" order by ordem asc ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery,hql.toString());	
	}
	
	public List<RegistroLayoutEDI> findFilhos(Long idLayoutEDI, Long idRegistroPai, String cabecalho){ 
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idLayout", idLayoutEDI);
		
		StringBuilder hql = new StringBuilder()
		.append("SELECT distinct rlEdi ")
		.append("from ")
		.append(ComposicaoLayoutEDI.class.getName())
		.append(" as clEdi ")
		.append("join clEdi.registroLayout rlEdi ")
		.append("where clEdi.layout.id = :idLayout ");
		
		
		if (idRegistroPai == null){
			if(cabecalho != null ){
				hql.append("   AND (rlEdi.nomeIdentificador = '"+cabecalho+"') ");
			}else{
				hql.append(" AND rlEdi.nomeIdentificador = 'REMETENTE' ");
			}
		}
					
		else {
			params.put("idRegistroPai", idRegistroPai);
			if(cabecalho != null ){
				hql.append(" AND rlEdi.idRegistroPai = :idRegistroPai ");
			}else{
				hql.append(" AND rlEdi.idRegistroPai = :idRegistroPai AND (rlEdi.nomeIdentificador != 'CABINTERC' AND rlEdi.nomeIdentificador != 'TOTAL' AND rlEdi.nomeIdentificador != 'CABDOCUMEN' )  ");
			}
		}
		
		hql.append("order by rlEdi.ordem asc");
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}
	
	public List<RegistroLayoutEDI> findDadosQuebraArquivo(Long idLayoutEDI){ 
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idLayoutEdi", idLayoutEDI);
		
		StringBuilder hql = new StringBuilder()
		.append("SELECT distinct rlEdi ")
		.append("from ")
		.append(ComposicaoLayoutEDI.class.getName())
		.append(" as clEdi ")
		.append("join clEdi.registroLayout rlEdi ")
		.append("where clEdi.layout.id = :idLayoutEdi  ")
		.append(" and (rlEdi.nomeIdentificador = 'REMETENTE' or rlEdi.nomeIdentificador = 'DESTINATAR' or rlEdi.nomeIdentificador = 'CABINTERC' or rlEdi.nomeIdentificador = 'TOTAL' or rlEdi.nomeIdentificador = 'CABDOCUMEN' ) ");
		
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}
	
	public List<RegistroLayoutEDI> findByIdLayoutEDI(Long idLayoutEDI){
		StringBuilder hql = new StringBuilder()

		.append("SELECT distinct rlEdi ")
		.append("from ")
		.append(ComposicaoLayoutEDI.class.getName())
		.append(" as clEdi ")
		.append("join clEdi.registroLayout rlEdi ")
		.append("where clEdi.layout.id = :idLayout ")
		.append("order by rlEdi.ordem asc ");
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idLayout", idLayoutEDI);
	}
	
	public Long findIdRegistroPrincipal(Long idLayoutEDI){
		StringBuilder hql = new StringBuilder()
		
		.append(" SELECT distinct rlEdi ")
		.append(" FROM ")
		.append(ComposicaoLayoutEDI.class.getName()) 
		.append(" as clEdi ")
		.append(" join clEdi.registroLayout rlEdi ")
		.append(" join clEdi.campoLayout calEdi ")
		.append(" WHERE calEdi.campoTabela like 'CNPJ_REME' ")
		.append("   AND clEdi.layout.id = :idLayout ")		
		.append("   AND rlEdi.nomeIdentificador != 'CABINTERC' AND rlEdi.nomeIdentificador != 'TOTAL' AND rlEdi.nomeIdentificador != 'CABDOCUMEN' ");
		

		List<RegistroLayoutEDI> rleList = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idLayout", idLayoutEDI);
		
		if (rleList != null && rleList.size() != 0)
			return rleList.get(0).getIdRegistroLayoutEdi();
		else
			return null;
		
	}
		 
	public Long findIdRegistroPrincipalPorTipo(Long idClienteLayoutEdi, String tipoLayout){						
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("tipoLayout", tipoLayout);
		params.put("idClienteLayoutEdi", idClienteLayoutEdi);		
		
		StringBuilder hql = new StringBuilder()
		.append("select distinct re ")
		.append(" from ").append(LayoutEDI.class.getName()).append(" as la, ")
		.append(ClienteLayoutEDI.class.getName()).append(" as cli, ")
		.append(ComposicaoLayoutEDI.class.getName()).append(" as co, ")
		.append(RegistroLayoutEDI.class.getName()).append(" as re ")
		.append(" where  la.idLayoutEdi = cli.layoutEDI.idLayoutEdi ")
		.append(" and    cli.layoutEDI.idLayoutEdi = co.layout.idLayoutEdi ")
		.append(" and    co.registroLayout.idRegistroLayoutEdi = re.idRegistroLayoutEdi ")
		.append(" and    cli.embarcadora in ( select ce.embarcadora ")
		.append("                             from ").append(ClienteLayoutEDI.class.getName()).append(" ce ")
		.append("                             where  ce.idClienteLayoutEDI = :idClienteLayoutEdi ) ")
		.append(" and    la.tpLayoutEdi = :tipoLayout ")
		.append(" and    re.idRegistroPai is null");		
		
		List<RegistroLayoutEDI> rleList = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);		
		
		if (rleList != null && rleList.size() != 0)			
			return rleList.get(0).getIdRegistroLayoutEdi();
		else
			return null;				 
	}
	
	
	public Long findIdRegistroPrincipalXml(Long idClienteLayoutEdi, String tipoLayout){						
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("tipoLayout", tipoLayout);
		params.put("idClienteLayoutEdi", idClienteLayoutEdi);		
		
		StringBuilder hql = new StringBuilder()
		.append("select distinct rle ")
		.append(" from ").append(RegistroLayoutEDI.class.getName()).append(" as rle ")
		.append(" where rle.idRegistroLayoutEdi in ( select re.idRegistroPai ")
		.append("  from ").append(LayoutEDI.class.getName()).append(" as la, ")
		.append(ClienteLayoutEDI.class.getName()).append(" as cli, ")
		.append(ComposicaoLayoutEDI.class.getName()).append(" as co, ")
		.append(RegistroLayoutEDI.class.getName()).append(" as re ")
		.append("  where  la.idLayoutEdi = cli.layoutEDI.idLayoutEdi ")
		.append("  and    cli.layoutEDI.idLayoutEdi = co.layout.idLayoutEdi ")
		.append("  and    co.registroLayout.idRegistroLayoutEdi = re.idRegistroLayoutEdi ")
		.append("  and    cli.idClienteLayoutEDI = :idClienteLayoutEdi ")
		.append("  and    la.tpLayoutEdi = :tipoLayout ) ")		
		.append(" and rle.idRegistroPai is null");
		
		List<RegistroLayoutEDI> rleList = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);		
		
		if (rleList != null && rleList.size() != 0)			
			return rleList.get(0).getIdRegistroLayoutEdi();
		else
			return null;				 
	}	
	
	
	public Long findClienteLayoutPorTipo(Long idClienteLayoutEdi, String tipoLayout){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("tipoLayout", tipoLayout);
		params.put("idClienteLayoutEdi", idClienteLayoutEdi);	
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT cli.id_cliente_edi_layout AS ID_CLIENTE_EDI_LAYOUT \n");
		sql.append("FROM cliente_edi_layout cli, \n");
		sql.append("  layout_edi la \n");
		sql.append("WHERE cli.ceid_cliente_edi_filial_embarc IN \n");
		sql.append("  (SELECT ce.ceid_cliente_edi_filial_embarc \n");
		sql.append("  FROM cliente_edi_layout ce \n");
		sql.append("  WHERE ce.id_cliente_edi_layout = :idClienteLayoutEdi \n");
		sql.append("  ) \n");
		sql.append("AND la.tp_layout          = :tipoLayout \n");
		sql.append("AND cli.laed_id_layout_edi=la.id_layout_edi");
		
		List rleList = getAdsmHibernateTemplate().findBySql(sql.toString(), params,new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("ID_CLIENTE_EDI_LAYOUT", Hibernate.LONG);
            }
        });
		
		if (rleList != null && rleList.size() != 0){
		    Object r = rleList.get(0);
            return (Long)r;
		}
		    
		else
			return null;		
		
	}
		
	public Long findLayoutEdiPorTipo(Long idClienteLayoutEdi, String tipoLayout){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("tipoLayout", tipoLayout);
		params.put("idClienteLayoutEdi", idClienteLayoutEdi);	
		
		StringBuilder hql = new StringBuilder()
		.append("select distinct la.idLayoutEdi ")
		.append(" from ").append(ClienteLayoutEDI.class.getName()).append(" as cli ")
		.append(" join cli.layoutEDI la ")
		.append(" where cli.clienteEDI in ( select ce.clienteEDI.idClienteEDI ")		
		.append("                           from ").append(ClienteLayoutEDI.class.getName()).append(" ce ")
		.append("                           where  ce.idClienteLayoutEDI = :idClienteLayoutEdi ) ")
		.append(" and   la.tpLayoutEdi = :tipoLayout ");
		
		List<Long> rleList = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		
		if (rleList != null && rleList.size() != 0)			
			return rleList.get(0);
		else
			return null;		
		
	}
	
	public List<RegistroLayoutEDI> findFilhosPorTipo(Long idLayoutEDI, Long idRegistroPai, String cabecalho, String...args){ 
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idLayout", idLayoutEDI);
		
		StringBuilder hql = new StringBuilder()
		.append("SELECT distinct rlEdi ")
		.append("from ")
		.append(ComposicaoLayoutEDI.class.getName())
		.append(" as clEdi ")
		.append("join clEdi.registroLayout rlEdi ")
		.append("where clEdi.layout.id = :idLayout ");
		
		
		if (idRegistroPai == null){
			if(cabecalho != null ){
				hql.append("   AND (rlEdi.nomeIdentificador = '"+cabecalho+"') ");
			}
		} else {
			params.put("idRegistroPai", idRegistroPai);
			hql.append(" AND rlEdi.idRegistroPai = :idRegistroPai ");
		}
		hql.append("order by rlEdi.ordem asc");
		
		if(args != null && args.length > 0 ){
			StringBuilder sb = new StringBuilder()
			.append(" select distinct rle ")
			.append(" from ").append(RegistroLayoutEDI.class.getName()).append(" as rle ")
			.append(" where rle.idRegistroLayoutEdi in ( select re.idRegistroPai ")
			.append("   from ").append(ComposicaoLayoutEDI.class.getName()).append(" as co, ")
			.append(RegistroLayoutEDI.class.getName()).append(" as re ")
			.append("  where  co.registroLayout.idRegistroLayoutEdi = re.idRegistroLayoutEdi ")
			.append("  and    co.layout.idLayoutEdi = :idLayout ) ")
			.append(" and rle.idRegistroPai is null ")
			.append(" union ");
			hql = sb.append(hql);
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}

	
	public Long findIdRegistroCabecalho(Long idLayoutEDI, String cabecalho){
		StringBuilder hql = new StringBuilder()
		
		.append(" SELECT distinct rlEdi ")
		.append(" FROM ")
		.append(ComposicaoLayoutEDI.class.getName()) 
		.append(" as clEdi ")
		.append(" join clEdi.registroLayout rlEdi ")
		.append(" join clEdi.campoLayout calEdi ")
		.append(" WHERE  ")
		.append("   clEdi.layout.id = :idLayout ");
		if(cabecalho == null || cabecalho.equals("")){
			hql.append("   AND rlEdi.nomeIdentificador != 'CABINTERC' AND rlEdi.nomeIdentificador != 'TOTAL' AND rlEdi.nomeIdentificador != 'CABDOCUMEN' ");
		}else{
			hql.append("   AND rlEdi.nomeIdentificador = '"+cabecalho+"'  ");
		}

		List<RegistroLayoutEDI> rleList = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idLayout", idLayoutEDI);
		
		if (rleList != null && rleList.size() != 0)
			return rleList.get(0).getIdRegistroLayoutEdi();
		else
			return null;
		
		 
	}

	public List<RegistroLayoutEDI> findLookupRegistro(Map<String, Object> params) {
		
		StringBuilder hql = new StringBuilder()
		
		.append(" from RegistroLayoutEDI ")
		.append(" where  identificador like :nrIdentificador ");
				
		return  getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "nrIdentificador", params.get("nrIdentificador"));
	}
}

