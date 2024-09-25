<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAliquotasIVA" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/tributos/manterAliquotasIVA" cmd="list" />
			<adsm:tab title="detalhamento" id="cad" src="/tributos/manterAliquotasIVA" cmd="cad" />
		</adsm:tabGroup>
</adsm:window>
