<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="onPageLoadCallBack">
	<adsm:form action="/vendas/emitirTabelasCliente" id="reportForm">

		<adsm:i18nLabels>
			<adsm:include key="LMS-30050"/>
			<adsm:include key="LMS-30051"/>
			<adsm:include key="LMS-30052"/>
			<adsm:include key="LMS-01040"/>
		</adsm:i18nLabels>

		<adsm:combobox label="regional" 
			property="regional.idRegional"
			required="false"
			optionLabelProperty="siglaDescricao" 
			optionProperty="idRegional" 
			service="lms.vendas.emitirTabelasClienteAction.montaComboRegional" 
			labelWidth="12%" 
			width="38%" 
			boxWidth="170"
		>
			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:hidden property="regional.siglaDescricao"/>

		<adsm:lookup label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			action="/municipios/manterFiliais"
			service="lms.vendas.emitirTabelasClienteAction.findLookupFilial" 
			dataType="text"
			exactMatch="true"
			minLengthForAutoPopUpSearch="3"
			labelWidth="20%"
			width="8%"
			size="3"
			maxLength="3"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" width="20%" property="filial.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:lookup>

		<adsm:lookup label="promotor"
			service="lms.vendas.manterPromotoresClienteAction.findLookupFuncionarioPromotor" 
			dataType="text"
			property="promotor"
			idProperty="usuario.idUsuario"
			exactMatch="true"
			criteriaProperty="nrMatricula"
			size="20"
			maxLength="16"
			labelWidth="12%"
			width="45%"
			cmd="promotor"
			action="/configuracoes/consultarFuncionarios"
		>
			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:propertyMapping relatedProperty="promotor.usuario.idUsuario" modelProperty="idUsuario"/>
			<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" size="30" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup
			property="cliente" 
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.emitirTabelasClienteAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao" dataType="text" 
			label="cliente" size="20" maxLength="20" width="75%" labelWidth="12%" 
			onDataLoadCallBack="clienteClienteCallBack" 
			onPopupSetValue="populaCombos"
			onchange="return clienteClienteChange();">	
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" maxLength="50" disabled="true" />
		</adsm:lookup>

		<adsm:combobox
			property="divisaoCliente.idDivisaoCliente"
			label="divisao"
			autoLoad="false"
			optionLabelProperty="dsDivisaoCliente"
			optionProperty="idDivisaoCliente"
			service=""
			onchangeAfterValueChanged="true"
			onchange="validateTabelasAereo(); "
			labelWidth="12%"
			width="38%"
			boxWidth="280"/>	
		
		<adsm:combobox
			property="data.idData"
			label="data"
			autoLoad="false"
			optionLabelProperty="dsData"
			optionProperty="idData"
			service=""
			labelWidth="12%"
			width="38%"
			boxWidth="280"
			>
				<adsm:propertyMapping relatedProperty="data.dsData" modelProperty="dsData"/>
		</adsm:combobox>	
			
		<adsm:hidden property="data.dsData"/>

		<adsm:combobox 
			service="" 
			property="contato.idContato" 
			autoLoad="false" 
			optionLabelProperty="nmContato" 
			optionProperty="idContato"
			label="contato" 
			labelWidth="12%"
			width="38%" 
			boxWidth="160"			/>

		<adsm:checkbox property="tabelaNova" label="tabelaNova" labelWidth="12%" width="38%"/>

		<adsm:checkbox property="cargaCompleta" label="cargaCompleta" labelWidth="20%" width="30%"/>

		<adsm:combobox 
			label="produtoEspecifico" 
			labelWidth="12%"
			onlyActiveValues="true"
			optionLabelProperty="nrTarifaEspecifica" 
			optionProperty="idProdutoEspecifico" 
			property="produtoEspecifico.idProdutoEspecifico" 
			service="lms.vendas.emitirTabelasClienteAction.findlookupProdutoEspecifico"
			width="38%" 
			boxWidth="160"
		/>

		<adsm:checkbox property="impTermosCondicoes" label="imprimeTermosCondicoes" labelWidth="20%" width="30%"/>

		<adsm:section  caption="tabelaDoAereo"/>
		<adsm:lookup property="aeroportoOrigem" idProperty="idAeroporto" criteriaProperty="sgAeroporto"
			service="lms.tabelaprecos.emitirTabelasMercurioAction.findLookupAeroporto"
			action="municipios/manterAeroportos" exactMatch="true" dataType="text"
			label="aeroportoOrigem" 
			labelWidth="12%" width="38%" 
			size="3" maxLength="3"
			disabled="true"
			serializable="true">
			<adsm:textbox property="nmAeroportoOrigem" disabled="true" dataType="text"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="nmAeroportoOrigem"/>
		</adsm:lookup>

		<adsm:lookup property="aeroportoDestino" idProperty="idAeroporto" criteriaProperty="sgAeroporto"
			service="lms.tabelaprecos.emitirTabelasMercurioAction.findLookupAeroporto"
			action="municipios/manterAeroportos" exactMatch="true" dataType="text"
			label="aeroportoDestino" 
			labelWidth="20%" width="30%" 
			size="3" maxLength="3"
			disabled="true"
			serializable="true">
			<adsm:textbox property="nmAeroportoDestino" disabled="true" dataType="text"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="nmAeroportoDestino"/>
		</adsm:lookup>

		<adsm:checkbox property="blEmitirEspecifica" label="emitirEspecifica" 
			labelWidth="12%" width="38%" 
			disabled="true"/>
		<adsm:checkbox property="blEmitirSomenteCapitais" label="emitirSomenteCapitais"
			labelWidth="20%" width="30%"
			disabled="true"/>

		<adsm:buttonBar>
			<adsm:button id="btnEmitir" caption="visualizar" onclick="return emitirReport();" />

			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">


	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "cleanButton_click") {
			resetValue("divisaoCliente.idDivisaoCliente");
			getElement("divisaoCliente.idDivisaoCliente").options.length = 1;
			resetValue("contato.idContato");
			getElement("contato.idContato").options.length = 1;
			resetValue("data.idData");
			getElement("data.idData").options.length = 1;
		}
		setDisabled("btnEmitir", false);
	}

	function onPageLoadCallBack_cb(data, error) {
		onPageLoad_cb(data, error);
		setDisabled("btnEmitir", false);
	}

	//EMITIR RELATORIO
	function emitirReport(){
		var idUsuario = getElementValue("promotor.usuario.idUsuario");
		var idRegional = getElementValue("regional.idRegional");
		var idFilial = getElementValue("filial.idFilial");
		var idCliente = getElementValue("cliente.idCliente");

		if(idRegional == "" && idFilial == "" && idCliente == "" && idUsuario == ""){
			alert(i18NLabel.getLabel("LMS-30050"));
			return;
		}

		if(idCliente == null || idCliente == ""){//se não tiver cliente pergunta se quer agendar relatorio
			if (confirmI18nMessage("LMS-30051")) {
				var data = buildFormBeanFromForm(document.forms[0]);
				var sdo = createServiceDataObject("lms.vendas.emitirTabelasClienteAction.scheduleReport", "retornoReport", data);
				xmit({serviceDataObjects:[sdo]});
			}		
		} else {//se tiver cliente valida a impressao

			if (getElementValue("aeroportoOrigem.idAeroporto")!="" && getElementValue("aeroportoDestino.idAeroporto")!=""){
				var msg = i18NLabel.getLabel("LMS-01040");
				alert(msg.replace(/[{]0[}]/,getElement("aeroportoDestino.sgAeroporto").label));
				setFocus("aeroportoDestino.sgAeroporto");
				return;
			}

			emitirReportPDF();
		}
	}
	
	function validateTabelasAereo(){
		if (getElementValue("divisaoCliente.idDivisaoCliente") == ""){
			disableSectionAereo(true);
			return;
		}
		
		var data = new Object();
		data.idDivisaoCliente = getElementValue("divisaoCliente.idDivisaoCliente");
		var sdo = createServiceDataObject("lms.vendas.emitirTabelasClienteAction.validateTabelasAereo","validateTabelasAereo",data);
		var sdo2 = createServiceDataObject("lms.vendas.emitirTabelasClienteAction.findDataRange", "range_ok", data);
		xmit({serviceDataObjects:[sdo, sdo2]});
	}

	function validateTabelasAereo_cb(data,error){		
		if (error != undefined){
			alert(error);
			return;
		}
		if (data.hasTabelaAereo != undefined){
			disableSectionAereo(data.hasTabelaAereo != "true");	
		}
	}
	
	function disableSectionAereo(arg){
		setDisabled("aeroportoOrigem.idAeroporto",arg);
		setDisabled("aeroportoDestino.idAeroporto",arg);
		setDisabled("blEmitirEspecifica",arg);
		setDisabled("blEmitirSomenteCapitais",arg);

		if (arg == true){
			setElementValue("blEmitirEspecifica",false);
			setElementValue("blEmitirSomenteCapitais",false);

			setElementValue("aeroportoOrigem.idAeroporto","");
			setElementValue("aeroportoOrigem.sgAeroporto","");
			setElementValue("nmAeroportoOrigem","");
			setElementValue("aeroportoDestino.idAeroporto","");
			setElementValue("aeroportoDestino.sgAeroporto","");
			setElementValue("nmAeroportoDestino","");
		}else{
			setElementValue("blEmitirEspecifica",true);
		}
	}
	
	function retornoReport_cb(data, error){
		if(error != undefined) {
			alert(error);
			return;
		}
		alertI18nMessage("LMS-30052");
	}

	//EMITIR PDF
	function emitirReportPDF() {
		reportButtonScript('lms.vendas.emitirTabelasClienteAction', 'openPdf', document.Lazy);
	}

	//POPULA COMBOS
	function populaCombos(data) {
		var sdo = createServiceDataObject("lms.vendas.emitirTabelasClienteAction.findContato", "contato_ok", data);
		var sdo2 = createServiceDataObject("lms.vendas.emitirTabelasClienteAction.findDivisao", "divisao_ok", data);		
		
		xmit({serviceDataObjects:[sdo, sdo2]});
	}
	
	function contato_ok_cb(data, error) {
		if (error != undefined){
			alert(error);
		} else {
			contato_idContato_cb(data)
		}
	}

	function range_ok_cb(data, error) {
		if (error != undefined){
			alert(error);
		} else {			
			data_idData_cb(data);
		}
	}

	function clienteClienteChange() {
		var r = cliente_pessoa_nrIdentificacaoOnChangeHandler();
		if (getElementValue("cliente.pessoa.nrIdentificacao") == "") { 
			document.getElementById("divisaoCliente.idDivisaoCliente").options.length = 1;
			document.getElementById("contato.idContato").options.length = 1;
		}
		return r;
	}

	function clienteClienteCallBack_cb(data, error) {
		if (error != undefined){
			alert(error);
		} else {
			if (cliente_pessoa_nrIdentificacao_exactMatch_cb(data)) {
				populaCombos(data[0]);
			}
		}
	}

	function divisao_ok_cb(data, error) {
		if (error != undefined) {
			alert(error);
		} else {
			divisaoCliente_idDivisaoCliente_cb(data); 
		}
	}

</script>