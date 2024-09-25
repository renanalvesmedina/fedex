<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRotaColetaEntrega" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterRotaColetaEntrega" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterRotaColetaEntrega" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  