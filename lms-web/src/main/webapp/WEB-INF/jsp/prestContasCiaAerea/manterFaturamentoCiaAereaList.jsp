<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.prestcontasciaaerea.faturamentoCiaAereaAction" >
	<adsm:form action="/prestContasCiaAerea/faturamentoCiaAerea">
	
		<adsm:combobox property="ciaFilialMercurio.idCiaFilialMercurio" 
				optionLabelProperty="pessoa.nmPessoa" 
				optionProperty="idEmpresa"   
				service="lms.municipios.empresaService.findCiaAerea"
				onlyActiveValues="true"
				label="ciaAerea" width="60%" labelWidth="17%" serializable="true" >
		</adsm:combobox>
		
		<adsm:lookup property="filial" 
						idProperty="idFilial"
						service="lms.municipios.filialService.findLookup" 
						dataType="text" 
						criteriaProperty="sgFilial" 
						label="filial" size="3" maxLength="3" 
						action="/municipios/manterFiliais" labelWidth="17%" width="83%" exactMatch="true" >
				<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="37" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpPeriodicidade"
				label="periodicidade" 
				domain="DM_PERIODICIDADE_FATURAMENTO" 
				labelWidth="17%" width="83%"
				boxWidth="120" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="faturamentoCiaAerea"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="faturamentoCiaAerea" 
				idProperty="idFaturamentoCiaAerea"  
				gridHeight="200" 
				unique="true" 
				rows="11" >
				
				
		<adsm:gridColumn property="nmciaFilialMercurio" dataType="text" title="ciaAerea" width="20%" />
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="filialsgFilial" dataType="text" width="60" title="filial"/>
			<adsm:gridColumn property="filialnmFantasia" dataType="text" width="60" title=""/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn property="periocidade" dataType="text" title="periodicidade" width="11%" />		
		<adsm:gridColumn property="ddFaturamento" dataType="text" title="diaReferenciaFaturamento"  />
		<adsm:gridColumn property="comissao" dataType="percent" title="percentualComissao" width="9%" />
		<adsm:gridColumn property="dtVigenciaInicial" dataType="JTDate" title="dtVigenciaInicial" width="12%" />
		<adsm:gridColumn property="dtVigenciaFinal" dataType="JTDate" title="dtVigenciaFinal"  width="12%" />
		
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>