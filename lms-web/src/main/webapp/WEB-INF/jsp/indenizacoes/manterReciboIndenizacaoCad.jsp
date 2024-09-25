<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/indenizacoes/manterReciboIndenizacao" height="387" idProperty="idReciboIndenizacao"  service="lms.indenizacoes.manterReciboIndenizacaoAction.findByIdReciboIndenizacao" onDataLoadCallBack="dataLoad">

		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="buttonClicked" value="none" serializable="false"/>

		<adsm:complement label="rim" labelWidth="25%" width="75%" separator="branco">
			<adsm:textbox property="sgFilialRecibo" dataType="text" size="3" disabled="true"/>
			<adsm:textbox property="nrReciboIndenizacao" dataType="integer" disabled="true" mask="00000000" />
		</adsm:complement>			
		<adsm:hidden property="nrReciboComposto"/>
		<adsm:hidden property="idPendencia"/>	

		<adsm:textbox label="status" property="tpStatusIndenizacao" dataType="text" disabled="true" labelWidth="25%" width="75%" size="22"/>
		<adsm:hidden property="tpStatusIndenizacaoValue"/>	
		
		<adsm:textbox label="situacaoAprovacao" property="tpSituacaoWorkFlow" dataType="text" disabled="true" labelWidth="25%" width="75%" size="22"/>
		<adsm:hidden property="tpSituacaoWorkFlowValue"/>	
		
		<adsm:textbox label="tipoIndenizacao" property="tpIndenizacao" dataType="text" disabled="true" labelWidth="25%" width="75%" size="22"/>
		<adsm:hidden property="tpIndenizacaoValue" serializable="true"/>
		
		<adsm:textbox label="dataEmissao" property="dtEmissao" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />
		
		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="25%" width="75%" disabled="true" />
		<adsm:hidden  property="idProcessoSinistro"/>
		
		<adsm:textbox property="tipoSeguro" dataType="text" label="tipoSeguro" disabled="true" labelWidth="25%" width="75%" size="22"/>
 		<!-- LMS-666 REQ006 Segurado com seguro próprio-->
		<adsm:checkbox property="blSegurado" label="blSegurado" labelWidth="25%"/>
		
		<adsm:complement label="valorIndenizacao" width="75%" labelWidth="25%" separator="branco">
			<adsm:textbox property="sgSimboloMoeda" dataType="text" size="8" disabled="true"/>			
			<adsm:textbox property="vlIndenizacao" dataType="currency" size="18" disabled="true"/>		
			<adsm:hidden property="idMoedaHidden"/>
		</adsm:complement>	

		<adsm:textbox label="volumesIndenizados" property="qtVolumesIndenizados" dataType="integer" required="false" width="75%" labelWidth="25%" maxLength="6" size="6" disabled="true"/>
		
		<!-- LMS-666 REQ006 Criar campo Nota Fiscal de débito do cliente-->
		<adsm:textbox label="notaFiscalDebitoCliente" property="nrNotaFiscalDebitoCliente" dataType="integer" width="75%" labelWidth="25%" maxLength="10" size="10"/>

		<adsm:combobox label="salvados" property="blSalvados" domain="DM_SIM_NAO" required="true" width="75%" labelWidth="25%" onchange="onBlSalvadosChange(this);" renderOptions="true"/>

		<adsm:textarea label="observacao" property="obReciboIndenizacao" maxLength="1000" columns="100" rows="3" width="75%" labelWidth="25%" />
		
        <%-- ============================================================ --%>
		<adsm:section caption="beneficiario"/>
		<adsm:combobox label="tipoBeneficiario" property="tpBeneficiarioIndenizacao" domain="DM_BENEFICIARIO_INDENIZACAO"  boxWidth="85" labelWidth="25%" width="75%" onchange="onTpBeneficiarioIndenizacaoChange(this, true);" required="true" renderOptions="true"/>
		<adsm:lookup label="filial" labelWidth="25%" width="75%"
		             property="filialBeneficiada"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true" disabled="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filialBeneficiada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filialBeneficiada.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="67"
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup label="cliente" dataType="text" property="clienteBeneficiario" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="25%" width="75%" disabled="true">
			<adsm:propertyMapping relatedProperty="clienteBeneficiario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="clienteBeneficiario.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:lookup label="terceiro" dataType="text" property="beneficiarioTerceiro" idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas" 
					 exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="25%" width="75%" disabled="true">
			<adsm:propertyMapping relatedProperty="beneficiarioTerceiro.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:textbox dataType="text" property="beneficiarioTerceiro.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:section caption="favorecido"/>		
		<adsm:combobox label="tipoFavorecido" property="tpFavorecidoIndenizacao" domain="DM_BENEFICIARIO_INDENIZACAO"  boxWidth="85" labelWidth="25%" width="75%" required="false" onchange="onTpFavorecidoIndenizacaoChange(this, true)" renderOptions="true"/>
		<adsm:lookup label="filial" labelWidth="25%" width="75%"
		             property="filialFavorecida"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             disabled="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filialFavorecida.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filialFavorecida.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="67" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		<adsm:lookup label="cliente" dataType="text" property="clienteFavorecido" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" disabled="true"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="25%" width="75%">
			<adsm:propertyMapping relatedProperty="clienteFavorecido.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="clienteFavorecido.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>		
		
		<adsm:lookup label="terceiro" dataType="text" property="favorecidoTerceiro" idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas" 
					 exactMatch="false"
					 size="20" maxLength="20" serializable="true" labelWidth="25%" width="75%" disabled="true">
			<adsm:propertyMapping relatedProperty="favorecidoTerceiro.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:textbox dataType="text" property="favorecidoTerceiro.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
        <%-- ============================================================ --%>		
        
        <adsm:section caption="dadosBancarios"/>
        
		<adsm:combobox label="formaPagamento" property="tpFormaPagamento" domain="DM_FORMA_PAGAMENTO_INDENIZACAO" required="true" width="75%" labelWidth="25%" onchange="onTpFormaPagamentoChange(this, true);" renderOptions="true"/>

		<adsm:textbox label="dataProgramadaPagamento" property="dtProgramadaPagamento" dataType="JTDate" width="75%" labelWidth="25%" />		
		

		<adsm:lookup dataType="integer" 
					 property="banco" 
					 idProperty="idBanco"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupBanco" 
					 label="banco" size="5" maxLength="3"
					 criteriaProperty="nrBanco"
					 onchange="return bancoChange(this.value);" 
					 onPopupSetValue="resetValueAgencia"
					 onDataLoadCallBack="retornoBanco"
					 action="/configuracoes/manterBancos" 					 
					 required="false"
					 labelWidth="25%"
				  	 disabled="true"
					 width="75%"
					 >
			<adsm:hidden value="A" property="statusAtivo"/>					 
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" inlineQuery="true"/>		 		 
			<adsm:propertyMapping modelProperty="nmBanco" formProperty="banco.nmBanco"/> 
			<adsm:textbox property="banco.nmBanco" dataType="text" size="25" maxLength="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="integer" 
					 property="agenciaBancaria" 
					 idProperty="idAgenciaBancaria"
					 service="lms.indenizacoes.manterReciboIndenizacaoAction.findLookupAgencia" 
					 onDataLoadCallBack="onAgenciaBancariaDataLoadCallback"
					 onPopupSetValue="onAgenciaBancariaPopupSetValue"
					 label="agencia" maxLength="4" size="7" 
					 criteriaProperty="nrAgenciaBancaria" 
					 action="/configuracoes/manterAgencias" 
					 disabled="true"
					 required="false"
					 labelWidth="25%"
					 width="75%"
					 >			
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" inlineQuery="true"/>		 		 					 
			<adsm:propertyMapping criteriaProperty="banco.idBanco" modelProperty="banco.idBanco" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="banco.nmBanco" modelProperty="banco.nmBanco" inlineQuery="false" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="banco.nrBanco" modelProperty="banco.nrBanco" inlineQuery="false" addChangeListener="false"/>
			<adsm:propertyMapping modelProperty="nmAgenciaBancaria" formProperty="agenciaBancaria.nmAgenciaBancaria"/> 			
			<adsm:textbox property="agenciaBancaria.nmAgenciaBancaria" dataType="text" size="25" maxLength="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:complement label="numeroConta" labelWidth="25%" width="75%" separator="branco">
			<adsm:textbox label="numeroConta" dataType="integer" property="nrContaCorrente" maxLength="15" size="14" required="false" minValue="0"  disabled="true"/>
			<adsm:textbox dataType="text" property="nrDigitoContaCorrente" maxLength="2"  style="width:30px;"        required="false"  disabled="true"/>
		</adsm:complement>
		
		<adsm:textbox label="parcelas" property="qtParcelasBoletoBancario" dataType="integer" size="2" maxLength="2" width="75%" labelWidth="25%" disabled="true" />
		<adsm:hidden property="qtParcelasBoletoBancarioHidden" serializable="false"/>
		
		<adsm:textbox label="dataLiberacaoPgto" property="dtLiberacaoPgto" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />
		
		<adsm:textbox label="dataDevolucaoBanco" property="dtDevolucaoBanco" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />
		<adsm:textbox label="dataPagamento" property="dtPagamentoEfetuado" dataType="JTDate" width="75%" labelWidth="25%" disabled="true" picker="false" />						

		<adsm:buttonBar>
			<adsm:button caption="indenizacaoLancamentosFrq" id="btnLancamentosFranqueados"
			onclick="exibeLancamentos();"/>
			<adsm:button caption="historicoAprovacao" id="btnFluxoAprovacao"
				onclick="showModalDialog('workflow/listarHistoricoPendencia.do?cmd=list&pendencia.idPendencia='+getElementValue('idPendencia'),window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:800px;dialogHeight:520px;');"/>
			<adsm:storeButton id="storeButton" service="lms.indenizacoes.manterReciboIndenizacaoAction.storeRIM"/>
			<adsm:button caption="fechar" id="closeButton" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
		
		<script>
			var LMS_21000 = '<adsm:label key="LMS-21000"/>'; 
		</script>
	</adsm:form>
</adsm:window>
<script>
	function exibeLancamentos() {
		var idReciboIndenizacao = getElementValue("idReciboIndenizacao");
		showModalDialog('franqueados/popupReciboIndenizacaoFranqueados.do?cmd=list&idReciboIndenizacao='+idReciboIndenizacao,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:820px;dialogHeight:440px;');
	}


	document.getElementById("sgSimboloMoeda").masterLink='true';
	document.getElementById("idMoedaHidden").masterLink='true';
	

	function dataLoad_cb(data, error) {
		
		
		setDisabled('blSegurado', false);
		setDisabled('nrNotaFiscalDebitoCliente', false);
		setDisabled('blSalvados', false);
		setDisabled('obReciboIndenizacao', false);

		setDisabled('tpBeneficiarioIndenizacao', false);
		setDisabled('filialBeneficiada.idFilial', false);
		setDisabled('clienteBeneficiario.idCliente', false);
		setDisabled('beneficiarioTerceiro.idPessoa', false);

		setDisabled('tpFavorecidoIndenizacao', false);
		setDisabled('filialFavorecida.idFilial', false);
		setDisabled('clienteFavorecido.idCliente', false);
		setDisabled('favorecidoTerceiro.idPessoa', false);
		setDisabled('tpFormaPagamento', false);
		setDisabled('dtProgramadaPagamento', false);
		setDisabled('banco.idBanco', false);
		setDisabled('agenciaBancaria.idAgenciaBancaria', false);
		setDisabled('nrContaCorrente', false);
		setDisabled('nrDigitoContaCorrente', false);
		setDisabled('qtParcelasBoletoBancario', false);
	
		
		onDataLoad_cb(data, error);
		var idProcessoWorkflow = getIdProcessoWorkflow();
		if(error==undefined && getElementValue('idReciboIndenizacao')!=''){
			onTpBeneficiarioIndenizacaoChange(document.getElementById('tpBeneficiarioIndenizacao'), false);			
			onTpFavorecidoIndenizacaoChange(document.getElementById('tpFavorecidoIndenizacao'), false);
			onTpFormaPagamentoChange(document.getElementById('tpFormaPagamento'), false);
			if (data.tpFavorecidoIndenizacao=='T' && data.favorecidoTerceiro!=undefined && idProcessoWorkflow) {
				setDisabled('favorecidoTerceiro.idPessoa', false);			
			}
			if (data.tpBeneficiarioIndenizacao=='T' && data.beneficiarioTerceiro!=undefined && !idProcessoWorkflow) {
				setDisabled('beneficiarioTerceiro.idPessoa', false);			
			}
			// habilita a aba de documentos		
			tabSetDisabled('documentos', false);
			tabSetDisabled('debitadas', false);
			tabSetDisabled('eventos', false);
			tabSetDisabled('anexo', false);
			
			// se salvados, então habilida a aba mda
			if (getElementValue('blSalvados')=='S') {
				tabSetDisabled('mda', false);
			} else {
				tabSetDisabled('mda', true);			
			}
			
			// Seta campo hidden utilizado para barra verde (masterlink) em outras abas
			setElementValue("nrReciboComposto", getElementValue("sgFilialRecibo") + " " + setFormat(document.getElementById("nrReciboIndenizacao"), getElementValue("nrReciboIndenizacao")));
			var tpStatus = document.getElementById("tpStatusIndenizacaoValue").value;
			if (tpStatus == 'C' || tpStatus == 'P') {
				setDisabled('storeButton',true);
				setDisabled('blSegurado', true);
				setDisabled('nrNotaFiscalDebitoCliente', true);
				setDisabled('blSalvados', true);
				setDisabled('obReciboIndenizacao', true);

				setDisabled('tpBeneficiarioIndenizacao', true);
				setDisabled('filialBeneficiada.idFilial', true);
				setDisabled('clienteBeneficiario.idCliente', true);
				setDisabled('beneficiarioTerceiro.idPessoa', true);

				setDisabled('tpFavorecidoIndenizacao', true);
				setDisabled('filialFavorecida.idFilial', true);
				setDisabled('clienteFavorecido.idCliente', true);
				setDisabled('favorecidoTerceiro.idPessoa', true);
				setDisabled('tpFormaPagamento', true);
				setDisabled('dtProgramadaPagamento', true);
				setDisabled('banco.idBanco', true);
				setDisabled('agenciaBancaria.idAgenciaBancaria', true);
				setDisabled('nrContaCorrente', true);
				setDisabled('nrDigitoContaCorrente', true);
				setDisabled('qtParcelasBoletoBancario', true);
			}
		}
	}
	
	
	// page load callback
	function pageLoad_cb(data, error) {
		onPageLoad_cb(data,error);
		getMoedaSessao();
		hideCloseButton();
		
		// se for uma visualizacao de acao do workflow
		//FIXME A visualizacao nao passou a ser na tela de consuta de recibo rim?		
		var idProcessoWorkflow = getIdProcessoWorkflow();
		if (idProcessoWorkflow) {
			executaDetalhamentoWorkflow(idProcessoWorkflow)
		}
	}	
	
	// detalha o recibo indenizacao, para o workflow
	//FIXME A visualizacao nao passou a ser na tela de consuta de recibo rim?
	function executaDetalhamentoWorkflow(idProcessoWorkflow) {
		var data = new Object();
		data.idProcessoWorkflow = idProcessoWorkflow;
		var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.findByIdReciboIndenizacao", "executaDetalhamentoWorkflow", data);
		xmit({serviceDataObjects:[sdo]});
	}

	// callback da funcao acima
	function executaDetalhamentoWorkflow_cb(data, error) {
		setDisabled(document, true);
		dataLoad_cb(data, error);
		setDisabled('closeButton',!getIdProcessoWorkflow());
		setDisabled('storeButton',getIdProcessoWorkflow());
	}

	// init window
	function initWindow(evento) {
		if (evento.name == "newButton_click" || evento.name=="tab_click" ) {
			if (evento.name == "newButton_click" || (evento.name=="tab_click" && evento.src.tabGroup.oldSelectedTab.properties.id=="pesq")) { 
				newMaster();
				resetTpFavorecidoIndenizacao(true);	
				resetTpBeneficiarioIndenizacao(true);						
				disableTpFormaPagamentoRelateds(true);
				tabSetDisabled('mda', true);
				tabSetDisabled('documentos', true);
				tabSetDisabled('parcelas', true);
				tabSetDisabled('debitadas', true);
				tabSetDisabled('anexo', true);
				tabSetDisabled('eventos', true);				
			}
		} 
		setDisabled('closeButton',!getIdProcessoWorkflow());
		setDisabled('storeButton',getIdProcessoWorkflow());
		if (document.getElementById("tpStatusIndenizacaoValue") != null) {
			var tpStatus = document.getElementById("tpStatusIndenizacaoValue").value;
			if (tpStatus == 'C' || tpStatus == 'P' || getIdProcessoWorkflow()) {
				setDisabled('storeButton',true);
			} else {
				setDisabled('storeButton',false);
			}
		}
	}

	// obtém o id processo, caso seja uma visualização de ação do workflow
	function getIdProcessoWorkflow() {
		var url = new URL(parent.location.href);
		return url.parameters["idProcessoWorkflow"];
	}

	// esconde o botão fechar
	function hideCloseButton(){
		var isPopup = window.dialogArguments && window.dialogArguments.window;
		if (!isPopup) {
			document.getElementById("closeButton").style.display="none";
		}	
	}
	
	// obtém a moeda da sessão e seta nos campos de valor
	function getMoedaSessao() {
		var service = "lms.indenizacoes.manterReciboIndenizacaoAction.getMoedaSessao";
		var callback = "getMoedaSessao";
		data = new Array();
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject(service, callback, data)); 
		xmit(remoteCall); 		
	}
	
	// calback da função acima
	function getMoedaSessao_cb(data, error) {
		if (error==undefined) {
			setElementValue("sgSimboloMoeda", data.sgSimboloMoeda);
			setElementValue("idMoedaHidden", data.idMoeda);
		}
	}
	
	// executa o new master
	function newMaster() {
		var service = "lms.indenizacoes.manterReciboIndenizacaoAction.newMaster";
		var callback = "newMaster";
		data = new Array();
		var remoteCall = {serviceDataObjects:new Array()}; 
		remoteCall.serviceDataObjects.push(createServiceDataObject(service, callback, data)); 
		xmit(remoteCall); 		
	}
	
	// calback da função acima
	function newMaster_cb(data, error) {
		if (error==undefined) {
			getMoedaSessao();
		}
	}
	
	
	// onchange do campo
	function onTpBeneficiarioIndenizacaoChange(e, resetFields) {		

		resetTpBeneficiarioIndenizacao(resetFields);
		
		if (e.value=='R' || e.value=='D' || e.value=='C' || e.value=='V') {
			setDisabled('clienteBeneficiario.idCliente', false);
			document.getElementById('clienteBeneficiario.pessoa.nrIdentificacao').required = 'true';						
		
		} else if (e.value=='F') {
			setDisabled('filialBeneficiada.idFilial', false);		
			document.getElementById('filialBeneficiada.sgFilial').required = 'true';			
		
		} else if (e.value=='T') {
			setDisabled('beneficiarioTerceiro.idPessoa', false);		
			document.getElementById('beneficiarioTerceiro.pessoa.nrIdentificacao').required = 'true';
		}
	}
	
	// reseta os dados de todo o grupo de beneficiario
	function resetTpBeneficiarioIndenizacao(resetFields) {
	
		if (resetFields==true) {
			resetValue('filialBeneficiada.idFilial');
			resetValue('clienteBeneficiario.idCliente');
			resetValue('beneficiarioTerceiro.idPessoa');
		}	
		
		setDisabled('filialBeneficiada.idFilial', true);
		setDisabled('clienteBeneficiario.idCliente', true);
		setDisabled('beneficiarioTerceiro.idPessoa', true);
		
		document.getElementById('clienteBeneficiario.pessoa.nrIdentificacao').required = 'false';
		document.getElementById('filialBeneficiada.sgFilial').required = 'false';
		document.getElementById('beneficiarioTerceiro.pessoa.nrIdentificacao').required = 'false';
		
	}
	
	function onTpFavorecidoIndenizacaoChange(e, resetFields) {		

		resetTpFavorecidoIndenizacao(resetFields);		
			
		if (e.value=='R' || e.value=='D' || e.value=='C' || e.value=='V') {
			setDisabled('clienteFavorecido.idCliente', false);
			document.getElementById('clienteFavorecido.pessoa.nrIdentificacao').required = 'true';
		
		} else if (e.value=='F') {
			setDisabled('filialFavorecida.idFilial', false);
			document.getElementById('filialFavorecida.sgFilial').required = 'true';

		
		} else if (e.value=='T') {
			setDisabled('favorecidoTerceiro.idPessoa', false);		
			document.getElementById('favorecidoTerceiro.pessoa.nrIdentificacao').required = 'true';
		}	
	}

	// reseta os dados de todo o grupo de favorecido
	function resetTpFavorecidoIndenizacao(resetFields) {
		if (resetFields==true) {
			resetValue('filialFavorecida.idFilial');
			resetValue('clienteFavorecido.idCliente');
			resetValue('favorecidoTerceiro.idPessoa');
		}
		setDisabled('filialFavorecida.idFilial', true);
		setDisabled('clienteFavorecido.idCliente', true);
		setDisabled('favorecidoTerceiro.idPessoa', true);
		document.getElementById('clienteFavorecido.pessoa.nrIdentificacao').required = 'false';
		document.getElementById('filialFavorecida.sgFilial').required = 'false';
		document.getElementById('favorecidoTerceiro.pessoa.nrIdentificacao').required = 'false';		
	}

	// obtém os dados bancarios da pessoa
	function findDadosBancariosPessoa() {
		
		var idFilialBeneficiada = getElementValue('filialBeneficiada.idFilial');
		var idClienteBeneficiario = getElementValue('clienteBeneficiario.idCliente');
		var idTerceiroBeneficiario = getElementValue('beneficiarioTerceiro.idPessoa');
		
		var data = new Array();

		if (idFilialBeneficiada!='') {
			data.idPessoa = idFilialBeneficiada;
			
		} else if (idClienteBeneficiario!='') {
			data.idPessoa = idClienteBeneficiario;
			
		} else if (idTerceiroBeneficiario!='') {
			data.idPessoa = idTerceiroBeneficiario;		
		}
		
		var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.findDadosBancariosPessoa", "findDadosBancariosPessoa", data);
    	xmit({serviceDataObjects:[sdo]});
	}

	// callback da funcao acima	
	function findDadosBancariosPessoa_cb(data) {
		if (data.conta!=undefined) {
			setElementValue("banco.idBanco",data.banco.idBanco);
			setElementValue("banco.nrBanco",data.banco.nrBanco);
			setElementValue("banco.nmBanco",data.banco.nmBanco);
			setElementValue("agenciaBancaria.idAgenciaBancaria",data.agenciaBancaria.idAgenciaBancaria);
			setElementValue("agenciaBancaria.nrAgenciaBancaria",data.agenciaBancaria.nrAgenciaBancaria);
			setElementValue("agenciaBancaria.nmAgenciaBancaria",data.agenciaBancaria.nmAgenciaBancaria);						
			setElementValue("nrContaCorrente",data.conta.nrContaCorrente);
			setElementValue("nrDigitoContaCorrente",data.conta.nrDigitoContaCorrente);
			setDisabled('agenciaBancaria.idAgenciaBancaria', false);
		}
	}
	
	// reseta o valor da agencia
	function resetValueAgencia(){
		resetValue(document.getElementById("agenciaBancaria.idAgenciaBancaria"));	
		setDisabled('agenciaBancaria.idAgenciaBancaria', false);
		setFocus(document.getElementById("agenciaBancaria.nrAgenciaBancaria"));
	}
	
	function onAgenciaBancariaDataLoadCallback_cb(data,erro){
		var retorno = agenciaBancaria_nrAgenciaBancaria_exactMatch_cb(data);
		if( retorno == true ){
			if( data != undefined && data[0] != undefined && data[0].banco != undefined ){
				setElementValue("banco.idBanco",data[0].banco.idBanco);
				setElementValue("banco.nrBanco",data[0].banco.nrBanco);
				setElementValue("banco.nmBanco",data[0].banco.nmBanco);
			}
		}
	}	
	
	function onAgenciaBancariaPopupSetValue(data) {
		if( data != undefined && data.banco != undefined ){
			setElementValue("banco.idBanco",data.banco.idBanco);
			setElementValue("banco.nrBanco",data.banco.nrBanco);
			setElementValue("banco.nmBanco",data.banco.nmBanco);
		}		
	}
	
	function onTpFormaPagamentoChange(e, resetFields) {

		disableTpFormaPagamentoRelateds(resetFields);
	
		if (e[e.selectedIndex].value=='BO') {
			tabSetDisabled('parcelas', false);			
			if (getElementValue('qtParcelasBoletoBancarioHidden')!='' ) {
				setElementValue('qtParcelasBoletoBancario', getElementValue('qtParcelasBoletoBancarioHidden'));
			}

			setDisabled('banco.idBanco', false);
			document.getElementById('banco.nrBanco').required = 'true';

			document.getElementById('agenciaBancaria.nrAgenciaBancaria').required = 'false';

			setDisabled('nrContaCorrente', false);
			document.getElementById('nrContaCorrente').required = 'false';

			setDisabled('nrDigitoContaCorrente', false);
			document.getElementById('nrDigitoContaCorrente').required = 'false';
		} else {
		
			resetValue('qtParcelasBoletoBancario');
	
			tabSetDisabled('parcelas', true);
		
			if (e[e.selectedIndex].value=='DC') {

				setDisabled('banco.idBanco', false);
				document.getElementById('banco.nrBanco').required = 'true';
				
				//setDisabled('agenciaBancaria.idAgenciaBancaria', false);
				document.getElementById('agenciaBancaria.nrAgenciaBancaria').required = 'true';
				
				setDisabled('nrContaCorrente', false);
				document.getElementById('nrContaCorrente').required = 'true';
				
				setDisabled('nrDigitoContaCorrente', false);
				document.getElementById('nrDigitoContaCorrente').required = 'true';
							
				findDadosBancariosPessoa();
			
			} else if (e[e.selectedIndex].value=='PU') {
				//setDisabled('dtProgramadaPagamento', false);
				document.getElementById('dtProgramadaPagamento').required = 'true';
			} 
		}
	}
	
	function disableTpFormaPagamentoRelateds(resetFields) {
	
		if (resetFields==true) {
			//resetValue(document.getElementById('qtParcelasBoletoBancario'));		
			resetValue(document.getElementById('banco.idBanco'));		
			resetValue(document.getElementById('agenciaBancaria.idAgenciaBancaria'));		
			resetValue(document.getElementById('nrContaCorrente'));			
			resetValue(document.getElementById('nrDigitoContaCorrente'));	
			resetValue(document.getElementById('dtProgramadaPagamento'));			
		}
	
		setDisabled('banco.idBanco', true);
		document.getElementById('banco.nrBanco').required = 'false';
		setDisabled('agenciaBancaria.idAgenciaBancaria', true);
		document.getElementById('agenciaBancaria.nrAgenciaBancaria').required = 'false';
		setDisabled('nrContaCorrente', true);
		document.getElementById('nrContaCorrente').required = 'false';
		setDisabled('nrDigitoContaCorrente', true);
		document.getElementById('nrDigitoContaCorrente').required = 'false';
		//setDisabled('dtProgramadaPagamento', true);
		document.getElementById('dtProgramadaPagamento').required = 'false';				
	}
	
	function onBlSalvadosChange(e) {
		if (e[e.selectedIndex].value=='S') 
			disableMDATab(false);
		else 
			disableMDATab(true);
	}
	
	function disableMDATab(disable) {
		tabSetDisabled('mda', disable);
	}
	
	function disableParcelasTab(disable) {
		tabSetDisabled('parcelas', disable);
	}
		
	function tabSetDisabled(tab, disable) {
		var tabGroup = getTabGroup(this.document);
 		tabGroup.setDisabledTab(tab, disable);	
	}
	
	function bancoChange(valor){
		var r = banco_nrBancoOnChangeHandler();
		if (r == true || valor == "") {
			resetValueAgencia();
			setDisabled('agenciaBancaria.idAgenciaBancaria', true);
		}
		return  r;
	}
	
	function retornoBanco_cb(data, error){
		var boolean = banco_nrBanco_exactMatch_cb(data);
		if (boolean == true){
			resetValueAgencia();
			setDisabled('agenciaBancaria.idAgenciaBancaria', false);
			setFocus(document.getElementById("agenciaBancaria.nrAgenciaBancaria"));
		}
	}
	
	function storeItem_cb(data, error) {

		document.getElementById('blSalvados').required = 'true';
		document.getElementById('tpBeneficiarioIndenizacao').required = 'true';
		document.getElementById('tpFormaPagamento').required = 'true';
		document.getElementById('banco.nrBanco').required = 'true';
		document.getElementById('agenciaBancaria.nrAgenciaBancaria').required = 'true';

		if (error != undefined) {
				alert(error);
		}
	}

</script>
