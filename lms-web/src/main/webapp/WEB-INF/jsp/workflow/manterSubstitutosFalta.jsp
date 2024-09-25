<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterSubstitutosFalta" type="main">

    <adsm:i18nLabels>
		<adsm:include key="LMS-39003"/>
    </adsm:i18nLabels>

	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/workflow/manterSubstitutosFalta" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/workflow/manterSubstitutosFalta" cmd="cad"/>
	</adsm:tabGroup>

</adsm:window>
