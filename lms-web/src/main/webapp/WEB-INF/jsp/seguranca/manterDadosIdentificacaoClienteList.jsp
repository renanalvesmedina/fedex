<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function myOnPageLoad(){
		onPageLoad();

		initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa"),
		tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"),
		numberElement:document.getElementById("pessoa.idPessoa") });		
	}
</script>
<adsm:window service="lms.seguranca.manterUsuarioLMSClienteAction" onPageLoad="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="cliente"/>
		<adsm:include key="LMS-01104"/>
	</adsm:i18nLabels>
	<adsm:form action="/seguranca/manterUsuarioLMSCliente">
		<adsm:hidden property="filialByIdFilialAtendeOperacional.idFilial"/>
		<adsm:hidden property="usuariosCliente.usuarioLMS.idUsuario"/>
		<adsm:combobox definition="TIPO_PESSOA.list" property="pessoa.tpPessoa" onlyActiveValues="true" labelWidth="15%" width="35%" label="tipoPessoa" domain="DM_TIPO_PESSOA"/>
		<adsm:complement label="identificacao">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
			<adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
		<adsm:textbox dataType="text" size="47" property="pessoa.nmPessoa" label="nomeRazaoSocial" maxLength="50" labelWidth="15%" width="81%"/>
		<adsm:textbox dataType="text" property="nmFantasia" label="nomeFantasia" labelWidth="15%" width="35%" maxLength="50" size="36"/>
		<adsm:textbox dataType="integer" property="nrConta" label="numeroConta" labelWidth="15%" width="35%" maxLength="12" size="15"/>
		<adsm:combobox property="tpCliente" domain="DM_TIPO_CLIENTE" label="tipoCliente" labelWidth="15%" width="35%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS_PESSOA" label="situacao" labelWidth="15%" width="35%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" onclick="consultar(this);" buttonType="findButton" id="__buttonBar:0.findButton" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idCliente" property="cliente" gridHeight="200" unique="true"
			service="lms.seguranca.manterUsuarioLMSClienteAction.findPaginatedCliente"
 		  rowCountService="lms.seguranca.manterUsuarioLMSClienteAction.getRowCountPaginatedCliente"
		>
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="70" />
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="100" align="right"/>
		<adsm:gridColumn title="nome" property="pessoa.nmPessoa" width="160"/>
		<adsm:gridColumn title="nomeFantasia" property="pessoa.nmFantasia" width="115"/>
		<adsm:gridColumn title="numeroConta" property="nrConta" width="115" align="right"/>
		<adsm:gridColumn title="tipoCliente" property="tpCliente" isDomain="true" width="115"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	/*
	 * Validação da consulta de cliente.
	 * Não pode consultar sem antes ter informado Identificação, Nome/Razão social, Nome fantasia ou Número da conta.
	 * Se não informado nenhum mostrar mensagem LMS-01104
	 */
	function validateConsultar() {
		if(getElementValue("pessoa.nrIdentificacao") == "" &&
			getElementValue("pessoa.nmPessoa") == "" &&
			getElementValue("nmFantasia") == "" &&
			getElementValue("nrConta") == ""){
			alert(i18NLabel.getLabel("LMS-01104"));
			return false;
		}
		return true;
	}

	function consultar(eThis) {
		if(validateConsultar()) {
			findButtonScript('cliente', eThis.form);
		}
	}

	function myOnShow(x, eventObj) {
		if(eventObj.name == "tab_click") {
			var tabGroup = getTabGroup(this.document);
			var tabCad = tabGroup.getTab("cad");
			var telaCad = tabCad.tabOwnerFrame;
			telaCad.initPage();
		}
		tab_onShow();
	}

	/**
	 * Seta dataType para pessoa.nrIdentificacao conforme Tipo Pessoa (Física/Jurídica) e
	 * Tipo de identificação for CPF/CNPJ
	 */
	function trocaTpIdentificacao() {
		var tpPessoa = getElementValue("pessoa.tpPessoa");
		setDataTypeNrIdentificacao();
	}

	/**
	 * Atribuir o dataType no campo número de dentificação dependendo
	 * do tipo de pessoa e do tipo de identificação.
	 **/
	function setDataTypeNrIdentificacao() {
		var tpPessoa = getElementValue("pessoa.tpPessoa");
		var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");

		if (tpPessoa == "F") {
			if (tpIdentificacao == "C" ) {
				document.getElementById("pessoa.nrIdentificacao").dataType = "CPF";
			} else if (tpIdentificacao == "I" ) {
				document.getElementById("pessoa.nrIdentificacao").dataType = "DNI";
			}
		} else if (tpPessoa == "J") {
			if (tpIdentificacao == "C" ) {
				document.getElementById("pessoa.nrIdentificacao").dataType = "CNPJ";
			} else if(tpIdentificacao == "I" ) {
				document.getElementById("pessoa.nrIdentificacao").dataType = "CUIT";
			}
		} else if (tpIdentificacao == "R" ) {
			document.getElementById("pessoa.nrIdentificacao").dataType = "RUT";
		} else if (tpIdentificacao == "U" ) {
			document.getElementById("pessoa.nrIdentificacao").dataType = "RUC";
		}
	}
</script>