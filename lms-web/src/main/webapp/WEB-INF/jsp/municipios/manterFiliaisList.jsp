<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.municipios.filialService" onPageLoad="onPageLoadDesenv" onPageLoadCallBack="paginaConcluida">
<script type="text/javascript">
<!--
	var data;
	var dataAtual = null;

	function onPageLoadDesenv(data,error) {
		onPageLoad();
		addServiceDataObject(createServiceDataObject("adsm.configuration.domainValueService.find", "tpFilialCallBack", {domain:{name:"DM_TIPO_FILIAL"}}));
		addServiceDataObject(createServiceDataObject("adsm.configuration.domainValueService.find", "tpEmpresaCallBack", {domain:{name:"DM_TIPO_EMPRESA"}}));
		xmit({onXmitDone:"returnOnPageLoad"});

		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
			,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
			,numberElement:document.getElementById("pessoa.nrIdentificacao")}
		);
		writeDataSession();
		setFocus("sgFilial");
	}

	function tpEmpresaCallBack_cb(data) {
		empresa_tpEmpresa_cb(data);
		var tpEmpresa = document.getElementById("empresa.tpEmpresa");
		for(x = 0; x < tpEmpresa.length; x++) {
			if (tpEmpresa[x].value != "" && tpEmpresa[x].value != "M" && tpEmpresa[x].value != "P")
				tpEmpresa[x] = null;
		}
	}

	function tpFilialCallBack_cb(data) {
		historicoFiliais_tpFilial_cb(data);
		if (document.getElementById("flag").value == "R" || document.getElementById("flag").value == "C") {
			var flag = document.getElementById("flag").value;
			var type = document.getElementById("flagType").value;
			var types = getTypes(type,flag);

			var tpFilial = document.getElementById("historicoFiliais.tpFilial");
			for(x = 0; x < tpFilial.length; x++) {
				if (!validaType(types,tpFilial[x].value)) {
					tpFilial[x] = null;
					x--;
				}
			}
			document.getElementById("obFilial").value = document.getElementById("flagType").value + ":" + document.getElementById("flag").value;
			setElementValue("historicoFiliais.tpFilial","FI");
		}
	}

	function returnOnPageLoad_cb() {
		onPageLoad_cb();

		changeTypePessoaWidget(
			{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
			tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
			numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'list'});
		
		buscaEmpresa();
	} 

	/*Busca a empresa do usuário logado por padrão quando for lookup, caso tipo de acesso for A=Aberto ou F=Filiais, não seta a empresa */
	function buscaEmpresa() {
		var isLookup = window.dialogArguments && window.dialogArguments.window;
		if (isLookup) {
			var tpAcesso = getElementValue("tpAcesso");
			var blBuscaEmpresaUsuarioLogado = getElementValue("flagBuscaEmpresaUsuarioLogado");
			if (!tpAcesso || (blBuscaEmpresaUsuarioLogado == "true")) {
				var sdo = createServiceDataObject("lms.municipios.manterFiliaisAction.findEmpresaUsuarioLogado", "findEmpresa", data);
				xmit({serviceDataObjects:[sdo]});
			} else if(tpAcesso == "EM") {
				/* Forca a busca da empresa Mercurio */
				var sdo = createServiceDataObject("lms.municipios.manterFiliaisAction.findEmpresaMercurio", "findEmpresa", data);
				xmit({serviceDataObjects:[sdo]});
			}
		} else setElementValue("tpAcesso", "A");
	}

	function findEmpresa_cb(data, error) {
		if (error) {
			alert(error);
			return false;
		} else if (data && getNestedBeanPropertyValue(data, "empresa.idEmpresa")) {
			setElementValue("empresa.idEmpresa", getNestedBeanPropertyValue(data, "empresa.idEmpresa"));
			setElementValue("empresa.pessoa.nrIdentificacao", getNestedBeanPropertyValue(data, "empresa.pessoa.nrIdentificacaoFormatado"));
			setElementValue("empresa.pessoa.nmPessoa", getNestedBeanPropertyValue(data, "empresa.pessoa.nmPessoa"));		
			setElementValue("empresa.tpEmpresa", getNestedBeanPropertyValue(data, "empresa.tpEmpresa"));

			var tpAcesso = getElementValue("tpAcesso");
			var flagDesabilitaEmpresaUsuarioLogado = getElementValue("flagDesabilitaEmpresaUsuarioLogado");
			if (!tpAcesso || (tpAcesso == "EM") || (flagDesabilitaEmpresaUsuarioLogado=="true")) {
				setDisabled("empresa.idEmpresa", true);
				setDisabled("empresa.tpEmpresa", true);		
			} else {
				setDisabled("empresa.idEmpresa", false);
				setDisabled("empresa.tpEmpresa", false);		
			}
		}
	}

	function validaType(types,valor) {
		for (y = 0; y < types.length; y++) {
			if (valor == types[y])
				return true;
		}
		return false;
	}

	function getTypes(type,flag) {
		if (type == "FI" || (type == "FR" && flag == "C"))
			return new Array("","FI","FR");
		else if (type == "LO")
			return new Array("","FI","FR");
		else
			return new Array("","FI","FR","LO","MA","PA","SU"); 
	}

	function paginaConcluida_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.municipios.manterFiliaisAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
	}

	function onChangeSgFilial(field) {
		field.value = field.value.toUpperCase();
		return validate(field);
	}

	function limpaCampos() {
		cleanButtonScript(this.document);
		buscaEmpresa();
		if (document.getElementById("empresa.tpEmpresa").masterLink != "true") {
			setElementValue("empresa.tpEmpresa", getElementValue("empresa.tpEmpresaPadrao"));
		}
		writeDataSession();
	}

	function dataSession_cb(data) {
		dataAtual = setFormat(document.getElementById("historicoFiliais.vigenteEm"), getNestedBeanPropertyValue(data,"dataAtual"));
		writeDataSession();
	}

	function writeDataSession() {
		if( (dataAtual != null)
			&& (document.getElementById("historicoFiliais.vigenteEm").masterLink != "true")
		) {
			setElementValue("historicoFiliais.vigenteEm", dataAtual);
		}
	}
//-->
</script>
	<adsm:form idProperty="idFilial" action="/municipios/manterFiliais" height="100">
		<adsm:hidden property="empresa.tpEmpresaPadrao" value="" serializable="false"/>
		<adsm:hidden property="flag" value="telaFilial" serializable="false"/>
		<adsm:hidden property="flagType" serializable="false"/>
		<adsm:hidden property="idProcessoWorkflow"/>
		<adsm:hidden property="obFilial"/>
		<adsm:hidden property="municipioFiliais.municipio.idMunicipio"/>
		<adsm:hidden property="regionalFiliais.regional.idRegional"/>
		<adsm:hidden property="pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa"/>
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:hidden property="findEndereco"/>

		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" serializable="false"/>
		<adsm:hidden property="tpAcesso" serializable="true"/>

		<adsm:lookup label="empresa" labelWidth="21%" dataType="text" size="20" maxLength="20" width="75%" exactMatch="true"
			service="lms.municipios.empresaService.findLookupFilial" property="empresa" idProperty="idEmpresa"
			criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" action="/municipios/manterEmpresas"
		>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
			<adsm:propertyMapping modelProperty="tpEmpresa" criteriaProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping modelProperty="tpEmpresa" relatedProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="50" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:combobox property="empresa.tpEmpresa" label="tipoEmpresa" domain="DM_TIPO_EMPRESA" onchange="return true;" labelWidth="21%" width="29%" onDataLoadCallBack="tpEmpresaCallBack"/>

		<adsm:combobox label="tipoFilial" labelWidth="21%" property="historicoFiliais.tpFilial" domain="DM_TIPO_FILIAL" width="29%" onDataLoadCallBack="tpFilialCallBack"/>

		<adsm:textbox label="sigla" maxLength="3" size="3" dataType="text" property="sgFilial" labelWidth="21%" width="29%" onchange="return onChangeSgFilial(this);" />

		<adsm:complement labelWidth="21%" width="29%" label="identificacao">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
			<adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>

		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" maxLength="50" size="35" labelWidth="21%" width="29%" />
		<adsm:textbox dataType="text" property="pessoa.nmFantasia" label="nomeFantasia" maxLength="60" size="35" labelWidth="21%" width="29%"/>

		<adsm:textbox label="vigenteEm" labelWidth="21%" width="29%" dataType="JTDate" property="historicoFiliais.vigenteEm" picker="true"/>	

		<adsm:combobox property="grupoClassificacaoFiliais.divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao"
			label="grupoClassificacao" service="lms.municipios.manterFiliaisAction.findComboGrupoClassificacao"
			optionLabelProperty="dsGrupoClassificacao" optionProperty="idGrupoClassificacao" labelWidth="21%" width="29%" boxWidth="200"/>
		<adsm:combobox property="grupoClassificacaoFiliais.divisaoGrupoClassificacao.idDivisaoGrupoClassificacao"
			label="divisaoGrupo" service="lms.municipios.manterFiliaisAction.findComboDivisaoGrupoClassificacao"
			optionLabelProperty="dsDivisaoGrupoClassificacao" optionProperty="idDivisaoGrupoClassificacao"
			labelWidth="21%" width="29%" boxWidth="200">
			<adsm:propertyMapping criteriaProperty="grupoClassificacaoFiliais.divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao"
				modelProperty="grupoClassificacao.idGrupoClassificacao"/>
		</adsm:combobox>

		<adsm:range label="dataReal" labelWidth="21%" width="29%" >
			<adsm:textbox dataType="JTDate" property="historicoFiliais.dtRealOperacaoInicial" required="false" picker="true" />
			<adsm:textbox dataType="JTDate" property="historicoFiliais.dtRealOperacaoFinal" picker="true"/>
		</adsm:range>	

		<adsm:textbox property="dtImplantacaoLMS" label="dataImplantaçãoLMS" labelWidth="21%" width="79%" dataType="JTDate"/>					
		<adsm:hidden property="filialReponsavel.empresa.tpEmpresa" serializable="false" value="M"/>

		<adsm:lookup label="filialResponsavel" labelWidth="21%" dataType="text" size="2" maxLength="3" width="8%"
			service="lms.municipios.filialService.findLookup" property="filialByIdFilialResponsavel" idProperty="idFilial"
			criteriaProperty="sgFilial" action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="nomeFilialResp"/>
			<adsm:propertyMapping criteriaProperty="filialReponsavel.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" serializable="false" property="nomeFilialResp" size="50" disabled="true" width="71%"/>

		<adsm:lookup label="filialCiaAerea" labelWidth="21%" dataType="text" size="2" maxLength="3" width="8%"
			service="lms.municipios.filialService.findLookup" property="filialByIdFilialResponsavalAwb" idProperty="idFilial"
			criteriaProperty="sgFilial" action="/municipios/manterFiliais" cellStyle="vertical-align:bottom">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="nomeFilialCiaAerea"/>
			<adsm:propertyMapping criteriaProperty="filialReponsavel.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" serializable="false" property="nomeFilialCiaAerea" size="50" disabled="true" width="71%" cellStyle="vertical-align:bottom"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="filial" />
			<adsm:button caption="limpar" onclick="limpaCampos()" disabled="false" buttonType="resetButton"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		property="filial"
		idProperty="idFilial"
		selectionMode="check"
		gridHeight="180"
		unique="true"
		scrollBars="horizontal"
		rows="9">
			<adsm:editColumn title="hidden" property="endereco.municipio.unidadeFederativa.pais.nmPais" field="hidden" width=""/>
			<adsm:editColumn title="hidden" property="endereco.municipio.unidadeFederativa.pais.idPais" field="hidden" width=""/>
			<adsm:editColumn title="hidden" property="endereco.municipio.unidadeFederativa.idUnidadeFederativa" field="hidden" width=""/>
			<adsm:editColumn title="hidden" property="endereco.municipio.unidadeFederativa.sgUnidadeFederativa" field="hidden" width=""/>
			<adsm:editColumn title="hidden" property="endereco.municipio.unidadeFederativa.nmUnidadeFederativa" field="hidden" width=""/>

 			<adsm:gridColumn title="empresa" property="empresa.pessoa.nmPessoa" width="150"/>
			<adsm:gridColumn title="sigla" property="sgFilial" width="50"/>
			<adsm:gridColumnGroup separatorType="FILIAL">
				<adsm:gridColumn property="lastRegional.sgRegional" width="50" title="regional"/>
				<adsm:gridColumn property="lastRegional.dsRegional" width="100" title=""/>
			</adsm:gridColumnGroup>
			<adsm:gridColumn property="pessoa.tpIdentificacao" width="68" title="identificacao" isDomain="true"/>
			<adsm:gridColumn property="pessoa.nrIdentificacao" width="124" title="" align="right"/>
			<adsm:gridColumn title="filial" property="pessoa.nmFantasia" width="150" />

			<adsm:gridColumnGroup separatorType="FILIAL">
				<adsm:gridColumn property="filialByIdFilialResponsavel.sgFilial" width="50" title="filialResponsavel"/>
				<adsm:gridColumn property="filialByIdFilialResponsavel.pessoa.nmFantasia" width="120" title=""/>
			</adsm:gridColumnGroup>

			<adsm:gridColumnGroup separatorType="FILIAL">
				<adsm:gridColumn property="filialByIdFilialResponsavalAwb.sgFilial" width="80" title="filialCiaAerea"/>
				<adsm:gridColumn property="filialByIdFilialResponsavalAwb.pessoa.nmFantasia" width="180" title=""/>
			</adsm:gridColumnGroup>

			<adsm:gridColumn title="dataImplantaçãoLMS" property="dtImplantacaoLMS" dataType="JTDate" width="150"/>
			<adsm:gridColumn title="tipoFilial" property="lastHistoricoFilial.tpFilial" isDomain="true" width="100"/>
			<adsm:gridColumn title="dataRealInicial" property="lastHistoricoFilial.dtRealOperacaoInicial" dataType="JTDate" width="100"/>
			<adsm:gridColumn title="dataRealFinal" property="lastHistoricoFilial.dtRealOperacaoFinal" dataType="JTDate" width="100"/>
			<adsm:gridColumn property="empresa.tpEmpresa" width="0" title="" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window> 