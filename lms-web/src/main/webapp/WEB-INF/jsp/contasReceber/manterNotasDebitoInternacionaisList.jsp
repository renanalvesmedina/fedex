<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterNotasDebitoInternacionaisAction">
	<adsm:form action="/contasReceber/manterNotasDebitoInternacionais" idProperty="idFatura"
		service="lms.contasreceber.manterNotasDebitoInternacionaisAction">
		
		<adsm:hidden property="tpFatura" value="D"/>
		
		<adsm:lookup property="filialByIdFilial" idProperty="idFilial" criteriaProperty="sgFilial" 
			service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findLookupFilial" dataType="text"  label="filialFaturamento" size="3" 
			action="/municipios/manterFiliais" width="9%" 
			minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px" maxLength="3" >
			<adsm:propertyMapping relatedProperty="filialByIdFilial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
					<adsm:textbox dataType="text" property="filialByIdFilial.pessoa.nmFantasia" width="26%" size="30" serializable="false" disabled="true"/>
		</adsm:lookup>	
		
		<adsm:textbox dataType="integer" label="numero" property="nrFatura" size="10" maxLength="10" width="35%" disabled="false" />			

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			dataType="text" 
			exactMatch="true"  
			idProperty="idCliente" 
			label="cliente" 
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findLookupCliente" 
			size="20" 
			width="85%">
			
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
				
		</adsm:lookup>
		

		<adsm:lookup property="filialByIdFilialCobradora" idProperty="idFilial" criteriaProperty="sgFilial" 
		service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findLookupFilialCobradora" dataType="text"  label="filialCobranca" size="3" 
		action="/municipios/manterFiliais" width="9%" minLengthForAutoPopUpSearch="3" 
		exactMatch="false" style="width:45px" maxLength="3" >
			<adsm:propertyMapping relatedProperty="codFilial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
					<adsm:textbox dataType="text" property="codFilial.pessoa.Fantasia" width="26%" size="30" serializable="false" disabled="true"/>
		</adsm:lookup>


		<adsm:textbox dataType="text" label="numeroPreFatura" property="nrPreFatura" size="10" maxLength="20" />
				
		<adsm:range label="dataEmissao" width="30%" labelWidth="20%" >
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" size="10" maxLength="20"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" size="10" maxLength="20"/>
		</adsm:range>

		<adsm:range labelWidth="20%" label="dataVencimento" width="30%">
			<adsm:textbox dataType="JTDate" property="dtVencimentoInicial" size="10" maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtVencimentoFinal" size="10" maxLength="20" />
		</adsm:range>
		<adsm:range labelWidth="20%" label="dataLiquidacao" width="30%">		
			<adsm:textbox dataType="JTDate" property="dtLiquidacaoInicial" size="10" maxLength="20"/>
			<adsm:textbox dataType="JTDate" property="dtLiquidacaoFinal" size="10" maxLength="20"/>
		</adsm:range>

 	<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="fatura"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idFatura" property="fatura" service="lms.contasreceber.manterFaturasAction.findPaginated"
		defaultOrder="filialByIdFilial_.sgFilial, nrFatura"
		rowCountService="lms.contasreceber.manterFaturasAction.getRowCount" rows="7">
		<adsm:gridColumn title="filialFaturamento" property="filialByIdFilial.sgFilial" width="11%"/>
		<adsm:gridColumn title="numero" property="nrFatura" width="9%"/>
		<adsm:gridColumn title="filialCobranca" property="filialByIdFilialCobradora.sgFilial" width="10%"/>
		<adsm:gridColumn title="cliente" property="cliente.pessoa.nmPessoa" width="20%"/>
		<adsm:gridColumn title="dataEmissao" dataType="JTDate" property="dtEmissao" width="10%"/>
		<adsm:gridColumn title="dataVencimento" dataType="JTDate" property="dtVencimento" width="10%"/>
		<adsm:gridColumn title="dataPagamento" dataType="JTDate" property="dtLiquidacao" width="10%"/>
		<adsm:gridColumn title="valorTotal" dataType="currency" property="vlTotal" width="11%" />		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>