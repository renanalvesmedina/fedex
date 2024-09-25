<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.pedidoColetaService" title="cancelarColeta">
	<adsm:form action="/coleta/cancelarColeta" idProperty="pedidoColeta.idPedidoColeta">
		<adsm:combobox label="motivoCancelamento" labelWidth="20%" width="80%"
					   service="lms.coleta.ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColetaCancelada"
					   property="ocorrenciaColeta.idOcorrenciaColeta"
					   optionProperty="idOcorrenciaColeta"
					   optionLabelProperty="dsDescricaoCompleta"
					   onlyActiveValues="true"
					   required="true"
		/>
					   
		<adsm:textarea label="observacao" property="observacao" maxLength="100" rows="3" columns="50" labelWidth="20%" width="80%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="confirmar" service="lms.coleta.cancelarColetaAction.executeCancelarColeta" disabled="false" callbackProperty="confirmaCancelaColeta" />
			<adsm:button caption="fechar" disabled="false" onclick="window.close()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script javascript>
setElementValue("pedidoColeta.idPedidoColeta",dialogArguments.window.document.getElementById("pedidoColeta.idPedidoColeta").value);

function confirmaCancelaColeta_cb(data, error) {
	if (error != undefined) {
		alert(error);
	} else {
		showSuccessMessage();
		window.returnValue = data;
		window.close();
	}
}


</script>
