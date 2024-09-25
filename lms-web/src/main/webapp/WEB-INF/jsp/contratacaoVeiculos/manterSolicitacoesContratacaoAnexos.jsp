<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction">

	<adsm:form action="/contratacaoVeiculos/manterSolicitacoesContratacao"
			   service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findAnexoSolicContratacaoById"
			   idProperty="idAnexoSolicContratacao" onDataLoadCallBack="myOnDataLoadCallBack"
			   height="87">

	<adsm:hidden property="idSolicitacaoContratacao" serializable="true" />

	<adsm:complement
			label="solicitacaoContratacao"
			labelWidth="10%"
			width="90%">

			<adsm:textbox
				dataType="text"
				property="anexoSolicContratacao.anexoSolicContratacaoString"
				size="5"
				maxLength="7"
				disabled="true"
				serializable="false"/>

			<adsm:textbox
				dataType="text"
				maxLength="60"
				property="anexoSolicContratacao.dsDescricao"
			 	size="20"
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
					  tableName="ANEXO_SOLIC_CONTRATACAO"
					  primaryKeyColumnName="ID_ANEXO_SOLIC_CONTRATACAO"
					  primaryKeyValueProperty="idAnexoSolicContratacao"
					  width="80%"
					  labelWidth="10%"
					  size="90"
					  serializable="true"
					  required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar"
							  id="storeButton"
							  service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.storeAnexoSolicContratacao"
							  callbackProperty="storeAnexo"/>
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idAnexoSolicContratacao"
			   property="anexoSolicitacaoContratacao"
			   service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.findPaginatedAnexoSolicContratacao"
			   rowCountService="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.getRowCountAnexoSolicContratacao"
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
					caption="excluir"
					service="lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.removeByIdsAnexoSolicContratacao"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj){
		preencherDadosSolicitacaoContratacao();

		limparCamposTela();

		if(eventObj.name != "newButton_click"){
			popularGrid();
		}
	}

	function myOnDataLoadCallBack_cb(data, error) {
		onDataLoad_cb(data, error);
		preencherDadosSolicitacaoContratacao();
	}

	function popularGrid(){
		anexoSolicitacaoContratacaoGridDef.executeSearch({idSolicitacaoContratacao:getElementValue("idSolicitacaoContratacao")}, true);
	}

	function preencherDadosSolicitacaoContratacao(){
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idSolicitacaoContratacao = tabDet.getFormProperty("idSolicitacaoContratacao");
		
		if(idSolicitacaoContratacao != undefined && idSolicitacaoContratacao != '') {			
			setElementValue("idSolicitacaoContratacao", idSolicitacaoContratacao);						
			setElementValue("anexoSolicContratacao.anexoSolicContratacaoString", tabDet.getFormProperty("filial.sgFilial"));
			setElementValue("anexoSolicContratacao.dsDescricao", tabDet.getFormProperty("nrSolicitacaoContratacao"));
			
			// Define que nao existiu alteracao de cadastro devido as alteracoes.
			tabDet.setChanged(false);
			
			if (tabDet.getFormProperty("idProcessoWorkflow") != "") {
				setDisabled(document, true);
			}
		}
	}

	function removeAnexo(){
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "ids", anexoSolicitacaoContratacaoGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterSolicitacoesContratacaoAction.removeByIdsAnexoSolicContratacao", "removeAnexo", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	function removeAnexo_cb(data, error, eventObj){
		if(error == undefined){
			preencherDadosSolicitacaoContratacao();
			popularGrid();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function storeAnexo_cb(data, error, eventObj) {
		if(error == undefined){
			preencherDadosSolicitacaoContratacao();
			popularGrid();
			limparCamposTela();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function limparCamposTela(){
		resetValue("idAnexoSolicContratacao");
		resetValue("dsAnexo");
		resetValue("dcArquivo");
	}

</script>