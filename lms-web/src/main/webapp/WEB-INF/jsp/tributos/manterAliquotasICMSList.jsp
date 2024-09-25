<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.tributos.manterAliquotasICMSAction"
>
	<adsm:form
		action="/tributos/manterAliquotasICMS"
		idProperty="idAliquotaIcms"
	>
        <adsm:combobox
	        service="lms.tributos.manterAliquotasICMSAction.findUnidadeFederativaPaisLogado"
	        property="unidadeFederativaOrigem.idUnidadeFederativa"
	        optionProperty="idUnidadeFederativa"
	        optionLabelProperty="siglaDescricao" 
	        labelWidth="21%" 
	        width="27%" 
	        boxWidth="120"
	        label="ufOrigem"
	        
        />

		<adsm:combobox
	        service="lms.tributos.manterAliquotasICMSAction.findUnidadeFederativaPaisLogado"
	        property="unidadeFederativaDestino.idUnidadeFederativa"
	        optionProperty="idUnidadeFederativa"
	        optionLabelProperty="siglaDescricao" 
			labelWidth="22%" 
			width="20%" 
			boxWidth="130"
			label="ufDestino" 
		/>

		<adsm:combobox 
			property="regiaoGeografica.idRegiaoGeografica" 
			label="regiaoGeograficaDestino" 
			service="lms.municipios.regiaoGeograficaService.find" 
			optionLabelProperty="dsRegiaoGeografica" 
			optionProperty="idRegiaoGeografica" 
			labelWidth="21%" width="80%" boxWidth="220" onlyActiveValues="false"/>

		<adsm:combobox 
			property="tpSituacaoTribRemetente" 
			label="situacaoTributariaRemetente"  
			domain="DM_SITUACAO_TRIBUTARIA"
			labelWidth="21%" 
			width="27%" 
			boxWidth="220"
		/>
		
		<adsm:combobox
			property="tpSituacaoTribDestinatario"
			label="situacaoTributariaDestinatario" 
			domain="DM_SITUACAO_TRIBUTARIA"
			labelWidth="22%" 
			width="20%" 
			boxWidth="220"
		/>
		
		<adsm:combobox
			property="tpTipoFrete"
			label="tipoFrete"
			domain="DM_TIPO_FRETE"
			labelWidth="21%"
			width="27%"
		/>
        
        <adsm:combobox 
	        service="lms.tributos.manterAliquotasICMSAction.findTipoTributacaoIcms" 
	        property="tipoTributacaoIcms.idTipoTributacaoIcms" 
	        optionLabelProperty="dsTipoTributacaoIcms" 
	        optionProperty="idTipoTributacaoIcms" 
	        label="tipoTributacao"
	        labelWidth="22%" 
	        width="20%"
	        boxWidth="130" 
        />
		
		<adsm:textbox 
			size="6" 
			labelWidth="21%" 
			width="27%" 
			maxLength="6" 
			dataType="decimal" 
			property="pcAliquota" 
			label="percentualAliquota"
			maxValue="100"
			minLength="0"
			mask="##0.00"
		/>
		
		<adsm:textbox 
			size="6" 
			labelWidth="22%" 
			width="20%" 
			dataType="decimal" 
			property="pcEmbute" 
			label="percentualEmbutimento"
			mask="##0.00"
		/>
        
        <adsm:range 
        	label="vigencia" 
        	labelWidth="21%" 
        	width="74%"
        >
			<adsm:textbox 
				dataType="JTDate" 
				property="dtVigencia"
			/>
        </adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="aliquotaIcms"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid selectionMode="check" idProperty="idAliquotaIcms" defaultOrder=""
			   service="lms.tributos.manterAliquotasICMSAction.findPaginatedTela"
			   rowCountService="lms.tributos.manterAliquotasICMSAction.getRowCountTela"
			   scrollBars="horizontal"
			   property="aliquotaIcms" gridHeight="150" unique="true" rows="7">
        <adsm:gridColumn title="ufOrigem" property="unidadeFederativaOrigem.siglaDescricao" width="100" />
        <adsm:gridColumn title="ufDestino" property="unidadeFederativaDestino.siglaDescricao" width="100" />
        <adsm:gridColumn title="regiaoGeografica" property="regiaoGeografica.dsRegiaoGeografica" width="100" />
		<adsm:gridColumn title="situacaoTributariaRemetente" property="tpSituacaoTribRemetente" isDomain="true" width="150" />
		<adsm:gridColumn title="situacaoTributariaDestinatario" property="tpSituacaoTribDestinatario" isDomain="true" width="200" />
		<adsm:gridColumn title="tipoFrete" property="tpTipoFrete" isDomain="true" width="80" />

		<adsm:gridColumn title="tipoTributacao" property="tipoTributacaoIcms.dsTipoTributacaoIcms" width="150" />
        <adsm:gridColumn title="percentualAliquota" property="pcAliquota" width="100" dataType="decimal" />
        <adsm:gridColumn title="percentualEmbutimento" property="pcEmbute" width="100" dataType="decimal" />
        <adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="100" dataType="JTDate"/>
        <adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="100" dataType="JTDate"/>

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	
	</adsm:grid>
</adsm:window>
