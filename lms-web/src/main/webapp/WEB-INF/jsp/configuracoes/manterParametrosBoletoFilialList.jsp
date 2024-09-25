<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
	
<adsm:window service="lms.configuracoes.manterParametrosBoletoFilialAction">

	<adsm:form action="/configuracoes/manterParametrosBoletoFilial">
	
        <adsm:lookup property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.configuracoes.manterParametrosBoletoFilialAction.findLookupFilial" 
					 dataType="text"  
					 label="filial" 
					 size="3" 
					 action="/municipios/manterFiliais" 
					 labelWidth="15%"
					 width="35%" 
					 minLengthForAutoPopUpSearch="3" 
					 exactMatch="false" 
					 maxLength="3">
					 
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" 
								  modelProperty="pessoa.nmFantasia" />
								  
			<adsm:textbox dataType="text" 
						  property="filial.pessoa.nmFantasia" 
						  size="30" 
						  serializable="true" 
						  disabled="true"/>
						  
		</adsm:lookup>
        
		<adsm:textbox 
					label="vigencia" 
					dataType="JTDate" 
					property="dtVigencia"
					labelWidth="15%"
					width="35%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="parametroBoletoFilial"/>
			<adsm:resetButton/>
		</adsm:buttonBar>        
		
	</adsm:form>

	<adsm:grid property="parametroBoletoFilial"
			   idProperty="idParametroBoletoFilial"  
			   gridHeight="200"  
			   service="lms.configuracoes.manterParametrosBoletoFilialAction.findPaginatedTela"
			   rowCountService="lms.configuracoes.manterParametrosBoletoFilialAction.getRowCountTela"
	           rows="14">
			
			<adsm:gridColumn title="filial" property="siglaNomeFilial" width="" />
			<adsm:gridColumn title="vigencia" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
			<adsm:gridColumn title="" property="dtVigenciaFinal" dataType="JTDate" width="90"/>
			<adsm:gridColumn title="blValorLiquido" property="blValorLiquido" renderMode="image-check" width="200" />			
   			<adsm:gridColumn title="aprovarCancelamento" property="blWorkflowCancelamento" renderMode="image-check" width="200" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>

</adsm:window>