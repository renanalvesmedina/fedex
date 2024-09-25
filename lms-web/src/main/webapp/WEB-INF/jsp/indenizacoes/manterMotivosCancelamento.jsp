<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMotivosCancelamento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/indenizacoes/manterMotivosCancelamento" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/indenizacoes/manterMotivosCancelamento" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
