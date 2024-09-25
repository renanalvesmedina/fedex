<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.manterParametrosSubstituicaoTributariaAction">

	<adsm:form action="/tributos/manterParametrosSubstituicaoTributaria">

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
					  width="20%" 
					  size="20" />
					  
		<adsm:textbox dataType="percent" 
					  label="percentualRetencao"
					  property="pcRetencao" 
					  labelWidth="25%"
					  width="25%" 
					  minValue="0"
					  maxValue="100"
					  size="20" />
		
		<adsm:textbox label="vigencia" labelWidth="20%" width="30%" dataType="JTDate" property="dtVigencia" />
		
		<adsm:combobox property="blEmbuteICMSParcelas" 
					   label="embuteIcmsParcelas" 
					   domain="DM_SIM_NAO"
					   labelWidth="25%" width="25%"/>
		
		<adsm:combobox property="blImpDadosCalcCTRC" 
					   label="imprimirDadosICMSCtrc" 
					   domain="DM_SIM_NAO"
					   labelWidth="20%" width="30%"/>
					   
		<adsm:combobox property="blAplicarClientesEspeciais" 
					   label="blAplicarClientesEspeciais" 
					   domain="DM_SIM_NAO"
					   labelWidth="25%" width="25%"/>
					   
		<adsm:combobox property="blImprimeMemoCalcCte" 
					   label="imprimeMemoCalcCte" 
					   domain="DM_SIM_NAO"
					   labelWidth="20%" width="30%"/>
					   
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parametroSubstituicaoTrib"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idParametroSubstituicaoTrib" 
			   property="parametroSubstituicaoTrib" 
			   defaultOrder="unidadeFederativa_.nmUnidadeFederativa, dtVigenciaInicial" 
			   selectionMode="check" 
			   rows="11"
			   gridHeight="200" 
			   unique="true">
		
		<adsm:gridColumn title="uf" property="unidadeFederativa.sgUnidadeFederativa" width="30" dataType="text" />
		<adsm:gridColumn title="percentualRetencao" property="pcRetencao" width="90" dataType="percent" />
		<adsm:gridColumn title="vigencia" property="dtVigenciaInicial" dataType="JTDate" width="80"/>
		<adsm:gridColumn title="" property="dtVigenciaFinal" dataType="JTDate" width="70"/>
		<adsm:gridColumn title="embuteIcmsParcelas" property="blEmbuteICMSParcelas"  width="125" align="center" renderMode="image-check"/>
		<adsm:gridColumn title="imprimirDadosCtrc" property="blImpDadosCalcCTRC"  width="115" align="center" renderMode="image-check"/>
		<adsm:gridColumn title="impMemoriaCalc" property="blImprimeMemoCalcCte"  width="110" align="center" renderMode="image-check"/>
		<adsm:gridColumn title="blAplicarClientesEspeciais" property="blAplicarClientesEspeciais"  width="" align="center" renderMode="image-check"/>
        
        <adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
	
</adsm:window>
