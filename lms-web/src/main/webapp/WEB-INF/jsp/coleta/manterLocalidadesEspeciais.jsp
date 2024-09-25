<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterLocalidadesEspeciais" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/coleta/manterLocalidadesEspeciais" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/coleta/manterLocalidadesEspeciais" cmd="cad" />
		</adsm:tabGroup>
</adsm:window>