<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterProcessosSinistro" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem"     id="pesq" src="/seguros/manterProcessosSinistro"                 cmd="list" onShow="onListagemShow"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterProcessosSinistro"                  cmd="cad" onShow="onDetalhamentoShow" />
		<adsm:tab title="documentos"   id="documentos" src="/seguros/manterProcessosSinistro"           cmd="documentos" masterTabId="cad" disabled="true" copyMasterTabProperties="true" onShow=""/>
		<adsm:tab title="composicaoPrejuizo" id="custosAdicionais" src="/seguros/manterProcessosSinistro" cmd="custosAdicionais" disabled="true" onShow="onCustosAdicionaisShow" />
		<adsm:tab title="anexos"            id="fotos" src="/seguros/manterProcessosSinistro"            cmd="fotos" disabled="true"            onShow="onTabShow" />
	</adsm:tabGroup>
</adsm:window>
