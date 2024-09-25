<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.rnc.manterOcorrenciasNaoConformidadeAction" >
	<adsm:form action="/rnc/manterOcorrenciasNaoConformidade" idProperty="idFotoOcorrencia"
			   service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findByIdFotoOcorrencia" 
			   onDataLoadCallBack="retornoCarregaDados" >

		<adsm:hidden property="foto.idFoto" />

		<adsm:textbox dataType="text" property="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" size="3" maxLength="3"
					  label="naoConformidade" width="85%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade" 
						  size="9" maxLength="8" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="integer" property="ocorrenciaNaoConformidade.nrOcorrenciaNc" label="numeroOcorrencia" size="3" 
					  maxLength="2" width="85%" disabled="true" serializable="false" />
		<adsm:hidden property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade" />

		<adsm:textbox dataType="text"
					  property="dsFotoOcorrencia"
					  label="descricao" size="50"
					  maxLength="80"
					  width="85%"
					  required="true" />
		
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
			<adsm:storeButton caption="salvar" id="botaoSalvar" service="lms.rnc.manterOcorrenciasNaoConformidadeAction.storeFotoOcorrencia" />
			<adsm:newButton caption="limpar" id="botaoLimpar" />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid onRowClick="populaForm" property="fotos" idProperty="idFotoOcorrencia" defaultOrder="dsFotoOcorrencia:asc"
			   selectionMode="check" autoSearch="false"  onPopulateRow="povoaLinhas" rows="11"
			   service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findPaginatedFotoOcorrencia"
			   rowCountService="lms.rnc.manterOcorrenciasNaoConformidadeAction.getRowCountFotoOcorrencia" >
		<adsm:gridColumn title="descricao" property="dsFotoOcorrencia" width="90%" />
		<adsm:gridColumn title="anexoFoto" property="foto.idFoto" width="10%" image="/images/camera.gif" align="center" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirFoto" id="botaoExcluir" service="lms.rnc.manterOcorrenciasNaoConformidadeAction.removeByIdsFotoOcorrencia" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>


<script>

function povoaLinhas(tr, data) {

	fakeDiv = document.createElement("<DIV></DIV>");
	fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:showPicture('" + data.foto.idFoto + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/camera.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
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
		var sgFilial = tabDet.getFormProperty("naoConformidade.filial.sgFilial");
		var nrOcorrenciaNc = tabDet.getFormProperty("nrOcorrenciaNc");

		setElementValue("ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade", nrNaoConformidade);
		setElementValue("ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial", sgFilial);
		setElementValue("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", idOcorrenciaNaoConformidade);
		setElementValue("ocorrenciaNaoConformidade.nrOcorrenciaNc", nrOcorrenciaNc);

		setNestedBeanPropertyValue(ocorrenciaNaoConformidade, "nrNaoConformidade", nrNaoConformidade);
		setNestedBeanPropertyValue(ocorrenciaNaoConformidade, "sgFilial", sgFilial);
		setNestedBeanPropertyValue(ocorrenciaNaoConformidade, "idOcorrenciaNaoConformidade", idOcorrenciaNaoConformidade);
		setNestedBeanPropertyValue(ocorrenciaNaoConformidade, "nrOcorrenciaNc", nrOcorrenciaNc);
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

	setElementValue("ocorrenciaNaoConformidade.nrOcorrenciaNc", 
					getNestedBeanPropertyValue(ocorrenciaNaoConformidade, "nrOcorrenciaNc"));
}


function retornoCarregaDados_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	onDataLoad_cb(data, error);
}
</script>