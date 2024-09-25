<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterGruposClassificacaoFilial" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterGruposClassificacaoFilial" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterGruposClassificacaoFilial" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  