package com.mercurio.lms.expedicao.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.util.ConstantesAwb;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CtoAwbDAO extends BaseCrudDao<CtoAwb, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CtoAwb.class;
    }

    /**
     * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
     * @param idDoctoServico
     * @param idAwb
     * @return <b>CtoAwb</b>
     */
    public CtoAwb findCtoAwb(Long idDoctoServico, Long idAwb) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("conhecimento", "c");
    	dc.createAlias("awb", "awb");
    	dc.add(Restrictions.eq("c.idDoctoServico", idDoctoServico));
    	dc.add(Restrictions.eq("awb.idAwb", idAwb));

		return (CtoAwb) getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    public List<Awb> findCtoAwbBydDoctoServico(Long idDoctoServico){
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select a ")
    	.append(" from ").append(getPersistentClass().getName()).append(" as ca ")
    	.append(" join ca.awb as a ")
    	.append(" join ca.conhecimento as c ")
    	.append(" join fetch a.ciaFilialMercurio cfm ")
    	.append(" join fetch cfm.empresa ")
    	.append(" where c.idDoctoServico = ? ")
    	.append(" and a.tpStatusAwb = 'E' ");
    	
    	List data = getAdsmHibernateTemplate().find(hql.toString(), idDoctoServico);
    	List<Awb> result = new ArrayList<Awb>();
    	if(data != null && !data.isEmpty()) {
    		for(int i = 0;i<data.size();i++) {
    			Awb awb = (Awb) data.get(i);
    			result.add((Awb) awb);
    		}
    		return result;
    	}
    	return result;
    }

    
    public List findByIdDoctoServico(Long idDoctoServico) {
    	DetachedCriteria dc = createDetachedCriteria()
    	.createAlias("conhecimento", "c")
    	.add(Restrictions.eq("c.idDoctoServico", idDoctoServico));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public Conhecimento findByIdDoctoServicoAwbEmitido(Long idDoctoServico) {
    	StringBuilder hql = new StringBuilder();
    	hql.append(" select c ")
    	.append(" from ").append(getPersistentClass().getName()).append(" as ca ")
    	.append(" join ca.awb as a ")
    	.append(" join ca.conhecimento as c ")
    	.append(" where c.idDoctoServico = ? ")
    	.append(" and a.tpStatusAwb = 'E' ");
    	return (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[] {idDoctoServico});
    }
    
    public List findByIdAwb(Long idAwb) {
    	StringBuilder hql = new StringBuilder()
    	.append(" from ").append(getPersistentClass().getName()).append(" as ca ")
    	.append(" join ca.awb as a ")
    	.append(" join ca.conhecimento as c ")
    	.append(" join c.localizacaoMercadoria as lm ")
    	.append(" join c.servico as s ")
    	.append(" left outer join c.aeroportoByIdAeroportoDestino as ad ")
    	.append(" left outer join c.aeroportoByIdAeroportoOrigem ao")
    	.append(" join c.filialByIdFilialOrigem as fo ")
    	.append(" join c.filialByIdFilialDestino as fd ")
    	.append(" left outer join fd.aeroporto as fda ")
    	.append(" where a.idAwb = ? ");
    	
    	List data = getAdsmHibernateTemplate().find(hql.toString(), idAwb);
    	if(data != null && !data.isEmpty()) {
    		List result = new ArrayList();
    		for(int i = 0;i<data.size();i++) {
    			Object[] obj = (Object[]) data.get(i);
    			result.add((CtoAwb) obj[0]);
    		}
    		return result;
    	}
    	
    	return null;
    }
    
    public void removeByIdAwb(Long idAwb) {
    	StringBuilder hql = new StringBuilder()
    	.append(" delete ").append(getPersistentClass().getName())
    	.append(" where awb = :id");
    	
    	Awb awb = new Awb();
    	awb.setIdAwb(idAwb);
    	
    	getAdsmHibernateTemplate().removeById(hql.toString(), awb);
    }

	public List<Awb> findAwbByCtoAwb(Long idCtoAwb) {
		StringBuilder hql = new StringBuilder();
   
		hql.append("SELECT a");
		hql.append("  FROM ");
		hql.append(getPersistentClass().getName());
		hql.append(" as ca ");
		hql.append("  JOIN ca.awb as a ");
		hql.append(" WHERE ca.idCtoAwb = :idCtoAwb ");
		hql.append(" and a.tpStatusAwb != :tpStatusAwb");

		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idCtoAwb", idCtoAwb);
		criteria.put("tpStatusAwb", ConstantesAwb.TP_STATUS_CANCELADO);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);		
	}
	
	public Awb findAwbByIdCto(Long idCto, String tpStatusAwb) {
		StringBuilder hql = new StringBuilder();
		List<Object> parameters = new ArrayList<Object>();
   
		hql.append("SELECT awb");
		hql.append("  FROM ");
		hql.append(getPersistentClass().getName());
		hql.append(" as ca ");
		hql.append("  JOIN ca.awb as awb ");
		hql.append("  JOIN ca.conhecimento as con ");		
		hql.append(" WHERE con.id = ? ");
		parameters.add(idCto);
		hql.append(" AND con.tpSituacaoConhecimento != 'C' ");
		hql.append(" AND awb.tpStatusAwb != 'C' ");		
		
		if (tpStatusAwb != null) {			
			hql.append(" AND awb.tpStatusAwb = ? ");
			parameters.add(tpStatusAwb);
		}
		
		hql.append(" order by awb.idAwb desc ");
		
		List<Awb> list = getAdsmHibernateTemplate().find(hql.toString(), parameters.toArray());	
		
		if(list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public ResultSetPage findPaginatedPreAwbConferencia(Long idAwb, FindDefinition findDef) {
		StringBuilder sql = getSqlConferirAwbPreAwb();

		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("nmFilialOrigemPA", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServicoPA", Hibernate.LONG);
				sqlQuery.addScalar("pesoRealPA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("pesoCubadoPA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("qtdVolumesPA", Hibernate.INTEGER);
				sqlQuery.addScalar("idConhecimento", Hibernate.LONG);
				sqlQuery.addScalar("ctoIdCtoAwb", Hibernate.LONG);
				sqlQuery.addScalar("preCtoIdCtoAwb", Hibernate.LONG);
				sqlQuery.addScalar("nrPreAwbPA", Hibernate.LONG);
				sqlQuery.addScalar("ciaAereaPA", Hibernate.STRING);
				sqlQuery.addScalar("sgAeroportoOrigemPA", Hibernate.STRING);
				sqlQuery.addScalar("sgAeroportoDestinoPA", Hibernate.STRING);
			}
		};

		Object[] param = {idAwb};

		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), param, confSql);
	}
	
	public Integer getRowCount(Map criteria) {
		List param = new ArrayList();
		param.add((Long)criteria.get("idAwb"));
		StringBuilder sql = getSqlConferirAwbPreAwb();
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), param.toArray());
	}
	
	private StringBuilder getSqlConferirAwbPreAwb(){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT FO.SG_FILIAL    AS nmFilialOrigemPA,	")
		.append(" DS.NR_DOCTO_SERVICO       AS nrDoctoServicoPA,	")
		.append(" DS.PS_REAL                AS pesoRealPA,			")
		.append(" DS.PS_CUBADO_REAL         AS pesoCubadoPA,		")
		.append(" DS.QT_VOLUMES             AS qtdVolumesPA,		")
		.append(" CTO_AWB.ID_CONHECIMENTO   AS idConhecimento,		")
		.append(" CTO_AWB.ID_CTO_AWB        as ctoIdCtoAwb, 		")
		.append(" PRECTOAWB.ID_CTO_AWB      as preCtoIdCtoAwb, 		")
		.append(" PREAWB.ID_AWB             AS nrPreAwbPA, 			")
		.append(" PCFM.NM_PESSOA            AS ciaAereaPA, 			")
		.append(" AOR.SG_AEROPORTO        	AS sgAeroportoOrigemPA, ")
		.append(" ADE.SG_AEROPORTO 	        AS sgAeroportoDestinoPA ")
		.append(" FROM AWB	")
		.append(" INNER JOIN CTO_AWB                      ON AWB.ID_AWB = CTO_AWB.ID_AWB	")
		.append(" INNER JOIN DOCTO_SERVICO DS             ON DS.ID_DOCTO_SERVICO = CTO_AWB.ID_CONHECIMENTO	")
		.append(" INNER JOIN FILIAL FO                    ON DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL	")
		.append(" LEFT OUTER JOIN CTO_AWB PRECTOAWB       ON DS.ID_DOCTO_SERVICO = PRECTOAWB.ID_CONHECIMENTO AND PRECTOAWB.ID_AWB <> AWB.ID_AWB		")
		.append(" LEFT OUTER JOIN AWB PREAWB              ON PREAWB.ID_AWB = PRECTOAWB.ID_AWB AND PREAWB.TP_STATUS_AWB = 'P'	")
		.append(" LEFT OUTER JOIN CIA_FILIAL_MERCURIO CFM ON PREAWB.ID_CIA_FILIAL_MERCURIO = CFM.ID_CIA_FILIAL_MERCURIO 	")
		.append(" LEFT OUTER JOIN PESSOA PCFM             ON CFM.ID_EMPRESA = PCFM.ID_PESSOA 	")
		.append(" LEFT OUTER JOIN AEROPORTO AOR           ON PREAWB.ID_AEROPORTO_ORIGEM = AOR.ID_AEROPORTO		") 
		.append(" LEFT OUTER JOIN AEROPORTO ADE           ON PREAWB.ID_AEROPORTO_DESTINO = ADE.ID_AEROPORTO		")
		.append(" WHERE AWB.ID_AWB = ?		")
		.append(" AND   AWB.TP_STATUS_AWB = 'E' ");
		return sql;
	}

	public void updateConferenciaPreAwb(final Long idCtoAwb, final Long idCtoPreAwb) {
		HibernateCallback hcb = new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws SQLException {
				String sql = " update cto_awb set ID_CTO_PREAWB = :ID_CTO_PREAWB where id_cto_awb = :ID_CTO_AWB";
				
				SQLQuery query = session.createSQLQuery(sql);
				query.setLong("ID_CTO_AWB", idCtoAwb);
				query.setLong("ID_CTO_PREAWB", idCtoPreAwb);

				query.executeUpdate();
				return null;
			}
			
		};
		
		getAdsmHibernateTemplate().execute(hcb);
	}
	
	public List<Map<String, Object>> findAwbForDocto(Long idDoctoServico) {
		StringBuilder s = new StringBuilder();
		
		s.append("SELECT AWB.ID_AWB AS ID_AWB, ");
		s.append("  AWB.NR_AWB      AS NR_AWB, ");
		s.append("  AWB.DS_SERIE    AS DS_SERIE, ");
		s.append("  AWB.DV_AWB      AS DV_AWB, ");
		s.append("  EMP.SG_EMPRESA  AS SG_EMPRESA ");
		s.append("FROM CTO_AWB CA ");
		s.append("LEFT JOIN LIBERA_AWB_COMPLEMENTAR LAC ");
		s.append("ON CA.ID_AWB = LAC.ID_AWB_ORIGINAL ");
		s.append("JOIN AWB AWB ");
		s.append("ON AWB.ID_AWB IN (CA.ID_AWB, LAC.ID_AWB_COMPLEMENTAR) ");
		s.append("JOIN CIA_FILIAL_MERCURIO CFM ");
		s.append("ON CFM.ID_CIA_FILIAL_MERCURIO = AWB.ID_CIA_FILIAL_MERCURIO ");
		s.append("JOIN EMPRESA EMP ");
		s.append("ON EMP.ID_EMPRESA        = CFM.ID_EMPRESA ");
		s.append("WHERE CA.ID_CONHECIMENTO = :idDoctoServico ");
		s.append("AND AWB.TP_STATUS_AWB    = 'E' ");
		s.append("ORDER BY DS_SERIE, ");
		s.append("  NR_AWB, ");
		s.append("  DV_AWB");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = new TypedFlatMap();		
       	params.put("idDoctoServico", idDoctoServico);       	
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(s.toString(), params, getConfigureSqlQueryAwbForDocto());
	}

	private ConfigureSqlQuery getConfigureSqlQueryAwbForDocto() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_AWB", Hibernate.LONG);
				sqlQuery.addScalar("NR_AWB", Hibernate.LONG);				
				sqlQuery.addScalar("DS_SERIE", Hibernate.STRING);
				sqlQuery.addScalar("DV_AWB", Hibernate.INTEGER);
				sqlQuery.addScalar("SG_EMPRESA", Hibernate.STRING);
			}
		};
		
		return csq;
	}

	public List<Map<String, Object>> findDoctosForReport(Long idAwb, Integer mod, Long rowIni) {
		StringBuilder s = new StringBuilder();		
		
		s.append("SELECT * ");
		s.append("FROM ");
		s.append("  (SELECT MOD(ROW_NUMBER() OVER (ORDER BY DS.ID_DOCTO_SERVICO), 2) AS MOD, ");
		s.append("  ROW_NUMBER() OVER (ORDER BY DS.ID_DOCTO_SERVICO)				 AS LINHA, ");
		s.append("    'DACTE'                                                        AS TP_DOC, ");
		s.append("    PES.NR_IDENTIFICACAO                                           AS CNPJ_CPF_EMITENTE, ");
		s.append("    ''                                                             AS SERIE, ");
		s.append("    MDE.NR_CHAVE                                                   AS NRO_DOC ");
		s.append("  FROM DOCTO_SERVICO DS ");
		s.append("  JOIN PESSOA PES ");
		s.append("  ON DS.ID_CLIENTE_REMETENTE = PES.ID_PESSOA ");
		s.append("  JOIN CTO_AWB CA ");
		s.append("  ON DS.ID_DOCTO_SERVICO = CA.ID_CONHECIMENTO ");
		s.append("  JOIN MONITORAMENTO_DOC_ELETRONICO MDE ");
		s.append("  ON DS.ID_DOCTO_SERVICO = MDE.ID_DOCTO_SERVICO ");
		s.append("  WHERE CA.ID_AWB        = :idAwb ");
		s.append("  ORDER BY DS.ID_DOCTO_SERVICO ");
		s.append("  ) ");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = new TypedFlatMap();
		s.append(" WHERE 1=1 ");
		if(mod != null){
			s.append(" AND MOD = :mod ");
			params.put("mod", mod);
			if(rowIni != null){
				s.append(" AND LINHA < :linha ");
				params.put("linha", rowIni);
			}
		}
		else if(rowIni != null){
			s.append(" AND LINHA >= :linha ");
			params.put("linha", rowIni);
		}
		
       	params.put("idAwb", idAwb);
       	
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(s.toString(), params, getConfigureSqlQueryDadosAwb());
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryDadosAwb() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("LINHA", Hibernate.STRING);
				sqlQuery.addScalar("TP_DOC", Hibernate.STRING);				
				sqlQuery.addScalar("CNPJ_CPF_EMITENTE", Hibernate.STRING);
				sqlQuery.addScalar("SERIE", Hibernate.STRING);
				sqlQuery.addScalar("NRO_DOC", Hibernate.STRING);
			}
		};
		
		return csq;
	}

	
	public List<CtoAwb> findByAwbAndPedidoColeta(Long idAwb, Long idPedidoColeta) {
		StringBuilder sql = new StringBuilder();
		
		sql
			.append("SELECT cto ")
			.append("FROM ")
			.append(CtoAwb.class.getSimpleName()).append(" as cto ")
			.append(" JOIN cto.conhecimento con ")
			.append(" JOIN cto.awb aw ")
			.append(" JOIn con.detalheColetas as dc")
			.append(" JOIN dc.pedidoColeta pc ")
			.append(" WHERE ")
			.append(" aw.idAwb = :idAwb ")
			.append(" AND pc.idPedidoColeta = :idPedidoColeta ");
		
		TypedFlatMap criteria = new  TypedFlatMap();
		criteria.put("idAwb", idAwb);
		criteria.put("idPedidoColeta", idPedidoColeta);
		
		
		List<CtoAwb> l =getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
		
		return l;
	}
	
	public List<Awb> findByPedidoColeta(Long idPedidoColeta) {
		StringBuilder sql = new StringBuilder();
		
		sql
			.append("SELECT aw ")
			.append("FROM ")
			.append(CtoAwb.class.getSimpleName()).append(" as cto ")
			.append(" JOIN cto.conhecimento con ")
			.append(" JOIN cto.awb aw ")
			.append(" JOIn con.detalheColetas as dc")
			.append(" JOIN dc.pedidoColeta pc ")
			.append(" WHERE ")
			.append(" pc.idPedidoColeta = :idPedidoColeta ");
		
		TypedFlatMap criteria = new  TypedFlatMap();
		criteria.put("idPedidoColeta", idPedidoColeta);
		
		
		List<Awb> l =getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
		
		return l;
	}
}