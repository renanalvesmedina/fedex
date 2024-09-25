package com.mercurio.lms.expedicao.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 *
 * @author Claiton Grings
 * @spring.bean
 */
public class EmitirCTRDAO extends AdsmDao {

	public List<Conhecimento> updateConhecimentosStatusXSOM(Long idFilial,
			DateTime dhFiltro, List<String> list) {
		List<Conhecimento> listConhecimentos = new ArrayList<Conhecimento>();
		List<String> subList = new ArrayList<String>();
		while (true) {
			if (list.size() == 0) {
				break;
			}
			if (list.size() > 1000) {
				subList = new ArrayList<String>(list.subList(0, 999));
			} else {
				subList = new ArrayList<String>(list);
			}
			list.removeAll(subList);
			final StringBuilder sbNrConh = new StringBuilder();
			for (String row : subList) {
				String[] cels = row.split(";");
				String serie = cels[0];
				String nrConh = cels[1];
				nrConh = nrConh.replaceAll("[A-Z]| ", "");
				sbNrConh.append("'");
				sbNrConh.append(serie);
				sbNrConh.append(nrConh);
				sbNrConh.append("'");
				sbNrConh.append(",");
			}
			if (sbNrConh.length() > 0) {
				sbNrConh.setLength(sbNrConh.length() - 1);
				StringBuilder sql = new StringBuilder();
				sql.append(" select c.idDoctoServico,c.nrConhecimento, c.nrFormulario, c.dhEmissao,c.tpSituacaoConhecimento ");
				sql.append(" from ").append(Conhecimento.class.getName())
						.append(" as c ");
				sql.append(" where ");
				sql.append(" c.filialByIdFilialOrigem.id = :idFilial");
		sql.append("   and c.tpSituacaoConhecimento in ('E', 'C') ");
				sql.append(" and c.tpDocumentoServico = '")
						.append(ConstantesExpedicao.CONHECIMENTO_NACIONAL)
						.append("'");
		sql.append("   and c.nrFormulario is not null ");
				sql.append("   and SYS_EXTRACT_UTC(c.dtColeta.value) >= :dhFiltro ");
				sql.append("   and c.nrConhecimento in (")
						.append(sbNrConh).append(")");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("idFilial", idFilial);
				
				param.put("dhFiltro", dhFiltro);

				List listConh = getAdsmHibernateTemplate().findByNamedParam(
						sql.toString(), param);
				final StringBuilder sbConh = new StringBuilder();
				for (Object conh : listConh) {
					Object[] arr = (Object[]) conh;
					Conhecimento c = new Conhecimento();
					c.setIdDoctoServico((Long) arr[0]);
					c.setNrConhecimento((Long) arr[1]);
					c.setNrFormulario((Long) arr[2]);
					c.setDhEmissao((DateTime) arr[3]);
					c.setTpSituacaoConhecimento((DomainValue)arr[4]);
					sbConh.append(c.getIdDoctoServico()).append(",");
					listConhecimentos.add(c);
			}

				if (sbConh.length() > 0) {
					sbConh.setLength(sbConh.length() - 1);
					HibernateCallback updateSituacao = new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {

							StringBuffer sql = new StringBuffer();
							sql.append(" UPDATE CONHECIMENTO SET TP_SITUACAO_ATUALIZACAO_SOM = null ");
							sql.append(" WHERE ID_CONHECIMENTO IN (").append(sbConh)
									.append(")");
							session.createSQLQuery(sql.toString()).executeUpdate();
							return null;
		}
					};
					getAdsmHibernateTemplate().execute(updateSituacao);
				}
			}
		
	}
		return listConhecimentos;
	}

	public void updateConhecimentosErroSOM(final Long idFilial,
			final DateTime dhFiltro) {

		HibernateCallback updateSituacao = new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				StringBuffer sql = new StringBuffer();
				sql.append(" UPDATE CONHECIMENTO SET TP_SITUACAO_ATUALIZACAO_SOM = 'A' ");
				sql.append(" WHERE ID_FILIAL_ORIGEM = :idFilial");
				sql.append(" AND TP_SITUACAO_CONHECIMENTO IN ('E', 'C') ");
				sql.append(" AND TP_DOCUMENTO_SERVICO = '")
						.append(ConstantesExpedicao.CONHECIMENTO_NACIONAL)
						.append("'");
				sql.append(" AND NR_FORMULARIO IS NOT NULL ");
				sql.append(" AND (TP_SITUACAO_ATUALIZACAO_SOM IS NULL OR TP_SITUACAO_ATUALIZACAO_SOM = 'E' ) ");
				sql.append(" AND SYS_EXTRACT_UTC(DT_COLETA) >= :dhFiltro ");

				session.createSQLQuery(sql.toString())
						.setLong("idFilial", idFilial)
						.setDate("dhFiltro", dhFiltro.toDate()).executeUpdate();

				return null;
			}
		};
		getAdsmHibernateTemplate().execute(updateSituacao);
	}

	/**
	 * Verifica se a emissão pode ser realizada (já está processado pela
	 * receita) Retorna 0 para liberar a emissão
	 * 
	 * @param idMonitoramentoDescarga
	 * @return
	 */
	public Integer findNumberEmissaoLiberada(Long idMonitoramentoDescarga) {
		String sql = "select COUNT( distinct c.id_conhecimento) count_cte "
				+ "from monitoramento_doc_eletronico mde, "
				+ "volume_nota_fiscal vnf, "
				+ "nota_fiscal_conhecimento nfc, "
				+ "conhecimento c, "
				+ "docto_servico ds "
				+ "where vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento "
				+ "and c.id_conhecimento = nfc.id_conhecimento "
				+ "and ds.id_docto_servico = mde.id_docto_servico "
				+ "and ds.id_docto_servico = c.id_conhecimento "
				+ "and c.tp_documento_servico = '"
				+ ConstantesExpedicao.CONHECIMENTO_ELETRONICO + "' "
				+ "and c.tp_situacao_conhecimento = 'P' "
				+ "and mde.tp_situacao_documento = 'E' "
				+ "and vnf.id_monitoramento_descarga = :idMonitoramentoDescarga";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idMonitoramentoDescarga", idMonitoramentoDescarga);

		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("count_cte", Hibernate.INTEGER);
			}
		};

		return (Integer) getAdsmHibernateTemplate().findByIdBySql(sql, params, confSql);
	}

	/**
	 * Lista de conhecimentos (CTE) que já estão processados pela receita
	 * 
	 * @param idMonitoramentoDescarga
	 * @return
	 */
	public List<Object[]> findConhecimentoCTE(Long idMonitoramentoDescarga) {
		if (findNumberEmissaoLiberada(idMonitoramentoDescarga) == 0) {
			String sql = "select  mde.ID_DOCTO_SERVICO, "
					+ "mde.TP_SITUACAO_DOCUMENTO, "
					+ "mde.NR_PROTOCOLO "
					+ "from monitoramento_doc_eletronico mde, "
					+ "conhecimento c, "
					+ "docto_servico ds "
					+ "where ds.id_docto_servico = mde.id_docto_servico "
					+ "and ds.id_docto_servico = c.id_conhecimento "
					+ "and c.tp_documento_servico = '"
					+ ConstantesExpedicao.CONHECIMENTO_ELETRONICO
					+ "' "
					+ "and c.tp_situacao_conhecimento = '"
					+ ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO
					+ "' "
					+ "and mde.tp_situacao_documento = 'A' "
					+ "and exists ("
					+ "select 1 from "
					+ "nota_fiscal_conhecimento nfc, "
					+ "volume_nota_fiscal vnf "
					+ "where vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento "
					+ "and c.id_conhecimento = nfc.id_conhecimento "
					+ "and vnf.id_monitoramento_descarga = :idMonitoramentoDescarga"
					+ ") "
					+ "order by c.nr_ordem_emissao_edi asc, ds.dh_inclusao asc, ds.id_docto_servico";

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
			
			ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
				public void configQuery(SQLQuery sqlQuery) {				
					sqlQuery.addScalar("TP_SITUACAO_DOCUMENTO",
							Hibernate.STRING);
					sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
					sqlQuery.addScalar("NR_PROTOCOLO", Hibernate.LONG);
				}
			};

			return getAdsmHibernateTemplate().findPaginatedBySql(sql,
					Integer.valueOf(1), Integer.valueOf(10000), params, confSql)
					.getList();
		}

		return null;
	}

	 /**
	 * Lista de conhecimentos (CTE) que já estão processados pela receita
	 * 
	 * @param idFatura
	 * @return
	 */
	public List<Object[]> findConhecimentoCTEbyIdFatura(Long idFatura) {
		if (idFatura != null) {
			String sql = "select  mde.ID_DOCTO_SERVICO, "
				+ "mde.TP_SITUACAO_DOCUMENTO, "
				+ "mde.NR_PROTOCOLO "
				+ "from monitoramento_doc_eletronico mde, "
				+ "conhecimento c, "
				+ "docto_servico ds "
				+ "where ds.id_docto_servico = mde.id_docto_servico "
				+ "and ds.id_docto_servico = c.id_conhecimento "
				+ "and c.tp_documento_servico = '"
				+ ConstantesExpedicao.CONHECIMENTO_ELETRONICO
				+ "' "
				+ "and c.tp_situacao_conhecimento = '"
				+ ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO
				+ "' "
				+ "and mde.tp_situacao_documento in ('A', 'R') "
				+ "and exists ("
				+ "select 1 from "
				+ "item_fatura itf, "
				+ "devedor_doc_serv_fat ddst "
				+ "where itf.id_devedor_doc_serv_fat = ddst.id_devedor_doc_serv_fat "
				+ "and ddst.id_docto_servico = ds.id_docto_servico "
				+ "and itf.id_fatura = :idFatura"
				+ ") "
				+ "order by ds.id_docto_servico";
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("idFatura", idFatura);
			
			ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
				public void configQuery(SQLQuery sqlQuery) {				
					sqlQuery.addScalar("TP_SITUACAO_DOCUMENTO",
							Hibernate.STRING);
					sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
					sqlQuery.addScalar("NR_PROTOCOLO", Hibernate.LONG);
				}
			};

			return getAdsmHibernateTemplate().findPaginatedBySql(sql,
					Integer.valueOf(1), Integer.valueOf(10000), params, confSql)
					.getList();
		}

		return null;
	}
	
	/**
	 * Lista de conhecimentos (CTE) que já estão processados pela receita
	 * 
	 * @param idCliente, dhEmissao
	 * @return
	 */
	public List<Object[]> findConhecimentoCTEbyIdCliente(Long idCliente, YearMonthDay dhEmissao) {
		if (findNumberEmissaoLiberada(idCliente) == 0) {
			String sql = "select  mde.ID_DOCTO_SERVICO, "
					+ "mde.TP_SITUACAO_DOCUMENTO, "
					+ "mde.NR_PROTOCOLO "
					+ "from monitoramento_doc_eletronico mde, "
					+ "conhecimento c, "
					+ "docto_servico ds, "
					+ "devedor_doc_serv_fat ddsf "
					+ "where ds.id_docto_servico = mde.id_docto_servico "
					+ "and ds.id_docto_servico = ddsf.id_docto_servico "
					+ "and ds.id_docto_servico = c.id_conhecimento "
					+ "and c.tp_documento_servico = '"
					+ ConstantesExpedicao.CONHECIMENTO_ELETRONICO
					+ "' "
					+ "and c.tp_situacao_conhecimento = '"
					+ ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO
					+ "' "
					+ "and mde.tp_situacao_documento in ('A', 'R') "
					+ "and TRUNC(CAST(ds.dh_emissao AS date )) = :dhEmissao"
					+ " and ddsf.id_cliente = :idCliente"
					+ " order by ds.nr_docto_servico";
					

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dhEmissao", dhEmissao);
			params.put("idCliente", idCliente);
			
			ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
				public void configQuery(SQLQuery sqlQuery) {				
					sqlQuery.addScalar("TP_SITUACAO_DOCUMENTO",
							Hibernate.STRING);
					sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
					sqlQuery.addScalar("NR_PROTOCOLO", Hibernate.LONG);
				}
			};

			return getAdsmHibernateTemplate().findPaginatedBySql(sql,
					Integer.valueOf(1), Integer.valueOf(10000), params, confSql)
					.getList();
		}

		return null;
	}
	

	public List<Object[]> findConhecimento(String tpDocumento,
			String tpOperacaoEmissao, String tpOpcaoEmissao,
			Long nrConhecimento, Long idFilial, Long idMonitoramentoDescarga,
			List<Map> ctrcsDuplicados, Long nrProximoFormulario,
			Boolean semNumeroReservado) {
		
		Map<String, Object> namedParams = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("  select c as conhecimento, vnf as volume ");
		sql.append("  from Conhecimento as c");
		sql.append("  join c.notaFiscalConhecimentos as nfc");
		sql.append("  join nfc.volumeNotaFiscais as vnf");
		sql.append("  join vnf.monitoramentoDescarga as md");
		sql.append(" where c.filialByIdFilialOrigem.id = :idFilial ");
		
		namedParams.put("idFilial", idFilial);
		
		if (ConstantesExpedicao.CD_EMISSAO.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_EMISSAO_NFT.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao)) {
			sql.append("   and c.tpSituacaoConhecimento = 'P'");
		} else if (ConstantesExpedicao.CD_REEMISSAO.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_REEMISSAO_NFT.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_REEMISSAO_NTE.equals(tpOperacaoEmissao)
				|| ConstantesExpedicao.CD_REEMISSAO_CTE.equals(tpOperacaoEmissao)) {
			sql.append("   and c.tpSituacaoConhecimento in ('C', 'E')");

			}
		
		List<String> tiposDocumentos = new ArrayList<String>();
		tiposDocumentos.add(tpDocumento);
		if (ConstantesExpedicao.CD_GERACAO_CTE.equals(tpOperacaoEmissao) && ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumento)) {
			//Nota eletrônica é gerada quando selecionado Geração de CT-e/RPS-t na combo da tela
			tiposDocumentos.add(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA);
		}
		sql.append("   and c.tpDocumentoServico in ('").append(StringUtils.join(tiposDocumentos.toArray(new String[]{}), "','")).append("')");
		
			sql.append("   and vnf.nrSequencia <= 1 ");
			sql.append("   and vnf.tpVolume in ('M', 'U') ");	
			if(BooleanUtils.isFalse(semNumeroReservado)){
				sql.append("   and vnf.nrConhecimento > 0 ");
			}

		if(ctrcsDuplicados != null && ctrcsDuplicados.size() > 0) {
			String sqlDuplicados = new String();
			sqlDuplicados = sqlDuplicados.concat("   and c.id not in ( ");
			for (Map map : ctrcsDuplicados) {
				sqlDuplicados = sqlDuplicados.concat(" "
						+ map.get("idConhecimento") + ", ");
			}
			sqlDuplicados = sqlDuplicados.substring(0,
					sqlDuplicados.length() - 2);
			sqlDuplicados = sqlDuplicados.concat(" ) ");
			sql.append(sqlDuplicados);
			}
		if(ConstantesExpedicao.CD_OPCAO_EMISSAO_CONHECIMENTO.equals(tpOpcaoEmissao)) {
			sql.append("   and vnf.nrConhecimento = :nrConhecimento ");
			namedParams.put("nrConhecimento", nrConhecimento);
		} else if(ConstantesExpedicao.CD_OPCAO_EMISSAO_VEICULO.equals(tpOpcaoEmissao)) {
			sql.append("   and md.id = :idMonitoramentoDescarga ");
			namedParams.put("idMonitoramentoDescarga", idMonitoramentoDescarga);
		}
		if (ConstantesExpedicao.CD_REEMISSAO_NTE.equals(tpOperacaoEmissao)) {
			sql.append("   and exists(from MonitoramentoDocEletronico mde where mde.doctoServico.idDoctoServico = c.idDoctoServico and mde.tpSituacaoDocumento in ('A','E')) ");
		}
		sql.append(" order by c.nrOrdemEmissaoEDI asc, c.dhInclusao asc, c.idDoctoServico");
		
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), namedParams);
	}

	public Conhecimento findConhecimentoById(Long idConhecimento, Long idFilial) {
		
		Conhecimento conhecimento = null;

		StringBuffer sql = new StringBuffer();
		sql.append("  from Conhecimento as c");
		sql.append("  join fetch c.devedorDocServs as dds");
		sql.append(" where c.id = ").append(":idConhecimento");
		sql.append("   and c.filialByIdFilialOrigem.id = ").append(":idFilial");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idConhecimento", idConhecimento);
		params.put("idFilial", idFilial);
		
		List<Conhecimento> result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		
		if(result.size() == 1) {
			conhecimento = result.get(0);
		}
		
		return conhecimento;
	}
	
}