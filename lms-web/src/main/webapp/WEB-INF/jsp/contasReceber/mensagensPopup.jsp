<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterFaturasAction" onPageLoadCallBack="myOnPageLoad">
	<script>
		function myOnPageLoad_cb(d, e, o, x) {
			mensagemGridDef.resetGrid();
			var u = new URL(parent.location.href);
			setElementValue("idFatura", u.parameters["idFatura"]);
			var idFatura = u.parameters["idFatura"];
			var data = new Array();
			setNestedBeanPropertyValue(data, "idFatura", idFatura);
			mensagemGridDef.executeSearch(data);
		}

		function myOnRowClick() {
			return false;
		}
	</script>

	<adsm:grid property="mensagem" title="mensagens"
		onRowClick="myOnRowClick();" idProperty="idMonitoramentoMensagem"
		selectionMode="none"
		service="lms.contasreceber.manterFaturasAction.findHistoricoMensagens"
		showRowIndex="false" autoAddNew="false" gridHeight="70"
		showGotoBox="false" showPagging="false" showTotalPageCount="false"
		scrollBars="vertical">

		<adsm:gridColumn title="tpMensagem" property="tpModeloMensagem"
			width="9%" dataType="text" />
		<adsm:gridColumn title="tpEnvio" property="tpEnvioMensagem" width="9%"
			dataType="text" />
		<adsm:gridColumn title="para" property="para" width="10%"
			dataType="text" />
		<adsm:gridColumn title="inclusao" property="dhInclusao" width="9%"
			dataType="text" />
		<adsm:gridColumn title="dataProcessamento" property="dhProcessamento"
			width="9%" dataType="text" />
			
			
		<adsm:gridColumn title="envio" property="dhEnvio" width="9%"
			dataType="text" />
		<adsm:gridColumn title="recebimento" property="dhRecebimento"
			width="9%" dataType="text" />
		<adsm:gridColumn title="devolucao" property="dhDevolucao" width="9%"
			dataType="text" />
		<adsm:gridColumn title="erro" property="dhErro" width="9%"
			dataType="text" />
		
		<adsm:gridColumn property="idMonitoramentoMensagemEvento" title="eventos" width="80" image="/images/popup.gif" openPopup="true" link="contasReceber/manterFaturas.do?cmd=mensagensEventosPopup" align="center" linkIdProperty="idMonitoramentoMensagemEvento"/>
		<adsm:gridColumn property="idMonitoramentoMensagemConteudo" title="conteudo" width="80" image="/images/popup.gif" openPopup="true" link="contasReceber/manterFaturas.do?cmd=mensagensConteudoPopup" align="center" linkIdProperty="idMonitoramentoMensagemConteudo"/>

	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" onclick="self.close();" disabled="false" />
	</adsm:buttonBar>
	<adsm:form action="/contasReceber/manterFaturas" idProperty="idFatura">
		<adsm:hidden property="idFatura" />
	</adsm:form>
</adsm:window>
