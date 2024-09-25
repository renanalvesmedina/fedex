<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.rnc.abrirRNCAction" >
	<adsm:form action="/rnc/abrirRNC" idProperty="idFotoOcorrencia" service="lms.rnc.abrirRNCAction.findByIdFotoOcorrencia" >

		<adsm:textbox dataType="text" property="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" size="3" maxLength="3" 
					  label="naoConformidade" width="85%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade" 
						  size="9" maxLength="8" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:hidden property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade" />
		<adsm:hidden property="foto.idFoto" />

		<adsm:textbox dataType="text" property="dsFotoOcorrencia" label="descricao" size="50"  maxLength="80" width="85%" required="true" />
		
		<adsm:combobox label="tipoAnexo" property="tpAnexo" domain="DM_ANEXO_OCORRENCIA" required="true">
			<adsm:propertyMapping relatedProperty="tpAnexo.valor"
				modelProperty="value" />
			<adsm:propertyMapping relatedProperty="tpAnexo.descricao"
				modelProperty="description" />
			<adsm:propertyMapping relatedProperty="tpAnexo.id"
				modelProperty="id" />
		</adsm:combobox>		

		<adsm:textbox dataType="picture"
					  property="foto.foto"
					  label="anexoFoto"
					  blobColumnName="FOTO"
					  tableName="FOTO"
					  primaryKeyValueProperty="foto.idFoto"
					  primaryKeyColumnName="ID_FOTO"
					  size="50"
					  width="85%"
					  required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar" service="lms.rnc.abrirRNCAction.storeFotoOcorrencia" callbackProperty="storeFoto"/>
			<adsm:newButton caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid onRowClick="populaForm" property="fotos" idProperty="idFotoOcorrencia" defaultOrder="dsFotoOcorrencia:asc"
			   selectionMode="check" autoSearch="false"  onPopulateRow="povoaLinhas"
			   rows="12"
			   service="lms.rnc.abrirRNCAction.findPaginatedFotoOcorrencia" 
			   rowCountService="lms.rnc.abrirRNCAction.getRowCountFotoOcorrencia" >
		<adsm:gridColumn title="descricao" property="dsFotoOcorrencia" width="90%" />
		<adsm:gridColumn title="anexoFoto" property="foto.idFoto" width="10%" image="/images/camera.gif" align="center" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirFoto" service="lms.rnc.abrirRNCAction.removeByIdsFotoOcorrencia" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>


<script>

function povoaLinhas(tr, data) {
	fakeDiv = document.createElement("<DIV></DIV>");
	fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:showPicture('" + data.foto.idFoto  + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/camera.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
	tr.children[2].innerHTML = fakeDiv.children[0].children[0].children[0].children[0].innerHTML;
}

var ocorrenciaNaoConformidade = new Array();

function initWindow(eventObj) {
	if (eventObj.name == "removeButton_grid" || eventObj.name == "newButton_click"){
		novaFoto();
	}
	else 
	if (eventObj.name == "tab_click") {
		preparaFoto();
	}
	else
	if (eventObj.name == "storeButton") {
		populaGrid();
	}
}

function preparaFoto() {
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
		novaFoto();
	}
}
	
function populaGrid() {
	fotosGridDef.executeSearch(
		{
			ocorrenciaNaoConformidade:{idOcorrenciaNaoConformidade:getElementValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade")}
		}, true);
}

function populaForm(valor) {
	onDataLoad(valor);
	populaDadosMaster();
	return false;
}

function novaFoto() {
	populaDadosMaster();
	resetValue("idFotoOcorrencia");
	resetValue("dsFotoOcorrencia");
	resetValue("foto.foto");
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

function storeFoto_cb(data, error){
	if(error){
		alert(error);
		return false;
	}
	store_cb(data, error);
	novaFoto();
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