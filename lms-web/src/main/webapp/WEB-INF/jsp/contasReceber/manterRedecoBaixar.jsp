<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterRedecoAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/manterRedeco" service="lms.contasreceber.manterRedecoAction">
		<adsm:textbox label="dataLiquidacao" property="dtLiquidacao" dataType="JTDate" width="85%" required="true"/>
		
		<adsm:hidden property="idRedeco" serializable="true"/>

		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="baixar();" disabled="false"/>
			<adsm:button caption="cancelar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function baixar(){
		valid = validateForm(document.forms[0]);

		// apenas prossegue se a valida??o dos dados foi realizada com sucesso.
		if (valid == false) {
			return false;
		}			
		
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false, showSuccessMessage:true};
	    var dataCall = createServiceDataObject("lms.contasreceber.manterRedecoAction.baixarRedeco", "baixar", {idRedeco:dialogArguments.window.document.getElementById("idRedeco").value, dtLiquidacao:getElementValue("dtLiquidacao")});
	    remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);		
	}
	
	function baixar_cb(d,e,c,x){
		if (e == undefined){
			alert('LMS-36278 - Processamento concluído');
			window.close();
		} else {
			alert(e);
		}
	}	
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		setElementValue("dtLiquidacao", setFormat("dtLiquidacao", dialogArguments.window.getElementValue("dtRecebimento")));
	}
</script>