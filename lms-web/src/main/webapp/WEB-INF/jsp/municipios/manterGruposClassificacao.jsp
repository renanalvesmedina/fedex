<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="manterGruposClassificacao" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterGruposClassificacao" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterGruposClassificacao" cmd="cad" />
		</adsm:tabGroup>
</adsm:window>
