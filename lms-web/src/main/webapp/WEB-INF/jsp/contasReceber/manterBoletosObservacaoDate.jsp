<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterBoletos" 
	service="lms.contasreceber.manterBoletosAction">
		<adsm:hidden property="idBoleto"/>

		<adsm:combobox 
			service="lms.contasreceber.manterBoletosAction.findComboMotivoOcorrenciaVencimento" 
			optionLabelProperty="dsMotivoOcorrencia" 
			optionProperty="idMotivoOcorrencia" 
			property="idMotivoOcorrencia" 
			label="motivoOcorrencia"
			onlyActiveValues="true"
			boxWidth="320"
			required="true" labelWidth="22%" width="75%"> 
		</adsm:combobox>
		
		<adsm:textbox label="novoVencimentoEmAprovacao" labelWidth="22%" property="dtVencimentoNovo" dataType="JTDate" required="true"  width="15%"/>

		<adsm:textarea label="observacao"  labelWidth="22%" property="dsHistoricoBoleto" width="75%" maxLength="500" 
			columns="67" rows="5"/>

		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="return prorrogarVencimentoBoleto();" disabled="false"/>
			<adsm:button caption="cancelar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
	function prorrogarVencimentoBoleto(){
		if (validateForm(document.forms[0])){
			var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
			var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.prorrogarVencimentoBoleto", "prorrogarVencimentoBoleto", 
				{idBoleto:getElementValue("idBoleto"),
				 idMotivoOcorrencia:getElementValue("idMotivoOcorrencia"),
				 dsHistoricoBoleto:getElementValue("dsHistoricoBoleto"),
				 dtVencimentoNovo:getElementValue("dtVencimentoNovo")
				});
			remoteCall.serviceDataObjects.push(storeSDO);
			xmit(remoteCall);
		}
	}
	
	function prorrogarVencimentoBoleto_cb(d,e,c,x){
		if (e == undefined) {
			
			dialogArguments.window.setElementValue("dtVencimentoNovo", setFormat("dtVencimentoNovo", d.dtVencimentoNovo));
			
			dialogArguments.window.showSuccessMessage();
			window.close();
		} else {
			alert(e);
		}
	}
	
	function myOnPageLoad_cb(){
		setElementValue("idBoleto",dialogArguments.window.document.getElementById("idBoleto").value);	
	}
	
	function setData_cb(d,e,c){
		if (d != null){
			setElementValue("dtVencimentoNovo", setFormat(getElement("dtVencimentoNovo"), d._value));
		}

	}	
</script>