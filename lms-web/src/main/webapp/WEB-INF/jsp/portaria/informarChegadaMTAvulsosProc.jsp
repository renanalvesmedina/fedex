<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
 function chegadaPageLoad_cb(){
 	onPageLoad_cb();
 	document.getElementById("meioTransporteTerceiro.nrIdentificao").serializable = "true";
 }
//--> 
</script>
<adsm:window onPageLoadCallBack="chegadaPageLoad">
	<adsm:form action="/portaria/manterFinalidadesBox" id="formMain">
	<adsm:hidden property="tpEmpresa" serializable="false" value="M"></adsm:hidden>
		
  		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				 service="lms.portaria.selecionarMeiosTransporteChegadaAction.findLookupFilial" dataType="text" label="filial" size="3" 
				 action="/municipios/manterFiliais" required="true" disabled="true"
				 labelWidth="15%" width="35%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px">
				 
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<%--adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>  
		
		<adsm:combobox label="portaria" property="portaria.idPortaria" labelWidth="15%" width="35%" 
				service="lms.portaria.selecionarMeiosTransporteChegadaAction.findPortaria" 
				optionLabelProperty="dsPortaria" optionProperty="idPortaria" onDataLoadCallBack="portariaDataLoad"
				onchange="portariaChange(this)" boxWidth="200" required="true" >
  			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="idFilial"/>
	   	</adsm:combobox> 
	   
		<adsm:lookup dataType="text" property="meioTransporteTerceiro"
				service="lms.portaria.selecionarMeiosTransporteChegadaAction.findMeioTransporteTerceiro"
				idProperty="idMeioTransporteTerceiro" criteriaProperty="nrIdentificao"
				picker="false" exactMatch="true" action="/configuracoes/manterPessoas"
				label="meioTransporte" maxLength="10" size="14" allowInvalidCriteriaValue="true"
				onDataLoadCallBack="meioTransporteTerceiroDataLoad"
				onchange="return meioTransporteTerceiroChange(this)"
				labelWidth="15%" width="35%" cellStyle="vertical-Align:bottom" required="true" >
					 
  			<adsm:propertyMapping relatedProperty="meioTransporteTerceiro.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
  					modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="true"/>
  			<adsm:propertyMapping relatedProperty="meioTransporteTerceiro.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
  					modelProperty="modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte" blankFill="true"/>
  			<adsm:propertyMapping relatedProperty="idModeloMeioTransporteTemp" modelProperty="modeloMeioTransporte.idModeloMeioTransporte"/>
  			<adsm:propertyMapping relatedProperty="meioTransporteTerceiro.nrAno" modelProperty="nrAno"/>
  			<adsm:propertyMapping relatedProperty="meioTransporteTerceiro.nmTransportadora" modelProperty="nmTransportadora"/>
		</adsm:lookup>
        
		<adsm:textbox label="semiReboque" labelWidth="15%" width="35%" size="14" maxLength="10" dataType="text" 
				property="nrIdentificacaoSemiReboque" cellStyle="vertical-Align:bottom" 
				onchange="return meioTransporteSemiReboqueChange(this)"/>
	
		<adsm:combobox property="meioTransporteTerceiro.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
				optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
				service="lms.portaria.selecionarMeiosTransporteChegadaAction.findTipoMeioTransporte" onlyActiveValues="true"				
				label="tipoMeioTransporte" labelWidth="15%" width="85%" boxWidth="172" cellStyle="vertical-align:bottom;"
				serializable="false" required="true"/>
	
 		<adsm:combobox property="meioTransporteTerceiro.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
				optionProperty="idMarcaMeioTransporte" optionLabelProperty="dsMarcaMeioTransporte"
				service="lms.portaria.selecionarMeiosTransporteChegadaAction.findMarcaMeioTransporte" onlyActiveValues="true"				
				label="marca" labelWidth="15%" width="35%" boxWidth="172" serializable="false"  required="true"/>
	
		<adsm:hidden property="idModeloMeioTransporteTemp"/>	
		<adsm:combobox property="meioTransporteTerceiro.modeloMeioTransporte.idModeloMeioTransporte"
				optionProperty="idModeloMeioTransporte" optionLabelProperty="dsModeloMeioTransporte"
				service="lms.portaria.selecionarMeiosTransporteChegadaAction.findModeloMeioTransporte" onlyActiveValues="true" required="true"
				label="modelo" labelWidth="15%" width="35%" boxWidth="172" >
			<adsm:propertyMapping criteriaProperty="meioTransporteTerceiro.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
					modelProperty="tipoMeioTransporte.idTipoMeioTransporte" />	
			<adsm:propertyMapping criteriaProperty="meioTransporteTerceiro.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
					modelProperty="marcaMeioTransporte.idMarcaMeioTransporte" />
		</adsm:combobox>
		<adsm:textbox dataType="integer" property="meioTransporteTerceiro.nrAno" size="5" maxLength="4" label="ano"/>

		<adsm:section caption="motorista"/>

		<adsm:hidden property="motoristaTerceiro.idMotoristaTerceiro" />
		<adsm:textbox dataType="text" property="motoristaTerceiro.nrRg" onchange="return findDadosMotorista('motoristaTerceiro.nrRg', 'nrRg', 'preencheDadosMotoristaRg')" size="15" maxLength="12" label="rg" cellStyle="vertical-align:bottom"/>
        <adsm:textbox label="cpf" property="motoristaTerceiro.nrCpf" onchange="return findDadosMotorista('motoristaTerceiro.nrCpf', 'nrCpf', 'preencheDadosMotoristaCpf')" labelWidth="15%" width="30%" required="true"  dataType="CPF" />		       			 
		<adsm:textbox dataType="text" property="motoristaTerceiro.nmMotorista" size="30" maxLength="60" label="nome" width="35%" required="true" cellStyle=""/>
		<adsm:textbox dataType="integer" property="motoristaTerceiro.nrCnh" onchange="return findDadosMotorista('motoristaTerceiro.nrCnh', 'nrCnh', 'preencheDadosMotoristaCnh')" size="15" maxLength="11" label="CNH"/>		
    	<adsm:textbox dataType="text" property="meioTransporteTerceiro.nmTransportadora" size="30" maxLength="60" label="transportadora" />

	</adsm:form>

	<adsm:grid idProperty="idControleEntSaidaTerceiro" property="chegadaSaida" autoSearch="false" onDataLoadCallBack="gridDataLoad"
			service="lms.portaria.selecionarMeiosTransporteChegadaAction.findGridChegadaSaida" showPagging="false" gridHeight="140" 
			unique="true" rows="2" title="chegadasSaidas" scrollBars="both"
			selectionMode="none" onRowClick="rowClick">
				
		<adsm:gridColumn title="empresa" property="nmEmpresa" width="180" align="left"/>
		<adsm:gridColumn title="filial" property="siglaNomeFilial" width="180" align="left"/>
		<adsm:gridColumn title="chegada" property="dhEntrada" dataType="JTDateTimeZone" width="145" align="center"/>
		<adsm:gridColumn title="saida" property="dhSaida"  dataType="JTDateTimeZone" width="145" align="center"/>
		<adsm:gridColumn title="cpf" property="nrCpf" width="90" align="right"/>
		<adsm:gridColumn title="nome" property="nmMotorista" dataType="text" width="180"/>
		<adsm:gridColumn title="rg" property="nrRg" dataType="integer" width="90" />
		<adsm:gridColumn title="CNH" property="nrCnh" dataType="integer" width="90" /> 

		<adsm:buttonBar>			
			<adsm:button id="informarSaidas" caption="informarSaidasButton" action="/portaria/informarSaida" cmd="main">
				<adsm:linkProperty src="filial.idFilial" target="idFilialTemp" disabled="false"/>
				<adsm:linkProperty src="filial.sgFilial" target="filial.sgFilial" disabled="false" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia" />
			</adsm:button>
			<adsm:button id="confirmar" caption="confirmar" onclick="storeButtonScript('lms.portaria.selecionarMeiosTransporteChegadaAction.storeMtAvulso', 'afterConfirmar', document.getElementById('formMain') );"/>
			<adsm:newButton id="cancelar" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--

	function rowClick(){
		return false;
	}
	
	function initWindow(evt){
		setDisabled("informarSaidas", false);
		setDisabled("cancelar", false);
		setDisabled("confirmar", false);
		
		//Se entrou na tab carrega a combo de filial e seta os campos filial e portaria de acordo com a tab list
		setaCamposFilialPortaria();
		setFocus("meioTransporteTerceiro.nrIdentificao");
		chegadaSaidaGridDef.resetGrid();
	}
	
	//Seta os campos filial e portaria de acordo com a tab list
	function setaCamposFilialPortaria(){
		var tab = getTabList();
		var idFilial = tab.tabOwnerFrame.document.getElementById("filial.idFilial");
		var sgFilial = tab.tabOwnerFrame.document.getElementById("filial.sgFilial");
		var nmFantasia = tab.tabOwnerFrame.document.getElementById("filial.pessoa.nmFantasia");
		
		//Verifica se a filial da tab list foi trocada, neste caso, troca a filial e recarrega a combo de portaria		
		if (getElementValue("filial.idFilial") != idFilial.value) {
			setElementValue("filial.idFilial", idFilial.value);			
			setElementValue("filial.sgFilial", sgFilial.value);
			setElementValue("filial.pessoa.nmFantasia", nmFantasia.value);
							
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
			
		//Senao, somente seta a portaria com a portaria da tab list
		} else {
			var tab = getTabList();
			var portaria = tab.tabOwnerFrame.document.getElementById("portaria.idPortaria");
			setElementValue("portaria.idPortaria", portaria.value);
		}	

	}
	
	function afterConfirmar_cb(data, error){
		if (error != undefined){
			alert(error);
		} else {
			var tabGroup = getTabGroup(this.document);
			tabGroup.getTab("cad").changed = false;
			tabGroup.selectTab('pesq',{name:'tab_click'});
			getTabList().tabOwnerFrame.loadGrids();
		}
	}
	
	function findDadosMotorista(nmCampo, nmCriterio, nmCallback) {
		if (getElementValue(nmCampo) != "") {
			var data = new Array();
			setNestedBeanPropertyValue(data, nmCriterio, getElementValue(nmCampo));
			var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.findMotoristaTerceiro", nmCallback, data);
			xmit({serviceDataObjects:[sdo]}); 
		} else {
			limpaCamposMotorista(nmCriterio);
		}
		return true;
	}
	
	function preencheDadosMotorista_cb(data, error) {
	}
	
	function preencheDadosMotoristaRg_cb(data, error) {
		preencheDadosMotorista(data, error, "motoristaTerceiro.nrRg");
	}
	
	function preencheDadosMotoristaCpf_cb(data, error) {
		preencheDadosMotorista(data, error, "motoristaTerceiro.nrCpf");
	}
	
	function preencheDadosMotoristaCnh_cb(data, error) {
		preencheDadosMotorista(data, error, "motoristaTerceiro.nrCnh");
	}
	
	function preencheDadosMotorista(data, error, nmCampo) {
		if (error != undefined) {
			setFocus(nmCampo, false);
			alert(error);
			return;
		}
		if (data != undefined && data.length >= 1){
			setElementValue("motoristaTerceiro.idMotoristaTerceiro", getNestedBeanPropertyValue(data, ":0.idMotoristaTerceiro"));
			setElementValue("motoristaTerceiro.nrRg", getNestedBeanPropertyValue(data, ":0.nrRg"));
			setElementValue("motoristaTerceiro.nmMotorista", getNestedBeanPropertyValue(data, ":0.nmMotorista"));
			setElementValue("motoristaTerceiro.nrCpf", formatCPF(getNestedBeanPropertyValue(data, ":0.nrCpf")));
			setElementValue("motoristaTerceiro.nrCnh", getNestedBeanPropertyValue(data, ":0.nrCnh"));
			setElementValue("nrIdentificacaoSemiReboque", getNestedBeanPropertyValue(data, ":0.nrIdentificacaoSemiReboque"));
		}
	}

	function limpaCamposMotorista(criterio){
		if(getElementValue("motoristaTerceiro.idMotoristaTerceiro") != "" || criterio == "meioTransporte") {
			if (criterio == undefined || criterio != "idMotoristaTerceiro")
				resetValue("motoristaTerceiro.idMotoristaTerceiro");
			if (criterio == undefined || criterio != "nrRg")
				resetValue("motoristaTerceiro.nrRg");
			if (criterio == undefined || criterio != "nmMotorista")
				resetValue("motoristaTerceiro.nmMotorista");
			if (criterio == undefined || criterio != "nrCpf")
				resetValue("motoristaTerceiro.nrCpf");
			if (criterio == undefined || criterio != "nrCnh")
				resetValue("motoristaTerceiro.nrCnh");
		}
	}
	

	function filialDataLoad_cb(data){
		filial_idFilial_cb(data);
					
		if (data.length == 1)			
			setDisabled("filial.idFilial", true);
		else 
			setDisabled("filial.idFilial", false);
	}


	function portariaDataLoad_cb(data){
		portaria_idPortaria_cb(data);
		
		if (data.length > 0){
			var tab = getTabList();
			var filial = tab.tabOwnerFrame.document.getElementById("filial.idFilial");
			//Se retornou so uma portaria, seleciona e desabilita
			if (data.length == 1) {
				document.getElementById("portaria.idPortaria").selectedIndex = 1;
				setDisabled("portaria.idPortaria", true);
				
			} else {
				setDisabled("portaria.idPortaria", false);
				//Senao verifica se a filial da tab list foi eh igual e seta a portaria de acordo
				if (filial.value == getElementValue("filial.idFilial")){				
					var portaria = tab.tabOwnerFrame.document.getElementById("portaria.idPortaria");
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
		}
	}
	
	
	function meioTransporteTerceiroDataLoad_cb(data){
		meioTransporteTerceiro_nrIdentificao_exactMatch_cb(data);
		
		if (data != undefined && data.length > 0) {
			var idMeioTransporte = getNestedBeanPropertyValue(data, ":0.idMeioTransporteTerceiro");
			var idMarca = getNestedBeanPropertyValue(data, ":0.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte");
			var idTipo = getNestedBeanPropertyValue(data, ":0.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
			loadComboModelo(idMarca, idTipo);

		} else {
			
			resetValue("meioTransporteTerceiro.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
			resetValue("meioTransporteTerceiro.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte");
			resetValue("idModeloMeioTransporteTemp");
			resetValue("meioTransporteTerceiro.nrAno");
			resetValue("meioTransporteTerceiro.nmTransportadora");
			resetValue("nrIdentificacaoSemiReboque");
			chegadaSaidaGridDef.resetGrid();
			limpaCamposMotorista("meioTransporte");
		}
	}
	
	function meioTransporteTerceiroChange(obj){
		obj.value = obj.value.toUpperCase();
		validate(obj);		
		if (getElementValue("meioTransporteTerceiro.nrIdentificao") == '' && getElementValue("idModeloMeioTransporteTemp") != '') {
			document.getElementById("meioTransporteTerceiro.modeloMeioTransporte.idModeloMeioTransporte").options.length = 1;
			document.getElementById("meioTransporteTerceiro.modeloMeioTransporte.idModeloMeioTransporte").selectedIndex = 0;
			limpaCamposMotorista("meioTransporte");
			resetValue("nrIdentificacaoSemiReboque");
			chegadaSaidaGridDef.resetGrid();
		}
		return meioTransporteTerceiro_nrIdentificaoOnChangeHandler();
	}
	
	function loadComboModelo(idMarca, idTipo){
		var data = new Array();
		setNestedBeanPropertyValue(data, "marcaMeioTransporte.idMarcaMeioTransporte", idMarca);
		setNestedBeanPropertyValue(data, "tipoMeioTransporte.idTipoMeioTransporte", idTipo);
		
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.findModeloMeioTransporte", "preencheComboModelo",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function preencheComboModelo_cb(data){
		meioTransporteTerceiro_modeloMeioTransporte_idModeloMeioTransporte_cb(data);
		setElementValue("meioTransporteTerceiro.modeloMeioTransporte.idModeloMeioTransporte", getElementValue("idModeloMeioTransporteTemp"));
		loadGridChegadaSaida();		
	}
	
	function loadGridChegadaSaida(idMeioTransporte){
		var data = new Array();		
		setNestedBeanPropertyValue(data, "idMeioTransporte", getElementValue("meioTransporteTerceiro.idMeioTransporteTerceiro"));		
		chegadaSaidaGridDef.executeSearch(data);
	}
	
	function gridDataLoad_cb(data){
		loadUltimoMotorista();
	}
	
	function loadUltimoMotorista(){
		var data = new Array();		
		setNestedBeanPropertyValue(data, "idMeioTransporte", getElementValue("meioTransporteTerceiro.idMeioTransporteTerceiro"));	
		var sdo = createServiceDataObject("lms.portaria.selecionarMeiosTransporteChegadaAction.findUltimoMotorista", "preencheDadosMotorista",data);
		xmit({serviceDataObjects:[sdo]});
	}
	

	function portariaChange(obj){
		comboboxChange({e:obj});		
	}

	function getTabList(){
		var tabGroup = getTabGroup(this.document);
		return tabGroup.getTab("pesq");
	}
	
	function meioTransporteSemiReboqueChange(field) {
		field.value = field.value.toUpperCase();
		return validate(field);
	}
//-->
</script>