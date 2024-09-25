<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.coleta.cadastrarPedidoColetaAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00061" />
		<adsm:include key="LMS-02112" />
		<adsm:include key="LMS-02113" />
		<adsm:include key="LMS-02128" />
	</adsm:i18nLabels>
	<adsm:form action="/coleta/cadastrarPedidoColeta" idProperty="idDetalheColeta" height="240"
		service="lms.coleta.cadastrarPedidoColetaAction.findByIdDetalheColeta" onDataLoadCallBack="habilitaCamposGridClick"
	>
		<adsm:masterLink showSaveAll="true" idProperty="idPedidoColeta">
			<adsm:masterLinkItem label="numeroColeta" property="siglaNumeroColeta" itemWidth="25" />
			<adsm:masterLinkItem label="qtdCtes" property="qtdeTotalDocumentos" itemWidth="30" />
			<adsm:masterLinkItem label="peso" property="vlPesoTotal" itemWidth="20" />
			<adsm:masterLinkItem label="valorTotal" property="valorTotalDocumentos" itemWidth="25" />
		</adsm:masterLink>
		<adsm:hidden property="origem" />
		<adsm:hidden property="idMoedaCotacao" serializable="false" />
		<adsm:hidden property="cotacao.idCotacao" />
		<adsm:hidden property="pessoa.endereco.municipio.idMunicipio" />
		<adsm:hidden property="doctoAwb" />
		<adsm:hidden property="inclusaoParcial" />
		<!-- Dados para Pop-up de Liberar Coleta para Destino Bloqueado -->
		<adsm:hidden property="eventoColeta.usuario.idUsuario" />
		<adsm:hidden property="eventoColeta.ocorrenciaColeta.idOcorrenciaColeta" />
		<adsm:hidden property="eventoColeta.dsDescricao" />
		<adsm:hidden property="buttonDestinoBloqueado" value="false" />
		<adsm:hidden property="tpPedidoColeta" />
		<adsm:hidden property="servico.dsServico" />
		<adsm:combobox property="servico.idServico" optionProperty="idServico" optionLabelProperty="dsServico" label="servico" labelWidth="18%" width="32%"
			onlyActiveValues="true" boxWidth="230" required="true" onchange="return habilitaCampos()"
		/>
		<adsm:hidden property="naturezaProduto.dsNaturezaProduto" />
		<adsm:combobox label="naturezaProduto" property="naturezaProduto.idNaturezaProduto" optionProperty="idNaturezaProduto"
			optionLabelProperty="dsNaturezaProduto" service="lms.coleta.cadastrarPedidoColetaAction.findNaturezaProduto" labelWidth="18%" width="32%"
			onlyActiveValues="true" required="true"
		>
			<adsm:propertyMapping relatedProperty="naturezaProduto.dsNaturezaProduto" modelProperty="dsNaturezaProduto" />
		</adsm:combobox>
		<adsm:combobox label="tipoFrete" property="tpFrete" domain="DM_TIPO_FRETE" required="true" labelWidth="18%" width="32%"
			onchange="return tpFrete_onChange()" defaultValue="C" renderOptions="true"
		/>
		<adsm:combobox label="destino" property="destino" domain="DM_MUNICIPIO_LOCALIDADE_ESPECIAL" labelWidth="18%" width="32%" required="true"
			onchange="return habilitaMunicipioOuLocalidade()" defaultValue="M" renderOptions="true"
		/>
		<adsm:hidden property="municipio.idMunicipio" serializable="true" />
		<adsm:hidden property="vigentes" value="S" />
		<adsm:lookup label="municipioDestino" idProperty="idMunicipioFilial" property="municipioFilial" criteriaProperty="municipio.nmMunicipio"
			action="/municipios/manterMunicipiosAtendidos" exactMatch="false" minLengthForAutoPopUpSearch="3" maxLength="60"
			service="lms.coleta.cadastrarPedidoColetaAction.findLookupMunicipio" dataType="text" size="35" labelWidth="18%" width="32%"
			onDataLoadCallBack="municipioFilialOnDataLoadCallBack" onPopupSetValue="municipioFilialOnPopupSetValue"
			onchange="return limpaCampoMunicipio(this.value)"
		>
			<adsm:propertyMapping criteriaProperty="vigentes" modelProperty="vigentes" />
			<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio" />
			<adsm:propertyMapping relatedProperty="sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
		</adsm:lookup>
		<adsm:hidden property="localidadeEspecial.tpSituacao" value="A" />
		<adsm:lookup label="localidadeEspecial" idProperty="idLocalidadeEspecial" property="localidadeEspecial" criteriaProperty="dsLocalidade"
			maxLength="60" action="/coleta/manterLocalidadesEspeciais" service="lms.coleta.cadastrarPedidoColetaAction.findLookupLocalidadeEspecial"
			exactMatch="false" minLengthForAutoPopUpSearch="3" dataType="text" size="35" width="32%" labelWidth="18%"
		>
			<adsm:propertyMapping criteriaProperty="localidadeEspecial.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="filial.idFilial" relatedProperty="filial.idFilial" />
			<adsm:propertyMapping modelProperty="filial.sgFilial" relatedProperty="filial.sgFilial" />
			<adsm:propertyMapping modelProperty="filial.pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia" />
		</adsm:lookup>
		<adsm:textbox label="filialDestino" property="filial.sgFilial" dataType="text" size="3" maxLength="3" labelWidth="18%" width="32%" disabled="true">
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="35" disabled="true" />
			<adsm:hidden property="filial.idFilial" />
		</adsm:textbox>
		<adsm:textbox label="uf" property="sgUnidadeFederativa" dataType="text" size="3" disabled="true" labelWidth="18%" width="12%" />
		<adsm:lookup label="destinatario" idProperty="idCliente" property="cliente" criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" service="lms.coleta.cadastrarPedidoColetaAction.findLookupCliente"
			action="/coleta/cadastrarPedidoColeta" cmd="consultarClientes" onPopupSetValue="destinatarioLookupPopUp" onDataLoadCallBack="destinatarioLookup"
			dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%"
		>
			<adsm:propertyMapping relatedProperty="nmDestinatario" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="pessoa.endereco.municipio.idMunicipio" modelProperty="pessoa.endereco.municipio.idMunicipio" />
			<adsm:textbox dataType="text" property="nmDestinatario" size="50" maxLength="50" serializable="true" />
		</adsm:lookup>
		<adsm:combobox label="valor" property="moeda.idMoeda" optionProperty="idMoeda" optionLabelProperty="siglaSimbolo"
			service="lms.coleta.cadastrarPedidoColetaAction.findMoeda" width="12%" labelWidth="18%" boxWidth="85"
		>
			<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="siglaSimbolo" />
		</adsm:combobox>
		<adsm:hidden property="moeda.siglaSimbolo" />
		<adsm:textbox property="vlMercadoria" dataType="currency" mask="###,###,###,###,##0.00" size="16" maxLength="18" width="20%" required="true" />
		<adsm:textbox label="volumes" property="qtVolumes" dataType="integer" required="true" size="6" maxLength="6" onchange="return desmarcaAforado()"
			labelWidth="18%" width="32%"
		/>
		<adsm:textbox label="peso" property="psMercadoria" dataType="weight" required="true" unit="kg" labelWidth="18%" width="20%" size="16" maxLength="16"
			onchange="return desmarcaAforado()"
		/>
		<adsm:checkbox label="aforado" property="aforado" labelWidth="6%" width="6%" onclick="rotinaCalculoPeso()" />
		<adsm:textbox label="pesoAforado" property="psAforado" dataType="weight" unit="kg" size="16" maxLength="16" labelWidth="18%" width="18%" />
		<adsm:checkbox label="entregaDireta" property="blEntregaDireta" labelWidth="18%" width="32%" />
		<adsm:combobox property="doctoServico.tpDocumentoServico" service="lms.coleta.cadastrarPedidoColetaAction.findTpDoctoServico" optionProperty="value"
			optionLabelProperty="description" label="documentoServico" labelWidth="18%" width="32%"
			onchange="onChangeTpDocumentoServico();return changeDocumentWidgetType({
					   		documentTypeElement:this, 
					   		filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					   		documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					   		documentGroup:'SERVICE',
					   		actionService:'lms.coleta.cadastrarPedidoColetaAction'
					   	});"
			onchangeAfterValueChanged="true"
		>
			<adsm:lookup dataType="text" property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" service="" action=""
				size="3" maxLength="3" picker="false" disabled="true" serializable="false"
				onchange="changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						  }); return limparDadosAwb(this.value); "
			>
			</adsm:lookup>
			<adsm:lookup dataType="integer" property="doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" criteriaSerializable="true"
				service="" action="" mask="00000000" afterPopupSetValue="onDoctoServicoAfterPopupSetValue" onDataLoadCallBack="onDoctoServicoDataCallback"
				onchange="return doctoServicoNrDoctoServico_OnChange();" size="10" serializable="true" disabled="true"
			/>
		</adsm:combobox>
		<adsm:combobox label="preAwbAwb" property="tpStatusAwb" domain="DM_LOOKUP_AWB" labelWidth="18%" width="32%" defaultValue="E" renderOptions="true">
			<adsm:lookup property="ciaFilialMercurio.empresa" idProperty="idEmpresa" dataType="text" criteriaProperty="sgEmpresa" criteriaSerializable="true"
				service="lms.coleta.cadastrarPedidoColetaAction.findLookupSgCiaAerea" action="" size="3" maxLength="3" picker="false"
			>
			</adsm:lookup>
			<adsm:lookup dataType="integer" size="13" maxLength="11" property="awb" idProperty="idAwb" criteriaProperty="nrAwb" criteriaSerializable="true"
				service="lms.coleta.cadastrarPedidoColetaAction.findLookupAwb" action="expedicao/consultarAWBs" onchange="return limparDadosAwb(this.value)"
				onDataLoadCallBack="awbOnDataLoadCallBack" onPopupSetValue="findAwb_cb"
			>
				<adsm:propertyMapping modelProperty="tpStatusAwb" criteriaProperty="tpStatusAwb" disable="true" />
				<adsm:propertyMapping modelProperty="ciaFilialMercurio.empresa.idEmpresa" criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" disable="true" />
			</adsm:lookup>
		</adsm:combobox>
		<adsm:textbox label="numeroCRT" property="ctoInternacional.sgPais" dataType="text" size="1" maxLength="2" labelWidth="18%" width="32%"
			onchange="return sgPaisChange(this);" serializable="false" disabled="true"
		>
			<adsm:lookup property="ctoInternacional" idProperty="idDoctoServico" dataType="integer" criteriaProperty="nrCrt" criteriaSerializable="true"
				popupLabel="pesquisarNumeroCRT" service="lms.coleta.cadastrarPedidoColetaAction.findLookupCRT" action="/expedicao/manterCRT" cmd="list"
				onPopupSetValue="findLookupCRT" size="6" maxLength="6" mask="000000" disabled="true"
			>
				<adsm:propertyMapping criteriaProperty="ctoInternacional.sgPais" modelProperty="sgPais" />
			</adsm:lookup>
		</adsm:textbox>
		<adsm:listbox label="notaFiscal" property="notaFiscalColetas" onContentChange="contentChangeNf" optionProperty="idNotaFiscalColeta"
			optionLabelProperty="nrNotaFiscal" size="9" boxWidth="120" labelWidth="18%" width="32%"
		>
			<adsm:textbox property="nrNotaFiscal" dataType="integer" maxLength="9" serializable="false" />
		</adsm:listbox>
		<adsm:listbox label="chaveNfe" property="nrChaveNfe" onContentChange="contentChange" optionProperty="idNotaFiscalColeta"
			optionLabelProperty="nrChave" size="9" boxWidth="280" labelWidth="18%" width="50%" labelStyle="vertical-align:top"
		>
			<adsm:textbox property="nrChave" dataType="integer" maxLength="44" serializable="false" size="50" onchange="return validateChaveNfe()" />
		</adsm:listbox>
		<adsm:textarea label="observacao" property="obDetalheColeta" maxLength="100" columns="92" rows="2" labelWidth="18%" width="82%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="salvarDetalhe" id="salvarDetalheButton" onclick="salvarDetalhe_onClick()" />
			<adsm:button caption="limpar" id="newButton" onclick="novoRegistro();" />
		</adsm:buttonBar>
		<script>
			var lms_02025 = 'LMS-02025 - <adsm:label key="LMS-02025"/>';
			var lms_02027 = 'LMS-02027 - <adsm:label key="LMS-02027"/>';
			var lms_02077 = 'LMS-02077 - <adsm:label key="LMS-02077"/>';
			var lms_02091 = 'LMS-02091 - <adsm:label key="LMS-02091"/>';
			var lms_04400 = 'LMS-04400 - <adsm:label key="LMS-04400"/>';
			var lms_02103 = 'LMS-02103 - <adsm:label key="LMS-02103"/>';
			var lms_04508 = 'LMS-04508 - <adsm:label key="LMS-04508"/>';
			var lms_02128 = 'LMS-02128 - <adsm:label key="LMS-02128"/>';

			var label_valor = '<adsm:label key="valor"/>';
			var label_moeda = '<adsm:label key="moeda"/>';
		</script>
	</adsm:form>
	<adsm:grid property="detalheColeta" idProperty="idDetalheColeta" detailFrameName="detalheColeta" onRowClick="onRowClick" autoSearch="false" rows="2"
		selectionMode="check" defaultOrder="servico_.idServico:asc" onDataLoadCallBack="gridOnDataLoadCallBack" showPagging="true" showGotoBox="true"
		gridHeight="40" unique="true" scrollBars="horizontal" service="lms.coleta.cadastrarPedidoColetaAction.findPaginatedDetalheColeta"
		rowCountService="lms.coleta.cadastrarPedidoColetaAction.getRowCountDetalheColeta"
	>
		<adsm:gridColumn title="servico" property="servico.dsServico" width="200" />
		<adsm:gridColumn title="naturezaProduto" property="naturezaProduto.dsNaturezaProduto" width="170" />
		<adsm:gridColumn title="frete" property="tpFrete" isDomain="true" width="50" />
		<adsm:gridColumn title="volumes" property="qtVolumes" width="80" align="right" />
		<adsm:gridColumn title="peso" property="psMercadoria" dataType="decimal" mask="###,###,###,###,##0.000" align="right" unit="kg" width="105" />
		<adsm:gridColumn title="pesoAforado" property="psAforado" dataType="decimal" mask="###,###,###,###,##0.000" align="right" unit="kg" width="110" />
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="valor" property="moeda.sgMoeda" width="30" />
			<adsm:gridColumn title="" property="moeda.dsSimbolo" width="30" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" property="vlMercadoria" dataType="decimal" mask="#,###,###,###,###,##0.00" width="100" align="right" />
		<adsm:gridColumn title="notaFiscal" property="notaFiscal" image="/images/popup.gif" align="center"
			link="/coleta/cadastrarPedidoColeta.do?cmd=listaNota" linkIdProperty="idDetalheColeta" popupDimension="380,240" width="80"
		/>
		<adsm:gridColumn title="preAwbAwb" property="awb" width="140" align="left" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="documentoServico" property="doctoServico.tpDoctoSgFilial" width="65" />
			<adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="entregaDireta" property="blEntregaDireta" width="100" renderMode="image-check" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="crt" property="ctoInternacional.sgPais" width="30" />
			<adsm:gridColumn title="" property="ctoInternacional.nrCrt" width="70" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="destinatario" property="nmDestinatario" width="300" />
		<adsm:gridColumn title="municipioDestino" property="municipio.nmMunicipio" width="130" />
		<adsm:gridColumn title="localidadeEspecial" property="localidadeEspecial.dsLocalidade" width="200" />
		<adsm:gridColumn title="filialDestino" property="filial.sgFilial" width="105" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirDetalhe" service="lms.coleta.cadastrarPedidoColetaAction.removeByIdsDetalheColeta" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	//Os serializable abaixo poderiam estar dentro das próprias tags.
	document.getElementById("servico.dsServico").serializable = true;
	document.getElementById("naturezaProduto.dsNaturezaProduto").serializable = true;
	document.getElementById("moeda.siglaSimbolo").serializable = true;
	document.getElementById("nmDestinatario").serializable = true;
	document.getElementById("municipioFilial.municipio.nmMunicipio").serializable = true;
	document.getElementById("localidadeEspecial.dsLocalidade").serializable = true;
	carregaDadosFilial();

	// Pega a aba 'cad' para pegar os valores dos properties
	var tabGroup = getTabGroup(this.document);
	var tabDet = tabGroup.getTab("pedidoColeta");

	function initWindow(eventObj) {
		setElementValue("cotacao.idCotacao", tabDet
				.getFormProperty("cotacao.idCotacao"));
		setElementValue("origem", tabDet.getFormProperty("origem"));
		setElementValue("tpPedidoColeta", tabDet
				.getFormProperty("tpPedidoColeta"));

		var idPedidoColeta = tabDet.getFormProperty("idPedidoColeta");
		if (idPedidoColeta != null && idPedidoColeta != "") {
			setDisabledDocument(this.document, true);
		} else {
			if (getElementValue("ctoInternacional.idDoctoServico") == "") {
				setDisabled("ctoInternacional.sgPais", true);
				setDisabled("ctoInternacional.idDoctoServico", true);
			}
			carregaDados();
		}
		/** COTACAO */
		if (getElementValue("idDetalheColeta") == ""
				&& getElementValue("origem") == "cotacao") {
			setDisabled("newButton", true);
		}
		if ((eventObj.name == "storeItemButton" || detalheColetaGridDef.gridState.rowCount > 0)
				&& getElementValue("cotacao.idCotacao") != "") {

			setDisabled("salvarDetalheButton", true);
			detalheColetaGridDef.disabled = true;
		}

		/** PRE-ALERTA */
		if (getElementValue("origem") == "prealerta"
				&& detalheColetaGridDef.gridState.rowCount > 0) {
			setDisabled("newButton", true);
			setDisabled("salvarDetalheButton", true);
			detalheColetaGridDef.disabled = true;
		}

		findComboServicos();
	}

	function findComboServicos() {
		var filter = {
			tpSituacao : "A"
		};

		if (getElementValue("tpPedidoColeta") == "AE") {
			filter.tpModal = "A";
		}

		var sdo = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.findServico",
				"findComboServicos", filter);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	function findComboServicos_cb(data, error) {

		if (error != undefined) {
			alert(error);
		}

		servico_idServico_cb(data);

		exibirOcultarObjetos();
	}

	function exibirOcultarObjetos() {
		var tpPedidoColeta = getElementValue("tpPedidoColeta");

		if (tpPedidoColeta != 'AE') {
			fieldsAereo(true);
			filedsDiferenteAereo(false);
			setDisabled("blEntregaDireta", true);
			setElementValue("servico.idServico", tabDet
					.getFormProperty("idServicoDefault"));
		} else {
			fieldsAereo(false);
			filedsDiferenteAereo(true);
			setDisabled("blEntregaDireta", false);
			setElementValue("servico.idServico", tabDet
					.getFormProperty("idServicoAereo"));
		}
		setDisabled("naturezaProduto.idNaturezaProduto", false);
		document.getElementById("naturezaProduto.idNaturezaProduto").required = "true";
	}

	function fieldsAereo(disabled) {
		setDisabled("awb.idAwb", disabled);
		setDisabled("ciaFilialMercurio.empresa.idEmpresa", disabled);
		setDisabled("ciaFilialMercurio.empresa.sgEmpresa", disabled);
		setDisabled("tpStatusAwb", disabled);
		setDisabled("doctoServico.tpDocumentoServico", disabled);
		setDisabled("doctoServico.filialByIdFilialOrigem.sgFilial", disabled);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", disabled);
		setDisabled("doctoServico.idDoctoServico", disabled);
	}

	function filedsDiferenteAereo(disabled) {
		setDisabled("cliente.idCliente", disabled);
		setDisabled("nmDestinatario", disabled);
		setDisabled("moeda.idMoeda", disabled);
		setDisabled("vlMercadoria", disabled);
		setDisabled("qtVolumes", disabled);
		setDisabled("psMercadoria", disabled);
		setDisabled("aforado", disabled);
		setDisabled("psAforado", disabled);
		setDisabled("notaFiscalColetas", disabled);
		setDisabled("notaFiscalColetas_nrNotaFiscal", disabled);
		setDisabled("nrChaveNfe", disabled);
		setDisabled("nrChaveNfe_nrChave", disabled);

		getElement("vlMercadoria").required = (!disabled);
		getElement("qtVolumes").required = (!disabled);
		getElement("psMercadoria").required = (!disabled);
	}

	/**
	 * Função de callBack da gri de detalhe de coleta.
	 */
	function gridOnDataLoadCallBack_cb(data, error) {
		populaSomatorios();
		if (detalheColetaGridDef.gridState.rowCount == 0) {
			setDisabled("salvarDetalheButton", false);
			detalheColetaGridDef.disabled = false;
		}
	}

	function populaSomatorios() {
		var remoteCall = {
			serviceDataObjects : new Array()
		};
		var dataCall = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.findSomatorios",
				"populaSomatorios", {
					idColeta : getElementValue("masterId"),
					tpColeta : tabDet.getFormProperty("tpPedidoColeta")
				});
		remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);

	}

	function populaSomatorios_cb(data, error, erromsg) {
		if (error != undefined) {
			alert(error + '');
			return;
		}

		var telaCad = tabDet.tabOwnerFrame;

		setElementValue(telaCad.document.getElementById("qtdeTotalDocumentos"),
				data.qtdeTotalDocumentos);
		setElementValue(telaCad.document.getElementById("vlPesoTotal"),
				setFormat(telaCad.document.getElementById("psTotalInformado"),
						data.vlPesoTotal));
		setElementValue(
				telaCad.document.getElementById("valorTotalDocumentos"),
				setFormat(telaCad.document.getElementById("vlTotalInformado"),
						data.valorTotalDocumentos));

		tabGroup.selectTab("pedidoColeta");
		tabGroup.selectTab("detalheColeta");

	}

	/**
	 * Carrega dados na tela
	 */
	function carregaDados() {
		if (getElementValue("idDetalheColeta") == ""
				&& getElementValue("origem") != "cotacao") {
			setDisabled("newButton", false);
			setDisabled("salvarDetalheButton", false);
			setElementValue("servico.idServico", tabDet
					.getFormProperty("idServicoDefault"));
			setDisabled("servico.idServico", false);
			setDisabled("naturezaProduto.idNaturezaProduto", false);
			setDisabled("tpFrete", false);
			setDisabled("destino", false);
			setDisabled("municipioFilial.idMunicipioFilial", false);
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);

			var tpPedidoColeta = getElementValue("tpPedidoColeta");

			if (tpPedidoColeta != 'AE') {
				setDisabled("cliente.idCliente", false);
				setDisabled("nmDestinatario", false);

				setDisabled("moeda.idMoeda", false);
				setDisabled("vlMercadoria", false);
				setDisabled("qtVolumes", false);
				setDisabled("psMercadoria", false);
				setDisabled("aforado", false);
				setDisabled("naturezaProduto.idNaturezaProduto", false);
			}

			setDisabled("notaFiscalColetas", false);
			setDisabled("notaFiscalColetas_nrNotaFiscal", false);
			setDisabled("obDetalheColeta", false);
		}

		buscarDadosUsuarioSessao();

		document.getElementById("municipioFilial.municipio.nmMunicipio").required = "true";

		var tipoColeta = tabDet.getFormProperty("tpPedidoColeta");
		if (tipoColeta == "DE") {
			document.getElementById("nmDestinatario").required = "true";
		}

		// Seta o label para o campo valor de mercadoria
		document.getElementById("vlMercadoria").label = label_valor;
		document.getElementById("moeda.idMoeda").label = label_moeda;
		document.getElementById("moeda.idMoeda").required = "true";

		// Seta o label para o campo nmDestinatario
		document.getElementById("nmDestinatario").label = document
				.getElementById("cliente.idCliente").label;
	}

	/**
	 * Retorno do Salvamento do registro de Detalhe Coleta
	 */
	function salvarDetalhe_cb(data, erros, errorMsg, eventObj) {
		storeItem_cb(data, erros, errorMsg, eventObj);

		if (getElementValue("origem") == 'prealerta') {
			setDisabled("salvarDetalheButton", true);
			setDisabled("newButton", true);
		} else {
			setDisabled("salvarDetalheButton", false);
			setDisabled("newButton", false);
		}
	}

	function destinatarioLookupPopUp(data) {
		validatePCE(data.idCliente);
		if (data.idCliente.length > 0) {
			if (getElementValue("tpFrete") == "F") {
				validaBloqueioDestinatario(data.idCliente);
			}
		}
	}

	function destinatarioLookup_cb(data, error) {
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (data[0] && data[0].idCliente) {
			validatePCE(data[0].idCliente);
		}
		if (getElementValue("cliente.pessoa.nrIdentificacao").length > 0) {
			if (getElementValue("tpFrete") == "F") {
				validaBloqueioDestinatario()
			}
		}
	}

	//#####################################################
	// Inicio da validacao do pce
	//#####################################################

	/**
	 * Faz a validacao da tela
	 */
	function validatePCE(idCliente) {
		var tpPedidoColeta = tabDet.getFormProperty("tpPedidoColeta");

		if (tpPedidoColeta == "DE") {
			var data = new Object();
			data.idCliente = idCliente;
			data.tpPedidoColeta = tpPedidoColeta;
			var sdo = createServiceDataObject(
					"lms.coleta.cadastrarPedidoColetaAction.validatePCEDetalheColeta",
					"validatePCE", data);
			xmit({
				serviceDataObjects : [ sdo ]
			});
		}
	}

	/**
	 * Callback da chamada de validacao do PCE, chama a popUp de alert com os dados do
	 * PCE caso necessario.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		if (data._exception != undefined) {
			alert(error);
			return false;
		}

		if (data.codigo != undefined) {
			showModalDialog(
					'vendas/alertaPce.do?idVersaoDescritivoPce=' + data.codigo
							+ '&cmd=pop',
					window,
					'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
		}
	}

	/**
	 * Este callback existe decorrente de uma necessidade da popUp de alert.
	 */
	function alertPCE_cb() {
		//Vazio
	}

	//#####################################################
	// Fim da validacao do pce
	//#####################################################

	function validaBloqueioDestinatario(idCliente) {
		var data = new Object();
		if (idCliente == undefined) {
			data.idCliente = getElementValue("cliente.idCliente");
		} else {
			data.idCliente = idCliente;
		}
		var sdo = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.validaBloqueioDestinatario",
				"validaBloqueioDestinatario", data);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	function validaBloqueioDestinatario_cb(data, error) {
		if (error != undefined) {
			alert(error);
		} else {
			if (data.clienteBloqueado == "true") {
				window
						.showModalDialog(
								"coleta/liberarBloqueioCreditoColeta.do?cmd=main&idCliente="
										+ getElementValue("cliente.idCliente"),
								window,
								'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:760px;dialogHeight:490px;');
				if (getElementValue("buttonDestinoBloqueado") == "false") {
					novoRegistro();
				}
			} else {
				storeDetalheColeta();
			}
		}
	}
	/**
	 * Função chamada pela popup de liberação, se ela for chamada é porque o 
	 * Bloqueio de Crédito foi liberado 
	 */
	function setaDadosPopupLiberarBloqueioCreditoColeta(param) {
		setElementValue("buttonDestinoBloqueado", true);
	}

	/**
	 * Limpa a tela para um novo registro.
	 */
	function novoRegistro() {
		newButtonScript();
		setFocusOnFirstFocusableField(this.document);
	}

	/**
	 * Carrega a filial que o usuario está logado
	 * 
	 */
	function carregaDadosFilial() {
		var criteria = new Array();

		var sdo = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.getFilialUsuarioLogado",
				"resultadoBusca", criteria);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	var idFilialUsuarioLogado = null;
	function resultadoBusca_cb(data, error) {

		if (data.idFilial == undefined || data.idFilial == "") {
			return false;
		} else {
			idFilialUsuarioLogado = data.idFilial;

		}
	}

	/**
	 * Bloquear coleta e transporte para frete FOB quando o destinatário for Consumidor Final e estiver em lista de bloqueio. 
	 */
	function validarFOB() {

		var remoteCall = {
			serviceDataObjects : new Array()
		};
		var dataCall = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.validaColetaFOB",
				"validaColetaFOB", {
					idPessoa : getElementValue("cliente.idCliente"),
					idFilial : idFilialUsuarioLogado

				});
		remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);

	}

	function validaColetaFOB_cb(data, error) {

		if (!error) {

			if (data.validaColetaFOB == "true") {
				alert(lms_02128);

			} else {
				storeButtonScript(
						"lms.coleta.cadastrarPedidoColetaAction.saveDetalheColeta",
						"salvarDetalhe", document.forms[0]);
			}

		} else {
			alert(error);

		}

	}

	function salvarDetalhe() {
		if (getElementValue("pessoa.endereco.municipio.idMunicipio") != ""
				&& getElementValue("municipio.idMunicipio") != ""
				&& getElementValue("pessoa.endereco.municipio.idMunicipio") != getElementValue("municipio.idMunicipio")) {
			confirmMunicipioDiferenteMunicipioCliente();
		} else {
			awbOuPreawbPrenchido();
		}
	}
	function salvarDetalhe_onClick() {

		salvarDetalhe();

	}

	function confirmMunicipioDiferenteMunicipioCliente() {
		if (confirm(lms_02091)) {
			awbOuPreawbPrenchido();
		}
	}

	function awbOuPreawbPrenchido() {
		if (getElementValue("awb.idAwb") != ""
				&& getElementValue("awb.idAwb") != null) {
			validaDocumentoJaTemColetaCadastrada();
		} else {
			setElementValue("inclusaoParcial", false);
			storeDetalheColeta();
		}
	}

	function validaDocumentoJaTemColetaCadastrada() {
		var remoteCall = {
			serviceDataObjects : new Array()
		};
		var dataCall = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.findDocumentosAwbComColeta",
				"findDocumentosAwbComColeta", {
					idColeta : getElementValue("masterId"),
					idAwb : getElementValue("awb.idAwb")
				});
		remoteCall.serviceDataObjects.push(dataCall);
		xmit(remoteCall);
	}

	function findDocumentosAwbComColeta_cb(data, error) {
		if (!error) {
			if (data.valor == 0) {
				setElementValue("inclusaoParcial", false);
				storeDetalheColeta();
			} else if (data.valor == 1) {
				confirmaInclusaoParcialDocumentos();
			} else if (data.valor == 2) {
				alert(lms_02103);
				//setElementValue("idPreAwb", "");
				//setElementValue("idAwbHidden", "");
				setElementValue("ciaFilialMercurio.empresa.idEmpresa", "");
				setElementValue("ciaFilialMercurio.empresa.sgEmpresa", "");
				setElementValue("awb.idAwb", "");
				setElementValue("awb.nrAwb", "");
			}
		} else {
			alert(error);
		}
	}

	function confirmaInclusaoParcialDocumentos() {
		if (confirm("LMS-02113 - " + getI18nMessage('LMS-02113'))) {
			setElementValue("inclusaoParcial", true);
			storeDetalheColeta();
		}
	}

	function storeDetalheColeta() {
		if (getElementValue("tpFrete") == "F"
				&& getElementValue("cliente.idCliente") != "") {
			validarFOB();
		} else {
			storeButtonScript(
					"lms.coleta.cadastrarPedidoColetaAction.saveDetalheColeta",
					"salvarDetalhe", document.forms[0]);
		}
	}

	/**
	 * Habilita campos caso o serviço seja informado
	 */
	function habilitaCampos() {

		if (getElementValue("tpPedidoColeta") == 'AE') {
			return true;
		}

		var idServico = getElementValue("servico.idServico");

		resetValue("municipioFilial.idMunicipioFilial");
		setElementValue("filial.idFilial", "");
		setElementValue("filial.sgFilial", "");
		setElementValue("filial.pessoa.nmFantasia", "");
		setElementValue("sgUnidadeFederativa", "");

		if (idServico.length > 0) {
			var comboBoxServico = document.getElementById("servico.idServico");
			setElementValue("servico.dsServico",
					comboBoxServico.options[comboBoxServico.selectedIndex].text);

			setDisabled("naturezaProduto.idNaturezaProduto", false);
			setDisabled("tpFrete", false);
			setDisabled("destino", false);
			setDisabled("municipioFilial.idMunicipioFilial", false);
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			setDisabled("cliente.idCliente", false);
			setDisabled("nmDestinatario", false);
			setDisabled("moeda.idMoeda", false);
			setDisabled("vlMercadoria", false);
			setDisabled("qtVolumes", false);
			setDisabled("psMercadoria", false);
			setDisabled("aforado", false);
			setDisabled("notaFiscalColetas", false);
			setDisabled("notaFiscalColetas_nrNotaFiscal", false);

			setDisabled("obDetalheColeta", false);

			// Função que verifica se o serviço selecionado é Internacional.
			// Se for habilita a lookup de CRT.	 
			var mapCriteria = new Array();
			setNestedBeanPropertyValue(mapCriteria, "idServico", idServico);
			var sdo = createServiceDataObject(
					"lms.coleta.cadastrarPedidoColetaAction.verificaServicoInternacional",
					"verificaServicoInternacional", mapCriteria);
			xmit({
				serviceDataObjects : [ sdo ]
			});
		} else {
			setElementValue("servico.dsServico", "");

			setDisabled("naturezaProduto.idNaturezaProduto", true);
			setDisabled("tpFrete", true);
			setDisabled("destino", true);
			setDisabled("municipioFilial.idMunicipioFilial", true);
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			setDisabled("cliente.idCliente", true);
			setDisabled("nmDestinatario", true);
			setDisabled("moeda.idMoeda", true);
			setDisabled("vlMercadoria", true);
			setDisabled("qtVolumes", true);
			setDisabled("psMercadoria", true);
			setDisabled("aforado", true);
			setDisabled("notaFiscalColetas", true);
			setDisabled("notaFiscalColetas_nrNotaFiscal", true);
			setDisabled("obDetalheColeta", true);
		}
		return true;
	}

	function clearMunicipio() {
		resetValue("municipioFilial.idMunicipioFilial");
		setElementValue("filial.idFilial", "");
		setElementValue("filial.sgFilial", "");
		setElementValue("filial.pessoa.nmFantasia", "");
		setElementValue("sgUnidadeFederativa", "");
	}

	/**
	 * Retorno da verificação se serviço é Internacional.
	 */
	function verificaServicoInternacional_cb(data, error) {
		if (!error) {
			if (data.blServicoInternacional) {
				setDisabled("ctoInternacional.sgPais", false);
				setDisabled("ctoInternacional.idDoctoServico", false);
			} else {
				setDisabled("ctoInternacional.sgPais", true);
				setDisabled("ctoInternacional.idDoctoServico", true);
			}
		} else {
			alert(error);
		}
	}

	function habilitaMunicipioOuLocalidade() {

		var comboBox = document.getElementById("destino");

		if (comboBox.value == "M") {
			resetValue("localidadeEspecial.idLocalidadeEspecial");
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");
			setElementValue("filial.pessoa.nmFantasia", "");
			setDisabled("municipioFilial.idMunicipioFilial", false);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "true";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "false";
		} else if (comboBox.value == "L") {
			resetValue("municipioFilial.idMunicipioFilial");
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");
			setElementValue("filial.pessoa.nmFantasia", "");
			setDisabled("municipioFilial.idMunicipioFilial", true);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "false";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", false);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "true";
		}

		return true;
	}

	function habilitaCamposGridClick_cb(data, error) {
		if (error != undefined) {
			alert(error);
			return;
		}

		if (data.municipioFilial != undefined) {
			setNestedBeanPropertyValue(data, "destino", "M");
			setDisabled("municipioFilial.idMunicipioFilial", false);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "true";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", true);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "false";
		}
		if (data.localidadeEspecial != undefined) {
			setNestedBeanPropertyValue(data, "destino", "L");
			setDisabled("municipioFilial.idMunicipioFilial", true);
			document.getElementById("municipioFilial.municipio.nmMunicipio").required = "false";
			setDisabled("localidadeEspecial.idLocalidadeEspecial", false);
			document.getElementById("localidadeEspecial.dsLocalidade").required = "true";
		}

		var tpPedidoColeta = tabDet.getFormProperty("tpPedidoColeta");

		onDataLoad_cb(data);
		if (data.origem == "cotacao") {
			setDisabled("servico.idServico", true);
			setDisabled("naturezaProduto.idNaturezaProduto", true);
			setDisabled("tpFrete", true);
			setDisabled("destino", true);
			setDisabled("municipioFilial.idMunicipioFilial", true);

			if (tpPedidoColeta != 'AE') {
				setDisabled("cliente.idCliente", true);
				setDisabled("nmDestinatario", true);
				setDisabled("moeda.idMoeda", true);
				setDisabled("vlMercadoria", true);
				setDisabled("qtVolumes", false);
				setDisabled("psMercadoria", true);
				setDisabled("aforado", true);
			}

			setDisabled("notaFiscalColetas", true);
			setDisabled("notaFiscalColetas_nrNotaFiscal", true);
			setDisabled("obDetalheColeta", true);
			setDisabled("salvarDetalheButton", false);
			detalheColetaGridDef.disabled = true;
		} else {
			setDisabled("servico.idServico", true);
			setDisabled("naturezaProduto.idNaturezaProduto", false);
			setDisabled("tpFrete", false);
			setDisabled("destino", false);

			if (tpPedidoColeta != 'AE') {
				setDisabled("cliente.idCliente", false);
				setDisabled("nmDestinatario", false);
				setDisabled("moeda.idMoeda", false);
				setDisabled("vlMercadoria", false);
				setDisabled("qtVolumes", false);
				setDisabled("psMercadoria", false);
				setDisabled("aforado", false);
			}
			setDisabled("notaFiscalColetas", false);
			setDisabled("notaFiscalColetas_nrNotaFiscal", false);

			setDisabled("obDetalheColeta", false);
			setDisabled("newButton", data.origem == "prealerta");
			setDisabled("salvarDetalheButton", data.origem == "prealerta");
			detalheColetaGridDef.disabled = false;

			// Função que verifica se o serviço selecionado é Internacional.
			// Se for habilita a lookup de CRT.	 
			var mapCriteria = new Array();
			setNestedBeanPropertyValue(mapCriteria, "idServico",
					data.servico.idServico);
			var sdo = createServiceDataObject(
					"lms.coleta.cadastrarPedidoColetaAction.verificaServicoInternacional",
					"verificaServicoInternacional", mapCriteria);
			xmit({
				serviceDataObjects : [ sdo ]
			});
		}

		if (data.psAforado != undefined && data.psAforado != "") {
			document.getElementById("aforado").checked = true;
		} else {
			document.getElementById("aforado").checked = false;
		}

		if (data.blEntregaDireta == "true") {
			document.getElementById("blEntregaDireta").checked = true;
		} else {
			document.getElementById("blEntregaDireta").checked = false;
		}
		fieldsAereo(true);
	}

	/**
	 * Chama PopUp de calculo de Peso Aforado.
	 */
	function rotinaCalculoPeso() {
		if (getElementValue("qtVolumes") != ""
				&& getElementValue("psMercadoria") != "") {
			var checkBox = document.getElementById("aforado");
			if (checkBox.checked) {
				window
						.showModalDialog(
								'coleta/cadastrarPedidoColeta.do?cmd=pesoAforado',
								window,
								'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:300px;dialogHeight:215px;');
				//Se fechou a janela pelo X, entao deve desmarcar o checkbox.
				if (getElementValue("psAforado") == "") {
					document.getElementById("aforado").checked = false;
				}
			} else {
				setElementValue("psAforado", "");
			}
		} else {
			desmarcaAforado();
			alert(lms_02025);
		}
	}

	/**
	 * Pega o resultado da PopUp de calculo de Peso Aforado.
	 */
	function resultadoRotinaCalculoPeso(resultado) {
		setElementValue("psAforado", setFormat(document
				.getElementById("psAforado"), resultado));
	}

	/**
	 * Seta os dados da sessão.
	 */
	function buscarDadosUsuarioSessao() {
		var comboBox = document.getElementById("moeda.idMoeda");

		if (getElementValue("idMoedaCotacao") != "") {
			for (var i = 0; i < comboBox.length; i++) {
				if (comboBox.options[i].value == getElementValue("idMoedaCotacao")) {
					comboBox.options[i].selected = "true";
				}
			}
		} else {
			for (var i = 0; i < comboBox.length; i++) {
				if (comboBox.options[i].text == tabDet
						.getFormProperty("moeda.siglaSimbolo")) {
					setElementValue("moeda.siglaSimbolo", tabDet
							.getFormProperty("moeda.siglaSimbolo"));
					comboBox.options[i].selected = "true";
				}
			}
		}
	}

	function tpFrete_onChange() {
		clearMunicipio();
		var retorno = verificaObrigatoriedadeDestinatario();
		if (getElementValue("tpFrete") == "F"
				& getElementValue("cliente.idCliente").length > 0) {
			validaBloqueioDestinatario()
		}

		return retorno;
	}

	/**
	 * Verifica se o tipo de Frete é FOB e se o tipo cliente não for um cliente especial.
	 */
	function verificaObrigatoriedadeDestinatario() {
		var tipoCliente = tabDet.getFormProperty("tipoCliente");
		var tipoFrete = getElementValue("tpFrete");
		if (tipoFrete == "F" && (tipoCliente != "" && tipoCliente != "S")) {
			document.getElementById("cliente.pessoa.nrIdentificacao").required = "true";
		} else {
			document.getElementById("cliente.pessoa.nrIdentificacao").required = "false";
		}

		return true;
	}

	/**
	 * Função chamada no onDataLoadCallBack da lookup de Municipio Filial.
	 */
	function municipioFilialOnDataLoadCallBack_cb(data) {
		municipioFilial_municipio_nmMunicipio_exactMatch_cb(data);
		if (data.length == 1) {
			validaVigenciaMunicipioFilial(data[0]);
		}
	}

	/**
	 * Função chamada no onPopupSetValue da lookup de Municipio Filial.
	 */
	function municipioFilialOnPopupSetValue(data) {
		validaVigenciaMunicipioFilial(data);
	}

	/**
	 * Função que verifica a vigencia do municipio selecionado.
	 */
	function validaVigenciaMunicipioFilial(data) {
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "idMunicipioFilial",
				data.idMunicipioFilial);
		setNestedBeanPropertyValue(mapCriteria, "idMunicipio",
				data.municipio.idMunicipio);

		var sdo = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.validaVigenciaAtendimento",
				"resultado_validaVigenciaAtendimento", mapCriteria);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	/**
	 * Retorno da verificação de vigencia do municipio.
	 */
	function resultado_validaVigenciaAtendimento_cb(data, error) {
		if (!error) {
			verificaMunicipioDestinoBloqueado(data);
		} else {
			alert(error);
			clearMunicipio();
			setFocus("municipioFilial.municipio.nmMunicipio");
			return false;
		}
	}

	var origemCbAwb = "";

	function findAwb_cb(data, error) {
		if (data != null) {
			data = data[0] != null ? data[0] : data;
			setElementValue("awb.idAwb", data.idAwb);
			setElementValue("ciaFilialMercurio.empresa.idEmpresa",
					data.ciaFilialMercurio.empresa.idEmpresa);
			setElementValue("ciaFilialMercurio.empresa.sgEmpresa",
					data.ciaFilialMercurio.empresa.sgEmpresa);
			origemCbAwb = "AWB";

			findDadosAwb(data.idAwb, null);
		}
	}

	function awbOnDataLoadCallBack_cb(data) {
		awb_nrAwb_exactMatch_cb(data);
		if (data != null && data.length > 0) {
			data = data[0] != null ? data[0] : data;
			origemCbAwb = "AWB";

			findDadosAwb(data.idAwb, null);
		}
	}

	function findDadosAwb(idAwb, idConhecimento) {
		if ((idAwb != null && idAwb != "")
				|| (idConhecimento != null && idConhecimento != "")) {
			var sdo = createServiceDataObject(
					"lms.coleta.cadastrarPedidoColetaAction.findDadosColetaAwb",
					"findDadosAwb", {
						idAwb : idAwb,
						idConhecimento : idConhecimento
					});
			if (idAwb != null && idAwb != "") {
				xmit({
					serviceDataObjects : [ sdo ]
				});
			} else {
				xmit({
					serviceDataObjects : [ sdo ],
					onXmitDone : "findDadosAwbDoctoServico"
				});
			}
		} else {
			limparDadosAwb();
		}
	}

	function findDadosAwb_cb(data, error) {
		limparCamposAwb();
		setElementValue("vlMercadoria", "");
		setElementValue("qtVolumes", "");
		setElementValue("psMercadoria", "");
		setElementValue("psAforado", "");
		setElementValue("psAforado", "");
		setElementValue("naturezaProduto.dsNaturezaProduto", "");

		if (error) {
			alert(error);
		} else {
			if (data.nmAeroporto != undefined) {

				setElementValueFormated("vlMercadoria", data.vlMercadoria
						.replace('.', ','));
				setElementValue("qtVolumes", data.qtdVolumes);
				setElementValueFormated("psMercadoria", data.psReal.replace(
						'.', ','));
				setElementValueFormated("psAforado", data.psAforado.replace(
						'.', ','));
				setElementValue("naturezaProduto.idNaturezaProduto",
						data.dsNaturezaProduto);

				setElementValue("doctoAwb", 1);
			} else if (getElementValue("awb.idAwb")) {
				setFocus("awb.idAwb");
				alert(getI18nMessage('LMS-00061'));
				resetValue("ciaFilialMercurio.empresa.idEmpresa");
				resetValue("ciaFilialMercurio.empresa.sgEmpresa");
				resetValue("awb.idAwb");
			}
		}
	}

	function limparCamposAwb() {
		switch (origemCbAwb) {
		case "AWB":
			resetValue('doctoServico.tpDocumentoServico');
			resetValue('doctoServico.idDoctoServico');
			resetValue('doctoServico.nrDoctoServico');
			resetValue('doctoServico.filialByIdFilialOrigem.idFilial');
			resetValue('doctoServico.filialByIdFilialOrigem.sgFilial');
			break;
		case "CTO":
			resetValue("awb.idAwb");
			resetValue("ciaFilialMercurio.empresa.idEmpresa");
			resetValue("ciaFilialMercurio.empresa.sgEmpresa");
			break;
		default:
			break;
		}
	}

	function findDadosAwbDoctoServico_cb(data, error) {
		if (getElementValue("doctoAwb") != null
				&& getElementValue("doctoAwb") == 1) {
			unblockUI();
		} else {
			setElementValue('doctoServico.idDoctoServico', "");
			setElementValue("doctoAwb", 0);
			limparDadosAwb();
			unblockUI();
			alert("LMS-02112 - " + getI18nMessage('LMS-02112'));
			resetValue('doctoServico.idDoctoServico');
			setFocus("doctoServico.nrDoctoServico");
		}
	}

	function setElementValueFormated(element, value) {
		elementFormated = getElement(element);
		elementFormated.value = value;
		format(elementFormated);
	}

	function limparDadosAwb(value) {
		if (value == null || value == "") {
			setElementValue("vlMercadoria", "");
			setElementValue("qtVolumes", "");
			setElementValue("psMercadoria", "");
			setElementValue("psAforado", "");
			setElementValue("psAforado", "");
			setElementValue("awb.idAwb", "");
			setElementValue("doctoAwb", "");
		}
		return awb_nrAwbOnChangeHandler();
	}

	/**
	 * Verifica se o Municipio recebe coletas eventuais, caso NÃO, chamar 
	 * especificação 02.06.01.03 Liberar Coleta para Destino Bloqueado 
	 */
	function verificaMunicipioDestinoBloqueado(data) {
		var mapCriteria = new Array();
		setNestedBeanPropertyValue(mapCriteria, "idMunicipio", data.idMunicipio);
		setNestedBeanPropertyValue(mapCriteria, "idServico",
				getElementValue("servico.idServico"));
		setNestedBeanPropertyValue(mapCriteria, "idCliente", tabDet
				.getFormProperty("cliente.idCliente"));
		setNestedBeanPropertyValue(mapCriteria, "tpFrete",
				getElementValue("tpFrete"));

		var sdo = createServiceDataObject(
				"lms.coleta.cadastrarPedidoColetaAction.getMunicipioDestinoBloqueado",
				"resultado_verificaMunicipioDestinoBloqueado", mapCriteria);
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	/**
	 * Retorno da verificação de Destino Bloqueado
	 */
	function resultado_verificaMunicipioDestinoBloqueado_cb(data, error) {
		if (error) {
			alert(error);
			clearMunicipio();
			setFocus("municipioFilial.municipio.nmMunicipio");
			return false;
		}
		setElementValue("eventoColeta.usuario.idUsuario", "");
		setElementValue("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta", "");
		setElementValue("eventoColeta.dsDescricao", "");

		setElementValue("filial.idFilial", "");
		setElementValue("filial.sgFilial", "");
		setElementValue("filial.pessoa.nmFantasia", "");
		setElementValue("sgUnidadeFederativa", "");
		if (data.filialId != undefined) {
			setElementValue("filial.idFilial", data.filialId);
			setElementValue("filial.sgFilial", data.filialSigla);
			setElementValue("filial.pessoa.nmFantasia", data.filialNome);
			setElementValue("sgUnidadeFederativa", data.sgUnidadeFederativa);
		} else {
			alert(lms_02077);
			resetValue("municipioFilial.idMunicipioFilial");
			setElementValue("municipio.idMunicipio", "");
			setElementValue("sgUnidadeFederativa", "");
		}

		if (data.recebeColetaEventual != undefined
				&& data.recebeColetaEventual == "false"
				&& data.hasToValidateEmbarqueProibido == "true") {
			if (data.bloqCredEmbProib != undefined
					&& data.bloqCredEmbProib == "true") {
				alert(lms_04508);
				return;
			}

			window
					.showModalDialog(
							'coleta/liberarColetasDestinoBloqueado.do?cmd=main',
							window,
							'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:760px;dialogHeight:490px;');
			if (getElementValue("buttonDestinoBloqueado") == "false") {
				resetValue("municipioFilial.idMunicipioFilial");
				setElementValue("filial.idFilial", "");
				setElementValue("filial.sgFilial", "");
				setElementValue("filial.pessoa.nmFantasia", "");
				setElementValue("sgUnidadeFederativa", "");
			} else {
				setElementValue("buttonDestinoBloqueado", "false");
			}
		}
	}

	/**
	 * Função que lima o campo da filial caso o municipio esteja em branco.
	 */
	function limpaCampoMunicipio(value) {
		var boolean = municipioFilial_municipio_nmMunicipioOnChangeHandler();
		if (value == "") {
			setElementValue("filial.idFilial", "");
			setElementValue("filial.sgFilial", "");
			setElementValue("filial.pessoa.nmFantasia", "");
			setElementValue("sgUnidadeFederativa", "");
		}
		return boolean;
	}

	/**
	 * Desmarca o checkbox de Aforado
	 */
	function desmarcaAforado() {
		if (document.getElementById("aforado").checked == true
				&& document.getElementById("aforado").disabled == true) {
			//Deixa alterar	os volumes.
		} else {
			setElementValue("psAforado", "");
			document.getElementById("aforado").checked = false;
		}
	}

	/**
	 * Seta os dados da Pop-up de Liberar Bloqueio de Crédito para Coleta.
	 */
	function setaDadosPopupLiberarColetaDestinoBloqueado(mapDestinoBloqueado) {
		setElementValue("eventoColeta.usuario.idUsuario",
				mapDestinoBloqueado.idUsuario);
		setElementValue("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta",
				mapDestinoBloqueado.idOcorrenciaColeta);
		setElementValue("eventoColeta.dsDescricao",
				mapDestinoBloqueado.dsDescricao);
		setElementValue("buttonDestinoBloqueado", "true");
	}

	/**
	 * Função que manipula o rowClick na grid.
	 */
	function onRowClick() {
		var idPedidoColeta = tabDet.getFormProperty("idPedidoColeta");

		if (idPedidoColeta != null && idPedidoColeta != "") {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Função que ajusta o campo sigla pais da lookup de CRT.
	 */
	function sgPaisChange(campo) {
		var isValid = getElementValue(campo).length > 0;
		if (isValid) {
			setElementValue(campo, getElementValue(campo).toUpperCase());
		}
		resetValue("ctoInternacional.idDoctoServico");
		return true;
	}

	/**
	 * Função chamado no onPopupValue da Lookup de CRT
	 */
	function findLookupCRT(data) {
		setElementValue("ctoInternacional.sgPais", data.sgPais);
		setElementValue("ctoInternacional.nrCrt", data.nrCrt);
		return lookupChange({
			e : document.getElementById("ctoInternacional.idDoctoServico"),
			forceChange : true
		});
	}

	function exibeRestricoes() {
		showModalDialog(
				'/coleta/cadastrarPedidoColeta.do?cmd=restricoesColeta',
				window,
				'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}

	/* functions para NF-e e NF */

	/**
	 * Valida a chave Nfe.
	 * (Método repetido na tela: manterColetasDetalheColeta)
	 */
	function validateChaveNfe() {
		var chaveNfe = getElementValue("nrChaveNfe_nrChave");

		if (chaveNfe.length >= 44) {
			if (!validateDigitoVerificadorNfe(chaveNfe)) {
				setElementValue("nrChaveNfe_nrChave", "");
				return false;
			}
		} else {
			alert(lms_04400);
			setElementValue("nrChaveNfe_nrChave", "");
			return false;
		}

		var sizeNFe = nrChaveNfeListboxDef.getData().length;
		nrChaveNfeListboxDef.insertOrUpdateOption();

		if (sizeNFe != nrChaveNfeListboxDef.getData().length) {
			setaValorNotaFiscal(chaveNfe);
		} else {
			setElementValue("nrChaveNfe_nrChave", "");
		}

		return true;
	}

	/**
	 * Valida o digito verificador da Chave Nfe
	 * (Método repetido na tela: manterColetasDetalheColeta)
	 */
	function validateDigitoVerificadorNfe(chaveNfe) {
		var dvChaveNfe = chaveNfe.substring(chaveNfe.length - 1,
				chaveNfe.length);
		var chave = chaveNfe.substring(0, chaveNfe.length - 1);
		var calculoChaveNfe = modulo11(chave);

		if (dvChaveNfe == (calculoChaveNfe)) {
			return true;
		} else {
			alert(lms_04400);
			return false;
		}
	}

	/**
	 * (Método repetido na tela: manterColetasDetalheColeta)
	 */
	function modulo11(chave) {
		var n = new Array();
		var peso = 2;
		var soma = 0;

		n = chave.split('');

		for (var i = n.length - 1; i >= 0; i--) {
			var value = n[i];
			soma = soma + value * peso++;
			if (peso == 10) {
				peso = 2;
			}
		}

		var mod = soma % 11;
		var dv;

		if (mod == 0 || mod == 1) {
			dv = 0;
		} else {
			dv = 11 - mod;
		}

		return dv
	}

	/**
	 * (Método repetido/similar na tela: manterColetasDetalheColeta)
	 */
	function setaValorNotaFiscal(chaveNfe) {
		if (!verificarNFAdd(chaveNfe)) {
			var nrNf = chaveNfe.substring(25, 34);
			setElementValue("notaFiscalColetas_nrNotaFiscal",
					parseInt(nrNf, 10));
			notaFiscalColetasListboxDef.insertOrUpdateOption();
		}
	}

	/**
	 * Eventos de modificação de conteúdo do listbox
	 * (Método repetido na tela: manterColetasDetalheColeta)
	 */
	function contentChange(event) {
		if (event.name == "deleteButton_click") {
			removeNF();
		}
		if (event.name == "cleanButton_click") {
			setElementValue("nrChaveNfe_nrChave", "");
			var elementNfe = document.getElementById("nrChaveNfe");
			elementNfe.selectedIndex = -2;
		}
	}

	function contentChangeNf(event) {
		if (event.name == "deleteButton_click") {
			removeNFe();
		}
		if (event.name == "cleanButton_click") {
			setElementValue("notaFiscalColetas_nrNotaFiscal", "");
			var elementNf = document.getElementById("notaFiscalColetas");
			elementNf.selectedIndex = -2;
		}
	}

	/**
	 * Remove a Nota Fiscal de acordo com a chave NFe removida
	 * (Método repetido na tela: manterColetasDetalheColeta)
	 */
	function removeNF() {
		var notasFiscais = notaFiscalColetasListboxDef.getData();
		var nfToCompare = parseInt(getElementValue("nrChaveNfe_nrChave")
				.substring(25, 34), 10);
		var element = document.getElementById("notaFiscalColetas");

		setElementValue("notaFiscalColetas_nrNotaFiscal", "");
		for (var i = 0; i < notaFiscalColetasListboxDef.getData().length; i++) {
			if (parseInt(notasFiscais[i].nrNotaFiscal, 10) == nfToCompare) {
				element.selectedIndex = i;
				notaFiscalColetasListboxDef.deleteOption();
			}
		}
	}

	function verificarNFAdd(chaveNfe) {
		var notasFiscais = notaFiscalColetasListboxDef.getData();
		var nfToCompare = parseInt(chaveNfe.substring(25, 34), 10);
		var element = document.getElementById("notaFiscalColetas");

		for (var i = 0; i < notaFiscalColetasListboxDef.getData().length; i++) {
			if (parseInt(notasFiscais[i].nrNotaFiscal, 10) == nfToCompare) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove a NFe de acordo com a chave Nota Fiscal removida
	 */
	function removeNFe() {
		var notasFiscaisEle = nrChaveNfeListboxDef.getData();
		var nfToCompare = parseInt(
				getElementValue("notaFiscalColetas_nrNotaFiscal"), 10);
		var element = document.getElementById("nrChaveNfe");

		setElementValue("nrChaveNfe_nrChave", "");
		for (var i = 0; i < notasFiscaisEle.length; i++) {
			var nfeToCompare = parseInt(notasFiscaisEle[i].nrChave.substring(
					25, 34), 10);
			if (nfeToCompare == nfToCompare) {
				element.selectedIndex = i;
				nrChaveNfeListboxDef.deleteOption();
			}
		}
	}

	// 	Lookup Nova doctoservico
	function doctoServicoNrDoctoServico_OnChange() {
		if (getElementValue('doctoServico.nrDoctoServico') == '') {
			resetValue("doctoServico.idDoctoServico");
			limparDadosAwb();
		}

		return doctoServico_nrDoctoServicoOnChangeHandler();
	}

	function onDoctoServicoAfterPopupSetValue(data) {
		setElementValue("doctoAwb", 0);
		var criteria = new Array();
		setNestedBeanPropertyValue(criteria, "idDoctoServico",
				getNestedBeanPropertyValue(data, 'idDoctoServico'));
		setNestedBeanPropertyValue(criteria, "tpDocumentoServico",
				getElementValue('doctoServico.tpDocumentoServico'));
		setNestedBeanPropertyValue(criteria, "filialByIdFilialOrigem.idFilial",
				getElementValue('doctoServico.filialByIdFilialOrigem.idFilial'));
		document.getElementById("doctoServico.nrDoctoServico").previousValue = undefined;
		lookupChange({
			e : document.getElementById("doctoServico.idDoctoServico"),
			forceChange : true
		});

		return false;
	}

	function onDoctoServicoDataCallback_cb(data, error) {
		var result = doctoServico_nrDoctoServico_exactMatch_cb(data);
		if (result == true) {

			var idDoctoServico = getNestedBeanPropertyValue(data,
					":0.doctoServico.idDoctoServico");
			var nrDoctoServico = getNestedBeanPropertyValue(data,
					":0.doctoServico.nrDoctoServico");
			var idFilial = getNestedBeanPropertyValue(data,
					":0.doctoServico.filialByIdFilialOrigem.idFilial");
			var sgFilial = getNestedBeanPropertyValue(data,
					":0.doctoServico.filialByIdFilialOrigem.sgFilial");

			setElementValue('doctoServico.idDoctoServico', idDoctoServico);
			setElementValue('doctoServico.nrDoctoServico', nrDoctoServico);
			setElementValue('doctoServico.filialByIdFilialOrigem.idFilial',
					idFilial);
			setElementValue('doctoServico.filialByIdFilialOrigem.sgFilial',
					sgFilial);

			setElementValue("doctoAwb", 0);
			blockUI();
			origemCbAwb = "CTO";
			findDadosAwb(null, idDoctoServico);

		} else {
			if (document.getElementById('doctoServico.nrDoctoServico').disabled == false) {
				setFocus("doctoServico.nrDoctoServico");
			} else {
				setFocus("doctoServico.filialByIdFilialOrigem.sgFilial");
			}
		}

		return result;
	}

	function onChangeTpDocumentoServico() {
		var combo = document.getElementById("doctoServico.tpDocumentoServico");
		resetValue('doctoServico.filialByIdFilialOrigem.sgFilial');
	}
</script>
