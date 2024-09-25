<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarGeneralidadeClienteLogAction">
	<adsm:form action="/vendas/consultarGeneralidadeClienteLog" >
		
		<adsm:lookup >
		
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idGeneralidadeClienteLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
			onRowClick="rowClick"
  	>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>

		<adsm:gridColumn property="parametroCliente.idParametroCliente" title="parametroCliente.idParametroCliente" dataType="integer"/>
		<adsm:gridColumn property="parcelaPreco.idParcelaPreco" title="parcelaPreco.idParcelaPreco" dataType="integer"/>
		<adsm:gridColumn property="tpIndicador" title="tpIndicador" isDomain="true"/>
		<adsm:gridColumn property="vlGeneralidade" title="vlGeneralidade" dataType="currency"/>
		<adsm:gridColumn property="pcReajGeneralidade" title="pcReajGeneralidade" dataType="currency"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>

<script>
function rowClick(){
	return false;
}
</script>