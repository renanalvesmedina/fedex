<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="digitarNotaFiscalServicoTitulo" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/expedicao/digitarPreNotaFiscalServico" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/expedicao/digitarPreNotaFiscalServico" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
