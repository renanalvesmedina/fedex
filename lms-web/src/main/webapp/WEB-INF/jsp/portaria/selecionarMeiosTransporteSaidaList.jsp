<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script>

	function pageLoad_cb(){
		onPageLoad_cb();

		if (document.getElementById("idFilialTemp").value != ''){
			setElementValue("filial.idFilial", getElementValue("idFilialTemp"));
			document.getElementById("filial.pessoa.nmFantasia").masterLink = 'false';
			setFocusOnFirstFocusableField();
			
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
		} else {
			findFilialSessao();	
		}
	}
 
</script>

<adsm:window title="selecionarMeiosTransporteSaida" service="lms.portaria.selecionarMeiosTransporteSaidaAction" onPageLoadCallBack="pageLoad">
<adsm:form action="/portaria/selecionarMeiosTransporteSaida">
	
	<adsm:hidden property="idControleTemp"/>
	<adsm:hidden property="idFilialTemp"/>
	<%---adsm:hidden property="tpEmpresa" serializable="false" value="M"></adsm:hidden--%>
  	   		
 	<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				 service="lms.portaria.selecionarMeiosTransporteSaidaAction.findLookupFilial" dataType="text" label="filial" size="3" 
				 action="/municipios/manterFiliais" onchange="return filialChange(this)" required="true"
				 labelWidth="15%" width="35%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px" disabled="true">
				 
		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
		<%--adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
	</adsm:lookup>  
		
  	<adsm:combobox label="portaria" property="portaria.idPortaria" labelWidth="15%" width="35%"  service="lms.portaria.selecionarMeiosTransporteSaidaAction.findPortaria" 
  				   optionLabelProperty="dsPortaria" optionProperty="idPortaria" onDataLoadCallBack="portariaDataLoad" boxWidth="200">
  			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="idFilial"/>
	 </adsm:combobox>
	   
	<adsm:grid title="viagem" property="viagem" autoSearch="false" idProperty="idControle" onDataLoadCallBack="gridViagem"
				service="lms.portaria.selecionarMeiosTransporteSaidaAction.findGridViagem" showPagging="false" selectionMode="none" 
				gridHeight="130" rows="5" unique="false" scrollBars="vertical"  onRowClick="gridRowClick">				
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="65"/>
		<adsm:gridColumn title="" property="nrIdentificador" width="75" align="left"/>		
		<adsm:gridColumn title="semiReboque" property="nrFrotaReboque" width="65"/>   
		<adsm:gridColumn title="" property="nrIdentificadorReboque" width="75" align="left"/>  		 
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="controleCarga" property="sgFilialCC" width="40" />		
			<adsm:gridColumn title="" property="nrControleCarga" width="40" mask="00000000" dataType="integer"/>
		</adsm:gridColumnGroup> 
		<adsm:gridColumn title="filialDestino" property="sgFilial" width=""/>
		
		<adsm:gridColumn title="rota" property="nrRota" dataType="integer" mask="0000" width="30" />
		<adsm:gridColumn title="" property="dsRota" width="125"/>
		
		<adsm:gridColumn title="dataHoraSaidaPrevista" property="dhPrevisaoSaida" dataType="JTDateTimeZone" width="115" align="center"/>		
	</adsm:grid>   
 
	<adsm:grid title="coletaEntregaOrdemDeSaidaVisitante" property="coletaEntrega"  idProperty="idControle"  autoSearch="false"
				service="lms.portaria.selecionarMeiosTransporteSaidaAction.findGridColetaEntrega" showPagging="false" selectionMode="none" 
				gridHeight="130" unique="false" rows="5" scrollBars="vertical" onRowClick="gridRowClick">
				
		<adsm:gridColumn title="meioTransporte"  property="nrFrota" width="65"/>
		<adsm:gridColumn title=""  property="nrIdentificador" width="75" align="left"/>
		<adsm:gridColumn title="semiReboque" property="nrFrotaReboque" width="65"/> 
		<adsm:gridColumn title="" property="nrIdentificadorReboque" width="75" align="left"/> 
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="controleCarga" property="sgFilial" width="50"/>		
			<adsm:gridColumn title="" property="nrControleCarga" width="55" mask="00000000" dataType="integer"/>
		</adsm:gridColumnGroup>

		<adsm:gridColumnGroup customSeparator=" - ">		
			<adsm:gridColumn title="rota" property="nrRota" width="60"/>
			<adsm:gridColumn title="" property="dsRota" width="150"/>
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
		<adsm:button caption="informarChegadasButton" id="informarChegadas" action="/portaria/informarChegada" cmd="main">
			<adsm:linkProperty src="filial.idFilial" target="idFilialTemp"/>
			<adsm:linkProperty src="filial.sgFilial" target="filial.sgFilial" disabled="true" />
			<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia" />
		</adsm:button>
		<adsm:button caption="atualizar" id="atualizar" onclick="atualizarClick()"/>
	</adsm:buttonBar> 

	</adsm:form> 
</adsm:window>
 <script> 
 	

	function procuraMeioTransporte(nrIdentificador, nrFrota){
		
		var data = viagemGridDef.gridState.data;
		var achou = false;
		
		nrIdentificador = nrIdentificador != undefined ? nrIdentificador.toUpperCase() : "";
		nrFrota = nrFrota != undefined ? nrFrota.toUpperCase() : "";
		
		for(var idx = 0; idx < data.length; idx++) {
			var nrIdentificadorGrid = data[idx].nrIdentificador.toUpperCase();
			var nrFrotaGrid = data[idx].nrFrota != undefined ? data[idx].nrFrota.toUpperCase() : undefined;
			if ((nrIdentificadorGrid == nrIdentificador) || 
				(nrFrotaGrid == nrFrota)) {
				achou = true;
				gridRowClick(data[idx].idControle);
				break;
			}
		}
		
		if (!achou) {
			var data = coletaEntregaGridDef.gridState.data;				
			for(var idx = 0; idx < data.length; idx++) {
				var nrIdentificadorGrid = data[idx].nrIdentificador.toUpperCase();
				var nrFrotaGrid = data[idx].nrFrota != undefined ? data[idx].nrFrota.toUpperCase() : undefined;
				if ((nrIdentificadorGrid == nrIdentificador) || 
					(nrFrotaGrid == nrFrota)) {
					achou = true;
					gridRowClick(data[idx].idControle);
					break;
				}
			}
		}
		
		if (!achou){
			LMS_06018();
		}

	}
 	 
  	function initWindow(){  
 		setDisabled("atualizar", false);
 		setDisabled("informarChegadas", false); 
 		setDisabled("buscarMeioTransporte", false); 
 	}
	
	
	function findFilialSessao(){

		var data = new Array();
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteSaidaAction.findFilialSessao",
					"findFilialSessao",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function findFilialSessao_cb(data, exception){
		if (exception == null){
			setElementValue("filial.idFilial", data.idFilial);
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
			//lookupChange({e:document.getElementById("filial.idFilial"), forceChange:true});
			notifyElementListeners({e:document.getElementById("filial.idFilial")});		
		}
	}
	function portariaDataLoad_cb(data){
		portaria_idPortaria_cb(data);
		
		if (data.length == 1) {
			document.getElementById("portaria.idPortaria").selectedIndex = 1;
			setDisabled("portaria.idPortaria");
		} else {		
			for (var i=0; i < data.length; i++){
				var blPadraoFilial = getNestedBeanPropertyValue(data, ':' + i + '.blPadraoFilial');
				if (blPadraoFilial == 'true'){
					document.getElementById("portaria.idPortaria").selectedIndex = (i+1);
					break;
				}
			}
		}	
		
		reloadGrids();	
	}

	function filialChange(obj){
		var retorno = filial_sgFilialOnChangeHandler();
		
		if (obj.value == '') {
			viagemGridDef.resetGrid();
			coletaEntregaGridDef.resetGrid();
		}
		
		return retorno;
	}
	
	function gridViagem_cb(data){
		loadGridColetaEntrega();
	}
	
	var janela;
	var janela_closed = true;
  	function reloadGrids(){  
  		if ((janela == undefined || janela_closed))
	  		loadGrids(); 
  	}
   
    function atualizarClick(){
		var tab = getTab(document);
		
		if (tab.validate({name:"findButton_click"})) {
			loadGrids();
			setFocusOnFirstFocusableField();
		}
    }
   
	function loadGrids(){
		loadGridViagem();
		atualizaGrids = true;
		
	}
	 
 	function loadGridViagem(){
 		if (getElementValue("filial.idFilial") != '') {
	 		var data= new Array();
 			setNestedBeanPropertyValue(data, "idFilial", getElementValue("filial.idFilial"));
 			setNestedBeanPropertyValue(data, "idPortaria", getElementValue("portaria.idPortaria"));
			viagemGridDef.executeSearch(data);
		}
 	}
 
	function loadGridColetaEntrega(){
		if (getElementValue("filial.idFilial") != '') {
			var data= new Array();
			setNestedBeanPropertyValue(data, "idFilial", getElementValue("filial.idFilial"));
 			setNestedBeanPropertyValue(data, "idPortaria", getElementValue("portaria.idPortaria"));
			coletaEntregaGridDef.executeSearch(data);
		}
	}
	
	function gridRowClick(id){
		setElementValue("idControleTemp", id);
		
		validaManifestoETransito(id);
		
		return false;
	}
	
	
	function validaManifestoETransito(id){
		var data = new Array();
		setNestedBeanPropertyValue(data, "idControleTemp", id);
		
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteSaidaAction.validaManifestoETransito",
				"validaManifestoETransito",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validaManifestoETransito_cb(data, error, errorId) {
		if (error != undefined) { 
			alert(error);
		} else {
			openConfirmacao();
		}
	}
	
	function openConfirmacao(){
		janela_closed = false;
		janela = showModalDialog('portaria/informarSaida.do?cmd=proc',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:640px;dialogHeight:400px;');
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