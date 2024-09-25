<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="consultarComplementosRomaneios" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="list" src="/contasReceber/consultarComplementosRomaneios" cmd="list"/>
		</adsm:tabGroup>
</adsm:window>
