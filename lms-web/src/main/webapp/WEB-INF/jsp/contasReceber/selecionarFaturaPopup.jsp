<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterFaturasAction" 
	onPageLoadCallBack="myOnPageLoad" >
	<adsm:form action="/contasReceber/manterFaturas"
		service="lms.contasreceber.manterFaturasAction" idProperty="idFatura" >

		<adsm:i18nLabels>
			<adsm:include key="LMS-36218"/>
		</adsm:i18nLabels>		
		
		<adsm:hidden property="tpSituacaoFaturaValido" serializable="true" />

		<adsm:hidden property="tpAbrangencia" serializable="true" />
		<adsm:hidden property="tpModal" serializable="true" />
		
		<adsm:lookup property="filialByIdFilial" idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.contasreceber.manterFaturasAction.findLookupFilial"
			dataType="text" label="filialFaturamento" size="3"
			action="/municipios/manterFiliais" width="9%" 
			minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px"
			maxLength="3" disabled="true">
			<adsm:propertyMapping
				relatedProperty="filialByIdFilial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialByIdFilial.pessoa.nmFantasia" width="26%" size="30"
				serializable="false" disabled="true" />
		</adsm:lookup>

		<adsm:combobox property="tpSituacaoFatura" label="situacaoFatura"
			domain="DM_STATUS_ROMANEIO" />

		<adsm:hidden property="tpFatura" value="R" serializable="true" />

		<adsm:lookup action="/vendas/manterDadosIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" exactMatch="true" idProperty="idCliente"
			label="cliente" maxLength="20" property="cliente"
			service="lms.contasreceber.manterFaturasAction.findInitialValue"
			size="20" labelWidth="15%" width="85%" disabled="true">

			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />

			<adsm:textbox dataType="text" disabled="true"
				property="cliente.pessoa.nmPessoa" serializable="false" size="58" />

		</adsm:lookup>

		<adsm:range label="dataEmissao">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" size="10"
				maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" size="10"
				maxLength="20" />
		</adsm:range>
		<adsm:range label="dataVencimento">
			<adsm:textbox dataType="JTDate" property="dtVencimentoInicial"
				size="10" maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtVencimentoFinal"
				size="10" maxLength="20" />
		</adsm:range>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				buttonType="findButton"
				caption="consultar" 
				id="__buttonBar:0.findButton" 
				disabled="false" 
				onclick="return myFindButtonScript('fatura', this.form);"/>
			<adsm:button caption="limpar" id="cleanButton" disabled="false" onclick="limparCampos()" />
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid idProperty="idFatura" property="fatura"
		scrollBars="horizontal" rows="10" gridHeight="270"
		defaultOrder="filialByIdFilial_.sgFilial, nrFatura"
		service="lms.contasreceber.manterFaturasAction.findPaginatedFatura"
		rowCountService="lms.contasreceber.manterFaturasAction.getRowCountFatura"
		onRowClick="rowClickFatura">

		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="fatura"
				property="filialByIdFilial.sgFilial" />
			<adsm:gridColumn width="85" title="" property="nrFatura"
				mask="0000000000" dataType="integer" />
		</adsm:gridColumnGroup>

		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="cliente"
				property="cliente.pessoa.nrIdentificacao" width="80" />
			<adsm:gridColumn title="" property="cliente.pessoa.nmPessoa"
				width="150" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="situacao" width="100" isDomain="true"
			property="tpSituacaoFatura" />

		<adsm:gridColumn title="dataEmissao" dataType="JTDate"
			property="dtEmissao" width="80" />
		<adsm:gridColumn title="dataVencimento" dataType="JTDate"
			property="dtVencimento" width="80" />
		

		<adsm:gridColumn title="valorTotal" property="siglaSimbolo" width="55"
			dataType="text" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="vlTotal" dataType="currency"
				width="65" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="valorDesconto" property="siglaSimboloDesconto"
			width="55" dataType="text" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="vlDesconto" dataType="currency"
				width="65" />
		</adsm:gridColumnGroup>

		<adsm:buttonBar>
			<adsm:button buttonType="button" caption="utilizarTodosFaturasLocalizadas" boxWidth="245" onclick="selectAllFaturas();"/>
		    <adsm:button buttonType="button" caption="utilizarFaturasSelecionadas" boxWidth="230" onclick="selectedFaturas();"/>
			<adsm:button id="closeButton" caption="fechar" disabled="false" onclick="javascript:window.close();" buttonType="closeButton"/>
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script>




//Seleciona varias faturas - INICIO

	/**
	 * Captura os ids selecionados na grid.
	 */
	function selectedFaturas(){
	    var ids = faturaGridDef.getSelectedIds();
	    
		if (ids.ids.length >0){   
		    dialogArguments.window.setGridSelectedIds(ids);
       	    self.close();
       	}       	

	}
	
	function rowClickFatura(pkValue) {
		
		var ids = {ids:[pkValue]};

	    dialogArguments.window.setGridSelectedIds(ids);
   	    self.close();
		
		return true;
	}

	function Grid_getSelectedIds() 
		{
			var gridFormElems = document.getElementById("idDoctoServico").elements;
			// array que armazena os ids das linhas que deverão ser removidas da camada de dados.
			var selectedIds = new Array();
			for (var j = 0; j < gridFormElems.length; j++) 
			{
				if ((gridFormElems[j].type == "checkbox") || (gridFormElems[j].type == "radio")) {
					if ((gridFormElems[j].name.indexOf("."+this.idProperty)>0) || (gridFormElems[j].type == "radio")) {
						if (gridFormElems[j].checked) {
							if (gridFormElems[j].value != "undefined" && gridFormElems[j].value != "null") {
								selectedIds.push(gridFormElems[j].value);
							}
						}
					}
				}
			}
			var selMap = new Object();
			selMap["ids"] = selectedIds;
			return selMap;
		}
	
	/**
	 * Captura todos os ids da grid.
	 */
	function selectAllFaturas(){

		var data = new Object();
		var dtEmissaoInicial = new Object();
		var dtEmissaoFinal = new Object();
		var dtVencimentoInicial = new Object();
		var dtVencimentoFinal = new Object();
		var cliente = new Object();
		var filialByIdFilialCobradora = new Object();
		var filialByIdFilial = new Object();
		var tpAbrangencia = new Object();
		var tpModal = new Object();
		var tpSituacaoFatura = new Object();
		
		dtEmissaoInicial = getElementValue("dtEmissaoInicial");
		dtEmissaoFinal = getElementValue("dtEmissaoFinal");
		dtVencimentoInicial = getElementValue("dtVencimentoInicial");
		dtVencimentoFinal = getElementValue("dtVencimentoFinal");
		cliente.idCliente = getElementValue("cliente.idCliente");
		filialByIdFilial.idFilial = getElementValue("filialByIdFilial.idFilial");
		filialByIdFilial.sgFilial = getElementValue("filialByIdFilial.sgFilial");
		tpAbrangencia = getElementValue("tpAbrangencia");
		tpModal = getElementValue("tpModal");
		tpSituacaoFatura = getElementValue("tpSituacaoFatura");

		data.dtEmissaoInicial = dtEmissaoInicial;
		data.dtEmissaoFinal = dtEmissaoFinal;
		data.dtVencimentoInicial = dtVencimentoInicial;
		data.dtVencimentoFinal = dtVencimentoFinal;
		data.dtEmissaoInicial = dtEmissaoInicial;
		data.cliente = cliente;
		data.filialByIdFilial = filialByIdFilial;
		data.tpAbrangencia = tpAbrangencia;
		data.tpModal = tpModal;
		data.tpSituacaoFatura = tpSituacaoFatura;

		getElementValue("tpAbrangencia")
		data._currentPage=1
		data._pageSize=100

		var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findAllIdFaturas", 
					"selectAllFaturas", data);
	    xmit({serviceDataObjects:[sdo]});

	}
	
	function selectAllFaturas_cb(data, error){
		if (data != undefined && data != null){
		    dialogArguments.window.setGridSelectedIds(data);
       	    self.close();
       	}     
	}
	

//Seleciona varias faturas - FIM


	document.getElementById("tpSituacaoFaturaValido").masterLink = "true";
	document.getElementById("tpFatura").masterLink = "true";
	document.getElementById("filialByIdFilial.sgFilial").serializable = true;

	/**
	*	Busca a filial do usuário logado
	*/
	function findFilialUsuario(){
		if (getElement("filialByIdFilial.idFilial").masterLink == undefined){
			var dados = new Array();
	        
	        var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findFilialSessao",
	                                          "findFilialUsuario",
	                                          dados);
	        xmit({serviceDataObjects:[sdo]});
        }	
	}

	/**
	*	Retorno da busca da filial do usuário.
	*   Seta a filial da seção na lookup de filial
	*/
	function findFilialUsuario_cb(data,erro){
		if (erro == undefined){
			setElementValue("filialByIdFilial.idFilial", data.filialByIdFilial.idFilial);
			setElementValue("filialByIdFilial.sgFilial", data.filialByIdFilial.sgFilial);
			setElementValue("filialByIdFilial.pessoa.nmFantasia", data.filialByIdFilial.pessoa.nmFantasia);
		}			
	}

	/** dados cliente **/
	function carregaDadosCliente() {
		var u = new URL(parent.location.href);
		var idCliente = u.parameters["idCliente"];
		var map = new Array();
		setNestedBeanPropertyValue(map, "idCliente", idCliente);
	    var sdo = createServiceDataObject("lms.coleta.consultaClientesColetaAction.findCliente", "resultadoCarregaDadosCliente", map);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	 function resultadoCarregaDadosCliente_cb(data, error) {
		if(error!=undefined) {
			alert(error);
			return false;
		}
		setElementValue('cliente.pessoa.nrIdentificacao', data.pessoa.nrIdentificacao);
		setElementValue('cliente.pessoa.nmPessoa',data.pessoa.nmPessoa);
		setElementValue('cliente.idCliente', data.idCliente);
		
		document.getElementById("cliente.pessoa.nrIdentificacao").masterLink=true;		
		document.getElementById("cliente.pessoa.nmPessoa").masterLink=true;		
		document.getElementById("cliente.idCliente").masterLink=true;		
	}
	 
	 
	
	function myOnShow(x, eventObj){		
		if (eventObj.name == "tab_click"){		
			getTabGroup(this.document).getTab("cad").tabOwnerFrame.initPage();						
		}		
		tab_onShow();
	}
	
	function initWindow(eventObj) {
    	if (eventObj.name == "cleanButton_click") {
	    	initPage();
	    }
	}

	function initPage(){
		if (getElement('filialByIdFilial.sgFilial').masterLink != 'true' && getElement('nrFatura').masterLink != 'true'){
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findFilialSessao", "initPage", null)); 
			xmit(false);
		}
	}
	
	/*
	 * Monta as duas constantes que tem a lista de situação de fatura	
	 */
	function initPage_cb(d,e,o,x){
		if (e == undefined) {	
			fillFormWithFormBeanData(0, d);		
		}
	}
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);		
		findFilialUsuario();
		carregaDadosCliente();
	}
	
	function myFindButtonScript(callback, form){
		if (!validateForm(document.forms[0])){
			return false;
		}
		
		return findButtonScript(callback, form);
	}	

	function limparCampos() {
		setElementValue("dtEmissaoInicial", "");
		setElementValue("dtEmissaoFinal", "");
		
		setElementValue("dtVencimentoInicial", "");
		setElementValue("dtVencimentoFinal", "");
		
		setElementValue("tpSituacaoFatura", "");
	}
	
</script>