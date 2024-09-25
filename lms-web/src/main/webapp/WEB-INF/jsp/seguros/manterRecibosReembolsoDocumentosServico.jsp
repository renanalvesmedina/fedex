<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.seguros.manterRecibosReembolsoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/seguros/manterRecibosReembolso" idProperty="idReciboReembolsoDoctoServ" service="lms.seguros.manterRecibosReembolsoAction.findItemById" onDataLoadCallBack="dataLoad">

		<adsm:masterLink idProperty="idReciboReembolsoProcesso">
			<adsm:masterLinkItem property="nrReciboReembolso" label="numeroRecibo" itemWidth="50" />
			<adsm:masterLinkItem property="dtEmissao"         label="dataReembolso" itemWidth="50" />
		</adsm:masterLink>
		
		<adsm:hidden property="processoSinistro.idProcessoSinistro" serializable="true"/>
		
		<adsm:combobox 
			label="documentoServico"
			labelWidth="20%"
			width="80%" serializable="true"
			property="doctoServico.tpDocumentoServico"
			service="lms.seguros.manterRecibosReembolsoAction.findTpDocumentoServico"
			optionProperty="value" 
			optionLabelProperty="description"
			onchange="return changeDocumentWidgetType({
						documentTypeElement:this,
						filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						parentQualifier:'',
						documentGroup:'SERVICE',
						actionService:'lms.seguros.manterRecibosReembolsoAction'
			});">
		
			<adsm:lookup 
				dataType="text"
				property="doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="" serializable="true"
				disabled="true"
				action=""
				size="3" 
				maxLength="3" 
				picker="false" 
				popupLabel="pesquisarFilial"
				onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
							 });"
			/>
			<adsm:lookup 
				dataType="integer"
				property="doctoServico"
				idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
				service=""
				action=""
				popupLabel="pesquisarDocumentoServico"
				onDataLoadCallBack="retornoDocumentoServico"
				size="10" 
				maxLength="8" 
				serializable="true" 
				disabled="true" 
				required="true"
				mask="00000000" >
				</adsm:lookup>
				
		</adsm:combobox>
		
		<adsm:complement label="valorReembolsado" labelWidth="20%" width="80%" required="true" separator="branco"> 
		<adsm:textbox property="sgSimboloReembolso" dataType="text"  size="10" disabled="true"  serializable="false">
		<adsm:hidden property="moeda.idMoeda"/>
		<adsm:textbox property="vlReembolso" dataType="currency"    size="20" maxLength="20" required="true"/>
		</adsm:textbox>	
		</adsm:complement>				
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="button" caption="incluirDocumentosProcesso" boxWidth="220" onclick="incluirDocsProcesso()"/>
			<adsm:storeButton id="storeButton" caption="salvarItem" service="lms.seguros.manterRecibosReembolsoAction.saveItemSessao"/>
			<adsm:newButton   id="newButton"   caption="limpar"/>
		</adsm:buttonBar>
		<script>
			var LMS_22018 = '<adsm:label key="LMS-22018"/>'; 
		</script>
	</adsm:form>
	
	<adsm:grid property="reciboReembolsoDoctoServ" 
				idProperty="idReciboReembolsoDoctoServ" 
				selectionMode="check" 
				service="lms.seguros.manterRecibosReembolsoAction.findPaginatedItens"
				rowCountService="lms.seguros.manterRecibosReembolsoAction.getRowCountItens"
				detailFrameName="documentosServico"
				autoSearch="false"
				>
		<adsm:gridColumn property="tpDocumentoServico" title="documentoServico" dataType="text"  width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn property="sgFilialDocumento" dataType="text" title="" width="40" />
			<adsm:gridColumn property="nrDocumentoServico" dataType="integer" mask="00000000" title="" width="240" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="valorReembolsado" property="siglaSimboloReembolsado" width="60"/>
		<adsm:gridColumn title=""                 property="vlReembolsado"           width="150" align="right" dataType="currency"/>
		<adsm:gridColumn title="valorDocumento" property="siglaSimboloDocumento"  width="60"/>
		<adsm:gridColumn title=""               property="vlDocumento"            width="150" align="right" dataType="currency" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton" caption="excluirDocumento" service="lms.seguros.manterRecibosReembolsoAction.removeItens"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>


	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");
	var abaListagem = tabGroup.getTab("pesq");
	

	/****************************************************************************
 	 * Funções Gerais
	 ****************************************************************************/
	function pageLoad_cb() {
		onPageLoad_cb();	 
		document.getElementById('moeda.idMoeda').masterLink = 'true';
		document.getElementById('sgSimboloReembolso').masterLink = 'true';
		document.getElementById("processoSinistro.idProcessoSinistro").masterLink="true";
		
		if (dialogArguments.document.forms[0].elements["idProcessoSinistro"])
			setElementValue("processoSinistro.idProcessoSinistro", dialogArguments.document.forms[0].elements["idProcessoSinistro"].value);
	}
	 
	function dataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		if (error==undefined) {
			// habilita o documento de servico ao detalhar		
			disableDocumentoServico(false);
		}
	}
	
	function onDoctoPopupSetValue(data) {
		setElementValue('doctoServico.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data, ':sgFilialOrigem'));
	}

	function disableDocumentoServico(disable) {
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", disable);
			setDisabled("doctoServico.idDoctoServico", disable);
	}
	
	function initWindow(event) {
		// se ação de limpar, então seta o documento de servico para seu estado inicial (semi-bloqueado)
		if ((event.name=="newItemButton_click") || 
			(event.name=="storeItemButton") ||
			(event.name=="tab_click" && event.src.tabGroup.oldSelectedTab.properties.id=="pesq" ))
			disableDocumentoServico(true);
	}
	
	function incluirDocsProcesso() {			
		var data = new Object();
		
		data.idProcessoSinistro = abaListagem.getDocument().getElementById('processoSinistro.idProcessoSinistro').value;
		data.idReciboReembolsoProcesso = abaDetalhamento.getDocument().getElementById('idReciboReembolsoProcesso').value;
		data.idMoeda =  abaDetalhamento.getDocument().getElementById('moeda.idMoeda').value;
		
		var sdo = createServiceDataObject("lms.seguros.manterRecibosReembolsoAction.incluirDocumentosProcesso", "incluirDocsProcesso", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function incluirDocsProcesso_cb(data, error) {
		if (error==undefined) {
			reciboReembolsoDoctoServGridDef.executeSearch(
				{idReciboReembolsoProcesso:abaDetalhamento.getDocument().getElementById('idReciboReembolsoProcesso').value}, true
			);
		}
	}
	
	// INICIO JS da tag de doctoServico ********************************************

	function retornoDocumentoServico_cb(data) {
		doctoServico_nrDoctoServico_exactMatch_cb(data);
	}	
		
	function onTabShow(fromTab) {

		var elementoMoeda = abaDetalhamento.getDocument().getElementById('moeda.idMoeda');
		
		if (elementoMoeda.options[elementoMoeda.selectedIndex].value!='') {
			setElementValue('sgSimboloReembolso', elementoMoeda.options[elementoMoeda.selectedIndex].text);
			setElementValue('moeda.idMoeda', elementoMoeda.options[elementoMoeda.selectedIndex].value);		

		} else {
			alert(LMS_22018);
			tabGroup.selectTab(fromTab.properties.id, "tab_click");
		}
	}
		
</script>