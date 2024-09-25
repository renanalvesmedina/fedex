<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window 	
	service="lms.tributos.manterTipoTributacaoExportacaoUfAction">
	
	<adsm:form 	
		action="/tributos/manterTipoTributacaoExportacaoUf" service="lms.tributos.manterTipoTributacaoExportacaoUfAction">



		<adsm:lookup 
  	    			property="unidadeFederativa"
				    idProperty="idUnidadeFederativa" 
				    criteriaProperty="sgUnidadeFederativa"
					service="lms.tributos.manterTipoTributacaoExportacaoUfAction.findLookupUF" 
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
			<adsm:propertyMapping relatedProperty="unidadeFederativa.siglaDescricao" modelProperty="siglaDescricao" />
			
		</adsm:lookup>
		
		<adsm:hidden
					property="unidadeFederativa.siglaDescricao" 
					serializable="false"/>			
		
		<adsm:textbox
					dataType="text" 
					disabled="true"
					property="unidadeFederativa.nmUnidadeFederativa" 
					serializable="false"
					width="20%" 
					size="20" />
					  
		<adsm:combobox 
					label="tipoTributacao" 
					property="tipoTributacaoIcms.idTipoTributacaoIcms" 
					service="lms.tributos.manterTipoTributacaoExportacaoUfAction.findComboTipoTributacaoIcms" 
					optionLabelProperty="dsTipoTributacaoIcms" 
					optionProperty="idTipoTributacaoIcms" 
					labelWidth="20%" 
					width="30%" 
					boxWidth="200"/>	  


		<adsm:textbox 
					label="vigencia" 
					dataType="JTDate" 
					property="dtVigencia"
					labelWidth="20%"
					width="30%"/>
					
		
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoTributacaoUf" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>



		<adsm:grid property="tipoTributacaoUf" idProperty="idTipoTributacaoUf" 
   			service="lms.tributos.manterTipoTributacaoExportacaoUfAction.findPaginatedTela"
			rowCountService="lms.tributos.manterTipoTributacaoExportacaoUfAction.getRowCountTela"
			selectionMode="check" 
			rows="13"
			gridHeight="200" 
			unique="true">
		
		<adsm:gridColumn title="uf" property="unidadeFederativa.sgUnidadeFederativa" width="" dataType="text" />
		<adsm:gridColumn title="vigencia" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="" property="dtVigenciaFinal" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="tipoTributacao" property="tipoTributacaoIcms" width="130" dataType="text"/>
     
        <adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>

		
	</adsm:grid>
	
</adsm:window>
