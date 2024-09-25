<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.carregamento.consultarControleCargasAction" onPageLoadCallBack="retornoCarregaPagina" >
	<adsm:form action="/carregamento/consultarControleCargas" idProperty="idControleCarga" onDataLoadCallBack="retorno_carregaDados"
			   service="lms.carregamento.consultarControleCargasAction.findById" >

		<adsm:hidden property="tpFormatoRelatorio" value="xls" serializable="true"/>
		
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>

		<adsm:hidden property="tpSituacao" value="A" serializable="false" />

		<adsm:hidden property="filialUsuario.idFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.sgFilial" serializable="false" />
		<adsm:hidden property="filialUsuario.pessoa.nmFantasia" serializable="false" />
		
		<adsm:hidden property="meioTransporteByIdTransportado.idMeioTransporte" serializable="false" />
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" serializable="false" />
		<adsm:hidden property="rotaIdaVolta.idRotaIdaVolta" serializable="false" />
		
		<adsm:textbox dataType="text" label="controleCargas" property="filialByIdFilialOrigem.sgFilial"
					  size="3" labelWidth="18%" width="37%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:hidden property="tpControleCarga.value" serializable="true" />
		<adsm:textbox dataType="text" label="tipo" property="tpControleCarga.description" width="30%" disabled="true" serializable="false" />

		<adsm:hidden property="tpRotaViagem.value" serializable="false" />
		
		<adsm:textbox dataType="text" label="tipoRota" property="tpRotaViagem.description" labelWidth="18%" width="37%" 
					  disabled="true" serializable="false" />

		<adsm:textbox dataType="text" label="solicitContratacao" property="solicitacaoContratacao.filial.sgFilial" 
					 size="3" width="30%" picker="false" serializable="false" disabled="true" >
			<adsm:textbox property="solicitacaoContratacao.nrSolicitacaoContratacao"
						 dataType="integer" size="10" mask="0000000000" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox label="rotaViagem" property="rotaIdaVolta.nrRota" dataType="integer" size="4" 
					  labelWidth="18%" width="37%" disabled="true" serializable="false" >
			<adsm:textbox property="rotaIdaVolta.rota.dsRota" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox label="numeroSMP" property="nrSMP" dataType="integer" size="18" 
					  width="20%" disabled="true" serializable="false" >
		</adsm:textbox>

		<adsm:textbox label="dataHoraPrevista" property="dhPrevisaoSaida" dataType="JTDateTimeZone" labelWidth="18%" width="37%" 
					  picker="false" disabled="true" serializable="false" />

		<adsm:textbox label="tempoViagem" property="hrTempoViagem" dataType="text" width="30%" size="6" maxLength="6"
					  disabled="true" serializable="false" />


		<adsm:textbox dataType="text" property="meioTransporteByIdTransportadoViagem.nrFrota"
					  label="meioTransporte" labelWidth="18%" width="37%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportadoViagem.nrIdentificador"
						  size="24" serializable="false" disabled="true" />
		</adsm:textbox>
		

		<adsm:textbox dataType="text" property="meioTransporteByIdSemiRebocadoViagem.nrFrota"
					  label="semiReboque" width="30%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdSemiRebocadoViagem.nrIdentificador"
						  size="24" serializable="false" disabled="true" />
		</adsm:textbox>
		

		<adsm:textbox label="proprietario" property="proprietarioViagem.pessoa.nrIdentificacaoFormatado"
					 dataType="text" size="20" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietarioViagem.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox label="motorista" dataType="text" size="20" labelWidth="18%" width="82%"
					 property="motoristaViagem.pessoa.nrIdentificacaoFormatado" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="motoristaViagem.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox label="valorFreteCarreteiro" property="moedaVlFreteCarreteiro" dataType="text" size="6" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox property="vlFreteCarreteiro" dataType="currency" mask="###,###,###,###,##0.00" disabled="true" serializable="false" />
		</adsm:textbox>



		<adsm:textbox label="rotaColetaEntrega" property="rotaColetaEntrega.nrRota"
					 dataType="integer" size="3" labelWidth="18%" width="82%" serializable="false" disabled="true" >
			<adsm:textbox property="rotaColetaEntrega.dsRota" dataType="text" size="30" serializable="false" disabled="true" />
		</adsm:textbox>


		<adsm:textbox dataType="text" property="meioTransporteByIdTransportadoColeta.nrFrota"
					 label="meioTransporte" labelWidth="18%" width="37%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdTransportadoColeta.nrIdentificador"
						 size="24" serializable="false" disabled="true" />
		</adsm:textbox>


		<adsm:textbox dataType="text" property="meioTransporteByIdSemiRebocadoColeta.nrFrota"
					  label="semiReboque" width="30%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporteByIdSemiRebocadoColeta.nrIdentificador"
						  size="24" serializable="false" disabled="true" />
		</adsm:textbox>


		<adsm:textbox label="proprietario" property="proprietarioColeta.pessoa.nrIdentificacaoFormatado"
					  dataType="text" size="20" labelWidth="18%" width="82%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietarioColeta.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>


		<adsm:textbox label="motorista" dataType="text" size="20" labelWidth="18%" width="82%"
					  property="motoristaColeta.pessoa.nrIdentificacaoFormatado"
					  serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="motoristaColeta.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox property="tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega" label="tabelaColetaEntrega" 
					  labelWidth="18%" width="37%" dataType="text" serializable="false" disabled="true" />

		<adsm:textbox dataType="text" label="notaCredito" 
					 property="notaCredito.filial.sgFilial" 
					 size="3" width="30%" serializable="false" disabled="true" >
			<adsm:textbox dataType="integer" property="notaCredito.nrNotaCredito" size="12" mask="0000000000" serializable="false" 
						  disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="quilomSaida" property="nrQuilometragemSaida" dataType="integer" size="6" 
					  labelWidth="18%" width="37%" disabled="true" serializable="false" />
		
		<adsm:textbox label="quilomRetorno" property="nrQuilometragemRetorno" dataType="integer" size="6" 
					  width="30%" disabled="true" serializable="false" />


		<adsm:complement label="status" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="text" property="tpStatusControleCarga.description" size="40"
						  disabled="true" serializable="false" />
	
			<adsm:textbox dataType="text" property="filialAtualizaStatus.sgFilial" size="3" disabled="true" serializable="false" />
		</adsm:complement>
		
		<!-- LMSA-6340 -->
		<adsm:complement label="cargaCompartilhada" labelWidth="18%" width="82%" >
			<adsm:textbox dataType="text" property="tpCargaCompartilhada" size="40"
						  disabled="true" serializable="false" />
	    </adsm:complement>

		<adsm:hidden property="contratacoesPuxada.value" serializable="false" />
		<adsm:buttonBar lines="2">
			<adsm:button id="botaoEventos" caption="eventos" boxWidth="90" onclick="exibeEventos();" />
			<adsm:button id="botaoDocumentos" caption="documentos" onclick="exibeDocumentos();" />
			<adsm:button id="botaoLacres" caption="lacres" onclick="exibeLacres();" />
			<adsm:button id="botaoEquipe" caption="equipeColetaEntrega" onclick="exibeEquipes();" />
			<adsm:button id="botaoVeiculos" caption="veiculos" onclick="exibeVeiculos();" />
			<adsm:button id="botaoSemiReboques" caption="semiReboques" onclick="exibeSemiReboques();" />
				
			<adsm:button id="botaoRelatorioControleCarga" caption="visualizar" onclick="imprimeRelatorioControleCarga(this.form);" />		
			<adsm:button id="botaoRelatorioDivergencia" caption="relatorioDivergencia" onclick="relatorioDivergencia();" />	
			<adsm:button id="botaoPagamentoProprietario" caption="pagamentoProprietario" onclick="exibePagamentos();" />
			<adsm:button id="botaoMotoristas" caption="motoristas" onclick="exibeMotoristas();" />
			<adsm:button id="botaoEmitir" caption="emitir" onclick="exibeEmitirControleCargas();" />
			
			<!-- LMSA-6159 --> 
			<adsm:button id="botaoRegerarEDIFedEX" caption="regerarEDIFedEX" onclick="regerarNotFisEDIFedEX();" />

		</adsm:buttonBar>
	</adsm:form>
</adsm:window>


<script>

	document.getElementById("idProcessoWorkflow").masterLink = "true";
	
	function initWindow(eventObj) {
		desabilitaBotoes(false);
		if (eventObj.name == "gridRow_click") {
			escondeCamposViagem();
			escondeCamposColeta();
		}
	}
	
	function retornoCarregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		var sdo = createServiceDataObject("lms.carregamento.consultarControleCargasAction.getDataUsuario", "resultado_loadDataUsuario");
	   	xmit({serviceDataObjects:[sdo]});
	}
	
	function desabilitaBotoes(valor) {
		setDisabled("botaoEventos", valor);
		setDisabled("botaoDocumentos", valor);
		setDisabled("botaoLacres", valor);
		setDisabled("botaoEquipe", valor);
		setDisabled("botaoVeiculos", valor);
		setDisabled("botaoSemiReboques", valor);
		setDisabled("botaoPagamentoProprietario", valor);
		setDisabled("botaoMotoristas", valor);
		setDisabled("botaoEmitir", valor);
		
		
		if (valor == false) {
			if (getElementValue("tpControleCarga.value") == "V") {
				setDisabled("botaoEquipe", true);
			}
		}
	}
	
	function resultado_loadDataUsuario_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		setElementValue("filialUsuario.idFilial", getNestedBeanPropertyValue(data,"filial.idFilial"));
		setElementValue("filialUsuario.sgFilial", getNestedBeanPropertyValue(data,"filial.sgFilial"));
		setElementValue("filialUsuario.pessoa.nmFantasia", getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
		
		if (getElementValue("idProcessoWorkflow") != "") {
			escondeCamposViagem();
			escondeCamposColeta();
	    	setDisabled("botaoEmitir", true);
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	
	function retorno_carregaDados_cb(data, error) {
		onDataLoad_cb(data, error);
		var tpRotaViagem = getElementValue("tpRotaViagem.value");
	
		// VIAGEM
		if (getElementValue("tpControleCarga.value") == "V") {
			setDisabled("botaoEquipe", true);
			escondeCamposViagem("false");
			if (tpRotaViagem == "EX" || tpRotaViagem == "EC") {
				desabilitaTab("trechos", false);
				desabilitaTab("pontosParada", false);
				desabilitaTab("postosPassagem", false);
			}
			else
			if (tpRotaViagem == "EV") {	
				desabilitaTab("trechos", false);
				desabilitaTab("postosPassagem", false);
			}
			if (getElementValue("meioTransporteByIdTransportadoViagem.nrFrota") != "") {
				//desabilitaTab("adiantamentos", false);
			}
			desabilitaTab("adiantamentoTrecho", false);
			desabilitaTab("trechoCorporativo", false);
		}
		// COLETA
		else {
			setDisabled("botaoEquipe", false);
			escondeCamposColeta("false");
			desabilitaTab("postosPassagem", false);
		}
		
		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled("botaoEmitir", true);
		}
	
		setFocusOnFirstFocusableField();
	}
	
	function escondeCamposViagem(valor) {
		setRowVisibility("tpRotaViagem.description", valor);
		setRowVisibility("rotaIdaVolta.nrRota", valor);
		setRowVisibility("dhPrevisaoSaida", valor);
		setRowVisibility("meioTransporteByIdTransportadoViagem.nrFrota", valor);
		setRowVisibility("proprietarioViagem.pessoa.nrIdentificacaoFormatado", valor);
		setRowVisibility("motoristaViagem.pessoa.nrIdentificacaoFormatado", valor);
		setRowVisibility("moedaVlFreteCarreteiro", valor);
	}
	
	function escondeCamposColeta(valor) {
		setRowVisibility("rotaColetaEntrega.nrRota", valor);
		setRowVisibility("meioTransporteByIdTransportadoColeta.nrFrota", valor);
		setRowVisibility("proprietarioColeta.pessoa.nrIdentificacaoFormatado", valor);
		setRowVisibility("motoristaColeta.pessoa.nrIdentificacaoFormatado", valor);
		setRowVisibility("tipoTabelaColetaEntrega.dsTipoTabelaColetaEntrega", valor);
		setRowVisibility("nrQuilometragemSaida", valor);
	}
	
	
	function desabilitaTab(aba, disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab(aba, disabled);
	}
	
	function exibeEventos() {
		showModalDialog('carregamento/consultarControleCargasEventos.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibeDocumentos() {
		showModalDialog('carregamento/consultarControleCargasDocumentos.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibeLacres() {
		showModalDialog('carregamento/consultarControleCargasLacres.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibeEquipes() {
		showModalDialog('carregamento/consultarControleCargasEquipeColetaEntrega.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibeVeiculos() {
		showModalDialog('carregamento/consultarControleCargasVeiculos.do?cmd=main', window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibeSemiReboques() {
		showModalDialog('carregamento/consultarControleCargasSemiReboques.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibeMotoristas() {
		showModalDialog('carregamento/consultarControleCargasMotoristas.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibePagamentos() {
		showModalDialog('carregamento/consultarControleCargasPagamentoProprietario.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function imprimeRelatorioControleCarga(form) {
	    if (!validateForm(form)) {
			return false;
		}
		reportButtonScript('lms.carregamento.consultarControleCargasAction.executeRelatorioControleCarga', 'openPdf', document.forms[0]);
	}
	
	function relatorioDivergencia() {
		var dadosUrl = '&idControleCarga='+getElementValue("idControleCarga");
		dadosUrl += '&idFilial='+getElementValue("filialByIdFilialOrigem.idFilial");
		dadosUrl += '&tpControleCarga='+getElementValue("tpControleCarga.value");
		window.showModalDialog('carregamento/consultarControleCargasCarregamentoDescarga.do?cmd=main'+dadosUrl,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
	
	function exibeEmitirControleCargas() {
		var parametros = 
			'&idControleCarga=' + getElementValue("idControleCarga") +
			'&nrControleCarga=' + getElementValue("nrControleCarga") +
			'&idFilialOrigem=' + getElementValue("filialByIdFilialOrigem.idFilial") +
			'&sgFilialOrigem=' + getElementValue("filialByIdFilialOrigem.sgFilial");
			
		if (getElementValue("tpControleCarga.value") == "V") {
			parent.parent.redirectPage("carregamento/emitirControleCargas.do?cmd=main" + parametros);
		}
		else
			parent.parent.redirectPage("coleta/emitirControleCargasColetaEntrega.do?cmd=main" + parametros);
	}

/* 	Exibe loader e bloqueia tela por tempo ilimitado. O mesmo só será removido na função 'hideMessageMDFe'. 
Implementado dessa forma para garantir que tela não fique liberada nos intervalos das chamadas, e para que loader seja exibido durante toda a espera do processo de autorização da MDFe. */
function showMessageWaiting(){
	blockUI();
	var doc = getMessageDocument(null);
	var messageWaiting = doc.getElementById("messageWaiting.div");
	if(messageWaiting == null || messageWaiting == undefined) {
		showSystemMessage('processando...', null, true);
		var messageSign = doc.getElementById("message.div");
		var messageWaiting = messageSign;
		messageWaiting.id = 'messageWaiting.div';
		doc.body.appendChild(messageWaiting);
	}
}

function hideMessageWaiting(){
	unblockUI();
	var doc = getMessageDocument(null);
	var messageWaiting = doc.getElementById("messageWaiting.div");
	if(messageWaiting != null && messageWaiting != undefined){
		doc.body.removeChild(messageWaiting);
	}		
}

function regerarNotFisEDIFedEX() {
	showMessageWaiting();
	var sdo = createServiceDataObject(
			"lms.carregamento.consultarControleCargasAction.generateNotFisFedEXAgain",
			"resultRegerarNotFisEDIFedEX",
			{idControleCarga:getElementValue("idControleCarga")});
 	xmit({serviceDataObjects:[sdo]});
}

function resultRegerarNotFisEDIFedEX_cb(data, error) {
	var v_retorno = true;
	if (error != undefined) {
		alert(error);
		v_retorno = false;
	} else {
		var messagesuccess = getNestedBeanPropertyValue(data,"sucesso");
		alert(messagesuccess);
	}
	hideMessageWaiting();
	return v_retorno;
}


</script>