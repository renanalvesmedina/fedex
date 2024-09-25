<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.tabelaprecos.manterTabelasPrecoAction">

	<adsm:form action="/tabelaPrecos/manterTabelasPreco"
			   service="lms.tabelaprecos.manterTabelasPrecoAction.findTabelaPrecoAnexoById"
			   idProperty="idTabelaPrecoAnexo" onDataLoadCallBack="myOnDataLoadCallBack"
			   height="87">

	<adsm:hidden property="idTabelaPreco" serializable="true" />

	<adsm:complement
			label="tabela"
			labelWidth="10%"
			width="90%">

			<adsm:textbox
				dataType="text"
				property="tabelaPreco.tabelaPrecoString"
				size="8"
				maxLength="7"
				disabled="true"
				serializable="false"/>

			<adsm:textbox
				dataType="text"
				maxLength="60"
				property="tabelaPreco.dsDescricao"
			 	size="30"
			 	disabled="true"
			 	serializable="false"/>

		</adsm:complement>

		<adsm:textbox label="descricao"
					  dataType="text"
					  property="dsAnexo"
					  width="90%"
					  labelWidth="10%"
					  maxLength="100"
					  size="100"
					  required="true" />

		<adsm:textbox label="arquivo"
					  dataType="file"
					  property="dcArquivo"
					  blobColumnName="DC_ARQUIVO"
					  tableName="TABELA_PRECO_ANEXO"
					  primaryKeyColumnName="ID_TABELA_PRECO_ANEXO"
					  primaryKeyValueProperty="idTabelaPrecoAnexo"
					  width="80%"
					  labelWidth="10%"
					  size="90"
					  serializable="true"
					  required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar"
							  id="storeButton"
							  service="lms.tabelaprecos.manterTabelasPrecoAction.storeTabelaPrecoAnexo"
							  callbackProperty="storeAnexo"/>
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idTabelaPrecoAnexo"
			   property="tabelaPrecoAnexo"
			   service="lms.tabelaprecos.manterTabelasPrecoAction.findPaginatedTabelaPrecoAnexo"
			   rowCountService="lms.tabelaprecos.manterTabelasPrecoAction.getRowCountTabelaPrecoAnexo"
			   selectionMode="check"
			   gridHeight="160"
			   detailFrameName="anexo"
			   scrollBars="none"
			   unique="true"
			   rows="10"
			   autoSearch="false">

		<adsm:gridColumn title="nomeArquivo" property="nmArquivo" width="25%"/>
		<adsm:gridColumn title="descricao" property="dsAnexo" width="37%" />
		<adsm:gridColumn title="dataHoraDeInclusao" property="dhInclusao" width="18%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="usuario" property="nmUsuario" width="20%" />

		<adsm:buttonBar>
			<adsm:removeButton
					id="btnExcluir"
					caption="excluir"
					service="lms.tabelaprecos.manterTabelasPrecoAction.removeByIdsTabelaPrecoAnexo"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj){
		preencherDadosTabelaPreco();

		limparCamposTela();

		if(eventObj.name != "newButton_click"){
			popularGrid();
		}
		handleTelaWorkflow();
	}

	function myOnDataLoadCallBack_cb(data, error) {
		onDataLoad_cb(data, error);
		preencherDadosTabelaPreco();
		handleTelaWorkflow();
	}

	function popularGrid(){
		tabelaPrecoAnexoGridDef.executeSearch({idTabelaPreco:getElementValue("idTabelaPreco")}, true);
	}

	function preencherDadosTabelaPreco(){
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idTabelaPreco = tabDet.getFormProperty("idTabelaPreco");

		if(idTabelaPreco != undefined && idTabelaPreco != '') {
			var frame = parent.document.frames["cad_iframe"];
			var data = frame.getDadosTabela();
			setElementValue("idTabelaPreco", idTabelaPreco);
			setElementValue("tabelaPreco.tabelaPrecoString", data.sgTabelaPreco);
			setElementValue("tabelaPreco.dsDescricao", data.dsTabelaPreco);
		}
	}

	function removeAnexo(){
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "ids", tabelaPrecoAnexoGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.tabelaprecos.manterTabelasPrecoAction.removeByIdsTabelaPrecoAnexo", "removeAnexo", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	function removeAnexo_cb(data, error, eventObj){
		if(error == undefined){
			preencherDadosTabelaPreco();
			popularGrid();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function storeAnexo_cb(data, error, eventObj) {
		if(error == undefined){
			preencherDadosTabelaPreco();
			popularGrid();
			limparCamposTela();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function limparCamposTela(){
		resetValue("idTabelaPrecoAnexo");
		resetValue("dsAnexo");
		resetValue("dcArquivo");
	}
	
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}
	
	function handleTelaWorkflow() {
		if (getIdProcessoWorkflow() != "" && getIdProcessoWorkflow() != undefined) {
			setDisabled(document,true);
			setDisabled("btnExcluir", true);
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisplay('dcArquivo_delete', false);
		}
	}

</script>