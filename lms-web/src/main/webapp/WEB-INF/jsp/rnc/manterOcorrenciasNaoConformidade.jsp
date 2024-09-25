<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOcorrenciasNaoConformidade" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/rnc/manterOcorrenciasNaoConformidade" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/rnc/manterOcorrenciasNaoConformidade" cmd="cad"/>
		<adsm:tab title="caracteristicas" id="caracteristicas" src="/rnc/manterOcorrenciasNaoConformidade" 
				  cmd="caracteristicas" disabled="true" masterTabId="cad" />
		<adsm:tab title="anexoFoto" id="fotos" src="/rnc/manterOcorrenciasNaoConformidade" cmd="fotos" disabled="true" masterTabId="cad" />
		<adsm:tab title="itensDaNFe" id="item" src="/rnc/manterOcorrenciasNaoConformidade" cmd="item" disabled="true"/>
	</adsm:tabGroup>
</adsm:window>