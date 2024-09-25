<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterPaisPermissionadoMeiosTransporte" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterPaisPermissionadoMeiosTransporte" cmd="list" height="440" />
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterPaisPermissionadoMeiosTransporte" cmd="cad" height="440" />
		</adsm:tabGroup>
</adsm:window>