<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="efetivarRecebimentoTransferenciaDebito" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/contasReceber/efetivarRecebimentoTransferenciaDebito" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/contasReceber/efetivarRecebimentoTransferenciaDebito" cmd="cad" onShow="onCadShow"/>
        <adsm:tab title="documentosServicoTitulo" id="proc" src="/contasReceber/efetivarRecebimentoTransferenciaDebito" 
        	cmd="proc" boxWidth="140" onShow="onProcShow"/>
	</adsm:tabGroup>
</adsm:window>