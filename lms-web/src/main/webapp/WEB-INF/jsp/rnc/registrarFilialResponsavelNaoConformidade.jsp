<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="registrarFilialResponsavelNaoConformidade" service="lms.rnc.registrarFilialResponsavelNaoConformidadeAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/rnc/registrarFilialResponsavelNaoConformidade" idProperty="idOcorrenciaNaoConformidade" >

		<adsm:combobox property="filial.idFilial" 
					   optionProperty="idFilial" optionLabelProperty="sgFilial" 
					   service="lms.rnc.registrarFilialResponsavelNaoConformidadeAction.findFilialResponsavelNC" 
					   onDataLoadCallBack="retornoCombo"
					   label="registrarFilialResponsavelNaoConformidade" labelWidth="35%" width="65%" required="true" >
			<adsm:propertyMapping modelProperty="idManifesto" criteriaProperty="idManifesto" />
			<adsm:propertyMapping modelProperty="idControleCarga" criteriaProperty="idControleCarga" />
		</adsm:combobox>

		<adsm:hidden property="idManifesto" serializable="false" />
		<adsm:hidden property="idControleCarga" serializable="false" />
					   
		<adsm:buttonBar freeLayout="false" >
			<adsm:button caption="ok" id="botaoOk" onclick="javascript:mostraFilialResponsavel(this.form);" disabled="false" />
			<adsm:button caption="cancelar" id="botaoCancelar" onclick="botaoCancelar_OnClick();" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
function carregaPagina_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	var idManifesto = dialogArguments.window.document.getElementById('manifesto.idManifesto').value;
	if (idManifesto == undefined || idManifesto == '') {
		idManifesto = 0;
	}
	setElementValue('idManifesto', idManifesto);
	
	var idControleCarga = dialogArguments.window.document.getElementById('controleCarga.idControleCarga').value;
	if (idControleCarga == undefined || idControleCarga == '') {
		idControleCarga = 0;
	}
	setElementValue('idControleCarga', idControleCarga);
	
	notifyElementListeners({e:document.getElementById("idManifesto")});
}

function retornoCombo_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return;
	}
	filial_idFilial_cb(data);
	if ( document.getElementById('filial.idFilial').length == 2 ) {
		document.getElementById('filial.idFilial').options[1].selected = true; 
		setDisabled('filial.idFilial', true);
		setFocus(document.getElementById("botaoOk"), true, true);
	}
	else
		setDisabled('filial.idFilial', false);
}

function mostraFilialResponsavel(form) {
	if (!validateForm(form)) {
		return false;
	}
	buscarFilial(getElementValue('filial.idFilial'));
}

function buscarFilial(idFilial) {
	var sdo = createServiceDataObject("lms.rnc.registrarFilialResponsavelNaoConformidadeAction.findByIdFilial", 
			  "resultado_buscarFilial", {idFilial:idFilial});
	xmit({serviceDataObjects:[sdo]});
}

function resultado_buscarFilial_cb(data, error) {
	dialogArguments.window.document.getElementById('filialByIdFilialResponsavel.pessoa.nmFantasia').value = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
	dialogArguments.window.document.getElementById('filialByIdFilialResponsavel.idFilial').value = getElementValue('filial.idFilial');
	var selectedIndex = document.getElementById('filial.idFilial').selectedIndex; 
	var texto = document.getElementById('filial.idFilial').options[selectedIndex].text;
	dialogArguments.window.document.getElementById('filialByIdFilialResponsavel.sgFilial').value = texto;
	window.close();
}

function botaoCancelar_OnClick() {
	dialogArguments.window.document.getElementById('filialByIdFilialResponsavel.pessoa.nmFantasia').value = "";
	dialogArguments.window.document.getElementById('filialByIdFilialResponsavel.idFilial').value = "";
	dialogArguments.window.document.getElementById('filialByIdFilialResponsavel.sgFilial').value = "";
	window.close();
}
</script>