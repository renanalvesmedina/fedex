<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterDadosBancariosPessoa" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterDadosBancariosPessoa" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterDadosBancariosPessoa" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
