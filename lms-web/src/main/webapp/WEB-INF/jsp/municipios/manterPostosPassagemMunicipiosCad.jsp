<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.postoPassagemMunicipioService">
	<adsm:form action="/municipios/manterPostosPassagemMunicipios" idProperty="idPostoPassagemMunicipio" service="lms.municipios.postoPassagemMunicipioService.findByIdDetalhamento" onDataLoadCallBack="pageLoad">
	    
		<adsm:lookup dataType="text" property="municipioFilial.filial" idProperty="idFilial" criteriaProperty="sgFilial" required="true"
    			service="lms.municipios.filialService.findLookup" action="/municipios/manterFiliais" serializable="false"
    			size="3" maxLength="3" 
				label="filialResponsavel" labelWidth="17%" width="75%" minLengthForAutoPopUpSearch="3" exactMatch="true" >
         	<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:propertyMapping relatedProperty="municipioFilial.municipio.nmMunicipio" modelProperty="blank" />
     		<adsm:propertyMapping relatedProperty="municipioFilial.idMunicipioFilial" modelProperty="blank"/>
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" serializable="false"/>
        </adsm:lookup>

        
		<adsm:lookup service="lms.municipios.municipioFilialService.findLookup" labelWidth="17%" dataType="text" property="municipioFilial"
					criteriaProperty="municipio.nmMunicipio" idProperty="idMunicipioFilial" label="municipioAtendido" size="35" maxLength="50" width="33%"
					action="/municipios/manterMunicipiosAtendidos" minLengthForAutoPopUpSearch="2" exactMatch="false" cellStyle="vertical-align:bottom;" required="true">
					<adsm:propertyMapping criteriaProperty="municipioFilial.filial.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
					<adsm:propertyMapping criteriaProperty="municipioFilial.filial.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.filial.idFilial" modelProperty="filial.idFilial"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.filial.sgFilial" modelProperty="filial.sgFilial"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
		</adsm:lookup>
   		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" serializable="false" labelWidth="8%" width="16%" disabled="true"/>
		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" dataType="text" label="pais" serializable="false" width="16%" labelWidth="8%" disabled="true"/>  
		
		<adsm:hidden property="flag" value="01"/>
		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>
        		
		<adsm:range label="vigencia" labelWidth="17%" width="83%">
        	<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
            <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
		<adsm:section caption="informacaoPostoPassagem"/>
		<adsm:lookup dataType="text" property="postoPassagem" idProperty="idPostoPassagem" criteriaProperty="tpPostoPassagem.description" onchange="return changeLookup();"
        		action="/municipios/manterPostosPassagem" service="lms.municipios.postoPassagemService.findLookupByFormaCobranca" onPopupSetValue="popUpLookup"
        		size="30" minLengthForAutoPopUpSearch="3" exactMatch="false" onDataLoadCallBack="dataLoadLookup"
        		label="tipoPostoPassagem" cellStyle="vertical-align:bottom;" labelWidth="17%" width="75%" required="true">
        	<adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.tpSentidoCobranca.description" modelProperty="tpSentidoCobranca.description" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" /> 
        	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" modelProperty="concessionaria.pessoa.nrIdentificacaoFormatado"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nmPessoa" modelProperty="concessionaria.pessoa.nmPessoa"/>
	    </adsm:lookup>
	    <adsm:textbox dataType="text" cellStyle="vertical-align:bottom;"  property="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" size="18" label="concessionaria" labelWidth="17%"  disabled="true" width="15%" serializable="false"/>
		<adsm:textbox dataType="text" cellStyle="vertical-align:bottom;" property="postoPassagem.concessionaria.pessoa.nmPessoa" size="50" disabled="true" width="50%" serializable="false"/>
	    
		<adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio" serializable="false" label="localizacao" size="30" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca.description" serializable="false" label="sentido" size="30" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia" serializable="false" label="rodovia" size="15" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.nrKm" label="km" serializable="false" size="15" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>
		<adsm:listbox optionLabelProperty="text" optionProperty="" property="valor" serializable="false" size="7" boxWidth="575" labelWidth="17%" width="83%" label="valor"/>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.postoPassagemMunicipioService.storeMap" />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--

	function pageLoad_cb(data,error) {
		enabledFields();
		onDataLoad_cb(data,error);
		acaoVigencia(data);
		setDisabled("valor",false);
		loadList();
	}
	
	
	function afterStore_cb(data,exception) {
		store_cb(data,exception);
		if (exception == undefined) {
			acaoVigencia(data);
			setFocus(document.getElementById("__buttonBar:0.newButton"),false);
		}
	}
	
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
			  setDisabled("__buttonBar:0.removeButton",false);
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.storeButton",false);
		      setDisabled("__buttonBar:0.removeButton",true);
		      setDisabled("__buttonBar:0.newButton",false);
		      setDisabled("dtVigenciaFinal",false);
		      setFocus(document.getElementById("dtVigenciaFinal"));
		} else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.newButton",false);
		      setDisabled("__buttonBar:0.removeButton",true);
		      setFocus(document.getElementById("__buttonBar:0.newButton"),false);
		}
	}	
	
	function enabledFields() {
		setDisabled(document,false);
		setDisabled("postoPassagem.municipio.nmMunicipio",true);
		setDisabled("municipioFilial.filial.idFilial",document.getElementById("municipioFilial.filial.idFilial").masterLink == "true");
		setDisabled("municipioFilial.idMunicipioFilial",document.getElementById("municipioFilial.idMunicipioFilial").masterLink == "true");
		setDisabled("municipioFilial.filial.pessoa.nmFantasia",true);
		setDisabled("postoPassagem.tpSentidoCobranca.description",true);
		setDisabled("postoPassagem.rodovia.sgRodovia",true);
		setDisabled("postoPassagem.nrKm",true);
		setDisabled("postoPassagem.nrKm",true);
		setDisabled("postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado",true);
		setDisabled("postoPassagem.concessionaria.pessoa.nmPessoa",true);
		setDisabled("postoPassagem.idPostoPassagem",false);
		
		setDisabled("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa",true);
		setDisabled("municipioFilial.municipio.unidadeFederativa.pais.nmPais",true);
		
		setDisabled("valor",false);
		setDisabled("__buttonBar:0.removeButton",true);
		document.getElementById("postoPassagem.tpPostoPassagem.description").disabled = true;
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click"))
			enabledFields();
	}
	
	///DAQUI PRA BAIXO É SO PARA A LIST
		function loadList() {
			var sdo = createServiceDataObject("lms.municipios.postoPassagemService.findValoresPostosPassagemList","resultLoad",{idPostoPassagem:getElementValue("postoPassagem.idPostoPassagem")});
			xmit({serviceDataObjects:[sdo]});
		}
		function clearList() {
			for(x = document.getElementById("valor").options.length; x >= 0; x--)
				document.getElementById("valor").options[x] = null;
		}
		function resultLoad_cb(data,exception) {
			if (exception != undefined) {
				clearList();
				return;
			}
			clearList();
			for(x = 0; x < data.length; x++)
				document.getElementById("valor").options[document.getElementById("valor").options.length] = 
					new Option(data[x].text,"",true);
			//valor_cb(data,exception);
		}
		//change lookup
		function changeLookup() {
			if (getElementValue("postoPassagem.tpPostoPassagem.description") == "")
				clearList();
			return postoPassagem_tpPostoPassagem_descriptionOnChangeHandler();
		}
		//CallBack
		function dataLoadLookup_cb(data,exception) {
			var temp = getElementValue("postoPassagem.idPostoPassagem");
			var flag = postoPassagem_tpPostoPassagem_description_exactMatch_cb(data);
			if (getElementValue("postoPassagem.idPostoPassagem") != temp)
				loadList();
		}
		//PopUp
		function popUpLookup(data) {
			if (getElementValue("postoPassagem.idPostoPassagem") != data.idPostoPassagem) {
				setElementValue("postoPassagem.idPostoPassagem",data.idPostoPassagem);
				loadList();			
			}
			return true;
		}
//-->
</script>