<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	var blRncAutomaticaNovo = false;

	function carregaPagina_cb() {

		var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.newMaster");
		xmit({serviceDataObjects:[sdo]});

		// Busca Dados da sessão
		buscarDadosSessao();

		// Rotina que pega a referência da tela pai para usar parametros ou chamar funções
		// que serão usadas na tela filho.
		var doc = document;
		if (doc.parentWindow.dialogArguments != undefined && doc.parentWindow.dialogArguments.window != undefined) {
			doc = doc.parentWindow.dialogArguments.window.document;
		} else {
		   doc = document;
		}

		// Pega parâmetros da tela pai.
		var tabGroup = getTabGroup(doc.parentWindow.dialogArguments.window.document);
		var tabDet = tabGroup.getTab("recepcaoDescarga");
		var idCarregamentoDescarga = tabDet.getFormProperty("idCarregamentoDescarga");
		var idFilialControleCarga = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.idFilial");
		var idControleCarga = tabDet.getFormProperty("controleCarga.idControleCarga");
		var sgFilialControleCarga = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial");
		var nrControleCarga = tabDet.getFormProperty("controleCarga.nrControleCarga");
		var tpControleCarga = tabDet.getFormProperty("controleCarga.tpControleCarga");
		var idFilial = tabDet.getFormProperty("idFilial");
		var sgFilial = tabDet.getFormProperty("sgFilial");
		var nmFilial = tabDet.getFormProperty("pessoa.nmFantasia");
		var sgPostoAvancado = tabDet.getFormProperty("sgFilialPostoAvancado");
		var nmPostoAvancado = tabDet.getFormProperty("nmPessoaPostoAvancado");

		var formPrincipal = document.getElementById("idCarregamentoDescarga");
		formPrincipal.value = idCarregamentoDescarga;
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", idFilialControleCarga);
		setElementValue("controleCarga.idControleCarga", idControleCarga);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", sgFilialControleCarga);
		setElementValue("controleCarga.nrControleCarga", nrControleCarga);
		setElementValue("controleCarga.tpControleCarga", tpControleCarga);
		setElementValue("idFilial", idFilial);
		setElementValue("sgFilial", sgFilial);
		setElementValue("nmFilial", nmFilial);
		setElementValue("sgPostoAvancado", sgPostoAvancado);
		setElementValue("nmPostoAvancado", nmPostoAvancado);

		if(tpControleCarga == "V"){
			tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("volumeManifestoNacional", false);
		}else{
			tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("volumeManifestoEntrega", false);
	}

	}

</script>

<adsm:window service="lms.recepcaodescarga.finalizarDescargaAction" onPageLoadCallBack="carregaPagina">
	<adsm:i18nLabels>
		<adsm:include key="LMS-03022"/>
		<adsm:include key="LMS-45223"/>
	</adsm:i18nLabels>

	<adsm:form action="/recepcaoDescarga/finalizarDescarga" idProperty="idCarregamentoDescarga" id="finalizarDescargaForm">

		<adsm:hidden property="empresa.idEmpresa" serializable="false"/>
		<adsm:hidden property="empresa.pessoa.nrIdentificacao" serializable="false"/>
		<adsm:hidden property="empresa.pessoa.nrIdentificacaoFormatado" serializable="false"/>
		<adsm:hidden property="empresa.pessoa.nmPessoa" serializable="false"/>

		<adsm:hidden property="abreRncAutomatica" />
		<adsm:hidden property="controleCarga.tpControleCarga" />
		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial" />
		<adsm:hidden property="controleCarga.idControleCarga" />
		<adsm:hidden property="confirmaRncSobraAutomatica" />

		<adsm:textbox label="controleCarga" property="controleCarga.filialByIdFilialOrigem.sgFilial" dataType="text"
					  disabled="true" size="3" maxLength="3" labelWidth="18%" width="81%">
			<adsm:textbox property="controleCarga.nrControleCarga" dataType="text" disabled="true"
						  size="8" maxLength="8" />
		</adsm:textbox>

		<adsm:hidden property="idFilial" />
		<adsm:textbox label="filial" property="sgFilial" dataType="text"
					  disabled="true" size="3" maxLength="3" labelWidth="18%" width="81%">
			<adsm:textbox property="nmFilial" dataType="text" disabled="true"
						  size="50" maxLength="50" />
		</adsm:textbox>

		<adsm:textbox label="postoAvancado" property="sgPostoAvancado" dataType="text"
					  disabled="true" size="3" maxLength="3" labelWidth="18%" width="81%">
			<adsm:textbox property="nmPostoAvancado" dataType="text" disabled="true"
						  size="50" maxLength="50" />
		</adsm:textbox>

		<adsm:textbox label="fimDescarga" property="dhFimDescarga" dataType="JTDateTimeZone"
					  labelWidth="18%" width="31%" picker="true" disabled="true"/>

		<adsm:textarea label="observacao" property="obOperacao" maxLength="200" columns="80"
					   rows="3" labelWidth="18%" width="81%"/>

		<adsm:buttonBar >
			<adsm:button id="btnFinalizar" caption="finalizarDescarga" buttonType="store" onclick="verificaBlRncAutomaticaNovo();"/>
			<adsm:button id="closeButton" caption="fechar" onclick="windowClose()"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script type="text/javascript">

	/**
	 * Função chamada ao iniciar a tela
	 */
	function initWindow(eventObj) {
		setDisabled("closeButton", false);
	}

	function windowClose(){
		this.document.parentWindow.close();
		window.close();
	}

	//#####################################################
	// Inicio da validacao do pce
	//#####################################################

	var codigos;

	/**
	 * Este get existe decorrente de uma necessidade da popUp de alert.
	 */
	function getCodigos() {
		return codigos;
	}

	function verificaAberturaRncSobraFaltaAutomatica() {
		findVolumesSobraAutomatica();
	}

	function findVolumesSobraAutomatica() {

		var data = new Object();
		data.idControleCarga = getElementValue("controleCarga.idControleCarga");
		var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.findVolumesSobraAutomatica", "findVolumesSobraAutomatica", data);
		xmit({serviceDataObjects:[sdo]});

	}

	function findVolumesSobraAutomatica_cb(data, error){
		if (error != null) {
			alert(error);
		} else if(data.qtVolumesSobra != null && data.qtVolumesSobra > 0) {
			var abreRncSobraAutomatica = confirm(getI18nMessage("LMS-45223"));

			if(abreRncSobraAutomatica) {
				data.idControleCarga = getElementValue("controleCarga.idControleCarga");

				var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.abrirRncSobraAutomatica", "abrirRncSobraAutomatica", data);
				xmit({serviceDataObjects:[sdo]});

			}
		}

		if(data.qtVolumesSobra == 0 || getElementValue("confirmaRncSobraAutomatica")) {
			setDisabled("btnFinalizar", true);

			if (blRncAutomaticaNovo) {
				verificaPossibilidadeCriacaoRNCautomatica();
			} else {
				validatePCE();
			}

		}
	}

	function abrirRncSobraAutomatica_cb(data, error) {

		if (error != null) {
			alert(error);
			setElementValue("confirmaRncSobraAutomatica", false);
		}else {
			setElementValue("confirmaRncSobraAutomatica", true);
		}
	}

	function verificaBlRncAutomaticaNovo() {

		verificaAberturaRncSobraFaltaAutomatica();
	}

	function verificaPossibilidadeCriacaoRNCautomatica(){
		var data = new Object();
		data.idControleCarga = getElementValue("controleCarga.idControleCarga");
		data.tpControleCarga = getElementValue("controleCarga.tpControleCarga");
		var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.verificaPossibilidadeCriacaoRNCautomatica", "verificaPossibilidadeCriacaoRNCautomatica", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function verificaPossibilidadeCriacaoRNCautomatica_cb(data, error){
		if (error != null) {
			alert(error);
			setFocusOnFirstFocusableField(document);
		} else {
			if(data.rncAutomaticaMessage && confirm(getI18nMessage("LMS-03022", data.rncAutomaticaMessage, false))){
				data.idControleCarga = getElementValue("controleCarga.idControleCarga");
				var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.abreRNCautomatica", "abreRNCautomatica", data);
				xmit({serviceDataObjects:[sdo]});
			}else if(!data.rncAutomaticaMessage){
				validatePCE();
			}
		}
	}

	function abreRNCautomatica_cb(data, error){
		if (error != null) {
			alert(error);
			setFocusOnFirstFocusableField(document);
		} else {
			validatePCE();
		}

	}

	/**
	 * Faz o mining de ids de pedidoColeta para iniciar a validacao dos mesmos
	 *
	 * @param methodComplement
	 */
	function validatePCE() {
		var data = new Object();
		data.idControleCarga = getElementValue("controleCarga.idControleCarga");
		data.tpControleCarga = getElementValue("controleCarga.tpControleCarga");
		var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Callback da validacao.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		if (!error){
			if(data.warnings != undefined){
				for (var i = 0; i < data.warnings.length; i++) {
					alert(data.warnings[i].warning);
				}
			}
		}
		// Janela de chamada para a tela de pce
		// Apos sua chamada cai na funcao de callBack - alertPCE
		codigos = data.codigos;
		if (codigos != null && codigos.length>0) {
			showModalDialog('vendas/alertaPce.do?cmd=list', window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:353px;');
		}
		existEventoControleCarga();
	}

	//#####################################################
	// Fim da validacao do pce
	//#####################################################

	function existEventoControleCarga(){
		var data = new Object();
		data.idControleCarga = getElementValue("controleCarga.idControleCarga");
		var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.existEventoControleCarga", "existEventoControleCarga", data);
		xmit({serviceDataObjects:[sdo]});
	}

	function existEventoControleCarga_cb(data, error){
		if (!error){
			if(data.warnings != undefined){
				for (var i = 0; i < data.warnings.length; i++) {
					alert(data.warnings[i].warning);
				}
			}
		}

		if(data.existEventoControleCarga != null && data.existEventoControleCarga == "true"){
			windowClose();
		} else {
			finalizarDescarga();
		}
	}


	/**
	 * Realiza a persistencia dos dados.
	 */
	function finalizarDescarga() {
		storeButtonScript('lms.recepcaodescarga.finalizarDescargaAction.store', 'finalizarDescarga', document.getElementById("finalizarDescargaForm"));
	}

	/**
	 * Função de retorno do objeto de finalizar descarga
	 */
	function finalizarDescarga_cb(data, error) {
		if (error != null) {
			alert(error);
			setFocusOnFirstFocusableField(document);
		} else {

			//lms-3544
			if (data.idManifestoEletronicoEncerrado) {
				showMessageMDFe();
				verificaEncerramentoMdfe(data);
			} else {
			windowClose();
		}

	}
	}

	//lms-3544
	function verificaEncerramentoMdfe(data) {
		var dataEnviar = new Object();
		dataEnviar.idManifestoEletronicoEncerrado = data.idManifestoEletronicoEncerrado;
		dataEnviar.dhEncerramento = data.dhEncerramento;
		var sdo = createServiceDataObject("lms.carregamento.emitirControleCargasAction.verificaEncerramentoMdfe", "verificaEncerramentoMdfe", dataEnviar);
	 	xmit({serviceDataObjects:[sdo]});
	}

	//lms-3544
	function verificaEncerramentoMdfe_cb(data, error) {
		if (error) {
			hideMessageMDFe();
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}

		if(data.limiteEspera) {

			setTimeout(function() {
					verificaEncerramentoMdfe(data);
					return false;
				},
				data.limiteEspera*1000);

		} else {
			hideMessageMDFe();
			windowClose();
		}

	}

	/**
	 * Busca a Data e a Hora atual do sistema.
	 */
	function buscarDadosSessao() {
		var sdo = createServiceDataObject("lms.recepcaodescarga.finalizarDescargaAction.getDadosSessao", "buscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Retorno da pesquisa de Data Atual em getDadosSessao().
	 */
	function buscarDadosSessao_cb(data, error) {
		setElementValue("empresa.idEmpresa", data.empresa.idEmpresa);
		setElementValue("empresa.pessoa.nrIdentificacao", data.empresa.pessoa.nrIdentificacao);
		setElementValue("empresa.pessoa.nrIdentificacaoFormatado", data.empresa.pessoa.nrIdentificacaoFormatado);
		setElementValue("empresa.pessoa.nmPessoa", data.empresa.pessoa.nmPessoa);

		setElementValue("dhFimDescarga", setFormat(document.getElementById("dhFimDescarga"), data.dataHoraAtual));

		if (data && data.blRncAutomaticaNovo && data.blRncAutomaticaNovo == "true"){
			blRncAutomaticaNovo = true;
		}else{
			blRncAutomaticaNovo = false;
		}
	}

	function showMessageMDFe(){
		blockUI();
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe == null || messageMDFe == undefined){
			showSystemMessage('processando', null, true);
			var messageSign = doc.getElementById("message.div");
			var messageMDFe = messageSign;
			messageMDFe.id = 'messageMDFe.div';
			doc.body.appendChild(messageMDFe);
		}
	}

	function hideMessageMDFe(){
		unblockUI();
		var doc = getMessageDocument(null);
		var messageMDFe = doc.getElementById("messageMDFe.div");
		if(messageMDFe != null && messageMDFe != undefined){
			doc.body.removeChild(messageMDFe)
		}
	}

</script>
