<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.manterRotaIntervaloCEPAction" onPageLoadCallBack="rotaIntervaloCepPageLoad">
 
	<adsm:form action="/municipios/manterRotaIntervaloCEP" idProperty="idRotaIntervaloCep" height="100">
	
	    <adsm:hidden property="tpEmpresa" value="M" serializable="false"></adsm:hidden>
		<adsm:i18nLabels>
                <adsm:include key="LMS-00013"/>
                <adsm:include key="filial"/>
                <adsm:include key="rotaColetaEntrega2"/>
                <adsm:include key="cep"/>
                <adsm:include key="cliente"/>
    	</adsm:i18nLabels>	

		<adsm:lookup dataType="text"
					 property="rotaColetaEntrega.filial"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.municipios.manterRotaIntervaloCEPAction.findLookupFilial"
    				 label="filial" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="20%" 
    				 width="30%" 
    				 exactMatch="true" 
    				 required="true"
    				 action="/municipios/manterFiliais">
         	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
         	<adsm:propertyMapping criteriaProperty="tpEmpresa"  modelProperty="empresa.tpEmpresa" />
         	<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
	    </adsm:lookup>

		<adsm:lookup label="rotaColetaEntrega2" property="rotaColetaEntrega" 
			 		 idProperty="idRotaColetaEntrega" 
			 		 criteriaProperty="nrRota"
					 action="/municipios/manterRotaColetaEntrega" 
					 service="lms.municipios.manterRotaIntervaloCEPAction.findLookupRotaColetaEntrega" 
					 dataType="integer" size="3" maxLength="3" labelWidth="20%" width="80%" >
			<adsm:propertyMapping modelProperty="dsRota" relatedProperty="rotaColetaEntrega.dsRota" />
			<adsm:propertyMapping modelProperty="filial.idFilial" criteriaProperty="rotaColetaEntrega.filial.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" criteriaProperty="rotaColetaEntrega.filial.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>			
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:lookup>		
	
		<adsm:lookup service="lms.municipios.municipioService.findLookupMunicipio"  labelWidth="20%" dataType="text" property="municipio"
					 criteriaProperty="nmMunicipio" idProperty="idMunicipio" label="municipio" size="35" maxLength="50" width="80%"
					 action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="2" exactMatch="false" cellStyle="vertical-align:bottom;" >
		</adsm:lookup>
		
        <adsm:textbox label="cep" labelWidth="20%" width="30%" dataType="text" size="8" maxLength="8" required="false" property="nrCep"/>

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="cliente" 
			maxLength="20" 
			property="cliente" 
			service="lms.municipios.manterRotaIntervaloCEPAction.findLookupCliente" 
			size="20" 
			labelWidth="20%" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
		</adsm:lookup>
		
		<adsm:combobox service="lms.municipios.manterRotaIntervaloCEPAction.findEnderecoByIdCliente" 
					   labelWidth="20%" 
					   width="31%" 
					   boxWidth="200"
					   required="false" 
					   optionProperty="idEnderecoPessoa" 
					   optionLabelProperty="dsEndereco" 
					   property="enderecoCliente"
					   label="endereco">
			<adsm:propertyMapping criteriaProperty="cliente.idCliente" modelProperty="pessoa.idPessoa" />
		</adsm:combobox>

		<adsm:combobox property="tpGrauRisco" labelWidth="20%" label="grauRisco" domain="DM_GRAU_RISCO" width="30%" />	
		
		<adsm:combobox property="tipoDificuldadeAcesso.idTipoDificuldadeAcesso" labelWidth="20%" width="30%" label="dificuldadeAcesso"
				service="lms.municipios.manterRotaIntervaloCEPAction.findTipoDificuldadeAcesso"	
				optionLabelProperty="dsTipoDificuldadeAcesso" boxWidth="210" optionProperty="idTipoDificuldadeAcesso" />
				
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaIntervaloCEP"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form> 
	
	<adsm:grid  property="rotaIntervaloCEP" idProperty="idRotaIntervaloCep" selectionMode="check"
			scrollBars="horizontal" unique="true"  gridHeight="180" rows="9"
			service="lms.municipios.manterRotaIntervaloCEPAction.findPaginatedCustom"
			rowCountService="lms.municipios.manterRotaIntervaloCEPAction.getRowCountCustom"
			defaultOrder="nrOrdemOperacao, municipio_.nmMunicipio, nrCepInicial" >
		
		<adsm:gridColumnGroup customSeparator=" - ">		
				<adsm:gridColumn title="rota" property="rotaColetaEntrega.nrRota" width="35"/>
				<adsm:gridColumn title="" property="rotaColetaEntrega.dsRota" width="150"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="130" />
		
		<adsm:gridColumn title="ordemOperacao" property="nrOrdemOperacao" dataType="integer" width="120" />
		
		<adsm:gridColumn title="cepInicial" property="nrCepInicial" width="75" />
		<adsm:gridColumn title="cepFinal" property="nrCepFinal" width="60" />		
		 
		<adsm:gridColumn title="identificacao" property="tpIdentificacaoCliente" isDomain="true" width="50"/>
		<adsm:gridColumn title="" property="nrIdentificacaoCliente" align="right" width="100"/>
		<adsm:gridColumn title="cliente" property="cliente" width="200" />		

		<adsm:gridColumn title="endereco" property="enderecoCliente" width="250" />
		
		<adsm:gridColumn title="grauRisco" property="tpGrauRisco" isDomain="true" width="80" />
		<adsm:gridColumn title="dificuldadeAcesso" property="tipoDificuldadeAcesso.dsTipoDificuldadeAcesso" width="135" />
		<adsm:gridColumn title="horaCorteSolicitacaoColeta" property="hrCorteSolicitacao" dataType="JTTime" width="195" align="center" />
		<adsm:gridColumn title="horaCorteParaColeta" property="hrCorteExecucao" width="160" dataType="JTTime" align="center" />
		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
		 
	</adsm:grid>
</adsm:window>
<script>
 

	function initWindow(eventObj) {
	  	if (eventObj.name == "cleanButton_click" ) {
			writeDataSession();
			
			setFocusOnFirstFocusableField(document);			
		}
	}

	function rotaIntervaloCepPageLoad_cb(data){
		onPageLoad_cb(data);

		if (!document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink == "true" || document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink == undefined) {
			var sdo = createServiceDataObject("lms.municipios.manterRotaIntervaloCEPAction.findDataSession","dataSession",null);
			xmit({serviceDataObjects:[sdo]});
		}
		
	//	carregaMunicipiosAtendidos();	
	}
	
	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			
		writeDataSession();
	}

	function writeDataSession() {
		if (idFilial != null &&
			sgFilial != null &&
			nmFilial != null) {
			setElementValue("rotaColetaEntrega.filial.idFilial",idFilial);
			setElementValue("rotaColetaEntrega.filial.sgFilial",sgFilial);
			setElementValue("rotaColetaEntrega.filial.pessoa.nmFantasia",nmFilial);
		}		
			
		setFocusOnFirstFocusableField(document);			
	}
	
	function carregaMunicipiosAtendidos(){

		var data = new Array();

		setNestedBeanPropertyValue(data, "idFilial", getElementValue("rotaColetaEntrega.filial.idFilial"));
			
		var sdo = createServiceDataObject("lms.municipios.manterRotaIntervaloCEPAction.findMunicipioAtendido", "municipio.idMunicipio", data);
		xmit({serviceDataObjects:[sdo]});
	}
</script>
