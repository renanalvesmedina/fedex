<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterInscricoesEstaduais" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterInscricoesEstaduais" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterInscricoesEstaduais" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
