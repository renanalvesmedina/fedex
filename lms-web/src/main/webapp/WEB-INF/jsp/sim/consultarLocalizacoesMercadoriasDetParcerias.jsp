 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window >
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="principal" id="parceriaPrincipal" src="/sim/consultarLocalizacoesMercadorias" cmd="parceriasPrincipal" height="220" autoLoad="false" onShow="findByIdDetalhamentoAbaParcerias"/>
			<adsm:tab title="integrantes" id="parceriaIntegrantes" src="/sim/consultarLocalizacoesMercadorias" cmd="parceriasIntegrantes" height="220" autoLoad="false" onShow="findPaginatedIntegrantesAbaParcerias"/>
			<adsm:tab title="notasFiscais" id="parceriaNotasFiscais" src="/sim/consultarLocalizacoesMercadorias" cmd="parceriasNotasFiscais" boxWidth="80" height="220" autoLoad="false" onShow="findPaginatedNotasFiscaisParcerias"/>
			<adsm:tab title="dadosFrete" id="parceriaFrete" src="/sim/consultarLocalizacoesMercadorias" cmd="parceriasFreteParcerias" height="220" boxWidth="100" autoLoad="false" onShow="findPaginatedDadosFrete"/>
			<adsm:tab title="dadosCalculo" id="parceiraCalculo" src="/sim/consultarLocalizacoesMercadorias" cmd="parceriasCalculo" height="220" boxWidth="100" autoLoad="false" onShow="findDadosCalculoAbaParcerias"/>
			<adsm:tab title="outros" id="parceiraOutros" src="/sim/consultarLocalizacoesMercadorias" cmd="parceriasOutros" height="220" autoLoad="false" onShow="findOutrosAbaParcerias"/>
		</adsm:tabGroup>
</adsm:window>
<script>

function buscaIdDoctoServicoAbaParcerias(){
	var tabGroup = getTabGroup(this.document);
	var tabParcerias = tabGroup.getTab('parcerias');
	tabParcerias.childTabGroup.selectTab('parceriaPrincipal',{name:'tab_click'},true);
	
	
}

</script>   