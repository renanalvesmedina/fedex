
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service=""  onPageLoadCallBack="pageLoadCallback">
	<adsm:form action="/indenizacoes/incluirReciboIndenizacao" idProperty="idReciboIndenizacao" height="200" >

		<adsm:complement width="75%" labelWidth="25%" label="RIM" separator="branco">
		<adsm:textbox  property="sgFilial" disabled="true" size="3" dataType="text"/>
		<adsm:textbox  property="nrReciboIndenizacao" dataType="integer" disabled="true" mask="00000000"/>
		<adsm:hidden property="reciboIndenizacao"/>
		</adsm:complement>		
		
		<adsm:combobox label="tipoIndenizacao" property="tpIndenizacao" domain="DM_TIPO_INDENIZACAO" required="true" width="75%" labelWidth="25%" onchange="onTpIndenizacaoChange(this);" renderOptions="true"/>

		<adsm:lookup label="processoSinistro" 
					property="processoSinistro" 
					idProperty="idProcessoSinistro" 
					criteriaProperty="nrProcessoSinistro"
					disabled="true"
					picker="false" 
					dataType="text"	
					action=""	
					onchange="return onProcessoSinistroChange(this);"
					onDataLoadCallBack="onProcessoSinistroCallback"
					service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupProcessoSinistro"
					labelWidth="25%"
					width="75%">
			<adsm:propertyMapping relatedProperty="tipoSeguro.sgTipo" modelProperty="processoSinistro.tipoSeguro.sgTipo"/>
		</adsm:lookup>
		
		<adsm:textbox label="tipoSeguro" property="tipoSeguro.sgTipo" labelWidth="25%" width="75%" dataType="text" disabled="true" size="15" serializable="false"/>
		<!-- LMS-428 REQ001 Segurado com seguro próprio-->
		<adsm:checkbox property="blSegurado" label="blSegurado" labelWidth="25%"/>
		
		<adsm:complement label="beneficiario" width="75%" labelWidth="25%" required="true" separator="branco">
		<adsm:combobox 
			label="beneficiario" 
			property="tpBeneficiarioIndenizacao"  
			boxWidth="85"
			service="lms.indenizacoes.incluirReciboIndenizacaoAction.findComboTpBeneficiarioIndenizacao" 
			optionProperty="value" 
			optionLabelProperty="description"  
			renderOptions="true"/>
		<adsm:lookup dataType="text" property="clienteBeneficiario" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" 
					 onPopupSetValue="beneficiarioPopUp" onDataLoadCallBack="beneficiarioCallBack"
					 exactMatch="false" size="20" maxLength="20" serializable="true" >

			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" maxLength="50" disabled="true" serializable="false" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
						
			<adsm:propertyMapping relatedProperty="favorecido.idPessoa"        modelProperty="idCliente"/>
			<adsm:propertyMapping relatedProperty="favorecido.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/>
			<adsm:propertyMapping relatedProperty="favorecido.nmPessoa"        modelProperty="pessoa.nmPessoa"/>			
		</adsm:lookup>
			
		<adsm:button 
			style="FmLbSection" 
			id="clienteBeneficiarioButton" 
			caption="incluir" 
			onclick="WindowUtils.showCadastroCliente('clienteBeneficiario', 'beneficiarioCallBack');" />
		</adsm:complement>
			
		<adsm:textbox label="notaFiscalDebitoCliente" property="nrNotaFiscalDebitoCliente" dataType="integer" width="75%" labelWidth="25%" maxLength="10"/>		

	

		<adsm:hidden property="moeda.idMoeda"/> 
		<adsm:hidden property="idMoedaHidden"/> 		
		<adsm:hidden property="qtVolumesIndenizados"/>
		
		<adsm:combobox label="salvados" property="blSalvados" domain="DM_SIM_NAO" required="true" width="75%" labelWidth="25%" onchange="onBlSalvadosChange(this);" renderOptions="true"/>

		<adsm:textarea label="observacao" property="obReciboIndenizacao" maxLength="1000" columns="50" rows="3" width="75%" labelWidth="25%" />

		<adsm:combobox property="tpFormaPagamento" 
						label="formaPagamento" 
						service="lms.indenizacoes.incluirReciboIndenizacaoAction.findTipoFormaPagamento"
						optionProperty="value" optionLabelProperty="description"
						required="true" 
						width="75%" 
						labelWidth="25%" 
						onchange="onTpFormaPagamentoChange(this);" 
						renderOptions="true"/>
						
		<adsm:lookup label="favorecido" dataType="text" property="favorecido" idProperty="idPessoa" picker="false"
					 criteriaProperty="nrIdentificacao" relatedCriteriaProperty="nrIdentificacaoFormatado" disabled="true"
					 service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupPessoa" action="/configuracoes/manterPessoas"					 
					 size="20" maxLength="20" serializable="true" required="false" labelWidth="25%" width="75%">
			<adsm:propertyMapping relatedProperty="favorecido.nmPessoa" modelProperty="nmPessoa"/>
			<adsm:textbox dataType="text" property="favorecido.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox label="dataProgramadaPagamento" property="dtProgramadaPagamento" dataType="JTDate" width="75%" labelWidth="25%" required="true"/>

		<adsm:lookup dataType="integer" 
					 property="banco" 
					 idProperty="idBanco"
					 service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupBanco" 
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
					 service="lms.indenizacoes.incluirReciboIndenizacaoAction.findLookupAgencia" 
					 onDataLoadCallBack="setaBanco"
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
			<adsm:textbox label="numeroConta" dataType="integer" property="nrContaCorrente" maxLength="15" size="14" required="fasle" minValue="0"  disabled="true"/>
			<adsm:textbox dataType="text" property="nrDigitoContaCorrente" maxLength="2"  style="width:30px;"        required="false"  disabled="true"/>
		</adsm:complement>
		
		<adsm:textbox label="parcelas" property="qtParcelasBoletoBancario" dataType="integer" size="3" maxLength="2" width="75%" labelWidth="25%" disabled="true" onchange="return onNrParcelasChange(this);"/>
		<adsm:textbox label="numeroBoleto" property="nrBoleto" dataType="text" maxLength="13" size="13" width="75%" labelWidth="25%" disabled="true"/>
		<adsm:textbox label="dataVencimento" property="dtVencimento" dataType="JTDate" width="75%" labelWidth="25%" disabled="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button id="storeButton" caption="salvar" buttonType="storeButton" onclick="onStoreButtonClick(this.form);" disabled="false"/>
			<adsm:button id="newButton" caption="limpar" onclick="newButtonScript(this.document, true, {name:'newButton_click'}); filialDebitadaGridDef.resetGrid(); setFocusOnFirstFocusableField(document); habilitarAnexosTab();" buttonType="newButton" disabled="false"/>
		</adsm:buttonBar>
		
		<script>
			var LMS_21000 = '<adsm:label key="LMS-21000"/>'; 
			var LMS_21010 = '<adsm:label key="LMS-21010"/>';
		</script>
		
	</adsm:form>

	<adsm:grid property="filialDebitada" 
				idProperty="idFilialDebitada" 
				service="lms.indenizacoes.incluirReciboIndenizacaoAction.findPaginatedFilialDebitada"
				rowCountService="lms.indenizacoes.incluirReciboIndenizacaoAction.getRowCountFilialDebitada"
				selectionMode="none" 
				gridHeight="60" 
				rows="5" 
				unique="true" 
				onRowClick="onRowClick">
				
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn property="sgFilial"  dataType="text" title="filial" width="60"/>
			<adsm:gridColumn property="nmFantasia" dataType="text" title="" width="150" />
		</adsm:gridColumnGroup>
				
		<adsm:gridColumn property="pcDebitado" title="percentualDebitado" width="550" dataType="decimal" align="right" />
		<adsm:buttonBar>
			<adsm:button caption="emitirRIM" onclick="onReportButtonClick();" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">

	document.getElementById('idMoedaHidden').masterLink='true';
	document.getElementById('sgFilial').masterLink='true';
	var tabGroup = getTabGroup(this.document);
	var abaRNC = tabGroup.getTab("rnc");
	var abaDOC = tabGroup.getTab("processo");
	var isBlocked = false;

	/*****************************************************************************************************
	 * Eventos da tela
	 *****************************************************************************************************/
	function pageLoadCallback_cb() {
		newMaster();
		onPageLoad_cb();
		newItem();
		unblockAll();
		findMoedaPadrao();
	}
	
	function findMoedaPadrao() {
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.findMoedaPadrao", 'findMoedaPadrao', new Array());
    	xmit({serviceDataObjects:[sdo]});		
	}
	
	function findMoedaPadrao_cb(data, error) {
		if (error==undefined) {
			setElementValue('idMoedaHidden', data.idMoeda);
			setElementValue('sgFilial', data.sgFilial);
			setMoedaPadrao();
		}
	}
	
	function setMoedaPadrao() {
		setElementValue('moeda.idMoeda', getElementValue('idMoedaHidden'));
	}
	
	// eventos gerais da tela
	function initWindow(e) {
		if (e.name=='newButton_click') {
			blockAll();
			unblockAll();
			newItem();	
			setMoedaPadrao();
		} else if (e.name=='tab_click' && isBlocked==true) {
			setDisabled('storeButton', true);
		}
	}

	// evento de grid_click nao gera nenhuma acao
	function onRowClick() {
		return false;
	}
	
	// store customizado
	function onStoreButtonClick(form) {
		storeButtonScript('lms.indenizacoes.incluirReciboIndenizacaoAction.storeRIM', 'storeCallback', form);
	}
		
	// callback do store
	function storeCallback_cb(data, error) {
	
		store_cb(data, error);
		if (error==undefined) {
			executeSearch(getElementValue('idReciboIndenizacao'));			
			setElementValue('reciboIndenizacao', data.sgFilial + ' ' + getElementValue('nrReciboIndenizacao'));

			blockAll();
			disableAnexosTab(true);
			
			// confirma geracao do relatório 
			var result = confirm(LMS_21010);
			if (result == true) {
				onReportButtonClick();
			}
		}
	}
	
	// desabilita tudo
	function blockAll() {
		// desabilita tudo da aba rim
		setDisabled(document, true);			
		// desabilita as demais abas
		disableProcessoTab(true);
		disableParcelasTab(true);
		disableRNCTab(true);
		disableMDATab(true);			

		// habilita apenas o botao limpar
		setDisabled('newButton', false);
		// seta a variavel de bloqueio
		isBlocked=true;		
		// seleciona a aba rim
		var evento = new Array();
		evento.name = "tab_click";
		tabGroup.selectTab("rim", evento);
		
	}

	// habilita tudo o que for necessário ao limpar
	function unblockAll() {
		setDisabled('tpIndenizacao', false);
		setDisabled('tpBeneficiarioIndenizacao', false);
		setDisabled('clienteBeneficiario.idCliente', false);		
		setDisabled('moeda.idMoeda', false);
		setDisabled('nrNotaFiscalDebitoCliente', false);		
		setDisabled('blSalvados', false);
		setDisabled('obReciboIndenizacao', false);		
		setDisabled('tpFormaPagamento', false);
		setDisabled('storeButton', false);		
		setDisabled('newButton', false);
		setDisabled('dtProgramadaPagamento', false);
		isBlocked=false;
		setDisabled("clienteBeneficiarioButton", false);
	}
	
	// trata evento de <novo>
	function newItem() {
		setElementValue('blSalvados', 'N');
		disableTpFormaPagamentoRelateds();	
		disableProcessoSinistro(true);
	}
	
	function executeSearch(idReciboIndenizacao) {
			setElementValue('idReciboIndenizacao', idReciboIndenizacao);
			var fb = buildFormBeanFromForm(document.forms[0]);
			filialDebitadaGridDef.executeSearch(fb);
	}
	
	// emite o o recibo (RIM)
	function onReportButtonClick() {
		var data = new Array();
		setNestedBeanPropertyValue(data, 'idReciboIndenizacao', getElementValue('idReciboIndenizacao')); 
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.executeEmitirReciboRimGeraEventoRIM", "onReportButtonClick", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function onReportButtonClick_cb(data, error) {
		if (error!=undefined) {
			alert(error);
		} else {
			executeReport();
		}
	}
	
	function executeReport() {
		reportButtonScript('lms.indenizacoes.incluirReciboIndenizacaoAction.executeEmitirReciboRimReport', 'openPdf', document.forms[0]);		
	}
	
	/**
	 * Retorno popup da lookup de beneficiario.
	 *
	 * @param data
	 */
	function beneficiarioPopUp(data) {
		validatePCE(data.idCliente);
		setFocus(document.getElementById("nrNotaFiscalDebitoCliente"));
	}
	
	/**
	 * Callback da lookUp de benefiario
	 *
	 * @param data
	 * @param error
	 */
	function beneficiarioCallBack_cb(data, error) {
		clienteBeneficiario_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data[0] && data[0].idCliente) {
			validatePCE(data[0].idCliente);
		}
		setFocus(document.getElementById("nrNotaFiscalDebitoCliente"));
	}
	
	//#####################################################
	// Inicio da validacao do pce
	//#####################################################
	
	/**
	 * Faz a validacao da tela
	 */
	function validatePCE(idCliente) {
		var data = new Object();
		data.idCliente = idCliente;
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Callback da chamada de validacao do PCE, chama a popUp de alert com os dados do
	 * PCE caso necessario.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		if (data._exception==undefined) {
			if (data.codigo!=undefined) {
				showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
			}
		} else {
			alert(error);
		}
	}
	
	/**
	 * Este callback existe decorrente de uma necessidade da popUp de alert.
	 */
	function alertPCE_cb() {
		//Empty...
	}	
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################
	
	/*****************************************************************************************************
	 * Dinâmica da tela
	 *****************************************************************************************************/

	// verifica se é permitido mudar para a aba de processos
	function allowChangeProcessoTab() {
	
		var idProcessoSinistro = getElementValue('processoSinistro.idProcessoSinistro');
		var tpIndenizacao = document.getElementById('tpIndenizacao');
        var tpIndenizacaoValue = tpIndenizacao[tpIndenizacao.selectedIndex].value;

		if (idProcessoSinistro!='' && tpIndenizacaoValue=='PS')
			return true;
			
		return false;
	}
	
	function disableProcessoTab(disable) {
		tabSetDisabled('processo', disable);
	}
	
	function disableRNCTab(disable) {
		tabSetDisabled('rnc', disable);
	}

	function disableMDATab(disable) {
		tabSetDisabled('mda', disable);
	}
	
	function disableParcelasTab(disable) {
		tabSetDisabled('parcelas', disable);
	}
	
	function disableAnexosTab(disable) {
		tabSetDisabled('anexo', disable);
	}

	function disableProcessoSinistro(disable) {
		document.getElementById('processoSinistro.nrProcessoSinistro').required=""+!disable+"";
		setDisabled('processoSinistro.idProcessoSinistro', disable);
		resetValue('processoSinistro.idProcessoSinistro');
	}

	function onBlSalvadosChange(e) {
		if (e[e.selectedIndex].value=='S') 
			disableMDATab(false);
		else 
			disableMDATab(true);
	}

	function onTpIndenizacaoChange(e) {
		newMaster();
	
		if (e[e.selectedIndex].value=='PS') {
		
			disableProcessoSinistro(false);
			disableRNCTab(true);			
			if (allowChangeProcessoTab()==true) {
				disableProcessoTab(false);
			}
			
		} else if (e[e.selectedIndex].value=='NC') {
			// allowChangeRNCTab()
			disableRNCTab(false);
			disableProcessoTab(true);
			disableProcessoSinistro(true);
			
		
		} else {
			disableRNCTab(true);
			disableProcessoTab(true);			
			disableProcessoSinistro(true);
		}
	}

	// onchange do campo processoSinistro
	function onProcessoSinistroChange(e) {

		if (e.value=='') {
			disableProcessoTab(true);
		}
	
		return processoSinistro_nrProcessoSinistroOnChangeHandler();
	}
	
	// ondataloadcallback do processo sinistro
	function onProcessoSinistroCallback_cb(data) {
		var result = processoSinistro_nrProcessoSinistro_exactMatch_cb(data);
		if (allowChangeProcessoTab()==true) {
			disableProcessoTab(false);			
		}
		return result;
	}
	
	function disableTpFormaPagamentoRelateds() {
	
		resetValue(document.getElementById('qtParcelasBoletoBancario'));		
		setDisabled('qtParcelasBoletoBancario', true);
		document.getElementById('qtParcelasBoletoBancario').required = 'false';
				
		resetValue(document.getElementById('banco.idBanco'));		
		setDisabled('banco.idBanco', true);
		document.getElementById('banco.nrBanco').required = 'false';
		
		resetValue(document.getElementById('agenciaBancaria.idAgenciaBancaria'));		
		setDisabled('agenciaBancaria.idAgenciaBancaria', true);
		document.getElementById('agenciaBancaria.nrAgenciaBancaria').required = 'false';
		
		resetValue(document.getElementById('nrContaCorrente'));		
		setDisabled('nrContaCorrente', true);
		document.getElementById('nrContaCorrente').required = 'false';
		
		resetValue(document.getElementById('nrDigitoContaCorrente'));		
		setDisabled('nrDigitoContaCorrente', true);
		document.getElementById('nrDigitoContaCorrente').required = 'false';
					
		resetValue(document.getElementById('nrBoleto'));		
		setDisabled('nrBoleto', true);
		document.getElementById('nrBoleto').required = 'false';			
		
		resetValue(document.getElementById('dtVencimento'));		
		setDisabled('dtVencimento', true);
		document.getElementById('dtVencimento').required = 'false';
		
		resetValue(document.getElementById('dtProgramadaPagamento'));		
	}
	
	function onTpFormaPagamentoChange(e) {
		// sempre desabilita e seta como nao obrigatório
		disableTpFormaPagamentoRelateds();
	
		if (e[e.selectedIndex].value=='BO') {
			setDisabled('qtParcelasBoletoBancario', false);
			document.getElementById('qtParcelasBoletoBancario').required = 'true';

			setDisabled('banco.idBanco', false);
			document.getElementById('banco.nrBanco').required = 'true';
			
			document.getElementById('agenciaBancaria.nrAgenciaBancaria').required = 'false';
			
			setDisabled('nrContaCorrente', false);
			document.getElementById('nrContaCorrente').required = 'false';
			
			setDisabled('nrDigitoContaCorrente', false);
			document.getElementById('nrDigitoContaCorrente').required = 'false';
		}
		else {
			disableParcelasTab(true);
		
			if (e[e.selectedIndex].value=='DC') {
				setDisabled('banco.idBanco', false);
				document.getElementById('banco.nrBanco').required = 'true';
				
				document.getElementById('agenciaBancaria.nrAgenciaBancaria').required = 'true';
				
				setDisabled('nrContaCorrente', false);
				document.getElementById('nrContaCorrente').required = 'true';
				
				setDisabled('nrDigitoContaCorrente', false);
				document.getElementById('nrDigitoContaCorrente').required = 'true';

				findDadosBancariosPessoa();
			} 
		}
	}
	
	function inicializaCamposByTpFormaPagamento() {
	}
	
	
	function findDadosBancariosPessoa() {
		var idBeneficiario = getElementValue('clienteBeneficiario.idCliente');
		var data = new Array();
		data.idPessoa = idBeneficiario;
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.findDadosBancariosPessoa", "findDadosBancariosPessoa", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function newMaster() {
		var sdo = createServiceDataObject("lms.indenizacoes.incluirReciboIndenizacaoAction.newMaster", null, new Array());
    	xmit({serviceDataObjects:[sdo]});
	}
	
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
	
	function onNrParcelasChange(e) {
		// executa sempre como ação padrão
		disableParcelasTab(true);
		setDisabled('nrBoleto', true);
		resetValue('nrBoleto');
		resetValue(document.getElementById('dtVencimento'));
		document.getElementById('nrBoleto').required = 'false';
		setDisabled('dtVencimento', true);
		resetValue(document.getElementById('dtVencimento'));
		document.getElementById('dtVencimento').required = 'false';			
	
		if (e.value == '') {
			return true;
			
		} else if (e.value == 1) {
			setDisabled('nrBoleto', false);
			document.getElementById('nrBoleto').required = 'true';			
			setDisabled('dtVencimento', false);
			document.getElementById('dtVencimento').required = 'true';			
			return true;

		} else if (e.value > 1 ) {
			disableParcelasTab(false);
			return true;
			
		} else if (e.value < 1) {
			alert(LMS_21000);
			return false;
		}
	}
	
	// função para habilitar/ desablitar tab	
	function tabSetDisabled(tab, disable) {
 		tabGroup.setDisabledTab(tab, disable);	
	}

	/**************************************************
	 * Funções relativas a dados bancários
	 **************************************************/	
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
	
	function resetValueAgencia(){
		resetValue(document.getElementById("agenciaBancaria.idAgenciaBancaria"));
		setDisabled('agenciaBancaria.idAgenciaBancaria', false);
		setFocus(document.getElementById("agenciaBancaria.nrAgenciaBancaria"));
	}
	
	function setaBanco_cb(data,erro){
		var retorno = agenciaBancaria_nrAgenciaBancaria_exactMatch_cb(data);
		if( retorno == true ){
			if( data != undefined && data[0] != undefined && data[0].banco != undefined ){
				setElementValue("banco.idBanco",data[0].banco.idBanco);
				setElementValue("banco.nrBanco",data[0].banco.nrBanco);
				setElementValue("banco.nmBanco",data[0].banco.nmBanco);
			}
		}
	}
	
	function habilitarAnexosTab(){
		disableAnexosTab(false);	
	}
	
	/***
	**** TRECHO COPIADO DA TELA digitarPreAWBGeracaoManutencao
	***/
	var WindowUtils = {
		Options : {
			unardorned : 'no'
			,scroll : 'auto'
			,resizable : 'no'
			,status : 'no'
			,center : 'yes'
			,help : 'no'
			,dialogWidth : '700px'
			,dialogHeight : '350px'
			,toStr : function(){
				var str = '';
				for(var k in this){
					if(typeof(this[k]) == 'string') str += k + ':' + this[k] + ';';
				}
				return str;
			}
		}
		,showModalParams : function(url, params, options){
			return showModalDialog(url, params, options);
		}
		,showModal : function(url){
			return this.showModalParams(url, window, this.Options.toStr());
		}
		,showModalBySize : function(url, window, width, height){
			var opts = ObjectUtils.extend(this.Options, {});
			opts.dialogWidth = width + 'px';
			opts.dialogHeight = height + 'px';
			return this.showModalParams(url, window, opts.toStr());
		}
		,showCadastroCliente : function(tipo, callBack) {
			var data = this.showModalBySize('expedicao/cadastrarClientes.do?cmd=main&origem=exp', window, 750, 300);
			if(data) {
				setDisabled(tipo+"Button", true);
				resetValue(tipo + ".idCliente");
				setElementValue(tipo + ".idCliente", data.idCliente);

				var service = "lms.expedicao.digitarPreAWBAction.findDadosCliente";
				var sdo = createServiceDataObject(service, callBack, {idCliente:data.idCliente});
				xmit({serviceDataObjects:[sdo]});
			}
		}
	}

	var ObjectUtils = {
			extend : function(from, to){
				if(!from || !to) return null;
				for(var k in from) to[k] = from[k];
				return to;
			}
			,objToString : function (o){
				if(!o) return '';

				var saida = '';
				var reg = /function (.*?)\(/m;
				var c = reg.test("" + o.constructor) ? RegExp.$1 : '';

				for(var a in o) saida += c + '.' + a + '='+ o[a] + '\n';

				return saida;
			}
		}
	
</script>
