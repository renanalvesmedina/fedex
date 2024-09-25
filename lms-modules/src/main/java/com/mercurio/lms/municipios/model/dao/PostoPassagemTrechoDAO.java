package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.PostoPassagemTrecho;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PostoPassagemTrechoDAO extends BaseCrudDao<PostoPassagemTrecho, Long>{

	public void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filialOrigem", FetchMode.JOIN);
		fetchModes.put("filialOrigem.pessoa", FetchMode.JOIN);
		fetchModes.put("filialDestino", FetchMode.JOIN);
		fetchModes.put("filialDestino.pessoa", FetchMode.JOIN);
		fetchModes.put("postoPassagem", FetchMode.JOIN);
		fetchModes.put("postoPassagem.concessionaria", FetchMode.JOIN);
		fetchModes.put("postoPassagem.concessionaria.pessoa", FetchMode.JOIN);
		fetchModes.put("postoPassagem.municipio",FetchMode.JOIN);
		fetchModes.put("postoPassagem.municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("postoPassagem.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("postoPassagem.rodovia",FetchMode.JOIN);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = montaQueryPostoPassagemTrecho(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = montaQueryPostoPassagemTrecho(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}

	public SqlTemplate montaQueryPostoPassagemTrecho(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(pp.tpPostoPassagem as tpPostoPassagem, " +
				"nvl2(filOr.sgFilial,filOr.sgFilial||' - '||pesOr.nmFantasia,'') as filOrigem, " +
				"nvl2(filDest.sgFilial,filDest.sgFilial||' - '||pesDest.nmFantasia,'') as filDestino, " +
				"mun.nmMunicipio as nmMunicipio, " +
				"uf.sgUnidadeFederativa as sgUnidadeFederativa, " +
				""+PropertyVarcharI18nProjection.createProjection("pais.nmPais")+" as nmPais, " +
				"pp.tpSentidoCobranca as tpSentidoCobranca, " +
				"rodo.sgRodovia as sgRodovia, " +
				"pp.nrKm as nrKm, " +
				"pesConce.nmPessoa as concessionaria, " +
				"postoTrecho.dtVigenciaInicial as dtVigenciaInicial, " +
				"postoTrecho.dtVigenciaFinal as dtVigenciaFinal, " +
				"postoTrecho.idPostoPassagemTrecho as idPostoPassagemTrecho)");

		hql.addFrom(PostoPassagemTrecho.class.getName()+ " postoTrecho "+
				"join postoTrecho.filialOrigem filOr " +
				"join filOr.pessoa pesOr " +
				"join postoTrecho.filialDestino filDest " +
				"join filDest.pessoa pesDest " +
				"join postoTrecho.postoPassagem pp " +
				"join pp.municipio mun " +
				"join mun.unidadeFederativa uf " +
				"join uf.pais pais " +
				"left outer join pp.rodovia rodo " +
				"join pp.concessionaria conce " +
				"join conce.pessoa pesConce ");

		hql.addCriteria("filOr.idFilial","=", criteria.getLong("filialOrigem.idFilial"));
		hql.addCriteria("filDest.idFilial","=", criteria.getLong("filialDestino.idFilial"));
		hql.addCriteria("pp.idPostoPassagem","=", criteria.getLong("postoPassagem.idPostoPassagem"));
		hql.addCriteria("postoTrecho.dtVigenciaInicial",">=", criteria.getYearMonthDay("dtVigenciaInicial"));
		hql.addCriteria("postoTrecho.dtVigenciaFinal","<=", criteria.getYearMonthDay("dtVigenciaFinal"));

		hql.addOrderBy("filOr.sgFilial");
		hql.addOrderBy("filDest.sgFilial");
		hql.addOrderBy("mun.nmMunicipio");
		hql.addOrderBy("rodo.sgRodovia");
		hql.addOrderBy("postoTrecho.dtVigenciaInicial");

		return hql;
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PostoPassagemTrecho.class;
	}

	/**
	 * Conta o numero de postos de passagem existentes no trecho entre uma filial de origem e uma filial de destino
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param dtVigencia
	 * @return
	*/
	public Integer findQtdPostosPassagemByRotaFilialOrigemFilialDestino(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia){
		SqlTemplate sql = new SqlTemplate();	 

		sql.addProjection("count(*)");
		sql.addFrom(new StringBuffer("PostoPassagemTrecho ppt")
						.append(" inner join ppt.filialOrigem fo")
						.append(" inner join ppt.filialDestino fd").toString());		 
		sql.addCriteria("ppt.dtVigenciaInicial", "<=", dtVigencia);
		sql.addCriteria("ppt.dtVigenciaFinal", ">=", dtVigencia);
		sql.addCriteria("fo.idFilial", "=", idFilialOrigem);
		sql.addCriteria("fd.idFilial", "=", idFilialDestino);

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Andresa
	 * 
	 * Retorna os postos de passagem das rotas compreendidas pelas filiais enviadas por parametro 
	 * 
	 * @param filiais
	 * @return
	 */
	public List findPostosPassagemByFiliais(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtCalculo){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(pp.idPostoPassagem as idPostoPassagem)");

		hql.addFrom(PostoPassagemTrecho.class.getName()+ " ppt " +
				"join ppt.filialOrigem filOr " +
				"join ppt.filialDestino filDest " +
				"join ppt.postoPassagem pp ");

		hql.addCriteria("filOr.idFilial","=", idFilialOrigem);
		hql.addCriteria("filDest.idFilial","=", idFilialDestino);
		hql.addCriteria("ppt.dtVigenciaFinal",">=", dtCalculo);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public boolean findPostoPassagemVigenteByFiliais(PostoPassagemTrecho postoPassagemTrecho){
		DetachedCriteria dc = createDetachedCriteria();
	 	if(postoPassagemTrecho.getIdPostoPassagemTrecho() != null)
	 		dc.add(Restrictions.ne("idPostoPassagemTrecho", postoPassagemTrecho.getIdPostoPassagemTrecho()));

		dc.add(	Restrictions.eq("filialOrigem.id", postoPassagemTrecho.getFilialOrigem().getIdFilial()));
		dc.add(	Restrictions.eq("filialDestino.id", postoPassagemTrecho.getFilialDestino().getIdFilial()));
		dc.add(	Restrictions.eq("postoPassagem.id", postoPassagemTrecho.getPostoPassagem().getIdPostoPassagem()));

		dc.add(Restrictions.le("dtVigenciaInicial",postoPassagemTrecho.getDtVigenciaInicial()));
		dc.add(Restrictions.ge("dtVigenciaFinal",postoPassagemTrecho.getDtVigenciaFinal()));

		return findByDetachedCriteria(dc).size()>0;
	}
} 