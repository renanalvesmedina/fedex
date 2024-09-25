<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarAliquotaContribuicaoServLogAction">
	<adsm:form action="/tributos/consultarAliquotaContribuicaoServLog" >
		
		<adsm:combobox 
			property="tpImposto" 
			label="tipoImposto" 
			domain="DM_TIPO_IMPOSTO" 
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idAliquotaContribuicaoServLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="aliquotaContribuicaoServ.idAliquotaContribuicaoServ" title="aliquotaContribuicaoServ.idAliquotaContribuicaoServ" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="pcBcalcReduzida" title="pcBcalcReduzida" dataType="currency"/>
		<adsm:gridColumn property="pcAliquota" title="pcAliquota" dataType="currency"/>
		<adsm:gridColumn property="vlPiso" title="vlPiso" dataType="currency"/>
		<adsm:gridColumn property="tpImposto" title="tpImposto" isDomain="true"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="pessoa.idPessoa" title="pessoa.idPessoa" dataType="integer"/>
		<adsm:gridColumn property="servicoAdicional.idServicoAdicional" title="servicoAdicional.idServicoAdicional" dataType="integer"/>
		<adsm:gridColumn property="servicoTributo.idServicoTributo" title="servicoTributo.idServicoTributo" dataType="integer"/>
		<adsm:gridColumn property="obAliquotaContribuicaoServ" title="obAliquotaContribuicaoServ"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>