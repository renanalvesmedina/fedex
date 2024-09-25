<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.emitirPreFaturasImportadasAction">

	<adsm:form action="/contasReceber/emitirPreFaturasImportadas">

		<adsm:hidden 
			property="cliente.nrIdentificacao" 
			serializable="true"/>
			
		<adsm:hidden 
			property="cliente.nmCliente" 
			serializable="true"/>
		
		<adsm:hidden 
			property="cliente.tpIdentificacao" 
			serializable="true"/>
			
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			dataType="text" 
			exactMatch="true" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			idProperty="idCliente" 
			label="cliente" 
			labelWidth="20%"
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.emitirDocumentosFaturarAction.findClienteLookup" 
			size="20" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
				
			<adsm:propertyMapping 
				relatedProperty="cliente.nrIdentificacao" 
				modelProperty="pessoa.nrIdentificacao"/>
				
			<adsm:propertyMapping 
				relatedProperty="cliente.nmCliente" 
				modelProperty="pessoa.nmPessoa"/>
				
			<adsm:propertyMapping 
				relatedProperty="cliente.tpIdentificacao" 
				modelProperty="pessoa.tpIdentificacao.value"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="30"/>
				
		</adsm:lookup>

		<adsm:range label="periodoImportacao" labelWidth="20%" width="80%">
			<adsm:textbox property="periodoImportacaoInicial" dataType="JTDate"/>		
			<adsm:textbox property="periodoImportacaoFinal" dataType="JTDate"/>		
		</adsm:range>

		<adsm:combobox  property="tpFormatoRelatorio" 
						required="true"
						defaultValue="pdf"
						label="formatoRelatorio" 
						domain="DM_FORMATO_RELATORIO"
						labelWidth="20%" width="30%"/>
						
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirPreFaturasImportadasAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>