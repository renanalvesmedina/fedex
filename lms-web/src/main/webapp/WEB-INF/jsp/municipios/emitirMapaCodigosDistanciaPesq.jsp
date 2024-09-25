<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.emitirMapaCodigosDistanciaAction" onPageLoadCallBack="pagaLoadCustom" >
	<adsm:form action="/municipios/emitirMapaCodigosDistancia">
	
		<%-- Hidden para carregado na lookup de município atendido --%>
		<adsm:hidden property="flag" value="01" serializable="false" />
		
		<adsm:combobox property="tpEmissao" label="tipoEmissao" domain="DM_TIPO_EMISSAO_MCD" width="35%" required="true" />
		<adsm:combobox property="servico.idServico" optionLabelProperty="dsServico" optionProperty="idServico"
				service="lms.municipios.emitirMapaCodigosDistanciaAction.findComboServico"
				label="servico" width="35%" boxWidth="200" >
			<adsm:propertyMapping relatedProperty="servico.dsServico" modelProperty="dsServico" />
		</adsm:combobox>
		<adsm:hidden property="servico.dsServico" serializable="true" />
		
		<adsm:section caption="origem" />
		<adsm:lookup dataType="text" property="municipioFilialByIdMunicipioFilialOrigem.filial" idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.municipios.emitirMapaCodigosDistanciaAction.findLookupFilial" action="/municipios/manterFiliais"
    			label="filialResponsavel" exactMatch="true" size="3" maxLength="3" width="85%" cellStyle="vertical-Align:bottom" >
         	<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia" size="30" disabled="true" />
	    </adsm:lookup>
		
		<adsm:lookup dataType="text" idProperty="idMunicipioFilial" criteriaProperty="municipio.nmMunicipio"
				service="lms.municipios.emitirMapaCodigosDistanciaAction.findLookupMunicipio" property="municipioFilialByIdMunicipioFilialOrigem"
				label="municipio" size="38" maxLength="50" width="85%" serializable="false"
				action="/municipios/manterMunicipiosAtendidos" minLengthForAutoPopUpSearch="3" exactMatch="false" >
				
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.filial.idFilial" 
					modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.filial.sgFilial" 
					modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia" 
					modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" addChangeListener="false" inlineQuery="false"/>					
						
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.idUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="nmUnidadeFederativaOrigem" 
					modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" addChangeListener="false" inlineQuery="false" />
						
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.idFilial" 
					modelProperty="filial.idFilial" blankFill="false" />
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.sgFilial" 
					modelProperty="filial.sgFilial" blankFill="false" />
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia"
					modelProperty="filial.pessoa.nmFantasia" blankFill="false" />
			
			<adsm:propertyMapping criteriaProperty="idPaisOrigem" modelProperty="municipio.unidadeFederativa.pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="nmPaisOrigem" modelProperty="municipio.unidadeFederativa.pais.nmPais" inlineQuery="false" />
					
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio"
					modelProperty="municipio.idMunicipio"/>
			
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.idUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="nmUnidadeFederativaOrigem" 
					modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" blankFill="false" />
					
			<adsm:propertyMapping relatedProperty="idPaisOrigem" modelProperty="municipio.unidadeFederativa.pais.idPais" blankFill="false" />
			<adsm:propertyMapping relatedProperty="nmPaisOrigem" modelProperty="municipio.unidadeFederativa.pais.nmPais" blankFill="false" />
   		</adsm:lookup>
   		
   		<adsm:lookup property="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa"
   				idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa"
				service="lms.municipios.emitirMapaCodigosDistanciaAction.findLookupUf" dataType="text"
				label="uf" size="3" maxLength="3" width="35%"
				action="/municipios/manterUnidadesFederativas" exactMatch="false" >
    		<adsm:propertyMapping relatedProperty="nmUnidadeFederativaOrigem" modelProperty="nmUnidadeFederativa" />
    		<adsm:propertyMapping relatedProperty="idPaisOrigem" modelProperty="pais.idPais" />
    		<adsm:propertyMapping relatedProperty="nmPaisOrigem" modelProperty="pais.nmPais" />
    		<adsm:textbox dataType="text" property="nmUnidadeFederativaOrigem" size="30" disabled="true"/>
		</adsm:lookup>
		<adsm:combobox label="regional" 
					property="idRegionalOrigem" 
					service="lms.municipios.emitirMapaCodigosDistanciaAction.findRegionaisValidas" 
					optionProperty="idRegional" 
					optionLabelProperty="siglaDescricao"
					serializable="true"
					width="100%"/>
		<adsm:hidden property="idPaisOrigem" />
		<adsm:textbox dataType="text" property="nmPaisOrigem" 
				label="pais" width="35%" size="30" disabled="true"/>
				
		<adsm:hidden property="municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio"/>
        
                
        <adsm:section caption="destino" />
        <adsm:lookup dataType="text" property="municipioFilialByIdMunicipioFilialDestino.filial" idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.municipios.emitirMapaCodigosDistanciaAction.findLookupFilial" action="/municipios/manterFiliais"
    			label="filialResponsavel" exactMatch="true" size="3" maxLength="3" width="85%" cellStyle="vertical-Alignment:bottom" >
         	<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia" size="30" disabled="true" />
	    </adsm:lookup>
        
		<adsm:lookup dataType="text" idProperty="idMunicipioFilial" criteriaProperty="municipio.nmMunicipio"
				service="lms.municipios.emitirMapaCodigosDistanciaAction.findLookupMunicipio" property="municipioFilialByIdMunicipioFilialDestino"
				label="municipio" size="38" maxLength="50" width="85%" serializable="false"
				action="/municipios/manterMunicipiosAtendidos" minLengthForAutoPopUpSearch="3" exactMatch="false" >
				
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.filial.idFilial" 
					modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.filial.sgFilial" 
					modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia" 
					modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" addChangeListener="false" inlineQuery="false"/>					
						
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.idUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="nmUnidadeFederativaDestino" 
					modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" addChangeListener="false" inlineQuery="false" />
					
			<adsm:propertyMapping criteriaProperty="idPaisDestino" modelProperty="municipio.unidadeFederativa.pais.idPais"/>
			<adsm:propertyMapping criteriaProperty="nmPaisDestino" modelProperty="municipio.unidadeFederativa.pais.nmPais" inlineQuery="false" />

			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.idFilial" 
					modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.sgFilial" 
					modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia"
					modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
					
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio"
					modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.idUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="nmUnidadeFederativaDestino" 
					modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" blankFill="false" />
					
			<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="municipio.unidadeFederativa.pais.idPais" blankFill="false" />
			<adsm:propertyMapping relatedProperty="nmPaisDestino" modelProperty="municipio.unidadeFederativa.pais.nmPais" blankFill="false" />
   		</adsm:lookup>
   		
   		<adsm:lookup property="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa"
   				idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				service="lms.municipios.emitirMapaCodigosDistanciaAction.findLookupUf" dataType="text"
				label="uf" size="3" maxLength="3" width="35%"
				action="/municipios/manterUnidadesFederativas" exactMatch="false" >
    		<adsm:propertyMapping relatedProperty="nmUnidadeFederativaDestino" modelProperty="nmUnidadeFederativa" />
    		<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="pais.idPais" />
    		<adsm:propertyMapping relatedProperty="nmPaisDestino" modelProperty="pais.nmPais" />
    		<adsm:textbox dataType="text" property="nmUnidadeFederativaDestino" size="30" disabled="true"/>
		</adsm:lookup>
		<adsm:combobox label="regional" 
					property="idRegionalDestino" 
					service="lms.municipios.emitirMapaCodigosDistanciaAction.findRegionaisValidas" 
					optionProperty="idRegional" 
					optionLabelProperty="siglaDescricao"
					serializable="true"
					width="100%"/>
		<adsm:hidden property="idPaisDestino" />		
		<adsm:textbox dataType="text" property="nmPaisDestino" 
				label="pais" width="35%" size="30" disabled="true"/>
		
		<adsm:hidden property="municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio" />
		<adsm:textbox dataType="JTDate" property="dtVigencia" label="mcdVigenteEm" width="75%" required="true"/> 
		<adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirMapaCodigosDistanciaAction" />
			<adsm:resetButton />
		</adsm:buttonBar>
		<adsm:i18nLabels>
			<adsm:include key="LMS-46006"/>
		</adsm:i18nLabels>		
	</adsm:form>
</adsm:window>
<script>
<!--
	document.getElementById("municipioFilialByIdMunicipioFilialOrigem.municipio.nmMunicipio").serializable = true;
	document.getElementById("municipioFilialByIdMunicipioFilialDestino.municipio.nmMunicipio").serializable = true;
	document.getElementById("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa").serializable = true;
	document.getElementById("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa").serializable = true;
	document.getElementById("municipioFilialByIdMunicipioFilialOrigem.filial.sgFilial").serializable = true;
	document.getElementById("municipioFilialByIdMunicipioFilialDestino.filial.sgFilial").serializable = true;
	
	function pagaLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		getDataAtual();
	}
	
	var dataWithDate = undefined;
	
	function getDataAtual() {
		var sdo = createServiceDataObject("lms.municipios.emitirMapaCodigosDistanciaAction.getDataAtual",
				"getDataAtual",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function getDataAtual_cb(data,error) {
		dataWithDate = data;
		setNestedBeanPropertyValue(dataWithDate,"tpEmissao","D");
		preencheDadosDefault();
	}

	function preencheDadosDefault() {
		onDataLoad_cb(dataWithDate,undefined);
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			preencheDadosDefault();
		}
	}	 
	 
	function validateTab() {
		if (validateTabScript(document.forms)){
			if(getElementValue("municipioFilialByIdMunicipioFilialOrigem.filial.idFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialOrigem.idMunicipioFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.idUnidadeFederativa") == "" &&
					getElementValue("idRegionalOrigem") == "" &&
					getElementValue("idPaisOrigem") == ""){
				alert(i18NLabel.getLabel("LMS-46006"));
				return false;
			}					
			if(getElementValue("municipioFilialByIdMunicipioFilialDestino.filial.idFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialDestino.idMunicipioFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.idUnidadeFederativa") == "" &&
					getElementValue("idRegionalDestino") == "" &&
					getElementValue("idPaisDestino") == ""){
				alert(i18NLabel.getLabel("LMS-46006"));
				 	return false;
				}		
					return true;
				}	 		
			} 

//-->
</script>