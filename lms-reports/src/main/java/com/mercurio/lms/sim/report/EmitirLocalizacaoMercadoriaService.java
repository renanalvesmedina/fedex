package com.mercurio.lms.sim.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.sim.emitirLocalizacaoMercadoriaService"
 * @spring.property name="reportName"
 *                  value="com/mercurio/lms/sim/report/emitirLocalizacaoMercadoria.jasper"
 */
public class EmitirLocalizacaoMercadoriaService extends ReportServiceSupport {

	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private DomainService domainService;
	
		
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
	
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
		
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public String getDmTipoImposto(String value) {
		return domainService.findByName("DM_TIPO_IMPOSTO").findDomainValueByValue(value).getDescription().toString();
	}
	
	public JRReportDataObject execute(Map criteria) throws Exception {

		TypedFlatMap map = (TypedFlatMap) criteria;

		SqlTemplate sql = createSqlTemplate(map);

		// Seta os parametros
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		jr.setParameters(parametersReport);
		
		
		return jr;
	}
	
	public String findEnderecoCompleto(Long idPessoa) {
		
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
		
		if (enderecoPessoa != null) {
			return enderecoPessoaService.formatEnderecoPessoaComplemento(enderecoPessoa);
		} else {
			return null;
		}
		
	}
	
	public String findTelefoneEnderecoPadrao(Long idPessoa) {
		TelefoneEndereco telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoPadrao(idPessoa);
		
		if (telefoneEndereco != null) {
			if(telefoneEndereco.getNrDdi()!= null)
				return "+" + telefoneEndereco.getNrDdi() + " " + telefoneEndereco.getDddTelefone(); 
			else 
				return telefoneEndereco.getDddTelefone(); 
		}
		
		return null;
	}		
	
	public JRDataSource generateSubReportComposicaoFrete(Long idDoctoServico) throws Exception {

		JRDataSource dataSource = null;

		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlComposicaoFrete(idDoctoServico);

			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}
		
		return dataSource;
		
	}
	
	public JRDataSource generateSubReportDadosServicosAdicionais(Long idDoctoServico) throws Exception {

		JRDataSource dataSource = null;

		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlDadosServicosAdicionais(idDoctoServico);

			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}
		return dataSource;
	}
						
	public JRDataSource generateSubReportServicosAdicionais(Long idDoctoServico) throws Exception {
		
		JRDataSource dataSource = null;
		
		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlServicosAdicionais(idDoctoServico);
			
			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}		
		return dataSource;
	}

	public JRDataSource generateSubReportImpostos(Long idDoctoServico) throws Exception {
		
		JRDataSource dataSource = null;
		
		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlImpostos(idDoctoServico);
			
			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}		
		return dataSource;
	}

	public JRDataSource generateSubReportViagem(Long idDoctoServico) throws Exception {
		
		JRDataSource dataSource = null;
		
		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlViagem(idDoctoServico);
			
			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}
		
		return dataSource;
	}

	public Integer totalRegistroViagem(Long idDoctoServico) throws Exception {
		SqlTemplate sql = null;
		sql = createSqlViagem(idDoctoServico);
		//FIXME Aqui poderia ter um addProjection e utilizar .queryForInt para a consulta.
        List result = (List)getJdbcTemplate().queryForList(sql.getSql(), sql.getCriteria());
        
        if (result != null ) {
        	return result.size();
        }
        
        return Integer.valueOf(0);
	}
	
	public JRDataSource generateSubReportNotasFiscais(Long idDoctoServico) throws Exception {

		JRDataSource dataSource = null;

		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlNotasFiscais(idDoctoServico);

			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}

		return dataSource;
	}
	
	public Integer totalRegistroNotasFiscais(Long idDoctoServico) throws Exception {
		SqlTemplate sql = null;
		sql = createSqlNotasFiscais(idDoctoServico);
		//FIXME Aqui poderia ter um addProjection e utilizar .queryForInt para a consulta.
        List result = (List)getJdbcTemplate().queryForList(sql.getSql(), sql.getCriteria());
        
        if (result != null ) {
        	return result.size();
        }
        
        return Integer.valueOf(0);
	}	

	public JRDataSource generateSubReportControlesCarga(Long idDoctoServico) throws Exception {
		
		JRDataSource dataSource = null;
		
		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlControlesCarga(idDoctoServico);
			
			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}
		
		return dataSource;
	}
	
	public Integer totalRegistroControlesCarga(Long idDoctoServico) throws Exception {
		SqlTemplate sql = null;
		sql = createSqlControlesCarga(idDoctoServico);
		//FIXME Aqui poderia ter um addProjection e utilizar .queryForInt para a consulta.
        List result = (List)getJdbcTemplate().queryForList(sql.getSql(), sql.getCriteria());
        
        if (result != null ) {
        	return result.size();
        }
        
        return Integer.valueOf(0);
	}
	
	public JRDataSource generateSubReportManifestoEntrega(Long idDoctoServico) throws Exception {

		JRDataSource dataSource = null;

		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlManifestoEntrega(idDoctoServico);

			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}

		return dataSource;
	}

	public Integer totalRegistroManifestoEntrega(Long idDoctoServico) throws Exception {
		SqlTemplate sql = null;
		sql = createSqlManifestoEntrega(idDoctoServico);
		//FIXME Aqui poderia ter um addProjection e utilizar .queryForInt para a consulta.
        List result = (List)getJdbcTemplate().queryForList(sql.getSql(), sql.getCriteria());
        
        if (result != null ) {
        	return result.size();
        }
        
        return Integer.valueOf(0);
	}

	public JRDataSource generateSubReportCobranca(Long idDoctoServico) throws Exception {
		
		totalRegistroCobranca(idDoctoServico);		
		
		JRDataSource dataSource = null;
		
		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlCobranca(idDoctoServico);
			
			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}
		
		return dataSource;
	}
	
	public Integer totalRegistroCobranca(Long idDoctoServico) throws Exception {
		SqlTemplate sql = null;
		sql = createSqlCobranca(idDoctoServico);
		//FIXME Aqui poderia ter um addProjection e utilizar .queryForInt para a consulta.
        List result = (List)getJdbcTemplate().queryForList(sql.getSql(), sql.getCriteria());
        
        if (result != null ) {
        	return result.size();
        }
        
        return Integer.valueOf(0);
	}
	
	public JRDataSource generateSubReportEventos(Long idDoctoServico) throws Exception {

		JRDataSource dataSource = null;

		if (idDoctoServico != null) {
			SqlTemplate sql = null;
			sql = createSqlEventos(idDoctoServico);

			dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
		}

		return dataSource;
	}

	public Integer totalRegistroEventos(Long idDoctoServico) throws Exception {
		SqlTemplate sql = null;
		sql = createSqlEventos(idDoctoServico);
		//FIXME Aqui poderia ter um addProjection e utilizar .queryForInt para a consulta.
        List result = (List)getJdbcTemplate().queryForList(sql.getSql(), sql.getCriteria());
        
        if (result != null ) {
        	return result.size();
        }
        
        return Integer.valueOf(0);
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria)
			throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */ 
		sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "FILIAL_ORIGEM_SG_FILIAL");
		sql.addProjection("DOSE.NR_DOCTO_SERVICO", "DOSE_NR_DOCTO_SERVICO");
		sql.addProjection("DOSE.TP_DOCUMENTO_SERVICO","DOSE_TP_DOCUMENTO_SERVICO");
		sql.addProjection("DOSE.NR_CFOP","DOSE_NR_CFOP");
		sql.addProjection("CONH.TP_CONHECIMENTO", "CONH_TP_CONHECIMENTO");
		sql.addProjection("CONH.DV_CONHECIMENTO", "CONH_DV_CONHECIMENTO");
		sql.addProjection("CASE  WHEN CONH.NR_CAE IS NULL THEN '' ELSE (FILIAL_ORIGEM.SG_FILIAL || ' ' || LPAD(CONH.NR_CAE,8,'0')) END", "NR_CAE");
		sql.addProjection("TITI.DS_TIPO_TRIBUTACAO_ICMS", "TITI_DS_TIPO_TRIBUTACAO_ICMS");
		sql.addProjection("DOSE.DH_EMISSAO", "DOSE_DH_EMISSAO");
		sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "FILIAL_DESTINO_SG_FILIAL");
		sql.addProjection("PESSOA_DESTINO.NM_PESSOA", "PESSOA_DESTINO_NM_PESSOA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SERV.DS_SERVICO_I"), "SERV_DS_SERVICO");
		sql.addProjection("CONH.TP_FRETE", "CONH_TP_FRETE");
		sql.addProjection("DOSE.VL_TOTAL_PARCELAS", "DOSE_VL_TOTAL_PARCELAS");
		sql.addProjection("DOSE.VL_TOTAL_SERVICOS", "DOSE_VL_TOTAL_SERVICOS");
		sql.addProjection("DOSE.VL_TOTAL_DOC_SERVICO","DOSE_VL_TOTAL_DOC_SERVICO");
		sql.addProjection("MOED.DS_SIMBOLO", "MOED_DS_SIMBOLO");
		sql.addProjection("DOSE.DT_PREV_ENTREGA", "DOSE_DT_PREV_ENTREGA");      
		sql.addProjection("DOSE.NR_DIAS_BLOQUEIO", "CONH_NR_DIAS_BLOQUEIO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LOME.DS_LOCALIZACAO_MERCADORIA_I"), "LOME_DS_LOCALIZACAO_MERCADORIA");
		sql.addProjection("DOSE.QT_VOLUMES", "DOSE_QT_VOLUMES");
		sql.addProjection("DOSE.PS_REAL", "DOSE_PS_REAL");
		sql.addProjection("DOSE.PS_AFORADO", "DOSE_PS_AFORADO");
		sql.addProjection("DOSE.PS_AFERIDO", "DOSE_PS_AFERIDO"); //Demanda LMS-2345
		sql.addProjection("DOSE.PS_REFERENCIA_CALCULO", "DOSE_PS_REFERENCIA_CALCULO");
		sql.addProjection("DOSE.VL_MERCADORIA", "DOSE_VL_MERCADORIA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("NAPR.DS_NATUREZA_PRODUTO_I"), "NAPR_DS_NATUREZA_PRODUTO"); 
		sql.addProjection("PECO.DS_COMPLEMENTO_ENDERECO", "PECO_DS_COMPLEMENTO_ENDERECO");    
		sql.addProjection("NVL2(PECO.NR_ENDERECO,PECO.ED_COLETA||', '||PECO.NR_ENDERECO,PECO.ED_COLETA)","PECO_ED_COLETA");
		sql.addProjection("PECO.NR_CEP", "PECO_NR_CEP");
		sql.addProjection("PECO.NR_COLETA", "PECO_NR_COLETA");
		sql.addProjection("PECO.DT_PREVISAO_COLETA", "PECO_DT_PREVISAO_COLETA");
		sql.addProjection("PECO.DH_COLETA_DISPONIVEL", "PECO_DH_COLETA_DISPONIVEL");
		sql.addProjection("PECO.TP_PEDIDO_COLETA", "PECO_TP_PEDIDO_COLETA");
		sql.addProjection("PECO.TP_MODO_PEDIDO_COLETA", "PECO_TP_MODO_PEDIDO_COLETA");
		sql.addProjection("MUNI.NM_MUNICIPIO", "MUNI_NM_MUNICIPIO");
		sql.addProjection("UNFE.SG_UNIDADE_FEDERATIVA", "UNFE_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS.NM_PAIS_I"), "PAIS_NM_PAIS");
		sql.addProjection("DOSE.DS_ENDERECO_ENTREGA_REAL", "DOSE_DS_ENDERECO_ENTREGA_REAL");
		sql.addProjection("TITP.TP_TIPO_TABELA_PRECO", "TITP_TP_TIPO_TABELA_PRECO");
		sql.addProjection("TITP.NR_VERSAO", "TITP_NR_VERSAO");
		sql.addProjection("STTP.TP_SUBTIPO_TABELA_PRECO", "STTP_TP_SUBTIPO_TABELA_PRECO");
		sql.addProjection("DOSE.TP_CALCULO_PRECO", "DOSE_TP_CALCULO_PRECO");
		sql.addProjection("DOSE.ID_DOCTO_SERVICO", "DOSE_ID_DOCTO_SERVICO");		
		sql.addProjection("MACO.NR_MANIFESTO", "MACO_NR_MANIFESTO");
		sql.addProjection("MACO.DH_EMISSAO", "MACO_DH_EMISSAO");
		sql.addProjection("( SELECT EVDS.DH_EVENTO " +
						  "  FROM EVENTO_DOCUMENTO_SERVICO EVDS, " +
						  "		  EVENTO EVEN" +
						  "  WHERE EVDS.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO" +
						  "  AND EVEN.ID_EVENTO = EVDS.ID_EVENTO" +
						  "  AND EVEN.CD_EVENTO = 21" +
						  "  AND EVDS.BL_EVENTO_CANCELADO != 'S' " + 
						  "	 AND ROWNUM <= 1)", "EVDS_DH_EVENTO");		
		//Integrantes Remetente
		sql.addProjection("PERE.ID_PESSOA", "PERE_ID_PESSOA");
		sql.addProjection("PERE.TP_IDENTIFICACAO", "PERE_TP_IDENTIFICACAO");
		sql.addProjection("PERE.NR_IDENTIFICACAO", "PERE_NR_IDENTIFICACAO");
		sql.addProjection("PERE.NM_PESSOA", "PERE_NM_PESSOA");
		sql.addProjection("ENDERECO_PERE.DS_ENDERECO", "ENDERECO_PERE_DS_ENDERECO");
		sql.addProjection("ENDERECO_PERE.NR_ENDERECO", "ENDERECO_PERE_NR_ENDERECO");
		sql.addProjection("ENDERECO_PERE.NR_CEP", "ENDERECO_PERE_NR_CEP");
		sql.addProjection("MUNICIPIO_PERE.NM_MUNICIPIO", "MUNICIPIO_PERE_NM_MUNICIPIO");
		sql.addProjection("UF_PERE.SG_UNIDADE_FEDERATIVA", "UF_PERE_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PERE.NM_PAIS_I"), "PAIS_PERE_NM_PAIS");
		//Integrantes Destinatario
		sql.addProjection("PEDE.ID_PESSOA", "PEDE_ID_PESSOA");
		sql.addProjection("PEDE.TP_IDENTIFICACAO", "PEDE_TP_IDENTIFICACAO");
		sql.addProjection("PEDE.NR_IDENTIFICACAO", "PEDE_NR_IDENTIFICACAO");
		sql.addProjection("PEDE.NM_PESSOA", "PEDE_NM_PESSOA");
		sql.addProjection("ENDERECO_PEDE.DS_ENDERECO", "ENDERECO_PEDE_DS_ENDERECO");
		sql.addProjection("ENDERECO_PEDE.NR_ENDERECO", "ENDERECO_PEDE_NR_ENDERECO");
		sql.addProjection("ENDERECO_PEDE.NR_CEP", "ENDERECO_PEDE_NR_CEP");
		sql.addProjection("MUNICIPIO_PEDE.NM_MUNICIPIO", "MUNICIPIO_PEDE_NM_MUNICIPIO");
		sql.addProjection("UF_PEDE.SG_UNIDADE_FEDERATIVA", "UF_PEDE_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PEDE.NM_PAIS_I"), "PAIS_PEDE_NM_PAIS");
		//Integrantes Redespacho
		sql.addProjection("PERD.ID_PESSOA", "PERD_ID_PESSOA");
		sql.addProjection("PERD.TP_IDENTIFICACAO", "PERD_TP_IDENTIFICACAO");
		sql.addProjection("PERD.NR_IDENTIFICACAO", "PERD_NR_IDENTIFICACAO");
		sql.addProjection("PERD.NM_PESSOA", "PERD_NM_PESSOA");
		sql.addProjection("ENDERECO_PERD.DS_ENDERECO", "ENDERECO_PERD_DS_ENDERECO");
		sql.addProjection("ENDERECO_PERD.NR_ENDERECO", "ENDERECO_PERD_NR_ENDERECO");
		sql.addProjection("ENDERECO_PERD.NR_CEP", "ENDERECO_PERD_NR_CEP");
		sql.addProjection("MUNICIPIO_PERD.NM_MUNICIPIO", "MUNICIPIO_PERD_NM_MUNICIPIO");
		sql.addProjection("UF_PERD.SG_UNIDADE_FEDERATIVA", "UF_PERD_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PERD.NM_PAIS_I"), "PAIS_PERD_NM_PAIS");
		//Integrantes Consignatario
		sql.addProjection("PECON.ID_PESSOA", "PECON_ID_PESSOA");
		sql.addProjection("PECON.TP_IDENTIFICACAO", "PECON_TP_IDENTIFICACAO");
		sql.addProjection("PECON.NR_IDENTIFICACAO", "PECON_NR_IDENTIFICACAO");
		sql.addProjection("PECON.NM_PESSOA", "PECON_NM_PESSOA");
		sql.addProjection("ENDERECO_PECON.DS_ENDERECO", "ENDERECO_PECON_DS_ENDERECO");
		sql.addProjection("ENDERECO_PECON.NR_ENDERECO", "ENDERECO_PECON_NR_ENDERECO");
		sql.addProjection("ENDERECO_PECON.NR_CEP", "ENDERECO_PECON_NR_CEP");
		sql.addProjection("MUNICIPIO_PECON.NM_MUNICIPIO", "MUNICIPIO_PECON_NM_MUNICIPIO");
		sql.addProjection("UF_PECON.SG_UNIDADE_FEDERATIVA", "UF_PECON_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_PECON.NM_PAIS_I"), "PAIS_PECON_NM_PAIS");	
		
		sql.addProjection("DOSE.VL_FRETE_LIQUIDO","DOSE_VL_FRETE_LIQUIDO");
		sql.addProjection("DOSE.VL_ICMS_ST","DOSE_VL_ICMS_ST");
		
		/** FROM */
		sql.addFrom("DOCTO_SERVICO", "DOSE");
		sql.addFrom("FILIAL", "FILIAL_ORIGEM");
		sql.addFrom("FILIAL", "FILIAL_DESTINO");
		sql.addFrom("PESSOA", "PESSOA_DESTINO");
		sql.addFrom("CONHECIMENTO", "CONH");
		sql.addFrom("SERVICO", "SERV");
		sql.addFrom("MOEDA", "MOED");
		sql.addFrom("LOCALIZACAO_MERCADORIA", "LOME");
		sql.addFrom("NATUREZA_PRODUTO", "NAPR");
		sql.addFrom("PEDIDO_COLETA", "PECO");
		sql.addFrom("MUNICIPIO", "MUNI");
		sql.addFrom("UNIDADE_FEDERATIVA", "UNFE");
		sql.addFrom("PAIS", "PAIS");
		sql.addFrom("TABELA_PRECO", "TAPE");
		sql.addFrom("TIPO_TABELA_PRECO", "TITP");
		sql.addFrom("TIPO_TRIBUTACAO_ICMS", "TITI");
		sql.addFrom("SUBTIPO_TABELA_PRECO", "STTP");
		sql.addFrom("MANIFESTO_COLETA", "MACO");

		// Remetente
		sql.addFrom("PESSOA", "PERE");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PERE");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PERE");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PERE");
		sql.addFrom("PAIS", "PAIS_PERE");
		// Destinatario
		sql.addFrom("PESSOA", "PEDE");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PEDE");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PEDE");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PEDE");
		sql.addFrom("PAIS", "PAIS_PEDE");
		// Redespacho
		sql.addFrom("PESSOA", "PERD");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PERD");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PERD");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PERD");
		sql.addFrom("PAIS", "PAIS_PERD");
		// Consignatario
		sql.addFrom("PESSOA", "PECON");
		sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PECON");
		sql.addFrom("MUNICIPIO", "MUNICIPIO_PECON");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF_PECON");
		sql.addFrom("PAIS", "PAIS_PECON");
		
		
		/** JOIN */
		sql.addJoin("FILIAL_ORIGEM.ID_FILIAL(+)", "DOSE.ID_FILIAL_ORIGEM");
		sql.addJoin("FILIAL_DESTINO.ID_FILIAL(+)", "DOSE.ID_FILIAL_DESTINO");
		sql.addJoin("PESSOA_DESTINO.ID_PESSOA(+)", "FILIAL_DESTINO.ID_FILIAL");
		sql.addJoin("PECO.ID_PEDIDO_COLETA(+)", "DOSE.ID_PEDIDO_COLETA");
		sql.addJoin("MACO.ID_MANIFESTO_COLETA(+)", "PECO.ID_MANIFESTO_COLETA");
		sql.addJoin("TAPE.ID_TABELA_PRECO(+)", "DOSE.ID_TABELA_PRECO");
		sql.addJoin("TITP.ID_TIPO_TABELA_PRECO(+)",	"TAPE.ID_TIPO_TABELA_PRECO");
		sql.addJoin("STTP.ID_SUBTIPO_TABELA_PRECO(+)", "TAPE.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("MUNI.ID_MUNICIPIO(+)", "PECO.ID_MUNICIPIO");
		sql.addJoin("UNFE.ID_UNIDADE_FEDERATIVA(+)", "MUNI.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS.ID_PAIS(+)", "UNFE.ID_PAIS");
		sql.addJoin("CONH.ID_CONHECIMENTO(+)", "DOSE.ID_DOCTO_SERVICO");
		sql.addJoin("TITI.ID_TIPO_TRIBUTACAO_ICMS(+)", "CONH.ID_TIPO_TRIBUTACAO_ICMS");
		sql.addJoin("NAPR.ID_NATUREZA_PRODUTO(+)", "CONH.ID_NATUREZA_PRODUTO");
		sql.addJoin("SERV.ID_SERVICO(+)", "DOSE.ID_SERVICO");
		sql.addJoin("MOED.ID_MOEDA(+)", "DOSE.ID_MOEDA");
		sql.addJoin("LOME.ID_LOCALIZACAO_MERCADORIA(+)", "DOSE.ID_LOCALIZACAO_MERCADORIA");

		
		// Remetente
		sql.addJoin("PERE.ID_PESSOA(+)", "DOSE.ID_CLIENTE_REMETENTE");
		sql.addJoin("ENDERECO_PERE.ID_ENDERECO_PESSOA(+)", "PERE.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PERE.ID_MUNICIPIO(+)", "ENDERECO_PERE.ID_MUNICIPIO");
		sql.addJoin("UF_PERE.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PERE.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PERE.ID_PAIS(+)", "UF_PERE.ID_PAIS");
		// Destinatario
		sql.addJoin("PEDE.ID_PESSOA(+)", "DOSE.ID_CLIENTE_DESTINATARIO");
		sql.addJoin("ENDERECO_PEDE.ID_ENDERECO_PESSOA(+)", "PEDE.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PEDE.ID_MUNICIPIO(+)", "ENDERECO_PEDE.ID_MUNICIPIO");
		sql.addJoin("UF_PEDE.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PEDE.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PEDE.ID_PAIS(+)", "UF_PEDE.ID_PAIS");
		// Redespacho
		sql.addJoin("PERD.ID_PESSOA(+)", "DOSE.ID_CLIENTE_REDESPACHO");
		sql.addJoin("ENDERECO_PERD.ID_ENDERECO_PESSOA(+)", "PERD.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PERD.ID_MUNICIPIO(+)", "ENDERECO_PERD.ID_MUNICIPIO");
		sql.addJoin("UF_PERD.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PERD.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PERD.ID_PAIS(+)", "UF_PERD.ID_PAIS");
		// Consignatario
		sql.addJoin("PECON.ID_PESSOA(+)", "DOSE.ID_CLIENTE_CONSIGNATARIO");
		sql.addJoin("ENDERECO_PECON.ID_ENDERECO_PESSOA(+)", "PECON.ID_ENDERECO_PESSOA");
		sql.addJoin("MUNICIPIO_PECON.ID_MUNICIPIO(+)", "ENDERECO_PECON.ID_MUNICIPIO");
		sql.addJoin("UF_PECON.ID_UNIDADE_FEDERATIVA(+)", "MUNICIPIO_PECON.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS_PECON.ID_PAIS(+)", "UF_PECON.ID_PAIS");
		
		/** CRITERIA */
		if (criteria.getLong("idDoctoServico") != null) {
			sql.addCriteria("DOSE.ID_DOCTO_SERVICO", "=", criteria.getLong("idDoctoServico"));
		}

		return sql;
	}

	private SqlTemplate createSqlComposicaoFrete(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAPR.NM_PARCELA_PRECO_I"), "PAPR_NM_PARCELA_PRECO");
		sql.addProjection("PADS.VL_PARCELA", "PADS_VL_PARCELA");

		/** FROM */
		sql.addFrom("PARCELA_DOCTO_SERVICO", "PADS");
		sql.addFrom("PARCELA_PRECO", "PAPR");

		/** JOIN */
		sql.addJoin("PADS.ID_PARCELA_PRECO", "PAPR.ID_PARCELA_PRECO");

		/** CRITERIA */
		sql.addCriteria("PADS.ID_DOCTO_SERVICO", "=", idDoctoServico);
		sql.addCriteria("PAPR.TP_PARCELA_PRECO", "<>", "S");
		
		sql.addOrderBy("PAPR_NM_PARCELA_PRECO");

		return sql;
	}
	
	private SqlTemplate createSqlDadosServicosAdicionais(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("SA.DS_SERVICO_ADICIONAL_I"), "DS_SERVICO_ADICIONAL");
		sql.addProjection("SADS.VL_MERCADORIA", "VL_MERCADORIA_ADICIONAL");
		sql.addProjection("SADS.QT_CHEQUES", "QT_CHEQUES_ADICIONAIS");
		sql.addProjection("SADS.DT_PRIMEIRO_CHEQUE", "DT_PRIMEIRO_CHEQUE_ADICIONAL");
		sql.addProjection("SADS.QT_PALETES", "QT_PALETES_ADICIONAIS");
		sql.addProjection("SADS.QT_DIAS", "QT_DIAS_ADICIONAIS");
		sql.addProjection("SADS.NR_KM_RODADO", "NR_KM_RODADO_ADICIONAIS");
		sql.addProjection("SADS.QT_SEGURANCAS_ADICIONAIS", "QT_SEGURANCAS_ADICIONAIS");
		sql.addProjection("SADS.QT_COLETAS", "QT_COLETAS_ADICIONAIS");

		/** FROM */
		sql.addFrom("SERV_ADICIONAL_DOC_SERV", "SADS");
		sql.addFrom("SERVICO_ADICIONAL", "SA");
		sql.addFrom("DOCTO_SERVICO", "DS");

		/** JOIN */
		sql.addJoin("SADS.ID_SERVICO_ADICIONAL", "SA.ID_SERVICO_ADICIONAL");
		sql.addJoin("SADS.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
		

		/** CRITERIA */
		sql.addCriteria("SADS.ID_DOCTO_SERVICO", "=", idDoctoServico);
		sql.addCustomCriteria("SADS.ID_SERVICO_ADICIONAL NOT IN (1,42,43,44,45)");
		return sql;
	}

	private SqlTemplate createSqlServicosAdicionais(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAPR.NM_PARCELA_PRECO_I"), "PAPR_NM_PARCELA_PRECO");
		sql.addProjection("PADS.VL_PARCELA", "PADS_VL_PARCELA");
		
		/** FROM */
		sql.addFrom("PARCELA_DOCTO_SERVICO", "PADS");
		sql.addFrom("PARCELA_PRECO", "PAPR");
		
		/** JOIN */
		sql.addJoin("PADS.ID_PARCELA_PRECO", "PAPR.ID_PARCELA_PRECO");
		
		/** CRITERIA */
		sql.addCriteria("PADS.ID_DOCTO_SERVICO", "=", idDoctoServico);
		sql.addCriteria("PAPR.TP_PARCELA_PRECO", "=", "S");
		
		return sql;
	}

	private SqlTemplate createSqlImpostos(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */
		sql.add(" SELECT INSE.TP_IMPOSTO AS INSE_TP_IMPOSTO, ");
		sql.add(" INSE.VL_BASE_CALCULO AS INSE_VL_BASE_CALCULO, ");
		sql.add(" INSE.PC_ALIQUOTA AS INSE_PC_ALIQUOTA, ");
		sql.add(" INSE.VL_IMPOSTO AS INSE_VL_IMPOSTO ");
		
		/** FROM */
		sql.add(" FROM IMPOSTO_SERVICO INSE ");
		
		/** CRITERIA */
		sql.add(" WHERE INSE.ID_CONHECIMENTO = " + idDoctoServico);

		sql.add(" UNION ");
		
		/** SELECT */
		sql.add(" SELECT 'ICMS' AS INSE_TP_IMPOSTO, ");
		sql.add(" DECODE(DOSE.VL_BASE_CALC_IMPOSTO, null, 0, DOSE.VL_BASE_CALC_IMPOSTO) AS INSE_VL_BASE_CALCULO, ");
		sql.add(" DECODE(DOSE.PC_ALIQUOTA_ICMS, null, 0, DOSE.PC_ALIQUOTA_ICMS) AS INSE_PC_ALIQUOTA, ");
		sql.add(" DECODE(DOSE.VL_IMPOSTO, null, 0, DOSE.VL_IMPOSTO) AS INSE_VL_IMPOSTO ");
		
		/** FROM */
		sql.add(" FROM DOCTO_SERVICO DOSE ");
		
		/** JOIN */
		sql.add(" WHERE DOSE.ID_DOCTO_SERVICO = " + idDoctoServico);
		sql.add(" AND DOSE.VL_IMPOSTO IS NOT NULL ");
		
		return sql;
	}
	
	private SqlTemplate createSqlNotasFiscais(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */
		sql.addProjection("NOFC.NR_NOTA_FISCAL", "NOFC_NR_NOTA_FISCAL");
		sql.addProjection("NOFC.DS_SERIE", "NOFC_DS_SERIE");
		sql.addProjection("NOFC.DT_EMISSAO", "NOFC_DT_EMISSAO");
		sql.addProjection("NOFC.QT_VOLUMES", "NOFC_QT_VOLUMES");
		sql.addProjection("NOFC.PS_MERCADORIA", "NOFC_PS_MERCADORIA");
		sql.addProjection("NOFC.VL_TOTAL", "NOFC_VL_TOTAL");
		sql.addProjection("NOFC.DT_SAIDA", "NOFC_DT_SAIDA");
		sql.addProjection("NOFC.NR_CFOP", "NOFC_NR_CFOP");

		/** FROM */
		sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "NOFC");

		/** CRITERIA */
		sql.addCriteria("NOFC.ID_CONHECIMENTO", "=", idDoctoServico);

		/** ORDER */
		sql.addOrderBy("NOFC.NR_NOTA_FISCAL");

		return sql;
	}

	private SqlTemplate createSqlCobranca(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */
		sql.addProjection("PESS.TP_IDENTIFICACAO", "PESS_TP_IDENTIFICACAO");
		sql.addProjection("PESS.NR_IDENTIFICACAO", "PESS_NR_IDENTIFICACAO");
		sql.addProjection("PESS.NM_PESSOA", "PESS_NM_PESSOA");
		sql.addProjection("DDSF.DT_LIQUIDACAO", "DDSF_DT_LIQUIDACAO");
		sql.addProjection("DDSF.TP_SITUACAO_COBRANCA", "DDSF_TP_SITUACAO_COBRANCA");
		sql.addProjection("FILIAL.SG_FILIAL", "FILIAL_SG_FILIAL");
		sql.addProjection("MUNI.NM_MUNICIPIO", "MUNI_NM_MUNICIPIO");
		sql.addProjection("UNFE.SG_UNIDADE_FEDERATIVA", "UNFE_SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS.NM_PAIS_I"), "PAIS_NM_PAIS");
		sql.addProjection("ENPE.NR_CEP", "ENPE_NR_CEP");
		sql.addProjection("ENPE.ID_PESSOA", "ENPE_ID_PESSOA");
		
		/** FROM */
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addFrom("CLIENTE", "CLIE");
		sql.addFrom("PESSOA", "PESS");
		sql.addFrom("ENDERECO_PESSOA", "ENPE");
		sql.addFrom("MUNICIPIO", "MUNI");
		sql.addFrom("UNIDADE_FEDERATIVA", "UNFE");
		sql.addFrom("PAIS", "PAIS");
		sql.addFrom("FILIAL", "FILIAL");

		/** JOIN */
		sql.addJoin("CLIE.ID_CLIENTE", "DDSF.ID_CLIENTE");
		sql.addJoin("PESS.ID_PESSOA", "CLIE.ID_CLIENTE");
		sql.addJoin("ENPE.ID_PESSOA(+)", "PESS.ID_PESSOA");
		
		sql.addJoin("ENPE.ID_ENDERECO_PESSOA", "nvl(( select ep.id_endereco_pessoa"+ 
		         						            " from endereco_pessoa ep,"+ 
		         						            "      tipo_endereco_pessoa tiep"+
		         						            " where ep.id_pessoa = enpe.id_pessoa" +
		         						            " and ep.id_endereco_pessoa = tiep.id_endereco_pessoa"+
		         						            " and tiep.tp_endereco ='COB'"+
		         						            " and trunc(sysdate) between ep.dt_vigencia_inicial and ep.dt_vigencia_final ), PESS.ID_ENDERECO_PESSOA)");
		sql.addJoin("MUNI.ID_MUNICIPIO", "ENPE.ID_MUNICIPIO");
		sql.addJoin("UNFE.ID_UNIDADE_FEDERATIVA", "MUNI.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PAIS.ID_PAIS", "UNFE.ID_PAIS");
		sql.addJoin("FILIAL.ID_FILIAL", "DDSF.ID_FILIAL");
		
		/** CRITERIA */
		sql.addCriteria("DDSF.ID_DOCTO_SERVICO", "=", idDoctoServico);
		
		return sql;
	}

	private SqlTemplate createSqlControlesCarga(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */
		sql.add(" SELECT FILIAL_ORIGEM.SG_FILIAL AS FILIAL_ORIGEM_SG_FILIAL, " + 
				" 		 COCA.NR_CONTROLE_CARGA AS COCA_NR_CONTROLE_CARGA, " + 
				"		 FILIAL_DESTINO.SG_FILIAL AS FILIAL_DESTINO_SG_FILIAL, " + 
				"		 COCA.TP_CONTROLE_CARGA AS COCA_TP_CONTROLE_CARGA, " + 
				"		 COCA.DH_GERACAO AS COCA_DH_GERACAO, " +  
				"		 DECODE (COCA.TP_CONTROLE_CARGA, 'C', COCA.DH_SAIDA_COLETA_ENTREGA, ( SELECT COTR.DH_SAIDA " +
				"																			  FROM CONTROLE_TRECHO COTR " +
				"																			  WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				"																			  AND  COTR.ID_FILIAL_ORIGEM = COCA.ID_FILIAL_ORIGEM" +
				"																		      AND  COTR.ID_FILIAL_DESTINO = COCA.ID_FILIAL_DESTINO ) ) AS COCA_DH_SAIDA, " + 
				"		 DECODE (COCA.TP_CONTROLE_CARGA, 'V', ( SELECT COTR.DH_CHEGADA " +
				"											    FROM CONTROLE_TRECHO COTR " +
				"  											    WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				"											    AND  COTR.ID_FILIAL_ORIGEM = COCA.ID_FILIAL_ORIGEM" +
				"											    AND  COTR.ID_FILIAL_DESTINO = COCA.ID_FILIAL_DESTINO ), COCA.DH_CHEGADA_COLETA_ENTREGA ) AS COCA_DH_CHEGADA " + 
		
		/** FROM */
				" FROM DOCTO_SERVICO DOSE," +
				"      PEDIDO_COLETA PECO," +
				"      MANIFESTO_COLETA MACO," +
				"      CONTROLE_CARGA COCA," +
				"      FILIAL FILIAL_ORIGEM," + 
				"      FILIAL FILIAL_DESTINO " + 
		/** JOIN */
				" WHERE DOSE.ID_DOCTO_SERVICO = " + idDoctoServico + 
				" AND   PECO.ID_PEDIDO_COLETA = DOSE.ID_PEDIDO_COLETA" +
				" AND   MACO.ID_MANIFESTO_COLETA = PECO.ID_MANIFESTO_COLETA" +
				" AND   COCA.ID_CONTROLE_CARGA = MACO.ID_CONTROLE_CARGA" + 
				" AND   FILIAL_ORIGEM.ID_FILIAL = COCA.ID_FILIAL_ORIGEM" +
				" AND   FILIAL_DESTINO.ID_FILIAL(+) = COCA.ID_FILIAL_DESTINO" +
		
				" UNION ALL " + 

		/** SELECT */
				" SELECT FILIAL_ORIGEM.SG_FILIAL AS FILIAL_ORIGEM_SG_FILIAL, " + 
				" 		 COCA.NR_CONTROLE_CARGA AS COCA_NR_CONTROLE_CARGA, " + 
				"		 FILIAL_DESTINO.SG_FILIAL AS FILIAL_DESTINO_SG_FILIAL, " + 
				"		 COCA.TP_CONTROLE_CARGA AS COCA_TP_CONTROLE_CARGA, " + 
				"		 COCA.DH_GERACAO AS COCA_DH_GERACAO, " +  
				"		 DECODE (COCA.TP_CONTROLE_CARGA, 'C', COCA.DH_SAIDA_COLETA_ENTREGA, ( SELECT COTR.DH_SAIDA " +
				"																			  FROM CONTROLE_TRECHO COTR " +
				"																			  WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				"																			  AND  COTR.ID_FILIAL_ORIGEM = COCA.ID_FILIAL_ORIGEM" +
				"																		      AND  COTR.ID_FILIAL_DESTINO = COCA.ID_FILIAL_DESTINO ) ) AS COCA_DH_SAIDA, " + 
				"		 DECODE (COCA.TP_CONTROLE_CARGA, 'V', ( SELECT COTR.DH_CHEGADA " +
				"												FROM CONTROLE_TRECHO COTR " +
				"  												WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				"												AND  COTR.ID_FILIAL_ORIGEM = COCA.ID_FILIAL_ORIGEM" +
				"												AND  COTR.ID_FILIAL_DESTINO = COCA.ID_FILIAL_DESTINO ), COCA.DH_CHEGADA_COLETA_ENTREGA ) AS COCA_DH_CHEGADA " + 
				
		/** FROM */
				" FROM DOCTO_SERVICO DOSE," +
				"      PRE_MANIFESTO_DOCUMENTO PRMD," +
				"      MANIFESTO MANI," +
				"      CONTROLE_CARGA COCA," +
				"      FILIAL FILIAL_ORIGEM," + 
				"      FILIAL FILIAL_DESTINO " + 
				
		/** JOIN */
				" WHERE DOSE.ID_DOCTO_SERVICO = " + idDoctoServico + 
				" AND   PRMD.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO" +
				" AND   MANI.ID_MANIFESTO = PRMD.ID_MANIFESTO" +
				" AND   COCA.ID_CONTROLE_CARGA = MANI.ID_CONTROLE_CARGA" + 
				" AND   COCA.TP_STATUS_CONTROLE_CARGA <> 'CA'" + 
				" AND   FILIAL_ORIGEM.ID_FILIAL = COCA.ID_FILIAL_ORIGEM" +
				" AND   FILIAL_DESTINO.ID_FILIAL(+) = COCA.ID_FILIAL_DESTINO" +
				
				" ORDER BY 5"); 
		return sql;
	}

	private SqlTemplate createSqlViagem(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();
		
		/** SELECT */
		sql.add("SELECT FILIAL_ORIGEM.SG_FILIAL AS SG_FILIAL_ORIGEM, ");
		sql.add("MAVN.NR_MANIFESTO_ORIGEM AS NR_MANIFESTO, ");
		sql.add("FILIAL_DESTINO.SG_FILIAL AS SG_FILIAL_DESTINO, ");
		sql.add("MANI.DH_EMISSAO_MANIFESTO AS DH_EMISSAO_MANIFESTO, ");
		sql.add("(SELECT COTR.DH_PREVISAO_SAIDA AS DH_PREVISAO_SAIDA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_PREVISAO_SAIDA, ");
		sql.add("(SELECT COTR.DH_SAIDA AS DH_SAIDA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_SAIDA, ");
		
		sql.add("(SELECT COTR.DH_PREVISAO_CHEGADA AS DH_PREVISAO_CHEGADA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_PREVISAO_CHEGADA, ");

		sql.add("(SELECT COTR.DH_CHEGADA AS DH_CHEGADA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_CHEGADA, ");

		sql.add("(SELECT CADE.DH_INICIO_OPERACAO AS DH_INICIO_OPERACAO " +
				" FROM CARREGAMENTO_DESCARGA CADE" +
				" WHERE CADE.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND CADE.ID_FILIAL = MANI.ID_FILIAL_DESTINO" +
				" AND CADE.TP_OPERACAO = 'D'" +
				" AND CADE.DH_CANCELAMENTO_OPERACAO IS NULL" +
				" AND ROWNUM <= 1 ) AS DH_INICIO_OPERACAO, ");

		sql.add("(SELECT CADE.DH_FIM_OPERACAO AS DH_FIM_OPERACAO " +
				" FROM CARREGAMENTO_DESCARGA CADE" +
				" WHERE CADE.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND CADE.ID_FILIAL = MANI.ID_FILIAL_DESTINO" +
				" AND CADE.TP_OPERACAO = 'D'" +
				" AND CADE.DH_CANCELAMENTO_OPERACAO IS NULL" +
				" AND ROWNUM <= 1 ) AS DH_FIM_OPERACAO ");
		
		/** FROM */
		sql.add("FROM MANIFESTO_NACIONAL_CTO MANC, ");
		sql.add("MANIFESTO_VIAGEM_NACIONAL MAVN, ");
		sql.add("MANIFESTO MANI, ");
		sql.add("FILIAL FILIAL_ORIGEM, ");
		sql.add("FILIAL FILIAL_DESTINO, ");
		sql.add("CONTROLE_CARGA COCA ");
		
		/** JOIN */
		sql.add("WHERE MAVN.ID_MANIFESTO_VIAGEM_NACIONAL = MANC.ID_MANIFESTO_VIAGEM_NACIONAL ");
		sql.add("AND MANI.ID_MANIFESTO = MAVN.ID_MANIFESTO_VIAGEM_NACIONAL ");
		sql.add("AND FILIAL_ORIGEM.ID_FILIAL = MANI.ID_FILIAL_ORIGEM ");
		sql.add("AND FILIAL_DESTINO.ID_FILIAL = MANI.ID_FILIAL_DESTINO ");
		sql.add("AND COCA.ID_CONTROLE_CARGA(+) = MANI.ID_CONTROLE_CARGA ");
		
		/** CRITERIA */
		sql.add("AND MANC.ID_CONHECIMENTO = " + idDoctoServico + " ");
		
		sql.add(" UNION ALL ");

		/** SELECT */
		sql.add("SELECT FILIAL_ORIGEM.SG_FILIAL AS SG_FILIAL_ORIGEM, ");
		sql.add("MAIN.NR_MANIFESTO_INT AS NR_MANIFESTO, ");
		sql.add("FILIAL_DESTINO.SG_FILIAL AS SG_FILIAL_DESTINO, ");
		sql.add("MANI.DH_EMISSAO_MANIFESTO AS DH_EMISSAO_MANIFESTO, ");		
		sql.add("(SELECT COTR.DH_PREVISAO_SAIDA AS DH_PREVISAO_SAIDA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_PREVISAO_SAIDA, ");
		sql.add("(SELECT COTR.DH_SAIDA AS DH_SAIDA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_SAIDA, ");
		
		sql.add("(SELECT COTR.DH_PREVISAO_CHEGADA AS DH_PREVISAO_CHEGADA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_PREVISAO_CHEGADA, ");

		sql.add("(SELECT COTR.DH_CHEGADA AS DH_CHEGADA " +
				" FROM CONTROLE_TRECHO COTR" +
				" WHERE COTR.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND COTR.ID_FILIAL_ORIGEM = MANI.ID_FILIAL_ORIGEM" +
				" AND COTR.ID_FILIAL_DESTINO = MANI.ID_FILIAL_DESTINO" +
				" AND ROWNUM <= 1 ) AS DH_CHEGADA, ");

		sql.add("(SELECT CADE.DH_INICIO_OPERACAO AS DH_INICIO_OPERACAO " +
				" FROM CARREGAMENTO_DESCARGA CADE" +
				" WHERE CADE.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND CADE.ID_FILIAL = MANI.ID_FILIAL_DESTINO" +
				" AND CADE.TP_OPERACAO = 'D'" +
				" AND CADE.DH_CANCELAMENTO_OPERACAO IS NULL" +
				" AND ROWNUM <= 1 ) AS DH_INICIO_OPERACAO, ");

		sql.add("(SELECT CADE.DH_FIM_OPERACAO AS DH_FIM_OPERACAO " +
				" FROM CARREGAMENTO_DESCARGA CADE" +
				" WHERE CADE.ID_CONTROLE_CARGA = COCA.ID_CONTROLE_CARGA" +
				" AND CADE.ID_FILIAL = MANI.ID_FILIAL_DESTINO" +
				" AND CADE.TP_OPERACAO = 'D'" +
				" AND CADE.DH_CANCELAMENTO_OPERACAO IS NULL" +
				" AND ROWNUM <= 1 ) AS DH_FIM_OPERACAO ");
		
		/** FROM */
		sql.add("FROM MANIFESTO_INTERNAC_CTO MAIC, ");
		sql.add("MANIFESTO_INTERNACIONAL MAIN, ");
		sql.add("MANIFESTO MANI, ");
		sql.add("FILIAL FILIAL_ORIGEM, ");
		sql.add("FILIAL FILIAL_DESTINO, ");
		sql.add("CONTROLE_CARGA COCA ");

		/** JOIN */
		sql.add("WHERE MAIN.ID_MANIFESTO_INTERNACIONAL = MAIC.ID_MANIFESTO_INTERNACIONAL ");
		sql.add("AND MANI.ID_MANIFESTO = MAIN.ID_MANIFESTO_INTERNACIONAL ");
		sql.add("AND FILIAL_ORIGEM.ID_FILIAL = MANI.ID_FILIAL_ORIGEM ");
		sql.add("AND FILIAL_DESTINO.ID_FILIAL = MANI.ID_FILIAL_DESTINO ");
		sql.add("AND COCA.ID_CONTROLE_CARGA(+) = MANI.ID_CONTROLE_CARGA ");
		
		/** CRITERIA */
		sql.add("AND MAIC.ID_CTO_INTERNACIONAL = " + idDoctoServico + " ");		

		/** ORDER */
		sql.add("ORDER BY 4 ");
		
		return sql;
	}
	
	private SqlTemplate createSqlManifestoEntrega(Long idDoctoServico)
			throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */
		sql.addProjection("FILIAL.SG_FILIAL", "FILIAL_SG_FILIAL");
		sql.addProjection("MAEN.NR_MANIFESTO_ENTREGA", "MAEN_NR_MANIFESTO_ENTREGA");
		sql.addProjection("MANI.DH_EMISSAO_MANIFESTO", "MANI_DH_EMISSAO_MANIFESTO");
		sql.addProjection("MAED.DH_OCORRENCIA", "MAED_DH_OCORRENCIA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("OCEN.DS_OCORRENCIA_ENTREGA_I"), "OCEN_DS_OCORRENCIA_ENTREGA");
		sql.addProjection("MAED.NM_RECEBEDOR", "MAED_NM_RECEBEDOR");

		/** FROM */
		sql.addFrom("MANIFESTO", "MANI");
		sql.addFrom("FILIAL", "FILIAL");
		sql.addFrom("MANIFESTO_ENTREGA", "MAEN");
		sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO", "MAED");
		sql.addFrom("OCORRENCIA_ENTREGA", "OCEN");

		/** JOIN */
		sql.addJoin("MANI.ID_MANIFESTO", "MAEN.ID_MANIFESTO_ENTREGA");
		sql.addJoin("FILIAL.ID_FILIAL", "MAEN.ID_FILIAL");
		sql.addJoin("MAEN.ID_MANIFESTO_ENTREGA", "MAED.ID_MANIFESTO_ENTREGA");
		sql.addJoin("OCEN.ID_OCORRENCIA_ENTREGA(+)", "MAED.ID_OCORRENCIA_ENTREGA");

		/** CRITERIA */
		sql.addCriteria("MAED.ID_DOCTO_SERVICO", "=", idDoctoServico);

		/** ORDER */
		sql.addOrderBy("MANI.DH_EMISSAO_MANIFESTO");

		return sql;
	}

	private SqlTemplate createSqlEventos(Long idDoctoServico) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		/** SELECT */
		sql.addProjection("EVDS.TP_DOCUMENTO", "EVDS_TP_DOCUMENTO");
		sql.addProjection("EVDS.NR_DOCUMENTO", "EVDS_NR_DOCUMENTO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("DEEV.DS_DESCRICAO_EVENTO_I"), "DEEV_DS_DESCRICAO_EVENTO");
		sql.addProjection("EVDS.DH_EVENTO", "EVDS_DH_EVENTO");
		sql.addProjection("EVDS.DH_EVENTO", "EVDS_DIA_DH_EVENTO");
		sql.addProjection("EVDS.OB_COMPLEMENTO", "EVDS_OB_COMPLEMENTO");
	sql.addProjection("NVL("+PropertyVarcharI18nProjection.createProjection("OCEN.DS_OCORRENCIA_ENTREGA_I")+", "+PropertyVarcharI18nProjection.createProjection("OCPE.DS_OCORRENCIA_I")+")", "DS_OCORRENCIA");

		/** FROM */
		sql.addFrom("EVENTO_DOCUMENTO_SERVICO", "EVDS");
		sql.addFrom("EVENTO", "EVEN");
		sql.addFrom("DESCRICAO_EVENTO", "DEEV");
		sql.addFrom("OCORRENCIA_ENTREGA", "OCEN");
		sql.addFrom("OCORRENCIA_PENDENCIA", "OCPE");

		/** JOIN */
		sql.addJoin("EVDS.ID_EVENTO", "EVEN.ID_EVENTO");
		sql.addJoin("OCEN.ID_OCORRENCIA_ENTREGA(+)", "EVDS.ID_OCORRENCIA_ENTREGA");
		sql.addJoin("OCPE.ID_OCORRENCIA_PENDENCIA(+)", "EVDS.ID_OCORRENCIA_PENDENCIA");
		sql.addJoin("EVEN.ID_DESCRICAO_EVENTO", "DEEV.ID_DESCRICAO_EVENTO");

		/** CRITERIA */
		sql.addCriteria("EVDS.ID_DOCTO_SERVICO", "=", idDoctoServico);

		/** ORDER */
		sql.addOrderBy("EVDS.DH_INCLUSAO");

		return sql;
	}

	/**
	 * Configura Dominios
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("COCA_TP_CONTROLE_CARGA", "DM_TIPO_CONTROLE_CARGAS");
		config.configDomainField("EVDS_TP_DOCUMENTO", "DM_TIPO_DOCUMENTO");
		config.configDomainField("DOSE_TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
		config.configDomainField("CONH_TP_CONHECIMENTO", "DM_TIPO_CONHECIMENTO");
		config.configDomainField("CONH_TP_FRETE", "DM_TIPO_FRETE");
		config.configDomainField("PECO_TP_PEDIDO_COLETA", "DM_TIPO_PEDIDO_COLETA");
		config.configDomainField("PECO_TP_MODO_PEDIDO_COLETA", "DM_MODO_PEDIDO_COLETA");
		config.configDomainField("TITP_TP_TIPO_TABELA_PRECO", "DM_TIPO_TABELA_PRECO");
		config.configDomainField("DOSE_TP_CALCULO_PRECO", "DM_TIPO_CALCULO_FRETE");
		config.configDomainField("DDSF_TP_SITUACAO_COBRANCA", "DM_STATUS_COBRANCA_DOCTO_SERVICO");
	}
}