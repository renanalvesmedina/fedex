<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterBoletosAction"
	onPageLoadCallBack="myOnPageLoad">
	<adsm:form id="boleto.form" idProperty="idBoleto"
		action="/contasReceber/manterBoletos"
		service="lms.contasreceber.manterBoletosAction.findById"
		onDataLoadCallBack="myOnDataLoad">
		
		<adsm:textbox label="numeroBoleto" property="nrBoleto" maxLength="13"
			dataType="text" labelWidth="20%" width="30%" disabled="true" />
		<adsm:textbox label="sequencialFilial" property="nrSequenciaFilial"
			maxLength="13" dataType="integer" width="30%" labelWidth="20%"
			disabled="true" />
		<adsm:hidden property="boletoDsConcatenado" serializable="false"/>

		<adsm:hidden property="idProcessoWorkflow"/>
		
		<adsm:combobox property="documento.tpDocumento"
			label="documentoServico" width="30%" labelWidth="20%"
			   service="lms.contasreceber.manterBoletosAction.findComboTpDocumento"
			optionProperty="value" optionLabelProperty="description"
			serializable="true" onchange="return tpDocumentoServicoOnChange();">

			<adsm:lookup dataType="text" property="documento.filial"
				idProperty="idFilial" criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterBoletosAction.findLookupFilial"
				disabled="true" action="" size="3" maxLength="3" picker="false"
						 onchange="return filialOnChange();"
				onDataLoadCallBack="filialOnChange" exactMatch="true">
				<adsm:propertyMapping
					relatedProperty="documento.filial.pessoa.nmFantasia"
					modelProperty="pessoa.nmFantasia" />
			</adsm:lookup>
			<adsm:lookup dataType="integer" property="documento"
				idProperty="idDocumento" criteriaProperty="nrDocumento"
						 service="lms.contasreceber.manterBoletosAction.findDocumento"
				action="/contasReceber/pesquisarDevedorDocServFatLookUp" cmd="pesq"
				size="11" maxLength="10" serializable="true" disabled="true"
				exactMatch="true" popupLabel="pesquisarDocumentoServico"
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadDocumento"
				onchange="return onChangeDocumento();" mask="0000000000"
				required="true">
			</adsm:lookup>
		</adsm:combobox>
		
		<adsm:hidden property="documento.filial.pessoa.nmFantasia"/>

		<adsm:combobox 
			service="lms.contasreceber.manterBoletosAction.findComboCedentesAtivos" 
			optionLabelProperty="comboText" optionProperty="idCedente"
			property="cedente.idCedente" label="banco" boxWidth="180"
			labelWidth="20%" width="30%" required="true"> 
		</adsm:combobox>	
		
		<adsm:hidden property="fatura.filialByIdFilialCobranca.idFilial"/>
		
		<adsm:textbox property="fatura.filialByIdFilialCobranca.sgFilial"
			dataType="text" label="filialCobranca" size="3" width="30%"
			labelWidth="20%" style="width:45px" maxLength="3" disabled="true">
			<adsm:textbox dataType="text"
				property="fatura.filialByIdFilialCobranca.pessoa.nmFantasia"
				size="30" serializable="false" disabled="true" />
		</adsm:textbox>			

		<adsm:section caption="dadosCliente" />

		<adsm:hidden property="cliente.idCliente"/>

		<adsm:textbox label="cliente" dataType="text"
			property="cliente.pessoa.nrIdentificacao" size="20" maxLength="15"
			labelWidth="20%" width="80%" disabled="true">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa"
				disabled="true" size="60" />
		</adsm:textbox>
		
		<adsm:textbox property="cliente.divisaoCliente.dsDivisaoCliente"
			label="divisao" width="80%" labelWidth="20%" dataType="text"
			size="50" disabled="true" />
		<adsm:hidden property="cliente.divisaoCliente.idDivisaoCliente"/>	
		
		<adsm:textbox label="enderecoCobranca" property="enderecoCobranca"
			dataType="text" size="70" labelWidth="20%" width="80%"
			disabled="true" />
		<adsm:textbox label="municipio" property="municipio" dataType="text"
			size="35" labelWidth="20%" width="30%" disabled="true" />
		<adsm:textbox label="bairro" property="bairro" dataType="text"
			size="35" labelWidth="22%" width="28%" disabled="true" />
		<adsm:textbox label="cep" property="cep" dataType="text" size="20"
			labelWidth="20%" width="80%" disabled="true" />

		<adsm:section caption="informacoesComplementares" />

		<adsm:combobox label="situacao" property="tpSituacaoBoleto"
			domain="DM_STATUS_BOLETO" labelWidth="20%" width="30%"
			defaultValue="DI" disabled="true" />
		
		<adsm:textbox label="dataEmissao" property="dtEmissao"
			dataType="JTDate" labelWidth="22%" width="28%" required="true"
			onchange="findDataVencimento();" />

		<adsm:textbox label="dataVencimento" property="dtVencimento"
			dataType="JTDate" labelWidth="20%" width="30%" />
		
		<adsm:textbox label="novoVencimentoEmAprovacao"
			property="dtVencimentoNovo" dataType="JTDate" labelWidth="22%"
			width="28%" disabled="true" picker="false" />
		
		
		<adsm:textbox label="cotacao" width="8%" labelWidth="20%"
			dataType="text" property="simboloMoedaPais" size="8"
			serializable="false" disabled="true" />
			
		<adsm:textbox property="dtCotacaoMoeda" dataType="JTDate"
			style="width:89px;" disabled="true" picker="false" width="9%"
			serializable="false"/>
		
		<adsm:lookup property="cotacaoMoeda" idProperty="idCotacaoMoeda" 
			criteriaProperty="vlCotacaoMoeda" 
			action="/configuracoes/manterCotacoesMoedas" dataType="currency"
			onchange="vlCotacaoMoedaOnChange(this);"
			onPopupSetValue="cotacaoMoedaSetValue"
			service="lms.contasreceber.manterFaturasAction.findLookupCotacaoMoeda" 
			serializable="true" disabled="true" size="10" width="13%">
			<adsm:propertyMapping criteriaProperty="idPaisCotacao"
				modelProperty="moedaPais.pais.idPais" disable="false" />
			<adsm:propertyMapping criteriaProperty="nmPaisCotacao"
				modelProperty="moedaPais.pais.nmPais" disable="false" />
			<adsm:propertyMapping relatedProperty="dtCotacaoMoeda"
				modelProperty="dtCotacaoMoeda" />
			<adsm:propertyMapping relatedProperty="vlCotacaoMoedaTmp"
				modelProperty="vlCotacaoMoeda" />
		</adsm:lookup>
		<adsm:hidden property="tpAbrangencia"/>
		<adsm:hidden property="vlCotacaoMoeda" serializable="true"/>
		<adsm:hidden property="vlCotacaoMoedaTmp" serializable="false"/>
		<adsm:hidden property="idPaisCotacao" serializable="false"/>
		<adsm:hidden property="nmPaisCotacao" serializable="false"/>		
		
		<adsm:textbox label="valorBoleto" property="vlTotal"
			dataType="currency" labelWidth="22%" width="28%" disabled="true" />

		<adsm:textbox label="dataLiquidacao" property="fatura.dtLiquidacao"
			dataType="JTDate" labelWidth="20%" width="30%" disabled="true" />
		<adsm:textbox label="valorJuro" property="fatura.vlJuroCalculado"
			dataType="currency" labelWidth="22%" width="28%" disabled="true" />

		<adsm:hidden property="relacaoCobranca.idRelacaoCobranca"/>
		<adsm:complement label="relacaoCobranca" labelWidth="20%" width="30%">
			<adsm:textbox dataType="text"
				property="relacaoCobranca.filial.sgFilial" size="3" width="4%"
				disabled="true" maxLength="3" />
			<adsm:textbox dataType="text"
				property="relacaoCobranca.nrRelacaoCobrancaFilial" size="6"
				width="9%" disabled="true" maxLength="10" />
		</adsm:complement>
		
		<adsm:textbox label="valorDesconto" property="vlDesconto"
			dataType="currency" labelWidth="22%" width="28%" disabled="true" />

		<adsm:textbox label="valorJuroDiario" property="vlJurosDia"
			dataType="currency" labelWidth="20%" width="30%" disabled="true" />
		<adsm:checkbox label="gerarEDI" property="fatura.blGerarEdi"
			labelWidth="22%" width="28%" />
		<adsm:textbox labelWidth="20%" dataType="JTDateTimeZone"
			label="dataTransmissaoEDI" property="dhTransmissao" size="10"
			maxLength="20" width="30%" disabled="true" picker="false"/>


		<adsm:textbox dataType="text" label="usuarioReemissor"
			property="usuario.nmUsuario" size="30" maxLength="60"
			labelWidth="22%" width="28%" disabled="true" />
		<adsm:textbox labelWidth="20%" dataType="JTDateTimeZone"
			label="dataHoraReemissao" property="dhReemissao" size="10"
			maxLength="20" width="30%" disabled="true" />


		<adsm:buttonBar lines="2">
			<adsm:button id="consultarHistoricoOcorrenciasBoleto"
				caption="consultarHistoricoOcorrenciasBoleto"
				action="/contasReceber/consultarHistoricoOcorrenciasBoleto"
				boxWidth="290" cmd="main">
				<adsm:linkProperty src="idBoleto" target="idBoleto"/>
				<adsm:linkProperty src="nrBoleto" target="nrBoleto"/>				
				<adsm:linkProperty src="documento.filial.sgFilial" target="sgFilial"/>
				<adsm:linkProperty src="documento.nrDocumento" target="nrFatura"/>								
			</adsm:button>
			<adsm:button id="baixarBanco" caption="baixarProtesto" boxWidth="110"
				onclick="return baixarBancoBoleto();" breakBefore="true" />
			<adsm:button id="alterarCEP" caption="alterarCEP" boxWidth="90"
				onclick="return alterarCEPBoleto();" />
			<adsm:button id="emitir" caption="emitir" boxWidth="70"
				onclick="return emitirBoleto();" />
			<adsm:button id="retransmitir" caption="retransmitir" boxWidth="90"
				onclick="return retransmitirBoleto();" />
			<adsm:button id="protestar" caption="protestar" boxWidth="70"
				onclick="return protestarBoleto();" />
			<adsm:button id="prorrogarVencimento" caption="prorrogarVencimento"
				onclick="return prorrogarVencimentoBoleto();" boxWidth="140" />
			<adsm:button id="cancelar" caption="cancelar" boxWidth="70"
				onclick="return cancelarBoleto();" />
			<adsm:button id="fatura" caption="fatura"
				action="/contasReceber/manterFaturas" cmd="main" boxWidth="70">
				<adsm:linkProperty src="documento.idDocumento" target="idFatura"/>
				<adsm:linkProperty src="documento.filial.sgFilial"
					target="filialByIdFilial.sgFilial" />
				<adsm:linkProperty src="documento.filial.idFilial"
					target="filialByIdFilial.idFilial" />
				<adsm:linkProperty src="documento.filial.pessoa.nmFantasia"
					target="filialByIdFilial.pessoa.nmFantasia" />
				<adsm:linkProperty src="documento.nrDocumento" target="nrFatura"/>				

			</adsm:button>
			<adsm:storeButton callbackProperty="myStore" id="storeButton"/>
			<adsm:newButton id="newButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script
	src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js"
	type="text/javascript"></script>
<script>
	
	getElement("documento.tpDocumento").required = true;
	getElement("documento.filial.sgFilial").required = true;
	
	var nameEvento = "";
	var habilitaBotoes = "";
	var habilitaRetransmitir = "";
	var habilitaBotoesProtesto = "";
	
	/*
	 *
	 *
	 * CÓDIGO PARA A LOOKUP DE DOCUMENTO DE SERVICO/FATURA
	 *
	 *
	 *
	 */
	 
	 
	/*
	 * On Change da lookup de Filial
	 */
	function resetDevedor() {
	
		resetValue("documento.idDocumento");
		resetValue("documento.nrDocumento");
		
		if (getElementValue("documento.tpDocumento") == "") {
			setDisabled("documento.filial.idFilial",true);
			setDisabled("documento.idDocumento",true);
		} else {
			setDisabled("documento.filial.idFilial",false);
			habilitaLupa();
			if (getElementValue("documento.filial.sgFilial") != "") {
				setDisabled("documento.idDocumento",false);
			}
		}
		
		resetDadosCliente();	
	}			
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("documento.idDocumento", false);
		setDisabled("documento.nrDocumento", true);
	}	 


	/*
	 * On change da combo de Tipo de Documento de Serivo.<BR>
	 * Altera lookup de conhecimento
	 * @see changeLookupConhecimento
	 */
	function tpDocumentoServicoOnChange(){	
		resetDevedor();				

		if (getElementValue("documento.tpDocumento") == "FAT"){
			document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterBoletosAction.findFatura";
			document.getElementById("documento.idDocumento").cmd = "list";
			document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/manterFaturas.do";
			
			document.getElementById("documento.idDocumento").propertyMappings = [  
			{ modelProperty:"nrFatura", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false }, 
			{ modelProperty:"nrFatura", relatedProperty:"documento.nrDocumento", blankFill:true }, 
			{ modelProperty:"filialByIdFilial.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
			{ modelProperty:"filialByIdFilial.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
			{ modelProperty:"filialByIdFilial.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
			{ modelProperty:"filialByIdFilial.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },
			{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", criteriaProperty:"documento.filial.pessoa.nmFantasia", inlineQuery:true, disable:true },
			{ modelProperty:"filialByIdFilial.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", inlineQuery:true, blankFill:false }			
			];			
						
		} else {
			document.getElementById("documento.idDocumento").service = "lms.contasreceber.manterBoletosAction.findDevedorServDocFat";
			document.getElementById("documento.idDocumento").cmd = "pesq";
			document.getElementById("documento.idDocumento").url = contextRoot+"/contasReceber/pesquisarDevedorDocServFatLookUp.do";
			
			document.getElementById("documento.idDocumento").propertyMappings = [  
			{ modelProperty:"doctoServico.nrDoctoServico", criteriaProperty:"documento.nrDocumento", inlineQuery:true, disable:false }, 
			{ modelProperty:"doctoServico.nrDoctoServico", relatedProperty:"documento.nrDocumento", blankFill:true }, 
			{ modelProperty:"doctoServico.tpDocumentoServico", criteriaProperty:"documento.tpDocumento", inlineQuery:true, disable:true }, 
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.idFilial", criteriaProperty:"documento.filial.idFilial", inlineQuery:true, disable:true }, 
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.sgFilial", criteriaProperty:"documento.filial.sgFilial", inlineQuery:true, disable:true },
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.sgFilial", relatedProperty:"documento.filial.sgFilial", blankFill:false },			
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.idFilial", relatedProperty:"documento.filial.idFilial", blankFill:false },
			{ modelProperty:"doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", relatedProperty:"documento.filial.pessoa.nmFantasia", blankFill:false }						
			];
		}

		resetValue("documento.filial.idFilial");
		filialOnChange();		
		setMaskNrDocumento("documento.nrDocumento", getElementValue("documento.tpDocumento"))					

	}
	

	
	/*
	 * On Change da lookup de Filial
	 */
	function filialOnChange(){
		
		resetDevedor();		
		
		var siglaFilial = getElement('documento.filial.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = documento_filial_sgFilialOnChangeHandler();
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('documento_lupa',false);		
		}
		
		return retorno;
	}	
	
	/*
	 * On Change callBack da lookup de Filial
	 */	
	function filialOnChange_cb(data,e,c,x){
		resetDevedor();
		if (data.length == 1) {			
			setDisabled("documento.idDocumento",false);	
			__lookupSetValue({e:getElement("documento.filial.idFilial"), data:data[0]});
			return true;
		} else {
			alert(lookup_noRecordFound);
		}			
	}
	
	function onChangeDocumento(){
		if (getElementValue("documento.nrDocumento") == ""){
			resetDadosCliente();
		}
		return documento_nrDocumentoOnChangeHandler();
	}	
	
		
	function myOnPopupSetValue(data, dialogWindow){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false};
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.validateMonitoramentoEletronicoAutorizado", "validateMonitoramentoEletronicoAutorizado", {data:data});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
		
		return false;
	}
	
	function validateMonitoramentoEletronicoAutorizado_cb(data, error) {
		if (error) {
			alert(error);
		} else {
			if (data.data.idFatura != null) {
				setElementValue("documento.idDocumento",data.data.idFatura);
				setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.data.nrFatura));
				setElementValue("documento.filial.idFilial",data.data.filialByIdFilial.idFilial);
				setElementValue("documento.filial.sgFilial",data.data.filialByIdFilial.sgFilial);
				setElementValue("documento.filial.pessoa.nmFantasia",data.data.filialByIdFilial.pessoa.nmFantasia);
				setDisabled("documento.idDocumento", false);
				findDadosFatura();
			} else if (data.data.idDevedorDocServFat != null) {			
				setElementValue("documento.idDocumento",data.data.idDevedorDocServFat);
				setElementValue("documento.nrDocumento",setFormat(getElement("documento.nrDocumento"),data.data.nrDoctoServico));
				setElementValue("documento.filial.idFilial",data.data.idFilialOrigem);
				setElementValue("documento.filial.sgFilial",data.data.sgFilialOrigem);
				setDisabled("documento.idDocumento", false);			
				findDadosDevedor();						
			} else {
				habilitaLupa();	
			}
		}
		
		return false;
	}
	
	function myOnDataLoadDocumento_cb(d,e){
		documento_nrDocumento_exactMatch_cb(d,e);
		if (e == undefined) {
			if ((d.length == 1)){
				myOnPopupSetValue(d[0], undefined);
			}			
		}
	}
	
	function findDadosFatura(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.findFaturaRelated", "findDadosFatura", {idFatura:getElementValue("documento.idDocumento")});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}	

	function findDadosDevedor(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.findDevedorDocServFatRelated", "findDadosDevedor", {idDevedor:getElementValue("documento.idDocumento"), dtEmissao:getElementValue("dtEmissao")});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}	
	
	function findDadosFatura_cb(d,e,c,x){
		onDataLoad_cb(d,e,c,x);
		getTab(document).setChanged(true);
		
		if (e == null){ 
			fillDadosCliente(d);
			//Se tem cedente e é uma fatura, não pode trocar o cedente
			if (d.blDisableCedente == "S"){
				setDisabled("cedente.idCedente", true);
			}

			//Sempre desabilitar a cotação
			setDisabled("cotacaoMoeda.idCotacaoMoeda", true);
			document.getElementById("cotacaoMoeda.idCotacaoMoeda").required = "false";			
		}			
	}
	
	function findDadosDevedor_cb(d,e,c,x){
		onDataLoad_cb(d,e,c,x);
		getTab(document).setChanged(true);

		if (e == null){
			fillDadosCliente(d);
			
			//Se o tipo de abrangencia é internation, habilitar a cotação
			if (d.tpAbrangencia == 'I'){
				setDisabled("cotacaoMoeda.idCotacaoMoeda", false);
				document.getElementById("cotacaoMoeda.idCotacaoMoeda").required = "true";
			} else {
				setDisabled("cotacaoMoeda.idCotacaoMoeda", true);
				document.getElementById("cotacaoMoeda.idCotacaoMoeda").required = "false";
			}
		}
	}	
	
	function fillDadosCliente(d){
		if (d.cedentes != null) {
			//montar a combo de divisão			
			cedente_idCedente_cb(d.cedentes);			
		}				
			
		//Se tem cedente
		if (d.cedente != null){
			setElementValue("cedente.idCedente", d.cedente.idCedente);
		}
		
		var evento = new Object();
		evento.name = "newButton";
		setEnableDisableButtons(document, evento);		
	}
	
	function resetDadosCliente(){
		resetValue("fatura.filialByIdFilialCobranca.sgFilial");
		resetValue("fatura.filialByIdFilialCobranca.pessoa.nmFantasia");
		resetValue("cliente.idCliente");
		resetValue("cliente.pessoa.nrIdentificacao");
		resetValue("cliente.pessoa.nmPessoa");
		resetValue("cliente.divisaoCliente.idDivisaoCliente");
		resetValue("cliente.divisaoCliente.dsDivisaoCliente");
		resetValue("enderecoCobranca");
		resetValue("municipio");
		resetValue("bairro");
		resetValue("cep");
		resetValue("vlTotal");
		resetValue("vlDesconto");
		resetValue("fatura.blGerarEdi");
		resetValue("dhTransmissao");
		resetValue("tpAbrangencia");    	
		resetValue("dtEmissao");
		resetValue("dtVencimento"); 
		resetValue("cotacaoMoeda.idCotacaoMoeda");
		resetValue("simboloMoedaPais");
		resetValue("dtCotacaoMoeda");
		resetValue("tpAbrangencia");
		resetValue("vlCotacaoMoeda");
		resetValue("vlCotacaoMoedaTmp");	
		
		setElementValue("dtEmissao", setFormat(getElement("dtEmissao"), dtAtual));
		
		setElementValue("fatura.blGerarEdi", true);
		//desabilitar a cotação
		setDisabled("cotacaoMoeda.idCotacaoMoeda", true);
		setDisabled("cotacaoMoeda.vlCotacaoMoeda", true);
		document.getElementById("cotacaoMoeda.idCotacaoMoeda").required = "false";

		setDisabled("cedente.idCedente", false);
	}
	
	function findDataVencimento(){
		if (getElementValue("documento.idDocumento") != null && getElementValue("documento.tpDocumento") != "FAT"){
			var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
			var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.findDataVencimento", "onDataLoad", {idDevedor:getElementValue("documento.idDocumento"), dtEmissao:getElementValue("dtEmissao")});
			remoteCall.serviceDataObjects.push(storeSDO);
			xmit(remoteCall);
		}
	}

	
	/*
	 *
	 *
	 * CÓDIGO PARA A COTAÇÃO
	 *
	 *
	 */
	
	
	/*
	 * Ao clicar na listagem da popUp (lookup) atribui o valor do simboloMoedaPais
	 */
	function cotacaoMoedaSetValue(data){
		if (data == undefined) return;
		setElementValue( "simboloMoedaPais", getSimboloMoedaPais(data.moedaPais) );
	}
	
	/*
	 * Não faz a busca quando alterar o valor, deixando apenas no clique da picker.
	 */
	function vlCotacaoMoedaOnChange(eThis){
		if (getElementValue("vlCotacaoMoedaTmp") == getElementValue("cotacaoMoeda.vlCotacaoMoeda")){
			setElementValue("vlCotacaoMoeda","");
		} else {
			setElementValue("vlCotacaoMoeda",getElementValue("cotacaoMoeda.vlCotacaoMoeda"));
			format(document.getElementById("vlCotacaoMoeda"), event);
		}
		return true;
	}
	
	/*
	 * Concatena o simbolo da moeda com a sigla do país
	 */
	function getSimboloMoedaPais(moedaPais){
		if (moedaPais == undefined) return "";
		if (moedaPais.moeda != undefined && moedaPais.pais != undefined){
			return moedaPais.moeda.dsSimbolo + " - " + moedaPais.pais.sgPais;
		}
		return "";
	}
	
	
	
	/**
	 *
	 *
	 * CÓDIGO ESPECIFICO DOS BOTÕES
	 *
	 *
	 */		
	
	function cancelarBoleto(){
		var tpSituacaoBoletoTmp = getElementValue("tpSituacaoBoleto");
		if (tpSituacaoBoletoTmp == "BN" || tpSituacaoBoletoTmp == "BP") {
			var sdo = createServiceDataObject(
					"lms.contasreceber.manterBoletosAction.findRelacaoPagtoParcial",
					"findRelacaoPagtoParcial",
					{ idBoleto : getElementValue("idBoleto") });
			xmit({ serviceDataObjects : [ sdo ] });
		} else {
			var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:true}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
			var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.cancelarBoleto", "cancelarBoleto", 
				{idBoleto:getElementValue("idBoleto")});
			remoteCall.serviceDataObjects.push(storeSDO);
			xmit(remoteCall);
		}
	}
	
	function findRelacaoPagtoParcial_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		openModalDialog(
				'contasReceber/manterBoletos.do?cmd=observacao',
				{
					idBoleto : getElementValue("idBoleto"),
					onDataLoadCallBack : myOnDataLoad_cb
				},
				'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:530px;dialogHeight:260px;');
	}

	function cancelarBoleto_cb(d,e,c,x){
		myOnDataLoad_cb(d,e,c,x);
	}
	
	function prorrogarVencimentoBoleto(){
		var tpSituacaoBoletoTmp = getElementValue("tpSituacaoBoleto");
		
		//Se a situação do boleto for 'Em banco' ou 'Em banco com protesto':
		//abrir uma popup que pedi informações complementares
		if (tpSituacaoBoletoTmp == "BN"){
			openModalDialog('contasReceber/manterBoletos.do?cmd=observacaoDate',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:550px;dialogHeight:200px;');		
		} else {
			openModalDialog('contasReceber/manterBoletos.do?cmd=date',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:300px;dialogHeight:90px;');		
		}
	}	
	
	function baixarBancoBoleto(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.baixarBancoBoleto", "baixarBancoBoleto", 
			{idBoleto:getElementValue("idBoleto")});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}
	
	function baixarBancoBoleto_cb(data, error){
		if (buttons_cb(data, error)){
			setElementValue("tpSituacaoBoleto", "EM");		
		}
	}
	
	function alterarCEPBoleto(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.alterarCEPBoleto", "buttons", 
			{idBoleto:getElementValue("idBoleto")});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}
	
	function emitirBoleto(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.emitirBoleto", "emitirRedeco", 
			buildFormBeanFromForm(document.forms[0]));
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}
	
	function emitirRedeco_cb(d,e){
		openReportWithLocator(d,e);
		if (e == undefined) {
			setElementValue("tpSituacaoBoleto", d.tpSituacaoBoleto);
		}
	}	
	
	function retransmitirBoleto(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.retransmitirBoleto", "buttons", 
			{idBoleto:getElementValue("idBoleto")});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}
	
	function protestarBoleto(){
		var remoteCall = {serviceDataObjects:new Array(), handleCallbackUserMessages:false}; // n?o deseja que o alert seja exibido, a fun??o store_cb ir? mostrar caso ocorra erro.
		var storeSDO = createServiceDataObject("lms.contasreceber.manterBoletosAction.protestarBoleto", "buttons", 
			{idBoleto:getElementValue("idBoleto")});
		remoteCall.serviceDataObjects.push(storeSDO);
		xmit(remoteCall);
	}		
			
	
	function buttons_cb(data, error){
		if ( error != undefined ) {
			alert(error);
			return false;
		}
		showSuccessMessage();		
		return true;
	}
	
	/**
	 *
	 *
	 * COMPORTAMENTO DA TELA
	 *
	 *
	 */
	 
	var dtAtual;	
	
	function myOnDataLoad_cb(d,e,c,x){
		if (e == undefined) {
			if (d.cedentes) {
				//montar a combo de cedente			
				cedente_idCedente_cb(d.cedentes);			
			}		
		}	

		onDataLoad_cb(d,e,c,x);	
		document.getElementById("storeButton").focus();
	
		setDisabled("boleto.form", true);
		setDisabled("storeButton", true);

		if (e == undefined) {
			controlButtonOnDetail(d);
		}
		
		var idProcessoWorkflow = getElementValue('idProcessoWorkflow');
		
		/** Caso exista idProcessoWorkflow, seleciona a tab cad, e desabilita todo documento */
		if (idProcessoWorkflow != ""){
			showSuccessMessage();
			setDisabled(document, true);
		}
		
		setElementValue("boletoDsConcatenado", 
				getElementValue("documento.tpDocumento") + " - " + 
				getElementValue("documento.filial.sgFilial") + " - " +
				getElementValue("documento.nrDocumento")
				 );
		
	}
	
	function controlButtonOnDetail (d) {
		
		//Variáveis usadas no momento de troca da tela de anexos para detalhamento.
		//Dessa forma os botões ficam habilitados/desabilitados de acordo com a regra.
		habilitaBotoes = d.blHabilitaBotoes;
		habilitaRetransmitir = d.blHabilitaRetransmitir;
		habilitaBotoesProtesto = d.blHabilitaBotoesProtesto;
		
		setDisabled("consultarHistoricoOcorrenciasBoleto",false);
		setDisabled("fatura",false);
		setDisabled("newButton",false);
		
		document.getElementById("newButton").focus();
					
		//Se a filial da fatura é a mesma que a filial da sessão, habilitar os boões
		if (d.blHabilitaBotoes == "S"){
			desabilitaBotoes(false); 
		}
		
		if (d.blHabilitaRetransmitir == "true") {  
			setDisabled("retransmitir", false);
		} else {
			setDisabled("retransmitir", true);
		}
		
		if (d.blHabilitaBotoesProtesto == "true") {
			setDisabled("protestar", false);
			setDisabled("baixarBanco", false);
		} else {
			setDisabled("protestar", true);
			setDisabled("baixarBanco", true);
		}
	}
	
	function desabilitaBotoes(blHabilita){
		setDisabled("consultarHistoricoOcorrenciasBoleto",blHabilita);	
		setDisabled("alterarCEP",blHabilita);
		setDisabled("emitir",blHabilita);
		setDisabled("retransmitir",blHabilita);
		setDisabled("prorrogarVencimento",blHabilita);
		setDisabled("cancelar",blHabilita);
		setDisabled("fatura",blHabilita);
		setDisabled("newButton",blHabilita);
	}

	
	function initWindow(evento){
		if(evento.name == undefined){
			if(nameEvento == "gridRow"){
				var id = getElementValue("idBoleto");
				if(id != ""){
					addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.findById", "myOnDataLoad", {idBoleto:id}));
	    			xmit(false);
				}				
			} else {
				setDisabled("boleto.form", true);
				desabilitaBotoes(false);
				setDisabled("storeButton", true);				
			}
		}
		
		//Ao clicar no botão 'salvar' na tela de 'detalhamento', executa este IF
		//Ao clicar no botão 'salvar tudo' na tela de 'anexo', executa este IF e depois o IF acima
		if (evento.name == "storeButton"){
			setDisabled("boleto.form", true);
			desabilitaBotoes(false);		
			setDisabled("storeButton", true);
		}
		
		//Quando a tela de detalhamento é aberta ao detalhar um registro.
		//Variável 'nameEvento' usada para teste no momento de 'Salvar Tudo'
		if(evento.name == "gridRow_click"){
			nameEvento = "gridRow";
		}
		
		if (evento.name == "newButton_click" || (evento.name == "tab_click" && evento.src.tabGroup.oldSelectedTab.properties.id == "pesq") || evento.name == "removeButton" ){
			limpaVariaveis();
			resetTela();
			resetDevedor();
			setFocusOnFirstFocusableField();
			getCedentePadrao();
		    addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.clearSessionItens",
                    null,
                    null));
    		xmit(false);
		}
		
		//IF usado ao sair da tela de 'anexo' e voltar para 'detalhamento' após um registro ser detalhado.
		//Dessa forma os botões continuam do mesmo jeito (habilitado/desabilitado)que estavam no momento em que o registro foi detalhado 
		if(evento.name == "tab_click" && evento.src.tabGroup.oldSelectedTab.properties.id == "anexo"){
			var id = getElementValue("idBoleto");
			if(id != ""){		
				setDisabled("boleto.form", true);
				setDisabled("storeButton", true);
				
				if (habilitaBotoes == "S"){
					desabilitaBotoes(false); 
	}
	
				if (habilitaRetransmitir == "true") {  
					setDisabled("retransmitir", false);
				} else {
					setDisabled("retransmitir", true);
				}
				
				if (habilitaBotoesProtesto == "true") {
					setDisabled("protestar", false);
					setDisabled("baixarBanco", false);
				} else {
					setDisabled("protestar", true);
					setDisabled("baixarBanco", true);
				}
			}
		}
	}
	
	function limpaVariaveis(){
		nameEvento = "";
		habilitaBotoes = "";
		habilitaRetransmitir = "";
		habilitaBotoesProtesto = "";
	}
	
	function resetTela(){
		setDisabled("documento.tpDocumento", false);
		setDisabled("cedente.idCedente", false);
		setDisabled("fatura.filialByIdFilialCobranca.idFilial", false);
		setDisabled("cliente.idCliente", false);
		setDisabled("dtEmissao", false);
		setDisabled("dtVencimento", false);
		setDisabled("cotacaoMoeda.vlCotacaoMoeda", true);
		setDisabled("fatura.blGerarEdi", false);
		tpDocumentoServicoOnChange();
		resetDadosCliente();
		
		setElementValue("fatura.blGerarEdi", true);
		setElementValue("dtEmissao", setFormat(getElement("dtEmissao"), dtAtual));
	}
	
	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);
		
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.findDataAtual", "setData")); 
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.findPaisSessao", "setPais")); 
		xmit(false);	
		
		var url = new URL(parent.location.href);
		
		if (url.parameters != undefined && url.parameters.idProcessoWorkflow != undefined && url.parameters.idProcessoWorkflow != ''){   
			
			// Carrega o Boleto de acordo com idHistoricoBoleto
			var dados = new Array();
	        setNestedBeanPropertyValue(dados, "idBoleto", url.parameters.idProcessoWorkflow);
	        var sdo = createServiceDataObject("lms.contasreceber.manterBoletosAction.findBoletoByIdHistoricoBoleto",
	                                             "myOnDataLoad",
	                                             dados);
	         xmit({serviceDataObjects:[sdo]});
		
			//onDataLoad(url.parameters.idProcessoWorkflow);		
		}
	
	}
	
	function setData_cb(d,e,c){
		if (e == undefined){
			dtAtual = d._value;			
		}				
	}
	
	function setPais_cb(d,e,c){
		if (e == undefined){
			setElementValue("idPaisCotacao", d.idPaisCotacao);
			setElementValue("nmPaisCotacao", d.nmPaisCotacao);
			document.getElementById("idPaisCotacao").masterLink = "true";
			document.getElementById("nmPaisCotacao").masterLink = "true";
		}				
	}				
	
	function myStore_cb(data, errorMsg, errorKey, showErrorAlert, eventObj){
		store_cb(data, errorMsg, errorKey, showErrorAlert, eventObj);
		
		if (errorMsg == undefined){
			if(document.getElementById("newButton")) {
				getTabGroup(this.document).selectTab("cad");
				document.getElementById("newButton").focus();
			}
		}
		
		if(errorKey=="LMS-36099"){
			document.getElementById("dtEmissao").focus();
			document.getElementById("dtEmissao").select();
		}
	}
	
	function getCedentePadrao(){	
		_serviceDataObjects = new Array();	
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterBoletosAction.findCedente", "getCedentePadrao", {cliente:{idCliente:getElementValue("cliente.idCliente")}}));		
		xmit(false);		
	}
	
	function getCedentePadrao_cb(d,e){
		if (e == undefined) {
			setElementValue("cedente.idCedente",d._value);		
		} else {
			alert(e);
		}
	}
	
	document.getElementById("nrSequenciaFilial").style.textAlign = "right";		
</script>