<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="programacaoColetasVeiculos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="veiculo" id="veiculo" src="/coleta/programacaoColetasVeiculos" cmd="list" />
		<adsm:tab title="conteudo" id="cad" src="/coleta/programacaoColetasVeiculos" cmd="cad" disabled="true" masterTabId="veiculo" />
		<adsm:tab title="coletasPendentesTitulo" id="pendentes" src="/coleta/programacaoColetasVeiculos" cmd="colPendentes" boxWidth="110" disabled="true" masterTabId="veiculo"  />
		<adsm:tab title="entregasRealizadasTitulo" id="realizadas" src="/coleta/programacaoColetasVeiculos" cmd="entregasRealizadas" boxWidth="110" disabled="true" masterTabId="veiculo"  />
		<adsm:tab title="gerenciamentoRiscosTitulo" id="riscos" src="/coleta/programacaoColetasVeiculos" cmd="gerRiscos" boxWidth="140" disabled="true" masterTabId="veiculo" />
	</adsm:tabGroup>
</adsm:window>