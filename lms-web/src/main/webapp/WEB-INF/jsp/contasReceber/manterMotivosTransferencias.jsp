<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterMotivosTransferencias" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterMotivosTransferencias.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterMotivosTransferencias.do" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>