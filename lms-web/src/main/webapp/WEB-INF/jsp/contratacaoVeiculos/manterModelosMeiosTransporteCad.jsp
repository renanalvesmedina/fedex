<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
  function modeloMeioTransporteOnDataLoad_cb(data,exception){
        onDataLoad_cb(data,exception);
        var acao= "";
        desabilitaCombos(data,acao);
  } 
  
  function modeloMeioTransporteStoreButton_cb(data,exception){
        store_cb(data,exception);
        if(exception == undefined){
        	var acao= "store";
        	desabilitaCombos(data,acao);
        }	
  } 
  
  function preencheHiddenMarca() {
		var valor = document.getElementById("marcaMeioTransporte.idMarcaMeioTransporte").options[document.getElementById("marcaMeioTransporte.idMarcaMeioTransporte").selectedIndex].text;
		document.getElementById("marcaMeioTransporte.dsMarcaMeioTransporte").value = valor;
	}
	
	function preencheHiddenTipo() {
		var valor = document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").options[document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").selectedIndex].text;
		document.getElementById("tipoMeioTransporte.dsTipoMeioTransporte").value = valor;
	}
	
	function preencheHiddenMeio() {
		var valor = document.getElementById("tipoMeioTransporte.tpMeioTransporte").options[document.getElementById("tipoMeioTransporte.tpMeioTransporte").selectedIndex].text;
		document.getElementById("meioTransporte").value = valor;
	}
          
  function desabilitaCombos(data,acao){
        if(acao == ""){
            setFocus(document.getElementById("dsModeloMeioTransporte"));
        }else{
        	setFocus(document.getElementById("botaoLimpar"),false);
        }
        preencheHiddenMarca();
        preencheHiddenTipo();
        preencheHiddenMeio();
            
       	if(document.getElementById("tipoMeioTransporte.tpMeioTransporte").value != ''){
	  		setDisabled("tipoMeioTransporte.tpMeioTransporte",true);
	  	}
	  	if(document.getElementById("tipoMeioTransporte.idTipoMeioTransporte").value != ''){
	  	  setDisabled("tipoMeioTransporte.idTipoMeioTransporte",true);
	   	}
	  	
	  	if(document.getElementById("marcaMeioTransporte.idMarcaMeioTransporte").value != ''){
	  	 setDisabled("marcaMeioTransporte.idMarcaMeioTransporte",true);
	  	}
	  	
  }
  
  function initWindow(eventObj){
	  if(eventObj.name= "tab_click" ){
	  	setDisabled("marcaMeioTransporte.idMarcaMeioTransporte",false);
	  	setDisabled("tipoMeioTransporte.idTipoMeioTransporte",false);
	  	setDisabled("tipoMeioTransporte.tpMeioTransporte",false);
	  	setFocus(document.getElementById("tipoMeioTransporte.tpMeioTransporte"));
	  }
  }	  
</script>
<adsm:window service="lms.contratacaoveiculos.modeloMeioTransporteService">
	<adsm:form action="/contratacaoVeiculos/manterModelosMeiosTransporte" idProperty="idModeloMeioTransporte" onDataLoadCallBack="modeloMeioTransporteOnDataLoad">
		 
		<adsm:combobox property="tipoMeioTransporte.tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE" label="modalidade" labelWidth="23%" width="27%" required="true" serializable="false" service="" boxWidth="150"/>
		<adsm:hidden property="meioTransporte" serializable="false"/>
		
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" required="true" label="tipoMeioTransporte" service="lms.contratacaoveiculos.modeloMeioTransporteService.findTipoMeioTranspByMeio" optionLabelProperty="dsTipoMeioTransporte" optionProperty="idTipoMeioTransporte" onlyActiveValues="true" labelWidth="23%" width="27%" boxWidth="150">
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.tpMeioTransporte" modelProperty="tpMeioTransporte"/>
		</adsm:combobox>
		<adsm:hidden property="tipoMeioTransporte.dsTipoMeioTransporte" serializable="false"/>
		
		<adsm:combobox property="marcaMeioTransporte.idMarcaMeioTransporte" required="true" label="marcaMeioTransporte" service="lms.contratacaoveiculos.modeloMeioTransporteService.findMarcaMeioTranspByMeio" onlyActiveValues="true" optionLabelProperty="dsMarcaMeioTransporte" optionProperty="idMarcaMeioTransporte" labelWidth="23%" width="77%" boxWidth="150">
		 	<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.tpMeioTransporte" modelProperty="tpMeioTransporte"/>
		</adsm:combobox>
		<adsm:hidden property="marcaMeioTransporte.dsMarcaMeioTransporte" serializable="false"/>
		
		<adsm:textbox dataType="text" property="dsModeloMeioTransporte" required="true" label="descricaoModelo" maxLength="60" size="50" labelWidth="23%" width="77%"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="23%" width="27%" required="true" />
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="modeloMeioTransporteStoreButton"/>
			<adsm:newButton id="botaoLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   