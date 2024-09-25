<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterControleCargasAction" >

	<adsm:form action="/carregamento/manterControleCargas">

		<adsm:hidden property="idControleCarga" />
		<adsm:hidden property="blPermiteAlterar" serializable="false" />

		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="28%" width="72%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="nmEmpresaAnterior" label="empresaCarregamentoAnterior" size="50" maxLength="60" 
					  labelWidth="28%" width="72%" required="true" />

		<adsm:textbox dataType="text" property="nrTelefoneEmpresa" label="telefoneEmpresa" size="20" maxLength="20" 
					  labelWidth="28%" width="72%" required="true"/>

		<adsm:textbox dataType="text" property="nmResponsavelEmpresa" label="responsavelEmpresa" size="50" maxLength="60" 
					  labelWidth="28%" width="72%" required="true"/>

		<adsm:checkbox property="blPertenceProjCaminhoneiro" label="pertenceProjetoCaminhoneiro" labelWidth="28%" width="72%" />
		<adsm:buttonBar>
			<adsm:button id="botaoSalvar" caption="salvar" onclick="salvar_onClick(this.form);" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

function initWindow(eventObj) {
	if (eventObj.name == "tab_load") {
		resetValue(this.document.forms[0]);
	}
	populaDadosMaster();
	if (getElementValue("blPermiteAlterar") == "true")
		setDisabled("botaoSalvar", false);
	else
		setDisabled("botaoSalvar", true);

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

function populaDadosMaster() {
    var tabDet = getTabGroup(this.document).getTab("cad");
    setElementValue("idControleCarga", tabDet.getFormProperty("idControleCarga"));
    setElementValue("filialByIdFilialOrigem.sgFilial", tabDet.getFormProperty("filialByIdFilialOrigem.sgFilial"));
    setElementValue("nrControleCarga", tabDet.getFormProperty("nrControleCarga"));
    setElementValue("blPermiteAlterar", tabDet.getFormProperty("blPermiteAlterar"));
}

function salvar_onClick() {
	var form = document.forms[0];
	if (!validateForm(form)) {
		return false;
	}
	getTabGroup(this.document).getTab("cad").tabOwnerFrame.window.salvar_onClick();
}
</script>