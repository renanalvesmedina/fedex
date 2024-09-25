<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/manterReciboIndenizacao" height="196" service="lms.indenizacoes.manterReciboIndenizacaoAction.findItemById" idProperty="idDoctoServicoIndenizacao" onDataLoadCallBack="dataLoad">

	    <adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="true">
		    <adsm:masterLinkItem property="nrReciboComposto"   label="numeroRIM" itemWidth="50" />
			<adsm:hidden property="tpBeneficiarioIndenizacao"/>
			<adsm:hidden property="idCliente"/>
	    </adsm:masterLink>
		<adsm:hidden property="tpIndenizacaoValue"/>
		<adsm:hidden property="doctoServico.idDoctoServico" serializable="true" />
		<adsm:textbox property="doctoServico.tpDocumentoServico" 
					   size="5"
					   label="documentoServico" 
					   dataType="text"
					   labelWidth="20%" 
					   width="30%" 
					   serializable="false" 
					   disabled="true">

			<adsm:textbox dataType="text"
						 property="doctoServico.filialByIdFilialOrigem.sgFilial"
						 size="3"disabled="true" serializable="false"/>

			<adsm:textbox dataType="integer"
						 mask="00000000"
						 property="doctoServico.nrDoctoServico" 
						 picker="false"	size="10" serializable="false" disabled="true" required="true"/>
		</adsm:textbox>
		
		
		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="20%" width="30%" disabled="true" />
		<adsm:hidden  property="idProcessoSinistro"/>
		
		<adsm:combobox label="tipoProduto" 
						property="produto.idProduto" 
						optionProperty="idProduto"
						optionLabelProperty="dsProduto"
						required="false" autoLoad="false"
						labelWidth="20%" width="30%">
		</adsm:combobox>

		<adsm:complement labelWidth="20%" width="30%" separator="branco" label="rnc">
			<adsm:textbox property="naoConformidade.filial.sgFilial" dataType="text" disabled="true" size="3" />
			<adsm:textbox property="naoConformidade.nrRnc" dataType="text" disabled="true" size="8"/>
		</adsm:complement>		
		<adsm:hidden  property="naoConformidade.idNaoConformidade"/>

		<adsm:complement label="valorIndenizacao" width="30%" labelWidth="20%" separator="branco" required="true">
			<adsm:combobox property="moeda.idMoeda" 
						   boxWidth="85"
						   service="lms.indenizacoes.manterReciboIndenizacaoAction.findComboMoeda" 
						   optionProperty="idMoeda" 
						   optionLabelProperty="siglaSimbolo" 
						   onlyActiveValues="true"  disabled="true" defaultValue="1">
			</adsm:combobox>
			<adsm:textbox property="vlIndenizacao" dataType="currency" disabled="false"/>
		</adsm:complement>		
		
		<adsm:textbox dataType="integer" label="quantidadeVolumes" property="qtVolumesIndenizados" size="6" maxLength="6" labelWidth="20%" width="30%" disabled="true"/>		

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

	<!-- LMS-666 REQ006 Criar combos ocorrencia e rotas de sinistro -->
		<adsm:combobox label="ocorrencias" 
						property="ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade"
						optionProperty="idOcorrencia" 						
						optionLabelProperty="dsMotivo" 
						service="" autoLoad="false"
						required="false" 
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
			<adsm:button caption="salvarDocumento" buttonType="storeButton" onclick="onStoreButtonClick();"/>			
			<adsm:button id="btnOcorrencias" disabled="true" caption="rnc" onclick="openRNC();"/>
			<adsm:button id="btnProcessoSinistro" disabled="true" caption="processoSinistro" onclick="openProcessoSinistro();"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid  
		property="doctoServicoIndenizacao" 
		idProperty="idDoctoServicoIndenizacao" 
		service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedDocumentos"
		rowCountService="lms.indenizacoes.manterReciboIndenizacaoAction.getRowCountDocumentos"
		selectionMode="none" gridHeight="91" 
		detailFrameName="documentos"		
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

		<adsm:gridColumn property="sgSimboloTotalDocto"       title="valorTotalMercadorias" width="50" align="left"                      />
		<adsm:gridColumn property="vlTotalDoctoServico"       title=""                      width="120" align="right" dataType="currency"/>		

		<adsm:gridColumn property="sgSimboloVlIndenizado"     title="valorIndenizado" width="50"  align="left"                     />
		<adsm:gridColumn property="vlIndenizado"              title=""                width="120" align="right" dataType="currency"/>

		<%-- campos novos --%>
		<adsm:gridColumn property="qtVolumes"                 title="quantidadeVolumes" width="90" align="right" dataType="integer"/>
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="sgFilialControleCarga" title="controleCarga" width="50" />	
			<adsm:gridColumn property="nrControleCarga"       title=""  width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<%-- campos novos --%>

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
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	document.getElementById('idCliente').masterLink = 'true';
	document.getElementById('nrProcessoSinistro').masterLink = 'true';
	document.getElementById('tpIndenizacaoValue').masterLink = 'true';
	document.getElementById('tpBeneficiarioIndenizacao').masterLink = 'true';

	document.getElementById('doctoServico.tpDocumentoServico').required = 'true';

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");
	
	function pageLoad_cb() {
		onPageLoad_cb();
	}
	
	
	function initWindow(e) {
		setDisabled('btnOcorrencias', true);
		setDisabled('btnProcessoSinistro', true);
		if (e.name=='cleanButton_click' || e.name=='removeButton_grid') { 
			resetSelects();
			getMoedaSessao();
		}
		habilitarCampoProcessoSinistro();
	}

	function resetSelects() {
		notasFiscaisListboxDef.clearSourceOptions();
		produto_idProduto_cb(new Array());	
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
		data.myScreen = 'documentos';
		var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.saveItem", "storeItemCallback", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	// callback do store
	function storeItemCallback_cb(data, errorMsg, errorKey, showErrorAlert) {
		var eventObj = {name:"storeItemButton"};
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
		if (errorMsg==undefined) {
			resetValue(document);
			resetSelects();
			getMoedaSessao();
		}
	}
	
	// onDataLoadCallback
	function dataLoad_cb(data, error) {
		resetSelects();
		if (data.notasFiscaisSource != undefined) {
			notasFiscais_source_cb(data.notasFiscaisSource);
		}
		//LMS-666
		
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
		// popula o form e a listbox destino
		onDataLoad_cb(data, error);
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
		
		habilitarCampoProcessoSinistro();
		
	}
	
	function habilitarCampoProcessoSinistro() {
		var tabCad = getTabGroup(this.document).getTab("cad");
		var idProcessoSinistro = tabCad.getElementById('idProcessoSinistro').value;
		setElementValue('idProcessoSinistro', idProcessoSinistro);
		var disableProcessoSinistro = true;
		if(document.getElementById('tpIndenizacaoValue').value == 'PS' && idProcessoSinistro != null && idProcessoSinistro != "") {
			disableProcessoSinistro = false;
		}
		setDisabled('btnProcessoSinistro', disableProcessoSinistro);
	}
	
	function findQtde() {
		var data = new Array(); 
		data.idOcorrenciaNaoConformidade = getElementValue('ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade');
		data.idNaoConformidade = getElementValue('naoConformidade.idNaoConformidade');
		var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.findQtde", "findQtde", data);
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
		var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.findComboProdutoByIdDoctoServico", "findComboProduto", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	// callback que popula a combo
	function findComboProduto_cb(data, error) {
		if (error==undefined) {
		
			produto_idProduto_cb(data.produtos);
			
			// comboboxLoadOptions({e:document.getElementById("produto.idProduto"), data:data.produtos});
			
			
			if (data.produtos.length == 0) {
				document.getElementById('produto.idProduto').required = 'false';
			} else {
				document.getElementById('produto.idProduto').required = 'true';
			}
			
			// se existe idProduto para detalhar, então já seleciona o produto
			if (data.idProduto!='' && data.produtos.length > 0)
				setElementValue('produto.idProduto', data.idProduto);
			else {
				resetValue('produto.idProduto');
			}
		}
	}

	function setMasterLinkProperties() {
		document.getElementById('tpBeneficiarioIndenizacao').value = abaDetalhamento.getFormProperty("tpBeneficiarioIndenizacao");
		document.getElementById('idCliente').value = abaDetalhamento.getFormProperty("clienteBeneficiario.idCliente");
		document.getElementById('nrProcessoSinistro').value = abaDetalhamento.getFormProperty("nrProcessoSinistro");
		document.getElementById('tpIndenizacaoValue').value = abaDetalhamento.getFormProperty("tpIndenizacaoValue");
	}

	function onTabShow(fromTab) {
		// resetValue(document);
		setMasterLinkProperties();		
		getMoedaSessao();		
	}
	
	function getMoedaSessao() {
		setElementValue('moeda.idMoeda', abaDetalhamento.getFormProperty("idMoedaHidden"));		
	}
	
	function openRNC() {
		var param = getParametrosRnc();
		param += '&isReciboIndenizacao=true';
		showModalDialog('rnc/manterOcorrenciasNaoConformidade.do?cmd=main' + param, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
	}
	
	function openProcessoSinistro() {
		var param = "&nrProcessoSinistro=" + encodeString(document.getElementById("nrProcessoSinistro").value);
		param += '&isReciboIndenizacao=true';
		showModalDialog('seguros/manterProcessosSinistro.do?cmd=main' + param, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');
	}
	
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}
	
	function getParametrosRnc() {
		var qs = "";
		if (document.getElementById("naoConformidade.filial.sgFilial").type == 'checkbox') {
			qs += "&naoConformidade.filial.sgFilial=" + document.getElementById("naoConformidade.filial.sgFilial").checked;
		} else {
			qs += "&naoConformidade.filial.sgFilial=" + encodeString(document.getElementById("naoConformidade.filial.sgFilial").value);
		}
		if (document.getElementById("naoConformidade.nrRnc").type == 'checkbox') {
			qs += "&naoConformidade.nrNaoConformidade=" + document.getElementById("naoConformidade.nrRnc").checked;
		} else {
			qs += "&naoConformidade.nrNaoConformidade=" + encodeString(document.getElementById("naoConformidade.nrRnc").value);
		}
		if (document.getElementById("naoConformidade.idNaoConformidade").type == 'checkbox') {
			qs += "&naoConformidade.idNaoConformidade=" + document.getElementById("naoConformidade.idNaoConformidade").checked;
		} else {
			qs += "&naoConformidade.idNaoConformidade=" + encodeString(document.getElementById("naoConformidade.idNaoConformidade").value);
		}
		if (document.getElementById("doctoServico.idDoctoServico").type == 'checkbox') {
			qs += "&naoConformidade.doctoServico.idDoctoServico=" + document.getElementById("doctoServico.idDoctoServico").checked;
		} else {
			qs += "&naoConformidade.doctoServico.idDoctoServico=" + encodeString(document.getElementById("doctoServico.idDoctoServico").value);
		}
		if (document.getElementById("doctoServico.tpDocumentoServico").type == 'checkbox') {
			qs += "&naoConformidade.doctoServico.tpDocumentoServico.description=" + document.getElementById("doctoServico.tpDocumentoServico").checked;
		} else {
			qs += "&naoConformidade.doctoServico.tpDocumentoServico.description=" + encodeString(document.getElementById("doctoServico.tpDocumentoServico").value);
		}
		if (document.getElementById("doctoServico.filialByIdFilialOrigem.sgFilial").type == 'checkbox') {
			qs += "&naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial=" + document.getElementById("doctoServico.filialByIdFilialOrigem.sgFilial").checked;
		} else {
			qs += "&naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial=" + encodeString(document.getElementById("doctoServico.filialByIdFilialOrigem.sgFilial").value);
		}
		if (document.getElementById("doctoServico.nrDoctoServico").type == 'checkbox') {
			qs += "&naoConformidade.doctoServico.nrDoctoServico=" + document.getElementById("doctoServico.nrDoctoServico").checked;
		} else {
			qs += "&naoConformidade.doctoServico.nrDoctoServico=" + encodeString(document.getElementById("doctoServico.nrDoctoServico").value);
		}
		if (document.getElementById("moeda.idMoeda").type == 'checkbox') {
			qs += "&naoConformidade.doctoServico.moeda.idMoeda=" + document.getElementById("moeda.idMoeda").checked;
		} else {
			qs += "&naoConformidade.doctoServico.moeda.idMoeda=" + encodeString(document.getElementById("moeda.idMoeda").value);
		}
		if (document.getElementById("idCliente").type == 'checkbox') {
			qs += "&naoConformidade.clienteByIdClienteRemetente.idCliente=" + document.getElementById("idCliente").checked;
		} else {
			qs += "&naoConformidade.clienteByIdClienteRemetente.idCliente=" + encodeString(document.getElementById("idCliente").value);
		}
		return qs;
	}

</script>
