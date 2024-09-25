<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.vendas.gerarCotacoesAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/gerarCotacoesObservacoes">
		<adsm:section caption="observacoes" width="70"/>
		<adsm:label key="branco" style="border:none;" width="1%" />

		<adsm:textarea maxLength="300"  property="obCotacao" columns="100" rows="6" width="99%"/>

		<adsm:buttonBar>
			<adsm:storeButton caption="salvar" id="salvarButton"
				service="lms.vendas.gerarCotacoesAction.storeObservacao" 
				callbackProperty="storeObservacao"/>
			<adsm:button caption="fechar" id="closeButton" disabled="false" 
				buttonType="closeButton" onclick="self.close()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript">
			
			function myPageLoad_cb()  {
				var u = new URL(parent.location.href);
		   		var idCotacao = u.parameters["idCotacao"]; 
		   		if(idCotacao && idCotacao != "") {
		   			setElementValue("obCotacao", u.parameters["ob"]);
		   			setDisabled("salvarButton", true);
		   			setFocus(document.getElementById("closeButton"), false);
		   		} else {
					var sdo = createServiceDataObject("lms.vendas.gerarCotacoesAction.findObservacao", "findObservacao");
					xmit({serviceDataObjects:[sdo]});
					setDisabled("salvarButton", false);
				}
			}
			
			function findObservacao_cb(data){
				if(data)
					setElementValue("obCotacao", data._value);
			}
			
			function storeObservacao_cb(data, erros){
				if(erros)
					alert(erros);
				self.close();	
			}
</script>	