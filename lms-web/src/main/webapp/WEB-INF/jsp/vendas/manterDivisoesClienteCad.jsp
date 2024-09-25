<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterDivisoesClienteAction" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form action="/vendas/manterDivisoesCliente" idProperty="idDivisaoCliente" onDataLoadCallBack="myDataLoad">

		<adsm:hidden property="cliente.idCliente"/>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="dsMotivoSolicitacao"/>
		<adsm:hidden property="temPendenciaSituacao"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<adsm:complement
			label="cliente"
			width="70%"
			labelWidth="22%"
			separator="branco"
			>
			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nrIdentificacao"
				size="20"
				maxLength="15"
				disabled="true"
				serializable="false"
			/>
			<adsm:textbox
				dataType="text"
				maxLength="30"
				property="cliente.pessoa.nmPessoa"
				size="30"
				disabled="true"
				serializable="false"
			/>
		</adsm:complement>

		<adsm:textbox
        	dataType="integer"
        	property="cdDivisaoCliente"
        	label="codigo"
        	required="true"
			labelWidth="22%"
			width="28%"
        	maxLength="10"
        	size="10"
        />
		<adsm:textbox
			dataType="text"
			property="dsDivisaoCliente"
			label="divisao"
			required="true"
			maxLength="60"
			labelWidth="10%"
			size="40"
		/>

        <adsm:textbox
        	dataType="integer"
        	property="nrQtdeDocsRomaneio"
        	label="quantidadeDocumentosRomaneio"
        	maxLength="5"
        	size="10"
        	labelWidth="22%"
			width="28%"
        />

		<%--------------------%>
		<%-- SITUACAO COMBO --%>
		<%--------------------%>
		<adsm:combobox
			label="situacao"
			labelWidth="7%"
			property="tpSituacao"
			width="14%"
			domain="DM_STATUS"
			disabled="true"
		/>

		<adsm:combobox
			required="true"
			label="situacaoSolicitada"
			labelWidth="13%"
			property="tpSituacaoSolicitada"
			width="15%"
			domain="DM_STATUS"
		/>

		<adsm:combobox
			label="naturezaProdutoPadrao"
			labelWidth="22%"
			boxWidth="160"
			width="78%"
			optionProperty="idNaturezaProduto"
			optionLabelProperty="dsNaturezaProduto"
			property="naturezaProduto.idNaturezaProduto"
			service="lms.expedicao.naturezaProdutoService.find"
			onlyActiveValues="true"
		/>

		<adsm:buttonBar lines="2" >
			<adsm:button id="naturezaProdutoCliente" caption="naturezaProdutoCliente" action="/vendas/manterDivisaoClienteNaturezaProduto" cmd="main" boxWidth="210">
				<adsm:linkProperty src="idDivisaoCliente" target="divisaoCliente.idDivisaoCliente"/>
				<adsm:linkProperty src="dsDivisaoCliente" target="divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
			</adsm:button>

			<adsm:button id="produtos" caption="produtos" action="/vendas/manterProdutosDivisao" cmd="main" boxWidth="80">
				<adsm:linkProperty src="idDivisaoCliente" target="divisaoCliente.idDivisaoCliente"/>
				<adsm:linkProperty src="dsDivisaoCliente" target="divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
			</adsm:button>

			<adsm:button id="agrupamentos" caption="agrupamentos" action="/vendas/manterAgrupamentosDivisao" cmd="main" boxWidth="100">
				<adsm:linkProperty src="idDivisaoCliente" target="divisaoCliente.idDivisaoCliente"/>
				<adsm:linkProperty src="dsDivisaoCliente" target="divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="cliente.idCliente" target="divisaoCliente.cliente.idCliente"/>
			</adsm:button>

			<adsm:button id="diasFaturamento" caption="diasFaturamento" action="/vendas/manterDiasFaturamentoDivisao" cmd="main" boxWidth="145">
				<adsm:linkProperty src="idDivisaoCliente" target="divisaoCliente.idDivisaoCliente"/>
				<adsm:linkProperty src="dsDivisaoCliente" target="divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="divisaoCliente.cliente.pessoa.nmPessoa"/>
			</adsm:button>

			<adsm:button id="tabelas" caption="tabelas" action="/vendas/manterTabelasDivisao" cmd="main" disabled ="true">
				<adsm:linkProperty src="idDivisaoCliente" target="divisaoCliente.idDivisaoCliente"/>
				<adsm:linkProperty src="dsDivisaoCliente" target="divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
			</adsm:button>

			<adsm:button id="fatorCubagemReal" caption="fatorCubagemReal" action="/vendas/manterFatorCubagemDivisao" cmd="main">
				<adsm:linkProperty src="idDivisaoCliente" target="divisaoCliente.idDivisaoCliente"/>
				<adsm:linkProperty src="cdDivisaoCliente" target="divisaoCliente.cdDivisaoCliente"/>
				<adsm:linkProperty src="dsDivisaoCliente" target="divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente.pessoa.nmPessoa" target="divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
			</adsm:button>
			<adsm:button caption="historicoWK" id="btnHistoricoWK" onclick="openModalHistoricoWorkflow()" disabled="false"/>
			<adsm:button id="storeButton" onclick="onclickSalvar(this.form)" caption="salvar" disabled="false" buttonType="storeButton"/>
 			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton" />

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	/* LMS-6784 - WorkFlow */
	function onclickSalvar(form) {
		if (!validateForm(form)) {
			return "false";
		}
		if (isInclusaoDivisao()) {
			setElementValue("tpSituacao", getElementValue("tpSituacaoSolicitada"));
		}
		var isDisabledSituacaoSolicitada = document.getElementById("tpSituacaoSolicitada").isDisabled;
		var mostrouPopup = false;
		if (!isDisabledSituacaoSolicitada && trocouStatusSituacaoSolicitada()) {
			var mostrouPopup = true;
			setElementValue("dsMotivoSolicitacao", '');
			var url = '/vendas/manterDivisoesCliente.do?cmd=motivoSolicitacao&idDivisaoCliente=' + getElementValue("idDivisaoCliente");
			var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:200px;';
			showModalDialog(url, window, wProperties);
		}
		if ((mostrouPopup && isMotivoSetado()) || !mostrouPopup) {
			storeButtonScript('lms.vendas.manterDivisoesClienteAction.store', 'storeButton', form);
		}
	}

	function isInclusaoDivisao() {
		var idDivisaoCliente = getElementValue("idDivisaoCliente");
		if (idDivisaoCliente != '' && idDivisaoCliente != null) {
			return false;
		} return true;
	}

	function isMotivoSetado() {
		txtMotivo = getElementValue("dsMotivoSolicitacao");
		if (txtMotivo != null && txtMotivo != '') {
			return true;
		} return false;
	}

	function trocouStatusSituacaoSolicitada() {
		if (getElementValue("tpSituacaoSolicitada") == '' || getElementValue("tpSituacaoSolicitada") == null) {
			return false;
		}
		if (getElementValue("tpSituacaoSolicitada") != getElementValue("tpSituacao")) {
			return true;
		} return false;
	}
	/*
	 Criada para validar acesso do usuário
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		validaPermissao();
		verificarDiaPrazoVencimento();
		validatePendenciaSolicitacao();
		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document, true);
			setDisabled("fatorCubagemReal", true);
			setDisabled("tabelas", true);
			setDisabled("diasFaturamento", true);
			setDisabled("agrupamentos", true);
			setDisabled("produtos", true);
			setDisabled("naturezaProdutoCliente", true);
		}
		if (getElementValue("tpSituacaoSolicitada") == "") {
			setElementValue("tpSituacaoSolicitada", getElementValue("tpSituacao"));
		}
	}

	 function validatePendenciaSolicitacao() {
		 if (!isInclusaoDivisao()) {
			 if (getElementValue("temPendenciaSituacao") == 'true') {
				 setDisabled("tpSituacaoSolicitada", true);
			 } else {
				 setDisabled("tpSituacaoSolicitada", false);
			 }
		 }
	 }

	 function retornoCarregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
	 	}
	}

	/*
	 Criada para validar acesso do usuário
	 logado à filial do cliente
	*/
	function validaPermissao(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

	function initWindow(obj){

		if (obj.name == "tab_click" || obj.name == "gridRow_click"){
			validaPermissao();
			setElementValue("idFilial",getTabGroup(document).getTab("pesq").getElementById("idFilial").value);
		}

		if (obj.name == "tab_click" || obj.name == "newButton_click") {
			setDisabled("tpSituacaoSolicitada", false);
			setElementValue("tpSituacaoSolicitada", getElementValue("tpSituacao"));
		}
		if (obj.name == "storeButton" && isMotivoSetado()) {
			setDisabled("tpSituacaoSolicitada", true);
		}
	}

	var hasDiaVencimento;

	function verificarDiaPrazoVencimento(){
		if(hasValue(getElementValue("idDivisaoCliente"))){
			var sdo = createServiceDataObject("lms.vendas.manterDiasFaturamentoDivisaoAction.findByIdDivisaoCliente", "findByIdDivisaoCliente", {idDivisaoCliente:getElementValue("idDivisaoCliente")});
			xmit({serviceDataObjects:[sdo]});

		}
	}

	function storeButton_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		store_cb(data);
		verificarDiaPrazoVencimento();
		validatePendenciaSolicitacao();
	}

	function openModalHistoricoWorkflow() {
		var param = "&idProcesso=" + getElementValue("idDivisaoCliente");
		param += "&nmTabela=DIVISAO_CLIENTE";
		param += "&cliente.pessoa.nmPessoa=" + escape(document.getElementById("cliente.pessoa.nmPessoa").value);
		param += "&cliente.pessoa.nrIdentificacao=" + document.getElementById("cliente.pessoa.nrIdentificacao").value;
		param += "&dsDivisaoCliente=" + document.getElementById("dsDivisaoCliente").value;

		var url = '/workflow/historicoWorkflow.do?cmd=list' + param;
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;';
		showModalDialog(url, window, wProperties);
	}

	function findByIdDivisaoCliente_cb(data,erros) {
		if(erros!=undefined){
			alert(erros);
			return;
		}
		data = (data[0] != undefined) ? data[0] : data;
		if(data != undefined){
			var idDiaFaturamento = getNestedBeanPropertyValue(data, "idDiaFaturamento");
		}
			if(idDiaFaturamento > 0){
				var sdo = createServiceDataObject("lms.vendas.manterDivisoesClienteAction.findPrazoVencimentoByIdDivisao", "findPrazoByIdDivisaoCliente", {idDivisaoCliente:getElementValue("idDivisaoCliente")});
				xmit({serviceDataObjects:[sdo]});
			}else{
				setDisabled("tabelas", true);
			}
	}

	function findPrazoByIdDivisaoCliente_cb(data,erros) {
		if(erros!=undefined){
			alert(erros);
			return;
		}
		data = (data[0] != undefined) ? data[0] : data;
		if(data != undefined){
			var idPrazoVencimento = getNestedBeanPropertyValue(data, "idPrazoVencimento");
		}
		if(idPrazoVencimento != undefined ){
			if (getElementValue("idProcessoWorkflow") == "") {
				setDisabled("tabelas", false);
			}
		}else{
			setDisabled("tabelas", true);
		}
	}



</script>