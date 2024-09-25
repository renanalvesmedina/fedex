<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirChequesSubstituidosAction">
	<adsm:form action="/contasReceber/emitirChequesSubstituidos">
	
		<adsm:hidden property="filialMatriz"/>	
		<adsm:lookup dataType="text" 
					 property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.contasreceber.emitirChequesSubstituidosAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="80%"
					 labelWidth="20%"
					 exactMatch="true">
				<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>				
				<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial"/>			
				<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="40" maxLength="40" disabled="true"/>
			</adsm:lookup>
	   	<adsm:hidden property="siglaFilial" serializable="true"/> 
		
        <adsm:range label="periodoSubstituicao" labelWidth="20%" width="40%">
			<adsm:textbox property="periodoInicial" dataType="JTDate" required="true"/>		
			<adsm:textbox property="periodoFinal" dataType="JTDate" required="true"/>		
		</adsm:range>
		
		<adsm:hidden property="moeda.dsSimbolo" serializable="true"/>

		<adsm:combobox property="moeda.idMoeda" label="moedaExibicao" optionProperty="idMoeda" 
			optionLabelProperty="siglaSimbolo"
			service="lms.contasreceber.emitirRelacaoContaFreteAction.findMoedaPaisCombo" 
			labelWidth="20%"
			width="20%" required="true" serializable="true"
			onDataLoadCallBack="setMoedaPadrao">
			<adsm:propertyMapping relatedProperty="moeda.dsSimbolo" modelProperty="siglaSimbolo"/>			
		</adsm:combobox>
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" required="true" labelWidth="20%" defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton
				service="lms.contasreceber.emitirChequesSubstituidosAction" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script>

	function initWindow(eventObj){
	
		if(eventObj.name == 'tab_load' || eventObj.name == 'cleanButton_click') {
			
			_serviceDataObjects = new Array();
			
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirChequesSubstituidosAction.findMoedaUsuario",
				"setMoedaUsuario", 
				new Array()));
		
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirChequesSubstituidosAction.findFilialUsuarioLogado",
				"setFilialUsuario", 
				new Array()));

			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirChequesSubstituidosAction.validateFilialUsuarioMatriz",
				"setFilialMatriz", 
				new Array()));
			
	        xmit(false);

			_serviceDataObjects = new Array();
	     
	   }
	        
	}

	// seta a filial quando não é matriz...
	function setFilialUsuario_cb(data, error) {
		if (data != null) {
			if ((getElementValue("filialMatriz") != "true")) {
				setElementValue("filial.idFilial", data.idFilial);
				setElementValue("filial.sgFilial", data.sgFilial);
				setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
			}
		}
		setDisabled("filial.idFilial", (getElementValue("filialMatriz") != "true"));
		setFocusOnFirstFocusableField(document);
	}

	function setFilialMatriz_cb(data, error) {
		if (data != null) {
			setElementValue("filialMatriz", data._value);
		}
	}

	function setMoedaUsuario_cb(data, error) {
		setElementValue('moeda.idMoeda', data.idMoeda);
		setElementValue('moeda.dsSimbolo', data.siglaSimbolo);
	}
	
	function setMoedaPadrao_cb(data){

			moeda_idMoeda_cb(data);
			_serviceDataObjects = new Array();
			
			addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirChequesSubstituidosAction.findMoedaUsuario",
				"setMoedaUsuario", 
				new Array()));
		
	        xmit(false);

	}
	
</script>