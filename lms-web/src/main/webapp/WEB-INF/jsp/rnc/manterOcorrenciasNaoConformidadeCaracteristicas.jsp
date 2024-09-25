<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.manterOcorrenciasNaoConformidadeAction" >
	<adsm:form action="/rnc/abrirRNC" idProperty="idCaractProdutoOcorrencia" 
			   service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findByIdCaractProdutoOcorrencia" 
			   onDataLoadCallBack="retornoCarregaDados" >

		<adsm:textbox dataType="text" property="ocorrenciaNaoConformidade.naoConformidade.filial.sgFilial" size="3" maxLength="3" 
					  label="naoConformidade" width="85%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="ocorrenciaNaoConformidade.naoConformidade.nrNaoConformidade" 
						  size="9" maxLength="8" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:hidden property="ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade" />
		
		<adsm:textbox dataType="integer" property="ocorrenciaNaoConformidade.nrOcorrenciaNc" label="numeroOcorrencia" size="3" 
					  maxLength="2" width="85%" disabled="true" serializable="false" />
		<adsm:hidden property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade" />

		<adsm:combobox property="caracteristicaProduto.idCaracteristicaProduto" 
			optionProperty="idCaracteristicaProduto" optionLabelProperty="dsCaracteristicaProduto" 
			service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findCaracteristicaProduto" 
			label="caracteristicaProduto" onlyActiveValues="true"
			width="85%" required="true" cellStyle="vertical-align:bottom" />

		<adsm:textbox dataType="text" property="dsCaractProdutoOcorrencia" label="descricao" size="60" maxLength="60" 
					  width="85%" required="true" />
					  
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarCaracteristica" id="botaoSalvarCaracteristicas" service="lms.rnc.manterOcorrenciasNaoConformidadeAction.storeCaractProdutoOcorrencia" />
			<adsm:newButton caption="limpar" id="botaoLimpar" />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="caracteristicas" idProperty="idCaractProdutoOcorrencia" selectionMode="check" 
			   onRowClick="populaForm" 
			   autoSearch="false" defaultOrder="caracteristicaProduto_.dsCaracteristicaProduto:asc,dsCaractProdutoOcorrencia:asc" 
			   service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findPaginatedCaractProdutoOcorrencia"
			   rowCountService="lms.rnc.manterOcorrenciasNaoConformidadeAction.getRowCountCaractProdutoOcorrencia" >
		<adsm:gridColumn property="caracteristicaProduto.dsCaracteristicaProduto" title="caracteristica" width="40%" />
		<adsm:gridColumn property="dsCaractProdutoOcorrencia" title="descricao" width="60%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirCaracteristica" id="botaoExcluir" service="lms.rnc.manterOcorrenciasNaoConformidadeAction.removeByIdsCaractProdutoOcorrencia" />
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