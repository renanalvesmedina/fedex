<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.entrega.manterAgendamentosAction" onPageLoadCallBack="manterAgendamentoCancelar" >
	<adsm:form action="/entrega/manterAgendamentos" onDataLoadCallBack="formLoad" >

		<adsm:hidden property="blCancelamento" value="true"/>
		<adsm:hidden property="idAgendamentoEntrega"/>

		<adsm:label key="branco" width="100%" style="height:25px;border:none"/>
	
		<adsm:section caption="cancelamento"/>
		<adsm:combobox service="lms.entrega.manterAgendamentosAction.findMotivoAgendamento" labelWidth="20%" width="30%" required="false" property="motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento"  label="motivo"  optionProperty="idMotivoAgendamento"  optionLabelProperty="dsMotivoAgendamento" onlyActiveValues="true" boxWidth="200"/>
		<adsm:textarea label="observacao" property="obCancelamento" maxLength="500" style="width:463px" disabled="false" required="false" labelWidth="20%" width="80%" />
		<adsm:buttonBar freeLayout="false">
			<adsm:storeButton id="storeButton" callbackProperty="salvar" />
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function salvar_cb(data,error,key) {
		if (error) {
			alert(error);
			if (key = "LMS-09065") {
				setFocus("motivoAgendamentoByIdMotivoCancelamento.idMotivoAgendamento");
			}
		} else {
			var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findById", "loadBasicData", data);
	    	xmit({serviceDataObjects:[sdo]});
		}
	}

	function loadBasicData_cb(data) {
		window.dialogArguments.formLoad_cb(data);
		self.close();
	}

	function manterAgendamentoCancelar_cb() {
		onPageLoad_cb();   
		setDisabled("btnFechar",false);
		setElementValue("idAgendamentoEntrega", getElementValue(window.dialogArguments.document.forms[0].elements["idAgendamentoEntrega"]));
	}
	
</script>	