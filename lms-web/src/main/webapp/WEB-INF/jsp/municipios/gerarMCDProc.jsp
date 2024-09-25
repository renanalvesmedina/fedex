<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/gerarMCD">
		<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" label="vigenciaInicial"/>
		<adsm:buttonBar>
			<adsm:button caption="gerar" id="gerar" onclick="gerarMcd()" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<adsm:i18nLabels>
	<adsm:include key="LMS-29171"/>
</adsm:i18nLabels>
<script>

	function initWindow(){
		setDisabled("gerar", false);
	}
	
	function gerarMcd(confirmado){
				
		if (validateForm(document.forms[0])){		
			setDisabled("gerar", true);
			var data = new Array();				
			setNestedBeanPropertyValue(data, "dtVigenciaInicial", getElementValue("dtVigenciaInicial"));	
			
			if (confirmado != undefined){
				setNestedBeanPropertyValue(data, "confirmado", confirmado);	
			}
				
			var sdo = createServiceDataObject("lms.municipios.gerarMCDAction.execute",
												"gerarMcd",data);	
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function gerarMcd_cb(data, error, key){
		setDisabled("gerar", false);
		if (error != undefined) {
			if (key == 'LMS-29148'){
				if (confirm(error)){
					gerarMcd(true);
				}
			} else {
				alert(error);
			}
		} else {
			alert(i18NLabel.getLabel("LMS-29171"));
		}
	}
	
</script>