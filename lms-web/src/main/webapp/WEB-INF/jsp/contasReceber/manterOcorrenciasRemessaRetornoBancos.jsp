<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterOcorrenciasRemessaRetornoBancos" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/manterOcorrenciasRemessaRetornoBancos" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/manterOcorrenciasRemessaRetornoBancos" cmd="cad"/>
   </adsm:tabGroup>
</adsm:window>