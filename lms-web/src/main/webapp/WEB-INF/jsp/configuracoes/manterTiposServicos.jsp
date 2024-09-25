<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTiposServicos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterTiposServicos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterTiposServicos" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
