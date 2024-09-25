<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.emitirDocumentosFaturarAction" onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirDocumentosFaturar">

		<adsm:hidden 
			property="filialFaturamento.nmFilial" 
			serializable="true"/>
			
		<adsm:hidden 
			property="filialFaturamento.siglaFilial" 
			serializable="true"/>
			
		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.emitirDocumentosFaturarAction.findLookupFilial" 
					 dataType="text" 
					 property="filialFaturamento" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label = "filialFaturamento" 
					 size="3" 
					 maxLength="3" 
					 width="82%"
					 labelWidth="18%"
					 exactMatch="true">
					 
			<adsm:propertyMapping 
				modelProperty="pessoa.nmFantasia" 
				formProperty="filialFaturamento.pessoa.nmFantasia"/>
				
			<adsm:propertyMapping 
				modelProperty="pessoa.nmFantasia" 
				formProperty="filialFaturamento.nmFilial"/>				
			
			<adsm:propertyMapping 
				modelProperty="sgFilial" 
				formProperty="filialFaturamento.siglaFilial"/>
			
			<adsm:textbox 
				dataType="text" 	
				property="filialFaturamento.pessoa.nmFantasia" 
				size="50" 
				maxLength="50" 
				disabled="true" 
				serializable="false"/>
			
		</adsm:lookup>

		<adsm:hidden 
			property="filialCobranca.nmFilial" 
			serializable="true"/>
			
		<adsm:hidden 
			property="filialCobranca.siglaFilial" 
			serializable="true"/>
			
		<adsm:lookup action="/municipios/manterFiliais" 
					 service="lms.contasreceber.emitirDocumentosFaturarAction.findLookupFilial" 
					 dataType="text" 
					 property="filialCobranca" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label = "filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="82%"
					 labelWidth="18%"
					 exactMatch="true">
					 
			<adsm:propertyMapping 
				modelProperty="pessoa.nmFantasia" 
				formProperty="filialCobranca.pessoa.nmFantasia"/>
				
			<adsm:propertyMapping 
				modelProperty="pessoa.nmFantasia" 
				formProperty="filialCobranca.nmFilial"/>				
			
			<adsm:propertyMapping 
				modelProperty="sgFilial" 
				formProperty="filialCobranca.siglaFilial"/>
			
			<adsm:textbox 
				dataType="text" 
				property="filialCobranca.pessoa.nmFantasia" 
				size="50" 
				maxLength="50" 
				disabled="true" 
				serializable="false"/>
			
		</adsm:lookup>

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
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="cliente" 
			labelWidth="18%"
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.emitirDocumentosFaturarAction.findClienteLookup" 
			size="20" 
			width="82%">
			
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
				size="33"/>
				
		</adsm:lookup>
		
		<adsm:combobox property="tpFormatoRelatorio" 
			required="true"
			defaultValue="pdf"
			label="formatoRelatorio" 
			domain="DM_FORMATO_RELATORIO"
			labelWidth="18%" width="82%"/>	
		
		<adsm:buttonBar>
		
			<adsm:reportViewerButton service="lms.contasreceber.emitirDocumentosFaturarAction"/>
		
			<adsm:resetButton/>
		
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script>
function myPageLoadCallBack_cb(data, erro){
	onPageLoad_cb(data,erro);
	setElementValue("tpFormatoRelatorio", "pdf");
	//document.getElementById("tpFormatoRelatorio").selectedIndex = 1;
}
</script>