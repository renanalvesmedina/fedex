<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterLiberacaoEmbarque" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/manterTiposLiberacaoEmbarque" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/manterTiposLiberacaoEmbarque" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
