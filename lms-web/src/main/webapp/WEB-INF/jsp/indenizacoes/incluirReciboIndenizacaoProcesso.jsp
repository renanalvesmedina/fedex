<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.indenizacoes.incluirReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:i18nLabels>
    	<adsm:include key="LMS-21017"/>
    </adsm:i18nLabels>
	<adsm:form action="/indenizacoes/incluirReciboIndenizacao" height="190" service="lms.indenizacoes.incluirReciboIndenizacaoAction.findDocumentoProcessoByKey" idProperty="idDoctoServicoIndenizacao" onDataLoadCallBack="dataLoad">

	    <adsm:masterLink idProperty="idReciboIndenizacao" >
		    <adsm:masterLinkItem property="reciboIndenizacao"                 label="RIM" itemWidth="50" />
		    <adsm:masterLinkItem property="processoSinistro.nrProcessoSinistro" label="numeroProcesso" itemWidth="50" />
		    <adsm:hidden property="processoSinistro.idProcessoSinistro"/>
			<adsm:hidden property="tpBeneficiarioIndenizacao"/>
			<adsm:hidden property="idCliente"/>
	    </adsm:masterLink>

		<adsm:hidden property="doctoServico.idDoctoServico" serializable="true" />
		
		<adsm:textbox property="doctoServico.tpDocumentoServico" 
					   size="5"
					   label="documentoServico" 
					   dataType="text"
					   labelWidth="20%" 
					   width="80%" 
					   required="true"
					   serializable="false" 
					   disabled="true">

			<adsm:textbox dataType="text"
						 property="doctoServico.filialByIdFilialOrigem.sgFilial"
						 size="3"disabled="true" serializable="false"/>

			<adsm:textbox dataType="integer"
						 mask="00000000"
						 property="doctoServico.nrDoctoServico" 
						 picker="false"	size="10" serializable="false" disabled="true" />
		</adsm:textbox>
		
				
		<adsm:combobox label="tipoProduto" 
						property="produto.idProduto" 
						optionProperty="idProduto"
						optionLabelProperty="dsProduto"
						required="false" autoLoad="false"
						labelWidth="20%" width="30%">
		</adsm:combobox>

		<adsm:hidden property="tpPrejuizo.value" serializable="false"/>		
		<adsm:textbox property="tpPrejuizo.description"
						dataType="text"
						label="tipoPrejuizo" 
						disabled="true" 
						labelWidth="20%" 
						width="30%"
		/>

		<adsm:complement label="valorIndenizacao" width="80%" labelWidth="20%" separator="branco" required="true">
			<adsm:combobox property="moeda.idMoeda" 
						   boxWidth="85"
						   service="lms.indenizacoes.incluirReciboIndenizacaoAction.findComboMoeda" 
						   optionProperty="idMoeda" 
						   optionLabelProperty="siglaSimbolo" 
						   onlyActiveValues="true" disabled="true" defaultValue="1">
			</adsm:combobox>
			<adsm:textbox property="vlIndenizacao" dataType="currency" disabled="false"/>
		</adsm:complement>		
		
		<adsm:textbox dataType="integer" label="quantidadeVolumes" property="qtVolumesIndenizados" size="6" maxLength="6" labelWidth="20%" width="80%"/>

		<adsm:pairedListbox label="notasFiscais" 
							property="notasFiscais" 
							optionProperty="idNotaFiscalConhecimento" 
							optionLabelProperty="nrNotaFiscal" 
							service="" 
							size="6" 
							boxWidth="91" 
							required="false" 
							width="80%" 
							labelWidth="20%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="adicionarDocumentoProcesso" onclick="onAdicionarDocumentosClick();" disabled="false" buttonType="storeButton"/>
			<adsm:button caption="salvarDocumentoProcesso" buttonType="storeButton" onclick="onStoreButtonClick();"/>			
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid  
		property="doctoServicoIndenizacao" 
		idProperty="idDoctoServicoIndenizacao" 
		service="lms.indenizacoes.incluirReciboIndenizacaoAction.findPaginatedDocumentosProcesso"
		rowCountService="lms.indenizacoes.incluirReciboIndenizacaoAction.getRowCountDocumentosProcesso"
		selectionMode="check" gridHeight="99" 
		detailFrameName="processo"		
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

		<adsm:gridColumn property="dsProduto"                 title="tipoProduto" width="150"/>
		<adsm:gridColumnGroup separatorType="RNC">
			<adsm:gridColumn property="sgFilialNaoConformidade" dataType="text" title="RNC" width="40" />
			<adsm:gridColumn property="nrNaoConformidade"       dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:buttonBar>
			<adsm:button id="calculateButton" caption="calcularIndenizacaoParcial" buttonType="resetButton" onclick="calculaIndenizacaoParcial();" disabled="true"/>		
			<adsm:removeButton caption="excluirDocumentoProcesso" service="lms.indenizacoes.incluirReciboIndenizacaoAction.removeDocumentosByIds"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>

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

	function setMoedaSessao() {
		setElementValue('moeda.idMoeda', abaRim.getFormProperty("idMoedaHidden"));		
	}	
	
	function pageLoad_cb() {
		onPageLoad_cb();		
	}
	
	function initWindow(e) {		
		if (e.name=='cleanButton_click' || e.name=='removeButton_grid') { 
			resetSelects();
			setMoedaSessao();
			setDisabled('calculateButton', false);
		}
	}

	function resetSelects() {
		notasFiscaisListboxDef.clearSourceOptions();
		produto_idProduto_cb(new Array());	
	}

	// botão store
	function onStoreButtonClick() {
		// valida o formulário	
		var tab = getTab(document);
		valid = tab.validate({name:"storeButton_click"});
		if (valid == false) {
			return false;
		}
		// chamada ao método de store na sessao
		var data = new Array();	
		merge(data, buildFormBeanFromForm(document.forms[0]));
		data.myScreen = 'documentos';
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.saveItemDocumentosProcesso", "storeItemCallback", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	// callback do store
	function storeItemCallback_cb(data, errorMsg, errorKey, showErrorAlert) {
		var eventObj = {name:"storeItemButton"};
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
		resetSelects();
		setMoedaSessao();
	}
	
	// onDataLoadCallback
	function dataLoad_cb(data, error) {
		resetSelects();
		if (data.notasFiscaisSource != undefined) {
			notasFiscais_source_cb(data.notasFiscaisSource);
		}
		// popula o form e a listbox destino
		onDataLoad_cb(data, error);
		// busca popula a combo de produto, de acordo com o documento de servico
		var idProduto = getNestedBeanPropertyValue(data, 'produto.idProduto');
		findComboProduto(idProduto);
	}
		
	// busca os dados da combo de produto	
	function findComboProduto(idProduto) {
		var data = new Array(); 
		data.idDoctoServico = getElementValue('doctoServico.idDoctoServico');
		data.idProduto = idProduto;
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.findComboProdutoByIdDoctoServico", "findComboProduto", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	// callback que popula a combo
	function findComboProduto_cb(data, error) {
		if (error==undefined) {
			produto_idProduto_cb(data.produtos);
			
			if (data.produtos.length == 0) {
				document.getElementById('produto.idProduto').required = 'false';
			} else {
				document.getElementById('produto.idProduto').required = 'true';
			}
			
			// se existe idProduto para detalhar, então já seleciona o produto
			if (data.idProduto!='')
				setElementValue('produto.idProduto', data.idProduto);
		}
	}

	function setMasterLinkProperties() {
		document.getElementById('processoSinistro.idProcessoSinistro').value = abaRim.getFormProperty("processoSinistro.idProcessoSinistro");
		document.getElementById('processoSinistro.idProcessoSinistro').masterLink = 'true';
		document.getElementById('tpBeneficiarioIndenizacao').value = abaRim.getFormProperty("tpBeneficiarioIndenizacao");
		document.getElementById('tpBeneficiarioIndenizacao').masterLink = 'true';
		document.getElementById('idCliente').value = abaRim.getFormProperty("clienteBeneficiario.idCliente");
		document.getElementById('idCliente').masterLink = 'true';
	}

	function onTabShow(fromTab) {
		resetValue(document);
		if (abaRim.getFormProperty("clienteBeneficiario.idCliente")=='' || abaRim.getFormProperty("tpBeneficiarioIndenizacao")=='') {
			alert(i18NLabel.getLabel("LMS-21017"));
			tabGroup.selectTab(fromTab.properties.id, "tab_click");
		}
		setMoedaSessao();
		setMasterLinkProperties();		
	}
	
	function onAdicionarDocumentosClick() {
		showModalDialog('/indenizacoes/incluirReciboIndenizacaoSelecaoDocumentosServico.do?cmd=selecao',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:750px;dialogHeight:480px;');
	}

</script>
