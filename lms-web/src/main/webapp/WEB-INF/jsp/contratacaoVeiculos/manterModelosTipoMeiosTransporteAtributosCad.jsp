<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
//variavel global para popular um array de tpComponente

var arrayTpComponente = new Array();


//verifica no array global se tpComponente=="M", se sim, habilita o campo conteudo
//pega do array global a tpSituacao de cada registro
  
  function verificaTpComponente(){
    var e = document.getElementById("atributoMeioTransporte.idAtributoMeioTransporte");
    
    if(arrayTpComponente[e.selectedIndex] != null){
        var valorTpComponente = arrayTpComponente[e.selectedIndex].value;
	    if (valorTpComponente == 'M'){
	       setDisabled("conteudoAtributoModelos", false);
	       document.getElementById("conteudoAtributoModelos").required = "true";
	       setDisabled("conteudoAtributoModelos_dsConteudoAtributoModelo", false); 
	    }else{
	       while(document.getElementById("conteudoAtributoModelos").length != 0)
		      document.getElementById("conteudoAtributoModelos")[0] = null;
	       
	       setDisabled("conteudoAtributoModelos_dsConteudoAtributoModelo", true);  
	  	   setDisabled("conteudoAtributoModelos", true);  
	  	   document.getElementById("conteudoAtributoModelos").required = "false";
	  	 } 
  	}else{
  	    while(document.getElementById("conteudoAtributoModelos").length != 0)
		      document.getElementById("conteudoAtributoModelos")[0] = null;
  		setDisabled("conteudoAtributoModelos", true); 
  		setDisabled("conteudoAtributoModelos_dsConteudoAtributoModelo", true); 
  		document.getElementById("conteudoAtributoModelos").required = "false";
  	}  
  	   
  }
  
 //funcao chamada no callback da combo atributoMeioTransporte
 //seta o tpComponente em um array global
 function setaTpComponente_cb(data) {
 	atributoMeioTransporte_idAtributoMeioTransporte_cb(data);
 	var total = data.length;
 	for(i = 0; i < total ;i++) {	
		arrayTpComponente[i+1] = getNestedBeanPropertyValue(data,":"+ i +".tpComponente");
	} 
 }

//funcao chamada no callback do form
function habilitaDesabilitaConteudo_cb(data,exception){
  onDataLoad_cb(data,exception);
   var valorTpComponente = getNestedBeanPropertyValue(data,"atributoMeioTransporte.tpComponente.value");
   if (valorTpComponente == 'M'){
       setDisabled("conteudoAtributoModelos", false);
       setDisabled("conteudoAtributoModelos_dsConteudoAtributoModelo", false); 
    }else{
       setDisabled("conteudoAtributoModelos", true);  
       setDisabled("conteudoAtributoModelos_dsConteudoAtributoModelo", true); 
  	}  
} 

//chamada no callback da window
function desabilitaConteudo_cb(data,exception){
  onPageLoad_cb(data,exception);
  setDisabled("conteudoAtributoModelos", true);  
  setDisabled("conteudoAtributoModelos_dsConteudoAtributoModelo", true); 
} 

  
 
</script>
<adsm:window service="lms.contratacaoveiculos.modeloMeioTranspAtributoService" onPageLoadCallBack="desabilitaConteudo">
	<adsm:form action="/contratacaoVeiculos/manterModelosTipoMeiosTransporteAtributos" idProperty="idModeloMeioTranspAtributo" onDataLoadCallBack="habilitaDesabilitaConteudo">
		
		<adsm:hidden property="tipoMeioTransporte.idTipoMeioTransporte"/>
		<adsm:combobox property="tipoMeioTransporte.tpMeioTransporte" boxWidth="170" label="modalidade" domain="DM_TIPO_MEIO_TRANSPORTE" labelWidth="23%" width="27%">
		</adsm:combobox>
		<adsm:textbox dataType="text" property="tipoMeioTransporte.dsTipoMeioTransporte" label="tipoMeioTransporte" maxLength="50" labelWidth="23%" width="27%" size="30" disabled="true" serializable="false"/>
		
		<adsm:combobox onlyActiveValues="true"  onDataLoadCallBack="setaTpComponente" property="atributoMeioTransporte.idAtributoMeioTransporte" optionLabelProperty="dsAtributoMeioTransporte" optionProperty="idAtributoMeioTransporte" service="lms.contratacaoveiculos.atributoMeioTransporteService.findAtributoMeioTransporte" required="true" label="atributo" labelWidth="23%" width="27%" onchange="verificaTpComponente()" boxWidth="170"/>
		
		
		
		<adsm:listbox 
                   label="conteudo" 
                   size="4" 
                   property="conteudoAtributoModelos" 
				   optionProperty="idConteudoAtributoModelo"
				   optionLabelProperty="dsConteudoAtributoModelo"
				   labelWidth="23%"
                   width="77%" 
                   showOrderControls="false" boxWidth="198" showIndex="false" serializable="true" >
                 <adsm:textbox property="dsConteudoAtributoModelo" dataType="text" size="35" maxLength="60"/>
       </adsm:listbox>
        <adsm:checkbox property="blOpcional" label="opcional" labelWidth="23%" width="27%" cellStyle="vertical-align:bottom;" />   
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="23%" width="27%" cellStyle="vertical-align:bottom;" required="true"/>
		
		
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   