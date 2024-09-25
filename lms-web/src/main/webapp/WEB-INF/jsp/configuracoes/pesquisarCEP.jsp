<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="pesquisarCEP" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="pesquisa" id="pesq" src="/configuracoes/pesquisarCEP" cmd="pesq"/>
		</adsm:tabGroup>
</adsm:window>