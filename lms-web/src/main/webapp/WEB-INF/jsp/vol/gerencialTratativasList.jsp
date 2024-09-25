<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.gerencialTratativasAction" onPageLoad="myPageLoad">
	<adsm:form action="/vol/gerencialTratativas">
	
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
					 exactMatch="true"
					 disabled="true">
					 
			 <adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador" />
			 <adsm:textbox dataType="text" 
            			  property="meioTransporte.nrIdentificador" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox property="dsNumero" dataType="text" label="telefone" maxLength="15" size="15" width="70%" disabled="true" />
	</adsm:form>

	<adsm:grid property="tratativas" idProperty="idEventoCelular" disableMarkAll="true" onPopulateRow="populateRow"
		selectionMode="none" rows="9" unique="true" 
		service="lms.vol.gerencialTratativasAction.findPaginatedTratativas"
		onRowClick="myRowClick"
		rowCountService="lms.vol.gerencialTratativasAction.getRowCountTratativas">

		<adsm:gridColumn property="istatus" title="status" width="10%" image="/images/bandeira_vermelha_piscante.gif" align="center"/>
		<adsm:gridColumn property="tpOrigem" title="origem" width="10%"  image="/images/bandeira_vermelha_piscante.gif" align="center"/>
		<adsm:gridColumn property="dhSolicitacao" title="data" width="15%" dataType="JTDateTimeZone" />
		<adsm:gridColumn property="tipo" title="tipo" width="7%" align="center"/>
		<adsm:gridColumn property="documento" title="documento" width="15%" dataType="integer" mask="00000000" />
		<adsm:gridColumn property="problema" title="problema" width="20%" />
		<adsm:gridColumn property="obAtendente" title="observacao" width="30%" />
		
	</adsm:grid>
	<adsm:buttonBar/>

</adsm:window>

<script>
    document.getElementById("filial.sgFilial").masterLink = "true";	
    document.getElementById("filial.pessoa.nmFantasia").masterLink = "true";	
    document.getElementById("filial.idFilial").masterLink = "true";	
    
    document.getElementById("meioTransporte.nrFrota").masterLink = "true";	
    document.getElementById("meioTransporte.nrIdentificador").masterLink = "true";	
    document.getElementById("meioTransporte.idMeioTransporte").masterLink = "true";	
    
    document.getElementById("dsNumero").masterLink = "true";	
    
    var blEditar = false; 
    	
	function initWindow(eventObj) {		
		var url = new URL(parent.location.href);
		var sgFilial = url.parameters["sgFilial"];
        var frota 	= url.parameters["meioTransporte2.nrFrota"];
        var idMeioTransporte = url.parameters["idMeioTransporte"];
        var dsNumero = url.parameters["dsNumero"];
        
        var flag = url.parameters["bRed"];
        var tabGroup = getTabGroup(this.document);
        
        // se veio do click da coluna tratativa
        blEditar = url.parameters["isTratativa"]; 
        
        if (flag == undefined) {
           setElementValue('filial.sgFilial', sgFilial);
           setElementValue('dsNumero', dsNumero);
        
           lookupChange({e:document.getElementById("filial.idFilial"),forceChange:true});
        
   	   	   setElementValue('meioTransporte.nrFrota', frota);
    	   lookupChange({e:document.getElementById("meioTransporte.idMeioTransporte"),forceChange:true});
   		   setElementValue('meioTransporte.idMeioTransporte', idMeioTransporte);

		   findButtonScript('tratativas', document.forms[0]);	
		   
	       tabGroup.setDisabledTab("cad",true);
		} else {
		   tabGroup.selectTab('cad', {name:'tab_click_comFlag'},true);
		   tabGroup.setDisabledTab("pesq",true);
		}		
	}

	function populateRow(tr,data) { 
	    var dhAtendimento = getNestedBeanPropertyValue(data,"dhAtendimento");
	//    var tpOrigem = getNestedBeanPropertyValue(data,"tpOrigem.description")
	    
    	var origem = getNestedBeanPropertyValue(data,"tpOrigem.value");
    	
         if (origem == undefined || origem == '') {
            tr.children[1].innerHTML = "<NOBR></NOBR>";
         }
    	
    	if(dhAtendimento != undefined) {
			tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_verde.gif"); 
    	}
    	
    	if(origem == 'C') {
    		tr.children[1].innerHTML = tr.children[1].innerHTML.replace("bandeira_vermelha_piscante.gif","celular.gif"); 
    	}    	
    	
    //	tr.children[0].innerHTML = tr.children[0].innerHTML + getNestedBeanPropertyValue(data,"chamados");
    	
    }
    
	function myPageLoad() {
		onPageLoad();
   	}
   	
   	function myRowClick(id) {   	   
		var tabGroup = getTabGroup(this.document);
      	tabGroup.setDisabledTab("cad",false);
        tratativasGridDef.detailGridRow("onDataLoad", id);   	   
   	}
   	
</script>
