<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOcorrenciasEntrega" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/entrega/manterOcorrenciasEntrega" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/entrega/manterOcorrenciasEntrega" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
