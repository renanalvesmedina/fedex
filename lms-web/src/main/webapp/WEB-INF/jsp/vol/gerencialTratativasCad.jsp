<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.gerencialTratativasAction" onPageLoad="myOnPageLoad">
	<adsm:form action="/vol/gerencialTratativas" idProperty="idEventoCelular" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:lookup label="filial" labelWidth="15%" width="85%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.gerencialTratativasAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             disabled="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" 
        						  modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        
        <adsm:lookup label="meioTransporte" dataType="text" size="6" maxLength="6" width="85%"
					 property="meioTransporte"  
					 criteriaProperty="nrFrota"
					 service="lms.vol.gerencialTratativasAction.findLookupMeioTransporte"
					 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list"
					 idProperty="idMeioTransporte"
					 disabled="true">
					 
			 <adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador" />
			 <adsm:textbox dataType="text" 
            			  property="meioTransporte.nrIdentificador" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
		</adsm:lookup>	

		<adsm:textbox property="dsNumero" dataType="text" label="numero"
			maxLength="15" size="15" width="70%" disabled="true" />
		
		<adsm:combobox service="lms.vol.gerencialTratativasAction.findComboDocumentos" property="documento" 
				   label="documento" optionLabelProperty="dsDocumento" optionProperty="idDocumento" 
				   width="30%" autoLoad="false"/>
	
		<adsm:textarea maxLength="255" property="obAtendente" columns="50" label="observacoes" required="true" rows="8"
						width="70%"/>
						
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	document.getElementById("filial.sgFilial").masterLink = "true";	
    document.getElementById("filial.pessoa.nmFantasia").masterLink = "true";	
    document.getElementById("filial.idFilial").masterLink = "true";	
    
    document.getElementById("meioTransporte.nrFrota").masterLink = "true";	
    document.getElementById("meioTransporte.nrIdentificador").masterLink = "true";	
    document.getElementById("meioTransporte.idMeioTransporte").masterLink = "true";	
    
    document.getElementById("dsNumero").masterLink = "true";	
    
	function initWindow(eventObj) {		
		if (eventObj.name == "tab_click_comFlag") {
		   var tabGroup = getTabGroup(this.document);
		   tabGroup.setDisabledTab("pesq",true);
		}
      
	}
	
	function populaDocumento_cb(data) {
 	   comboboxLoadOptions({e:document.getElementById("documento"), data:data});
    }
	
	function myOnDataLoad_cb(data){
 	   onDataLoad_cb(data);
 	   
 	   var conhecimento = getNestedBeanPropertyValue(data,"conhecimento.idDoctoServico");
       var pedidoColeta	= getNestedBeanPropertyValue(data,"pedidoColeta.idPedidoColeta");

       if (conhecimento != undefined) {
         setElementValue("documento",'E-' + conhecimento);
       } else {
         setElementValue("documento",'C-' + pedidoColeta);    
       }
	   setDisabled("documento",true);
	   setDisabled("newButton",true);
	}
	
	function myOnPageLoad() {
	    onPageLoad();
	    carregaParametros();
    }
    function carregaParametros() {
       var url = new URL(parent.location.href);
		var sgFilial = url.parameters["sgFilial"];
        var frota 	= url.parameters["meioTransporte2.nrFrota"];
        var idMeioTransporte = url.parameters["idMeioTransporte"];
        var dsNumero = url.parameters["dsNumero"];
        
        setElementValue('filial.sgFilial', sgFilial);
        setElementValue('dsNumero', dsNumero);
        
        lookupChange({e:document.getElementById("filial.idFilial"),forceChange:true});
        
   		setElementValue('meioTransporte.nrFrota', frota);
   		lookupChange({e:document.getElementById("meioTransporte.idMeioTransporte"),forceChange:true});
   		setElementValue('meioTransporte.idMeioTransporte', idMeioTransporte);

        var dados = new Array();
        setNestedBeanPropertyValue(dados,"meioTransporte.idMeioTransporte",idMeioTransporte);
        var sdo = createServiceDataObject ("lms.vol.gerencialTratativasAction.findComboDocumentos", "populaDocumento", dados);
        xmit({serviceDataObjects:[sdo]});
    }
</script>