package com.mercurio.lms.tabelaprecos.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.LimiteDesconto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LimiteDescontoDAO extends BaseCrudDao<LimiteDesconto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return LimiteDesconto.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("divisaoGrupoClassificacao", FetchMode.JOIN);
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		lazyFindById.put("parcelaPreco", FetchMode.JOIN);
		lazyFindById.put("subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition def) {

		StringBuilder projection = new StringBuilder();
		projection.append(" new map(ld.idLimiteDesconto as idLimiteDesconto,");
		projection.append(" dgc.dsDivisaoGrupoClassificacao as dsDivisaoGrupoClassificacao,");
		projection.append(" gc.dsGrupoClassificacao as dsGrupoClassificacao, fl.sgFilial as sgFilial,");
		projection.append(" usr.nmUsuario as nmUsuario,");
		projection.append(" pp.nmParcelaPreco as nmParcelaPreco, sttp.tpSubtipoTabelaPreco as tpSubtipoTabelaPreco,");
		projection.append(" ld.pcLimiteDesconto as pcLimiteDesconto, ld.tpIndicadorDesconto as tpIndicadorDesconto)");

		StringBuilder from = new StringBuilder();
		from.append(LimiteDesconto.class.getName()).append(" as ld");
		from.append(" left join ld.divisaoGrupoClassificacao as dgc");
		from.append(" left join dgc.grupoClassificacao as gc");
		from.append(" left join ld.filial as fl");
		from.append(" left join ld.parcelaPreco as pp");
		from.append(" left join ld.usuario as usr");
		from.append(" left join ld.subtipoTabelaPreco as sttp");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(projection.toString());

		sql.addFrom(from.toString());

		sql.addCriteria("dgc.id", "=", criteria.getLong("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao"));
		sql.addCriteria("fl.id", "=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("usr.id", "=", criteria.getLong("usuario.idUsuario"));
		sql.addCriteria("pp.id", "=", criteria.getLong("parcelaPreco.idParcelaPreco"));
		sql.addCriteria("pp.id", "=", criteria.getLong("parcelaPreco.idParcelaPreco"));
		sql.addCriteria("ld.tpTipoTabelaPreco", "=", criteria.getString("tpTipoTabelaPreco"));
		sql.addCriteria("sttp.id", "=", criteria.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"));
		sql.addCriteria("ld.tpIndicadorDesconto", "=", criteria.getString("tpIndicadorDesconto"));
		sql.addCriteria("ld.tpSituacao", "=", criteria.getString("tpSituacao"));

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("gc.dsGrupoClassificacao", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("dgc.dsDivisaoGrupoClassificacao", LocaleContextHolder.getLocale()));
		sql.addOrderBy("fl.sgFilial");
		sql.addOrderBy("usr.nmUsuario");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("pp.nmParcelaPreco", LocaleContextHolder.getLocale()));
		sql.addOrderBy("sttp.tpSubtipoTabelaPreco"); 

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),def.getCurrentPage(),def.getPageSize(),sql.getCriteria());  
	}

	public BigDecimal findPcLimiteDesconto(	String cdParcelaPreco,
											Long idParcelaPreco, 
											Long idSubtipoTabelaPreco, 
											String tpTipoTabelaPreco, 
											String tpIndicadorDesconto, 
											String tpSituacao, 
											Long idUsuario, 
											Long idFilial, 
											Long idDivisaoGrupoClassificacao, Long... idsPerfil) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ld")
		.setProjection(Projections.property("ld.pcLimiteDesconto"))
		.createAlias("ld.parcelaPreco", "pp")
		.add(Restrictions.eq("ld.subtipoTabelaPreco.id", idSubtipoTabelaPreco))
		.add(Restrictions.eq("ld.tpTipoTabelaPreco", tpTipoTabelaPreco))
		.add(Restrictions.or(Restrictions.eq("ld.tpIndicadorDesconto", tpIndicadorDesconto),Restrictions.isNull("ld.tpIndicadorDesconto")))
		.add(Restrictions.eq("ld.tpSituacao", tpSituacao));
		
		if (cdParcelaPreco != null) {
			dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));
		}
		if (idParcelaPreco != null) {
			dc.add(Restrictions.eq("pp.idParcelaPreco", idParcelaPreco));
		}
		if (idFilial != null) {
			dc.add(Restrictions.eq("ld.filial.id", idFilial));
		} else {
			dc.add(Restrictions.isNull("ld.filial"));
		}
		if (idUsuario != null) {
			dc.add(Restrictions.eq("ld.usuario.id", idUsuario));
		} else {
			dc.add(Restrictions.isNull("ld.usuario"));
		}
		if(idDivisaoGrupoClassificacao != null) {
			dc.add(Restrictions.eq("ld.divisaoGrupoClassificacao.id", idDivisaoGrupoClassificacao));
		} else {
			dc.add(Restrictions.isNull("ld.divisaoGrupoClassificacao"));
		}

		if(idsPerfil != null){
			if(idsPerfil.length > 0){
				dc.add(Restrictions.in("ld.perfil.id", idsPerfil));
			}else{
				dc.add(Restrictions.isNull("ld.perfil"));
			}
		}
				
		dc.addOrder(Order.asc("ld.tpIndicadorDesconto"));

		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty()) {
			return (BigDecimal)l.get(0);
		}
		return null;
	}

}