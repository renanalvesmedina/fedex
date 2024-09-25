<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="monitorarJobs" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/consultarJobs" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/consultarJobs" cmd="cad" disabled="true"/>
		</adsm:tabGroup>
</adsm:window>
