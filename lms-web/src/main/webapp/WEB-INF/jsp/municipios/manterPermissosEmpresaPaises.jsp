<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterPermissosEmpresaPaises" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterPermissosEmpresaPaises" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterPermissosEmpresaPaises" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
