<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">

	<adsm:form action="/indenizacoes/manterReciboIndenizacao" idProperty="idFilialDebitada" service="lms.indenizacoes.manterReciboIndenizacaoAction.findByIdFilialDebitada">

		<adsm:hidden property="reciboIndenizacao.idReciboIndenizacao"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		
		<adsm:complement label="numeroRIM" labelWidth="25%" width="75%" separator="branco">
		<adsm:textbox property="reciboIndenizacao.filial.sgFilial" dataType="text"        disabled="true" size="3"/>
		<adsm:textbox property="reciboIndenizacao.nrReciboIndenizacao" dataType="integer" disabled="true" size="8" mask="00000000"/>
		</adsm:complement>

		<adsm:lookup label="filial" labelWidth="25%" width="75%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             required="true"
		             exactMatch="true" disabled="false"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50"
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:textbox label="percentualDebitado" property="pcDebitado" dataType="integer" maxValue="100" required="true" size="5" width="75%" labelWidth="25%"/>
		<adsm:lookup label="filialReembolsada" labelWidth="25%" width="75%"
		             property="filialReembolsada"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true" disabled="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filialReembolsada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filialReembolsada.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50"
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:complement label="valorReembolso" width="75%" labelWidth="25%" separator="branco" >
			<adsm:combobox property="moeda.idMoeda" 
						   boxWidth="85"
						   disabled="true"
						   service="lms.indenizacoes.manterReciboIndenizacaoAction.findComboMoeda" 
						   optionProperty="idMoeda" 
						   optionLabelProperty="siglaSimbolo" 
						   onlyActiveValues="true">
			</adsm:combobox>
			<adsm:textbox property="vlReembolso" dataType="currency" disabled="true" />
		</adsm:complement>		
		<adsm:textbox label="dataDadosReembolso" property="dtDadoReembolso" dataType="JTDate" disabled="true" width="75%" labelWidth="25%"/>
		<adsm:textbox label="dataReembolso" property="dtReembolso" dataType="JTDate" disabled="true" width="75%" labelWidth="25%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="storeButtonDebitada" service="lms.indenizacoes.manterReciboIndenizacaoAction.storeFilialDebitada" callbackProperty="storeCallback" caption="salvarFilial"/>
			<adsm:newButton id="newButton" caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="filialDebitada" 
				idProperty="idFilialDebitada"  
				gridHeight="170"
				unique="true" 
				showPagging="false"
				scrollBars="vertical" 
				service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedFilialDebitada"		
				rowCountService=""
				detailFrameName="debitadas"
				onDataLoadCallBack="onDataLoadLocal"
				>
		
		<adsm:gridColumn property="sgFilial"             title="filial"             width="290" />
		<adsm:gridColumn property="pcDebitado"           title="percentualDebitado" width="220" dataType="integer" />
		<adsm:gridColumn property="sgSimboloVlReembolso" title="valorReembolso"     width="70" align="left" />
		<adsm:gridColumn property="vlReembolso"          title=""                   width="140" align="right" dataType="currency" />
		
		<adsm:buttonBar>
			<adsm:button id="removeButton" caption="excluirFilial" buttonType="removeButton" onclick="onRemoveButtonClick();"/>			
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");

	document.getElementById('reciboIndenizacao.idReciboIndenizacao').masterLink='true';
	document.getElementById('reciboIndenizacao.nrReciboIndenizacao').masterLink='true';
	document.getElementById('reciboIndenizacao.filial.sgFilial').masterLink='true';	

	function pageLoad_cb() {
		onPageLoad_cb();
	}

	// tabShow
	function onTabShow() {	
		//resetValue(document);	
		setMasterLinkProperties();
		disableMainFields();
		setMoedaSessao();
		executeSearch();
	}
	
	function onRemoveButtonClick() {
		filialDebitadaGridDef.removeByIds('lms.indenizacoes.manterReciboIndenizacaoAction.removeFiliaisDebitadasByIds', 'remove');
		limpaCampos();
	}	
	
	function remove_cb(data, error) {
		filialDebitadaGridDef.removeByIds_cb(data, error);
		onTabShow();
	}

	function storeCallback_cb(data, error) {
		store_cb(data, error);
		onTabShow();
	}
	
	
	// seta os valores masterLink
	function setMasterLinkProperties() {
		setElementValue('reciboIndenizacao.idReciboIndenizacao', abaDetalhamento.getFormProperty("idReciboIndenizacao"));
		setElementValue('reciboIndenizacao.filial.sgFilial',     abaDetalhamento.getFormProperty("sgFilialRecibo"));
		setElementValue('reciboIndenizacao.nrReciboIndenizacao', abaDetalhamento.getFormProperty("nrReciboIndenizacao"));
	}
	
	// habilita/desabilita os principais campos
	function disableMainFields() {

		var disable = true;
		if (abaDetalhamento.getFormProperty("tpFormaPagamento")=='PU' && abaDetalhamento.getFormProperty("tpStatusIndenizacaoValue")=='P' ) {
			disable = false;
		}
		setDisabled('moeda.idMoeda', disable);
		setDisabled('vlReembolso', disable);			
		setDisabled('dtReembolso', disable);
		setDisabled('dtDadoReembolso', disable);		
		setDisabled('filialReembolsada.idFilial', disable);				
	}
	
	// executa a pesquisa da grid
	function executeSearch() {
		var data = new Array();
		data.idReciboIndenizacao = getElementValue('reciboIndenizacao.idReciboIndenizacao');
		filialDebitadaGridDef.executeSearch(data);	
	}
	
	function onDataLoadLocal_cb() {
		if (getIdProcessoWorkflow()) {
			var chk = document.getElementsByTagName("input");
	        for (var i = 0; i < chk.length; i++) {
	            if(chk[i].type == "checkbox") {
		        	chk[i].disabled = true;
	            }
	        }
	        desabilitarBotoes();
	        filialDebitadaGridDef.onRowClickFunction = "disableGridClick";
		}
	}
	/**
	 * Desabilita o click na grid
	 */
	function disableGridClick(){
		return false;
	}
	
	function desabilitarBotoes() {
		if (getIdProcessoWorkflow()) {
			setDisabled('storeButtonDebitada',true);
			setDisabled('removeButton',true);
		}
	}

	function initWindow(e) {
		desabilitarBotoes();
		if (e.name == 'newButton_click') {
			setMoedaSessao();
		}
		else
		if (e.name == 'tab_click') {
			limpaCampos();
		}
	}

	function setMoedaSessao() {
		setElementValue('moeda.idMoeda', abaDetalhamento.getFormProperty("idMoedaHidden"));		
	}
	
	function limpaCampos() {
		var tab = getTab(this.document);
		tab.setChanged(false);
		tab.itemTabChanged = false;
		newButtonScript(this.document, true, {name:'newButton_click'});
	}
	
	// obtem o id processo, caso seja uma visualização de ação do workflow
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}

</script>