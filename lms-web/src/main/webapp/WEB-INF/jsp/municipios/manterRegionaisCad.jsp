<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function regionalLoad_cb(data,exception){
  onDataLoad_cb(data,exception);
  //variaveis para o linkProperty do "filiais da Regional" em função das regionais fora de vigencia
  document.getElementById("idRegionalF").value = document.getElementById("idRegional").value;
  document.getElementById("dsRegionalF").value = document.getElementById("dsRegional").value;
  var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  validaVigencia(acaoVigenciaAtual, null);
}

function validaVigencia(acaoVigenciaAtual,tipoEvento ){
	if(acaoVigenciaAtual == 0){
     setDisabled(document, false);
     setDisabled("botaoExcluir",false);
     setDisabled("usuario.nmUsuario",true);
     if(tipoEvento == "" ||  tipoEvento == null)
     	setFocusOnFirstFocusableField(document);
     else
       setFocusOnNewButton(document);	
  }else if(acaoVigenciaAtual == 1) {
	 setDisabled(document,true);
     setDisabled("botaoNovo",false);
     setDisabled("botaoSalvar",false);
     setDisabled("dtVigenciaFinal", false);
     setDisabled("filiaisRegional", false);
     setDisabled("nrDdd", false);
   	 setDisabled("nrTelefone", false);
   	 setDisabled("dsEmailFaturamento", false);
   	 setDisabled("usuarioFaturamento.idUsuario", false);
   	
     if(tipoEvento == "" ||  tipoEvento == null)
     	setFocusOnFirstFocusableField(document);
     else
       setFocusOnNewButton(document);	
  }else if(acaoVigenciaAtual == 2) {
  	 setDisabled(document,true);
     setDisabled("botaoNovo",false);
     setDisabled("filiaisRegional", false);
     setFocusOnNewButton(document);
  }
}

//nova function do button novo
function habilitaCampos(){
	newButtonScript();
 	setDisabled(document,false);
	setDisabled("botaoExcluir",true);
	setDisabled("usuario.nmUsuario",true);
	setDisabled("filiaisRegional",true); 
}

//é executada qdo clica na aba detalhamento
function initWindow(eventObj) {
    if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton"){
		setDisabled(document,false);
		setFocus(document.getElementById("sgRegional"));
		setDisabled("usuario.nmUsuario",true);
		setDisabled("botaoExcluir",true);
		setDisabled("filiaisRegional",true);
	}
}

function onSgRegionalChange() {
	document.getElementById("sgRegional").value = document.getElementById("sgRegional").value.toUpperCase();
}

	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			setElementValue(document.getElementById("idRegionalF"), document.getElementById("idRegional").value);
  			setElementValue(document.getElementById("dsRegionalF"), document.getElementById("dsRegional").value);
  			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  			var store = "true";
  			validaVigencia(acaoVigenciaAtual, store);
		}
    }
    
    
    function pageLoad_cb(data) {
    	onPageLoad_cb(data);
	    var remoteCall = {serviceDataObjects:new Array()};
		remoteCall.serviceDataObjects.push(createServiceDataObject("lms.municipios.regionalService.findParameterFuncionario", "funcao", {value:"CD_GERENTE_REGIONAL"}));
		xmit(remoteCall);
		document.getElementById("ds_funcao").masterLink = "true";
		document.getElementById("cd_funcao").masterLink = "true";
    }
    
    function funcao_cb(data,exception) {
    	if  (exception) {
    		alert(exception);
    		return;
    	}
    	onDataLoad_cb(data);
    }
    

</script>

<adsm:window service="lms.municipios.regionalService" onPageLoadCallBack="pageLoad">
	<adsm:form idProperty="idRegional" action="/municipios/manterRegionais" onDataLoadCallBack="regionalLoad" 
			service="lms.municipios.regionalService.findByIdEValidaVigencia">
	
		<adsm:textbox dataType="text" required="true" property="sgRegional" label="sigla" maxLength="3" size="3" labelWidth="17%" onchange="onSgRegionalChange();"/>
		<adsm:textbox dataType="text" required="true" property="dsRegional" label="descricao" maxLength="60" size="37" labelWidth="13%" />
		
		<adsm:hidden property="cd_funcao"/>
		<adsm:hidden property="ds_funcao"/>
		
		<adsm:hidden property="idFaturamento"/>
		<adsm:hidden property="nomeFaturamento"/>
		
		<adsm:lookup property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula" 
					 dataType="text" label="responsavelRegional" size="16" maxLength="16" labelWidth="17%" width="83%" required="true" 
				     service="lms.municipios.manterRegionaisAction.findLookupUsuarioFuncionario" 
				     action="/configuracoes/consultarFuncionariosView">
			<adsm:propertyMapping criteriaProperty="cd_funcao" modelProperty="codFuncao.idCodigo" disable="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="cd_funcao" modelProperty="codFuncao.codigo" disable="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="ds_funcao" modelProperty="codFuncao.nome" disable="false" inlineQuery="false"/>
			
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
			
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:lookup property="usuarioFaturamento" idProperty="idUsuario" criteriaProperty="nrMatricula"
					 disabled="false"
					 dataType="text" label="responsavelFaturamentoRegional" size="16" maxLength="16" labelWidth="17%" width="83%" required="false" 
				     service="lms.municipios.manterRegionaisAction.findLookupUsuario" 
				     action="/configuracoes/consultarFuncionariosView">
			
			<adsm:propertyMapping criteriaProperty="cd_funcao" modelProperty="codFuncao.idCodigo" disable="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="cd_funcao" modelProperty="codFuncao.codigo" disable="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="ds_funcao" modelProperty="codFuncao.nome" disable="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="usuarioFaturamento.nmUsuario" modelProperty="nmUsuario"/>
			<adsm:textbox dataType="text" property="usuarioFaturamento.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:complement label="telefone" labelWidth="17%" width="100%" >
			<adsm:textbox dataType="integer" property="nrDdd"  size="5" maxLength="5" />
			<adsm:textbox dataType="integer" property="nrTelefone" size="10" maxLength="10"/>
		</adsm:complement>
		<adsm:textbox dataType="email" property="dsEmailFaturamento" label="email" size="40" maxLength="60" width="35%" labelWidth="17%"  required="false" disabled="false"/>
				
		
        <adsm:hidden property="idRegionalF" serializable="false" />
        <adsm:hidden property="dsRegionalF" serializable="false" />
        
		<adsm:buttonBar>
			<adsm:button caption="filiaisRegional" action="/municipios/manterRegionalFilial" cmd="main" id="filiaisRegional">
			    <adsm:linkProperty src="idRegionalF" target="idRegionalF" disabled="true"/>
                <adsm:linkProperty src="dsRegionalF" target="dsRegionalF" disabled="true"/>
                <adsm:linkProperty src="sgRegional" target="sgRegionalF" disabled="true"/>
                <adsm:linkProperty src="dtVigenciaInicial" target="regional.dtVigenciaInicial" disabled="true"/>
                <adsm:linkProperty src="dtVigenciaFinal" target="regional.dtVigenciaFinal" disabled="true"/>
               	<adsm:linkProperty src="usuario.nmUsuario" target="regional.usuario.nmUsuario" disabled="true"/>
			</adsm:button>
			<adsm:storeButton id="botaoSalvar" callbackProperty="afterStore" service="lms.municipios.manterRegionaisAction.store" />
			
			<adsm:newButton id="botaoNovo" />
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar> 
	</adsm:form>
</adsm:window>