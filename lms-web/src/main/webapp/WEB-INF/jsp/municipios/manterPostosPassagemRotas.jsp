<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="postosPassagemTrechos" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterPostosPassagemRotas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterPostosPassagemRotas" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
