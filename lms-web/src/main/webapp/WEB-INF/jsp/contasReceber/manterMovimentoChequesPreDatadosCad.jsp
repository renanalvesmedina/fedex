<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterMovimentoChequesPreDatadosAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-36172" />
	</adsm:i18nLabels>
	<adsm:form action="/contasReceber/manterMovimentoChequesPreDatados" 
		service="lms.contasreceber.manterMovimentoChequesPreDatadosAction">
	
		<adsm:hidden value="A" property="statusAtivo"/>	
		<adsm:hidden property="filialMatriz"/>	
		<adsm:hidden property="dtVencimentoAtual"/>	
		<adsm:hidden property="dtVencimentoNova"/>	
		<adsm:hidden property="idProcessoWorkflow"/>
	
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.manterMovimentoChequesPreDatadosAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filial" 
					 size="3" 
					 maxLength="3" 
					 width="33%"
					 labelWidth="15%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="30" disabled="true" serializable="true"/>
		</adsm:lookup>	

		<adsm:range label="lote" width="35%" labelWidth="17%">
			<adsm:textbox dataType="integer" size="10" maxLength="10" property="nrLoteChequeInicial"/>
			<adsm:textbox dataType="integer" size="10" maxLength="10" property="nrLoteChequeFinal"/>
		</adsm:range>

		<adsm:hidden property="nrBanco"/>

		<adsm:lookup 
			dataType="integer" 
			property="agenciaBancaria.banco" 
			idProperty="idBanco"
			service="lms.contasreceber.manterMovimentoChequesPreDatadosAction.findLookupBanco" 
			label="banco" 
			size="3" 
			maxLength="3"
			width="33%"
			allowInvalidCriteriaValue="true"
		    serializable="false"
			criteriaProperty="nrBanco"
			onchange="return bancoOnChange();" 
			onPopupSetValue="bancoOnPopup"
			onDataLoadCallBack="setaNrBanco"
			action="/configuracoes/manterBancos">
 				
			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				formProperty="agenciaBancaria.banco.nmBanco"/> 
				
			<adsm:propertyMapping 
				modelProperty="nrBanco" 
				formProperty="nrBanco"/> 
				
			<adsm:textbox 
				property="agenciaBancaria.banco.nmBanco" 
				dataType="text" 
				size="20" 
				maxLength="30" 
				disabled="true" 
				serializable="false"/>
				
		</adsm:lookup>

		<adsm:hidden property="nrAgencia"/>

		<adsm:lookup 
			dataType="integer" 
			property="agenciaBancaria" 
			idProperty="idAgenciaBancaria"
			service="lms.contasreceber.manterMovimentoChequesPreDatadosAction.findLookupAgencia" 
			label="agencia" 
			maxLength="4" 
			size="4" 
			width="35%" 
			labelWidth="17%"
		    serializable="false"
   			allowInvalidCriteriaValue="true"
			criteriaProperty="nrAgenciaBancaria"
			onchange="return agenciaOnChange();"
			onPopupSetValue="agenciaOnPopup"
			onDataLoadCallBack="myOnDataLoadAgencia"
			action="/configuracoes/manterAgencias">			
				
			<adsm:propertyMapping 
				modelProperty="banco.idBanco" 
				criteriaProperty="agenciaBancaria.banco.idBanco"/> 			
				
			<adsm:propertyMapping 
				modelProperty="banco.nrBanco" 
				criteriaProperty="agenciaBancaria.banco.nrBanco" inlineQuery="true"/> 			
				
			<adsm:propertyMapping 
				modelProperty="banco.nmBanco" 
				criteriaProperty="agenciaBancaria.banco.nmBanco" inlineQuery="true"/> 			
				
			<adsm:propertyMapping 
				modelProperty="nmAgenciaBancaria" 
				formProperty="agenciaBancaria.nmAgenciaBancaria"/> 
				
			<adsm:propertyMapping 
				modelProperty="nrAgenciaBancaria" 
				formProperty="nrAgencia"/> 
				
			<adsm:textbox 
				property="agenciaBancaria.nmAgenciaBancaria" 
				dataType="text" 
				size="20" 
				maxLength="50" 
				disabled="true" 
				serializable="false"/>
		</adsm:lookup>


		<adsm:textbox dataType="integer" property="nrContaBancaria" labelWidth="15%" width="33%" label="conta" size="15" maxLength="12"/>

		<adsm:textbox dataType="integer" property="nrCheque" label="cheque" size="10" maxLength="10" width="35%" labelWidth="17%"/>		

		<adsm:combobox label="situacao" property="tpSituacaoCheque" domain="DM_STATUS_CHEQUE" width="33%" labelWidth="15%"/>

        <adsm:combobox label="ultimaAcao" property="tpHistoricoCheque" domain="DM_HISTORICO_CHEQUE" width="35%" labelWidth="17%" />
        
		<adsm:range label="dataEmissao" width="33%" labelWidth="15%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" width="15%" picker="true"/>		
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" width="20%" picker="true"/>		
		</adsm:range>

		<adsm:range label="dataReapresentacao" width="35%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtReapresentacaoInicial" width="15%" picker="true"/>		
			<adsm:textbox dataType="JTDate" property="dtReapresentacaoFinal" width="20%" picker="true"/>		
		</adsm:range>

		<adsm:range label="dataVencimento" width="85%" labelWidth="15%">
			<adsm:textbox dataType="JTDate" property="dtVencimentoInicial" width="15%" picker="true"/>		
			<adsm:textbox dataType="JTDate" property="dtVencimentoFinal" width="20%" picker="true"/>		
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="historicoCheque"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:form id="acao.form" action="/contasReceber/manterMovimentoChequesPreDatados" service="lms.contasreceber.manterMovimentoChequesPreDatadosAction">
        <adsm:combobox label="acao" labelWidth="17%" width="83%" property="tpHistoricoChequeNovo" service="lms.contasreceber.manterMovimentoChequesPreDatadosAction.findTpHistoricoCheque" optionProperty="value" optionLabelProperty="description" required="true"/>		
		<adsm:hidden property="observacao"/>
		<adsm:hidden property="empresaCobranca"/>
		<adsm:hidden property="alinea"/>
		<adsm:hidden property="dtReapresentacao"/>
	</adsm:form>	
	
	<adsm:grid onDataLoadCallBack="myOnDataLoad" onRowClick="funcaoVazil" gridHeight="147"
		property="historicoCheque" idProperty="idHistoricoCheque" unique="false" scrollBars="vertical" rows="9999"
		showGotoBox="false" showTotalPageCount="false" showRowIndex="false" showPagging="false">
		<adsm:gridColumn width="90" title="banco" dataType="integer" property="cheque.nrBanco"/>
		<adsm:gridColumn width="90" title="agencia" dataType="integer" property="cheque.nrAgencia"/>
		<adsm:gridColumn width="90" title="conta" dataType="integer" property="cheque.nrContaCorrente"/>
		<adsm:gridColumn width="90" title="cheque" dataType="integer" property="cheque.nrCheque"/>
		<adsm:gridColumn width="90" title="dataVencimento" dataType="JTDate" property="cheque.dtVencimento"/>
		<adsm:gridColumn width="90" title="situacao" isDomain="true" property="cheque.tpSituacaoCheque"/>
	
		<adsm:gridColumn title="valor" property="cheque.moedaPais.moeda.siglaSimbolo" width="40" dataType="text"/>			
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">			
			<adsm:gridColumn title="" property="cheque.vlCheque" dataType="currency" width="80"/>
		</adsm:gridColumnGroup>
		
				
		<adsm:gridColumn width="" title="historico" property="cheque" align="center" image="/images/icons/Rotas.gif" link="javascript:openHistoricoCheque(this);" />
		<adsm:buttonBar>			
			<adsm:button caption="alterarVencimento" id="alterarVencimento" onclick="return alterarVencimentoClick();" buttonType="removeButton" disabled="false" />
			<adsm:button caption="aplicarAcao" id="aplicarAcao" onclick="return aplicarAcaoClick();" buttonType="removeButton" disabled="false" />
		</adsm:buttonBar>		
	</adsm:grid>
</adsm:window>
<script>


	function alterarVencimentoClick(){

		if (historicoChequeGridDef.getSelectedIds().ids.length > 1){
			alert(''+i18NLabel.getLabel("LMS-36172"));
		} else {
			//abrir uma popup com a data de vencimento
			setElementValue("dtVencimentoAtual", historicoChequeGridDef.getDataRowById(historicoChequeGridDef.getSelectedIds().ids[0]).cheque.dtVencimento);
			showModalDialog('contasReceber/manterMovimentoChequesPreDatados.do?cmd=dataVencimento',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:130px;');
		}
	}

	function alterarVencimentoScript(){

		var idCheque = historicoChequeGridDef.getDataRowById(historicoChequeGridDef.getSelectedIds().ids[0]).cheque.idCheque;
		var idUltimoHistorico = historicoChequeGridDef.getDataRowById(historicoChequeGridDef.getSelectedIds().ids[0]).idHistoricoCheque;
		var dtVencimentoNova = getElementValue("dtVencimentoNova");

		var data = new Array();			
		
		data.idCheque = idCheque;
		data.idUltimoHistorico = idUltimoHistorico;
		data.dtVencimentoNova = dtVencimentoNova;

		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterMovimentoChequesPreDatadosAction.alterarVencimento", "alterarVencimento", data)); 
		xmit(true);

	}
	
	// Remonta a grid após alterar data de vencimento
	function alterarVencimento_cb(d, e) {
		if (e==undefined) {
			setFocusOnFirstFocusableField(document);
			historicoChequeGridDef.executeLastSearch();
		}
	}

	function initWindow(eventObj){
	
		if(eventObj.name == 'tab_load' || eventObj.name == 'cleanButton_click') {
			
			_serviceDataObjects = new Array();
			
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterMovimentoChequesPreDatadosAction.findFilialUsuarioLogado",
				"setFilialUsuario", 
				new Array()));

			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterMovimentoChequesPreDatadosAction.validateFilialUsuarioMatriz",
				"setFilialMatriz", 
				new Array()));
			
	        xmit(false);

	   }
	}

	// seta a filial quando não é matriz...
	function setFilialUsuario_cb(data, error) {
		if (data != null) {
			if ((getElementValue("filialMatriz") != "true")) {
				setElementValue("filial.idFilial", data.idFilial);
				setElementValue("filial.sgFilial", data.sgFilial);
				setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
			}
		}
		setDisabled("filial.idFilial", (getElementValue("filialMatriz") != "true"));
		setFocusOnFirstFocusableField(document);
	}

	function setFilialMatriz_cb(data, error) {
		if (data != null) {
			setElementValue("filialMatriz", data._value);
		}
	}

	function funcaoVazil(){
		return false;
	}

	function bancoOnChange(){
		var retorno = agenciaBancaria_banco_nrBancoOnChangeHandler();
		setElementValue("nrBanco", getElementValue("agenciaBancaria.banco.nrBanco"));		
	
		return retorno;	
	}
	
	function bancoOnPopup(){
		setElementValue("nrBanco", getElementValue("agenciaBancaria.banco.nrBanco"));		
	}
	
	function agenciaOnChange(){
		setElementValue("nrAgencia", getElementValue("agenciaBancaria.nrAgenciaBancaria"));		
		return agenciaBancaria_nrAgenciaBancariaOnChangeHandler();
	}

	function agenciaOnPopup(){
		setElementValue("nrAgencia", getElementValue("agenciaBancaria.nrAgenciaBancaria"));					
	}
	
	function setaNrBanco_cb(d, e){
		
		// Caso retorne apenas um banco, seta o hidden
		if ( agenciaBancaria_banco_nrBanco_exactMatch_cb(d,e) ){
			setElementValue("nrBanco", getElementValue("agenciaBancaria.banco.nrBanco"));			
		
		// Caso retorne mais de um banco ou não retorne nenhum banco, limpa o nome do banco
		} else {
			setElementValue("agenciaBancaria.banco.nmBanco", "");			
		}
		
	
	}
	
	function myOnDataLoadAgencia_cb(d,e){
		agenciaBancaria_nrAgenciaBancaria_exactMatch_cb(d,e);
		if (e == undefined) {
			if ((d.length == 0)){
				setElementValue("agenciaBancaria.nmAgenciaBancaria", "");
			}			
		}
		setElementValue("nrBanco", getElementValue("agenciaBancaria.banco.nrBanco"));		
		setElementValue("nrAgencia", getElementValue("agenciaBancaria.nrAgenciaBancaria"));
	}		
	
	function openHistoricoCheque(imagem){ 
	}
	
	function myOnPageLoad_cb(d,e,c,x){
	
		var url = new URL(parent.location.href);
		
		if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
			
			// Carrega o Boleto de acordo com idHistoricoBoleto
			var dados = new Array();
		
		    setNestedBeanPropertyValue(dados, "idProcesso", url.parameters.idProcessoWorkflow);
	        var sdo = createServiceDataObject("lms.contasreceber.manterMovimentoChequesPreDatadosAction.findCheque",
	                                             "findCheque",
	                                             dados);
	         xmit({serviceDataObjects:[sdo]});
		
		}
		
		onPageLoad_cb(d,e,c,x);
		setDisabled("aplicarAcao", true);
		
	}
	
	/**
	 * CallBack da função findCheque 
	 */
	function findCheque_cb(data, error){
		
		if( error != undefined ){
			alert(error);
		}
		
		// Popula o form 
		onDataLoad_cb(data, error);
		
		// Popula a grid de acordo com os filtros 
		historicoChequeGridDef.executeSearch(data);
		
	}
	
	function myOnDataLoad_cb(searchFilters){
		setDisabled("aplicarAcao", true);
	}	
	
	function aplicarAcaoClick(){
	
		document.getElementById("tpHistoricoChequeNovo").required = 'true';

		var valid = validateTabScript(document.getElementById("acao.form"));

		document.getElementById("tpHistoricoChequeNovo").required = 'false';
		
		// apenas prossegue se a validação dos dados foi realizada com sucesso.
		if (valid == false) {
			return false;
		}	
	
		//Se a nova situação do historico do cheque é 'Transferência para empresa cobrança'
		//abrir uma popup com uma combo de empresa cobrança
		if (getElementValue("tpHistoricoChequeNovo") == "TC"){
			showModalDialog('contasReceber/manterMovimentoChequesPreDatados.do?cmd=lookup',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:200px;');
		//Se a nova situação do historico do cheque é 'Reapresentado'
		//abrir uma popup com um de data		
		} else if (getElementValue("tpHistoricoChequeNovo") == "RE"){
			showModalDialog('contasReceber/manterMovimentoChequesPreDatados.do?cmd=date',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:200px;');
		//Se a nova situação do historico do cheque é 'Devolvido pelo Banco'
		//abrir uma popup com uma lookup de alinea
		} else if (getElementValue("tpHistoricoChequeNovo") == "DB"){
			showModalDialog('contasReceber/manterMovimentoChequesPreDatados.do?cmd=alinea',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:200px;');
		//Senão, uma popup com um campo de observação			
		} else {
			showModalDialog('contasReceber/manterMovimentoChequesPreDatados.do?cmd=observacao',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:180px;');		
		}
		
	}
	
	// Remonta a grid após aplicar as ações para atualizar os ids de histórico
	function aplicarAcao_cb(d, e) {
		onDataLoad_cb(d, e);
		if (e==undefined) {
			resetValue("tpHistoricoChequeNovo");
			setFocusOnFirstFocusableField(document);
			historicoChequeGridDef.executeLastSearch();
		}
	}

	function aplicarAcaoScript(){
		
		var dataAcao = buildFormBeanFromForm(document.getElementById("acao.form"));
		var dataGrid = historicoChequeGridDef.getSelectedIds(); //buildFormBeanFromForm(document.getElementById("historicoCheque.form"));
		
		var data = new Array();			

		merge(data,dataAcao);
		merge(data,dataGrid);
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterMovimentoChequesPreDatadosAction.store", "aplicarAcao", data)); 
		xmit(true); // deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
	}
	
	document.getElementById("tpHistoricoChequeNovo").required = false;
</script>