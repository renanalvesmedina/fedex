<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/expedicao/manterManifestosViagemCancelarManifesto">

		<adsm:section caption="cancelamentoManifesto" width="40%" />
		<adsm:label key="espacoBranco" style="border:none" width="100%" />

		<adsm:label key="branco" style="border:none" width="11%" />
		<adsm:label key="cancelarManifestoFrase" style="border:none" width="89%" />

		<adsm:label key="espacoBranco" style="border:none" width="100%" />
		<adsm:label key="branco" style="border:none" width="12%" />
		<adsm:checkbox property="reaproveitarDados" labelWidth="20%" width="68%" text="reaproveitarDados"/>

	<adsm:hidden property="idManifestoViagemNacional" />
	<adsm:hidden property="blForceCancel" value="true" />
	<adsm:buttonBar>
		<adsm:button id="simButton" caption="sim" 
					 service="lms.expedicao.manterManifestosViagemAction.cancelManifestoViagem"
					 callbackProperty="cancelarCallBack"
					 disabled="false"/>
		<adsm:button id="naoButton" caption="nao" disabled="false" onclick="window.close();"/>
	</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
<!--
	function cancelarCallBack_cb(data, error, errorKey) {
		if (error != undefined) {
			if(errorKey == "LMS-04211") {
				if(confirm(error)) {
					setElementValue("blForceCancel", false);
					storeButtonScript("lms.expedicao.manterManifestosViagemAction.cancelManifestoViagem", "cancelarCallBack", document.forms[0]);
				}
			} else {
				alert(error);
			}
			return;
		}
		window.returnValue = data.blReaproveitarDados;
		if (data.tpStatusManifesto != undefined) {
			dialogArguments.window.setElementValue("manifesto.tpStatusManifesto.description", data.tpStatusManifesto);
		}
		window.close();
	}
	function myOnPageLoad_cb(d,e){
		onPageLoad_cb(d,e);
		setElementValue("idManifestoViagemNacional", dialogArguments.window.getElementValue("idManifestoViagemNacional"));
	}
</script>