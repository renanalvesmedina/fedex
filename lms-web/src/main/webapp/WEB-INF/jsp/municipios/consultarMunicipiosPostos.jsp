<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/consultarMunicipios" cmd="postosList" height="373" relatedTargetProperty="idMunicipioFilial" onShow="realizaPaginacao"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/consultarMunicipios" cmd="postosDet" height="373" disabled="true"/>
		</adsm:tabGroup>
</adsm:window>
<script>
getTab(document).properties.ignoreChangedState=true;
function selecionaAbaListPostos(){
	var tabGroup = getTabGroup(window);
	tabGroup.selectTab('pesq',{name:'tab_click'});
}
</script>