<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	onPageLoad();
	
}
</script>
<adsm:window service="lms.indenizacoes.informarPagamentoAction" onPageLoad="carregaPagina" >
	<adsm:form action="/indenizacoes/informarPagamento">

		<adsm:textarea property="ltReciboIndenizacao" maxLength="65535"
				label="lote" rows="10" columns="64" labelWidth="18%" width="82%" disabled="false" 
					onchange="javascript:(this.value!= '' ? document.getElementById('confirmarPagamentoLote').disabled=false : document.getElementById('confirmarPagamentoLote').disabled=true);"/>

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="confirmarPagamento" id="confirmarPagamentoLote" disabled="false" 
						 onclick="pagar_onClick(); " />
		</adsm:buttonBar>
		
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-21002"/>
		</adsm:i18nLabels>
		
	</adsm:form>


</adsm:window>

<script>
function initWindow(eventObj) {
	if (getElementValue("ltReciboIndenizacao") != null || getElementValue("ltReciboIndenizacao") != "" ) {
		setDisabled('confirmarPagamentoLote', false);
	} else {
		setDisabled('confirmarPagamentoLote', true);
	}
	if (eventObj.name == 'tab_load') {
		setDisabled('confirmarPagamentoLote', true);
	}
	setFocusOnFirstFocusableField();
}

function pagar_onClick() {
	var msg = i18NLabel.getLabel("LMS-21002");
	if(confirm(msg) == true ) {
	    var fields = buildFormBeanFromForm(this.document.forms[0]);
	    var sdo = createServiceDataObject("lms.indenizacoes.informarPagamentoAction.executeInformaPagamentoLote", "retornoExecucao", 
	    		fields);
	    xmit({serviceDataObjects:[sdo]});
	}
}

function retornoExecucao_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setFocusOnFirstFocusableField();
		return false;
	}
   	showSuccessMessage();
}
</script>