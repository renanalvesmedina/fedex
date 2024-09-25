<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarTipoTributacaoUfLogAction">
	<adsm:form action="/tributos/consultarTipoTributacaoUfLog" >
		
		<adsm:lookup 
  	    			property="unidadeFederativa"
				    idProperty="idUnidadeFederativa" 
				    criteriaProperty="sgUnidadeFederativa"
					service="lms.tributos.consultarTipoTributacaoUfLogAction.findLookupUF" 
					dataType="text" 
					labelWidth="20%" 
					maxLength="3"
					width="10%" 
					label="uf" 
					size="3"
					action="/municipios/manterUnidadesFederativas" 
					minLengthForAutoPopUpSearch="2" 
					exactMatch="false">
					 
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			
		</adsm:lookup>
		
		<adsm:textbox
					dataType="text" 
					disabled="true"
					property="unidadeFederativa.nmUnidadeFederativa" 
					serializable="false"
					width="20%" 
					size="20" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idTipoTributacaoUfLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="tipoTributacaoUf.idTipoTributacaoUf" title="tipoTributacaoUf.idTipoTributacaoUf" dataType="integer"/>
		<adsm:gridColumn property="unidadeFederativa.idUnidadeFederativa" title="unidadeFederativa.idUnidadeFederativa" dataType="integer"/>
		<adsm:gridColumn property="tipoTributacaoIcms.idTipoTributacaoIcms" title="tipoTributacaoIcms.idTipoTributacaoIcms" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>