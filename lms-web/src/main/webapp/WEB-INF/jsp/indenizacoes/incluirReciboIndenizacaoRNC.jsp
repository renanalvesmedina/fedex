<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.indenizacoes.incluirReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
    	<adsm:include key="LMS-21017"/>
    </adsm:i18nLabels>
	<adsm:form action="/indenizacoes/incluirReciboIndenizacao" height="190" idProperty="idDoctoServicoIndenizacao" service="lms.indenizacoes.incluirReciboIndenizacaoAction.findDocumentoRNCByKey" onDataLoadCallBack="dataLoad">

	    <adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="true">
		    <adsm:masterLinkItem property="reciboIndenizacao" label="RIM"/>
	    </adsm:masterLink>
	    <adsm:hidden property="idCliente"/>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   service="lms.indenizacoes.manterReciboIndenizacaoAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   label="documentoServico" labelWidth="20%" width="80%" serializable="false"
					   required="false"
					   onchange="resetDoctoRelateds();
					   		return changeDocumentWidgetType({
					   		documentTypeElement:this, 
					   		filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					   		documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					   		documentGroup:'SERVICE',
					   		actionService:'lms.indenizacoes.incluirReciboIndenizacaoAction'
					   	});" onchangeAfterValueChanged="true">

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 onDataLoadCallBack="enableNaoConformidadeDoctoServico"
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false"
 						 onchange="resetDoctoRelateds();
							 return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						  }); "/>

			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 mask="00000000"
						 afterPopupSetValue="onDoctoServicoAfterPopupSetValue"
						 onDataLoadCallBack="onDoctoServicoDataCallback"
						 onchange="resetDoctoRelateds(); return doctoServicoNrDoctoServico_OnChange();"
						 size="10" serializable="true" disabled="true" />
		</adsm:combobox>



		<adsm:complement labelWidth="20%" width="30%" separator="branco" label="rnc">
			<adsm:textbox property="naoConformidade.filial.sgFilial" dataType="text" disabled="true" size="3" />
			<adsm:textbox property="naoConformidade.nrRnc" dataType="text" disabled="true" mask="00000000" size="8"/>
		</adsm:complement>		
		<adsm:hidden  property="naoConformidade.idNaoConformidade"/>
		
		<adsm:combobox label="tipoProduto" 
						property="produto.idProduto" 
						optionProperty="idProduto"
						optionLabelProperty="dsProduto"
						service="" autoLoad="false"
						required="false" 
						labelWidth="20%" width="80%">		
		</adsm:combobox>

		<adsm:complement label="valorIndenizacao" width="80%" labelWidth="20%" separator="branco" required="true">
		<adsm:combobox property="moeda.idMoeda" 
					   boxWidth="85"
					   service="lms.indenizacoes.incluirReciboIndenizacaoAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   onlyActiveValues="true"  disabled="true" defaultValue="1">
			<adsm:textbox property="vlIndenizacao" dataType="currency" required="false" disabled="false"/>
		</adsm:combobox>
		</adsm:complement>
		<adsm:hidden property="idMoedaPadrao" serializable="false"/>
		
		
		<adsm:textbox dataType="integer" label="quantidadeVolumes" disabled="true" property="qtVolumesIndenizados" size="6" maxLength="6" labelWidth="20%" width="80%"/>

		<adsm:pairedListbox label="notasFiscais" 
							property="notasFiscais" 
							optionProperty="idNotaFiscalConhecimento" 
							optionLabelProperty="nrNotaFiscal" 
							service="" 
							size="6" 
							boxWidth="91" 
							required="false" 
							width="30%" 
							labelWidth="20%" />

		<adsm:combobox label="ocorrencias" 
						property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"
						optionProperty="idOcorrencia" 						
						optionLabelProperty="dsMotivo" 
						service="" autoLoad="false"
						required="true" 
						onchange="findQtde();"
						width="80%" labelWidth="20%"> 		
		</adsm:combobox>

		<adsm:combobox label="rotaSinistro" 
						property="rotasId"
						optionProperty="idFilialOrigemDestino"				
						optionLabelProperty="labelFilialOrigemDestino" 
						service="" autoLoad="false"
						required="false" 
						width="80%" labelWidth="20%">		
		</adsm:combobox>


		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="salvarDocumentoRNC" buttonType="storeButton" onclick="onStoreButtonClick();" />
			<adsm:button id="btnOcorrencias" caption="rnc" action="/rnc/manterOcorrenciasNaoConformidade" cmd="main" >
				<adsm:linkProperty src="naoConformidade.filial.sgFilial" target="naoConformidade.filial.sgFilial" />
				<adsm:linkProperty src="naoConformidade.nrRnc" target="naoConformidade.nrNaoConformidade" />
				<adsm:linkProperty src="naoConformidade.idNaoConformidade" target="naoConformidade.idNaoConformidade" />
				<adsm:linkProperty src="doctoServico.idDoctoServico" target="naoConformidade.doctoServico.idDoctoServico" />
				<adsm:linkProperty src="doctoServico.tpDocumentoServico" target="naoConformidade.doctoServico.tpDocumentoServico.description" />
				<adsm:linkProperty src="doctoServico.filialByIdFilialOrigem.sgFilial" target="naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial" />
				<adsm:linkProperty src="doctoServico.nrDoctoServico" target="naoConformidade.doctoServico.nrDoctoServico" />
				<adsm:linkProperty src="moeda.idMoeda" target="naoConformidade.doctoServico.moeda.idMoeda" />
				<adsm:linkProperty src="idCliente" target="naoConformidade.clienteByIdClienteRemetente.idCliente" />
			</adsm:button>
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid  
		property="doctoServicoIndenizacao" 
		idProperty="idDoctoServicoIndenizacao" 
		service="lms.indenizacoes.incluirReciboIndenizacaoAction.findPaginatedDocumentosRNC"
		rowCountService="lms.indenizacoes.incluirReciboIndenizacaoAction.getRowCountDocumentosRNC"
		selectionMode="check" gridHeight="99" 
		detailFrameName="rnc"		
		scrollBars="both" 		
		unique="true" 
		rows="30">
	
		<adsm:gridColumn property="tpDoctoServico" isDomain="true" title="documentoServico" dataType="text"  width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn property="sgFilialOrigemDocto" dataType="text" title="" width="40" />
			<adsm:gridColumn property="nrDoctoServico" dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="sgFilialDestinoDocto"      title="destino" width="60"/>
		<adsm:gridColumn property="nmClienteRemetente"        title="remetente" width="200"/>
		<adsm:gridColumn property="nmClienteDestinatario"     title="destinatario" width="200"/>

		<adsm:gridColumn property="sgSimboloTotalDocto"       title="valorDocumentoServico" width="50" align="left"                      />
		<adsm:gridColumn property="vlTotalDoctoServico"       title=""                      width="120" align="right" dataType="currency"/>		

		<adsm:gridColumn property="sgSimboloVlIndenizado"     title="valorIndenizado" width="50"  align="left"                     />
		<adsm:gridColumn property="vlIndenizado"              title=""                width="120" align="right" dataType="currency"/>
		
		<adsm:gridColumn property="qtVolumes"                 title="quantidadeVolumes" width="90" align="right" dataType="integer"/>
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="sgFilialControleCarga" title="controleCarga" width="50" />	
			<adsm:gridColumn property="nrControleCarga"       title=""  width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="MANIFESTO">
		<adsm:gridColumn property="sgFilialManifesto"         title="manifesto" width="50" />
		<adsm:gridColumn property="nrManifesto"               title=""  width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="dsProduto"                 title="tipoProduto" width="150"/>
		<adsm:gridColumnGroup separatorType="RNC">
			<adsm:gridColumn property="sgFilialNaoConformidade" dataType="text" title="RNC" width="40" />
			<adsm:gridColumn property="nrNaoConformidade"       dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:buttonBar>
			<adsm:button id="calculateButton" caption="calcularIndenizacaoParcial" buttonType="resetButton" onclick="calculaIndenizacaoParcial();" disabled="true"/>
			<adsm:removeButton caption="excluirDocumentoRNC" service="lms.indenizacoes.incluirReciboIndenizacaoAction.removeDocumentosRNCByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	document.getElementById('idCliente').masterLink='true';
	document.getElementById('idMoedaPadrao').masterLink='true';
	document.getElementById('doctoServico.filialByIdFilialOrigem.sgFilial').required='true';
	document.getElementById('doctoServico.nrDoctoServico').required='true';	
	setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', false);
	setDisabled('doctoServico.idDoctoServico', true);

   	var tabGroup = getTabGroup(this.document);
	var abaRim = tabGroup.getTab("rim");
	
	function calculaIndenizacaoParcial() {
		var data = new Array(); 
		data.idReciboIndenizacao = abaRim.getFormProperty('idReciboIndenizacao');
		data.tpIndenizacao = abaRim.getFormProperty('tpIndenizacao');
		data.idMoeda = abaRim.getFormProperty('moeda.idMoeda');		
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.findSomatorioParcialIndenizacao", 'calculaIndenizacaoParcial', data);
    	xmit({serviceDataObjects:[sdo]});		
	}
	
	function calculaIndenizacaoParcial_cb(data, error) {
	
		if (error!=undefined) {
			alert(error);
			return;
		}
		
		alert(data.msg+": "+data.sgSimbolo+" "+ setFormat(document.getElementById('vlIndenizacao'), data.vlParcialIndenizado) );
	}
	
	function pageLoad_cb() {
		onPageLoad_cb();
	}
	
	function setMoedaPadrao() {
		setElementValue('moeda.idMoeda', abaRim.getFormProperty("idMoedaHidden"));		
	}		
	
	// onDataLoadCallback
	function dataLoad_cb(data, error) {
		if (error != undefined) {
			return false;
		}
		resetDoctoRelateds();
		populaAllLists(data);
		// popula o form e a listbox destino
		onDataLoad_cb(data, error);

		setElementValue("doctoServico.idDoctoServico", getNestedBeanPropertyValue(data,"doctoServico.idDoctoServico"));
		var nrDoctoServico = getFormattedValue("integer",  getNestedBeanPropertyValue(data, "doctoServico.nrDoctoServico"), "00000000", true);
		setElementValue("doctoServico.nrDoctoServico", nrDoctoServico);
		setDisabled("doctoServico.nrDoctoServico", false);

		if (data.notasFiscaisSource != undefined) {
			notasFiscais_source_cb(data.notasFiscaisSource);
			
		}

		// busca popula a combo de produto, de acordo com o documento de servico
		
		var idProduto = getNestedBeanPropertyValue(data, 'produto.idProduto');
		findComboProduto(idProduto);
		var disableOco = true;
		if(getNestedBeanPropertyValue(data, 'naoConformidade.idNaoConformidade')){
			disableOco = false;
		}else{
			disableOco = true;			
	}
		setDisabled('btnOcorrencias', disableOco);
	}
	
	// botão store
	function onStoreButtonClick() {
		// valida o formulário	
		var tab = getTab(document);
		valid = tab.validate({name:"storeItemButton"});
		if (valid == false) {
			return false;
		}
		// chamada ao método de store na sessao
		var data = new Array();	
		merge(data, buildFormBeanFromForm(document.forms[0]));
		data['tpBeneficiarioIndenizacao'] = abaRim.getFormProperty('tpBeneficiarioIndenizacao');
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.saveItemDocumentosRNC", "storeItemCallback", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
		// callback do store
	function storeItemCallback_cb(data, errorMsg, errorKey, showErrorAlert) {
		var eventObj = {name:"storeItemButton"};
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
		if (errorMsg==undefined) {
			resetDoctoRelateds();
			resetDocumentoServico();
			setMoedaPadrao();			
		}
		
		if(data) {
			if(data.warnings != undefined){
				for (var i = 0; i < data.warnings.length; i++) {
					alert(data.warnings[i].warning);
				}
			}	
		}
	}
	
	function initWindow(e) {
		setDisabled('btnOcorrencias', true);	
		if (e.name=='newItemButton_click' || e.name=='removeButton_grid') { 
			resetDoctoRelateds();
			resetDocumentoServico();
			setMoedaPadrao();
			setElementValue('doctoServico.tpDocumentoServico', 'CTR');
			setDisabled('calculateButton', false);
		}
	}
	
	function resetDocumentoServico() {
		resetValue('doctoServico.filialByIdFilialOrigem.idFilial');
		resetValue('doctoServico.idDoctoServico');
		setDisabled('doctoServico.filialByIdFilialOrigem.idFilial', false);
		setDisabled('doctoServico.idDoctoServico', true);
	
	}
	
	function onTabShow(fromTab) {
		resetValue(document);
		if (abaRim.getFormProperty("clienteBeneficiario.idCliente")=='' || abaRim.getFormProperty("tpBeneficiarioIndenizacao")=='') {
			alert(i18NLabel.getLabel("LMS-21017"));
			tabGroup.selectTab(fromTab.properties.id, "tab_click");			
		}
		setElementValue('doctoServico.tpDocumentoServico', 'CTR');
		//document.getElementById("doctoServico.tpDocumentoServico").onchange();
		setElementValue('idCliente', abaRim.getFormProperty("clienteBeneficiario.idCliente"));		
	}
	
	// limpa os selects
	function resetDoctoRelateds() {
		// limpa paired listbox de notas
		notasFiscaisListboxDef.clearSourceOptions();
		resetValue('ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade');
		//resetValue('notasFiscais');
		// limpa list de motivos
		//resetValue('motivosOcorrencia');
		// limpa combo de produtos
		produto_idProduto_cb(new Array());	
		// limpa o rnc
		resetValue('naoConformidade.filial.sgFilial');
		resetValue('naoConformidade.idNaoConformidade');
		resetValue('naoConformidade.nrRnc');						
		resetValue('qtVolumesIndenizados');
	}

	function findQtde() {
		var data = new Array(); 
		data.idOcorrenciaNaoConformidade = getElementValue('ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade');
		data.idNaoConformidade = getElementValue('naoConformidade.idNaoConformidade');
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.findQtde", "findQtde", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	function findQtde_cb(data, error) {
		if (error==undefined) {
			
			setElementValue('qtVolumesIndenizados', data.qtVolumesIndenizados);
			
		} else {
			alert(error);			
			return true;
		}
	}
	// busca os dados da combo de produto	
	function findComboProduto(idProduto) {

		var data = new Array(); 
		data.idDoctoServico = getElementValue('doctoServico.idDoctoServico');
		data.idProduto = idProduto;
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.findComboProdutoByIdDoctoServico", "findComboProduto", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	// callback que popula os selects
	function findComboProduto_cb(data, error) {
		if (error==undefined) {

			// carrega a combo de produto
			produto_idProduto_cb(data.produtos);

			// se nao há produtos para o documento, então produto nao é obrigatório
			if (data.produtos.length == 0) {
				document.getElementById('produto.idProduto').required = 'false';
			} else {
				document.getElementById('produto.idProduto').required = 'true';
			}

			// se existe idProduto para detalhar, então já seleciona o produto
			if (data.idProduto!='')
				setElementValue('produto.idProduto', data.idProduto);

		} else {
			alert(error);			
			return true;
		}
	}

	/****************************************************************************
 	 * Funções específicas da tag de DocumentoServico
	 ****************************************************************************/
	function enableNaoConformidadeDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}

	function onDoctoServicoAfterPopupSetValue(data) {
		var criteria = new Array();
	    setNestedBeanPropertyValue(criteria, "idDoctoServico", getNestedBeanPropertyValue(data, 'idDoctoServico'));
		setNestedBeanPropertyValue(criteria, "tpDocumentoServico", getElementValue('doctoServico.tpDocumentoServico')); 	    
		setNestedBeanPropertyValue(criteria, "filialByIdFilialOrigem.idFilial", getElementValue('doctoServico.filialByIdFilialOrigem.idFilial')); 	    
	    document.getElementById("doctoServico.nrDoctoServico").previousValue = undefined;
	    lookupChange({e:document.getElementById("doctoServico.idDoctoServico"), forceChange:true});
	    return false;
	}
	
	function onDoctoServicoDataCallback_cb(data, error) {
			resetDoctoRelateds();
			
			var result = doctoServico_nrDoctoServico_exactMatch_cb(data);
		   	if (result == true) {
		   	
				var idDoctoServico = getNestedBeanPropertyValue(data,":0.doctoServico.idDoctoServico");
				var nrDoctoServico = getNestedBeanPropertyValue(data,":0.doctoServico.nrDoctoServico");
				var idFilial = getNestedBeanPropertyValue(data,":0.doctoServico.filialByIdFilialOrigem.idFilial");
				var sgFilial = getNestedBeanPropertyValue(data,":0.doctoServico.filialByIdFilialOrigem.sgFilial");
				var qtVolumes = getNestedBeanPropertyValue(data,":0.qtVolumesIndenizados"); 

				setElementValue('qtVolumesIndenizados', qtVolumes);
				setElementValue('doctoServico.idDoctoServico', idDoctoServico);
				setElementValue('doctoServico.nrDoctoServico', nrDoctoServico);
				setElementValue('doctoServico.filialByIdFilialOrigem.idFilial', idFilial);
				setElementValue('doctoServico.filialByIdFilialOrigem.sgFilial', sgFilial);
				
				format(document.getElementById('doctoServico.nrDoctoServico'));		   	
			
				findComboProduto();	
				// relateds de docto servico
				var item = data[0];
				doctoServicoRelateds_cb(item);
				setDisabled('doctoServico.nrDoctoServico', false);
			} else {
			    if (document.getElementById('doctoServico.nrDoctoServico').disabled==false) {
				    setFocus("doctoServico.nrDoctoServico");
				} else {
					resetValue('doctoServico.idDoctoServico');
				    setFocus("doctoServico.filialByIdFilialOrigem.sgFilial");		
				}
			}
		return result;
	}
	
	
	function populaAllLists(data) {
		// listbox de notas fiscais
		if (data.notasFiscaisSource != undefined) {
			notasFiscais_source_cb(data.notasFiscaisSource);
		}
		//Popula a listbox da direita
		notasFiscaisListboxDef.renderOptions(data);
		
		if(data.rotas != undefined){
			//load source for rotas
			rotasId_cb(data.rotas);
			//renderOptions('rotas', data.rotas, 'idFilialOrigemDestino', 'labelFilialOrigemDestino', undefined);
		}
		
		if(data.motivosOcorrencia != undefined){
			// load source for ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade
			ocorrenciaNaoConformidade_idOcorrenciaNaoConformidade_cb(data.motivosOcorrencia);
			//renderOptions('ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade', data.motivosOcorrencia, 'idOcorrencia', 'dsMotivo', undefined);
		}
	}
	
	function doctoServicoRelateds_cb(data) {
		populaAllLists(data);
		onDataLoad_cb(data, null);
		// listbox de ocorrencias
		
		var sgFilialNaoConformidade = getNestedBeanPropertyValue(data, 'naoConformidade.filial.sgFilial');
		var idNaoConformidade = getNestedBeanPropertyValue(data, 'naoConformidade.idNaoConformidade');
		var nrRnc = getNestedBeanPropertyValue(data, 'naoConformidade.nrRnc');
					
		// seta a o valor da nao conformidade		
		if (sgFilialNaoConformidade!=undefined)
			setElementValue('naoConformidade.filial.sgFilial', sgFilialNaoConformidade);
		if (idNaoConformidade!=undefined)					
			setElementValue('naoConformidade.idNaoConformidade', idNaoConformidade);
		if (nrRnc!=undefined)			
			setElementValue('naoConformidade.nrRnc', getFormattedValue("integer",  nrRnc, "00000000", true));
	}
	
	
	function doctoServicoNrDoctoServico_OnChange() {
		if ( getElementValue('doctoServico.nrDoctoServico') == '' ) {
			resetValue("doctoServico.idDoctoServico");
		}
		return doctoServico_nrDoctoServicoOnChangeHandler();
	}

</script>