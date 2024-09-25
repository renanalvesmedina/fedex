package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.vendas.model.DocumentoCliente;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DocumentoClienteDAO extends BaseCrudDao<DocumentoCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return DocumentoCliente.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoDocumentoEntrega", FetchMode.JOIN);	 	
	}
	
	/**
	 * @author José Rodrigo Moraes
	 * @since  12/07/2006
	 * 
	 * RowCount criado para correto funcionamento junto ao findPaginatedTyped
	 * 
	 * @param tfm Critérios de pesquisa
	 * @return Quantidade de registros por página
	 */
	public Integer getRowCountTyped(TypedFlatMap tfm) {
		
		SqlTemplate sql = getHqlFindPaginated(tfm);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
		
	}
	
	/**
	 * @author José Rodrigo Moraes
	 * @since  12/07/2006
	 * 
	 * FindPaginated criado para correto uso do TypedFlatMap
	 * 
	 * @param tfm Critérios de pesquisa
	 * @return ResultSetPage com dados da pesquisa e dados de paginação
	 */
	public ResultSetPage findPaginatedTyped(TypedFlatMap tfm, FindDefinition findDef) {
		
		SqlTemplate sql = getHqlFindPaginated(tfm);
 		sql.addProjection("dc");
 		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
 		
	}

	private SqlTemplate getHqlFindPaginated(TypedFlatMap tfm) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(DocumentoCliente.class.getName() + " dc join fetch dc.tipoDocumentoEntrega as tde join dc.cliente as cl");
		sql.addFrom(DomainValue.class.getName() + " dv join dv.domain do");
		sql.addFrom(DomainValue.class.getName() + " dv2 join dv2.domain do2");			
		sql.addJoin("dc.tpModal","dv.value");
		sql.addJoin("dc.tpAbrangencia","dv2.value");			

		sql.addCriteria("do.name","=","DM_MODAL");
		sql.addCriteria("do2.name","=","DM_ABRANGENCIA");

		sql.addCriteria("cl.idCliente", "=", tfm.getLong("cliente.idCliente"));
		sql.addCriteria("tde.idTipoDocumentoEntrega", "=", tfm.getLong("tipoDocumentoEntrega.idTipoDocumentoEntrega"));
		sql.addCriteria("dc.tpModal", "like", tfm.getDomainValue("tpModal").getValue());
		sql.addCriteria("dc.tpAbrangencia", "like", tfm.getDomainValue("tpAbrangencia").getValue());			

		Boolean blFaturaVinculada = tfm.getBoolean("blFaturaVinculada");
		if(blFaturaVinculada != null) {
			sql.addCriteria("dc.blFaturaVinculada", "like", blFaturaVinculada);				
		}

		if (tfm.getYearMonthDay("dtVigencia") != null){
			sql.add(" AND (( ? BETWEEN dc.dtVigenciaInicial AND dc.dtVigenciaFinal) OR (dc.dtVigenciaInicial <= ? AND dc.dtVigenciaFinal is null))\n");
			YearMonthDay dtVigencia = tfm.getYearMonthDay("dtVigencia");
			
			sql.addCriteriaValue(dtVigencia);
			sql.addCriteriaValue(dtVigencia);
		}

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv2.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tde.dsTipoDocumentoEntrega", LocaleContextHolder.getLocale()));
		sql.addOrderBy("dc.dtVigenciaInicial");
		return sql;
	}

	public List findByCliente(Long idCliente, String tpModal, String tpAbrangencia, YearMonthDay dtVigencia) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dc");
		dc.setProjection(Projections.alias(Projections.property("tde.dsTipoDocumentoEntrega"), "tipoDocumentoEntrega.dsTipoDocumentoEntrega"));

		dc.createAlias("dc.tipoDocumentoEntrega", "tde");

		dc.add(Restrictions.eq("dc.cliente.id", idCliente));
		dc.add(Restrictions.eq("dc.tpModal", tpModal));
		dc.add(Restrictions.eq("dc.tpAbrangencia", tpAbrangencia));
		dc.add(Restrictions.le("dc.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("dc.dtVigenciaFinal", dtVigencia));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));

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