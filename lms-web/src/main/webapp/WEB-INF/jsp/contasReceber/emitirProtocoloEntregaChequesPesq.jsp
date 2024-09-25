<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirProtocoloEntregaCheques">

		<adsm:range label="periodoVencimento" width="30%" labelWidth="20%">
		
			<adsm:textbox property="dataInicial" dataType="JTDate"/>
			
			<adsm:textbox property="dataFinal" dataType="JTDate"/>
			
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" 
			required="true"
			label="formatoRelatorio" 
			domain="DM_FORMATO_RELATORIO"
			labelWidth="20%" width="30%"/>

		<adsm:buttonBar>
		
			<adsm:reportViewerButton service="lms.contasreceber.emitirProtocoloEntregaChequesAction" disabled="false"/>
			
			<adsm:resetButton/>
			
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>

<script>
function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
	setElementValue("tpFormatoRelatorio", "pdf");
	setaDataAtual();
}

function initWindow(){
	setElementValue("tpFormatoRelatorio", "pdf");
}

function setaDataAtual() {

	_serviceDataObjects = new Array();
	
	addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirProtocoloEntregaChequesAction.findDataAtual",
		"setDataAtual", 
		new Array()));

       xmit(false);	
}

function setDataAtual_cb(data, error) {
	setElementValue('dataInicial', data._value);
	setElementValue('dataFinal', data._value);
}

</script>