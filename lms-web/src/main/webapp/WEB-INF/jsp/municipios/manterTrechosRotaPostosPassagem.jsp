<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTrechosRotaPostosPassagem" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterTrechosRotaPostosPassagem" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterTrechosRotaPostosPassagem" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
