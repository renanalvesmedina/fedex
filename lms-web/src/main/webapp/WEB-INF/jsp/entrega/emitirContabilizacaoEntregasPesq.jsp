<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.entrega.emitirContabilizacaoEntregasAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
		document.getElementById("filial.sgFilial").serializable = "true";
	}	
	  
	//Implementação da filial do usuário logado como padrão
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	var dhIni = null;
	var dhFim = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		dhIni = getNestedBeanPropertyValue(data,"dhIni");
		dhFim = getNestedBeanPropertyValue(data,"dhFim");
		writeDataSession();
	}
	
	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("filial.idFilial",idFilial);
			setElementValue("filial.sgFilial",sgFilial);
			setElementValue("filial.pessoa.nmFantasia",nmFilial);
		}
		if (dhIni != null &&
			dhFim != null) {
			setElementValue("dhEmissaoInicial",setFormat(document.getElementById("dhEmissaoInicial"),dhIni));
			setElementValue("dhEmissaoFinal",setFormat(document.getElementById("dhEmissaoFinal"),dhFim))
		}
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click")
			writeDataSession();
	}

//-->
</script>
<adsm:window service="lms.entrega.emitirContabilizacaoEntregasAction" onPageLoadCallBack="pageLoad"> 
	<adsm:form action="/entrega/emitirContabilizacaoEntregas" >		

		<adsm:lookup label="filial" labelWidth="15%" dataType="text" size="3" maxLength="3" width="77%"
				     service="lms.entrega.emitirContabilizacaoEntregasAction.findLookupFilial" property="filial" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true">
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
					<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="filial.empresa.tpEmpresa"/>
					<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true"/> 
					
		</adsm:lookup>
		
		<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
		

		<adsm:combobox property="tpManifesto.value" label="tipoDeManifesto" domain="DM_TIPO_MANIFESTO_ENTREGA" labelWidth="15%" width="75%">				
			<adsm:propertyMapping relatedProperty="tpManifesto.description" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="tpManifesto.description" serializable="true"/>
		
		<adsm:range label="periodoEmissao" labelWidth="15%" width="75%" required="true" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="dhEmissaoInicial"/> 
			<adsm:textbox dataType="JTDate" property="dhEmissaoFinal"/>
		</adsm:range>
		
		<adsm:combobox labelWidth="15%" label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.entrega.emitirContabilizacaoEntregasAction" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>	
</adsm:window>