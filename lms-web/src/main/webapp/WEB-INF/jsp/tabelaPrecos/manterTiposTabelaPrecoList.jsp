<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.tipoTabelaPrecoService" >
	<adsm:form action="/tabelaPrecos/manterTiposTabelaPreco" >

		<adsm:combobox
			label="tipo"
			domain="DM_TIPO_TABELA_PRECO"
			onchange="onSelectTipo();"
			property="tpTipoTabelaPreco"
		/>

		<adsm:textbox
			label="versao"
			dataType="integer"
			maxLength="6"
			minValue="0"
			property="nrVersao"
			dataType="integer" 
			maxLength="6"
			minValue="0" 
			property="nrVersao" 
			size="6"
		/>
		<adsm:lookup
			label="empresa"
			idProperty="idEmpresa"
			property="empresaByIdEmpresaCadastrada"
			service="lms.tabelaprecos.manterTiposTabelaPrecoAction.findLookupEmpresa"
			action="/municipios/manterEmpresas"
			criteriaProperty="pessoa.nrIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="15%"
			maxLength="20"
			size="20"
			width="85%"
			afterPopupSetValue="afterPopupEmpresa"
		>
			<adsm:propertyMapping relatedProperty="empresaByIdEmpresaCadastrada.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				disabled="true"
				property="empresaByIdEmpresaCadastrada.pessoa.nmPessoa"
				size="30"
			/>
		</adsm:lookup>

		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.tabelaprecos.manterTiposTabelaPrecoAction.findLookupCliente"
			action="vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="15%"
			maxLength="20"
			size="20"
			width="85%"
			afterPopupSetValue="afterPopupCliente"
		>
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="cliente.pessoa.nmPessoa"
				size="30"
			/>
		</adsm:lookup>
		<adsm:combobox
			autoLoad="false"
			boxWidth="250"
			label="servico"
			onlyActiveValues="true"
			optionLabelProperty="dsServico"
			optionProperty="idServico"
			property="servico.idServico"
			service="lms.configuracoes.servicoService.find"
			onDataLoadCallBack="servico"
		/>
		<adsm:textbox
			dataType="text"
			label="identificacao"
			maxLength="60"
			property="dsIdentificacao"
			size="45"
		/>
		<adsm:combobox
			domain="DM_STATUS"
			label="situacao"
			property="tpSituacao"
			width="85%"
		/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoTabelaPreco"/>
			<adsm:button caption="limpar" id="limpar" buttonType="cleanButton" onclick="return myCleanButtonScript(this.document);" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoTabelaPreco" property="tipoTabelaPreco" gridHeight="180" unique="true" defaultOrder="tpTipoTabelaPreco,nrVersao" scrollBars="horizontal" rows="8">
		<adsm:gridColumn title="tipo" property="tpTipoTabelaPreco" width="80" isDomain="true" />
		<adsm:gridColumn title="versao" property="nrVersao" width="80" align="right"/>
		<adsm:gridColumn title="identificacao" property="dsIdentificacao" width="260" />
		<adsm:gridColumn title="empresa" property="empresaByIdEmpresaCadastrada.pessoa.nmPessoa" width="220" />
		<adsm:gridColumn title="cliente" property="cliente.pessoa.nmPessoa" width="220" />
		<adsm:gridColumn title="servico" property="servico.dsServico" width="220" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="80" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript">

function cleanListTrash() {
	tab_onShow();
	var tipo = getElementValue("tpTipoTabelaPreco");
	if (tipo == 'C') {
		setDisabled("servico.idServico", true);
		setDisabled("nrVersao", true);
	} else {
		setDisabled("servico.idServico", false);
		setDisabled("nrVersao", false);
	}
}

function onSelectTipo() {
	var tipo = getElementValue("tpTipoTabelaPreco");
	if (tipo == 'C') {
		setElementValue("nrVersao", "");
		setDisabled("nrVersao", true);
		setElementValue("servico.idServico", "");
		setDisabled("servico.idServico", true);
	} else {
		setDisabled("nrVersao", false);
		setDisabled("servico.idServico", false);
		var params = [];
		if (tipo == "A") {
			params.tpModal = "A";
		} else if (tipo == "I") {
			params.tpModal = "R";
			params.tpAbrangencia = "I";
		} else if ( (tipo == "R") || (tipo == "M") || (tipo == "@") || (tipo == "E") || (tipo == "B") || (tipo == "F")) {
			params.tpModal = "R";
		}
		if (tipo != "I") {
			params.tpAbrangencia = "N";
		}

		params.tpSituacao = "A";
		var sdo = createServiceDataObject("lms.configuracoes.servicoService.find", "servico.idServico", params);
		xmit({serviceDataObjects:[sdo]});
	}
}

function myCleanButtonScript(documento) {
	setDisabled("nrVersao", false);
	setDisabled("servico.idServico", false);
	cleanButtonScript(documento);
	var e = document.getElementById("servico.idServico");
	for (var i = e.options.length; i >= 1 ; i--) {
		e.options.remove(i);
	}
}

function afterPopupEmpresa(data) {
	setElementValue("empresaByIdEmpresaCadastrada.pessoa.nrIdentificacao",data.pessoa.nrIdentificacaoFormatado);
}

function afterPopupCliente(data) {
	setElementValue("cliente.pessoa.nrIdentificacao",data.pessoa.nrIdentificacaoFormatado);
}

</script>
