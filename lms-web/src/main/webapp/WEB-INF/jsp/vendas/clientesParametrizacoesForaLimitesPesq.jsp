<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="loadDataObjects">
	<adsm:form action="/vendas/clientesParametrizacoesForaLimites">
		<adsm:hidden property="tpAcesso" value="F"/>

		<adsm:combobox
			label="regional"
			property="regional.idRegional"
			optionLabelProperty="siglaDescricao"
			optionProperty="idRegional"
			service="lms.vendas.clientesParametrizacoesForaLimitesAction.findRegional"
			boxWidth="240"
   			labelWidth="13%" width="87%"/>

		<adsm:lookup 
			label="filial" labelWidth="13%" width="8%"
			property="filial"
            idProperty="idFilial"
            criteriaProperty="sgFilial"
            criteriaSerializable="true"
            action="/municipios/manterFiliais" 
			service="lms.vendas.clientesParametrizacoesForaLimitesAction.findLookupFilial"
            dataType="text"
            size="3" 
            maxLength="3"
            exactMatch="true"
            minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping 
        		relatedProperty="filial.pessoa.nmFantasia" 
        		modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping 
				relatedProperty="filial.sgFilial"
				modelProperty="sgFilial"/>
			<adsm:propertyMapping 
				criteriaProperty="regional.idRegional"
				modelProperty="regionalFiliais.regional.idRegional"/>
			<adsm:propertyMapping 
				criteriaProperty="tpAcesso"
				modelProperty="tpAcesso"/>

            <adsm:textbox 
            	dataType="text" 
         		property="filial.pessoa.nmFantasia" 
         		serializable="false" 
         		width="79%"
         		size="30" 
         		maxLength="50" disabled="true"/>
        </adsm:lookup>

		<adsm:textbox dataType="JTDate"
		  property="dataReferencia"
		  label="dataReferencia"
		  required="true"
		  labelWidth="13%"
		  width="30%"/> 

		<adsm:combobox property="tpFormatoRelatorio" 
		       label="formatoRelatorio"
		       labelWidth="15%" 
		       width="30%"
			   domain="DM_FORMATO_RELATORIO"
		       required="true" 
		       defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.clientesParametrizacoesForaLimitesAction"/>
			<adsm:resetButton onclick="dataSession_cb"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	/*DADOS INICIAIS DA TELA*/
	var idRegional
		,idFilial
		,sgFilial
		,nmEmpresa
		,dtAtual;

	function initWindow(event) {
		if(event.name == "cleanButton_click"){
			writeDataSession();
		}
	}

	/**
	 * Carrega dados iniciais
	 */
	function loadDataObjects_cb(data) {	
	    onPageLoad_cb(data); 
		var sdo = createServiceDataObject("lms.vendas.clientesParametrizacoesForaLimitesAction.getBasicData", "dataSession");
    	xmit({serviceDataObjects:[sdo]});
	}

	function dataSession_cb(data) {
		idRegional = getNestedBeanPropertyValue(data, "regional.idRegional");
		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmEmpresa = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");
		dtAtual = getNestedBeanPropertyValue(data, "dtAtual");
		writeDataSession();
	}

	function writeDataSession()	{
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmEmpresa);
		setElementValue("dataReferencia", setFormat(getElement("dataReferencia"),dtAtual));
		setElementValue("regional.idRegional", idRegional);
	}
</script>	