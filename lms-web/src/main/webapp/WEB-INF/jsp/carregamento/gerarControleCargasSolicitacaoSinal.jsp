<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.gerarControleCargasAction" >

	<adsm:form action="/carregamento/gerarControleCargas">
		<adsm:masterLink idProperty="idControleCarga" showSaveAll="true">
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" />
		</adsm:masterLink>

		<adsm:textbox dataType="text" property="nmEmpresaAnterior" label="empresaCarregamentoAnterior" size="50" maxLength="60" 
					  labelWidth="28%" width="72%" required="true" />

		<adsm:textbox dataType="text" property="nrTelefoneEmpresa" label="telefoneEmpresa" size="20" maxLength="20" 
					  labelWidth="28%" width="72%" required="true"/>

		<adsm:textbox dataType="text" property="nmResponsavelEmpresa" label="responsavelEmpresa" size="50" maxLength="60" 
					  labelWidth="28%" width="72%" required="true"/>

		<adsm:checkbox property="blPertenceProjCaminhoneiro" label="pertenceProjetoCaminhoneiro" labelWidth="28%" width="72%" />
		<adsm:buttonBar />
	</adsm:form>
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		var idControleCarga = getTabGroup(this.document).getTab("cad").getFormProperty("idControleCarga");
		if (idControleCarga == undefined || idControleCarga == "" || idControleCarga == "-1") {
			setDisabled("nmEmpresaAnterior", false);
			setDisabled("nrTelefoneEmpresa", false);
			setDisabled("nmResponsavelEmpresa", false);
			setDisabled("blPertenceProjCaminhoneiro", false);
		}
	}
	setFocusOnFirstFocusableField();
}

function validaCamposSolicSinal() {
	exigeCamposObrigatorios("true");
}

function exigeCamposObrigatorios(valor) {
	document.getElementById("nmEmpresaAnterior").required = valor;
	document.getElementById("nrTelefoneEmpresa").required = valor;
	document.getElementById("nmResponsavelEmpresa").required = valor;
}
</script>