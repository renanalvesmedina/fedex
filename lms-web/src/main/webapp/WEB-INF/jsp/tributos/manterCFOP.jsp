<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCFOP" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/tributos/manterCFOP" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/tributos/manterCFOP" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>