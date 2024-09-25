package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.IntegerUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MonitoramentoDescargaDAO extends BaseCrudDao<MonitoramentoDescarga, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return MonitoramentoDescarga.class;
	}

	public void removeDescargasCTRCEmitido(final Long idFilial, final DateTime dhTmpEmi) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = "delete MonitoramentoDescarga " +
									"where id in (Select id from MonitoramentoDescarga " +
									"              where filial.id = :idFilial " +
									"                and tpSituacaoDescarga = 'S' " +
									"                and dhEmissaoCTRC.value <= :dhTmpEmi)";
				query = session.createQuery(hqlDelete);
				query.setLong("idFilial", idFilial);
				query.setParameter("dhTmpEmi", new Timestamp(dhTmpEmi.getMillis()));
				
				query.executeUpdate();
		
				return null;
			}
		});
	}
	
	public void removeDescargasAntigas(final Long idFilial, final DateTime dhTmpExc) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = "delete MonitoramentoDescarga " +
									"where id in (Select id from MonitoramentoDescarga " +
									"              where filial.id = :idFilial" +
									"                and dhChegadaVeiculo.value <= :dhTmpExc)";
				query = session.createQuery(hqlDelete);
				query.setLong("idFilial", idFilial);
				query.setParameter("dhTmpExc", new Timestamp(dhTmpExc.getMillis()));
				
				query.executeUpdate();
		
				return null;
			}
		});
	}

	public void updateFinalizarVeiculo(final Long idMonitoramentoDescarga, final String tpSituacaoMonitoramento) {
		if (tpSituacaoMonitoramento == null) {
			throw new IllegalArgumentException("tpSituacaoMonitoramento must not be null");
		}

		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hql = "UPDATE " + getPersistentClass().getName() + " pojo \n" +
						"SET pojo.tpSituacaoDescarga = '" + tpSituacaoMonitoramento + "' \n" +
						", pojo.qtVolumesTotal = (select count(vnf)  \n" +
						" 	  from VolumeNotaFiscal vnf " +
						"	       join vnf.notaFiscalConhecimento nfc " +
						"		   join vnf.monitoramentoDescarga md " +
						"		   join nfc.conhecimento conh " +
						"	 where vnf.tpVolume in ('U', 'M') " +
						"	   and conh.blPesoAferido = 'N' " +
						"	   and md.id = :idMonitoramentoDescarga " +
						"   )	" +
						"WHERE pojo.id = :idMonitoramentoDescarga";
				query = session.createQuery(hql);
				query.setLong("idMonitoramentoDescarga", idMonitoramentoDescarga);
				query.executeUpdate();

				return null;
			}
		});
	}

	public void updateLimpezaTelaFinalizarVeiculo(final Long idMonitoramentoDescarga, final String tpSituacaoMonitoramento) {

		if (tpSituacaoMonitoramento == null) {
			throw new IllegalArgumentException("tpSituacaoMonitoramento must not be null");
		}

		List param = new ArrayList();
		StringBuilder sql = new StringBuilder();

		param.add(tpSituacaoMonitoramento);
		param.add(idMonitoramentoDescarga);

		sql.append("UPDATE ").append(MonitoramentoDescarga.class.getName()).append(" md SET ");
		sql.append("md.tpSituacaoDescarga = ? ");
		sql.append("WHERE md.idMonitoramentoDescarga = ? ");

		executeHql(sql.toString(), param);

	}
	
	
    public void updateTpSituacaoQtVolumesMonitoramentoDescarga(final Long idMonitoramentoDescarga, final String tipoSituacaoDescarga, final Boolean atualizarQtVolumesTotal) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

            	StringBuilder hql = new StringBuilder("UPDATE ");
            	hql.append(getPersistentClass().getName());
            	hql.append(" md ");
            	hql.append("SET md.tpSituacaoDescarga = :tpSituacaoDescarga ");
            	
            	if(atualizarQtVolumesTotal){
	                hql.append(", md.qtVolumesTotal = ( ");
	                hql.append(" 	SELECT SUM(vnf.qtVolumes) ");
	                hql.append(" 	FROM ");
	                hql.append(VolumeNotaFiscal.class.getName());
	                hql.append(" 	vnf ");
	                hql.append(" 	WHERE ");
	                hql.append(" 	vnf.monitoramentoDescarga.idMonitoramentoDescarga = :idMonitoramentoDescarga ");
	                hql.append(") ");
            	}
            	
            	hql.append("WHERE ");
            	hql.append("md.idMonitoramentoDescarga = :idMonitoramentoDescarga ");
                
                Query query = null;
                query = session.createQuery(hql.toString());
                query.setLong("idMonitoramentoDescarga", idMonitoramentoDescarga);
                query.setString("tpSituacaoDescarga", tipoSituacaoDescarga);
        
                return query.executeUpdate();
            }
        });
    }
	
	
	
	/**
     * Habilita Monitoramento para Finalização de Veículos após o concluído o Processamento EDI
     * 
     * @param idNotaFiscalEDI
     */
    public void updateMonitoramentoEDINotaCheia(final PedidoColeta pedidoColeta,final String nrIdentificacao, final String nrNotaFiscal) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = null;
                String tipoSituacaoDescarga = ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_INICIADA;
                
                if(pedidoColeta != null && "BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
                    tipoSituacaoDescarga = ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA;
                }
                
                String hql = "UPDATE " + getPersistentClass().getName() + " pojo \n" +
                             "SET pojo.tpSituacaoDescarga = :tpSituacaoDescarga "+
                             " WHERE pojo.id IN (select distinct md.id " +
                             "                  from MonitoramentoDescarga md " +
                             "                      join md.volumesNotaFiscal vnf " +
                             "                      join vnf.notaFiscalConhecimento nfc " +
                             "                      join nfc.cliente c " +
                             "                      join c.pessoa p " +
                             "                  where md.tpSituacaoDescarga = '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO+"' " +
                             "                      and p.nrIdentificacao = :nrIdentificacao"+
                             "                      and nfc.nrNotaFiscal = :nrNotaFiscal"+
                             "                  ) "; 
                
                
                
                query = session.createQuery(hql);
                query.setString("tpSituacaoDescarga", tipoSituacaoDescarga);
                query.setString("nrIdentificacao", nrIdentificacao);
                query.setString("nrNotaFiscal", nrNotaFiscal);
        
                return query.executeUpdate();
            }
        });
        
        this.updateMonitoramentoEDIVolumeNotaCheia(nrIdentificacao, nrNotaFiscal);
    }
	

	/**
	 * Habilita Monitoramento para Finalização de Veículos após o concluído o Processamento EDI
	 * 
	 * @param idNotaFiscalEDI
	 */
	public void updateMonitoramentoEDI(final PedidoColeta pedidoColeta,final String nrIdentificacao, final String nrNotaFiscal) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String tipoSituacaoDescarga = ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_INICIADA;
				
				if(pedidoColeta != null && "BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
					tipoSituacaoDescarga = ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA;
				}
				
				
				 String subqueryConhecimento = "select n.conhecimento from NotaFiscalConhecimento n"
			                + " inner join n.cliente cli "
			                + " inner join cli.pessoa p "
			                + "where "
			                + " p.nrIdentificacao = :nrIdentificacao"
			                + " and n.nrNotaFiscal = :nrNotaFiscal";
				 
				
				String hql = "UPDATE " + getPersistentClass().getName() + " pojo \n" +
							 "SET pojo.tpSituacaoDescarga = :tpSituacaoDescarga "+
							 " WHERE pojo.id IN (select distinct md.id " +
							 "					from MonitoramentoDescarga md " +
							 "						join md.volumesNotaFiscal vnf " +
							 "						join vnf.notaFiscalConhecimento nfc " +
							 "					where md.tpSituacaoDescarga = '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO+"' " +
							 "						and nfc.conhecimento in ("+subqueryConhecimento+")"+
							 "					) "; 
				
				
				
				query = session.createQuery(hql);
				query.setString("tpSituacaoDescarga", tipoSituacaoDescarga);
				query.setString("nrIdentificacao", nrIdentificacao);
				query.setString("nrNotaFiscal", nrNotaFiscal);
		
				return query.executeUpdate();
			}
		});
		
		this.updateMonitoramentoEDIVolume(nrIdentificacao, nrNotaFiscal);
	}

	public void updateMonitoramentoEDIVolume(final String nrIdentificacao, final String nrNotaFiscal) {
	    getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = null;
                
                 String subqueryConhecimento = "select n.conhecimento from NotaFiscalConhecimento n"
                         + " inner join n.cliente cli "
                         + " inner join cli.pessoa p "
                         + "where "
                         + " p.nrIdentificacao = :nrIdentificacao"
                         + " and n.nrNotaFiscal = :nrNotaFiscal";
                

                StringBuilder sql = new StringBuilder()
                        .append("update ")
                        .append(MonitoramentoDescarga.class.getName())
                        .append(" md set md.qtVolumesTotal = (select sum(v.qtVolumes) ")
                        .append("                        from ")
                        .append("                 "+VolumeNotaFiscal.class.getName() +" v, ")
                        .append(MonitoramentoDescarga.class.getName())
                        .append("  m where ")
                        .append("     v.monitoramentoDescarga.idMonitoramentoDescarga = m.idMonitoramentoDescarga ")
                        .append("  and ")
                        .append("   m.tpSituacaoDescarga = 'D' ")
                        .append(" and  ")
                        .append("  m.idMonitoramentoDescarga = md.idMonitoramentoDescarga) ")
                        .append(" where md.idMonitoramentoDescarga in (select distinct md.id " +
                             "                  from "+MonitoramentoDescarga.class.getName() +" md " +
                             "                      join md.volumesNotaFiscal vnf " +
                             "                      join vnf.notaFiscalConhecimento nfc " +
                             "                  where md.tpSituacaoDescarga = '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA+"' " +
                             "                      and nfc.conhecimento in ("+subqueryConhecimento+"))");                  

                query = session.createQuery(sql.toString());
                query.setString("nrIdentificacao", nrIdentificacao);
                query.setString("nrNotaFiscal", nrNotaFiscal);
                return query.executeUpdate();
            }
        });
        
    }

    /**
	 * Busca o monitoramento de descarga pelo seu identificador juntamente com
	 * dados que serão utilizados posteriormente.
	 * 
	 * @param idMonitoramentoDescarga
	 *            identificador do monitoramento de descarga
	 * @return monitoramento de descarga encontrado
	 */
	public MonitoramentoDescarga findByIdEager(final Long idMonitoramentoDescarga) {
		StringBuilder hql = new StringBuilder();
		hql.append("select md from ");
		hql.append(MonitoramentoDescarga.class.getName());
		hql.append(" as md left join fetch md.volumesNotaFiscal as vnfs ");
		hql.append(" left join fetch vnfs.notaFiscalConhecimento as nfc ");
		hql.append(" left join fetch nfc.conhecimento as c ");
		hql.append(" left join fetch c.clienteByIdClienteRemetente as ccr ");
		hql.append(" left join fetch c.clienteByIdClienteDestinatario as ccd ");
		hql.append(" left join fetch c.moeda as m ");
		hql.append(" left join fetch c.filialByIdFilialOrigem as fo ");
		hql.append(" left join fetch c.filialByIdFilialOrigem.pessoa as fop ");
		hql.append(" where md.id = ? ");
		MonitoramentoDescarga result = (MonitoramentoDescarga) getAdsmHibernateTemplate().findUniqueResult(
				hql.toString(), new Object[] { idMonitoramentoDescarga });
		return result;
	}

	public List findByDescargasFinalizadas(Long idFilial, Long idMonitoramentoDescarga) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		String sql = "select pojo " +
					 "  from "+ MonitoramentoDescarga.class.getName() + " as pojo " +
					 "          join pojo.filial as fl " +
					 " where fl.id = :idFilial " +
					 "   and pojo.tpSituacaoDescarga in ('M', 'P', 'Q') " +
					 "   and pojo.id = :idMonitoramentoDescarga ";
		namedParams.put("idFilial", idFilial);
		namedParams.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
		return getAdsmHibernateTemplate().findByNamedParam(sql, namedParams);
	}

	public List findByDescargasComPreCtrc(Long idFilial, String nrPlaca, String nrFrota) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		String sql = "select new Map(" +
					 "          pojo.nrFrota as nrFrota, " +
					 "          pojo.nrPlaca as nrPlaca, " +
					 "          pojo.id as idMonitoramentoDescarga " +
					 "       ) " +
					 "  from Conhecimento conh " +
					 " 			join conh.notaFiscalConhecimentos as nfc " +
					 " 			join nfc.volumeNotaFiscais as vnf " +
					 " 			join vnf.monitoramentoDescarga as pojo " +
					 "          join pojo.filial as fl " +
					 " where fl.id = :idFilial " +
					 "   and conh.tpSituacaoConhecimento = 'P' " +
					 "   and vnf.nrConhecimento is null " +
					 "   and pojo.tpSituacaoDescarga <> 'S' " +
					 "   and vnf.nrSequencia = 1 " +
					 "   and vnf.tpVolume in ('M', 'U') ";
		namedParams.put("idFilial", idFilial);
		if(nrFrota != null && !"".equals(nrFrota)) {
			sql += "   and pojo.nrFrota = :nrFrota ";
			namedParams.put("nrFrota", nrFrota);
		}
		if(nrPlaca != null && !"".equals(nrPlaca)) {
			sql += "   and pojo.nrPlaca = :nrPlaca ";
			namedParams.put("nrPlaca", nrPlaca);
		}
		return getAdsmHibernateTemplate().findByNamedParam(sql, namedParams);
	}
	
	public List findDescargasFinalizadas(Long idFilial, Long idMeioTransporte, Long idMonitoramentoDescarga, String nrPlaca, String nrFrota, String tpOperacao, DateTime dhFiltro, Boolean isCancelamento) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		String sql = "select new map(" +
					 "               pojo.nrFrota as nrFrota," +
					 "               pojo.nrPlaca as nrPlaca," +
					 "               pojo.dhInicioDescarga as dhInicioDescarga," +
					 "				 pojo.dsInfColeta as dsInfColeta," +
					 "               pojo.qtVolumesTotal as qtVolumesTotal," +
					 
					 "				(select nvl(sum(nvl(vonf.qtVolumes,0)),0) from VolumeNotaFiscal vonf " +
					 " 			      where vonf.monitoramentoDescarga.idMonitoramentoDescarga = pojo.idMonitoramentoDescarga " +
					 " 				  and vonf.tpVolume in ('D', 'U') " +
					 " 				  and vonf.psAferido is not null) as qtVolumesAferido , " +
					 
					 "               pojo.tpSituacaoDescarga as tpSituacaoDescarga," +
					 "               pojo.dhFimDescarga as dhFimDescarga," +
					 "               pojo.id as idMonitoramentoDescarga" +
					 "               ) " +
					 "  from "+ MonitoramentoDescarga.class.getName() + " as pojo" +
					 "          join pojo.filial as fl " +
					 " where fl.id = :idFilial ";
		namedParams.put("idFilial", idFilial);
		if (isCancelamento) {
			sql += "   and pojo.tpSituacaoDescarga in ('M', 'P', 'S') ";
			sql += "   and pojo.dhChegadaVeiculo.value >= :dhFiltro ";
			namedParams.put("dhFiltro", dhFiltro);
		} else if (ConstantesExpedicao.CD_EMISSAO.equals(tpOperacao)   || ConstantesExpedicao.CD_EMISSAO_NFT.equals(tpOperacao) ||
				ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacao)) {
			sql += "   and pojo.tpSituacaoDescarga in ('M', 'P') ";
		} else if (ConstantesExpedicao.CD_REEMISSAO.equals(tpOperacao) || ConstantesExpedicao.CD_REEMISSAO_NFT.equals(tpOperacao) || ConstantesExpedicao.CD_REEMISSAO_CTE.equals(tpOperacao) || ConstantesExpedicao.CD_REEMISSAO_NTE.equals(tpOperacao)) {
			sql += "   and pojo.tpSituacaoDescarga = 'S' ";
			sql += "   and pojo.dhEmissaoCTRC.value >= :dhFiltro ";
			namedParams.put("dhFiltro", dhFiltro);
		}
		putDefaultParametersForDescargas(sql, namedParams, idMeioTransporte, idMonitoramentoDescarga, nrPlaca, nrFrota);
		return getAdsmHibernateTemplate().findByNamedParam(sql, namedParams);
	}
	
	private void putDefaultParametersForDescargas(String sql, Map<String, Object> namedParams, Long idMeioTransporte, Long idMonitoramentoDescarga, String nrPlaca, String nrFrota){
		if(idMeioTransporte != null && idMeioTransporte > 0) {
			sql += "   and pojo.meioTransporte.id = :idMeioTransporte ";
			namedParams.put("idMeioTransporte", idMeioTransporte);
		}
		if(idMonitoramentoDescarga != null && idMonitoramentoDescarga > 0) {
			sql += "   and pojo.id = :idMonitoramentoDescarga ";
			namedParams.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
		}
		if(nrPlaca != null && !"".equals(nrPlaca)) {
			sql += "   and pojo.nrPlaca = :nrPlaca ";
			namedParams.put("nrPlaca", nrPlaca);
		}
		if(nrFrota != null && !"".equals(nrFrota)) {
			sql += "   and pojo.nrFrota = :nrFrota ";
			namedParams.put("nrFrota", nrFrota);
		}
		sql += "   order by pojo.id desc ";
	}
	
	public List findDescargasByFilialSorter(Long idFilial, Long idMeioTransporte, Long idMonitoramentoDescarga, String nrPlaca, String nrFrota) {
		Map<String, Object> namedParams = new HashMap<String, Object>();
		String sql = "select distinct new map(" +
					 "               pojo.nrFrota as nrFrota," +
					 "               pojo.nrPlaca as nrPlaca," +
					 "               pojo.dhInicioDescarga as dhInicioDescarga," +
					 "				 pojo.dsInfColeta as dsInfColeta," +
					 "               pojo.qtVolumesTotal as qtVolumesTotal," +
					 
					 "				(select nvl(sum(nvl(vonf.qtVolumes,0)),0) from VolumeNotaFiscal vonf " +
					 " 			      where vonf.monitoramentoDescarga.idMonitoramentoDescarga = pojo.idMonitoramentoDescarga " +
					 " 				  and vonf.tpVolume in ('D', 'U') " +
					 " 				  and vonf.psAferido is not null) as qtVolumesAferido , " +
					 
					 "               pojo.tpSituacaoDescarga as tpSituacaoDescarga," +
					 "               pojo.dhFimDescarga as dhFimDescarga," +
					 "               pojo.id as idMonitoramentoDescarga" +
					 "               ) " +
					 "  from "+ MonitoramentoDescarga.class.getName() + " as pojo " +
					 "          join pojo.volumesNotaFiscal as vnf " +
					 "          join vnf.notaFiscalConhecimento as nfc " +
					 "          join nfc.conhecimento as c " +
					 "          join pojo.filial as fl " +
					 " where fl.id = :idFilial " +
					 "   and c.blPesoAferido = 'S' " +
					 "   and pojo.tpSituacaoDescarga in ('G') ";
		
		namedParams.put("idFilial", idFilial);
		putDefaultParametersForDescargas(sql, namedParams, idMeioTransporte, idMonitoramentoDescarga, nrPlaca, nrFrota);
		return getAdsmHibernateTemplate().findByNamedParam(sql, namedParams);
	}
	
	
	public MonitoramentoDescarga findMonitoramentoDescargaByVolumeNotaFiscal(final Long idVolumeNotaFiscal) {
		final StringBuffer sql = new StringBuffer("");
		sql.append("select mode ");
		sql.append("from VolumeNotaFiscal as vonf, ");
		sql.append(" MonitoramentoDescarga as mode ");
		sql.append("where vonf.idVolumeNotaFiscal = ? ");
		sql.append(" and mode.idMonitoramentoDescarga = vonf.monitoramentoDescarga.idMonitoramentoDescarga ");
		return (MonitoramentoDescarga) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] {idVolumeNotaFiscal});
	}

	public List<Map<String, Object>> findMonitoramentoDescargasByIdFilialByAfterDate(DateTime dateTime, Long idFilial) {

		StringBuilder sql = new StringBuilder();
		StringBuilder groupBy = new StringBuilder();
		Map<String,Object> param = new HashMap<>();

		param.put("idFilial", idFilial);
		param.put("dateTime", dateTime);

		groupBy.append("md.nr_frota, ");
		groupBy.append("md.nr_placa, ");
		groupBy.append("md.dh_chegada_veiculo, ");
		groupBy.append("md.dh_inicio_descarga, ");
		groupBy.append("md.qt_volumes_total, ");
		groupBy.append("md.dh_ultima_afericao, ");
		groupBy.append("md.tp_situacao_descarga, ");
		groupBy.append("md.dh_fim_descarga, ");
		groupBy.append("md.ds_inf_coleta, ");
		groupBy.append("md.id_monitoramento_descarga ");

		sql.append("select ");
		sql.append("		md.nr_frota as nrFrota, ");
		sql.append("		md.nr_placa as nrPlaca, ");
		sql.append("		md.dh_chegada_veiculo as dhChegadaVeiculo, ");
		sql.append("		md.dh_inicio_descarga as dhInicioDescarga, ");
		sql.append("		md.qt_volumes_total as qtVolumesTotal, ");
		sql.append("		md.dh_ultima_afericao as dhUltimaAfericao, ");
		sql.append("		md.tp_situacao_descarga as tpSituacaoDescarga, ");
		sql.append("		md.dh_fim_descarga as dhFimDescarga, ");
		sql.append("		md.ds_inf_coleta as dsInfColeta, ");
		sql.append("		md.id_monitoramento_descarga as idMonitoramentoDescarga, ");
		sql.append("		sum(case when (vnf.ps_aferido IS NOT NULL) ");
		sql.append(" 				AND (vnf.tp_volume IN ('M')) ");
		sql.append("			then 1 else 0 end) as qtVolumesTotalPalete, ");
		sql.append("		sum(case when (vnf.ps_aferido IS NOT NULL) ");
		sql.append(" 				AND ( vnf.tp_volume IN ('M')) ");
		sql.append(" 			then 1 else 0 end) as qtVolumesAferidoPalete, ");
		sql.append("		sum(case when (vnf.ps_aferido IS NOT NULL) ");
		sql.append(" 				AND ( vnf.tp_volume IN ('M', 'U')) ");
		sql.append(" 			then 1 else 0 end) as qtVolumesAferido ");
		sql.append(" from monitoramento_descarga md, volume_nota_fiscal vnf ");
		sql.append(" where md.id_filial = :idFilial ");
		sql.append(" and ( md.tp_situacao_descarga <> 'S' OR md.tp_situacao_descarga = 'S' AND md.dh_chegada_veiculo >= :dateTime ) ");
		sql.append(" and md.id_monitoramento_descarga = vnf.id_monitoramento_descarga(+) ");
		sql.append(" group by ").append(groupBy);
		sql.append(" order by md.tp_situacao_descarga ");

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {

				sqlQuery.addScalar("nrFrota",Hibernate.STRING);
				sqlQuery.addScalar("nrPlaca",Hibernate.STRING);
				sqlQuery.addScalar("dhChegadaVeiculo",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dhInicioDescarga",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("qtVolumesTotal",Hibernate.LONG);
				sqlQuery.addScalar("dhUltimaAfericao",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("tpSituacaoDescarga",Hibernate.STRING);
				sqlQuery.addScalar("dhFimDescarga",Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("dsInfColeta",Hibernate.STRING);
				sqlQuery.addScalar("idMonitoramentoDescarga",Hibernate.LONG);
				sqlQuery.addScalar("qtVolumesTotalPalete",Hibernate.LONG);
				sqlQuery.addScalar("qtVolumesAferidoPalete",Hibernate.LONG);
				sqlQuery.addScalar("qtVolumesAferido",Hibernate.LONG);

				Properties propertiesTpSituacaoDescarga = new Properties();
				propertiesTpSituacaoDescarga.put("domainName","DM_SITUACAO_DESCARGA");
				sqlQuery.addScalar("tpSituacaoDescarga", Hibernate.custom(DomainCompositeUserType.class, propertiesTpSituacaoDescarga));
			}
		};


		return  getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), param, configureSqlQuery);

	}
	
	protected void initFindByIdLazyProperties(Map arg0) {
		arg0.put("filial", FetchMode.JOIN);
		arg0.put("filial.pessoa", FetchMode.JOIN);
		arg0.put("meioTransporte", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map arg0) {
		arg0.put("filial", FetchMode.JOIN);
		arg0.put("meioTransporte", FetchMode.JOIN);
	}

	/**
     * Busca o carregamento GM a partir do identificador de monitoramento de descarga
     * 
     * @param idMonitoramentoDescarga <code>Long</code>
     * @return <code>Long</code>
     * @author Sidarta Silva
     */
    public Long findIdCarregamentoGM(Long idMonitoramentoDescarga){   	
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom("Volume", "v");
		sql.addFrom("VolumeNotaFiscal", "vnf");
		sql.addFrom("Carregamento", "c");
		
		sql.addProjection("v.carregamento.id");
		
		sql.addCustomCriteria("v.codigoVolume = vnf.nrVolumeColeta");
		sql.addCustomCriteria("v.carregamento.id = c.id");
		 
		sql.addCriteria("v.codigoStatus", "=", new DomainValue("6")); //Expedido
		sql.addCriteria("vnf.monitoramentoDescarga.id", "=", idMonitoramentoDescarga);
		
		sql.addGroupBy("v.carregamento.id");
		
		return (Long)super.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }
    
    /**
     * Busca lista de mapas a partir do identificador de carregamento
     * 
     * @param idCarregamento <code>Long</code>
     * @return <code>List</code>
     * @author Sidarta Silva
     */
     public List findMapasByIdCarregamentoGM(Long idCarregamento) {	
    	SqlTemplate sql = new SqlTemplate();   
    	
		sql.addFrom("Carregamento", "car");
		sql.addFrom("Volume", "v");
		sql.addFrom("VolumeNotaFiscal", "vnf");   
		
		sql.addProjection("v.carregamento.id");
		sql.addProjection("v.mapaCarregamento");
		sql.addProjection("vnf.monitoramentoDescarga.id");
		
		sql.addCustomCriteria("v.carregamento.id = car.id");
		sql.addCustomCriteria("v.codigoVolume = vnf.nrVolumeColeta");

		sql.addCriteria("v.carregamento.id", "=", idCarregamento);
		
		sql.addGroupBy("v.carregamento.id");
		sql.addGroupBy("v.mapaCarregamento");
		sql.addGroupBy("vnf.monitoramentoDescarga.id");
    	
    	return super.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }  	

	/**
	 * Busca o monitoramento de descarga de acordo com os paramettros recebidos.
	 * Metodo criado para melhoria de performance no processo de criacao de
	 * conhecimentos.
	 * 
	 * @param idMeioTransporte
	 *            identificador do meio de transporte
	 * @param nrFrota
	 *            numero da frota
	 * @param tpSituacaoDescarga
	 *            situacao da descarga
	 * @param idFilial
	 *            identificador da filial
	 * @return monitoramento encontrado ou nulo
	 * @author Luis Carlos Poletto
	 */
	public MonitoramentoDescarga find(Long idMeioTransporte, String nrFrota, String tpSituacaoDescarga, Long idFilial) {
		return find(idMeioTransporte, nrFrota, tpSituacaoDescarga, idFilial, null);
     }
     
	public MonitoramentoDescarga find(Long idMeioTransporte, String nrFrota, String tpSituacaoDescarga, Long idFilial, String nrProcessamento) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select md from ");
		hql.append(MonitoramentoDescarga.class.getName());
		hql.append(" md where 1 = 1 ");

		Map<String, Object> params = new HashMap<String, Object>();
		if (idMeioTransporte != null) {
			hql.append(" and md.meioTransporte.id = :idMeioTransporte ");
			params.put("idMeioTransporte", idMeioTransporte);
		}
		if (nrFrota != null) {
			hql.append(" and md.nrFrota = :nrFrota ");
			params.put("nrFrota", nrFrota);
			
			if ("Balcao".equalsIgnoreCase(nrFrota) && tpSituacaoDescarga.equals(ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO) && nrProcessamento != null) {
				hql.append(" and md.nrProcessamento = :nrProcessamento ");
				params.put("nrProcessamento", nrProcessamento);
			}
		}

		hql.append(" and md.tpSituacaoDescarga = :tpSituacaoDescarga ");
		hql.append(" and md.filial.id = :idFilial ");

		params.put("tpSituacaoDescarga", tpSituacaoDescarga);
		params.put("idFilial", idFilial);

		List<MonitoramentoDescarga> result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Busca o monitoramento de descarga relacionado ao volume expedido
	 * representado pelo codigo de barras recebido por parametro.
	 * 
	 * @param codigoBarras
	 *            codigo de barras para ser usado como critério de busca
	 * @return volume encontrado ou nulo
	 * @author Luis Carlos Poletto
	 */
	public MonitoramentoDescarga findVolumeExpedido(final String codigoBarras) {
		if (StringUtils.isBlank(codigoBarras)) {
			throw new IllegalArgumentException("O código de barras não pode estar em branco.");
		}
		StringBuilder hql = new StringBuilder();
		hql.append(" select m from ");
		hql.append(VolumeNotaFiscal.class.getName());
		hql.append(" vnf join vnf.monitoramentoDescarga m, ");
		hql.append(Volume.class.getName());
		hql.append(" v join v.carregamento c where (vnf.nrVolumeEmbarque = v.codigoVolume ");
		hql.append(" or (vnf.nrVolumeColeta = v.codigoVolume and vnf.tpVolume in ('M', 'U'))) ");
		hql.append(" and m.tpSituacaoDescarga = 'S' and v.codigoVolume = ? ");
		hql.append(" and v.codigoStatus = 6 and c.tipoCarregamento = 'D' ");
		List<MonitoramentoDescarga> monitoramentos = getAdsmHibernateTemplate().find(hql.toString(), codigoBarras);
		if (monitoramentos != null && !monitoramentos.isEmpty()) {
			return monitoramentos.get(0);
		}
		return null;
	}

	/**
	 * Busca os monitoramentos de descarga associados aos volumes notas fiscais
	 * através da busca do conhecimento associado ao código de barras recebido
	 * por parametro.
	 * 
	 * @param codigoBarras
	 *            codigo de barras para ser usado como critério de busca
	 * @return lista de monitoramentos encontrados ou nulo
	 * @author Luis Carlos Poletto
	 */
	public List<MonitoramentoDescarga> findVolumesExpedidos(final String codigoBarras) {
		final StringBuilder hql = new StringBuilder();
		hql.append(" select m from ");
		hql.append(VolumeNotaFiscal.class.getName());
		hql.append(" vnf join vnf.monitoramentoDescarga m join vnf.notaFiscalConhecimento nfc, ");
		hql.append(VolumeNotaFiscal.class.getName());
		hql.append(" vnf1 join vnf1.notaFiscalConhecimento nfc1, ");
		hql.append(Volume.class.getName());
		hql.append(" v join v.carregamento c where (vnf.nrVolumeEmbarque = v.codigoVolume ");
		hql.append(" or (vnf.nrVolumeColeta = v.codigoVolume and vnf.tpVolume in ('M', 'U'))) ");
		hql.append(" and m.tpSituacaoDescarga = 'S' and v.codigoVolume = ? ");
		hql.append(" and v.codigoStatus = 6 and c.tipoCarregamento = 'D' ");
		hql.append(" and nfc1.conhecimento.id = nfc.conhecimento.id ");
		List<MonitoramentoDescarga> monitoramentos = getAdsmHibernateTemplate().find(hql.toString(), codigoBarras);
		if (monitoramentos != null && !monitoramentos.isEmpty()) {
			return monitoramentos;
		}
		return null;
	}
	
	
	/**
	 * Retorna o MonitoramentoDescarga à partir do idDoctoServico
	 * 
	 * @param idDoctoServico
	 * @return {@link MonitoramentoDescarga}
	 */
	public List<MonitoramentoDescarga> findByIdConhecimento(Long idConhecimento) {

		StringBuffer sql = new StringBuffer()
		.append(" select MD from " +  MonitoramentoDescarga.class.getName() + " MD	 ")
		.append(" join MD.volumesNotaFiscal VNF			")
		.append(" join VNF.notaFiscalConhecimento NFC	")
		.append(" where NFC.conhecimento.id = ?			")
		.append(" and rownum = 1						");

		return getAdsmHibernateTemplate().find(sql.toString(), idConhecimento);
	}	

	
	public void updateMonitoramentoEDIVolumeNotaCheia(final String nrIdentificacao, final String nrNotaFiscal) {
	    getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session)
                    throws HibernateException, SQLException {
                Query query = null;

                StringBuilder sql = new StringBuilder()
                        .append("update ")
                        .append(MonitoramentoDescarga.class.getName())
                        .append(" md set md.qtVolumesTotal = (select sum(v.qtVolumes) ")
                        .append("                        from ")
                        .append("                 "+VolumeNotaFiscal.class.getName() +" v, ")
                        .append(MonitoramentoDescarga.class.getName())
                        .append("  m where ")
                        .append("     v.monitoramentoDescarga.idMonitoramentoDescarga = m.idMonitoramentoDescarga ")
                        .append("  and ")
                        .append("   m.tpSituacaoDescarga = 'D' ")
                        .append(" and  ")
                        .append("  m.idMonitoramentoDescarga = md.idMonitoramentoDescarga) ")
                        .append(" where md.idMonitoramentoDescarga in (select distinct md.id " +
                             "                  from "+MonitoramentoDescarga.class.getName() +" md " +
                             "                      join md.volumesNotaFiscal vnf " +
                             "                      join vnf.notaFiscalConhecimento nfc " +
                             "                      join nfc.cliente c " +
                             "                      join c.pessoa p " +
                             "                  where md.tpSituacaoDescarga = '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_DIGITACAO_NOTAS_FISCAIS_FINALIZADA+"' " +
                             "                      and p.nrIdentificacao = :nrIdentificacao"+                  
                             "                      and nfc.nrNotaFiscal = :nrNotaFiscal)");

                query = session.createQuery(sql.toString());
                query.setString("nrIdentificacao", nrIdentificacao);
                query.setString("nrNotaFiscal", nrNotaFiscal);
                return query.executeUpdate();
            }
        });
		
	}
	
	
	public List<MonitoramentoDescarga> findMonitoramentoEmProcessamentoEDINotaCheia(String nrIdentificacaoReme, String nrNotaFiscal){
	    StringBuffer hql = new StringBuffer();
        hql.append("select distinct m ")
        .append("  from " + Conhecimento.class.getName() + " c ")       
        .append(" inner join c.notaFiscalConhecimentos nfc ")       
        .append(" inner join nfc.volumeNotaFiscais vnf ")
        .append(" inner join vnf.monitoramentoDescarga m ")
        
        .append("inner join nfc.cliente cli " )
        .append("inner join cli.pessoa p ")

        .append("where m.tpSituacaoDescarga = '" +ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO+"' ")
        .append(" and p.nrIdentificacao = ? ")
        .append(" and nfc.nrNotaFiscal = ? ");
        Integer nrNf = null;
        if( nrNotaFiscal != null || !nrNotaFiscal.isEmpty()){
            nrNf = IntegerUtils.getInteger(nrNotaFiscal);
        }
        return (List<MonitoramentoDescarga>) getAdsmHibernateTemplate().find(hql.toString(),new Object[]{nrIdentificacaoReme,nrNf});
	}
	
	public List<MonitoramentoDescarga> findMonitoramentoEmProcessamentoEDI(String nrIdentificacaoReme, String nrNotaFiscal){
        
        String subqueryConhecimento = "select n.conhecimento from NotaFiscalConhecimento n"
                + " inner join n.cliente cli "
                + " inner join cli.pessoa p "
                + "where "
                + " p.nrIdentificacao = ?"
                + " and n.nrNotaFiscal = ?";

        
        StringBuffer hql = new StringBuffer();
        hql.append("select distinct m ")
        .append("  from " + Conhecimento.class.getName() + " c ")       
        .append(" inner join c.notaFiscalConhecimentos nfc ")       
        .append(" inner join nfc.volumeNotaFiscais vnf ")
        .append(" inner join vnf.monitoramentoDescarga m ")

        .append("where m.tpSituacaoDescarga = '" +ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO+"' ")
        .append(" and nfc.conhecimento in ("+subqueryConhecimento+") ");
        Integer nrNf = null;
        if( nrNotaFiscal != null || !nrNotaFiscal.isEmpty()){
            nrNf = IntegerUtils.getInteger(nrNotaFiscal);
        }
        return (List<MonitoramentoDescarga>) getAdsmHibernateTemplate().find(hql.toString(),new Object[]{nrIdentificacaoReme,nrNf});
    
    }
	
	


	public void updateMonitoramentoNotaFiscalEDI(final MonitoramentoDescarga monitoramento){
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = null;

				StringBuilder sql = new StringBuilder()
						.append("update ")
						.append(MonitoramentoDescarga.class.getName())
						.append(" md set md.tpSituacaoDescarga =  '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA+"'")
						.append(" where md.idMonitoramentoDescarga = "+monitoramento.getIdMonitoramentoDescarga());
				

				query = session.createQuery(sql.toString());
				return query.executeUpdate();
			}
		});
	}
		
	public List<Conhecimento> findConhecimentoPertenceAoMonitoramento(MonitoramentoDescarga monitoramento){
		StringBuffer hql = new StringBuffer();

		hql.append("select distinct co ")
		.append("  from " + Conhecimento.class.getName() + " co ")
		.append(" inner join co.notaFiscalConhecimentos nfc ")
		.append(" inner join nfc.volumeNotaFiscais vnf ")

		.append("where vnf.monitoramentoDescarga.idMonitoramentoDescarga = " + monitoramento.getIdMonitoramentoDescarga())
		.append(" and co.blPesoAferido != 'S' ")
		.append(" and vnf.nrConhecimento is not null")
		.append(" and exists (select 1 ")
		.append("				from " + Conhecimento.class.getName() + " co2 ")
		.append("				inner join co2.notaFiscalConhecimentos nfc2 " )
		.append("				inner join nfc2.volumeNotaFiscais vnf2 " )
		.append("				where co2 = co ")
		.append("				and vnf2.psAferido is null ")
		.append("				and vnf2.nrConhecimento is not null) ");

		return (List<Conhecimento>) getAdsmHibernateTemplate().find(hql.toString());
	}
		
	public void updateFinalizaMonitoramentoDescarga(final Long idMonitoramentoDescarga) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
			throws HibernateException, SQLException {
				Query query = null;

				StringBuilder sql = new StringBuilder()
				.append("update ")
				.append(MonitoramentoDescarga.class.getName())
				.append(" md set md.tpSituacaoDescarga = '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_FINALIZADA+"'")
				.append(" where md.idMonitoramentoDescarga = " + idMonitoramentoDescarga);

				query = session.createQuery(sql.toString());
				return query.executeUpdate();
}
		});
	}

	public boolean validateUpdateSituacaoMonitoramento(Long idMonitoramentoDescarga) {
		StringBuilder sql = new StringBuilder();
				
		sql.append(" SELECT COUNT(ds.id_docto_servico)  AS qtDoctoServico, ");
		sql.append("   COUNT(docEletr.id_docto_servico) AS qtMonitoramento ");
		sql.append(" FROM docto_servico ds ");
		sql.append(" INNER JOIN nota_fiscal_conhecimento nfc ON (nfc.id_conhecimento = ds.id_docto_servico) ");
		sql.append(" INNER JOIN volume_nota_fiscal vnf ON (vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ) ");
		sql.append(" LEFT JOIN monitoramento_doc_eletronico docEletr ON (ds.id_docto_servico = docEletr.id_docto_servico) ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND vnf.id_monitoramento_descarga = :idMonitoramentoDescarga ");
		sql.append(" AND ds.nr_docto_servico > 0 ");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {    			
				sqlQuery.addScalar("qtDoctoServico", Hibernate.INTEGER);
				sqlQuery.addScalar("qtMonitoramento", Hibernate.INTEGER);
			}    		
		};
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
		
		List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);
		
		if (result != null && !result.isEmpty()) {
			Integer qtDoctoServico = (Integer) result.get(0)[0];
			Integer qtMonitoramento = (Integer) result.get(0)[1];
			if(qtDoctoServico > qtMonitoramento){
				return false;
			}
		}
		
		return true;
	}

	public List findByFilialByQuantidadeDiasLimpezaMonitoramentoDescarga
	(Long idFilial, String quantidadeDiasLimpeza, BigDecimal quantidadeDiaLimpezaProcessoEdiIniciado) {

		StringBuilder sql = new StringBuilder();
		StringBuilder subQueryVolumeNotaFiscal = new StringBuilder();
		StringBuilder subQueryVolumeNfConhecimentoNfConhecimento = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("idFilial", idFilial);
		param.put("quantidadeDiasLimpeza", quantidadeDiasLimpeza);
		param.put("quantidadeDiaLimpezaProcessoEdiIniciado", quantidadeDiaLimpezaProcessoEdiIniciado);

		subQueryVolumeNotaFiscal.append("SELECT 1 FROM VOLUME_NOTA_FISCAL vnf ");
		subQueryVolumeNotaFiscal.append("WHERE vnf.ID_MONITORAMENTO_DESCARGA = md.ID_MONITORAMENTO_DESCARGA");

		subQueryVolumeNfConhecimentoNfConhecimento.append("SELECT 1 FROM CONHECIMENTO c, ");
		subQueryVolumeNfConhecimentoNfConhecimento.append("VOLUME_NOTA_FISCAL vnf, NOTA_FISCAL_CONHECIMENTO nfc ");
		subQueryVolumeNfConhecimentoNfConhecimento.append("WHERE c.ID_CONHECIMENTO = nfc.ID_CONHECIMENTO ");
		subQueryVolumeNfConhecimentoNfConhecimento.append("AND vnf.ID_NOTA_FISCAL_CONHECIMENTO = nfc.ID_NOTA_FISCAL_CONHECIMENTO ");
		subQueryVolumeNfConhecimentoNfConhecimento.append("AND vnf.ID_MONITORAMENTO_DESCARGA = md.ID_MONITORAMENTO_DESCARGA ");
		subQueryVolumeNfConhecimentoNfConhecimento.append("AND c.TP_SITUACAO_CONHECIMENTO not in ('E', 'C')");

		sql.append("SELECT md.ID_MONITORAMENTO_DESCARGA AS idMonitoramentoDescarga ");
		sql.append("FROM MONITORAMENTO_DESCARGA md ");
		sql.append("WHERE md.ID_FILIAL = :idFilial AND trunc(md.DH_FIM_DESCARGA) <= trunc(SYSDATE-:quantidadeDiasLimpeza) ");
		sql.append("AND md.TP_SITUACAO_DESCARGA='P' ");
		sql.append(" OR (md.ID_FILIAL = :idFilial AND md.TP_SITUACAO_DESCARGA not in ('S', 'Y') AND NOT EXISTS(");
		sql.append(subQueryVolumeNotaFiscal);
		sql.append(")) ");
		sql.append("OR (md.ID_FILIAL = :idFilial AND md.TP_SITUACAO_DESCARGA in ('Q', 'M') AND NOT EXISTS(");
		sql.append(subQueryVolumeNfConhecimentoNfConhecimento);
		sql.append(")) ");
		sql.append("OR (md.ID_FILIAL = :idFilial AND md.TP_SITUACAO_DESCARGA='Y' ");
		sql.append("AND to_date(to_char(md.DH_INCLUSAO, 'DD/MM/YYYY HH24:MI:SS'), 'DD/MM/YYYY HH24:MI:SS') <=");
		sql.append(" to_date(to_char(SYSDATE-:quantidadeDiaLimpezaProcessoEdiIniciado, 'DD/MM/YYYY HH24:MI:SS'), 'DD/MM/YYYY HH24:MI:SS') ");
		sql.append("AND NOT EXISTS(");
		sql.append(subQueryVolumeNotaFiscal);
		sql.append(")) ");

		List idsMonitoramentoDescargas = getAdsmHibernateTemplate()
				.findBySql(sql.toString(), param, configureSqlQueryLimpezaMonitoramentoDescarga());

		return idsMonitoramentoDescargas;

	}

	public List<Object[]> findMonitoramentosConcluidos() {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT MD.NR_PROCESSAMENTO, MD.ID_MONITORAMENTO_DESCARGA ");
		sql.append("FROM MONITORAMENTO_DESCARGA MD");
		sql.append(" WHERE TP_SITUACAO_DESCARGA = 'Y'");
		sql.append(" AND EXISTS(");
		sql.append("	SELECT 1 FROM PROCESSAMENTO_EDI PE INNER JOIN PROCESSAMENTO_NOTA_EDI PNE ON ");
		sql.append("		PE.ID_PROCESSAMENTO_EDI = PNE.ID_PROCESSAMENTO_EDI ");
		sql.append("			WHERE PE.TP_STATUS = 'PI' AND MD.NR_PROCESSAMENTO = PNE.NR_PROCESSAMENTO)");

		return getAdsmHibernateTemplate().findBySql(sql.toString(), new HashMap<>(), configureSqlQueryMonit());
	}

	private ConfigureSqlQuery configureSqlQueryMonit(){
		return new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("NR_PROCESSAMENTO", Hibernate.STRING);
				sqlQuery.addScalar("ID_MONITORAMENTO_DESCARGA", Hibernate.LONG);
			}
		};
	}

	private ConfigureSqlQuery configureSqlQueryLimpezaMonitoramentoDescarga(){
		return new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idMonitoramentoDescarga", Hibernate.LONG);
			}
		};
	}

}
