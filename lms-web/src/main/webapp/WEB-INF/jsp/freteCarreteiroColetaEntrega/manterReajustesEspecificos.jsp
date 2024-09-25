<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterReajustesEspecificos" type="main"> 
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/freteCarreteiroColetaEntrega/manterReajustesEspecificos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/freteCarreteiroColetaEntrega/manterReajustesEspecificos" cmd="cad"/>
		<adsm:tab title="parcelas" id="parcelas" src="/freteCarreteiroColetaEntrega/manterReajustesEspecificos" cmd="item"/>
	</adsm:tabGroup>
</adsm:window>