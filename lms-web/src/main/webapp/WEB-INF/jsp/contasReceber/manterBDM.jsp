<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window title="manterBDM" type="main">

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterBDM" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterBDM" cmd="cad"/>
		<adsm:tab title="documentosServicoTitulo" masterTabId="cad" copyMasterTabProperties="true" 
		id="documentosServico" src="/contasReceber/manterBDM" cmd="documentosServico" boxWidth="140"/>
	</adsm:tabGroup>

</adsm:window>