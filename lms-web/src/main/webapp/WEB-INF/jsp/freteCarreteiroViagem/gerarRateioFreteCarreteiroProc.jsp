<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.gerarRateioFreteCarreteiroService">
	<adsm:form action="/freteCarreteiroViagem/gerarRateioFreteCarreteiro">
		<adsm:textbox dataType="text" property="idControleCarga" required="true" label="controleCarga"/>
		<adsm:textbox dataType="text" property="valorTotalPago" disabled="true" required="false" label="valorTotalPago"/>
		<adsm:buttonBar>
			<adsm:button id="gerar" caption="gerar" service="lms.fretecarreteiroviagem.gerarRateioFreteCarreteiroAction.execute" callbackProperty="onDataLoadGerar" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function initWindow(){
		setDisabled("gerar", false);
	}

	function onDataLoadGerar_cb(data, exception) {
		onDataLoad_cb(data);
		if (exception != undefined) {
			alert(exception);
		}
	}				
	
	function gerarRFC(){
		setElementValue("valorTotalPago", "");
		if (validateForm(document.forms[0])){		
			setDisabled("gerar", true);
			var data = new Array();				
			setNestedBeanPropertyValue(data, "idControleCarga", getElementValue("idControleCarga"));		
			var sdo = createServiceDataObject("lms.fretecarreteiroviagem.gerarRateioFreteCarreteiroService.execute","gerarRFC",data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function gerarRFC_cb(data, error){
		onDataLoad_cb(data);
		
		setDisabled("gerar", false);
		if (error != undefined)
			alert(error);
		setDisabled("gerar", false);
	}
	
</script>