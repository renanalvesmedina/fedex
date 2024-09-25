<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterReciboOficial" type="main">
	<adsm:tabGroup selectedTab="0"> 
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterReciboOficial" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterReciboOficial" cmd="cad"/>
		<adsm:tab title="documentos" id="faturas" src="/contasReceber/manterReciboOficial" cmd="faturas"/>
		<adsm:tab title="recebimentos" id="cheques" src="/contasReceber/manterReciboOficial" cmd="cheques"/>
	</adsm:tabGroup> 
</adsm:window>