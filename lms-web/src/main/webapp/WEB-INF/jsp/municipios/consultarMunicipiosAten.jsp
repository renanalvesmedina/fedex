<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarMunicipiosAten">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/consultarMunicipios" cmd="atenList" height="406" relatedTargetProperty="idMunicipio" onShow="realizaPaginacao"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/consultarMunicipios" cmd="atenDet" height="406" copyMasterTabProperties="true" disabled="true" />
			<adsm:tab title="postosPassagemTitulo" id="postos" src="/municipios/consultarMunicipios" cmd="postos" height="406" boxWidth="120" relatedTargetProperty="idMunicipioFilial" relatedIdTab="cad" onShow="selecionaAbaListPostos" disabled="true"/>
		</adsm:tabGroup>
</adsm:window>
<script>
function selecionaAbaList(){
	var tabGroup = getTabGroup(window);
	tabGroup.selectTab('pesq',{name:'tab_click'});
}
</script>