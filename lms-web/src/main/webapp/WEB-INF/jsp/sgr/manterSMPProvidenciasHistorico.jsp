<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
function carregaDadosPageLoad() {

	onPageLoad();
	setMasterLink(this.document, true);
    document.getElementById("dsHistoricoAlteracao").readOnly = "true";
    carregaDados();
}

function carregaDados() {
	var idExigenciaSmp = getElementValue("idExigenciaSmp");
	var map = new Array();
	setNestedBeanPropertyValue(map, "idExigenciaSMP", idExigenciaSmp);
    var sdo = createServiceDataObject("lms.sgr.manterSMPAction.findHistoricoById", "resultadoCarregaDados", map);
    xmit({serviceDataObjects:[sdo]});
}


function resultadoCarregaDados_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	
    setElementValue('dsHistoricoAlteracao', setFormat(document.getElementById("dsHistoricoAlteracao"), data.dsHistoricoAlteracao));
    document.getElementById("dsHistoricoAlteracao").readOnly = "true";
    setFocus( document.getElementById("idFechar"), true, true );
    
}

function closeWindow() {
	window.close();
}

</script>

<adsm:window service="lms.sgr.manterSMPAction" onPageLoad="carregaDadosPageLoad">
	<adsm:form action="/sgr/manterSMP" idProperty="idExigenciaSmp">
		<adsm:hidden property="idExigenciaSmp"/>
		<adsm:section caption="historico" width="72%" />
		<adsm:label key="branco" width="1%"  />
		<adsm:textarea property="dsHistoricoAlteracao" maxLength="1000" columns="100" rows="7" labelWidth="0%" width="99%"  disabled="false" />
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="fechar" id="idFechar" disabled="false" onclick="closeWindow()" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>