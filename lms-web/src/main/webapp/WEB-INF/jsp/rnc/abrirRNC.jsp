<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="abrirRNC" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="rnc" id="cad" src="/rnc/abrirRNC" cmd="cad" />
		<adsm:tab title="caracteristicas" onHide="tabClick" id="caracteristicas" src="/rnc/abrirRNC" cmd="caracteristicas" disabled="true" />
		<adsm:tab title="anexoFoto" id="fotos" onHide="tabClick" src="/rnc/abrirRNC" cmd="fotos" disabled="true" />
		<adsm:tab title="itensDaNFe" id="item" src="/rnc/abrirRNC" cmd="item" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>