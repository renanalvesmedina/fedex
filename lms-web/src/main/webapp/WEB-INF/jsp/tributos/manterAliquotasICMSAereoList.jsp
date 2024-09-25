<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window 
	service="lms.tributos.manterAliquotasICMSAereoAction"
>
	<adsm:form 
		action="/tributos/manterAliquotasICMSAereo" 
		idProperty="idAliquotaIcmsAereo"
	>
		
		<adsm:combobox
			service="lms.tributos.manterAliquotasICMSAereoAction.findUnidadeFederativa" 
			property="unidadeFederativa.idUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			boxWidth="120"
			optionLabelProperty="siglaDescricao"
			label="ufOrigem"
		/>

		<adsm:range label="vigencia" labelWidth="17%" width="33%">
	        <adsm:textbox dataType="JTDate" property="dtInicioVigenciaInicial"/>
	        <adsm:textbox dataType="JTDate" property="dtInicioVigenciaFinal"/>
        </adsm:range>
		
		<adsm:section caption="informacoesContribuinte"/>

		<adsm:textbox 
			dataType="percent" 
			property="pcAliquotaInterna" 
			label="percentualAliquota" 
			size="6"
			minValue="0"
			maxValue="100"
			
		/>
		
		<adsm:textbox 
			dataType="percent" 
			property="pcEmbuteInterno" 
			label="percentualEmbutimento" 
			size="6" 
			labelWidth="17%" 
			width="33%"
			minValue="0"
			maxValue="100"
			
		/>

		<adsm:section caption="informacoesNaoContribuinte"/>

		<adsm:textbox 
			dataType="percent"
			property="pcAliquotaInterestadual" 
			label="percentualAliquota" 
			size="6" 
			minValue="0"
			maxValue="100"
			
		/>
		
		<adsm:textbox 
			dataType="percent"
			property="pcEmbuteInterestadual" 
			label="percentualEmbutimento" 
			size="6" 
			labelWidth="17%" 
			width="33%"
			minValue="0"
			maxValue="100"
			
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="aliquotaIcmsAereo"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid selectionMode="check" idProperty="idAliquotaIcmsAereo" property="aliquotaIcmsAereo" gridHeight="200" unique="true" defaultOrder="unidadeFederativa_.sgUnidadeFederativa, dtInicioVigencia" rows="09">
		<adsm:gridColumn width="20%" title="ufOrigem" property="unidadeFederativa.siglaDescricao" />
		<adsm:gridColumn width="20%" title="vigenciaInicial" property="dtInicioVigencia" dataType="JTDate"/>
		<adsm:gridColumn width="15%" title="percentualAliquotaFreteInterno" property="pcAliquotaInterna" dataType="percent"/>
		<adsm:gridColumn width="15%" title="percentualEmbutimentoFreteInterno" property="pcEmbuteInterno" dataType="percent"/>
		<adsm:gridColumn width="15%" title="percentualAliquotaFreteInterestadual" property="pcAliquotaInterestadual" dataType="percent"/>
		<adsm:gridColumn width="15%" title="percentualEmbutimentoFreteInterestadual" property="pcEmbuteInterestadual" dataType="percent"/>

		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>