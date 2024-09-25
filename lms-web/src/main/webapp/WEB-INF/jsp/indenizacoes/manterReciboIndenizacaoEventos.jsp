<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/manterReciboIndenizacao" height="20" service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedEventoRimByFilial" idProperty="idReciboIndenizacao" onDataLoadCallBack="dataLoad">

		<adsm:hidden property="idEventoRIM" />
		<adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="false">
			<adsm:masterLinkItem property="nrReciboComposto" label="numeroRIM" itemWidth="50" />
			<adsm:hidden property="tpBeneficiarioIndenizacao" />
			<adsm:hidden property="idCliente" />
		</adsm:masterLink>

	</adsm:form>

	<adsm:grid  
		property="eventoRIM" 
		idProperty="idEventoRIM" 
		service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedEventoRimByFilial"
		rowCountService="lms.indenizacoes.manterReciboIndenizacaoAction.getRowCountEventosRIM"
		selectionMode="none" 
		gridHeight="290" 
		detailFrameName="eventos"
		scrollBars="none" 		
		unique="true"
		onRowClick="eventos_OnClick" 
		rows="15">
				
		<adsm:gridColumn property="eventoRim.dhEventoRim" 							title="dataEvento" 			width="75" dataType="JTDateTimeZone" />
		<adsm:gridColumn property="eventoRim.tpEventoIndenizacao"					title="tipoIndenizacao"		width="100" isDomain="true" />
		<adsm:gridColumn property="filial.sgFilial"        							title="filial" 				width="30" />
		<adsm:gridColumn property="usuario.nmUsuario"								title="nomeUsuario" 		width="85" />
		<adsm:gridColumn property="motivoCancelamentoRim.dsMotivoCancelamentoRim" 	title="motivoCancelamento" 	width="110" align="left" dataType="text" />
	</adsm:grid>
	<adsm:buttonBar/>
</adsm:window>

<script>

	document.getElementById('idEventoRIM').masterLink = 'true';

   	var tabGroup = getTabGroup(this.document);
	//var abaDetalhamento = tabGroup.getTab("cad");
	
	function pageLoad_cb() {
	}

	function onTabShow(fromTab) {

		executeSearch();	
			
	}

	// executa a pesquisa da grid
	function executeSearch(data) {
	}
	function eventos_OnClick(id) {
		return false;
	}
</script>
