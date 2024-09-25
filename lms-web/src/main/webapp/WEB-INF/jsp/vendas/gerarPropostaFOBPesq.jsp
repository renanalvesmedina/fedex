<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarPropostaFOBAction" onPageLoad="myOnLoadPage">
	<adsm:i18nLabels>
		<adsm:include key="LMS-28006" />
		<adsm:include key="LMS-00053" />
	</adsm:i18nLabels>
	<adsm:form action="/vendas/gerarPropostaFOB" idProperty="idPropostaFOB">

		<adsm:hidden property="isGenerate"  value="false" serializable="false" />
		<adsm:hidden property="blTabelaFob" value="N" serializable="false" />
		<adsm:hidden property="tpCliente" serializable="true" />
		<adsm:hidden property="idPropostaFOB" serializable="true" />


		<%--------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%--------------------%>
		<adsm:lookup
				action="/vendas/manterDadosIdentificacao"
				dataType="text"
				criteriaProperty="pessoa.nrIdentificacao"
				exactMatch="true"
				idProperty="idCliente"
				property="cliente"
				label="cliente"
				size="20"
				maxLength="20"
				width="45%"
				labelWidth="16%"
				service="lms.vendas.gerarPropostaFOBAction.findClienteLookup"
				required="true"
				onDataLoadCallBack="lookupCliente"
				afterPopupSetValue="aferPopupCliente">

			<adsm:propertyMapping
					modelProperty="pessoa.nmPessoa"
					relatedProperty="cliente.pessoa.nmPessoa" />

			<adsm:textbox
					dataType="text"
					disabled="true"
					serializable="false"
					property="cliente.pessoa.nmPessoa"
					size="30" />
		</adsm:lookup>


		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:combobox
				optionLabelProperty="dsDivisaoCliente"
				width="29%"
				optionProperty="idDivisaoCliente"
				labelWidth="10%"
				boxWidth="140"
				property="divisaoCliente.idDivisaoCliente"
				service="lms.vendas.gerarPropostaFOBAction.findDivisaoCombo"
				onchange="return changeDivisaoCliente(this);"
				required="true"
				label="divisao">

			<adsm:propertyMapping
					modelProperty="cliente.idCliente"
					criteriaProperty="cliente.idCliente" />
		</adsm:combobox>

		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:lookup
				label="tabela"
				property="tabelaPreco"
				service="lms.vendas.gerarPropostaFOBAction.findLookupTabelaPreco"
				action="/tabelaPrecos/manterTabelasPreco"
				idProperty="idTabelaPreco"
				criteriaProperty="tabelaPrecoString"
				dataType="text"
				onDataLoadCallBack="lookupTabelaPrecoFob"
				required="true"
				size="10"
				maxLength="9"
				width="35%"
				labelWidth="16%"
		>

			<adsm:propertyMapping
					modelProperty="dsDescricao"
					relatedProperty="tabelaPreco.dsDescricao"/>

			<adsm:propertyMapping
					criteriaProperty="blTabelaFob"
					modelProperty="blTabelaFob"/>

			<adsm:textbox dataType="text" property="tabelaPreco.dsDescricao"
						  size="30" maxLength="30" disabled="true"/>
		</adsm:lookup>

		<%----------------------------%>
		<%-- IMPRIME COND. CHECKBOX --%>
		<%----------------------------%>
		<adsm:checkbox property="impTermosCondicoes" label="imprimeTermosCondicoes" labelWidth="20%" width="21%"/>

		<%----------------------%>
		<%-- MUNICIPIO LOOKUP --%>
		<%----------------------%>
		<adsm:lookup property="municipio" criteriaProperty="nmMunicipio" idProperty="idMunicipio" dataType="text"
					 service="lms.municipios.municipioService.findMunicipioLookup" required="true"
					 label="municipio" size="30" action="/municipios/manterMunicipios" labelWidth="16%"
					 exactMatch="false" minLengthForAutoPopUpSearch="4" maxLength="60">

			<adsm:propertyMapping
					modelProperty="cliente.idCliente"
					criteriaProperty="cliente.idCliente" />

		</adsm:lookup>

		<adsm:buttonBar>
			<adsm:button id="botaoEfetivar" caption="botaoEfetivar" onclick="efetivarProposta();" disabled="true"/>
			<adsm:reportViewerButton id="emitir" service="lms.vendas.emitirRelatorioPropostaFOBAction" disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idFilial"
			   property="origensProposta"
			   service="lms.vendas.gerarPropostaFOBAction.findOrigensProposta"
			   scrollBars="vertical"
			   showGotoBox="false"
			   showPagging="false"
			   onRowClick="onRowClick"
			   selectionMode="1"
			   gridHeight="280" rows="9" unique="true">

		<adsm:editColumn property="isSelected" field="hidden" dataType="text" title="" width="1"/>
		<adsm:editColumn property="blIndicadorPadrao"   field="hidden"  dataType="text" title="" />
		<adsm:gridColumn title="origens" property="sgFilial" width="100%" />


	</adsm:grid>

</adsm:window>
<script>


	function myOnLoadPage(){
		onPageLoad();
		setDisabled("emitir",false);
		setDisabled("botaoEfetivar",true);
	}

	function lookupTabelaPrecoFob_cb(data, error) {

		var result = tabelaPreco_tabelaPrecoString_exactMatch_cb(data);

		var tpCliente = getElementValue("tpCliente");

		if(tpCliente == "" || tpCliente == "E" || tpCliente == "P"){
			var data = new Array();
			executeService("lms.vendas.gerarPropostaFOBAction.findTabelaPrecoFOBVigente","findTabelaPrecoFOBVigente",data);
			return true;
		}
		return result;
	}

	function findTabelaPrecoFOBVigente_cb(data,error){
		if(error != undefined){
			alert(error);
			return false;
		}
		if (data.length > 0) {
			setDataTabelaPreco(data.idTabelaPreco,data.dsDescricao);
			setDisabled("tabelaPreco.idTabelaPreco",true);
		}
		return true;
	}

	function validateStore() {

		var chOrigem = false;

		var dataTable = document.getElementById("origensProposta.dataTable");
		for (rowIndex=0; rowIndex < dataTable.rows.length; rowIndex++) {
			if(document.getElementById("origensProposta:"+rowIndex+".idFilial").checked){
				getElementByGrid(rowIndex,"isSelected").value = "true";
				chOrigem = true;
			}
		}

		return chOrigem;
	}

	function getElementValueByGrid(rowIndex, property) {
		return getElementValue(getElementByGrid(rowIndex,property));
	}

	function getElementByGrid(rowIndex, property) {
		return origensPropostaGridDef.getCellObject(rowIndex, property);
	}

	function onRowClick() {
		return false;
	}

	function efetivarProposta(){

		if (validateForm(document.forms[0])) {

			if(!validateStore()){
				alertI18nMessage("LMS-00053");
				return false;
			}

			var data = new Array();
			merge(data, buildFormBeanFromForm(document.forms[0]));
			merge(data, buildFormBeanFromForm(document.forms[1]));

			executeService("lms.vendas.gerarPropostaFOBAction.executeEfetivarProposta","executeEfetivarProposta",data);
		}
	}

	function executeEfetivarProposta_cb(data,error){
		if(error != undefined){
			alert(error);
			return;
		}

		var cdTabelaPreco = getElementValue("tabelaPreco.tabelaPrecoString");

		/*Informa o id da Proposta FOB*/
		onDataLoad_cb(data,error);
		setElementValue("idPropostaFOB", data.idPropostaFOB);
		setElementValue("tabelaPreco.tabelaPrecoString", cdTabelaPreco);

		/*Busca as origens da proposta FOB*/
		populateGrid(true);

		showSuccessMessage();
	}

	function changeDivisaoCliente(field){
		findTabelaPrecoFOB();
	}

	function initWindow(eventObj) {

		if(eventObj.name == "cleanButton_click"){
			setDisabled("tabelaPreco.idTabelaPreco",false);
			setDisabled("botaoEfetivar",true);
		}
		setDisabled("emitir",false);

		populateGrid(false);
	}

	function populateGrid(isGenerate){
		setElementValue("isGenerate",isGenerate);
		var formPrincipal = buildFormBeanFromForm(document.forms[0]);
		origensPropostaGridDef.executeSearch(formPrincipal, true);
	}

	function executeService(service,callback,data){
		var sdo = createServiceDataObject(service,callback,data);
		xmit({serviceDataObjects:[sdo]});
	}

	function findTabelaPrecoFOB(){
		var idDivisaoCliente = getElementValue("divisaoCliente.idDivisaoCliente");
		var data = {idDivisaoCliente:idDivisaoCliente};
		executeService("lms.vendas.gerarPropostaFOBAction.findTabelaPrecoFOB","findTabelaPrecoFOB",data);
	}

	function findTabelaPrecoFOB_cb(data,error){
		if(error != undefined){
			alert(error);
			return
		}
		if (data.length > 0) {
			setDataTabelaPreco(data.idTabelaPreco,data.dsDescricao);
		}

		var idCliente = getElementValue("cliente.idCliente");
		if(idCliente != undefined){
			var data = {idCliente:idCliente};
			executeService("lms.vendas.gerarPropostaFOBAction.findPropostaFOBCliente","findPropostaFOBCliente",data);
		}
		setDisabled("botaoEfetivar",false);
	}

	function findPropostaFOBCliente_cb(data,error){
		if(error != undefined){
			alert(error);
			return
		}
		if (data.existeParametroFOB == "true") {
			var loadFOB = confirm("J� existe proposta FOB para este cliente, deseja utilizar?");
			if(loadFOB){
				loadDataFOB();
			}
		}
	}

	function loadDataFOB(){
		var idCliente = getElementValue("cliente.idCliente");
		if(idCliente != undefined){
			var data = {idCliente:idCliente};
			executeService("lms.vendas.gerarPropostaFOBAction.findPropostaFOBClienteInformado","findPropostaFOBClienteInformado",data);
		}
	}

	function findPropostaFOBClienteInformado_cb(data,error){
		if(error != undefined){
			alert(error);
			return;
		}
		onDataLoad_cb(data,error);

		setElementValue("idPropostaFOB",data.idPropostaFOB);
		setElementValue("tabelaPreco.tabelaPrecoString",data.tabelaPreco.tabelaPrecoStringDescricao);

		populateGrid(true);

		setDisabled("emitir",false);
	}

	function setDataMunicipio(idMunicipio,nmMunicipio){
		setElementValue("municipio.idMunicipio",idMunicipio);
		setElementValue("municipio.nmMunicipio",nmMunicipio);
	}

	function setDataCliente(idCliente,nrIdentificacao,nmPessoa){
		setElementValue("cliente.idCliente",idCliente);
		setElementValue("cliente.pessoa.nrIdentificacao",nrIdentificacao);
		setElementValue("cliente.pessoa.nmPessoa",nmPessoa);
	}

	function setDataTabelaPreco(idTabelaPreco, dsDescricao){
		setElementValue("tabelaPreco.idTabelaPreco",idTabelaPreco);
		setElementValue("tabelaPreco.dsDescricao",dsDescricao);
	}

	/*Callback da lookup de cliente*/
	function lookupCliente_cb(data, error) {

		if (error != undefined) {
			alert(error);
			return false;
		}

		setDisabled("tabelaPreco.idTabelaPreco",false);
		resetValue("tabelaPreco.idTabelaPreco");

		if (data.length > 0) {
			if (!validateClienteEspecial(data[0])) {
				return;
			}
		}

		/** Popula dados e formata nrIdentificacao */
		var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data.length > 0) {
			setElementValue("cliente.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);

			/*Informa o municipio do endere�o padr�o do cliente*/
			setElementValue("municipio.idMunicipio", data[0].idMunicipio);
			setElementValue("municipio.nmMunicipio", data[0].nmMunicipio);
		}

		var dataTable = document.getElementById("origensProposta.dataTable");

		for (rowIndex=0; rowIndex < dataTable.rows.length; rowIndex++) {
			if(getElementValueByGrid(rowIndex,"blIndicadorPadrao") == "S") {
				document.getElementById("origensProposta:"+rowIndex+".idFilial").checked = true
			}
		}

		return retorno;
	}

	/*Utilizado pela pop up da lookup de cliente*/
	function aferPopupCliente(data) {

	}

	function validateClienteEspecial(cliente) {

		if(cliente != undefined){
			setElementValue("tpCliente", cliente.tpCliente.value);
		}

		if(cliente != undefined && cliente.tpCliente.value == "F") {

			alertI18nMessage("LMS-28006",cliente.pessoa.nrIdentificacao,false);
			resetValue("cliente.idCliente");
			resetValue("cliente.pessoa.nmPessoa");
			setFocus("cliente.pessoa.nrIdentificacao", false);
			return false;
		}
		return true;
	}

</script>