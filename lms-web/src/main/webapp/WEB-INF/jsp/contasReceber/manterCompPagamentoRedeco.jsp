<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterCompPagamentoRedeco" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterCompPagamentoRedeco" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterCompPagamentoRedeco" cmd="cad"/>
			<adsm:tab title="creditosBancariosLote" id="bat" src="/contasReceber/manterCompPagamentoRedeco" cmd="bat"/>
		</adsm:tabGroup>
</adsm:window>