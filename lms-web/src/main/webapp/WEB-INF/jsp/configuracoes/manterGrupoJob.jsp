<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterGruposJobs" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterGrupoJob" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterGrupoJob" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
