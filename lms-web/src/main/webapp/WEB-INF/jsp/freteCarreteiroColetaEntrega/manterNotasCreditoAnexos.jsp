<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction">

	<adsm:form action="freteCarreteiroColetaEntrega/manterNotasCredito"
			   service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findAnexoNotaCreditoById"
			   idProperty="idAnexoNotaCredito" onDataLoadCallBack="myOnDataLoadCallBack"
			   height="87">

	<adsm:hidden property="idNotaCredito" serializable="true" />

	<adsm:complement
			label="notaCredito"
			labelWidth="10%"
			width="90%">

			<adsm:textbox
				dataType="text"
				property="notaCredito.notaCreditoString"
				size="4"
				maxLength="7"
				disabled="true"
				serializable="false"/>

			<adsm:textbox
				dataType="text"
				maxLength="60"
				property="notaCredito.dsDescricao"
			 	size="12"
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
					  width="80%"
					  labelWidth="10%"
					  size="90"
					  serializable="true"
					  required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar"
							  id="storeButton"
							  service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.storeAnexoNotaCredito"
							  callbackProperty="storeAnexo"/>
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idAnexoNotaCredito"
			   property="anexoNotaCredito"
			   service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.findPaginatedAnexoNotaCredito"
			   rowCountService="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.getRowCountAnexoNotaCredito"
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
					service="lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.removeByIdsAnexoNotaCredito"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj){
		preencherDadosNotaCredito();

		limparCamposTela();

		if(eventObj.name != "newButton_click"){
			popularGrid();
		}
	}

	function myOnDataLoadCallBack_cb(data, error) {
		onDataLoad_cb(data, error);
		preencherDadosNotaCredito();
	}

	function popularGrid(){
		anexoNotaCreditoGridDef.executeSearch({idNotaCredito:getElementValue("idNotaCredito")}, true);
	}

	function preencherDadosNotaCredito(){
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		var idNotaCredito = tabDet.getFormProperty("idNotaCredito");
		
		if(idNotaCredito != undefined && idNotaCredito != '') {			
			setElementValue("idNotaCredito", idNotaCredito);						
			setElementValue("notaCredito.notaCreditoString", tabDet.getFormProperty("sgFilial"));
			setElementValue("notaCredito.dsDescricao", tabDet.getFormProperty("nrNotaCredito"));
			
			// Define que nao existiu alteracao de cadastro devido as alteracoes.
			tabDet.setChanged(false);
		}
	}

	function removeAnexo(){
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "ids", anexoNotaCreditoGridDef.getSelectedIds().ids);
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.manterNotasCreditoAction.removeByIdsAnexoNotaCredito", "removeAnexo", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	function removeAnexo_cb(data, error, eventObj){
		if(error == undefined){
			preencherDadosNotaCredito();
			popularGrid();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function storeAnexo_cb(data, error, eventObj) {
		if(error == undefined){
			preencherDadosNotaCredito();
			popularGrid();
			limparCamposTela();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function limparCamposTela(){
		resetValue("idAnexoNotaCredito");
		resetValue("dsAnexo");
		resetValue("dcArquivo");
	}

</script>