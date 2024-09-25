<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	      
			<adsm:grid property="eventoDocumentoServico" idProperty="idEventoDocumentoServico"  onRowClick="returnFalse" showGotoBox="false" gridHeight="215" showPagging="false" selectionMode="none" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedEventos" scrollBars="both" unique="true" >
				<adsm:gridColumn width="100" title="documento" property="tpDocumento" isDomain="true"/>
				<adsm:gridColumn width="100" title="numero" property="nrDocumento" dataType="text"/>
				<adsm:gridColumn width="150" title="evento" property="dsDescricaoEvento" dataType="text"/>
				<adsm:gridColumn width="250" title="complemento" property="obComplemento" dataType="text"/>
				<adsm:gridColumn width="150" title="dataEvento" property="dhEvento" dataType="JTDateTimeZone"/>
				<adsm:gridColumn width="50" title="dia" property="dia" align="center"/>
				<adsm:gridColumn width="250" title="ocorrencia" property="ocorrencia" dataType="text"/>
				<adsm:gridColumn width="100" title="cancelado" property="blEventoCancelado" renderMode="image-check" />
				<adsm:gridColumn width="120" title="inclusao" property="dhInclusao" dataType="JTDateTimeZone"/>
			</adsm:grid>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" idProperty="idDoctoServico" height="0">		
	</adsm:form>
</adsm:window>   
<script>
	function findEventosByIdDocto(){
		var idDoctoServico =  parent.document.getElementById("idDoctoServico").value;
		var data = new Array();
		setNestedBeanPropertyValue(data, "idDoctoServico",idDoctoServico );
		eventoDocumentoServicoGridDef.executeSearch(data);
	}
	
  	function returnFalse(){
  		return false;
  	}


</script>   