<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function carregaPagina() {
		onPageLoad();
	
		getFromParameter();
		if (isFromCarregamento()!=true) {
		
			// Rotina que pega a referência da tela pai para usar parametros ou chamar funções
			// que serão usadas na tela filho.
			var doc;
			if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {
				doc = window.dialogArguments.window.document;
			} else {
			   doc = document;
			}		
			var tabGroup = getTabGroup(doc)
			
			// Pega parâmetros da tela DOCUMENTOS.
			var tabDet = tabGroup.getTab("documentos");
			setElementValue("masterId", tabDet.getFormProperty("masterId"));
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", tabDet.getFormProperty("manifesto.filialByIdFilialOrigem.sgFilial"));
			setElementValue("manifesto.nrPreManifesto", setFormat(document.getElementById("manifesto.nrPreManifesto"), 
																  tabDet.getFormProperty("manifesto.nrPreManifesto")));
			
			document.getElementById('doctoServico.tpDocumento').required = 'true';
			document.getElementById('doctoServico.nrDoctoServico').required = 'true';
			document.getElementById('doctoServico.filialByIdFilialOrigem.sgFilial').required = 'true';
			
					
			// Pega parâmetros da tela CAD.
			var tabDet = tabGroup.getTab("cad");
			setElementValue("manifesto.tpAbrangencia", tabDet.getFormProperty("tpAbrangencia"));	
		} else {
			var parentWindow = dialogArguments.window.document;
			setElementValue("masterId", parentWindow.getElementById("manifesto.idManifesto").value);
			setElementValue("manifesto.nrPreManifesto", parentWindow.getElementById("manifesto.nrPreManifesto").value);
			setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("manifesto.filialByIdFilialOrigem.sgFilial").value);
		}
	}
	
	/**
	 * Seta o hiden from 
	 */
	function getFromParameter() {
		var url = new URL(parent.location.href);
		var from = url.parameters["from"];
	
		if (from!=undefined)  {
			setElementValue("from", from);
		} else {
			setElementValue("from", "");
		}
	}
	
	/**
	 * Verifica se a tela esta sendo chamada da tela de carregamento.
	 */
	function isFromCarregamento() {
		if (getElementValue("from")=="carregamento") {
			return true;
		}
		return false;
	}
</script>

<adsm:window title="adicionarDocumentosPreManifesto" service="lms.carregamento.manterGerarPreManifestoAction"
			 onPageLoad="carregaPagina">
			 
	<adsm:form action="/carregamento/manterGerarPreManifestoAdicionarDocumentos" height="65">

		<adsm:section caption="adicionarDocumentoManualTitulo"/>
		<adsm:hidden property="masterId"/>
		<adsm:hidden property="doctoServico.idDoctoServico"/>
		<adsm:hidden property="manifesto.tpAbrangencia"/>
		<adsm:hidden property="from"/> 

		<adsm:textbox dataType="integer" property="manifesto.filialByIdFilialOrigem.sgFilial" 
					  label="preManifesto" labelWidth="18%" width="82%" size="3" maxLength="3" disabled="true"  >
			<adsm:textbox dataType="integer" property="manifesto.nrPreManifesto" size="8" 
						  maxLength="8" mask="00000000" disabled="true" />
		</adsm:textbox>

		<adsm:combobox property="doctoServico.tpDocumento" 
					   label="documento" labelWidth="18%" width="82%" 
					   service="lms.carregamento.manterGerarPreManifestoAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description" 
					   onDataLoadCallBack="tpDocumentoServicoComboBox"
					   onchange="return disableFilialOrigem()"
					   defaultValue="CTR" required="true">
					   
			<adsm:lookup dataType="text" property="doctoServico.filialByIdFilialOrigem" 
						 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="lms.carregamento.manterGerarPreManifestoAction.findLookupBySgFilial" 
						 action="/municipios/manterFiliais" 
						 onchange="return sgFilialOnChangeHandler();"
						 size="3" maxLength="3" picker="false" disabled="true">
				<adsm:textbox dataType="integer" property="doctoServico.nrDoctoServico" size="8" required="true"
							  maxLength="8" mask="00000000" disabled="true" />							  							  
			</adsm:lookup>					   
					   				   
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="incluir" id="incluir" onclick="return incluirDocumento();" buttonType="removeButton" />
  		    <adsm:button caption="fechar" id="fechar" onclick="javascript:window.close();"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script type="text/javascript">
	setDisabled("incluir", false);
	setDisabled("fechar", false);
	
	/**
	 * Verifica se foi escolhido algum tipo para desabilitar o campo da filial origem.
	 */
	function disableFilialOrigem() {
		var comboBox = document.getElementById("doctoServico.tpDocumento");		
		if(comboBox.options[comboBox.selectedIndex].value != "") {
			if(getElementValue("doctoServico.filialByIdFilialOrigem.sgFilial") != "") {
				resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
				setElementValue("doctoServico.nrDoctoServico", "");
				setDisabled("doctoServico.nrDoctoServico", true);
			} else {
				setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
			}
		} else {
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		}
			
		return true;
	}
	
	/**
	 * Função que adiciona um documento da grid na tela pai.
	 */
	function incluirDocumento() {
	
		if(!validateForm(document.forms[0])) {
			return false;
		}

		var formPrincipal = buildFormBeanFromForm(window.parent.document.forms[0]);
		
		
		if (!isFromCarregamento()) {
			// Pega parâmetros da tela pai.
			var tabGroup = getTabGroup(window.dialogArguments.window.document);
			var tabDet = tabGroup.getTab("cad");
			setNestedBeanPropertyValue(formPrincipal, "manifesto.tpManifesto", tabDet.getFormProperty("tpManifesto"));
			setNestedBeanPropertyValue(formPrincipal, "manifesto.tpPreManifesto", tabDet.getFormProperty("tpPreManifesto"));
			setNestedBeanPropertyValue(formPrincipal, "manifesto.filialByIdFilialDestino.idFilial", tabDet.getFormProperty("filialByIdFilialDestino.idFilial"));
			formPrincipal.from = "manifesto";
		} else {
			formPrincipal.from = "carregamento";
		}
		
		var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.findDoctoServico", "pesquisarDocumento", formPrincipal);
		xmit({serviceDataObjects:[sdo]}); 
	}
	
	/**
	 * Retorno da pesquisa de Documento.
	 */
	function pesquisarDocumento_cb(data, error) {
		if(!error) {
			var confirmacao = true;
			if (data.error) {
				if (!window.confirm(data.error)) {
					confirmacao = false;
				}
			}
			
			if (confirmacao) {
				setElementValue("doctoServico.idDoctoServico", data.idDoctoServico)
				
				var mapData = new Array();
				setNestedBeanPropertyValue(mapData, "doctoServico", data);
				setNestedBeanPropertyValue(mapData, "checkedAll", false);	
				setNestedBeanPropertyValue(mapData, "masterId", getElementValue("masterId"));	
				
				if (!isFromCarregamento()) {
					// Pega parâmetros da tela pai.
					var tabGroup = getTabGroup(window.dialogArguments.window.document);
					var tabDet = tabGroup.getTab("cad");
				    			
				    setNestedBeanPropertyValue(mapData, "manifesto.tpManifesto", tabDet.getFormProperty("tpManifesto"));
				    setNestedBeanPropertyValue(mapData, "manifesto.tpPreManifesto", tabDet.getFormProperty("tpPreManifesto"));
				    setNestedBeanPropertyValue(mapData, "manifesto.tpModal", tabDet.getFormProperty("tpModal"));
				    setNestedBeanPropertyValue(mapData, "manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega", tabDet.getFormProperty("controleCarga.rotaColetaEntrega.idRotaColetaEntrega"));
				    setNestedBeanPropertyValue(mapData, "filialByIdFilialDestino.idFilial", tabDet.getFormProperty("filialByIdFilialDestino.idFilial"));
				    setNestedBeanPropertyValue(mapData, "manifesto.cliente.idCliente", tabDet.getFormProperty("cliente.idCliente"));
					mapData.from = "manifesto";
				} else {
					mapData.from = "carregamento";
				}			
				
				var sdo = createServiceDataObject("lms.carregamento.manterGerarPreManifestoAction.getValidacaoDoctoServico", "validacaoDoctoServico", mapData);
				xmit({serviceDataObjects:[sdo]});				
			} else {
				resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
				setElementValue("doctoServico.nrDoctoServico", "");
				setDisabled("doctoServico.nrDoctoServico", true);
				setFocus("doctoServico.filialByIdFilialOrigem.sgFilial");
			}
		} else {
			alert(error);
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			setElementValue("doctoServico.nrDoctoServico", "");
			setDisabled("doctoServico.nrDoctoServico", true);
			setFocus("doctoServico.filialByIdFilialOrigem.sgFilial");			
		}
	}	
	
	/**
	 * Retorno da validação do documento
	 */
	function validacaoDoctoServico_cb(data, error) {
		if(data.mensagem) {
			if(data.confirmar) {
				var option = window.confirm(data.mensagem);
				if(option == false || option == null) {
					return;
				}
			} else {
				alert(data.mensagem);
				return;
			}
		}
	
		var dataMerged = new Array();			
		merge(dataMerged, buildFormBeanFromForm(window.parent.document.forms[0]));
		setNestedBeanPropertyValue(dataMerged, "idDoctoServico", getElementValue("doctoServico.idDoctoServico"));
		//merge(dataMerged, dialogArguments.window.preManifestoDocumentoGridDef.getSelectedIds());
	
		_serviceDataObjects = new Array();	
		
		var actionAddress = "lms.carregamento.manterGerarPreManifestoAction.savePreManifestoDocumento";
		
		//Verifica se a tela esta sendo chamada da tela de carregarVeiculoDocumentos...
		if (isFromCarregamento()) {
			actionAddress = "lms.carregamento.manterGerarPreManifestoAction.savePreManifestoDocumentoToCarregamento";
		} 
		
		addServiceDataObject(createServiceDataObject(actionAddress, "incluirDocumento", dataMerged)); 
		xmit(true); // deseja que o alert seja exibido, a função store_cb irá mostrar caso ocorra erro.
				
		return;
	}
	
	/**
	 * Resultado da adição de documento na grid pai.
	 */
	function incluirDocumento_cb(data, error, errorMsg, eventObj) {
		onDataLoad_cb(data, error, errorMsg, eventObj);
		
		if (!error) {
			dialogArguments.window.preManifestoDocumentoGridDef.executeSearch();
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			setElementValue("doctoServico.nrDoctoServico", "");
			setDisabled("doctoServico.nrDoctoServico", true);
			setFocus("doctoServico.filialByIdFilialOrigem.sgFilial");
		} else {
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			setElementValue("doctoServico.nrDoctoServico", "");
			setDisabled("doctoServico.nrDoctoServico", true);
			setFocus("doctoServico.filialByIdFilialOrigem.sgFilial");
		}
	}	
	
	/**
	 * Controla o objeto de Filial Origem do DoctoServico
	 */	
	function sgFilialOnChangeHandler() {
		if (getElementValue("doctoServico.filialByIdFilialOrigem.sgFilial") == "") {
			setElementValue("doctoServico.nrDoctoServico", "");
			setDisabled("doctoServico.nrDoctoServico", true);
		} else {
			setDisabled("doctoServico.nrDoctoServico", false);
		}
		return lookupChange({e:document.forms[0].elements["doctoServico.filialByIdFilialOrigem.idFilial"]});
	}	
	
	/**
	 * Carrega a combobox de tipo de documento e seta o valor default.
	 */
	function tpDocumentoServicoComboBox_cb(data) {
		comboboxLoadOptions({e:document.getElementById("doctoServico.tpDocumento"), data:data});
		
		if(getElementValue("doctoServico.tpDocumento") != "") {
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
		}
	
		if (getElementValue("manifesto.tpAbrangencia") == "I") {
			var comboBox = document.getElementById("doctoServico.tpDocumento");		
			for(var i=0; i < comboBox.length; i++) {		
				if(comboBox.options[i].value == "CRT") {
					comboBox.options[i].selected = "true";
				}
			}			
			setDisabled("doctoServico.tpDocumento", true);
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
		}
	}	
	
</script>