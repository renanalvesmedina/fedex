<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterAliquotasICMSAereo" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tributos/manterAliquotasICMSAereo" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/tributos/manterAliquotasICMSAereo" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
