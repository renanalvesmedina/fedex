<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirListaMestra" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="relatorio" id="pesq" src="/vendas/imprimirListaMestra" cmd="pesq"/>
	</adsm:tabGroup>
</adsm:window>
