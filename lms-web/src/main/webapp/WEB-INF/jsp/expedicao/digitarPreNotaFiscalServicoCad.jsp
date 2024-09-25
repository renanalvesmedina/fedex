<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.expedicao.digitarPreNotaFiscalServicoAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04100"/>
		<adsm:include key="LMS-04105"/>
		<adsm:include key="LMS-04106"/>
	</adsm:i18nLabels>

	<adsm:form
		action="/expedicao/digitarPreNotaFiscalServico" idProperty="idNotaFiscalServico"
		height="380"
		onDataLoadCallBack="myOnDataLoad">

		<adsm:hidden property="filial.idFilial"/>
		<adsm:hidden property="filial.pessoa.endereco.municipio.idMunicipio"/>
		<adsm:textbox
			label="filialEmissao"
			property="filial.sgFilial"
			dataType="text"
			size="5"
			labelWidth="14%"
			width="36%"
			disabled="true"
			serializable="false"
			required="true">
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="filial.pessoa.nmFantasia"
				serializable="false"
				size="30"/>
		</adsm:textbox>

		<adsm:textbox
			label="numNF"
			property="nrNotaFiscalServico"
			dataType="integer"
			size="8"
			mask="000000"
			labelWidth="9%"
			width="13%"
			disabled="true"/>

		<adsm:hidden property="tpSituacaoNf"/>
		<adsm:textbox
			label="situacao"
			property="tpSituacaoNfLabel"
			dataType="text"
			labelWidth="11%"
			width="17%"
			size="10"
			disabled="true"/>

		<adsm:textbox
			label="dataHoraEmissao"
			property="dhEmissao"
			dataType="JTDateTimeZone"
			labelWidth="14%"
			width="36%"
			disabled="true"
			picker="false"/>

		<adsm:hidden property="idUsuarioLogado"/>
		<adsm:textbox
			label="emissor"
			property="nrIdentificacaoUsuarioLogado"
			dataType="text"
			size="12"
			labelWidth="9%"
			width="41%"
			disabled="true"
			serializable="false">
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="nmPessoaUsuarioLogado"
				serializable="false"
				size="30"/>
		</adsm:textbox>

		<adsm:section caption="dadosTomadorServico"/>
		<adsm:lookup
			label="cliente"
			property="clienteByIdClienteDestinatario"
			idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findDestinatario"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			allowInvalidCriteriaValue="true"
			criteriaSerializable="true"
			onchange="return onChangeDestinatario();"
			onDataLoadCallBack="destinatario"
			onPopupSetValue="destinatarioPopup"
			size="20"
			maxLength="20"
			width="43%"
			labelWidth="11%"
			exactMatch="true"
			required="true"
		>
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
			<adsm:propertyMapping
				modelProperty="pessoa.nrIdentificacao"
				relatedProperty="clienteByIdClienteDestinatario.nrIdentificacao"/>
			<adsm:propertyMapping
				modelProperty="tpCliente.value"
				relatedProperty="clienteByIdClienteDestinatario.tpCliente"/>
			<adsm:propertyMapping
				modelProperty="tpSituacao"
				criteriaProperty="tpSituacao"/>

			<adsm:hidden property="tpSituacao" value="A"/>

			<adsm:textbox
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				size="30"
				maxLength="30"
				disabled="true"/>
		</adsm:lookup>

		<adsm:hidden property="clienteByIdClienteDestinatario.nrIdentificacao"/>
		<adsm:hidden property="clienteByIdClienteDestinatario.tpCliente"/>

		<td colspan="12">
			<adsm:button
				caption="incluir"
				onclick="cadastrarCliente();"
				style="FmLbSection"
				id="clienteByIdClienteDestinatarioButton"
				disabled="true"/>
		</td>

		<adsm:combobox
			label="ie"
			property="clienteByIdClienteDestinatario.ie.id"
			optionLabelProperty="nrInscricaoEstadual"
			optionProperty="idInscricaoEstadual"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findInscricaoEstadual"
			boxWidth="110"
			labelWidth="10%"
			width="35%"
			onDataLoadCallBack="inscricaoEstadualDestinatario">
			<adsm:propertyMapping
				criteriaProperty="clienteByIdClienteDestinatario.idCliente"
				modelProperty="idPessoa"/>

			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteDestinatario.nrInscricaoEstadual"
				modelProperty="nrInscricaoEstadual"/>
			<adsm:propertyMapping
				relatedProperty="clienteByIdClienteDestinatario.idInscricaoEstadualAux"
				modelProperty="idInscricaoEstadual"/>
		</adsm:combobox>
		<adsm:hidden property="clienteByIdClienteDestinatario.nrInscricaoEstadual"/>
		<adsm:hidden property="clienteByIdClienteDestinatario.idInscricaoEstadualAux"/>
		<adsm:hidden property="clienteByIdClienteDestinatario.nrInscricoesEstaduais"/>

		<adsm:textbox
			label="endereco"
			property="clienteByIdClienteDestinatario.endereco.dsEndereco"
			dataType="text"
			size="40"
			maxLength="100"
			width="30%"
			labelWidth="11%"
			disabled="true"/>

		<adsm:textbox
			label="numero"
			property="clienteByIdClienteDestinatario.endereco.nrEndereco"
			dataType="text"
			size="5"
			maxLength="6"
			labelWidth="10%"
			width="15%"
			disabled="true"/>

		<adsm:textbox
			label="complemento"
			property="clienteByIdClienteDestinatario.endereco.dsComplemento"
			dataType="text"
			size="21"
			maxLength="40"
			labelWidth="10%"
			width="23%"
			disabled="true"/>

		<adsm:textbox
			label="municipio"
			property="clienteByIdClienteDestinatario.endereco.nmMunicipio"
			dataType="text"
			maxLength="50"
			width="30%"
			labelWidth="11%"
			disabled="true"/>

		<adsm:textbox
			label="uf"
			property="clienteByIdClienteDestinatario.endereco.sgUnidadeFederativa"
			dataType="text"
			size="5"
			labelWidth="10%"
			width="15%"
			disabled="true"/>

		<adsm:hidden property="clienteByIdClienteDestinatario.endereco.idUnidadeFederativa"/>

		<adsm:textbox
			label="cep"
			property="clienteByIdClienteDestinatario.endereco.nrCep"
			dataType="text"
			maxLength="12"
			labelWidth="10%"
			width="23%"
			size="12"
			disabled="true"/>

		<adsm:section caption="dadosAdicionais"/>
		<adsm:lookup
			label="municipioDoServico"
			property="municipio"
			idProperty="idMunicipio"
			criteriaProperty="nmMunicipio"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findMunicipioLookup"
			action="/municipios/manterMunicipios"
			labelWidth="15%"
			width="35%"
			dataType="text"
			size="34"
			maxLength="60"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			required="true">

			<adsm:propertyMapping
				modelProperty="tpSituacao"
				criteriaProperty="tpSituacao"/>

			<adsm:hidden
				property="tpSituacao"
				value="A"/>
		</adsm:lookup>

		<adsm:hidden property="divisaoCliente.dsDivisaoCliente"/>
		<adsm:combobox
			label="divisao"
			property="divisaoCliente.idDivisaoCliente"
			optionLabelProperty="dsDivisaoCliente"
			optionProperty="idDivisaoCliente"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findDivisaoCombo"
			onDataLoadCallBack="findDivisaoCombo"
			labelWidth="15%"
			width="35%"
			boxWidth="140">
			<adsm:propertyMapping 
				relatedProperty="divisaoCliente.dsDivisaoCliente" 
				modelProperty="dsDivisaoCliente"/>
			<adsm:propertyMapping 
				criteriaProperty="clienteByIdClienteDestinatario.idCliente" 
				modelProperty="cliente.idCliente"/>
			<adsm:propertyMapping 
				criteriaProperty="clienteByIdClienteDestinatario.tpCliente" 
				modelProperty="cliente.tpCliente"/>
		</adsm:combobox>

		<adsm:combobox
			label="tipoCalculo"
			property="tpCalculoPreco"
			domain="DM_TIPO_CALCULO_NFS"
			required="true"
			labelWidth="15%"
			width="35%"
			boxWidth="87"
			defaultValue="N"
			onchange="return onChangeTpCalculoPreco();"/>

		<adsm:combobox
			label="numCotacao"
			property="cotacao.idCotacao"
			optionLabelProperty="nrCotacao"
			optionProperty="idCotacao"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findCotacao"
			labelWidth="15%"
			width="35%">
			<adsm:propertyMapping 
				criteriaProperty="clienteByIdClienteDestinatario.idCliente" 
				modelProperty="clienteByIdClienteSolicitou.idCliente"/>
		</adsm:combobox>

		<adsm:hidden property="servico.tpAbrangencia"/>
		<adsm:hidden property="servico.tpModal"/>
		<adsm:combobox
			label="servico"
			property="servico.idServico"
			onlyActiveValues="true"
			optionLabelProperty="dsServico"
			optionProperty="idServico"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findServicos"
			required="true"
			width="85%"
			labelWidth="15%"
			boxWidth="250">
			<adsm:propertyMapping relatedProperty="servico.tpModal" modelProperty="tpModal"/>
			<adsm:propertyMapping relatedProperty="servico.tpAbrangencia" modelProperty="tpAbrangencia"/>
		</adsm:combobox>

		<adsm:section caption="dadosServico" />
		<adsm:hidden property="servicoAdicional.parcelaPreco.cdParcelaPreco"/>
		<adsm:hidden property="servicoAdicional.dsServicoAdicional"/>
		<adsm:hidden property="servicoAdicional.idParcelaPreco"/>
		<adsm:combobox
			label="servicoAdicional"
			property="servicoAdicional.idServicoAdicional"
			optionLabelProperty="dsServicoAdicional"
			optionProperty="idServicoAdicional"
			required="true"
			service="lms.expedicao.digitarPreNotaFiscalServicoAction.findServicosAdicionais"
			onchange="return onChangeServicoAdicional(this);"
			width="78%"
			boxWidth="220"
			labelWidth="22%">
			<adsm:propertyMapping relatedProperty="servicoAdicional.parcelaPreco.cdParcelaPreco" modelProperty="cdParcelaPreco"/>
			<adsm:propertyMapping relatedProperty="servicoAdicional.dsServicoAdicional" modelProperty="dsServicoAdicional"/>
			<adsm:propertyMapping relatedProperty="servicoAdicional.idParcelaPreco" modelProperty="idParcelaPreco"/>
		</adsm:combobox>

		<adsm:textbox
			label="qtdeDias"
			property="servicoAdicional.qtDias"
			dataType="integer"
			size="10"
			mask="##,###"
			labelWidth="22%"
			width="13%"
			disabled="true"
			minValue="0"/>

		<adsm:textbox
			label="quantidadeColetas"
			property="servicoAdicional.qtColetas"
			dataType="integer"
			size="10"
			labelWidth="18%"
			mask="##,###"
			width="12%"
			disabled="true"
			minValue="0"/>

		<adsm:textbox
			label="quantidadePaletes"
			property="servicoAdicional.qtPaletes"
			dataType="integer"
			size="10"
			labelWidth="18%"
			width="17%"
			mask="##,###"
			disabled="true"
			minValue="0"/>

		<adsm:textbox
			label="quantidadeSegurancasAdicionais"
			property="servicoAdicional.qtSegurancasAdicionais"
			dataType="integer"
			size="10"
			labelWidth="22%"
			width="13%"
			mask="##,###"
			disabled="true"
			minValue="0"/>
			
		<adsm:textbox
			label="quilometragemRodada"
			property="servicoAdicional.nrKmRodado"
			dataType="integer"
			size="10"
			labelWidth="18%"
			mask="##,###"
			width="12%"
			disabled="true"
			minValue="0"/>

		<adsm:textbox
			label="pesoMercadoria"
			property="servicoAdicional.psMercadoria"
			dataType="weight"
			mask="###,###,###,###,##0.000"
			unit="kg"
			size="18"
			labelWidth="18%"
			width="12%"
			disabled="true"
			minValue="0"/>

		<adsm:textbox
			label="dataPrimeiroCheque"
			property="servicoAdicional.dtPrimeiroCheque"
			dataType="JTDate"
			size="10"
			labelWidth="22%"
			width="13%"
			disabled="true"/>

		<adsm:textbox
			label="qtdeCheques"
			property="servicoAdicional.qtCheques"
			dataType="integer"
			size="10"
			labelWidth="18%"
			mask="##,###"
			width="12%"
			disabled="true"
			minValue="0"/>

		<adsm:textbox
			label="valorMercadoriaReais"
			property="servicoAdicional.vlMercadoria"
			dataType="decimal"
			mask="###,###,###,###,##0.00"
			size="18"
			labelWidth="18%"
			width="17%"
			disabled="true"
			minValue="0"/>

		<adsm:range label="periodoReferencia" labelWidth="22%" width="78%">
			<adsm:textbox dataType="JTDate" property="dtInicial" smallerThan="dtFinal"/>
			<adsm:textbox biggerThan="dtInicial" dataType="JTDate" property="dtFinal"/>
		</adsm:range>

		<adsm:section
			caption="observacoes"/>

		<adsm:textbox
			property="obs0"
			dataType="text"
			size="145"
			maxLength="500"
			width="100%"/>

		<adsm:textbox
			property="obs1"
			dataType="text"
			size="145"
			maxLength="500"
			width="100%"/>

		<adsm:textbox
			property="obs2"
			dataType="text"
			size="145"
			maxLength="500"
			width="100%"/>

		<adsm:textbox
			property="obs3"
			dataType="text"
			size="145"
			maxLength="500"
			width="100%"/>

		<adsm:buttonBar
			freeLayout="false">
			<adsm:button
				disabled="false"
				id="novaNFButton"
				buttonType="novaNFBT"
				caption="novo"
				onclick="defaultParameters()"/>
			<adsm:storeButton
				disabled="false"
				id="calcularNFButton"
				caption="calcularNF"
				service="lms.expedicao.digitarPreNotaFiscalServicoAction.calculaNFServicoPrimeiraFase"
				callbackProperty="calculaNFServicoPrimeiraFase"/>
			<adsm:button
				disabled="false"
				id="reemitirNFButton"
				buttonType="reemitir"
				caption="reemitir"
				onclick="popupReemissaoNF()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
	function initWindow(eventObj) {
		getElement("clienteByIdClienteDestinatario.idCliente").required = "true";
		getElement("clienteByIdClienteDestinatario.pessoa.nrIdentificacao").required = "false";
		limpaDesabilitaCamposDadosServico();
		if(eventObj.name == "tab_click") {
			if(getElementValue("idNotaFiscalServico") == "") {
				defaultParameters();
			}
		}
	}
	
	/*
	* Variaveis para armazenar Dados Default
	*/
	var idUsuarioLogado;
	var nrIdentificacaoUsuarioLogado;
	var nmPessoaUsuarioLogado;
	
	var idFilialUsuarioLogado;
	var sgFilialUsuarioLogado;
	var nmFantasiaFilialUsuarioLogado;
	var idMunicipioFilialUsuarioLogado;
	
	var idServicoPadrao;
	var tpAbrangenciaPadrao;
	var tpModalPadrao;
	
	function defaultParameters() {
		var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.findDefaultParameters", "defaultParameters", {});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function defaultParameters_cb(dados,erro) {
		if(erro!=undefined) {
			alert(erro);
			return;
		}
		/* Usuario */
		idUsuarioLogado = getNestedBeanPropertyValue(dados, "idUsuarioLogado");
		nrIdentificacaoUsuarioLogado = getNestedBeanPropertyValue(dados, "nrIdentificacaoUsuarioLogado");
		nmPessoaUsuarioLogado = getNestedBeanPropertyValue(dados, "nmPessoaUsuarioLogado");
		/* Filial */
		idFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "idFilialUsuarioLogado");
		sgFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "sgFilialUsuarioLogado");
		nmFantasiaFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "nmFantasiaFilialUsuarioLogado");
		/* Municipio */
		idMunicipioFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "idMunicipioFilialUsuarioLogado");
		/* Servico Padrao */
		idServicoPadrao = getNestedBeanPropertyValue(dados, "idServico");
		tpAbrangenciaPadrao = getNestedBeanPropertyValue(dados, "tpAbrangencia");
		tpModalPadrao = getNestedBeanPropertyValue(dados, "tpModal");
	
		preparaNovaInclusao();
	}
	
	function myOnDataLoad_cb(dados,erro) {
		if(erro!=undefined) {
			alert(erro);
			return;
		}
		onDataLoad_cb(dados,erro);
		/* Filial */
		idFilialUsuarioLogado = getNestedBeanPropertyValue(dados, "idFilialUsuarioLogado");
		if(getElementValue("idNotaFiscalServico") != "") {
			preparaParaConsulta();
		}
		setFocus('novaNFButton', false);
	}
	
	/*
	* Verifica se Filial pode Emitir Nota Fiscal
	*/
	function verificaPossibilidadeEmissaoNotaFiscal() {
		if(idFilialUsuarioLogado == getElementValue("filial.idFilial")) {
			var idFilialEmissao = getElementValue("filial.idFilial");
			var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.verificaPossibilidadeEmissaoNotaFiscal", "verificaPossibilidadeEmissaoNotaFiscal", {idFilialEmissao:idFilialEmissao});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function verificaPossibilidadeEmissaoNotaFiscal_cb(dados,erro) {
		if(erro != undefined) {
			setDisabled(this.document, true);
			alert(erro);
			return;
		}
	}
	
	/*
	* Calcula NF de Servico e adiciona referencia a Sessao(Primeira Fase).
	*/
	function calculaNFServicoPrimeiraFase_cb(dados,erros) {
		if(erros!=undefined) {
			alert(erros);
			return;
		}
		popupCalculoNF();
	}
	
	function cancelaNF() {
		if(window.confirm(i18NLabel.getLabel("LMS-04105"))) {
			var idNotaFiscalServico = getElementValue("idNotaFiscalServico");
			var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.cancelaNF", "cancelaNF", {idNotaFiscalServico:idNotaFiscalServico});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	function cancelaNF_cb(dados,erro) {
		if(erro!=undefined) {
			alert(erro);
			return;
		}
		/* Situacao NF */
		setElementValue("tpSituacaoNf",getNestedBeanPropertyValue(dados, "tpSituacaoNf"));
		setElementValue("tpSituacaoNfLabel",getNestedBeanPropertyValue(dados, "tpSituacaoNfLabel"));
		preparaParaConsulta();
		alert(i18NLabel.getLabel("LMS-04106"));
	}
	
	function preparaNovaInclusao() {
		setDisabled(this.document,false);
		desabilitaCamposParaNovaInclusao();
		resetValue(this.document);
		/* Seta Dados Default */
		setElementValue("idUsuarioLogado",idUsuarioLogado);
		setElementValue("nrIdentificacaoUsuarioLogado",nrIdentificacaoUsuarioLogado);
		setElementValue("nmPessoaUsuarioLogado",nmPessoaUsuarioLogado);
		setElementValue("filial.idFilial",idFilialUsuarioLogado);
		setElementValue("filial.sgFilial",sgFilialUsuarioLogado);
		setElementValue("filial.pessoa.nmFantasia",nmFantasiaFilialUsuarioLogado);
		setElementValue("filial.pessoa.endereco.municipio.idMunicipio",idMunicipioFilialUsuarioLogado);
		setElementValue("servico.idServico",idServicoPadrao);
		setElementValue("servico.tpAbrangencia",tpAbrangenciaPadrao);
		setElementValue("servico.tpModal",tpModalPadrao);
		/* Verifica se Filial pode Emitir Nota Fiscal */
		verificaPossibilidadeEmissaoNotaFiscal();	
	
		setDisabled("clienteByIdClienteDestinatarioButton", false);
	
		onChangeTpSituacaoNf();
		setFocusOnFirstFocusableField(document);
	}
	
	function preparaParaConsulta() {
		/* Verifica se Filial pode Emitir Nota Fiscal */
		verificaPossibilidadeEmissaoNotaFiscal();
		onChangeTpSituacaoNf();
	
		/* Desabilita Botoes caso filial seja Diferente */
		var blHabilitar = !(idFilialUsuarioLogado == getElementValue("filial.idFilial"));
		setDisabled("reemitirNFButton", blHabilitar);
		setDisabled("clienteByIdClienteDestinatarioButton", true);
	
		var tabGroup = getTabGroup(this.document);
		tabGroup.getTab('cad').setChanged(false); 
	}
	
	function desabilitaCamposParaNovaInclusao() {
		var flag = true;
		setDisabled("filial.sgFilial",flag);
		setDisabled("filial.pessoa.nmFantasia",flag);
		setDisabled("nrNotaFiscalServico",flag);
		setDisabled("tpSituacaoNfLabel",flag);
		setDisabled("dhEmissao",flag);
		setDisabled("clienteByIdClienteDestinatario.pessoa.nmPessoa",flag);
		setDisabled("clienteByIdClienteDestinatarioButton",flag);
		setDisabled("clienteByIdClienteDestinatario.endereco.dsEndereco",flag);
		setDisabled("clienteByIdClienteDestinatario.endereco.nrEndereco",flag);
		setDisabled("clienteByIdClienteDestinatario.endereco.dsComplemento",flag);
		setDisabled("clienteByIdClienteDestinatario.endereco.nmMunicipio",flag);
		setDisabled("clienteByIdClienteDestinatario.endereco.sgUnidadeFederativa",flag);
		setDisabled("clienteByIdClienteDestinatario.endereco.nrCep",flag);
		setDisabled("nrIdentificacaoUsuarioLogado", flag);
		setDisabled("nmPessoaUsuarioLogado", flag);
		setDisabled("cotacao.idCotacao", flag);
		limpaDesabilitaCamposDadosServico();
	}
	
	function setDisabledCamposDadosServico(flag) {
		setDisabled("servicoAdicional.qtDias", flag);
		getElement("servicoAdicional.qtDias").required = "false";
		setDisabled("servicoAdicional.qtCheques",flag);
		getElement("servicoAdicional.qtCheques").required = "false";
		setDisabled("servicoAdicional.qtColetas", flag);
		getElement("servicoAdicional.qtColetas").required = "false";
		setDisabled("servicoAdicional.nrKmRodado", flag);
		getElement("servicoAdicional.nrKmRodado").required = "false";
		setDisabled("servicoAdicional.qtPaletes", flag);
		getElement("servicoAdicional.qtPaletes").required = "false";
		setDisabled("servicoAdicional.qtSegurancasAdicionais", flag);
		getElement("servicoAdicional.qtSegurancasAdicionais").required = "false";
		setDisabled("servicoAdicional.dtPrimeiroCheque", flag);
		getElement("servicoAdicional.dtPrimeiroCheque").required = "false";
		setDisabled("servicoAdicional.vlMercadoria", flag);
		getElement("servicoAdicional.vlMercadoria").required = "false";
		setDisabled("servicoAdicional.psMercadoria", flag);
		getElement("servicoAdicional.psMercadoria").required = "false";
	}
	
	function limpaDesabilitaCamposDadosServico() {
		resetValue("servicoAdicional.qtColetas");
		resetValue("servicoAdicional.qtDias");
		resetValue("servicoAdicional.qtCheques");
		resetValue("servicoAdicional.nrKmRodado");
		resetValue("servicoAdicional.qtPaletes");
		resetValue("servicoAdicional.qtSegurancasAdicionais");
		resetValue("servicoAdicional.dtPrimeiroCheque");
		resetValue("servicoAdicional.vlMercadoria");
		resetValue("servicoAdicional.psMercadoria");
	
		setDisabledCamposDadosServico(true);
		return true;
	}
	
	function onChangeTpSituacaoNf() {
		if(getElementValue("tpSituacaoNf")=="E") {
			setDisabled("reemitirNFButton", false);
		} else {
			setDisabled("reemitirNFButton", true);
		}
		return true;
	}
	
	function onChangeServicoAdicional(combo) {
		comboboxChange({e:combo});
		limpaDesabilitaCamposDadosServico();
		var cdParcelaPreco = getElementValue("servicoAdicional.parcelaPreco.cdParcelaPreco");
		if(cdParcelaPreco==="IDReembolso") {
			setDisabled("servicoAdicional.vlMercadoria", false);
			getElement("servicoAdicional.vlMercadoria").required = "true";
			setDisabled("servicoAdicional.qtCheques", false);
			getElement("servicoAdicional.qtCheques").required = "true";
			setDisabled("servicoAdicional.dtPrimeiroCheque", false);
			getElement("servicoAdicional.dtPrimeiroCheque").required = "true";
		} else if(cdParcelaPreco==="IDArmazenagem") {
			setDisabled("servicoAdicional.qtDias", false);
			getElement("servicoAdicional.qtDias").required = "true";
			setDisabled("servicoAdicional.psMercadoria", false);
			getElement("servicoAdicional.psMercadoria").required = "true";
			setDisabled("servicoAdicional.vlMercadoria", false);
			getElement("servicoAdicional.vlMercadoria").required = "true";
		} else if(cdParcelaPreco==="IDEscolta") {
			setDisabled("servicoAdicional.nrKmRodado", false);
			getElement("servicoAdicional.nrKmRodado").required = "true";
			setDisabled("servicoAdicional.qtSegurancasAdicionais", false);
			getElement("servicoAdicional.qtSegurancasAdicionais").required = "true";
		} else if(cdParcelaPreco==="IDAgendamentoColeta") {
			setDisabled("servicoAdicional.qtColetas", false);
			getElement("servicoAdicional.qtColetas").required = "true";
		} else if(cdParcelaPreco==="IDEstadiaVeiculo") {
			setDisabled("servicoAdicional.qtDias", false);
			getElement("servicoAdicional.qtDias").required = "true";
		} else if(cdParcelaPreco==="IDPaletizacao") {
			setDisabled("servicoAdicional.qtPaletes", false);
			getElement("servicoAdicional.qtPaletes").required = "true";
		} else if(cdParcelaPreco==="IDAgendamentoEntregaSDF") {
			setDisabled("servicoAdicional.psMercadoria", false);
			getElement("servicoAdicional.psMercadoria").required = "true";
		} else if(cdParcelaPreco==="IDEntregaHorarioEspecial") {
			setDisabled("servicoAdicional.psMercadoria", false);
			getElement("servicoAdicional.psMercadoria").required = "true";
		} else if (cdParcelaPreco === "IDEstadia3/4"  ){
			setDisabled("servicoAdicional.qtDias", false);
			getElement("servicoAdicional.qtDias").required = "true";
		}
		return true;
	}
	
	function onChangeTpCalculoPreco() {
		var tpCalculoPreco = getElementValue("tpCalculoPreco");
		if(tpCalculoPreco != "C") {
			setElementValue("cotacao.idCotacao","");
			setDisabled("cotacao.idCotacao",true);
		} else {
			setDisabled("cotacao.idCotacao",false);
		}
		return true;
	}
	
	
	var idIeDest;
	/*
	* Função executada quando o destinatario e modificado
	* Atualiza os campos relacionados ou limpa os mesmos de acordo com
	* o novo valor fornecido a lookup
	*/
	function onChangeDestinatario() {
		var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
		if(nrId == getElementValue("clienteByIdClienteDestinatario.nrIdentificacao")) {
			return true;
		}
		if(nrId == "") {
			setEndereco(undefined, "clienteByIdClienteDestinatario");
			resetValue("clienteByIdClienteDestinatario.idCliente"); 
			setDisabled("clienteByIdClienteDestinatario.ie.id", false); 
		}
		return clienteByIdClienteDestinatario_pessoa_nrIdentificacaoOnChangeHandler();
	}
	/*
	* Callback da busca de um cliente pelo numero de identificacao fornecido
	* Foca o botao incluir caso nao tenha sido encontrado um cliente 
	* Desabilita o botao incluir e carrega os valores de endereco caso contrario
	*/
	function destinatario_cb(data) {
		if(data == undefined || data.length == 0) {
			var nrId = getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
			resetValue("clienteByIdClienteDestinatario.idCliente");
			setElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao", nrId);
			setDisabled("clienteByIdClienteDestinatarioButton", false);
			setFocus(getElement("clienteByIdClienteDestinatarioButton"), false);
		} else {
			setDisabled("clienteByIdClienteDestinatarioButton", true);
		}
		setEndereco(data[0], "clienteByIdClienteDestinatario");
		setDisabled("clienteByIdClienteDestinatario.ie.id", false);
		resetValue("clienteByIdClienteDestinatario.idInscricaoEstadualAux");
		return clienteByIdClienteDestinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
	}

	function findDadosCliente(data) {
		var idCliente = data.idCliente;
		var nrIdentificacao = data.pessoa.nrIdentificacao;
		var sdo = createServiceDataObject("lms.expedicao.digitarPreNotaFiscalServicoAction.findDestinatario", "destinatario", {idCliente:idCliente ,pessoa:{nrIdentificacao:nrIdentificacao}});
		xmit({serviceDataObjects:[sdo]});
	}

	/*
	* Função que recebe os valores e preenche os campos de endereço da tela
	*/
	function setEndereco(dados, tipo) {
		if(dados && dados.pessoa.endereco) {
			setElementValue(tipo + ".endereco.nrCep", dados.pessoa.endereco.nrCep);
			setElementValue(tipo + ".endereco.nmMunicipio", dados.pessoa.endereco.nmMunicipio);
			setElementValue(tipo + ".endereco.idUnidadeFederativa", dados.pessoa.endereco.idUnidadeFederativa);
			setElementValue(tipo + ".endereco.sgUnidadeFederativa", dados.pessoa.endereco.sgUnidadeFederativa);
			setElementValue(tipo + ".endereco.dsComplemento", dados.pessoa.endereco.dsComplemento);
			setElementValue(tipo + ".endereco.nrEndereco", dados.pessoa.endereco.nrEndereco);
			setElementValue(tipo + ".endereco.dsEndereco", dados.pessoa.endereco.dsEndereco);
		} else {
			resetValue(tipo + ".endereco.nrCep");
			resetValue(tipo + ".endereco.nmMunicipio");
			resetValue(tipo + ".endereco.idUnidadeFederativa");
			resetValue(tipo + ".endereco.sgUnidadeFederativa");
			resetValue(tipo + ".endereco.dsComplemento");
			resetValue(tipo + ".endereco.nrEndereco");
			resetValue(tipo + ".endereco.dsEndereco");
		}
	}

	/*
	* onPopupSetValue da lookup do destinatario.
	*/
	function destinatarioPopup(data) {
		if (data == undefined){
			return;
		}
		findDadosCliente(data);
	}

	/*
	* Função que cadastra o cliente quando o usuário clica no botão incluir
	* Chama a tela cadastrarClientes
	*/
	function cadastrarCliente() {
		var data = showModalDialog('expedicao/cadastrarClientes.do?cmd=main&origem=exp',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:750px;dialogHeight:300px;');
		if(data && data != undefined) {
			findDadosCliente(data);
			//cliente_cb(data, tipo);
		}
	}

	/*
	* Função de callback da combo de Inscrição Estadual
	* Popula a combo caso exista inscrições associadas ao destinatário ou
	* desabilita a mesma se não existir dados
	*/
	function inscricaoEstadualDestinatario_cb(data) {
		clienteByIdClienteDestinatario_ie_id_cb(data);
		if(data != undefined) {
			setElementValue("clienteByIdClienteDestinatario.nrInscricoesEstaduais",data.length);
			if(data.length == 1) {
				setElementValue("clienteByIdClienteDestinatario.ie.id", data[0].idInscricaoEstadual)
				setDisabled("clienteByIdClienteDestinatario.ie.id", true);
				setFocus("municipio.nmMunicipio");
				return true;
			}
			var idIe = getElementValue("clienteByIdClienteDestinatario.idInscricaoEstadualAux");
			if(idIe) {
				setElementValue("clienteByIdClienteDestinatario.ie.id", idIe);
			} else if (idIeDest) {
				setElementValue("clienteByIdClienteDestinatario.ie.id", idIeDest);
			}
		}
		setDisabled("clienteByIdClienteDestinatario.ie.id", false);
	}

	/*
	* CallBack da Combo de Divisao
	* - Combo so sera habilitada caso Cliente Seja Especial(S) e possuir mais de 1 registro
	*/
	function findDivisaoCombo_cb(data, error) {
		var divisaoCliente = getElement("divisaoCliente.idDivisaoCliente");
		setDisabled(divisaoCliente, true);
		divisaoCliente.required = "false";

		divisaoCliente_idDivisaoCliente_cb(data);

		var tpCliente = getElementValue("clienteByIdClienteDestinatario.tpCliente");
		if(tpCliente == "S" || tpCliente == "F") {
			if(data.length < 1) {
				divisaoCliente.selectedIndex = 1;
			} else {
				setDisabled(divisaoCliente, false);
				divisaoCliente.required = "true";
			}
		}
	}

	/*
	* Reemissao Nota Fiscal de Servico
	*/
	function popupReemissaoNF() {
		var data = showModalDialog('expedicao/digitarPreNotaFiscalServicoEmitirNF.do?cmd=main&idNF=' + getElementValue("idNotaFiscalServico"),window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:330px;dialogHeight:150px;');
		if(data != undefined) {
			if(data.dhEmissao!=undefined) {
				setElementValue("dhEmissao", setFormat(getElement("dhEmissao"), data.dhEmissao));
			}
			emitirNFS(data.nfs);
		}
	}

	/*
	* Calculo p/ Emissao Nota Fiscal de Servico
	*/
	function popupCalculoNF() {
		var params = '&idServAd='+getElementValue("servicoAdicional.idServicoAdicional")+
					 '&idPP='+getElementValue("servicoAdicional.idParcelaPreco")+
					 '&dsServAd='+getElementValue("servicoAdicional.dsServicoAdicional")+
					 '&tpCalc='+getElementValue("tpCalculoPreco")+
					 '&idNF='+getElementValue("idNotaFiscalServico");
		var data = showModalDialog('expedicao/digitarPreNotaFiscalServicoCalculoPreco.do?cmd=main'+params,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:448px;dialogHeight:450px;');
		if(data != undefined) {
			if(data.tpSituacao!=undefined) {
				setElementValue("tpSituacaoNf",data.tpSituacao);
				setElementValue("tpSituacaoNfLabel",data.tpSituacaoLabel);
			}
			if(data.nrNotaFiscalServico!=undefined) {
				setElementValue("nrNotaFiscalServico",data.nrNotaFiscalServico);
			}
			if(data.dhEmissao!=undefined) {
				setElementValue("dhEmissao", setFormat(getElement("dhEmissao"), data.dhEmissao));
			}
			if(data.idNotaFiscalServico!=undefined) {
				setElementValue("idNotaFiscalServico", data.idNotaFiscalServico);
			}

			preparaNovaInclusao();
			emitirNFS(data.nfs);
		}
	}
	
	/*
	* Envia Docto para Impressora
	*/
	function emitirNFS(data) {
		if( (data == undefined) || (data == "") ) {
			alert(i18NLabel.getLabel("LMS-04100"));
			return;
		}

		var printer = window.top[0].document.getElementById("printer");
		printer.print(data);
	}
</script>