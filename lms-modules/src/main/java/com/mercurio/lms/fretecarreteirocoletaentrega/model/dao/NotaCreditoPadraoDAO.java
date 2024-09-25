package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.usertype.ArrayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadrao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadraoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;

public class NotaCreditoPadraoDAO extends BaseCrudDao<NotaCredito, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return NotaCredito.class;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("controleCarga", FetchMode.JOIN);
		fetchModes.put("controleCarga.meioTransporteByIdTransportado", FetchMode.JOIN);
		fetchModes.put("controleCarga.proprietario", FetchMode.JOIN);
		fetchModes.put("controleCarga.proprietario.pessoa", FetchMode.JOIN);
		fetchModes.put("reciboFreteCarreteiro", FetchMode.JOIN);
		fetchModes.put("reciboFreteCarreteiro.filial", FetchMode.JOIN);
		fetchModes.put("pendencia", FetchMode.JOIN);
		fetchModes.put("pendencia.ocorrencia", FetchMode.JOIN);
		fetchModes.put("pendencia.ocorrencia.eventoWorkflow", FetchMode.JOIN);
		fetchModes.put("pendencia.ocorrencia.eventoWorkflow.tipoEvento", FetchMode.JOIN);
		fetchModes.put("moedaPais", FetchMode.JOIN);
		fetchModes.put("moedaPais.moeda", FetchMode.JOIN);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria) {		
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
	}
		
	private SqlTemplate getHqlForFindPaginated(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(notaCredito.idNotaCredito", "idNotaCredito");
		hql.addProjection("notaCredito.nrNotaCredito", "nrNotaCredito");
		hql.addProjection("CASE WHEN notaCredito.dhEmissao != null THEN 'E' ELSE 'N' END", "tpSituacao");		
		hql.addProjection("notaCredito.dhGeracao", "dhGeracao");
		hql.addProjection("notaCredito.dhEmissao", "dhEmissao");
		hql.addProjection("notaCredito.tpNotaCredito", "tpNotaCredito");		
		hql.addProjection("notaCredito.vlTotal", "vlTotal");
		hql.addProjection("moeda.dsSimbolo", "dsSimboloMoeda");
		hql.addProjection("recibo.nrReciboFreteCarreteiro", "nrReciboFreteCarreteiro");
		hql.addProjection("filialRecibo.sgFilial", "sgFilialReciboFreteCarreteiro");
		hql.addProjection("meioTransporte.nrIdentificador", "nrIdentificador");
		hql.addProjection("meioTransporte.nrFrota", "nrFrota");
		hql.addProjection("pessoa.tpIdentificacao", "tpIdentificacaoProprietario");
		hql.addProjection("pessoa.nrIdentificacao", "nrIdentificacaoProprietario");
		hql.addProjection("proprietario.tpProprietario", "tpProprietarioProprietario");
		hql.addProjection("pessoa.nmPessoa", "nmPessoaProprietario");
		hql.addProjection("filial.sgFilial", "sgFilial)");
		
    	StringBuilder from = new StringBuilder(NotaCredito.class.getName()).append(" AS notaCredito ")
    		.append("INNER JOIN notaCredito.filial AS filial ")
    		.append("INNER JOIN notaCredito.controleCarga AS controleCarga ")
    		.append("LEFT JOIN notaCredito.reciboFreteCarreteiro AS recibo ")
    		.append("LEFT JOIN notaCredito.moedaPais AS moedaPais ")
    		.append("LEFT JOIN moedaPais.moeda AS moeda ")
    		.append("LEFT JOIN recibo.filial AS filialRecibo ")   
    		.append("LEFT JOIN controleCarga.meioTransporteByIdTransportado AS meioTransporte ")
    		.append("LEFT JOIN controleCarga.proprietario AS proprietario ")
    		.append("LEFT JOIN proprietario.pessoa AS pessoa ");
    	
    	hql.addFrom(from.toString());
    	
    	hql.addCriteria("filial.id","=",criteria.getLong("idFilial"));    	    	
    	hql.addCriteria("notaCredito.nrNotaCredito","=",criteria.getLong("nrNotaCredito"));
    	
    	if(StringUtils.isNotBlank(criteria.getString("tpNotaCredito"))){
    		if ("C".equals(criteria.getString("tpNotaCredito"))) {
    			hql.addCriteriaIn("notaCredito.tpNotaCredito", new Object[]{"C","CP"});
			} else {
				hql.addCriteriaIn("notaCredito.tpNotaCredito", new Object[]{"E","EP"});
			}    		    	    
    	}
    	
    	hql.addCriteria("meioTransporte.id","=",criteria.getLong("idMeioTransporte"));
    	hql.addCriteria("proprietario.id","=",criteria.getLong("idProprietario"));
    	hql.addCriteria("recibo.idReciboFreteCarreteiro","=",criteria.getLong("idReciboFreteCarreteiro"));    	
    	    	
		hql.addCriteria("notaCredito.dhGeracao.value",">=",criteria.getDateTime("dhGeracaoInicial"));
		hql.addCriteria("notaCredito.dhGeracao.value","<=",criteria.getDateTime("dhGeracaoFinal"));    	
		hql.addCriteria("notaCredito.dhEmissao.value",">=",criteria.getDateTime("dhEmissaoInicial"));    	
		hql.addCriteria("notaCredito.dhEmissao.value","<=",criteria.getDateTime("dhEmissaoFinal"));
    	
		if (StringUtils.isNotBlank(criteria.getString("tpSituacao"))) {
			if ("N".equals(criteria.getString("tpSituacao"))) {
				hql.addCustomCriteria("notaCredito.dhEmissao.value IS NULL");
			} else {
				hql.addCustomCriteria("notaCredito.dhEmissao.value IS NOT NULL");
			}
    	}
		
		if (StringUtils.isNotBlank(criteria.getString("tpMostrarNotaZerada"))) {
			if ("N".equals(criteria.getString("tpMostrarNotaZerada"))) {
				hql.addCustomCriteria("notaCredito.vlTotal IS NOT NULL AND notaCredito.vlTotal > 0");
			}
    	}
		
		hql.addCustomCriteria("notaCredito.tpNotaCredito IS NOT NULL");
		
    	hql.addOrderBy("notaCredito.nrNotaCredito");
    	
    	return hql;
	}
	
	@SuppressWarnings("rawtypes")
	public void removeByIdsAnexoNotaCredito(List ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoNotaCredito.class.getName() + " WHERE idAnexoNotaCredito IN (:id)", ids);
	}

	public AnexoNotaCredito findAnexoNotaCreditoById(Long idAnexoNotaCredito) {
		return (AnexoNotaCredito) getAdsmHibernateTemplate().load(AnexoNotaCredito.class, idAnexoNotaCredito);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAnexoNotaCreditoByIdNotaCredito(Long idNotaCredito) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoNotaCredito AS idAnexoNotaCredito,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhCriacao,");
		hql.append(" usuario.usuarioADSM.nmUsuario AS nmUsuario)");
		hql.append(" FROM AnexoNotaCredito AS anexo");
		hql.append("  INNER JOIN anexo.notaCredito notaCredito");
		hql.append("  INNER JOIN anexo.usuario usuario");
		hql.append(" WHERE notaCredito.idNotaCredito = ?");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idNotaCredito});
	}
	
	public Integer getRowCountAnexoNotaCreditoByIdNotaCredito(Long idNotaCredito) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_nota_credito WHERE id_nota_credito = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{ idNotaCredito });
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findGerarNotaCredito(TypedFlatMap criteria, FindDefinition findDef) {
		String sql = this.getHqlForFindGerarNotaCredito(criteria);		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("id_controle_carga", Hibernate.LONG);
      			sqlQuery.addScalar("nr_controle_carga", Hibernate.LONG);
      			sqlQuery.addScalar("sg_filial_controle_carga", Hibernate.STRING);
      			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
      			sqlQuery.addScalar("tp_identificacao", Hibernate.STRING);
      			sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);      			
      		}
    	};
		
		return getAdsmHibernateTemplate().findPaginatedBySql(sql, findDef.getCurrentPage(), findDef.getPageSize(), criteria, csq);
	}
	
	public Integer getRowCountGerarNotaCredito(TypedFlatMap criteria) {		
		String sql = this.getHqlForFindGerarNotaCredito(criteria);		
		return getAdsmHibernateTemplate().getRowCountBySql(sql, criteria);
	}
		
	private String getHqlForFindGerarNotaCredito(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT controle_carga.id_controle_carga AS id_controle_carga,");
		sql.append("  controle_carga.nr_controle_carga AS nr_controle_carga,");
		sql.append("  filial.sg_filial AS sg_filial_controle_carga,");
		sql.append("  pessoa.nm_pessoa AS nm_pessoa,");
		sql.append("  pessoa.tp_identificacao AS tp_identificacao,");
		sql.append("  pessoa.nr_identificacao AS nr_identificacao");		
		sql.append(" FROM controle_carga controle_carga, ");
		sql.append("  filial filial, ");
		sql.append("  meio_transporte meioTransporte, ");
		sql.append("  proprietario proprietario, ");
		sql.append("  pessoa pessoa ");
		sql.append(" WHERE filial.id_filial = controle_carga.id_filial_origem");
		sql.append("  AND controle_carga.id_proprietario = proprietario.id_proprietario");
		sql.append("  AND proprietario.id_proprietario = pessoa.id_pessoa");
		sql.append("  AND (meioTransporte.id_meio_transporte  = controle_carga.id_transportado)");
		sql.append("  AND controle_carga.tp_controle_carga = 'C'");
		sql.append("  AND controle_carga.tp_status_controle_carga NOT IN ('CA')");
		
		if(criteria.getLong("idFilial") != null){
			sql.append(" AND controle_carga.id_filial_origem = :idFilial ");
		}
		
		if(criteria.getLong("idProprietario") != null){
			sql.append(" AND controle_carga.id_proprietario = :idProprietario ");
		}
		
		if(criteria.getLong("idMeioTransporte") != null){
			sql.append(" AND controle_carga.id_transportado = :idMeioTransporte ");
		}
		
		if(criteria.getLong("idControleCarga") != null){
			sql.append(" AND controle_carga.id_controle_carga = :idControleCarga ");
		}
		
		if(criteria.getDateTime("dhSaidaColetaEntrega") != null){
			sql.append(" AND controle_carga.dh_saida_coleta_entrega >= :dhSaidaColetaEntrega ");
		}
	
		if("C".equals(criteria.getString("tpGeracaoNotaCredito"))){
			sql.append(" AND controle_carga.dh_chegada_coleta_entrega IS NOT NULL ");
			sql.append(" AND EXISTS");
			sql.append(" (SELECT 1");
			sql.append("  FROM manifesto_coleta m, ");
			sql.append("  PEDIDO_COLETA PC,  ");
			sql.append("  DOCTO_SERVICO DOC,  ");
			sql.append("  CONHECIMENTO CON  ");
			sql.append("  WHERE m.id_controle_carga  = controle_carga.id_controle_carga");	
			sql.append("   AND DOC.id_pedido_coleta = PC.id_pedido_coleta ");
			sql.append("   AND CON.id_conhecimento  = DOC.id_docto_servico ");
			sql.append("   AND m.ID_MANIFESTO_COLETA        = PC.ID_MANIFESTO_COLETA "); 
			sql.append("   AND PC.TP_STATUS_COLETA           IN ('FI','NT','EX') ");
			sql.append("   AND DOC.DH_EMISSAO IS NOT NULL ");
			sql.append("   AND CON.TP_SITUACAO_CONHECIMENTO = 'E' ");
			sql.append("   AND ROWNUM = 1");
			sql.append(") AND ( NOT EXISTS");
			sql.append(" (SELECT nc.id_nota_credito");
			sql.append("  FROM nota_credito nc");
			sql.append("   WHERE (tp_nota_credito = 'C' OR tp_nota_credito   = 'CP' OR tp_nota_credito IS NULL)");
			sql.append("    AND nc.id_controle_carga = controle_carga.id_controle_carga");
			sql.append("))");
		} else if("P".equals(criteria.getString("tpGeracaoNotaCredito"))){
			sql.append("AND EXISTS");
			sql.append(" (SELECT 1");
			sql.append("  FROM manifesto m");
			sql.append("  WHERE m.id_controle_carga  = controle_carga.id_controle_carga");
			sql.append("   AND m.tp_manifesto_entrega = 'EP'");
			sql.append("   AND m.tp_status_manifesto <> 'CA'");
			sql.append("   AND ROWNUM = 1");
			sql.append(") AND ( NOT EXISTS");
			sql.append("  (SELECT nc.id_nota_credito");
			sql.append("    FROM nota_credito nc");
			sql.append("    WHERE nc.id_controle_carga = controle_carga.id_controle_carga");
			sql.append("     AND (tp_nota_credito = 'EP'OR tp_nota_credito IS NULL)");
			sql.append("  ) OR EXISTS");
			sql.append("  (SELECT nc.id_nota_credito ");
			sql.append("   FROM nota_credito nc, nota_credito_calc_pad_docto docto");
			sql.append("   WHERE nc.id_nota_credito = docto.id_nota_credito(+)");
			sql.append("  	AND nc.id_controle_carga = controle_carga.id_controle_carga");
			sql.append("  	AND tp_nota_credito = 'EP'");
			sql.append("  	AND (docto.bl_calculado   = 'N' or docto.bl_calculado is null)");
			sql.append("  ))");
			sql.append(" AND EXISTS ");
			sql.append("       (SELECT 1 ");
			sql.append("       FROM MANIFESTO_ENTREGA_DOCUMENTO MED,");
			sql.append("         MANIFESTO MA,");
			sql.append("         DOCTO_SERVICO DTO,");
			sql.append("         ENDERECO_PESSOA EP,");
			sql.append("         CONTROLE_CARGA CC,");
			sql.append("         MEIO_TRANSPORTE MT1,");
			sql.append("         MODELO_MEIO_TRANSPORTE MEI,");
			sql.append("         OCORRENCIA_ENTREGA OE,");
			sql.append("         PESSOA PE,");
			sql.append("         nota_credito_calc_pad_docto docto");
			sql.append("       WHERE MED.ID_DOCTO_SERVICO(+)     = DTO.ID_DOCTO_SERVICO");
			sql.append("       AND MED.ID_MANIFESTO_ENTREGA      = MA.ID_MANIFESTO");
			sql.append("       AND CC.ID_CONTROLE_CARGA          = MA.ID_CONTROLE_CARGA");
			sql.append("       AND CC.ID_CONTROLE_CARGA          = controle_carga.id_controle_carga");
			sql.append("       AND DTO.ID_CLIENTE_DESTINATARIO   = PE.ID_PESSOA");
			sql.append("       AND PE.ID_ENDERECO_PESSOA         = EP.ID_ENDERECO_PESSOA");
			sql.append("       AND CC.ID_TRANSPORTADO            = MT1.ID_MEIO_TRANSPORTE");
			sql.append("       AND MT1.ID_MODELO_MEIO_TRANSPORTE = MEI.ID_MODELO_MEIO_TRANSPORTE");
			sql.append("       AND MA.tp_manifesto_entrega      IN ('EN','EP','ED')");
			sql.append("       AND MA.tp_status_manifesto       <> 'CA'");
			sql.append("       AND MED.id_ocorrencia_entrega     = OE.id_ocorrencia_entrega");
			sql.append("       AND OE.tp_ocorrencia             IN ('A','E')");
			sql.append("       AND NOT EXISTS");
			sql.append("         (SELECT id_nota_credito_calc_pad_docto");
			sql.append("         FROM nota_credito_calc_pad_docto");
			sql.append("         WHERE id_docto_servico = DTO.ID_DOCTO_SERVICO");
			sql.append("         AND BL_CALCULADO       = 'S'");
			sql.append("         )");
			sql.append("       )");
		}
		
		sql.append(" ORDER BY controle_carga.id_controle_carga DESC");
		
    	return sql.toString();
	}
	
	private String getSqlForFindColetas() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT calc.id_tabela_fc_valores AS id_tabela_fc_valores, ");
		sql.append("  f.sg_filial AS sg_filial, ");
		sql.append("  pedido.nr_coleta AS nr_coleta, ");
		sql.append("  pedido.ps_total_informado AS ps_total_informado, ");
		sql.append("  pedido.vl_total_informado AS vl_total_informado, ");
		sql.append("  pedido.qt_total_volumes_informado AS qt_total_volumes_informado, ");
		sql.append("  fmanifesto.sg_filial AS sg_filial_manifesto, ");
		sql.append("  manifesto.nr_manifesto AS nr_manifesto, ");
		sql.append(PropertyVarcharI18nProjection.createProjection(" lograd.ds_tipo_logradouro_i", "ds_tipo_logradouro") + ",");
		sql.append("  endereco.ds_endereco AS ds_endereco, ");
		sql.append("  endereco.nr_endereco AS nr_endereco, ");
		sql.append("  endereco.ds_complemento AS ds_complemento, ");
		sql.append("  endereco.ds_bairro AS ds_bairro, ");
		sql.append("  mun.nm_municipio AS nm_municipio, ");
		sql.append("  uf.sg_unidade_federativa AS sg_unidade_federativa, ");
		sql.append("  calc.bl_calculado AS bl_calculado ");
		sql.append("FROM nota_credito_calc_pad_docto calc, ");
		sql.append("  pedido_coleta pedido, ");
		sql.append("  filial f, ");
		sql.append("  manifesto_coleta manifesto, ");
		sql.append("  filial fmanifesto, ");
		sql.append("  endereco_pessoa endereco, ");
		sql.append("  tipo_logradouro lograd, ");
		sql.append("  municipio mun, ");
		sql.append("  unidade_federativa uf ");
		sql.append("WHERE id_nota_credito             = :idNotaCredito ");
		sql.append("AND calc.id_pedido_coleta         = pedido.id_pedido_coleta ");
		sql.append("AND f.id_filial                   = pedido.id_filial_solicitante ");
		sql.append("AND manifesto.id_manifesto_coleta = pedido.id_manifesto_coleta ");
		sql.append("AND manifesto.id_filial_origem    = fmanifesto.id_filial ");
		sql.append("AND pedido.id_endereco_pessoa     = endereco.id_endereco_pessoa ");
		sql.append("AND lograd.id_tipo_logradouro     = endereco.id_tipo_logradouro ");
		sql.append("AND mun.id_municipio              = endereco.id_municipio ");
		sql.append("AND uf.id_unidade_federativa      = mun.id_unidade_federativa");
		
    	return sql.toString();
	}
	
	public List<Object[]> findColetas(Long idNotaCredito) {
		String sql = this.getSqlForFindColetas();		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("id_tabela_fc_valores", Hibernate.LONG);
      			sqlQuery.addScalar("sg_filial", Hibernate.STRING);
      			sqlQuery.addScalar("nr_coleta", Hibernate.LONG);
      			sqlQuery.addScalar("ps_total_informado", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("vl_total_informado", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("qt_total_volumes_informado", Hibernate.INTEGER);
      			sqlQuery.addScalar("sg_filial_manifesto", Hibernate.STRING);
      			sqlQuery.addScalar("nr_manifesto", Hibernate.LONG);
      			sqlQuery.addScalar("ds_tipo_logradouro", Hibernate.STRING);
      			sqlQuery.addScalar("ds_endereco", Hibernate.STRING);
      			sqlQuery.addScalar("nr_endereco", Hibernate.STRING);
      			sqlQuery.addScalar("ds_complemento", Hibernate.STRING);
      			sqlQuery.addScalar("ds_bairro", Hibernate.STRING);
      			sqlQuery.addScalar("nm_municipio", Hibernate.STRING);
      			sqlQuery.addScalar("sg_unidade_federativa", Hibernate.STRING);
      			sqlQuery.addScalar("bl_calculado", Hibernate.STRING);
      		}
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idNotaCredito", idNotaCredito);
    	
		return getAdsmHibernateTemplate().findBySql(sql, parameters, csq);
	}
	
	private String getSqlForFindDoctos() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT calc.id_tabela_fc_valores AS id_tabela_fc_valores, ");
		sql.append("  filial.sg_filial AS sg_filial, ");
		sql.append("  docto.nr_docto_servico AS nr_docto_servico, ");
		sql.append("  docto.qt_volumes AS qt_volumes, ");
		sql.append("  docto.vl_mercadoria AS vl_mercadoria, ");
		sql.append("  NVL(docto.ps_aferido, docto.ps_real)  AS ps_calculo, ");
		sql.append("  docto.vl_frete_liquido AS vl_frete_liquido, ");
		sql.append("  docto.vl_total_doc_servico AS vl_total_doc_servico, ");
		sql.append("  docto.ds_endereco_entrega_real AS ds_endereco_entrega_real, ");		
		sql.append(PropertyVarcharI18nProjection.createProjection("ocorr.ds_ocorrencia_entrega_i", "ds_ocorrencia_entrega") + ",");		
		sql.append("  (CASE WHEN serv.tp_modal = 'A' THEN 'ANC' WHEN serv.tp_modal = 'R' THEN 'RNC' END) AS tp_modal, ");
		sql.append("  remetente.nm_pessoa AS nm_pessoa_remetente, ");
		sql.append("  destinatario.nm_pessoa AS nm_pessoa_destinatario, ");
		sql.append("  calc.bl_calculado AS bl_calculado, ");
		sql.append("  docto.ps_aferido  AS ps_aferido, ");
		sql.append("  docto.ps_real  AS ps_real, ");
		sql.append("  docto.ps_referencia_calculo  AS ps_referencia, ");
		sql.append("  conhecFedex.sg_filial_origem ||  ' ' ||conhecFedex.nr_conhecimento AS cte_fedex, ");
		sql.append("  (SELECT CAST(COLLECT(TO_CHAR (nr_nota_fiscal)) AS sys.DBMSOUTPUT_LINESARRAY) ");
		sql.append("    FROM nota_fiscal_conhecimento ");
		sql.append("    WHERE nota_fiscal_conhecimento.id_conhecimento = docto.id_docto_servico ");
	    sql.append("    GROUP BY id_conhecimento) AS notas_fiscais, ");
	    sql.append("  calc.QT_VOLUMES_CALCULADO as volume_calculado, ");
	    sql.append("  calc.PS_CALCULADO as peso_calculado, ");
	    sql.append("  calc.VL_MERCAD_CALCULADO as valor_mercadoria_calculado ");
		sql.append("  FROM nota_credito_calc_pad_docto calc, ");
		sql.append("  docto_servico docto, ");
		sql.append("  manifesto_entrega_documento manifesto, ");
		sql.append("  ocorrencia_entrega ocorr, ");
		sql.append("  servico serv, ");
		sql.append("  filial filial, ");
		sql.append("  pessoa remetente, ");
		sql.append("  pessoa destinatario, ");
		sql.append("  conhecimento_fedex conhecFedex, ");
		sql.append("  nota_credito nc, ");
		sql.append("  manifesto man ");
		sql.append(" WHERE calc.id_nota_credito      = :idNotaCredito ");
		sql.append(" AND calc.id_docto_servico       = docto.id_docto_servico ");
		sql.append(" AND serv.id_servico             = docto.id_servico ");
		sql.append(" AND manifesto.id_docto_servico  = docto.id_docto_servico ");
		sql.append(" AND ocorr.id_ocorrencia_entrega = manifesto.id_ocorrencia_entrega ");
		sql.append(" AND remetente.id_pessoa         = docto.id_cliente_remetente ");
		sql.append(" AND destinatario.id_pessoa      = docto.id_cliente_destinatario ");
		sql.append(" AND docto.id_filial_origem      = filial.id_filial ");
		sql.append(" AND calc.ID_NOTA_CREDITO        = nc.id_nota_credito ");
		sql.append(" AND nc.id_controle_carga        = man.id_controle_carga ");
		sql.append(" AND man.id_manifesto            = manifesto.ID_MANIFESTO_ENTREGA ");
		sql.append(" AND docto.id_docto_servico      = conhecFedex.id_docto_servico(+) "); 
		
    	return sql.toString();
	}
	
	public List<Object[]> findDoctos(Long idNotaCredito) {
		String sql = this.getSqlForFindDoctos();		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery.addScalar("id_tabela_fc_valores", Hibernate.LONG);
      			sqlQuery.addScalar("sg_filial", Hibernate.STRING);
      			sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
      			sqlQuery.addScalar("qt_volumes", Hibernate.LONG);
      			sqlQuery.addScalar("vl_mercadoria", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("ps_calculo", Hibernate.BIG_DECIMAL);  
      			sqlQuery.addScalar("vl_frete_liquido", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("vl_total_doc_servico", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("ds_endereco_entrega_real", Hibernate.STRING);
      			sqlQuery.addScalar("ds_ocorrencia_entrega", Hibernate.STRING);
      			sqlQuery.addScalar("tp_modal", Hibernate.STRING);
      			sqlQuery.addScalar("nm_pessoa_remetente", Hibernate.STRING);  
      			sqlQuery.addScalar("nm_pessoa_destinatario", Hibernate.STRING);
      			sqlQuery.addScalar("bl_calculado", Hibernate.STRING); 
      			sqlQuery.addScalar("ps_aferido", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("ps_real", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("ps_referencia", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("cte_fedex", Hibernate.STRING);
      			sqlQuery.addScalar("notas_fiscais", Hibernate.custom(ArrayUserType.class));
      			sqlQuery.addScalar("volume_calculado", Hibernate.LONG);
      			sqlQuery.addScalar("peso_calculado", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("valor_mercadoria_calculado", Hibernate.BIG_DECIMAL);
      			
      			
      		}
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idNotaCredito", idNotaCredito);
    	
		return getAdsmHibernateTemplate().findBySql(sql, parameters, csq);
	}
	
	private String getSqlForFindTabelas() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT calc.tp_valor                   AS tp_valor, ");
		sql.append("  calc.qt_total                        AS qt_total, ");
		sql.append("  calc.vl_total                        AS vl_total ");
		
		sql.append(",(SELECT " + PropertyVarcharI18nProjection.createProjection("VDM.DS_VALOR_DOMINIO_I")+" FROM DOMINIO DM, VALOR_DOMINIO VDM "); 
	    sql.append(" WHERE   ");
	    sql.append(" DM.ID_DOMINIO = VDM.ID_DOMINIO AND "); 
	    sql.append(" DM.NM_DOMINIO = 'DM_TP_VALOR_TABELA_PADRAO' AND  ");
	    sql.append(" VDM.VL_VALOR_DOMINIO = calc.tp_valor ) AS nm_calculo "); 	
	    
		sql.append(", tvalores.id_tabela_fc_valores        AS id_tabela_fc_valores, ");
		sql.append("  tabela.id_tabela_frete_carreteiro_ce AS id_tabela_frete_carreteiro_ce, ");
		sql.append("  tabela.nr_tabela_frete_carreteiro_ce AS nr_tabela_frete_carreteiro_ce, ");
		sql.append("  tabela.tp_operacao                   AS tp_operacao, ");
		sql.append("  tabelaf.sg_filial                    AS sg_filial, ");
		sql.append("  tvalores.bl_geral                    AS bl_geral, ");
		sql.append("  tvalores.bl_tipo                     AS bl_tipo, ");
		sql.append("  tvalores.id_cliente                  AS id_cliente, ");
		sql.append("  tvalores.id_meio_transporte          AS id_meio_transporte, ");
		sql.append("  tvalores.id_municipio                AS id_municipio, ");
		sql.append("  tvalores.id_proprietario             AS id_proprietario, ");
		sql.append("  tvalores.id_rota_coleta_entrega      AS id_rota_coleta_entrega, ");
		sql.append("  tvalores.id_tipo_meio_transporte     AS id_tipo_meio_transporte, ");
		sql.append("  meio.nr_identificador                AS nr_identificador, ");
		sql.append("  meio.nr_frota                        AS nr_frota, ");
		sql.append("  mun.nm_municipio                     AS nm_municipio, ");
		sql.append("  prop.tp_proprietario                 AS tp_proprietario, ");
		sql.append("  rota.nr_rota                         AS nr_rota, ");
		sql.append("  rota.ds_rota                         AS ds_rota, ");
		sql.append("  meiotipo.ds_tipo_meio_transporte     AS ds_tipo_meio_transporte, ");
		sql.append("  ppessoa.nm_pessoa                    AS nome_proprietario, ");
		sql.append("  cpessoa.nm_pessoa                    AS nome_cliente ");
		
		sql.append(",(SELECT " + PropertyVarcharI18nProjection.createProjection("VDM.DS_VALOR_DOMINIO_I")+" FROM DOMINIO DM, VALOR_DOMINIO VDM "); 
	    sql.append(" WHERE   ");
	    sql.append(" DM.ID_DOMINIO = VDM.ID_DOMINIO AND "); 
	    sql.append(" DM.NM_DOMINIO = 'DM_TIPO_TAB_FC_VALORES' AND  ");
	    sql.append(" VDM.VL_VALOR_DOMINIO = tvalores.bl_tipo ) AS nm_tipo "); 		
	    
	    sql.append(",(SELECT " + PropertyVarcharI18nProjection.createProjection("VDM.DS_VALOR_DOMINIO_I")+" FROM DOMINIO DM, VALOR_DOMINIO VDM "); 
	    sql.append(" WHERE   ");
	    sql.append(" DM.ID_DOMINIO = VDM.ID_DOMINIO AND "); 
	    sql.append(" DM.NM_DOMINIO = 'DM_TIPO_OPERACAO_TAB_FRETE_CE' AND  ");
	    sql.append(" VDM.VL_VALOR_DOMINIO = tabela.tp_operacao ) AS nm_operacao ");	  
	    
	    sql.append(" ,tabela.pc_premio_cte AS pc_premio_cte, ");
	    sql.append("  tabela.pc_premio_evento AS pc_premio_evento, ");
	    sql.append("  tabela.pc_premio_diaria AS pc_premio_diaria, ");
	    sql.append("  tabela.pc_premio_volume AS pc_premio_volume, ");
	    sql.append("  tabela.pc_premio_saida AS pc_premio_saida, ");
	    sql.append("  tabela.pc_premio_frete_bruto AS pc_premio_frete_bruto, ");
	    sql.append("  tabela.pc_premio_frete_liq AS pc_premio_frete_liq, ");
	    sql.append("  tabela.pc_premio_mercadoria AS pc_premio_mercadoria ");
		sql.append("FROM tabela_fc_valores tvalores, ");
		sql.append("  tabela_frete_carreteiro_ce tabela, ");
		sql.append("  filial tabelaf, ");
		sql.append("  cliente cliente, ");
		sql.append("  meio_transporte meio, ");
		sql.append("  municipio mun, ");
		sql.append("  proprietario prop, ");
		sql.append("  rota_coleta_entrega rota, ");
		sql.append("  tipo_meio_transporte meiotipo, ");
		sql.append("  nota_credito nota, ");
		sql.append("  nota_credito_calc_padrao calc, ");
		sql.append("  pessoa ppessoa, ");
		sql.append("  pessoa cpessoa ");
		sql.append("WHERE tvalores.id_cliente                 =cliente.id_cliente(+) ");
		sql.append("AND cliente.id_cliente                    =cpessoa.id_pessoa(+) ");
		sql.append("AND tvalores.id_meio_transporte           =meio.id_meio_transporte(+) ");
		sql.append("AND tvalores.id_municipio                 =mun.id_municipio(+) ");
		sql.append("AND tvalores.id_proprietario              =prop.id_proprietario(+) ");
		sql.append("AND prop.id_proprietario                  =ppessoa.id_pessoa(+) ");
		sql.append("AND tvalores.id_rota_coleta_entrega       =rota.id_rota_coleta_entrega(+) ");
		sql.append("AND tvalores.id_tabela_frete_carreteiro_ce=tabela.id_tabela_frete_carreteiro_ce(+) ");
		sql.append("AND tabela.id_filial                      =tabelaf.id_filial(+) ");
		sql.append("AND tvalores.id_tipo_meio_transporte      =meiotipo.id_tipo_meio_transporte(+) ");
		sql.append("AND tvalores.id_tabela_fc_valores         = calc.id_tabela_fc_valores(+) ");
		sql.append("AND calc.id_nota_credito                  = nota.id_nota_credito ");
		sql.append("AND nota.id_nota_credito                  = :idNotaCredito");
		
    	return sql.toString();
	}
	
	public List<Object[]> findTabelas(Long idNotaCredito) {
		String sql = this.getSqlForFindTabelas();		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {    		    
    		    sqlQuery.addScalar("tp_valor", Hibernate.STRING);
    			sqlQuery.addScalar("qt_total", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vl_total", Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("nm_calculo", Hibernate.STRING);    		    
    			sqlQuery.addScalar("id_tabela_fc_valores", Hibernate.LONG);
    			sqlQuery.addScalar("id_tabela_frete_carreteiro_ce", Hibernate.LONG);
    			sqlQuery.addScalar("nr_tabela_frete_carreteiro_ce", Hibernate.LONG);
    			sqlQuery.addScalar("tp_operacao", Hibernate.STRING);
    			sqlQuery.addScalar("sg_filial", Hibernate.STRING);
      			sqlQuery.addScalar("bl_geral", Hibernate.STRING);
      			sqlQuery.addScalar("bl_tipo", Hibernate.STRING);
      			sqlQuery.addScalar("id_cliente", Hibernate.LONG);
      			sqlQuery.addScalar("id_meio_transporte", Hibernate.LONG);
      			sqlQuery.addScalar("id_municipio", Hibernate.LONG);  
      			sqlQuery.addScalar("id_proprietario", Hibernate.LONG);
      			sqlQuery.addScalar("id_rota_coleta_entrega", Hibernate.LONG);
      			sqlQuery.addScalar("id_tipo_meio_transporte", Hibernate.LONG);
      			sqlQuery.addScalar("nr_identificador", Hibernate.STRING);
      			sqlQuery.addScalar("nr_frota", Hibernate.STRING);
      			sqlQuery.addScalar("nm_municipio", Hibernate.STRING);   
      			sqlQuery.addScalar("tp_proprietario", Hibernate.STRING);
      			sqlQuery.addScalar("nr_rota", Hibernate.STRING);
      			sqlQuery.addScalar("ds_rota", Hibernate.STRING);
      			sqlQuery.addScalar("ds_tipo_meio_transporte", Hibernate.STRING);
      			sqlQuery.addScalar("nome_proprietario", Hibernate.STRING);
      			sqlQuery.addScalar("nome_cliente", Hibernate.STRING);
      			sqlQuery.addScalar("nm_tipo", Hibernate.STRING);
      			sqlQuery.addScalar("nm_operacao", Hibernate.STRING);      		
      			sqlQuery.addScalar("pc_premio_cte", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("pc_premio_evento", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("pc_premio_diaria", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("pc_premio_volume", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("pc_premio_saida", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("pc_premio_frete_bruto", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("pc_premio_frete_liq", Hibernate.BIG_DECIMAL);
      			sqlQuery.addScalar("pc_premio_mercadoria", Hibernate.BIG_DECIMAL);
      		}
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idNotaCredito", idNotaCredito);
    	
		return getAdsmHibernateTemplate().findBySql(sql, parameters, csq);
	}
	
	private String getSqlForFindCabecalho() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT fil.sg_filial AS sg_filial, ");
		sql.append("  ccarga.nr_controle_carga AS nr_controle_carga, ");
		sql.append("  meio.nr_frota AS nr_frota, ");
		sql.append("  meio.nr_identificador AS nr_identificador, ");
		sql.append("  tipo.ds_tipo_meio_transporte AS ds_tipo_meio_transporte, ");
		sql.append("  modelo.ds_modelo_meio_transporte AS ds_modelo_meio_transporte, ");
		sql.append("  motorista.nm_pessoa AS nm_pessoa, ");
		sql.append("  rota.nr_rota AS nr_rota, ");
		sql.append("  rota.ds_rota AS ds_rota, ");
		sql.append("  rota.nr_km AS nr_km, ");
		sql.append("  ccarga.dh_geracao AS dh_geracao, ");		
		sql.append("  fnota.sg_filial AS nota_sg_filial, ");
		sql.append("  nota.nr_nota_credito AS nr_nota_credito, ");
		sql.append("  nota.tp_nota_credito AS tp_nota_credito ");
		
		sql.append(",(SELECT " + PropertyVarcharI18nProjection.createProjection("VDM.DS_VALOR_DOMINIO_I")+" FROM DOMINIO DM, VALOR_DOMINIO VDM "); 
	    sql.append(" WHERE   ");
	    sql.append(" DM.ID_DOMINIO = VDM.ID_DOMINIO AND "); 
	    sql.append(" DM.NM_DOMINIO = 'DM_TIPO_NOTA_CREDITO_PADRAO' AND  ");
	    sql.append(" VDM.VL_VALOR_DOMINIO = nota.tp_nota_credito ) AS tp_nota_credito_ds ");			
		
		sql.append(" ,nota.dh_emissao AS dh_emissao, ");
		sql.append("  nota.dh_geracao AS dh_geracao, ");
		sql.append("  nota.vl_total AS vl_total, ");
		sql.append("  nota.vl_acrescimo AS vl_acrescimo, ");
		sql.append("  nota.vl_desconto AS vl_desconto, ");
		sql.append("  nota.vl_desc_uso_equipamento AS vl_desc_uso_equipamento, ");
		sql.append("  nota.ob_nota_credito AS ob_nota_credito ");		
		sql.append("FROM controle_carga ccarga, ");
		sql.append("  filial fil, ");
		sql.append("  nota_credito nota, ");
		sql.append("  filial fnota, ");
		sql.append("  meio_transporte meio, ");
		sql.append("  modelo_meio_transporte modelo, ");
		sql.append("  tipo_meio_transporte tipo, ");
		sql.append("  pessoa motorista, ");
		sql.append("  rota_coleta_entrega rota ");
		sql.append("WHERE nota.id_nota_credito         = :idNotaCredito ");
		sql.append("AND nota.id_filial        	   = fnota.id_filial ");
		sql.append("AND ccarga.id_controle_carga       = nota.id_controle_carga ");
		sql.append("AND tp_controle_carga              = 'C' ");
		sql.append("AND ccarga.id_filial_origem        = fil.id_filial ");
		sql.append("AND ccarga.id_transportado         = meio.id_meio_transporte(+) ");
		sql.append("AND meio.id_modelo_meio_transporte = modelo.id_modelo_meio_transporte(+) ");
		sql.append("AND modelo.id_tipo_meio_transporte = tipo.id_tipo_meio_transporte(+) ");
		sql.append("AND ccarga.id_motorista            = motorista.id_pessoa(+) ");
		sql.append("AND ccarga.id_rota_coleta_entrega  = rota.id_rota_coleta_entrega(+)");
		
    	return sql.toString();
	}
	
	public List<Object[]> findCabecalho(Long idNotaCredito) {
		String sql = this.getSqlForFindCabecalho();		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {    		    
    		    sqlQuery.addScalar("sg_filial", Hibernate.STRING);
    			sqlQuery.addScalar("nr_controle_carga", Hibernate.LONG);
    			sqlQuery.addScalar("nr_frota", Hibernate.STRING);
    			sqlQuery.addScalar("nr_identificador", Hibernate.STRING);    		    
    			sqlQuery.addScalar("ds_tipo_meio_transporte", Hibernate.STRING);
    			sqlQuery.addScalar("ds_modelo_meio_transporte", Hibernate.STRING);
    			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
    			sqlQuery.addScalar("nr_rota", Hibernate.STRING);
    			sqlQuery.addScalar("ds_rota", Hibernate.STRING);
    			sqlQuery.addScalar("nr_km", Hibernate.LONG);
      			sqlQuery.addScalar("dh_geracao", Hibernate.custom(JodaTimeDateTimeUserType.class));        			
      			sqlQuery.addScalar("sg_filial", Hibernate.STRING);
     			sqlQuery.addScalar("nr_nota_credito", Hibernate.LONG);
     			sqlQuery.addScalar("tp_nota_credito", Hibernate.STRING);
     			sqlQuery.addScalar("tp_nota_credito_ds", Hibernate.STRING);
     			sqlQuery.addScalar("dh_emissao", Hibernate.custom(JodaTimeDateTimeUserType.class));    		    
     			sqlQuery.addScalar("dh_geracao", Hibernate.custom(JodaTimeDateTimeUserType.class));
     			sqlQuery.addScalar("vl_total", Hibernate.BIG_DECIMAL);
     			sqlQuery.addScalar("vl_acrescimo", Hibernate.BIG_DECIMAL);
     			sqlQuery.addScalar("vl_desconto", Hibernate.BIG_DECIMAL);
     			sqlQuery.addScalar("vl_desc_uso_equipamento", Hibernate.BIG_DECIMAL);
     			sqlQuery.addScalar("ob_nota_credito", Hibernate.STRING);      			
      		}
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idNotaCredito", idNotaCredito);
    	
		return getAdsmHibernateTemplate().findBySql(sql, parameters, csq);
	}
	
	public void removeParcelasItens(List<NotaCreditoCalcPadrao> notaCreditoCalculoPadraoItens) {
		if(notaCreditoCalculoPadraoItens == null || notaCreditoCalculoPadraoItens.isEmpty()){
			return;
		}
		
		List<Long> ids = new ArrayList<Long>();
		
		for (NotaCreditoCalcPadrao item : notaCreditoCalculoPadraoItens) {
			if(item.getIdNotaCreditoCalcPadrao()!= null){
				ids.add(item.getIdNotaCreditoCalcPadrao());
			}
		}
		
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + NotaCreditoCalcPadrao.class.getName() + " as nt  WHERE nt.idNotaCreditoCalcPadrao IN (:id)", ids);		
	}
	
	public void removeDocs(List<NotaCreditoCalcPadraoDocto> notaCreditoCalcPadraoDoctoItens) {
		if(notaCreditoCalcPadraoDoctoItens == null || notaCreditoCalcPadraoDoctoItens.isEmpty()){
			return;
		}
		
		List<Long> ids = new ArrayList<Long>();
		
		for (NotaCreditoCalcPadraoDocto item : notaCreditoCalcPadraoDoctoItens) {
			if(item.getIdNotaCreditoCalcPadraoDocto()!= null){
				ids.add(item.getIdNotaCreditoCalcPadraoDocto());
			}
		}
		
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + NotaCreditoCalcPadraoDocto.class.getName() + " as nt WHERE nt.idNotaCreditoCalcPadraoDocto IN (:id)", ids);		
	}
	
	@SuppressWarnings("unchecked")
	public List<NotaCredito> findByIdControleCarga(Long idControleCarga) {    	
    	StringBuilder hql = new StringBuilder();
		hql.append("SELECT notaCredito");
		hql.append(" FROM NotaCredito AS notaCredito");
		hql.append("  LEFT JOIN notaCredito.controleCarga controleCarga");
		hql.append(" WHERE controleCarga.idControleCarga = ?");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{ idControleCarga });
	}
	
	public void storeAllParcela(List<NotaCreditoCalcPadrao> list) {
		getAdsmHibernateTemplate().saveOrUpdateAll(list);
	}
	
	public void storeAllDoct(List<NotaCreditoCalcPadraoDocto> list) {
		getAdsmHibernateTemplate().saveOrUpdateAll(list);
	}
	
	public void storeAllParcelas(List<NotaCreditoParcela> list) {
		getAdsmHibernateTemplate().saveOrUpdateAll(list);
	}

	public List<Object[]> findSituacaoDocs(Long idNotaCredito) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT qtd,tipo ");
		sql.append(" FROM( ");
		sql.append("   (SELECT COUNT(DISTINCT dto.id_DOCTO_SERVICO) AS qtd, ");
		sql.append("     'P'                                        AS tipo ");
		sql.append("   FROM MANIFESTO_ENTREGA_DOCUMENTO MED, ");
		sql.append("     MANIFESTO MA, ");
		sql.append("     DOCTO_SERVICO DTO, ");
		sql.append("     CONTROLE_CARGA CC ");
		sql.append("   WHERE MED.ID_DOCTO_SERVICO(+) = DTO.ID_DOCTO_SERVICO ");
		sql.append("   AND MED.ID_MANIFESTO_ENTREGA  = MA.ID_MANIFESTO ");
		sql.append("   AND CC.ID_CONTROLE_CARGA      = MA.ID_CONTROLE_CARGA ");
		sql.append("   AND CC.ID_CONTROLE_CARGA     IN ");
		sql.append("     ( SELECT ID_CONTROLE_CARGA FROM nota_credito WHERE id_nota_credito = :idNotaCredito ");
		sql.append("     ) ");
		sql.append("   AND MA.tp_manifesto_entrega IN ('EP') ");
		sql.append("   AND MA.tp_status_manifesto  <> 'CA' ");
		sql.append("   ) ");
		sql.append(" UNION ALL ");
		sql.append("   (SELECT COUNT(DISTINCT dto.id_DOCTO_SERVICO) , ");
		sql.append("     'N' ");
		sql.append("   FROM MANIFESTO_ENTREGA_DOCUMENTO MED, ");
		sql.append("     MANIFESTO MA, ");
		sql.append("     DOCTO_SERVICO DTO, ");
		sql.append("     CONTROLE_CARGA CC ");
		sql.append("   WHERE MED.ID_DOCTO_SERVICO(+) = DTO.ID_DOCTO_SERVICO ");
		sql.append("   AND MED.ID_MANIFESTO_ENTREGA  = MA.ID_MANIFESTO ");
		sql.append("   AND CC.ID_CONTROLE_CARGA      = MA.ID_CONTROLE_CARGA ");
		sql.append("   AND CC.ID_CONTROLE_CARGA     IN ");
		sql.append("     ( SELECT ID_CONTROLE_CARGA FROM nota_credito WHERE id_nota_credito = :idNotaCredito ");
		sql.append("     ) ");
		sql.append("   AND MA.tp_manifesto_entrega   IN ('EP') ");
		sql.append("   AND MA.tp_status_manifesto    <> 'CA' ");
		sql.append("   AND MED.id_ocorrencia_entrega IS NULL ");
		sql.append("   ) ");
		sql.append(" UNION ALL ");
		sql.append("   (SELECT COUNT(DISTINCT dto.id_DOCTO_SERVICO) , ");
		sql.append("     'E' ");
		sql.append("   FROM MANIFESTO_ENTREGA_DOCUMENTO MED, ");
		sql.append("     MANIFESTO MA, ");
		sql.append("     DOCTO_SERVICO DTO, ");
		sql.append("     CONTROLE_CARGA CC, ");
		sql.append("     OCORRENCIA_ENTREGA OE ");
		sql.append("   WHERE MED.ID_DOCTO_SERVICO(+) = DTO.ID_DOCTO_SERVICO ");
		sql.append("   AND MED.ID_MANIFESTO_ENTREGA  = MA.ID_MANIFESTO ");
		sql.append("   AND CC.ID_CONTROLE_CARGA      = MA.ID_CONTROLE_CARGA ");
		sql.append("   AND CC.ID_CONTROLE_CARGA     IN ");
		sql.append("     ( SELECT ID_CONTROLE_CARGA FROM nota_credito WHERE id_nota_credito = :idNotaCredito ");
		sql.append("     ) ");
		sql.append("   AND MA.tp_manifesto_entrega  IN ('EP') ");
		sql.append("   AND MA.tp_status_manifesto   <> 'CA' ");
		sql.append("   AND MED.id_ocorrencia_entrega = OE.id_ocorrencia_entrega ");
		sql.append("   AND OE.tp_ocorrencia         IN ('A','E') ");
		sql.append("   ) )");		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {    		    
    		    sqlQuery.addScalar("qtd", Hibernate.INTEGER);
    			sqlQuery.addScalar("tipo", Hibernate.STRING);    			      		
      		}
    	};
		
    	Map<String, Object> parameters = new HashMap<String, Object>();
    	parameters.put("idNotaCredito", idNotaCredito);
    	
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);
	}

	public void removeParcelas(List<NotaCreditoParcela> notaCreditoParcelas) {
		if(notaCreditoParcelas == null || notaCreditoParcelas.isEmpty()){
			return;
		}
		
		List<Long> ids = new ArrayList<Long>();
		
		for (NotaCreditoParcela item : notaCreditoParcelas) {
			if(item.getIdNotaCreditoParcela()!= null){
				ids.add(item.getIdNotaCreditoParcela());
			}
		}
		
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + NotaCreditoParcela.class.getName() + " as nt  WHERE nt.idNotaCreditoParcela IN (:id)", ids);		
		
	}

	/***
	 * Busca parcelas para calculo de rateio
	 * @param idNotaCredito
	 * @return
	 */
	public List<Map<String, Object>> findParcelasByIdNotaCredito(Long idNotaCredito) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ID_NOTA_CREDITO_CALC_PADRAO, TP_VALOR, QT_TOTAL, VL_TOTAL FROM NOTA_CREDITO_CALC_PADRAO WHERE ID_NOTA_CREDITO = :idNotaCredito ");
			
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("idNotaCredito", idNotaCredito);
			return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, null);
	}
}