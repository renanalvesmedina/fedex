<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.consultarLiberacaoEmbarqueLogAction">
	<adsm:form action="/vendas/consultarLiberacaoEmbarqueLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idLiberacaoEmbarqueLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="liberacaoEmbarque.idLiberacaoEmbarque" title="liberacaoEmbarque.idLiberacaoEmbarque" dataType="integer"/>
		<adsm:gridColumn property="cliente.idCliente" title="cliente.idCliente" dataType="integer"/>
		<adsm:gridColumn property="municipio.idMunicipio" title="municipio.idMunicipio" dataType="integer"/>
		<adsm:gridColumn property="tpModal" title="tpModal" isDomain="true"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>