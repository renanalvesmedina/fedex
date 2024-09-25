<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
  function myStoreButton_cb(data,exception){
  	store_cb(data,exception);
  	if(exception == undefined){
	  	setDisabled("filialRotas",true);
	  	setDisabled("filialRotas_filial.idFilial",true);
	  	setDisabled("botaoSalvar",true);
	  	setFocus(document.getElementById("botaoNovo"),false);
	 }	
  }
  
  function initWindow(eventObj){
  	if(eventObj.name=="tab_click" || eventObj.name=="removeButton"){
  	   setDisabled("filialRotas",false);
  	   setDisabled("filialRotas_filial.idFilial",false);
  	   setDisabled("botaoSalvar",false);
  	   setDisabled("botaoNovo",false);
  	   setFocus(document.getElementById("filialRotas_filial.sgFilial"));
  	}
  }
  
  function desabilitaCampos_cb(data,exception){
  	onDataLoad_cb(data,exception);
  	if(exception == undefined){
	  	setDisabled("filialRotas",true);
	  	setDisabled("filialRotas_filial.idFilial",true);
	  	setDisabled("filialRotas_nmFilial",true);
	  	setDisabled("botaoSalvar",true);
	  	setFocus(document.getElementById("botaoPostosPassagemRota"),false);
	 }
	 setFocusOnFirstFocusableField();	
  }
  
  function limpaCampos(){
  	   newButtonScript();
  	   setDisabled("filialRotas",false);
  	   setDisabled("filialRotas_filial.idFilial",false);
  	   setDisabled("botaoSalvar",false);
  	   setDisabled("botaoNovo",false);
  	   setDisabled("botaoExcluir",true);
  	   setFocus(document.getElementById("filialRotas_filial.sgFilial"));
  	   
  }
</script>
<adsm:window title="consultarPostosPassagemRota" service="lms.municipios.manterRotaAction">
	<adsm:form action="/municipios/manterPostosPassagemRota" idProperty="idRota" onDataLoadCallBack="desabilitaCampos">
	    <adsm:hidden property="dsRota" serializable="false"/>
	    <adsm:listbox 
                   label="rota" 
                   size="4" 
                   property="filialRotas" 
				   optionProperty="sgFilial"
				   optionLabelProperty="dsRota"
				   labelWidth="22%"
                   width="78%" 
                   showOrderControls="false" boxWidth="180" showIndex="false" serializable="true" required="true">
                 <adsm:lookup 
	                 property="filial" 
	                 idProperty="idFilial" 
	                 criteriaProperty="sgFilial" 
	                 dataType="text" size="3" maxLength="3" 
	                 service="lms.municipios.manterRotaAction.findFilialLookup" action="/municipios/manterFiliais"
	                 exactMatch="false" minLengthForAutoPopUpSearch="3">
	                 <adsm:propertyMapping relatedProperty="filialRotas_nmFilial" modelProperty="pessoa.nmFantasia"/>
	                 <adsm:textbox dataType="text" property="nmFilial" serializable="false"/>
	              </adsm:lookup>   
       </adsm:listbox>
    <adsm:buttonBar>
		<adsm:button caption="postosPassagemRotasViagemTrecho" action="municipios/manterPostosPassagemRotas" cmd="main" id="botaoPostosPassagemRota">
			<adsm:linkProperty src="dsRota" target="rota.dsRota"/>
			<adsm:linkProperty src="idRota" target="rota.idRota"/>
		</adsm:button>
		<adsm:storeButton callbackProperty="myStoreButton" id="botaoSalvar"/>
		<adsm:button caption="novo" id="botaoNovo" onclick="limpaCampos()"/>
		<adsm:removeButton id="botaoExcluir"/>
	</adsm:buttonBar>
	</adsm:form>
</adsm:window>