<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window  type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="processo" id="proc" src="/municipios/findFilialAtendimento" cmd="proc"/>
		</adsm:tabGroup>
</adsm:window>