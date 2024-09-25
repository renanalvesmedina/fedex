<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.escoltaReguladoraService">
	<adsm:form idProperty="idEscoltaReguladora" action="/seguros/manterEmpresasEscoltaReguladora" height="390">
		
		<adsm:hidden property="tipoSituacaoEscolta" value="A" />
		<adsm:hidden property="reguladoraSeguro.idReguladora"/>
		<adsm:textbox label="corretoraSeguro" size="50" maxLength="50" labelWidth="16%" width="84%" disabled="true" 
					  dataType="text" 
					  property="reguladoraSeguro.pessoa.nmPessoa"/>

		<adsm:lookup size="20" maxLength="20" labelWidth="16%" width="84%" required="true"
					 idProperty="idEscolta"
					 property="escolta" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 action="/sgr/manterEmpresasEscolta" 
					 service="lms.sgr.escoltaService.findLookup" 
					 dataType="text" 
					 label="empresaEscolta"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tipoSituacaoEscolta"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="escolta.pessoa.nmPessoa"/>
			<adsm:textbox size="40" maxLength="50" disabled="true"
						  dataType="text" 
						  property="escolta.pessoa.nmPessoa" />
		</adsm:lookup>
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>