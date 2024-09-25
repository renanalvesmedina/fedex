package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.AnexoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransportePeriferico;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MeioTransporteDAO extends BaseCrudDao<MeioTransporte, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return MeioTransporte.class;
	}

	protected void initFindListLazyProperties(Map fetchModes) {
		fetchModes.put("meioTransporteRodoviario",FetchMode.JOIN);
		fetchModes.put("meioTransporteRodoviario.meioTransporte",FetchMode.JOIN);
		fetchModes.put("filial",FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filial",FetchMode.JOIN);
		lazyFindPaginated.put("filial.pessoa",FetchMode.JOIN);

		lazyFindPaginated.put("modeloMeioTransporte",FetchMode.JOIN);
		lazyFindPaginated.put("modeloMeioTransporte.tipoMeioTransporte",FetchMode.JOIN);
		lazyFindPaginated.put("modeloMeioTransporte.marcaMeioTransporte",FetchMode.JOIN);
		lazyFindPaginated.put("modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filial",FetchMode.JOIN);
		lazyFindById.put("filial.pessoa",FetchMode.JOIN);

		lazyFindById.put("filialAgregadoCe",FetchMode.JOIN);
		lazyFindById.put("filialAgregadoCe.pessoa",FetchMode.JOIN);

		lazyFindById.put("modeloMeioTransporte",FetchMode.JOIN);
		lazyFindById.put("modeloMeioTransporte.tipoMeioTransporte",FetchMode.JOIN);
		lazyFindById.put("modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte",FetchMode.JOIN);
		lazyFindById.put("modeloMeioTransporte.marcaMeioTransporte",FetchMode.JOIN);

		lazyFindById.put("fotoMeioTransportes",FetchMode.SELECT);
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("modeloMeioTransporte",FetchMode.JOIN);
		lazyFindLookup.put("modeloMeioTransporte.marcaMeioTransporte",FetchMode.JOIN);
		lazyFindLookup.put("modeloMeioTransporte.tipoMeioTransporte",FetchMode.JOIN);
		lazyFindLookup.put("meioTransporteRodoviario",FetchMode.JOIN);
	}

	public MeioTransporte findByIdCustom(Long id) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("MT");
		StringBuilder hqlFrom = new StringBuilder()
		.append(MeioTransporte.class.getName()).append(" as MT ")
		.append("inner join fetch MT.filial as F ")
		.append("inner join fetch F.pessoa ")
		.append(" left join fetch MT.filialAgregadoCe as FAG ")
		.append(" left join fetch FAG.pessoa ")
		.append("inner join fetch MT.usuarioAtualizacao ")
		.append("inner join fetch MT.modeloMeioTransporte as MOD ")
		.append("inner join fetch MOD.marcaMeioTransporte ")
		.append("inner join fetch MOD.tipoMeioTransporte as TMT ")
		.append(" left join fetch TMT.tipoMeioTransporte ")
		.append(" left join fetch MT.meioTransporteRodoviario as MTR ")
		.append(" left join fetch MTR.eixosTipoMeioTransporte ")
		.append(" left join fetch MTR.operadoraMct OP ")
		.append(" left join fetch OP.pessoa")
		.append(" left join fetch MTR.meioTransporteRodoviario as MTRC ")
		.append(" left join fetch MTRC.meioTransporte ")
		.append(" left join fetch MTR.municipio as MUN ")
		.append(" left join fetch MUN.unidadeFederativa as UF ")
		.append(" left join fetch UF.pais as PA ")
		.append(" left join fetch MT.fotoMeioTransportes ")
		.append(" left join fetch MT.pendencia pe")
		.append(" left join fetch pe.ocorrencia oc")
		.append(" left join fetch oc.eventoWorkflow ev")
		.append(" left join fetch ev.tipoEvento tpe");
		
		hql.addFrom(hqlFrom.toString());

		hql.addCustomCriteria("MT.id = ?",id);

		return (MeioTransporte)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
				findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	public List findLookupWithProprietario(TypedFlatMap criteria) {
		SqlTemplate hql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	private SqlTemplate createHqlPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();

		sql.addProjection("new Map(MT.idMeioTransporte","idMeioTransporte");
		sql.addProjection("FILIAL.sgFilial","filial_sgFilial");
		sql.addProjection("PESSOA.nmFantasia","filial_pessoa_nmFantasia");
		sql.addProjection("MT.tpVinculo","tpVinculo");
		sql.addProjection("TIPO.idTipoMeioTransporte","modeloMeioTransporte_tipoMeioTransporte_idTipoMeioTransporte");
		sql.addProjection("TIPO.tpMeioTransporte","modeloMeioTransporte_tipoMeioTransporte_tpMeioTransporte");
		sql.addProjection("TIPO.dsTipoMeioTransporte","modeloMeioTransporte_tipoMeioTransporte_dsTipoMeioTransporte");
		sql.addProjection("MT.nrCapacidadeKg","nrCapacidadeKg");
		sql.addProjection("MT.nrFrota","nrFrota");
		sql.addProjection("MT.nrIdentificador","nrIdentificador");
		sql.addProjection("MARCA.dsMarcaMeioTransporte","modeloMeioTransporte_marcaMeioTransporte_dsMarcaMeioTransporte");
		sql.addProjection("MODELO.dsModeloMeioTransporte","modeloMeioTransporte_dsModeloMeioTransporte");
		sql.addProjection("MT.nrAnoFabricao","nrAnoFabricao");
		sql.addProjection("MT.nrCodigoBarra","nrCodigoBarra");
		sql.addProjection("MT_RODO.nrRastreador","meioTransporteRodoviario_nrRastreador");
		sql.addProjection("MT.tpSituacao","tpSituacao)");

		StringBuffer strFrom = new StringBuffer();
		strFrom.append(MeioTransporte.class.getName()).append(" as MT ")
		.append("inner join MT.modeloMeioTransporte as MODELO ")
		.append("inner join MODELO.marcaMeioTransporte as MARCA ")
		.append("inner join MODELO.tipoMeioTransporte as TIPO ")
		.append("inner join MT.filial as FILIAL ")
		.append(" left join MT.meioTransporteRodoviario as MT_RODO ")
		.append(" left join FILIAL.pessoa as PESSOA ");

		Long idProprietario = criteria.getLong("proprietario.idProprietario");
		if (idProprietario != null) {
			strFrom.append("left join MT.meioTranspProprietarios as MTPROP ")
			.append("left join MTPROP.proprietario	as PROP ");
		}
		sql.addFrom(strFrom.toString());				

		sql.addCriteria("FILIAL.idFilial","=",criteria.getLong("filial.idFilial"));
		sql.addCriteria("MT.tpVinculo","=",criteria.get("tpVinculo"));
		sql.addCriteria("MT.tpModal","=",criteria.get("tpModal"));
		sql.addCriteria("TIPO.tpMeioTransporte","=",criteria.get("modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"));
		sql.addCriteria("TIPO.idTipoMeioTransporte","=",criteria.getLong("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"));
		sql.addCriteria("MT.tipoCombustivel.id","=",criteria.getLong("tipoCombustivel.idTipoCombustivel"));

		sql.addCriteria("MT.nrFrota", "=", FormatUtils.formatNrFrota(criteria.getString("nrFrota")));

		String nrIdentificador = criteria.getString("nrIdentificador");
		if(StringUtils.isNotEmpty(nrIdentificador)) {
			sql.addCriteria("lower(MT.nrIdentificador)","like",nrIdentificador.toLowerCase());
		}
		
		sql.addCriteria("MARCA.idMarcaMeioTransporte","=",criteria.getLong("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"));
		sql.addCriteria("MODELO.idModeloMeioTransporte","=",criteria.getLong("modeloMeioTransporte.idModeloMeioTransporte"));
		sql.addCriteria("MT.nrAnoFabricao","=",criteria.getShort("nrAnoFabricao"));
		
		sql.addCriteria("MT.nrCodigoBarra","=",criteria.getLong("nrCodigoBarra"));
		
		if (idProprietario != null) {
			sql.addCriteria("PROP.idProprietario","=",idProprietario);	
			sql.addCriteria("MTPROP.dtVigenciaInicial","<=",dataAtual);
			sql.addCriteria("MTPROP.dtVigenciaFinal",">=",dataAtual);
		}

		if (!StringUtils.isBlank((String)criteria.get("tpSituacao"))) {
			sql.addCriteria("MT.tpSituacao","=",criteria.getString("tpSituacao"));
		}

		//Range da data de atualização do registro
		sql.addCriteria("MT.dtAtualizacao", ">=", criteria.getYearMonthDay("dtAtualizacaoInicial"));
		if (criteria.getYearMonthDay("dtAtualizacaoFinal") != null)
			sql.addCriteria("MT.dtAtualizacao", "<", criteria.getYearMonthDay("dtAtualizacaoFinal").plusDays(1));

		if (!StringUtils.isBlank((String)criteria.get("tpStatus"))) {
			if (((String)criteria.get("tpStatus")).equals("B"))	{	
				sql.addCustomCriteria(" exists (from BloqueioMotoristaProp BLOQ " +
						"where BLOQ.meioTransporte.idMeioTransporte = MT.idMeioTransporte");
				sql.addCustomCriteria("BLOQ.dhVigenciaInicial.value <= ?", dataHoraAtual);
				sql.addCustomCriteria("BLOQ.dhVigenciaFinal.value >= ?)",dataHoraAtual);
			} else if (((String)criteria.get("tpStatus")).equals("L")) {		
				sql.addCustomCriteria(" not exists (from BloqueioMotoristaProp BLOQ " +
						"where BLOQ.meioTransporte.idMeioTransporte = MT.idMeioTransporte");
				sql.addCustomCriteria("BLOQ.dhVigenciaInicial.value <= ?",dataHoraAtual);
				sql.addCustomCriteria("BLOQ.dhVigenciaFinal.value >= ?)",dataHoraAtual);
			}
		}

		sql.addOrderBy("case when MT.tpVinculo = 'P' then 1 " +
				" when MT.tpVinculo = 'A' then 2 " +
				" when MT.tpVinculo = 'E' then 3 " +
				" else 4 end");
		sql.addOrderBy("MT.nrFrota");

		return sql;
	}

	public MeioTransporte findMeioTransporteByIdentificacao(String nrIdentificacao) {
		Validate.notEmpty(nrIdentificacao, "nrIdentificacao cannot be null");

		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("modeloMeioTransporte", "mmt");
		dc.add(Restrictions.ilike("nrIdentificador", nrIdentificacao));

		return (MeioTransporte)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List<MeioTransporte> findMeioTransporteByCodigoBarra(final Long nrCodigoBarra, final Long idMeioTransporte) {
		if(idMeioTransporte != null){
			return getAdsmHibernateTemplate().findByCriteria(
															  DetachedCriteria.forClass(MeioTransporte.class)
															  				  .add(Restrictions.eq("nrCodigoBarra", nrCodigoBarra))
															  				  .add(Restrictions.ne("idMeioTransporte", idMeioTransporte))
															);
			
		} else {
			return getAdsmHibernateTemplate().findByCriteria(
																DetachedCriteria.forClass(MeioTransporte.class)
																				.add(Restrictions.eq("nrCodigoBarra", nrCodigoBarra))
															);
		}
	}

	/**
	 * Consulta Meio de transporte cujo Tipo de Vínculo for igual a Agregado ou Eventual 
	 * 
	 * @author Felipe Ferreira
	 * @return String último número de frota atribuído, null se não encontrar nenhum registro.
	 */
	public String getProximoNrFrota() {
		DetachedCriteria dc = DetachedCriteria.forClass(MeioTransporte.class);
		dc.setProjection(Projections.max("nrFrota"));
		dc.add(Restrictions.or(
				Restrictions.eq("tpVinculo", new DomainValue("A")),
				Restrictions.eq("tpVinculo", new DomainValue("E"))
			));

		List<String> l = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		if (l.get(0) == null)
			return null;
		else
			return l.get(0);
	}

	/**
	 * Retorna o número de meios de transportes de acordo com o tipo de vínculo informado.
	 * 
	 * autor Samuel Herrmann
	 * @param type
	 * @return Long
	 */
	public Long getRowCountByType(String tpVinculo, Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mt");
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("mt.filial.id", idFilial));
		dc.add(Restrictions.eq("mt.tpVinculo", tpVinculo));
		dc.add(Restrictions.eq("mt.tpSituacao", "A"));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return Long.valueOf(result.longValue());
	}

	/**
	 * Verifica se meio de transporte possui proprietário e liberação cadastrados e vigentes.
	 * 
	 * @param idMeioTransporte
	 * @param rodoviario
	 * @return true se possuir
	 */
	public boolean verificaSituacaoMeioTransporte(Long idMeioTransporte, boolean rodoviario) {
		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setProjection(Projections.rowCount());
		dc.createAlias("meioTranspProprietarios","props");
		dc.add(Restrictions.eq("id", idMeioTransporte));
		dc.add(Restrictions.le("props.dtVigenciaInicial", dtToday));
		dc.add(Restrictions.ge("props.dtVigenciaFinal", dtToday));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}
	
	
	public MeioTranspProprietario findProprietario(Long idMeioTransporte) {
		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();
		DetachedCriteria dc = DetachedCriteria.forClass(MeioTranspProprietario.class);
		dc.createAlias("proprietario","prop");
		dc.createAlias("prop.pessoa","pess");
		dc.createAlias("meioTransporte","meio");
		dc.add(Restrictions.eq("meio.idMeioTransporte", idMeioTransporte));
		dc.add(Restrictions.le("dtVigenciaInicial", dtToday));
		dc.add(Restrictions.ge("dtVigenciaFinal", dtToday));

		MeioTranspProprietario meio = (MeioTranspProprietario) getAdsmHibernateTemplate().findUniqueResult(dc);
		return meio;
	}
	

	/**
	 * Verifica se meio de transporte possui estado incompleto.
	 * 
	 * @param idMeioTransporte
	 * @param rodoviario
	 * @return true se possuir
	 */
	public boolean verificaSituacaoMeioTransporteForProprietarios(Long idMeioTransporte) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mt");
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("id", idMeioTransporte));
		dc.add(Restrictions.eq("tpSituacao", "N"));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	public List<Map<String, Object>> findInfoMeioTransporte(Long idMeioTransporte) {
		StringBuffer hql = new StringBuffer();
		hql.append("select new Map(")
		.append(" MT as meioTransporte, ")
		.append(" RODO.nrRastreador as nrRastreador,")
		.append(" MODELO.dsModeloMeioTransporte as dsModelo, ")
		.append(" TIPO.dsTipoMeioTransporte as dsTipo, ")
		.append(" MARCA.dsMarcaMeioTransporte as dsMarca, ")
		.append(" PESSOA.nmPessoa as nmOperadora, ")
		.append(" TIPO.tipoMeioTransporte.idTipoMeioTransporte as idTipoMeioComposto ) ")
		.append("from " + MeioTransporte.class.getName() + " as MT \n")
		.append(" left join MT.modeloMeioTransporte as MODELO ")
		.append(" left join MODELO.marcaMeioTransporte as MARCA ")
		.append(" left join MODELO.tipoMeioTransporte as TIPO ")
		.append(" left join MT.meioTransporteRodoviario as RODO ")
		.append(" left join RODO.operadoraMct as OPERADORA ")
		.append(" left join OPERADORA.pessoa as PESSOA ")
		.append(" where MT.idMeioTransporte = " + idMeioTransporte.toString());
		return getAdsmHibernateTemplate().find(hql.toString());
	}

	/**
	 * Retorna filial do meio de transporte informado.
	 * @param idMeioTransporte
	 * @return
	 */
	public Long findFilialByMeioTransporte(Long idMeioTransporte) {
		StringBuffer hql = new StringBuffer()
			.append(" select F.idFilial from " + MeioTransporte.class.getName() + " MT ")
			.append(" inner join MT.filial F ")
			.append(" where MT.idMeioTransporte = ? ");

		List<Long> result = getAdsmHibernateTemplate().find(hql.toString(), idMeioTransporte);

		return result.isEmpty() ? null : result.get(0);
	}

	/**
	 * Verifica se o meio de transporte está ativo.
	 * 
	 * @param map
	 * @return TRUE se o meioTransporte informado está ativo, FALSE caso contrário
	 */
	public Boolean validateMeioTransporteAtivo(Long idMeioTransporte){
		DetachedCriteria dc = DetachedCriteria.forClass(MeioTransporte.class)
		.setProjection(Projections.rowCount())
		.add(Restrictions.eq("tpSituacao","A"))
		.add(Restrictions.eq("id", idMeioTransporte));
		int rowCount = this.getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc).intValue();
		return (rowCount > 0);
	}

	public String findNrFrotaByNrIdentificacao(String nrIdentificacao){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("mt.nrFrota","nrFrota");
		hql.addFrom(MeioTransporte.class.getName()+" mt ");
		hql.addCriteria("mt.nrIdentificador","like",nrIdentificacao);
		return (String) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),new Object[]{nrIdentificacao});
	}

	public MeioTransporte findMeioTransporteByCodigoBarras(Long codigoBarras){
		DetachedCriteria dc = DetachedCriteria.forClass(MeioTransporte.class)
		.add(Restrictions.eq("nrCodigoBarra", codigoBarras));
		return (MeioTransporte) this.getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public MeioTransporte findMeioTransporteByNrFrota(String nrFrota){
		DetachedCriteria dc = DetachedCriteria.forClass(MeioTransporte.class)
		.add(Restrictions.eq("nrFrota", nrFrota).ignoreCase());
		return (MeioTransporte) this.getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	
	/**
	 * getRowCount utilizado na tela VOL/Métrica/Totais por frota.
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountTotaisPorFrota(TypedFlatMap criteria){
		StringBuilder query = new StringBuilder()
			.append(" from meio_transporte metr ")
			.append(" left join equipamento equip on equip.id_meio_transporte = metr.id_meio_transporte ")
			.append(" left join GRUPO_FROTA_VEICULO vgf on vgf.ID_MEIO_TRANSPORTE = metr.id_meio_transporte ")
			.append("    left join (")
			.append("       SELECT cg.id_transportado id_meio_transporte, ")
			.append("             Count(med.id_manifesto_entrega_documento) TOTAL_ENT ")
			.append("       FROM manifesto_entrega me ")
			.append("          inner join filial f ON f.id_filial = me.id_filial ") 
			.append("          inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
			.append("          inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
			.append("          inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append("       where F.ID_FILIAL =:idFilial ")
			.append("          AND me.dh_emissao between :dataInicial and :dataFinal ")
			.append("       GROUP BY cg.id_transportado ")
			.append("    ) entr on entr.iD_meio_transporte = metr.id_meio_transporte	")
			.append("    left join (	")
			.append("       SELECT ec.id_meio_transporte, ")
			.append("          Sum(CASE WHEN ec.tp_evento_coleta = 'EX' THEN 1 ELSE 0 END) TOTAL_COL ")
			.append("       FROM pedido_coleta pc ")
			.append("          inner join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append("          inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append("       WHERE F.ID_FILIAL =:idFilial ")
			.append("          AND PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append("       GROUP BY ec.id_meio_transporte ")
			.append("    ) cole on cole.iD_meio_transporte = metr.id_meio_transporte	")
			.append("    where nvl(entr.total_ent,0) + nvl(cole.total_col,0) > 0	")
			.append("    AND METR.ID_FILIAL =:idFilial ");

		if ("S".equals(criteria.getString("tpPossuiCelular"))){
			query.append(" and equip.id_equipamento is not null ");
		} else if ("N".equals(criteria.getString("tpPossuiCelular"))){
			query.append(" and equip.id_equipamento is null ");
		}

		if (criteria.getLong("grupo.idGrupoFrota") != null){
			query.append(" and vgf.id_grupo_frota =:idGrupoFrota ");  
		}

		if ( criteria.getLong("meioTransporte.idMeioTransporte") != null) {
			query.append(" and metr.id_meio_transporte =:idMeioTransporte");
		}

		Long idFilial = criteria.getLong("filial.idFilial"); 
		YearMonthDay dataInicial = criteria.getYearMonthDay("dataInicial");
		YearMonthDay dataFinal = criteria.getYearMonthDay("dataFinal");
		Long idGrupoFrota = criteria.getLong("grupo.idGrupoFrota");
		Long idMeioTransporte = criteria.getLong("meioTransporte.idMeioTransporte");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idFilial", idFilial);
		parameters.put("dataInicial", dataInicial);
		parameters.put("dataFinal", dataFinal);
		parameters.put("idGrupoFrota", idGrupoFrota);
		parameters.put("idMeioTransporte", idMeioTransporte);

		return getAdsmHibernateTemplate().getRowCountBySql(query.toString(), parameters);
	}

	/**
	 * FindPaginated utilizado na tela VOL/Métrica/Totais por frota.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedTotaisPorFrota(TypedFlatMap criteria, FindDefinition fd){

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idFrota",Hibernate.LONG);
				sqlQuery.addScalar("frota",Hibernate.STRING);
				sqlQuery.addScalar("totais_e",Hibernate.LONG);
				sqlQuery.addScalar("totais_c",Hibernate.LONG);
				sqlQuery.addScalar("totais_ec",Hibernate.LONG);
				sqlQuery.addScalar("volumes_e",Hibernate.LONG);
				sqlQuery.addScalar("volumes_c",Hibernate.LONG);
				sqlQuery.addScalar("volumes_ec",Hibernate.LONG);
				sqlQuery.addScalar("nao_realizadas_e",Hibernate.LONG);
				sqlQuery.addScalar("nao_realizadas_c",Hibernate.LONG);
				sqlQuery.addScalar("nao_realizadas_ec",Hibernate.LONG);
				sqlQuery.addScalar("nao_baixados_e",Hibernate.LONG);
				sqlQuery.addScalar("nao_baixados_c",Hibernate.LONG);
				sqlQuery.addScalar("nao_baixados_ec",Hibernate.LONG);
				sqlQuery.addScalar("cham_ent",Hibernate.LONG);
				sqlQuery.addScalar("cham_col",Hibernate.LONG);
				sqlQuery.addScalar("cham_tot",Hibernate.LONG);
				sqlQuery.addScalar("recusas_e",Hibernate.LONG);
				sqlQuery.addScalar("recusa_reentregas",Hibernate.LONG);
				sqlQuery.addScalar("recusa_devolucoes",Hibernate.LONG);
				sqlQuery.addScalar("col_automaticas",Hibernate.LONG);
				sqlQuery.addScalar("col_troc_frota",Hibernate.LONG);
				sqlQuery.addScalar("reentregas",Hibernate.LONG);
			}
		};

		Long idFilial = criteria.getLong("filial.idFilial");
		YearMonthDay dataInicial = criteria.getYearMonthDay("dataInicial");
		YearMonthDay dataFinal = criteria.getYearMonthDay("dataFinal");
		Long idGrupoFrota = criteria.getLong("grupo.idGrupoFrota");
		Long idMeioTransporte = criteria.getLong("meioTransporte.idMeioTransporte");
		String sql = this.montaConsultaTotaisPorFrota(criteria);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idFilial", idFilial);
		parameters.put("dataInicial", dataInicial);
		parameters.put("dataFinal", dataFinal);
		parameters.put("idGrupoFrota", idGrupoFrota);
		parameters.put("idMeioTransporte", idMeioTransporte);

		return getAdsmHibernateTemplate().findPaginatedBySql(sql, fd.getCurrentPage(), fd.getPageSize(), parameters, csq);
	}

	/**
	 * FIXME Consulta da tela Métricas/Totais por frota.
	 * autor ROBERTOFA
	 * @return
	 */
	private String montaConsultaTotaisPorFrota(TypedFlatMap criteria){
		StringBuilder query = new StringBuilder()
			.append(" select ")	
			.append("    metr.id_meio_transporte idFrota, ")
			.append("    CASE WHEN equip.id_equipamento is NOT NULL THEN metr.nr_frota ELSE metr.nr_frota || '*' END frota, ")
			.append("    nvl(entr.TOTAL_ENT,0) totais_e,	")
			.append("    nvl(cole.TOTAL_COL,0) totais_c,	")
			.append("    nvl(entr.TOTAL_ENT,0) + nvl(cole.TOTAL_COL,0) totais_ec, ")
			.append("    nvl(volm_ent.VOLUMES_ENT,0) volumes_e, ")
			.append("    nvl(volm_col.VOLUMES_COL,0) volumes_c, ")
			.append("    nvl(volm_ent.VOLUMES_ENT,0) + nvl(volm_col.VOLUMES_COL,0) volumes_ec, ")
			.append("    nvl(entr.NAO_REALIZADAS_ENT,0) nao_realizadas_e, ")
			.append("    nvl(cole.NAO_REALIZADAS_COL,0) nao_realizadas_c, ")
			.append("    nvl(entr.NAO_REALIZADAS_ENT,0) + nvl(cole.NAO_REALIZADAS_COL,0) nao_realizadas_ec, ")
			.append("    nvl(entr.NAO_BAIXADOS_ENT,0) nao_baixados_e, ")
			.append("    nvl(cole.NAO_BAIXADOS_COL,0) nao_baixados_c, ")
			.append("    nvl(entr.NAO_BAIXADOS_ENT,0) + nvl(cole.NAO_BAIXADOS_COL,0) nao_baixados_ec, ")	
			.append("    nvl(cham_ent.CHAMADAS_ENT,0) cham_ent, ")
			.append("    nvl(cham_col.CHAMADAS_COL,0) cham_col,	")
			.append("    nvl(cham_ent.CHAMADAS_ENT,0) + nvl(cham_col.CHAMADAS_COL,0) cham_tot,	")
			.append("    nvl(recusas_ent.RECUSAS_ENT,0) recusas_e, ")
			.append("    nvl(rec_tr_ree.REC_TR_REE,0) recusa_reentregas, ")
			.append("    nvl(rec_tr_dev.REC_TR_DEV,0) recusa_devolucoes, ")
			.append("    nvl(cole.TOTAL_AUTO,0) col_automaticas,	")
			.append("    nvl(troca_frota.TROCA_FROTA,0) col_troc_frota, ")
			.append("    nvl(reent.REENTREGA,0) reentregas ")
			.append(" from meio_transporte metr ")
			.append(" left join equipamento equip on equip.id_meio_transporte = metr.id_meio_transporte ")
			.append(" left join GRUPO_FROTA_VEICULO vgf on vgf.ID_MEIO_TRANSPORTE = metr.id_meio_transporte ")
			.append("    left join (	")
			.append("       SELECT cg.id_transportado id_meio_transporte, ")
			.append("             Count(med.id_manifesto_entrega_documento) TOTAL_ENT, ")
			.append("		      Sum(CASE WHEN oe.tp_ocorrencia = 'N' THEN 1 ELSE 0 END) NAO_REALIZADAS_ENT, ")
			.append("             Sum(CASE WHEN oe.id_ocorrencia_entrega IS NULL THEN 1 ELSE 0 END) NAO_BAIXADOS_ENT ")
			.append("       FROM manifesto_entrega me ")
			.append("          inner join filial f ON f.id_filial = me.id_filial ") 
			.append("          inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
			.append("          inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
			.append("          inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append("          left join ocorrencia_entrega oe ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ")
			.append("       where F.ID_FILIAL =:idFilial ")
			.append("          AND me.dh_emissao between :dataInicial and :dataFinal ")
			.append("       GROUP BY cg.id_transportado ")
			.append("    ) entr on entr.iD_meio_transporte = metr.id_meio_transporte	")
			.append("    left join (	")
			.append("       SELECT ec.id_meio_transporte, ")
			.append("          Sum(CASE WHEN ec.tp_evento_coleta = 'EX' THEN 1 ELSE 0 END) TOTAL_COL, ")
			.append("          Sum(CASE WHEN pc.tp_status_coleta <> 'EX' THEN 1 ELSE 0 END) NAO_REALIZADAS_COL, ")
			.append("          Sum(CASE WHEN pc.tp_status_coleta = 'TR' THEN 1 ELSE 0 END) NAO_BAIXADOS_COL, ")
			.append("          Sum(CASE WHEN pc.tp_modo_pedido_coleta = 'AU' THEN 1 ELSE 0 END) TOTAL_AUTO	")
			.append("       FROM pedido_coleta pc ")
			.append("          inner join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append("          inner join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append("       WHERE F.ID_FILIAL =:idFilial ")
			.append("          AND PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append("       GROUP BY ec.id_meio_transporte ")
			.append("    ) cole on cole.iD_meio_transporte = metr.id_meio_transporte	")
			.append("    left join ( ")
			.append("       SELECT cg.id_transportado, ")
			.append("          Sum (CASE WHEN nfc.qt_volumes IS NULL THEN 0 ELSE nfc.qt_volumes END) VOLUMES_ENT ")
			.append("       FROM manifesto_entrega me ")
			.append("          inner join filial f ON f.id_filial = me.id_filial ")
			.append("          inner join manifesto m ON me.id_manifesto_entrega = m.id_manifesto ")
			.append("          inner join controle_carga cg ON cg.id_controle_carga = m.id_controle_carga ")
			.append("          inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append("          inner join nota_fiscal_conhecimento nfc ON nfc.id_nota_fiscal_conhecimento = med.id_nota_fiscal_conhecimento ")
			.append("       WHERE f.id_filial =:idFilial ")
			.append("          and me.dh_emissao between :dataInicial and :dataFinal ")
			.append("       GROUP BY cg.id_transportado	")
			.append("    ) volm_ent on volm_ent.id_transportado = metr.id_meio_transporte ")
			.append("    left join (	")
			.append("       SELECT ec.id_meio_transporte, ")
			.append("          Sum(CASE WHEN dc.qt_volumes IS NULL THEN 0 ELSE dc.qt_volumes END) VOLUMES_COL ")
			.append("       FROM pedido_coleta pc ")
			.append("          inner join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append("          left join evento_coleta ec ON ec.id_pedido_coleta = pc.id_pedido_coleta ")
			.append("          left join detalhe_coleta dc ON dc.id_pedido_coleta = pc.id_pedido_coleta ")
			.append("       WHERE f.id_filial =:idFilial ")
			.append("          and PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append("       GROUP BY ec.id_meio_transporte ")
			.append("    ) volm_col on volm_col.id_meio_transporte = metr.id_meio_transporte	")
			.append("    left join (	")
			.append("       SELECT mt.id_meio_transporte, ")
			.append("          Sum(CASE WHEN ec.tp_origem = 'C' AND te.tp_tipo_evento = 'E' THEN 1 ELSE 0 END) CHAMADAS_ENT ")
			.append("       FROM meio_transporte mt	")
			.append("          left join eventos_celular ec ON mt.id_meio_transporte = ec.id_meio_transporte ")
			.append("          left join tipo_evento_celular te ON te.id_tipo_evento = ec.id_tipo_evento	")
			.append("       WHERE mt.id_filial =:idFilial ")
			.append("          and ec.dh_solicitacao between :dataInicial and :dataFinal ")
			.append("       GROUP BY mt.id_meio_transporte ")
			.append("    ) cham_ent on cham_ent.id_meio_transporte = metr.id_meio_transporte	")
			.append("    left join (	")
			.append("       SELECT mt.id_meio_transporte, ")
			.append("          Sum(CASE te.tp_tipo_evento WHEN  'C' THEN 1 ELSE 0 END) CHAMADAS_COL ")
			.append("       FROM meio_transporte mt	")
			.append("          left join eventos_celular ec ON ec.id_meio_transporte = mt.id_meio_transporte ")
			.append("          left join tipo_evento_celular te ON te.id_tipo_evento = ec.id_tipo_evento	")
			.append("       WHERE mt.id_filial =:idFilial ")
			.append("          AND ec.dh_solicitacao between :dataInicial and :dataFinal ")
			.append("       GROUP BY mt.id_meio_transporte ")
			.append("    ) cham_col on cham_col.id_meio_transporte = metr.id_meio_transporte	")
			.append("    left join (	")
			.append("       SELECT cg.id_transportado, ")
			.append("          Count(r.id_recusa) RECUSAS_ENT ")
			.append("       FROM controle_carga cg ")
			.append("          left join manifesto m ON m.id_controle_carga = cg.id_controle_carga	")
			.append("          left join manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
			.append("          left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega	")
			.append("          left join recusa r ON r.id_manifesto_entrega_documento = med.id_manifesto_entrega_documento ")
			.append("       WHERE r.id_filial =:idFilial ")
			.append("          AND r.dh_recusa between :dataInicial and :dataFinal ")
			.append("       GROUP BY cg.id_transportado	")
			.append("    ) recusas_ent on recusas_ent.id_transportado = metr.id_meio_transporte ")
			.append("    left join (	")
			.append("       SELECT cg.id_transportado, ")
			.append("          Sum(CASE WHEN r.tp_recusa = 'R' THEN 1 ELSE 0 END) REC_TR_REE ")
			.append("       FROM controle_carga cg ")
			.append("          left join manifesto m ON m.id_controle_carga = cg.id_controle_carga	")
			.append("          left join manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
			.append("          left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append("          left join recusa r ON r.id_manifesto_entrega_documento = med.id_manifesto_entrega_documento ")
			.append("       WHERE r.id_filial =:idFilial ")
			.append("          AND r.dh_recusa between :dataInicial and :dataFinal ")
			.append("       GROUP BY cg.id_transportado				")
			.append("    ) rec_tr_ree on rec_tr_ree.id_transportado = metr.id_meio_transporte ")
			.append("    left join (	")
			.append("       SELECT cg.id_transportado, ")
			.append("          Sum(CASE WHEN r.tp_recusa = 'D' THEN 1 ELSE 0 END) REC_TR_DEV ")
			.append("       FROM controle_carga cg ")
			.append("          left join manifesto m ON m.id_controle_carga = cg.id_controle_carga	")
			.append("          left join manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
			.append("          left join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
			.append("          left join recusa r ON r.id_manifesto_entrega_documento = med.id_manifesto_entrega_documento ")
			.append("       WHERE r.id_filial =:idFilial ")
			.append("          AND r.dh_recusa between :dataInicial and :dataFinal ")
			.append("       GROUP BY cg.id_transportado	")
			.append("    ) rec_tr_dev on rec_tr_dev.id_transportado = metr.id_meio_transporte ")
			.append("    left join (	")
			.append("       SELECT id_meio_transporte, ")
			.append("          Sum(CASE WHEN tr > 1 AND ex >=1 THEN 1 ELSE 0 END) TROCA_FROTA FROM ( ")
			.append("             SELECT mt.id_meio_transporte, pc.id_pedido_coleta, ")
			.append("                Count(DISTINCT ec2.id_evento_coleta) TR, Count(DISTINCT ec1.id_evento_coleta) EX ")
			.append("             FROM meio_transporte mt ")
			.append("                left join evento_coleta ec1 ON ec1.id_meio_transporte = mt.id_meio_transporte AND ec1.tp_evento_coleta = 'EX' ")
			.append("                left join pedido_coleta pc  ON pc.id_pedido_coleta = ec1.id_pedido_coleta ")
			.append("                left join evento_coleta ec2 ON ec2.id_pedido_coleta = pc.id_pedido_coleta AND ec2.tp_evento_coleta = 'TR' ")
			.append("                left join filial f ON f.id_filial = pc.id_filial_responsavel ")
			.append("             where f.id_filial =:idFilial ")
			.append("                and PC.DH_PEDIDO_COLETA between :dataInicial and :dataFinal ")
			.append("             GROUP BY mt.id_meio_transporte, pc.id_pedido_coleta	")
			.append("       ) GROUP BY id_meio_transporte ")
			.append("    ) troca_frota on troca_frota.id_meio_transporte = metr.id_meio_transporte ")
			.append("    left join (	")
			.append("              SELECT cg.id_transportado,  ")
			.append("                     Count(med.id_manifesto_entrega_documento) REENTREGA ")
			.append("               FROM manifesto_entrega me  ")
			.append("                   inner join filial f                        ON f.id_filial = me.id_filial  ")
			.append("                   inner join manifesto m                     ON me.id_manifesto_entrega = m.id_manifesto  ")
			.append("                   inner join controle_carga cg               ON cg.id_controle_carga = m.id_controle_carga  ")
			.append("                   inner join manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ") 
			.append("                   left join ocorrencia_entrega oe            ON oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ")
			.append("              WHERE f.id_filial =:idFilial ")
			.append("                    and me.dh_emissao between :dataInicial and :dataFinal ")
			.append("                    and exists ( ")
			.append("                             SELECT * ")
			.append("                             FROM manifesto_entrega_documento aux ")
			.append("                             WHERE aux.id_docto_servico = med.id_docto_servico ")
			.append("                             and aux.id_manifesto_entrega_documento  <> med.id_manifesto_entrega_documento  ")
			.append("                  ) group by cg.id_transportado ")
			.append("    ) reent on reent.id_transportado = metr.id_meio_transporte ")
			.append("    where nvl(entr.total_ent,0) + nvl(cole.total_col,0) > 0	")
			.append("    AND METR.ID_FILIAL =:idFilial ");

		if ("S".equals(criteria.getString("tpPossuiCelular"))) {
			query.append(" and equip.id_equipamento is not null ");
		} else if ("N".equals(criteria.getString("tpPossuiCelular"))) {
			query.append(" and equip.id_equipamento is null ");
		}

		if (criteria.getLong("grupo.idGrupoFrota") != null) {
			query.append(" and vgf.id_grupo_frota =:idGrupoFrota "); 
		}
		if ( criteria.getLong("meioTransporte.idMeioTransporte") != null) {
			query.append(" and metr.id_meio_transporte =:idMeioTransporte");
		}
		query.append(" order by metr.nr_frota ");
		return query.toString();
	}

	public void storeMeioTransporte(MeioTransporte meioTransporte) {
		if (meioTransporte.getIdMeioTransporte() != null) {
			getAdsmHibernateTemplate().removeById("delete " + MeioTransportePeriferico.class.getName()+" where meioTransporte.id = :id", meioTransporte.getIdMeioTransporte());
		}
		store(meioTransporte);
		List<MeioTransportePeriferico> meioTransportePerifericos = meioTransporte.getMeioTransportePerifericos();
		if(meioTransportePerifericos != null) {
			for (MeioTransportePeriferico meioTransportePeriferico : meioTransportePerifericos) {
				store(meioTransportePeriferico);
			}
		}
	}

	public List<MeioTransportePeriferico> findMeioTransportePerifericos(MeioTransporte meioTransporte) {
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(MeioTransportePeriferico.class.getName() + " as mtp inner join fetch mtp.meioTransporte as mt");
		sql.addCriteria("mt.idMeioTransporte","=",meioTransporte.getIdMeioTransporte());

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	/**
	 * Verifica se um meio de tranporte é um semi-reboque.
	 * 
	 * @param idMeioTransporte
	 * @return True se o meio de transporte é um semi-reboque, caso contrário, False.
	 */
	public Boolean findMeioTransporteIsSemiReboque(Long idMeioTransporte) {
		StringBuffer hql = new StringBuffer()
		.append("select tmt.id ")
		.append("from ")
		.append(TipoMeioTransporte.class.getName()).append(" as tmt ")
		.append("where ")
		.append("tmt.tipoMeioTransporte.id in ")
			.append("(select tmt2.id ")
			.append("from ")
			.append(TipoMeioTransporte.class.getName()).append(" as tmt2 ")
			.append("inner join tmt2.modeloMeioTransportes as mmt2 ")
			.append("inner join mmt2.meioTransportes as mt2 ")
			.append("where ")
			.append("mt2.id = ?) ");

		List<Long> list = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idMeioTransporte});
		return Boolean.valueOf(!list.isEmpty());
	}

	/**
	 * Verifica se um meio de tranporte é um cavalo-trator.
	 * 
	 * @param idMeioTransporte
	 * @return True se o meio de transporte é um cavalo-trator, caso contrário, False.
	 */
	public Boolean findMeioTransporteIsCavaloTrator(Long idMeioTransporte) {
	
		StringBuffer hql = new StringBuffer()
		.append("select tmt.id ")
		.append("from ")
		.append(MeioTransporte.class.getName()).append(" as mt ")
		.append("inner join mt.modeloMeioTransporte as mmt ")
		.append("inner join mmt.tipoMeioTransporte as tmt ")
		.append("where ")
		.append("mt.id = ?")
		.append("and tmt.tipoMeioTransporte is null");

		List<Long> list = getAdsmHibernateTemplate().find(hql.toString(), idMeioTransporte);
		return Boolean.valueOf(list.isEmpty());
	}

	public List<SolicitacaoContratacao> findInfoMeioTransporteSolicitacaoByNrPlaca(String nrIdentificador) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("SC");
		hql.addFrom(SolicitacaoContratacao.class.getName() + " SC " +
				"inner join fetch SC.tipoMeioTransporte TMT ");
		hql.addCriteria("SC.tpSituacaoContratacao","=","AP");
		hql.addCriteria("lower(SC.nrIdentificacaoMeioTransp)","=",nrIdentificador.toLowerCase());
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	/**
	 * Método que faz flush dos dados para o banco e retira do cache o bean antigo
	 * @param id
	 * @param beanOld
	 * @return
	 */
	public MeioTransporte findByIdForceFlush(Long id, MeioTransporte beanOld) {
		getAdsmHibernateTemplate().flush();
		getAdsmHibernateTemplate().evict(beanOld);
		return findByIdCustom(id);
	}

	
	/**
	 * Verifica se existe na tabela F1201 o numero da frota passado como]
	 * parametro
	 * 
	 * @param nrFrota
	 * @return
	 */
	public List findValidFrota(String nrFrota) {
		
		List param = new ArrayList();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select F1201.FAAPID as nrFrota, F1201.FAASID as nrPlaca ");
		sql.append(" FROM ");
		sql.append(" F1201 WHERE F1201.FAAPID = '").append(nrFrota).append("'");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("nrFrota", Hibernate.STRING);
				sqlQuery.addScalar("nrPlaca", Hibernate.STRING);
			}
		};
		
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(1000), param.toArray(), configureSqlQuery).getList();
	}
	
	//LMS-6178
	public String findTipoVeiculoByIdMeioTransporte(Long idMeioTransporte) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idMeioTransporte", idMeioTransporte);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT vi18n(cam.DS_CONTEUDO_ATRIBUTO_MODELO_I) AS CONTEUDO_ATRIBUTO ")
			.append("FROM PROCESSO_SINISTRO ps, ")
			.append("  MEIO_TRANSP_CONTEUDO_ATRIB mtca, ")
			.append("  CONTEUDO_ATRIBUTO_MODELO cam ")
			.append("WHERE ps.id_meio_transporte            = mtca.id_meio_transporte ")
			.append("AND mtca.ID_CONTEUDO_ATRIBUTO_MODELO   = cam.ID_CONTEUDO_ATRIBUTO_MODELO ")
			.append("AND cam.ID_MODELO_MEIO_TRANSP_ATRIBUTO = ")
			.append("  (SELECT ds_conteudo ")
			.append("  FROM parametro_geral ")
			.append("  WHERE nm_parametro_geral = 'ID_ATRIB_FUNCAO_MEIO_TRANSP' ")
			.append("  ) ")
			.append("AND ps.id_meio_transporte = :idMeioTransporte ");
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("CONTEUDO_ATRIBUTO", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);

		if(!list.isEmpty()){
			return String.valueOf(list.get(0));
		}
		
		return "";
	}
	
	/**
	 * Retorna o resultado da pesquisa para uma suggest de meio de transporte.
	 * 
	 * @param nrIdentificador
	 * @param nrFrota
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMeioTransporteSuggest(
			final Long idProprietario, final String nrIdentificador, final String nrFrota,
			final Integer limiteRegistros) {		
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
				
		final StringBuilder sql = getQueryMeioTransporteSuggest(idProprietario, nrIdentificador, nrFrota, limiteRegistros);
		
		final ConfigureSqlQuery csq = getConfigureQueryMeioTransporteSuggest();
		
		final HibernateCallback hcb = getCallbackMeioTransporteSuggest(idProprietario, nrIdentificador, nrFrota, limiteRegistros, sql, csq);
				
		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
	
		for (Object[] o: list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idMeioTransporte", o[0]);
			map.put("nrIdentificador", o[1]);
			map.put("nrFrota", o[2]);
			toReturn.add(map);			
		}
		
		return toReturn;
	}

	private HibernateCallback getCallbackMeioTransporteSuggest(
			final Long idProprietario, final String nrIdentificador,
			final String nrFrota, final Integer limiteRegistros,
			final StringBuilder sql, final ConfigureSqlQuery csq) {
		return new HibernateCallback() {
			public Object doInHibernate(Session session) {
				SQLQuery query = session.createSQLQuery(sql.toString());
				
				if(idProprietario != null){
					query.setLong("idProprietario", idProprietario);
				}
				
				if(!StringUtils.isBlank(nrIdentificador)){
					query.setString("nrIdentificador", nrIdentificador.trim());
				}
				
				if(!StringUtils.isBlank(nrFrota)){
					query.setString("nrFrota", nrFrota.trim());
				}
				
				if(limiteRegistros != null){
					query.setInteger("limiteRegistros", limiteRegistros);
				}
				
            	csq.configQuery(query);
				return query.list();
			}
		};
	}

	private ConfigureSqlQuery getConfigureQueryMeioTransporteSuggest() {
		return new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_meio_transporte", Hibernate.LONG);
				sqlQuery.addScalar("nr_identificador", Hibernate.STRING);
				sqlQuery.addScalar("nr_frota", Hibernate.STRING);
			}
		};
	}

	private StringBuilder getQueryMeioTransporteSuggest(
			final Long idProprietario, final String nrIdentificador,
			final String nrFrota, final Integer limiteRegistros) {
		if(limiteRegistros == null){
			throw new BusinessException("LMS-01130", new Object[] { "Parametro limiteRegistros" });
		}		
		
		final StringBuilder sql = new StringBuilder();
				
		sql.append("SELECT m.id_meio_transporte,");
		sql.append("       m.nr_identificador,"); 
		sql.append("       m.nr_frota");		
		sql.append(" FROM meio_transporte m");
		
		if(idProprietario != null){
			sql.append(", MEIO_TRANSP_PROPRIETARIO mtp");			
			sql.append(" WHERE mtp.ID_PROPRIETARIO = :idProprietario AND mtp.id_meio_transporte = m.id_meio_transporte");				
		} else {		
			sql.append(" WHERE 1=1");
		}
		
		if(!StringUtils.isBlank(nrIdentificador)){
			sql.append(" AND UPPER(m.nr_identificador) = UPPER(:nrIdentificador)");
		}
		
		if(!StringUtils.isBlank(nrFrota)){
			sql.append(" AND UPPER(m.nr_frota) = UPPER(:nrFrota)");
		}
		
		sql.append(" AND rownum <= :limiteRegistros");
		
		return sql;
	}
	
	public void removeByIdsAnexoMeioTransporte(List ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoMeioTransporte.class.getName() + " WHERE idAnexoMeioTransporte IN (:id)", ids);
	}

	public AnexoMeioTransporte findAnexoMeioTransporteById(Long idAnexoMeioTransporte) {
		return (AnexoMeioTransporte) getAdsmHibernateTemplate().load(AnexoMeioTransporte.class, idAnexoMeioTransporte);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<AnexoMeioTransporte> findPaginatedAnexoMeioTransporte(PaginatedQuery paginatedQuery) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoMeioTransporte AS idAnexoMeioTransporte,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhInclusao,");
		hql.append(" usuario.usuarioADSM.nmUsuario as nmUsuario)");
		hql.append(" FROM AnexoMeioTransporte AS anexo");
		hql.append("  INNER JOIN anexo.meioTransporte meioTransporte");
		hql.append("  INNER JOIN anexo.usuario usuario");
		hql.append(" WHERE meioTransporte.idMeioTransporte = :idMeioTransporte");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, hql.toString());
	}
	
	public Integer getRowCountAnexoMeioTransporte(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_meio_transporte WHERE id_meio_transporte = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{criteria.get("idMeioTransporte")});
	}
	
	/**
	 * Define a data e o usuário de alteração do registro. 
	 * 
	 * @param idMeioTransporte
	 * @param idUsuario
	 */
	public void updateMeioTransporteAlteracao(Long idMeioTransporte, Long idUsuario) {
		StringBuilder hql = new StringBuilder();
		hql.append("UPDATE MeioTransporte AS meioTransporte");
		hql.append(" SET meioTransporte.usuarioAtualizacao.idUsuario = ?, meioTransporte.dtAtualizacao = CURRENT_DATE()");
		hql.append(" WHERE meioTransporte.idMeioTransporte = ?");
		
		List<Object> parametersValues = new ArrayList<Object>();		
		parametersValues.add(idUsuario);
		parametersValues.add(idMeioTransporte);
		
		executeHql(hql.toString(), parametersValues);
	}
	
	public List<Map<String, Object>> findMeioTransporteByProprietarioAndFilial(Long idProprietario, Long idFilial) {
		StringBuilder s = new StringBuilder();
		
		s.append(" SELECT MT.NR_FROTA AS FROTA, ");
		s.append("  MT.NR_IDENTIFICADOR AS PLACA, ");
		s.append("  MT.ID_MEIO_TRANSPORTE AS ID_MT	");
		s.append(" FROM MEIO_TRANSP_PROPRIETARIO MTP, ");
		s.append("  MEIO_TRANSPORTE MT ");
		s.append(" WHERE MTP.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE ");
		s.append("	AND MTP.ID_PROPRIETARIO = :idProprietario ");
		s.append("  AND MT.ID_FILIAL = :idFilial ");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = new TypedFlatMap();		
       	params.put("idProprietario", idProprietario);  
    	params.put("idFilial", idFilial);  
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(s.toString(), params, getConfigureSqlQueryAwbForDocto());
	}

	private ConfigureSqlQuery getConfigureSqlQueryAwbForDocto() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("FROTA", Hibernate.STRING);
				sqlQuery.addScalar("PLACA", Hibernate.STRING);
				sqlQuery.addScalar("ID_MT", Hibernate.LONG);
			}
		};
		
		return csq;
	}
}