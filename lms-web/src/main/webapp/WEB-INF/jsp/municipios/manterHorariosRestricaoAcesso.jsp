<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterHorariosTransito" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterHorariosRestricaoAcesso" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterHorariosRestricaoAcesso" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  