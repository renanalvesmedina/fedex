<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction"  title="descricao" onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/consultarDescricaoAgendasCobrancaInadimplencia" idProperty="idAgendaCobranca">
	
		<adsm:textarea property="dsAgendaCobranca" maxLength="100" columns="70" rows="6" label="descricaoAgendaCobranca" labelWidth="14%" serializable="false"/>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="fechar" id="fechar" disabled="false" onclick="closeWindow();" buttonType="storeButton"/>
		</adsm:buttonBar>
		
	</adsm:form>

</adsm:window>

<script>

function myPageLoadCallBack_cb(){
	setMasterLink(this.document, true);
	carregaDescricaoAgendaCobranca();
}

function carregaDescricaoAgendaCobranca() {
	var idAgendaCobranca = getElementValue("idAgendaCobranca");
	var map = new Array();
	setNestedBeanPropertyValue(map, "idAgendaCobranca", idAgendaCobranca);
    var sdo = createServiceDataObject("lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction.findDescricaoAgendaCobranca", "populaTextAreaDescricao",  map);
    xmit({serviceDataObjects:[sdo]});
}

function populaTextAreaDescricao_cb(data, errors){
	setElementValue("dsAgendaCobranca", data.dsAgendaCobranca);
	setDisabled("dsAgendaCobranca", true);
	document.getElementById("fechar").focus();
}

function closeWindow(){
	window.close();	
}

</script>

