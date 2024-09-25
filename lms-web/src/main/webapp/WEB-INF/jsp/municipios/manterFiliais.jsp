<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFiliais" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterFiliais" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterFiliais" cmd="cad" onShow="buttonNewcs"/>
		</adsm:tabGroup>
</adsm:window>
  