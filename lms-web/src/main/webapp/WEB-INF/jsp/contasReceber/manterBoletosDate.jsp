<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterBoletos" 
	service="lms.contasreceber.manterBoletosAction">
		<adsm:hidden property="idBoleto"/>
		
		<adsm:textbox label="novoVencimentoEmAprovacao" property="dtVencimentoNovo" dataType="JTDate" required="true" labelWidth="22%" width="15%"/>

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
				 dtVencimentoNovo:getElementValue("dtVencimentoNovo")
				});
			remoteCall.serviceDataObjects.push(storeSDO);
			xmit(remoteCall);
		}
	}
	
	function prorrogarVencimentoBoleto_cb(d,e,c,x){
		if (e == undefined) {
			
			dialogArguments.window.setElementValue("dtVencimento", setFormat("dtVencimentoNovo", d.dtVencimento));
			dialogArguments.window.setElementValue("fatura.vlJuroCalculado", setFormat(dialogArguments.window.getElement("fatura.vlJuroCalculado"), "0"));
		
			dialogArguments.window.showSuccessMessage();
			window.close();
		} else {
			alert(e);
		}
	}
	
	function myOnPageLoad_cb(){
		setElementValue("idBoleto",dialogArguments.window.document.getElementById("idBoleto").value);
		
		/*_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.findDataAtual", "setData")); 
		xmit(false);*/			
	}
	
	function setData_cb(d,e,c){
		if (d != null){
			setElementValue("dtVencimentoNovo", setFormat(getElement("dtVencimentoNovo"), d._value));
		}

	}	
</script>