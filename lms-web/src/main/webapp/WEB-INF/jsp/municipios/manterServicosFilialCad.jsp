<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<SCRIPT>
	function filialServicoDataLoad_cb(data,exception){
		 onDataLoad_cb(data,exception);
		 var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		 validaAcaoVigencia(acaoVigenciaAtual, null);
		 
	}
	
	function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
		 if(acaoVigenciaAtual == 0){
		    setDisabled(document, false);
		    setDisabled("botaoExcluir",false);
		    setDisabled("filial.pessoa.nmFantasia",true);     
		    setDisabled("filial.idFilial",document.getElementById("filial.idFilial").masterLink == "true")     
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

	function sugereVigenciaPopUp(data) {
	      var idFilial = getNestedBeanPropertyValue(data,"idFilial");
	      sugereVigencia(idFilial);
	}
	
	function sugereVigenciaDataLoad_cb(data, erro) {
		  
		  if (erro != undefined) {
		  	alert(erro);
		  	return false;
		  }
		  
		  if (data != undefined && data != null) {		  	  
		      filial_sgFilial_exactMatch_cb(data);
		      if (data.length > 0) {
			      var idFilial = getNestedBeanPropertyValue(data,":0.idFilial");
			      sugereVigencia(idFilial);
			  }
		  }
	}
	
	function sugereVigencia(idFilial){
         var sdo = createServiceDataObject("lms.municipios.filialServicoService.findFilialComHistoricosFuturos", "sugereVigencia",
         {idFilial:idFilial});
         xmit({serviceDataObjects:[sdo]});
	}

	function sugereVigencia_cb(data, erro) {
	     if (erro != undefined) {
	  	   alert(erro);
	  	   return false;
	     }
	     if(data.dtPrevisaoOperacaoInicial != undefined)
         	setElementValue("dtVigenciaInicial", data.dtPrevisaoOperacaoInicial);
	}

	//é executada qdo clica na aba detalhamento, habilitando os campos... pois se um registro foi detalhado e era desabilitado e tela continua desabilitada
	function initWindow(eventObj) {
		var blDetalhamento = eventObj.name == "gridRow_click" || eventObj.name == "storeButton";
	    if (!blDetalhamento) {
			setDisabled(document,false);
			setDisabled("botaoExcluir",true);
			setDisabled("filial.pessoa.nmFantasia",true);
			if(document.getElementById("filial.idFilial").masterLink == "true"){
				setDisabled("filial.idFilial",document.getElementById("filial.idFilial").masterLink == "true");
				setFocus(document.getElementById("servico.idServico"));
			}else{
				setFocus(document.getElementById("filial.sgFilial"));
			}
		}
	}
		
	function afterStore_cb(data,exception,key){
		store_cb(data,exception,key);
		if (exception == undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";
  			validaAcaoVigencia(acaoVigenciaAtual, store);
		}	
	} 

</SCRIPT>
<adsm:window service="lms.municipios.filialServicoService">
	<adsm:form action="/municipios/manterServicosFilial" idProperty="idFilialServico" onDataLoadCallBack="filialServicoDataLoad"
		service="lms.municipios.filialServicoService.findByIdMap">
	
	  <adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.municipios.filialService.findLookup" 
	  			   dataType="text" label="filial" size="3" maxLength="3" width="35%" action="/municipios/manterFiliais" required="true"
	  			   style="width:45px" onPopupSetValue="sugereVigenciaPopUp" onDataLoadCallBack="sugereVigenciaDataLoad">
             <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		     <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/> 
      </adsm:lookup>
      
	 <adsm:combobox property="servico.idServico" label="servico"  service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" required="true" boxWidth="200"/>
	 
       <adsm:range label="vigencia" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
	   <adsm:buttonBar>
	   		<adsm:storeButton id="botaoSalvar" service="lms.municipios.filialServicoService.storeMap" callbackProperty="afterStore" />
	        <adsm:newButton id="botaoNovo"/>
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   