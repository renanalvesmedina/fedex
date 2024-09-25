/**
 * 
 */
package com.mercurio.lms.coleta.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do Relatório de Manifestos Montados.
 * Especificação técnica 02.01.02.05
 * 
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.coleta.emitirManifestosMontadosService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirManifestosMontados.jasper"
 */
public class EmitirManifestosMontadosService extends ReportServiceSupport {

    private static final String TP_CONTROLE_CARGA_COLETA_ENTREGA = "C";
    private static final String TP_STATUS_CONTROLE_CARGA_GERADO = "GE";
    private static final String TP_STATUS_COLETA_NO_MANIFESTO = "NM";
    
    private ConversaoMoedaService conversaoMoedaService;
    private EnderecoPessoaService enderecoPessoaService;
    
    /**
     * método responsável por gerar o relatório.
     */
    public JRReportDataObject execute(Map parameters) throws Exception {

        // aqui posso fazer o cast para typedFlatmap e passar para o mount sql
        TypedFlatMap tfm = (TypedFlatMap) parameters;

        SqlTemplate sql = mountSql(tfm);

        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

        // Seta os parametros que irão no cabeçalho da página,
        // os parametros de pesquisa
        Map parametersReport = new HashMap();

        parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        // Monta a moeda destino
        String dsSimboloMoeda = tfm.getString("dsSimboloMoedaHidden");
        
        // Parametros para localização do pais origem e para conversão de moeda
        parametersReport.put("moedaSelecionada", dsSimboloMoeda);        
        
        jr.setParameters(parametersReport);
        return jr;
    }
    
    /**
     * Busca o id do pais da filial selecionada, que será a filial origem
     * para ser usada na conversao de moeda  
     * @param idFilialOrigem
     * @return
     */
    private Long findIdPaisOrigem(Long idFilialOrigem) {
        Long idPais = getEnderecoPessoaService().findEnderecoPessoaPadrao(idFilialOrigem).getMunicipio().getUnidadeFederativa().getPais().getIdPais();
        return idPais;
    }

    /**
     * Monta a consulta a ser realizada pelo relatório.  
     * @param parameters
     * @return
     */
    private SqlTemplate mountSql(TypedFlatMap parameters) {
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("rce.NR_ROTA", "NR_ROTA");
        sql.addProjection("rce.DS_ROTA", "DS_ROTA");
        sql.addProjection("f_cc.SG_FILIAL", "SG_FILIAL_CC");
        sql.addProjection("cc.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
        sql.addProjection("mt.NR_FROTA", "NR_FROTA");
        sql.addProjection("mt.NR_IDENTIFICADOR", "NR_IDENTIFICADOR");
        sql.addProjection("mt.NR_CAPACIDADE_KG", "NR_CAPACIDADE_KG");
        sql.addProjection("f_mc.SG_FILIAL", "SG_FILIAL_MC");
        sql.addProjection("mc.NR_MANIFESTO", "NR_MANIFESTO");
        sql.addProjection("f_pc.SG_FILIAL", "SG_FILIAL_PC");
        sql.addProjection("pc.NR_COLETA", "NR_COLETA");
        sql.addProjection("p_cliente.NM_PESSOA", "CLIENTE");

        // monta o endereço
        sql.addProjection("PC.ED_COLETA", "ENDERECO");
        sql.addProjection("PC.NR_ENDERECO", "NR_ENDERECO");
        sql.addProjection("PC.DS_COMPLEMENTO_ENDERECO", "COMPLEMENTO");

        sql.addProjection("PC.DT_PREVISAO_COLETA", "DT_PREVISAO_COLETA");
        sql.addProjection("PC.HR_LIMITE_COLETA", "HR_LIMITE_COLETA");
        sql.addProjection("PC.OB_PEDIDO_COLETA", "OBSERVACAO");
        sql.addProjection("PC.PS_TOTAL_VERIFICADO", "PESO");
        sql.addProjection("PC.QT_TOTAL_VOLUMES_VERIFICADO", "VOLUMES");
        
    	sql.addProjection("F_CONV_MOEDA("+
    			findIdPaisOrigem(parameters.getLong("filial.idFilial")) + ", " +
    			"moeda.ID_MOEDA" + ", " +
    			SessionUtils.getPaisSessao().getIdPais() + ", " +
    			parameters.getLong("moeda.idMoeda") + ", " +
    			"to_date('" + JTDateTimeUtils.getDataAtual().toString("ddMMyyyy") + "', 'ddMMyyyy'), " +
    			"PC.VL_TOTAL_VERIFICADO" + ")", "VL_CONVERTIDO");
        
        sql.addProjection("m.NM_MUNICIPIO", "MUNICIPIO");
        sql.addProjection("uf.SG_UNIDADE_FEDERATIVA", "SG_UF");

        sql.addFrom("controle_carga", "cc");
        sql.addFrom("filial", "f_cc");
        sql.addFrom("filial", "f_mc");
        sql.addFrom("filial", "f_pc");
        sql.addFrom("pedido_coleta", "pc");
        sql.addFrom("manifesto_coleta", "mc");
        sql.addFrom("rota_coleta_entrega", "rce");
        sql.addFrom("meio_transporte", "mt");
        sql.addFrom("municipio", "m");
        sql.addFrom("unidade_federativa", "uf");
        sql.addFrom("moeda");
        sql.addFrom("cliente", "c");
        sql.addFrom("pessoa", "p_cliente");

        
        sql.addCriteria("cc.tp_controle_carga", "=", TP_CONTROLE_CARGA_COLETA_ENTREGA);
        sql.addCriteria("cc.tp_status_controle_carga", "=", TP_STATUS_CONTROLE_CARGA_GERADO);
        
        sql.addCriteria("cc.ID_FILIAL_ORIGEM", "=", parameters.getLong("filial.idFilial"));
        sql.addFilterSummary("filial", parameters.getString("filial.sgFilial"));

        Long idRotaColetaEntrega = parameters.getLong("rotaColetaEntrega.idRotaColetaEntrega");

        if (idRotaColetaEntrega != null) {
            sql.addCriteria("mc.ID_ROTA_COLETA_ENTREGA", "=", idRotaColetaEntrega);
            sql.addFilterSummary("rota", parameters.getString("rotaColetaEntrega.dsRota"));
        }
        sql.addJoin("mc.ID_MANIFESTO_COLETA","pc.ID_MANIFESTO_COLETA(+)");
        sql.addCriteria("pc.TP_STATUS_COLETA", "=", TP_STATUS_COLETA_NO_MANIFESTO);

        
        sql.addJoin("cc.ID_CONTROLE_CARGA", "mc.ID_CONTROLE_CARGA");
        sql.addCriteria("mc.TP_STATUS_MANIFESTO_COLETA","=", TP_STATUS_CONTROLE_CARGA_GERADO);
        
        sql.addJoin("cc.ID_FILIAL_ORIGEM", "f_cc.ID_FILIAL(+)");
        sql.addJoin("mc.ID_ROTA_COLETA_ENTREGA", "rce.ID_ROTA_COLETA_ENTREGA");
        sql.addJoin("cc.ID_TRANSPORTADO","mt.ID_MEIO_TRANSPORTE(+)");
        sql.addJoin("mc.ID_FILIAL_ORIGEM", "f_mc.ID_FILIAL(+)");
        sql.addJoin("pc.ID_CLIENTE", "c.ID_CLIENTE");
        sql.addJoin("c.ID_CLIENTE", "p_cliente.ID_PESSOA");
        sql.addJoin("PC.ID_MOEDA", "moeda.ID_MOEDA(+)");
        sql.addJoin("pc.ID_MUNICIPIO", "m.ID_MUNICIPIO(+)");
        sql.addJoin("m.ID_UNIDADE_FEDERATIVA", "uf.ID_UNIDADE_FEDERATIVA(+)");

        sql.addJoin("f_pc.ID_FILIAL", "pc.ID_FILIAL_RESPONSAVEL");
        
        sql.addOrderBy("rce.NR_ROTA");
        sql.addOrderBy("f_cc.SG_FILIAL");
        sql.addOrderBy("cc.NR_CONTROLE_CARGA");
        sql.addOrderBy("f_mc.SG_FILIAL");
        sql.addOrderBy("mc.NR_MANIFESTO");
        sql.addOrderBy("f_pc.SG_FILIAL");
        sql.addOrderBy("pc.NR_COLETA");
        
        return sql;
    }

    public ConversaoMoedaService getConversaoMoedaService() {
        return conversaoMoedaService;
    }

    public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
        this.conversaoMoedaService = conversaoMoedaService;
    }

    public EnderecoPessoaService getEnderecoPessoaService() {
        return enderecoPessoaService;
    }

    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }
}
