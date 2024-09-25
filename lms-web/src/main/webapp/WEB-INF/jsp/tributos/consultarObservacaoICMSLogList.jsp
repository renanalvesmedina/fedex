<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarObservacaoICMSLogAction">
	<adsm:form action="/tributos/consultarObservacaoICMSLog" >
		
		<adsm:combobox 
			service="lms.tributos.consultarObservacaoICMSLogAction.findUf" 
			property="descricaoTributacaoIcms.unidadeFederativa.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" style="width:222px"
			label="ufOrigem"
			required="true">
		</adsm:combobox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idObservacaoICMSLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="observacaoIcms.idObservacaoIcms" title="observacaoIcms.idObservacaoIcms" dataType="integer"/>
		<adsm:gridColumn property="descricaoTributacaoIcms.idDescricaoTributacaoIcms" title="descricaoTributacaoIcms.idDescricaoTributacaoIcms" dataType="integer"/>
		<adsm:gridColumn property="nrOrdemImpressao" title="nrOrdemImpressao" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="tpObservacaoIcms" title="tpObservacaoIcms" isDomain="true"/>
		<adsm:gridColumn property="obObservacaoIcms" title="obObservacaoIcms"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="nrVersao" title="nrVersao" dataType="integer"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>