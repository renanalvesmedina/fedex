<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.consultarTipoProdutoLogAction">
	<adsm:form action="/expedicao/consultarTipoProdutoLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idTipoProdutoLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="tipoProduto.idTipoProduto" title="tipoProduto.idTipoProduto" dataType="integer"/>
		<adsm:gridColumn property="dsTipoProduto" title="dsTipoProduto"/>
		<adsm:gridColumn property="tpSituacao" title="tpSituacao" isDomain="true"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>