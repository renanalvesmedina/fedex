<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterEnquadramentoRegrasSGRAction">
	<adsm:form
		action="/sgr/manterEnquadramentoRegrasSGR"
		idProperty="idEnquadramentoRegra"
		height="390"
		onDataLoadCallBack="enquadramentoRegrasOnDataLoad"
	>
		<adsm:hidden property="tpFormatoRelatorio" value="pdf" />

		<adsm:textbox
			dataType="text"
			label="descricao"
			maxLength="100"
			property="dsEnquadramentoRegra"
			required="true"
			size="100"
			width="85%"
		/>

		<adsm:range label="vigencia">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:combobox
			label="moeda"
			onlyActiveValues="true"
			optionLabelProperty="siglaSimbolo"
			optionProperty="idMoeda"
			property="moeda.idMoeda"
			required="true"
			service="lms.sgr.manterEnquadramentoRegrasSGRAction.findMoeda"
		>
			<adsm:propertyMapping modelProperty="siglaSimbolo" relatedProperty="moeda.siglaSimbolo" />
		</adsm:combobox>

		<adsm:hidden property="blSeguroMercurio" value="false" />
		<adsm:hidden property="blRequerPassaporteViagem" value="false" />

		<adsm:combobox
			autoLoad="true"
			boxWidth="240"
			label="naturezaProduto"
			onlyActiveValues="true"
			optionLabelProperty="dsNaturezaProduto"
			optionProperty="idNaturezaProduto"
			property="naturezaProduto.idNaturezaProduto"
			required="false"
			service="lms.expedicao.naturezaProdutoService.findOrderByDsNaturezaProduto"
		/>
		<adsm:combobox
			boxWidth="240"
			domain="DM_TIPO_OPERACAO_ENQ_REGRA"
			label="tipoOperacao"
			onchange="tpOperacaoOnChange();"
			onlyActiveValues="true"
			property="tpOperacao"
		/>
		<adsm:combobox
			boxWidth="240"
			domain="DM_ABRANGENCIA"
			label="abrangencia"
			onlyActiveValues="true"
			property="tpAbrangencia"
		/>
		<adsm:checkbox label="regraGeral" onclick="return blRegraGeralOnClick();" property="blRegraGeral" />

		<adsm:hidden property="tpSituacao" serializable="false" value="A" />
		<adsm:hidden property="moeda.siglaSimbolo" serializable="false" />
		<adsm:hidden property="dtVigencia" serializable="false" />
		<adsm:hidden property="clienteEnquadramentosLength" serializable="false" />

		<adsm:listbox
			boxWidth="440"
			label="cliente"
			onContentChange="clienteEnquadramentosOnContentChange"
			optionProperty="idClienteEnquadramento"
			property="clienteEnquadramentos"
			size="4"
			width="85%"
		>
			<adsm:textbox dataType="text" disabled="true" property="nrIdentificacao" style="display: none;" />
			<adsm:lookup
				action="/vendas/manterDadosIdentificacao"
				criteriaProperty="pessoa.nmPessoa"
				dataType="text"
				exactMatch="false"
				idProperty="idCliente"
				maxLength="50"
				minLengthForAutoPopUpSearch="3"
				property="cliente"
				service="lms.sgr.manterEnquadramentoRegrasSGRAction.findLookup"
				size="50"
			>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
				<adsm:propertyMapping modelProperty="pessoa.nrIdentificacaoFormatado" relatedProperty="clienteEnquadramentos_nrIdentificacao" />
			</adsm:lookup>
			<adsm:combobox
				boxWidth="150"
				domain="DM_TIPO_INTEGRANTE_FRETE_ENQ_REGRA"
				onlyActiveValues="true"
				property="tpIntegranteFrete"
			/>
		</adsm:listbox>

		<adsm:lookup
			action="/seguros/manterApolicesSeguro"
			criteriaProperty="nrApolice"
			dataType="text"
			idProperty="idApoliceSeguro"
			label="apoliceSeguro"
			maxLength="60"
			onchange="return apoliceSeguroOnChange();"
			onPopupSetValue="apoliceSeguroOnPopupSetValue"
			property="apoliceSeguro"
			service="lms.seguros.manterApolicesSeguroAction.findLookup"
			size="20"
			width="85%"
		>
			<adsm:propertyMapping criteriaProperty="dtVigencia" disable="true" modelProperty="dtVigencia" />
			<adsm:propertyMapping modelProperty="seguradora.pessoa.nmPessoa" relatedProperty="apoliceSeguro.seguradora.pessoa.nmPessoa" />
			<adsm:propertyMapping blankFill="false" modelProperty="vlLimiteApolice" relatedProperty="vlLimiteApolice" />
			<adsm:propertyMapping blankFill="false" modelProperty="vlLimiteControleCarga" relatedProperty="vlLimiteControleCarga" />
			<adsm:textbox dataType="text" disabled="true" property="apoliceSeguro.seguradora.pessoa.nmPessoa" size="50" />
		</adsm:lookup>
		<adsm:lookup
			action="/vendas/manterSegurosCliente"
			criteriaProperty="dsApolice"
			dataType="text"
			idProperty="idSeguroCliente"
			label="seguroCliente"
			maxLength="60"
			onchange="return seguroClienteOnChange();"
			onPopupSetValue="seguroClienteOnPopupSetValue"
			property="seguroCliente"
			service="lms.vendas.manterSegurosClienteAction.findLookup"
			size="20"
			width="85%"
		>
			<adsm:propertyMapping criteriaProperty="dtVigencia" disable="true" modelProperty="dtVigencia" />
			<adsm:propertyMapping modelProperty="seguradora.pessoa.nmPessoa" relatedProperty="seguroCliente.seguradora.pessoa.nmPessoa" />
			<adsm:propertyMapping blankFill="false" modelProperty="vlLimite" relatedProperty="vlLimiteApolice" />
			<adsm:propertyMapping blankFill="false" modelProperty="vlLimiteControleCarga" relatedProperty="vlLimiteControleCarga" />
			<adsm:textbox dataType="text" disabled="true" property="seguroCliente.seguradora.pessoa.nmPessoa" size="50" />
		</adsm:lookup>

		<adsm:textbox
			dataType="currency"
			disabled="true"
			label="valorApolice"
			labelWidth="15%"
			mask="#,###,###,##0.00"
			property="vlLimiteApolice"
			size="15"
			width="35%"
		/>
		<adsm:textbox
			dataType="currency"
			label="limiteCarregamento"
			labelWidth="15%"
			mask="#,###,###,##0.00"
			property="vlLimiteControleCarga"
			size="15"
			width="35%"
		/>

		<adsm:combobox
			boxWidth="170"
			domain="DM_CRITEIRIO_REGIAO_SGR"
			label="criterioOrigem"
			onchange="tpCriterioOrigemOnChange();"
			onlyActiveValues="true"
			property="tpCriterioOrigem"
		/>
		<adsm:combobox
			boxWidth="170"
			domain="DM_CRITEIRIO_REGIAO_SGR"
			label="criterioDestino"
			onchange="tpCriterioDestinoOnChange(); resetCriteriosDestino();"
			onlyActiveValues="true"
			property="tpCriterioDestino"
		/>

		<adsm:listbox
			boxWidth="170"
			label="filialOrigem"
			optionLabelProperty="sgFilial"
			optionProperty="idFilialEnquadramento"
			property="filialEnquadramentosOrigem"
			serializable="true"
			showIndex="false"
			showOrderControls="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterFiliais"
				criteriaProperty="sgFilial"
				dataType="text"
				exactMatch="false"
				idProperty="idFilial"
				maxLength="3"
				minLengthForAutoPopUpSearch="3"
				property="filialOrigem"
				service="lms.municipios.filialService.findLookup"
				size="30"
			/>
		</adsm:listbox>

		<adsm:listbox
			boxWidth="170"
			label="filialDestino"
			optionLabelProperty="sgFilial"
			optionProperty="idFilialEnquadramento"
			property="filialEnquadramentosDestino"
			serializable="true"
			showOrderControls="false"
			showIndex="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterFiliais"
				service="lms.municipios.filialService.findLookup"
				property="filialDestino"
				idProperty="idFilial"
				criteriaProperty="sgFilial"
				dataType="text"
				exactMatch="false"
				minLengthForAutoPopUpSearch="3"
				size="30"
				maxLength="3"
			/>
		</adsm:listbox>

		<adsm:listbox
			boxWidth="170"
			label="municipioOrigem"
			optionLabelProperty="nmMunicipio"
			optionProperty="idMunicipioEnquadramento"
			property="municipioEnquadramentosOrigem"
			serializable="true"
			showIndex="false"
			showOrderControls="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterMunicipios"
				criteriaProperty="nmMunicipio"
				dataType="text"
				exactMatch="false"
				idProperty="idMunicipio"
				maxLength="60"
				minLengthForAutoPopUpSearch="3"
				property="municipioOrigem"
				service="lms.municipios.municipioService.findLookup"
				size="30"
			>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			</adsm:lookup>
		</adsm:listbox>

		<adsm:listbox
			boxWidth="170"
			label="municipioDestino"
			optionLabelProperty="nmMunicipio"
			optionProperty="idMunicipioEnquadramento"
			property="municipioEnquadramentosDestino"
			serializable="true"
			showOrderControls="false"
			showIndex="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterMunicipios"
				criteriaProperty="nmMunicipio"
				dataType="text"
				exactMatch="false"
				idProperty="idMunicipio"
				maxLength="60"
				minLengthForAutoPopUpSearch="3"
				property="municipioDestino"
				service="lms.municipios.municipioService.findLookup"
				size="30"
			>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			</adsm:lookup>
		</adsm:listbox>

		<adsm:listbox
			boxWidth="170"
			label="ufOrigem"
			optionLabelProperty="sgUnidadeFederativa"
			optionProperty="idUnidadeFederativaEnquadramento"
			property="unidadeFederativaEnquadramentosOrigem"
			serializable="true"
			showIndex="false"
			showOrderControls="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterUnidadesFederativas"
				criteriaProperty="sgUnidadeFederativa"
				dataType="text"
				exactMatch="false"
				idProperty="idUnidadeFederativa"
				maxLength="2"
				minLengthForAutoPopUpSearch="3"
				property="unidadeFederativaOrigem"
				service="lms.municipios.unidadeFederativaService.findLookup"
				size="30"
			>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			</adsm:lookup>
		</adsm:listbox>

		<adsm:listbox
			boxWidth="170"
			label="ufDestino"
			optionLabelProperty="sgUnidadeFederativa"
			optionProperty="idUnidadeFederativaEnquadramento"
			property="unidadeFederativaEnquadramentosDestino"
			serializable="true"
			showIndex="false"
			showOrderControls="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterUnidadesFederativas"
				criteriaProperty="sgUnidadeFederativa"
				dataType="text"
				exactMatch="false"
				idProperty="idUnidadeFederativa"
				maxLength="2"
				minLengthForAutoPopUpSearch="3"
				property="unidadeFederativaDestino"
				service="lms.municipios.unidadeFederativaService.findLookup"
				size="30"
			>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			</adsm:lookup>
		</adsm:listbox>

		<adsm:listbox
			boxWidth="170"
			label="paisOrigem"
			optionLabelProperty="nmPais"
			optionProperty="idPaisEnquadramento"
			property="paisEnquadramentosOrigem"
			serializable="true"
			showIndex="false"
			showOrderControls="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterPaises"
				criteriaProperty="nmPais"
				dataType="text"
				exactMatch="false"
				idProperty="idPais"
				maxLength="60"
				minLengthForAutoPopUpSearch="3"
				property="paisOrigem"
				service="lms.municipios.paisService.findLookup"
				size="30"
			>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			</adsm:lookup>
		</adsm:listbox>

		<adsm:listbox
			boxWidth="170"
			label="paisDestino"
			optionLabelProperty="nmPais"
			optionProperty="idPaisEnquadramento"
			property="paisEnquadramentosDestino"
			serializable="true"
			showIndex="false"
			showOrderControls="false"
			size="4"
		>
			<adsm:lookup
				action="/municipios/manterPaises"
				criteriaProperty="nmPais"
				dataType="text"
				exactMatch="false"
				idProperty="idPais"
				maxLength="60"
				minLengthForAutoPopUpSearch="3"
				property="paisDestino"
				service="lms.municipios.paisService.findLookup"
				size="30"
			>
				<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
			</adsm:lookup>
		</adsm:listbox>

		<adsm:buttonBar>
			<adsm:reportViewerButton disabled="false" id="reportButton" service="lms.sgr.emitirRelatorioEnquadramentoRegrasAction" />
			<adsm:button action="/sgr/manterProcedimentosFaixasValores" caption="faixasValores" cmd="main">
				<adsm:linkProperty disabled="true" src="idEnquadramentoRegra" target="enquadramentoRegra.idEnquadramentoRegra" />
				<adsm:linkProperty disabled="true" src="dsEnquadramentoRegra" target="enquadramentoRegra.dsEnquadramentoRegra" />
				<adsm:linkProperty disabled="true" src="moeda.idMoeda" target="enquadramentoRegra.moeda.idMoeda" />
				<adsm:linkProperty disabled="true" src="moeda.siglaSimbolo" target="enquadramentoRegra.moeda.siglaSimbolo" />
				<adsm:linkProperty disabled="true" src="clienteEnquadramentosLength" target="clienteEnquadramentosLength" />
			</adsm:button>

			<adsm:storeButton />
			<adsm:resetButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	function initWindow(event) {
		var idEnquadramentoRegra = getElementValue("idEnquadramentoRegra");
		setDisabledReportButton(!idEnquadramentoRegra);

		var idMoeda = getElementValue("moeda.idMoeda");
		if (!idMoeda) {
			updateMoedaUsuario();
			updateForm();
		}
		updateDataAtual();
	}

	function enquadramentoRegrasOnDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		updateForm();
	}

	function updateForm() {
		tpOperacaoOnChange();
		blRegraGeralOnClick();
		tpCriterioOrigemOnChange();
		tpCriterioDestinoOnChange();
	}

	function updateMoedaUsuario() {
		var sdo = createServiceDataObject("lms.sgr.manterEnquadramentoRegrasSGRAction.getMoedaUsuario", "updateMoedaUsuario");
		xmit({ serviceDataObjects : [ sdo ] });
	}

	function updateMoedaUsuario_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		if (!data.idMoeda) {
			return;
		}
		setElementValue("moeda.idMoeda", data.idMoeda);
		setElementValue("moeda.siglaSimbolo", data.siglaSimbolo);
	}

	function updateDataAtual() {
		var sdo = createServiceDataObject("lms.sgr.manterEnquadramentoRegrasSGRAction.getDataAtual", "updateDataAtual");
		xmit({ serviceDataObjects : [ sdo ] });
	}

	function updateDataAtual_cb(data, error) {
		if (error) {
			alert(error);
			return;
		}
		setElementValue("dtVigencia", data._value);
	}

	function setDisabledNaturezaProduto(disabled) {
		setDisabled("naturezaProduto.idNaturezaProduto", disabled);
		if (disabled) {
			resetValue("naturezaProduto.idNaturezaProduto");
		}
	}

	function setDisabledTpAbrangencia(disabled) {
		setDisabled("tpAbrangencia", disabled);
		if (disabled) {
			resetValue("tpAbrangencia");
		}
	}

	function tpOperacaoOnChange() {
		var tpOperacao = getElementValue("tpOperacao");
		setDisabledTpAbrangencia(tpOperacao === "C" || tpOperacao === "E");
	}

	function blRegraGeralOnClick() {
		var blRegraGeral = getElementValue("blRegraGeral");
		setDisabledNaturezaProduto(blRegraGeral);
		setDisabledClienteEnquadramentos(blRegraGeral);
		updateClienteEnquadramentosLength();
		return true;
	}

	function setDisabledClienteEnquadramentos(disabled) {
		setDisabled("clienteEnquadramentos_nrIdentificacao", disabled);
		setDisabled("clienteEnquadramentos_cliente.idCliente", disabled);
		setDisabled("clienteEnquadramentos_idClienteEnquadramento", disabled);
		setDisabled("clienteEnquadramentos_tpIntegranteFrete", disabled);
		setDisabled("clienteEnquadramentos", disabled);
		if (disabled) {
			resetValue("clienteEnquadramentos_nrIdentificacao");
			resetValue("clienteEnquadramentos_cliente.idCliente");
			resetValue("clienteEnquadramentos_idClienteEnquadramento");
			resetValue("clienteEnquadramentos_tpIntegranteFrete");
			resetValue("clienteEnquadramentos");
		}
	}

	function clienteEnquadramentosOnContentChange(event) {
		var clienteEnquadramentos = getElement("clienteEnquadramentos");
		if (event) {
			if (event.name === "modifyButton_click") {
				if (!getElementValue("clienteEnquadramentos_cliente.idCliente")) {
					alert(getMessage(erRequiredInvalid, null));
					return false;
				}

				if (!getElementValue("clienteEnquadramentos_idClienteEnquadramento")) {
					var nrIdentificacao = getElementValue("clienteEnquadramentos_nrIdentificacao");
					var options = clienteEnquadramentos.options;
					for (var i = 0; i < options.length; ++i) {
						if (nrIdentificacao == options[i].data.nrIdentificacao) {
							alert(listbox_duplicatedOption);
							return false;
						}
					}
				}
			} else if (event.name === "modifyButton_afterClick" || event.name === "deleteButton_afterClick") {
				updateClienteEnquadramentosLength();
			}
		}
		return true;
	}

	function updateClienteEnquadramentosLength() {
		var clienteEnquadramentos = getElement("clienteEnquadramentos");
		setElementValue("clienteEnquadramentosLength", clienteEnquadramentos.length);
		if (clienteEnquadramentos.length) {
			setDisabledApoliceSeguro(true);
			setDisabledSeguroCliente(false);
			seguroClienteOnChange();
		} else {
			setDisabledApoliceSeguro(false);
			setDisabledSeguroCliente(true);
			apoliceSeguroOnChange();
		}
	}

	function apoliceSeguroOnChange() {
		var result = apoliceSeguro_nrApoliceOnChangeHandler();
		var nrApolice = getElementValue("apoliceSeguro.nrApolice");
		setDisabledVlLimiteControleCarga(!nrApolice);
		return result;
	}

	function apoliceSeguroOnPopupSetValue(data) {
		setDisabledVlLimiteControleCarga(false);
	}

	function setDisabledApoliceSeguro(disabled) {
		setDisabled("apoliceSeguro.idApoliceSeguro", disabled);
		if (disabled) {
			resetValue("apoliceSeguro.idApoliceSeguro");
		}
	}

	function seguroClienteOnChange() {
		var result = seguroCliente_dsApoliceOnChangeHandler();
		var dsApolice = getElementValue("seguroCliente.dsApolice");
		setDisabledVlLimiteControleCarga(!dsApolice);
		return result;
	}

	function seguroClienteOnPopupSetValue(data) {
		setDisabledVlLimiteControleCarga(false);
	}

	function setDisabledSeguroCliente(disabled) {
		setDisabled("seguroCliente.idSeguroCliente", disabled);
		if (disabled) {
			resetValue("seguroCliente.idSeguroCliente");
		}
	}

	function setDisabledVlLimiteControleCarga(disabled) {
		setDisabled("vlLimiteControleCarga", disabled);
		if (disabled) {
			resetValue("vlLimiteApolice");
			resetValue("vlLimiteControleCarga");
		}
	}

	function tpCriterioOrigemOnChange(event) {
		var tpCriterioOrigem = getElementValue("tpCriterioOrigem");
		setDisabledFilialEnquadramentosOrigem(tpCriterioOrigem !== "F");
		setDisabledMunicipioEnquadramentosOrigem(tpCriterioOrigem !== "M");
		setDisabledUnidadeFederativaEnquadramentosOrigem(tpCriterioOrigem !== "U");
		setDisabledPaisEnquadramentosOrigem(tpCriterioOrigem !== "P");
	}

	function setDisabledFilialEnquadramentosOrigem(disabled) {
		setDisabled("filialEnquadramentosOrigem_filialOrigem.idFilial", disabled);
		setDisabled("filialEnquadramentosOrigem_filialOrigem.sgFilial", disabled);
		setDisabled("filialEnquadramentosOrigem_idFilialEnquadramento", disabled);
		setDisabled("filialEnquadramentosOrigem", disabled);
		if (disabled) {
			resetValue("filialEnquadramentosOrigem_filialOrigem.idFilial");
			resetValue("filialEnquadramentosOrigem_filialOrigem.sgFilial");
			resetValue("filialEnquadramentosOrigem_idFilialEnquadramento");
			resetValue("filialEnquadramentosOrigem");
		}
		getElement("filialEnquadramentosOrigem").required = String(!disabled);
	}

	function setDisabledMunicipioEnquadramentosOrigem(disabled) {
		setDisabled("municipioEnquadramentosOrigem_municipioOrigem.idMunicipio", disabled);
		setDisabled("municipioEnquadramentosOrigem_municipioOrigem.nmMunicipio", disabled);
		setDisabled("municipioEnquadramentosOrigem_idMunicipioEnquadramento", disabled);
		setDisabled("municipioEnquadramentosOrigem", disabled);
		if (disabled) {
			resetValue("municipioEnquadramentosOrigem_municipioOrigem.idMunicipio");
			resetValue("municipioEnquadramentosOrigem_municipioOrigem.nmMunicipio");
			resetValue("municipioEnquadramentosOrigem_idMunicipioEnquadramento");
			resetValue("municipioEnquadramentosOrigem");
		}
		getElement("municipioEnquadramentosOrigem").required = String(!disabled);
	}

	function setDisabledUnidadeFederativaEnquadramentosOrigem(disabled) {
		setDisabled("unidadeFederativaEnquadramentosOrigem_unidadeFederativaOrigem.idUnidadeFederativa", disabled);
		setDisabled("unidadeFederativaEnquadramentosOrigem_unidadeFederativaOrigem.sgUnidadeFederativa", disabled);
		setDisabled("unidadeFederativaEnquadramentosOrigem_idUnidadeFederativaEnquadramento", disabled);
		setDisabled("unidadeFederativaEnquadramentosOrigem", disabled);
		if (disabled) {
			resetValue("unidadeFederativaEnquadramentosOrigem_unidadeFederativaOrigem.idUnidadeFederativa");
			resetValue("unidadeFederativaEnquadramentosOrigem_unidadeFederativaOrigem.sgUnidadeFederativa");
			resetValue("unidadeFederativaEnquadramentosOrigem_idUnidadeFederativaEnquadramento");
			resetValue("unidadeFederativaEnquadramentosOrigem");
		}
		getElement("unidadeFederativaEnquadramentosOrigem").required = String(!disabled);
	}

	function setDisabledPaisEnquadramentosOrigem(disabled) {
		setDisabled("paisEnquadramentosOrigem_paisOrigem.idPais", disabled);
		setDisabled("paisEnquadramentosOrigem_paisOrigem.nmPais", disabled);
		setDisabled("paisEnquadramentosOrigem_idPaisEnquadramento", disabled);
		setDisabled("paisEnquadramentosOrigem", disabled);
		if (disabled) {
			resetValue("paisEnquadramentosOrigem_paisOrigem.idPais");
			resetValue("paisEnquadramentosOrigem_paisOrigem.nmPais");
			resetValue("paisEnquadramentosOrigem_idPaisEnquadramento");
			resetValue("paisEnquadramentosOrigem");
		}
		getElement("paisEnquadramentosOrigem").required = String(!disabled);
	}

	function tpCriterioDestinoOnChange(event) {
		var tpCriterioDestino = getElementValue("tpCriterioDestino");
		setDisabledFilialEnquadramentosDestino(tpCriterioDestino !== "F");
		setDisabledMunicipioEnquadramentosDestino(tpCriterioDestino !== "M");
		setDisabledUnidadeFederativaEnquadramentosDestino(tpCriterioDestino !== "U");
		setDisabledPaisEnquadramentosDestino(tpCriterioDestino !== "P");
	}

	function setDisabledFilialEnquadramentosDestino(disabled) {
		setDisabled("filialEnquadramentosDestino_filialDestino.idFilial", disabled);
		setDisabled("filialEnquadramentosDestino_filialDestino.sgFilial", disabled);
		setDisabled("filialEnquadramentosDestino_idFilialEnquadramento", disabled);
		setDisabled("filialEnquadramentosDestino", disabled);
		if (disabled) {
			resetValue("filialEnquadramentosDestino_filialDestino.idFilial");
			resetValue("filialEnquadramentosDestino_filialDestino.sgFilial");
			resetValue("filialEnquadramentosDestino_idFilialEnquadramento");
			resetValue("filialEnquadramentosDestino");
		}
		getElement("filialEnquadramentosDestino").required = String(!disabled);
	}

	function setDisabledMunicipioEnquadramentosDestino(disabled) {
		setDisabled("municipioEnquadramentosDestino_municipioDestino.idMunicipio", disabled);
		setDisabled("municipioEnquadramentosDestino_municipioDestino.nmMunicipio", disabled);
		setDisabled("municipioEnquadramentosDestino_idMunicipioEnquadramento", disabled);
		setDisabled("municipioEnquadramentosDestino", disabled);
		if (disabled) {
			resetValue("municipioEnquadramentosDestino_municipioDestino.idMunicipio");
			resetValue("municipioEnquadramentosDestino_municipioDestino.nmMunicipio");
			resetValue("municipioEnquadramentosDestino_idMunicipioEnquadramento");
			resetValue("municipioEnquadramentosDestino");
		}
		getElement("municipioEnquadramentosDestino").required = String(!disabled);
	}

	function setDisabledUnidadeFederativaEnquadramentosDestino(disabled) {
		setDisabled("unidadeFederativaEnquadramentosDestino_unidadeFederativaDestino.idUnidadeFederativa", disabled);
		setDisabled("unidadeFederativaEnquadramentosDestino_unidadeFederativaDestino.sgUnidadeFederativa", disabled);
		setDisabled("unidadeFederativaEnquadramentosDestino_idUnidadeFederativaEnquadramento", disabled);
		setDisabled("unidadeFederativaEnquadramentosDestino", disabled);
		if (disabled) {
			resetValue("unidadeFederativaEnquadramentosDestino_unidadeFederativaDestino.idUnidadeFederativa");
			resetValue("unidadeFederativaEnquadramentosDestino_unidadeFederativaDestino.sgUnidadeFederativa");
			resetValue("unidadeFederativaEnquadramentosDestino_idUnidadeFederativaEnquadramento");
			resetValue("unidadeFederativaEnquadramentosDestino");
		}
		getElement("unidadeFederativaEnquadramentosDestino").required = String(!disabled);
	}

	function setDisabledPaisEnquadramentosDestino(disabled) {
		setDisabled("paisEnquadramentosDestino_paisDestino.idPais", disabled);
		setDisabled("paisEnquadramentosDestino_paisDestino.nmPais", disabled);
		setDisabled("paisEnquadramentosDestino_idPaisEnquadramento", disabled);
		setDisabled("paisEnquadramentosDestino", disabled);
		if (disabled) {
			resetValue("paisEnquadramentosDestino_paisDestino.idPais");
			resetValue("paisEnquadramentosDestino_paisDestino.nmPais");
			resetValue("paisEnquadramentosDestino_idPaisEnquadramento");
			resetValue("paisEnquadramentosDestino");
		}
		getElement("paisEnquadramentosDestino").required = String(!disabled);
	}

	function setDisabledReportButton(disabled) {
		setDisabled("reportButton", disabled);
	}
</script>
