package com.mercurio.lms.configuracoes.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.service.ControleFormularioService;
import com.mercurio.lms.configuracoes.model.service.ImpressoraFormularioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Mickaël Jalbert
 *
 * @spring.bean id="lms.configuracoes.emitirControleFormulariosService"
 * @spring.property name="reportName" value="com/mercurio/lms/configuracoes/report/emitirControleFormularios.jasper"
 */
public class EmitirControleFormulariosService extends ReportServiceSupport {

	private ControleFormularioService controleFormularioService;
	private ImpressoraFormularioService impressoraFormularioService;	
	private FilialService filialService;

	private List listUsado = new ArrayList();
	private List listEspaco = new ArrayList();	

	public JRReportDataObject execute(Map parameters) throws Exception {		
		SqlTemplate sql = mountSql(parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));

		jr.setParameters(parametersReport);		
		
		return jr; 
	}
	
	/**
	 * Método executado na chamada de cada registro principal.
	 * Retorna todos os intervalos sem impressora ligado ao controle
	 * de formulário.
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public JRDataSource executeSubEstoque(Object[] parameters) throws Exception {
		SqlTemplate sql = null;
		String sqlTotal = new String();
		
		listUsado = new ArrayList();
		listEspaco = new ArrayList();			
		
		montaIntervalos(parameters);

		// Por cada intervalo, criar um SQL		
		for(Iterator iter = listEspaco.iterator(); iter.hasNext();){
			sql = new SqlTemplate();
			Map valorIntervalos = (Map)iter.next();
			sql.addProjection("'"+((Long)valorIntervalos.get("valorInicial")).toString()+"-"+((Long)valorIntervalos.get("valorFinal")).toString()+"'", "NR_FORMULARIO");
			sql.addFrom("DUAL");
			sqlTotal += sql.getSql();
			
			if (iter.hasNext()) {
				sqlTotal += "\nUNION\n";
			}
		}
		if (StringUtils.isNotBlank(sqlTotal)) {
			return executeQuery(sqlTotal, new Object[]{}).getDataSource();
		} else {
			return null;
		}
	}

	/**
	 * Método executado na chamada de cada registro principal.
	 * Retorna todas as impressoras ligada ao controle
	 * de formulário.
	 * @param parameters
	 * @return
	 * @throws Exception
	 */	
	public JRDataSource executeSubUtilizado(Object[] parameters) throws Exception {
		SqlTemplate sql = null;
		String sqlTotal = new String();

		List criteriaTemp = null;		
		List criteriaTotal = new ArrayList();		
		
		// Por cada intervalo, criar um SQL
		for(Iterator iter = listUsado.iterator(); iter.hasNext();){
			sql = new SqlTemplate();
			Map valorIntervalos = (Map)iter.next();
			sql.addProjection("'"+((Long)valorIntervalos.get("valorInicial")).toString()+" - "+((Long)valorIntervalos.get("valorFinal")).toString()+"'", "NR_FORMULARIO");
			sql.addProjection("I.DS_LOCALIZACAO","DS_LOCALIZACAO");
			sql.addProjection("IF.NR_SELO_FISCAL_INICIAL");
			sql.addProjection("IF.NR_SELO_FISCAL_FINAL");
			sql.addProjection("IF.NR_ULTIMO_SELO_FISCAL", "NR_ULTIMO_SELO");
			sql.addProjection("IF.NR_SELO_FISCAL_INICIAL || ' - ' || IF.NR_SELO_FISCAL_FINAL","NR_SELOS_IF");
			sql.addProjection("IF.ID_IMPRESSORA_FORMULARIO","ID_IMPRESSORA_FORMULARIO");			
			sql.addProjection("I.DS_CHECK_IN","DS_CHECK_IN");
			sql.addProjection("IF.NR_FORMULARIO_INICIAL || ' - ' || IF.NR_FORMULARIO_FINAL","NR_FORMULARIO_IF");
			sql.addProjection("IF.NR_FORMULARIO_INICIAL","NR_FORMULARIO_INICIAL");			
			sql.addProjection("IF.NR_ULTIMO_FORMULARIO","NR_ULTIMA_IMPRESSAO");
			sql.addProjection("(IF.NR_FORMULARIO_FINAL - IF.NR_ULTIMO_FORMULARIO)","ESTOQUE_IMPRESSORA");		
			sql.addFrom("IMPRESSORA_FORMULARIO","IF");
			sql.addFrom("IMPRESSORA","I");
			sql.addJoin("I.ID_IMPRESSORA","IF.ID_IMPRESSORA");
			sql.addCriteria("IF.ID_CONTROLE_FORMULARIO","=",((Long)parameters[0]).toString(),Long.class);
			sql.addCriteria("IF.NR_FORMULARIO_INICIAL",">=",((Long)valorIntervalos.get("valorInicial")).toString(),Long.class);
			sql.addCriteria("IF.NR_FORMULARIO_FINAL","<=",((Long)valorIntervalos.get("valorFinal")).toString(),Long.class);			
			
			sqlTotal += sql.getSql();
			
			criteriaTemp = Arrays.asList(sql.getCriteria());
			
			criteriaTotal.addAll(criteriaTemp);	
			if (iter.hasNext()) {
				sqlTotal += "\nUNION\n";
			}
		}
		if (StringUtils.isNotBlank(sqlTotal)) {
			sqlTotal += "\nORDER BY NR_FORMULARIO_INICIAL";
		} else {
			sqlTotal = "SELECT '1' FROM DUAL WHERE 1 = 2";			
		}

		return executeQuery(sqlTotal,criteriaTotal.toArray()).getDataSource();
	}
	
	/**
	 * Método executado na chamada de cada registro principal.
	 * Retorna todos os controle_formulário filho.
	 * @param parameters
	 * @return
	 * @throws Exception
	 */		
	public JRDataSource executeSubDistribuicao(Object[] parameters) throws Exception {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("FI.SG_FILIAL || ' - ' || CF.NR_FORMULARIO_INICIAL || ' - ' || CF.NR_FORMULARIO_FINAL", "NR_FORMULARIO");
		sql.addFrom("CONTROLE_FORMULARIO", "CF");
		sql.addFrom("FILIAL","FI");
		sql.addJoin("FI.ID_FILIAL","CF.ID_FILIAL");
		sql.addCriteria("CF.ID_CONTROLE_ESTOQUE_ORIGINAL","=",((Long)parameters[0]).toString(),Long.class);
		return executeQuery(sql.getSql(),sql.getCriteria()).getDataSource();
	}
	
    public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("TP_FORMULARIO", "DM_TIPO_FORMULARIO");
        config.configDomainField("SITUACAO_FORMULARIO" , "DM_SITUACAO_FORMULARIO");
   }	
	
	/**
	 * Monta o sql principal
	 * @param parameters
	 * @return
	 * @throws Exception
	 */		
	private SqlTemplate mountSql(Map parameters) throws Exception{
		
		SqlTemplate sql = this.createSqlTemplate();			
		
		sql.addProjection("FI.ID_FILIAL");
		sql.addProjection("FI.SG_FILIAL || ' - ' || PEFI.NM_FANTASIA", "NM_FILIAL");
		sql.addProjection("CF.TP_FORMULARIO", "TP_FORMULARIO");		
		sql.addProjection("EM.ID_EMPRESA");		
		sql.addProjection("PEEM.NM_PESSOA");
		sql.addProjection("CF.ID_CONTROLE_FORMULARIO");
		sql.addProjection("CF.NR_FORMULARIO_INICIAL || ' - ' || CF.NR_FORMULARIO_FINAL", "NR_FORMULARIO");
		sql.addProjection("CF.NR_FORMULARIO_INICIAL");
		sql.addProjection("CF.NR_FORMULARIO_FINAL");			
		sql.addProjection("CF.DT_RECEBIMENTO");
		sql.addProjection("CF.TP_SITUACAO_FORMULARIO", "SITUACAO_FORMULARIO");				
		sql.addProjection("CF.NR_AIDF");
		sql.addProjection("CF.CD_SERIE");
		sql.addProjection("(CASE WHEN CF.NR_SELO_FISCAL_INICIAL IS NOT NULL THEN CF.NR_SELO_FISCAL_INICIAL || ' - ' || CF.NR_SELO_FISCAL_FINAL ELSE '' END)", "NR_SELO_FISCAL");
		sql.addProjection("(CASE WHEN FIOR.SG_FILIAL IS NOT NULL AND CFOR.NR_FORMULARIO_INICIAL IS NOT NULL AND CFOR.NR_FORMULARIO_FINAL IS NOT NULL  THEN FIOR.SG_FILIAL || ' - ' || CFOR.NR_FORMULARIO_INICIAL || ' - ' || CFOR.NR_FORMULARIO_FINAL ELSE '' END)", "ESTOQUE_ORIGINAL");
		sql.addFrom("FILIAL","FI");
		sql.addFrom("PESSOA","PEFI");
		sql.addFrom("CONTROLE_FORMULARIO","CF");
		sql.addFrom("EMPRESA","EM");
		sql.addFrom("PESSOA","PEEM");
		sql.addFrom("CONTROLE_FORMULARIO","CFOR");
		sql.addFrom("FILIAL","FIOR");	
		
		sql.addJoin("FI.ID_FILIAL","PEFI.ID_PESSOA");
		sql.addJoin("FI.ID_FILIAL","CF.ID_FILIAL");
		sql.addJoin("CF.ID_EMPRESA","EM.ID_EMPRESA");
		sql.addJoin("EM.ID_EMPRESA","PEEM.ID_PESSOA");
		sql.addJoin("CFOR.ID_CONTROLE_FORMULARIO(+)","CF.ID_CONTROLE_ESTOQUE_ORIGINAL");
		sql.addJoin("FIOR.ID_FILIAL(+)","CFOR.ID_FILIAL");
		
		if (!parameters.get("filial.idFilial").equals("")){
			Filial fil = this.getFilialService().findById(Long.valueOf((String)parameters.get("filial.idFilial")));
			sql.addCriteria("FI.ID_FILIAL","=",parameters.get("filial.idFilial"),Long.class);
			sql.addFilterSummary("filial",fil.getSgFilial() + " - " +  fil.getPessoa().getNmFantasia());
		}
		if (!parameters.get("tpFormulario").equals("")){		
			sql.addCriteria("CF.TP_FORMULARIO","=",parameters.get("tpFormulario"));
			sql.addFilterSummary("tipoDocumento",this.getDomainValueService().findDomainValueDescription("DM_TIPO_FORMULARIO",(String)parameters.get("tpFormulario")));			
		}
		if (!parameters.get("tpSituacaoFormulario").equals("")){		
			sql.addCriteria("CF.TP_SITUACAO_FORMULARIO","=",parameters.get("tpSituacaoFormulario"));		
			sql.addFilterSummary("situacao",this.getDomainValueService().findDomainValueDescription("DM_SITUACAO_FORMULARIO",(String)parameters.get("tpSituacaoFormulario")));			
		}
		
		sql.addOrderBy("FI.SG_FILIAL");
		sql.addOrderBy("CF.TP_FORMULARIO");
		sql.addOrderBy("PEEM.NM_PESSOA");
		sql.addOrderBy("CF.NR_FORMULARIO_INICIAL");
		sql.addOrderBy("CF.NR_FORMULARIO_FINAL");
		return sql;
	}

	/**
	 * Cria os intervalos de impressora e intervalos livre
	 * de controle_formulário.
	 * Executado a cada novo controle_formulário encontrado.
	 * @param parameters
	 * @return
	 * @throws Exception
	 */		
	private void montaIntervalos(Object[] parameters){
		List list = this.getImpressoraFormularioService().findByControleFormulario((Long)parameters[0]);
		
		Long usadoInicialAnt = null;
		Long usadoFinalAnt = null;
		Long usadoInicial = null;
		Long usadoFinal = null;
		Long espacoInicial = null;
		Long espacoFinal = null;
		Long totalInicial = (Long)parameters[1];
		Long totalFinal = (Long)parameters[2];		
		
		
		//Por cada impressora
		for(Iterator iter = list.iterator(); iter.hasNext();){
			ImpressoraFormulario impFor = (ImpressoraFormulario)iter.next();
			
			//Valores do intervalo anterior
			usadoInicialAnt = usadoInicial;
			usadoFinalAnt = usadoFinal;
			
			//Valores da intervalo atual
			usadoInicial = impFor.getNrFormularioInicial();
			usadoFinal = impFor.getNrFormularioFinal();
			
			//Se é a primeira vez executado
			if (usadoInicialAnt == null) {
				//Se começa com espaço
				if (totalInicial.longValue() < usadoInicial.longValue()){
					//Novo espaço temporário
					espacoInicial = totalInicial;
					espacoFinal = Long.valueOf(usadoInicial.longValue()-1);
				}
			//Se tem dois intervalos de impressora um depois 
			//do outro (não existe espaço entre os dois)
			} else if (usadoFinalAnt.longValue()+1 == usadoInicial.longValue()){
				//o começo do intervalo atual vira o começo do anterior
				usadoInicial = usadoInicialAnt;
			//Existe espaço
			} else {
				//Novo espaço temporário				
				espacoInicial = Long.valueOf(usadoFinalAnt.longValue()+1);
				espacoFinal = Long.valueOf(usadoInicial.longValue()-1);
			}
			//Se tem um intervalo de impressora anterior e um intervalo de espaço
			if (usadoInicialAnt != null && espacoInicial != null) {
				//Cria um novo intervalo de impressora
				criaNovoIntervalo(listUsado,usadoInicialAnt,usadoFinalAnt);
				//Cria um novo intervalo de espaço
				criaNovoIntervalo(listEspaco,espacoInicial,espacoFinal);
				
				//Reinicializa os valores
				usadoInicialAnt = null;
				usadoFinalAnt = null;
				espacoInicial = null;
				espacoFinal = null;	
//			Se tem um intervalo de espaço
			} else if (espacoInicial != null){
				//Cria um novo intervalo de espaço				
				criaNovoIntervalo(listEspaco,espacoInicial,espacoFinal);

				//Reinicializa os valores				
				espacoInicial = null;
				espacoFinal = null;						
			}
		}
		//Se existe um intervalo de impressora
		if (usadoInicial != null){
			//Cria um novo intervalo de impressora			
			criaNovoIntervalo(listUsado,usadoInicial,usadoFinal);
			
			//Se o último intervalo de impressora é menor que o intervalo total
			if (usadoFinal.longValue() < totalFinal.longValue()){
				//Cria um novo intervalo de espaço
				criaNovoIntervalo(listEspaco,Long.valueOf(usadoFinal.longValue()+1),totalFinal);
			}
		} else {
			//Cria um novo intervalo de espaço
			criaNovoIntervalo(listEspaco,totalInicial,totalFinal);			
		}		
	}
	
	/**
	 * Adiciona um map de intervalo na list de intervalos informado.
	 * @param list
	 * @param valorInicial
	 * @param valorFinal
	 */
	private void criaNovoIntervalo (List list, Long valorInicial, Long valorFinal) {
		Map  intervalo = new HashMap(2);
		intervalo.put("valorInicial", valorInicial);
		intervalo.put("valorFinal", valorFinal);		
		list.add(intervalo);
	}	

	public ControleFormularioService getControleFormularioService() {
		return controleFormularioService;
	}

	public void setControleFormularioService(
			ControleFormularioService controleFormularioService) {
		this.controleFormularioService = controleFormularioService;
	}

	public ImpressoraFormularioService getImpressoraFormularioService() {
		return impressoraFormularioService;
	}

	public void setImpressoraFormularioService(
			ImpressoraFormularioService impressoraFormularioService) {
		this.impressoraFormularioService = impressoraFormularioService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
