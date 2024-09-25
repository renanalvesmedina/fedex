package com.mercurio.lms.vol.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vol.model.VolEventosCelular;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GerencialDAO extends AdsmDao {
	
	public List<Map<String, Object>> findDetalhamentoGerencial(TypedFlatMap criteria) {
		
		String sql = mountQueryDetalhamentoGerencial();
		ConfigureSqlQuery configQuery = configureSqlQueryDetalhamentoGerencial();
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), criteria, configQuery);
	}
	
	private ConfigureSqlQuery configureSqlQueryDetalhamentoGerencial() {
		 
		return new ConfigureSqlQuery() {

			@Override
			public void configQuery(SQLQuery sqlQuery) {
				
				sqlQuery.addScalar("GRUPOFROTA");
				sqlQuery.addScalar("FROTA");
				sqlQuery.addScalar("CONTROLECARGA");
				sqlQuery.addScalar("MANIFESTO");
				sqlQuery.addScalar("OPERACAO");
				sqlQuery.addScalar("TipoOperacao");
				sqlQuery.addScalar("EFICIENCIA", Hibernate.STRING);
				sqlQuery.addScalar("REALIZADO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("TOTAL", Hibernate.LONG);

			}};
	}

	private String mountQueryDetalhamentoGerencial() {
		StringBuilder sql = new StringBuilder();
		
		sql.append(mountWithQueryDetalhamento())
		.append(mountQueryEntregaDetalhamento())
		.append("UNION ALL ")
		.append(mountQueryColetaDetalhamento());
		
		return sql.toString();
	}

	private String mountQueryColetaDetalhamento() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ")
		.append("       grupo_frota                   AS grupoFrota ")
		.append(",      nr_frota || decode(tb.id_equipamento, null, '*', ' ') AS Frota ")
		.append(",      nrIdentificador ")
		.append(",      id_meio_transporte            AS idMeioTransporte ")
		.append(",      tb.sg_Filial || ' ' || ")
		.append("       LPAD(nr_controle_carga, 8, 0) AS ControleCarga ")
		.append(",      mc.id_manifesto_coleta        as id_manifesto ")
		.append(",      f.sg_Filial || ' ' || ")
		.append("       LPAD(mc.nr_manifesto, 8, 0)   AS Manifesto ")
		.append(",      '   Coleta'                      as Operacao ")
		.append(",      case when pc.tp_pedido_coleta = 'AE' ")
		.append("            then 'A' ")
		.append("            else 'R' ")
		.append("       end                           as TipoOperacao ")
		.append(",      sum( case when pc.tp_status_coleta not in('CA') ")
		.append("                 then 1 ")
		.append("                 else 0 ")
		.append("            end) AS vTotalManifestado ")
		.append(",      sum( case when pc.tp_status_coleta in('EX','RC') ")
		.append("                 then 1 ")
		.append("                 else 0 ")
		.append("            end) AS vTotalBaixas ")
		.append("FROM ")
		.append("          tmp tb, manifesto_coleta mc, filial f, pedido_coleta pc ")
		.append("where ")
		.append("          tb.id_controle_carga = mc.id_controle_carga ")
		.append("          and mc.id_filial_origem = f.id_filial ")
		.append("          and mc.id_manifesto_coleta = pc.id_manifesto_coleta ")
		.append("GROUP BY ")
		.append("       grupo_frota ")
		.append(",      nr_frota || decode(tb.id_equipamento, null, '*', ' ') ")
		.append(",      nrIdentificador ")
		.append(",      id_meio_transporte ")
		.append(",      tb.sg_Filial || ' ' || ")
		.append("       LPAD(nr_controle_carga, 8, 0) ")
		.append(",      mc.id_manifesto_coleta ")
		.append(",      f.sg_Filial || ' ' || ")
		.append("       LPAD(mc.nr_manifesto, 8, 0) ")
		.append(",      '   Coleta' ")
		.append(",      case when pc.tp_pedido_coleta = 'AE' ")
		.append("            then 'A' ")
		.append("            else 'R' ")
		.append("       end ")
		.append("   ) tb2 ")
		.append("order by controlecarga, manifesto	");
		
		return sql.toString();
	}
	
	private String mountQueryEntregaDetalhamento() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ")
		.append(" GRUPOFROTA ")
		.append(",FROTA ")
		.append(",CONTROLECARGA ")
		.append(",MANIFESTO ")
		.append(",OPERACAO ")
		.append(",TipoOperacao ")
		.append(",case when (vTotalManifestado) > 0 ")
		.append("      then trunc((vtotalBaixas  * 100) / vTotalManifestado) || '%' ")
		.append("      else 0   || '%' ")
		.append(" end EFICIENCIA ")
		.append(",VTOTALBAIXAS as REALIZADO ")
		.append(",VTOTALMANIFESTADO as TOTAL ")
		.append("from ( ")
		.append("SELECT ")
		.append("       grupo_frota                   AS grupoFrota ")
		.append(",      nr_frota || decode(tb.id_equipamento, null, '*', ' ') AS Frota ")
		.append(",      nrIdentificador ")
		.append(",      id_meio_transporte            AS idMeioTransporte ")
		.append(",      tb.sg_Filial || ' ' || ")
		.append("       LPAD(nr_controle_carga, 8, 0) AS ControleCarga ")
		.append(",      id_manifesto                  as id_manifesto ")
		.append(",      f.sg_Filial || ' ' || ")
		.append("       LPAD(me.nr_manifesto_entrega, 8, 0) AS Manifesto ")
		.append(",      '   Entrega'                           as Operacao ")
		.append(",      case when med.id_awb is not null ")
		.append("            then 'A' ")
		.append("            else 'R' ")
		.append("       end                                 as TipoOperacao ")
		.append(",      sum( case when med.tp_situacao_documento NOT IN ( 'CANC' ) ")
		.append("            then 1 ")
		.append("            else 0 end) AS vTotalManifestado ")
		.append(",      sum( case when med.tp_situacao_documento NOT IN ( 'CANC' ) and med.dh_ocorrencia IS NOT NULL ")
		.append("            then 1 ")
		.append("            else 0 end) AS vTotalBaixas ")
		.append("FROM ")
		.append("          tmp tb, manifesto m, manifesto_entrega me, filial f, manifesto_entrega_documento med ")
		.append("where ")
		.append("          tb.id_controle_carga = m.id_controle_carga ")
		.append("          and m.id_manifesto = me.id_manifesto_entrega ")
		.append("          and m.id_filial_origem = f.id_filial ")
		.append("          and me.id_manifesto_entrega = med.id_manifesto_entrega ")
		.append("GROUP BY ")
		.append("       grupo_frota ")
		.append(",      nr_frota || decode(tb.id_equipamento, null, '*', ' ') ")
		.append(",      nrIdentificador ")
		.append(",      id_meio_transporte ")
		.append(",      tb.sg_Filial || ' ' || ")
		.append("       LPAD(nr_controle_carga, 8, 0) ")
		.append(",      id_manifesto ")
		.append(",      f.sg_Filial || ' ' || ")
		.append("       LPAD(me.nr_manifesto_entrega, 8, 0) ")
		.append(",      '   Entrega' ")
		.append(",      case when med.id_awb is not null ")
		.append("            then 'A' ")
		.append("            else 'R' ")
		.append("       end ");
		
		return sql.toString();
	}

	private String mountWithQueryDetalhamento() {
		StringBuilder sql = new StringBuilder();
		sql
		.append("with tmp as ")
		.append("( ")
		.append("    SELECT DISTINCT ")
		.append("           mt.nr_frota                        AS nr_frota ")
		.append("    ,      mt.id_meio_transporte              AS id_meio_transporte ")
		.append("    ,      mt.nr_identificador                AS nrIdentificador ")
		.append("    ,      ve.id_equipamento                  AS id_equipamento ")
		.append("    ,      fi.sg_Filial                       AS sg_filial ")
		.append("    ,      cc.id_controle_carga               AS id_controle_carga ")
		.append("    ,      cc.nr_controle_carga               AS nr_controle_carga ")
		.append("    ,      gf.ds_nome                         AS grupo_frota ")
		.append("    FROM   meio_transporte             mt ")
		.append("    ,      grupo_frota_veiculo         gv ")
		.append("    ,      grupo_frota                 gf ")
		.append("    ,      grupo_frota_funcionario     gfu ")
		.append("    ,      filial                      fi ")
		.append("    ,      controle_carga              cc ")
		.append("    ,      equipamento                 ve ")
		.append("    WHERE cc.tp_controle_carga = 'C' ")
		.append("    and cc.tp_status_controle_carga not in ('FE', 'CA') ")
		.append("    and mt.id_meio_transporte = gv.id_meio_transporte ")
		.append("    and gv.id_grupo_frota = gf.id_grupo_frota ")
		.append("    and gfu.id_grupo_frota = gf.id_grupo_frota ")
		.append("    and gf.id_filial = fi.id_filial ")
		.append("    and mt.id_meio_transporte = cc.id_transportado ")
		.append("    and mt.id_meio_transporte = ve.id_meio_transporte(+) ")
		.append("    and   gf.id_filial = :idFilial ")
		.append("    and   gfu.id_usuario = :idUsuarioLogado ")
		.append("    and   gv.id_meio_transporte = :idMeioTransporte ")
		.append(")");
		return sql.toString();
	}

	public List findAcompanhamentoGerencial(TypedFlatMap criteria) {
		List filtro = new ArrayList();
		filtro.add(criteria.getLong("idFilial"));
		filtro.add(criteria.getLong("idFilial"));
			filtro.add(criteria.getLong("idGrupoFrota"));
		filtro.add(criteria.getLong("idGrupoFrota"));
		filtro.add(SessionUtils.getUsuarioLogado().getIdUsuario());
 
 
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("nrFrota",Hibernate.STRING);
				sqlQuery.addScalar("idMeioTransporte",Hibernate.LONG);
				sqlQuery.addScalar("idEquipamento",Hibernate.LONG);
				sqlQuery.addScalar("nrControleCarga",Hibernate.STRING);
				sqlQuery.addScalar("grupoFrota",Hibernate.STRING);
				sqlQuery.addScalar("vNaoPossuiCelular",Hibernate.STRING);
				//Feita correção para restringir ao eventos do controle de carga ao invés do veiculo             
				sqlQuery.addScalar("vQuantidadeChamadosFrota",Hibernate.LONG);
				// Horario do primeiro chamado
				sqlQuery.addScalar("vPrimeiroChamadoFrota",Hibernate.custom(JodaTimeDateTimeUserType.class));
				// Total de entregas
				sqlQuery.addScalar("vTotalEntregas",Hibernate.LONG);
				//Total de entregas baixadas
				sqlQuery.addScalar("vTotalEntregasBaixadas",Hibernate.LONG);
				//Total de coletas 
				sqlQuery.addScalar("vTotalColetas",Hibernate.LONG);
//				/Total de coletas baixadas
				sqlQuery.addScalar("vTotalColetasBaixadas",Hibernate.LONG);
				//Evento alterado para considerar os executados e retornados
				//Total de coletas automaticas
				sqlQuery.addScalar("vTotalColetasAutomaticas",Hibernate.LONG);
				//Total de tratativas  
				sqlQuery.addScalar("vTotalTratativas",Hibernate.LONG);
				// Última dh_ocorrencia das entregas baixadas    
				sqlQuery.addScalar("dhOcorrenciaEntrega",Hibernate.custom(JodaTimeDateTimeUserType.class));
				// Última dh_evento_coleta das coletas baixadas
				sqlQuery.addScalar("dhEventoColeta",Hibernate.custom(JodaTimeDateTimeUserType.class));
				
				sqlQuery.addScalar("primeiraDhOcorrenciaEntrega",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dsNumero",Hibernate.STRING);
				sqlQuery.addScalar("dhEnvio",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("nrIdentificador",Hibernate.STRING);
				//Menor dh de coleta agendada no estado de TR
				sqlQuery.addScalar("dhAgendamentoColeta",Hibernate.custom(JodaTimeDateTimeUserType.class));
				//Menor dh de entrega agendada não cancelada
				sqlQuery.addScalar("dhAgendamentoEntrega",Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
		};

		HibernateCallback hcb = findBySql(mountSqlAcompanhamentoGerencial(), filtro.toArray(),csq);
		return (List)getAdsmHibernateTemplate().execute(hcb); 
	}
	
	public static HibernateCallback findBySql(final String sql,final Object[] parametersValues,final ConfigureSqlQuery configQuery) {

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				
				// chama a impl que configura a SQLQuery.
				if (configQuery != null) {
					configQuery.configQuery(query);
				}
			
				if (parametersValues != null) {
					for (int i = 0; i < parametersValues.length; i++) {
						query.setParameter(i, parametersValues[i],Hibernate.LONG);
					}
				}
				return query.list();
			}
			
		};
		return hcb;
	}
	
	private String mountSqlAcompanhamentoGerencial() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT DISTINCT ")
		.append(" nr_frota                      AS nrFrota,")
		.append(" id_meio_transporte            AS idMeioTransporte,")
		.append(" id_equipamento                AS idEquipamento,")
		.append(" sg_Filial || ' ' || ")
		.append(" LPAD(nr_controle_carga, 8, 0) AS nrControleCarga,")
		.append(" grupo_frota                   AS grupoFrota,")
		.append(" decode(tb.id_equipamento, null, '*', ' ') AS vNaoPossuiCelular,")
		.append(" nvl(ds_numero, 0)             AS dsNumero, ")
		.append(" ( SELECT COUNT( 1 )")
		.append("	FROM   eventos_celular ec")
		.append("	WHERE  ec.id_controle_carga = tb.id_controle_carga")
		.append("	AND    ec.dh_atendimento IS NULL ) AS vQuantidadeChamadosFrota,")
		.append(" ( SELECT MIN( ec.dh_solicitacao )")
		.append("	FROM   eventos_celular ec")
		.append("	WHERE  ec.id_controle_carga = tb.id_controle_carga ) AS vPrimeiroChamadoFrota,")
		.append(" ( SELECT COUNT( 1 )")
		.append("	FROM   manifesto_entrega_documento med")
		.append("	WHERE  med.id_manifesto_entrega = tb.id_manifesto")
		.append("	AND    med.tp_situacao_documento NOT IN ( 'CANC' ) )AS vTotalEntregas,")
		.append(" ( SELECT COUNT( 1 )")
		.append("	FROM   manifesto_entrega_documento med")
		.append("	WHERE  med.id_manifesto_entrega = tb.id_manifesto")
		.append("	AND    med.dh_ocorrencia IS NOT NULL ) AS vTotalEntregasBaixadas,")
		.append(" ( SELECT COUNT( 1 )")
		.append("	FROM   pedido_coleta pc  ")
		.append("    WHERE  tb.id_manifesto_coleta = pc.id_manifesto_coleta ")
		.append("	AND    EXISTS ( SELECT 1 ")
		.append("		FROM   evento_coleta evc ")
		.append("		WHERE  pc.id_pedido_coleta = evc.id_pedido_coleta")
		.append("		AND    evc.tp_evento_coleta != 'CA' ")
		.append("		AND    ROWNUM = 1 ) ) AS vTotalColetas,")
		.append(" ( SELECT COUNT( 1 )")
		.append("	FROM   pedido_coleta pc  ")
		.append("	WHERE  tb.id_manifesto_coleta = pc.id_manifesto_coleta ")
		.append("	AND    EXISTS ( SELECT 1 ")
		.append("		FROM   evento_coleta evc ")
		.append("		WHERE  pc.id_pedido_coleta = evc.id_pedido_coleta")
		.append("		AND    evc.tp_evento_coleta IN ( 'RC', 'EX' ) ")
		.append("		AND    ROWNUM = 1 ) ) AS vTotalColetasBaixadas,")
		.append(" ( SELECT COUNT( 1 )")
		.append("	FROM   pedido_coleta pc  ")
		.append("	WHERE  tb.id_manifesto_coleta = pc.id_manifesto_coleta ")
		.append("	AND    pc.tp_modo_pedido_coleta IN ( 'AU' )")
		.append("	AND    EXISTS ( SELECT 1 ")
		.append("		FROM   evento_coleta evc ")
		.append("		WHERE  pc.id_pedido_coleta = evc.id_pedido_coleta")
		.append("		AND    evc.tp_evento_coleta IN ( 'RC', 'EX' )")
		.append("		AND    ROWNUM = 1 ) ) AS vTotalColetasAutomaticas,")
		.append(" ( SELECT COUNT( 1 )")
		.append("	FROM   eventos_celular ec ")
		.append("	WHERE  ec.id_controle_carga = tb.id_controle_carga    ")
		.append("	AND    ec.dh_atendimento IS NULL ) AS vTotalTratativas,")
		.append(" ( SELECT MAX( med.dh_ocorrencia )")
		.append("	FROM   manifesto_entrega_documento med")
		.append("	WHERE  med.id_manifesto_entrega = tb.id_manifesto")
		.append("	AND    med.tp_situacao_documento NOT IN ( 'CANC' ) ) AS dhOcorrenciaEntrega,")
		.append(" ( SELECT MAX( evc.dh_evento )")
		.append("	FROM   pedido_coleta pc")
		.append("	JOIN   evento_coleta evc ON pc.id_pedido_coleta = evc.id_pedido_coleta  ")
		.append("	WHERE  tb.id_manifesto_coleta = pc.id_manifesto_coleta")
		.append("	AND    evc.tp_evento_coleta IN ( 'EX' ) ) AS dhEventoColeta,")
		.append(" ( SELECT MIN( med.dh_ocorrencia )")
		.append("	FROM   manifesto_entrega_documento med")
		.append("	WHERE  med.id_manifesto_entrega = tb.id_manifesto")
		.append("	AND    med.tp_situacao_documento NOT IN ( 'CANC' ) ) AS primeiraDhOcorrenciaEntrega,")
		.append(" ( SELECT MIN( vlog.dh_envio )")
		.append("	FROM   log_envio_sms vlog")
		.append("	WHERE  vlog.id_equipamento = tb.id_equipamento ) AS dhEnvio,")
		.append("  nrIdentificador,         ")
		.append("  ( SELECT MIN( pc.dh_coleta_disponivel ) ")
		.append("   FROM   pedido_coleta pc ")
		.append("   JOIN   evento_coleta evc ON pc.id_pedido_coleta = evc.id_pedido_coleta ")  
        .append("   WHERE  tb.id_manifesto_coleta = pc.id_manifesto_coleta ")
        .append("   AND    evc.tp_evento_coleta IN ( 'TR' ) ) AS dhAgendamentoColeta, ")
        .append("   ( SELECT MIN( ae.hr_preferencia_inicial ) ")
        .append("   FROM   agendamento_entrega         ae ")
		.append("   JOIN   agendamento_docto_servico   ads  ON ae.id_agendamento_entrega = ads.id_agendamento_entrega ")
		.append("   JOIN   manifesto_entrega_documento med  ON med.id_docto_servico = ads.id_agendamento_docto_servico ")
		.append("   WHERE  med.id_manifesto_entrega = tb.id_manifesto ")
		.append("   AND    med.dh_ocorrencia IS NULL ")
		.append("   AND    med.tp_situacao_documento NOT IN ( 'CANC' ) ) AS dhAgendamentoEntrega ")		
		.append(" FROM   (")
		.append(" SELECT mt.nr_frota                 AS nr_frota,")
		.append("  mt.id_meio_transporte              AS id_meio_transporte,")
		.append("  mt.nr_identificador                AS nrIdentificador,")
		.append("  ve.id_equipamento                  AS id_equipamento,")
		.append("  fi.sg_Filial                       AS sg_filial,")
		.append("  cc.id_controle_carga               AS id_controle_carga,")
		.append("  cc.nr_controle_carga               AS nr_controle_carga,")
		.append("  gf.ds_nome                         AS grupo_frota,")
		.append("  ve.ds_numero                       AS ds_numero,")
		.append("  m.id_manifesto                     AS id_manifesto,  ")
		.append("  mc.id_manifesto_coleta             AS id_manifesto_coleta")
		.append(" FROM      meio_transporte             mt")
		.append(" JOIN      GRUPO_FROTA_VEICULO         gv   ON  mt.id_meio_transporte = gv.id_meio_transporte")
		.append(" JOIN      grupo_frota                 gf   ON  gv.id_grupo_frota = gf.id_grupo_frota")
		.append(" JOIN      GRUPO_FROTA_FUNCIONARIO     gfu  ON  gfu.id_grupo_frota = gf.id_grupo_frota")
		.append(" JOIN      filial                      fi   ON  gf.id_filial = fi.id_filial")
		.append(" JOIN      controle_carga              cc   ON  mt.id_meio_transporte = cc.id_transportado AND cc.tp_status_controle_carga not in ('FE', 'CA')")
		.append(" LEFT JOIN equipamento                 ve   ON  mt.id_meio_transporte = ve.id_meio_transporte")
		.append(" LEFT JOIN manifesto                   m    ON  m.id_controle_carga = cc.id_controle_carga")
		.append(" LEFT JOIN manifesto_coleta            mc   ON  mc.id_controle_carga = cc.id_controle_carga")
		.append(" WHERE cc.tp_controle_carga = 'C'")
		.append("  AND   ( fi.id_filial = ?  or ? is null )")
		.append("  AND   ( gf.id_grupo_frota = ? or ? is null )")
		.append("  AND   ( gfu.id_usuario = ?)")
		.append(" ) tb ")
		.append(" order by grupo_frota, nr_frota");		  
		return sb.toString();
	}
	
	
	/**
	 * retorna as entregas não executadas e que possuem ou não agendamento de entrega
	 * @param idMeioTransporte
	 * @return
	 */ 
	public List findEntregasNaoExecutadasComAgendamento(Long idMeioTransporte){ 
		SqlTemplate sql = this.mountSqlEntregasNaoExeutadasComAgendamento(idMeioTransporte);
		StringBuffer projecao = new StringBuffer()
			.append("new map ( ")
			.append("mt.idMeioTransporte     as idMeioTransporte, " )
			.append("to_char(ae.hrPreferenciaInicial, 'hh24:MM') as hrPreferenciaInicial)" );
		sql.addProjection(projecao.toString());
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}
	
	private SqlTemplate mountSqlEntregasNaoExeutadasComAgendamento(Long idMeioTransporte){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(ManifestoEntregaDocumento.class.getName(), "as med " + 
				"INNER JOIN med.manifestoEntrega                as me " +
				"INNER JOIN med.doctoServico                    as doc " +
				"INNER JOIN me.manifesto                        as m " +
				"LEFT  JOIN doc.agendamentoDoctoServicos        as ads " +
				"LEFT  JOIN ads.agendamentoEntrega              as ae " +
				"INNER JOIN m.controleCarga                     as cc " +
				"INNER JOIN cc.meioTransporteByIdTransportado   as mt" );
		 
		sql.addCustomCriteria("cc.tpStatusControleCarga not in ('FE','CA')");
		sql.addCustomCriteria("med.dhOcorrencia.value is null" );
		sql.addCustomCriteria("med.tpSituacaoDocumento not in ('CANC')");
		sql.addCriteria("mt.idMeioTransporte", "=", idMeioTransporte );
		
		sql.addOrderBy("ae.hrPreferenciaInicial");
		
		return sql;
	}
	
	
	public List findColetasNaoExecutadas(Long idMeioTransporte) {
		SqlTemplate sql = this.mountSqlColetasNaoExecutadas(idMeioTransporte);
    	StringBuffer projecao = new StringBuffer()
    		.append("new Map (" )
    	    .append("pc.idPedidoColeta     as idPedidoColeta, ")
    		.append("mt.idMeioTransporte   as idMeioTransporte, ")
    		.append("pc.dhColetaDisponivel as dhColetaDisponivel)");
    		
    	sql.addProjection(projecao.toString());
    			
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

	}
	
	private SqlTemplate mountSqlColetasNaoExecutadas(Long idMeioTransporte){
		SqlTemplate sql = new SqlTemplate();
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
				
		sb.append(PedidoColeta.class.getName()).append(" as pc ");
		sb.append(" INNER JOIN pc.eventoColetas                  as ec ");
		sb.append(" INNER JOIN pc.manifestoColeta                as mc ");
		sb.append(" INNER JOIN mc.controleCarga                  as cc ");
        sb.append(" INNER JOIN cc.meioTransporteByIdTransportado as mt "); 
          
		sql.addFrom(sb.toString()); 
		
		sql.addCustomCriteria("cc.tpStatusControleCarga not in ('FE','CA')  ");
		sql.addCustomCriteria("ec.tpEventoColeta = 'TR' ");
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		
		
		sb1.append("( select ec.pedidoColeta  ");
		sb1.append(" FROM EventoColeta                            as ec "); 
		sb1.append(" INNER JOIN ec.pedidoColeta                   as pc ");
		sb1.append(" INNER JOIN pc.manifestoColeta                as mc ");
		sb1.append(" INNER JOIN mc.controleCarga                  as cc ");
        sb1.append(" INNER JOIN cc.meioTransporteByIdTransportado as mt "); 
        sb1.append(" WHERE cc.tpStatusControleCarga not in ('FE','CA') ");
        sb1.append("     AND ec.tpEventoColeta in ('EX') ");
        sb1.append("     AND mt.idMeioTransporte = " + idMeioTransporte + ")" );  
        
		sql.addCustomCriteria(" ec.pedidoColeta not in " + sb1.toString());
				
		sql.addOrderBy("pc.dhColetaDisponivel.value");
		
		return sql;	
	}

	private SqlTemplate mountSqlEntregas(Long idMeioTransporte) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer sb = new StringBuffer();
		sb.append(MeioTransporte.class.getName()).append(" as mt ");
		sb.append(" inner join mt.controleCargasByIdTransportado as cc " );
		sb.append(" inner join cc.manifestos m " );
		sb.append(" inner join m.manifestoEntrega me ");
		sb.append(" inner join me.manifestoEntregaDocumentos med ");
	
		sql.addFrom(sb.toString()); 	   
    	
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		sql.addCustomCriteria("cc.tpStatusControleCarga not in ('FE','CA')");
		return sql;
	}
	
	public Integer findTotalEntregas(Long idMeioTransporte) {
        SqlTemplate sql = mountSqlEntregas(idMeioTransporte);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}

	public Integer findTotalEntregasRealizadas(Long idMeioTransporte) {
        SqlTemplate sql = mountSqlEntregas(idMeioTransporte);
        sql.addCustomCriteria("med.dhOcorrencia.value is not null");
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}

	private SqlTemplate mountSqlColetas(Long idMeioTransporte) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer sb = new StringBuffer();
		sb.append(PedidoColeta.class.getName()).append(" as pc ");
		sb.append(" inner join pc.eventoColetas as ec " );
		sb.append(" inner join pc.manifestoColeta mc " );
		sb.append(" inner join mc.controleCarga cc ");
		sb.append(" inner join cc.meioTransporteByIdTransportado mt ");

		sql.addFrom(sb.toString()); 	   
    	
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		sql.addCustomCriteria("cc.tpStatusControleCarga not in ('FE','CA')");
		return sql;
	}
	
	public Integer findTotalColetas(Long idMeioTransporte) {
		StringBuffer sb = new StringBuffer();
		sb.append("(select max(ec1.idEventoColeta)").
		   append("   from EventoColeta ec1 ").
		   append("  inner join ec1.pedidoColeta pc1").
		   append("  where pc1.idPedidoColeta = pc.idPedidoColeta)");
        SqlTemplate sql = mountSqlColetas(idMeioTransporte);
        sql.addCustomCriteria("ec.idEventoColeta = " + sb.toString());
        sql.addCustomCriteria("ec.tpEventoColeta = 'TR'");
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	public Integer findTotalColetasRealizadas(Long idMeioTransporte) {
		StringBuffer sb = new StringBuffer();
		sb.append("(select max(ec1.idEventoColeta)").
		   append("   from EventoColeta ec1 ").
		   append("  inner join ec1.pedidoColeta pc1").
		   append("  where pc1.idPedidoColeta = pc.idPedidoColeta)");
        SqlTemplate sql = mountSqlColetas(idMeioTransporte);
        sql.addCustomCriteria("ec.idEventoColeta = " + sb.toString());
        sql.addCustomCriteria("ec.tpEventoColeta = 'EX'");
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	public Integer findTotalColetasAutomaticas(Long idMeioTransporte) {
		StringBuffer sb = new StringBuffer();
		sb.append("(select max(ec1.idEventoColeta)").
		   append("   from EventoColeta ec1 ").
		   append("  inner join ec1.pedidoColeta pc1").
		   append("  where pc1.idPedidoColeta = pc.idPedidoColeta)");
        SqlTemplate sql = mountSqlColetas(idMeioTransporte);
        sql.addCustomCriteria("ec.idEventoColeta = " + sb.toString());
        sql.addCustomCriteria("ec.tpEventoColeta = 'TR'");
        sql.addCriteria("pc.tpModoPedidoColeta","=","AU");
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	public TypedFlatMap findChamadoFrota(Long idMeioTransporte) {
		SqlTemplate sql = mountSqlChamadoFrota(idMeioTransporte);
		
		StringBuffer sb = new StringBuffer();
    	sb.append("new Map(ve.idEventoCelular,");
    	sb.append("ve.dhSolicitacao)");

		sql.addProjection(sb.toString());
    	sql.addOrderBy("ve.dhSolicitacao.value");
    	
    	List l = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	TypedFlatMap resultMap = new TypedFlatMap();
    	if (l.size() >0 ) {
    		Map map = (Map)l.get(0);
    		resultMap.put("ligacoes",l.size());
    		resultMap.put("hr_ligacoes",map.get("dhSolicitacao"));
    	}
	    return resultMap;
	}

	private SqlTemplate mountSqlChamadoFrota(Long idMeioTransporte) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer sb = new StringBuffer();
		sb.append(VolEventosCelular.class.getName()).append(" as ve ");
		sb.append(" inner join ve.meioTransporte as mt " );

		sql.addFrom(sb.toString()); 	   
    	
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		return sql;
	}
	
	public Boolean findFrotaComAtraso(Long idMeioTransporte) {
		SqlTemplate sql = mountSqlFrotaComAtraso(idMeioTransporte);
		
		StringBuffer sb = new StringBuffer();
    	sb.append("new Map(ve.idEventoCelular,");
    	sb.append("ve.dhSolicitacao)");

		sql.addProjection(sb.toString());
    	sql.addOrderBy("ve.dhSolicitacao.value");
    	
    	List l = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	TypedFlatMap resultMap = new TypedFlatMap();
    	if (l.size() >0 ) {
    		Map map = (Map)l.get(0);
    		resultMap.put("ligacoes",l.size());
    		resultMap.put("hr_ligacoes",map.get("dhSolicitacao"));
    	}
	    return Boolean.FALSE;
	}

	private SqlTemplate mountSqlFrotaComAtraso(Long idMeioTransporte) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer sb = new StringBuffer();
		sb.append(VolEventosCelular.class.getName()).append(" as ve ");
		sb.append(" inner join ve.meioTransporte as mt " );

		sql.addFrom(sb.toString()); 	   
    	
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		return sql;
	}
	
	public ResultSetPage findPaginatedEventos(TypedFlatMap criteria,FindDefinition findDef) {
		 SqlTemplate sql = mountSqlEventos(criteria);
		 
		 StringBuffer sb = new StringBuffer();
		 sb.append("new Map(vlog.dhEnvio as dhEnvio,");
		 sb.append("vlog.dhRetorno as dhRetorno)");
         sql.addProjection(sb.toString());
		 return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	private SqlTemplate mountSqlEventos(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
			
		StringBuffer sb = new StringBuffer();
		sb.append(" VolLogEnviosSms vlog ");
		sb.append("inner join vlog.volEquipamento eq ");
		sb.append("inner join eq.meioTransporte mt ");
			
		sql.addFrom(sb.toString());
		
		sql.addCriteria("mt.idMeioTransporte","=", criteria.getLong("meioTransporte.idMeioTransporte"));
		sql.addCriteria("trunc(vlog.dhEnvio.value)","=",JTDateTimeUtils.getDataAtual());
		sql.addOrderBy("vlog.dhEnvio.value");
		
		return sql;
	}
	
	public Integer getRowCountEventos(TypedFlatMap criteria) {
        SqlTemplate sql = mountSqlEventos(criteria);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}

	
	public ResultSetPage findPaginatedTratativas(TypedFlatMap criteria,FindDefinition findDef) {
		 SqlTemplate sql = mountSqlTratativas(criteria);
		 
		 StringBuffer sb = new StringBuffer();
		 sb.append("new Map(ve.idEventoCelular as idEventoCelular,");
		 sb.append("ve.dhSolicitacao as dhSolicitacao,");
		 sb.append("ve.dhAtendimento as dhAtendimento,");
		 sb.append("ve.tpOrigem as tpOrigem,");
		 sb.append("ve.obAtendente as obAtendente,");
		 sb.append("case when pc.nrColeta = null then 'ENT' else 'COL' end as tipo,");
		 sb.append("nvl(pc.nrColeta,conh.nrConhecimento) as documento,");
		 sb.append("vt.dsNome as problema)");
         sql.addProjection(sb.toString());
        
         
		 return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCountTratativas(TypedFlatMap criteria) {
        SqlTemplate sql = mountSqlTratativas(criteria);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}
	
	private SqlTemplate mountSqlTratativas(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;
			
		StringBuffer sb = new StringBuffer();
		sb.append(" VolEventosCelular ve ");
		sb.append("inner join ve.meioTransporte mt ");
		sb.append("inner join mt.controleCargasByIdTransportado cc " );
		sb.append("inner join ve.controleCarga ce " );
		sb.append("inner join ve.volTiposEvento vt ");
		sb.append("left  join ve.conhecimento conh ");
		sb.append("left  join ve.pedidoColeta pc ");
		
		sql.addFrom(sb.toString());
		
		sql.addCriteria("mt.idMeioTransporte","=", criteria.getLong("meioTransporte.idMeioTransporte"));
		sql.addCustomCriteria("ce.idControleCarga = cc.idControleCarga");
		sql.addOrderBy("ve.dhSolicitacao.value desc");
		
		return sql;
	}
	
	public List findComboDocumentos(Long idMeioTransporte) {

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idDocumento",Hibernate.STRING);
				sqlQuery.addScalar("dsDocumento",Hibernate.STRING);
			}
		};

		HibernateCallback hcb = findBySql(mountSqlDocumentos(idMeioTransporte), new Long[]{idMeioTransporte,idMeioTransporte},csq);
		return (List)getAdsmHibernateTemplate().execute(hcb); 
	}
	
	private String mountSqlDocumentos(Long idMeioTransporte) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select 'E-' || med.ID_DOCTO_SERVICO idDocumento , conh.NR_CONHECIMENTO dsDocumento")
          .append("   from  manifesto_entrega_documento med")
          .append("        ,manifesto_entrega me")
	      .append("        ,manifesto m") 
	      .append("        ,controle_carga cc")
          .append("        ,meio_transporte mt") 
	      .append("        ,conhecimento conh")
          .append("  where med.id_manifesto_entrega = me.id_manifesto_entrega") 
		  .append("    and me.id_manifesto_entrega = m.id_manifesto")
		  .append("    and m.id_controle_carga = cc.id_controle_carga")
          .append("    and cc.id_transportado = mt.id_meio_transporte")
    	  .append("    and cc.tp_status_controle_carga not in ('FE,CA')")
  		  .append("    and med.tp_situacao_documento not in ('CANC')") 
	      .append("    and med.ID_DOCTO_SERVICO = conh.id_conhecimento")
    	  .append("   and mt.id_meio_transporte = ? ")
          .append("union")
          .append("   select 'C-' || pc.id_pedido_coleta , pc.nr_coleta")
		  .append("   from  manifesto_coleta mc")
          .append("   ,controle_carga cc")
          .append("   ,meio_transporte mt") 
	      .append("   ,pedido_coleta pc") 
 		  .append("   ,evento_coleta ec") 
 		  .append("   where mc.id_controle_carga = cc.id_controle_carga ")
          .append("   and cc.id_transportado = mt.id_meio_transporte") 
          .append("   and mc.id_manifesto_coleta = pc.id_manifesto_coleta")
          .append("   and pc.id_pedido_coleta = ec.id_pedido_coleta")
          .append("   and cc.tp_status_controle_carga not in ('FE,CA')") 
          .append("   and ec.tp_evento_coleta = 'TR'")
          .append("   and mt.id_meio_transporte = ? ");
		  
		return sb.toString();
	}
}
