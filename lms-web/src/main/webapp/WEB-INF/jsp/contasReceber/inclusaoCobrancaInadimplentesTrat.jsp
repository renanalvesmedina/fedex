<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window
	service="lms.contasreceber.inclusaoCobrancaInadimplentesAction">

	<adsm:i18nLabels>
		<adsm:include key="LMS-36111" />
		<adsm:include key="LMS-00064" />
	</adsm:i18nLabels>

	<adsm:form action="/contasReceber/manterFaturas"
		id="tratativaCobInadimplenciaForm"
		service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findTratativaDetailByIdItemTratativaCobInadimplencia"
		idProperty="idTratativaCobInadimplencia"
		onDataLoadCallBack="myDataloadCallBack">

		<adsm:masterLink showSaveAll="true"
			idProperty="idCobrancaInadimplencia">
			<adsm:masterLinkItem property="nmPessoa" label="cliente" />
			<adsm:masterLinkItem property="nmUsuario" label="responsavel" />
			<adsm:masterLinkItem property="dsCobrancaInadimplencia"
				label="descricao" />
		</adsm:masterLink>



		<adsm:hidden property="siglaFilial" serializable="true" />
		<adsm:hidden property="idMotivoInadimplencia" serializable="true" />
		<adsm:hidden property="filialByIdFilial.pessoa.nmFantasia"
			serializable="false" />
		<adsm:hidden property="tpSituacaoFaturaValido" value="2" />

		<adsm:textbox property="dtTratativa" label="dtTratativa"
			dataType="JTDateTimeZone" size="30%" labelWidth="20%" width="83%"
			maxLength="16" required="true" />

		<adsm:lookup property="usuario" idProperty="idUsuario"
			criteriaProperty="nrMatricula" serializable="true" dataType="text"
			disabled="true" label="usuario" required="true" size="20"
			maxLength="20" labelWidth="20%" width="80%"
			service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupUsuario"
			action="/configuracoes/consultarFuncionariosView">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario"
				modelProperty="usuario.nmUsuario" />
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="60"
				maxLength="45" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:combobox property="blMotivoInadimplencia.idMotivoInadimplencia"
			label="motivoInadimplencia" optionLabelProperty="sgMotivo"
			optionProperty="idMotivoInadimplencia"
			service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findComboMotivoInadimplencia"
			labelWidth="20%" width="20%" required="true"
			onchange="findMotivoInadimplencia();" />
			
		<adsm:textbox dataType="text" property="dsPlanoAcao" label="planoAcao"	
			maxLength="200" labelWidth="20%" width="80%" required="true" size="89%" />
			

		<adsm:textbox dataType="JTDate" label="dtPrevistaSolucaoTratativa"
			property="dtPrevistaSolucao" size="8" maxLength="20" labelWidth="20%"
			required="true" width="30%" />

		<adsm:textbox dataType="text" property="dsParecerMatriz" label="parecerMatriz"
			maxLength="200" labelWidth="20%" width="80%" required="false" size="89%" />
		
		<adsm:buttonBar freeLayout="true">

			<adsm:button buttonType="storeButton" id="storeButton"
				onclick="myStoreButton();" caption="salvarItem" />

			<adsm:resetButton caption="limpar" />

		</adsm:buttonBar>

		
	</adsm:form>
<adsm:grid selectionMode="check"
			idProperty="idTratativaCobInadimplencia"
			property="tratativaCobInadimplencia"
			service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findTratativaByIdTratativaCobInadimplencia"
			rowCountService="lms.contasreceber.inclusaoCobrancaInadimplentesAction.getRowCountFaturaByIdTratativaCobInadimplencia"
			gridHeight="200"
			rows="2" detailFrameName="trat" unique="true">

			<adsm:gridColumn title="dtTratativa" dataType="JTDateTimeZone"
				align="center" property="dtTratativa" width="10%" />
			<adsm:gridColumn title="usuarioTratativa" property="usuarioTratativa"
				width="20%" />
			<adsm:gridColumn title="motivoInadimplencia"
				property="motivoInadimplencia" width="30%" dataType="text" />
			<adsm:gridColumn title="planoAcao" property="dsPlanoAcao" width="30%"
				dataType="text" />
			<adsm:gridColumn title="dtPrevistaSolucaoTratativa" dataType="JTDate"
				align="center" property="dtPrevisaoTratativa" width="10%" />

		</adsm:grid>


</adsm:window>


<script type="text/javascript">
	//busca o usuário da sessão
	function buscaUsuarioSessao() {
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject(
				"lms.contasreceber.emitirBDMAction.findFilialUsuario",
				"retornoBuscaFilialUsuario", new Array()));

		xmit(false);

	}

	/**
	 *	Seta a filial do usuário como padrão.
	 * 	Se filial usuario for 'MTZ' pesquisa somente filiais ativas
	 */
	function retornoBuscaFilialUsuario_cb(data, erro) {
		var filialMatriz = data.filialMatriz;

		if (filialMatriz != 'true') {
			setDisabled('dsParecerMatriz', true);
		}

	}

	/* Função que preenche a combo de motivo de inadimplencia */
	function findMotivoInadimplencia() {
		var sdo = createServiceDataObject(
				"lms.contasreceber.inclusaoCobrancaInadimplentesAction.findComboMotivoInadimplencia",
				"motivoInadimplencia", new Array());
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	/** Função que popula os campos Corretora e Seguradora no callBack */
	function motivoInadimplencia_cb(data, error, erromsg) {
		if (error == null) {
			var idMotivoInadimplencia = getNestedBeanPropertyValue(data,
					"idMotivoInadimplencia");
			var blMotivoInadimplencia = getNestedBeanPropertyValue(data,
					"sgMotivo");

			setElementValue("idMotivoInadimplencia", idMotivoInadimplencia);
			//setElementValue("blMotivoInadimplencia", blMotivoInadimplencia);
		}
	}

	/** Função para buscar os dados das faturas de uma cobrança inadimplencia */
	function populaDadosFaturasByCobrancaInadimplencia() {
		var remoteCall = {
			serviceDataObjects : new Array()
		};
		var dataCall = createServiceDataObject(
				"lms.contasreceber.inclusaoCobrancaInadimplentesAction.findDadosFaturasByCobrancaInadimplencia",
				"populaDadosFaturasByCobrancaInadimplencia", {
					idCobrancaInadimplencia : getElementValue("masterId")
				});
		remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);

	}

	/** Função (callBack) para popular os dados das faturas de uma cobrança inadimplencia */
	function populaDadosFaturasByCobrancaInadimplencia_cb(data, error, erromsg) {
		onDataLoad_cb(data);
		//document.getElementById("somaFaturas").masterLink = "true";

	}

	function initWindow(eventObj) {
		// Limpa todos campos da tela caso tenha mudado de tab.
		if (eventObj.name == "tab_click") {
			cleanButtonScript(this.document);
		}

		populateUserSession();
		buscaUsuarioSessao();
	}

	function myDataloadCallBack_cb(data, error) {
		if (error != undefined) {
			alert(error);
		}

		onDataLoad_cb(data);
	}

	function carregarUsuarioSession_cb(data, erro) {
		setElementValue("usuario.nrMatricula", data.nrMatricula);
		setElementValue("usuario.nmUsuario", data.nmUsuario);
		setElementValue("usuario.idUsuario", data.idUsuario);
	}

	function populateUserSession() {
		var sdo = createServiceDataObject(
				"lms.contasreceber.inclusaoCobrancaInadimplentesAction.getUsuarioSession",
				"carregarUsuarioSession", new Array());
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	function myStoreButton() {
		storeButtonScript(
				'lms.contasreceber.inclusaoCobrancaInadimplentesAction.salvarTratativa',
				'myStoreButton', document.forms[0]);
	}

	function myStoreButton_cb(data, errorMsg, errorKey, showErrorAlert) {
		var eventObj = {
			name : "storeItemButton"
		};
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
	}
</script>