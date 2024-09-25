<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window
	service="lms.configuracoes.manterDadosInternacionalizadosAction"
	onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterDadosInternacionalizados"
		idProperty="idDadosInternacionalizados">
		<adsm:combobox
			service="lms.configuracoes.manterDadosInternacionalizadosAction.findSistemas"
			optionLabelProperty="nmSistema" optionProperty="idSistema"
			onchange="_onChange(this);" boxWidth="200" property="sistema"
			label="sistema" labelWidth="15%" width="32%" required="true">
		</adsm:combobox>
		<adsm:hidden property="descricaoSistema" serializable="false" />
		<adsm:hidden property="tamanhoColuna" serializable="false" />
		<adsm:lookup disabled="true"
			service="lms.configuracoes.manterDadosInternacionalizadosAction.findTabelasInternacionalizaveis"
			property="tabela" label="tabela" idProperty="idTabela" criteriaProperty="nmTabela" dataType="text" size="36" maxLength="36"
			action="/configuracoes/manterPesquisaTabelasInternacionalizaveis"
			required="true" exactMatch="false" minLengthForAutoPopUpSearch="5">
			<adsm:propertyMapping criteriaProperty="sistema"
				modelProperty="idSistema" />
			<adsm:propertyMapping criteriaProperty="descricaoSistema"
				modelProperty="nmSistema" />
		</adsm:lookup>
		<adsm:combobox
			service="lms.configuracoes.manterDadosInternacionalizadosAction.findLocale"
			optionLabelProperty="nmLocale" optionProperty="locale"
			boxWidth="200" property="locale" label="idioma" labelWidth="15%"
			width="32%" required="true">
		</adsm:combobox>
		<adsm:combobox disabled="true"
			service="lms.configuracoes.manterDadosInternacionalizadosAction.findColunasInternacionalizadas"
			optionLabelProperty="nmColuna" optionProperty="idColuna" 
						property="coluna" label="coluna" boxWidth="200" labelWidth="15%" width="30%"
			required="true">
			<adsm:propertyMapping criteriaProperty="tabela.idTabela"
				modelProperty="nmTabela" />
			<adsm:propertyMapping criteriaProperty="sistema"
				modelProperty="idSistema" />
		</adsm:combobox>
		<adsm:textbox dataType="text" property="descricaoPortugues"
			label="descricaoPortugues" labelWidth="15%" width="32%" size="36" maxLength="35" />
		<adsm:textbox dataType="text" property="descricaoTraduzida"
			label="descricaoTraduzida" labelWidth="15%" width="32%" size="36" maxLength="35" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton id="consultaDados" callbackProperty="dadosInternacionalizados" />
			<adsm:button caption="limpar" id="limparFiltros" onclick="limparFiltrosConsulta();"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid onDataLoadCallBack="dataLoad" selectionMode="none"
		idProperty="idPortugues" property="dadosInternacionalizados"
		onRowClick="editarDadosExtendido"
		service="lms.configuracoes.manterDadosInternacionalizadosAction.findDadosI18n"
		rowCountService="lms.configuracoes.manterDadosInternacionalizadosAction.getRowCountDadosI18n"
		gridHeight="200" unique="true" rows="8">
		<adsm:editColumn property="idTraducao" field="hidden" title="branco"/>
		<adsm:gridColumn title="descricaoPortugues"
			property="descricaoPortugues" />
		<adsm:editColumn width="380" title="descricaoTraduzida"
			field="textbox" property="descricaoTraduzida" maxLength="60"
			maxValue="60" />
		<adsm:buttonBar>
			<adsm:button id="salvar" caption="salvar" onclick="store();" disabled="true"/>
			<adsm:button id="limpar" caption="limpar" onclick="resetFields();" disabled="true"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
function store() {
	storeEditGridScript("lms.configuracoes.manterDadosInternacionalizadosAction.storeUpdateDelete", "storeUpdateDelete", document.forms[0], document.forms[1]);
}
function _onChange(combo) {
	if (combo.options.value == '') {
		habilitaEscolhaUsuario(true);
 	} else {
		habilitaEscolhaUsuario(false);
	}
	clear();
	var index = document.getElementById("sistema").options.selectedIndex;
	var text = document.getElementById("sistema").options[index].text;
	document.getElementById("descricaoSistema").value = text;
}
function myOnPageLoad_cb(d,e,c,x){
	habilitaEscolhaUsuario(true);
	setDisabled("limpar", false);	
	setDisabled("limparFiltros", false);	
	onPageLoad_cb(d,e,c,x);
	setFocus("sistema");
}

function validateTab(eventObj) {
	var ret = validateTabScript(document.forms[0]);
	if (ret && eventObj.name!="storeButton_click") {
		ret = dadosInternacionalizadosGridDef.checkFieldsChanged();
	}
	return ret;
}

function habilitaCampos(disabled) {
	setDisabled("tabela.idTabela", disabled);
	setDisabled("coluna", disabled);
	setDisabled("tabela.nmTabela", disabled);
	setDisabled("sistema", disabled);
	setDisabled("locale", disabled);
}

function habilitaEscolhaUsuario(disabled) {
	setDisabled("tabela.idTabela", disabled);
	setDisabled("coluna", disabled);
}

function clear() {
	document.getElementById("tabela.nmTabela").value = "";
	document.getElementById("coluna").options.selectedIndex = 0;
}

function storeUpdateDelete_cb(data, exception) {	
	if (exception != null) {
		alert(exception);
		setFocus("salvar", false, true);
	} else {
		store_cb(data, exception);
		setDisabled("limpar", false);	
		setDisabled("salvar", false);
		setFocus("limpar", false, true);
		dadosInternacionalizadosGridDef.executeLastSearch();
	}
	setDisabled("consultaDados", false);
}


function resetFields() {
	if (dadosInternacionalizadosGridDef.checkFieldsChanged()) {
		cleanButtonScript();
		dadosInternacionalizadosGridDef.lastData = null;
		dadosInternacionalizadosGridDef.resetGrid();	
		dadosInternacionalizadosGridDef.resetEditGrid();
		setDisabled("limpar", false);	
		setDisabled("limparFiltros", false);	
		setFocus("sistema");
	}
}

function initWindow(eventObj) {
	if (eventObj.name == "cleanButton_click") {
		habilitaCampos(false);
		habilitaEscolhaUsuario(true);
	}
}

function editarDadosExtendido(id) {
	var dataRow = dadosInternacionalizadosGridDef.findById(id);
	var args = {descricaoPortugues:dataRow.descricaoPortugues,
				descricaoTraduzida:dataRow.descricaoTraduzida,
				tamanhoColuna:dadosInternacionalizadosGridDef.getCellObject(0, "descricaoTraduzida").maxLength
				};
	var descTraduzida = openModalDialog("configuracoes/manterDadosInternacionalizados.do?cmd=popupDados", args, "dialogHeight:540px; dialogWidth:700px; center=yes");
	setElementValue(dadosInternacionalizadosGridDef.getCellObjectById(id, "descricaoTraduzida"), descTraduzida);
	// modifica os flags que controlam a existencia de modificações
	dadosInternacionalizadosGridDef.gridState.fieldsChanged = true;
	return false; // retorna false para cancelar ação padrão do click na grid
}

function dataLoad_cb(data, error) {
	habilitaCampos(true);
	if (data.list == undefined) {
		setDisabled("salvar", true);
	} else if (dadosInternacionalizadosGridDef.gridState.rowCount>0) {
		// define o tamanho maximo da coluna
		for(var idx = 0; idx < dadosInternacionalizadosGridDef.gridState.rowCount; idx++) {
			dadosInternacionalizadosGridDef.getCellObject(idx, "descricaoTraduzida").maxLength = data.list[0].tamanhoColuna;
		}
	}
	if (dadosInternacionalizadosGridDef.gridState.rowCount>0) {
		dadosInternacionalizadosGridDef.getCellObject(0, "descricaoTraduzida").focus();
		dadosInternacionalizadosGridDef.getCellObject(0, "descricaoTraduzida").focus();
	}
}

function limparFiltrosConsulta() {
	// se o sistema estiver desabilitado é porque existe uma
	// consulta que foi feita pelo usuário então na limpa estes
	// campos
	if (getElement("sistema").disabled == false)  {
		resetValue("sistema");
		resetValue("locale");
	}
	resetValue("descricaoPortugues");
	resetValue("descricaoTraduzida");
	habilitaEscolhaUsuario(true);
}

</script>
