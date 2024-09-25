<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.vendas.manterRemetentesOTDAction">
	<adsm:form action="/vendas/manterRemetentesOTD">
	
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>
		
		<adsm:textbox label="cliente" property="cliente.pessoa.nrIdentificacao" dataType="text" size="20" maxLength="20" labelWidth="10%" width="85%" disabled="true" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:textbox>	

	<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.manterRemetentesOTDAction.findLookupCliente" 
			property="remetente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="remetente" 
			size="17" 
			maxLength="18"
			dataType="text"
			width="47%"
			labelWidth="10%">

			<adsm:propertyMapping
				relatedProperty="remetente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text" 
				property="remetente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="true" 
				serializable="false"/>
		</adsm:lookup>

	<adsm:buttonBar>
		<adsm:storeButton id="storeButton" />
		<adsm:newButton id="newButton"/>
		<adsm:removeButton id="removeButton"/>
	</adsm:buttonBar>	

	</adsm:form>

</adsm:window>

<script type="text/javascript">
	
	/***** Lookup filial *****/
	function filialCallBack_cb(data){
		if(!filial_sgFilial_exactMatch_cb(data))
			return;
		if(data[0] != undefined) {
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].nmFantasia);
			setElementValue("siglaDescricao", data[0].lastRegional.dsRegional);
			
		}
		return;
	}
	
</script>