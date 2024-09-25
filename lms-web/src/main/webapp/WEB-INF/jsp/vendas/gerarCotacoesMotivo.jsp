<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	title="branco"
	service="lms.vendas.gerarCotacoesAction"
	onPageLoadCallBack="myPageLoad">

	<adsm:form
		action="/vendas/gerarCotacoesMotivo">

		<adsm:section
			caption="reprovarCotacao"
			width="74"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textarea
			maxLength="300"
			property="dsMotivo"
			required="true"
			label="motivo"
			columns="90"
			rows="5"
			labelStyle="border:none"
			labelWidth="7%"
			width="84%"/>

		<adsm:hidden
			property="idCotacao"/>

		<adsm:buttonBar>
			<adsm:storeButton caption="salvar" id="salvarButton"
				service="lms.vendas.gerarCotacoesAction.reprovarCotacao" 
				callbackProperty="reprovarCotacao"/>
			<adsm:button caption="fechar" id="closeButton" disabled="false" 
				buttonType="closeButton" onclick="self.close()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">

	function myPageLoad_cb()  {
		var u = new URL(parent.location.href);
   		var idCotacao = u.parameters["idCotacao"]; 
   		setElementValue("idCotacao", idCotacao);
   		var dsMotivo = u.parameters["dsMotivo"]; 
   		
   		var tpSituacao = dialogArguments ? dialogArguments.tpSituacao : '';
   		
   		if(idCotacao && idCotacao != "" && tpSituacao == 'P'){
   			setElementValue("dsMotivo", dsMotivo);
   			setDisabled(document, true);
   			setDisabled("closeButton", false);
   			setFocus(document.getElementById("closeButton"), false);
   		} else {
			setDisabled(document, false);
		}
	}

	function reprovarCotacao_cb(data, erros){
		if(erros) {
			alert(erros);
			return false;
		}
		window.returnValue = data;
		window.close();
	}
</script>	