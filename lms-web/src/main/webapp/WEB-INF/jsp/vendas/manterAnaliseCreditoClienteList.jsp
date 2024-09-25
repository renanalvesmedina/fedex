<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterAnaliseCreditoClienteAction" onPageLoad="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="required2FieldsOr" />
		<adsm:include key="cliente" />
		<adsm:include key="situacao" />
		
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterAnaliseCreditoCliente">

		<!-- Periodo de solicitacao -->
		<adsm:range label="periodoSolicitacao" maxInterval="31" labelWidth="15%" width="85%">
			<adsm:textbox dataType="JTDateTimeZone" property="dhSolicitacaoInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhSolicitacaoFinal"/>
		</adsm:range>

		<!-- Lookup de filial -->
		<adsm:lookup
			label="filialComercial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais" 
			service="lms.vendas.manterAnaliseCreditoClienteAction.findFilialLookup"
			dataType="text"
			labelWidth="15%"
			width="85%"
			size="3" 
			maxLength="3"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping
				relatedProperty="filial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping
				relatedProperty="filial.sgFilial"
				modelProperty="sgFilial"/>

			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				serializable="false"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<!-- Lookup de funcionario -->
		<adsm:lookup
			property="usuario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			service="lms.vendas.manterAnaliseCreditoClienteAction.findFuncionarioLookup" 
			dataType="text" label="usuarioSolicitante" size="16" maxLength="16" 
			labelWidth="15%"
			width="85%" action="/configuracoes/consultarFuncionariosView">
			<adsm:propertyMapping
				relatedProperty="usuario.nmUsuario"
				modelProperty="nmUsuario"/>

			<adsm:textbox
				dataType="text"
				property="usuario.nmUsuario"
				size="30"
				disabled="true"/>
		</adsm:lookup>

		<!-- Lookup de cliente -->
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
			labelWidth="15%"
			width="85%"
			service="lms.vendas.manterAnaliseCreditoClienteAction.findClienteLookup"
			onDataLoadCallBack="lookupCliente"
			afterPopupSetValue="aferPopupCliente">
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

		<!-- Combo de situacao -->
		<adsm:combobox 
			property="tpSituacao" 
			domain="DM_SITUACAO_ANALISE_CREDITO" 
			label="situacao" 
			labelWidth="15%" 
			width="30%"
			autoLoad="false"
			renderOptions="true"/>

		<!-- Periodo de conclusao -->
		<adsm:range label="periodoConclusao" maxInterval="31" labelWidth="15%" width="40%">
			<adsm:textbox dataType="JTDateTimeZone" property="dhConclusaoInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhConclusaoFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button
				id="btnConsultar"
				caption="consultar"
				onclick="return callExecuteSearchTimer();"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		idProperty="idAnaliseCreditoCliente"
		property="analiseCreditoCliente"
		defaultOrder="dhSolicitacao"
		onRowClick="onRowClick"
		gridHeight="170"
		unique="true"
		rows="9">
		<adsm:gridColumn 
			dataType="JTDateTimeZone"
			title="dataHoraSolicitacao" 
			property="dhSolicitacao" 
			width="15%" />
		<adsm:gridColumn 
			title="filial" 
			property="filial.sgFilial" 
			width="5%" />
		<adsm:gridColumn 
			title="cliente"
			property="cliente.pessoa.nmPessoa"/>
		<adsm:gridColumn 
			title="usuarioSolicitante" 
			property="usuario.nmUsuario" 
			width="15%" />
		<adsm:gridColumn
			title="situacao"
			property="tpSituacao"
			isDomain="true"
			width="20%"/>
		<adsm:gridColumn 
			dataType="JTDateTimeZone"
			title="dataHoraConclusao" 
			property="dhConclusao" 
			width="15%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function initWindow(eventObj) {
		setDisabled("btnConsultar", false);
		getTabGroup(document).getTab("cad").setDisabled(true);

		/* Inicia o processo de Refresh automatico */
		if(eventObj.name == "tab_click") {
			startCurrentTimer();
		}
	}

	function myOnPageLoad() {
		var url = new URL(parent.location.href);
		if (url.parameters != undefined && hasValue(url.parameters.idAnaliseCreditoCliente)) {
			getTabGroup(document).getTab("cad").setDisabled(false);
			getTabGroup(document).selectTab("cad", "", true);
		} else {
			onPageLoad();
		}
	}

	function lookupCliente_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}

		/** Popula dados e formata nrIdentificacao */
		var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data.length > 0) {
			setElementValue("cliente.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
		}
		return retorno;
	}

	function aferPopupCliente(data) {
		setElementValue("cliente.pessoa.nrIdentificacao", data.pessoa.nrIdentificacaoFormatado);
	}

	var currentTimerID = 0;
	function callExecuteSearchTimer() {
		stopCurrentTimer();

		if(!hasValue(getElementValue("cliente.idCliente"))
			&& !hasValue(getElementValue("tpSituacao"))) {
			alertI18nMessage("required2FieldsOr", ["cliente", "situacao"], true);
			setFocus("cliente.pessoa.nrIdentificacao");
			return false;
		}
		/** Chama consulta e agenda proxima execução */
	   	analiseCreditoClienteGridDef.executeSearch(buildFormBeanFromForm(document.forms[0]));
	   	startCurrentTimer();
	}

	function startCurrentTimer() {
		currentTimerID = setTimeout("callExecuteSearchTimer()",1000*60*2);//2 min.
	}

	function stopCurrentTimer() {
		if(currentTimerID) {
	    	clearTimeout(currentTimerID);
	    }
	}

	function onRowClick() {
		getTabGroup(document).getTab("cad").setDisabled(false);
		return true;
	}
</script>