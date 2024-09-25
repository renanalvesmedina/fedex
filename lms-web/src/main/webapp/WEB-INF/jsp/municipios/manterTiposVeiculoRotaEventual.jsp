<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMeiosTransporteRotaEventual" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterTiposVeiculoRotaEventual" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterTiposVeiculoRotaEventual" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  