<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterConcessionariaTaxasPassagem" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterConcessionariaTaxasPassagem" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterConcessionariaTaxasPassagem" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>