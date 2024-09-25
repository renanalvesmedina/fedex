<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOperadorasEmpresaEscolta" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/sgr/manterOperadorasEmpresaEscolta" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/sgr/manterOperadorasEmpresaEscolta" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
