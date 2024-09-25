<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function habilitaDesabilitaCfmeVigencias_cb(data,exception){
  onDataLoad_cb(data,exception);
  //coloca na combo uma regional nao vigente
  var regional = document.getElementById("regional.idRegional");
  var idRegional = getNestedBeanPropertyValue(data, "regional.idRegional");
  var dsRegional = getNestedBeanPropertyValue(data, "regional.dsRegional");
  setElementValue(regional, idRegional);
  if(regional.value == "") {
       setComboBoxElementValue(regional, idRegional, idRegional, dsRegional);
	   setElementValue(regional, idRegional);
  }
  var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  validaAcaoVigencia(acaoVigenciaAtual, null);
  
} 

function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
  if(acaoVigenciaAtual == 0){
     setDisabled(document, false);
     if(document.getElementById("regional.idRegional").masterLink == "true" ) {
     	setDisabled("regional.idRegional", true);
     } 
     if(document.getElementById("filial.idFilial").masterLink == "true" ) {
     	setDisabled("filial.idFilial", true);     
     }
     setDisabled("regional.dtVigenciaInicial", true);
   	 setDisabled("regional.dtVigenciaFinal", true);
     setDisabled("filial.idFilial",document.getElementById("filial.idFilial").masterLink == "true");
     setDisabled("filial.pessoa.nmFantasia",true);
 	 setDisabled("regional.usuario.nmUsuario",true);
     setDisabled("botaoExcluir",false);
     if(tipoEvento == "" ||  tipoEvento == null)
     	setFocusOnFirstFocusableField(document);
     else
       setFocusOnNewButton(document);
  }else if(acaoVigenciaAtual == 1) {
      setDisabled(document,true);
	  setDisabled("botaoNovo",false);
      setDisabled("botaoSalvar",false);
      setDisabled("dtVigenciaFinal", false);
      if(tipoEvento == "" ||  tipoEvento == null)
     	setFocusOnFirstFocusableField(document);
     else
       setFocusOnNewButton(document);
   }else if(acaoVigenciaAtual == 2) {
  	 setDisabled(document,true);
     setDisabled("botaoNovo",false);
     setFocusOnNewButton(document);
   }

}
 
//nova function do button novo
function habilitaCampos() {
	setDisabled(document, true);
	if (getElement("regional.idRegional").masterLink != "true") {
		setDisabled("regional.idRegional", false);
	}
	if (getElement("filial.idFilial").masterLink != "true") {
		setDisabled("filial.idFilial", false);
	}
	setDisabled("regional.idRegional", false);
	setDisabled("filial.idFilial", false);
	setDisabled("filial.sgFilial", false);
	setDisabled("blSedeRegional", false);
	setDisabled("dtVigenciaInicial", false);
	setDisabled("dtVigenciaFinal", false);
	setDisabled("botaoNovo", false);
	setDisabled("botaoSalvar", false);
	setFocusOnFirstFocusableField(document);
}


//é executada qdo clica na aba detalhamento
function initWindow(eventObj) {
	
    if (eventObj.name == "tab_click") {
		habilitaCampos();
 	}
 	
 	if(eventObj.name=="newButton_click"){
 		habilitaCampos();
	}
}

//vem do linkProperty de regional - chamada no callback da window
function setaVigenciaInativaComboRegional_cb(data,error){
	  onPageLoad_cb(data,error);
	  if(document.getElementById("idRegionalF").value != ''){
	  	var regional = document.getElementById("regional.idRegional");
	  
	  	regional.masterLink = "true";
	  	regional.disabled=true;
	  
	  	var v = document.getElementById("idRegionalF").value;
	  	var d2 = document.getElementById("dsRegionalF").value;
	  	var d = document.getElementById("sgRegionalF").value;
	  	
	  
	  	setComboBoxElementValue(regional, v, v, d + " - " + d2);
	 }else{
	 	document.getElementById("regional.idRegional").disabled= false;
	 } 	
}

function setaMascaraVigenciaRegional_cb(data, error) {
	var i;
	var vi = document.getElementById("regional.dtVigenciaInicial");
	var vf = document.getElementById("regional.dtVigenciaFinal");	
	for (i = 0; i < data.length; i++) {
		setNestedBeanPropertyValue(data, i + ":dtVigenciaInicial", setFormat(vi, getNestedBeanPropertyValue(data, i + ":dtVigenciaInicial")));
		setNestedBeanPropertyValue(data, i + ":dtVigenciaFinal",   setFormat(vf, getNestedBeanPropertyValue(data, i + ":dtVigenciaFinal")));		
	}
	if (error != undefined) {
		alert(error);
		return false;
	}
	regional_idRegional_cb(data);
}
  
function afterStore_cb(data,exception,key) {
   	store_cb(data,exception,key);
   	if (exception == undefined) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		var store = "true";
 			validaAcaoVigencia(acaoVigenciaAtual, store);
	}
}
</script>
<adsm:window service="lms.municipios.regionalFilialService" onPageLoadCallBack="setaVigenciaInativaComboRegional">
	<adsm:form action="/municipios/manterRegionalFilial" idProperty="idRegionalFilial" onDataLoadCallBack="habilitaDesabilitaCfmeVigencias" service="lms.municipios.regionalFilialService.findByIdEValidaDtVigencia">
	
	   <adsm:hidden  property="dsRegionalF" serializable="false"/>
	   <adsm:hidden  property="sgRegionalF" serializable="false"/>
	   <adsm:hidden  property="idRegionalF" serializable="false"/>
	   <adsm:hidden  property="tpEmpresa" value="M" serializable="false"/>
	   
	    <adsm:combobox label="regional" property="regional.idRegional" optionLabelProperty="siglaDescricao" optionProperty="idRegional" service="lms.municipios.regionalService.findRegionaisVigentes" labelWidth="15%" width="85%" boxWidth="170" required="true" onDataLoadCallBack="setaMascaraVigenciaRegional" >
	         <adsm:propertyMapping relatedProperty="regional.usuario.nmUsuario" modelProperty="usuario.nmUsuario"/>
   	         <adsm:propertyMapping relatedProperty="regional.dtVigenciaInicial" modelProperty="dtVigenciaInicial"/>
	         <adsm:propertyMapping relatedProperty="regional.dtVigenciaFinal"   modelProperty="dtVigenciaFinal"/>
        </adsm:combobox>
		<adsm:range label="vigencia" width="35%">
             <adsm:textbox dataType="JTDate" cellStyle="vertical-align:bottom;" property="regional.dtVigenciaInicial" picker="false" disabled="true"/>
             <adsm:textbox dataType="JTDate" cellStyle="vertical-align:bottom;" property="regional.dtVigenciaFinal" picker="false" disabled="true" serializable="false"/>
        </adsm:range>
        <adsm:textbox dataType="text" cellStyle="vertical-align:bottom;" property="regional.usuario.nmUsuario" label="responsavelRegional" size="30" disabled="true" labelWidth="15%" width="35%" serializable="false" />
        
	   <adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" label="filial" 
	    			size="3" maxLength="3" width="75%" action="/municipios/manterFiliais" required="true"
	    			service="lms.municipios.filialService.findLookup" style="width:45px">
                  <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
        <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
		<adsm:checkbox property="blSedeRegional" label="sedeRegional" width="85%" />
		
		<adsm:range label="vigencia" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <%--adsm:hidden property="dtVigenciaInicialDetalhe" /--%>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
	    <adsm:buttonBar>
	    	<adsm:storeButton id="botaoSalvar" callbackProperty="afterStore" service="lms.municipios.regionalFilialService.storeMap" />
			<adsm:newButton id="botaoNovo"/>
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   