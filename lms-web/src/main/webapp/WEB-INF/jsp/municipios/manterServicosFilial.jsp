<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterServicosFilial" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterServicosFilial" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterServicosFilial" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  