<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	service="lms.configuracoes.manterDadosInternacionalizadosAction"
	onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterDadosInternacionalizados"
		idProperty="idDadosInternacionalizados">
		<adsm:textarea property="descricaoPortugues" columns="100"
			label="descricaoPortugues" labelWidth="15%" width="720"
			maxLength="2000" rows="16" readonly="true" />
		<adsm:textarea property="descricaoTraduzida" columns="100"
			label="descricaoTraduzida" labelWidth="15%" width="720"
			maxLength="2000" rows="16" />
		<adsm:buttonBar>
			<adsm:button id="close" caption="fechar" onclick="fechar();" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

window.onunload = function() {
	this.returnValue = document.getElementById("descricaoTraduzida").value;
}

function fechar() {
	window.close();
}
function myOnPageLoad_cb(d,e,c,x){
	document.getElementById("descricaoPortugues").value = window.dialogArguments.descricaoPortugues;
	document.getElementById("descricaoTraduzida").value = window.dialogArguments.descricaoTraduzida;
	document.getElementById("descricaoTraduzida").maxChars = window.dialogArguments.tamanhoColuna;
	onPageLoad_cb(d,e,c,x);
	setDisabled("close", false);
	setFocus("descricaoTraduzida");
}

</script>
