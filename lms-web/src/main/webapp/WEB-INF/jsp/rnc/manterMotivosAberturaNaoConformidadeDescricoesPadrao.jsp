<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.descricaoPadraoNcService" >
	<adsm:form action="/rnc/manterMotivosAberturaNaoConformidade" idProperty="idDescricaoPadraoNc">
		
		<adsm:textbox dataType="text" property="motivoAberturaNc.dsMotivoAbertura" label="motivoAberturaNaoConformidade" size="40" 
			maxLength="30" labelWidth="23%" width="77%" disabled="true" serializable="false" cellStyle="vertical-align:bottom" />

		<adsm:hidden property="motivoAberturaNc.idMotivoAberturaNc" />
		
		<adsm:textarea property="dsPadraoNc" label="descricaoPadrao" columns="85" rows="3" maxLength="200" labelWidth="23%" width="77%" required="true" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="23%" width="77%" required="true" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarDescricaoPadrao" id="salvarDescricaoPadrao" />
			<adsm:newButton caption="limpar" id="novaDescricaoPadrao" />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid onRowClick="populaForm" property="descricoesPadrao" idProperty="idDescricaoPadraoNc" defaultOrder="dsPadraoNc:asc" selectionMode="check" autoSearch="false">
		<adsm:gridColumn property="dsPadraoNc" title="descricaoPadrao" width="90%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDescricaoPadrao"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
var motivoAberturaNc = new Array();

function initWindow(eventObj) {
	if (eventObj.name == "removeButton_grid" || eventObj.name == "newButton_click"){
		novaDescricaoPadrao();
	}
	else 
	if (eventObj.name == "tab_click") {
		preparaDescricaoPadrao();
	}
	else
	if (eventObj.name == "storeButton") {
		populaGrid();
	}
}

function preparaDescricaoPadrao() {
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");
	
	var idMotivoAberturaNc = tabDet.getFormProperty("idMotivoAberturaNc");
	
	if (idMotivoAberturaNc != undefined && idMotivoAberturaNc != '') {
		var frame = parent.document.frames["cad_iframe"];
		var dsMotivoAbertura = tabDet.getFormProperty("dsMotivoAbertura");

		setElementValue("motivoAberturaNc.dsMotivoAbertura", dsMotivoAbertura);
		setElementValue("motivoAberturaNc.idMotivoAberturaNc", idMotivoAberturaNc);

		setNestedBeanPropertyValue(motivoAberturaNc, "dsMotivoAbertura", dsMotivoAbertura);
		setNestedBeanPropertyValue(motivoAberturaNc, "idMotivoAberturaNc", idMotivoAberturaNc);
		populaGrid();
		novaDescricaoPadrao();
	}
}
	
function populaGrid() {
	descricoesPadraoGridDef.executeSearch(
		{
			motivoAberturaNc:{idMotivoAberturaNc:getElementValue("motivoAberturaNc.idMotivoAberturaNc")}
		}, true);
}

function populaForm(valor) {
	onDataLoad(valor);
	populaDadosMaster();
	return false;
}

function novaDescricaoPadrao() {
	populaDadosMaster();
	resetValue("dsPadraoNc");
	setDefaultFieldsValues();
}

function populaDadosMaster() {
	setElementValue("motivoAberturaNc.idMotivoAberturaNc", getNestedBeanPropertyValue(motivoAberturaNc, "idMotivoAberturaNc"));
	setElementValue("motivoAberturaNc.dsMotivoAbertura", getNestedBeanPropertyValue(motivoAberturaNc, "dsMotivoAbertura"));
}
</script>