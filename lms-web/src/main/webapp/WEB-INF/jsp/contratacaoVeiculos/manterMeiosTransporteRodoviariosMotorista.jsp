<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMeiosTransporteRodoviariosMotoristas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/contratacaoVeiculos/manterMeiosTransporteRodoviariosMotorista" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/contratacaoVeiculos/manterMeiosTransporteRodoviariosMotorista" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>