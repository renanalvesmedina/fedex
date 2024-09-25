<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOperadorasMCT" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="contratacaoVeiculos/manterOperadorasMCT" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="contratacaoVeiculos/manterOperadorasMCT" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  