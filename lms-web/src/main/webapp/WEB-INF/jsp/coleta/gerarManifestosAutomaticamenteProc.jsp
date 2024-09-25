<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function showPopup(){
		//A mensagem a ser mostrada na confirmação está logo abaixo (dentro da tag form).
		var retorno = confirm(mensagem);
		if (retorno){
			setDisabled(document.getElementById("gerar"), true);
	    	var data = new Array();
			var sdo = createServiceDataObject("lms.coleta.gerarManifestosAutomaticamenteAction.gerarManifestosAutomaticamente", "geracao", data);
    		xmit({serviceDataObjects:[sdo]});
    		
		}
	}

	function geracao_cb(data, error) {
		if (!error){
			showSuccessMessage();
			if (data){
				alert(data.mensagem);
			}
		} else {
			alert(error);
		}
		setDisabled(document.getElementById("gerar"), false);
	}

	function initWindow(eventObj) {
		setDisabled(document.getElementById("gerar"), false);
	}

	
</script>
<adsm:window title="gerarManifestosAutomaticamente" onPageLoad="showPopup">
	<adsm:form action="/coleta/gerarManifestosAutomaticamente">
	<adsm:label key="branco" />
		<adsm:buttonBar>
			<adsm:button caption="gerar" onclick="showPopup()" id="gerar" />
		</adsm:buttonBar>
		<script type="text/javascript">
			var mensagem = '<adsm:label key="LMS-02003"/>';
		</script>
	</adsm:form>
</adsm:window>
