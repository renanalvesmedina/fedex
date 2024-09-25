<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	function myOnPageLoad(){

		onPageLoad();
		
		/**Seta os campos abaixo como mastelink para que não sejam limpos pelos eventos*/ 
		getElement("redeco.filial.sgFilial").masterLink = "true";
		getElement("redeco.nrRedeco").masterLink = "true";
	}

	/**Sobrescreve a função initWindow , Seta com os valores do Redeco 
	ao pressionar a tab detalhamento */
	function initWindow(eventObj) {

		
		/**Obtem o nome do evento*/
		var event = eventObj.name;

		/**verifica se o evento é tab_click*/
		if(event == "tab_click" || event == "storeButton") {

			/**Obtem informações do Redeco na aba listagem*/
			obtemInfoRedeco();
			/**Habilita ou desabilita botões da tela*/
			controlarBotoesComposicao();	
			/**Habilita ou desabilita a lookup banco*/
			controlarBanco();			
			/**Habilita ou desabilita os campos necessários através do evento*/						
			controlaCampos(event == "storeButton");
			
		}
	
		if(event == "cleanButton_click"){
			/**Habilita os campos tipod e pagamento e valor*/
			controlaCampos(false);
			/**Obtem informações do Redeco na aba Listagem*/
			obtemInfoRedeco();
		}
		
		initCampoFilial();
		if((event != "gridRow_click") && 
				(event != "storeButton") ){
			 resetCampoTipo();
		}
		atualizaCampoCreditoBancarioHabilitado();
		
		if(!getElementValue("idComposicaoPagamentoRedeco")){
			getElement("numeroDeParcelas").required = false;
		}
		
	}

	function resetCampoTipo(){
		initCampoTipoComArgs(null);
	}
	
	function initCampoTipo(){
		
		initCampoTipoComArgs(getElementValue("idComposicaoPagamentoRedeco"));
	}

	function initCampoTipoComArgs(idComposicaoPagamentoRedeco){
		var dados = new Array();        
		_serviceDataObjects = new Array();
		
		setNestedBeanPropertyValue(dados, "idComposicaoPagamentoRedeco", idComposicaoPagamentoRedeco);
        
        addServiceDataObject(createServiceDataObject("lms.contasreceber.manterComposicaoPagamentoRedecoAction.findTpComposicaoPagamentoRedeco",
			                                         "initCampoTipo",
			                                         dados));
		xmit(false);	      
	}
	
	function initCampoTipo_cb(data,erro){
		if(erro!=null){
			alert(erro);
		}
		
		var tipoRegistro = getElementValue("tpComposicaoPagamentoRedeco");
		comboboxLoadOptions({e:document.getElementById("tpComposicaoPagamentoRedeco"), data:data});
		setElementValue('tpComposicaoPagamentoRedeco', tipoRegistro);
		
		if(tipoRegistro === undefined || tipoRegistro == "") {
			var formz = window.document.forms[0];
			var tipo = formz.tpComposicaoPagamentoRedeco;
			for (var i = 0; i < tipo.length; i++) {
				if(tipo[i].value == 'B') {
					tipo[i].selected = "1";
					setDisabled("dtPagamento", true);
					break;
				}
			}
		}
		atualizaCampoCreditoBancarioHabilitado();
		
	}
	
	function initCampoFilial(){
		if (!getElementValue("idComposicaoPagamentoRedeco") && getElement('filialByIdFilial.sgFilial').masterLink != 'true'){
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterFaturasAction.findFilialSessao", "initCampoFilial", null)); 
			xmit(false);
		}
	}
	
	function initCampoFilial_cb(d,e,o,x){
		if (e == undefined) {	
			
			fillFormWithFormBeanData(0, d);		

			setElementValue("filial.idFilial",getElementValue("filialByIdFilial.idFilial"));	
			
			if(d.filialByIdFilial.sgFilial != 'MTZ'){
				setDisabled('filialByIdFilial.idFilial',true);
			}else{
				setDisabled('filialByIdFilial.idFilial',false);
			}
		}
	}
	
	function atualizaVisibilidadeCampoNrParcelas(){
		if(getElementValue('tpComposicaoPagamentoRedeco') != 'R'){
			setVisibility('numeroDeParcelas', false);
		}else{
			setVisibility('numeroDeParcelas', true);
		}
		
	}

	/**Obtem informações da tela de listagem*/
	function obtemInfoRedeco(){
		
		/**Através da função tabGroup obtem a tab com o id pesq (Listagem)*/
		var tabGroup = getTabGroup(this.document);
		/**Obtem a tab listagem*/
		var tabPes = tabGroup.getTab("pesq");
		/**Seta as informações do Redeco nos campos */
		var idRedeco = getElementValue(tabPes.getElementById("redeco.idRedeco"));
		setElementValue("redeco.idRedeco",idRedeco);			
		var nrRedeco = getElementValue(tabPes.getElementById("redeco.nrRedeco"));
		setElementValue("redeco.nrRedeco",leftPad(nrRedeco,10,"0"));
		var flRedeco = getElementValue(tabPes.getElementById("redeco.filial.sgFilial"));
		setElementValue("redeco.filial.sgFilial",flRedeco);			
		var stRedeco = getElementValue(tabPes.getElementById("redeco.tpSituacaoRedeco"));
		setElementValue("redeco.tpSituacaoRedeco",stRedeco);
		var dgRedeco = getElementValue(tabPes.getElementById("redeco.blDigitacaoConcluida"));
		setElementValue("redeco.blDigitacaoConcluida",dgRedeco);
				
	}

	/**Se a digitação não foi concluida N e a situação do redeco for
	Digitado DI ou Emitido EM habilita os botões da tela*/	
	function controlarBotoesComposicao(){

		var tabGroup = getTabGroup(this.document);
		var tabPes   = tabGroup.getTab("pesq");

		var filialLogada = getElementValue(tabPes.getElementById("filialLogada"));

		var stRedeco = getElementValue("redeco.tpSituacaoRedeco");
		var dgRedeco = getElementValue("redeco.blDigitacaoConcluida");
		var flRedeco = getElementValue("redeco.filial.sgFilial");
		
		if( (dgRedeco == "N") && (stRedeco == "DI" || stRedeco == "EM") && (flRedeco == filialLogada  || filialLogada == "MTZ") ){
			desabilitarBotoes(false);		
		}else{
			desabilitarBotoes(true);							
		}			
						
	}
	
	/**Concluir a digitação da composição redeco ao pressionar
	o botão Concluir Redeco */
	function concluirRedeco(){
		/**Através da função tabGroup obtem a tab com o id pesq (Listagem)*/
		var tabGroup = getTabGroup(this.document);
		/**Obtem a tab listagem*/
		var tabPes = tabGroup.getTab("pesq");
		/**Seta as informações do Redeco nos campos */
		var idRedeco = getElementValue(tabPes.getElementById("redeco.idRedeco"));
		
		var data = new Array();	
		setNestedBeanPropertyValue(data, "idRedeco", idRedeco);		
	
		var sdo = createServiceDataObject("lms.contasreceber.manterRedecoAction.validateBeforeConcluirDigitacao", "validateBeforeConcluirDigitacao", data);
		xmit({serviceDataObjects:[sdo]});
	}

	/**Esta função também existe em ManterRedecoCad.jsp pelo mesmo motivo. Provavelmente o ajuste deva ser em ambos. */ 
	function validateBeforeConcluirDigitacao_cb(data, error) {
		if (data.travaJuros == "true") {
			alert(data.errorMessage);
			
		} else if(data.encContasFilDif === "true" ) {
			alert(data.encContasFilDifMessage);
			
		} else if(data.redecoSemCompPagto == "true") {
			alert(data.errorMessage2);
			
		} else if (data.vlLiquidoLtVlTotalReceb == "true") {
			var LMS_36343 = 'Total da composição de pagamentos é inferior à soma dos saldos dos documentos do redeco. Deseja realizar uma baixa parcial dos documentos?';
			if (confirm(LMS_36343)) {
				if (data.nrItensRedecoMaiorQueUm == "true") {
					alert(data.nrItensRedecoMaiorQueUmMessage);
					return;
				}
				
				concluirDigitacao2();
			} else {
				alert(data.errorMessage); 
			} 
		} else if (data.vlLiquidoEqualsVlTotalReceb == "false") {
			alert(data.errorMessage);
		} else {
			concluirDigitacao2();
		}
	}

	function concluirDigitacao2() {
		/**Através da função tabGroup obtem a tab com o id pesq (Listagem)*/
		var tabGroup = getTabGroup(this.document);
		/**Obtem a tab listagem*/
		var tabPes = tabGroup.getTab("pesq");
		/**Seta as informações do Redeco nos campos */
		var idRedeco = getElementValue(tabPes.getElementById("redeco.idRedeco"));
	
		var data = new Array();	
		setNestedBeanPropertyValue(data, "idRedeco", idRedeco);		
		
	 	var sdo = createServiceDataObject("lms.contasreceber.manterRedecoAction.concluirDigitacao", "concluirRedeco", data);
	 	xmit({serviceDataObjects:[sdo]});
 	}

	/**Callback após concluir redeco */
	function concluirRedeco_cb(data,erros){
		if (erros != undefined) {
			alert(erros);
			return false;
		}	
		/**Mostra a mensagem de sucesso padrão*/
		showSuccessMessage();	
	}

	/**Habilita ou desabilita os botões da tela*/
	function desabilitarBotoes(desabilitar){
		setDisabled("btnSalvar", desabilitar);
		setDisabled("btnLimpar", desabilitar);

		/**Habilita somente quando o idComposicaoPagamentoRedeco existir*/
		if(getElementValue("idComposicaoPagamentoRedeco") != ""){
			setDisabled("btnExcluir", desabilitar);
		}

		setDisabled("btnConcluirRedeco", desabilitar);
	}

	/**Salva a composição de pagamento redeco*/
	function salvarCompPagamento(){
		
		/**Valida o campo observações*/
		var tpPagamento = getElementValue("tpComposicaoPagamentoRedeco");
		if(tpPagamento == "F" || tpPagamento == "E" || tpPagamento == "J"){
			document.getElementById("obComposicaoPagamentoRedeco").required = true;
		}else{
			document.getElementById("obComposicaoPagamentoRedeco").required = false;
		}
		
		var vlPagamento = getElementValue("vlPagamento");
		if (vlPagamento != undefined && vlPagamento < 0) {
			alert("Valor fora dos limites!"); //double check. a mensagem já é lançada no campo: minValue="0.01".
		}
		
		storeButtonScript('lms.contasreceber.manterComposicaoPagamentoRedecoAction.store', 'store', document.forms[0]);
	}

	/**Caso o tipo de pagamento for depósito habilita ou desabilita o campo BAnco*/
	function controlarBanco(){
		var tpPagamento = getElementValue("tpComposicaoPagamentoRedeco");
		setDisabled("dtPagamento", false);
		if(tpPagamento == "D"){
			setDisabled("banco.idBanco",false);
			setFocus("banco.nrBanco");
			document.getElementById("banco.nrBanco").required = true;
			
		} else if (tpPagamento == "B"){
			setDisabled("dtPagamento", true);
			setFocus("creditoBancario.idCreditoBancario");
			
		}else{
			setDisabled("banco.idBanco",true);
			setFocus("dtPagamento");
			document.getElementById("banco.nrBanco").required = false;
		}
		atualizaVisibilidadeCampoNrParcelas();

		atualizaCampoCreditoBancarioHabilitado();
		
		getElement("numeroDeParcelas").required = tpPagamento == 'R';
		
	}

	/**Sobrescreve a funcao para carregar os dados da tela*/
	function myOnDataLoad_cb(data, error){
		if(error){
			alert(error);
		}
		/**Carrega os dados*/
		onDataLoad_cb(data,error);
		/**Habilita ou desabilita a lookup banco*/
		controlarBanco();
		/**Habilita ou desabilita botões da tela*/
		controlarBotoesComposicao();		
		/**Desabilita os campos necessários*/
		controlaCampos(true);
		

		initCampoTipo();
		initFilialEdit(data);
		
		atualizaVisibilidadeCampoNrParcelas();
		if(data.creditoBancario  != null){
			setElementValue("creditoBancario.nmBanco",data.creditoBancario.banco.nmBanco);
			setElementValue("creditoBancario.saldo", setFormat(document.getElementById("creditoBancarioSaldo"), data.creditoBancario.saldo));
		}
		atualizaCampoCreditoBancarioHabilitado();
		
		getElement("numeroDeParcelas").required = (getElementValue("tpComposicaoPagamentoRedeco") == 'R');
	}
	function initFilialEdit(data){
		setElementValue("filial.idFilial",data.filial.idFilial);
		setElementValue("filialByIdFilial.idFilial",data.filial.idFilial);
		setElementValue("filialByIdFilial.pessoa.nmFantasia",data.filial.pessoa.nmFantasia);
		setElementValue("filialByIdFilial.sgFilial",data.filial.sgFilial);
		
	}

	/**Desabilita os campos tipo de composicao e valor do pagamento*/
	function controlaCampos(desabilita){			
		setDisabled("tpComposicaoPagamentoRedeco",desabilita);	
		setDisabled("vlPagamento",desabilita);
		
	}
	
	/**Preenche caracteres a esquerda em um determinado numero*/
	function leftPad(value, size, ch){
		
		var sz = size-value.length;
		if(sz < 0){
			return value;
		}		
		var result = "";
		for(var i=0; i<sz; i++){
			result = ch+result;	
		}
		return result + value;
	}	
	

	function atualizaCampoCreditoBancarioHabilitado(){
		if(getElementValue('tpComposicaoPagamentoRedeco') != 'B'){
			setDisabled("creditoBancario.idCreditoBancario",true);
		}else{
			setDisabled("creditoBancario.idCreditoBancario",false);
		}
	}

</script>
<adsm:window service="lms.contasreceber.manterComposicaoPagamentoRedecoAction" onPageLoad="myOnPageLoad">

	<adsm:form action="/contasReceber/manterCompPagamentoRedeco"  idProperty="idComposicaoPagamentoRedeco" onDataLoadCallBack="myOnDataLoad">

		<!-- Redeco -->
		<adsm:hidden property="redeco.idRedeco"/>
		<adsm:hidden property="redeco.tpSituacaoRedeco"/>		
		<adsm:hidden property="redeco.blDigitacaoConcluida"/>	
		
		<adsm:textbox  property="redeco.filial.sgFilial"  
			dataType="text" width="20%"  size="5"
			label="numeroRedeco" labelWidth="20%" 
			disabled="true"/>

		<adsm:textbox  
			property="redeco.nrRedeco" 
			dataType="integer" size="10" 
			width="60%" mask="0000000000" 
			disabled="true"/>
		
		
		<!-- lookup de filial -->
		<adsm:hidden property="filial.idFilial"/>
		<adsm:lookup property="filialByIdFilial" idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.contasreceber.manterFaturasAction.findLookupFilial"
			dataType="text" label="filial" size="3" labelWidth="20%"
			action="/municipios/manterFiliais" width="9%" required="true"
			minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px"
			maxLength="3">
			<adsm:propertyMapping
				relatedProperty="filial.idFilial"
				modelProperty="idFilial" />
			<adsm:propertyMapping
				relatedProperty="filialByIdFilial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text"
				property="filialByIdFilial.pessoa.nmFantasia" width="26%" size="30"
				serializable="false" disabled="true" />
		</adsm:lookup>
		
		<!-- Tipo -->
		
		<adsm:hidden property="tpComposicaoPagamentoRedecoTmp" serializable="false"/>
		<adsm:combobox
			property="tpComposicaoPagamentoRedeco"
			label="tipo" width="80%"
			labelWidth="20%"  required="true"
			optionProperty="value" optionLabelProperty="description"
			onchange="controlarBanco();"
			defaultValue="B" 
			>
		</adsm:combobox>
			
		<!-- lookup credito bancario -->
		<adsm:lookup 
			property="creditoBancario" 
	
			idProperty="idCreditoBancario"
	 		criteriaProperty="filial.sgFilial"
	
	 		dataType="text" 
	 		label="creditoBancario" size="3"
	 		labelWidth="20%"
	 		action="/contasreceber/creditoBancarioLookup" 
	 		width="10%" 
	 		minLengthForAutoPopUpSearch="3" 
	 		exactMatch="false" 
	 		style="width:45px"
	 		maxLength="3" >
	
	 		<adsm:propertyMapping relatedProperty="creditoBancario.nmBanco" modelProperty="banco.nmBanco" />
	 		<adsm:propertyMapping relatedProperty="creditoBancario.dhAlteracao" modelProperty="dhAlteracao" />
	 		<adsm:propertyMapping relatedProperty="creditoBancario.saldo" modelProperty="saldo" />
	 		<adsm:propertyMapping relatedProperty="dtPagamento" modelProperty="dtCredito" />
	
	 		<adsm:textbox dataType="text" property="creditoBancario.nmBanco" width="20%" size="30" serializable="false" disabled="true" />
	 		<adsm:textbox dataType="JTDateTimeZone" property="creditoBancario.dhAlteracao" width="20%" size="30" serializable="false" disabled="true" />
	 		<adsm:textbox dataType="currency" property="creditoBancario.saldo" width="20%" size="20" serializable="false" disabled="true" id="creditoBancarioSaldo"/>
		</adsm:lookup>

		<!-- Banco -->
		<adsm:lookup 
			dataType="integer" 
			property="banco" 
			idProperty="idBanco" 
			service="lms.configuracoes.bancoService.findLookup" 
			label="banco" 
			size="5" disabled="true"
			maxLength="3"
			labelWidth="20%" 
			width="9%"
			criteriaProperty="nrBanco" 
			serializable="true"
			action="/configuracoes/manterBancos">			

			<adsm:propertyMapping 
				modelProperty="nmBanco" 
				relatedProperty="banco.nmBanco"/> 
				
			<adsm:textbox 
				property="banco.nmBanco" 
				dataType="text" 
				size="45" 
				width="60%"
				maxLength="30" 
				disabled="true" 
				serializable="true"/>
				
		</adsm:lookup>

		<adsm:textbox dataType="JTDate" width="80%"
			property="dtPagamento" labelWidth="20%" required="true"
			label="dataPagamento"/>

		<adsm:textbox property="vlPagamento" width="80%" size="10"
			label="valorPagamento" labelWidth="20%" required="true"
			dataType="currency" minValue="0.01"/>

		<adsm:textbox label="numeroDeParcelas" property="numeroDeParcelas"
					 dataType="integer" size="20" labelWidth="20%" width="82%" />

		<adsm:textbox  property="obComposicaoPagamentoRedeco"  
			dataType="text" width="60%" 
			size="100" 
			maxLength="40"
			labelWidth="20%" label="observacao"/>
		
		<adsm:buttonBar >
			<adsm:button       id="btnSalvar" buttonType="storeButton" caption="salvar" onclick="salvarCompPagamento();"/>
			<adsm:resetButton  id="btnLimpar" />
			<adsm:removeButton id="btnExcluir"/>
			<adsm:button 	   id="btnConcluirRedeco" caption="concluirRedeco" onclick="concluirRedeco();" buttonType="storeButton" disabled="false"/>		
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>