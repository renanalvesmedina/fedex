<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.vendas.manterParametrosClienteGeneralidadesAction"
>
	<adsm:i18nLabels>
		<adsm:include key="LMS-01050"/>
		<adsm:include key="LMS-01060"/>
		<adsm:include key="LMS-01070"/>
		<adsm:include key="LMS-01067"/>
		<adsm:include key="LMS-01068"/>
		<adsm:include key="LMS-01075"/>
	</adsm:i18nLabels>
	<adsm:form 
		action="/vendas/manterParametrosCliente" 
		idProperty="idGeneralidadeCliente"
		onDataLoadCallBack="dlc"
	>
		<adsm:hidden property="parametroCliente.idParametroCliente"/>
		<%-------------------%>
		<%-- CLIENTE LOOKUP --%>
		<%-------------------%>
		<adsm:complement 
			label="cliente" 
			labelWidth="18%" 
			required="true"
			width="43%"
			separator="branco"
		>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false" 
				size="20"
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" 
				size="30"
			/>
		</adsm:complement>
		<%-------------------%>
		<%-- DIVISAO COMBO --%>
		<%-------------------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="divisao" 
			labelWidth="13%" 
			property="tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente"
			required="true"	
			serializable="false" 
			size="20" 
			width="20%"
		/>
		<%-------------------%>
		<%-- TABELA LOOKUP --%>
		<%-------------------%>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.idTabelaPreco"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"/>
		<adsm:hidden property="tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"/>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="tabela" 
			labelWidth="18%" 
			property="tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao"
			required="true"	
			serializable="false" 
			size="60" 
			width="82%"
		/>
		<%-------------------%>
		<%-- SERVICO COMBO --%>
		<%-------------------%>
		<%--adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="servico" 
			labelWidth="13%" 
			property="tabelaDivisaoCliente.servico.dsServico"
			required="true"
			serializable="false" 
			size="32" 
			width="26%"
		/--%>
		<%-------------------%>
		<%-- ROTAS TEXT--%>
		<%-------------------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="origem" 
			labelWidth="18%"
			property="rotaPreco.origemString" 
			size="80" 
			width="78%" 
		/>
		<adsm:textbox 
			dataType="text" 
			disabled="true" 
			label="destino" 
			labelWidth="18%"
			property="rotaPreco.destinoString" 
			size="80" 
			width="78%" 
		/>
		<%-------------------%>
		<%-- GENERALIDADE COMBO --%>
		<%-------------------%>
		<adsm:hidden property="parcelaPreco.cdParcelaPreco"/>
		<adsm:combobox 
			boxWidth="230"
			label="generalidade" 
			labelWidth="18%"
			onlyActiveValues="true"
			onchange="aplicaRegras(); changeDomainIndicador();"
			optionLabelProperty="nmParcelaPreco" 
			optionProperty="idParcelaPreco" 
			property="parcelaPreco.idParcelaPreco" 
			required="true"
			service="lms.vendas.manterParametrosClienteGeneralidadesAction.findGeneralidadeCombo" 
			width="43%"
			autoLoad="false">
			<adsm:propertyMapping 
				relatedProperty="parcelaPreco.cdParcelaPreco" 
				modelProperty="cdParcelaPreco"/>
		</adsm:combobox>
		<%-------------------%>
		<%-- INDICADOR COMBO --%>
		<%-------------------%>
		<adsm:combobox 
			domain="DM_INDICADOR_PARAMETRO_CLIENTE" 
			label="indicador" 
			labelWidth="13%"
			onchange="verifyCombo('tpIndicador','vlGeneralidade')"
			property="tpIndicador" 
			required="true"
			width="26%"
		/>
		<%-------------------%>
		<%-- MOEDA TEXT --%>
		<%-------------------%>
		<adsm:textbox 
			dataType="text" 
			disabled="true"
			property="tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo" 
			label="moeda" 
			size="9" 
			labelWidth="18%" 
			width="43%" 
			required="true" 
		/>
		<%-------------------%>
		<%-- VALOR TEXT --%>
		<%-------------------%>
		<adsm:textbox 
			dataType="decimal" 
			property="vlGeneralidade" 
			label="valor" 
			labelWidth="13%" 
			mask="###,###,###,###,##0.00"

			onchange="return onChangeValue('tpIndicador','vlGeneralidade');"
			size="18" 
			required="true" 
			width="26%" 
		/>

		<adsm:combobox property="tpIndicadorMinimo" label="indicadorDoMinimo" required="true"
			labelWidth="18%" width="43%"
			domain="DM_INDICADOR_PARAMETRO_CLIENTE"
			onchange="verifyCombo('tpIndicadorMinimo','vlMinimo');"/>

		<adsm:textbox property="vlMinimo" label="valorDoMinimo" required="true" 
			labelWidth="13%" width="26%" size="18"
			dataType="decimal" mask="###,###,###,###,##0.00"
			onchange="onChangeVlMinimo()"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarGeneralidade" disabled="false" />
			<adsm:button caption="limpar" id="limpar" buttonType="limpar" onclick="setDefaultValues()"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 
		autoSearch="false"
		detailFrameName="gen"
		idProperty="idGeneralidadeCliente" 
		gridHeight="170" 
		property="generalidadeCliente" 
		unique="true" 
		rows="9"
	>
		<adsm:gridColumn 
			title="generalidade" 
			property="parcelaPreco.nmParcelaPreco" 
			width="40%" 
		/>
		<adsm:gridColumn 
			title="indicador" 
			property="tpIndicador" 
			width="30%" 
			isDomain="true"
		/>
		<adsm:gridColumn 
			align="right"
			title="valorIndicador" 
			property="valorIndicador" 
			width="30%"
		/>
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirGeneralidade" />
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>
<script>
	var tpIndicadorValue;
	function loadComboGeneralidades() {
		/* Seta idTabelaPreco para garregar combo de Taxas */
		var idTabelaPreco = getTabGroup(this.document).getTab("cad").getElementById("tabelaDivisaoCliente.tabelaPreco.idTabelaPreco").value;
		var sdo = createServiceDataObject("lms.vendas.manterParametrosClienteGeneralidadesAction.findGeneralidadeCombo", "parcelaPreco.idParcelaPreco", {idTabelaPreco:idTabelaPreco});
		xmit({serviceDataObjects:[sdo]});
	}

	function onChangeVlMinimo(){
		var vlMinimo = str2number(getElementValue("vlMinimo"));
		if (getElementValue("tpIndicadorMinimo") == "D"){
			if (vlMinimo > str2number(percentualLimiteDesconto)){
				alertI18nMessage("LMS-01067");
				return false;
			}
		}else{
			if(vlMinimo < 0 || vlMinimo > 100){
				alertI18nMessage("LMS-01068");
				return false;
			}
		} 
		return true;
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

	/**
	* onDataLoadCallBack do form:
	* - habilita o valor de acordo com indicador da generalidade
	*/
	function dlc_cb(dados,erros,errorCode, eventObj) {
		tpIndicadorValue = dados.tpIndicador;
		changeDomainIndicador(dados.parcelaPreco.cdParcelaPreco);

		onDataLoad_cb(dados,erros,errorCode,eventObj);
		aplicaRegras();
		verifyCombo("tpIndicador","vlGeneralidade");
		verifyCombo("tpIndicadorMinimo","vlMinimo");
	}

	function setDefaultValues() {
		newButtonScript();
		setInitialValues();
	}

	/**
	* onHide da tab
	* - limpa os campos do detalhe da generalidade
	*/
	function hide() {
		newButtonScript();
		tab_onHide();
		return true;
	}

	/**
	* Valida a generalidade
	*/
	function validateTab() {
		return validateTabScript(document.forms) && 
				validation1() && 
				validation2() && 
				validation3();
	}

	/**
	* Percentual de limite de desconto referente à generalidade
	*/
	var percentualLimiteDesconto = 0;

	/**
	* Função auxiliar para transformas um string em um número
	*/
	function str2number(str) {
	    if ((str == "")||(str == null)||(str.length == 0)) {
	        return 0;
	    }
	    return parseFloat(str);
	}

	function validation1() {
		if(str2number(percentualLimiteDesconto)<100) {
			var field = getElement("tpIndicador");
			var tpIndicador = getElementValue(field);
			if(tpIndicador!="T" && tpIndicador!="D" && tpIndicador!="A") {
				alertI18nMessage("LMS-01070", field.label, false);
				return false;
			}
			
			tpIndicador = getElementValue("tpIndicadorMinimo");
			if(tpIndicador!="T" && tpIndicador!="D" && tpIndicador!="A") {
				alertI18nMessage("LMS-01067");
				return false;
			}
		}
		return true;
	}

	function validation2() {
		var tpIndicador = getElementValue("tpIndicador");
		var field = getElement("vlGeneralidade");
		var vlGeneralidade = str2number(getElementValue(field));
		if(tpIndicador=="V" || tpIndicador=="Q" || tpIndicador=="A") {
			if(vlGeneralidade<0) {
				alertI18nMessage("LMS-01050", field.label, false);
				return false;
			}
		} else if(tpIndicador=="D") {
			if(vlGeneralidade<0 || vlGeneralidade>100) {
				alertI18nMessage("LMS-01050", field.label, false);
				return false;
			}
		}

		tpIndicador = getElementValue("tpIndicadorMinimo");
		var vlMinimo = str2number(getElementValue("vlMinimo"));
		if(tpIndicador == "V" || tpIndicador == "D" || tpIndicador == "A"){
			if (vlMinimo < 0 || vlMinimo > 100){
				alertI18nMessage("LMS-01075");
				return false;
			}
		}
		if (tpIndicador == "D"){
			if (vlMinimo > str2number(percentualLimiteDesconto)){
				alertI18nMessage("LMS-01068");
				return false;		
			}
		}
		return true;
	}

	function validation3() {
		var tpIndicador = getElementValue("tpIndicador");
		var field = getElement("vlGeneralidade");
		var vlGeneralidade = str2number(getElementValue(field));
		if(tpIndicador=="D") {
			if(vlGeneralidade>str2number(percentualLimiteDesconto)) {
				alertI18nMessage("LMS-01068");
				return false;
			}
		}
		
		return true;
	}

	/**
	* Obtém o percentual de limite de desconto referente à generalidade
	*/
	function aplicaRegras() {
		var idParcelaPreco = getElementValue("parcelaPreco.idParcelaPreco");
		var idSubtipoTabelaPreco = getElementValue("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco");
		var tpTipoTabelaPreco = getElementValue("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco");
		var sdo = createServiceDataObject("lms.vendas.manterParametrosClienteGeneralidadesAction.findIndex", "aplicaRegras", {idSubtipoTabelaPreco:idSubtipoTabelaPreco, idParcelaPreco:idParcelaPreco, tpTipoTabelaPreco:tpTipoTabelaPreco});
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	* Armazena o percentual de limite de desconto referente à generalidade
	*/
	function aplicaRegras_cb(data, errMsg) {
		percentualLimiteDesconto = str2number(getNestedBeanPropertyValue(data, "percentualLimiteDesconto"));
		//var returnFlag = str2number(getNestedBeanPropertyValue(data, "returnFlag"));
	}

	function changeDomainIndicador(cdParcelaPreco) {
		if(cdParcelaPreco == undefined) {
			comboboxChange({e:getElement("parcelaPreco.idParcelaPreco")});
			cdParcelaPreco = getElementValue("parcelaPreco.cdParcelaPreco");
			tpIndicadorValue = undefined;
		}
		var isTad = ("IDTad" == cdParcelaPreco || "IDTransferencia" == cdParcelaPreco );
		
		var domain = isTad ? "DM_INDICADOR_TAD" : "DM_INDICADOR_PARAMETRO_CLIENTE";

		var tpIndicador = getElement("tpIndicador");
		if(tpIndicador.domain != domain) {
			var sdo = createServiceDataObject("lms.vendas.manterParametrosClienteGeneralidadesAction.findDomainValues", "tpIndicador", {e:domain});
			xmit({serviceDataObjects:[sdo]});
			tpIndicador.domain = domain;
		} else setDefaultValueIndicador();
	}

	function tpIndicador_cb(data) {
		comboboxLoadOptions({e:document.getElementById("tpIndicador"), data:data});
		setDefaultValueIndicador();
	}

	function setDefaultValueIndicador() {
		if(tpIndicadorValue != undefined) {
			setElementValue("tpIndicador", tpIndicadorValue);
		} else setInitialValues();
	}

	/**
	* Habilita/desabilita o campo de valor de acordo com o indicador
	*/
	
	
	function verifyCombo(fieldIndicador,fieldValue) {
		var comboValue = getElementValue(fieldIndicador);
		if(comboValue=="T" || comboValue=="") {
			setElementValue(fieldValue,"0,00");
			setDisabled(fieldValue,true);
		} else {
			setDisabled(fieldValue,false);
		}
	}

	/**
	* Popula a grid com as generalidades do parâmetro do cliente
	*/
	function populaGrid() {
		generalidadeClienteGridDef.executeSearch({parametroCliente:{idParametroCliente:getElementValue("parametroCliente.idParametroCliente")}}, true); 
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			loadComboGeneralidades();
			copyValuesFromCad();
			var frame = parent.document.frames["cad_iframe"];
			if(validateForm(frame.getForm())!=true) {
				var tabGroup = getTabGroup(this.document);
				tabGroup.selectTab("cad",{name:"tab_click"});
			}
			setInitialValues();
			populaGrid();
		} else if(eventObj.name == "storeButton") {
			populaGrid();
			setDefaultValues();
			setFocusOnFirstFocusableField();
		} else if(eventObj.name == "removeButton_grid") {
			setDefaultValues();
			populaGrid();
		} 
	}

	/**
	* Ajusta o valor inicial do indicador de generalidade e de seu valor
	*/
	function setInitialValues() {
		setElementValue("tpIndicador","T");
		setElementValue("tpIndicadorMinimo","T");
		setElementValue("vlGeneralidade","0,00");
		setElementValue("vlMinimo","0,00");
		setDisabled("vlGeneralidade",true);
		setDisabled("vlMinimo",true);
	}

	/**
	* Busca alguns valores do parametro do cliente
	*/
	function copyValuesFromCad() {
		var frame = parent.document.frames["cad_iframe"];
		var dados = frame.getValuesFromCad();
		copyValues(dados); 
	}

	/**
	* Auxiliar para a busca de valores de parametro do cliente
	*/
	function copyValuesAndSetMasterLink(dados,nome) {
		var obj = document.getElementById(nome);
		var value = getNestedBeanPropertyValue(dados, nome);
		setElementValue(obj, value);
		obj.masterLink = "true";
	}

	/**
	* Auxiliar para a busca de valores de parametro do cliente
	*/
	function copyValuesAndSetMasterLink2(dados,source,target) {
		var obj = document.getElementById(target);
		var value = getNestedBeanPropertyValue(dados, source);
		setElementValue(obj, value);
		obj.masterLink = "true";
	}

	/**
	* Auxiliar para a busca de valores de parametro do cliente
	*/
	function copyValues(dados) {
		copyValuesAndSetMasterLink2(dados,"idParametroCliente","parametroCliente.idParametroCliente");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.divisaoCliente.cliente.pessoa.nmPessoa");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.divisaoCliente.dsDivisaoCliente");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.tabelaPrecoStringDescricao");
		//copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.servico.dsServico");
		copyValuesAndSetMasterLink(dados,"tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo");

		var origemObj = document.getElementById("rotaPreco.origemString");
		setElementValue(origemObj, getDescricaoRota(dados, "Origem"));
		origemObj.masterLink = "true";

		var destinoObj = document.getElementById("rotaPreco.destinoString");
		setElementValue(destinoObj, getDescricaoRota(dados, "Destino"));
		destinoObj.masterLink = "true";
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
</script>