<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterEmpresas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterEmpresas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterEmpresas" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  