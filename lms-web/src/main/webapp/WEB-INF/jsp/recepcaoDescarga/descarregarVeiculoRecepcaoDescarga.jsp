<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects_cb(data, error) {
		onPageLoad_cb(data, error);
    	var data = new Array();
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.getBasicData", "loadBasicData", data);
    	xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var blRncAutomaticaNovo = false;
	var basicData;
	function loadBasicData_cb(data, error) {
		basicData = data;
		fillBasicData();
	}
</script>

<adsm:window service="lms.recepcaodescarga.descarregarVeiculoAction" onPageLoadCallBack="loadDataObjects">
	<adsm:i18nLabels>
		<adsm:include key="LMS-03022"/>
	</adsm:i18nLabels>
	<adsm:form action="/recepcaoDescarga/descarregarVeiculo" height="128" idProperty="idCarregamentoDescarga">

		<adsm:hidden property="blInformaKmPortaria"/>
		<adsm:hidden property="idFilial"/>
		<adsm:textbox dataType="text" property="sgFilial" label="filial" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox dataType="text" property="pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="idFilialPostoAvancado"/>
		<adsm:textbox dataType="text" property="sgFilialPostoAvancado"
					  label="postoAvancado" maxLength="3" size="3" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoaPostoAvancado" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="abreRncAutomatica" />
		<adsm:hidden property="tpStatusOperacao"/>
		<adsm:hidden property="controleCarga.tpControleCarga"/>
		<adsm:hidden property="controleCarga.tpStatusControleCarga"/>
		<adsm:hidden property="filialColetorDadoScan"/>
		<adsm:hidden property="onlyRemetenteComEtiquetaEDI"/>
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem"
					 idProperty="idFilial" criteriaProperty="sgFilial"
					 service="lms.recepcaodescarga.descarregarVeiculoAction.findLookupBySgFilial"
					 action="/municipios/manterFiliais"
					 onchange="return sgFilialOnChangeHandler();"
					 onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="18%" width="32%"
					 size="3" maxLength="3" picker="false" serializable="false" popupLabel="pesquisarFilial">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			<adsm:lookup dataType="integer" property="controleCarga"
						 idProperty="idControleCarga" criteriaProperty="nrControleCarga"
						 service="lms.recepcaodescarga.descarregarVeiculoAction.findCarregamentoDescargaByNrControleCarga"
						 action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="loadDataByNrControleCarga"
						 onDataLoadCallBack="loadDataByNrControleCarga"
						 onchange="return checkValueControleCarga(this.value)"
						 maxLength="8" size="8" mask="00000000" popupLabel="pesquisarControleCarga">
				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" disable="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial"/>
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial"/>
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
 				<adsm:propertyMapping modelProperty="tpControleCarga" criteriaProperty="tpControleCarga" disable="true" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:textbox property="tpControleCarga" dataType="text"
					   label="tipo" labelWidth="18%" width="32%" disabled="true"/>

		<adsm:textbox property="tpStatusControleCarga" dataType="text" size="30"
					   label="status" labelWidth="18%" width="32%" disabled="true"/>

		<adsm:textbox property="dhEvento" dataType="JTDateTimeZone"
					  label="chegadaPortaria" labelWidth="18%" width="32%" disabled="true" picker="false"/>

		<adsm:textbox property="nrFrotaTransporte" dataType="text"
					  label="meioTransporte" maxLength="6" size="6" labelWidth="18%" width="32%" disabled="true">
			<adsm:textbox property="nrIdentificadorTransporte"
						  dataType="text" maxLength="25" size="25" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="nrFrotaSemiReboque" dataType="text"
					  label="semiReboque" size="6" maxLength="6" labelWidth="18%" width="32%" disabled="true">
			<adsm:textbox property="nrIdentificadorSemiReboque"
						  dataType="text" maxLength="25" size="25" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="dhInicioOperacao" dataType="JTDateTimeZone" label="inicioDescarga" labelWidth="18%" width="32%" disabled="true" picker="false"/>

		<adsm:textbox property="dhFimOperacao" dataType="JTDateTimeZone" label="fimDescarga" labelWidth="18%" width="32%" disabled="true" picker="false"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="reset" caption="limpar" onclick="resetView()"/>
		</adsm:buttonBar>

		<script type="text/javascript">
			function lms_03005() {
				alert("<adsm:label key='LMS-03005'/>");
			}
			function lms_03009() {
				alert("<adsm:label key='LMS-03009'/>");
			}
			function lms_03010() {
				alert("<adsm:label key='LMS-03010'/>");
			}
			function lms_03011() {
				alert("<adsm:label key='LMS-03011'/>");
			}
			function lms_03013() {
				alert("<adsm:label key='LMS-03013'/>");
			}
			function lms_45075() {
				alert("<adsm:label key='LMS-45075'/>");
			}
			function lms_03020() {
				alert("<adsm:label key='LMS-03020'/>");
			}
		</script>

	</adsm:form>

	<adsm:grid property="manifesto" idProperty="idManifesto" onRowClick="manifestoColetaOnRowClick" rows="8"
			   selectionMode="none" gridHeight="100" onPopulateRow="onPopulateRow" onDataLoadCallBack="manifestoCallBack"
			   service="lms.recepcaodescarga.descarregarVeiculoAction.findPaginatedCarregamentoDescarga"
			   rowCountService="lms.recepcaodescarga.descarregarVeiculoAction.getRowCountCarregamentoDescarga">
        <adsm:gridColumnGroup separatorType="MANIFESTO">
    		<adsm:gridColumn property="sgFilialOrigem" title="manifesto" width="30"/>
    		<adsm:gridColumn property="nrManifesto" title="" dataType="integer" width="70" mask="00000000"/>
        </adsm:gridColumnGroup>
		<adsm:gridColumn property="dhGeracaoPreManifesto" dataType="JTDateTimeZone" title="dataHoraGeracao" width="140" align="center"/>
		<adsm:gridColumn property="tpManifesto" isDomain="true" title="tipo" width="140"/>
		<adsm:gridColumn property="tpStatusManifesto" isDomain="true" title="status" width="165"/>
		<adsm:gridColumnGroup customSeparator=" ">
		    <adsm:gridColumn property="sgMoeda" title="valorTotal" width="30"/>
		    <adsm:gridColumn property="dsSimbolo" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlTotalManifesto" dataType="currency" title="" width="70"/>
		<adsm:gridColumn property="documentos" title="documentos" align="center" image="/images/popup.gif" link="javascript:carregaDocumentos" linkIdProperty="idManifesto" popupDimension="790,496" width="80"/>
		<adsm:editColumn title="hidden" property="tpStatusManifesto.value" dataType="text" field="hidden" />

		<adsm:buttonBar>
			<adsm:button id="abrirRNC" caption="abrirRNC" action="/rnc/abrirRNC" cmd="main"/>
			<adsm:button id="trocarEquipe" caption="trocarEquipe" onclick="showModalDialog('recepcaoDescarga/descarregarVeiculo.do?cmd=trocar',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');"/>
			<adsm:button id="reabrirDescarga" caption="reabrirDescarga" onclick="reabrirDescargaVeiculo()" />
			<adsm:button id="iniciarDescarga" caption="iniciarDescarga" onclick="iniciarDescargaVeiculo()" />
			<adsm:button id="finalizarDescarga" caption="finalizarDescarga" onclick="verificaBlRncAutomaticaNovo()"/>
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script type="text/javascript"><!--
	var self = document;
	function initWindow(eventObj) {
		setDisabled("reset", false);
		if(getElementValue("controleCarga.filialByIdFilialOrigem.idFilial") == "") {
			setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
		}
		if (eventObj.name == "tab_click") {
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
			disableButtons(false);
		}
	}

	//##################################
    // Comportamentos apartir de objetos
	//##################################


	/**
	 * Controla o objeto de controle carga
	 */
	function sgFilialOnChangeHandler() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial")=="") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			resetView();
		} else {
			disableNrControleCarga(false);
		}
		return lookupChange({e:document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]});
	}

	function disableNrControleCarga_cb(data, error) {
		if (data.length==0) disableNrControleCarga(false);
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}

	/**
	 * Carrega os dados da tela de descarregarVeiculos apartir dos dados retornados da
	 * consulta de 'findCarregamentoDescargaByNrControleCarga'
	 */
	function loadDataByNrControleCarga_cb(data, error){
		controleCarga_nrControleCarga_exactMatch_cb(data);

		//Verifica se este objeto e nulo
		if (data[0]!=undefined) {
       		resetValue(this.document);
			fillBasicData();

			setElementValue("idCarregamentoDescarga", data[0].idCarregamentoDescarga);
			setElementValue("controleCarga.idControleCarga", data[0].idControleCarga);
			setElementValue("controleCarga.nrControleCarga", data[0].nrControleCarga);
			setElementValue("controleCarga.tpControleCarga", data[0].tpControleCarga);
			setElementValue("controleCarga.tpStatusControleCarga", data[0].tpStatusControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data[0].idFilialControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data[0].sgFilialControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", data[0].nmFantasia);
			document.getElementById("tpControleCarga").value = data[0].tpControleCarga.description;
			document.getElementById("tpStatusControleCarga").value = data[0].tpStatusControleCarga.description;
			setElementValue("dhEvento", setFormat(document.getElementById("dhEvento"), data[0].dhEvento));
			setElementValue("nrFrotaTransporte", data[0].nrFrotaTransporte);
			setElementValue("nrIdentificadorTransporte", data[0].nrIdentificadorTransporte);
			setElementValue("nrFrotaSemiReboque", data[0].nrFrotaSemiReboque);
			setElementValue("nrIdentificadorSemiReboque", data[0].nrIdentificadorSemiReboque);
			setElementValue("filialColetorDadoScan", data[0].filialColetorDadoScan);
			setElementValue("onlyRemetenteComEtiquetaEDI", data[0].onlyRemetenteComEtiquetaEDI);
			setElementValue("tpStatusOperacao", data[0].tpStatusOperacao);

			if (data[0].dhInicioOperacao!=undefined) {
				setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), data[0].dhInicioOperacao));
			}
			if (data[0].dhFimOperacao!=undefined) {
				setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), data[0].dhFimOperacao));
			}

			// Formata o campo de nrControlecarga
			format(document.getElementById("controleCarga.nrControleCarga"));

			populateGrid();
			setDisableTab(false);
			tabGroup = getTabGroup(this.document);
			if(data[0].tpControleCarga.value != "C")
				tabGroup.setDisabledTab("abaDocumentoServico", false);

			disableButtons(false);
			setFocus(document.getElementById("controleCarga.nrControleCarga"));

			if(data[0].tpStatusOperacao == undefined || "O" != data[0].tpStatusOperacao.value){
				document.getElementById("reabrirDescarga").disabled = true;
			}
		} else {
			var idFilial = getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
            var sgFilial = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");
            var nmFantasia = getElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia");
       		resetValue(this.document);
			manifestoGridDef.resetGrid();
			setDisableTab(true);
			disableButtons(true);
			document.getElementById("trocarEquipe").disabled = true;
			fillBasicData();
            setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
            setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", nmFantasia);
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
		}
	}

	function enableNrControleCarga(data){
		disableNrControleCarga(false);
	}

	function disableNrControleCarga(disable) {
		document.getElementById("controleCarga.nrControleCarga").disabled = disable;
	}

	/**
	 * Chama a consulta de 'findCarregamentoDescargaByNrControleCarga' a partir de um dos dados retornados
	 * da lookup
	 */
	function loadDataByNrControleCarga(data) {
		setNestedBeanPropertyValue(data, "filialByIdFilialOrigem.idFilial", data.filialByIdFilialOrigem.idFilial);
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.findCarregamentoDescargaByNrControleCarga", "loadDataByNrControleCarga", data);
    	xmit({serviceDataObjects:[sdo]});
    	setDisabled("controleCarga.nrControleCarga", false);
	}

	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueControleCarga(valor) {

		controleCarga_nrControleCargaOnChangeHandler();

		if (valor=="") {
			var idFilial = getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
            var sgFilial = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");
            var nmFantasia = getElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia");
       		resetValue(this.document);
			manifestoGridDef.resetGrid();
			setDisableTab(true);
			disableButtons(true);
			fillBasicData();
	        setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
            setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
            setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", nmFantasia);
            setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
		}
		return true;
	}

	/**
	 * Popula a grid
	 */
	function populateGrid() {
		var formData = new Object();
		setNestedBeanPropertyValue(formData, "idControleCarga", getElementValue("controleCarga.idControleCarga"));
		setNestedBeanPropertyValue(formData, "tpControleCarga", getElementValue("controleCarga.tpControleCarga"));
		manifestoGridDef.executeSearch(formData, true);
	}

	//#####################################################
	// Inicio da validacao do pce
	//#####################################################

	//Esta variavel deve ser declarada decorrente de uma necessidade da tela de popUp...
	var codigos;
	function getCodigos() {
		return codigos;
	}

	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validatePCE() {

		list = new Array();

		if (manifestoGridDef.gridState.data.length>0) {
			for (var i=0; i < manifestoGridDef.gridState.data.length; i++) {
				var row = new Object();
				row.idManifesto = manifestoGridDef.gridState.data[i].idManifesto;
				row.valido = false;
				list[i] = row;
			}

			var data = new Object();
			data.list = list;
			data.tpControleCarga=getElementValue("controleCarga.tpControleCarga");
			var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.validatePCE", "validatePCE", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	/**
	 * Callback da validacao.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {

		// Janela de chamada para a tela de pce
		// Apos sua chamada cai na funcao de callBack - alertPCE
		codigos = data.codigos;
		if (codigos.length>0) {
			showModalDialog('vendas/alertaPce.do?cmd=list', window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:353px;');
		}

		//Redireciona para o metodo de acordo com o botao selecionado.
		if (methodComplement=='Executar') {
			executar_OnClick();
		} else {
			retornar_OnClick
		}
	}


	//#####################################################
	// Fim da validacao do pce
	//#####################################################

	/**
	 * Abre a tela de iniciarDescarga e em seu retorno, re-carrega a tela com o os dados setados
	 */
	function iniciarDescargaVeiculo() {
		// Caso o controle de carga esteja fechado.
		if (getElementValue("controleCarga.tpStatusControleCarga") == "FE") {
			lms_03013();
			return false;
		}

		var filialColetorDadoScan = getElementValue("filialColetorDadoScan") == 'true';
		var onlyRemetenteComEtiquetaEDI = getElementValue("onlyRemetenteComEtiquetaEDI") == 'true';

		if (!onlyRemetenteComEtiquetaEDI && filialColetorDadoScan && getElementValue("controleCarga.tpControleCarga") == 'V') {
			lms_45075();
			var tabGroup = getTabGroup(this.document);
			if (tabGroup!=null) {
				 tabGroup._tabsIndex[1].setDisabled(true);
				 tabGroup._tabsIndex[2].setDisabled(true);
			}
			resetView();
			return false;
		}

		if (getElementValue("idCarregamentoDescarga") == "") {
			var controleCarga = new Array();
			setNestedBeanPropertyValue(controleCarga, "idControleCarga", getElementValue("controleCarga.idControleCarga"));
			var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.validateDoctoServicoComOcorrenciaVinculada", "validateDoctoServicoComOcorrenciaVinculada", controleCarga);
	    	xmit({serviceDataObjects:[sdo]});
		} else {
			// caso o descarga ja tenha sido iniciado...
			lms_03005();
			return false;
		}
	}

	function reabrirDescargaVeiculo() {
		if (getElementValue("tpStatusOperacao") == "O") {
			var idCarregamentoDescarga = new Array();
			setNestedBeanPropertyValue(idCarregamentoDescarga, "idCarregamentoDescarga", getElementValue("idCarregamentoDescarga"));

			var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.reabrirDescarga", "reabrirDescargaVeiculo", idCarregamentoDescarga);
	    	xmit({serviceDataObjects:[sdo]});

	    	return false;
		}
	}

	function reabrirDescargaVeiculo_cb(data, error) {
		if(error) {
			lms_03020();
	    	document.getElementById("reabrirDescarga").disabled = true;
		}
	}


	function validateDoctoServicoComOcorrenciaVinculada_cb(data, error) {
		if(!error) {
			if(data.warnings != undefined){
				for (var i = 0; i < data.warnings.length; i++) {
					alert(data.warnings[i].warning);
				}
			}

			window.showModalDialog('recepcaoDescarga/iniciarDescarga.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
			reloadDataDescarga();
		} else {
			alert(error);
			return false;
		}
	}
	
	function verificaBlRncAutomaticaNovo(){
		if(blRncAutomaticaNovo){
			finalizarDescargaVeiculo();
		}else{
			verificaPossibilidadeCriacaoRNCautomatica();
		}
	}

	function verificaPossibilidadeCriacaoRNCautomatica(){
		var data = new Object();
		data.idControleCarga = getElementValue("controleCarga.idControleCarga");
		data.tpControleCarga = getElementValue("controleCarga.tpControleCarga");
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.verificaPossibilidadeCriacaoRNCautomatica", "verificaPossibilidadeCriacaoRNCautomatica", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function verificaPossibilidadeCriacaoRNCautomatica_cb(data, error){
		if (error != null) {
			alert(error);
			setFocusOnFirstFocusableField(document);
		} else {
			if(data.rncAutomaticaMessage && confirm(getI18nMessage("LMS-03022", data.rncAutomaticaMessage, false))){
				data.idControleCarga = getElementValue("controleCarga.idControleCarga");
				var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.abreRNCautomatica", "abreRNCautomatica", data);
				xmit({serviceDataObjects:[sdo]});
			}else if(!data.rncAutomaticaMessage){
				finalizarDescargaVeiculo();
			}
		}
	}

	function abreRNCautomatica_cb(data, error){
		if (error != null) {
			alert(error);
			setFocusOnFirstFocusableField(document);
		} else {
			finalizarDescargaVeiculo();
		}

	}


	/**
	 * Abre a tela de finalizarDescarga e em seu retorno, re-carrega a tela com o os dados setados
	 */
	function finalizarDescargaVeiculo() {
		var dadosUrl = '&idControleCarga='+getElementValue("controleCarga.idControleCarga");
		dadosUrl += '&idFilialControleCarga='+getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
		dadosUrl += '&tpControleCarga='+getElementValue("controleCarga.tpControleCarga");
		dadosUrl += '&idCarregamentoDescarga='+getElementValue("idCarregamentoDescarga");
		dadosUrl += '&blContinuaDescarga=true';
		window.showModalDialog('recepcaoDescarga/finalizarDescarga.do?cmd=relatorioDivergencias'+dadosUrl,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:540px;dialogHeight:200px;');
				reloadDataDescarga();
	}

	/**
	 *	Abre a pop-up de documentos a partir da grid
	 */
	function carregaDocumentos(id) {
		if (getElementValue("dhFimOperacao")!=""){
		 lms_03011();
		 return false;
		}

		var idManifesto = null;
		var nrManifesto = null;
		var sgFilial = null;

		for (i=0; i<manifestoGridDef.gridState.data.length; i++) {
			if (manifestoGridDef.gridState.data[i].idManifesto == id){
				idManifesto = manifestoGridDef.gridState.data[i].idManifesto;
				nrManifesto = manifestoGridDef.gridState.data[i].nrManifesto;
				sgFilialOrigem = manifestoGridDef.gridState.data[i].sgFilialOrigem;
				break;
			}
		}
		var dadosURL = "&idManifesto="+idManifesto+"&nrManifesto="+nrManifesto+"&sgFilial="+sgFilialOrigem;
		showModalDialog("/recepcaoDescarga/descarregarVeiculo.do?cmd=documentos" + dadosURL , window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");
	}

	/**
	 *	Abre a pop-up de Pedidos Coletas a partir da grid
	 */
	function carregaDocumentosColetas(id) {
		var idManifesto = null;
		var nrManifesto = null;
		var sgFilial = null;

		for (var i=0; i < manifestoGridDef.gridState.data.length; i++) {
			if (manifestoGridDef.gridState.data[i].idManifesto == id){
				idManifesto = manifestoGridDef.gridState.data[i].idManifesto;
				nrManifesto = manifestoGridDef.gridState.data[i].nrManifesto;
				sgFilialOrigem = manifestoGridDef.gridState.data[i].sgFilialOrigem;
				break;
			}
		}
		var dadosURL = "&idManifesto="+idManifesto+"&nrManifesto="+nrManifesto+"&sgFilial="+sgFilialOrigem;
		showModalDialog("/recepcaoDescarga/descarregarVeiculo.do?cmd=documentosColetas" + dadosURL, window, "unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;");
	}


	//##################################
    // Funcoes basicas da tela
	//##################################

	/**
	 * Carrega os dados basicos da tela
	 */
	function fillBasicData() {
		setElementValue("blInformaKmPortaria", basicData.filial.blInformaKmPortaria);
		setElementValue("idFilial", basicData.filial.idFilial);
		setElementValue("sgFilial", basicData.filial.sgFilial);
		setElementValue("pessoa.nmFantasia", basicData.filial.pessoa.nmFantasia);
		
		if (basicData && basicData.blRncAutomaticaNovo && basicData.blRncAutomaticaNovo == "true"){		
			blRncAutomaticaNovo = true;			
		}else{
			blRncAutomaticaNovo = false;			
		}
		
	}

	/**
	 * Reseta a tela
	 */
	function resetView() {
		resetValue(this.document);
		manifestoGridDef.resetGrid();
		setDisableTab(true);
		disableButtons(true);
		fillBasicData();
		setDisabled(document.getElementById("controleCarga.nrControleCarga"), true, undefined, true);
		setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
	}

	/**
	 * Controla a propriedade de disable dos botoes da tela
	 */
	function disableButtons(disable) {
		//atualiza o status dos botões de iniciar e finalizar descarga. LMS-3453
		disableButtonsIniciarEFinalizarDescargaByStatusControleCarga();
		document.getElementById("reabrirDescarga").disabled = disable;
		document.getElementById("abrirRNC").disabled = disable;
		document.getElementById("trocarEquipe").disabled = disable;
	}

	/**
	 * Desabilita as abas de gerenciamento e riscos
	 *
	 * @param disableTab aba gerenciamento
	 */
	function setDisableTab(disableTab) {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup!=null) {
			if (disableTab!=null) tabGroup._tabsIndex[1].setDisabled(disableTab);
		}
	}

	/**
	 * Re-carrega os dados da tela de carregamento
	 */
	function reloadDataDescarga() {
		var controleCarga = new Array();
		setNestedBeanPropertyValue(controleCarga, "filialByIdFilialOrigem.idFilial", getElementValue("controleCarga.filialByIdFilialOrigem.idFilial"));
		setNestedBeanPropertyValue(controleCarga, "nrControleCarga", getElementValue("controleCarga.nrControleCarga"));

		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoAction.findCarregamentoDescargaByNrControleCarga", "loadDataByNrControleCarga", controleCarga);
	    xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Desabilita o click na row
	 */
	function manifestoColetaOnRowClick() {
		return false;
	}

	function manifestoCallBack_cb(data, error) {
		if (getElementValue("dhInicioOperacao") == "" || getElementValue("dhFimOperacao") != "") {
			document.getElementById("trocarEquipe").disabled = true;
		} else {
			document.getElementById("trocarEquipe").disabled = false;
		}

		if(getElementValue("tpStatusOperacao") == "" || "O" != getElementValue("tpStatusOperacao")){
			document.getElementById("reabrirDescarga").disabled = true;
	}
		//atualiza o status dos botões de iniciar e finalizar descarga. LMS-3453. chamdo neste callback pois no grid.js após a chamada setDisabledForm
		// estava anulando a regra dessa function habilitando os botões que não deveriam estar habilitados
		disableButtonsIniciarEFinalizarDescargaByStatusControleCarga();
	}

	/**
	 * -> Função que povoa a grid de manifestos corretamente para chamar a popup de coletas
	 * ou de documentos de serviço, dependendo do manifesto em questão.
	 * -> Função que verifica cada linha da grid (no momento em que ela esta sendo carregada)
	 * possui DPE menor ou igual que a data atual.
	 */
	function onPopulateRow(tr, data) {
		if(data.emDestaque == "true") {
			tr.style.backgroundColor = "#8FBFD6";
		}

		var tdblob = tr.children[6].innerHTML;
		var blob = tdblob.substring(tdblob.indexOf("</IMG>") + 6, tdblob.indexOf("</A>")).replace(" ","");
		fakeDiv = document.createElement("<DIV></DIV>");

		if(data.tpManifesto.value == "C") {
			fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:carregaDocumentosColetas('" + data.idManifesto + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/popup.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
		} else {
			fakeDiv.innerHTML = "<TABLE><TR><TD><NOBR><A onclick=\"javascript:carregaDocumentos('" + data.idManifesto + "'); event.cancelBubble=true;\"><IMG title=\"\" style=\"CURSOR: hand\" src=\"" + contextRoot + "/images/popup.gif\" border=0></IMG></A></NOBR></TD></TR></TABLE>";
		}
		tr.children[6].innerHTML = fakeDiv.children[0].children[0].children[0].children[0].innerHTML;
	}

	 /**
	 * Função responsável pela rotina de habilita_desabilita_botão - LMS-3453
	 * -> Desabilitar/Habilitar os botões Iniciar e Finalizar descarga, conforme Status do Controle de Carga
	 *
	 */
	 function disableButtonsIniciarEFinalizarDescargaByStatusControleCarga() {


		 var tpStatusControleCarga = getElementValue("controleCarga.tpStatusControleCarga");
		 if(tpStatusControleCarga == 'AD' || (tpStatusControleCarga == 'PO' && existTpStatusManifesto('AD'))) {
			 document.getElementById("iniciarDescarga").disabled = false;
			 document.getElementById("finalizarDescarga").disabled = true;
		 } else if(tpStatusControleCarga == 'ED' || (tpStatusControleCarga == 'EP' && existTpStatusManifesto('ED'))) {
			 document.getElementById("finalizarDescarga").disabled = false;
			 document.getElementById("iniciarDescarga").disabled = true;
		 } else {
			 document.getElementById("finalizarDescarga").disabled = true;
			 document.getElementById("iniciarDescarga").disabled = true;
		 }
	 }

	 /**
	 * Função que procura o tpStatusManifesto passado como parametro na grid da tela
	 * retorna true caso encontre o valor
	 *
	 */
	 function existTpStatusManifesto(tpStatusManifesto) {
		 if (manifestoGridDef.gridState.data.length>0) {
			 for (var i=0; i < manifestoGridDef.gridState.data.length; i++) {
				 var varTpStatusManifesto;
				 varTpStatusManifesto = manifestoGridDef.gridState.data[i].tpStatusManifesto.value;
				 if(varTpStatusManifesto == tpStatusManifesto){
					 return true;
				 }
			}
		 }
		 return false;
	 }
--></script>
