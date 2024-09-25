<%@ include file="/lib/imports.jsp"%>
<script type="text/javascript">
<!--


	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.vendas.imprimirListaMestraAction.findDateNow","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}
	var dtNow = null;
	
	function dataSession_cb(data) {
		dtNow = setFormat(document.getElementById("dtReferencia"),getNestedBeanPropertyValue(data,"dtNow"));
		writeDataSession();
	}
	
	function writeDataSession() {
		if (dtNow != null)
			setElementValue("dtReferencia",dtNow);
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click" || eventObj.name == "tab_click")
			writeDataSession();
	}
//-->
</script>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.imprimirListaMestraAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/vendas/imprimirListaMestra">

		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
			service="lms.vendas.imprimirListaMestraAction.findLookupFilial" dataType="text" label="filial" size="3" action="/municipios/manterFiliais"
			labelWidth="15%" width="35%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px">
	 		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="sgFilial" modelProperty="sgFilial" />
			<adsm:propertyMapping relatedProperty="nmFantasia" modelProperty="pessoa.nmFantasia" />			
			<adsm:propertyMapping relatedProperty="siglaNomeFilial" modelProperty="siglaNomeFilial" />			
			<adsm:propertyMapping relatedProperty="regional.idRegional" modelProperty="regional.idRegional" blankFill="false"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
			<adsm:hidden property="sgFilial" serializable="true"/>
			<adsm:hidden property="nmFantasia" serializable="true"/>			
			<adsm:hidden property="siglaNomeFilial" serializable="true"/>
		</adsm:lookup>

	    <adsm:combobox label="regional" property="regional.idRegional" optionLabelProperty="siglaDescricao" optionProperty="idRegional" service="lms.vendas.imprimirListaMestraAction.findRegional" labelWidth="15%" width="35%" boxWidth="170" >
	         <adsm:propertyMapping relatedProperty="sgRegional" modelProperty="sgRegional"/>
	         <adsm:propertyMapping relatedProperty="dsRegional" modelProperty="dsRegional"/>
	         <adsm:propertyMapping relatedProperty="siglaDescricao" modelProperty="siglaDescricao"/>	         
	         <adsm:hidden property="sgRegional" serializable="true"/>
   	         <adsm:hidden property="dsRegional" serializable="true"/>
   	         <adsm:hidden property="siglaDescricao" serializable="true"/>
       </adsm:combobox>

		<adsm:textbox dataType="JTDate" property="dtReferencia" label="dataReferencia" width="75%"/>
		<adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.imprimirListaMestraAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>