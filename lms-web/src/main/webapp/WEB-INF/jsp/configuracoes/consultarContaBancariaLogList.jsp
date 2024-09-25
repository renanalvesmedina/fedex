<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.consultarContaBancariaLogAction">
	<adsm:form action="/configuracoes/consultarContaBancariaLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idContaBancariaLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="contaBancaria.idContaBancaria" title="contaBancaria.idContaBancaria" dataType="integer"/>
		<adsm:gridColumn property="pessoa.idPessoa" title="pessoa.idPessoa" dataType="integer"/>
		<adsm:gridColumn property="agenciaBancaria.idAgenciaBancaria" title="agenciaBancaria.idAgenciaBancaria" dataType="integer"/>
		<adsm:gridColumn property="nrContaBancaria" title="nrContaBancaria" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="tpConta" title="tpConta" isDomain="true"/>
		<adsm:gridColumn property="dvContaBancaria" title="dvContaBancaria"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>