<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.consultarObservacaoICMSPessoaLogAction">
	<adsm:form action="/configuracoes/consultarObservacaoICMSPessoaLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idObservacaoICMSPessoaLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="observacaoIcmsPessoa.idObservacaoICMSPessoa" title="observacaoIcmsPessoa.idObservacaoICMSPessoa" dataType="integer"/>
		<adsm:gridColumn property="inscricaoEstadual.idInscricaoEstadual" title="inscricaoEstadual.idInscricaoEstadual" dataType="integer"/>
		<adsm:gridColumn property="nrOrdemImpressao" title="nrOrdemImpressao" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="tpObservacaoIcmsPessoa" title="tpObservacaoIcmsPessoa" isDomain="true"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="obObservacaoIcmsPessoa" title="obObservacaoIcmsPessoa"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>