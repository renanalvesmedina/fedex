<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirFechamentoDiarioCheques" >

		<adsm:hidden property="idFilial"/>
		<adsm:textbox labelWidth="20%" width="80%" dataType="text" label="filial" property="sgFilial" size="3" disabled="true">
		<adsm:textbox dataType="text" property="nmFilial" size="30" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox label="data"  property="data" dataType="JTDate" labelWidth="20%" width="30%" required="true"/>

		<adsm:combobox  labelWidth="20%" width="30%" boxWidth="85" label="moeda" property="moedaPais.idMoedaPais"  required="true"
        	optionLabelProperty="dsSimbolo"
        	optionProperty="idMoedaPais"
        	autoLoad="true"
        	service="lms.contasreceber.manterChequesPreDatadosAction.findComboMoeda"
        >
        	<adsm:propertyMapping modelProperty="dsSimbolo" relatedProperty="siglaSimbolo"/>
        </adsm:combobox>
        
        <adsm:hidden serializable="true" property="siglaSimbolo"/>
        
		<adsm:combobox property="tpFormatoRelatorio" 
			required="true"
			label="formatoRelatorio" 
			defaultValue="pdf"
			domain="DM_FORMATO_RELATORIO"
			labelWidth="20%" width="80%"/>

		<adsm:buttonBar>
		
			<adsm:button caption="visualizar" buttonType="reportViewerButton" onclick="gerarRelatorio();" disabled="false"/>
			
			<adsm:resetButton/>
			
		</adsm:buttonBar>
		
	</adsm:form>

</adsm:window>

<script>
	function myPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data,erro);
		buscaMoeda();
	}
	
	function initWindow(eventObj){
	
		if(eventObj.name == 'cleanButton_click') {
			buscaMoeda();
		}
		
		setElementValue("tpFormatoRelatorio", "pdf");
		
		if(eventObj.name == 'tab_load' || eventObj.name == 'cleanButton_click') {
				
			_serviceDataObjects = new Array();
				
			addServiceDataObject(createServiceDataObject(
					"lms.contasreceber.emitirFechamentoDiarioChequesAction.findFilialUsuarioLogado",
					"setFilialUsuario", 
					new Array()));
				
			addServiceDataObject(createServiceDataObject(
					"lms.contasreceber.emitirFechamentoDiarioChequesAction.findDataAtual", 
					"setDataAtual", 
					new Array())); 	
	
	        xmit(false);
	
	   }
		        
	}
	
	
	// seta a filial
	function setFilialUsuario_cb(data, error) {
		if (data != null) {
			setElementValue("idFilial", data.idFilial);
			setElementValue("sgFilial", data.sgFilial);
			setElementValue("nmFilial", data.pessoa.nmFantasia);
		}
	}
	
	// seta a data atual
	function setDataAtual_cb(data, error) {
		if (data != null) {
			setElementValue("data", setFormat("data", data.data));
		}
	}

	
	function buscaMoeda(){
	
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirFechamentoDiarioChequesAction.getMoedaUsuarioLogado",
			"setMoedaUsuario", 
			new Array()));
	
        xmit(false);

		_serviceDataObjects = new Array();

	}

	function setMoedaUsuario_cb(data, error) {
	
		if( error != undefined ){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;
		}

		if (data != null) {
			var moeda = data._value;
			var e = getElement("moedaPais.idMoedaPais");
			
			for (var i = 0; i < e.options.length; i++) {
				if (e.options[i].value == moeda) {
					e.options[i].selected = true;
					setElementValue("siglaSimbolo", e.options[i].text);
					break;
				}
			}
			
		}
	}

	/**
	 * Valida se a data é maior que a data atual
	 */
	function validateData(){
		
		var data = getElementValue("data");
		
		if ( data != '' ) {
			
			var dados = new Array();
			setNestedBeanPropertyValue(dados, "data", data);
			
			_serviceDataObjects = new Array();
		
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirFechamentoDiarioChequesAction.validateDate",
				"validateData", 
				dados));
		
	        xmit(false);
			
		}
	}
	
	/**
	 * CallBack da function validateData
	 */
	function validateData_cb(data, error){
		
		if ( error != undefined ) {
			alert(error);
			setFocus("data");
			return false;
		}
		
		reportButtonScript('lms.contasreceber.emitirFechamentoDiarioChequesAction', 'openPdf', document.forms[0]);
		return true;
	}
	
	/**
	 * Gera o relatório após fazer as validações necessárias
	 */
	function gerarRelatorio(){
		if ( validateTabScript(document.forms[0]) ) {
			validateData();
		}
	}
	

</script>