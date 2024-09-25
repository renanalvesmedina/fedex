<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.escoltaReguladoraService">
	<adsm:form idProperty="idEscoltaReguladora" action="/seguros/manterEmpresasEscoltaReguladora" height="70">
	
		<adsm:hidden property="reguladoraSeguro.idReguladora"/>
		<adsm:textbox label="corretoraSeguro" size="50" maxLength="50" labelWidth="16%" width="84%" disabled="true" 
					  dataType="text" 
					  property="reguladoraSeguro.pessoa.nmPessoa"/>

		<adsm:lookup size="20" maxLength="20" labelWidth="16%" width="84%"
					 idProperty="idEscolta"
					 property="escolta" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 action="/sgr/manterEmpresasEscolta" 
					 service="lms.sgr.escoltaService.findLookup" 
					 dataType="text" 
					 label="empresaEscolta" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" 
								  relatedProperty="escolta.pessoa.nmPessoa"/>
			<adsm:textbox size="40" maxLength="50" disabled="true"
						  dataType="text" 
						  property="escolta.pessoa.nmPessoa" />
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="escoltaReguladora"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idEscoltaReguladora" 
			   property="escoltaReguladora" 
			   selectionMode="check" 
			   defaultOrder="reguladoraSeguro_pessoa_.nmPessoa, escolta_pessoa_.nmPessoa:asc"
			   gridHeight="200" 
			   unique="true" >
		<adsm:gridColumn property="reguladoraSeguro.pessoa.nrIdentificacaoFormatado" title="corretoraSeguro" width="117" align="right" />
		<adsm:gridColumn property="reguladoraSeguro.pessoa.nmPessoa" title="" width="273" />
		<adsm:gridColumn property="escolta.pessoa.nrIdentificacaoFormatado" title="empresaEscolta" width="117" align="right" />
		<adsm:gridColumn property="escolta.pessoa.nmPessoa" title="" width="273" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>