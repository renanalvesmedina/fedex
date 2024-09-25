<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterAlineasDevolucoesCheques" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterAlineasDevolucoesCheques" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterAlineasDevolucoesCheques" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
