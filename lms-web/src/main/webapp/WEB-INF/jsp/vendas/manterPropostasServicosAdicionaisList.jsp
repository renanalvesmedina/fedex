<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPropostasServicosAdicionaisAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterPropostasServicosAdicionais">
		<adsm:hidden property="simulacao.idSimulacao"/>
		<adsm:hidden property="sgMoeda" serializable="false"/>
		<adsm:hidden property="dsSimbolo" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<adsm:complement label="cliente" labelWidth="15%" width="40%">
			<adsm:textbox
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				dataType="text"
				size="20"
				disabled="true"
				serializable="false"/>
			<adsm:textbox
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				dataType="text"
				size="28"
				disabled="true"
				serializable="false"/>
		</adsm:complement>

		<adsm:textbox
			label="divisao"
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			dataType="text"
			labelWidth="15%"
			width="30%"
			size="30"
			disabled="true"
			serializable="false"/>

		<adsm:complement label="tabelaBase" labelWidth="15%" width="40%">
			<adsm:hidden
				property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"
				value="0"
				serializable="false"/>
			<adsm:textbox
				property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoString"
				dataType="text"
				size="10"
				disabled="true"
				serializable="false"/>
			<adsm:textbox
				property="tabelaDivisaoCliente.tabelaPreco.dsDescricao"
				dataType="text"
				size="30"
				disabled="true"
				serializable="false"/>
		</adsm:complement>

		<adsm:textbox
			label="servico"
			property="tabelaDivisaoCliente.servico.dsServico"
			dataType="text"
			labelWidth="15%"
			width="30%"
			size="30"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="moeda"
			property="tabelaDivisaoCliente.tabelaPreco.moeda.sgMoeda.siglaSimbolo"
			dataType="text"
			labelWidth="15%"
			width="40%"
			size="9"
			disabled="true"
			serializable="false"/>

		<adsm:combobox
			label="servicoAdicional"
			property="parcelaPreco.idParcelaPreco"
			service="lms.vendas.manterPropostasServicosAdicionaisAction.findTabelaPrecoParcelaCombo"
			optionProperty="parcelaPreco.idParcelaPreco"
			optionLabelProperty="parcelaPreco.nmParcelaPreco"
			boxWidth="200"
			labelWidth="15%"
			width="30%"
			autoLoad="true">
			<adsm:hidden property="parcelaPreco.tpParcelaPreco" value="S" serializable="false"/>
			<adsm:propertyMapping
				criteriaProperty="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"
				modelProperty="tabelaPreco.idTabelaPreco"/>
			<adsm:propertyMapping
				criteriaProperty="parcelaPreco.tpParcelaPreco"
				modelProperty="parcelaPreco.tpParcelaPreco"/>
		</adsm:combobox>

		<adsm:combobox
			label="indicador"
			property="tpIndicador"
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			labelWidth="15%"
			width="40%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton
				id="btnConsultar" 
				callbackProperty="servicoAdicionalCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		property="servicoAdicionalCliente"
		idProperty="idServicoAdicionalCliente"
		service="lms.vendas.manterPropostasServicosAdicionaisAction.findPaginatedCustom"
		rowCountService="lms.vendas.manterPropostasServicosAdicionaisAction.getRowCountCustom"
		unique="true"
		gridHeight="220"
		rows="11">
		<adsm:gridColumn title="servicoAdicional" property="parcelaPreco.nmParcelaPreco" width="50%"/>
		<adsm:gridColumn title="indicador" property="tpIndicador" width="20%" isDomain="true"/>
		<adsm:gridColumn title="valorIndicador" property="vlValorFormatado" width="30%" align="right"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			notifyElementListeners({e:document.getElementById("parcelaPreco.tpParcelaPreco")});
		}
		disableWorkflow();
	}

	function myPageLoad_cb() {
	   	onPageLoad_cb();
	   	notifyElementListeners({e:document.getElementById("parcelaPreco.tpParcelaPreco")});
	   	disableWorkflow();
	}
	
	function disableWorkflow() {
		if (getElementValue("idProcessoWorkflow") != "") {
	   		setDisabled(document, true);
	   		setDisabled("btnConsultar", false);
	   	}
	}
</script>