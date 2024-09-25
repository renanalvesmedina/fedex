<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="historicoAnaliseCredito" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="consulta" id="pesq" src="/vendas/listarHistoricoAnaliseCredito.do" cmd="list"/>
		</adsm:tabGroup>
</adsm:window>