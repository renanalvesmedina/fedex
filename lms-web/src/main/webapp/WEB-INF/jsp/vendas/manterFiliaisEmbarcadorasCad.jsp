<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterFiliaisEmbarcadorasAction">
	<adsm:form action="/vendas/manterFiliaisEmbarcadoras" idProperty="idFilialEmbarcadora">
		<adsm:hidden property="cliente.tpCliente" value="S" serializable="false"/>
		<adsm:hidden property="cliente.tpSituacao" value="A" serializable="false"/>
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
			labelWidth="8%"
			required="true">
			<adsm:propertyMapping 
				criteriaProperty="cliente.tpCliente"
				modelProperty="tpCliente"/>
			<adsm:propertyMapping 
				criteriaProperty="cliente.tpSituacao"
				modelProperty="tpSituacao"/>
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
			onDataLoadCallBack="filial"
			afterPopupSetValue="afterPopup"
			dataType="text"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			size="3"
			maxLength="3"
			width="8%"
			labelWidth="8%"
			required="true">
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

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function filial_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}

		var isValid = filial_sgFilial_exactMatch_cb(data);
		if(isValid) {
			setFocus("storeButton", false);
		}
		return isValid;
	}

	function afterPopup() {
		if(getElementValue("filial.idFilial") != "") {
			setFocus("storeButton", false);
		}
	}
</script>