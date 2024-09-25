<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.informacaoDoctoClienteService">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01001"/>
		<adsm:include key="LMS-01002"/>
		<adsm:include key="LMS-01003"/>
		<adsm:include key="LMS-01004"/>
		<adsm:include key="LMS-04109"/>
		<adsm:include key="LMS-01192"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/vendas/manterInformacoesDocumentoCliente"
		onDataLoadCallBack="myForm"
		idProperty="idInformacaoDoctoCliente">

		<adsm:hidden property="tpCliente" serializable="false"/>
		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterInformacoesDocumentoClienteAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			size="18"
			maxLength="20"
			labelWidth="15%"
			width="18%"
			exactMatch="false"
			minLengthForAutoPopUpSearch="1"
			afterPopupSetValue="afterPopupCliente"
			onDataLoadCallBack="cliente"
			onchange="return changeCliente();"
			required="true">
			<adsm:propertyMapping
				relatedProperty="cliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping
				criteriaProperty="statusAtivo"
				modelProperty="tpSituacao"/>
			<adsm:textbox
				dataType="text"
				property="cliente.pessoa.nmPessoa"
				size="40"
				disabled="true"
				width="50%"
				required="true"/>
		</adsm:lookup>

		<adsm:hidden property="statusAtivo" serializable="false" value="A"/>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA"/>
		<adsm:textbox maxLength="50" dataType="text" property="dsCampo" label="campo" size="43" required="true"/>
		<adsm:combobox property="tpCampo" label="tipo" required="true" domain="DM_TIPO_CAMPO" onchange="onSelectType();"/>
		<adsm:textbox
			maxLength="50"
			dataType="text"
			property="dsFormatacao"
			disabled="true"
			onchange="return validateFormat();"
			label="formatacao"
			size="43"/>
		<adsm:textbox
			label="tamanho"
			property="nrTamanho"
			maxLength="2"
			disabled="true"
			minValue="1"
			dataType="integer"
			size="5"/>

		<adsm:textbox
			label="valorPadrao"
			property="dsValorPadrao"
			maxLength="50"
			dataType="text"
			size="43"/>
		<adsm:checkbox property="blValorFixo" label="valorFixo"/>

		<adsm:checkbox property="blOpcional" label="opcional" />
		<adsm:checkbox property="blImprimeConhecimento" label="imprimeCTO" />

		<adsm:checkbox property="blIndicadorNotaFiscal" label="indicadorNF" />
		<adsm:combobox
			property="tpSituacao"
			label="situacao"
			domain="DM_STATUS"
			required="true"/>

		<adsm:checkbox property="blRemetente" label="solRemetente"/>
		<adsm:checkbox property="blDestinatario" label="solDestinatario" />
		<adsm:checkbox property="blDevedor" label="solDevedor" />

		<adsm:buttonBar>
			<adsm:button  id="salvar" caption="salvar" onclick="salvarInformacoes();" buttonType="storeButton" disabled="false"/>
			<adsm:newButton id="btnLimpar"/>
			<adsm:removeButton id="excluir"/>
		</adsm:buttonBar>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="infDocClienteMaskInstrucoes" width="100%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="infDocClienteMaskNumerico" width="30%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="infDocClienteMaskAlfanumerico" width="30%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="infDocClienteMaskDataHora" width="40%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
	</adsm:form>
</adsm:window>

<script language="javascript">

	function salvarInformacoes(){

		var form = document.forms[0];
		if (!validateForm(form)) {
			return false;
		}
		if(!getElementValue("blRemetente") && !getElementValue("blDestinatario") && !getElementValue("blDevedor")){
			alertI18nMessage("LMS-01192");
			return false;
		}
	    var sdo = createServiceDataObject("lms.vendas.informacaoDoctoClienteService.store", "store", buildFormBeanFromForm(form));
	    xmit({serviceDataObjects:[sdo]});		
	}

	function validateTab() {
		return validateFormInformacaoDoctoCliente();
	}

	function validateFormInformacaoDoctoCliente() {
		var result = false;
		var cliente = getElementValue("cliente.idCliente");
		var campo = getElementValue("dsCampo");
		var tipo = getElementValue("tpCampo"); 
		var selectedType = getElementValue(document.forms[0].tpCampo);

		if (!validateValorDefault())
			return false;
		if (!validateForm(document.getElementById('form_idInformacaoDoctoCliente')))
			return false;
		if (!validateFormat())
			return false;

		if(selectedType == 'A'){
			result = validateAlfanumericType();
		} else if (selectedType == 'N') {
			result = validateNumericType();
		} else if (selectedType == 'D') {
			result = validateDatetimeType();
		}

		if (result == true){
			return result;
		}
		return false;
	}

	function validateAlfanumericType() {
		var fields = new Array(2);
		fields[0] = document.forms[0].dsFormatacao;
		fields[1] = document.forms[0].nrTamanho;
		if(!xorFilled(fields)){
			alertI18nMessage("LMS-01001");
			setFocus(document.getElementById("dsFormatacao"));
			return false;
		}
		return true;
	}

	function validateNumericType() {
		var fields = new Array(2);
		fields[0] = document.forms[0].dsFormatacao;
		fields[1] = document.forms[0].nrTamanho;
		if(!xorFilled(fields)){
			alertI18nMessage("LMS-01002");
			setFocus(document.getElementById("dsFormatacao"));
			return false;
		}
		return true;
	}

	function validateDatetimeType() {
		var formatacao = getElementValue(document.forms[0].dsFormatacao);
		if(formatacao == "") {
			alertI18nMessage("LMS-01003");
			setFocus("dsFormatacao");
			return false;
		}
		return true;
	}

	function validateValorDefault() {
		if(getElement("blValorFixo").checked) {
			var dsValorPadrao = getElement("dsValorPadrao");
			if(dsValorPadrao.value == "") {
				alertI18nMessage("LMS-04109", dsValorPadrao.label, false);
				setFocus("dsValorPadrao");
				return false;
			}
		}
		return true;
	}

	function xorFilled(fields) {
		var flag = false;
		for(var i=0;i<fields.length;i++) {
			if(getElementValue(fields[i]) != "") {
				if(flag) {
					return false;
				} else {
					flag = true;
				}
			}
		}
		return flag;
	}

	// Função que valida as entradas do campo formato de acordo com o tipo
	// especificado. Percorre a entrada de dados verificando se existe algum
	// caracter inválido
	function validateFormat(){
		var formatacao = getElementValue("dsFormatacao");
		var tipo = getElementValue("tpCampo");
		var validacao = false;
		var caracteres = "";

		if(tipo == "A") {
			caracteres = "A9?-/."
		} else if(tipo == "N") {
			caracteres = "#0,."
		} else if(tipo == "D") {
			caracteres = "dMy/"
		} else if(tipo == "Z") {
			caracteres = "dMy/ Hm:"
		}
		for(j=0; j < formatacao.length; j++) {
			validacao = false;
			for(i=0; i < caracteres.length;i++) {
				if (caracteres.charAt(i)==formatacao.charAt(j)) {
					validacao = true;
				}
			}
			if(validacao == false){
				setFocus(document.getElementById("dsFormatacao"));
				document.forms[0].dsFormatacao.select();
				alertI18nMessage("LMS-01004");
				return false;
			}
		}
		return true;
	}

	function myForm_cb(dados, erros){
		onDataLoad_cb(dados, erros);
		var tipo = getElementValue("tpCampo");
		var tamanho = document.getElementById("nrTamanho");
		if(tipo == 'D') {
			setDisabled(tamanho,true);
		}
		setElementValue("cliente.pessoa.nrIdentificacao",dados.cliente.pessoa.nrIdentificacaoFormatado);
		var tpCliente = dados.cliente.tpCliente.value;
		setElementValue("tpCliente", tpCliente);
	}

	// Função que desabilita o campo tamanho quando o tipo selecionado é Data/Hora
	function onSelectType() {
		var selectedType = getElementValue(document.forms[0].tpCampo);
		var tamanho = document.forms[0].nrTamanho;
		var formatacao = document.getElementById("dsFormatacao");
		if (selectedType == '') {
			setElementValue(tamanho,"");
			tamanho.disabled = true;
			tamanho.datatype = "integer"
			setElementValue(formatacao,"");
			setDisabled(formatacao,true);
			return;
		}
		if (selectedType == 'D') {
			formatacao.disabled = false;
			setElementValue(tamanho,"");
			tamanho.disabled = true;
			tamanho.datatype = "integer"
		} else {
			tamanho.disabled = false;
			formatacao.disabled = false;
		}
		validateFormat()
	}

	function initWindow(eventObj) {
		var event = eventObj.name;
		if( (event == "tab_click") || (event == "newButton_click") ) {
			onSelectType();
		} else if(event == "gridRow_click") {
			var formatacao = document.getElementById("dsFormatacao");
			var tamanho = document.getElementById("nrTamanho");
			setDisabled(formatacao, false);
			setDisabled(tamanho, false);
		}
	}

	function afterPopupCliente(data) {
		setElementValue("cliente.pessoa.nrIdentificacao",data.pessoa.nrIdentificacaoFormatado);
	}

	function changeCliente() {
		if (getElementValue("cliente.pessoa.nrIdentificacao") == "") {
			if (getElementValue("idInformacaoDoctoCliente") != "") {
				if (getElementValue("tpCliente") != "F") {
					disableButtons(false);
				}
			} else {
				setDisabled("salvar", false);
			}
		}
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}

	function cliente_cb(data) {
		//alterado conforme quest  CQPRO00029475
	/*	
		if (data != undefined && data.length > 0) {
			if (data[0].tpCliente.value == "F") {
				disableButtons(true);
			} else {
				disableButtons(false);
			}
		}
	*/
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
	}

	function disableButtons(disable) {
		setDisabled("salvar", disable);
		setDisabled("excluir", disable);
	}

</script>
