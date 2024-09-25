<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	function loadObjects() {
		setDisabled("visualizar", false);
	}

</script>

<adsm:window service="lms.carregamento.emitirControleRotasAction" onPageLoad="loadObjects">
	<adsm:form action="/carregamento/emitirControleRotas" id="reportForm">
	
		<adsm:range label="periodo" width="85%" required="true" maxInterval="15">
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true" />
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true"/>
		</adsm:range>
        
        <adsm:lookup label="rotaViagem" property="rotaIdaVolta" 
			 		 idProperty="idRotaIdaVolta" 
			 		 criteriaProperty="nrRota" criteriaSerializable="true"
					 action="/municipios/consultarRotas" cmd="idaVolta" 
					 service="lms.carregamento.emitirControleRotasAction.findLookupRotaIdaVolta" 
					 dataType="integer" size="4" maxLength="4" width="85%" mask="0000">
			<adsm:propertyMapping modelProperty="rota.dsRota" relatedProperty="dsRota" />
			<adsm:textbox property="dsRota" dataType="text" size="50" maxLength="150" disabled="true" serializable="true" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="filialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" criteriaSerializable="true"
		             action="/municipios/manterFiliais" service="lms.carregamento.emitirControleRotasAction.findLookupFilial" 
		             label="filialOrigem" width="85%" size="3" maxLength="3">
		             
        	<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado" />
        	
            <adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" 
            			  serializable="false" size="50" maxLength="150" disabled="true"/>
        </adsm:lookup>
		
		<adsm:lookup dataType="text" property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" criteriaSerializable="true"
		             action="/municipios/manterFiliais" service="lms.carregamento.emitirControleRotasAction.findLookupFilial" 
		             label="filialDestino" width="85%" size="3" maxLength="3">
		             
        	<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado" />
        	
            <adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" 
            			  serializable="false" size="50" maxLength="50" disabled="true"/>
        </adsm:lookup>
		
		
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true"/>
	  	<adsm:checkbox property="somenteComAtraso" label="somenteAtraso" width="85%"/>
 		<adsm:combobox labelWidth="15%" label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" renderOptions="true" defaultValue="pdf" required="true" />		
		
		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="visualizar" onclick="callReportViewer();"/>
			<adsm:button caption="limpar" disabled="false" buttonType="resetButton" onclick="resetButtonFunction()"/>
		</adsm:buttonBar>
		<script>
			function lms_05062() {
				alert("<adsm:label key='LMS-05062'/>");
			}
		</script>
	</adsm:form>
</adsm:window>

<script>

	/**
	 * Chamada do relatorio para a 
	 */
	function callReportViewer(reportStyle) {
	
		if ((validateTabScript(document.getElementById("reportForm")))==false) return false;
		
	    var dataObject = buildFormBeanFromForm(document.getElementById("reportForm"));
		var sdo = createServiceDataObject('lms.carregamento.emitirControleRotasAction.execute', 'openPdf', dataObject); 
		
		executeReportWindowed(sdo, getElementValue('tpFormatoRelatorio'));
		
		return false;
	}

	/**
	 *
	 */
	function resetButtonFunction() {
		resetValue(this.document);
		setFocusOnFirstFocusableField();
	}
</script>