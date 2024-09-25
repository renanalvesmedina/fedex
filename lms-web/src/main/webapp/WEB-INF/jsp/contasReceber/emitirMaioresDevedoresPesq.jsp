<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirMaioresDevedores">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-36121"/>
		</adsm:i18nLabels>

        <adsm:textbox dataType="JTDate" label="limiteVencimento" property="limiteVencimento" size="8" maxLength="20" required="true" labelWidth="20%" width="27%"/>
        
        <adsm:combobox property="situacao" label="situacao" domain="DM_SITUACAO_MAIORES_DEVEDORES" defaultValue="V" required="true" labelWidth="20%" width="33%" /> 

		<adsm:hidden property="dataAtual"/>

		<adsm:hidden property="sgDsRegional" serializable="true"/>

	    <adsm:combobox label="regional" property="regional.idRegional" 
	    	optionLabelProperty="siglaDescricao" optionProperty="idRegional" 
	    	service="lms.municipios.regionalService.findRegionaisVigentes" 
	    	labelWidth="20%" width="27%" boxWidth="170" required="false">
   	         <adsm:propertyMapping relatedProperty="dataAtual" modelProperty="dtVigenciaInicial"/>
	         <adsm:propertyMapping relatedProperty="dataAtual" modelProperty="dtVigenciaFinal"/>
	         <adsm:propertyMapping relatedProperty="sgDsRegional" modelProperty="siglaDescricao"/>
        </adsm:combobox>

		<adsm:hidden property="sgFilial"/>

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirMaioresDevedoresAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="33%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			<%--adsm:propertyMapping modelProperty="historicoFiliais.vigenteEm" criteriaProperty="dataAtual"/--%>
			<adsm:propertyMapping modelProperty="historicoFiliais.tpFilial" criteriaProperty="tipoFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="35" disabled="true" serializable="true"/>
		</adsm:lookup>
		
        <adsm:combobox property="tipoFilial" label="tipoFilial" domain="DM_TIPO_FILIAL" labelWidth="20%" width="27%"  /> 

		<adsm:hidden property="dsTipoCliente" serializable="true"/>
		<adsm:combobox
			property="tpCliente"
			label="tipoCliente"
			service="lms.contasreceber.emitirMaioresDevedoresAction.findTipoCliente"
			optionProperty="value" optionLabelProperty="description"
			onchange="return tipoClienteOnChange(this);"
			labelWidth="20%" width="33%" boxWidth="130"/>
		
        <adsm:combobox property="modal" label="modal" domain="DM_MODAL" labelWidth="20%" width="27%" />

        <adsm:combobox property="abrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="20%" width="33%" />

        <adsm:textbox dataType="integer" property="qtdCliente"  label="quantidadeClientes" maxLength="30" size="15" labelWidth="20%" width="27%" minValue="1"/>        
        
		<adsm:hidden property="moeda.dsSimbolo" serializable="true"/>

		<adsm:combobox property="moeda.idMoeda" label="moedaExibicao" optionProperty="idMoeda" 
			optionLabelProperty="siglaSimbolo"
			service="lms.contasreceber.emitirRelacaoContaFreteAction.findMoedaPaisCombo" 
			labelWidth="20%"
			width="33%" required="true" serializable="true">
			<adsm:propertyMapping relatedProperty="moeda.dsSimbolo" modelProperty="siglaSimbolo"/>			
		</adsm:combobox>

        <adsm:checkbox property="soTotais" label="soTotais" labelWidth="20%" width="27%" />

 		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="20%" 
    				   width="33%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirMaioresDevedoresAction" disabled="false"/>
			<adsm:button caption="limpar" buttonType="reset" onclick="setaDefault();" disabled="false" />
			
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script>

	function setaDefault() {
		
		cleanButtonScript(this.document);
		
		getElement('soTotais').checked = 'true';
	
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirMaioresDevedoresAction.findDataLimite",
			"setDataLimite", 
			new Array()));

		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirMaioresDevedoresAction.findDataAtual",
			"setDataAtual", 
			new Array()));

		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirMaioresDevedoresAction.findFilialUsuario",
			"setFilialUsuario", 
			new Array()));


		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirMaioresDevedoresAction.findMoedaUsuario",
			"setMoedaUsuario", 
			new Array()));			
		
        xmit(false);	
	}

    function myOnPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data, erro);

		setaDefault();		
	}

	function setFilialUsuario_cb(data, error) {
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue('filial.sgFilial', data.sgFilial);
		setElementValue('sgFilial', data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
		
		// Caso a filial da sessão do usuário não seja matriz, proteger os campos filialCobranca, regional e tipoFilial
		if(data.filialUserIsMatriz == "false"){
			setDisabled("regional.idRegional", true);
			setDisabled("filial.idFilial", true);
			setDisabled("tipoFilial", true);
		}
	}
	
	function setMoedaUsuario_cb(data, error) {
		setElementValue('moeda.idMoeda', data.idMoeda);
		setElementValue('moeda.dsSimbolo', data.siglaSimbolo);
	}
	
	function setDataLimite_cb(data, error) {
		setElementValue('limiteVencimento', data._value);
	}

	function setDataAtual_cb(data, error) {
		setElementValue('dataAtual', data._value);
	}

	function tipoClienteOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsTipoCliente', elem.options[elem.selectedIndex].text);		
		}
	}
</script>