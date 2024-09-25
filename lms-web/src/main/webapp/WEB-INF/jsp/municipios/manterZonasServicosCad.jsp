<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript">
//recebe os flags definindo se os campos ficarao habilitados ou desabilitados no detalhamento, validacao conforme as datas de vigencia

function zonaServico_cb(data,exception){
  onDataLoad_cb(data,exception);
  var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  validaAcaoVigencia(acaoVigenciaAtual, null); 
}

function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
  if(acaoVigenciaAtual == 0){
  	 setDisabled(document, false);
  	 setDisabled("botaoExcluir",false);
  	 if(tipoEvento == "" ||  tipoEvento == null)
    	setFocusOnFirstFocusableField(document);
     else
    	setFocus(document.getElementById("botaoNovo"),false);
   }else if(acaoVigenciaAtual == 1) {
     setDisabled(document,true);
     setDisabled("botaoNovo",false);
     setDisabled("botaoSalvar",false);
     setDisabled("dtVigenciaFinal", false);
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

//é executada qdo clica na aba detalhamento, habilitando os campos... pois se um registro foi detalhado e era desabilitado e tela continua desabilitada
function initWindow(eventObj) {
	 if (eventObj.name == "tab_click") {
        setDisabled(document,false);
		setDisabled("botaoExcluir",true);
	
	    if(document.getElementById("zona.idZona").value != ""){
	        document.getElementById("zona.idZona").masterLink="true";
	        setDisabled("zona.idZona", true);
	        setFocus(document.getElementById("servico.idServico"));
	    }else {
	    	setFocus(document.getElementById("zona.idZona"));
	    }
		
 	}
 	if (eventObj.name == "removeButton") 
 		setDisabled("botaoNovo", false);
 	
 	
}

//nova function do button novo
function habilitaCampos(){

 newButtonScript();
 setDisabled(document,false);
 setDisabled("botaoExcluir",true);

 
 setDisabled("dtVigenciaFinal", false);
 if(document.getElementById("zona.idZona").masterLink=="true"){
   setDisabled("zona.idZona",true);
 }else{
 	setDisabled("zona.idZona",false);
 } 
 
 setFocusOnFirstFocusableField(document);
}
function afterStore_cb(data,exception,key) {
    store_cb(data,exception,key);
    if (exception == undefined){
	     var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
	     var store = "true";
		 validaAcaoVigencia(acaoVigenciaAtual, store);
	}
}
</script>
<adsm:window service="lms.municipios.zonaServicoService">
	<adsm:form idProperty="idZonaServico" action="/municipios/manterZonasServicos" onDataLoadCallBack="zonaServico" service="lms.municipios.zonaServicoService.findByIdEValidaVigencias">
		
		<adsm:combobox property="zona.idZona" optionLabelProperty="dsZona" optionProperty="idZona"
				service="lms.municipios.zonaService.find" onlyActiveValues="true"
				label="zona" required="true" boxWidth="200" />
		
		<adsm:combobox property="servico.idServico" optionLabelProperty="dsServico" optionProperty="idServico"
				service="lms.configuracoes.servicoService.find" onlyActiveValues="true"
				label="servico" required="true" boxWidth="200" />
		
		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:buttonBar>
		    <adsm:storeButton id="botaoSalvar" service="lms.municipios.zonaServicoService.storeMap" callbackProperty="afterStore" />
			<adsm:button caption="limpar" onclick="habilitaCampos()" id="botaoNovo" disabled="false"/>
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
