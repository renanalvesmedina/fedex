<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="emitirPreFaturasImportadas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="relatorio" id="pesq" src="/contasReceber/emitirPreFaturasImportadas" cmd="pesq"/>
	</adsm:tabGroup>
</adsm:window>
