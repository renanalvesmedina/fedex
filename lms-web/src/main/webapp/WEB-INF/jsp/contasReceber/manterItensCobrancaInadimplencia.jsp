<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterItensCobrancaInadimplencia" type="main">
		<adsm:tabGroup selectedTab="0" >
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterItensCobrancaInadimplencia.do" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterItensCobrancaInadimplencia.do" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>