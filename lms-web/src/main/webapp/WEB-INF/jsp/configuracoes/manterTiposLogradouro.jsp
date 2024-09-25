<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTiposLogradouro" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterTiposLogradouro" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" onShow="showPage" src="/configuracoes/manterTiposLogradouro" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
