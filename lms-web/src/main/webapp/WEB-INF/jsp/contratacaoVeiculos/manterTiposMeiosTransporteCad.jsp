<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

 //seta a descricao do meio de transporte
 function setDsMeioTransporte_cb(data, exception){
   onDataLoad_cb(data,exception);
   if (document.getElementById("tpMeioTransporte")!= ''){
	   var e = document.getElementById("tpMeioTransporte");
	   var dsMeio = e.options[e.selectedIndex].text;
	   setElementValue("dsMeioTransporte", dsMeio); 
   }
   setDisabled("tpMeioTransporte",true);
   setDisabled("tpCategoria",true);
   setDisabled("tipoMeioTransporte.idTipoMeioTransporte",false);
   setFocusOnFirstFocusableField(document);
 } 

 function initWindow(eventObj){
 	
 	if (eventObj.name == "tab_click" || eventObj.name == "newButton_click" || eventObj.name == "removeButton"){
 		setDisabled("tpMeioTransporte",false);
    	setDisabled("tpCategoria",false);
        setDisabled("tipoMeioTransporte.idTipoMeioTransporte",false);
        setFocus(document.getElementById("tpMeioTransporte"));
 	}
 }

 function myStoreButton_cb(data,exception){
 	store_cb(data,exception);
 	if(exception == undefined){
	 	setDisabled("tpMeioTransporte",true);
	    setDisabled("tpCategoria",true);
	    //setDisabled("tipoMeioTransporte.idTipoMeioTransporte",true);
	   setFocus(document.getElementById("botaoNovo"),false);
	} 
 } 

</script>
<adsm:window service="lms.contratacaoveiculos.tipoMeioTransporteService">
	<adsm:form action="/contratacaoVeiculos/manterTiposMeiosTransporte" idProperty="idTipoMeioTransporte" onDataLoadCallBack="setDsMeioTransporte">
		
		<adsm:hidden property="dsMeioTransporte" serializable="false"/>
		
			
		<adsm:combobox boxWidth="150" property="tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE" label="modalidade" labelWidth="23%" width="27%" required="true" cellStyle="vertical-align:bottom">
			<adsm:propertyMapping relatedProperty="dsMeioTransporte" modelProperty="description"/>
		</adsm:combobox>
		
		<adsm:textbox dataType="text" property="dsTipoMeioTransporte" label="tipoMeioTransporte" maxLength="60" labelWidth="23%" size="30" width="27%" required="true" cellStyle="vertical-align:bottom"/>
		
		<adsm:combobox boxWidth="150" property="tpCategoria" domain="DM_CATEGORIA_VEICULO" label="categoria" labelWidth="23%" width="27%" required="true"/>
		
		<adsm:combobox boxWidth="175" property="tipoMeioTransporte.idTipoMeioTransporte" onlyActiveValues="true" label="compostoPor" service="lms.contratacaoveiculos.tipoMeioTransporteService.find" optionLabelProperty="dsTipoMeioTransporte" optionProperty="idTipoMeioTransporte" labelWidth="23%" width="27%">
		   <adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="tpMeioTransporte"/>
		</adsm:combobox>
		
		<adsm:listbox 
                   label="quantidadeEixos" 
                   size="4" 
                   property="eixosTipoMeioTransporte"
				   optionProperty="idEixosTipoMeioTransporte"
				   optionLabelProperty="qtEixos"
				   labelWidth="23%"
                   width="65%"
                   showOrderControls="false" boxWidth="100" showIndex="false" serializable="true">
                 <adsm:textbox property="qtEixos" dataType="integer" size="5" maxLength="2"/>
       </adsm:listbox>	 
       
	   <adsm:textbox dataType="integer" property="nrCapacidadePesoInicial" label="capacidadePesoInicial" unit="kg" maxLength="6" labelWidth="23%" size="11" width="27%" required="true"/>
	   <adsm:textbox dataType="integer" property="nrCapacidadePesoFinal" label="capacidadePesoFinal" unit="kg" maxLength="6" labelWidth="23%" size="11" width="27%" required="true"/>
		
	   <adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="23%" width="77%" required="true"/>
	   <adsm:buttonBar>
			<adsm:button caption="atributos" action="/contratacaoVeiculos/manterModelosTipoMeiosTransporteAtributos" cmd="main">
				<adsm:linkProperty src="idTipoMeioTransporte" target="tipoMeioTransporte.idTipoMeioTransporte"/>
				<adsm:linkProperty src="tpMeioTransporte" target="tipoMeioTransporte.tpMeioTransporte"/>
				<adsm:linkProperty src="dsTipoMeioTransporte" target="tipoMeioTransporte.dsTipoMeioTransporte"/>
			</adsm:button>
			<adsm:button caption="combustiveis" action="/contratacaoVeiculos/manterCombustiveisTiposMeiosTransporte.do" cmd="main">
			    <adsm:linkProperty src="dsMeioTransporte" target="dsMeioTransporte"/>
			    <adsm:linkProperty src="dsTipoMeioTransporte" target="tipoMeioTransporte.dsTipoMeioTransporte"/>
			    <adsm:linkProperty src="idTipoMeioTransporte" target="tipoMeioTransporte.idTipoMeioTransporte"/>
			</adsm:button>
			<adsm:storeButton callbackProperty="myStoreButton" id="botaoSalvar" service="lms.contratacaoveiculos.manterTiposMeiosTransporteAction.storeMap"/>
			<adsm:newButton id="botaoNovo"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   