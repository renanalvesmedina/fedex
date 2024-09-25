<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.emitirControleCargasAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-11352"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/carregamento/emitirControleCargas">
		<adsm:label key="LMS-11347"  width="75%" style="font-size: 11px;padding-bottom: 20px;padding-left: 10px"/>
		<adsm:textarea 
			property="dsMotivos"
			required="false"
			columns="90"
			rows="12"
			maxLength="1000"
			label="motivos" 
			labelStyle="padding-left: 10px;vertical-align:top"
			cellStyle="padding-left: 10px;vertical-align:top"
			labelWidth="10%" 
			width="85%"
			disabled="true"/>
		<adsm:textarea 
			property="dsJustificativa"
			maxLength="500"
			required="true"
			columns="90"
			rows="3"
			label="justificativa" 
			labelStyle="padding-left: 10px;vertical-align:top"
			cellStyle="padding-left: 10px;vertical-align:top"
			labelWidth="10%" 
			width="85%"/>
			
		<adsm:buttonBar>
		
			<adsm:button 
				caption="nao" 
				onclick="self.close()"
				disabled="false" />
			<adsm:button 
				buttonType="storeButton"
				caption="sim" 
				onclick="setJustificativa(this.form)"
				disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	var u = new URL(parent.location.href);
	var msgWorkflow = u.parameters["msgWorkflow"];
	var idControleCarga = u.parameters["idControleCarga"]
	setElementValue("dsMotivos", msgWorkflow);
	
	function setJustificativa(form) {
		if (!validateForm(form)) {
			return "false";
		}
		var dsMotivos = getElementValue("dsMotivos");
		var dsJustificativa = getElementValue("dsJustificativa");
		
		var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.generatePendenciaWorkflow", 
										  "generatePendenciaWorkflow", 
										  { idControleCarga:idControleCarga,
											dsMotivos:dsMotivos, 
											dsJustificativa:dsJustificativa });
		
		xmit({serviceDataObjects:[sdo]});
	}
	
	function generatePendenciaWorkflow_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		alertI18nMessage("LMS-11352");
		self.close();
			
	}
</script>