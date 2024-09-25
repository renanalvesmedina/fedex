<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposTabelaPreco" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/tabelaPrecos/manterTiposTabelaPreco" cmd="list" onShow="cleanListTrash()" />
		<adsm:tab title="detalhamento" id="cad" src="/tabelaPrecos/manterTiposTabelaPreco" cmd="cad" onShow="cleanCadTrash()" />
	</adsm:tabGroup>
</adsm:window>
