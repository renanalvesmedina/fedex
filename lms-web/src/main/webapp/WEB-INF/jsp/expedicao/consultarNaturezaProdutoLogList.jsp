<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.consultarNaturezaProdutoLogAction">
	<adsm:form action="/expedicao/consultarNaturezaProdutoLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idNaturezaProdutoLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="naturezaProduto.idNaturezaProduto" title="naturezaProduto.idNaturezaProduto" dataType="integer"/>
		<adsm:gridColumn property="dsNaturezaProduto" title="dsNaturezaProduto"/>
		<adsm:gridColumn property="tpSituacao" title="tpSituacao" isDomain="true"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>