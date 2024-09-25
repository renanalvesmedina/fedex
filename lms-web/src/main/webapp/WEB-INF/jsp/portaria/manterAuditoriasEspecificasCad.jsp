<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterAuditoriasEspecificasAction" onPageLoadCallBack="pageLoadCustom">

	<adsm:form action="/portaria/manterAuditoriasEspecificas" onDataLoadCallBack="onDataLoadCallback" idProperty="idConfiguracaoAuditoriaFil" service="lms.portaria.manterAuditoriasEspecificasAction.findByIdCustom">

		<adsm:lookup property="filial" onchange="return resetFilial();" 
					 idProperty="idFilial" required="true" criteriaProperty="sgFilial" 
					 maxLength="3" service="lms.portaria.manterAuditoriasEspecificasAction.findLookupFilial" dataType="text" 
					 label="filial" size="3" action="/municipios/manterFiliais" criteriaSerializable="true"
					 labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup> 
		
		<adsm:combobox property="tpOperacao" labelWidth="20%" label="tipoOperacao" domain="DM_TIPO_OPERACAO_AUDITORIA" width="80%" required="true" onchange="onTpOperacaoChange();"/>
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		
        <adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
				service="lms.portaria.manterAuditoriasEspecificasAction.findLookupMeioTransporteRodoviario" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="20%" width="8%" size="8" serializable="false" maxLength="6" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
								  
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
								  modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
				
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
					service="lms.portaria.manterAuditoriasEspecificasAction.findLookupMeioTransporteRodoviario" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					width="69%" size="20" required="false" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />
									  
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
									  modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />		
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
			</adsm:lookup>
        </adsm:lookup>
                
		<adsm:lookup service="lms.portaria.manterAuditoriasEspecificasAction.findLookupRotasViagem" dataType="integer" 
					 property="rotaIdaVolta" idProperty="idRotaIdaVolta" 
					 criteriaProperty="nrRota"
					 size="4" label="rotaViagem" 
					 maxLength="4" exactMatch="false"					 
					 labelWidth="20%" cellStyle="vertical-align:bottom;"
					 mask="0000" width="35%"
					 action="/municipios/consultarRotas" cmd="idaVolta" disabled="false" >
			
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filialIntermediaria.idFilial" disable="true"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filialIntermediaria.sgFilial" inlineQuery="false" disable="true"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialIntermediaria.nmFilial" inlineQuery="false" disable="true"/>
			
			<adsm:propertyMapping relatedProperty="rotaViagem.dsRota" modelProperty="rota.dsRota"/>
			<adsm:propertyMapping relatedProperty="rotaIdaVolta.versao" modelProperty="versao"/>
			<adsm:propertyMapping relatedProperty="rotaIdaVolta.tpRotaIdaVolta" modelProperty="tpRotaIdaVolta.description"/>
			
			<adsm:textbox dataType="text" property="rotaViagem.dsRota" size="30" cellStyle="vertical-align:bottom;" disabled="true" serializable="true"/>
			
			<adsm:hidden property="rotaIdaVolta.versao"/>
			
		</adsm:lookup>
		
		<adsm:textbox label="sentido" labelWidth="20%" width="25%" property="rotaIdaVolta.tpRotaIdaVolta" dataType="text" size="20" disabled="true" serializable="false"/>
	 
		<adsm:combobox label="rotaColetaEntrega" property="rotaColetaEntrega.idRotaColetaEntrega" onchange="changeComboRota(this)"
				optionLabelProperty="nrDsRota" optionProperty="idRotaColetaEntrega" boxWidth="250"
				service="lms.portaria.manterAuditoriasEspecificasAction.findComboRotaColetaEntrega" labelWidth="20%" width="80%">
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
		</adsm:combobox>
		
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox size="12" property="dtVigenciaInicialRota" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRota"   dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>
		
		<adsm:range label="horarioAuditoria" labelWidth="20%" width="80%" required="true">
             <adsm:textbox dataType="JTTime" property="hrAuditoriaInicial" />
             <adsm:textbox dataType="JTTime" property="hrAuditoriaFinal"   />
        </adsm:range>

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="storeCallback"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>  
<script>

	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;

	function changeComboRota(field) {
		comboboxChange({e:field});
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaInicialRota",setFormat(document.getElementById("dtVigenciaInicialRota"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaFinalRota",setFormat(document.getElementById("dtVigenciaFinalRota"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaInicialRota");
			resetValue("dtVigenciaFinalRota");
		}
	}
		
	function storeCallback_cb(data, error){
		store_cb(data, error);
		if (error == undefined && data != undefined){
			comportamentoDetalhe(data);
			setFocus(document.getElementById("newButton"), false);
		}else{
			setFocusOnFirstFocusableField(document);
		}
	}

	function onDataLoadCallback_cb(data, exception,key) {
		onDataLoad_cb(data, exception); 
		
		if (data != undefined) {
			changeComboRota(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"));
			//setElementValue("dtVigenciaInicialDetalhe", getNestedBeanPropertyValue(data, "dtVigenciaInicial"));
			comportamentoDetalhe(data);	
			//setElementValue("rotaViagem.dsRotaTotal", getNestedBeanPropertyValue(data, "rotaViagem.dsRotaTotal"));
			setElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota", getNestedBeanPropertyValue(data, "meioTransporteRodoviario.meioTransporte.nrFrota"));
			setDisabled("rotaIdaVolta.tpRotaIdaVolta", true);			
		}
	}
	
	function comportamentoDetalhe(data){
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");	
			// 0 = VIGENCIA INICIAL > HOJE
			// PODE TUDO

	if (acaoVigenciaAtual == 0) {
				estadoNovo();
	 			
			// 1 = VIGENCIA INICIAL <= HOJE E 
			//     VIGENCIA FINAL   >= HOJE
			//     DESABILITA VIGENCIA INICIAL		 
			} else if (acaoVigenciaAtual == 1) {
				setDisabled(document, true);
				setDisabled("newButton", false);
				setDisabled("storeButton", false);
				setDisabled("removeButton", true);
				setDisabled("dtVigenciaFinal", false);
				setFocusOnFirstFocusableField();
			
			// 2 = VIGENCIA INICIAL <= HOJE E 
			//     VIGENCIA FINAL   < HOJE
			//     DESABILITA TUDO, EXCETO O BOTÃO NOVO
			} else if (acaoVigenciaAtual == 2) {
				setDisabled(document, true);
				setDisabled("newButton", false);
				setFocus(document.getElementById("newButton"), false);
			}
			setDisabled("dtVigenciaInicialRota",true);
			setDisabled("dtVigenciaFinalRota",true);
	}
	function estadoNovo() {
		//setDisabled(document, false);
		setDisabled('filial.pessoa.nmFantasia', true);		
		setDisabled('rotaViagem.dsRota', true);
		setDisabled('rotaIdaVolta.tpRotaIdaVolta', true);
		onTpOperacaoChange();
		setFocusOnFirstFocusableField();
		setDisabled("dtVigenciaInicialRota",true);
		setDisabled("dtVigenciaFinalRota",true);
	}
		
	function initWindow(eventObj) {		
		if(eventObj.name != "gridRow_click" && eventObj.name != 'storeButton'){
			estadoNovo();
			setDisabled('removeButton', true);
			if (idFilialLogado != undefined && idFilialLogado != "") {
				setaValoresFilial();
			} else {
				getFilialUsuario();
			}
		}
    }
    
    function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.portaria.manterAuditoriasEspecificasAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	function getFilialCallBack_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setaValoresFilial();
		}
	}	
	
	function setaValoresFilial() {
		setElementValue("filial.idFilial", idFilialLogado);
		setElementValue("filial.sgFilial", sgFilialLogado);
		setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);
		notifyElementListeners({e:document.getElementById("filial.idFilial")});
	}
	
	function resetFilial() {
		if (getElementValue("filial.sgFilial") == "" ) {
			setElementValue("filial.pessoa.nmFantasia", "");
			setElementValue("filial.idFilial","");
		}
		return filial_sgFilialOnChangeHandler();
	}
    
    function pageLoadCustom_cb() {
    	onPageLoad_cb();
		disableRotas();
	}
    
    function onTpOperacaoChange() {
    	if (getElementValue("tpOperacao") == 'A')
    		enableRotas();
    	else if (getElementValue("tpOperacao") == 'E')
    		enableRotaColetaEntrega();
    	else if (getElementValue("tpOperacao") == 'V')
    		enableRotaViagem();
    	else 
    		disableRotas();
    }
    
    function disableRotas() {
    	document.getElementById("rotaColetaEntrega.idRotaColetaEntrega").selectedIndex = 0;
    	setDisabled("rotaColetaEntrega.idRotaColetaEntrega", true);
    	changeComboRota(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"));
    	resetValue("rotaIdaVolta.idRotaIdaVolta");
    	setDisabled("rotaIdaVolta.idRotaIdaVolta", true);
    	setDisabled("rotaIdaVolta.tpRotaIdaVolta", true);
    }
    
    function enableRotas() {
    	setDisabled("rotaColetaEntrega.idRotaColetaEntrega", false);
    	setDisabled("rotaIdaVolta.idRotaIdaVolta", false);
    }
    
    function enableRotaViagem() {
    	document.getElementById("rotaColetaEntrega.idRotaColetaEntrega").selectedIndex = 0;
    	setDisabled("rotaColetaEntrega.idRotaColetaEntrega", true);
    	changeComboRota(document.getElementById("rotaColetaEntrega.idRotaColetaEntrega"));
    	setDisabled("rotaIdaVolta.idRotaIdaVolta", false);
    }
    
    function enableRotaColetaEntrega() {
    	resetValue("rotaIdaVolta.idRotaIdaVolta");
    	setDisabled("rotaIdaVolta.idRotaIdaVolta", true);
    	setDisabled("rotaColetaEntrega.idRotaColetaEntrega", false);
    }

</script> 