<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/freteCarreteiroColetaEntrega/gerarNC">
	
		<adsm:textbox dataType="integer" property="idControleCarga" required="true" label="controleCarga"/>
		<adsm:textbox dataType="text" disabled="true" property="vlNotaCredito" label="valor"/>
		
		<adsm:buttonBar>
			<adsm:button caption="gerar" id="gerar" onclick="gerarNC()" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function initWindow(){
		setDisabled("gerar", false);
	}
	
	function gerarNC(){
				
		if (validateForm(document.forms[0])){		
			setDisabled("gerar", true);
			var data = new Array();				
			setNestedBeanPropertyValue(data, "idControleCarga", getElementValue("idControleCarga"));		
			var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.gerarNotaCreditoService.executeTeste",
												"gerarNC",data);	
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function gerarNC_cb(data, error){
		setDisabled("gerar", false);
		if (error != undefined) {
			alert(error);
		} else {
			setElementValue("vlNotaCredito", data._value);
		}
	}
	
	
</script>