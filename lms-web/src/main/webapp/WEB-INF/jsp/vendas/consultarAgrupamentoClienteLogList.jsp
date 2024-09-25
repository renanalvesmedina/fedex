<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarAgrupamentoClienteLogAction">
	<adsm:form action="/vendas/consultarAgrupamentoClienteLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idAgrupamentoClienteLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="agrupamentoCliente.idAgrupamentoCliente" title="agrupamentoCliente.idAgrupamentoCliente" dataType="integer"/>
		<adsm:gridColumn property="formaAgrupamento.idFormaAgrupamento" title="formaAgrupamento.idFormaAgrupamento" dataType="integer"/>
		<adsm:gridColumn property="divisaoCliente.idDivisaoCliente" title="divisaoCliente.idDivisaoCliente" dataType="integer"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>