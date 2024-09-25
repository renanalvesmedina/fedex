<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>
	/* Remove as criteriaProperty da lookup. */
	function myOnPageLoad(){
		onPageLoad();
		
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findInitialValueItem", "initPage", null)); 
		xmit(false);					
	}	

	function findVlrMinimoDoctoDesc(){
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterDescontosAction.findValorMinimoDocumentoDesconto", "findVlrMinimoDoctoDesc", new Array())); 
		xmit(false);					
	}
	
	function findVlrMinimoDoctoDesc_cb(data,erro){
		if(erro != undefined){
			alert(erro);
		}
		setElementValue('vlrMinimoDoctDesc', data._value);
	}		
</script>
<adsm:window onPageLoad="myOnPageLoad">
	<adsm:form action="/contasReceber/manterFaturas"
		service="lms.contasreceber.manterFaturasAction.findByIdItemFatura"
		idProperty="idItemFatura" onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="tpOrigem" serializable="false"/>

		<adsm:hidden property="idCliente" />
		<adsm:hidden property="nrCliente" />
		<adsm:hidden property="nmCliente" />
		<adsm:hidden property="idServicoTemp" />		
		<adsm:hidden property="tpSituacaoDevedorDocServFatValido" serializable="true" value="0"/>
		<adsm:hidden property="tpSituacaoDesconto" value="A" serializable="false"/>
		<adsm:hidden property="idMotivoDesconto" serializable="false"/>
		<adsm:hidden property="vlrMinimoDoctDesc" serializable="false"/>
		

		<adsm:masterLink showSaveAll="true" idProperty="idFatura">
			<adsm:masterLinkItem property="nrFaturaFormatada" label="fatura"
				itemWidth="45" />
			<adsm:masterLinkItem property="dtVencimento" label="dataVencimento"
				itemWidth="45" />
			<adsm:masterLinkItem property="tpFreteDescription" label="tipoFrete"
				itemWidth="45" />
			<adsm:masterLinkItem property="servico.dsServico" label="servico"
				itemWidth="45" />
		</adsm:masterLink>

		<adsm:combobox
			property="devedorDocServFat.doctoServico.tpDocumentoServico"
			label="documentoServico" width="35%"
			service="lms.contasreceber.manterFaturasAction.findTipoDocumentoServico"
			optionProperty="value" optionLabelProperty="description" defaultValue="CTR"
			onchange="return tpDocumentoServicoOnChange();">

			<adsm:lookup dataType="text"
				property="devedorDocServFat.doctoServico.filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
				service="lms.contasreceber.manterFaturasAction.findLookupFilial"
				disabled="true" action="" size="3" maxLength="3" picker="false"
				exactMatch="true" onchange="return filialOnChange();"
				onDataLoadCallBack="filialOnChange" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
				relatedProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
			<adsm:lookup dataType="integer" property="devedorDocServFat"
				idProperty="idDevedorDocServFat"
				criteriaProperty="doctoServico.nrDoctoServico"
				service="lms.contasreceber.manterFaturasAction.findDevedorServDocFat"
				action="/contasReceber/pesquisarDevedorDocServFatLookUp" cmd="pesq"
				size="10" maxLength="8" serializable="true" disabled="true"
				exactMatch="true"
				popupLabel="pesquisarDocumentoServico"
				onchange="return lookUpDevedorOnChange();"
				onDataLoadCallBack="lookUpDevedor" onPopupSetValue="lookUpDevedor"
				required="true">
				<adsm:propertyMapping
					modelProperty="doctoServico.tpDocumentoServico"
					criteriaProperty="devedorDocServFat.doctoServico.tpDocumentoServico" />
				<adsm:propertyMapping
					modelProperty="doctoServico.filialByIdFilialOrigem.idFilial"
					criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping
					modelProperty="doctoServico.filialByIdFilialOrigem.sgFilial"
					criteriaProperty="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial"
					inlineQuery="true" />
				<adsm:propertyMapping modelProperty="cliente.idCliente"
					criteriaProperty="idCliente" />
				<adsm:propertyMapping modelProperty="cliente.pessoa.nrIdentificacao"
					criteriaProperty="nrCliente" />		
				<adsm:propertyMapping modelProperty="cliente.pessoa.nmPessoa"
					criteriaProperty="nmCliente" />
				<adsm:propertyMapping modelProperty="idMoeda"
					criteriaProperty="devedorDocServFat.doctoServico.moeda.idMoeda" />
				<adsm:propertyMapping modelProperty="idServico"
					criteriaProperty="devedorDocServFat.doctoServico.servico.idServico" />
				<adsm:propertyMapping modelProperty="tpModal"
					criteriaProperty="devedorDocServFat.doctoServico.servico.tpModal" />
				<adsm:propertyMapping modelProperty="tpAbrangencia"
					criteriaProperty="devedorDocServFat.doctoServico.servico.tpAbrangencia" />
				<adsm:propertyMapping modelProperty="tpSituacaoDevedorDocServFatValido" 
					criteriaProperty="tpSituacaoDevedorDocServFatValido"/>						
				<adsm:propertyMapping modelProperty="idDivisaoCliente"
					criteriaProperty="devedorDocServFat.divisaoCliente.idDivisaoCliente" />	
				<adsm:propertyMapping modelProperty="tpFrete"
					criteriaProperty="tpFreteValue" />	

				<adsm:propertyMapping modelProperty="vlTotalDocServico"
					relatedProperty="devedorDocServFat.doctoServico.vlTotalDocServico" />
				<adsm:propertyMapping modelProperty="vlDesconto"
					relatedProperty="devedorDocServFat.desconto.vlDesconto" />
				<adsm:propertyMapping modelProperty="idDesconto"
					relatedProperty="devedorDocServFat.desconto.idDesconto" />
				<adsm:propertyMapping modelProperty="idMotivoDesconto"
					relatedProperty="devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem" />
				<adsm:propertyMapping modelProperty="idDoctoServico"
					relatedProperty="devedorDocServFat.doctoServico.idDoctoServico" />
				<adsm:propertyMapping modelProperty="doctoServico.nrDoctoServico"
					relatedProperty="devedorDocServFat.doctoServico.nrDoctoServicoTmp" />
				<adsm:propertyMapping modelProperty="idMoeda"
					relatedProperty="devedorDocServFat.doctoServico.moeda.idMoeda" />
				<adsm:propertyMapping modelProperty="idServico"
					relatedProperty="idServicoTemp" />
				<adsm:propertyMapping modelProperty="tpModal"
					relatedProperty="devedorDocServFat.doctoServico.servico.tpModal" />
				<adsm:propertyMapping modelProperty="tpAbrangencia"
					relatedProperty="devedorDocServFat.doctoServico.servico.tpAbrangencia" />
			</adsm:lookup>
		</adsm:combobox>

		<adsm:hidden property="devedorDocServFat.doctoServico.idDoctoServico" />
		<adsm:hidden property="devedorDocServFat.doctoServico.moeda.idMoeda" />
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.idServico" />
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.dsServico" />
		<adsm:hidden property="tpFreteValue" /> 
		<adsm:hidden property="tpFreteDescription" />
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.tpModal" />
		<adsm:hidden property="devedorDocServFat.doctoServico.servico.tpAbrangencia" />
		<adsm:hidden property="devedorDocServFat.doctoServico.nrDoctoServicoTmp" />
		<adsm:hidden property="devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:hidden property="devedorDocServFat.desconto.idDesconto" />
		<adsm:hidden property="devedorDocServFat.divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="saldoTotalDocumento" />
		

		<adsm:textbox label="valorDocumento" dataType="currency" size="10" width="10%"
			property="devedorDocServFat.doctoServico.vlTotalDocServico" labelWidth="15%"
			disabled="true" maxLength="18" />
			
		<adsm:textbox label="valorRecebidoParcial" dataType="currency" size="10" labelWidth="15%"
			property="sumValorRecebidoParcial" disabled="true" maxLength="18" width="5%"/>
				
		<adsm:textbox label="valorDesconto" dataType="currency" size="10" onchange="return onChangeValorDesconto();" width="10%"
			property="devedorDocServFat.desconto.vlDesconto" disabled="false" labelWidth="15%" maxLength="18" />
			
		<adsm:textbox label="valorSaldoDevedor" dataType="currency" size="10" labelWidth="15%"
			property="saldoDevedor" disabled="true" maxLength="18" width="10%"/>
			
		<adsm:combobox label="situacaoDesconto"	domain="DM_STATUS_WORKFLOW" boxWidth="150" disabled="true"
			property="devedorDocServFat.desconto.tpSituacaoAprovacao" />
			
			
		<adsm:combobox label="motivoDesconto" width="85%"
			service="lms.contasreceber.manterFaturasAction.findComboMotivoDesconto"
			optionLabelProperty="dsMotivoDesconto"
			optionProperty="idMotivoDesconto" boxWidth="300" disabled="true" autoLoad="true"
			property="devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem" />

		<adsm:textarea columns="80" width="85%" rows="2" maxLength="500"
			disabled="false" label="observacao"
			property="devedorDocServFat.desconto.obDesconto" />

		<adsm:section caption="totaisFatura" />

		<adsm:textbox dataType="integer" label="qtdeTotalDocumentos"
			property="qtdeTotalDocumentos" size="10" labelWidth="20%"
			maxLength="6" disabled="true" width="13%" />
		<adsm:textbox dataType="currency" label="valorTotalDocumentos"
			property="valorTotalDocumentos" size="10" labelWidth="21%"
			maxLength="18" disabled="true" width="12%" />
		<adsm:textbox dataType="currency" label="valorTotalDesconto"
			property="valorTotalDesconto" size="10" labelWidth="21%"
			maxLength="18" width="13%" disabled="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="storeButton"
				caption="salvarDocumentoServico" id="storeButton"
				onclick="myStoreButton();" />
			<adsm:button buttonType="cleanButton" id="cleanButton" caption="limpar"
				onclick="myOnShow();" />
			<adsm:button caption="consultaDocumentoServico" boxWidth="200" id="btnConsultarDoctoServico"
				action="/contasReceber/consultarDadosCobrancaDocumentoServico" 
				cmd="main" disabled="true">
				<adsm:linkProperty
					src="devedorDocServFat.doctoServico.tpDocumentoServico"
					target="doctoServico.tpDocumentoServico" />
				<adsm:linkProperty
					src="devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial"
					target="doctoServico.filialByIdFilialOrigem.sgFilial" />
				<adsm:linkProperty
					src="devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"
					target="doctoServico.filialByIdFilialOrigem.idFilial" />
				<adsm:linkProperty
					src="devedorDocServFat.doctoServico.nrDoctoServico"
					target="doctoServico.nrDoctoServico" />
				<adsm:linkProperty
					src="devedorDocServFat.doctoServico.idDoctoServico"
					target="doctoServico.idDoctoServico" />
				<adsm:linkProperty
					src="devedorDocServFat.doctoServico.idDoctoServico"
					target="doctoServico.idDoctoServicoHidden" />
			</adsm:button>
			
			<adsm:button caption="localizarDocumentoServico" id="selecionarDocumentosServico" onclick="buscarItemFatura();" boxWidth="200" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idItemFatura" property="itemFatura"
		unique="true" autoSearch="false" rows="5" showGotoBox="true" onSelectRow="disabledButtons();" onSelectAll="disabledButtons();"
		onDataLoadCallBack="myItemFatura" showPagging="true"
		detailFrameName="documentoServico"
		service="lms.contasreceber.manterFaturasAction.findPaginatedItemFatura"
		rowCountService="lms.contasreceber.manterFaturasAction.getRowCountItemFatura">

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoServico" isDomain="true"
				property="tpDocumentoServico" width="100" />
			<adsm:gridColumn title="" property="sgFilial" width="150" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="150" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="valorDocumento" property="siglaSimbolo"
			width="40" dataType="text" />
		<adsm:gridColumn title="" dataType="currency"
			property="vlTotalDocServico" width="100" />
		<adsm:gridColumn title="valorDesconto" property="siglaSimboloDesconto"
			width="40" dataType="text" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="vlDesconto" dataType="currency"
				width="100" />
		</adsm:gridColumnGroup>

	<adsm:i18nLabels>
		<adsm:include key="LMS-36003"/>
		<adsm:include key="LMS-36271"/>
		<adsm:include key="LMS-36269"/>
	</adsm:i18nLabels>

		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"
				caption="excluirDocumentoServico"
				service="lms.contasreceber.manterFaturasAction.removeByIdsItemFatura" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script>
	document.getElementById("tpSituacaoDevedorDocServFatValido").masterLink = "true";
	getElement("devedorDocServFat.doctoServico.tpDocumentoServico").required = true;
	getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial").required = true;
	
	/* Onchange do campo "valor do desconto". */
	function onChangeValorDesconto(){
		
		// Validar que o valor do Desconto não possa ser maior que o valor do Saldo Devedor
		if(!compareData(getElementValue("devedorDocServFat.desconto.vlDesconto"), getElementValue("saldoTotalDocumento"), "currency") && getElementValue("devedorDocServFat.desconto.vlDesconto") != ""){
			alertI18nMessage("LMS-36269", "(R$ "+getElementValue("saldoTotalDocumento")+")" , false);
			return false;
		}
		
		//Se o valor de desconto é maior que o valor total de documento, lançar exception
		if (!compareData(getElementValue("devedorDocServFat.desconto.vlDesconto"), getElementValue("devedorDocServFat.doctoServico.vlTotalDocServico"), "currency") && getElementValue("devedorDocServFat.desconto.vlDesconto") != ""){
			alert(i18NLabel.getLabel("LMS-36003")+"");
			return false;
		}

		//Se o valor do desconto é maior que o valor anterior e é maior que zero
		if (compareData(document.getElementById("devedorDocServFat.desconto.vlDesconto").previousValue, getElementValue("devedorDocServFat.desconto.vlDesconto"), "currency") && (!compareData(getElementValue("devedorDocServFat.desconto.vlDesconto"), "0.00", "currency"))){
			setElementValue("devedorDocServFat.desconto.tpSituacaoAprovacao", "E");
		}

		if (document.getElementById("devedorDocServFat.desconto.vlDesconto").previousValue != "" && getElementValue("devedorDocServFat.desconto.vlDesconto") == ""){
			setElementValue("devedorDocServFat.desconto.vlDesconto", "0")
		}


		recalculaValores();
		return true;
	}

	function recalculaValores(){
		
		var valorDevido 			= getElementValue("devedorDocServFat.doctoServico.vlTotalDocServico");
		var desconto				= getElementValue("devedorDocServFat.desconto.vlDesconto");
		var valorRecebidoParcial 	= getElementValue("sumValorRecebidoParcial"); 
		
		var novoSaldoDevedor 		= valorDevido - desconto - valorRecebidoParcial;
		
		var novoSaldoDevedorFormatado = novoSaldoDevedor.toFixed(2).replace(".",",");

		setElementValue("saldoDevedor", novoSaldoDevedorFormatado);
		format(document.getElementById("saldoDevedor"));

		return true;
	}
	
	function setValuesFromGridToItem(){
		var dataGrid = itemFaturaGridDef.gridState.data[0];						
		
		setElementValue("devedorDocServFat.doctoServico.moeda.idMoeda", dataGrid.idMoeda);
		/* Setar os filtros de documento de serviço. */
		if (validFatura()){
			setElementValue("devedorDocServFat.divisaoCliente.idDivisaoCliente", dataGrid.idDivisaoCliente);
			setElementValue("devedorDocServFat.doctoServico.servico.idServico", dataGrid.idServico);
			setElementValue("devedorDocServFat.doctoServico.servico.tpModal", dataGrid.tpModal);
			setElementValue("devedorDocServFat.doctoServico.servico.tpAbrangencia", dataGrid.tpAbrangencia);
			setElementValue("devedorDocServFat.doctoServico.moeda.idMoeda", dataGrid.idMoeda);
		}
	}
	
	function setValuesFromCadToItem(){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	
		
		setElementValue("devedorDocServFat.doctoServico.moeda.idMoeda", getElementValue(telaCad.document.getElementById("moeda.idMoeda")));
		if (validFatura){
			setElementValue("devedorDocServFat.doctoServico.servico.idServico", getElementValue(telaCad.document.getElementById("servico.idServico")));
			setElementValue("devedorDocServFat.doctoServico.servico.dsServico", getElementValue(telaCad.document.getElementById("servico.dsServico")));
			setElementValue("devedorDocServFat.doctoServico.servico.tpModal", getElementValue(telaCad.document.getElementById("tpModal")));
			setElementValue("devedorDocServFat.doctoServico.servico.tpAbrangencia", getElementValue(telaCad.document.getElementById("tpAbrangencia")));
			setElementValue("devedorDocServFat.divisaoCliente.idDivisaoCliente", getElementValue(telaCad.document.getElementById("divisaoCliente.idDivisaoCliente")));
			setElementValue("tpFreteValue", getElementValue(telaCad.document.getElementById("tpFreteValue")));
			setElementValue("tpFreteDescription", getElementValue(telaCad.document.getElementById("tpFreteDescription")));
		}		
		
	}

	function setValuesFromItemToCad(){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;
		
		setElementValue(telaCad.document.getElementById("servico.idServico"), getElementValue("devedorDocServFat.doctoServico.servico.idServico"));
		setElementValue(telaCad.document.getElementById("servico.dsServico"), getElementValue("devedorDocServFat.doctoServico.servico.dsServico"));
		setElementValue(telaCad.document.getElementById("tpModal"), getElementValue("devedorDocServFat.doctoServico.servico.tpModal"));
		setElementValue(telaCad.document.getElementById("tpAbrangencia"), getElementValue("devedorDocServFat.doctoServico.servico.tpAbrangencia"));
		setElementValue(telaCad.document.getElementById("tpFreteValue"), getElementValue("tpFreteValue"));
		setElementValue(telaCad.document.getElementById("tpFreteDescription"), getElementValue("tpFreteDescription"));
		setElementValue(telaCad.document.getElementById("divisaoCliente.idDivisaoClienteTmp"), getElementValue("devedorDocServFat.divisaoCliente.idDivisaoCliente"));
	}
	
	/* Callback da grid. */
	function myItemFatura_cb(searchFilters) {
		populaSomatorios();
		// Caso exista dados na grid, os mesmos devem ser 
		// usados como filtro na pesquisa de documentos.
		if(itemFaturaGridDef.currentRowCount > 0){
			setValuesFromGridToItem();
		// Caso não exista dados na grid, deve ser usado 
		// os dados da cad como filtro na pesquisa de documentos.
		} else {
			setValuesFromCadToItem();
		}		


		// valida se a soma dos descontos dos documentos é maior que o valorMinimo se sim, seta os campos como obrigatórios
		var totalDesc = 0;
		for (i=0; i<itemFaturaGridDef.gridState.data.length; i++) {
			if(itemFaturaGridDef.gridState.data[i].tpSituacaoAprovacao.value == "E") {
				totalDesc = totalDesc + parseFloat(itemFaturaGridDef.gridState.data[i].vlDesconto);
			}
		}
	
		if(totalDesc > 0){
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			var telaCad = tabCad.tabOwnerFrame;
			telaCad.document.getElementById("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto").required = true;
			telaCad.document.getElementById("tpSetorCausadorAbatimento").required = true;					
			if(totalDesc > parseFloat(getElementValue("vlrMinimoDoctDesc"))){
				telaCad.document.getElementById("obAcaoCorretiva").required = true;
				telaCad.document.getElementById("obFatura").required = true;
				telaCad.document.getElementById("isDesconto").value = true;					
			}
		}
	}
	
	var constTpSituacaoAprovacao = new Array();
	document.getElementById("qtdeTotalDocumentos").masterLink = "true";
	document.getElementById("valorTotalDocumentos").masterLink = "true";
	document.getElementById("valorTotalDesconto").masterLink = "true";
	document.getElementById("tpOrigem").masterLink = "true";
	document.getElementById("devedorDocServFat.doctoServico.moeda.idMoeda").masterLink = "true";
	document.getElementById("devedorDocServFat.doctoServico.servico.idServico").masterLink = "true";
	document.getElementById("devedorDocServFat.doctoServico.servico.dsServico").masterLink = "true";
	document.getElementById("devedorDocServFat.doctoServico.servico.tpModal").masterLink = "true";
	document.getElementById("devedorDocServFat.doctoServico.servico.tpAbrangencia").masterLink = "true";
	document.getElementById("devedorDocServFat.divisaoCliente.idDivisaoCliente").masterLink = "true";
	document.getElementById("tpFreteValue").masterLink = "true";
	document.getElementById("tpFreteDescription").masterLink = "true";
	document.getElementById("idCliente").masterLink = "true";
	document.getElementById("nmCliente").masterLink = "true";
	document.getElementById("nrCliente").masterLink = "true";

	/* Botão store. */
	function myStoreButton(){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;
		var elemTab = getTab(telaCad.document, false);
				
		//Valida o campo Observação	
		var obs = getElementValue("devedorDocServFat.desconto.obDesconto");
		var len = obs.length;
		if (len > 500) {
			alert(getMessage("Ajuste o campo Observação. Máximo: 500 caracteres, mas está com $0.", [ len ]));
			return false;
		}

		var currentElem = telaCad.document.getElementById("cliente.pessoa.nrIdentificacao");
		if (currentElem.required != null) {
			if (currentElem.required == 'true' && trim(currentElem.value.toString()).length == 0) {
				if(currentElem == '[object]') {
					if ((currentElem.label != undefined)) {
						alert(getMessage(erRequired, new Array(currentElem.label)));
					}
					if (elemTab != null) {
						// se a aba do elemento n?o estiver selecionada, muda a aba
						// para a correta.
						if (elemTab.properties.id != tabGroup.selectedTab.properties.id) {
							var eventObj = {name:"tab_click", src:elemTab}; 	 // simula um clique na aba, para garantir funcionamento
							tabGroup.selectTab(elemTab.properties.id, eventObj); // uniforme das rotinas de habilita??o de botoes.
						}
					}
					setFocus(currentElem);
					return false;
				}
			}
		}

		// TODO 
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.mountFaturaInSesion", "storeItemFatura", buildFormBeanFromForm(telaCad.document.forms[0])));
		xmit(false);
	}

	function storeItemFatura_cb(d,e,c,x){
		if (e != undefined) {
			alert(e+'');
		} else {
		storeButtonScript('lms.contasreceber.manterFaturasAction.storeItemFatura', 'myStoreItem', document.forms[0]);
	}
	}
	
	/* Callback do store. */
	function myStoreItem_cb(data, errorMsg, errorKey, showErrorAlert) {
		var eventObj = {name:"storeItemButton"};
		
		setElementValue("devedorDocServFat.doctoServico.servico.idServico", getElementValue("idServicoTemp"));
		
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);		
		myNewButtonScript();
 
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	

		setElementValue("idCliente", getElementValue(telaCad.document.getElementById("cliente.idCliente")));	
		setElementValue("nmCliente", getElementValue(telaCad.document.getElementById("cliente.pessoa.nmPessoa")));
		setElementValue("nrCliente", getElementValue(telaCad.document.getElementById("cliente.pessoa.nrIdentificacao")));
        
        
		if (errorMsg == undefined) {
			setElementValue("devedorDocServFat.doctoServico.moeda.idMoeda", data.devedorDocServFat.doctoServico.moeda.idMoeda);
			setElementValue(telaCad.document.getElementById("moeda.idMoeda"), getElementValue("devedorDocServFat.doctoServico.moeda.idMoeda"));

			if (validFatura()){
				setElementValue("devedorDocServFat.doctoServico.servico.idServico", data.devedorDocServFat.doctoServico.servico.idServico);
				setElementValue("devedorDocServFat.doctoServico.servico.dsServico", data.devedorDocServFat.doctoServico.servico.dsServico);
				setElementValue("devedorDocServFat.doctoServico.servico.tpModal", data.devedorDocServFat.doctoServico.servico.tpModal);
				setElementValue("devedorDocServFat.doctoServico.servico.tpAbrangencia", data.devedorDocServFat.doctoServico.servico.tpAbrangencia);			

				if (data.devedorDocServFat.divisaoCliente != undefined){
					setElementValue("devedorDocServFat.divisaoCliente.idDivisaoCliente", data.devedorDocServFat.divisaoCliente.idDivisaoCliente);
				} else {
					setElementValue("devedorDocServFat.divisaoCliente.idDivisaoCliente", "");
				}

				setElementValue("tpFreteValue", data.tpFreteValue);
				setElementValue("tpFreteDescription", data.tpFreteDescription);				
			
				setValuesFromItemToCad();
			}

			telaCad.onChangeTpAbrangencia();
			telaCad.findComboDivisaoCliente();
		} else {	
			// Caso exista dados na grid, os mesmos devem ser 
			// usados como filtro na pesquisa de documentos.
			if(itemFaturaGridDef.currentRowCount > 0){
				setValuesFromGridToItem();
			// Caso não exista dados na grid, deve ser usado 
			// os dados da cad como filtro na pesquisa de documentos.
			} else {
				setValuesFromCadToItem();
			}		
		}
		
		setFocus("devedorDocServFat.doctoServico.tpDocumentoServico");
	}	
	
	/*
	 * Monta as duas constantes que tem a lista de situação de fatura	
	 */
	function initPage_cb(d,e,o){
		if (d != null) {	
			constTpSituacaoAprovacao = d;		
		}
	}
	
	/*
	 * On change da combo de Tipo de Documento de Serivo.<BR>
	 * Altera lookup de conhecimento
	 * @see changeLookupConhecimento
	 */
	function tpDocumentoServicoOnChange(){
		resetDevedor();	
		resetValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");
		filialOnChange();		
		setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"));
	}
	
	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor(){
	
		resetValue("devedorDocServFat.idDevedorDocServFat");
		resetValue("devedorDocServFat.doctoServico.nrDoctoServico");
		
		if (getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico") == "") {
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("devedorDocServFat.idDevedorDocServFat",true);	
		} else {
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",false);
			habilitaLupa();
			if (getElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial") != ""){
				setDisabled("devedorDocServFat.idDevedorDocServFat",false);		
			}
		}			
	}
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("devedorDocServFat.idDevedorDocServFat", false);
		setDisabled("devedorDocServFat.doctoServico.nrDoctoServico", true);
	}				
	
	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
		resetDevedor();
		
		var siglaFilial = getElement('devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = devedorDocServFat_doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('devedorDocServFat_lupa',false);		
		}
		
		return retorno;		
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
		resetDevedor();
		if (data.length == 1) {			
			setDisabled("devedorDocServFat.idDevedorDocServFat",false);
			setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico"))					
	
			__lookupSetValue({e:getElement("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"), data:data[0]});
			
			return true;
		} else {
			alert(lookup_noRecordFound);
		}		
	}
	
	/*
	 * Habilita ou desabilita os componentes da tag documento de serviço
	 */
	function disableDocumentoServico(){
		//Se não tem tipo de documento, desabilitar a filial
		if (getElementValue("devedorDocServFat.doctoServico.tpDocumentoServico") == "") {
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("devedorDocServFat.idDevedorDocServFat",true);
		} else {
			setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",false);
		}

		//Se não tem filial, desabilitar o documento de serviço
		if (getElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial") == ""){
			setDisabled("devedorDocServFat.idDevedorDocServFat",true);
		} else {
			setDisabled("devedorDocServFat.idDevedorDocServFat",false);		
		}
	}
	
	/* Limpar todos os campos de desconto. */
	function limparDesconto(){
		resetValue("devedorDocServFat.desconto.tpSituacaoAprovacao");
		resetValue("devedorDocServFat.desconto.idDesconto");
		resetValue("devedorDocServFat.desconto.vlDesconto");
		resetValue("devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem");
		resetValue("devedorDocServFat.desconto.obDesconto");
	}
	
	function verificaTipoDocumentoSelecionado(data, cb){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject("lms.contasreceber.manterFaturasAction.validateMonitoramentoEletronicoAutorizado", cb, { data:data});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}
	
	/* Popup callback da lookup de devedor. */
	function lookUpDevedor(data, dialogWindow) {
		return verificaTipoDocumentoSelecionado(data, "lookUpDevedorCb");
	}
	
	/* Popup callback da lookup de devedor. */
	function lookUpDevedorCb_cb(data, error) {
		if (error) {
			alert(error);
			resetValue('devedorDocServFat.doctoServico.idDoctoServico');
			resetDevedor();
			setFocus("devedorDocServFat.doctoServico.nrDoctoServico");
		} else {
			if (data.data.tpSituacaoAprovacao != undefined) {
				setElementValue("devedorDocServFat.desconto.tpSituacaoAprovacao", data.data.tpSituacaoAprovacao.value);
			}
			setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",data.data.idFilialOrigem);
			setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial",data.data.sgFilialOrigem);
			setElementValue("idMotivoDesconto", data.idMotivoDesconto);
	
			resetDevedor();
		}
	}
	
	function lookUpDevedor_cb(data){
		devedorDocServFat_doctoServico_nrDoctoServico_exactMatch_cb(data)
		if (data.length <= 0){
			return;
		}
		if (data.length > 1){
			lookupClickPicker({e:document.forms[0].elements['devedorDocServFat.idDevedorDocServFat']});
		} else {
			verificaTipoDocumentoSelecionado(data[0], "lookUpDevedor_cb");
		}
	}
		
	function lookUpDevedor_cb_cb(data, error){
		if (error) {
			alert(error);
			resetValue('devedorDocServFat.doctoServico.idDoctoServico');
			resetDevedor();
			setFocus("devedorDocServFat.doctoServico.nrDoctoServico");
		} else {
			if (data.data.tpSituacaoAprovacao != undefined){	
				setElementValue("devedorDocServFat.desconto.tpSituacaoAprovacao", data.data.tpSituacaoAprovacao.value);								
			}		
			setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",data.data.idFilialOrigem);
			setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial",data.data.sgFilialOrigem);
			setElementValue("idMotivoDesconto", data.data.idMotivoDesconto);			
			
			if(data.data.hasDataEntregaCtrc == "true") {
				if(!confirm(i18NLabel.getLabel("LMS-36271"))) {
					setElementValue("devedorDocServFat.doctoServico.tpDocumentoServico", 0);
					setElementValue("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial", "");
					resetValue("devedorDocServFat.idDevedorDocServFat");
					setFocus("devedorDocServFat.doctoServico.tpDocumentoServico");
				}
			}
		}
	}
	
	function lookUpDevedorOnChange(){
		if (getElementValue("devedorDocServFat.doctoServico.nrDoctoServico") == ""){
			resetValue("devedorDocServFat.desconto.tpSituacaoAprovacao");	
		}
		resetValue("devedorDocServFat.desconto.vlDesconto");
		resetValue("devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem");	
		resetValue("devedorDocServFat.desconto.obDesconto");
		
		return devedorDocServFat_doctoServico_nrDoctoServicoOnChangeHandler();
	}	
	
	function myOnShow(e){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	
			
		if (e != undefined && e.properties.id == "pesq"){
			telaCad.newButtonScript();
		}
		
		myNewButtonScript();
		
		document.getElementById("idCliente").value = telaCad.document.getElementById("cliente.idCliente").value;	
		document.getElementById("nmCliente").value = telaCad.document.getElementById("cliente.pessoa.nmPessoa").value;
		document.getElementById("nrCliente").value = telaCad.document.getElementById("cliente.pessoa.nrIdentificacao").value;
		
		document.getElementById("devedorDocServFat.doctoServico.moeda.idMoeda").value = telaCad.document.getElementById("moeda.idMoeda").value;
		document.getElementById("tpOrigem").value = telaCad.document.getElementById("tpOrigem").value;
		
		if (!validFatura()){
			setValuesDefault();		
		}
		
		setFocusOnFirstFocusableField();
		return false;
	}
	
	function setValuesDefault(){
		document.getElementById("devedorDocServFat.doctoServico.servico.idServico").value = "";
		document.getElementById("devedorDocServFat.doctoServico.servico.dsServico").value = "";
		document.getElementById("devedorDocServFat.doctoServico.servico.tpModal").value = "";
		document.getElementById("devedorDocServFat.doctoServico.servico.tpAbrangencia").value = "";
		document.getElementById("devedorDocServFat.divisaoCliente.idDivisaoCliente").value = "";
		document.getElementById("tpFreteValue").value = "";
		document.getElementById("tpFreteDescription").value = "";
	}
		
	function myOnDataLoad_cb(d,e,c,x){	
		if (e==undefined){
			setMaskNrDocumento("devedorDocServFat.doctoServico.nrDoctoServico", d.devedorDocServFat.doctoServico.tpDocumentoServico.value);
		}		
		
		var idDesconto;
		if (d.devedorDocServFat && d.devedorDocServFat.desconto && d.devedorDocServFat.desconto.motivoDesconto ) {
			idDesconto = d.devedorDocServFat.desconto.idDesconto;
			setElementValue("idMotivoDesconto", d.devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto);
		}
		
		setDisabled("devedorDocServFat.doctoServico.tpDocumentoServico", true);
		setDisabled("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("devedorDocServFat.idDevedorDocServFat", true);
		// Repopula a combo de motivo de desconto, pois pode estar sendo detalhado um desconto 
		// com um motivo inativo
		//findComboMotivoDesconto(idDesconto);

		if( getElementValue('idItemFatura') != '' ){
			setDisabled('btnConsultarDoctoServico',false);
		} else {
			setDisabled('btnConsultarDoctoServico',true);
		}

		disabledButtons();
		onDataLoad_cb(d,e,c,x);
	}
	
	function myNewButtonScript(){
		setDisabled("devedorDocServFat.doctoServico.tpDocumentoServico", false);
		resetDevedor();
		newButtonScript();
	}
	
	function buscarItemFatura(){
		var url = '/contasReceber/manterFaturas.do?cmd=buscar';
		showModalDialog(url,window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:150px;')
	}
	
	/** Função para buscar os somatórios dos itens de fatura */
	
	function populaSomatorios(){
	      var remoteCall = {serviceDataObjects:new Array()};
	      var dataCall = createServiceDataObject("lms.contasreceber.manterFaturasAction.findSomatorios", "populaSomatorios", 
	            {
	                  idFatura:getElementValue("masterId")
	            }
	      );
	      remoteCall.serviceDataObjects.push(dataCall);
	      xmit(remoteCall);  
	
	}
	
	/** Função (callBack) para popular os dados dos somatórios */
	
	function populaSomatorios_cb(data, error, erromsg){
		if (error != undefined) {
			alert(error+'');
			return;
		}
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;	
		
		setElementValue(getElement("valorTotalDocumentos"), setFormat(getElement("valorTotalDocumentos"), data.valorTotalDocumentos));
		setElementValue(getElement("valorTotalDesconto"), setFormat(getElement("valorTotalDesconto"), data.valorTotalDesconto));		
	    setElementValue("qtdeTotalDocumentos",data.qtdeTotalDocumentos);
		setElementValue(telaCad.document.getElementById("vlDesconto"),setFormat(getElement("valorTotalDesconto"), data.valorTotalDesconto));	    	    
	}

	function initWindow(eventObj){
    	if( getElementValue('idItemFatura') != '' ){
			setDisabled('btnConsultarDoctoServico',false);
		} else {
			setDisabled('btnConsultarDoctoServico',true);
		}
		
		if( eventObj.name == 'removeButton_grid' ){
			myNewButtonScript();
		}
		
		setDisabled('selecionarDocumentosServico',false);			
		
		disabledButtons();
		findVlrMinimoDoctoDesc();//carrega o campo hidden vlrMinimoDoctDesc
		// Carrega a combo de motivos de desconto.
		//findComboMotivoDesconto();
	}
	
	function disabledButtons(){
		var tabGroup = getTabGroup(this.document);
		var tabCad = tabGroup.getTab("cad");
		var telaCad = tabCad.tabOwnerFrame;
			
		if (telaCad.getElementValue("idProcessoWorkflow") != ""){
			setDisabled("storeButton",true);
			setDisabled("removeButton",true);		
			setDisabled("cleanButton",true);		
			setDisabled("btnConsultarDoctoServico",true);
			setDisabled("storeAllButtton",true);
			
			return false;
		}	
	}
	
	function findNavigatedItemFatura(idDevedorDocServFat){
	
		var dataObject = new Object();
		_serviceDataObjects = new Array();	
		dataObject.idDevedorDocServFat = idDevedorDocServFat;
		dataObject.idFatura = getElementValue("masterId");
		dataObject._pageSize = itemFaturaGridDef.pageSize;
		dataObject._order = {};
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findNavigatedItemFatura", "findNavigatedItemFatura", dataObject));
		xmit(false);	
	}

	function findNavigatedItemFatura_cb(d,e){
		if (e == undefined) {
		
			if (d.currentPage != undefined){
				itemFaturaGridDef.gotoPage(d.currentPage);
				myOnDataLoad_cb(d);
				disabledButtons();
			}
		} else {
			alert(e+'');
		}
	}
	
	/**
		retorno um boolean que informa se tem que filtrar os itens de fatura
	*/
	function validFatura(){
		if (getElementValue("tpOrigem") != "I" && getElementValue("tpOrigem") != "P"){
			return true;
		}
		
		return false;
	}
	
	function findComboMotivoDesconto(idDesconto){
		var dados = new Array();        
		_serviceDataObjects = new Array();
		
		setNestedBeanPropertyValue(dados, "tpSituacao", getElementValue("tpSituacaoMotivoDesconto"));
        setNestedBeanPropertyValue(dados, "desconto.idDesconto", idDesconto);
        
        addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboMotivoDesconto",
			                                         "retornoMotivoDesconto",
			                                         dados));

		xmit(false);	      
	}
	
	function findComboMotivoDescontoDefault(dados){
		
	}
	
	/**
	*	Função utilizada para setar o valor correto do motivo desconto quando esta tela é chamada pelo workflow
	*   OBS: Este método não é utilizado para a combo de motivo desconto a qual está utilizando o retorno padrão
	*/
	function retornoMotivoDesconto_cb(data,erro){
		comboboxLoadOptions({e:document.getElementById("devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem"), data:data});
		setElementValue('devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem', getElementValue('idMotivoDesconto'));
	}
	
	function retornoMotivoDesconto_cb(data,erro){
		comboboxLoadOptions({e:document.getElementById("devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem"), data:data});
		setElementValue('devedorDocServFat.desconto.motivoDesconto.idMotivoDescontoItem', getElementValue('idMotivoDesconto'));
	}	
</script>
