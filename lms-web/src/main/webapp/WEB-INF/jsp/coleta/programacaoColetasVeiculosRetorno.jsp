<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="retornoColeta" service="lms.coleta.programacaoColetasVeiculosRetornoAction" onPageLoadCallBack="carregaPagina_retorno">
	<adsm:form action="/coleta/programacaoColetasVeiculos" idProperty="idPedidoColeta">
		<adsm:hidden property="idControleCarga"  serializable="false"/>
		<adsm:hidden property="tpEventoColeta" value="RC" serializable="false"/>
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		<adsm:combobox property="ocorrenciaColeta.idOcorrenciaColeta" 
					   optionProperty="idOcorrenciaColeta" optionLabelProperty="dsDescricaoCompleta" 
					   service="lms.coleta.programacaoColetasVeiculosRetornoAction.findOcorrenciaColeta" 
					   label="motivoRetorno" boxWidth="290" labelWidth="20%" width="45%" required="true" 
					   onchange="return ocorrenciaColeta_OnChange(this);" >
				<adsm:propertyMapping modelProperty="tpEventoColeta" criteriaProperty="tpEventoColeta" />
				<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
				<adsm:propertyMapping modelProperty="blIneficienciaFrota" relatedProperty="ocorrenciaColeta.blIneficienciaFrota" />
		</adsm:combobox>
		
		<adsm:combobox property="ocorrenciaColeta.blIneficienciaFrota" label="ineficienciaFrota" domain="DM_SIM_NAO" 
					   renderOptions="true" width="20%" required="true" />

		<adsm:textarea property="dsDescricao" label="observacao" maxLength="100" columns="90" rows="3" labelWidth="20%" width="80%"  />

		<adsm:lookup dataType="text" property="meioTransporte" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.coleta.programacaoColetasVeiculosRetornoAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 picker="false" label="retransmitirFrota" labelWidth="20%" width="80%" size="6" serializable="true" maxLength="6" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
		</adsm:lookup>
		
		<adsm:textbox label="dtHoraOcorrencia" dataType="JTDateTimeZone" property="dtHoraOcorrencia" labelWidth="20%" width="30%" required="true"/>
		
		<adsm:textbox label="registradaPor" property="nmUsuario" dataType="text" labelWidth="10%" width="40%" disabled="true" serializable="false" />
		
		
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="confirmar" id="botaoConfirmar" disabled="false" onclick="javascript:salvar(this.form);"/>
			<adsm:button caption="fechar" id="botaoFechar" disabled="false" onclick="javascript:window.close();" />
		</adsm:buttonBar>

		<script>
			var msgRegSelec = '<adsm:label key="LMS-00053"/>';
		</script>
	</adsm:form>
</adsm:window>

<script>
function carregaPagina_retorno_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	notifyElementListeners({e:document.getElementById("tpEventoColeta")});
	
	var remoteCall = {serviceDataObjects:new Array()}; 
	remoteCall.serviceDataObjects.push(createServiceDataObject("lms.coleta.informarDataEventoAction.findUsuario", "carregaDados_retorno_dataHora", data)); 
	xmit(remoteCall); 	
}

function carregaDados_retorno_dataHora_cb(data, error) {
	onDataLoad_cb(data, error);
}

function ocorrenciaColeta_OnChange(combo) {
	var r = comboboxChange({e:combo});
	if (getElementValue('ocorrenciaColeta.blIneficienciaFrota') == "")
		setDisabled('ocorrenciaColeta.blIneficienciaFrota', false);
	else
		setDisabled('ocorrenciaColeta.blIneficienciaFrota', true);
	return r;
}

function salvar(form) {
	if (!validateForm(form)) {
		return false;
	}
	var ids = dialogArguments.window.coletasPendentesGridDef.getSelectedIds().ids;
	
	if (ids.length == 0) {		
		alert(msgRegSelec);
	}else{
		var idsColeta = ids.join(";");
		
		var data = new Object();
		data.dtHoraOcorrencia = getElementValue('dtHoraOcorrencia');
		data.idPedidoColeta = idsColeta;
		data.idControleCarga = dialogArguments.window.getIdControleCarga();
		
		var sdo = createServiceDataObject("lms.coleta.informarDataEventoAction.confirmarDataHoraEvento", "confirmarDataHoraEvento", data);
		xmit({serviceDataObjects:[sdo]});
	}	
}

function confirmarDataHoraEvento_cb(data, error) {			
	if (data._exception!=undefined) {
		alert(data._exception._message);
	}else{				
		var idsMap = dialogArguments.window.coletasPendentesGridDef.getSelectedIds();
				
		data = buildFormBeanFromForm(document.forms[0]);		
		setNestedBeanPropertyValue(data, "idsPedidoColeta", idsMap);
		
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.coleta.programacaoColetasVeiculosRetornoAction.generateRetornarColeta", "resultadoExecutar", data)); 
		xmit(remoteCall);
	}	
}


function resultadoExecutar_cb(data, error) {
	if (error)
		alert(error);
	else {
		dialogArguments.window.povoaGrid();
		window.close();
	}
}
</script>