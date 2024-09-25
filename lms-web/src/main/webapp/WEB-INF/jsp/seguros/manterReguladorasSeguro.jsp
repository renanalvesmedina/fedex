<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterReguladorasSeguro" type="main"> 
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguros/manterReguladorasSeguro" cmd="list" />
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterReguladorasSeguro" cmd="cad" />
	</adsm:tabGroup>
</adsm:window>
