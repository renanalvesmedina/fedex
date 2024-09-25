<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="programacaoColetas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="programacao" id="list" src="/coleta/programacaoColetas" cmd="list" />
			<adsm:tab title="alteracoesColetasProgramadas" id="pendentes" src="/coleta/programacaoColetas" cmd="alteracoesColProgramadas" boxWidth="210" />
		</adsm:tabGroup>
</adsm:window>