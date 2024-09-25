<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.vendas.manterParametrosClienteTaxasAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterParametrosCliente"
		idProperty="idTaxaCliente" onDataLoadCallBack="myOnDataLoad">
		<adsm:hidden property="parametroCliente.idParametroCliente"/>

		<adsm:complement
			label="cliente"
			labelWidth="18%" 
			required="true"
			width="43%"
			separator="branco">
			<adsm:textbox dataType="text" disabled="true" 
				property="parametroCliente.tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false" size="20"/>
			<adsm:textbox dataType="text" disabled="true" 
				property="parametroCliente.tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" size="30"/>
		</adsm:complement>

		<adsm:textbox dataType="text" disabled="true" label="divisao" 
			labelWidth="13%" property="parametroCliente.tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			required="true"	serializable="false" size="20" width="20%"/>
		
		<adsm:textbox dataType="text" label="tabela" labelWidth="18%" 
			required="true"	width="82%" serializable="false" size="60" disabled="true"
			property="parametroCliente.tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"/>

		<%--adsm:textbox dataType="text" disabled="true" label="servico" 
			labelWidth="13%" property="parametroCliente.tabelaDivisaoCliente.servico.dsServico"
			required="true"	serializable="false" size="32" width="26%"/--%>

		<adsm:textbox dataType="text" label="origem" size="80"
			property="parametroCliente.rotaOrigem" labelWidth="18%" 
			width="78%" disabled="true" />
		<adsm:textbox dataType="text" label="destino" size="80"
			property="parametroCliente.rotaDestino" labelWidth="18%" 
			width="78%" disabled="true" />

		<adsm:combobox property="parcelaPreco.idParcelaPreco" 
			label="taxa" optionLabelProperty="nmParcelaPreco" 
			optionProperty="idParcelaPreco" required="true" 
			service="lms.vendas.manterParametrosClienteTaxasAction.findParcelaPrecoTaxa" 
			labelWidth="18%" width="43%" autoLoad="false">
		</adsm:combobox>

		<adsm:combobox property="tpTaxaIndicador" label="indicador" 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" onlyActiveValues="true"
			required="true" labelWidth="13%" width="26%" onchange="changeIndicador(this);" />
		<adsm:textbox dataType="text" disabled="true"
			property="parametroCliente.tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo"
			label="moeda" maxLength="10" size="9" 
			required="true" 
			labelWidth="18%" width="43%"/>	
		<adsm:textbox dataType="decimal" property="vlTaxa" 
			label="valor" required="true" 
			mask="##,###,###,###,##0.00" size="18" 
			onchange="return onChangeValue('tpTaxaIndicador','vlTaxa');"
			labelWidth="13%" width="21%" />
		<adsm:textbox dataType="decimal" property="psMinimo" 
			label="minimoKgKm" 
			mask="#,###,###,###,##0.000"
			size="18" onchange="return validaTaxa()"
			labelWidth="18%" width="43%" unit="kg" />
		<adsm:textbox dataType="decimal" property="vlExcedente" 
			label="valorExcedente" mask="##,###,###,###,##0.00" size="18" 
			onchange="return validaTaxa()"
			labelWidth="13%" width="26%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarTaxa"/>
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid autoSearch="false" detailFrameName="tax" property="taxaCliente"
		gridHeight="170" unique="true" idProperty="idTaxaCliente" rows="8">
		<adsm:gridColumn title="taxa" property="parcelaPreco.nmParcelaPreco" 
			width="27%" />
		<adsm:gridColumn title="indicador" property="tpTaxaIndicador" 
			width="20%" isDomain="true" />
		<adsm:gridColumn title="valorIndicador" property="valor" 
			align="right" width="18%" />
		<adsm:gridColumn title="pesoMinimoKg" dataType="decimal" 
			property="psMinimo" align="right" width="15%" 
			mask="#,###,###,###,##0.000" />
		<adsm:gridColumn title="valorExcedenteReal" 
			property="vlExcedente" mask="##,###,###,###,##0.00"
			dataType="decimal" align="right" width="20%"/>
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirTaxa"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function loadComboTaxas() {
		/* Seta idTabelaPreco para garregar combo de Taxas */
		var idTabelaPreco = getTabGroup(this.document).getTab("cad").getElementById("tabelaDivisaoCliente.tabelaPreco.idTabelaPreco").value;
		var sdo = createServiceDataObject("lms.vendas.manterParametrosClienteTaxasAction.findParcelaPrecoTaxa", "parcelaPreco.idParcelaPreco", {idTabelaPreco:idTabelaPreco});
		xmit({serviceDataObjects:[sdo]});
	}

	function hide() {
		newButtonScript();
		tab_onHide();
		return true;
	}

	/**
	* Função auxiliar para transformas um string em um número
	*/
	function str2number(str) {
		if ((str == "")||(str == null)||(str.length == 0)) {
			return 0;
		}
		return parseFloat(str);
	}

	function onChangeValue(comboName,valueName) {
		var type = getElementValue(comboName);
		var field = getElement(valueName);
		var value = str2number(getElementValue(valueName));
		if(type=="D") {
			if(value<0 || value>100) {
				alertI18nMessage("LMS-01050", field.label, false);
				return false;
			}
		} else {
			if(value<0) {
				alertI18nMessage("LMS-01050", field.label, false);
				return false;
			}
		}
		return true;
	}

	function validateTab() {
		return validateTabScript(document.forms) && validaTaxa();
	}

	function validaTaxa() {
		var ind = getElementValue("tpTaxaIndicador");
		var vlObj = getElement("vlTaxa");
		var val = stringToNumber(prepareStringToNumber(vlObj.value));
		if(ind == "V" || ind == "A") {
			if(val < 0) {
				alertI18nMessage("LMS-01050", vlObj.label, false);
				setFocus(vlObj);
				return false;
			}
		} else if(ind == "D") {
			if(val < 0 || val > 100) {
				alertI18nMessage("LMS-01050", vlObj.label, false);
				setFocus(vlObj);
				return false;
			}
		}
		var psObj = document.getElementById("psMinimo");
		var peso = stringToNumber(psObj.value);
		if(peso < 0) {
			alertI18nMessage("LMS-01050", psObj.label, false);
			setFocus(psObj);
			return false;
		}
		var vlExObj = document.getElementById("vlExcedente");
		var vlEx = stringToNumber(vlExObj.value);
		if(vlEx < 0) {
			alertI18nMessage("LMS-01050", vlExObj.label, false);
			setFocus(vlExObj);
			return false;
		}
		return true;
	}

	function copyValuesFromCad() {
		var frame = parent.document.frames["cad_iframe"];
		var dados = frame.getValuesFromCad();
		copyValuesAndSetMasterLink(dados);
	}

	function copyValuesAndSetMasterLink(dados) {
		var idParamentroObj = document.getElementById("parametroCliente.idParametroCliente");
		var idParamentroValue = getNestedBeanPropertyValue(dados, "idParametroCliente");
		setElementValue(idParamentroObj, idParamentroValue);
		idParamentroObj.masterLink = "true";

		var nrIdentificacaoObj = document.getElementById("parametroCliente.tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao");
		var nrIdentificacaoValue = getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao");
		setElementValue(nrIdentificacaoObj, nrIdentificacaoValue);
		nrIdentificacaoObj.masterLink = "true";

		var nmPessoaObj = document.getElementById("parametroCliente.tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa");
		var nmPessoaValue = getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa");
		setElementValue(nmPessoaObj, nmPessoaValue);
		nmPessoaObj.masterLink = "true";

		var divisaoObj = document.getElementById("parametroCliente.tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente");
		var divisaoValue = getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente");
		setElementValue(divisaoObj, divisaoValue);
		divisaoObj.masterLink = "true";

		var tabelaCritObj = document.getElementById("parametroCliente.tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao");
		var tabelaCritValue = getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao");
		setElementValue(tabelaCritObj, tabelaCritValue);
		tabelaCritObj.masterLink = "true";

		var origemObj = document.getElementById("parametroCliente.rotaOrigem");
		setElementValue(origemObj, getDescricaoRota(dados, "Origem"));
		origemObj.masterLink = "true";

		var destinoObj = document.getElementById("parametroCliente.rotaDestino");
		setElementValue(destinoObj, getDescricaoRota(dados, "Destino"));
		destinoObj.masterLink = "true";

		var tabelaMoedaObj = document.getElementById("parametroCliente.tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo");
		var tabelaMoedaValue = getNestedBeanPropertyValue(dados, "tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo");
		setElementValue(tabelaMoedaObj, tabelaMoedaValue);
		tabelaMoedaObj.masterLink = "true";
	}

	function getDescricaoRota(dados, tipo) {
		var rota = "";
		rota += getParteRota(dados, "zonaByIdZona" + tipo + ".dsZona");
		rota += getParteRota(dados, "paisByIdPais" + tipo + ".nmPais");
		rota += getParteRota(dados, "unidadeFederativaByIdUf" + tipo + ".siglaDescricao");
		rota += getParteRota(dados, "filialByIdFilial" + tipo + ".sgFilial");
		rota += getParteRota(dados, "municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio");
		rota += getParteRota(dados, "aeroportoByIdAeroporto" + tipo + ".sgAeroporto");
		rota += getParteRota(dados, "tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".dsTipoLocalizacaoMunicipio");
		if(rota != "") {
			var i = rota.lastIndexOf("-");
			rota = rota.substring(0, i);
		}
		return rota;
	}

	function getParteRota(dados, parte) {
		value = getNestedBeanPropertyValue(dados, parte);
		return ((value == "") ? "" : value + "-");
	}

	function initWindow(eventObj) {
		var event = eventObj.name;
		if(event == "tab_click") {
			loadComboTaxas();
			copyValuesFromCad();
			var frame = parent.document.frames["cad_iframe"];
			if(validateForm(frame.getForm())!=true) {
				var tabGroup = getTabGroup(this.document);
				tabGroup.selectTab("cad",{name:"tab_click"});
			}
			populaGrid();
		} else if(event == "storeButton") {
			populaGrid();
		} else if(eventObj.name == "removeButton_grid") {
			newButtonScript();
			populaGrid();
		}
	}

	function populaGrid() {
		taxaClienteGridDef.executeSearch({parametroCliente:{idParametroCliente:getElementValue("parametroCliente.idParametroCliente")}}, true);
	}

	function changeIndicador(combo) {
		comboboxChange({e:combo});
		configIndicador();
	}

	function configIndicador() {
		var ind = getElementValue("tpTaxaIndicador");
		if(ind == "T") {
			setElementValue("vlTaxa", "0,00");
			setElementValue("psMinimo", "0,00");
			setElementValue("vlExcedente", "0,00");
			setDisabled("vlTaxa", true);
			setDisabled("psMinimo", true);
			setDisabled("vlExcedente", true);
		} else {
			setDisabled("vlTaxa", false);
			setDisabled("psMinimo", false);
			setDisabled("vlExcedente", false);
		}
	}

	function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj) {
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
		configIndicador();
	}
</script>