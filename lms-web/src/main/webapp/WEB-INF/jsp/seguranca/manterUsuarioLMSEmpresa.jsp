<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="vincularEmpresaFiliaisTitulo" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguranca/manterUsuarioLMSEmpresa" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguranca/manterUsuarioLMSEmpresa" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
