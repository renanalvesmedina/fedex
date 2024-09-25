package com.mercurio.lms.carregamento.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ReciboPostoPassagem;
import com.mercurio.lms.carregamento.model.service.ReciboPostoPassagemService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Recibo de Posto de Passagem - RPP
 * Especificação técnica 05.01.01.05
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.carregamento.emitirReciboPostoPassagemService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/emitirReciboPostoPassagem.jasper"
 */
public class EmitirReciboPostoPassagemService extends ReportServiceSupport {
	
	private ConfiguracoesFacade configuracoesFacade;
	private ReciboPostoPassagemService reciboPostoPassagemService;
	private TelefoneEnderecoService telefoneEnderecoService;
	

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setReciboPostoPassagemService(ReciboPostoPassagemService reciboPostoPassagemService) {
		this.reciboPostoPassagemService = reciboPostoPassagemService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}


	/**
     * método responsável por gerar o relatório. 
     * 
     * @param parameters 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap criteria = (TypedFlatMap) parameters;
        
    	//Carrega os parametros do relatorio...
        Map parametersReport = new HashMap();

        Long idReciboPostoPassagem = criteria.getLong("idReciboPostoPassagem");
        ReciboPostoPassagem rpp = reciboPostoPassagemService.findById(idReciboPostoPassagem);
        if (rpp.getDhEmissao() != null) {
        	parametersReport.put("reemissao", configuracoesFacade.getMensagem("reemissao"));  
        }

        parametersReport.put("nmEmpresaUsuarioLogado", SessionUtils.getEmpresaSessao().getPessoa().getNmPessoa());
        parametersReport.put("tpIdentificadorEmpresaUsuarioLogado", SessionUtils.getEmpresaSessao().getPessoa().getTpIdentificacao().getValue());
        parametersReport.put("nrIdentificadorEmpresaUsuarioLogado", FormatUtils.formatIdentificacao(
        		SessionUtils.getEmpresaSessao().getPessoa().getTpIdentificacao(),
        		SessionUtils.getEmpresaSessao().getPessoa().getNrIdentificacao()));

        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

        //Realiza a consulta e gera uma collection para o relatorio...
        List result = findReciboPostoPassagem(getSql(idReciboPostoPassagem));
    	JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(result);
        JRReportDataObject jrReportDataObject = createReportDataObject(jrMap, parametersReport);
        
        return jrReportDataObject;
    }

    /**
     * Gera a collection necessaria para utilizar o relatorio.
     * 
     * <b>Obs:<br>
     * Replica o registro capturado pela consulta para gera a segunda via do recibo 
     * no mesmo</b> 
     * 
     * @param sql
     * @return
     */
    private List findReciboPostoPassagem(SqlTemplate sql){
    
	    List query = getJdbcTemplate().query(sql.getSql(), sql.getCriteria(), new RowMapper(){
	    	
			Map map;
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				
				map = new HashMap();
				
				map.put("NRCONTROLECARGA", resultSet.getLong("nrControleCarga"));
				map.put("TPCONTROLECARGA", resultSet.getString("tpControleCarga"));
				
				map.put("DSENDERECOMOT", resultSet.getString("dsEnderecoMot"));
				map.put("NRENDERECOMOT", resultSet.getLong("nrEnderecoMot"));
				map.put("DSCOMPLEMENTOENDPESSOAMOT", resultSet.getString("dsComplementoEndPessoaMot"));
				map.put("DSENDERECOPROP", resultSet.getString("dsEnderecoProp"));
				map.put("NRENDERECOPROP", resultSet.getLong("nrEnderecoProp"));
				map.put("DSCOMPLEMENTOENDPESSOAPROP", resultSet.getString("dsComplementoEndPessoaProp"));
				
				map.put("SGFILIALORIGEM", resultSet.getString("sgFilialOrigem"));
				map.put("SGFILIALDESTINO", resultSet.getString("sgFilialDestino"));
				
				map.put("DSMARCAMEIOTRANS", resultSet.getString("dsMarcaMeioTrans"));
				map.put("NRANOFABRICACAO", resultSet.getLong("nrAnoFabricacao"));
				map.put("NRCAPACIDADEKG", resultSet.getLong("nrCapacidadeKG"));
				map.put("NRIDENTIFICADORMEIOTRANS", resultSet.getString("nrIdentificadorMeioTrans"));
				
				map.put("DSMODELOMEIOTRANSPORTE", resultSet.getString("dsModeloMeioTransporte"));
				map.put("NMMUNICIPIOMEIOTRANS", resultSet.getString("nmMunicipioMeioTrans"));
				map.put("DSTIPOMEIOTRANSPORTE", resultSet.getString("dsTipoMeioTransporte"));
				
				map.put("NMMUNICIPIOMOT", resultSet.getString("nmMunicipioMot"));
				map.put("NMMUNICIPIOPROP", resultSet.getString("nmMunicipioProp"));
				map.put("NMMUNICIPIOTRANS", resultSet.getString("nmMunicipioTrans"));
				
				map.put("SGUFMOT", resultSet.getString("sgUfMot"));
				map.put("SGUFPROP", resultSet.getString("sgUfProp"));
				
				map.put("NMPESSOAFILIALRPP", resultSet.getString("nmPessoaFilialRPP"));
				map.put("NMFANTASIACC", resultSet.getString("nmFantasiaCC"));
				map.put("TPIDENTIFICACAOCC", resultSet.getString("tpIdentificacaoCC"));
				map.put("NMFANTASIACCD", resultSet.getString("nmFantasiaCCD"));
				map.put("NRIDENTPESSOAFILIALRPP", resultSet.getLong("nrIdentPessoaFilialRPP"));
				map.put("NMFANTASIARPP", resultSet.getString("nmFantasiaRPP"));
				map.put("NMPESSOAFILIALRPP", resultSet.getString("nmPessoaFilialRPP"));
				map.put("NMPESSOAMOT", resultSet.getString("nmPessoaMot"));
				map.put("NRIDENTIFICACAOMOT", resultSet.getLong("nrIdentificacaoMot"));
				map.put("IDPESSOAMOT", resultSet.getLong("idPessoaMot"));
				map.put("NMPESSOAPROP", resultSet.getString("nmPessoaProp"));
				map.put("NRIDENTIFICACAOPROP", resultSet.getLong("nrIdentificacaoProp"));
				map.put("TPIDENTIFICACAOPROP", resultSet.getString("tpIdentificacaoProp"));
				map.put("IDPESSOAPROP", resultSet.getLong("idPessoaProp"));
				map.put("NRPIS", resultSet.getString("nrPis"));
				
				map.put("NRRECIBOPOSTOPASSAGEM", resultSet.getLong("nrReciboPostoPassagem"));
				map.put("VLBRUTO", resultSet.getBigDecimal("vlBruto"));
				
				map.put("DSSIMBOLO", resultSet.getString("dsSimbolo"));
				map.put("SGMOEDA", resultSet.getString("sgMoeda"));
				
				map.put("DSROTA", resultSet.getString("dsRota"));
				
				return map;
			}
		});
	    
	    //Replicacao de registro para a segunda via...
	    if (!query.isEmpty()) { 
	    	query.add(query.get(0)); 
	    }
	    
	    return query;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param idReciboPostoPassgem
     * @return SqlTemplate
     */
    private SqlTemplate getSql(Long idReciboPostoPassgem) { 
    	
    	SqlTemplate sql = createSqlTemplate();    	
    	
    	//Projection......
    	sql.addProjection("controleCarga.NR_CONTROLE_CARGA", "nrControleCarga");
    	sql.addProjection("controleCarga.ID_CONTROLE_CARGA", "idControleCarga");
    	sql.addProjection("controleCarga.TP_CONTROLE_CARGA", "tpControleCarga");
    	
    	sql.addProjection("enderecoPessoaMot.DS_ENDERECO", "dsEnderecoMot");
    	sql.addProjection("enderecoPessoaMot.NR_ENDERECO", "nrEnderecoMot");
    	sql.addProjection("enderecoPessoaMot.DS_COMPLEMENTO", "dsComplementoEndPessoaMot");
    	sql.addProjection("enderecoPessoaProp.DS_ENDERECO", "dsEnderecoProp");
    	sql.addProjection("enderecoPessoaProp.NR_ENDERECO", "nrEnderecoProp");
    	sql.addProjection("enderecoPessoaProp.DS_COMPLEMENTO", "dsComplementoEndPessoaProp");
    	
    	sql.addProjection("filialCC.SG_FILIAL", "sgFilialOrigem");
    	sql.addProjection("filialCCD.SG_FILIAL", "sgFilialDestino");
    	
    	sql.addProjection("marcaMeioTransporte.DS_MARCA_MEIO_TRANSPORTE", "dsMarcaMeioTrans");    	
    	sql.addProjection("meioTransporte.NR_ANO_FABRICAO", "nrAnoFabricacao");    	
    	sql.addProjection("meioTransporte.NR_CAPACIDADE_KG", "nrCapacidadeKG");    
    	sql.addProjection("meioTransporte.NR_IDENTIFICADOR", "nrIdentificadorMeioTrans");	
    	    	
    	sql.addProjection("modeloMeioTransporte.DS_MODELO_MEIO_TRANSPORTE", "dsModeloMeioTransporte");    	
    	sql.addProjection("municipioMeioTransporte.NM_MUNICIPIO", "nmMunicipioMeioTrans");    	
    	sql.addProjection("tipoMeioTransporte.DS_TIPO_MEIO_TRANSPORTE", "dsTipoMeioTransporte");
    	
    	sql.addProjection("municipioEnderecoPessoaMot.NM_MUNICIPIO", "nmMunicipioMot");
    	sql.addProjection("municipioEnderecoPessoaProp.NM_MUNICIPIO", "nmMunicipioProp");
    	sql.addProjection("municipioMeioTransporte.NM_MUNICIPIO", "nmMunicipioTrans");
    	
    	sql.addProjection("ufMunicipioEnderecoPessoaMot.SG_UNIDADE_FEDERATIVA", "sgUfMot");
    	sql.addProjection("ufMunicipioEnderecoPessoaProp.SG_UNIDADE_FEDERATIVA", "sgUfProp");
    	
    	sql.addProjection("pessoaEmpresaFilialRPP.NM_PESSOA", "nmPessoaFilialRPP");
    	sql.addProjection("pessoaFilialCC.NM_FANTASIA", "nmFantasiaCC");
    	sql.addProjection("pessoaFilialCC.TP_IDENTIFICACAO", "tpIdentificacaoCC");
    	sql.addProjection("pessoaFilialCCD.NM_FANTASIA", "nmFantasiaCCD");
    	sql.addProjection("pessoaFilialRPP.NR_IDENTIFICACAO", "nrIdentPessoaFilialRPP");
    	sql.addProjection("pessoaFilialRPP.NM_FANTASIA", "nmFantasiaRPP");
    	sql.addProjection("pessoaFilialRPP.NM_PESSOA", "nmPessoaFilialRPP");
    	sql.addProjection("pessoaMot.NM_PESSOA", "nmPessoaMot");
    	sql.addProjection("pessoaMot.NR_IDENTIFICACAO", "nrIdentificacaoMot");
    	sql.addProjection("pessoaMot.ID_PESSOA", "idPessoaMot");
    	sql.addProjection("pessoaProp.NM_PESSOA", "nmPessoaProp");
    	sql.addProjection("pessoaProp.NR_IDENTIFICACAO", "nrIdentificacaoProp");
    	sql.addProjection("pessoaProp.TP_IDENTIFICACAO", "tpIdentificacaoProp");
    	sql.addProjection("pessoaProp.ID_PESSOA", "idPessoaProp");
    	sql.addProjection("proprietario.NR_PIS", "nrPis");
    	
    	sql.addProjection("reciboPostoPassagem.NR_RECIBO_POSTO_PASSAGEM", "nrReciboPostoPassagem");
    	sql.addProjection("reciboPostoPassagem.VL_BRUTO", "vlBruto");
    	sql.addProjection("rota.DS_ROTA", "dsRota");
    	
    	sql.addProjection("moeda.DS_SIMBOLO", "dsSimbolo");
    	sql.addProjection("moeda.SG_MOEDA", "sgMoeda");
    	
    	//Inicio From...
    	sql.addFrom("RECIBO_POSTO_PASSAGEM", "reciboPostoPassagem");
    	
    	sql.addFrom("CONTROLE_CARGA", "controleCarga");
    	sql.addFrom("EMPRESA", "empresaFilialRPP");
    	sql.addFrom("ENDERECO_PESSOA", "enderecoPessoaMot");
    	sql.addFrom("ENDERECO_PESSOA", "enderecoPessoaProp");

    	sql.addFrom("FILIAL", "filialCC");
    	sql.addFrom("FILIAL", "filialCCD");
    	sql.addFrom("FILIAL", "filialRPP");

    	sql.addFrom("MARCA_MEIO_TRANSPORTE", "marcaMeioTransporte");
    	sql.addFrom("MEIO_TRANSPORTE", "meioTransporte");
    	sql.addFrom("MEIO_TRANSPORTE_RODOVIARIO", "meioTransporteRodoviario");
    	sql.addFrom("MODELO_MEIO_TRANSPORTE", "modeloMeioTransporte");
    	sql.addFrom("MOEDA", "moeda");
    	sql.addFrom("MOTORISTA", "motorista");
    	sql.addFrom("MUNICIPIO", "municipioEnderecoPessoaMot");
    	sql.addFrom("MUNICIPIO", "municipioEnderecoPessoaProp");
    	sql.addFrom("MUNICIPIO", "municipioMeioTransporte");

    	sql.addFrom("UNIDADE_FEDERATIVA", "ufMunicipioEnderecoPessoaMot");
    	sql.addFrom("UNIDADE_FEDERATIVA", "ufMunicipioEnderecoPessoaProp");
    	
    	sql.addFrom("PESSOA", "pessoaEmpresaFilialRPP");
    	sql.addFrom("PESSOA", "pessoaFilialRPP");
    	sql.addFrom("PESSOA", "pessoaFilialCC");
    	sql.addFrom("PESSOA", "pessoaFilialCCD");
    	sql.addFrom("PESSOA", "pessoaMot");
    	sql.addFrom("PESSOA", "pessoaProp");
    	sql.addFrom("PROPRIETARIO", "proprietario");
    	
    	sql.addFrom("ROTA", "rota");

    	sql.addFrom("TIPO_MEIO_TRANSPORTE", "tipoMeioTransporte");

    	//Joins...
    	sql.addJoin("reciboPostoPassagem.ID_FILIAL", "filialRPP.ID_FILIAL");
    	sql.addJoin("filialRPP.ID_EMPRESA", "empresaFilialRPP.ID_EMPRESA");
    	sql.addJoin("empresaFilialRPP.ID_EMPRESA", "pessoaEmpresaFilialRPP.ID_PESSOA");
    	sql.addJoin("filialRPP.ID_FILIAL", "pessoaFilialRPP.ID_PESSOA");
    	sql.addJoin("reciboPostoPassagem.ID_MOEDA", "moeda.ID_MOEDA");
    	sql.addJoin("reciboPostoPassagem.ID_CONTROLE_CARGA", "controleCarga.ID_CONTROLE_CARGA");
    	sql.addJoin("controleCarga.ID_FILIAL_ORIGEM", "filialCC.ID_FILIAL");
    	sql.addJoin("controleCarga.ID_FILIAL_DESTINO", "filialCCD.ID_FILIAL(+)");
    	sql.addJoin("filialCC.ID_FILIAL", "pessoaFilialCC.ID_PESSOA");
    	sql.addJoin("filialCCD.ID_FILIAL", "pessoaFilialCCD.ID_PESSOA(+)");
    	sql.addJoin("controleCarga.ID_ROTA", "rota.ID_ROTA(+)");
    	sql.addJoin("reciboPostoPassagem.ID_PROPRIETARIO", "proprietario.ID_PROPRIETARIO(+)");
    	sql.addJoin("proprietario.ID_PROPRIETARIO", "pessoaProp.ID_PESSOA(+)");
    	sql.addJoin("pessoaProp.ID_ENDERECO_PESSOA", "enderecoPessoaProp.ID_ENDERECO_PESSOA(+)");
    	sql.addJoin("enderecoPessoaProp.ID_MUNICIPIO", "municipioEnderecoPessoaProp.ID_MUNICIPIO(+)");
    	sql.addJoin("municipioEnderecoPessoaProp.ID_UNIDADE_FEDERATIVA", "ufMunicipioEnderecoPessoaProp.ID_UNIDADE_FEDERATIVA(+)");
    	sql.addJoin("controleCarga.ID_MOTORISTA", "motorista.ID_MOTORISTA(+)");
    	sql.addJoin("motorista.ID_MOTORISTA", "pessoaMot.ID_PESSOA(+)");
    	sql.addJoin("pessoaMot.ID_ENDERECO_PESSOA", "enderecoPessoaMot.ID_ENDERECO_PESSOA(+)");
    	sql.addJoin("enderecoPessoaMot.ID_MUNICIPIO", "municipioEnderecoPessoaMot.ID_MUNICIPIO(+)");
    	sql.addJoin("municipioEnderecoPessoaMot.ID_UNIDADE_FEDERATIVA", "ufMunicipioEnderecoPessoaMot.ID_UNIDADE_FEDERATIVA(+)");
    	sql.addJoin("reciboPostoPassagem.ID_MEIO_TRANSPORTE", "meioTransporte.ID_MEIO_TRANSPORTE");
    	sql.addJoin("meioTransporte.ID_MODELO_MEIO_TRANSPORTE", "modeloMeioTransporte.ID_MODELO_MEIO_TRANSPORTE");
    	sql.addJoin("modeloMeioTransporte.ID_MARCA_MEIO_TRANSPORTE", "marcaMeioTransporte.ID_MARCA_MEIO_TRANSPORTE");
    	sql.addJoin("meioTransporte.ID_MEIO_TRANSPORTE", "meioTransporteRodoviario.ID_MEIO_TRANSPORTE(+)");
    	sql.addJoin("meioTransporteRodoviario.ID_MUNICIPIO", "municipioMeioTransporte.ID_MUNICIPIO(+)");
    	sql.addJoin("modeloMeioTransporte.ID_TIPO_MEIO_TRANSPORTE", "tipoMeioTransporte.ID_TIPO_MEIO_TRANSPORTE");
    	
    	//Filtro...
    	sql.addCriteria("reciboPostoPassagem.ID_RECIBO_POSTO_PASSAGEM" , "=", idReciboPostoPassgem);
 
        return sql;         
    }
     
    public void configReportDomains(ReportDomainConfig config) {
	}
    
    /**
     * Captura o telefone de uma determinada pessoa e o devolve formatado.
     * 
     * @param idPessoa
     * @param tpTelefone
     * @param tpUso
     * @return
     */
    public String getTelefoneFormatado(Long idPessoa, String tpTelefone, String tpUso) {
    	TypedFlatMap telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoByIdPessoaTpTelefone(idPessoa, "C", tpUso);
    	if (telefoneEndereco == null) {
    		telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoByIdPessoaTpTelefone(idPessoa, "R", tpUso);
    	}
    	return FormatUtils.formatTelefone(telefoneEndereco.getString("nrTelefone"), telefoneEndereco.getString("nrDdd"), null);
    }
    
    /**
     * Formata o endereco.
     * 
     * @param dsEndereco
     * @param nrEndereco
     * @param dsComplemento
     * @param nmMunicipio
     * @param sgUf
     * @return
     */
    public String formatEndereco(String dsEndereco, Long nrEndereco, String dsComplemento, String nmMunicipio, String sgUf) {
    	StringBuffer enderecoFormatado = new StringBuffer();
    	if (dsEndereco!=null) {
	    	enderecoFormatado.append(FormatUtils.formatEnderecoPessoa("", dsEndereco, String.valueOf(nrEndereco), dsComplemento));
	    	if (nmMunicipio != null) {
	    		enderecoFormatado.append(" - ").append(nmMunicipio);
	    	}
	    	if (sgUf != null) {
	    		enderecoFormatado.append(" - ").append(sgUf);
	    	}
    	}
    	return enderecoFormatado.toString();
    }
    
    /**
     * Retorna uma label para o campo de nrRegistro do proprietario.
     * 
     * @param tpPessoa
     * @return
     */
    public String getLabelFromTpIdentificacao(String tpPessoa) {
    	return configuracoesFacade.getMensagem(tpPessoa.toLowerCase());
    }
}