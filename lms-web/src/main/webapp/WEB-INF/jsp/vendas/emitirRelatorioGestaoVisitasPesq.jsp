<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/emitirRelatorioGestaoVisitas">
		<adsm:hidden property="regional.siglaDescricao"/>
		<adsm:hidden property="tpAcesso" value="F"/>
		<adsm:hidden property="tpEmp" value="M" serializable="false" />

		<adsm:combobox
			label="regional"
			property="regional.idRegional"
			optionLabelProperty="siglaDescricao"
			optionProperty="idRegional"
			service="lms.vendas.emitirGestaoVisitasAction.montaComboRegional"
			required="true"
			labelWidth="15%"
			width="38%"
			boxWidth="170"
		>
			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:lookup dataType="text" disabled="true"
			property="filial" idProperty="idFilial" criteriaProperty="sgFilial"
			service="lms.vendas.manterRegistrosVisitaAction.findFiliais"
			action="/municipios/manterFiliais"
			label="filial" labelWidth="15%" width="80%" size="3" maxLength="3" exactMatch="true"
			required="false">
			<adsm:propertyMapping
				relatedProperty="filial.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping
				modelProperty="empresa.tpEmpresa"
				criteriaProperty="tpEmp" />
			<adsm:propertyMapping 
        		criteriaProperty="tpAcesso" 
        		modelProperty="tpAcesso" />

			<adsm:textbox
				dataType="text"
				property="filial.pessoa.nmFantasia"
				disabled="true"
				serializable="false"
				size="30"/>
		</adsm:lookup>

		<adsm:hidden property="usuario.idUsuario"/>		
		<adsm:lookup
			label="funcionario"
			property="funcionario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula" 
			service="lms.vendas.emitirGestaoVisitasAction.montaLookupFuncionario"
			dataType="text"
			size="20"
			maxLength="20"
			labelWidth="15%"
			width="80%"
			action="/configuracoes/consultarFuncionariosView" 
			exactMatch="true"
			disabled="false">
			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:propertyMapping relatedProperty="usuario.idUsuario" modelProperty="idUsuario"/>
			<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" size="30" maxLength="60" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="idCliente"
			action="/vendas/manterDadosIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text"
			exactMatch="true"
			maxLength="20"
			service="lms.vendas.emitirGestaoVisitasAction.findLookupCliente"
			size="20"
			labelWidth="15%"
			width="80%">
			<adsm:propertyMapping relatedProperty="nmRemetente" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="nmRemetente"
				serializable="true"
				size="30"
				maxLength="60"/>
		</adsm:lookup>

		<adsm:combobox
			property="tipoVisita.idTipoVisita"
			label="tipoVisita"
			optionLabelProperty="dsTipoVisita"
			optionProperty="idTipoVisita"
			service="lms.vendas.emitirGestaoVisitasAction.montaComboTipoVisita"
			required="false"
			labelWidth="15%"
			width="80%"/>

		<adsm:combobox
			label="tipoServico"
			labelWidth="15%"
			property="servico"
			optionLabelProperty="dsServico"
			optionProperty="idServico" 
			service="lms.vendas.emitirGestaoVisitasAction.montaComboServico" 
		/>

		<adsm:combobox
			property="campanhaMarketing.idCampanhaMarketing" 
			label="indicadorMarketing" 
			optionLabelProperty="dsCampanhaMarketing" 
			optionProperty="idCampanhaMarketing" 
			service="lms.vendas.emitirGestaoVisitasAction.montaComboCampanhaMarketing"
			labelWidth="15%" 
			width="50%"/>

		<adsm:range
			label="periodo"
			labelWidth="15%"
			width="32%"
			maxInterval="31">
			<adsm:textbox
				dataType="JTDate"
				property="dtInicial"
				label="horarioInicial"
				required="false"
				labelWidth="16%"
				width="37%"/>
			<adsm:textbox
				dataType="JTDate"
				property="dtFinal"
				label="horarioFinal"
				labelWidth="14%"
				width="31%"/>
		</adsm:range>

		<adsm:combobox
			property="tpPerspectivaFaturamento"
			label="perspectivaFaturamento"
			domain="DM_PERSPECTIVA_FATUR"
			labelWidth="15%"
			width="50%"
		/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.vendas.emitirGestaoVisitasAction" caption="visualizar"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
	var defaultData;
	function initWindow(event) {
		if(event.name == "cleanButton_click"){
			setDefaultData();
		}
	}

	function myPageLoad_cb() {
		findDefaultData();
	}

	function findDefaultData() {
		var sdo = createServiceDataObject("lms.vendas.emitirGestaoVisitasAction.findDefaultData", "findDefaultData");
		xmit({serviceDataObjects:[sdo]});
	}

	function findDefaultData_cb(data, error) {
		if(error) {
			alert(error);
			return;
		}
		if(data) {
			defaultData = data;
			setDefaultData();
		}
	}

	//Preenche os dados basicos da tela
	function setDefaultData() {
		setElementValue("regional.idRegional", getNestedBeanPropertyValue(defaultData, "regional.idRegional"));
		setElementValue("regional.siglaDescricao", getNestedBeanPropertyValue(defaultData, "regional.siglaDescricao"));
		setElementValue("filial.idFilial", getNestedBeanPropertyValue(defaultData, "filial.idFilial"));
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(defaultData, "filial.sgFilial"));
		setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(defaultData, "filial.pessoa.nmFantasia"));

		var isGerente = getNestedBeanPropertyValue(defaultData, "contato.isGerenteComercial");
		if(isGerente == 'true' || getNestedBeanPropertyValue(defaultData, "isFilialMatriz") == 'true') {
			setDisabled("regional.idRegional", false);
			setDisabled("filial.idFilial", false);
			setDisabled("funcionario.idUsuario", false);
		} else {
			setElementValue("funcionario.idUsuario", getNestedBeanPropertyValue(defaultData, "contato.idUsuario"));
			setElementValue("funcionario.nrMatricula", getNestedBeanPropertyValue(defaultData, "contato.nrMatricula"));
			setElementValue("funcionario.codPessoa.nome", getNestedBeanPropertyValue(defaultData, "contato.nmUsuario"));
			setDisabled("regional.idRegional", true);
			setDisabled("filial.idFilial", true);
			setDisabled("funcionario.idUsuario", true);
		}

		setElementValue("dtInicial", setFormat("dtInicial", getNestedBeanPropertyValue(defaultData, "dtInicial")));
		setElementValue("dtFinal", setFormat("dtFinal", getNestedBeanPropertyValue(defaultData, "dtFinal")));
		setFocusOnFirstFocusableField();
	}
</script>