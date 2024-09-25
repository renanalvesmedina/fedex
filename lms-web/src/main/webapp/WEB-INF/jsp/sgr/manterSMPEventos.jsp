<%@ taglib prefix="adsm" uri="/WEB-INF/adsm.tld" %>
<adsm:window service="lms.sgr.manterSMPAction">
	<adsm:grid
		idProperty="idEventoSMP"
		onRowClick="eventosRowClick"
		property="eventos"
		rowCountService="lms.sgr.manterSMPAction.getRowCountEventoSMP"
		rows="17"
		selectionMode="none"
		service="lms.sgr.manterSMPAction.findPaginatedEventoSMP"
	>
		<adsm:gridColumn property="cdEvento" title="tipo" width="10%" />
		<adsm:gridColumn property="dsEvento" title="descricao" width="20%" />
		<adsm:gridColumn dataType="JTDateTimeZone" property="dhEvento" title="dataHora" width="15%" />
		<adsm:gridColumn property="dsObservacao" title="observacao" width="35%" />
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function initWindow(event) {
		if (event.name === "onDataLoad") {
			var tab = getTabGroup(document).getTab("cad");
		    var idSolicMonitPreventivo = tab.getFormProperty("idSolicMonitPreventivo");
		    eventosGridDef.executeSearch({ "idSolicMonitPreventivo" : idSolicMonitPreventivo });
		}
	}

	function eventosRowClick() {
		return false;
	}
</script>
