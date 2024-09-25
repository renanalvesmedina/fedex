<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="execucaoColeta" service="lms.coleta.informarDataEventoAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/informarDataEvento" idProperty="idPedidoColeta" 
			   service="lms.coleta.informarDataEventoAction.findUsuario" onDataLoadCallBack="carregaDados_retorno" >

		<adsm:section caption="execucaoColeta" />

		<adsm:hidden property="idControleCarga"  serializable="false"/>
		<adsm:hidden property="idPedidoColeta"  serializable="false"/>		
		
		<adsm:textbox label="dtHoraOcorrencia" dataType="JTDateTimeZone" property="dtHoraOcorrencia" labelWidth="18%" width="32%" required="true" />
		
		<adsm:textbox label="registradaPor" property="nmUsuario" dataType="text" labelWidth="10%" width="40%" disabled="true" serializable="false" />
		

		<adsm:buttonBar> 
			<adsm:button caption="confirmar" id="botaoExecutarHora" onclick="javascript:confirmarDataHoraEvento(this.form)" />
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script>




function confirmarDataHoraEvento(form) {
	if (!validateForm(form)) {
		return false;
	}
	var data = new Object();
	data.dtHoraOcorrencia = getElementValue('dtHoraOcorrencia');
	data.idPedidoColeta = getElementValue('idPedidoColeta');
	data.idControleCarga = getElementValue('idControleCarga');
	var sdo = createServiceDataObject("lms.coleta.informarDataEventoAction.confirmarDataHoraEvento", "confirmarDataHoraEvento", data);
	xmit({serviceDataObjects:[sdo]});
}

function confirmarDataHoraEvento_cb(data, error) {			
	if (data._exception!=undefined) {
		alert(data._exception._message);
	}else{		

		var data = new Object();
		data.dtHoraOcorrencia = getElementValue('dtHoraOcorrencia');

		var parentWindow = dialogArguments.window;
		parentWindow.validatePCEExecutarColetasEntregas(data);
		self.close();
	}
	
}

function povoaForm() {
	onDataLoad( getElementValue('idPedidoColeta') );
}

function carregaDados_retorno_cb(data, error) {
	onDataLoad_cb(data, error);
}

function initWindow(eventObj) {
	if (eventObj.name == 'storeButton') {
		setFocus(document.getElementById("botaoFechar"), true, true);
	}
}
</script>