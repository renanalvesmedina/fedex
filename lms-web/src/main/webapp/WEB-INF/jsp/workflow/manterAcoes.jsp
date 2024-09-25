<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterAcoes" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/workflow/manterAcoes" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/workflow/manterAcoes" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
