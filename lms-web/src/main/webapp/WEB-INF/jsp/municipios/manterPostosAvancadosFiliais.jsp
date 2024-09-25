<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPostosAvancadosFiliais" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterPostosAvancadosFiliais" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterPostosAvancadosFiliais" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  