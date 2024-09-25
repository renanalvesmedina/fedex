<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">

	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
		//Seta a origem da chamada da tela.
		var url = new URL(parent.location.href);	
	   	setElementValue("origem", url.parameters["origem"]);	
	
		if (getElementValue("origem") != "") {		
			setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", url.parameters["controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"]);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", url.parameters["controleCarga.filialByIdFilialOrigem.idFilial"]);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", url.parameters["controleCarga.filialByIdFilialOrigem.sgFilial"]);
			disableNrControleCarga(false);
			setElementValue("controleCarga.nrControleCarga", url.parameters["controleCarga.nrControleCarga"]);
			lookupChange({e:document.getElementById("controleCarga.idControleCarga"), forceChange:true});
		} else {
			document.getElementById("controleCarga.nrControleCarga").disabled = true;
		}
		
		document.getElementById("reset").disabled = false;
		
    	var data = new Array();
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.getBasicData", "loadBasicData", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega um array 'dataUsuario' com os dados do usuario em sessao
	 */
	var basicData;
	function loadBasicData_cb(data, error) {
		basicData = data;
		fillBasicData();
		onPageLoad();
	}
</script>

<adsm:window service="lms.carregamento.carregarVeiculoAction" onPageLoad="loadDataObjects">
	<adsm:form action="/carregamento/carregarVeiculo" height="123" idProperty="idCarregamentoDescarga" id="carregarVeiculoForm">
		
		<adsm:hidden property="origem"/>
		
		<adsm:hidden property="idFilial"/>
		<adsm:textbox dataType="text" property="sgFilial" label="filial" size="3" maxLength="3" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoa" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="idPostoAvancado"/>
		<adsm:textbox dataType="text" property="sgFilialPostoAvancado" 
					  label="postoAvancado" maxLength="3" size="3" labelWidth="18%" width="82%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoaPostoAvancado" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />				
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.carregamento.carregarVeiculoAction.findLookupBySgFilial" action="/municipios/manterFiliais" 
					 onchange="return sgFilialOnChangeHandler();" onDataLoadCallBack="disableNrControleCarga"
					 popupLabel="pesquisarFilial"
					 label="controleCargas" labelWidth="18%" width="32%" size="3" maxLength="3" picker="false" serializable="false">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.carregamento.carregarVeiculoAction.findCarregamentoDescargaByNrControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="loadDataByNrControleCarga" onDataLoadCallBack="loadDataByNrControleCarga" onchange="return checkValueControleCarga(this.value)"
						 popupLabel="pesquisarControleCarga"
						 maxLength="8" size="8" mask="00000000">
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

		<adsm:textbox property="tpStatusControleCarga" dataType="text"
					   label="status" labelWidth="18%" width="32%" size="36" disabled="true"/>
		
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
		
		<adsm:textbox property="dhInicioOperacao" dataType="JTDateTimeZone" label="inicioCarregamento" labelWidth="18%" width="32%" disabled="true" picker="false"/>
		
		<adsm:textbox property="dhFimOperacao" dataType="JTDateTimeZone" label="fimCarregamento" labelWidth="18%" width="32%" disabled="true" picker="false"/>
		
		<adsm:hidden property="inicioCarregamento" value="false"/>
		<adsm:hidden property="tpControleCargaValue"/> 
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button id="reset" caption="limpar" onclick="btnLimpar_click()"/>
		</adsm:buttonBar>
		<script>
			function lms_05015() {
				alert("<adsm:label key='LMS-05015'/>");
			}
			function lms_05018() {
				alert("<adsm:label key='LMS-05018'/>");
			}
			
			var lms_05019 = '<adsm:label key="LMS-05019"/>';
			
			function lms_05020() {
				alert("<adsm:label key='LMS-05020'/>");
			}
			function lms_05025() {
				alert("<adsm:label key='LMS-05025'/>");
			}
			function lms_05059() {
				alert("<adsm:label key='LMS-05059'/>");
			}
			function lms_05145() {
				return confirm("<adsm:label key='LMS-05145'/>");
			}
			function lms_05156() {
				alert("<adsm:label key='LMS-05156'/>");
			}
			function lms_05163() {
				alert("<adsm:label key='LMS-05163'/>");
			}
		</script>
	</adsm:form>
	<adsm:grid property="manifestosColeta" idProperty="idManifesto" onRowClick="manifestoColetaOnRowClick"
			   selectionMode="radio" scrollBars="horizontal" gridHeight="160" onPopulateRow="checkStatusManifesto" rows="8">
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="sgFilial" title="preManifesto" width="30"/>
			<adsm:gridColumn property="nrPreManifesto" title="" dataType="integer" width="70" mask="00000000"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="dhGeracaoPreManifesto" dataType="JTDateTimeZone" title="dataHoraGeracao" width="140" align="center"/>
		<adsm:gridColumn property="tpManifesto" isDomain="true" title="operacao" width="140"/>
		<adsm:gridColumn property="tipo" isDomain="true" title="tipo" width="140"/>		
		<adsm:gridColumn property="tpStatusManifesto" isDomain="true" title="status" width="180"/>
		<adsm:gridColumn property="iniciar" title="iniciar" align="center" image="/images/popup.gif" link="javascript:iniciarCarregamentoGrid" linkIdProperty="idManifestoColeta" popupDimension="790,496" width="70"/>
		<adsm:gridColumn property="finalizar" title="finalizar" align="center" image="/images/popup.gif" link="javascript:finalizarPreManifesto" linkIdProperty="idManifestoColeta" popupDimension="790,496" width="70"/>
		<adsm:gridColumn property="dhInicioCarregamento" dataType="JTDateTimeZone" title="inicioCarregamento" width="140"/>
		<adsm:gridColumn property="dhFimCarregamento" dataType="JTDateTimeZone" title="fimCarregamento" width="140"/>
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="sgMoeda" dataType="text" title="valorTotal" width="30"/>
			<adsm:gridColumn property="dsSimbolo" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="vlTotalManifesto" dataType="currency" title="" width="70"/>
		<adsm:gridColumn property="documentos" title="documentos" align="center" image="/images/popup.gif" link="javascript:carregaDocumentos" linkIdProperty="idManifestoColeta" popupDimension="790,496" width="80"/>
		<adsm:buttonBar>
			<adsm:button id="abrirRNC" caption="abrirRNC" action="/rnc/abrirRNC" cmd="main" />
			<adsm:button id="excluirPreManifesto" caption="excluirPreManifesto" onclick="excluirPreManifestoClick();" />
			<adsm:button id="trocarEquipe" caption="trocarEquipe" onclick="trocarEquipeCarregamento()"/>
			<adsm:button id="iniciarCarregamento" caption="iniciarCarregamento" onclick="iniciarCarregamentoDescarga()"/>
			<adsm:button id="finalizarCarregamento" caption="finalizarCarregamento" onclick="finalizarProcessoCarregamento()"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			setFocus(document.getElementById("controleCarga.idControleCarga"));
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
		if (data.length==0){
			disableNrControleCarga(false);
		}
		return lookupExactMatch({e:document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"), data:data});
	}
	
	/**
	 * Carrega os dados da tela de carregarVeiculos apartir dos dados retornados da 
	 * consulta de 'findCarregamentoDescargaByNrControleCarga'
	 */
	function loadDataByNrControleCarga_cb(data, error){
		var result = controleCarga_nrControleCarga_exactMatch_cb(data);
	
		//Verifica se este objeto e nulo
		if (data[0]!=undefined) {
			setElementValue("idCarregamentoDescarga", data[0].idCarregamentoDescarga);
			setElementValue("controleCarga.idControleCarga", data[0].idControleCarga);
			setElementValue("controleCarga.nrControleCarga", data[0].nrControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data[0].idFilialControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data[0].sgFilialControleCarga);
			setElementValue("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia", data[0].nmFantasia);
			setElementValue("sgFilialPostoAvancado", data[0].sgFilialPostoAvancado);
			setElementValue("nmPessoaPostoAvancado", data[0].nmPessoaPostoAvancado);
			document.getElementById("tpControleCarga").value = data[0].tpControleCarga.description;
			document.getElementById("tpControleCargaValue").value = data[0].tpControleCarga.value;
			document.getElementById("tpStatusControleCarga").value = data[0].tpStatusControleCarga.description;
			setElementValue("dhEvento", setFormat(document.getElementById("dhEvento"), data[0].dhEvento));
			setElementValue("nrFrotaTransporte", data[0].nrFrotaTransporte);
			setElementValue("nrIdentificadorTransporte", data[0].nrIdentificadorTransporte);
			setElementValue("nrFrotaSemiReboque", data[0].nrFrotaSemiReboque);
			setElementValue("nrIdentificadorSemiReboque", data[0].nrIdentificadorSemiReboque);
			
			if (data[0].dhInicioOperacao!=undefined) {
				setElementValue("dhInicioOperacao", setFormat(document.getElementById("dhInicioOperacao"), data[0].dhInicioOperacao));
			} else {
				resetValue("dhInicioOperacao");
			}
			
			if (data[0].dhFimOperacao!=undefined){
				setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), data[0].dhFimOperacao));
			} else {
				resetValue("dhFimOperacao");
			}
			
			//Formata o campo de nrControleCarga
			format(document.getElementById("controleCarga.nrControleCarga"));
			
			populateGrid();
			setDisableTab(false);
			disableButtons(false);
			
		} else {
			var idFilial = document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").value;
			var sgFilial = document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value;
			
			resetView();
			
			document.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").value = idFilial;
			document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value = sgFilial;
			
			setFocus(document.getElementById("controleCarga.nrControleCarga"));
		}
		return result;
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
	function loadDataByNrControleCarga(rowValues) {
		var data = new Array();
		
		data.nrControleCarga = rowValues.nrControleCarga;
		setNestedBeanPropertyValue(data, "filialByIdFilialOrigem.idFilial", rowValues.filialByIdFilialOrigem.idFilial);
		
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.findCarregamentoDescargaByNrControleCarga", "loadDataByNrControleCarga", data);
    	xmit({serviceDataObjects:[sdo]});
    	
	}
	
	/**
	 * Verifica o atual valor do campo de nrControleCarga
	 */
	function checkValueControleCarga(valor) {
		var result = controleCarga_nrControleCargaOnChangeHandler();
		if (valor=="") {
			var idFilial = getElementValue("controleCarga.filialByIdFilialOrigem.idFilial");
			var sgFilial = getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial");
			resetView();
			setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilial);
			setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilial);
		}
		
		return result;
	}
	
	/**
	 * Popula a grid
	 */
	function populateGrid() {
		var formData = new Object();
		
		setNestedBeanPropertyValue(formData, "idControleCarga", getElementValue("controleCarga.idControleCarga"));
		setNestedBeanPropertyValue(formData, "idFilial", getElementValue("idFilial"));
		setNestedBeanPropertyValue(formData, "idPostoAvancado", getElementValue("idPostoAvancado"));
		
		manifestosColetaGridDef.executeSearch(formData, true);
	}

	/**
	 * Inicia se o carregamento e caso nao exista um carregamentoDescarga abre a tela como o botao de
	 * iniciar
	 *
	 * @param idManifestoValue o id do manifesto clicado
	 */
	function iniciarCarregamentoGrid(idManifestoValue) {
		
		if (getElementValue("dhFimOperacao")!="") lms_05025();
		
		if (getElementValue("idCarregamentoDescarga")!="") {
			if (checkCarregamento(idManifestoValue)) {
				lms_05020();
			} else if ( confirm(lms_05019, focusSim) ) {
				var formData = new Object();
				
				setNestedBeanPropertyValue(formData, "idManifesto", idManifestoValue);
				setNestedBeanPropertyValue(formData, "idCarregamentoDescarga", getElementValue("idCarregamentoDescarga"));
			
		    	var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.generateCarregamento", "iniciarCarregamentoGrid", formData);
		    	xmit({serviceDataObjects:[sdo]});
	    	}
		} else {
			iniciarCarregamentoDescarga();
		}
	}
	
	/**
	 * Verifica se o status do manifesto e igual a 'EC'
	 */
	function checkCarregamento(id){
		for (i=0; i<manifestosColetaGridDef.gridState.data.length; i++) {
			if (manifestosColetaGridDef.gridState.data[i].idManifesto==id){
				if ((manifestosColetaGridDef.gridState.data[i].tpStatusManifesto.value=="EC") || 
					(manifestosColetaGridDef.gridState.data[i].tpStatusManifesto.value=="CC")){
					return true;
				}
			}
		}
		return false;
	}
	
	function trocarEquipeCarregamento() {
		if (getElementValue("dhFimOperacao")!="") {
			lms_05059();
		} else {
			showModalDialog('carregamento/carregarVeiculo.do?cmd=trocar',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
		}
	}
	
	/**
	 * Callback da inicio de carregamento de pre-manifesto.
	 */
	function iniciarCarregamentoGrid_cb(data, error){
		if (data._exception==undefined) {
			populateGrid();
		} else {
			alert(data._exception._message);
		}
	}
	
	/**
	 * Remove o manifesto selecionado pelo radio button.
	 */
	function excluirPreManifestoClick() {
		if (manifestosColetaGridDef.getSelectedIds().ids.length>0) {
			if (getElementValue("dhFimOperacao")!="") {
				lms_05059();
			} else if (lms_05145()) {
				for (i=0; i<manifestosColetaGridDef.getSelectedIds().ids.length; i++) {
				
					var data = new Object();
					data.idManifesto = manifestosColetaGridDef.getSelectedIds().ids[i];
					data.idCarregamentoDescarga = getElementValue("idCarregamentoDescarga");
					data.idControleCarga = getElementValue("controleCarga.idControleCarga");
					
					var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.removeManifestoCarregamento", "excluirPreManifestoClick", data);
				    xmit({serviceDataObjects:[sdo]});
				}
			}
		}
	}
	
	function excluirPreManifestoClick_cb(data, error) {
		if (data._exception!=undefined) {
			alert(data._exception._message);
		} else {
			populateGrid();
		}
	}
	
	/**
	 * Chama a tela de finalizar pre manifesto
	 */
	function finalizarPreManifesto(id){
		
		if (getElementValue("dhFimOperacao")!="") lms_05025();
		
		var idManifesto = null;
		var nrPreManifesto = null;
		var sgFilial = null;
		var dadosURL = null;
		
		for (i=0; i<manifestosColetaGridDef.gridState.data.length; i++) {
			if (manifestosColetaGridDef.gridState.data[i].idManifesto==id){
				if (manifestosColetaGridDef.gridState.data[i].tpStatusManifesto.value=="EC") {
					idManifesto = manifestosColetaGridDef.gridState.data[i].idManifesto;
					nrPreManifesto = manifestosColetaGridDef.gridState.data[i].nrPreManifesto;
					sgFilial = manifestosColetaGridDef.gridState.data[i].sgFilial;
					
					dadosURL = "&idManifesto="+idManifesto+"&nrPreManifesto="+nrPreManifesto+"&sgFilial="+sgFilial;
					showModalDialog('carregamento/finalizarCarregamentoPreManifestos.do?cmd=main'+dadosURL, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
					
					populateGrid();
				} else {
					lms_05018();
				}
				break;
			}
		}
	}
	
	/**
	 * Abre a tela de iniciarCarregamento e em seu retorno, re-carrega a tela com o os dados setados
	 */
	function iniciarCarregamentoDescarga() {
		if (manifestosColetaGridDef.gridState.data.length<1) {
			lms_05156();
			return false;
		}
		
		if (getElementValue("idCarregamentoDescarga")=="") {
			showModalDialog('carregamento/iniciarCarregamento.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
			reloadDataCarregamento();
		} else {
			// caso o carregamento ja tenha sido iniciado...
			lms_05015();
		}
	}
	
	function carregaDocumentos(id) {
		
		if (getElementValue("dhFimOperacao")!="") lms_05025();
		
		var idManifestoValue = null;
		var nrPreManifesto = null;
		var sgFilial = null;
		var tipo = null; //Para verificar se é "RETORNO VAZIO"
		
		for (i=0; i<manifestosColetaGridDef.gridState.data.length; i++) {
			if (manifestosColetaGridDef.gridState.data[i].idManifesto==id){
				idManifestoValue = manifestosColetaGridDef.gridState.data[i].idManifesto;
				nrPreManifesto = manifestosColetaGridDef.gridState.data[i].nrPreManifesto;
				sgFilial = manifestosColetaGridDef.gridState.data[i].sgFilial;
				tipo = manifestosColetaGridDef.gridState.data[i].tipo.value;
				break;
			}
		}
		
		// Se "RETORNO VAZIO", não deixa abrir a popup de documentos.
		if (tipo == "RV"){
			lms_05163();
			return false;
		}
			
		var dadosURL = "&idManifesto="+idManifestoValue+"&nrPreManifesto="+nrPreManifesto+"&sgFilial="+sgFilial;
		
		showModalDialog('carregamento/carregarVeiculo.do?cmd=documentos'+dadosURL ,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
		
		var dataObject = new Object();
		dataObject.idManifesto = idManifestoValue;
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.updateValorTotalManifesto", "carregaDocumentos", dataObject);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function carregaDocumentos_cb(data, error) {
		if (error){
			alert (error);
		}
		populateGrid();
	}
	
	function finalizarProcessoCarregamento(){
		
		if (getElementValue("dhFimOperacao")!="") {
			lms_05059();
		} else {			
			var formData = new Object();
		
			setNestedBeanPropertyValue(formData, "idControleCarga", getElementValue("controleCarga.idControleCarga"));
			setNestedBeanPropertyValue(formData, "idFilial", getElementValue("idFilial"));
			setNestedBeanPropertyValue(formData, "idPostoAvancado", getElementValue("idPostoAvancado"));
			
			var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.validatePreManifestos", "finalizarProcessoCarregamento", formData);
			xmit({serviceDataObjects:[sdo]});
	   	}
	}

	function finalizarProcessoCarregamento_cb(data, error) {
		if (data._exception==undefined) {
			var success = showModalDialog('carregamento/finalizarCarregamentoControleCargas.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
			
			if ((success!=undefined) && (success!=false)){
				var dataObject = new Object();
				setNestedBeanPropertyValue(dataObject, "idCarregamentoDescarga", getElementValue("idCarregamentoDescarga"));
				
				var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.loadFinalStates", "finalizarCarregamento", dataObject);
			    xmit({serviceDataObjects:[sdo]});	
		   	}
		} else {
			alert(data._exception._message);
		}
	}

	function finalizarCarregamento_cb(data, error){
		setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), data.dhFimOperacao));
		setElementValue("tpStatusControleCarga", data.tpStatusControleCarga);
		
		disableButtons(true);
	}

	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * Carrega os dados basicos da tela
	 */
	function fillBasicData() {
		setElementValue("idFilial", basicData.filial.idFilial);
		setElementValue("sgFilial", basicData.filial.sgFilial);
		setElementValue("nmPessoa", basicData.filial.pessoa.nmFantasia);
	}
	
	function btnLimpar_click(){
		resetView();
		disableNrControleCarga(true);
		setFocus(document.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial"));
	}
	
	/**
	 * Reseta a tela deixa
	 */
	function resetView(){
		resetValue(this.document);
		manifestosColetaGridDef.resetGrid();
		setDisableTab(true);
		disableButtons(true);
		fillBasicData();
	}
	
	/**
	 * Controla a propriedade de disable dos botoes da tela
	 */
	function disableButtons(disable) {
		
		document.getElementById("reset").disabled = false; //decorrente de seu comportamento sera sempre enabled
		
		document.getElementById("iniciarCarregamento").disabled=disable;
		document.getElementById("abrirRNC").disabled=disable;  
		document.getElementById("excluirPreManifesto").disabled=disable;  
		
		if (((getElementValue("dhInicioOperacao")!="") && (getElementValue("dhFimOperacao")=="")) || (disable==true)) {
			document.getElementById("trocarEquipe").disabled=disable;
			document.getElementById("finalizarCarregamento").disabled=disable;
		}
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
	function reloadDataCarregamento() {
		
		var controleCarga = new Array();
		setNestedBeanPropertyValue(controleCarga, "filialByIdFilialOrigem.idFilial", getElementValue("controleCarga.filialByIdFilialOrigem.idFilial"));
		setNestedBeanPropertyValue(controleCarga, "nrControleCarga", getElementValue("controleCarga.nrControleCarga"));
		
		var sdo = createServiceDataObject("lms.carregamento.carregarVeiculoAction.findCarregamentoDescargaByNrControleCarga", "loadDataByNrControleCarga", controleCarga);
	    xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Desabilita o click na row
	 */
	function manifestoColetaOnRowClick() {
		return false;
	}
	
	/**
	 * Verifica cada linha da grid (no momento em que ela esta sendo carregada) 
	 * e valida se o manifesto em questao foi iniciado caso tenha sido a linha da grid 
	 * tem sua cor alterada
	 */
	function checkStatusManifesto(tr, data) {
		if (data.tpStatusManifesto.value == "EC") tr.style.backgroundColor = "#8FBFD6";
	}	
	
		
</script>