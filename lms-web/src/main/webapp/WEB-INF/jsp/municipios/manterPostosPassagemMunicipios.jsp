<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterPostosPassagemMunicipios" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterPostosPassagemMunicipios" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterPostosPassagemMunicipios" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
