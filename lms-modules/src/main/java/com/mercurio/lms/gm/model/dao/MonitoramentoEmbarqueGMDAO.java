package com.mercurio.lms.gm.model.dao;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Hibernate;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;

public class MonitoramentoEmbarqueGMDAO extends BaseCrudDao<Carregamento, Long>{

	@Override
	protected Class getPersistentClass() {
		return CabecalhoCarregamento.class;
	}

	
	@Override
	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = this.getDetachedCriteria(criteria, Boolean.TRUE);
		Integer rowCountQuery = Integer.valueOf(getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sql.getCriteria(), null).toString());
		
		return rowCountQuery;
	}
	
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
	 
		SqlTemplate sql = this.getDetachedCriteria(criteria, Boolean.FALSE);
			
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_CARREGAMENTO", Hibernate.INTEGER);
				sqlQuery.addScalar("FROTA_VEICULO", Hibernate.STRING);
				sqlQuery.addScalar("PLACA_VEICULO", Hibernate.STRING);	
				sqlQuery.addScalar("TOTAL_VOLUMES", Hibernate.LONG);
				sqlQuery.addScalar("TOTAL_PESO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("ROTA_CARREGAMENTO", Hibernate.STRING);
				sqlQuery.addScalar("TIPO_CARREGAMENTO", Hibernate.STRING);
				Properties propertiesCodigoStatus = new Properties();
				propertiesCodigoStatus.put("domainName","DM_STATUS_CARREGAMENTO");
    			sqlQuery.addScalar("CODIGO_STATUS",Hibernate.custom(DomainCompositeUserType.class,propertiesCodigoStatus));
				sqlQuery.addScalar("DATA_INICIO", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DATA_FIM", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("CODIGO_VOLUME", Hibernate.STRING);
				sqlQuery.addScalar("RESPONSAVEL", Hibernate.STRING);
				sqlQuery.addScalar("HORARIO_CORTE", Hibernate.TIME);
			}
		};
		
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria(), csq);
		
	}
	
	
	@SuppressWarnings("deprecation")
	private SqlTemplate getDetachedCriteria(Map criteria, boolean isCount) {
			
		SqlTemplate sql = new SqlTemplate();
		
		if(isCount) {
			sql.addProjection("count(*) FROM ( SELECT 0 ");
		}else {
			sql.addProjection("c.ID_CARREGAMENTO","ID_CARREGAMENTO");
			sql.addProjection("c.FROTA_VEICULO","FROTA_VEICULO");
			sql.addProjection("c.PLACA_VEICULO","PLACA_VEICULO");

			//Ajustes JIRA LMS-5875
			sql.addProjection(
					" (SELECT sum(tca.total_volumes) FROM total_carregamento tca 						" +
					"		WHERE tca.id_cabecalho_carregamento IN (									" +
					"				SELECT DISTINCT (tc.id_cabecalho_carregamento)						" +
					"				FROM TOTAL_CARREGAMENTO tc, VOLUME v								" +
					"				WHERE v.ID_CARREGAMENTO = c.id_carregamento							" +
					"					AND v.ID_CABECALHO_CARREGAMENTO = tc.ID_CABECALHO_CARREGAMENTO	" +
					"					AND v.CODIGO_STATUS NOT IN ('4', '5'))							" +
					"		)																			","TOTAL_VOLUMES");

			sql.addProjection(
					"(SELECT sum(tca.total_peso) FROM total_carregamento tca							" +
					"		WHERE tca.id_cabecalho_carregamento IN (									" +
					"				SELECT DISTINCT (tc.id_cabecalho_carregamento)						" +
					"				FROM TOTAL_CARREGAMENTO tc, VOLUME v								" +
					"				WHERE v.ID_CARREGAMENTO = c.id_carregamento							" +
					"					AND v.ID_CABECALHO_CARREGAMENTO = tc.ID_CABECALHO_CARREGAMENTO	" +
					"					AND v.CODIGO_STATUS NOT IN ('4', '5'))							" +
					"		)																			","TOTAL_PESO");
			
			sql.addProjection("c.ROTA_CARREGAMENTO","ROTA_CARREGAMENTO");
			sql.addProjection("c.TIPO_CARREGAMENTO","TIPO_CARREGAMENTO");
			sql.addProjection("c.CODIGO_STATUS","CODIGO_STATUS");
			sql.addProjection("c.DATA_INICIO","DATA_INICIO");
			sql.addProjection("c.DATA_FIM","DATA_FIM");
			
			sql.addProjection("(Select COUNT(v.CODIGO_VOLUME) from VOLUME v" +
					" WHERE v.id_carregamento = c.id_carregamento" +
					" AND v.CODIGO_STATUS NOT IN ('4','5'))","CODIGO_VOLUME");
					
			sql.addProjection("u.NM_USUARIO","RESPONSAVEL");
			
			sql.addProjection("re.HORARIO_CORTE","HORARIO_CORTE");
		}
		
		sql.addInnerJoin("CARREGAMENTO c");
		
		sql.addInnerJoin("USUARIO u on u.ID_USUARIO = c.MATRICULA_CHEFIA");
		sql.addInnerJoin("ROTA_EMBARQUE re on re.SIGLA_ROTA = c.ROTA_CARREGAMENTO");
		
		sql.addLeftOuterJoin("VOLUME v on c.ID_CARREGAMENTO = v.ID_CARREGAMENTO");
		sql.addLeftOuterJoin("CABECALHO_CARREGAMENTO cc on v.ID_CABECALHO_CARREGAMENTO = cc.ID_CABECALHO_CARREGAMENTO");
		sql.addLeftOuterJoin("DETALHE_CARREGAMENTO dc on cc.ID_CABECALHO_CARREGAMENTO = dc.ID_CABECALHO_CARREGAMENTO");
		sql.addLeftOuterJoin("TOTAL_CARREGAMENTO tc on cc.ID_CABECALHO_CARREGAMENTO = tc.ID_CABECALHO_CARREGAMENTO");
		
		// Inicio dos testes dos filtros
		if(!"".equals(criteria.get("codigoVolume"))){
			sql.addCriteria("v.codigo_volume", "like", criteria.get("codigoVolume"));		
		}
		
		if(criteria.get("rotaCarregamento")!= null && !(criteria.get("rotaCarregamento").equals(""))){
			sql.addCriteria("upper(c.rota_carregamento)", "like", criteria.get("rotaCarregamento").toString().toUpperCase());
		}
		
		if(criteria.get("idMeioTransporte")!= null && !(criteria.get("idMeioTransporte").equals(""))){
			sql.addCriteria("c.frota_veiculo", "=", criteria.get("idMeioTransporte"));
		}
		
		if(criteria.get("mapaCarregamento")!= null && !(criteria.get("mapaCarregamento").equals(""))){
			Long mapaCarregamento = Long.parseLong((String) criteria.get("mapaCarregamento"));
			sql.addCriteria("cc.mapa_carregamento", "=", mapaCarregamento);
		}
		
		if((criteria.get("dataDisponivel")!= null) && (!criteria.get("dataDisponivel").equals(""))){
			sql.addCriteria("to_char(cc.data_disponivel, 'YYYY-MM-DD')","=", criteria.get("dataDisponivel"));
		}
		
		if(criteria.get("matriculaChefia")!= null && !(criteria.get("matriculaChefia").equals(""))){
			Long matriculaChefia = Long.parseLong((String) criteria.get("matriculaChefia"));
			sql.addCriteria("c.matricula_chefia", "=", matriculaChefia);
		}
		
		if(criteria.get("statusCarregamento")!= null && !(criteria.get("statusCarregamento").equals(""))){
			sql.addCriteria("c.codigo_status", "=", criteria.get("statusCarregamento"));
		}else if(criteria.get("rotaCarregamento").equals("") && 
				criteria.get("idMeioTransporte").equals("") && 
				criteria.get("mapaCarregamento").equals("") && 
				criteria.get("dataDisponivel").equals("") &&  
				criteria.get("codigoVolume").equals("") && 
				criteria.get("matriculaChefia").equals("")){
					
			sql.addCustomCriteria("c.codigo_status = '2' ");
		} 
 		// Fim dos testes dos filtros
		sql.addGroupBy("c.id_carregamento");
		sql.addGroupBy("c.frota_veiculo");
		sql.addGroupBy("c.placa_veiculo");
		sql.addGroupBy("c.rota_carregamento");
		sql.addGroupBy("c.tipo_carregamento");
		sql.addGroupBy("c.codigo_status");
		sql.addGroupBy("c.data_inicio");
		sql.addGroupBy("c.data_fim");
		sql.addGroupBy("u.nm_usuario");
		sql.addGroupBy("re.horario_corte");
		
		sql.addOrderBy("re.horario_corte desc");
		sql.addOrderBy("c.rota_carregamento");
		sql.addOrderBy("c.frota_veiculo" + (isCount ? ")" : ""));
		
		return sql;
	}
	
	  /**
	   * Método que retorna o ResultSetPage da grid da aba de detalhamento da tela de Monitoramento Embarque GM Sorocaba
	   * LMS-2781
	   * @param criteria
	   * @param findDef
	   * @return
	   */
		public ResultSetPage findPaginatedDetalheEmbarqueGM(Map criteria, FindDefinition findDef) {
			SqlTemplate sql = montaSqlTemplateDetalheEmbarqueGM(criteria, Boolean.FALSE);
			
			ConfigureSqlQuery csq = new ConfigureSqlQuery() {
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("mapaCarregamento",Hibernate.STRING);
					sqlQuery.addScalar("dataCriacao",Hibernate.custom(JodaTimeDateTimeUserType.class));
					sqlQuery.addScalar("dataDisponivel",Hibernate.custom(JodaTimeDateTimeUserType.class));
					
					sqlQuery.addScalar("totalVolume",Hibernate.INTEGER);
					sqlQuery.addScalar("totalPeso",Hibernate.INTEGER);
					sqlQuery.addScalar("qtdVolumes",Hibernate.INTEGER);
					
				}
			};
			
			return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria(),csq);
		}
	
	/**
	 * Método que retorna o count da grid da aba de detalhamento  para a tela de Monitoramento Embarque GM Sorocaba
	 * LMS-2781
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountMpc(Map criteria) {
		SqlTemplate sql = montaSqlTemplateDetalheEmbarqueGM(criteria, Boolean.TRUE);	
		Integer rowCountQuery = Integer.valueOf(getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sql.getCriteria(), null).toString());
		
		return rowCountQuery;
	}

	/**
	 * Monta o SqlTemplate para a consulta da grid da aba de detalhamento da tela de Monitoramento Embarque GM Sorocaba
	 * LMS-2781
	 * @param criteria
	 * @return
	 */
	private SqlTemplate montaSqlTemplateDetalheEmbarqueGM(Map criteria, boolean isCount) {		
		SqlTemplate sql = new SqlTemplate();
		if(isCount) {
			sql.addProjection("count(*) FROM ( SELECT 0 ");
		}else {
			sql.addProjection("cc.MAPA_CARREGAMENTO", "mapaCarregamento");
			sql.addProjection("cc.DATA_CRIACAO", "dataCriacao");
			sql.addProjection("cc.DATA_DISPONIVEL", "dataDisponivel");
			sql.addProjection("tc.TOTAL_VOLUMES", "totalVolume");
			sql.addProjection("tc.TOTAL_PESO", "totalPeso");		
			sql.addProjection("count((SELECT v.codigo_volume FROM VOLUME v WHERE   v.codigo_volume = vo.codigo_volume AND v.codigo_status NOT IN (4,5)))", "qtdVolumes");
		}
		
		
		sql.addFrom("CABECALHO_CARREGAMENTO","cc");
		sql.addFrom("TOTAL_CARREGAMENTO","tc");		
		sql.addJoin("cc.ID_CABECALHO_CARREGAMENTO", "tc.ID_CABECALHO_CARREGAMENTO");
		
		sql.addFrom("VOLUME","vo");
		sql.addJoin("cc.ID_CABECALHO_CARREGAMENTO", "vo.ID_CABECALHO_CARREGAMENTO");
		
		sql.addFrom("CARREGAMENTO","ca");
		sql.addJoin("ca.ID_CARREGAMENTO", "vo.ID_CARREGAMENTO");
		
		sql.addCriteria("ca.ID_CARREGAMENTO", "=", Long.valueOf(criteria.get("idCarregamento").toString()));

		sql.addGroupBy("cc.MAPA_CARREGAMENTO");
		sql.addGroupBy("cc.DATA_CRIACAO");
		sql.addGroupBy("cc.DATA_DISPONIVEL");
		sql.addGroupBy("tc.TOTAL_VOLUMES");
		sql.addGroupBy("tc.TOTAL_PESO"  + (isCount ? ")" : ""));
		

		return sql;
	}
	
}
