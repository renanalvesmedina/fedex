<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="motivoParadaTitulo" service="lms.carregamento.manterControleCargasJanelasAction" onPageLoad="carregaPagina" >

	<adsm:form action="/carregamento/manterControleCargasLocalTroca" >
		<adsm:hidden property="idLocalTroca" />
		<adsm:label key="espacoBranco" width="1%" style="border:none;"/>
		<adsm:textarea property="dsTroca" maxLength="300" columns="81" rows="8" 
					   labelWidth="0%" width="100%" />

		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function povoaForm() {
	var sdo = createServiceDataObject("lms.carregamento.manterControleCargasJanelasAction.findDescricaoLocalTroca", "retornoDescricaoLocalTroca", 
		{idLocalTroca:getElementValue("idLocalTroca")});
    xmit({serviceDataObjects:[sdo]});
}

function retornoDescricaoLocalTroca_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (data != undefined) {
		setElementValue("dsTroca", getNestedBeanPropertyValue(data, "_value"));
		document.getElementById("dsTroca").readOnly = true;
		setDisabled("botaoFechar", false);
		setFocus(document.getElementById("botaoFechar"), true, true);
	}
}
</script>