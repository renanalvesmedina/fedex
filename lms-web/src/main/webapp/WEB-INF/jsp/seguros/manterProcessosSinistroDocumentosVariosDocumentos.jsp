
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.seguros.manterProcessosSinistroAction" onPageLoadCallBack="pageLoad" onPageLoad="loadData">
	<adsm:form action="/seguros/manterProcessosSinistroDocumentosVariosDocumentos" idProperty="idDoctoServico" id="formDocumentos">
	<adsm:section caption="pesquisarDocumentosServico"/>
	
	<!-- Lookup Manifesto -->
		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" labelWidth="15%" width="40%" serializable="false" 
					   service="lms.seguros.manterProcessosSinistroAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="limpaManifesto();
					   			 return changeDocumentWidgetType({
					   		documentTypeElement:this, 
					   		filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
					   		documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
					   		documentGroup:'MANIFESTO',
					   		actionService:'lms.seguros.manterProcessosSinistroAction'}); " >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false"
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
						 onchange="limpaManifesto();
						 		   return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	});" >						 	
			</adsm:lookup>

			<adsm:lookup dataType="integer" onDataLoadCallBack="retornoManifesto"
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" popupLabel="pesquisarManifesto"
						 afterPopupSetValue="manifestoAfterPopupSetValue"
						 onchange="return manifestoNrManifestoOrigem_OnChange();"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" >						 
			</adsm:lookup>
		</adsm:combobox>
		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:hidden property="manifesto.tpStatusManifesto" serializable="false"/>
		<adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" serializable="false" />
		<adsm:hidden property="manifesto.tpManifestoEntrega" value="EN" serializable="false"/>
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />
		
		
		<!-- Lookup Documento de Serviço -->
		<adsm:combobox property="doctoServico.tpDocumentoServico"
			label="documentoServico" labelWidth="15%" width="30%"
			service="lms.seguros.manterProcessosSinistroAction.findTpDoctoServico"
			optionProperty="value" optionLabelProperty="description"
			onchange="return changeDocumentWidgetType({
         documentTypeElement:this, 
         filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
         documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
         parentQualifier:'',
         documentGroup:'SERVICE',
         actionService:'lms.seguros.manterProcessosSinistroAction'
        });">

		<adsm:lookup dataType="text"
				property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial"
				criteriaProperty="sgFilial" service="" disabled="true" action=""
				size="3" maxLength="3" picker="false"
				onDataLoadCallBack="enableDoctoServico"
				onchange="return changeDocumentWidgetFilial({
       filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
       documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
       });" />

			<adsm:lookup dataType="integer" property="doctoServico"
				idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
				service="" action="" onDataLoadCallBack="retornoDocumentoServico"
				popupLabel="pesquisarDocumentoServico" size="10" maxLength="8"
				serializable="true" disabled="true" mask="00000000" />

		</adsm:combobox>	
		
		<!-- Lookup Controle de Carga -->
		<adsm:hidden property="filialByIdFilialOrigem.nmFilial"/>
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.seguros.manterProcessosSinistroAction.findLookupFilial" action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 onchange="return onFilialControleCargaChange();" onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="15%" width="40%" size="3" maxLength="3" picker="false" serializable="false">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.nmFilial" modelProperty="pessoa.nmFantasia" />
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.seguros.manterProcessosSinistroAction.findLookupControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="onControleCargaPopupSetValue" 
						 popupLabel="pesquisarControleCarga"
						 maxLength="8" size="8" mask="00000000" disabled="false">

				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false" inlineQuery="false"/>				
			</adsm:lookup>
		</adsm:lookup>
	
	<adsm:buttonBar freeLayout="true">
		<adsm:button  id="btnConsultar" caption="consultar" onclick="return validaFiltros();"/>
		<adsm:button id="newButton" caption="limpar" onclick="resetForm(this.document);"/>
	</adsm:buttonBar>
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
	</adsm:i18nLabels>
	
	</adsm:form>
	
	<adsm:grid property="doctoServico"
				idProperty="idDoctoServico" 
				selectionMode="true" 
				scrollBars="horizontal" 
				gridHeight="250"
				rows="12"
				onRowClick="disableGridClick"
				defaultOrder="doctoServico.tpDocumentoServico, doctoServico.filialByIdFilialOrigem.sgFilial, doctoServico.nrDoctoServico"
				service="lms.seguros.manterProcessosSinistroAction.findPaginatedDocumentosPopup"
				rowCountService="lms.seguros.manterProcessosSinistroAction.getRowCountDocumentosPopup">

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn property="tpDocumentoServico" title="documentoServico" dataType="text"  width="40"/>
			<adsm:gridColumn property="sgFilialDocumento" dataType="text" title="" width="40" />
			<adsm:gridColumn property="nrDocumentoServico" dataType="integer" mask="00000000" title="" width="40" align="right"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="destino" property="sgFilialDestino" width="60"/>
		
		<adsm:gridColumn title="dataEmissao" property="dataEmissao" width="140" align="center"/>
		
		<adsm:gridColumn property="vlMercadoria" title="valorMercadoria" dataType="currency" width="100"/>
		
		<adsm:gridColumn title="vol." property="qtVolumes" width="60" dataType="integer"/>
		
		<adsm:gridColumn title="peso" property="peso" width="60" align="right" unit="kg" mask="###,###,###,##0.000" dataType="decimal"/>
		
		<adsm:gridColumn title="remetente" property="remetente" width="180"/>
		
		<adsm:gridColumn title="destinatario" property="destinatario" width="180"/>
		
		<adsm:gridColumn title="devedor" property="devedor" width="180"/>		
		
		<adsm:buttonBar>
			<adsm:button buttonType="button" caption="utilizarTodosDocumentosLocalizados" boxWidth="245" onclick="selectAllDocuments();"/>
		    <adsm:button buttonType="button" caption="utilizarDocumentosSelecionados" boxWidth="230" onclick="selectedDocuments();"/>
			<adsm:button id="closeButton" caption="fechar" disabled="false" onclick="javascript:window.close();" buttonType="closeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script>

	function initWindow(e) {	
		if (e.name == 'cleanButton_click') {
			limparTagManifesto();
		}
	}
	
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data, error);
	}
	
	function loadData() {
		onPageLoad();
		setDisabled("btnConsultar", false);
		setDisabled("newButton", false);
	}
	
	function loadData_cb(data, error) {
		onPageLoad();
	}
	
	/*******************************************************************************************************************
	 * Início Manifesto
	 *******************************************************************************************************************/

	/**
	 * Limpa os campos relacionados ao manifesto
	 */
	function limparCamposRelacionadosManifesto() {
		resetValue('controleCarga.idControleCarga');
		resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
		limparTagManifesto();
	}
	
	/**
	 * Limpa os dados informados na tag manifesto
	 */
	function limparTagManifesto() {
		resetValue("manifesto.tpManifesto");
		resetValue("manifesto.idManifesto");
		resetValue("manifesto.filialByIdFilialOrigem.idFilial");
		resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
		desabilitaTagManifesto(true);
		disableControleCarga(true);
	}
	
	function limpaManifesto() {
		resetValue('manifesto.idManifesto');
		disableControleCarga(true);
	}
	
	function desabilitaTagManifesto(valor) {
		if (valor == true) {
			setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
			setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', true);
		}
		else {
			if (getElementValue('manifesto.idManifesto') != "" || getElementValue('manifesto.tpManifesto') != "")
				setDisabled('manifesto.filialByIdFilialOrigem.idFilial', false);
			if (getElementValue('manifesto.idManifesto') != "" || getElementValue('manifesto.filialByIdFilialOrigem.idFilial') != "")
				setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', false);
		}
	}
	
	/**
	 * Quando o "Manifesto" for informado
	 */
	function retornoManifesto_cb(data) {
		var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
		if (r == true) {
			var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
			setElementValue('manifesto.idManifesto', idManifesto);
			buscarManifesto(idManifesto);
		}
	}
	
	/**
	 * Busca os dados relacionados ao manifesto.
	 */
	function buscarManifesto(idManifesto) {
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findManifestoByRNC", 
					"resultado_buscarManifesto", {idManifesto:idManifesto});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Povoa os campos com os dados retornados da busca em manifesto
	 */
	function resultado_buscarManifesto_cb(data, error) {
		if (data != undefined) {
	
			setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.idControleCarga"));
			setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.nrControleCarga"));
			setElementValue('controleCarga.filialByIdFilialOrigem.idFilial', getNestedBeanPropertyValue(data,"0:controleCarga.filialByIdFilialOrigem.idFilial"));
			setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data,"0:controleCarga.filialByIdFilialOrigem.sgFilial"));
			format(document.getElementById("controleCarga.nrControleCarga"));
			disableControleCarga(false);
		}
	}
	
	/**
	 * Busca os dados relacionados ao Manifesto/Controle de cargas
	 */
	function buscarManifestoControleCargas(idDoctoServico) {
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findManifestoComControleCargas", 
					"resultado_buscarManifestoControleCargas", {idDoctoServico:idDoctoServico});
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Povoa os campos com os dados retornados da busca em Manifesto/Controle de cargas
	 */
	function resultado_buscarManifestoControleCargas_cb(data, error) {
		if (data == undefined) {
			return;
		}
		setElementValue('controleCarga.idControleCarga', data.idControleCarga);
		setElementValue('controleCarga.nrControleCarga', data.nrControleCarga); 
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.sgFilialControleCarga); 
		
		setElementValue('manifesto.idManifesto', data.idManifesto);
		setElementValue('manifesto.filialByIdFilialOrigem.sgFilial', data.sgFilialManifesto);
		setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', data.nrManifesto);
		setElementValue('manifesto.tpManifesto', data.tpManifesto);
	
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', data.veiculoFrota);
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', data.veiculoPlaca);
	
		if (getElementValue('manifesto.tpManifesto') != "") {
			changeDocumentWidget({documentDefinition:eval(getElementValue("manifesto.tpManifesto") + "_MANIFESTO_DOCUMENT_WIDGET_DEFINITION"), 
				  filialElement:document.getElementById("manifesto.filialByIdFilialOrigem.idFilial"), 
				  documentNumberElement:document.getElementById("manifesto.manifestoViagemNacional.idManifestoViagemNacional"), 
				  actionService:"lms.seguros.manterProcessosSinistroAction"
				  });  
		}
	
		format(document.getElementById("controleCarga.nrControleCarga"));
		format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	}
	
	
	function disableControleCarga(disable) {
		setDisabled('controleCarga.nrControleCarga', disable);
		if (disable==true)	{
			resetValue('controleCarga.idControleCarga');
			resetValue('controleCarga.filialByIdFilialOrigem.idFilial');			
		}
	}
	
	function manifestoNrManifestoOrigem_OnChange() {
		var r = manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
		if (getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
			resetaControleCarga();
			resetValue('manifesto.idManifesto');
		}
		return r;
	}
	
	function manifestoAfterPopupSetValue(data) {
		setDisabled('manifesto.manifestoViagemNacional.nrManifestoOrigem', false);
		var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		setElementValue('manifesto.idManifesto', idManifesto);
		buscarManifesto(idManifesto);
	}
		
	function enableManifestoManifestoViagemNacioal_cb(data) {
	   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
	      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	   }
	}
	
	function carregaManifesto(data) {
		if ( getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega") != undefined ) {
			setElementValue('manifesto.tpManifesto', 'EN');
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoEntrega.nrManifestoEntrega"));
			document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "00000000";
			format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
		else
		if ( getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem") != undefined) {
			setElementValue('manifesto.tpManifesto', 'VN');
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoViagemNacional.nrManifestoOrigem"));
			document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "00000000";
			format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
		else
		if ( getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt") != "" && getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt") != undefined) {
			setElementValue('manifesto.tpManifesto', 'VI');
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data, "manifesto.idManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data, "manifesto.manifestoInternacional.nrManifestoInt"));
			document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").mask = "000000";
			format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
	}
	
	function resetaControleCarga() {
		resetValue('manifesto.idManifesto');
		resetValue('controleCarga.idControleCarga');
		resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
	}
	
	/*******************************************************************************************************************
	 * Término Manifesto
	 *******************************************************************************************************************/


	/*************************************************************************************
	 * Controle de Cargas
	 *************************************************************************************/
	function onFilialControleCargaChange() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			return true;
			
		} else {
			disableNrControleCarga(false);
			return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
		}
	}
	

	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.nrControleCarga", disable);
	}
	
	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}

	function onControleCargaPopupSetValue(data) {
    	setDisabled('controleCarga.idControleCarga', false);
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', data.filialByIdFilialOrigem.sgFilial);				
	}
	

	/**
	 * javaScripts para a 'tag documents'
	 */
	function enableDoctoServico_cb(data) {
		var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if (r == true) {
			setDisabled("doctoServico.idDoctoServico", false);
			setFocus(document.getElementById("doctoServico.nrDoctoServico"));
		}
	}

	function retornoDocumentoServico_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
	}
	
	// LMS-6180
	function findDoctos(){
		var data = new Object();
		
		data,idManifesto = getElementValue("");
		data.idDoctoServico = getElementValue("");
		data.idControleCarga = getElementValue("");;
	}
	
		/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
	
	/**
	 * Captura os ids selecionados na grid.
	 */
	function selectedDocuments(){
	    
	    var ids = doctoServicoGridDef.getSelectedIds();
	    
		if (ids.ids.length >0){   
		    dialogArguments.window.setGridSelectedIds(ids);
       	    self.close();
       	}       	

	}
	
	/**
	 * Captura todos os ids da grid.
	 */
	function selectAllDocuments(){
		var data = new Object();
		var manifesto = new Object();
		var controleCarga = new Object();
		var doctoServico = new Object();
		
		manifesto.idManifesto = getElementValue("manifesto.idManifesto");
		controleCarga.idControleCarga = getElementValue("controleCarga.idControleCarga");
		doctoServico.idDoctoServico = getElementValue("doctoServico.idDoctoServico");
		
		data.manifesto = manifesto;
		data.controleCarga = controleCarga;
		data.doctoServico = doctoServico;
		
		var sdo = createServiceDataObject("lms.seguros.manterProcessosSinistroAction.findAllDocuments", 
					"selectAllDocuments", data);
	    xmit({serviceDataObjects:[sdo]});

	}
	
	function selectAllDocuments_cb(data, error){
		if (data != undefined && data != null){
		    dialogArguments.window.setGridSelectedIds(data);
       	    self.close();
       	}     
	}
	
	function validaFiltros(){
		
		if(getElementValue("doctoServico.idDoctoServico") == "" && getElementValue("manifesto.idManifesto") == "" && getElementValue("controleCarga.idControleCarga") == ""){
			alert("LMS-00055- " + i18NLabel.getLabel("LMS-00055"));
			return false;
		}
		
		return findButtonScript('doctoServico', document.getElementById("formDocumentos"));
		
	}
	
	function resetForm(doc){
	
		/** Reseta todos campos da tela */
		cleanButtonScript(doc);
	
		/** Reseta os dados da grid */
		doctoServicoGridDef.resetGrid();
	
		setDisabled("btnConsultar", false);
		setDisabled("newButton", false);

	}
	
</script>
