<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterRecibosViagem" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroViagem/manterRecibos" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroViagem/manterRecibos" cmd="cad" disabled="true" />
			<adsm:tab title="adiantamento" id="adi" onShow="detalhamentoAba"
					src="/freteCarreteiroViagem/manterRecibos" cmd="adi" disabled="true" />
		</adsm:tabGroup>
</adsm:window>
 