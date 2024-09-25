<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRecibosColetaEntrega" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroColetaEntrega/manterRecibos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroColetaEntrega/manterRecibos" disabled="true" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  