package com.mercurio.lms.sgr.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de SMP. 
 * Especificação técnica 11.03.01.08
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.sgr.emitirSMPService"
 * @spring.property name="reportName" value="com/mercurio/lms/sgr/report/emitirSMPCEMOPMZ.jasper"
 */
public class EmitirSMPService extends ReportServiceSupport {
	
	private Long idPessoa;
	private ConversaoMoedaService conversaoMoedaService;
	private EnderecoPessoaService enderecoPessoaService;
	private PessoaService pessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ParametroGeralService parametroGeralService;
	
	
	private ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	private PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	private TelefoneEnderecoService getTelefoneEnderecoService() {
		return telefoneEnderecoService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	private EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	private ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	private Long getIdPessoa() {
		return idPessoa;
	}
	private void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	/**
	 * Método responsável pela geração do relatório via jsp
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
        TypedFlatMap tfm = (TypedFlatMap)parameters;
        SqlTemplate sql = createQuery(tfm);

        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        // Seta os parametros que irão no cabeçalho da página,
        // os parametros de pesquisa
        Map parametersReport = addParametersReport();

        Boolean isReenvio = (Boolean) parameters.get("isReenvio");
       	parametersReport.put("reenvio", isReenvio);

        jr.setParameters(parametersReport);
        return jr;
    }

	/**
	 * Adiciona os parametros a serem enviados ao relatorio.
	 * @return
	 */
	private Map addParametersReport() {
		Map parametersReport = new HashMap();

        String destinatario = null;
        String fone = "";
        String fax = "";
        
		if (getIdPessoa() != null) {
			Pessoa p = this.getPessoaService().findById(getIdPessoa());
	        destinatario = p.getNmPessoa();

	        String tpTelefone = "C";
	        TypedFlatMap foneMap = getTelefoneEnderecoService().findTelefoneEnderecoByIdPessoaTpTelefone(getIdPessoa(),tpTelefone, "FO");
	        if (foneMap != null) {
	        	fone = "("+ foneMap.getString("nrDdd") +")" + " " + foneMap.getString("nrTelefone");
	        }

	        TypedFlatMap faxMap = getTelefoneEnderecoService().findTelefoneEnderecoByIdPessoaTpTelefone(getIdPessoa(),tpTelefone, "FA");
	        if(faxMap != null) {
	        	fax = "("+ faxMap.getString("nrDdd") +")" + " " + faxMap.getString("nrTelefone");
	        }
	        
		} else {
			destinatario = (String) this.getParametroGeralService().findConteudoByNomeParametro("SETOR_SMP", false);
			fone = (String) this.getParametroGeralService().findConteudoByNomeParametro("FONE_SETOR_SMP", false);
			fax = (String) this.getParametroGeralService().findConteudoByNomeParametro("FAX_SETOR_SMP", false);
		}
			
        parametersReport.put("destinatario", destinatario);
        parametersReport.put("fone", fone);
        parametersReport.put("fax", fax);
        parametersReport.put("conversaoMoedaService", getConversaoMoedaService());
        parametersReport.put("idMoedaDestino", SessionUtils.getMoedaSessao().getIdMoeda());
        parametersReport.put("idPaisDestino", SessionUtils.getPaisSessao().getIdPais());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("enderecoPessoaService", getEnderecoPessoaService());
        parametersReport.put("dataCotacao",JTDateTimeUtils.getDataAtual());
        parametersReport.put("siglaAndSgMoeda", SessionUtils.getMoedaSessao().getSiglaSimbolo());
		return parametersReport;
	}


    /**
     * Cria a consulta principal 
     * @param tfm
     * @return
     */
	private SqlTemplate createQuery(TypedFlatMap tfm) {
        Long idSMP = tfm.getLong("idSolicMonitPreventivo");
        if (idSMP == null) {
        	throw new BusinessException("LMS-11319");
        }

        SqlTemplate sql = createSqlTemplate();
        sql.addProjection("cc.ID_CONTROLE_CARGA");
        sql.addProjection("smp.ID_SOLIC_MONIT_PREVENTIVO");
        sql.addProjection("ct.ID_CONTROLE_TRECHO");
        sql.addProjection("f_smp.SG_FILIAL", "FILIAL_SMP");
        sql.addProjection("smp.NR_SMP", "NR_SMP");
        sql.addProjection("p_prop.NM_PESSOA", "NM_PROPRIETARIO");
        sql.addProjection("marca_mt.DS_MARCA_MEIO_TRANSPORTE", "DS_MARCA_MEIO_TRANSPORTE");
        sql.addProjection("modelo_mt.DS_MODELO_MEIO_TRANSPORTE", "DS_MODELO_MEIO_TRANSPORTE");
        sql.addProjection("tipo_mt.DS_TIPO_MEIO_TRANSPORTE", "DS_TIPO_MEIO_TRANSPORTE");
        sql.addProjection("mt.NR_IDENTIFICADOR", "PLACA");
        sql.addProjection("mt.NR_FROTA", "FROTA");
        sql.addProjection("reboque.NR_IDENTIFICADOR", "PLACA_CARRETA");
        sql.addProjection("reboque.NR_FROTA", "FROTA_CARRETA");
        sql.addProjection("m.NM_MUNICIPIO", "NM_MUNICIPIO");
        sql.addProjection("p_motorista.NM_PESSOA", "MOTORISTA");
        sql.addProjection("p_motorista.nr_identificacao", "NR_IDENTIFICACAO");
        sql.addProjection("p_motorista.tp_identificacao", "TP_IDENTIFICACAO");
        sql.addProjection("p_motorista.tp_identificacao", "TP_IDENTIFICACAO");
        
        sql.addProjection("(select max(te.nr_ddd || ' ' || te.nr_telefone) from motorista m " +
        		"inner join pessoa p on m.id_motorista = p.id_pessoa " +
        		"inner join telefone_endereco te on te.id_pessoa = p.id_pessoa where te.tp_telefone='C' and p.id_pessoa = p_motorista.id_pessoa)", "DS_TELEFONE");

        // -- controle carga
        sql.addProjection("moeda_cc.ID_MOEDA", "ID_MOEDA_CC");
        sql.addProjection("f_cc.SG_FILIAL", "FILIAL_CC");
        sql.addProjection("cc.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
        // -- manifesto
        sql.addProjection("f_m_o.ID_FILIAL", "ID_FILIAL_MANIFESTO_ORIGEM");
        sql.addProjection("f_m_o.SG_FILIAL", "FILIAL_MANIFESTO_ORIGEM");
        sql.addProjection("f_m_d.SG_FILIAL", "FILIAL_MANIFESTO_DESTINO");
        sql.addProjection("manifesto.VL_TOTAL_MANIFESTO", "VL_TOTAL_MANIFESTO");
        
        sql.addProjection("moeda_m.ID_MOEDA", "ID_MOEDA_M");
        sql.addProjection("moeda_m.SG_MOEDA", "SG_MOEDA_M");
        sql.addProjection("moeda_m.DS_SIMBOLO", "DS_SIMBOLO_M");
        //ct
        sql.addProjection("ct.DH_PREVISAO_SAIDA","DH_PREVISAO_SAIDA");
        sql.addProjection("ct.DH_PREVISAO_CHEGADA","DH_PREVISAO_CHEGADA");
        sql.addProjection("f_ct_o.SG_FILIAL","F_CT_ORIGEM");
        sql.addProjection("f_ct_d.SG_FILIAL","F_CT_DESTINO");
        sql.addProjection(
        		"case when rota.DS_ROTA is not null then rota.DS_ROTA " +
        		"else rotaCc.DS_ROTA " +
        		"end as DS_ROTA");
        
        sql.addProjection("mtr.NR_RASTREADOR","NR_MCT");
		sql.addProjection("manifesto.DH_EMISSAO_MANIFESTO", "DH_EMISSAO_MANIFESTO");
        sql.addProjection("(SELECT "+ PropertyVarcharI18nProjection.createProjection(" np.ds_natureza_produto_i ") + " FROM natureza_produto np WHERE " +
    		    "(SELECT COUNT(co.ID_NATUREZA_PRODUTO) FROM  manifesto_nacional_cto mn_cto, conhecimento co " +
    		    "WHERE mvn.ID_MANIFESTO_VIAGEM_NACIONAL = mn_cto.ID_MANIFESTO_VIAGEM_NACIONAL " +
    		    "AND mn_cto.ID_CONHECIMENTO = co.ID_CONHECIMENTO) = 1 " +
    		    "AND EXISTS(SELECT 1 FROM  manifesto_nacional_cto mn_cto, conhecimento co " +
    		    "WHERE mvn.ID_MANIFESTO_VIAGEM_NACIONAL = mn_cto.ID_MANIFESTO_VIAGEM_NACIONAL " +
    		    "AND mn_cto.ID_CONHECIMENTO = co.ID_CONHECIMENTO AND co.ID_NATUREZA_PRODUTO = np.ID_NATUREZA_PRODUTO)) ","NATUREZA_PRODUTO");                                   		  
	  
        sql.addProjection(
                "case" +
                " when manifesto.DH_EMISSAO_MANIFESTO is null then manifesto.nr_pre_manifesto" +
                " else " +
                " case " +
                "  when manifesto.TP_MANIFESTO = 'E' THEN me.nr_manifesto_entrega" +
                "  when manifesto.TP_MANIFESTO = 'V'" +
                "  THEN" +
                "  case" +
                "   when manifesto.tp_abrangencia = 'N' then mvn.nr_manifesto_origem" +
                "   when manifesto.tp_abrangencia = 'I' then mi.nr_manifesto_int" +
                "  end " +
                " end " +
                "end as NR_MANIFESTO");
        sql.addFrom("manifesto_entrega"," me");
        sql.addFrom("manifesto_internacional","mi");
        sql.addFrom("manifesto_viagem_nacional","mvn");
        sql.addFrom("manifesto");
        sql.addFrom("filial", "f_ct_o");
        sql.addFrom("filial", "f_ct_d");
        sql.addFrom("solic_monit_preventivo", "smp");
        sql.addFrom("filial", "f_smp");
        sql.addFrom("controle_trecho", "ct");
        sql.addFrom("controle_carga", "cc");
        sql.addFrom("moeda", "moeda_cc");
        sql.addFrom("filial", "f_cc");
        sql.addFrom("proprietario", "prop");
        sql.addFrom("pessoa", "p_prop");
        sql.addFrom("endereco_pessoa", "ep");
        sql.addFrom("municipio", "m");
        sql.addFrom("meio_transporte", "mt");
        sql.addFrom("meio_transporte_rodoviario", "mtr");
        sql.addFrom("modelo_meio_transporte", "modelo_mt");
        sql.addFrom("marca_meio_transporte", "marca_mt");
        sql.addFrom("tipo_meio_transporte", "tipo_mt");
        sql.addFrom("meio_transporte", "reboque");
        sql.addFrom("filial", "f_m_o");
        sql.addFrom("filial", "f_m_d");
        sql.addFrom("motorista");
        sql.addFrom("pessoa", " p_motorista");
        sql.addFrom("trecho_rota_ida_volta", "triv");
        sql.addFrom("rota_ida_volta", "riv");
        sql.addFrom("rota", "rota");
        sql.addFrom("rota", "rotaCc");
        sql.addFrom("smp_manifesto");
        sql.addFrom("moeda", "moeda_m");

        sql.addJoin("smp.ID_FILIAL", "f_smp.ID_FILIAL");
        sql.addJoin("smp.ID_CONTROLE_TRECHO", "ct.ID_CONTROLE_TRECHO(+)");
        sql.addJoin("smp.ID_MEIO_TRANSPORTE", "mt.ID_MEIO_TRANSPORTE");
        sql.addJoin("mtr.ID_MEIO_TRANSPORTE(+)", "mt.ID_MEIO_TRANSPORTE");
        
        sql.addJoin("smp.ID_MOTORISTA", "motorista.ID_MOTORISTA");
        sql.addJoin("motorista.ID_MOTORISTA", "p_motorista.ID_PESSOA");
        sql.addJoin("smp.ID_MEIO_SEMI_REBOQUE", "reboque.ID_MEIO_TRANSPORTE(+)");
        
        sql.addJoin("ct.ID_FILIAL_ORIGEM", "f_ct_o.ID_FILIAL");
        sql.addJoin("ct.ID_FILIAL_DESTINO", "f_ct_d.ID_FILIAL");
        sql.addJoin("smp.ID_CONTROLE_CARGA", "cc.ID_CONTROLE_CARGA");
        
        sql.addJoin("cc.ID_FILIAL_ORIGEM", "f_cc.ID_FILIAL");
        sql.addJoin("cc.ID_ROTA", "rotaCc.ID_ROTA(+)");
        sql.addJoin("cc.ID_MOEDA", "moeda_cc.ID_MOEDA(+)");
        sql.addJoin("cc.ID_PROPRIETARIO", "prop.ID_PROPRIETARIO(+)");
        sql.addJoin("prop.ID_PROPRIETARIO", "p_prop.ID_PESSOA(+)");
        sql.addJoin("p_prop.ID_ENDERECO_PESSOA", "ep.ID_ENDERECO_PESSOA(+)");
        sql.addJoin("ep.ID_MUNICIPIO", "m.ID_MUNICIPIO(+)");
        
        sql.addJoin("ct.ID_TRECHO_ROTA_IDA_VOLTA","triv.ID_TRECHO_ROTA_IDA_VOLTA(+)");
        sql.addJoin("triv.ID_ROTA_IDA_VOLTA","riv.ID_ROTA_IDA_VOLTA(+)");
        sql.addJoin("riv.ID_ROTA","rota.ID_ROTA(+)");

        sql.addJoin("smp.ID_SOLIC_MONIT_PREVENTIVO", "smp_manifesto.ID_SOLIC_MONIT_PREVENTIVO(+)");
		sql.addJoin("smp_manifesto.ID_MANIFESTO", "manifesto.ID_MANIFESTO(+)");
		
		sql.addJoin("manifesto.ID_MOEDA", "moeda_m.ID_MOEDA(+)");
		 
        sql.addJoin("manifesto.ID_FILIAL_ORIGEM", "f_m_o.ID_FILIAL(+)");
        sql.addJoin("manifesto.ID_FILIAL_DESTINO", "f_m_d.ID_FILIAL(+)");

        //busca o nr_manifesto
        sql.addJoin("manifesto.ID_MANIFESTO ","me.id_manifesto_entrega (+)");
        sql.addJoin("manifesto.ID_MANIFESTO","mvn.ID_MANIFESTO_VIAGEM_NACIONAL(+)");
        sql.addJoin("manifesto.ID_MANIFESTO","mi.ID_MANIFESTO_INTERNACIONAL (+)");

        sql.addJoin("mt.ID_MODELO_MEIO_TRANSPORTE", "modelo_mt.ID_MODELO_MEIO_TRANSPORTE");
        sql.addJoin("modelo_mt.ID_MARCA_MEIO_TRANSPORTE", "marca_mt.ID_MARCA_MEIO_TRANSPORTE");
        sql.addJoin("modelo_mt.ID_TIPO_MEIO_TRANSPORTE", "tipo_mt.ID_TIPO_MEIO_TRANSPORTE");
        sql.addCustomCriteria("((manifesto.TP_MANIFESTO not in ('FE','CA','DC','PM')) OR (manifesto.TP_MANIFESTO IS NULL))");

        if(idSMP!=null) {
            sql.addCriteria("smp.ID_SOLIC_MONIT_PREVENTIVO","=",idSMP);
        }

       	setIdPessoa(tfm.getLong("idPessoa"));
        return sql;
    }
    
    /**
     * Sub relatorio de paradas previstas
     * @return
     */
    public JRDataSource executeParadasPrevistas(Long idControleTrecho) {
    	YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
    	SqlTemplate sql = createSqlTemplate();

    	sql.addProjection("pp.nm_ponto_parada","NM_PONTO_PARADA");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("mp.ds_motivo_parada_i"),"DS_MOTIVO_PARADA");
    	sql.addProjection("ppt.nr_tempo_parada","NR_TEMPO_PARADA");

    	sql.addFrom("controle_trecho","ct");
    	sql.addFrom("trecho_rota_ida_volta","triv");
    	sql.addFrom("ponto_parada_trecho","ppt");
    	sql.addFrom("ponto_parada","pp");
    	sql.addFrom("motivo_parada_ponto_trecho","mppt");
    	sql.addFrom("motivo_parada","mp");

    	sql.addJoin("ct.ID_TRECHO_ROTA_IDA_VOLTA","triv.ID_TRECHO_ROTA_IDA_VOLTA");
    	sql.addJoin("ppt.ID_TRECHO_ROTA_IDA_VOLTA","triv.ID_TRECHO_ROTA_IDA_VOLTA");
    	sql.addJoin("ppt.ID_PONTO_PARADA","pp.ID_PONTO_PARADA");
    	sql.addJoin("ppt.ID_PONTO_PARADA_TRECHO","mppt.ID_PONTO_PARADA_TRECHO(+)");
    	sql.addJoin("mppt.ID_MOTIVO_PARADA","mp.ID_MOTIVO_PARADA(+)");

    	sql.addCriteria("ppt.DT_VIGENCIA_INICIAL", "<=", dtAtual);
    	sql.addCriteria("ppt.DT_VIGENCIA_FINAL", ">=", dtAtual);
    	sql.addCriteria("mppt.DT_VIGENCIA_INICIAL", "<=", dtAtual);
    	sql.addCriteria("mppt.DT_VIGENCIA_FINAL", ">=", dtAtual);
    	sql.addCriteria("ct.ID_CONTROLE_TRECHO", "=", idControleTrecho);

    	return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Sub relatorio de providencias 
     * @return
     */
    public JRDataSource executeProvidencias(Long idSolicMonitPreventivo) {
    	SqlTemplate sql = createSqlTemplate();

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("egr.DS_RESUMIDA_I"), "DS_RESUMIDA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("egr.DS_COMPLETA_I"), "DS_COMPLETA");

		sql.addFrom("solic_monit_preventivo","smp");
		sql.addFrom("exigencia_smp","esmp");
		sql.addFrom("exigencia_ger_risco","egr");

		sql.addJoin("smp.ID_SOLIC_MONIT_PREVENTIVO","esmp.ID_SOLIC_MONIT_PREVENTIVO");
		sql.addJoin("esmp.ID_EXIGENCIA_GER_RISCO","egr.ID_EXIGENCIA_GER_RISCO");
		sql.addCriteria("smp.ID_SOLIC_MONIT_PREVENTIVO","=", idSolicMonitPreventivo);

		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
}