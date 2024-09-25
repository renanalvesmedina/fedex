<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="requiredField"/>
		<adsm:include key="tipoCancelamento"/>
		<adsm:include key="motivoCancelamento"/>
	</adsm:i18nLabels>

	<adsm:form action="/expedicao/cancelarAWB">
		<adsm:label key="branco" width="1%"/>
		<adsm:combobox 
			property="tpMotivoCancelamentoAwb" 
			label="tipoCancelamento"
			width="82%" 
			labelWidth="17%" 
			domain="DM_TIPO_CANCELAMENTO_AWB"
			required="true"/>
		
		<adsm:label key="branco" width="1%"/>		
		<adsm:textarea 
			label="motivoCancelamento"
			width="82%"
			labelWidth="17%"
			maxLength="1000" 
			property="dsTpMotivoCancelamentoAwb"
			columns="98"
			rows="6"
			required="true" />

		<adsm:buttonBar>
			<adsm:button 
				id="btnConfirmar"
				caption="confirmar"
				onclick="cancelarAwbs();"/>
						
			<adsm:button 
				id="btnFechar"
				onclick="self.close();"
				caption="fechar"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
function myOnPageLoad_cb() {
	onPageLoad_cb();
	setDisabled("btnConfirmar", false);
	setDisabled("btnFechar", false);
	setFocus("btnFechar", false);
}

/***************************/
/* ONCLICK CANCELAR BUTTON */
/***************************/
function cancelarAwbs() {	
	var vIdAwb = dialogArguments.getElementValue("awb.idAwb");
	var tpMotivoCancelamentoAwb = getElementValue("tpMotivoCancelamentoAwb");
	var dsTpMotivoCancelamentoAwb = getElementValue("dsTpMotivoCancelamentoAwb");
	
	if((tpMotivoCancelamentoAwb === null || tpMotivoCancelamentoAwb === '') || 
			(dsTpMotivoCancelamentoAwb === null || dsTpMotivoCancelamentoAwb === '')) {
		
		if(tpMotivoCancelamentoAwb === null || tpMotivoCancelamentoAwb === ''){
			alertRequiredField("tipoCancelamento", true);
		} else {
			if(dsTpMotivoCancelamentoAwb === null || dsTpMotivoCancelamentoAwb === ''){
				alertRequiredField("motivoCancelamento", true);
			}
		}
	} else {
		var data = {idAwb : vIdAwb,
					tpMotivoCancelamentoAwb: tpMotivoCancelamentoAwb, 
					dsTpMotivoCancelamentoAwb: dsTpMotivoCancelamentoAwb};
		var service = "lms.expedicao.cancelarAwbAction.executeCancelarAwbs";
		var sdo = createServiceDataObject(service, "cancelarAwbs", data);
		xmit({serviceDataObjects:[sdo]});
	}
}

function cancelarAwbs_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return;
	}
	self.close();	
}


-->
</script>