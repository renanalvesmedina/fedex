package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.ClienteRegiao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ClienteRegiaoDAO extends BaseCrudDao<ClienteRegiao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ClienteRegiao.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("municipio", FetchMode.SELECT);
		lazyFindById.put("tipoLocalizacaoMunicipio", FetchMode.SELECT);
		lazyFindById.put("municipio.unidadeFederativa", FetchMode.SELECT);
	}

	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append(ClienteRegiao.class.getName()).append(" as cr");
		sqlFrom.append(" join fetch cr.municipio as mu");
		sqlFrom.append(" join cr.cliente as cl");
		sqlFrom.append(" join fetch mu.unidadeFederativa as uf");
		sqlFrom.append(" left join fetch cr.tipoLocalizacaoMunicipio as ti");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("cr");
		sql.addFrom(sqlFrom.toString());

		sql.addCriteria("cl.idCliente", "=", (String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"cliente.idCliente"),Long.class);
		sql.addCriteria("mu.idMunicipio", "=", (String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"municipio.idMunicipio"),Long.class);
		sql.addCriteria("ti.idTipoLocalizacaoMunicipio", "=", (String) ReflectionUtils.getNestedBeanPropertyValue(criteria,"tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio"),Long.class);
		sql.addCriteria("cr.tpModal", "like", (String) criteria.get("tpModal"));
		sql.addOrderBy("mu.nmMunicipio");
		sql.addOrderBy("uf.nmUnidadeFederativa");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("ti.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

 		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
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