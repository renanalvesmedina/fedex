<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterMotivosOcorrenciasRemessaRetornoBancos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterMotivosOcorrenciasRemessaRetornoBancos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterMotivosOcorrenciasRemessaRetornoBancos" cmd="cad"/>
   </adsm:tabGroup>
</adsm:window>