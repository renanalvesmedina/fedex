<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTiposTributacaoICMS" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterTiposTributacaoICMS.do" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterTiposTributacaoICMS.do" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>