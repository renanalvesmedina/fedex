<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterValorAction">
	<adsm:form action="/vendas/manterValorCpt">
        
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			criteriaProperty="pessoa.nrIdentificacao" 
			exactMatch="true" 
			idProperty="idCliente" 
			property="cliente" 
			label="cliente" 
			size="20"
			maxLength="20"
			width="45%" 
			service="lms.vendas.manterValorAction.findClienteLookup">

			<adsm:propertyMapping 
				modelProperty="pessoa.nmPessoa" 
				relatedProperty="cliente.pessoa.nmPessoa" />

			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				serializable="false"
				property="cliente.pessoa.nmPessoa" 
				size="30" />
		</adsm:lookup>


		<adsm:combobox
			property="segmentoMercado.idSegmentoMercado"
			onlyActiveValues="true"
			optionLabelProperty="dsSegmentoMercado"
			optionProperty="idSegmentoMercado"
			service="lms.vendas.manterClienteAction.findSegmentoMercado"
			label="segmentoMercado"
			width="35%"
			boxWidth="240"
			autoLoad="false"
			renderOptions="true"/>

		<adsm:combobox 
			property="cptTipoValor" 
			label="tipoValor" 
			onlyActiveValues="true"
			optionLabelProperty="dsTipoValor" 
			optionProperty="idCptTipoValor" 
			service="lms.vendas.manterTiposValorAction.findTiposValor" 
			width="30%" 
			boxWidth="200"/>


		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" disabled="false" onclick="findValorCpt();" buttonType="findButton"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid property="cptValorCpt" idProperty="identificador"  rows="12" width="1400" scrollBars="horizontal" gridHeight="230">
	    
	    <adsm:gridColumn title="cliente" 			property="nmcliente" 			width="30%" />
		<adsm:gridColumn title="segmentoMercado" 	property="segmento" 			width="20%" />
		<adsm:gridColumn title="tipoValor" 			property="tipovalor" 		    width="10%" />	
		<adsm:gridColumn title="vlComplexidade" 	property="valor" 		    	width="10%" mask="##,###,###,###,##0.00" dataType="decimal"/>	
		<adsm:gridColumn title="medida" 			property="medida" 		        width="10%" />	
		<adsm:gridColumn title="veiculo" 			property="veiculo" 		    	width="10%" />	
		<adsm:gridColumn title="funcionario" 		property="nrmatricula" 		    width="20%" />	

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function findValorCpt(){

		if(isBlank(getElementValue("cliente.idCliente")) 
				&& isBlank(getElementValue("segmentoMercado.idSegmentoMercado"))){
			alert("É obrigatório informar o Cliente ou o Segmento de Mercado");
			return false;	
		}
		
		findButtonScript('cptValorCpt', document.forms[0]);
	}

</script>