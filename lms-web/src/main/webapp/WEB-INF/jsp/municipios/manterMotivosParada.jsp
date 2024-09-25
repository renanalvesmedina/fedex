<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMotivoParada" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterMotivosParada" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterMotivosParada" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>
   