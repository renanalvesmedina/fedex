<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.indenizacoes.consultarReciboIndenizacaoAction" >
	
	<adsm:form action="/indenizacoes/consultarReciboIndenizacao" 
			   service="lms.indenizacoes.consultarReciboIndenizacaoAction.findByIdEventoRim" 
			   idProperty="idEventoRim"
			   height="20">		
		
		<adsm:hidden property="idEventoRim" />					   
		<adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="false">						 
			<adsm:masterLinkItem property="nrReciboComposto" label="numeroRIM" itemWidth="50"/>			
		</adsm:masterLink>
							  
	</adsm:form>
		
	<adsm:grid idProperty="idEventoRim"
			   property="eventoRim" 
			   service="lms.indenizacoes.consultarReciboIndenizacaoAction.findPaginatedEventoRim"
			   rowCountService="lms.indenizacoes.consultarReciboIndenizacaoAction.getRowCountEventoRim"
			   selectionMode="none" 
			   gridHeight="290" 
			   detailFrameName="eventos"		
			   scrollBars="none" 		
			   unique="true" 
			   rows="15">			  
	
		<adsm:gridColumn title="dataEvento" property="dhEvento" width="15%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="descricaoEvento" property="dsEvento" width="25%"/>
		<adsm:gridColumn title="siglaFilial" property="sgFilial" width="5%"/>
		<adsm:gridColumn title="nomeUsuario" property="nmUsuario" width="20%" />
		<adsm:gridColumn title="motivoCancelamento" property="dsMotivoCancelamento" width="35%" align="left" dataType="text" />
		
	</adsm:grid>
	<adsm:buttonBar/>
</adsm:window>

<script>

	function onTabShow(fromTab) {
		resetValue(document);		
	}

</script>