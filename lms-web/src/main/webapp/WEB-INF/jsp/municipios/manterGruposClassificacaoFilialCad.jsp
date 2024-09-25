<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
function grupoClassificacaoFilialOnDataLoad_cb(data,exception){
	  onDataLoad_cb(data,exception);
	  var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
      validaAcaoVigencia(acaoVigenciaAtual, null);
	  
  }
  
 function grupoClassificacaoFilialStoreButton_cb(data,exception){
	store_cb(data,exception);
	if(exception == undefined){
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		var store = "true";
		validaAcaoVigencia(acaoVigenciaAtual, store);
	}	
} 
  
  function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
	   if(acaoVigenciaAtual == 0){
	     setDisabled(document, false);
	     setDisabled("botaoExcluir",false);
	     setDisabled("filial.pessoa.nmFantasia",true); 
	     setDisabled("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao",document.getElementById("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao").masterLink == "true");
	     setDisabled("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao",document.getElementById("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao").masterLink == "true");
	     setDisabled("filial.idFilial",document.getElementById("filial.idFilial").masterLink == "true");
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

	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
	      setDisabled(document,false);
	      setDisabled("botaoExcluir",true);
		  setDisabled("filial.pessoa.nmFantasia",true);
		  setDisabled("filial.idFilial",document.getElementById("filial.idFilial").masterLink == "true");
	      if(document.getElementById("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao").masterLink == "true"){
	          setDisabled("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao",true);
	          setDisabled("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao",true);
	          setFocus(document.getElementById("filial.sgFilial"));
	      }else{
	      	
	   		setFocus(document.getElementById("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao"));
	 	   }
		} else if (eventObj.name == "newButton_click") {
			setDisabled(document,false);
			setDisabled("filial.pessoa.nmFantasia",true);
			setDisabled("filial.idFilial",document.getElementById("filial.idFilial").masterLink == "true");
			setDisabled("botaoExcluir",true);
			if(document.getElementById("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao").masterLink == "true"){
				setDisabled("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao",true);
			    setDisabled("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao",true);
			    setFocus(document.getElementById("filial.sgFilial"));
			}else{
			    setFocus(document.getElementById("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao"));
			}
	   	} else if (eventObj.name == "removeButton") {
	   		setDisabled("botaoNovo", false);
	   	}
	}

//função chamada pelo proprio combo quando o onchange da combo "grupo classificação" é chamado"
//comportamento:com essa função a combo é populada(divisaoGrupoClassificacao_idDivisaoGrupoClassificacao_cb(data)) 
//e depois é setado o valor do idDivisaoGrupoClassificacao vindos do linkProperty da tela "Divisão Grupo de classificação"
 function populaDivisaoGrupoClassificacao_cb(data) {
    	divisaoGrupoClassificacao_idDivisaoGrupoClassificacao_cb(data);
        if (this.document.getElementById("idDivisaoGrupoClassificacaoTemp").value != ''){
			document.getElementById("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao").value = document.getElementById("idDivisaoGrupoClassificacaoTemp").value;
			document.getElementById("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao").disabled = true;
			document.getElementById("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao").masterLink = "true";
		}
		
}	
  

//funcao chamada no call back da window para popular a combo de divisaoGrupoClassificação
function habilitaDesabilitaComboDivisao_cb(dado,erro){
    onPageLoad_cb(dado,erro);
	if(document.getElementById("idDivisaoGrupoClassificacaoTemp").value != null
         || document.getElementById("idDivisaoGrupoClassificacaoTemp").value != '' ){
         document.getElementById("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao").onchange();
	}
	
 }
</script> 
<adsm:window service="lms.municipios.grupoClassificacaoFilialService" onPageLoadCallBack="habilitaDesabilitaComboDivisao">
	<adsm:form action="/municipios/manterGruposClassificacaoFilial" idProperty="idGrupoClassificacaoFilial" onDataLoadCallBack="grupoClassificacaoFilialOnDataLoad" service="lms.municipios.grupoClassificacaoFilialService.findByIdEValidaDtVigencia">
		
		<adsm:hidden property="idDivisaoGrupoClassificacaoTemp" serializable="false"/>
		<adsm:hidden property="dsDivisaoGrupoClassificacaoTemp" serializable="false"/>

		<adsm:combobox property="divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao" label="grupoClassificacao"  service="lms.municipios.grupoClassificacaoService.find" optionLabelProperty="dsGrupoClassificacao" optionProperty="idGrupoClassificacao" required="true" labelWidth="20%" width="80%" boxWidth="220"/>
		
		<adsm:combobox boxWidth="220" onDataLoadCallBack="populaDivisaoGrupoClassificacao" property="divisaoGrupoClassificacao.idDivisaoGrupoClassificacao" label="divisaoGrupo" service="lms.municipios.divisaoGrupoClassificacaoService.find" optionLabelProperty="dsDivisaoGrupoClassificacao" optionProperty="idDivisaoGrupoClassificacao" required="true" labelWidth="20%" width="80%">
			<adsm:propertyMapping criteriaProperty="divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao" modelProperty="grupoClassificacao.idGrupoClassificacao" />
		</adsm:combobox>
		

		<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" property="filial" 
					 criteriaProperty="sgFilial" idProperty="idFilial" label = "filial" size="3" required="true"
					 labelWidth="20%" maxLength="3" width="75%" action="/municipios/manterFiliais">
             <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
     		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/> 
        </adsm:lookup>

		<adsm:range label="vigencia" width="80%" labelWidth="20%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		
		<adsm:buttonBar>
			<adsm:storeButton service="lms.municipios.grupoClassificacaoFilialService.storeMap"
					callbackProperty="grupoClassificacaoFilialStoreButton" id="botaoSalvar"/>
			<adsm:newButton id="botaoNovo"/>
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   