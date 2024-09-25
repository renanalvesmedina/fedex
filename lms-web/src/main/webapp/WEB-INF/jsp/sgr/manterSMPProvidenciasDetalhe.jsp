<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>
function carregaDadosPageLoad() {

	onPageLoad();
	setMasterLink(this.document, true);
	
    document.getElementById("dsCompleta").readOnly = "true";
	carregaDados();
}

function carregaDados() {
	var idExigenciaGerRisco = getElementValue("exigenciaGerRisco.idExigenciaGerRisco");
	
	var map = new Array();
	setNestedBeanPropertyValue(map, "idExigenciaGerRisco", idExigenciaGerRisco);
    var sdo = createServiceDataObject("lms.sgr.manterSMPAction.findExigenciaById", "resultadoCarregaDados", map);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoCarregaDados_cb(data, error) {
	if(error!=undefined) {
		alert(error);
		return false;
	}
	
    setElementValue('dsCompleta', setFormat(document.getElementById("dsCompleta"), data.dsCompleta));
    document.getElementById("dsCompleta").readOnly = "true";
    setFocus( document.getElementById("idFechar"), true, true );
    
}

function closeWindow() {
	window.close();
}

</script>

<adsm:window service="lms.sgr.manterSMPAction" onPageLoad="carregaDadosPageLoad">
	<adsm:form action="/sgr/manterSMP" idProperty="idExigenciaSmp">
		<adsm:hidden property="exigenciaGerRisco.idExigenciaGerRisco"/>
		<adsm:section caption="exigenciaDetalhe" width="72%" />
		<adsm:label key="branco" width="1%"  />
		<adsm:textarea property="dsCompleta" maxLength="1000" columns="100" rows="7" labelWidth="0%" width="99%"  disabled="false" />
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="fechar" id="idFechar" disabled="false" onclick="closeWindow()" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>