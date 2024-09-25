<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterDivisoesGrupoClassificacao" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterDivisoesGrupoClassificacao" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterDivisoesGrupoClassificacao" cmd="cad" />
		</adsm:tabGroup>
</adsm:window>
