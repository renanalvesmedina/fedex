<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window  service="lms.sgr.escoltaOperadoraMctService">
	<adsm:form action="/sgr/manterOperadorasEmpresaEscolta">
	
		<adsm:textbox dataType="integer" property="escolta.pessoa.nrIdentificacao" 
	     			  labelWidth="25%" width="75%" label="empresaEscolta" size="18">
			<adsm:textbox dataType="text" property="escolta.pessoa.nmPessoa" size="50" maxLength="40" disabled="true"/>
		</adsm:textbox>
		
		<adsm:lookup dataType="text" property="operadoraMct" idProperty="idOperadoraMct"  
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 service="lms.contratacaoveiculos.operadoraMctService.findLookup" action="/contratacaoVeiculos/manterOperadorasMCT"
					 labelWidth="25%" width="75%" label="operadoraMCT" size="18" maxLength="18">
        	<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="operadoraMct.pessoa.nmPessoa"/>

        	
            <adsm:textbox dataType="text" property="operadoraMct.pessoa.nmPessoa" size="50" maxLength="40" disabled="true"/>
        </adsm:lookup>
        
        <adsm:textbox dataType="integer" property="nrContaOrigem" label="numeroContaOrigem" labelWidth="25%" width="75%" maxLength="10" />
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="operadorasMCT"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="operadorasMCT" idProperty="idEscoltaOperadoraMct" 
			   rows="11" gridHeight="220" unique="true" scrollBars="horizontal" >
		<adsm:gridColumn title="identificacaoEmpresaEscolta" 	property="escolta.pessoa.tpPessoa" width="70" isDomain="true" />
		<adsm:gridColumn title="" 								property="escolta.pessoa.nrIdentificacaoFormatado" width="140" align="right" />
		<adsm:gridColumn title="empresaEscolta" 				property="escolta.pessoa.nmPessoa" width="140" />
		<adsm:gridColumn title="identificacaoOperadoraMCT" 		property="operadoraMct.pessoa.tpPessoa" width="70" isDomain="true"/>
		<adsm:gridColumn title="" 								property="operadoraMct.pessoa.nrIdentificacaoFormatado" width="140" align="right" />
		<adsm:gridColumn title="operadoraMCT" 					property="operadoraMct.pessoa.nmPessoa" width="140"/>
		<adsm:gridColumn title="contaOrigem" 					property="nrContaOrigem" width="100" dataType="integer"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
