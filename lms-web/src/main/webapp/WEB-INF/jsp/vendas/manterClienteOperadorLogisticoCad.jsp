<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterClienteOperadorLogisticoAction">
	<adsm:form action="/vendas/manterClienteOperadorLogistico" idProperty="idClienteOperadorLogistico">

		<adsm:hidden property="cliente.idCliente"/>
		<adsm:hidden property="idFilial"/>
	
		<adsm:textbox 
				dataType="text" 
				label="operadorLogistico"
				labelWidth="16%"
				property="cliente.pessoa.nrIdentificacao"
				width="74%"
				disabled="true"
				serializable="false">
				<adsm:textbox	dataType="text" 
 				property="cliente.pessoa.nmPessoa"
 				size="50" 
				serializable="false"
 				disabled="true"
			/>
 		</adsm:textbox>
		
		<adsm:lookup
			label="cliente"
			property="clienteOperado"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterParametrosClienteAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="16%"
			maxLength="20"
			size="20"
			width="45%">
			<adsm:propertyMapping 
				relatedProperty="clienteOperado.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="clienteOperado.pessoa.nmPessoa" 
				serializable="false"
				size="30" />
		</adsm:lookup>
		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>		
			<adsm:newButton id="btnLimpar"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
function myPageLoad_cb(data, error) {
	onPageLoad_cb();
	var idFilial = getElementValue("idFilial");
	var data = new Array();	   
	setNestedBeanPropertyValue(data, "idFilial", idFilial);
	var sdo = createServiceDataObject("lms.vendas.manterClienteOperadorLogisticoAction.validateAcessoFilial","validateAcessoFilial",data);
	xmit({serviceDataObjects:[sdo]});
}                                      


function validateAcessoFilial_cb(data, error){		
	if(data._value!="true") {
		setDisabled("removeButton", true);
		setDisabled("storeButton", true);
		setDisabled("btnLimpar", true);
	}
}
</script>