<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarAliquotaInssPessoaFisicaLogAction">
	<adsm:form action="/tributos/consultarAliquotaInssPessoaFisicaLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idAliquotaInssPessoaFisicaLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="aliquotaInssPessoaFisica.idAliquotaInssPessoaFisica" title="aliquotaInssPessoaFisica.idAliquotaInssPessoaFisica" dataType="integer"/>
		<adsm:gridColumn property="dtInicioVigencia" title="dtInicioVigencia" dataType="JTDate"/>
		<adsm:gridColumn property="pcAliquota" title="pcAliquota" dataType="currency"/>
		<adsm:gridColumn property="pcBcalcReduzida" title="pcBcalcReduzida" dataType="currency"/>
		<adsm:gridColumn property="vlSalarioBase" title="vlSalarioBase" dataType="currency"/>
		<adsm:gridColumn property="vlMaximoRecolhimento" title="vlMaximoRecolhimento" dataType="currency"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>