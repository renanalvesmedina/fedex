<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposLocalizacaoMunicipios" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterTiposLocalizacaoMunicipios" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterTiposLocalizacaoMunicipios" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  