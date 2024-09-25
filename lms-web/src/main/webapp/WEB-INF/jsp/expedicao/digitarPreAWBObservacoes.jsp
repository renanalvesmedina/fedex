<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/expedicao/digitarPreAWBObservacoes">
	
		<adsm:hidden property="awb.idAwb"/>
		
		<adsm:section caption="observacoes" width="70"/>
		
		<adsm:label 
			key="branco" 
			style="border:none;" 
			width="1%" />
			
		<adsm:textarea 
			width="99%"
			maxLength="500" 
			property="obAwb"
			columns="101"
			rows="6" />

		<adsm:buttonBar>
			<adsm:button 
				id="btnSalvar"
				onclick="storeInSession();"
				caption="salvar" />
			<adsm:button 
				id="btnFechar"
				onclick="self.close();"
				caption="fechar" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
function myOnPageLoad_cb() {
	onPageLoad_cb();
	setDisabled("btnFechar", false);
	/*var idAwb = getElement("awb.idAwb", dialogArguments.document).value;
	if(idAwb != null && idAwb != "") {
		var obAwb = getElement("obAwb", dialogArguments.document).value;
		setElementValue("obAwb", obAwb);
		setDisabled("obAwb", true);
		setDisabled("btnSalvar", true);
		setFocus(getElement("btnFechar"), false);
	} else {*/
		setDisabled("btnSalvar", false);
		var service = "lms.expedicao.digitarPreAWBObservacoesAction.findInSession";
		var sdo = createServiceDataObject(service, "findInSession");
		xmit({serviceDataObjects:[sdo]});
	/*}*/
}

function findInSession_cb(data, error) {
	if(error) {
		alert(error);
		return;
	}
	if(data) {
		setElementValue("obAwb", data.obAwb);
	}
}

function storeInSession() {
	var obAwb = getElementValue("obAwb");
	dialogArguments.setElementValue("obAwb", obAwb);
	var service = "lms.expedicao.digitarPreAWBObservacoesAction.storeInSession";
	var sdo = createServiceDataObject(service, "storeInSession", {obAwb:obAwb});
	xmit({serviceDataObjects:[sdo]});
}

function storeInSession_cb() {
	self.close();
}
-->
</script>