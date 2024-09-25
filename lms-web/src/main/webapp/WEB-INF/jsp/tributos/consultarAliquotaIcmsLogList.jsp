<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarAliquotaIcmsLogAction">
	<adsm:form action="/tributos/consultarAliquotaIcmsLog" >
		
		<adsm:combobox
	        service="lms.tributos.manterAliquotasICMSAction.findUnidadeFederativaPaisLogado"
	        property="unidadeFederativaOrigem.idUnidadeFederativa"
	        optionProperty="idUnidadeFederativa"
	        optionLabelProperty="siglaDescricao" 
	        labelWidth="26%" 
	        width="27%" 
	        boxWidth="170"
	        label="ufOrigem"
	        required="true"
        />

		<adsm:combobox
	        service="lms.tributos.manterAliquotasICMSAction.findUnidadeFederativaPaisLogado"
	        property="unidadeFederativaDestino.idUnidadeFederativa"
	        optionProperty="idUnidadeFederativa"
	        optionLabelProperty="siglaDescricao" 
			labelWidth="27%" 
			width="20%" 
			boxWidth="130"
			label="ufDestino"
			required="true" 
		/>
		
		<adsm:combobox 
			property="tpSituacaoTribRemetente" 
			label="situacaoTributariaRemetente"  
			domain="DM_SITUACAO_TRIBUTARIA"
			labelWidth="26%" 
			width="27%" 
		/>
		
		<adsm:combobox
			property="tpSituacaoTribDestinatario"
			label="situacaoTributariaDestinatario" 
			domain="DM_SITUACAO_TRIBUTARIA"
			labelWidth="27%" 
			width="20%" 
		/>
		
		<adsm:combobox
			property="tpTipoFrete"
			label="tipoFrete"
			domain="DM_TIPO_FRETE"
			labelWidth="26%"
			width="27%"
		/>
		
		<adsm:range 
			label="periodo" 
			width="38%"
		>
			<adsm:textbox 
				dataType="JTDateTimeZone"
				property="dhLogInicial" 
				smallerThan="dataFinal"
			/>
			<adsm:textbox 
				dataType="JTDateTimeZone" 
				property="dhLogFinal"
				biggerThan="dataInicial"
			/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idAliquotaIcmsLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="aliquotaIcms.idAliquotaIcms" title="aliquotaIcms.idAliquotaIcms" dataType="integer"/>
		<adsm:gridColumn property="pcAliquota" title="pcAliquota" dataType="currency"/>
		<adsm:gridColumn property="pcEmbute" title="pcEmbute" dataType="currency"/>
		<adsm:gridColumn property="unidadeFederativaOrigem.idUnidadeFederativa" title="unidadeFederativaOrigem.idUnidadeFederativa" dataType="integer"/>
		<adsm:gridColumn property="unidadeFederativaDestino.idUnidadeFederativa" title="unidadeFederativaDestino.idUnidadeFederativa" dataType="integer"/>
		<adsm:gridColumn property="tipoTributacaoIcms.idTipoTributacaoIcms" title="tipoTributacaoIcms.idTipoTributacaoIcms" dataType="integer"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="tpSituacaoTribRemetente" title="tpSituacaoTribRemetente" isDomain="true"/>
		<adsm:gridColumn property="tpSituacaoTribDestinatario" title="tpSituacaoTribDestinatario" isDomain="true"/>
		<adsm:gridColumn property="tpTipoFrete" title="tpTipoFrete" isDomain="true"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>