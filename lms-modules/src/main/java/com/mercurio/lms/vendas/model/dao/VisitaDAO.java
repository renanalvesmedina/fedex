package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.Visita;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VisitaDAO extends BaseCrudDao<Visita, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Visita.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa.enderecoPessoa", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa.enderecoPessoa.municipio", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);

		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);

		lazyFindById.put("usuarioByIdUsuario", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuario.vfuncionario", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuario.vfuncionario.filial", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuario.vfuncionario.filial.pessoa", FetchMode.JOIN);

		lazyFindById.put("usuarioByIdUsuarioVisto", FetchMode.JOIN);
		
		lazyFindById.put("pendencia", FetchMode.JOIN);
		lazyFindById.put("pendencia.ocorrencia", FetchMode.JOIN);
		
	}

	/**
	 * Consulta visita para grid
	 * @param idVisita
	 * @return
	 */
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = montaSqlPaginated((TypedFlatMap) criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = montaSqlPaginated(criteria);
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
	}

	/**
	 * Comando SQL de consulta de visitas
	 * @param idVisita
	 * @return
	 */
	private SqlTemplate montaSqlPaginated(TypedFlatMap parametros){

		Long idTipoVisita = parametros.getLong("tipoVisita.idTipoVisita");
		SqlTemplate sql = new SqlTemplate();

		//visita
		sql.addProjection("new Map(v.idVisita", "idVisita");
		sql.addProjection("v.dtVisita", "dtVisita");

		//cliente
		sql.addProjection("c.idCliente", "idCliente");
		sql.addProjection("p_c.nmPessoa", "nmPessoa");

		//usuario que fez a visita
		sql.addProjection("usu.idUsuario", "idUsuario");
		sql.addProjection("usu.nmUsuario", "nmUsuario");

		//tipo de visita
		if(idTipoVisita != null){
			sql.addProjection("tev.idTipoVisita", "idTipoVisita");
		}

		//filial
		sql.addProjection("f.idFilial", "idFilial");
		sql.addProjection("f.sgFilial", "sgFilial)");

		StringBuffer from = new StringBuffer(Visita.class.getName() + " v inner join v.cliente c inner join c.pessoa p_c ");
		if(idTipoVisita != null){
			from.append(" left outer join v.etapaVisitas ev left outer join ev.tipoVisita tev ");
		}
		from.append(" left outer join v.filial f inner join v.usuarioByIdUsuario usu inner join usu.vfuncionario vfun ");
		sql.addFrom(from.toString());

		sql.addCriteria("f.idFilial", "=", parametros.getLong("filial.idFilial"));
		sql.addCriteria("usu.idUsuario", "=", parametros.getLong("usuarioByIdUsuario.idUsuario"));
		sql.addCriteria("c.idCliente", "=", parametros.getLong("cliente.idCliente"));

		if(idTipoVisita != null){
			sql.addCriteria("ev.tipoVisita.idTipoVisita", "=", parametros.getLong("tipoVisita.idTipoVisita"));
		}

		sql.addCriteria("v.dtVisita", ">=", parametros.getYearMonthDay("dtVisitaInicial"));
		sql.addCriteria("v.dtVisita", "<=", parametros.getYearMonthDay("dtVisitaFinal"));

		sql.addCriteria("v.dtRegistro", ">=", parametros.getYearMonthDay("dtRegistroInicial"));
		sql.addCriteria("v.dtRegistro", "<=", parametros.getYearMonthDay("dtRegistroFinal"));

		sql.addOrderBy("usu.nmUsuario");
		sql.addOrderBy("f.sgFilial");
		sql.addOrderBy("p_c.nmPessoa");
		sql.addOrderBy("v.dtVisita");

		return sql;
	}

}
