<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.escoltaOperadoraMctService">
	<adsm:form action="/sgr/manterOperadorasEmpresaEscolta" idProperty="idEscoltaOperadoraMct" 
			   service="lms.sgr.escoltaOperadoraMctService.findByIdDetalhamento" >
	
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />	
	
		<adsm:hidden property="escolta.idEscolta"/>
		<adsm:textbox dataType="integer" property="escolta.pessoa.nrIdentificacao" 
	     			  labelWidth="25%" width="75%" label="empresaEscolta" size="18">
			<adsm:textbox dataType="text" property="escolta.pessoa.nmPessoa" size="50" maxLength="40" disabled="true"/>
		</adsm:textbox>
		
		<adsm:lookup dataType="text" property="operadoraMct" idProperty="idOperadoraMct"  
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 service="lms.contratacaoveiculos.operadoraMctService.findLookup" action="/contratacaoVeiculos/manterOperadorasMCT"
					 labelWidth="25%" width="75%" label="operadoraMCT" size="18" maxLength="18" required="true">
			 <adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>
        	<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="operadoraMct.pessoa.nmPessoa"/>
            <adsm:textbox dataType="text" property="operadoraMct.pessoa.nmPessoa" size="50" maxLength="40" disabled="true"/>
        </adsm:lookup>
        
        <adsm:textbox dataType="integer" property="nrContaOrigem" label="numeroContaOrigem" labelWidth="25%" width="75%" maxLength="10"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>