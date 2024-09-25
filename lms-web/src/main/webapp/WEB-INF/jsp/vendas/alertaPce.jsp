<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript"> 
<!--
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		//onDataLoad_cb(data);
		var s = document.location.href.indexOf("?");
		var field = "idVersaoDescritivoPce";
		if (s != -1) {
			var pS = document.location.href.indexOf(field, s);
			if (pS != -1) {
				var pE = document.location.href.indexOf("&", pS);
				var valor = "";
				if (pE != -1)
					valor = document.location.href.substring(pS + field.length + 1, pE);
				else
					valor = document.location.href.substring(pS + field.length + 1);
				setElementValue("idVersaoDescritivoPce", valor);
			}
		}
		var sdo = createServiceDataObject("lms.vendas.alertaPceAction.findDataAlertPCE", "callBackDefault", {idVersaoDescritivoPce:getElementValue("idVersaoDescritivoPce")});
		xmit({serviceDataObjects:[sdo]});
	}

	function callBackDefault_cb(data,exeption) {
		onDataLoad_cb(data,exeption);
		setDisabled(document, true);
		setDisabled("versaoContatoPces", false);
		document.getElementById("versaoContatoPces").readOnly = true;
		setDisabled("okButton", false);
		setFocus("okButton", false);
	}

	function callFunction() {
		var sdo = createServiceDataObject("lms.vendas.alertaPceAction.executeConfirmaRecebimentoDoAlerta","callBackConfirm",{idVersaoDescritivoPce:getElementValue("idVersaoDescritivoPce")});
		xmit({serviceDataObjects:[sdo]});
	}

	function callBackConfirm_cb(data,excpetion) {
		if (excpetion)
			alert(excpetion);
		else {
			if (dialogArguments.window.alertPCE_cb)
				dialogArguments.window.alertPCE_cb();
			self.close();
		}
	}
//-->
</script>
<adsm:window onPageLoadCallBack="pageLoad">
	<adsm:form action="/vendas/alertaPce" idProperty="qualquerCoisa">
		<adsm:section caption="alertaPce" width="65"/>
		<adsm:label key="branco" width="1%"/>
		<adsm:hidden property="idVersaoDescritivoPce"/>
		<adsm:lookup service="lms.vendas.manterVersoesPCEAction.findLookupCliente" dataType="text" property="versaoPce.cliente" idProperty="idCliente"
					criteriaProperty="pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" labelWidth="12%"
					width="88%" action="/vendas/manterDadosIdentificacao" exactMatch="true">
			<adsm:textbox dataType="text" property="versaoPce.cliente.pessoa.nmPessoa" size="40" disabled="true"/>
		</adsm:lookup>

		<adsm:label key="branco" width="1%"/>
		<adsm:textarea maxLength="250" columns="69" rows="6" label="descricao" property="dsDescritivoPce" labelWidth="12%" width="88%" />
		<adsm:label key="branco" width="1%"/>
		<adsm:textarea maxLength="50000" style="word-wrap:normal;overflow-x:scroll;overflow-y:scroll;" label="contatos" property="versaoContatoPces" columns="69" rows="4" labelWidth="12%" width="88%"/>
		<adsm:label key="branco" width="1%"/>
		<adsm:textbox dataType="text" label="usuario" property="usuario.nmUsuario" labelWidth="12%" width="88%"/>

		<adsm:buttonBar>
			<adsm:button caption="ok" onclick="callFunction()" id="okButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
