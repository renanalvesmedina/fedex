<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterSubstitutos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/workflow/manterSubstitutos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/workflow/manterSubstitutos" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
