<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterCarteiraVendasAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00061"></adsm:include>
	</adsm:i18nLabels>

	<adsm:form 	action="/vendas/manterCarteiraVendas" >
	 	<adsm:textbox property="usuario.nrMatricula" label="vendedor" size="16" maxLength="16" dataType="text"
	 		width="43%" labelWidth="17%" disabled="true">
	 		<adsm:textbox property="usuario.nmUsuario" size="30" maxLength="45" dataType="text" disabled="true" serializable="false"/>
	 	</adsm:textbox>
	 	
	 	<adsm:textbox label="numeroLote" property="idCarteiraVendas" dataType="text" width="25%" labelWidth="15%"
	 		disabled="true"/>
	</adsm:form> 	
 	<adsm:grid property="carteiraVendasCliente" idProperty="idCarteiraVendasCliente" onDataLoadCallBack="gridCallBack"
 		service="lms.vendas.manterCarteiraVendasAction.findClientesCarteira"
 		showPagging="false"  scrollBars="both"		
 		onRowClick="onRowClick();"
 		gridHeight="300"
 		onchange="onChangeField"
 		onValidate="validateFields"
 		autoSearch="false"
 		>
 		<adsm:editColumn field="textBox" property="nrIdentificacao" maxLength="14" title="cliente" 
 			width="100" dataType="integer" required="true"/>
 		<adsm:editColumn field="hidden" property="idCliente" title="" width="1" dataType="integer"/>
		<adsm:editColumn field="textbox" property="nmPessoa" title="" width="200" dataType="text" disabled="true"/>
		<adsm:editColumn field="textbox" property="tpCliente" domain="DM_TIPO_CLIENTE" title="tipoCliente"  width="100" disabled="true"/>
		
		<adsm:editColumn field="combobox" property="tpModal" domain="DM_MODAL" title="modal" width="100" />
 		<adsm:editColumn field="combobox" property="tpAbrangencia" domain="DM_ABRANGENCIA" title="abrangencia" width="100" disabled="true" value="N"/>
 		<adsm:editColumn field="combobox" property="tpComissao" domain="DM_TIPO_COMISSAO" title="tipoComissao" width="100"/>
 		<adsm:editColumn field="textbox" property="dtPromotor" title="data" dataType="JTDate" width="100"/>
 		<adsm:editColumn field="textbox" property="dtCliente" title="dataInicioCliente" dataType="JTDate" width="100"/>
		<adsm:buttonBar>
			<adsm:button caption="adicionar" id="btnAdicionar" onclick="adicionaCliente()" disabled="false"/>
			<adsm:button caption="salvar" id="btnSalvar" onclick="onClickSalvar()" disabled="true"/>
 			<adsm:button caption="excluir" id="btnRemove" onclick="onclickExcluir()"/>
 		</adsm:buttonBar>
 	</adsm:grid>
	
</adsm:window>
<script type="text/javascript">

var currentRow = 0;

function adicionaCliente(){
	if (validaInsercao()){
		document.getElementById('carteiraVendasCliente.noResultMessage').innerText="";
		carteiraVendasClienteGridDef.insertRow();
		disableFields(carteiraVendasClienteGridDef.gridState.rowCount);
		document.getElementById("btnSalvar").disabled=false;
	}
}

function onclickExcluir(){
	var remove = carteiraVendasClienteGridDef.getSelectedIds();
	if(remove.ids.length >0){
		var data = new Object();
		data.ids = remove.ids;
		var sdo = createServiceDataObject("lms.vendas.manterCarteiraVendasAction.removeClientes","removeClientes",data);
		xmit({serviceDataObjects:[sdo]});
	}
}

function removeClientes_cb(data,error){
	if(error!=undefined){
		alert(error);
	}else{
		var searchData = new Array();
		setNestedBeanPropertyValue(searchData,"idCarteiraVendas", getElementValue("idCarteiraVendas"));
		carteiraVendasClienteGridDef.executeSearch(searchData);
	}
}

function onClickSalvar(){
	if ( validateGrid() ){
	var data = new Array();	
	merge(data, buildFormBeanFromForm(document.forms[0]));
	merge(data, buildFormBeanFromForm(document.forms[1]));

	var sdo = createServiceDataObject(
			"lms.vendas.manterCarteiraVendasAction.storeClientes", 
			"storeClientes", data);
	xmit({serviceDataObjects:[sdo]});
}
}

function storeClientes_cb(data,error){
	if (error != undefined){
		alert(error);
	}else{
		showSuccessMessage();
	}
}

function gridCallBack_cb(data,error){
	if (error != undefined){
		alert(error);
	}

	var rowCount = carteiraVendasClienteGridDef.gridState.rowCount;
	for (rowIndex = 1; rowIndex <= rowCount; rowIndex++){
		disableFields(rowIndex);
	}
	if (rowCount == 0){
		document.getElementById("btnSalvar").disabled=true;
	}else{
		document.getElementById("btnSalvar").disabled=false;
}
}

function validateGrid(){
	var rowCount = carteiraVendasClienteGridDef.gridState.rowCount;
	for (row = 1; row <= rowCount; row++){
		var idCliente = carteiraVendasClienteGridDef.getCellObject(row-1,"idCliente").value;
		for (var columnName in carteiraVendasClienteGridDef.columns){
			var value = carteiraVendasClienteGridDef.getCellObject(row-1,columnName).value;		
			if (columnName != "idCliente" && columnName != "idCarteiraVendasCliente" && value.length == 0 && idCliente.length > 0){
				alert("Preenchimento inválido !");
				setFocus(carteiraVendasClienteGridDef.getCellObject(row-1,columnName));
				return false;
			}
		}
	}
	return true;
}

function onRowClick(){
	return false;
}

function isIn(val, arr){
	for(var k in arr) if(arr[k] == val) return true;
	return false;
}

function validateFields(rowIndex, columnName, objCell){

	if (columnName == "tpModal"){
		if ( (objCell.value == null) || (objCell.value == "") ){
			alert("Preenchimento inválido !");
			return false;
		}
	}

	if (columnName == "tpComissao"){
		if ( (objCell.value == null) || (objCell.value == "") ){
			alert("Preenchimento inválido !");
			return false;
		}
	}

	if (rowIndex != carteiraVendasClienteGridDef.gridState.rowCount) return;
	
	if (columnName == "dtPromotor" && validaInsercao()){
		carteiraVendasClienteGridDef.insertRow();
	}
}

function validaInsercao(){
	var row = carteiraVendasClienteGridDef.gridState.rowCount;
	if(row == 0) return true;
	for (var columnName in carteiraVendasClienteGridDef.columns){
		var value = carteiraVendasClienteGridDef.getCellObject(row-1,columnName).value;		
		if (columnName != "idCarteiraVendasCliente" &&
			value.length == 0){
			return false;
		}
	}
	return true;
}

function onChangeField(columnName, rowIndex) {
	//Chama a lookup de cliente
	if(isIn(columnName, ["nrIdentificacao"])) {
		var data = new Object();
		data.rowIndex = rowIndex;
		data.nrIdentificacao = carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"nrIdentificacao").value;			
		if (data.nrIdentificacao.length > 0){
			var sdo = createServiceDataObject("lms.vendas.manterCarteiraVendasAction.findClienteByIdentificacao","findClienteByIdentificacao",data);
			xmit({serviceDataObjects:[sdo]});
		}else clearFieldsCliente(rowIndex);
	}

	if (isIn(columnName,["tpModal"])){
		setElementValue(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"tpAbrangencia"),"N");
	}
	
	if (isIn(columnName,["dtPromotor"])){
		var data = new Object();
		currentRow = rowIndex;
		var dtPromotor = carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"dtPromotor").value;
		data.dtPromotor = formatDate(dtPromotor,"dd/MM/yyyy","yyyy-MM-dd");		
		if ( (data.dtPromotor != null) && (data.dtPromotor.length == 10) ){
		var sdo = createServiceDataObject("lms.vendas.manterCarteiraVendasAction.validaCliente","validaCliente",data);
		xmit({serviceDataObjects:[sdo]});
	}
	}
	return true;
}

function validaCliente_cb(data,error){
	if (error != undefined){
		alert(error);
		carteiraVendasClienteGridDef.getCellObject(currentRow-1,"dtPromotor").value = "";
		setFocus(carteiraVendasClienteGridDef.getCellObject(data.rowIndex-1,"dtPromotor"));
	}else{
		adicionaCliente();
	}
}

function clearFieldsCliente(rowIndex){
	setElementValue(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"nrIdentificacao"),"");
	setElementValue(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"nmPessoa"),"");
	setElementValue(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"tpCliente"),"");
	setElementValue(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"idCliente"),"");
}

function disableFields(rowIndex){
	setDisabled(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"nmPessoa"),true);
	setDisabled(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"tpCliente"),true);
	setDisabled(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"tpAbrangencia"),true);
	setDisabled(carteiraVendasClienteGridDef.getCellObject(rowIndex-1,"dtCliente"),true);
}

function findClienteByIdentificacao_cb(data,error){
	if(data.exception.length >0) {
		clearFieldsCliente(data.rowIndex);
		alert(i18NLabel.getLabel(data.exception));
		setFocus(carteiraVendasClienteGridDef.getCellObject(data.rowIndex-1,"nrIdentificacao"));
		return;
	}
	if (data.idCliente != undefined){
		carteiraVendasClienteGridDef.getCellObject(data.rowIndex-1,"idCliente").value = data.idCliente;
		carteiraVendasClienteGridDef.getCellObject(data.rowIndex-1,"nmPessoa").value = data.nmPessoa;
		carteiraVendasClienteGridDef.getCellObject(data.rowIndex-1,"tpCliente").value = data.tpCliente;
		carteiraVendasClienteGridDef.getCellObject(data.rowIndex-1,"dtCliente").value = formatDate(data.dtCliente,"yyyy-MM-dd","dd/MM/yyyy");
	}
}

function initWindow(eventObj){
	if (eventObj.name == "tab_click"){
		var docCad = getTabGroup(this.document).getTab("cad").getDocument();
		
		//se está inserindo... cria uma linha para a digitação
		if (docCad.getElementById("idCarteiraVendas").value != undefined ||
			docCad.getElementById("idCarteiraVendas").value != ""){
			carteiraVendasClienteGridDef.insertRow();
		}
		
		setElementValue("idCarteiraVendas", docCad.getElementById("idCarteiraVendas").value);
		setElementValue("usuario.nrMatricula", docCad.getElementById("usuario.nrMatricula").value);
		setElementValue("usuario.nmUsuario", docCad.getElementById("usuario.nmUsuario").value);

		if (getElementValue("idCarteiraVendas") != null && getElementValue("idCarteiraVendas").length > 0){
			var searchData = new Array();
			setNestedBeanPropertyValue(searchData,"idCarteiraVendas", getElementValue("idCarteiraVendas"));
			carteiraVendasClienteGridDef.executeSearch(searchData);
		}
	}
}

</script>