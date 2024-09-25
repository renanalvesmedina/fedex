<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarParametroClienteLogAction">
	<adsm:grid property="gridLog" idProperty="idGeneralidadeClienteLog"
			selectionMode="none"
			scrollBars="horizontal"
			onRowClick="rowClick"
			title="consultarGeneralidadeClienteLog"
			rows="16"
			rowCountService="lms.vendas.consultarParametroClienteLogAction.getRowCountGeneralidadeCliente"
			paginatedService="lms.vendas.consultarParametroClienteLogAction.findPaginatedGeneralidadeCliente"
  	>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true" width="80"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone" width="140"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true" width="80"/>
		<adsm:gridColumn property="loginLog" title="loginLog" width="80"/>

		<adsm:gridColumn title="generalidade" property="nmParcelaPreco" width="120"/>
		<adsm:gridColumn property="tpIndicador" title="indicador" isDomain="true" width="80"/>
		<adsm:gridColumn property="vlGeneralidade" title="valorIndicador" dataType="currency" width="100"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>

<script>
function rowClick(){
	return false;
}

var url = new URL(parent.location.href);
if (url.parameters.idGeneralidadeCliente != undefined && url.parameters.idGeneralidadeCliente != ""){
	var filter = new Object();
	setNestedBeanPropertyValue(filter,"generalidadeCliente.idGeneralidadeCliente",url.parameters.idGeneralidadeCliente);
	gridLogGridDef.executeSearch(filter);
}
</script>