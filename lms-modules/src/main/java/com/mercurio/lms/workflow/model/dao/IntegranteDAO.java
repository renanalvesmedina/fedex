package com.mercurio.lms.workflow.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.workflow.model.Integrante;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class IntegranteDAO extends BaseCrudDao<Integrante, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Integrante.class;
	}

	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("count (int.id)");
		sql.addFrom(Integrante.class.getName()+" int " +
				"left outer join int.usuario us " +
				"left outer join int.perfil per " +
				"join int.comite com");
		sql.addCriteria("com.id","=", ((Map)criteria.get("comite")).get("idComite"), Long.class);
		sql.addCriteria("per.id","=", ((Map)criteria.get("perfil")).get("idPerfil"), Long.class);
		sql.addCriteria("us.id","=", ((Map)criteria.get("usuario")).get("idUsuario"), Long.class);
		sql.addCriteria("int.nrOrdemAprovacao","=", criteria.get("nrOrdemAprovacao"), Byte.class);
		sql.addCriteria("int.tpSituacao","=", criteria.get("tpSituacao"));
		Long result = (Long)this.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("int");
		sql.addFrom(Integrante.class.getName()+" int " +
				"left outer join fetch int.usuario us " +
				"left outer join fetch int.perfil per " +
				"join fetch int.comite com");
		sql.addCriteria("com.id","=", ((Map)criteria.get("comite")).get("idComite"), Long.class);
		sql.addCriteria("per.id","=", ((Map)criteria.get("perfil")).get("idPerfil"), Long.class);
		sql.addCriteria("us.id","=", ((Map)criteria.get("usuario")).get("idUsuario"), Long.class);
		sql.addCriteria("int.nrOrdemAprovacao","=", criteria.get("nrOrdemAprovacao"), Byte.class);
		sql.addCriteria("int.tpSituacao","=", criteria.get("tpSituacao"));  
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("com.nmComite", LocaleContextHolder.getLocale()));
		sql.addOrderBy("per.dsPerfil");
		sql.addOrderBy("us.nmUsuario");
		sql.addOrderBy("int.nrOrdemAprovacao");
		return this.getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("comite",FetchMode.JOIN);
		lazyFindById.put("perfil",FetchMode.JOIN);
		lazyFindById.put("usuario",FetchMode.JOIN);
	}

	/**
	 * Retorna a lista de integrantes do comité informado.
	 * @param Long idComite
	 * @param Long idFilial
	 * @return List
	 * */
	public List findIntegrantesByComite(Long idComite, Long idFilial, Long idEmpresa) {
		StringBuilder hql = new StringBuilder()
		.append(" select distinct int")
		.append("   from ").append(Integrante.class.getName()).append(" as int")
		.append("   left outer join fetch int.usuario as us")
		.append("   left outer join us.empresaUsuario as eui")
		.append("   left outer join eui.filiaisUsuario as fuu")
		.append("  	left outer join eui.regionalUsuario as reu")
		.append("	left outer join reu.regional.regionalFiliais as ref")
		.append("   left outer join fetch int.perfil as pe")
		.append("   left outer join pe.perfilUsuarios as pu")
		.append("   left outer join pu.usuario as usp")
		.append("   left outer join usp.empresaUsuario as eup")
		.append("   left outer join eup.filiaisUsuario as fup")
		.append("  	left outer join eup.regionalUsuario as rep")
		.append("	left outer join rep.regional.regionalFiliais as refp")
		.append("   join int.comite com")
		.append("    where com.id = ?")
		.append("      and int.tpSituacao = ?")
		.append("      and ( ( fuu.filial.id = ?")
		.append("          and fuu.blAprovaWorkflow = ?")
		.append("          and eui.empresa.id = ?")
		.append("        )")
		.append("        or ( fup.filial.id = ?")
		.append("         and fup.blAprovaWorkflow = ?")
		.append("         and eup.empresa.id = ?")
		.append("        )")
		.append("        or ( ref.filial.id = ?")
		.append("		  and (ref.dtVigenciaInicial <= sysdate and ref.dtVigenciaFinal >= sysdate) ")
		.append("         and reu.blAprovaWorkflow = ?")
		.append("         and eui.empresa.id = ?")
		.append("        )")
		.append("        or ( eui.blIrrestritoFilial = ?")
		.append("         and eui.empresa.id = ?")
		.append("        )")
		.append("        or ( eup.blIrrestritoFilial = ?")
		.append("         and eup.empresa.id = ?")
		.append("        )")
		.append("        or ( refp.filial.id = ?")
		.append("		  and (refp.dtVigenciaInicial <= sysdate and refp.dtVigenciaFinal >= sysdate) ")
		.append("         and rep.blAprovaWorkflow = ?")
		.append("         and eup.empresa.id = ?")
		.append("        )")
		.append("      )")
		.append(" order by int.nrOrdemAprovacao asc");

		Object[] paramValues = new Object[]{
			idComite,
			"A",
			idFilial,
			Boolean.TRUE,
			idEmpresa,
			idFilial,
			Boolean.TRUE,
			idEmpresa,
			idFilial,
			Boolean.TRUE,
			idEmpresa,
			Boolean.TRUE,
			idEmpresa,
			Boolean.TRUE,
			idEmpresa,
			idFilial,
			Boolean.TRUE,
			idEmpresa,
		};
		return this.getAdsmHibernateTemplate().find(hql.toString(), paramValues);
	}
}