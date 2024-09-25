<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/expedicao/justificarInclusaoAWBPrejuizo">	
		<adsm:section caption="justificativaInclusaoPrejuizo" width="70%" style="align:center" />	
		<adsm:textarea
			width="95%"
			labelWidth="0%"
			maxLength="500" 
			property="dsJustificativaPrejuizo"
			columns="90"
			rows="6"/>

		<adsm:buttonBar>
			<adsm:button 
				id="btnSalvarJustificativa"
				caption="salvar"
				onclick="salvarJustificativa();"/>
						
			<adsm:button 
				id="btnFecharJustificativa"
				onclick="self.close();"
				caption="fechar"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-04515"/>
		</adsm:i18nLabels>
		
	</adsm:form>
</adsm:window>
<script type="text/javascript">

function myOnPageLoad_cb() {
	onPageLoad_cb();
	setDisabled("btnSalvarJustificativa", false);
	setDisabled("btnFecharJustificativa", false);
}

/***************************/
/* ONCLICK CANCELAR BUTTON */
/***************************/
function salvarJustificativa() {
	var f = document.forms[0];
 	if (getElementValue('dsJustificativaPrejuizo') == "" || getElementValue('dsJustificativaPrejuizo') == null){
 		alert(i18NLabel.getLabel('LMS-04515'));
 		return false;
 	}
	
 	var elementParent = dialogArguments.window.document.getElementById('dsJustificativaPrejuizo');
	elementParent.value = getElementValue('dsJustificativaPrejuizo');
	self.close();
	
}

function storeSession_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return;
	}
	self.close();	
}


</script>