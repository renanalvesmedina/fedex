<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contratacaoveiculos.manterMeiosTransporteAction">

	<adsm:form action="/contratacaoVeiculos/manterMeiosTransporte"
			   service="lms.contratacaoveiculos.manterMeiosTransporteAction.findAnexoMeioTransporteById"
			   idProperty="idAnexoMeioTransporte" onDataLoadCallBack="myOnDataLoadCallBack"
			   height="87">

	<adsm:hidden property="idMeioTransporte" serializable="true" />

	<adsm:complement
			label="frotaPlaca"
			labelWidth="10%"
			width="90%">

			<adsm:textbox
				dataType="text"
				property="meioTransporte.meioTransporteString"
				size="8"
				maxLength="7"
				disabled="true"
				serializable="false"/>

			<adsm:textbox
				dataType="text"
				maxLength="60"
				property="meioTransporte.dsDescricao"
			 	size="10"
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
					  tableName="ANEXO_MEIO_TRANSPORTE"
					  primaryKeyColumnName="ID_ANEXO_MEIO_TRANSPORTE"
					  primaryKeyValueProperty="idAnexoMeioTransporte"
					  width="80%"
					  labelWidth="10%"
					  size="90"
					  serializable="true"
					  required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar"
							  id="storeButton"
							  service="lms.contratacaoveiculos.manterMeiosTransporteAction.storeAnexoMeioTransporte"
							  callbackProperty="storeAnexo"/>
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idAnexoMeioTransporte"
			   property="anexoMeioTransporte"
			   service="lms.contratacaoveiculos.manterMeiosTransporteAction.findPaginatedAnexoMeioTransporte"
			   rowCountService="lms.contratacaoveiculos.manterMeiosTransporteAction.getRowCountAnexoMeioTransporte"
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
			<adsm:button
					id="btRemove"
					caption="excluir"
					onclick="removeAnexo()"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");

	function initWindow(eventObj){
		preencherDadosMeioTransporte();

		limparCamposTela();

		if(eventObj.name != "newButton_click"){
			popularGrid();
		}
	}

	function myOnDataLoadCallBack_cb(data, error) {
		onDataLoad_cb(data, error);
		preencherDadosMeioTransporte(data);
	}

	function popularGrid(){
		anexoMeioTransporteGridDef.executeSearch({idMeioTransporte:getElementValue("idMeioTransporte")}, true);
	}

	function preencherDadosMeioTransporte(data){
		var idMeioTransporte = tabDet.getFormProperty("idMeioTransporte");

		if(idMeioTransporte != undefined && idMeioTransporte != '') {
			setElementValue("idMeioTransporte", idMeioTransporte);
			setElementValue("meioTransporte.meioTransporteString", tabDet.getFormProperty("nrFrota"));
			setElementValue("meioTransporte.dsDescricao", tabDet.getFormProperty("nrIdentificador"));
		
			if(data){
				// Atualiza campos necessarios na aba detalhamento.
				setTabDetElementValue("desabilitaCad", getNestedBeanPropertyValue(data, "desabilitaCad"));
				setTabDetElementValue("dsPendencia", getNestedBeanPropertyValue(data, "dsPendencia"));
				setTabDetElementValue("dtAtualizacao", getFormattedValue("JTDate", getNestedBeanPropertyValue(data, "dtAtualizacao"), "dd/MM/yyyy", true));
				setTabDetElementValue("usuarioAtualizacao.nrMatricula", getNestedBeanPropertyValue(data, "usuarioAtualizacao.nrMatricula"));
				setTabDetElementValue("usuarioAtualizacao.nmUsuario", getNestedBeanPropertyValue(data, "usuarioAtualizacao.nmUsuario"));
				
				// Define que nao existiu alteracao de cadastro devido as alteracoes.
				tabDet.setChanged(false);
			}
			
			if (tabDet.getFormProperty("idProcessoWorkflow") != "") {
				setDisabled(document, true);
			}
		}
	}

	function setTabDetElementValue(name, value){
		if(!value || value == undefined){
			return;
		}
		
		var element = tabDet.getElementById(name);
		
		if(element){
			element.value = value;
		}
	}
	
	function removeAnexo(){
		var idMeioTransporte = tabDet.getFormProperty("idMeioTransporte");
				
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "ids", anexoMeioTransporteGridDef.getSelectedIds().ids);
		setNestedBeanPropertyValue(mapCriteria, "idMeioTransporte", idMeioTransporte);
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.removeByIdsAnexoMeioTransporte", "removeAnexo", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	function removeAnexo_cb(data, error, eventObj){
		if(error == undefined){
			preencherDadosMeioTransporte(data);
			popularGrid();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function storeAnexo_cb(data, error, eventObj) {
		if(error == undefined){
			preencherDadosMeioTransporte(data);
			popularGrid();
			limparCamposTela();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function limparCamposTela(){
		resetValue("idAnexoMeioTransporte");
		resetValue("dsAnexo");
		resetValue("dcArquivo");
	}

</script>