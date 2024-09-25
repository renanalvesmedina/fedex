<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarParametroClienteLogAction">
	<adsm:grid property="gridLog" idProperty="idTaxaClienteLog"
			selectionMode="none"
			scrollBars="horizontal"
			rows="17"
			onRowClick="rowClick"
			title="consultarTaxaClienteLog"
			rowCountService="lms.vendas.consultarParametroClienteLogAction.getRowCountTaxaCliente"
			paginatedService="lms.vendas.consultarParametroClienteLogAction.findPaginatedTaxaCliente">
  		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
  		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone" width="120"/>
  		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="tpTaxaIndicador" title="indicador" isDomain="true"/>
		<adsm:gridColumn property="vlTaxa" title="valor" dataType="currency"/>
		<adsm:gridColumn property="vlExcedente" title="valorExcedente" dataType="currency"/>
		<adsm:gridColumn property="psMinimo" title="pesoMinimo" />
		<adsm:gridColumn property="pcReajTaxa" title="percentualReajuste" dataType="currency"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>
<script>
function rowClick(){
	return false;
}
var url = new URL(parent.location.href);
if (url.parameters.idTaxaCliente != undefined && url.parameters.idTaxaCliente != ""){
	var filter = new Object();
	setNestedBeanPropertyValue(filter,"taxaCliente.idTaxaCliente",url.parameters.idTaxaCliente);
	gridLogGridDef.executeSearch(filter);
}
</script>