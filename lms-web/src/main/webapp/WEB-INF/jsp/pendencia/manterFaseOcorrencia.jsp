<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterFaseProcessoOcorrencia" type="main">
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="listagem" id="pesq" src="/pendencia/manterFaseOcorrencia" cmd="list"/>
		<adsm:tab title="detalhamento" id="cad" src="/pendencia/manterFaseOcorrencia" cmd="cad"/>
	</adsm:tabGroup>
</adsm:window>
