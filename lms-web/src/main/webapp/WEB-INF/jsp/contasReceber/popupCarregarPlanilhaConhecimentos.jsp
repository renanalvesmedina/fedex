<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	service="lms.contasreceber.manterConhecimentosSeremTransferidosAction"
	onPageLoad="myPageLoad">
	<script>
		function myPageLoad(d, e, o, x) {

		}
		function myPageLoad_cb(d, e, o, x) {
			/*var u = new URL(parent.location.href);
			
			onPageLoad_cb(d,e,o,x);*/

			//var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findHistoricoMensagens", "findMensagens",{idFatura:idFatura});
			//xmit({serviceDataObjects:[sdo]});
		}

		function cancela() {
			window.close();
		}
	</script>
	<adsm:form action="/contasReceber/manterConhecimentosSeremTransferidos"
		idProperty="idBloqueioMotoristaProp">

		<adsm:textbox property="arquivo" dataType="file" label="arquivo"
			labelWidth="11%" width="10%" size="52" serializable="true" />

		<adsm:label key="branco" width="1%" />

		<adsm:textarea width="84%" columns="74" rows="6" maxLength="50000"
			property="obEvento" label="observacao" labelWidth="10%" />


		<adsm:buttonBar>
			<adsm:storeButton caption="ok" id="btnOk"
				callbackProperty="storeItem"
				service="lms.contasreceber.manterConhecimentosSeremTransferidosAction.storeGeraAnalisePlanilha" />

			<adsm:button caption="cancelar" id="cancelButton"
				buttonType="cancelarButton" onclick="return cancela();"
				boxWidth="63" disabled="false" />

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function storeItem_cb(data, error) {
		alert("LMS-36278 - Processamento concluído");
		setElementValue("obEvento", data['erro']);
	}
	document.getElementById("arquivo_browse").style.display = 'none';
</script>