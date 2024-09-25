<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTiposEndereco" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterTiposEndereco.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterTiposEndereco.do" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>