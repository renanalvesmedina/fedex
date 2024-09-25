<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterFuncionariosRegiao" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/coleta/manterFuncionariosRegiao" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/coleta/manterFuncionariosRegiao" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
