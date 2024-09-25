 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
 <script>


	// UF
	function setaAbrangenciaUFExact_cb(data){
		lookupExactMatch({e:document.getElementById("unidadeFederativa.idUnidadeFederativa"), data:data, callBack:"setaAbrangenciaUFLikeEnd"});
        preparaDadosAbrangencia(data);        
        resetValue("municipio.nmMunicipio");
        resetValue("municipio.idMunicipio");
        
    }
    
    function setaAbrangenciaUFLikeEnd_cb(data){
		unidadeFederativa_sgUnidadeFederativa_likeEndMatch_cb(data);
        preparaDadosAbrangencia(data);
        resetValue("municipio.nmMunicipio");
        resetValue("municipio.idMunicipio");
    }
    
    
	function verificaOnchangeUF(){
			
		var idPais = getElementValue("pais.idPais");
		var nmPais = getElementValue("pais.nmPais");
				
		var flag = unidadeFederativa_sgUnidadeFederativaOnChangeHandler();
		
		if (document.forms[0].elements["unidadeFederativa.sgUnidadeFederativa"].value == ''){
			
			setElementValue("pais.nmPais", nmPais);
			setElementValue("pais.idPais" ,idPais);
			setElementValue("municipio.nmMunicipio", "");
			setElementValue("municipio.idMunicipio", "");
			setaAbrangencia();
		}
		return flag;
	}	
	
	function setaAbrangenciaOnPopupUF(data) {

	   if (data == undefined)
	      return;

	   var idMunicipio = getElementValue("municipio.idMunicipio");
       var idUF = getNestedBeanPropertyValue(data, "idUnidadeFederativa");
	   var idPais = getNestedBeanPropertyValue(data, "idPais");
	
       setaAbrangencia(idMunicipio, idUF, idPais);
	}
	//-------------------------------------------------------
    
	// Pais
	function setaAbrangenciaPaisExact_cb(data){
         lookupExactMatch({e:document.getElementById("pais.idPais"), data:data, callBack:"setaAbrangenciaPaisLikeEnd"});
        
         preparaDadosAbrangencia(data);
    }
    
	function setaAbrangenciaPaisLikeEnd_cb(data){
		 pais_nmPais_likeEndMatch_cb(data);
		 
         preparaDadosAbrangencia(data);
    }
    
    function verificaOnchangePais(){
				
		var flag = pais_nmPaisOnChangeHandler();
		
		if (getElementValue("pais.nmPais") == ''){
			
			resetValue("unidadeFederativa.idUnidadeFederativa");
			resetValue("unidadeFederativa.nmUnidadeFederativa");
			resetValue("unidadeFederativa.sgUnidadeFederativa");
			resetValue("municipio.nmMunicipio");
			resetValue("municipio.idMunicipio");		
		  	
		  	setaAbrangencia();
		}
		return flag;
	}
	
	function setaAbrangenciaOnPopupPais(data) {

	   if (data == undefined)
	      return;
	      
	   var idMunicipio = getElementValue("municipio.idMunicipio");;
	   var idUF = getElementValue("unidadeFederativa.idUnidadeFederativa");
	   var idPais = getNestedBeanPropertyValue(data, "idPais");
	
       setaAbrangencia(idMunicipio, idUF, idPais);
	}
    
	//-------------------------------------------------------
    

	// Municipio
	function setaAbrangenciaMunicipioExact_cb(data){
	     var r = lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:"setaAbrangenciaMunicipioLikeEnd"});
         preparaDadosAbrangencia(data);
         
         return r;
	}
	
	function setaAbrangenciaMunicipioLikeEnd_cb(data) {
	
	   var r = municipio_nmMunicipio_likeEndMatch_cb(data);
	   preparaDadosAbrangencia(data);
	   
	   return r;
	}
	
	function verificaOnchangeMunicipio(){
	   	
		var nmPais = getElementValue("pais.nmPais");
		var idPais = getElementValue("pais.idPais");
		
		var idUnidadeFederativa = getElementValue("unidadeFederativa.idUnidadeFederativa");
		var nmUnidadeFederativa = getElementValue("unidadeFederativa.nmUnidadeFederativa");
		var sgUnidadeFederativa = getElementValue("unidadeFederativa.sgUnidadeFederativa");
		
		var r = municipio_nmMunicipioOnChangeHandler();
		
		if (getElementValue("municipio.nmMunicipio") == ''){
			
		
			setElementValue("pais.nmPais", nmPais);
			setElementValue("pais.idPais", idPais);
			
			setElementValue("unidadeFederativa.idUnidadeFederativa", idUnidadeFederativa);
		  	setElementValue("unidadeFederativa.nmUnidadeFederativa", nmUnidadeFederativa);
		  	setElementValue("unidadeFederativa.sgUnidadeFederativa", sgUnidadeFederativa);
		  	
		  	setaAbrangencia();
		}
		
		return r;
	}	
	
	function setaAbrangenciaOnPopupMunicipio(data) {

	   if (data == undefined)
	      return;

       var idMunicipio = getNestedBeanPropertyValue(data, "idMunicipio");
       var idUF = getNestedBeanPropertyValue(data, "unidadeFederativa.idUnidadeFederativa");
	   var idPais = getNestedBeanPropertyValue(data, "unidadeFederativa.pais.idPais");
	
       setaAbrangencia(idMunicipio, idUF, idPais);
	}
	
	//-------------------------------------------------------
	
	// Abrangencia
	function preparaDadosAbrangencia(data) {

	   if (data == undefined || data.length == undefined || data.length == 0)
	      return;

	   var idMunicipio = getNestedBeanPropertyValue(data, ":0.idMunicipio");
       var idUF = getNestedBeanPropertyValue(data, ":0.unidadeFederativa.idUnidadeFederativa");
	   var idPais = getNestedBeanPropertyValue(data, ":0.unidadeFederativa.pais.idPais");
		
       setaAbrangencia(idMunicipio, idUF, idPais);
	}
	

	function setaAbrangencia(idMunicipio, idUF, idPais){
		
		var _idMunicipio;
		var _idUF;
		var _idPais;
		
		if (idMunicipio == undefined && idUF == undefined && idPais == undefined) {
		
		   	_idMunicipio = getElementValue("municipio.idMunicipio");
	    	_idUF = getElementValue("unidadeFederativa.idUnidadeFederativa");
    		_idPais = getElementValue("pais.idPais");
		} else {
			_idMunicipio = idMunicipio;
			_idUF = idUF;
			_idPais = idPais;
		}		
	
		if (_idMunicipio != undefined && _idMunicipio != ''){
			setElementValue('abrangencia', 'Municipal');		
		} else if (_idUF != undefined && _idUF != '') {
			setElementValue('abrangencia', 'Estadual');
		} else if (_idPais != undefined && _idPais != ''){
			setElementValue('abrangencia', 'Nacional');
		} else {
			setElementValue('abrangencia', 'Mundial');
		}
	}
	//-------------------------------------------------------
	
	function novo(){
		setDisabled("dtFeriado", false);
		setDisabled("dsFeriado", false);
		setDisabled("tpferiado", false);
		setDisabled("blFacultativo", false);
		setDisabled("dtVigenciaFinal", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("municipio.idMunicipio", document.getElementById("municipio.idMunicipio").masterLink == "true");
		setDisabled("unidadeFederativa.idUnidadeFederativa", document.getElementById("unidadeFederativa.idUnidadeFederativa").masterLink == "true");
		setDisabled("pais.idPais", document.getElementById("pais.idPais").masterLink == "true");		
		setDisabled("__buttonBar:0.storeButton", false);	
	}
	
	function verificaVigencia(dados){
		var acaoVigenciaAtual = getNestedBeanPropertyValue(dados, "acaoVigenciaAtual");

		if (acaoVigenciaAtual == 0){
			novo();						
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("dtVigenciaFinal", false);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("__buttonBar:0.storeButton", false);
		} else if (acaoVigenciaAtual == 2){
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setFocusOnNewButton();
		}
	}
	
	
	function verificaMasterLink(){
		return (document.forms[0].elements["municipio.idMunicipio"].masterLink == "true" ||
			  	document.forms[0].elements["unidadeFederativa.idUnidadeFederativa"].masterLink == "true" ||
    			document.forms[0].elements["pais.idPais"].masterLink == "true")
	}
	
	function initWindow(eventObj) {
	    
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton") {	
			if (!verificaMasterLink())
			    setElementValue("abrangencia", "Mundial");	
			else
				setaAbrangencia();
			novo();
			
			if(document.getElementById("unidadeFederativa.idUnidadeFederativa").masterLink == "true"){
				setDisabled("unidadeFederativa.idUnidadeFederativa",true);
				setDisabled("pais.idPais",true);
			}
			setFocusOnFirstFocusableField();
		} 
			
    }
    
    function feriado_dataLoad_cb(dados, erros){
    	onDataLoad_cb(dados, erros); 
    	
    	setaAbrangencia();
    	verificaVigencia(dados);
    	var acaoVigenciaAtual = getNestedBeanPropertyValue(dados, "acaoVigenciaAtual");
    	if (acaoVigenciaAtual == 2)
    		setFocusOnNewButton();
    	else
	    	setFocusOnFirstFocusableField();
    }
    
    
    function verificaConfirmacao_cb(data, erros, errorKey){
		var showError= (errorKey != "LMS-29032");
	    store_cb(data, erros, errorKey, showError);
    	
    	if (errorKey == "LMS-29032" && confirm(erros)) {    		
    	    document.forms[0].elements["verificaFeriadoExistente"].value = "N";
    	    storeButtonScript('lms.municipios.feriadoService.store', "verificaConfirmacao", document.getElementById('form_idFeriado'));
    	} 
    	
    	if (erros == undefined){
	    	verificaVigencia(data);
	    	setFocusOnNewButton();
    	} 		
    }
    
  
</script>
<adsm:window service="lms.municipios.feriadoService" > 
	<adsm:form idProperty="idFeriado" action="/municipios/manterFeriados" service="lms.municipios.feriadoService.findByIdDetalhamento" onDataLoadCallBack="feriado_dataLoad">
		<adsm:hidden serializable="true" property="verificaFeriadoExistente" value="S"/>
		<adsm:hidden serializable="false" property="tpSituacaoPesquisa" value="A"/>
		<adsm:lookup onchange="return verificaOnchangeUF()" onDataLoadCallBack="setaAbrangenciaUFExact" property="unidadeFederativa"  
					idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				 	service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					width="7%" label="uf" size="2" maxLength="2" onPopupSetValue="setaAbrangenciaOnPopupUF"
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping criteriaProperty="tpSituacaoPesquisa" modelProperty="tpSituacao"/>
			<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="pais.nmPais" blankFill="false" />
			<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="pais.idPais" blankFill="false"/>
		</adsm:lookup>

		<adsm:textbox maxLength="60" dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="28%" size="19" serializable="false" disabled="true"/>
		
		<adsm:lookup onchange="return verificaOnchangePais()" onDataLoadCallBack="setaAbrangenciaPaisExact"
			         property="pais" idProperty="idPais" criteriaProperty="nmPais" onPopupSetValue="setaAbrangenciaOnPopupPais"
					 service="lms.municipios.paisService.findLookup" dataType="text" 
			         label="pais" size="30" minLengthForAutoPopUpSearch="3" maxLength="60" exactMatch="false"
			         action="/municipios/manterPaises" >
			<adsm:propertyMapping criteriaProperty="tpSituacaoPesquisa" modelProperty="tpSituacao"/>         
			<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativas.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.idMunicipio" modelProperty="unidadeFederativas.municipios.idMunicipio" addChangeListener="false"/>
   		</adsm:lookup>

		<adsm:lookup onchange="return verificaOnchangeMunicipio();" onDataLoadCallBack="setaAbrangenciaMunicipioExact"
					 property="municipio" criteriaProperty="nmMunicipio" idProperty="idMunicipio" dataType="text" 
					 service="lms.municipios.municipioService.findLookup" onPopupSetValue="setaAbrangenciaOnPopupMunicipio"
					 label="municipio" size="30" action="/municipios/manterMunicipios" 
					 exactMatch="false" minLengthForAutoPopUpSearch="3" maxLength="60">
				<adsm:propertyMapping criteriaProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais"/>	
				<adsm:propertyMapping criteriaProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" inlineQuery="false"/> 	 
				<adsm:propertyMapping criteriaProperty="tpSituacaoPesquisa" modelProperty="tpSituacao"/>	 				
				<adsm:propertyMapping criteriaProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
	 			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="pais.idPais" modelProperty="unidadeFederativa.pais.idPais" blankFill="false"/>
		 </adsm:lookup>
		
		<adsm:textbox dataType="JTDate" property="dtFeriado" mask="dd/MM" label="diaMes" size="6" required="true" labelWidth="15%" width="25%" picker="false"/>
		
		<adsm:textbox dataType="text" property="dsFeriado" label="descricao" size="30" maxLength="60" />
		
		<adsm:combobox property="tpFeriado" label="tipo" domain="DM_TIPO_FERIADO" required="true"/>
		
		<adsm:checkbox property="blFacultativo" label="facultativo" />
		
		<adsm:textbox  property="abrangencia" disabled="true" dataType="text" label="abrangencia" size="30" serializable="true"/>
		
		<adsm:range label="vigencia" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
 		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="verificaConfirmacao"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>