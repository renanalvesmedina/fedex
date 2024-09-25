package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MeioTransporteRodoviarioDAO extends BaseCrudDao<MeioTransporteRodoviario, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return MeioTransporteRodoviario.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("operadoraMct",FetchMode.JOIN);
		lazyFindById.put("operadoraMct.pessoa",FetchMode.JOIN);
		lazyFindById.put("municipio",FetchMode.JOIN);
		lazyFindById.put("municipio.unidadeFederativa",FetchMode.JOIN);
		lazyFindById.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
		lazyFindById.put("meioTransporteRodoviario",FetchMode.JOIN);
		lazyFindById.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginated(criteria,false);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
				findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public List findLookupImpl(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria,false);
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	public ResultSetPage findPaginatedLookup(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginated(criteria,true);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
				findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria,false);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	public List findLookupWithProprietario(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria,false);
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	private SqlTemplate createHqlPaginated(TypedFlatMap criteria, boolean isLookup) {
		SqlTemplate sql = new SqlTemplate();
		StringBuffer selectClause = new StringBuffer();

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();

		selectClause.append(" new Map(MT.idMeioTransporte as idMeioTransporte, ")
		.append(" FILIAL.sgFilial as meioTransporte_filial_sgFilial, ")
		.append(" PESSOA.nmFantasia as meioTransporte_filial_pessoa_nmFantasia, ")
		.append(" MT.tpVinculo as meioTransporte_tpVinculo, ")
		.append(" TIPO.tpMeioTransporte as meioTransporte_modeloMeioTransporte_tipoMeioTransporte_tpMeioTransporte, ")
		.append(" TIPO.dsTipoMeioTransporte as meioTransporte_modeloMeioTransporte_tipoMeioTransporte_dsTipoMeioTransporte, ")
		.append(" TIPO.idTipoMeioTransporte as meioTransporte_modeloMeioTransporte_tipoMeioTransporte_idTipoMeioTransporte, ")
		.append(" MODELO.dsModeloMeioTransporte as meioTransporte_modeloMeioTransporte_dsModeloMeioTransporte, ")
		.append(" MT.idMeioTransporte as meioTransporte_idMeioTransporte, ")
		.append(" MT.nrFrota as meioTransporte_nrFrota, ")
		.append(" MT.nrIdentificador as meioTransporte_nrIdentificador, ")
		.append(" MARCA.dsMarcaMeioTransporte as meioTransporte_modeloMeioTransporte_marcaMeioTransporte_dsMarcaMeioTransporte, ")
		.append(" MT.nrAnoFabricao as meioTransporte_nrAnoFabricao, ")
		.append(" MT.tpSituacao as meioTransporte_tpSituacao, ")
		.append(" TIPO.tipoMeioTransporte.idTipoMeioTransporte as idTipoMeioComposto ");

		if (isLookup) {
			selectClause.append(",")
			.append(" MT.nrCapacidadeKg as meioTransporte_nrCapacidadeKg, ")
			.append(" MT.nrCapacidadeM3 as meioTransporte_nrCapacidadeM3, ")
			.append(" MT_RODO.nrRastreador as meioTransporte_meioTransporteRodoviario_nrRastreador, ")
			.append(" PESSOA.nmPessoa as meioTransporte_meioTransporteRodoviario_operadoraMct_pessoa_nmPessoa");
		}

		selectClause.append(") ");
		sql.addProjection(selectClause.toString());

		StringBuffer sb = new StringBuffer();
		sb.append(MeioTransporte.class.getName()).append(" as MT ");
		if (isLookup) {
			sb.append("inner join MT.meioTransporteRodoviario as MT_RODO ");
		}
		sb.append("inner join MT.modeloMeioTransporte as MODELO ")
			.append("inner join MODELO.marcaMeioTransporte as MARCA ")
			.append("inner join MODELO.tipoMeioTransporte as TIPO ")
			.append("inner join MT.filial as FILIAL ")
			.append(" left join FILIAL.pessoa as PESSOA ");

		Long idProprietario = criteria.getLong("proprietario.idProprietario");
		if (idProprietario != null) {
			sb.append("left join MT.meioTranspProprietarios as MTPROP ")
			.append("left join MTPROP.proprietario as PROP ");
		}

		sql.addFrom(sb.toString());

		sql.addCriteria("FILIAL.idFilial", "=", criteria.getLong("meioTransporte.filial.idFilial"));
		sql.addCriteria("MT.tpVinculo", "=", criteria.get("meioTransporte.tpVinculo"));
		sql.addCriteria("MT.tpModal", "=", criteria.get("meioTransporte.tpModal"));
		sql.addCriteria("TIPO.tpMeioTransporte", "=", criteria.get("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"));
		sql.addCriteria("TIPO.idTipoMeioTransporte", "=", criteria.getLong("meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"));

		String nrFrota = criteria.getString("meioTransporte.nrFrota");
		if (StringUtils.isNotEmpty(nrFrota)) {
			if (nrFrota.endsWith("%")) {
				nrFrota = nrFrota.substring(0,nrFrota.length()-1);
			}
			if(nrFrota.length() < 6){
				String filler = "000000";
				nrFrota = filler.substring(nrFrota.length())+ nrFrota;
			}
			sql.addCriteria("lower(MT.nrFrota)","like",nrFrota.toLowerCase());
		}

		String nrIdentificador = criteria.getString("meioTransporte.nrIdentificador");
		if (StringUtils.isNotEmpty(nrIdentificador)) {
			if (nrIdentificador.endsWith("%")) {
				nrIdentificador = nrIdentificador.substring(0,nrIdentificador.length()-1);
			}
			sql.addCriteria("lower(MT.nrIdentificador)","like",nrIdentificador.toLowerCase());
		}

		sql.addCriteria("MARCA.idMarcaMeioTransporte", "=", criteria.getLong("meioTransporte.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"));
		sql.addCriteria("MODELO.idModeloMeioTransporte","=", criteria.getLong("meioTransporte.modeloMeioTransporte.idModeloMeioTransporte"));
		sql.addCriteria("MT.nrAnoFabricao","=",criteria.getInteger("meioTransporte.nrAnoFabricao"));

		if (idProprietario != null && !idProprietario.equals("")) {
			sql.addCriteria("PROP.idProprietario","=",idProprietario);		
			sql.addCriteria("MTPROP.dtVigenciaInicial","<=",dataAtual);
			sql.addCriteria("MTPROP.dtVigenciaFinal",">=",dataAtual);
		}

		if (!StringUtils.isBlank((String)criteria.get("tpStatus"))) {		
			if (((String)criteria.get("tpStatus")).equals("B"))	{	
				sql.addCustomCriteria(" exists (from BloqueioMotoristaProp BLOQ " +
						"where BLOQ.meioTransporte.idMeioTransporte = MT.idMeioTransporte");
				sql.addCustomCriteria("BLOQ.dhVigenciaInicial.value <= ?",dataHoraAtual);
				sql.addCustomCriteria("BLOQ.dhVigenciaFinal.value >= ?)",dataHoraAtual);

			} else if (((String)criteria.get("tpStatus")).equals("L")) {		
				sql.addCustomCriteria(" not exists (from BloqueioMotoristaProp BLOQ " +
						"where BLOQ.meioTransporte.idMeioTransporte = MT.idMeioTransporte");
				sql.addCustomCriteria("BLOQ.dhVigenciaInicial.value <= ?",dataHoraAtual);
				sql.addCustomCriteria("BLOQ.dhVigenciaFinal.value >= ?)",dataHoraAtual);
			}
		}

		sql.addCriteria("MT.tpSituacao","=",criteria.get("meioTransporte.tpSituacao"));
		sql.addOrderBy("FILIAL.sgFilial, MT.tpVinculo, MT.nrIdentificador");

		return sql;
	}

	public Map findMeioTransporteToEstadoLiberacao(Long idMeioTransporte) {
		StringBuffer hql = new StringBuffer()
			.append("select new Map( ")
			.append(" MT.idMeioTransporte as idMeioTransporte, ")
			.append(" MT.dtVencimentoSeguro as dtVencimentoSeguro ) from ")
			.append(getPersistentClass().getName() + " MT ")
			.append(" where MT.idMeioTransporte = ? ");

		List l = getAdsmHibernateTemplate().find(hql.toString(),idMeioTransporte);

		return l.size() == 0 ? null : (Map)l.get(0);
	}

	/**
	 * Método que busca o MeioTransporteRodoviario com o ID do MeioTransporte
	 * @param idMeioTransporte
	 * @return
	 */
	public MeioTransporteRodoviario findMeioTransporteRodoviarioByIdMeioTransporte(Long idMeioTransporte) {
		DetachedCriteria dc = DetachedCriteria.forClass(MeioTransporteRodoviario.class);
		dc.add(Restrictions.eq("meioTransporte.id", idMeioTransporte));

		Criteria criteria = dc.getExecutableCriteria(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession());

		return (MeioTransporteRodoviario)criteria.uniqueResult();	
	}

	/**
	 * 
	 * @param idMeioTransporte
	 * @return
	 */
	public Map findDadosTipoMeioTransporte(Long idMeioTransporte) {
		StringBuffer sql = new StringBuffer()
			.append("select new Map( ")
			.append("mtr.idMeioTransporte as idMeioTransporte, ")
			.append("mtr.blControleTag as blControleTag, ")
			.append("etmt.qtEixos as qtEixos, ")
			.append("tmt.idTipoMeioTransporte as idTipoMeioTransporte) ")
			.append("from ")
			.append(getPersistentClass().getName() + " mtr ")
			.append("inner join mtr.eixosTipoMeioTransporte as etmt ")
			.append("left join etmt.tipoMeioTransporte as tmt ")
			.append("where mtr.id = ? ");
		
		List result = getAdsmHibernateTemplate().find(sql.toString(), idMeioTransporte);
		return result.isEmpty() ? null : (Map)result.get(0);
	}

}