<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function abreAlert() {
		var id = getElementValue("idVersaoDescritivoPce");
		if (id != "")
			showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + id + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
		else
			alert("informe o idVersaoDescritivoPce");	
	}
	function verificaExistencia() {	
		var idO = getElementValue("cdOcorrencia");
		var idE = getElementValue("cdEvento");
		var idP = getElementValue("cdProcesso");
		var idC = getElementValue("idCliente");
		if (idO != "" && idE != "" && idP != "" && idC != "") {
			var sdo = createServiceDataObject("lms.vendas.alertaPceAction.validateifExistPceByCriteria","callBackConfirm",{idCliente:idC
				,cdOcorrenciaPce:idO,cdEventoPce:idE,cdProcessoPce:idP});
			xmit({serviceDataObjects:[sdo]});
		}else
			alert("informe todos os dados");
	}
	
	function retornaMensagem() {	
		var idO = getElementValue("cdOcorrencia");
		var idE = getElementValue("cdEvento");
		var idP = getElementValue("cdProcesso");
		var idC = getElementValue("idCliente");
		if (idO != "" && idE != "" && idP != "" && idC != "") {
			var sdo = createServiceDataObject("lms.vendas.alertaPceAction.findMensagemPce","callBackConfirm2",{idCliente:idC
				,cdOcorrenciaPce:idO,cdEventoPce:idE,cdProcessoPce:idP});
			xmit({serviceDataObjects:[sdo]});
		}else
			alert("informe todos os dados");
	}
	
	function callBackConfirm_cb(data,exception) {
		if (exception)
			alert(exception);
		else if (getNestedBeanPropertyValue(data,"_value") != undefined)
			alert("achou, o id é :" + getNestedBeanPropertyValue(data,"_value"));
		else
			alert("não encontrou");
	}
	
	function callBackConfirm2_cb(data,exception) {
		if (exception) {
			alert(exception);
			return;
		}
		if (data == undefined || data == null)
			alert("não encontrou");
		else
			alert(data._value);
	}
	
	function initWindow(eventObj) {
		setDisabled("alertPCE",false);
		setDisabled("validExist",false);
		setDisabled("returnMessage",false);
	}
//-->
</script>
<adsm:window>
	<adsm:form action="/vendas/alertaPce">
		<adsm:section caption="verificaExisencia" width="100"></adsm:section>
		<adsm:textbox label="cdOcorrencia" dataType="integer" property="cdOcorrencia"/>
		<adsm:textbox label="cdEvento" dataType="integer" property="cdEvento"/>
		<adsm:textbox label="cdProcesso" dataType="integer" property="cdProcesso"/>
		<adsm:textbox label="idCliente" dataType="integer" property="idCliente"/>
		<adsm:section caption="alertaPce" width="100"></adsm:section>
		<adsm:textbox label="idVersaoDescritivoPce" dataType="integer" property="idVersaoDescritivoPce"/>
		
		
    	<adsm:buttonBar>				
			<adsm:button id="alertPCE" disabled="false" caption="alertaPce" onclick="abreAlert();" /> 
			<adsm:button id="validExist" disabled="false" caption="verifica se existe" onclick="verificaExistencia();" /> 
			<adsm:button id="returnMessage" disabled="false" caption="retorna mensagem" onclick="retornaMensagem();" /> 
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   