<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function desabilitaLookupPostoPassagem() {
   onPageLoad();
   setDisabled("postoPassagem.idPostoPassagem", false);
   setDisabled("postoPassagem.tpPostoPassagem.description", true);
}


//funcao que habilita ou desabilita campos cfme as vigencias
function habilitaDesabilitaCfmeVigencias_cb(data,exception){
	onDataLoad_cb(data,exception);
	loadList();
	var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
	if(acaoVigenciaAtual != null)
    	validaAcaoVigencia(acaoVigenciaAtual, null);
    //para trazer os valores do pedagio
  	
  	
  	
  	
}

function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
	if(acaoVigenciaAtual == 0){
	  setDisabled("dtVigenciaInicial", false);
      setDisabled("dtVigenciaFinal", false);
      setDisabled("postoPassagem.idPostoPassagem", false);
      setDisabled("postoPassagem.tpPostoPassagem.description",true);
      setDisabled("valor", true);
      setDisabled("botaoExcluir",false);
      if(tipoEvento == "" ||  tipoEvento == null)
     	setFocus(document.getElementById("filialOrigem.sgFilial"));
      else
       setFocus(document.getElementById("botaoNovo"), false);
    }else if(acaoVigenciaAtual == 1) {
      setDisabled(document,true);
      setDisabled("dtVigenciaFinal", false);
      setDisabled("botaoNovo",false);
      setDisabled("botaoSalvar",false);
      setDisabled("botaoExcluir",true);
      if(tipoEvento == "" ||  tipoEvento == null)
     	setFocus(document.getElementById("dtVigenciaFinal"));
      else
        setFocus(document.getElementById("botaoNovo"), false);
    }else if(acaoVigenciaAtual == 2){
  	 setDisabled(document,true);
     setDisabled("botaoNovo",false);
     setDisabled("botaoExcluir",true);
     setFocus(document.getElementById("botaoNovo"),false);
    
   }
}

function initWindow(eventObject){
	if(eventObject.name == "tab_click" || eventObject.name == "removeButton" || eventObject.name == "newButton_click"){
		setDisabled("filialOrigem.idFilial",false);
		setDisabled("filialDestino.idFilial",false);
		setDisabled("postoPassagem.idPostoPassagem", false);
	    setDisabled("postoPassagem.tpPostoPassagem.description", true);
	   	setDisabled("dtVigenciaInicial", false);
	    setDisabled("dtVigenciaFinal", false);
	    setDisabled("botaoNovo",false);
	    setDisabled("botaoExcluir",true);
	    setFocus(document.getElementById("filialOrigem.sgFilial"));
	    findFilialUsuario();
	    
	}
	
}



function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
    	if (exception == undefined) {
			var store = true;
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			validaAcaoVigencia(acaoVigenciaAtual, store);
		}else{	
			setDisabled("postoPassagem.idPostoPassagem", false);
     		setDisabled("postoPassagem.tpPostoPassagem.description",true);
     		setDisabled("botaoNovo",false);
     	}
}	


</script>
<adsm:window service="lms.municipios.manterPostoPassagemTrechoAction" onPageLoad="desabilitaLookupPostoPassagem" >
	<adsm:form action="/municipios/manterPostosPassagemRotas" idProperty="idPostoPassagemTrecho" onDataLoadCallBack="habilitaDesabilitaCfmeVigencias" service="lms.municipios.manterPostoPassagemTrechoAction.findByIdEValidaDtVigencia">
	    
	    <adsm:hidden property="tpEmpresaMercurioValue" value="M" serializable="false" />
	    
	   	<adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filialOrigem" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.municipios.manterPostoPassagemTrechoAction.findLookupFilial" 
	        label="filialOrigem" labelWidth="17%" size="3" width="33%" maxLength="3" required="true">
	        
	        <adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
        <adsm:hidden property="tpAcesso" value="A" serializable="false"/>
        <adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filialDestino" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.municipios.manterPostoPassagemTrechoAction.findLookupFilial" 
	        label="filialDestino" labelWidth="17%" size="3" width="33%" maxLength="3" >
	        
	     	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
	        <adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
		
		<adsm:lookup service="lms.municipios.manterPostoPassagemTrechoAction.findLookupByFormaCobranca" onDataLoadCallBack="dataLoadLookup" onPopupSetValue="popUpLookup" onchange="return changeLookup();" dataType="text" property="postoPassagem" idProperty="idPostoPassagem" criteriaProperty="tpPostoPassagem.description"  labelWidth="17%"  label="postoPassagem" size="35" maxLength="30" width="85%" action="/municipios/manterPostosPassagem" exactMatch="false" minLengthForAutoPopUpSearch="3" required="true" disabled="true">
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
	        <adsm:propertyMapping relatedProperty="postoPassagem.municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.tpSentidoCobranca.description" modelProperty="tpSentidoCobranca.description" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nmPessoa" modelProperty="concessionaria.pessoa.nmPessoa"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" modelProperty="concessionaria.pessoa.nrIdentificacaoFormatado"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" />
	    </adsm:lookup>
	    
	    <adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" size="18" label="concessionaria" labelWidth="17%"  disabled="true" width="15%" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nmPessoa" size="45" disabled="true" width="68%" serializable="false"/>
		
		<adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio" label="localizacao" size="35" maxLength="35" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca.description" label="sentido" size="35" maxLength="35" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="text" property="postoPassagem.municipio.unidadeFederativa.sgUnidadeFederativa" label="uf" size="3" maxLength="2" labelWidth="17%" width="5%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.municipio.unidadeFederativa.nmUnidadeFederativa"  size="27" maxLength="2" width="28%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="text" property="postoPassagem.municipio.unidadeFederativa.pais.nmPais" label="pais" size="35" maxLength="35" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia" label="rodovia" size="35" maxLength="35" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="integer" mask="##,###.#" property="postoPassagem.nrKm" label="km" size="10" maxLength="10" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		
		
		<adsm:listbox 
			labelWidth="17%" 
			property="valor" 
			width="83%" size="7" label="valorPostoPassagem" 
			optionLabelProperty="text" 
			style="width:205px" 
			optionProperty="" 
			serializable="false" boxWidth="575"/>
		
			
		<adsm:range label="vigencia" labelWidth="17%" width="83%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
	
	<adsm:buttonBar>
		<adsm:storeButton id="botaoSalvar" callbackProperty="afterStore"/>
		<adsm:newButton id="botaoNovo" />
		<adsm:removeButton id="botaoExcluir"/>
	</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

document.getElementById("tpEmpresaMercurioValue").masterLink = true;

function findFilialUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.municipios.manterPostoPassagemTrechoAction.findFilialUsuarioLogado", "onDataLoadFilialUsuario", new Array()));
	  	xmit();
}

function onDataLoadFilialUsuario_cb(data, exception){
	var sgFilial = getNestedBeanPropertyValue(data,"filialOrigem.sgFilial");
	var idFilial = getNestedBeanPropertyValue(data,"filialOrigem.idFilial");
	var nmFantasia = getNestedBeanPropertyValue(data,"filialOrigem.pessoa.nmFantasia");
	
	setElementValue("filialOrigem.sgFilial",sgFilial);
	setElementValue("filialOrigem.idFilial",idFilial);
	setElementValue("filialOrigem.pessoa.nmFantasia",nmFantasia);
}

///DAQUI PRA BAIXO É SO PARA A LIST
		function loadList() {
			var sdo = createServiceDataObject("lms.municipios.manterPostoPassagemTrechoAction.findValoresPostosPassagemList","resultLoad",{idPostoPassagem:getElementValue("postoPassagem.idPostoPassagem")});
			xmit({serviceDataObjects:[sdo]});
		}
		function clearList() {
			for(x = document.getElementById("valor").options.length; x >= 0; x--)
				document.getElementById("valor").options[x] = null;
		}
		function resultLoad_cb(data,exception) {
			if (exception != undefined) {
				clearList();
				alert(exception);
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

</script>