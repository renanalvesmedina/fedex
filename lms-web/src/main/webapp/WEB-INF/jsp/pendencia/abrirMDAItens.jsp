<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.abrirMDAAction">

	<adsm:form action="/pendencia/abrirMDA" idProperty="idItemMda" height="200"
			   service="lms.pendencia.abrirMDAAction.findByIdItemMda" onDataLoadCallBack="onDataLoadCallBack">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-17050"/>
		</adsm:i18nLabels>		   
			   
		<adsm:masterLink idProperty="idDoctoServico" showSaveAll="true">
			<adsm:masterLinkItem property="mdaConcatenado" label="mda"/>
		</adsm:masterLink>
	
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		
		<adsm:combobox label="documentoServico"
					   labelWidth="23%" width="77%"
					   property="doctoServico.tpDocumentoServico"
					   service="lms.pendencia.abrirMDAAction.findTipoDocumentoServico" 
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return doctoServicoTpDoctoServico_OnChange(this.value);">
								 
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
						 idProperty="idFilial" criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 onDataLoadCallBack="enableDoctoServico"
						 onchange="return doctoServicoFilialByIdFilialOrigem_OnChange(this.value);"
						 popupLabel="pesquisarFilial"/>
								   
			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service=""
						 action=""
						 onDataLoadCallBack="retornoDocumentoServico"
						 afterPopupSetValue="retornoPopupDoctoServico"
						 onchange="return verificarDocumentoCancelado(this.value)"
						 size="10" 
						 maxLength="9" 
						 serializable="true" 
						 disabled="true" 
						 mask="000000000"
						 popupLabel="pesquisarDocumentoServico"/>
		</adsm:combobox>

		<adsm:lookup property="naoConformidade.filial" idProperty="idFilial" 					 
					 criteriaProperty="sgFilial" 
					 service="lms.pendencia.abrirMDAAction.findLookupBySgFilial" 
					 action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" 
					 onDataLoadCallBack="disableNrNaoConformidade"
					 label="naoConformidade" dataType="text"
					 popupLabel="pesquisarFilial"
					 labelWidth="23%" width="32%" size="3" 
					 maxLength="3" picker="false" serializable="false">
		        <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
				<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
				
			<adsm:lookup property="naoConformidade" idProperty="idNaoConformidade" 							
						 criteriaProperty="nrNaoConformidade"
						 service="lms.pendencia.abrirMDAAction.findLookupNaoConformidade"
						 action="/rnc/manterNaoConformidade"
						 onDataLoadCallBack="loadNrNaoConformidade" 
						 onPopupSetValue="enableNrNaoConformidade"
						 onchange="return nrNaoConformidadeOnChangeHandler();" 
						 exactMatch="false" dataType="integer" size="15"
						 maxLength="8" mask="00000000" popupLabel="pesquisarNaoConformidade"> 
				<adsm:propertyMapping criteriaProperty="naoConformidade.filial.idFilial" modelProperty="filial.idFilial" disable="false"/>
				<adsm:propertyMapping criteriaProperty="naoConformidade.filial.sgFilial" modelProperty="filial.sgFilial" disable="false"/>
				<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="naoConformidade.filial.sgFilial" blankFill="false"/>						 
			</adsm:lookup>
		</adsm:lookup>

		<adsm:combobox label="naturezaMercadoria" property="naturezaProduto.idNaturezaProduto" 
					   optionProperty="idNaturezaProduto" optionLabelProperty="dsNaturezaProduto" 
					   service="lms.pendencia.abrirMDAAction.findNaturezaProduto"
					   required="true" labelWidth="23%" width="77%" onlyActiveValues="true"/>

		<adsm:hidden property="qtVolumesDoctoServico"/>
		<adsm:textbox property="qtVolumes" label="volumes" dataType="integer" 
					  size="8%" maxLength="6" labelWidth="23%" width="32%" required="true"
					  onchange="return validaVolumes()" />

		<adsm:hidden property="psItemDoctoServico"/>
		<adsm:textbox label="peso" property="psItem" dataType="weight" 
					  unit="kg" required="true" 
					  labelWidth="20%" width="25%" size="16" maxLength="16" 
					  onchange="return validaPeso()"/>

		<adsm:hidden property="vlMercadoriaDoctoServico"/>
		<adsm:hidden property="moeda.siglaSimbolo" />					   
		<adsm:combobox label="valorMercadoria" property="moeda.idMoeda" 
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   service="lms.pendencia.abrirMDAAction.findMoeda"
					   labelWidth="23%" width="12%" boxWidth="85">
			<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="siglaSimbolo" />			
			<adsm:textbox property="vlMercadoria" dataType="currency" width="17%"
					  mask="#,###,###,###,###,##0.00" required="true" 
					  size="16" maxLength="18"
					  onchange="return validaValorMercadoria()"/>
		</adsm:combobox>
		
		<adsm:textarea property="dsMercadoria" label="discriminacaoMercadoria" maxLength="200" 
					   columns="80" rows="3" labelWidth="23%" width="77%" required="true"/>

		<adsm:pairedListbox property="nfItemMdas" 
							size="9" boxWidth="90" label="notasFiscaisLabel" labelWidth="23%" width="27%" 
							optionProperty="idNfItemMda" optionLabelProperty="nrNotaFiscal" 
							sourceOptionProperty="idNotaFiscalConhecimento" />

		<adsm:textarea property="obItemMda" label="observacoes" maxLength="200" columns="80" 
					   rows="3" labelWidth="23%" width="77%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarItem" id="salvarItemMdaButton" callbackProperty="salvarItemMda" 
							  service="lms.pendencia.abrirMDAAction.saveItemMda" />
			<adsm:button caption="limpar" id="newButton" buttonType="newButton" onclick="newButtonClick()"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="itemMda" idProperty="idItemMda" selectionMode="check" gridHeight="82" 
			   unique="true" scrollBars="horizontal" rows="4" detailFrameName="itens" onRowClick="onRowClick" 
			   service="lms.pendencia.abrirMDAAction.findPaginatedItemMda" autoSearch="false"
			   rowCountService="lms.pendencia.abrirMDAAction.getRowCountItemMda">
			   			   
		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" isDomain="true" width="30"/>
        <adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        	<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="30" />
            <adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="000000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="150" />
		<adsm:gridColumn title="discriminacaoMercadoria" property="dsMercadoria" width="200" />		
        <adsm:gridColumnGroup separatorType="MANIFESTO">
        	<adsm:gridColumn title="naoConformidade" property="naoConformidade.filial.sgFilial" width="40" />
            <adsm:gridColumn title="" property="naoConformidade.nrNaoConformidade" width="80" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="volumes" property="qtVolumes" width="100" align="right" />
		<adsm:gridColumn title="peso" property="psItem" width="100" unit="kg" dataType="decimal" mask="###,###,##0.000"/>
        <adsm:gridColumnGroup customSeparator=" ">
        	<adsm:gridColumn title="valorMercadoria" property="moeda.sgMoeda" dataType="text" width="30"/>
            <adsm:gridColumn title="" property="moeda.dsSimbolo" width="30"/>
        </adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlMercadoria" dataType="decimal" mask="#,###,###,###,###,##0.00" width="100" align="right"/>	
		<adsm:gridColumn title="observacoes" property="obItemMda" width="250" />
	
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirItem" service="lms.pendencia.abrirMDAAction.removeByIdsItemMda" />
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">
	// Pega a aba 'cad' para pegar os valores dos properties
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("mda");

	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		var idDoctoServico = tabDet.getFormProperty("idDoctoServico");
		if(idDoctoServico != null && idDoctoServico != "") {
			setDisabledDocument(this.document, true);
		} else {
			setDisabled("newButton", false);
			if (eventObj.name=='tab_click'){
				if(getElementValue("naoConformidade.idNaoConformidade")!="" && document.getElementById("naoConformidade.idNaoConformidade").disabled==false){
					setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), false);
				}
			} else {
				setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), true, undefined, true);
			}
		
			// Seta o label para o campo valor de mercadoria
			document.getElementById("moeda.idMoeda").required = "true";			

		}
	}

	/**
	 * Retorno do Salvamento do registro de Item MDA
	 */
	function salvarItemMda_cb(data, error, errorMsg, eventObj) {
		if(error){
			alert(error);
			return false;
		}
		storeItem_cb(data, error, errorMsg, eventObj);
  	    resetListBoxValue(document.getElementById("_nfItemMdas_source"));
		setDisabled("moeda.idMoeda", false);
	}
	
	/**
	 * Função para limpar a tela.
	 */
	function newButtonClick() {
  	    resetListBoxValue(document.getElementById("_nfItemMdas_source"));	
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);	
		setDisabled("moeda.idMoeda", false);
		setDisabled("doctoServico.tpDocumentoServico", false);
		setDisabled("naoConformidade.filial.sgFilial", false);		
    	setDisabled("naoConformidade.idNaoConformidade", false);
		setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), true, undefined, true);
		cleanButtonScript(this.document);
		var tabGroup = getTabGroup(this.document);
		var itemTab = tabGroup.selectedTab;
		itemTab.setChanged(false);
		itemTab.itemTabChanged = false;
		
		setFocus("doctoServico.tpDocumentoServico");
	}


	/**
	 * #############################################################
	 * # Inicio das funções para a tag customizada de DoctoServico #
	 * #############################################################
	 */

	/**
	 * Controla as tags aninhadas para habilitar/desabilitar
	 */
	function enableDoctoServico_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if (r == true) {
	    	setDisabled("doctoServico.idDoctoServico", false);
	      	setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   	}
	}
	
	function retornoDocumentoServico_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		var boolean = doctoServico_nrDoctoServico_exactMatch_cb(data);
		if (boolean == true) {
			if (data != undefined && data.length > 0) {
				setElementValue("doctoServico.idDoctoServico", data[0].idDoctoServico);
				resetListBoxValue(document.getElementById("_nfItemMdas_source"));
				buscaDadosTela(data[0]);
			}
		}
	}
	
	/**
	 * Realiza pesquisa para os dados necessarios da tela.
	 * Esta função não é obrigatória.
	 */
	function buscaDadosTela(data) { 
		if(data.naturezaProduto != undefined) {
			setElementValue("naturezaProduto.idNaturezaProduto", data.naturezaProduto.idNaturezaProduto);
		}		
		setElementValue("qtVolumes", data.qtVolumes);
		setElementValue("psItem", setFormat(document.getElementById("psItem"), data.psReal));
		setElementValue("moeda.idMoeda", data.moeda.idMoeda);
		setElementValue("moeda.siglaSimbolo", data.moeda.siglaSimbolo);
		setDisabled("moeda.idMoeda", true);
		setElementValue("vlMercadoria", setFormat(document.getElementById("vlMercadoria"), data.vlMercadoria));
		
		setElementValue("qtVolumesDoctoServico", getElementValue("qtVolumes"));
		setElementValue("psItemDoctoServico", getElementValue("psItem"));
		setElementValue("vlMercadoriaDoctoServico", getElementValue("vlMercadoria"));
		
		if(data.notaFiscalConhecimentos != undefined) {
			nfItemMdas_source_cb(data.notaFiscalConhecimentos);
		}
		
		// Busca os dados de NaoConformidade a partir do ID do DoctoServico
		var mapCriteria = new Array();	   
	   	setNestedBeanPropertyValue(mapCriteria, "idDoctoServico", getElementValue("doctoServico.idDoctoServico"));		
		var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.getDadosNaoConformidade", "resultado_buscaDadosTela", 
										  mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da pesquisa dos dados da tela.
	 * Esta função não é obrigatória. 
	 */
	function resultado_buscaDadosTela_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		resetValue("naoConformidade.filial.sgFilial");
		resetValue("naoConformidade.idNaoConformidade");		
		if(data.idNaoConformidade != undefined) {			
			setElementValue("naoConformidade.idNaoConformidade", data.idNaoConformidade);
			setElementValue("naoConformidade.filial.sgFilial", data.sgFilial);
			setElementValue("naoConformidade.nrNaoConformidade", 
			setFormat(document.getElementById("naoConformidade.nrNaoConformidade"), data.nrNaoConformidade));
		}
		setFocus("doctoServico.tpDocumentoServico");
		setDisabled("naoConformidade.idNaoConformidade", true);
		setDisabled("naoConformidade.filial.idFilial", true);
	}

	function doctoServicoTpDoctoServico_OnChange(valor){
		var boolean = changeDocumentWidgetType({
								 documentTypeElement:document.getElementById('doctoServico.tpDocumentoServico'),
								 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
								 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
								 documentGroup:'SERVICE',
								 actionService:'lms.pendencia.abrirMDAAction'});
		doctoServicoFilialByIdFilialOrigem_OnChange("");
		return boolean;
	}
	
	function doctoServicoFilialByIdFilialOrigem_OnChange(valor) {
		var boolean = changeDocumentWidgetFilial( { 
											filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"),
								      		documentNumberElement:document.getElementById("doctoServico.idDoctoServico") } );
		
		if (valor == "") {
			var tpDocumentoServico = getElementValue("doctoServico.tpDocumentoServico");
       	    resetValue(this.document);
       	    resetListBoxValue(document.getElementById("_nfItemMdas_source"));
			setDisabled("naoConformidade.filial.sgFilial", false);
			setDisabled("moeda.idMoeda", false);
    		setDisabled("naoConformidade.idNaoConformidade", false);
			setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), true, undefined, true);
       	    setElementValue("doctoServico.tpDocumentoServico", tpDocumentoServico);
		}
		
		return boolean;		
	}	
	
	function doctoServicoNrDoctoServico_OnChange(valor) {
		var retorno = doctoServico_nrDoctoServicoOnChangeHandler();
		
		if (valor == "") {
			limparCampos();
		}
		
		return retorno;		
	}	
	
	function limparCampos(){
		var tpDocumentoServico = getElementValue("doctoServico.tpDocumentoServico");
		var idFilial = getElementValue("doctoServico.filialByIdFilialOrigem.idFilial");
	    var sgFilial = getElementValue("doctoServico.filialByIdFilialOrigem.sgFilial");
	    
   	    resetValue(this.document);
   	    resetListBoxValue(document.getElementById("_nfItemMdas_source"));
   	    
		setDisabled("naoConformidade.filial.sgFilial", false);
		setDisabled("naoConformidade.idNaoConformidade", false);
		setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), true, undefined, true);
		setDisabled("moeda.idMoeda", false);
		
   	    setElementValue("doctoServico.tpDocumentoServico", tpDocumentoServico);
       	setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", idFilial);
        setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", sgFilial);
	}
	
	function retornoDocumentoCancelado_cb(data){
		var valor = data.nrDoctoServico;
		var retorno = doctoServico_nrDoctoServicoOnChangeHandler();
		
		if (data.isDocumentoCancelado == "true"){
			var msgDocumentoCancelado = i18NLabel.getLabel('LMS-17050');
			alert('LMS-17050 - ' + msgDocumentoCancelado);
			limparCamposAposMsg();			
       	    return false;	
		}
		
		if (valor == "") {
			limparCampos();
		}
		
		return retorno;	
	}
	
	function limparCamposAposMsg(){
		resetValue(this.document);
   	 	resetListBoxValue(document.getElementById("_nfItemMdas_source"));	
   	    setDisabled("naoConformidade.filial.sgFilial", false);
		setDisabled("moeda.idMoeda", false);
		setDisabled("naoConformidade.idNaoConformidade", false);
		setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), true, undefined, true);
   	    setDisabled("doctoServico.tpDocumentoServico", false);
   	 	setFocus("doctoServico.tpDocumentoServico");   	 
	}
	
	function verificarDocumentoCancelado(valor){
		if (valor != ""){	
			var idFilialById = document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial').value;
			var data = {nrDoctoServico : valor, 
						idFilialOrigem : idFilialById};
			
			var service = "lms.pendencia.abrirMDAAction.findDocumentoCancelado";
			var sdo = createServiceDataObject(service, "retornoDocumentoCancelado", data);
			xmit({serviceDataObjects:[sdo]});	
		}	
	}
		
	/**
	 * ##########################################################
	 * # Fim das funções para a tag customizada de DoctoServico #
	 * ##########################################################
	 */


	/**
	 * #####################################################
	 * # Inicio das funções para a tag de Não Conformidade #
	 * #####################################################
	 */	
	 
	function sgFilialOnChangeHandler() {
		if (getElementValue("naoConformidade.filial.sgFilial") == "") {
			disableNrNaoConformidade(true);
			resetValue("naoConformidade.idNaoConformidade");
		} else {
			disableNrNaoConformidade(false);
		}
		var x = lookupChange({e:document.forms[0].elements["naoConformidade.filial.idFilial"]});
		nrNaoConformidadeOnChangeHandler();
		return x;
	}
	
	function nrNaoConformidadeOnChangeHandler() {
		var sgFilial = getElementValue("naoConformidade.filial.sgFilial");
		var idFilial = getElementValue("naoConformidade.filial.idFilial");
		if (getElementValue("naoConformidade.nrNaoConformidade") == "") {
			newButtonClick();
			setElementValue("naoConformidade.filial.sgFilial", sgFilial);
			setElementValue("naoConformidade.filial.idFilial", idFilial);
			disableNrNaoConformidade(false);
			lookupChange({e:document.forms[0].elements["naoConformidade.filial.idFilial"]});
		}
		
		return lookupChange({e:document.forms[0].elements["naoConformidade.idNaoConformidade"]});
	}
	
	
	function disableNrNaoConformidade_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		if (data.length==0) {
			disableNrNaoConformidade(false);
		}
		return lookupExactMatch({e:document.getElementById("naoConformidade.filial.idFilial"), data:data});		
	}
	
	function loadNrNaoConformidade_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		naoConformidade_nrNaoConformidade_exactMatch_cb(data);
		if (data[0]!=undefined) {
			resetValue("doctoServico.tpDocumentoServico");
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			resetValue("doctoServico.idDoctoServico");
			resetListBoxValue(document.getElementById("_nfItemMdas_source"));
			setDisabled("doctoServico.tpDocumentoServico", true);
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);
			if (data[0].doctoServico != undefined) {
				setElementValue("doctoServico.idDoctoServico", data[0].doctoServico.idDoctoServico);
				setElementValue("doctoServico.tpDocumentoServico", data[0].doctoServico.tpDocumentoServico);
				setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", data[0].doctoServico.filialByIdFilialOrigem.idFilial);
				setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", data[0].doctoServico.filialByIdFilialOrigem.sgFilial);
				setElementValue("doctoServico.nrDoctoServico",  setFormat(document.getElementById("doctoServico.nrDoctoServico"), data[0].doctoServico.nrDoctoServico));
				
				
				if (data[0].doctoServico.notaFiscalConhecimentos){
					nfItemMdas_source_cb(data[0].doctoServico.notaFiscalConhecimentos);
				}
				if (data[0].doctoServico.naturezaProduto){
					setElementValue("naturezaProduto.idNaturezaProduto", data[0].doctoServico.naturezaProduto.idNaturezaProduto);
				}
				
				setElementValue("qtVolumes", data[0].doctoServico.qtVolumes);
				setElementValue("psItem", setFormat(document.getElementById("psItem"), data[0].doctoServico.psReal));
				setElementValue("moeda.idMoeda", data[0].doctoServico.moeda.idMoeda);
				setElementValue("moeda.siglaSimbolo", data[0].doctoServico.moeda.siglaSimbolo);
				setDisabled("moeda.idMoeda", true);
				setElementValue("vlMercadoria", setFormat(document.getElementById("vlMercadoria"), data[0].doctoServico.vlMercadoria));
				
				setElementValue("qtVolumesDoctoServico", getElementValue("qtVolumes"));
				setElementValue("psItemDoctoServico", getElementValue("psItem"));
				setElementValue("vlMercadoriaDoctoServico", getElementValue("vlMercadoria"));				
			}
			
			setElementValue("naoConformidade.filial.sgFilial", data[0].filial.sgFilial);
		}
	}
	
	function disableNrNaoConformidade(disable) {
		setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), disable, undefined, true);
	}
	
	function enableNrNaoConformidade(data){
		if (data.nrNaoConformidade!=undefined) {
			resetValue("doctoServico.tpDocumentoServico");
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			resetValue("doctoServico.idDoctoServico");
			setDisabled("doctoServico.tpDocumentoServico", true);
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);	
			if (data.doctoServico != undefined) {
				setElementValue("doctoServico.idDoctoServico", data.doctoServico.idDoctoServico);
				setElementValue("doctoServico.tpDocumentoServico", data.doctoServico.tpDocumentoServico);
				setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", data.doctoServico.filialByIdFilialOrigem.idFilial);
				setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", data.doctoServico.filialByIdFilialOrigem.sgFilial);
				setElementValue("doctoServico.nrDoctoServico",  setFormat(document.getElementById("doctoServico.nrDoctoServico"), data.doctoServico.nrDoctoServico));

				if (data.doctoServico.tpDocumentoServico.value=="CTR" || data.doctoServico.tpDocumentoServico.value=="CTE"){
					var mapCriteria = new Array();	   
		   			setNestedBeanPropertyValue(mapCriteria, "idDoctoServico", data.doctoServico.idDoctoServico);
					var sdo1 = createServiceDataObject("lms.pendencia.abrirMDAAction.findNaturezaProdutoByIdConhecimento", "resultado_naturezaProduto", mapCriteria);		
					var sdo2 = createServiceDataObject("lms.pendencia.abrirMDAAction.findNotasFiscaisConhecimento", "resultado_notasFiscais", mapCriteria);
					xmit({serviceDataObjects:[sdo1, sdo2]});
				}
				
				setElementValue("qtVolumes", data.doctoServico.qtVolumes);
				setElementValue("psItem", setFormat(document.getElementById("psItem"), data.doctoServico.psReal));
				setElementValue("moeda.idMoeda", data.doctoServico.moeda.idMoeda);
				setElementValue("moeda.siglaSimbolo", data.doctoServico.moeda.sgMoeda + " " + data.doctoServico.moeda.dsSimbolo);
				setDisabled("moeda.idMoeda", true);
				setElementValue("vlMercadoria", setFormat(document.getElementById("vlMercadoria"), data.doctoServico.vlMercadoria));
				
				setElementValue("qtVolumesDoctoServico", getElementValue("qtVolumes"));
				setElementValue("psItemDoctoServico", getElementValue("psItem"));
				setElementValue("vlMercadoriaDoctoServico", getElementValue("vlMercadoria"));				
			}			
			disableNrNaoConformidade(false);
		} else {
			disableNrNaoConformidade(true);
		}
	}
	
	function resultado_notasFiscais_cb(data, error){
		if(error){
			alert(error);
			return false;
		}
		if (data!=undefined){
			nfItemMdas_source_cb(data);
		}
	}
	
	function resultado_naturezaProduto_cb(data, error){
		if(error){
			alert(error);
			return false;
		}
		if (data!=undefined){
			setElementValue("naturezaProduto.idNaturezaProduto", data.idNaturezaProduto);
		}
	}
	
	function retornoPopupDoctoServico(data){
		var retorno = lookupChange({e:document.getElementById("doctoServico.idDoctoServico"), forceChange:true});
		setDisabled("doctoServico.tpDocumentoServico", false);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
		setDisabled("doctoServico.idDoctoServico", false);
		return retorno; 
		
	}
	
	/**
	 * ##################################################
	 * # Fim das funções para a tag de Não Conformidade #
	 * ##################################################
	 */	
	 
	 
	// Função que valida o campo Volumes.
	function validaVolumes() {		
		if(getElementValue("doctoServico.idDoctoServico") != "" && getElementValue("qtVolumes") != "") {
			var mapCriteria = new Array();
			setNestedBeanPropertyValue(mapCriteria, "campo", "Volumes");
		   	setNestedBeanPropertyValue(mapCriteria, "valorDoctoServico", getElementValue("qtVolumesDoctoServico"));
		   	setNestedBeanPropertyValue(mapCriteria, "valorInformado", getElementValue("qtVolumes"));
		   	
			var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.validateCamposDoctoServico", "validaVolumes", 
											  mapCriteria);
			return xmit({serviceDataObjects:[sdo]});
		}
		return true;
	}
	
	// Função de retorno do validaVolumes.
	function validaVolumes_cb(data, error) {
	 	if(error) {
	 		alert(error);
	 		setElementValue("qtVolumes", "");
	 		setFocus("qtVolumes");
	 		return false;
	 	}	 	
	 	return true;
	}	
	
	// Função que valida o campo Peso.
	function validaPeso() {
		if(getElementValue("doctoServico.idDoctoServico") != "" && getElementValue("psItem") != "") {
			var mapCriteria = new Array();
			setNestedBeanPropertyValue(mapCriteria, "campo", "Peso");
		   	setNestedBeanPropertyValue(mapCriteria, "valorDoctoServico", getElementValue("psItemDoctoServico"));
		   	setNestedBeanPropertyValue(mapCriteria, "valorInformado", getElementValue("psItem"));
		   	
			var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.validateCamposDoctoServico", "validaPeso", 
											  mapCriteria);
			return xmit({serviceDataObjects:[sdo]});
		}
		return true;
	}
	
	// Função de retorno do validaPeso.
	function validaPeso_cb(data, error) {
	 	if(error) {
	 		alert(error);
	 		setElementValue("psItem", "");
	 		setFocus("psItem");
	 		return false;
	 	}	 	
	 	return true;
	}	
	
	// Função que valida o campo Valor Mercadoria.
	function validaValorMercadoria() {
		if(getElementValue("doctoServico.idDoctoServico") != "" && getElementValue("vlMercadoria") != "") {
			var mapCriteria = new Array();
			setNestedBeanPropertyValue(mapCriteria, "campo", "Valor Mercadoria");
			setNestedBeanPropertyValue(mapCriteria, "siglaSimbolo", getElementValue("moeda.siglaSimbolo"));
		   	setNestedBeanPropertyValue(mapCriteria, "valorDoctoServico", getElementValue("vlMercadoriaDoctoServico"));
		   	setNestedBeanPropertyValue(mapCriteria, "valorInformado", getElementValue("vlMercadoria"));
		   	
			var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.validateCamposDoctoServico", "validaValorMercadoria", 
											  mapCriteria);
			return xmit({serviceDataObjects:[sdo]});
		}
		return true;
	}	 
	
	// Função de retorno do validaValorMercadoria.
	function validaValorMercadoria_cb(data, error) {
	 	if(error) {
	 		alert(error);	 		
	 		setElementValue("vlMercadoria", "");
	 		setFocus("vlMercadoria");
	 		return false;
	 	}	 	
	 	return true;
	}
	
	/**
	 * Função que manipula o rowClick na grid.
	 */
	function onRowClick(id) {
		var idDoctoServico = tabDet.getFormProperty("idDoctoServico");
		if(idDoctoServico != null && idDoctoServico != "") {
			return false;
		} else {
			return true;
		}
	}	 
	
	
	function onDataLoadCallBack_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}

		resetListBoxValue(document.getElementById("_nfItemMdas_source"));
		if (data.notaFiscalConhecimentoSource){
			nfItemMdas_source_cb(data.notaFiscalConhecimentoSource);
		}
		
		onDataLoad_cb(data, error);

		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);	
		setDisabled("doctoServico.tpDocumentoServico", true);
		setDisabled("naoConformidade.filial.sgFilial", true);		
    	setDisabled("naoConformidade.idNaoConformidade", true);
		setDisabled("moeda.idMoeda", true);
		setDisabled(document.getElementById("naoConformidade.nrNaoConformidade"), true, undefined, true);
		setFocusOnFirstFocusableField();
		
	}
</script>