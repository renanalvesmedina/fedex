<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacaoEvento" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="consulta" id="listagem" src="/sim/consultarLocalizacoesDELL" cmd="list"/>
		<adsm:tab title="documentosDELL" id="internacional" src="/sim/consultarLocalizacoesDELL" cmd="inte"  boxWidth="130"/>
		<adsm:tab title="documentos" id="nacional" src="/sim/consultarLocalizacoesDELL" cmd="naci"/>
		<adsm:tab title="detalhamentoDELL" id="detalhamentoDELL" src="/sim/consultarLocalizacoesDELL" cmd="detDELL" boxWidth="130"/>
		<adsm:tab title="detalhamento" id="detalhamento" src="/sim/consultarLocalizacoesDELL" cmd="det"/>
	</adsm:tabGroup>
</adsm:window>