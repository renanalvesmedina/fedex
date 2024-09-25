<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterApolicesSeguroAction">
	<adsm:form action="/seguros/manterApolicesSeguro" height="102">
	
		<!-- LMS-6146 -->
        <adsm:lookup size="20" maxLength="30" width="85%"
					 idProperty="idSegurado"
					 property="segurado" 
					 criteriaProperty="nrIdentificacao"
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 action="/configuracoes/manterPessoas" 
					 service="lms.seguros.manterApolicesSeguroAction.findLookupPessoa" 
					 dataType="text" 
					 exactMatch="true"
					 label="segurado">
			<adsm:propertyMapping relatedProperty="segurado.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:propertyMapping relatedProperty="segurado.idPessoa" modelProperty="idPessoa"/>
			<adsm:propertyMapping relatedProperty="segurado.nrPessoa" modelProperty="nrIdentificacao"/>
			<adsm:textbox size="50" maxLength="50" disabled="true" dataType="text" property="segurado.nmPessoa"/>
		</adsm:lookup>
		<adsm:hidden property="segurado.idPessoa" serializable="true"/>
		<adsm:hidden property="segurado.nrPessoa" serializable="true"/>

		<adsm:textbox dataType="text" property="nrApolice" label="numeroApolice" size="20" width="30%" maxLength="60" />

		<adsm:combobox property="tipoSeguro.idTipoSeguro"
			optionProperty="idTipoSeguro" optionLabelProperty="sgTipo"
			service="lms.seguros.manterApolicesSeguroAction.findTipoSeguroOrderBySgTipo"
			label="tipoSeguro" width="30%">
			<adsm:propertyMapping modelProperty="sgTipo" relatedProperty="tipoSeguro.sgTipo"/>	
		</adsm:combobox>
		<adsm:hidden property="tipoSeguro.sgTipo" serializable="true"/>
				   
		<adsm:combobox property="seguradora.idSeguradora" optionProperty="seguradora.idSeguradora" optionLabelProperty="seguradora.pessoa.nmPessoa" 
					   service="lms.seguros.manterApolicesSeguroAction.findReguladoraSeguradoraOrderByNmPessoa" 
					   label="seguradora" width="30%">	
					   <adsm:propertyMapping modelProperty="seguradora.pessoa.nmPessoa" relatedProperty="seguradora.pessoa.nmPessoa"/>
		</adsm:combobox>
		<adsm:hidden property="seguradora.pessoa.nmPessoa" serializable="true"/>
					   
		<adsm:combobox property="reguladoraSeguro.idReguladora" optionProperty="pessoa.idPessoa" optionLabelProperty="pessoa.nmPessoa" 
					   service="lms.seguros.manterApolicesSeguroAction.findReguladoraOrderByNmPessoa" 
					   label="reguladora" width="35%">
					   <adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="reguladoraSeguro.pessoa.nmPessoa"/>			   
	    </adsm:combobox>
	    <adsm:hidden property="reguladoraSeguro.pessoa.nmPessoa" />
		   
		<adsm:textbox label="vigencia" dataType="JTDate" property="dtVigencia" width="30%" picker="true"/>
		
		<adsm:combobox property="moeda.idMoeda" label="limiteValor"
					   service="lms.seguros.manterApolicesSeguroAction.findMoeda" 
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   boxWidth="100" width="35%">
			<adsm:textbox dataType="currency" 
						  property="vlLimiteApolice" 
						  mask="###,###,###,###,##0.00" minValue="0.01"
						  size="18" maxLength="15" />
		</adsm:combobox>
		
		<adsm:buttonBar freeLayout="true">
		<adsm:button id="exportarExcel" caption="exportarExcel" onclick="emitirReport();"/>
			<adsm:findButton callbackProperty="apolicesSeguro" />
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	<adsm:grid property="apolicesSeguro" idProperty="idApoliceSeguro" 
			   service="lms.seguros.manterApolicesSeguroAction.findPaginatedApolicesSeguro" selectionMode="none"
			   rowCountService="lms.seguros.manterApolicesSeguroAction.getRowCountApolicesSeguro"
			   unique="true" scrollBars="horizontal" rows="9" gridHeight="180" >
		<adsm:gridColumn property="segurado" title="segurado" width="210" />
		<adsm:gridColumn property="nrApolice" title="numeroApolice" width="130" align="right" />
		<adsm:gridColumn property="reguladoraSeguro.pessoa.nmPessoa" title="reguladora" width="190" />
		<adsm:gridColumn property="seguradora.pessoa.nmPessoa" title="seguradora" width="190" />
		<adsm:gridColumn property="tipoSeguro.sgTipo" title="tipoSeguro" width="115" />
		<adsm:gridColumn property="dtVigencia" dataType="JTDate" title="vigencia" width="150"/>
		<adsm:gridColumn property="moeda.siglaSimbolo" title="limiteValor" width="50"/>
		<adsm:gridColumn property="vlLimiteApolice" title="" width="80" mask="###,###,###,###,##0.00" dataType="currency" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function initWindow(eventObj) {
		setDisabled("exportarExcel", false);
	}
	

	function onElementDataLoad_cb(data, error) {
		reguladoraSeguro_idReguladora_cb(data);
	}
	
	function verificaPessoa_cb(data,error){
		
		var retorno = facade_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		return retorno;
	}
	
	function emitirReport() {
		executeReportWithCallback('lms.seguros.manterApolicesSeguroAction.executeReport', 'openReportWithLocator', document.forms[0]);
	}

</script> 