<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterClientesPerdidos" type="main">
  	<adsm:tabGroup selectedTab="0">
  		<adsm:tab title="listagem" id="pesq" src="/vendas/manterClientesPerdidos" cmd="list"/>
 	 	<adsm:tab title="detalhamento" id="cad" src="/vendas/manterClientesPerdidos" cmd="cad"/>
    </adsm:tabGroup>
</adsm:window>
