<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterObservacoesCiasAereas" type="main">
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/municipios/manterObservacoesCiasAereas" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/municipios/manterObservacoesCiasAereas" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
  