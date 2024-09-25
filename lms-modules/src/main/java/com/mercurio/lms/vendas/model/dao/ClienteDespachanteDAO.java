package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.ClienteDespachante;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ClienteDespachanteDAO extends BaseCrudDao<ClienteDespachante, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ClienteDespachante.class;
	}

	/**
	 * Método responsável pelo detalhamento de um ClienteDespachante.
	 * 
	 * @param id do cliente despachante
	 * @return retorna um map ESPECÍFICO por a tela
	 * 
	 * @author Mickaël Jalbert
	 */
	public Map findByIdSpecificTela(Long id) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(cd.idClienteDespachante as idClienteDespachante, cd.tpLocal as tpLocal, cd.tpSituacao as tpSituacao, "
						+ "pe.nrIdentificacao as despachante_pessoa_nrIdentificacao, pe.tpIdentificacao as despachante_pessoa_tpIdentificacao,"
						+ "pe.nmPessoa as despachante_pessoa_nmPessoa, pe.idPessoa as despachante_pessoa_idPessoa, pe.tpPessoa as despachante_pessoa_tpPessoa, de.idDespachante as despachante_idDespachante)");
		sql.addFrom(ClienteDespachante.class.getName()
						+ " cd join cd.despachante as de join de.pessoa as pe join cd.cliente as cl");
		sql.addCriteria("cd.idClienteDespachante", "=", id);
		List despachantes = getAdsmHibernateTemplate().find(sql.getSql(),
				sql.getCriteria());

		Map despachante = (Map) despachantes.get(0);

		List list = new ArrayList();

		list.add(despachante);

		list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);

		return (Map) list.get(0);
	}

	/**
	 * Método responsável pela listagem da tela de cadastro de cliente
	 * despachante.
	 * 
	 * @param criteria filtros da tela
	 * @return retorna uma lista ESPECÍFICA paginada dos clientes despachante.
	 * 
	 * @author Mickaël Jalbert
	 */
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map(cd.idClienteDespachante as idClienteDespachante, cd.tpLocal as tpLocal, cd.tpSituacao as tpSituacao,"
						+ "pe.nrIdentificacao as nrIdentificacao, pe.nmPessoa as nmPessoa, pe.idPessoa as idPessoa, pe.tpPessoa as tpPessoa)");
		sql.addFrom(ClienteDespachante.class.getName()
						+ " cd join cd.despachante as de join de.pessoa as pe join cd.cliente as cl");
		sql.addCriteria("cl.idCliente", "=", (String) ReflectionUtils.getNestedBeanPropertyValue(criteria, "cliente.idCliente"), Long.class);
		sql.addCriteria("de.idDespachante", "=", (String) ReflectionUtils.getNestedBeanPropertyValue(criteria, "despachante.idDespachante"), Long.class);
		sql.addCriteria("cd.tpSituacao", "=", (String) criteria.get("tpSituacao"));
		sql.addCriteria("cd.tpLocal", "=", (String) criteria.get("tpLocal"));
		sql.addOrderBy("pe.nmPessoa");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(),
				findDef.getPageSize(), sql.getCriteria());
		return rsp;
	}

	public List findAll(){
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("tpLocal"), "tpLocal")
		.add(Projections.property("dcip.idDespachante"), "despachante_idDespachante")
		.add(Projections.property("dcipp.idPessoa"), "pessoa_idPessoa")
		.add(Projections.property("dcipp.nmPessoa"), "pessoa_nmPessoa")
		.add(Projections.property("dcipp.tpPessoa"), "pessoa_tpPessoa")
		.add(Projections.property("dcippep.idEnderecoPessoa"), "pessoa_enderecoPessoa_idEnderecoPessoa")
		;

		DetachedCriteria dc = createDetachedCriteria()
		.setProjection(pl)
		.createAlias("despachante", "dcip")
		.createAlias("dcip.pessoa", "dcipp")
		.createAlias("dcipp.enderecoPessoa", "dcippep")
		.setResultTransformer(new AliasToNestedMapResultTransformer())
		;

		return findByDetachedCriteria(dc);
	}

	public List findByIdCliente(Long id){
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("cd.idClienteDespachante"), "idClienteDespachante")
		.add(Projections.property("cd.tpLocal"), "tpLocal")
		.add(Projections.property("cdd.idDespachante"), "despachante.idDespachante")
		.add(Projections.property("cddp.idPessoa"), "despachante.pessoa.idPessoa")
		.add(Projections.property("cddp.nmPessoa"), "despachante.pessoa.nmPessoa")
		.add(Projections.property("cddp.tpPessoa"), "despachante.pessoa.tpPessoa")
		.add(Projections.property("cddp.tpPessoa"), "despachante.pessoa.tpPessoa")
		.add(Projections.property("cddpe.idEnderecoPessoa"), "despachante.pessoa.enderecoPessoa.idEnderecoPessoa")
		;

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cd")
		.createAlias("cd.cliente", "cdc")
		.createAlias("cd.despachante", "cdd")
		.createAlias("cdd.pessoa", "cddp")
		.createAlias("cddp.enderecoPessoa", "cddpe")
		.add(Restrictions.eq("cdc.id", id))
		.setProjection(pl)
		.addOrder(Order.asc("cddp.nmPessoa"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()))
		;

		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		StringBuilder hql = new StringBuilder()
		.append(" DELETE ").append(getPersistentClass().getName())
		.append(" WHERE cliente.id = :id");

		getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
	}
}