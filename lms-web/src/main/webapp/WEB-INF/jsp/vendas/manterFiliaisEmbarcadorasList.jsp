<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterFiliaisEmbarcadorasAction">
	<adsm:form action="/vendas/manterFiliaisEmbarcadoras">
		<adsm:i18nLabels>
			<adsm:include key="LMS-00055"/>
		</adsm:i18nLabels>

		<adsm:hidden property="cliente.tpCliente" value="S" serializable="false"/>
		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			service="lms.vendas.manterFiliaisEmbarcadorasAction.findLookupCliente"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			action="/vendas/manterDadosIdentificacao"
			dataType="text" 
			size="20"
			maxLength="20"
			width="19%"
			labelWidth="8%">
			<adsm:propertyMapping 
				criteriaProperty="cliente.tpCliente"
				modelProperty="tpCliente"/>
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />

			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				width="60%" size="50"
				maxLength="50"
				disabled="true"
				serializable="false"/>
		</adsm:lookup>

		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			service="lms.vendas.manterFiliaisEmbarcadorasAction.findLookupFilial"
			dataType="text"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			size="3"
			maxLength="3"
			width="8%"
			labelWidth="8%">
			<adsm:propertyMapping
				relatedProperty="filial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>

			<adsm:textbox
				dataType="text"
				width="60%"
				property="filial.pessoa.nmFantasia"
				size="30"
				disabled="true"
				serializable="false"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="filialEmbarcadora"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="filialEmbarcadora" idProperty="idFilialEmbarcadora" rows="13"
		rowCountService="lms.vendas.manterFiliaisEmbarcadorasAction.getRowCountCustom"
		service="lms.vendas.manterFiliaisEmbarcadorasAction.findPaginatedCustom">
		<adsm:gridColumn 
			title="identificacao" 
			property="cliente.pessoa.tpIdentificacao" 
			isDomain="true" 
			width="70" />
		<adsm:gridColumn 
			title="" 
			property="cliente.pessoa.nrIdentificacaoFormatado" 
			width="100" 
			align="right"/>
		<adsm:gridColumn 
			title="nome" 
			property="cliente.pessoa.nmPessoa" 
			width="350"/>
		<adsm:gridColumn 
			title="filial" 
			property="filial.pessoa.nmFantasia" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
function validateTab() {
	if(getElementValue("cliente.idCliente") == "" && 
	   getElementValue("filial.idFilial") == "") {
		alertI18nMessage("LMS-00055");
		setFocusOnFirstFocusableField();
		return false;
	}
	return validateTabScript(document.forms);
}
</script>
