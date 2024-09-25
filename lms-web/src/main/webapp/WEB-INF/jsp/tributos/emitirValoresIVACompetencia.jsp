<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="emitirValoresIVACompetencia" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="relatorio" id="pesq" src="/tributos/emitirValoresIVACompetencia" cmd="pesq"/>
	</adsm:tabGroup>
</adsm:window>