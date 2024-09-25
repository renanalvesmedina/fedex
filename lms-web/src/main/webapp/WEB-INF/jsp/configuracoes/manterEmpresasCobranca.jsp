<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterEmpresasCobranca" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterEmpresasCobranca" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterEmpresasCobranca" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
