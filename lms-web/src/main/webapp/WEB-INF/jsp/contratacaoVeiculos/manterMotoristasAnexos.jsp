<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contratacaoveiculos.manterMotoristasAction">

	<adsm:form action="/contratacaoVeiculos/manterMotoristas"
			   service="lms.contratacaoveiculos.manterMotoristasAction.findAnexoMotoristaById"
			   idProperty="idAnexoMotorista" onDataLoadCallBack="myOnDataLoadCallBack"
			   height="87">

	<adsm:hidden property="idMotorista" serializable="true" />

	<adsm:complement
			label="motorista"
			labelWidth="10%"
			width="90%">

			<adsm:textbox
				dataType="text"
				property="motorista.motoristaString"
				size="18"
				maxLength="7"
				disabled="true"
				serializable="false"/>

			<adsm:textbox
				dataType="text"
				maxLength="60"
				property="motorista.dsDescricao"
			 	size="50"
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
					  blobColumnName="DC_ARQUIVO"
					  tableName="ANEXO_MOTORISTA"
					  primaryKeyColumnName="ID_ANEXO_MOTORISTA"
					  primaryKeyValueProperty="idAnexoMotorista"
					  property="dcArquivo"
					  width="80%"
					  labelWidth="10%"
					  size="90"
					  serializable="true"
					  required="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvar"
							  id="storeButton"
							  service="lms.contratacaoveiculos.manterMotoristasAction.storeAnexoMotorista"
							  callbackProperty="storeAnexo"/>
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="idAnexoMotorista"
			   property="anexoMotorista"
			   service="lms.contratacaoveiculos.manterMotoristasAction.findPaginatedAnexoMotorista"
			   rowCountService="lms.contratacaoveiculos.manterMotoristasAction.getRowCountAnexoMotorista"
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
			<adsm:button id="btRemove"
					caption="excluir"
					onclick="removeAnexo()"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("cad");

	function initWindow(eventObj){
		preencherDadosMotorista();

		limparCamposTela();
		
		if(eventObj.name != "newButton_click"){
			popularGrid();
		}
	}

	function myOnDataLoadCallBack_cb(data, error) {
		onDataLoad_cb(data, error);
		preencherDadosMotorista();
	}

	function popularGrid(){
		anexoMotoristaGridDef.executeSearch({idMotorista:getElementValue("idMotorista")}, true);
	}

	function preencherDadosMotorista(data){
		var idMotorista = tabDet.getFormProperty("idMotorista");
		
		if(idMotorista != undefined && idMotorista != '') {
			setElementValue("idMotorista", idMotorista);						
			setElementValue("motorista.motoristaString", tabDet.getFormProperty("pessoa.nrIdentificacao"));
			setElementValue("motorista.dsDescricao", tabDet.getFormProperty("pessoa.nmPessoa"));	
						
			if(data){
				// Atualiza campos necessarios na aba detalhamento.
				setTabDetElementValue("desabilitaCad", getNestedBeanPropertyValue(data, "desabilitaCad"));
				setTabDetElementValue("dsPendencia", getNestedBeanPropertyValue(data, "dsPendencia"));
				setTabDetElementValue("dtAtualizacao", getFormattedValue("JTDate", getNestedBeanPropertyValue(data, "dtAtualizacao"), "dd/MM/yyyy", true));
				setTabDetElementValue("usuarioAlteracao.nrMatricula", getNestedBeanPropertyValue(data, "usuarioAlteracao.nrMatricula"));
				setTabDetElementValue("usuarioAlteracao.nmUsuario", getNestedBeanPropertyValue(data, "usuarioAlteracao.nmUsuario"));
				
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
		var idMotorista = tabDet.getFormProperty("idMotorista");
				
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "ids", anexoMotoristaGridDef.getSelectedIds().ids);
		setNestedBeanPropertyValue(mapCriteria, "idMotorista", idMotorista);
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMotoristasAction.removeByIdsAnexoMotorista", "removeAnexo", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}

	function removeAnexo_cb(data, error, eventObj){
		if(error == undefined){
			preencherDadosMotorista(data);
			popularGrid();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function storeAnexo_cb(data, error, eventObj) {
		if(error == undefined){
			preencherDadosMotorista(data);
			popularGrid();
			limparCamposTela();
			showSuccessMessage();
		}else{
			alert(error);
		}
	}

	function limparCamposTela(){
		resetValue("idAnexoMotorista");
		resetValue("dsAnexo");
		resetValue("dcArquivo");
	}

</script>