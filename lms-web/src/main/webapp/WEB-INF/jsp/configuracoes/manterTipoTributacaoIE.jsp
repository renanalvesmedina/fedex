<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTipoTributacaoIE" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterTipoTributacaoIE" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterTipoTributacaoIE" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
