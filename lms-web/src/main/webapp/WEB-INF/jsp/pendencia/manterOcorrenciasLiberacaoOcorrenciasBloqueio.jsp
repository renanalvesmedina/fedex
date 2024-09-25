<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterOcorrenciasLiberacaoOcorrenciasBloqueio" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/pendencia/manterOcorrenciasLiberacaoOcorrenciasBloqueio" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/pendencia/manterOcorrenciasLiberacaoOcorrenciasBloqueio" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
