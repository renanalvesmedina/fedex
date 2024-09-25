package com.mercurio.lms.carregamento.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DispositivoUnitizacaoDAO extends BaseCrudDao<DispositivoUnitizacao, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    @Override
	protected final Class getPersistentClass() {
        return DispositivoUnitizacao.class;
    }

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("empresa", FetchMode.JOIN);
		lazyFindPaginated.put("empresa.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("tipoDispositivoUnitizacao", FetchMode.JOIN);
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("empresa", FetchMode.JOIN);
		lazyFindById.put("empresa.pessoa", FetchMode.JOIN);
		lazyFindById.put("tipoDispositivoUnitizacao", FetchMode.JOIN);
	}

	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("empresa", FetchMode.JOIN);
		lazyFindLookup.put("empresa.pessoa", FetchMode.JOIN);
		lazyFindLookup.put("tipoDispositivoUnitizacao", FetchMode.JOIN);
	}	
	
	public List<Long> findDispUnitizacaoByIdentificacao(String nrIdentificacao){
		Validate.notEmpty(nrIdentificacao, "nrIdentificacao cannot be null");

		StringBuilder query = new StringBuilder()
		.append("select disp.idDispositivoUnitizacao ")
		.append("from ").append(DispositivoUnitizacao.class.getName()).append(" as disp ")
		.append("where disp.nrIdentificacao = :nrIdentificacao");

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(),"nrIdentificacao", nrIdentificacao);
	}

	public List<DispositivoUnitizacao> findDispositivoUnitizacaoByIdentificacao(final String nrIdentificacao){
		Validate.notEmpty(nrIdentificacao, "nrIdentificacao cannot be null");

		return getAdsmHibernateTemplate().findByCriteria(
															DetachedCriteria.forClass(DispositivoUnitizacao.class)
																.add(Restrictions.ilike("nrIdentificacao", nrIdentificacao, MatchMode.ANYWHERE))
														);
	}
	
	public List findConhecimentoByDispositivoUnitizacao(Long idDispositivoUnitizacao, Long idControleCarga) {	
		StringBuilder query =  new StringBuilder();
		
		query.append(" select new map(con.id as idDoctoServico, vnf.nrConhecimento as nrConhecimento,");
		query.append(" fil.sgFilial as sgFilialDocumento, ");
		query.append(" con.qtVolumes as qtVolumes, ");
		query.append(" con.nrCae as nrCae, ");
		query.append(" con.tpDoctoServico as tpDoctoServico, ");
		query.append(" count(vnf.idVolumeNotaFiscal) as qtVolDispUnit) ");
		query.append(" from VolumeNotaFiscal vnf ");
        query.append(" join vnf.notaFiscalConhecimento nfc ");      
		query.append(" join nfc.conhecimento con ");
		query.append(" join vnf.dispositivoUnitizacao dun ");
		query.append(" join con.filialOrigem fil ");
		query.append(" where dun.idDispositivoUnitizacao = ?");
	
		query.append(" group by con.id, vnf.nrConhecimento, fil.sgFilial, con.qtVolumes, con.nrCae, con.tpDoctoServico");
		
			List<Map<String,Object>> result =  getAdsmHibernateTemplate().find(query.toString(), new Object[]{ idDispositivoUnitizacao });
			for(Map<String,Object> item : result){
				item.put("tpDoctoServico", ((DomainValue)item.get("tpDoctoServico")).getValue());
			}
			return result;
	}
	
	public DispositivoUnitizacao findByBarcode(String barcode){
		DetachedCriteria dc = DetachedCriteria.forClass(DispositivoUnitizacao.class)
		.setFetchMode("tipoDispositivoUnitizacao", FetchMode.JOIN)
		.setFetchMode("macroZona", FetchMode.JOIN)
		.setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
		.setFetchMode("localizacaoFilial", FetchMode.JOIN)
		.setFetchMode("dispositivoUnitizacaoPai", FetchMode.JOIN)
		.setFetchMode("volumes", FetchMode.JOIN)
		.add(Restrictions.eq("nrIdentificacao", barcode));
		return (DispositivoUnitizacao) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public DispositivoUnitizacao findByBarcodeIdEmpresa(String barcode, Long idEmpresa){
		DetachedCriteria dc = DetachedCriteria.forClass(DispositivoUnitizacao.class)
		.setFetchMode("tipoDispositivoUnitizacao", FetchMode.JOIN)
		.setFetchMode("macroZona", FetchMode.JOIN)
		.setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
		.setFetchMode("localizacaoFilial", FetchMode.JOIN)
		.setFetchMode("empresa", FetchMode.JOIN)
		.add(Restrictions.eq("nrIdentificacao", barcode))
		.add(Restrictions.eq("empresa.idEmpresa", idEmpresa));
		return (DispositivoUnitizacao) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	
	@Override
	public DispositivoUnitizacao findById(Long id){
		DetachedCriteria dc = DetachedCriteria.forClass(DispositivoUnitizacao.class)
		.setFetchMode("tipoDispositivoUnitizacao", FetchMode.JOIN)
		.setFetchMode("macroZona", FetchMode.JOIN)
		.setFetchMode("macroZona.terminal", FetchMode.JOIN)
		.setFetchMode("macroZona.terminal.filial", FetchMode.JOIN)
		.setFetchMode("macroZona.terminal.pessoa", FetchMode.JOIN)
		.setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
		.setFetchMode("localizacaoFilial", FetchMode.JOIN)
		.setFetchMode("dispositivoUnitizacaoPai", FetchMode.JOIN)
		.setFetchMode("dispositivoUnitizacaoPai.tipoDispositivoUnitizacao", FetchMode.JOIN)
		.setFetchMode("empresa", FetchMode.JOIN)
		.setFetchMode("empresa.pessoa", FetchMode.JOIN)
		.add(Restrictions.eq("idDispositivoUnitizacao", id));
		return (DispositivoUnitizacao) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Busca a lista de dispositivos que contenham um pai com o parametro enviado.
	 * @param idDispositivoUnitizacaoPai
	 * @return lista de dispositivos unitizacao 
	 */
	public List<DispositivoUnitizacao> findDispositivosByIdPai(Long idDispositivoUnitizacaoPai) {
		DetachedCriteria dc = DetachedCriteria.forClass(DispositivoUnitizacao.class)
		.setFetchMode("tipoDispositivoUnitizacao", FetchMode.JOIN)
		.setFetchMode("macroZona", FetchMode.JOIN)
		.setFetchMode("localizacaoMercadoria", FetchMode.JOIN)
		.setFetchMode("localizacaoFilial", FetchMode.JOIN)
		.setFetchMode("dispositivoUnitizacaoPai", FetchMode.JOIN)
		.add(Restrictions.eq("dispositivoUnitizacaoPai.idDispositivoUnitizacao", idDispositivoUnitizacaoPai));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);			
	}
	
	
	
	public List<DispositivoUnitizacao> findByCarregamentoDescargaManifesto(Long idCarregamentoDescarga, Long idManifesto, List<Short> cdsLocalizacaoMercadoria, String tpManifesto) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());	
    	
    	dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
    	dc.setFetchMode("dispCarregIdentificados", FetchMode.JOIN);
    	dc.setFetchMode("dispCarregIdentificados.carregamentoDescarga", FetchMode.JOIN);
    	
    	dc.createAlias("dispCarregIdentificados.carregamentoDescarga", "carregamentoDescarga");
    	
    	dc.createAlias("dispCarregIdentificados.carregamentoPreManifesto", "carregamentoPreManifesto");
    	dc.createAlias("carregamentoPreManifesto.manifesto", "manifesto");
    	
    	dc.createAlias("localizacaoMercadoria", "localizacao");

    	dc.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga)); 

    	if(cdsLocalizacaoMercadoria!=null){
    	dc.add(Restrictions.in("localizacao.cdLocalizacaoMercadoria", cdsLocalizacaoMercadoria));
    	}
    	
		dc.add(Restrictions.eq("manifesto.tpManifesto", tpManifesto));
		dc.add(Restrictions.eq("manifesto.idManifesto", idManifesto));
		
		return super.findByDetachedCriteria(dc);	
	}
	
    public List<Map<String, Object>> findByControleCarga(Long idControleCarga){
    	StringBuffer sql = new StringBuffer()
    	.append("select new map(du.idDispositivoUnitizacao as idDispositivoUnitizacao, ")
    	.append("manifesto.tpManifesto as tpManifesto, ")
    	.append("manifesto.tpStatusManifesto as tpStatusManifesto ")
    	.append(")")
    	.append(" from ")
    	.append(DispositivoUnitizacao.class.getName()).append(" as du ")
    	.append("inner join du.dispCarregIdentificados dispCarregIdentificados ")
    	.append("inner join dispCarregIdentificados.carregamentoDescarga carregamentoDescarga ")
    	.append("inner join carregamentoDescarga.controleCarga controleCarga ")
    	.append("inner join dispCarregIdentificados.carregamentoPreManifesto carregamentoPreManifesto ")
    	.append("inner join carregamentoPreManifesto.manifesto manifesto ")
    	.append("where controleCarga.id = ? ")
    	.append("and manifesto.controleCarga.id = controleCarga.id ");
    	
    
    	return getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idControleCarga});
    }
	
	
	public ResultSetPage<DispositivoUnitizacao> findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> criteria = paginatedQuery.getCriteria();				
		
		String sql = this.getHqlPaginatedCabecalho() +  this.getHqlPaginated(criteria);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idEmpresa", MapUtils.getLong(criteria, "idEmpresa"));
		parameters.put("idTipoDispositivoUnitizacao", MapUtils.getLong(criteria, "idTipoDispositivoUnitizacao"));
		parameters.put("nrIdentificacao", MapUtils.getString(criteria, "nrIdentificacao"));
		parameters.put("tpNrIdentificacao", MapUtils.getString(criteria, "tpNrIdentificacao"));
		parameters.put("tpSituacao", MapUtils.getString(criteria, "tpSituacao"));
		parameters.put("idMacroZona", MapUtils.getLong(criteria, "idMacroZona"));
		parameters.put("idDispositivoUnitizacaoPai", MapUtils.getLong(criteria, "idDispositivoUnitizacaoPai"));

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_DISPOSITIVO_UNITIZACAO", Hibernate.LONG);
				sqlQuery.addScalar("DS_TIPO_DISP_UNITIZACAO", Hibernate.STRING);
				sqlQuery.addScalar("ID_TIPO_DISPOSITIVO_UNITIZACAO", Hibernate.LONG);
				sqlQuery.addScalar("NR_IDENTIFICACAO", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA", Hibernate.STRING);
				sqlQuery.addScalar("ID_EMPRESA", Hibernate.LONG);
				sqlQuery.addScalar("TP_SITUACAO", Hibernate.STRING);
				sqlQuery.addScalar("VOLUMES", Hibernate.INTEGER);
				sqlQuery.addScalar("DISPOSITIVOS", Hibernate.INTEGER);
	}
		};
			
		return  getAdsmHibernateTemplate().findPaginatedBySql(sql, paginatedQuery.getCurrentPage(), paginatedQuery.getPageSize(), parameters, configureSqlQuery);
	}

	@Override
	public Integer getRowCount(Map criteria) {				
		return getAdsmHibernateTemplate().getRowCountBySql(this.getHqlPaginated(criteria), criteria);
	}
	
	public String getHqlPaginatedCabecalho() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISPOSITIVO.ID_DISPOSITIVO_UNITIZACAO													");
		sql.append(" 	, VI18N(TIPODISPOSITIVO.DS_TIPO_DISP_UNITIZACAO_I) AS DS_TIPO_DISP_UNITIZACAO				");
		sql.append(" 	, DISPOSITIVO.ID_TIPO_DISPOSITIVO_UNITIZACAO												");
		sql.append(" 	, DISPOSITIVO.NR_IDENTIFICACAO																");
		sql.append(" 	, PESS.NM_PESSOA																			");
		sql.append(" 	, EMPRES.ID_EMPRESA																			");
		sql.append(" 	,(	select VI18N(VM.DS_VALOR_DOMINIO_I)														");
		sql.append(" 		from VALOR_DOMINIO VM																	");
		sql.append(" 		inner join DOMINIO DM on DM.ID_DOMINIO = VM.ID_DOMINIO									");
		sql.append(" 		where DM.NM_DOMINIO = 'DM_STATUS'														");
		sql.append(" 			and VM.VL_VALOR_DOMINIO = DISPOSITIVO.TP_SITUACAO									");
		sql.append(" 	) as TP_SITUACAO																			");
		sql.append("	,(	SELECT count(*) from VOLUME_NOTA_FISCAL VOLUMES											");
		sql.append(" 		WHERE VOLUMES.ID_DISPOSITIVO_UNITIZACAO = DISPOSITIVO.ID_DISPOSITIVO_UNITIZACAO			");
		sql.append(" 	) AS volumes																				");
		sql.append(" 	,(	select count(*)																			");
		sql.append(" 		from DISPOSITIVO_UNITIZACAO du															");
		sql.append(" 		where du.id_dispositivo_Unitizacao_pai = dispositivo.id_dispositivo_unitizacao			");
		sql.append(" 	) as dispositivos																			");

		return sql.toString();
	}
	
	public String getHqlPaginated(Map<String,Object> criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM DISPOSITIVO_UNITIZACAO dispositivo														");
		sql.append(" LEFT JOIN MACRO_ZONA macrozona ON MACROZONA.ID_MACRO_ZONA = DISPOSITIVO.ID_MACRO_ZONA			");
		sql.append(" LEFT JOIN LOCALIZACAO_MERCADORIA localizacao ON LOCALIZACAO.ID_LOCALIZACAO_MERCADORIA = DISPOSITIVO.ID_LOCALIZACAO_MERCADORIA									");
		sql.append(" LEFT JOIN FILIAL FILIALLOCALIZACAO ON FILIALLOCALIZACAO.ID_FILIAL = DISPOSITIVO.ID_LOCALIZACAO_FILIAL															");
		sql.append(" INNER JOIN TIPO_DISPOSITIVO_UNITIZACAO TIPODISPOSITIVO ON TIPODISPOSITIVO.ID_TIPO_DISPOSITIVO_UNITIZACAO = DISPOSITIVO.ID_TIPO_DISPOSITIVO_UNITIZACAO			");
		sql.append(" INNER JOIN EMPRESA empres ON EMPRES.ID_EMPRESA = DISPOSITIVO.ID_EMPRESA						");
		sql.append(" INNER JOIN pessoa pess ON pess.id_pessoa = empres.id_empresa									");
		sql.append(" WHERE 1 = 1																					");
		
		if(criteria.get("idEmpresa") != null) {
			sql.append(" and empres.id_empresa = :idEmpresa ");			
		}
		if(criteria.get("idTipoDispositivoUnitizacao") != null) {			
			sql.append(" and dispositivo.id_tipo_Dispositivo_unitizacao = :idTipoDispositivoUnitizacao ");			
		}	
		if(criteria.get("nrIdentificacao") == null) {			
			if (MapUtils.getString(criteria, "tpNrIdentificacao") != null)
				sql.append(" and dispositivo.nr_Identificacao like :tpNrIdentificacao ");
		} else {
			sql.append(" and dispositivo.nr_Identificacao like :nrIdentificacao ");			
		}	
		
		if(criteria.get("tpSituacao") != null) {			
			sql.append("and dispositivo.tp_Situacao = :tpSituacao ");			
		}	
		if(criteria.get("idMacroZona") != null) {			
			sql.append("and dispositivo.id_macro_Zona = :idMacroZona ");
			sql.append("and dispositivo.id_dispositivo_Unitizacao_Pai is null ");
		}
		if(criteria.get("idDispositivoUnitizacaoPai") != null) {			
			sql.append("and dispositivo.id_dispositivo_Unitizacao_Pai = :idDispositivoUnitizacaoPai ");
		}
		if(criteria.get("dispositivoVazio")!=null && Boolean.parseBoolean(criteria.get("dispositivoVazio").toString())) {
			sql.append("and (select count(dispositivo2.id_dispositivo) from Dispositivo_Unitizacao dispositivo2 where dispositivo2.id_dispositivo_Unitizacao_Pai = dispositivo.id_dispositivo) >0");			
		}
		return sql.toString();
	}

	public List<DispositivoUnitizacao> findByIdManifesto(Long idManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());	
    	dc.setFetchMode("dispCarregIdentificados", FetchMode.JOIN);
    	dc.setFetchMode("localizacaoMercadoria", FetchMode.JOIN);
    	dc.createAlias("dispCarregIdentificados.carregamentoPreManifesto", "carregamentoPreManifesto");
    	dc.createAlias("carregamentoPreManifesto.manifesto", "manifesto");
    	
		dc.add(Restrictions.eq("manifesto.idManifesto", idManifesto));
		
		return super.findByDetachedCriteria(dc);	

	}
	
	/**
	 * Busca a lista de dispositivos de unitização que está carregado no do controle de cargas. 
	 * @param idControleCarga
	 * @return
	 */
	public List<DispositivoUnitizacao> findListaDispositivoUnitizacaoCarregadoControleCarga(Long idControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());	
		dc.setFetchMode("dispCarregIdentificados", FetchMode.JOIN);
		dc.createAlias("dispCarregIdentificados.carregamentoDescarga", "carregamentoDescarga");
		dc.setFetchMode("carregamentoDescarga", FetchMode.JOIN);
		dc.createAlias("carregamentoDescarga.controleCarga", "controleCarga");
		dc.setFetchMode("controleCarga", FetchMode.JOIN);
		dc.createAlias("dispCarregIdentificados.dispositivoUnitizacao", "dispositivoUnitizacao");
		dc.setFetchMode("dispositivoUnitizacao", FetchMode.JOIN);
		dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
		
		return super.findByDetachedCriteria(dc);	
		
	}
	
	/**
	 * Atualiza a filial da localizacao do dispositivo pela filial do usuário logado
	 */
	public void executeAtualizarFilialLocalizacaoDispositivo(DispositivoUnitizacao dispositivoUnitizacao) {
		getAdsmHibernateTemplate().update(dispositivoUnitizacao);
		getAdsmHibernateTemplate().flush();		
	}
	
	public Integer getRowCountDispositivosUnitizacao(final TypedFlatMap criteria) {
		final StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) as ct from ( ");

		sql.append(getSqlFind());
		
		sql.append(" ) ");
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ct", Hibernate.INTEGER);
			}
		};
		
		final HibernateCallback hcb = getHcb(sql, csq, criteria, null, null);
		
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
		
		Integer ret = ((List<Integer>) getHibernateTemplate().executeFind(hcb)).get(0);
	
		return ret;
	}
	
	public List<Map<String, Object>> findDispositivosUnitizacao(final TypedFlatMap criteria) {
		final StringBuilder sql = new StringBuilder();
		sql.append(" select id_DISPOSITIVO_UNITIZACAO, ");
		sql.append("        NVL(VI18N(ds_tipo_disp_unitizacao_i, '").append(LocaleContextHolder.getLocale()).append("'), VI18N(ds_tipo_disp_unitizacao_i)) as DS_TIPO_DISP_UNITIZACAO, ");
		sql.append("        ID_TIPO_DISPOSITIVO_UNITIZACAO, ");
		sql.append("        NR_IDENTIFICACAO, ");
		sql.append("        NM_PESSOA, ");
		sql.append("        id_empresa, ");
		sql.append("        TP_SITUACAO, ");
		sql.append("        volumes, ");
		sql.append("        dispositivos, ");
		sql.append("        sgFilialLocalizacao, ");
		sql.append("        NVL(VI18N(dsLocalizacaoMercadoria, '").append(LocaleContextHolder.getLocale()).append("'), VI18N(dsLocalizacaoMercadoria)) as dsLocalizacaoMercadoria, ");
		sql.append("        dhUltimaMovimentacao ");
		sql.append("        from( ");

		sql.append(getSqlFind());
		
		sql.append(" ) ");
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_DISPOSITIVO_UNITIZACAO", Hibernate.LONG);
				sqlQuery.addScalar("DS_TIPO_DISP_UNITIZACAO", Hibernate.STRING);
				sqlQuery.addScalar("ID_TIPO_DISPOSITIVO_UNITIZACAO", Hibernate.LONG);
				sqlQuery.addScalar("NR_IDENTIFICACAO", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA", Hibernate.STRING);
				sqlQuery.addScalar("id_empresa", Hibernate.LONG);
				
				Properties propertiesTpDocumentoServico = new Properties();
				propertiesTpDocumentoServico.put("domainName","DM_STATUS");
				sqlQuery.addScalar("TP_SITUACAO", Hibernate.custom(DomainCompositeUserType.class,propertiesTpDocumentoServico));
				sqlQuery.addScalar("volumes", Hibernate.INTEGER);
				sqlQuery.addScalar("dispositivos", Hibernate.INTEGER);
				sqlQuery.addScalar("sgFilialLocalizacao", Hibernate.STRING);
				sqlQuery.addScalar("dsLocalizacaoMercadoria", Hibernate.STRING);
				sqlQuery.addScalar("dhUltimaMovimentacao", Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
		};
		Integer currentPage = criteria.get("_currentPage") != null ? Integer.parseInt(criteria.get("_currentPage").toString()) : null;
		Integer pageSize = criteria.get("_pageSize") != null ? Integer.parseInt(criteria.get("_pageSize").toString()) : null;
		final HibernateCallback hcb = getHcb(sql, csq, criteria, currentPage, pageSize);
		
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
		
		List<Object[]> list = getAdsmHibernateTemplate().executeFind(hcb);
	
		for (Object[] o: list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("id", o[0]);
			map.put("dsTipoDispositivoUnitizacao", o[1]);
			map.put("idTipoDispositivoUnitizacao", o[2]);
			map.put("nrIdentificacao", o[3]);
			map.put("nmPessoa", o[4]);
			map.put("idEmpresa", o[5]);
			map.put("tpSituacao", ((DomainValue)o[6]));
			map.put("volumes", o[7]);
			map.put("dispositivos", o[8]);
			map.put("sgFilialLocalizacao", o[10] != null && o[9] != null ? o[10] + " " + o[9] : "");
			map.put("dhUltimaMovimentacao", o[11]);
			
			toReturn.add(map);
			
		}
		
		return toReturn;
	}
	
	private HibernateCallback getHcb(final StringBuilder sql, final ConfigureSqlQuery csq, final TypedFlatMap criteria, final Integer pageNumber, final Integer pageSize) {
		final HibernateCallback hcb = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.setParameter("idEmpresa", criteria.getLong("empresa.idEmpresa"), Hibernate.LONG);
				query.setParameter("idFilial", criteria.getLong("filial.idFilial"), Hibernate.LONG);
				query.setParameter("idTipoDispositivoUnitizacao", criteria.getLong("tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao"), Hibernate.LONG);
				String tpNrIdentificacao = criteria.getString("tpNrIdentificacao");
				String nrIdentificacao = criteria.getString("nrIdentificacao");
				if (tpNrIdentificacao != null && nrIdentificacao != null) {
					nrIdentificacao = tpNrIdentificacao + "%" + nrIdentificacao;
				}
				query.setParameter("nrIdentificacao", nrIdentificacao == null ? null : "%"+nrIdentificacao+"%", Hibernate.STRING);
				query.setParameter("tpSituacao", criteria.get("tpSituacao.value"), Hibernate.STRING);
				query.setParameter("idMacroZona", criteria.getLong("idMacroZona"), Hibernate.LONG);
				query.setParameter("idDispositivoUnitizacaoPai", criteria.getLong("idDispositivoUnitizacaoPai"), Hibernate.LONG);
				query.setParameter("dispositivoVazio", criteria.get("dispositivoVazio") != null && Boolean.parseBoolean((String)criteria.get("dispositivoVazio")) ? 1 : 0, Hibernate.INTEGER);

            	csq.configQuery(query);
            	if (pageSize != null && pageNumber != null) {
            		query.setMaxResults(pageSize.intValue());
            		query.setFirstResult(pageSize.intValue() * (pageNumber.intValue() - 1));
            	}
		        return query.list();
			}
		};
		
		return hcb;
	}
	
	private StringBuilder getSqlFind() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * ");
		sql.append(" from ( ");
		sql.append(" SELECT 	du.id_DISPOSITIVO_UNITIZACAO, ");
		sql.append("         tdu.DS_TIPO_DISP_UNITIZACAO_I, ");
		sql.append("         tdu.ID_TIPO_DISPOSITIVO_UNITIZACAO, ");
		sql.append("         du.NR_IDENTIFICACAO, ");
		sql.append("         p.NM_PESSOA, ");
		sql.append("         du.id_empresa, ");
		sql.append("         du.TP_SITUACAO, ");
		sql.append("         (select count(*) from volume_nota_fiscal vnf where vnf.ID_DISPOSITIVO_UNITIZACAO = du.ID_DISPOSITIVO_UNITIZACAO) as volumes, ");
		sql.append("         (select count(*) from dispositivo_unitizacao duf where duf.ID_DISPOSITIVO_UNITIZACAO_PAI = du.ID_DISPOSITIVO_UNITIZACAO) as dispositivos, ");
		sql.append("         fl.SG_FILIAL  as sgFilialLocalizacao, ");
		sql.append("         lm.DS_LOCALIZACAO_MERCADORIA_I as dsLocalizacaoMercadoria, ");
		sql.append("         (SELECT max(edu.DH_EVENTO) FROM evento_dispositivo_unitizacao edu WHERE edu.ID_DISPOSITIVO_UNITIZACAO = du.ID_DISPOSITIVO_UNITIZACAO GROUP BY edu.ID_DISPOSITIVO_UNITIZACAO) as dhUltimaMovimentacao ");
		sql.append(" FROM DISPOSITIVO_UNITIZACAO du ");
		sql.append("      inner join tipo_dispositivo_unitizacao tdu on tdu.ID_TIPO_DISPOSITIVO_UNITIZACAO = du.ID_TIPO_DISPOSITIVO_UNITIZACAO ");
		sql.append("      inner join pessoa p on p.id_pessoa = du.id_empresa ");
		sql.append("      left join filial fl on fl.id_filial = du.id_localizacao_filial ");
		sql.append("      left join localizacao_mercadoria lm on lm.id_localizacao_mercadoria = du.id_localizacao_mercadoria ");
		sql.append(" where nvl(:idEmpresa, 0) in (0,du.id_empresa) ");
		sql.append("   and nvl(:idFilial, 0) in (0,du.id_localizacao_filial) ");
		sql.append("   and nvl(:idTipoDispositivoUnitizacao, 0) in (0, tdu.ID_TIPO_DISPOSITIVO_UNITIZACAO) ");
		sql.append("   and ((:nrIdentificacao is null) or  du.NR_IDENTIFICACAO like :nrIdentificacao||'%') ");
		sql.append("   and nvl(:tpSituacao, 'xyz') in ('xyz', du.tp_situacao) ");
		sql.append("   and ((:idMacroZona is null) or  du.ID_MACRO_ZONA = :idMacroZona and du.ID_DISPOSITIVO_UNITIZACAO_PAI is null) ");
		sql.append("   and nvl(:idDispositivoUnitizacaoPai, 0) in (0, du.ID_DISPOSITIVO_UNITIZACAO_PAI) ");
		sql.append(" ) a ");
		sql.append(" where a.dispositivos >= nvl(:dispositivoVazio,0) ");
		
		return sql;
	}
	
	
	public String findUltimoItenficadorPorEmpresa(String cnpj, Long idTipoDipositivo) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("cnpj", cnpj);
        parameters.put("idTipoDipositivo", idTipoDipositivo);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select max(dis.NR_IDENTIFICACAO) as NR_IDENTIFICACAO  \n");
		sql.append(" from DISPOSITIVO_UNITIZACAO dis \n");
		sql.append(" inner join  EMPRESA emp on (dis.ID_EMPRESA = emp.ID_EMPRESA) \n");
		sql.append(" inner join PESSOA pessoa on (emp.ID_EMPRESA =pessoa.ID_PESSOA) \n");
		sql.append(" left join Filial fil on (emp.ID_FILIAL_TOMADOR_FRETE  =fil.ID_FILIAL)\n");
		sql.append(" WHERE lower(pessoa.NR_IDENTIFICACAO) = :cnpj \n");
		sql.append("  and dis.ID_TIPO_DISPOSITIVO_UNITIZACAO = :idTipoDipositivo \n");
		
		 List queryResult = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, new ConfigureSqlQuery() {
	            @Override
	            public void configQuery(SQLQuery query) {
	                query.addScalar("NR_IDENTIFICACAO", Hibernate.STRING);	                
	            }
	        });
		 
		 String  valor  = (String) queryResult.get(0);
		 return valor ;
	}

}