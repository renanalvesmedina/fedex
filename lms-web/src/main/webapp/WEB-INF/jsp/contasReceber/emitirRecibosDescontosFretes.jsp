<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="emitirRecibosDescontosFretes" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="relatorio" id="pesq" src="/contasReceber/emitirRecibosDescontosFretes" cmd="pesq"/>
		</adsm:tabGroup>
</adsm:window>