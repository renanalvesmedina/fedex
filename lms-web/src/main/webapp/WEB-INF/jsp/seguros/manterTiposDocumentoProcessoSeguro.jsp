<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterTiposDocumentoProcessoSeguro" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/seguros/manterTiposDocumentoProcessoSeguro" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/seguros/manterTiposDocumentoProcessoSeguro" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
