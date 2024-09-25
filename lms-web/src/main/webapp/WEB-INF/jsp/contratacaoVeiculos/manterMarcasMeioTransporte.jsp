<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMarcasMeioTransporte" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterMarcasMeioTransporte" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterMarcasMeioTransporte" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
