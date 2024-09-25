<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.rotaPostoControleService">

	<adsm:form action="/sgr/manterPostosControleRota">
	
		<adsm:lookup dataType="text" property="rota" idProperty="idRota" criteriaProperty="dsRota"
					 service="lms.municipios.rotaService.findLookup" action="/municipios/manterPostosPassagemRotasViagem" cmd="list" 
		 			 label="rota" labelWidth="25%" width="75%" size="30" maxLength="60" exactMatch="true" minLengthForAutoPopUpSearch="3"/>
		 			 
		<adsm:checkbox property="somenteRotasPostoControle" label="somenteRotasPostoControle" labelWidth="25%" width="75%"/>
		 			 
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="rotas" idProperty="idRota" gridHeight="200" unique="true" selectionMode="none" rows="13"
			   service="lms.sgr.rotaPostoControleService.findRotasPaginated" rowCountService="lms.sgr.rotaPostoControleService.getRowCountRotaPostoControle">
		<adsm:gridColumn property="dsRota" title="rota" />
		<adsm:buttonBar/>
	</adsm:grid>
	
</adsm:window>

<script>
function initWindow(eventObj) {
	if (eventObj.name == "tab_click") {
		desabilitaTabPostosControle(true);
	}
}

function desabilitaTabPostosControle(disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("postosControle", disabled);
}
</script>
