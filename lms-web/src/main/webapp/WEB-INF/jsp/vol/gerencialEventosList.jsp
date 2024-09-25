<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.gerencialEventosAction">
	<adsm:form action="/vol/gerencialEventos">
	   <adsm:lookup label="meioTransporte" dataType="text" size="6" maxLength="6" width="85%"
					 property="meioTransporte"  
					 criteriaProperty="nrFrota"
					 service="lms.vol.gerencialEventosAction.findLookupMeioTransporte"
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
	</adsm:form>

	<adsm:grid property="eventos" idProperty="idEvento" disableMarkAll="true"
		selectionMode="none" rows="15" gridHeight="150" unique="true" onRowClick="disableClick"
		service="lms.vol.gerencialEventosAction.findPaginatedEventos"
		rowCountService="lms.vol.gerencialEventosAction.getRowCountEventos">

		<adsm:gridColumn property="dhEnvio" title="envio"  dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="dhRetorno" title="retorno"  dataType="JTDateTimeZone"/>
		
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
    document.getElementById("meioTransporte.nrFrota").masterLink = "true";	
    document.getElementById("meioTransporte.nrIdentificador").masterLink = "true";	
    document.getElementById("meioTransporte.idMeioTransporte").masterLink = "true";	
    
	function disableClick() {
		return false;
	}

	function initWindow(eventObj) {		
		var url = new URL(parent.location.href);
        var frota 	= url.parameters["meioTransporte2.nrFrota"];
        var idMeioTransporte = url.parameters["idMeioTransporte"];
   		setElementValue('meioTransporte.nrFrota', frota);
        lookupChange({e:document.getElementById("meioTransporte.idMeioTransporte"),forceChange:true});
        setElementValue('meioTransporte.idMeioTransporte', idMeioTransporte);
		findButtonScript('eventos', document.forms[0]);	
				
	}
	
</script>
