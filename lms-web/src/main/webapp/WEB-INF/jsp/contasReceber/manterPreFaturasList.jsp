<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterPreFaturasAction">
	<adsm:form action="/contasReceber/manterPreFaturas" service="lms.contasreceber.manterPreFaturasAction">

		<adsm:textbox dataType="integer" label="numeroPreFatura" property="nrFatura" size="10" maxLength="10" disabled="false" 
		labelWidth="17%" width="33%" mask="0000000000"/>
		
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			dataType="text" 
			exactMatch="true"  
			idProperty="idCliente" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			label="clienteResponsavel" 
			maxLength="20" 
			property="cliente" 
			onPopupSetValue="popupCliente"
			onDataLoadCallBack="onDataLoadCliente"	
			onchange="return myClienteOnChange();"		
			service="lms.contasreceber.manterFaturasAction.findLookupCliente" 
			size="20" 
			labelWidth="17%" width="83%" required="true">
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
		</adsm:lookup>

		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" boxWidth="180"
		labelWidth="17%" width="33%"/>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" boxWidth="180"
		labelWidth="17%" width="33%" defaultValue="N" disabled="true"/>

        <adsm:combobox property="divisaoCliente.idDivisaoCliente" label="divisao" 
        		service="lms.contasreceber.manterFaturasAction.findComboDivisaoCliente" 
				optionLabelProperty="dsDivisaoCliente" 
				autoLoad="true"
				optionProperty="idDivisaoCliente" boxWidth="180"
				labelWidth="17%" width="33%"
				onlyActiveValues="false">				
		</adsm:combobox>				

		<adsm:combobox property="agrupamentoCliente.idAgrupamentoCliente" 
				label="formaAgrupamento" boxWidth="180"
				service="lms.contasreceber.manterFaturasAction.findAgrupamentoCliente" 
				optionLabelProperty="formaAgrupamento.dsFormaAgrupamento" 
				optionProperty="idAgrupamentoCliente" 
				labelWidth="17%" width="33%">
				<adsm:propertyMapping criteriaProperty="divisaoCliente.idDivisaoCliente" modelProperty="divisaoCliente.idDivisaoCliente"/>
		</adsm:combobox>
				
		<adsm:combobox property="tipoAgrupamento.idTipoAgrupamento" boxWidth="180"
			label="tipoAgrupamento" service="lms.contasreceber.manterFaturasAction.findComboTipoAgrupamento" 
			optionLabelProperty="dsTipoAgrupamento" optionProperty="idTipoAgrupamento" labelWidth="17%" width="33%">
			<adsm:propertyMapping criteriaProperty="agrupamentoCliente.idAgrupamentoCliente" modelProperty="agrupamentoCliente.idAgrupamentoCliente"/>
		</adsm:combobox>

		<adsm:range label="dataEmissao" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" size="10" maxLength="20"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" size="10" maxLength="20"/>
		</adsm:range>
		<adsm:range label="dataVencimento" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" property="dtVencimentoInicial" size="10" maxLength="20" />
			<adsm:textbox dataType="JTDate" property="dtVencimentoFinal" size="10" maxLength="20" />
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="fatura"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idFatura" property="fatura" service="lms.contasreceber.manterPreFaturasAction.findPaginated"
		defaultOrder="filialByIdFilial_.sgFilial, nrFatura" 
		rowCountService="lms.contasreceber.manterPreFaturasAction.getRowCount" rows="8">
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">		
			<adsm:gridColumn width="50" title="numeroPreFatura" property="filialByIdFilial.sgFilial" />	
			<adsm:gridColumn width="100" title="" property="nrFatura" mask="0000000000" dataType="integer" />
		</adsm:gridColumnGroup>
		
			
		<adsm:gridColumn title="divisao" property="divisaoCliente.dsDivisaoCliente" width="20%"/>
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="10%"/>
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="20%"/>
		<adsm:gridColumn title="dataEmissao" property="dtEmissao" dataType="JTDate" width="20%"/>
		<adsm:gridColumn title="dataVencimento" property="dtVencimento" dataType="JTDate" width="20%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
	function popupCliente(data){
		if (data == undefined) {
			return;
		}
		resetComboDivisao();
		findComboDivisaoCliente(data.idCliente);
	}

	function onDataLoadCliente_cb(data){
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data != undefined && data.length == 1) {
			findComboDivisaoCliente(data[0].idCliente);
		}
	}
	
	/** Busca as divisões do cliente */
	function findComboDivisaoCliente(idCliente){
	
		var data = new Array();	   
		
		setNestedBeanPropertyValue(data, "idCliente", idCliente);
		
		var sdo = createServiceDataObject("lms.contasreceber.manterFaturasAction.findComboDivisaoCliente","divisaoCliente.idDivisaoCliente", data);
		xmit({serviceDataObjects:[sdo]});

	}
	
	function myClienteOnChange(){
		resetComboDivisao();
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}		
	
	function resetComboDivisao(){
		getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
		getElement("agrupamentoCliente.idAgrupamentoCliente").options.length = 1;
		getElement("tipoAgrupamento.idTipoAgrupamento").options.length = 1;
		getElement("divisaoCliente.idDivisaoCliente").options[0].selected = true;
		getElement("agrupamentoCliente.idAgrupamentoCliente").options[0].selected = true;
		getElement("tipoAgrupamento.idTipoAgrupamento").options[0].selected = true;	
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			resetComboDivisao();
		}
	}	

</script>