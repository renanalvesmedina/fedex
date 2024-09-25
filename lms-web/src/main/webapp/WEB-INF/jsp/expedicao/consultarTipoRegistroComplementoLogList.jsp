<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.consultarTipoRegistroComplementoLogAction">
	<adsm:form action="/expedicao/consultarTipoRegistroComplementoLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idTipoRegistroComplementoLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="tipoRegistroComplemento.idTipoRegistroComplemento" title="tipoRegistroComplemento.idTipoRegistroComplemento" dataType="integer"/>
		<adsm:gridColumn property="dsTipoRegistroComplemento" title="dsTipoRegistroComplemento"/>
		<adsm:gridColumn property="nrVersao" title="nrVersao" dataType="integer"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>