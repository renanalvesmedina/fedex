<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirOcorrenciasPreFaturasImportadasAction">
	<adsm:form action="/contasReceber/emitirOcorrenciasPreFaturasImportadas" >
        
        <adsm:hidden property="tpOcorrencia" value="PADRAO"/>
        
		<adsm:lookup label="cliente"
					 service="lms.contasreceber.emitirOcorrenciasPreFaturasImportadasAction.findLookupCliente" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="20"
					 labelWidth="20%"
					 maxLength="20" 
					 width="80%" 
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="cliente.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacaoFormatado" formProperty="nrIdentificacaoFormatado"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="30" serializable="true"/>	
		</adsm:lookup>
		
		<adsm:hidden property="nrIdentificacaoFormatado" serializable="true"/>
        
		<adsm:range label="periodoImportacao" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="periodoInicial" size="10"/>
			<adsm:textbox dataType="JTDate" property="periodoFinal"  size="10"/>
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" required="true" labelWidth="20%" defaultValue="pdf" width="80%"/>
	   
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirOcorrenciasPreFaturasImportadasAction" id="btnVisualizar"/>
			<adsm:resetButton id="btnLimpar"/>
		</adsm:buttonBar>
	   
   </adsm:form>
</adsm:window>
<script language="javascript">
	setMasterLink(this.document, true);
</script>