<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.abrirRNCAction" >
	<adsm:form action="/rnc/abrirRNC" idProperty="idCaractProdutoOcorrencia" service="lms.rnc.abrirRNCAction.findByIdCaractProdutoOcorrencia" >

		<adsm:textbox dataType="text" property="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" size="3" maxLength="3" 
					  label="naoConformidade" width="85%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade" 
						  size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:hidden property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade" />

		<adsm:combobox property="caracteristicaProduto.idCaracteristicaProduto" 
			optionProperty="idCaracteristicaProduto" optionLabelProperty="dsCaracteristicaProduto" 
			service="lms.rnc.abrirRNCAction.findCaracteristicaProduto" 
			label="caracteristicaProduto" onlyActiveValues="true"
			width="85%" required="true" cellStyle="vertical-align:bottom" />

		<adsm:textbox dataType="text" property="dsCaractProdutoOcorrencia" label="descricao" size="60" maxLength="60" 
					  width="85%" required="true" />
					  
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarCaracteristica" service="lms.rnc.abrirRNCAction.storeCaractProdutoOcorrencia" callbackProperty="storeCaracteristica"/>
			<adsm:newButton caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid onRowClick="populaForm" property="caracteristicas" idProperty="idCaractProdutoOcorrencia" selectionMode="check" 
			   autoSearch="false" defaultOrder="caracteristicaProduto_.dsCaracteristicaProduto:asc,dsCaractProdutoOcorrencia:asc" 
			   rows="11"
			   service="lms.rnc.abrirRNCAction.findPaginatedCaractProdutoOcorrencia"
			   rowCountService="lms.rnc.abrirRNCAction.getRowCountCaractProdutoOcorrencia">
		<adsm:gridColumn property="caracteristicaProduto.dsCaracteristicaProduto" title="caracteristica" width="40%" />
		<adsm:gridColumn property="dsCaractProdutoOcorrencia" title="descricao" width="60%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirCaracteristica" service="lms.rnc.abrirRNCAction.removeByIdsCaractProdutoOcorrencia" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>


<script>
var ocorrenciaNaoConformidade= new Array();

function initWindow(eventObj) {
	if (eventObj.name == "removeButton_grid" || eventObj.name == "newButton_click"){
		novaCaracteristica();
	}
	else 
	if (eventObj.name == "tab_click") {
		preparaCaracteristica();
	}
	else
	if (eventObj.name == "storeButton") {
		populaGrid();
	}
}

function preparaCaracteristica() {
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");
	
	var idOcorrenciaNaoConformidade = tabDet.getFormProperty("idOcorrenciaNaoConformidade");
	
	if (idOcorrenciaNaoConformidade != undefined && idOcorrenciaNaoConformidade != '') {
		var frame = parent.document.frames["cad_iframe"];

		var nrNaoConformidade = tabDet.getFormProperty("naoConformidade.nrNaoConformidade");
		var sgFilial = tabDet.getFormProperty("naoConformidade.filial.sgFilial");;

		setElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", nrNaoConformidade);
		setElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial", sgFilial);
		setElementValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", idOcorrenciaNaoConformidade);

		setNestedBeanPropertyValue(ocorrenciaNaoConformidade, "nrNaoConformidade", nrNaoConformidade);
		setNestedBeanPropertyValue(ocorrenciaNaoConformidade, "sgFilial", sgFilial);
		setNestedBeanPropertyValue(ocorrenciaNaoConformidade, "idOcorrenciaNaoConformidade", idOcorrenciaNaoConformidade);
		populaGrid();
		novaCaracteristica();
	}
}
	
function populaGrid() {
	caracteristicasGridDef.executeSearch(
		{
			ocorrenciaNaoConformidade:{idOcorrenciaNaoConformidade:getElementValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade")}
		}, true);
}

function populaForm(valor) {
	onDataLoad(valor);
	populaDadosMaster();
	return false;
}

function novaCaracteristica() {
	populaDadosMaster();
	resetValue("idCaractProdutoOcorrencia");
	resetValue("caracteristicaProduto.idCaracteristicaProduto");
	resetValue("dsCaractProdutoOcorrencia");
	setDefaultFieldsValues();
}

function populaDadosMaster() {
	setElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", 
					getNestedBeanPropertyValue(ocorrenciaNaoConformidade, "nrNaoConformidade"));

	setElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial", 
					getNestedBeanPropertyValue(ocorrenciaNaoConformidade, "sgFilial"));

	setElementValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", 
					getNestedBeanPropertyValue(ocorrenciaNaoConformidade, "idOcorrenciaNaoConformidade"));

	format(document.getElementById("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade"));
}

function storeCaracteristica_cb(data, error){
	if(error){
		alert(error);
		return false;
	}
	store_cb(data, error);
	novaCaracteristica();
	setFocusOnFirstFocusableField();
}


function tabClick() {
	var tab = getTabGroup(this.document).selectedTab;
	if (tab.hasChanged()) {
		var retorno = confirm(erChangeTabDiscardChanges);
		if (retorno) {
			tab.changed = false;
		}
		return retorno; 
	}
}
</script>