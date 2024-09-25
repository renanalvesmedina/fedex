<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterEnderecoPessoa" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterEnderecoPessoa" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterEnderecoPessoa" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
