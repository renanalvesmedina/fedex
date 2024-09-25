<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="alteracaoValoresColeta" service="lms.coleta.alteracaoValoresColetaAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/alteracaoValoresColeta" idProperty="idPedidoColeta" 
			   service="lms.coleta.alteracaoValoresColetaAction.findById" onDataLoadCallBack="carregaDados_retorno" >

		<adsm:section caption="alteracaoValoresColeta" />
		
		<adsm:textbox label="numeroColeta" property="filialByIdFilialResponsavel.sgFilial" dataType="text" 
					  size="3" labelWidth="20%" width="80%" disabled="true" serializable="false"  >
			<adsm:textbox property="nrColeta" dataType="integer" mask="0000000000" disabled="true" serializable="false" />
		</adsm:textbox>
	
		<adsm:textbox label="volumesInformado" property="qtTotalVolumesInformado" dataType="integer" size="10"  
					  labelWidth="20%" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="volumesVerificado" property="qtTotalVolumesVerificado" dataType="integer" size="10" maxLength="6" 
					  labelWidth="20%" width="30%" required="true" />

		<adsm:textbox label="valorTotalInformado" property="siglaSimboloInformado" dataType="text" size="8"
					  labelWidth="20%" width="30%" disabled="true" serializable="false" >
			<adsm:textbox property="vlTotalInformado" dataType="currency" size="18" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="valorTotalVerificado" property="siglaSimboloVerificado" dataType="text" size="8"
					  labelWidth="20%" width="30%" disabled="true" >
			<adsm:textbox property="vlTotalVerificado" dataType="currency" mask="###,###,###,###,##0.00" minValue="0.01"
						  size="18" maxLength="18" required="true" />
		</adsm:textbox>

		<adsm:textbox label="pesoInformado" property="psTotalInformado" dataType="weight"
					  unit="kg" size="18" labelWidth="20%" width="30%" disabled="true" serializable="false" />

		<adsm:textbox label="pesoVerificado" property="psTotalVerificado" dataType="weight"
					  unit="kg" size="18" maxLength="18" labelWidth="20%" width="30%" required="true" />

		<adsm:buttonBar> 
			<adsm:storeButton service="lms.coleta.alteracaoValoresColetaAction.store" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
		
		<script>
			var labelValorTotalVerificado = '<adsm:label key="valorTotalVerificado"/>';
		</script>
		
	</adsm:form>
</adsm:window>

<script>
document.getElementById("vlTotalVerificado").label = labelValorTotalVerificado;

function povoaForm() {
	onDataLoad( getElementValue('idPedidoColeta') );
}

function carregaDados_retorno_cb(data, error) {
	onDataLoad_cb(data, error);
	format(document.getElementById("nrColeta"));
}

function initWindow(eventObj) {
	if (eventObj.name == 'storeButton') {
		setFocus(document.getElementById("botaoFechar"), true, true);
	}
}
</script>