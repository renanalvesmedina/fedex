package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;

public class CalculoTabelaFreteCarreteiroCeDAO extends	BaseCrudDao<TabelaFreteCarreteiroCe, Long> {

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return TabelaFreteCarreteiroCe.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("proprietario", FetchMode.JOIN);
		fetchModes.put("proprietario.pessoa", FetchMode.JOIN);
		fetchModes.put("usuarioCriacao", FetchMode.JOIN);
		fetchModes.put("usuarioCriacao.usuarioADSM", FetchMode.JOIN);
		fetchModes.put("usuarioAlteracao", FetchMode.JOIN);
		fetchModes.put("usuarioAlteracao.usuarioADSM", FetchMode.JOIN);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findParametrosDocumentos(Long idControleCarga) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL", Hibernate.LONG);
				sqlQuery.addScalar("ID_PROPRIETARIO", Hibernate.LONG);
				sqlQuery.addScalar("ID_CLIENTE_DESTINATARIO", Hibernate.LONG);
				sqlQuery.addScalar("ID_CLIENTE_REMETENTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO", Hibernate.LONG);
				sqlQuery.addScalar("MEIO_TRANSPORTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_MUNICIPIO_EP", Hibernate.LONG);
				sqlQuery.addScalar("CD_OCORRENCIA_ENTREGA", Hibernate.SHORT);
			}
		};

		return (List<Object[]>)getAdsmHibernateTemplate().findPaginatedBySql(getQueryDadosDocumentos(), Integer.valueOf(1), Integer.valueOf(10000),	parameters, configureSqlQuery).getList();
	}

	private String getQueryDadosDocumentos() {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT DISTINCT  ");
		sql.append("     DTO.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO,   ");
		sql.append("     MA.ID_FILIAL_ORIGEM AS ID_FILIAL,   ");
		sql.append("     CC.ID_PROPRIETARIO AS ID_PROPRIETARIO,   ");
		sql.append("     DTO.ID_CLIENTE_DESTINATARIO  AS ID_CLIENTE_DESTINATARIO,   ");
		sql.append("     DTO.ID_CLIENTE_REMETENTE  AS ID_CLIENTE_REMETENTE,       ");
		sql.append("     CON.ID_MUNICIPIO_ENTREGA AS ID_MUNICIPIO,  ");
		sql.append("     MT1.ID_MODELO_MEIO_TRANSPORTE ID_MODELO_MEIO_TRANSPORTE,   ");
		sql.append("     MEI.ID_TIPO_MEIO_TRANSPORTE AS MEIO_TRANSPORTE,   ");
		sql.append("     EP.ID_MUNICIPIO AS ID_MUNICIPIO_EP,    ");
		sql.append("     OE.CD_OCORRENCIA_ENTREGA AS CD_OCORRENCIA_ENTREGA    ");
		sql.append(" FROM MANIFESTO_ENTREGA_DOCUMENTO MED,   ");
		sql.append("      MANIFESTO MA,   ");
		sql.append("      DOCTO_SERVICO DTO,   ");     
		sql.append("      ENDERECO_PESSOA EP,   ");
		sql.append("      CONTROLE_CARGA CC,   ");
		sql.append("      MEIO_TRANSPORTE MT1,     ");
		sql.append("      MODELO_MEIO_TRANSPORTE MEI,   ");
		sql.append("      OCORRENCIA_ENTREGA OE,   ");
		sql.append("      PESSOA PE,  ");
		sql.append("      CONHECIMENTO CON  ");
		sql.append(" WHERE MED.ID_DOCTO_SERVICO(+) = DTO.ID_DOCTO_SERVICO    ");
		sql.append("       AND DTO.ID_DOCTO_SERVICO(+)  = CON.ID_CONHECIMENTO  ");
		sql.append("       AND MED.ID_MANIFESTO_ENTREGA = MA.ID_MANIFESTO       ");
		sql.append("       AND CC.ID_CONTROLE_CARGA = MA.ID_CONTROLE_CARGA   ");
		sql.append("       AND CC.ID_CONTROLE_CARGA =  :idControleCarga"); //1703883
		sql.append("       AND DTO.ID_CLIENTE_DESTINATARIO = PE.ID_PESSOA   ");
		sql.append("       AND PE.ID_ENDERECO_PESSOA       = EP.ID_ENDERECO_PESSOA   ");
		sql.append("       AND CC.ID_TRANSPORTADO = MT1.ID_MEIO_TRANSPORTE   ");
		sql.append("       AND MT1.ID_MODELO_MEIO_TRANSPORTE = MEI.ID_MODELO_MEIO_TRANSPORTE   ");
		sql.append("       AND MA.tp_manifesto_entrega IN ('EN','EP','ED')   ");
		sql.append("       AND MA.tp_status_manifesto <> 'CA'   ");
		sql.append("       AND MED.ID_OCORRENCIA_ENTREGA = OE.ID_OCORRENCIA_ENTREGA   ");
		sql.append("       AND OE.TP_OCORRENCIA IN ('A','E')   ");
		sql.append("       AND (NOT exists(select id_nota_credito_calc_pad_docto from nota_credito_calc_pad_docto where id_docto_servico = DTO.ID_DOCTO_SERVICO AND BL_CALCULADO = 'S') ");
		sql.append("            OR exists(SELECT 1 ");
		sql.append("                      FROM EVENTO_DOCUMENTO_SERVICO EDS, OCORRENCIA_ENTREGA OE ");
		sql.append("                      WHERE DTO.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO ");
		sql.append("                      AND EDS.ID_OCORRENCIA_ENTREGA = OE.ID_OCORRENCIA_ENTREGA ");
		sql.append("                      AND OE.CD_OCORRENCIA_ENTREGA = 102 ");
		sql.append("                      AND EDS.BL_EVENTO_CANCELADO = 'N' ");
		sql.append("                      AND NOT EXISTS ");
		sql.append("                      (SELECT ID_NOTA_CREDITO_CALC_PAD_DOCTO "); 
		sql.append("                              FROM NOTA_CREDITO_CALC_PAD_DOCTO NCPD, NOTA_CREDITO NC ");
		sql.append("                              WHERE NCPD.ID_DOCTO_SERVICO = DTO.ID_DOCTO_SERVICO ");
		sql.append("                              AND NCPD.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ");
		sql.append("                              AND NC.ID_CONTROLE_CARGA = :idControleCarga ");
		sql.append("                              AND NCPD.BL_CALCULADO = 'S')) ");
		sql.append("            OR exists(SELECT 1 ");
		sql.append("                      FROM EVENTO_DOCUMENTO_SERVICO EDS, OCORRENCIA_ENTREGA OE ");
		sql.append("                      WHERE DTO.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO ");
		sql.append("                      AND EDS.ID_OCORRENCIA_ENTREGA = OE.ID_OCORRENCIA_ENTREGA "); 
		sql.append("                      AND OE.CD_OCORRENCIA_ENTREGA = 102 ");
		sql.append("                      AND EXISTS (SELECT 1 ");
		sql.append("                                  FROM EVENTO_DOCUMENTO_SERVICO EDS, EVENTO E ");
		sql.append("                                  WHERE EDS.ID_EVENTO = E.ID_EVENTO ");
		sql.append("                                  AND DTO.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO "); 
		sql.append("                                  AND E.CD_EVENTO = 154) ");
		sql.append("                                  AND NOT EXISTS "); 
		sql.append("                                  (SELECT ID_NOTA_CREDITO_CALC_PAD_DOCTO "); 
		sql.append("                                          FROM NOTA_CREDITO_CALC_PAD_DOCTO NCPD, NOTA_CREDITO NC ");
		sql.append("                                          WHERE NCPD.ID_DOCTO_SERVICO = DTO.ID_DOCTO_SERVICO ");
		sql.append("                                          AND NCPD.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ");
		sql.append("                                          AND NC.ID_CONTROLE_CARGA = :idControleCarga ");
		sql.append("                                          AND NCPD.BL_CALCULADO = 'S'))) ");
		return sql.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findParametrosColetas(Long idControleCarga) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_PEDIDO_COLETA", Hibernate.LONG);
				sqlQuery.addScalar("ID_FILIAL", Hibernate.LONG);
				sqlQuery.addScalar("ID_PROPRIETARIO", Hibernate.LONG);
				sqlQuery.addScalar("ID_ROTA_COLETA_ENTREGA", Hibernate.LONG);
				sqlQuery.addScalar("ID_CLIENTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_MODELO_MEIO_TRANSPORTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_CONTROLE_CARGA", Hibernate.LONG);
				sqlQuery.addScalar("TP_CONHECIMENTO", Hibernate.STRING);
				sqlQuery.addScalar("ID_CONHECIMENTO", Hibernate.LONG);
			}
		};

		return (List<Object[]>)getAdsmHibernateTemplate().findPaginatedBySql(getQueryDadosColetas(), Integer.valueOf(1), Integer.valueOf(10000),	parameters, configureSqlQuery).getList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findPagamentoPorControleCarga(Long idControleCarga) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("TP_VALOR", Hibernate.STRING);
				sqlQuery.addScalar("NR_NOTA_CREDITO", Hibernate.STRING);
			}
		};
		return (List<Object[]>)getAdsmHibernateTemplate().findPaginatedBySql(getQueryPagamentoPorControleCarga(), Integer.valueOf(1), Integer.valueOf(10000),	parameters, configureSqlQuery).getList();
	}

	@SuppressWarnings("unchecked")
	public List<Long> findColetasSemConhecimento(Long idControleCarga, List<Long> idPedidos) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);
		parameters.put("pedidos", idPedidos);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_PEDIDO_COLETA", Hibernate.LONG);
			}
		};

		return (List<Long>)getAdsmHibernateTemplate().findPaginatedBySql(getQueryColetasNaoViraramConhecimento(idPedidos.isEmpty()), Integer.valueOf(1), Integer.valueOf(10000),	parameters, configureSqlQuery).getList();
	}
	
	
	private String getQueryColetasNaoViraramConhecimento(boolean pedidos){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ID_PEDIDO_COLETA ");
		sql.append(" FROM PEDIDO_COLETA ");
		sql.append(" WHERE ID_MANIFESTO_COLETA IN ");
		sql.append("   (SELECT ID_MANIFESTO_COLETA ");
		sql.append("   FROM MANIFESTO_COLETA ");
		sql.append("   WHERE ID_CONTROLE_CARGA = :idControleCarga ");
		sql.append("   ) ");
		
		if(!pedidos){		
			sql.append(" AND ID_PEDIDO_COLETA NOT IN ( :pedidos )");
		}
		
		return sql.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findConhecimentoNaoEntrgues(Long idControleCarga) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
			}
		};

		return (List<Long>)getAdsmHibernateTemplate().findPaginatedBySql(getQueryDocumentosNaoEntregues(), Integer.valueOf(1), Integer.valueOf(10000),	parameters, configureSqlQuery).getList();
	}
	
	
	private String getQueryDocumentosNaoEntregues(){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT  ");
		sql.append("     DTO.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO     ");
		sql.append(" FROM MANIFESTO_ENTREGA_DOCUMENTO MED,  ");
		sql.append("      MANIFESTO MA,  ");
		sql.append("      DOCTO_SERVICO DTO, "); 
		sql.append("      CONTROLE_CARGA CC,  ");
		sql.append("     OCORRENCIA_ENTREGA OE  ");
		sql.append(" WHERE MED.ID_DOCTO_SERVICO(+) = DTO.ID_DOCTO_SERVICO   ");
		sql.append("       AND MED.ID_MANIFESTO_ENTREGA = MA.ID_MANIFESTO      ");
		sql.append("       AND CC.ID_CONTROLE_CARGA = MA.ID_CONTROLE_CARGA  ");
		sql.append("       AND CC.ID_CONTROLE_CARGA = :idControleCarga       ");
		sql.append("       AND MA.tp_manifesto_entrega IN ('EN','EP','ED')  ");
		sql.append("       AND MA.tp_status_manifesto <> 'CA'  ");
		sql.append("       AND (MED.id_ocorrencia_entrega IS NULL OR (MED.id_ocorrencia_entrega = OE.id_ocorrencia_entrega AND OE.tp_ocorrencia not IN ('A','E')))");		
		return sql.toString();
	}
	
	
	
	
	
	
	private String getQueryDadosColetas(){
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT DISTINCT PC.ID_PEDIDO_COLETA AS ID_PEDIDO_COLETA, ");
		sql.append("      PC.ID_FILIAL_RESPONSAVEL            AS ID_FILIAL, ");
		sql.append("      CC.ID_PROPRIETARIO                  AS ID_PROPRIETARIO, ");
		sql.append("      CC.ID_ROTA_COLETA_ENTREGA           AS ID_ROTA_COLETA_ENTREGA, ");
		sql.append("      PC.ID_CLIENTE                       AS ID_CLIENTE, ");		
		sql.append("      MEI.ID_TIPO_MEIO_TRANSPORTE AS ID_MODELO_MEIO_TRANSPORTE, ");
		sql.append("      CC.ID_CONTROLE_CARGA                AS ID_CONTROLE_CARGA, ");
		sql.append("      CON.TP_CONHECIMENTO                AS TP_CONHECIMENTO, ");
		sql.append("      CON.ID_CONHECIMENTO                AS ID_CONHECIMENTO ");
		sql.append("    FROM CONTROLE_CARGA CC, ");
		sql.append("      MANIFESTO_COLETA MC, ");
		sql.append("      PEDIDO_COLETA PC, ");
		sql.append("      MEIO_TRANSPORTE MT1, ");
		sql.append("      MODELO_MEIO_TRANSPORTE MEI, ");
		sql.append("      DOCTO_SERVICO DOC, ");
		sql.append("      CONHECIMENTO CON ");
		sql.append("    WHERE CC.ID_CONTROLE_CARGA        = MC.ID_CONTROLE_CARGA ");
		sql.append("    AND MC.ID_MANIFESTO_COLETA        = PC.ID_MANIFESTO_COLETA ");
		sql.append("    AND CON.ID_CONHECIMENTO           = DOC.ID_DOCTO_SERVICO ");
		sql.append("    AND PC.TP_STATUS_COLETA          IN ('FI','NT','EX') ");
		sql.append("    AND CC.ID_TRANSPORTADO            = MT1.ID_MEIO_TRANSPORTE ");
		sql.append("    AND MT1.ID_MODELO_MEIO_TRANSPORTE = MEI.ID_MODELO_MEIO_TRANSPORTE ");
		sql.append("    AND cc.ID_CONTROLE_CARGA         IN ( :idControleCarga ) ");
		sql.append("    AND DOC.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA ");
		sql.append("    AND DOC.DH_EMISSAO IS NOT NULL");
		sql.append("    AND CON.TP_SITUACAO_CONHECIMENTO = 'E'");
		sql.append("    AND CON.TP_CONHECIMENTO in  ('CF','NO')");
		sql.append("    AND NOT exists(select id_nota_credito_calc_pad_docto from nota_credito_calc_pad_docto where id_pedido_coleta = PC.ID_PEDIDO_COLETA AND BL_CALCULADO = 'S') ");
		return sql.toString();
	}
	
	private String getQueryPagamentoPorControleCarga(){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TP_VALOR, NR_NOTA_CREDITO ");
		sql.append(" FROM NOTA_CREDITO_CALC_PADRAO,NOTA_CREDITO ");
		sql.append(" WHERE  NOTA_CREDITO_CALC_PADRAO.ID_NOTA_CREDITO =  NOTA_CREDITO.ID_NOTA_CREDITO ");
		sql.append(" AND TP_VALOR      IN ('KME','DIA','PRE','PNO','TRA','LOC','VAJ','PDI','PSA','HOR','DED') ");
		sql.append(" AND NOTA_CREDITO_CALC_PADRAO.VL_TOTAL > 0 ");
		sql.append(" AND NOTA_CREDITO_CALC_PADRAO.ID_NOTA_CREDITO IN ");
		sql.append("   (SELECT ID_NOTA_CREDITO FROM NOTA_CREDITO WHERE ID_CONTROLE_CARGA = :idControleCarga ");
		sql.append("   ) ");
		return sql.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findPagamentoDiariaControleCargas(Long idControleCarga) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("nr_nota_credito", Hibernate.STRING);
				sqlQuery.addScalar("qt_total", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total", Hibernate.BIG_DECIMAL);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findPaginatedBySql(getQueryPagamentoDiariaControleCargas(), Integer.valueOf(1), Integer.valueOf(10000),	parameters, configureSqlQuery).getList();
		
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
		
		for (Object[] o: list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("nr_nota_credito", o[0]);
			map.put("qt_total", o[1]);
			map.put("vl_total", o[2]);
			
			toReturn.add(map);
		}
		
		return toReturn;
	}
	
	
	private String getQueryPagamentoDiariaControleCargas() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT nc.nr_nota_credito, np.qt_total, np.vl_total ");
	    sql.append("    FROM nota_credito nc, nota_credito_calc_padrao np ");
	    sql.append("       WHERE nc.id_nota_credito = np.id_nota_credito  ");
	    sql.append("       and nc.id_nota_credito  ");
	    sql.append("         IN ");
	    sql.append("         (SELECT id_nota_credito ");
	    sql.append("           FROM NOTA_CREDITO_CALC_PADRAO ");
	    sql.append("             WHERE id_nota_credito  ");
	    sql.append("               IN ");
	    sql.append("               (SELECT id_nota_credito ");
	    sql.append("                   FROM nota_credito ");
	    sql.append("                     WHERE id_controle_carga  ");
	    sql.append("                       IN ");
	    sql.append("                       (SELECT cg1.id_controle_carga ");
	    sql.append("                         FROM controle_carga cg1 ");
	    sql.append("                           WHERE (TRUNC(cg1.DH_GERACAO), cg1.id_transportado, TRUNC(cg1.dh_saida_coleta_entrega),cg1.id_filial_origem)  ");
	    sql.append("                             IN ");
	    sql.append("                             (SELECT TRUNC(cg2.DH_GERACAO),cg2.id_transportado,TRUNC(cg2.dh_saida_coleta_entrega) , cg1.id_filial_origem ");
	    sql.append("                                 FROM controle_carga cg2 ");
	    sql.append("                                   WHERE cg2.ID_controle_carga = :idControleCarga ");
	    sql.append("                             ) ");
	    sql.append("                             AND cg1.ID_controle_carga        <> :idControleCarga ");
	    sql.append("                             AND cg1.tP_CONTROLE_CARGA         = 'C' ");
	    sql.append("                             AND cg1.tP_status_CONTROLE_CARGA <> 'CA' ");
	    sql.append("                       ) ");
	    sql.append("                   ) ");
	    sql.append("             AND tp_valor = 'DIA' ");
	    sql.append("             AND vl_total > 0 ");
	    sql.append("           ) ");
	    sql.append("          AND np.TP_VALOR = 'DIA' ");
	    sql.append("          AND np.vl_total > 0 ");
	    return sql.toString();
	}
	
	
	public boolean findExistePagamentoDiaria(Long idControleCarga) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idControleCarga", idControleCarga);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("nr_nota_credito", Hibernate.STRING);
				sqlQuery.addScalar("qt_total", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_total", Hibernate.BIG_DECIMAL);
			}
		};
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = getAdsmHibernateTemplate().findPaginatedBySql(getExistePagamentoDiaria(), Integer.valueOf(1), Integer.valueOf(10),	parameters, configureSqlQuery).getList();
		
		return !list.isEmpty();
	}
	
	
	private String getExistePagamentoDiaria() {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT nc.nr_nota_credito, ");
		sql.append("    np.qt_total, ");
		sql.append("    np.vl_total , ");
		sql.append("    nc.id_controle_carga ");
		sql.append("  FROM nota_credito nc, ");
		sql.append("    nota_credito_calc_padrao np ");
		sql.append("  WHERE NC.ID_NOTA_CREDITO = NP.ID_NOTA_CREDITO ");
		sql.append("  AND NC.ID_CONTROLE_CARGA = :idControleCarga ");
		sql.append("  AND NP.TP_VALOR          = 'DIA' ");
		sql.append("  AND NP.QT_TOTAL          > 0 ");
	    return sql.toString();
	}
	
}