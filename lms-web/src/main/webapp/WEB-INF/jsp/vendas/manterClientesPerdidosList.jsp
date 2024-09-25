<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.vendas.manterClientePerdidoAction">
	<adsm:form action="/vendas/manterClientesPerdidos" idProperty="idClientePerdido">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
	</adsm:i18nLabels>
		
	<adsm:lookup 
		property="filial" 
		idProperty="idFilial" 
		required="false" 
		criteriaProperty="sgFilial" 
		maxLength="3"
		service="lms.vendas.manterClientePerdidoAction.findLookupFilial" 
		dataType="text" 
		label="filial" size="3"
		action="/municipios/manterFiliais" 
		labelWidth="10%" width="47%" 
		minLengthForAutoPopUpSearch="3"
		exactMatch="false" disabled="false" onDataLoadCallBack="filialCallBack">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="siglaDescricao" modelProperty="lastRegional.dsRegional" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
	</adsm:lookup>

	<adsm:hidden property="idRegional" serializable="false" />

	<adsm:textbox
		dataType="text" 
		label="regional"
		property="siglaDescricao" 
		disabled="true"
		size="35"
		maxLength="60"
		width="28%"
		labelWidth="15%"
		serializable="false"
		required="false"
	/>

	<adsm:lookup
			action="/vendas/manterDadosIdentificacao"
			service="lms.vendas.manterClientePerdidoAction.findLookupCliente" 
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="cliente" 
			size="17" 
			maxLength="18"
			dataType="text"
			width="47%"
			labelWidth="10%">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text" 
				property="cliente.pessoa.nmPessoa" 
				size="35" 
				maxLength="50"
				disabled="true" 
				serializable="true"/>
		</adsm:lookup>

		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" serializable="true" labelWidth="15%" width="28%"/>	
		
		<adsm:combobox property="tpMotivoPerda" label="motivoPerda" domain="DM_MOTIVO_PERDA" serializable="true" labelWidth="10%" width="47%"/>

		<adsm:combobox property="segmentoMercado.idSegmentoMercado" optionLabelProperty="dsSegmentoMercado" optionProperty="idSegmentoMercado"
				label="segmentoMercado" service="lms.vendas.segmentoMercadoService.find" labelWidth="15%" width="28%" boxWidth="172" required="false" />
		
		<adsm:range label="dataPerda" width="90%" labelWidth="10%">
			<adsm:textbox dataType="JTDate" property="dtInicial" />
			<adsm:textbox dataType="JTDate" property="dtFinal" />
		</adsm:range>
		

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				caption="consultar" 
				onclick="consultar(this);" 
				buttonType="findButton" 
				id="__buttonBar:0.findButton" 
				disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid	idProperty="idClientePerdido" 
		property="clientePerdido" 
		unique="true"
		rowCountService="lms.vendas.manterClientePerdidoAction.getRowCountCustom" 
		service="lms.vendas.manterClientePerdidoAction.findPaginatedCustom"
		gridHeight="220"
		rows="9">
		<adsm:gridColumn
			title="filial" 
			property="sgFilial" 
			dataType="text"
			width="10%" />
		<adsm:gridColumn
			title="cliente" 
			property="nmPessoa" 
			width="40%"
			dataType="text" />
		<adsm:gridColumn
			title="dataPerda" 
			property="dtPerda" 
			width="15%"
			dataType="JTDate" 
			align="center" />
		<adsm:gridColumn
			title="motivoPerda" 
			property="tpMotivoPerda" 
			width="35%"
			dataType="text" 
			isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script type="text/javascript">

	function consultar(eThis) {
		if(validateConsultar()) {
			findButtonScript('clientePerdido', eThis.form);
		}
	}
	

	function validateConsultar() {
		if(getElementValue("filial.idFilial") == "" &&
			getElementValue("cliente.idCliente") == "" &&
			getElementValue("dtInicial") == "" &&
			getElementValue("dtFinal") == "" &&
			getElementValue("tpAbrangencia") == "" &&
			getElementValue("tpMotivoPerda") == "" &&
			getElementValue("segmentoMercado.idSegmentoMercado") == "")
		{
			alert(i18NLabel.getLabel("LMS-00055"));
			return false;
		}
		return true;
	}
	
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