<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarPreNotaFiscalServicoAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/expedicao/digitarPreNotaFiscalServicoEmitirNF" idProperty="idNotaFiscalServico">

		<adsm:section caption="reemitirNotaFiscalTitulo" width="43%"/>
		<adsm:hidden property="userip"/>
		<adsm:hidden property="idNotaFiscalServico"/>

		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:textbox 
			property="numFormulario" 
			label="numeroProximoFormulario" 
			dataType="integer" 
			size="12" 
			labelWidth="26%" 
			width="73%" 
			minValue="0" 
			maxLength="6" 
			required="true"/>

		<adsm:buttonBar>
			<adsm:button disabled="false" id="reemitirID" buttonType="reemitirBT" caption="reemitir" onclick="reemitir()" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
function myOnPageLoad_cb() {
	onPageLoad_cb();
	var u = new URL(parent.location.href);
	var idNF = u.parameters["idNF"];
	setElementValue("idNotaFiscalServico",idNF);
}

function reemitir() {
	var idNotaFiscalServico = getElementValue("idNotaFiscalServico");
	var numFormulario = getElementValue("numFormulario");
	var userip = getElementValue("userip");
	var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.reemiteNF", "reemitir", {idNotaFiscalServico:idNotaFiscalServico,numFormulario:numFormulario,userip:userip});
	xmit({serviceDataObjects:[sdo]});
}

function reemitir_cb(dados, erros) {
	if(erros != undefined) {
		alert(erros);
		return;
	}
	closePopup(dados);
}

function closePopup(retorno) {
	window.returnValue = retorno;
	self.close();
}
</script>