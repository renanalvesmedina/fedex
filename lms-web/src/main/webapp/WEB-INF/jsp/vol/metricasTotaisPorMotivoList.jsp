<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
	//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.vol.metricasTotaisPorMotivoAction.findDataSession","dataSession",data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Carrega os dados de filial do usuario logado
	 */
	var dataUsuario;
	function dataSession_cb(data, error) {
		dataUsuario = data;
		fillDataUsuario();
		onPageLoad_cb(data,error);
	}
	
	/**
	 * Faz o callBack do carregamento da pagina
	 */
	function loadPage_cb(data, error) {
		setDisabled("filial.idFilial", false);
		document.getElementById("filial.sgFilial").disabled=false;
		document.getElementById("filial.sgFilial").focus;
	}
	
	/**
	 * Retorna o parametro 'mode' que contem o modo em que a tela esta sendo utilizada.
	 * Caso mode seja igual a 'lookup' significa que a tela esta sendo aberta por uma lookup.
	 */
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}

	function initWindow(eventObj) {
		
		if (eventObj.name == "cleanButton_click") {
			fillDataUsuario();
			
			filial_sgFilialOnChangeHandler();	
			
			setFocus(document.getElementById("filial.sgFilial"));
		}
		 
	}

	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
			setElementValue("dataInicial", setFormat(document.getElementById("dataInicial"), dataUsuario.dataPrimeiroDiaMes));
			setElementValue("dataFinal", setFormat(document.getElementById("dataFinal"), dataUsuario.dataAtual));
		}
	}
	function onRowClick(idOcorrenciaEntrega, data) {
		setElementValue("idOcorrenciaEntrega",idOcorrenciaEntrega);
		reportButtonScript('lms.vol.emitirDetalhamentoOcorrenciasService', 'openPdf', document.forms[0]);
		return false;
	}    	
</script>
<adsm:window service="lms.vol.metricasTotaisPorMotivoAction" onPageLoadCallBack="pageLoad">
	<adsm:form  action="/vol/metricasTotaisPorMotivo">
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/> 
		<adsm:hidden property="idOcorrenciaEntrega" value="0" serializable="true"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:lookup label="filial" width="8%" labelWidth="10%" 
				     property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.metricasTotaisPorMotivoAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             required="true">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true" width="50%"/>
        </adsm:lookup>

		<adsm:range label="periodo" width="70%" labelWidth="10%" >
			<adsm:textbox dataType="JTDate" property="dataInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dataFinal" required="true"/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="totais"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
		
	</adsm:form>

	<adsm:grid property="totais" idProperty="idOcorrenciaEntrega" selectionMode="none"  disableMarkAll="true"
			   rows="14" gridHeight="150" unique="true" onRowClick="onRowClick">
		<adsm:gridColumn property="cdOcorrenciaEntrega" title="codigo" width="15%" dataType="integer"/>
		<adsm:gridColumn property="dsOcorrenciaEntrega" title="ocorrencia" width="70%" />
		<adsm:gridColumn property="qtde" title="qtde" width="15%" dataType="integer" />		
		
		<adsm:buttonBar> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
