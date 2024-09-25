<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	
	function pageLoad_cb(){
		onPageLoad_cb();
		if (document.getElementById("idFilialTemp").masterLink == 'true'){
			setElementValue("filial.idFilial", getElementValue("idFilialTemp"));
			document.getElementById("filial.pessoa.nmFantasia").masterLink = 'false';			
			setFocusOnFirstFocusableField();
			
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
		} else {
			findFilialSessao();
		}
	}

</script>
<adsm:window title="selecionarMeiosTransporteChegada" service="lms.portaria.selecionarMeiosTransporteChegadaAction" onPageLoadCallBack="pageLoad">
  <adsm:form action="/portaria/selecionarMeiosTransporteChegada">
   		
  		<adsm:hidden property="idControleTemp"/> 		
  		<adsm:hidden property="idFilialTemp"/> 		
  		<adsm:hidden property="sgFilialTemp"/> 
  		<%--adsm:hidden property="tpEmpresa" serializable="false" value="M"></adsm:hidden--%>
  		 
  	 	<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				 service="lms.portaria.selecionarMeiosTransporteChegadaAction.findLookupFilial" dataType="text" label="filial" size="3" 
				 action="/municipios/manterFiliais" onchange="return filialChange(this)" required="true"
				 labelWidth="15%" width="35%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px" disabled="true">
				 
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<%--adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup> 
  		
  		<adsm:combobox label="portaria" property="portaria.idPortaria" labelWidth="15%" width="35%"  service="lms.portaria.selecionarMeiosTransporteChegadaAction.findPortaria" 
  					   optionLabelProperty="dsPortaria" optionProperty="idPortaria" onDataLoadCallBack="portariaDataLoad" boxWidth="200">
  			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="idFilial"/> 
	   </adsm:combobox>
  					   
		<adsm:grid idProperty="idControle" property="viagem" onRowClick="gridViagemRowClick" autoSearch="false" onDataLoadCallBack="viagemDataLoad"
				   service="lms.portaria.selecionarMeiosTransporteChegadaAction.findGridViagemChegada" showPagging="false" title="viagem" 
				   gridHeight="130" rows="5" scrollBars="vertical" selectionMode="none">
			<adsm:gridColumn title="meioTransporte" property="nrFrota" width="65"/>
			<adsm:gridColumn title="" property="nrIdentificador" width="75" align="left"/>
			<adsm:gridColumn title="semiReboque" property="nrFrotaReboque" width="65"/>
			<adsm:gridColumn title="" property="nrIdentificadorReboque" width="75" align="left"/>
			<adsm:gridColumnGroup customSeparator=" ">
				<adsm:gridColumn title="controleCarga" property="sgFilial" width="50" />		
				<adsm:gridColumn title="" property="nrControleCarga" width="50" mask="00000000" dataType="integer"/>
			</adsm:gridColumnGroup> 
			
			<adsm:gridColumn title="rota" property="nrRota" dataType="integer" mask="0000" width="30" />
			<adsm:gridColumn title="" property="dsRota" width="160"/>
			
			<adsm:gridColumn title="dataHoraChegadaPrevista" dataType="JTDateTimeZone" property="dhPrevisaoChegada" width="" align="center"/>
		</adsm:grid>
		
		<adsm:grid idProperty="idControle" property="coletaEntrega" onRowClick="gridColetaEntregaRowClick" autoSearch="false"
					service="lms.portaria.selecionarMeiosTransporteChegadaAction.findGridColetaEntrega" 
					title="coletaEntregaOrdemSaida" showPagging="false" gridHeight="130" rows="5" scrollBars="vertical"
					selectionMode="none"> 
			<adsm:gridColumn title="meioTransporte" property="nrFrota" width="65" />
			<adsm:gridColumn title="" property="nrIdentificador" width="75" align="left"/>
			<adsm:gridColumn title="semiReboque" property="nrFrotaReboque" width="65" />
			<adsm:gridColumn title="" property="nrIdentificadorReboque" width="75" align="left"/>
			<adsm:gridColumnGroup customSeparator=" ">
				<adsm:gridColumn title="controleCarga" property="sgFilial" width="50" />		
				<adsm:gridColumn title="" property="nrControleCarga" width="55" mask="00000000" dataType="integer"/>
			</adsm:gridColumnGroup>
			
			<adsm:gridColumnGroup customSeparator=" - ">		
				<adsm:gridColumn title="rota" property="nrRota" width="60"/>
				<adsm:gridColumn title="" property="dsRota" width="175"/>
			</adsm:gridColumnGroup>
		
			<adsm:gridColumn title="tipo" property="tipo" width=""/>
			
		</adsm:grid>
	
	<script>
		function LMS_06018(){
			alert('<adsm:label key="LMS-06018"/>');
		}
	</script>
	
		<adsm:buttonBar>
			<adsm:button id="buscarMeioTransporte" caption="buscarMeioTransporte" onclick="openBuscarMeioTransporte()" cmd="main"/>
			<adsm:button id="informarSaidas" caption="informarSaidasButton" action="/portaria/informarSaida" cmd="main">
				<adsm:linkProperty src="filial.idFilial" target="idFilialTemp" disabled="false" />
				<adsm:linkProperty src="filial.sgFilial" target="filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia" />
			</adsm:button>
			<adsm:button id="atualizar" caption="atualizar" onclick="atualizarClick()"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	function procuraMeioTransporte(nrIdentificador, nrFrota){
		
	    var data = viagemGridDef.gridState.data;				
		var achou = false;
		
		nrIdentificador = nrIdentificador != undefined ? nrIdentificador.toUpperCase() : "";
		nrFrota = nrFrota != undefined ? nrFrota.toUpperCase() : "";

		for(var idx = 0; idx < data.length; idx++) {
			
			if ((data[idx].nrIdentificador == nrIdentificador) || 
				((data[idx].nrFrota == nrFrota))) {
				achou = true;
				gridViagemRowClick(data[idx].idControle);
				break;
			}
		}
		
		if (!achou) {
			var data = coletaEntregaGridDef.gridState.data;
			for(var idx = 0; idx < data.length; idx++) {
				if ((data[idx].nrIdentificador == nrIdentificador) || 
					((data[idx].nrFrota == nrFrota))) {
					achou = true;
					gridColetaEntregaRowClick(data[idx].idControle);
					break;
				}
			}
		}
		
		if (!achou){
			LMS_06018();
		}		

	}
	
	var idFilial;
	function filialDataLoad_cb(data){
		filial_idFilial_cb(data);

		if (data.length == 1) {			
			setDisabled("filial.idFilial", true);
			document.getElementById("filial.idFilial").selectedIndex = 1;			
		} else {
		}			
		
	}

	function findFilialSessao(){

		var data = new Array();
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.findFilialSessao",
					"findFilialSessao",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function findFilialSessao_cb(data, exception){
		if (exception == null){
			setElementValue("filial.idFilial", data.idFilial);
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
			idFilial = data.idFilial;
			notifyElementListeners({e:document.getElementById("filial.idFilial")});						
		}
	}
	
	function portariaDataLoad_cb(data){
		portaria_idPortaria_cb(data);
		
		//Se possui somente uma portaria, seleciona e desabilita
		if (data.length == 1) {
			document.getElementById("portaria.idPortaria").selectedIndex = 1;
			setDisabled("portaria.idPortaria", true);
		} else {		
			setDisabled("portaria.idPortaria", false);
			var tab = getTabAvulso();
			var portaria = tab.tabOwnerFrame.document.getElementById("portaria.idPortaria");
			var filial = tab.tabOwnerFrame.document.getElementById("filial.idFilial");
			
			//Senao verifica se a filial da tab avulso eh igual e seta a portaria de acordo
			if (filial != null && filial.value == getElementValue("filial.idFilial")){
				setElementValue("portaria.idPortaria", portaria.value);
			} else {				
				//Senao seta a portaria padrao da filial
				for (var i=0; i < data.length; i++){
					var blPadraoFilial = getNestedBeanPropertyValue(data, ':' + i + '.blPadraoFilial');
					if (blPadraoFilial == 'true'){
						document.getElementById("portaria.idPortaria").selectedIndex = (i+1);
						break;
					}
				}
			}
		}
		//Recarrega as grids
		reloadGrids();
				
	}

	function filialChange(obj){
		var retorno = filial_sgFilialOnChangeHandler();
		verificaChangeFilial(obj);		
		return retorno;
	}

	function verificaChangeFilial(obj){
		if (obj.value == '') {		
			resetGrids();
		}
	}

	function resetGrids(){
		coletaEntregaGridDef.resetGrid();
		viagemGridDef.resetGrid();
	}

	function portariaChange(obj){
		comboboxChange({e:obj});		
	
	}

	function getTabAvulso(){
		var tabGroup = getTabGroup(this.document);
		return tabGroup.getTab("cad");
	}
	

  	function initWindow(evt){  		
 		setDisabled("atualizar", false);
 		setDisabled("informarSaidas", false); 
 		setDisabled("buscarMeioTransporte", false);
 		if (evt.name == 'tab_click'){		
			setaCamposFilialPortaria();
		}
 	}


	function setaCamposFilialPortaria(){
		var tab = getTabAvulso();
		var idFilial = tab.tabOwnerFrame.document.getElementById("filial.idFilial");
		var sgFilial = tab.tabOwnerFrame.document.getElementById("filial.sgFilial");
		var nmFantasia = tab.tabOwnerFrame.document.getElementById("filial.pessoa.nmFantasia");
						
		if (getElementValue("filial.idFilial") != idFilial.value) {			
			setElementValue("filial.idFilial", idFilial.value);			
			setElementValue("filial.sgFilial", sgFilial.value);
			setElementValue("filial.pessoa.nmFantasia", nmFantasia.value);
			verificaChangeFilial(document.getElementById("filial.idFilial"));		
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
		} else {			
			var portaria = tab.tabOwnerFrame.document.getElementById("portaria.idPortaria");
			setElementValue("portaria.idPortaria", portaria.value);
		}

	}
	
	function atualizarClick(){
		var tab = getTab(document);
		
		if (tab.validate({name:"findButton_click"})) {
			loadGrids();
			setFocusOnFirstFocusableField();
		}
    }
    
  	function reloadGrids(){  
  		if ((janela == undefined || janela_closed) && getTabGroup(document).selectedTab.properties.id == "pesq")
	  		loadGrids(); 
  	}
   
	function loadGrids(){
		loadGridViagem();		 
	}
	
	function viagemDataLoad_cb(data){
		loadGridColetaEntrega();
	}
	 
 	function loadGridViagem(){
	 	if (getElementValue("filial.idFilial") != '') {
 			var data= new Array();
 			setNestedBeanPropertyValue(data, "idFilial", getElementValue("filial.idFilial"));
			viagemGridDef.executeSearch(data);
		}
 	}
 
	function loadGridColetaEntrega(){
		if (getElementValue("filial.idFilial") != '') {
			var data= new Array();
			setNestedBeanPropertyValue(data, "idFilial", getElementValue("filial.idFilial"));
			coletaEntregaGridDef.executeSearch(data);
		}
	}
	
	
	function gridViagemRowClick(id){
		setElementValue("idControleTemp", id);		
		
		globalId = id;
		
		validaOperacaoFedex(id);
		
		return false;
	}
	
	function gridColetaEntregaRowClick(id){
		setElementValue("idControleTemp", id);
		validarBloqueioColetaEntrega(id);
		return false;
	}
	
	function validarBloqueioColetaEntrega(id){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", id);
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.validarBloqueio", "validarBloqueioColetaEntrega", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validarBloqueioColetaEntrega_cb(data, error, errorId) {
		if (error != undefined) { 
			alert(error);
		} else {
			openConfirmacao();
		}
	}
	
	function validaOperacaoFedex(id) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", id);
		
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.validaOperacaoFedex",
											"validaOperacaoFedex",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validaOperacaoFedex_cb(data, error, errorId) {
		if (error != undefined) { 
			alert(error);
		} else {
			validaTransito(globalId);
		}
	}
	
	function validaTransito(id) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", id);
		
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.validaTransito",
											"validaTransito",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validaTransito_cb(data, error, errorId) {
		if (error != undefined) { 
			alert(error);
		} else {
			validarBloqueio(globalId);
		}
	}

	function validarBloqueio(id){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", id);
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.validarBloqueio", "validarBloqueio", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validarBloqueio_cb(data, error, errorId) {
		if (error != undefined) { 
			alert(error);
		} else {
			validaTempoEspera(globalId);
		}
	}
	
	
	function validaTempoEspera(id){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", id);
		
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.validateTempoEspera",
											"validaTempoEspera",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validaTempoEspera_cb(data, error, errorId){
		if(error != undefined){
			if (errorId == 'LMS-06016'){
			if (confirm(error)){
				openConfirmacao();			
			}
			}else{
				alert(error);
			}
		} else{ 
			openConfirmacao();
	}
	}
	
	var globalId;
	var janela;
	var janela_closed = true;
	function openConfirmacao(){
		janela_closed = false;
		janela = showModalDialog('portaria/informarChegada.do?cmd=entrada',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:640px;dialogHeight:400px;');
		janela_closed = true;
		setFocusOnFirstFocusableField();
	}
	
	function openBuscarMeioTransporte(){
		janela_closed = false;
		janela = showModalDialog('portaria/consultarMeiosTransporteChegada.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:480px;dialogHeight:120px;');
		janela_closed = true;
		setFocusOnFirstFocusableField();
	}
</script>