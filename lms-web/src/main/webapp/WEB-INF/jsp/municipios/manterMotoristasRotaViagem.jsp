<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMotoristasRotaViagem" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterMotoristasRotaViagem" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterMotoristasRotaViagem" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  