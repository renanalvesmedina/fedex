<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="exigenciaDetalhe" service="lms.coleta.programacaoColetasVeiculosAction" onPageLoad="carregaPagina" >
	<adsm:form action="/coleta/programacaoColetasVeiculos" idProperty="idExigenciaGerRisco" 
		service="lms.coleta.programacaoColetasVeiculosAction.findByIdExigenciaGerRisco" >
	
		<adsm:section caption="exigenciaDetalhe" width="72%" />
		
		<adsm:label key="espacoBranco" width="1%" style="border:none;"/>
		<adsm:textarea property="dsCompleta" maxLength="1000" columns="101" rows="7" labelWidth="0%" width="100%" disabled="true" />

		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script>
function povoaForm() {
	onDataLoad( getElementValue('idExigenciaGerRisco') );
	setDisabled(document.getElementById("botaoFechar"), false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}
</script>