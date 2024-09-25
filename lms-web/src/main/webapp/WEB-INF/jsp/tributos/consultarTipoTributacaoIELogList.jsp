<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarTipoTributacaoIELogAction">
	<adsm:form action="/tributos/consultarTipoTributacaoIELog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idTipoTributacaoIELog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
		>
		<adsm:gridColumn property="tipoTributacaoIe.idTipoTributacaoIE" title="tipoTributacaoIe.idTipoTributacaoIE-" dataType="integer"/>
		<adsm:gridColumn property="inscricaoEstadual.idInscricaoEstadual" title="inscricaoEstadual.idInscricaoEstadual" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="tpSituacaoTributaria" title="tpSituacaoTributaria" isDomain="true"/>
		<adsm:gridColumn property="blIsencaoExportacoes" title="blIsencaoExportacoes" renderMode="image-check"/>
		<adsm:gridColumn property="blAceitaSubstituicao" title="blAceitaSubstituicao" renderMode="image-check"/>
		<adsm:gridColumn property="blIncentivada" title="blIncentivada" renderMode="image-check"/>
		<adsm:gridColumn property="tipoTributacaoIcms.idTipoTributacaoIcms" title="tipoTributacaoIcms.idTipoTributacaoIcms" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>