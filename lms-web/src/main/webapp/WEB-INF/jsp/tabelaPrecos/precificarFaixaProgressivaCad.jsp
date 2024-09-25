<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form
		action="/tabelaPrecos/precificarParcelas"
		idProperty="idValorFaixaProgressiva"
		service="lms.tabelaprecos.precificarParcelasAction.findByIdValorFaixaProgressiva"
		onDataLoadCallBack="form">

		<adsm:i18nLabels>
			<adsm:include key="LMS-30014"/>
			<adsm:include key="LMS-30018"/>
			<adsm:include key="LMS-30020"/>
			<adsm:include key="LMS-30021"/>
			<adsm:include key="requiredField"/>
		</adsm:i18nLabels>

		<adsm:hidden property="idFaixaProgressiva"/>
		<adsm:hidden property="faixaProgressiva.versao"/>
		<adsm:hidden property="versao"/>
		<adsm:hidden
			property="tarifaPreco.tpSituacao"
			serializable="false"
			value="A"/>

		<adsm:lookup
			action="/tabelaPrecos/manterTarifasPreco"
			criteriaProperty="cdTarifaPreco"
			dataType="text"
			exactMatch="true"
			idProperty="idTarifaPreco"
			labelWidth="18%"
			label="tarifa"
			maxLength="5"
			property="tarifaPreco"
			service="lms.tabelaprecos.tarifaPrecoService.findLookup"
			size="5"
			width="82%">

			<adsm:propertyMapping
				criteriaProperty="tarifaPreco.tpSituacao"
				modelProperty="tpSituacao"/>

		</adsm:lookup>

		<adsm:lookup
			action="/tabelaPrecos/manterRotas"
			criteriaProperty="origemString"
			dataType="text"
			idProperty="idRotaPreco"
			labelWidth="18%"
			label="rotaOrigem"
			disabled="true"
			property="rotaPreco"
			service="lms.tabelaprecos.rotaPrecoService.findLookup"
			size="80"
			width="82%"
			onclickPicker="onclickPickerLookupRotas()">
			<adsm:hidden property="tpRestricao" value="Z" serializable="false"/>
			<adsm:propertyMapping
				criteriaProperty="tpRestricao"
				modelProperty="tpRestricao"/>

			<adsm:propertyMapping
				relatedProperty="rotaPreco.destinoString"
				modelProperty="destinoString"/>
		</adsm:lookup>

		<adsm:textbox
			dataType="text"
			disabled="true"
			label="rotaDestino"
			labelWidth="18%"
			property="rotaPreco.destinoString"
			size="80"
			width="82%"/>

		<adsm:textbox
			dataType="decimal"
			label="fatorMultiplicacao"
			labelWidth="18%"
			mask="##,##0.0000"
			minValue="0.0001"
			property="nrFatorMultiplicacao"
			size="9"
			width="32%"/>

		<adsm:textbox
			dataType="decimal"
			label="valorFixo"
			labelWidth="18%"
			mask="###,###,###,###,##0.0000"
			minValue="0.0001"
			property="vlFixo"
			size="18"
			width="32%"/>

		<adsm:textbox
				dataType="decimal"
				label="valorTaxaFixa"
				labelWidth="18%"
				mask="###,###,###,###,##0.00"
				minValue="0.01"
				property="vlTaxaFixa"
				size="18"
				width="32%"/>

		<adsm:textbox
				dataType="decimal"
				label="valorKmExtra"
				labelWidth="18%"
				mask="###,###,###,###,##0.00"
				minValue="0.01"
				property="vlKmExtra"
				size="18"
				width="32%"/>

		<adsm:textbox
			dataType="decimal"
			label="percentualTaxas"
			labelWidth="18%"
			mask="##0.00"
			maxValue="100"
			minValue="0.01"
			property="pcTaxa"
			size="5"
			width="32%"/>

		<adsm:textbox
			dataType="decimal"
			label="percentualDesconto"
			labelWidth="18%"
			mask="##0.00"
			maxValue="100"
			minValue="0.01"
			property="pcDesconto"
			size="5"
			width="32%"/>

		<adsm:textbox
			dataType="decimal"
			label="valorAcrescimo"
			labelWidth="18%"
			mask="###,###,###,###,##0.00"
			minValue="0.01"
			property="vlAcrescimo"
			size="18"
			width="82%"/>

		<adsm:checkbox
			label="charter"
			labelWidth="18%"
			onclick="checkPromocional();"
			property="blPromocional"
			width="32%"/>

		<adsm:range
			label="vigencia"
			labelWidth="18%"
			width="32%">

			<adsm:textbox
				dataType="JTDate"
				property="dtVigenciaPromocaoInicial"
				smallerThan="dtVigenciaPromocaoFinal"/>

			<adsm:textbox
				biggerThan="dtVigenciaPromocaoInicial"
				dataType="JTDate"
				property="dtVigenciaPromocaoFinal"/>
		</adsm:range>

		<adsm:buttonBar>

			<adsm:storeButton
				caption="salvar"
				id="storeItemButton"
				service="lms.tabelaprecos.precificarParcelasAction.storeValorFaixaProgressiva"
				callbackProperty="storeItem"/>

			<adsm:button
				caption="novo"
				id="newButton"
				buttonType="newButton"
				onclick="newButtonScript(this.document, true, {name:'cleanButton_click'}); estadoNovo();"/>

			<adsm:removeButton
				id="removeButton"
				disabled="true"
				service="lms.tabelaprecos.precificarParcelasAction.removeByIdValorFaixaProgressiva"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script type="text/javascript">
	/**
	* Captura o evento de um clique na tab com o objetivo de ajustar a habilitação de
	* vigência promocional.
	*/
	function initWindow(eventObj) {
		var event = eventObj.name;
		if (event == "tab_click"
			|| event == "storeItemButton"
			|| event == "removeButton_grid"){
			estadoNovo();
		}
		setPropertyValues();
		getTabGroup(document).getTab("pesq").getElementById("executeSearch").value = "true";
	}

	function onclickPickerLookupRotas(){
		var origemString = getElementValue("rotaPreco.origemString");
		var destinoString = getElementValue("rotaPreco.destinoString");
		if(origemString != ""){
			setElementValue("rotaPreco.origemString","");
		}
		if(destinoString != ""){
			setElementValue("rotaPreco.destinoString","");
		}

		lookupClickPicker({e:document.forms[0].elements['rotaPreco.idRotaPreco']});
		if(getElementValue("rotaPreco.origemString")=="" && origemString != "")	{
			setElementValue("rotaPreco.origemString",origemString);
		}
		if(getElementValue("rotaPreco.destinoString")=="" && destinoString != ""){
			setElementValue("rotaPreco.destinoString",destinoString);
		}
	}

	/**
	* Restaura o estado novo da tela
	*/
	function estadoNovo(){
		setDisabled(document.forms[0],false);
		setDisabled("rotaPreco.origemString", true);
		setDisabled("rotaPreco.destinoString",true);
		//checkPromocional();

		var data = getPropertyValues();
		var blEfetivada = data.tabelaPrecoParcela.tabelaPreco.blEfetivada;
		var idPendencia = data.tabelaPrecoParcela.tabelaPreco.idPendencia;
		var tpTipoTabelaPreco = data.tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco;

		if(blEfetivada == "true" && tpTipoTabelaPreco != "C"){
			setDisabled('storeItemButton', true);
			setDisabled('removeButton', true);
			setDisabled("newButton", true);
			//enableVigencia(false);
		} else {
			setDisabled('storeItemButton', false);
			setDisabled('removeButton', getElementValue("idValorFaixaProgressiva") == "");
			setDisabled("newButton", false);
			//enableVigencia(true);
		}

		if (tpTipoTabelaPreco == "C") {
			enableVigencia(true);
		} else {
			enableVigencia(false);
		}

		if(idPendencia != "" || data.isVisualizacaoWK == "true"){
			setDisabled('storeItemButton', true);
		}

		setFocus("tarifaPreco.cdTarifaPreco", false);
	}

	function getPropertyValues() {
		var tabGroup = getTabGroup(this.document);
		var telaPrecif = tabGroup.parentTabGroup.getTab("precif").tabOwnerFrame;
		return telaPrecif.getParametrosTabelaPreco();
	}

	function setPropertyValues() {
		var tabGroup = getTabGroup(this.document);
		var cadDocument = tabGroup.parentTabGroup.getTab("cad").getDocument();
		setElementValue("idFaixaProgressiva", cadDocument.getElementById("idFaixaProgressiva").value);
		setElementValue("faixaProgressiva.versao", cadDocument.getElementById("versao").value);
	}

	/**
	* Habilita ou desabilita a vigência promocional, de acordo com o parâmetro.
	*/
	function enableVigencia(flag){
		if(flag==true){
			setDisabled("dtVigenciaPromocaoInicial",false);
			setDisabled("dtVigenciaPromocaoFinal",false);
			document.getElementById("dtVigenciaPromocaoInicial").required = "true";
			document.getElementById("dtVigenciaPromocaoFinal").required = "false";
		}else{
			setDisabled("dtVigenciaPromocaoInicial",true);
			resetValue("dtVigenciaPromocaoInicial");

			setDisabled("dtVigenciaPromocaoFinal",true);
			resetValue("dtVigenciaPromocaoFinal");
		}
	}

	/**
	* Onchange do checkbox "blPromocional".
	*/
	function checkPromocional(){
		//enableVigencia(getElement("blPromocional").checked==true);
	}

	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento.
	 */
	function form_cb(data,errorMsg){
		onDataLoad_cb(data,errorMsg);
		validateVigencia(data)
		setPropertyValues();
		var values = getPropertyValues();
		var tpTipoTabelaPreco = values.tabelaPrecoParcela.tabelaPreco.tpTipoTabelaPreco;
		if (tpTipoTabelaPreco == "C"){
			document.getElementById("dtVigenciaPromocaoInicial").required = "true";
			document.getElementById("dtVigenciaPromocaoFinal").required = "false";
	}
	}

	function storeItem_cb(data, error) {
		store_cb(data, error);
		if (data.msgAtualizacaoAutomatica != undefined){
			alert(data.msgAtualizacaoAutomatica)
		}
		validateVigencia(data);
		setElementValue("versao", stringToNumber(getElementValue("versao")));
		setFocus("newButton", false);
	}

	function validateVigencia(data) {
		//ajusta a tela de acordo com o retorno da validação de vigência.
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0){
			estadoNovo();
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document.forms[0],true);
			setDisabled("dtVigenciaPromocaoFinal",false);
			setDisabled("storeItemButton",false);
			setDisabled("removeButton",false);
			setDisabled("newButton", false);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document.forms[0],true);
		}
	}

	/**
	* Validação: preenchimento exclusivo de 'fator de multiplicação' e 'valor fixo'.
	*/
	function runValidation3() {
		var fatorMultiplicacao = getElementValue("nrFatorMultiplicacao");
		var valorFixo = getElementValue("vlFixo");
		if( ((fatorMultiplicacao == "") && (valorFixo == "")) || ((fatorMultiplicacao != "") && (valorFixo!="")) ) {
			alertI18nMessage("LMS-30014");
			setFocus("nrFatorMultiplicacao", false);
			return false;
		}
		return true;
	}

	/**
	* Validação: caso os campos 'tarifa' e 'rota' sejam prenchidos, o campo 'valor fixo' deve
	* ser também preenchido.
	*/
	function runValidation2() {
		var tarifa = getElementValue("tarifaPreco.idTarifaPreco");
		var rota = getElementValue("rotaPreco.idRotaPreco");
		var valorFixo = getElementValue("vlFixo");
		if( ((tarifa != "") && (rota != "")) && valorFixo == "") {
			alertRequiredField("vlFixo");
			setFocus("vlFixo", false);
			return false;
		}
		return true;
	}

	/**
	* Validação: caso os campos 'tarifa', 'rota' e 'valor fixo' não tenham sido preenchidos, o campo
	* 'fator de multiplicação' deve ser preenchido.
	*/
	function runValidation1() {
		var tarifa = getElementValue("tarifaPreco.idTarifaPreco");
		var rota = getElementValue("rotaPreco.idRotaPreco");
		var fatorMultiplicacao = getElementValue("nrFatorMultiplicacao");
		var valorFixo = getElementValue("vlFixo");
		if(tarifa=='' && rota=='' && valorFixo=='' && fatorMultiplicacao=='') {
			alertRequiredField("nrFatorMultiplicacao");
			setFocus("nrFatorMultiplicacao", false);
			return false;
		}
		return true;
	}

	/**
	* Validação: se for promocional, a vigência inicial deve ser preenchida.
	*/
	function runValidation4() {
		if(getElementValue("blPromocional")==true && getElementValue("dtVigenciaPromocaoInicial")=="") {
			alertI18nMessage("LMS-30018");
			setFocus("dtVigenciaPromocaoInicial", false);
			return false;
		}
		return true;
	}

	/**
	* Validação: a tarifa e a rota devem ser preenchidas exclusivamente.
	*/
	function runValidation5() {
		if(getElementValue("tarifaPreco.idTarifaPreco")!="" && getElementValue("rotaPreco.idRotaPreco")!="") {
			alertI18nMessage("LMS-30020");
			setFocus("tarifaPreco.cdTarifaPreco", false);
			return false;
		}
		return true;
	}

	/**
	* Validação: se for promocional, a tarifa ou a rota precisam ser preenchidas
	*/
	function runValidation6() {
		if(getElementValue("blPromocional")==true && getElementValue("tarifaPreco.idTarifaPreco")=="" && getElementValue("rotaPreco.idRotaPreco")=="") {
			alertI18nMessage("LMS-30021");
			setFocus("tarifaPreco.cdTarifaPreco", false);
			return false;
		}
		return true;
	}

	/**
	* Validação do form.
	*/
	function validateTab() {
		return validateTabScript(document.forms) && runValidation6() && runValidation5() && runValidation1()
				&& runValidation2() && runValidation3()	&& runValidation4();
	}
</script>
