<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="carregarArquivosRetornoBancos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="importacao" id="pesq" src="/contasReceber/carregarArquivosRetornoBancos" cmd="proc" />
	</adsm:tabGroup>
</adsm:window>
