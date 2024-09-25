package com.mercurio.lms.sim.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.util.FormatUtils;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.sim.emitirCartaServicosLocalizacaoClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/sim/report/emitirCartaServicoLocalizacaoCliente.jasper"
 */
public class EmitirCartaServicoLocalizacaoClienteService extends EmitirServicosLocalizacaoClienteService {
	
	private TelefoneContatoService telefoneContatoService;
	private ContatoService contatoService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		return super.execute(parameters);
	}

	public JRDataSource executeCabecalho(Long idDoctoServico, Long idContato, String tpCabecalho){
		SqlTemplate sql = montaSqlCabecalho(idDoctoServico, tpCabecalho);
		
		final List dados = new LinkedList();		
		
		Contato contato = null;
		if (idContato.longValue() > 0) {
			contato = contatoService.findById(idContato);
		}
		
		final String nmContato = contato == null ? null : contato.getNmContato();
		final List telefone;
		final List fax;
		if (idContato.longValue() > 0) {
			telefone = telefoneContatoService.findTelefoneByContatoAndTpUso(idContato, new String[]{"FO"});
			fax = telefoneContatoService.findTelefoneByContatoAndTpUso(idContato, new String[]{"FA"});
		} else {
			telefone = new ArrayList();
			fax = new ArrayList();
		}
		
		getJdbcTemplate().query(sql.getSql(true), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), new ResultSetExtractor() {
			
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {				
				
				if (rs.next()){
					Map linha = new HashMap();
					
					linha.put("nmPessoa", rs.getString("NM_PESSOA"));
					linha.put("tpIdentificacao", rs.getString("TP_IDENTIFICACAO"));					
					linha.put("nrIdentificacao", FormatUtils.formatIdentificacao(rs.getString("TP_IDENTIFICACAO"), rs.getString("NR_IDENTIFICACAO")));					
					linha.put("nrIe", rs.getString("NR_INSCRICAO_ESTADUAL"));		
					linha.put("nmMunicipio", rs.getString("NM_MUNICIPIO"));
					linha.put("sgUf", rs.getString("SG_UNIDADE_FEDERATIVA"));					
					linha.put("nrCep", FormatUtils.formatCep(rs.getString("SG_PAIS"), rs.getString("NR_CEP")));					
					linha.put("dsTipoLogradouro", rs.getString("DS_TIPO_LOGRADOURO"));
					linha.put("dsEndereco", rs.getString("DS_ENDERECO"));
					linha.put("nrEndereco", rs.getString("NR_ENDERECO"));
					linha.put("dsComplemento", rs.getString("DS_COMPLEMENTO"));
					linha.put("dsBairro", rs.getString("DS_BAIRRO"));
					
					linha.put("nmContato", nmContato);
					
					String nrTelefone = new String();
					if (!telefone.isEmpty()){
						Object[] tel = (Object[]) telefone.get(0);
						nrTelefone = FormatUtils.formatTelefone((String)tel[2], (String)tel[1], (String)tel[0]);
					}
					linha.put("nrTelefone", nrTelefone);
					
					String nrFax = new String();
					if (!fax.isEmpty()){
						Object[] f = (Object[]) fax.get(0);		
						nrFax = FormatUtils.formatTelefone((String)f[2], (String)f[1], (String)f[0]);			
					}
					linha.put("nrFax", nrFax);
														
					dados.add(linha);
				}						
				
				return null;
			}}
		);
					
		return new JRMapCollectionDataSource(dados);					
		
	}
	
	
	
	private SqlTemplate montaSqlCabecalho(Long idDoctoServico, String tpCabecalho){
		SqlTemplate sql = createSqlTemplate();
		 
		sql.addProjection("P.NM_PESSOA");
		sql.addProjection("P.TP_IDENTIFICACAO");
		sql.addProjection("P.NR_IDENTIFICACAO");
		sql.addProjection("IE.NR_INSCRICAO_ESTADUAL");		
    	sql.addProjection("M.NM_MUNICIPIO");
    	sql.addProjection("UF.SG_UNIDADE_FEDERATIVA");
    	sql.addProjection("PS.SG_PAIS");
    	sql.addProjection("EP.NR_CEP");
    	sql.addProjection("EP.DS_ENDERECO");
    	sql.addProjection("EP.NR_ENDERECO");
    	sql.addProjection("EP.DS_COMPLEMENTO");
    	sql.addProjection("EP.DS_BAIRRO");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("TL.DS_TIPO_LOGRADOURO_I"), "DS_TIPO_LOGRADOURO");
    	
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("PESSOA", "P");
		sql.addFrom("ENDERECO_PESSOA", "EP");
		sql.addFrom("INSCRICAO_ESTADUAL", "IE");
		sql.addFrom("MUNICIPIO", "M");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF");
		sql.addFrom("PAIS", "PS");
		sql.addFrom("TIPO_LOGRADOURO", "TL");
		
		if (tpCabecalho.equals("R"))
			sql.addJoin("DS.ID_CLIENTE_REMETENTE", "P.ID_PESSOA");
		else
			sql.addJoin("DS.ID_CLIENTE_DESTINATARIO", "P.ID_PESSOA");
		
		sql.addJoin("P.ID_ENDERECO_PESSOA", "EP.ID_ENDERECO_PESSOA");
		sql.addJoin("EP.ID_TIPO_LOGRADOURO", "TL.ID_TIPO_LOGRADOURO (+)");
		sql.addJoin("EP.ID_MUNICIPIO", "M.ID_MUNICIPIO (+)");
		sql.addJoin("M.ID_UNIDADE_FEDERATIVA", "UF.ID_UNIDADE_FEDERATIVA (+)");
		sql.addJoin("UF.ID_PAIS", "PS.ID_PAIS (+)");		
		sql.addJoin("P.ID_PESSOA", "IE.ID_PESSOA (+)");
		
		sql.addCustomCriteria("(IE.ID_INSCRICAO_ESTADUAL IS NULL OR IE.BL_INDICADOR_PADRAO = 'S')");
		sql.addCriteria("DS.ID_DOCTO_SERVICO", "=", idDoctoServico);
		
		return sql;
	}

	/**
	 * @param telefoneContatoService The telefoneContatoService to set.
	 */
	public void setTelefoneContatoService(
			TelefoneContatoService telefoneContatoService) {
		this.telefoneContatoService = telefoneContatoService;
	}

	/**
	 * @param contatoService The contatoService to set.
	 */
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	
}
