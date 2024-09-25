<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarTaxaClienteLogAction">
	<adsm:form action="/vendas/consultarTaxaClienteLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idTaxaClienteLog"
			selectionMode="none"
			width="2000"
			scrollBars="horizontal"
			rows="11"
			onRowClick="rowClick"
  	>
  		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
  		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
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