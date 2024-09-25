<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarParametroSubstituicaoTribLogAction">
	<adsm:form action="/tributos/consultarParametroSubstituicaoTribLog" >
		
		<adsm:lookup property="unidadeFederativa"
				     idProperty="idUnidadeFederativa" 
				     criteriaProperty="sgUnidadeFederativa"
					 service="lms.municipios.unidadeFederativaService.findLookup" 
					 dataType="text" 
					 labelWidth="20%" 
					 maxLength="10"
					 width="10%" 
					 label="uf" 
					 size="5"
					 action="/municipios/manterUnidadesFederativas" 
					 minLengthForAutoPopUpSearch="2" 
					 exactMatch="false">
					 
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.siglaDescricao" modelProperty="siglaDescricao" />
			
		</adsm:lookup>
		
		<adsm:hidden property="unidadeFederativa.siglaDescricao" serializable="false"/>			
		
		<adsm:textbox dataType="text" 
					  property="unidadeFederativa.nmUnidadeFederativa" 
					  disabled="true"
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
			idProperty="idParametroSubstituicaoTribLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="parametroSubstituicaoTrib.idParametroSubstituicaoTrib" title="parametroSubstituicaoTrib.idParametroSubstituicaoTrib" dataType="integer"/>
		<adsm:gridColumn property="unidadeFederativa.idUnidadeFederativa" title="unidadeFederativa.idUnidadeFederativa" dataType="integer"/>
		<adsm:gridColumn property="pcRetencao" title="pcRetencao" dataType="currency"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="blEmbuteIcmsParcelas" title="blEmbuteIcmsParcelas" renderMode="image-check"/>
		<adsm:gridColumn property="blImpDadosCalcCtrc" title="blImpDadosCalcCtrc" renderMode="image-check"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="blAplicarClientesEspeciais" title="blAplicarClientesEspeciais" renderMode="image-check"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>