<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="gerarPropostasCliente" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab
				title="listagem"
				id="pesq"
				src="/vendas/manterPropostasCliente"
				cmd="list" />
		<adsm:tab
				title="detalhamento"
				id="cad"
				src="/vendas/manterPropostasCliente"
				cmd="cad" />
		<adsm:tab
				title="formalidades"
				id="form"
				src="/vendas/manterPropostasCliente"
				cmd="form" />
		<adsm:tab
				title="resumo"
				id="resumo"
				src="/vendas/manterPropostasCliente"
				cmd="resumo" />
		<adsm:tab
				title="anexos"
				id="anexo"
				src="/vendas/manterPropostasCliente"
				cmd="anexo" />
		<adsm:tab title="historico" id="historico" src="/vendas/manterPropostasCliente" cmd="historico"/>
	</adsm:tabGroup>
</adsm:window>
