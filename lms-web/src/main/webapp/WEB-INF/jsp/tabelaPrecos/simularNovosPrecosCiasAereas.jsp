<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="simularNovosPrecosCiasAereas" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/simularNovosPrecosCiasAereas" cmd="list"/>
		<adsm:tab title="reajuste" id="reaj" src="/tabelaPrecos/simularNovosPrecosCiasAereas" cmd="reaj"/>
		<adsm:tab title="reajustesEspecificosTitulo" id="reajEsp" src="/tabelaPrecos/simularNovosPrecosCiasAereas" cmd="reajEsp" boxWidth="150"/>
		<adsm:tab title="reajustesExcecoesTitulo" id="reajExce" src="/tabelaPrecos/simularNovosPrecosCiasAereas" cmd="reajExce" boxWidth="150"/>
	</adsm:tabGroup>
</adsm:window>
