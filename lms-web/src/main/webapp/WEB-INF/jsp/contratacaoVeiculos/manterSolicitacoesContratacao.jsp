<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterSolicitacoesContratacao" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterSolicitacoesContratacao" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterSolicitacoesContratacao" cmd="cad"/>
			<adsm:tab title="parcelasFreteColetaEntrega" onShow="onShow" disabled="true" id="parc" src="/contratacaoVeiculos/manterSolicitacoesContratacao" cmd="parc" boxWidth="205" />
			<adsm:tab title="fluxosContratacaoTitulo" onShow="onShow" disabled="true" id="fluxoContratacao" src="/contratacaoVeiculos/manterSolicitacoesContratacao" cmd="fluxoContratacao" boxWidth="205" />
			<adsm:tab title="anexo" masterTabId="cad" disabled="true" id="anexo" src="/contratacaoVeiculos/manterSolicitacoesContratacao" cmd="anexo" autoLoad="false"/>
		</adsm:tabGroup>
</adsm:window>