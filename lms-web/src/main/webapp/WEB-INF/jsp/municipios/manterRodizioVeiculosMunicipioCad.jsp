<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

//funcao chamada no call back do form - recebe os flags definindo se os campos ficarao habilitados ou desabilitados no detalhamento, validacao conforme as datas de vigencia
function pageLoad_cb(data,exception) {
  onDataLoad_cb(data,exception);  
  var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  validaAcaoVigencia(acaoVigenciaAtual, null);
}
 
function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
 if(acaoVigenciaAtual == 0){
  	 setDisabled(document, false);
     setDisabled("botaoExcluir",false);
	 setDisabled("municipio.idMunicipio",document.getElementById("municipio.idMunicipio").masterLink == "true");
     setDisabled("municipio.unidadeFederativa.sgUnidadeFederativa",true);
	 setDisabled("municipio.unidadeFederativa.nmUnidadeFederativa",true);
     setDisabled("municipio.unidadeFederativa.pais.nmPais",true);
     if(tipoEvento == "" ||  tipoEvento == null)
    	setFocusOnFirstFocusableField(document);
     else
    	setFocus(document.getElementById("botaoNovo"),false);
  }else if(acaoVigenciaAtual == 1) {
     setDisabled(document,true);
     setDisabled("dtVigenciaFinal", false);
     setDisabled("botaoNovo",false);
     setDisabled("botaoSalvar",false);
     if(tipoEvento == "" ||  tipoEvento == null)
    	setFocusOnFirstFocusableField(document);
     else
    	setFocus(document.getElementById("botaoNovo"),false);
   }else if(acaoVigenciaAtual == 2) {
     setDisabled(document,true);
     setDisabled("botaoNovo",false);
     setFocus(document.getElementById("botaoNovo"),false);
  }
}

	function afterStore_cb(data,exception,key) {
		store_cb(data,exception,key);
		if (exception == undefined){
	     	var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
	     	var store = "true";
		 	validaAcaoVigencia(acaoVigenciaAtual, store);
		}
	}

// ############################################################
// tratamento dos eventos da initWindow para <tab_click>, 
// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
// ############################################################
function initWindow(eventObj) {
    if (eventObj.name == "tab_click") {
		setDisabled(document,false);
		setDisabled("botaoExcluir",true);
	 	setDisabled("municipio.idMunicipio",document.getElementById("municipio.idMunicipio").masterLink == "true");	
		setDisabled("municipio.unidadeFederativa.sgUnidadeFederativa",true);
		setDisabled("municipio.unidadeFederativa.nmUnidadeFederativa",true);
        setDisabled("municipio.unidadeFederativa.pais.nmPais",true);
		setFocusOnFirstFocusableField(document);
 	} else if (eventObj.name == "newButton_click" || eventObj.name == "removeButton_click") {
	 	habilitaCampos();
 	}
}


//nova function do button novo


	// tambem nao eh aqui
	function habilitaCampos(){
		setDisabled(document,false);
		setDisabled("botaoExcluir",true);
        setDisabled("municipio.idMunicipio",document.getElementById("municipio.idMunicipio").masterLink == "true");		 			 
		setDisabled("municipio.unidadeFederativa.sgUnidadeFederativa",true);
		setDisabled("municipio.unidadeFederativa.nmUnidadeFederativa",true);
		setDisabled("municipio.unidadeFederativa.pais.nmPais",true);
		setFocusOnFirstFocusableField(document);
}

	function onRemoveButtonClick(eThis) {
		removeButtonScript('lms.municipios.rodizioVeiculoMunicipioService.removeById', 'removeById', 'idRodizioVeiculoMunicipio', eThis.document);
		setDisabled("botaoNovo", false);
	}
</script>
<adsm:window service="lms.municipios.rodizioVeiculoMunicipioService">
	<adsm:form action="/municipios/manterRodizioVeiculosMunicipio" idProperty="idRodizioVeiculoMunicipio" onDataLoadCallBack="pageLoad" service="lms.municipios.rodizioVeiculoMunicipioService.findByIdEValidaVigencia">
		
		<adsm:lookup 
			service="lms.municipios.municipioService.findLookup" 
			dataType="text" 
			property="municipio"
			criteriaProperty="nmMunicipio" 
			idProperty="idMunicipio" 
			label="municipio" 
			size="30" 
			maxLength="30" 
			width="85%"
			action="/municipios/manterMunicipios" 
			minLengthForAutoPopUpSearch="3" 
			required="true"
			exactMatch="false">
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais"/>
		</adsm:lookup>
				
		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.pais.nmPais" label="pais" required="false" disabled="true" serializable="false" />
	
		<adsm:section caption="configuracaoRodizio" />

		<adsm:combobox property="diaSemana" label="diaSemana" required="true" domain="DM_DIAS_SEMANA"/>
		
		<adsm:textbox dataType="integer" property="nrFinalPlaca" label="finalPlaca" maxLength="1" size="3" required="true"/>

		<adsm:range label="horario">
			<adsm:textbox dataType="JTTime" property="hrRodizioInicial" />
			<adsm:textbox dataType="JTTime" property="hrRodizioFinal"/>
		</adsm:range>

		<adsm:range label="vigencia" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
		    <adsm:storeButton id="botaoSalvar" callbackProperty="afterStore" service="lms.municipios.rodizioVeiculoMunicipioService.storeMap" />
			<adsm:newButton id="botaoNovo"/>
			<adsm:button caption="excluir" id="botaoExcluir" onclick="onRemoveButtonClick(this);" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
