<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="associarCedentesEmpresas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/associarCedentesEmpresas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/associarCedentesEmpresas" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
