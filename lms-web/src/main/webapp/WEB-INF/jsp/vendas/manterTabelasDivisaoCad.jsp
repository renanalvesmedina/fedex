<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTabelasDivisaoAction" onPageLoadCallBack="myOnPageLoad" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-01163" />
		<adsm:include key="LMS-01162" />
		<adsm:include key="LMS-01220"/>
	</adsm:i18nLabels>

	<adsm:form action="/vendas/manterTabelasDivisao" idProperty="idTabelaDivisaoCliente" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="origem" value="div" serializable="false"/>
		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" serializable="false" />
		<adsm:hidden property="tabelaPreco.tipoTabelaPreco.nrVersao" serializable="false"/>
		<adsm:hidden property="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="tabelaPreco.moeda.dsSimbolo" serializable="false"/>
		<adsm:hidden property="tabelaPreco.moeda.sgMoeda" serializable="false"/>
		<adsm:hidden property="tabelaPreco.moeda.siglaSimbolo" serializable="false"/>
		<adsm:hidden property="tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" serializable="false"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente" />
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="divisaoCliente.cliente.idCliente"/>
		<adsm:hidden property="servico.tpModal.value" />
		<adsm:hidden property="servico.tpAbrangencia.value" />
		<adsm:hidden property="blTabelaFob" value="N" serializable="false"/>
		<adsm:hidden property="dsMotivoSolicitacao" />
		<adsm:hidden property="temPendenciaTpPesoCalculo"/>
		<adsm:hidden property="temPendenciaNrFatorDensidade"/>
		<adsm:hidden property="temPendenciaNrFatorCubagem"/>
		<adsm:hidden property="temPendenciaBlObrigaDimensoes"/>
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<adsm:complement
				label="cliente"
				width="70%"
				labelWidth="19%"
				required="true"
				separator="branco"
		>
			<adsm:textbox
					dataType="text"
					property="divisaoCliente.cliente.pessoa.nrIdentificacao"
					size="20"
					maxLength="20"
					disabled="true"
					serializable="false"
			/>
			<adsm:textbox
					dataType="text"
					maxLength="50"
					property="divisaoCliente.cliente.pessoa.nmPessoa"
					size="30"
					disabled="true"
					serializable="false"
			/>
		</adsm:complement>

		<adsm:textbox
				label="divisao"
				dataType="text"
				maxLength="60"
				property="divisaoCliente.dsDivisaoCliente"
				size="20"
				serializable="false"
				labelWidth="19%"
				width="70%"
				disabled="true"
				required="true"
		/>

		<adsm:lookup
				action="/tabelaPrecos/manterTabelasPreco"
				criteriaProperty="tabelaPrecoString"
				dataType="text"
				exactMatch="true"
				idProperty="idTabelaPreco"
				label="tabela"
				onclickPicker="onclickPickerLookupTabelaPreco('tabelaPreco')"
				property="tabelaPreco"
				required="true"
				service="lms.vendas.manterTabelasDivisaoAction.findLookupTabelaPreco"
				onDataLoadCallBack="lookupTabelaPreco"
				afterPopupSetValue="validateTipoTabelaPreco"
				onPopupSetValue="validateSubtipoTabelaPreco"
				onchange="return changeTabelaPreco();"
				size="10"
				maxLength="9"
				labelWidth="19%"
				width="38%"
		>

			<adsm:propertyMapping
					relatedProperty="servico.idServico"
					modelProperty="tipoTabelaPreco.servico.idServico"
			/>

			<adsm:propertyMapping
					relatedProperty="servico.dsServico"
					modelProperty="tipoTabelaPreco.servico.dsServico"
			/>

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.dsDescricao"
					modelProperty="dsDescricao"

			/>

			<adsm:propertyMapping
					relatedProperty="servico.tpModal.value"
					modelProperty="tipoTabelaPreco.servico.tpModal.value"
			/>
			<adsm:propertyMapping
					relatedProperty="servico.tpAbrangencia.value"
					modelProperty="tipoTabelaPreco.servico.tpAbrangencia.value"
			/>

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"
					modelProperty="tipoTabelaPreco.tpTipoTabelaPreco.value"
			/>

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.tipoTabelaPreco.nrVersao"
					modelProperty="tipoTabelaPreco.nrVersao"
			/>

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco"
					modelProperty="subtipoTabelaPreco.tpSubtipoTabelaPreco"
			/>

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"
					modelProperty="subtipoTabelaPreco.idSubtipoTabelaPreco"
			/>

			<adsm:propertyMapping
					relatedProperty="tabelaPreco.moeda.dsSimbolo"
					modelProperty="moeda.dsSimbolo"
			/>

			<adsm:propertyMapping
					criteriaProperty="blTabelaFob"
					modelProperty="blTabelaFob"/>

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPreco.dsDescricao"
					size="30"
			/>
		</adsm:lookup>

		<adsm:checkbox
				property="blAtualizacaoAutomatica"
				label="atualizacaoAutomatica"
				width="18%"
				labelWidth="24%"
		/>

		<adsm:hidden property="servico.dsServico"/>

		<adsm:combobox
				property="servico.idServico"
				label="servico"
				onlyActiveValues="true"
				optionLabelProperty="dsServico"
				optionProperty="idServico"
				service="lms.vendas.manterTabelasDivisaoAction.findServico"
				onchange="changeServico(this)"
				required="true"
				width="38%"
				labelWidth="19%"
				boxWidth="250"
				disabled="true"
		>
		</adsm:combobox>

		<adsm:textbox
				dataType="percent"
				property="pcAumento"
				label="percentualAumentoSobreTabelaAnterior"
				maxLength="5"
				size="8"
				labelWidth="24%"
				width="18%"
				disabled="true"
		/>

		<adsm:checkbox
				property="blObrigaDimensoes"
				label="obrigarDimensoes"
				width="38%"
				labelWidth="19%"
				disabled="true"
		/>

		<adsm:checkbox
				property="blObrigaDimensoesSolicitado"
				label="obrigarDimensoesSolicitado"
				width="18%"
				labelWidth="24%"
		/>

		<adsm:checkbox
				property="blPagaFreteTonelada"
				label="pagaFreteTonelada"
				labelWidth="19%"
				width="18%"
				disabled="true"
		/>

		<adsm:lookup
				action="/tabelaPrecos/manterTabelasPreco"
				criteriaProperty="tabelaPrecoString"
				dataType="text"
				exactMatch="true"
				idProperty="idTabelaPreco"
				label="tabelaFob"
				maxLength="9"
				onclickPicker="onclickPickerLookupTabelaPreco('tabelaPrecoFob')"
				property="tabelaPrecoFob"
				service="lms.vendas.manterTabelasDivisaoAction.findLookupTabelaPreco"
				onPopupSetValue="validateSubtipoTabelaPrecoFob"
				onDataLoadCallBack="lookupTabelaPrecoFob"
				size="10"
				labelWidth="19%"
				width="70%"
				disabled="true">

			<adsm:propertyMapping
					relatedProperty="tabelaPrecoFob.dsDescricao"
					modelProperty="dsDescricao"/>

			<adsm:textbox
					dataType="text"
					disabled="true"
					property="tabelaPrecoFob.dsDescricao"
					size="30"/>

		</adsm:lookup>

		<%-- LMS-6531 --%>
		<adsm:combobox
				property="tpPesoCalculo"
				label="tipoPesoCalculo"
				domain="DM_TIPO_PESO_CALCULO"
				labelWidth="22%"
				width="50%"
				boxWidth="210"
				defaultValue="A"
				onlyActiveValues="true"
				disabled="true"/>

		<%-- LMS-6787 --%>
		<adsm:combobox
				property="tpPesoCalculoSolicitado"
				label="tipoPesoCalculoSolicitado"
				domain="DM_TIPO_PESO_CALCULO"
				labelWidth="22%"
				width="35%"
				boxWidth="210"
				onlyActiveValues="true"
				required="true"/>

		<adsm:textbox label="nrFatorCubagem" minValue="0" maxValue="999"
					  dataType="currency" labelWidth="19%"
					  property="nrFatorCubagem" width="38%"
					  size="8" disabled="true"
					  maxLength="10"/>

		<adsm:textbox label="nrFatorCubagemSolicitado" minValue="0" maxValue="999"
					  dataType="currency" labelWidth="25%"
					  property="nrFatorCubagemSolicitado" width="10%"
					  size="8" disabled="true"
					  maxLength="10"/>

		<adsm:textbox label="fatorDensidade" minValue="0" maxValue="999.99"
					  dataType="decimal" labelWidth="19%"
					  property="nrFatorDensidade" width="38%"
					  onchange="return validateFatorDensidade('nrFatorDensidade');"
					  size="8" disabled="true"
					  maxLength="10"/>

		<adsm:textbox label="fatorDensidadeSolicitado" minValue="0" maxValue="999.99"
					  dataType="decimal" labelWidth="25%"
					  property="nrFatorDensidadeSolicitado" width="10%"
					  size="8" disabled="true"
					  onchange="return validateFatorDensidade('nrFatorDensidadeSolicitado');"
					  maxLength="10"/>

		<adsm:section caption="baseCalculoComImposto" />

		<adsm:checkbox
				property="blImpBaseDevolucao"
				label="devolucao"
				width="31%"
				labelWidth="9%"
		/>

		<adsm:checkbox
				property="blImpBaseReentrega"
				label="reentrega"
				width="25%"
				labelWidth="10%"
		/>

		<adsm:checkbox
				property="blImpBaseRefaturamento"
				label="refaturamento"
				width="10%"
				labelWidth="12%"
		/>

		<adsm:section caption="limitadores" />

		<adsm:textbox label="nrLimiteMetragemCubica" minValue="0"
					  mask="###,##0.000"
					  maxValue="999999.999"
					  dataType="currency" labelWidth="19%"
					  property="nrLimiteMetragemCubica" width="38%"
					  size="10"
					  maxLength="10"/>

		<adsm:textbox
				dataType="integer"
				property="nrLimiteQuantVolume"
				label="nrLimiteQuantVolume"
				labelWidth="25%"
				width="10%"
				maxLength="5"
				size="10"/>

		<adsm:buttonBar lines="2">
			<adsm:button
					id="btnHistorico"
					caption="historicoReajuste"
					onclick="openHistoricoPopup();"
					boxWidth="140"/>

			<adsm:button caption="relatorioParametrizacoes" action="/vendas/emitirRelacaoClientesEspeciaisTabelas" cmd="main" boxWidth="180">
				<adsm:linkProperty src="tabelaPreco.tabelaPrecoString" target="tabelaPreco.tabelaPrecoString"/>
				<adsm:linkProperty src="tabelaPreco.idTabelaPreco" target="tabelaPreco.idTabelaPreco"/>
				<adsm:linkProperty src="tabelaPreco.dsDescricao" target="tabelaPreco.dsDescricao"/>
				<adsm:linkProperty src="servico.tpModal.value" target="tpModal"/>
				<adsm:linkProperty src="servico.tpAbrangencia.value" target="tpAbrangencia"/>
				<adsm:linkProperty src="divisaoCliente.cliente.idCliente" target="cliente.idCliente"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nrIdentificacao" target="cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nmPessoa" target="cliente.pessoa.nmPessoa"/>
			</adsm:button>

			<adsm:button id="servicosAdicionaisButton" caption="servicosAdicionais" action="/vendas/manterServicosAdicionaisCliente" cmd="main" boxWidth="130">
				<adsm:linkProperty src="idTabelaDivisaoCliente" target="tabelaDivisaoCliente.idTabelaDivisaoCliente"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nrIdentificacao" target="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nmPessoa" target="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="divisaoCliente.dsDivisaoCliente" target="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="tabelaPreco.idTabelaPreco" target="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"/>
				<adsm:linkProperty src="tabelaPreco.tabelaPrecoString" target="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoString"/>
				<adsm:linkProperty src="tabelaPreco.dsDescricao" target="tabelaDivisaoCliente.tabelaPreco.dsDescricao"/>
				<adsm:linkProperty src="servico.dsServico" target="tabelaDivisaoCliente.servico.dsServico"/>
				<adsm:linkProperty src="tabelaPreco.moeda.siglaSimbolo" target="tabelaDivisaoCliente.tabelaPreco.moeda.sgMoeda.siglaSimbolo"/>
				<adsm:linkProperty src="tabelaPreco.moeda.sgMoeda" target="sgMoeda"/>
				<adsm:linkProperty src="tabelaPreco.moeda.dsSimbolo" target="dsSimbolo"/>
			</adsm:button>

			<adsm:button id="parametrizacoesButton" caption="parametrizacoes" action="/vendas/manterParametrosCliente" cmd="main" boxWidth="120">
				<adsm:linkProperty src="idTabelaDivisaoCliente" target="tabelaDivisaoCliente.idTabelaDivisaoCliente"/>
				<adsm:linkProperty src="origem" target="origem"/>
				<adsm:linkProperty src="divisaoCliente.cliente.idCliente" target="tabelaDivisaoCliente.divisaoCliente.cliente.idCliente"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nrIdentificacao" target="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nmPessoa" target="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"/>
				<adsm:linkProperty src="divisaoCliente.idDivisaoCliente" target="idDivisaoCliente"/>
				<adsm:linkProperty src="divisaoCliente.dsDivisaoCliente" target="dsDivisaoCliente"/>
				<adsm:linkProperty src="servico.dsServico" target="dsServico"/>
				<adsm:linkProperty src="idTabelaDivisaoCliente" target="idTabelaDivisaoCliente"/>
				<adsm:linkProperty src="tabelaPreco.idTabelaPreco" target="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"/>
				<adsm:linkProperty src="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" target="tipoTabelaPreco"/>
				<adsm:linkProperty src="tabelaPreco.tipoTabelaPreco.nrVersao" target="nrVersao"/>
				<adsm:linkProperty src="tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco" target="subtipoTabelaPreco"/>
				<adsm:linkProperty src="tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco" target="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"/>
				<adsm:linkProperty src="tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco" target="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"/>
				<adsm:linkProperty src="tabelaPreco.dsDescricao" target="dsDescricao"/>
				<adsm:linkProperty src="tabelaPreco.moeda.sgMoeda" target="sgMoeda"/>
				<adsm:linkProperty src="tabelaPreco.moeda.dsSimbolo" target="dsSimboloMoeda"/>
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
		if (isCadastroNovo()) {
			setElementValue("tpPesoCalculo", getElementValue("tpPesoCalculoSolicitado"));
			setElementValue("nrFatorDensidade", getElementValue("nrFatorDensidadeSolicitado"));
			setElementValue("nrFatorCubagem", getElementValue("nrFatorCubagemSolicitado"));
			setElementValue("blObrigaDimensoes", document.getElementById("blObrigaDimensoesSolicitado").isDisabled);
		}

		var mostrouPopup = false;
		if (!isDisabledCamposWK() && solicitouMudancaParaWorkflow()) {
			var mostrouPopup = true;
			setElementValue("dsMotivoSolicitacao", '');
			var url = '/vendas/manterTabelasDivisao.do?cmd=motivoSolicitacao&idTabelaDivisaoCliente=' + getElementValue("idTabelaDivisaoCliente");
			var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:200px;';
			showModalDialog(url, window, wProperties);
		}
		if ((mostrouPopup && isMotivoSetado()) || !mostrouPopup) {
			storeButtonScript('lms.vendas.manterTabelasDivisaoAction.store', 'storeButton', form);
		}
	}

	function isDisabledCamposWK() {
		if (document.getElementById("tpPesoCalculoSolicitado").isDisabled
				&& document.getElementById("nrFatorDensidadeSolicitado").isDisabled
				&& document.getElementById("nrFatorCubagemSolicitado").isDisabled
				&& document.getElementById("blObrigaDimensoesSolicitado").isDisabled) {
			return true;
		} return false;
	}

	var idServicoPadrao;

	function storeButton_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		store_cb(data);
		disableCamposComPendenciaWK();
	}

	function isCadastroNovo() {
		var idDivisaoTabela = getElementValue("idTabelaDivisaoCliente");
		if (idDivisaoTabela != '' && idDivisaoTabela != null) {
			return false;
		} return true;
	}

	function solicitouMudancaParaWorkflow() {
		if (solicitouWK("tpPesoCalculo")
				|| solicitouWKNr("nrFatorDensidade")
				|| solicitouWKNr("nrFatorCubagem")
				|| mudouObrigaDimensoes()) {
			return true;
		} return false;
	}

	function solicitouWK(campo) {
		var campoSolicitado = campo + "Solicitado";
		var temPendenciaCampo = "temPendencia" + primeiraLetraMaiusculo(campo);
		if (getElementValue(campoSolicitado) == '' || getElementValue(campoSolicitado) == null) {
			return false;
		}
		if (getElementValue(campoSolicitado) != getElementValue(campo)
				&& getElementValue(temPendenciaCampo) == 'false') {
			return true;
		} return false;
	}

	function solicitouWKNr(campo) {
		var campoSolicitado = campo + "Solicitado";
		var temPendenciaCampo = "temPendencia" + primeiraLetraMaiusculo(campo);
		if (getElementValue(campoSolicitado) == undefined || getElementValue(campoSolicitado) == null) {
			return false;
		}
		if (getElementValue(campoSolicitado) != getElementValue(campo)
				&& getElementValue(temPendenciaCampo) == 'false') {
			return true;
		} return false;
	}

	function mudouObrigaDimensoes() {
		if (getElementValue("temPendenciaBlObrigaDimensoes") == 'true') {
			return false;
		}
		var blObrigaDimensoesSolicitado = getElementValue("blObrigaDimensoesSolicitado");
		var blObrigaDimensoes = getElementValue("blObrigaDimensoes");
		var mudouObrigaDimensoes = blObrigaDimensoesSolicitado != blObrigaDimensoes;
		return mudouObrigaDimensoes;
	}

	function isMotivoSetado() {
		txtMotivo = getElementValue("dsMotivoSolicitacao");
		return isElementEmpty(txtMotivo);
	}

	function isElementEmpty(txtMotivo) {
		if (txtMotivo != null && txtMotivo != '') {
			return true;
		} return false;
	}

	function disableCamposComPendenciaWK() {
		validatePendenciaSolicitacao("tpPesoCalculo");
		validatePendenciaSolicitacao("nrFatorDensidade");
		validatePendenciaSolicitacao("nrFatorCubagem");
		validatePendenciaSolicitacao("blObrigaDimensoes");
	}

	function validatePendenciaSolicitacao(campo) {
		var campoSolicitado = campo + "Solicitado";
		var temPendenciaCampo = "temPendencia" + primeiraLetraMaiusculo(campo);
		if (getElementValue(temPendenciaCampo) == 'true') {
			setDisabled(campoSolicitado, true);
		} else {
			setDisabled(campoSolicitado, false);
		}
	}

	function onclickPickerLookupTabelaPreco(campo) {
		var tabelaPrecoString = getElementValue(campo+".tabelaPrecoString");
		if(tabelaPrecoString != "") {
			setElementValue(campo+".tabelaPrecoString","");
		}
		lookupClickPicker({e:getElement(campo+".idTabelaPreco")});

		if(getElementValue(campo+".tabelaPrecoString") == "" && tabelaPrecoString != "") {
			setElementValue(campo+".tabelaPrecoString", tabelaPrecoString);
		}
	}

	function openModalHistoricoWorkflow() {
		var param = "&idProcesso=" + getElementValue("idTabelaDivisaoCliente");
		param += "&nmTabela=TABELA_DIVISAO_CLIENTE";
		param += "&cliente.pessoa.nmPessoa=" + escape(document.getElementById("divisaoCliente.cliente.pessoa.nmPessoa").value);
		param += "&cliente.pessoa.nrIdentificacao=" + document.getElementById("divisaoCliente.cliente.pessoa.nrIdentificacao").value;
		param += "&dsDivisaoCliente=" + document.getElementById("divisaoCliente.dsDivisaoCliente").value;
		param += "&tabelaPreco.tabelaPrecoString=" + document.getElementById("tabelaPreco.tabelaPrecoString").value;
		param += "&tabelaPreco.dsDescricao=" + document.getElementById("tabelaPreco.dsDescricao").value;

		var url = '/workflow/historicoWorkflow.do?cmd=list' + param;
		var wProperties = 'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;';
		showModalDialog(url, window, wProperties);
	}

	/*
	 Criada para validar acesso do usu�rio
	 logado � filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		validateTipoTabelaPreco();
		validaPermissao();
		if (getElementValue("servico.idServico") == idServicoPadrao) {
			setDisabled("tabelaPrecoFob.idTabelaPreco", false);
		}
		disableCamposComPendenciaWK();

		if(validaLimitadores()){
			limpaLimitadores();
			desabilitaLimitadores();
		}else{
			habilitaLimitadores();
		}

		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document, true);
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
		}
		normalizaCamposNr();
	}

	function desabilitaLimitadores(){
		setDisabled("nrLimiteMetragemCubica",true);
		setDisabled("nrLimiteQuantVolume",true);
	}

	function habilitaLimitadores(){
		setDisabled("nrLimiteMetragemCubica",false);
		setDisabled("nrLimiteQuantVolume",false);
	}

	function limpaLimitadores(){
		setElementValue("nrLimiteMetragemCubica", "");
		setElementValue("nrLimiteQuantVolume", "");
	}

	function validaLimitadores(){
		if (getElementValue("servico.idServico") != idServicoPadrao || validaPesoBaseCalculo()) {
			return true;
		}
		return false;
	}

	function validaPesoBaseCalculo(){
		if("D" == getElementValue("tpPesoCalculo") ||
				"C" == getElementValue("tpPesoCalculo") ||
				"F" == getElementValue("tpPesoCalculo") ||
				"G" == getElementValue("tpPesoCalculo")){
			return true;
		}
		return false;
	}

	function normalizaCamposNr() {
		var nrFatorDensidade = getFormattedValue(getElement("nrFatorDensidade").dataType, getElementValue("nrFatorDensidade"), getElement("nrFatorDensidade").mask, true);
		var nrFatorCubagem = getFormattedValue(getElement("nrFatorCubagem").dataType, getElementValue("nrFatorCubagem"), getElement("nrFatorCubagem").mask, true);

		if (getElementValue("temPendenciaNrFatorDensidade") == "false") {
			setElementValue("nrFatorDensidadeSolicitado", nrFatorDensidade);
		}

		if (getElementValue("temPendenciaNrFatorCubagem") == "false") {
			setElementValue("nrFatorCubagemSolicitado", nrFatorCubagem);
		}
	}

	function lookupTabelaPreco_cb(data, error) {
		var isValid = tabelaPreco_tabelaPrecoString_exactMatch_cb(data);
		if(!isValid) {
			resetValue("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco");
		}
		validateTipoTabelaPreco();
		if (data.length == 1 && !validateSubtipoTabelaPreco(data[0])) {
			resetValue("tabelaPreco.idTabelaPreco");
			setFocus("tabelaPreco.tabelaPrecoString", false);
			return false;
		} else {
			return isValid;
		}
	}

	function lookupTabelaPrecoFob_cb(data, error) {
		var result = tabelaPrecoFob_tabelaPrecoString_exactMatch_cb(data);
		if (data.length == 1 && !validateSubtipoTabelaPrecoFob(data[0])) {
			resetValue("tabelaPrecoFob.idTabelaPreco");
			setFocus("tabelaPrecoFob.tabelaPrecoString", false);
		}
		return result;
	}

	/*
	 * Valida bot�es de acordo com o TipoTabelaPreco
	*/
	function validateTipoTabelaPreco() {
		if(validaLimitadores()){
			limpaLimitadores();
			desabilitaLimitadores();
		}else{
			habilitaLimitadores();
		}
		if(getElementValue("idTabelaDivisaoCliente") == "") {
			disabledCheckBox(true);
			return;
		}
		var tpTipoTabelaPreco = getElementValue("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco");

		var value = (tpTipoTabelaPreco != "M" && tpTipoTabelaPreco != "R");
		disabledCheckBox(value);

		var tpSubtipoTabelaPreco = getElementValue("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco");

		var disabledButtons = tpSubtipoTabelaPreco == "P";
		setDisabled("servicosAdicionaisButton", disabledButtons);
		setDisabled("parametrizacoesButton", disabledButtons);
	}

	function validateSubtipoTabelaPrecoFob(data, dialogWindow) {
		if (data != undefined) {
			if (data.subtipoTabelaPreco.tpSubtipoTabelaPreco == "F") {
				return true;
			} else {
				alertI18nMessage("LMS-01163");
				return false;
			}
		}
		return true;
	}

	function validateSubtipoTabelaPreco(data, dialogWindow) {
		if (data != undefined) {
			if (data.subtipoTabelaPreco.tpSubtipoTabelaPreco != "F") {
				return true;
			} else {
				alertI18nMessage("LMS-01162");
				return false;
			}
		}
		return true;
	}

	function validateFatorDensidade(campo, data, dialogWindow) {
		if (getElement(campo).value != ""){
			var vlFator = getElement(campo).value.toString().replace(",", ".").valueOf();
			if (vlFator > 1.009) {	//1.009 para que ap�s a aplica��o da m�scara, a parte decimal n�o seja arredondada e ignorada pela compara��o
				return true;
			} else {
				alertI18nMessage("LMS-01220");
				resetValue(campo);
				setFocus(campo, false);
				return false;
			}
		}
		return true;
	}

	/*
	 * Reseta e desabilita checkBox
	 */
	function disabledCheckBox(value) {
		if(value) {
			resetCheckBoxValue("blPagaFreteTonelada");
		}
		setDisabled("blPagaFreteTonelada", value);
	}

	/*
	 Criada para validar acesso do usu�rio
	 logado � filial do cliente
	*/
	function validaPermissao(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}

	function loadComboServico(){
		var sdo = createServiceDataObject("lms.vendas.manterTabelasDivisaoAction.findIdServicoDiaVencimento","findIdServicoDiaVencimento", {idDivisaoCliente:getElementValue("divisaoCliente.idDivisaoCliente")});
		xmit({serviceDataObjects:[sdo]});
	}

	function findIdServicoDiaVencimento_cb(data, error){
		if (error != undefined) {
			alert(error);
			return;
		}
		var servico = getNestedBeanPropertyValue(data, "servico.idServico");
		setElementValue("servico.idServico", servico);
		var sdo = createServiceDataObject("lms.vendas.manterTabelasDivisaoAction.findTabelaByIdServico","findTabelaByIdServico", {idServico:servico});
		xmit({serviceDataObjects:[sdo]});
	}

	function findTabelaByIdServico_cb(data, error){
		if (error != undefined) {
			alert(error);
			return;
		}
		var tabela = getNestedBeanPropertyValue(data, "tabelaPrecoString");
		var descricao = getNestedBeanPropertyValue(data, "DS_DESCRICAO");
		var idTabela = getNestedBeanPropertyValue(data, "ID_TABELA_PRECO");
		setElementValue("tabelaPreco.idTabelaPreco", idTabela);
		setElementValue("tabelaPreco.tabelaPrecoString", tabela);
		setElementValue("tabelaPreco.dsDescricao", descricao);
		if (getElementValue("servico.idServico") == idServicoPadrao) {
			setDisabled("tabelaPrecoFob.idTabelaPreco", false);
		} else {
			resetValue("tabelaPrecoFob.idTabelaPreco");
			setDisabled("tabelaPrecoFob.idTabelaPreco", true);
		}
	}


	function initWindow(obj){
		if (obj.name == "tab_click" || obj.name == "gridRow_click"){
			validaPermissao();
			setElementValue("idFilial",getTabGroup(document).getTab("pesq").getElementById("idFilial").value)
		} if(obj.name == "storeButton") {
			validateTipoTabelaPreco();
		} else {
			disabledCheckBox(true);
		}
		if (obj.name == "tab_click" || obj.name == "newButton_click"){
			setElementValue("blAtualizacaoAutomatica", true);
			setElementValue("blImpBaseDevolucao", true);
			setElementValue("blImpBaseReentrega", true);
			setElementValue("blImpBaseRefaturamento", true);
			loadComboServico();
			setDisabled("tpPesoCalculoSolicitado", true);
			setElementValue("tpPesoCalculoSolicitado", getElementValue("tpPesoCalculo"));

			setDisabled("nrFatorCubagemSolicitado", true);
			setDisabled("nrFatorDensidadeSolicitado", true);

			setDisabled("blObrigaDimensoesSolicitado", true);
			setElementValue("blObrigaDimensoes", true);
			setElementValue("blObrigaDimensoesSolicitado", getElementValue("blObrigaDimensoes"));
		}
	}

	function myOnPageLoad_cb() {
		onPageLoad_cb();
		var sdo = createServiceDataObject("lms.vendas.manterTabelasDivisaoAction.findDataSession", "findDataSession");
		xmit({serviceDataObjects:[sdo]});
		retornoCarregaPagina_cb();
	}

	function retornoCarregaPagina_cb() {
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function findDataSession_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}
		idServicoPadrao = data.idServicoPadrao;
	}

	function changeServico(element) {
		comboboxChange({e:element});
		if (getElementValue("servico.idServico") == idServicoPadrao) {
			setDisabled("tabelaPrecoFob.idTabelaPreco", false);
		} else {
			resetValue("tabelaPrecoFob.idTabelaPreco");
			setDisabled("tabelaPrecoFob.idTabelaPreco", true);
		}
	}

	function changeTabelaPreco() {
		resetValue("tabelaPrecoFob.idTabelaPreco");
		setDisabled("tabelaPrecoFob.idTabelaPreco", true);

		return tabelaPreco_tabelaPrecoStringOnChangeHandler();
	}

	function openHistoricoPopup() {
		var url = "&idTabelaDivisaoCliente="+getElementValue("idTabelaDivisaoCliente");
		showModalDialog("vendas/listarHistoricoReajusteCliente.do?cmd=list"+url,window,"unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:610px;dialogHeight:380px;");
	}

	function primeiraLetraMaiusculo(string) {
		return string.charAt(0).toUpperCase() + string.slice(1);
	}
</script>