<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	/**
	 * Seta id da pessoa após inserir um registro.
	 * Desabilita tipo e número de identificação após inserir um registro.
	 */
	function empresaLoad_cb(data, exception){
		onDataLoad_cb(data, exception);
		
		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"), 
										numberElement:document.getElementById("pessoa.nrIdentificacao")});
		
		var idPessoa = getNestedBeanPropertyValue(data, "idEmpresa");
		if (idPessoa != undefined){
			setElementValue("pessoa.idPessoa", idPessoa);
			setDisabled("pessoa.tpIdentificacao", true );
			setDisabled("pessoa.nrIdentificacao", true );
			setDisabled("tpEmpresa", true );
		}
		setDisabled("filiais",(getElementValue("tpEmpresa") == "S" || getElementValue("tpEmpresa") == "F"));
		
		setFocusOnFirstFocusableField(document);	
		verificaTipoEmpresa();
		preencheFilialTomador();
	} 
	
	function preencheFilialTomador() {
		var criteria = new Array();
		var id = document.getElementById("filialTomadorFrete.idFilial").value;
		
		if (id == "") {
			document.getElementById("filialTomadorFrete.nmFilial").value = "";
			return;
		}
		
	    setNestedBeanPropertyValue(criteria, "idFilial", id);
		var sdoFilialTomador = createServiceDataObject("lms.municipios.manterEmpresasAction.findNmSgFilialByIdFilial",
			"preencheFilial", criteria);
		xmit({serviceDataObjects:[sdoFilialTomador]});
	}
	
	function preencheFilial_cb(data, exception){
		if (exception == null) {
			document.getElementById("filialTomadorFrete.nmFilial").value = getNestedBeanPropertyValue(data,'pessoa.nmFantasia');
		} else {
			document.getElementById("filialTomadorFrete.nmFilial").value = "";
		}
	}

	function pageLoadEmpresa() {
   		onPageLoad();
   		verificaTipoEmpresa();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	  			   ,numberElement:document.getElementById("pessoa.idPessoa")});
	}

	function onChangeSgEmpresa(field) {
		field.value = field.value.toUpperCase();
		return validate(field);
	}
	
	// LMS-4599  A checkbox "Pode Agendar Entrega" deve ficar habilitada  apenas se o valor da combobox "Tipo da empresa:" for Parceiro. 
	// Em qualquer outra situação ele deve estar desabilitado e não selecionado.
	function verificaTipoEmpresa(){
		// Se o tipo da empresa for igual a Parceira, então desabilita a checkbox 'pode agendar'
		if(document.getElementById("tpEmpresa").value == "P"){
			setDisabled("blPodeAgendar", false );
		}else{
			resetValue("blPodeAgendar");
			setDisabled("blPodeAgendar", true );
		}
		
		if(document.getElementById("tpEmpresa").value == "C"){
			setDisabled("filialTomadorFrete.idFilial", false);
			setDisabled("nrContaCorrente", false);
			document.getElementById("filialTomadorFrete.idFilial").required = true;
			document.getElementById("nrContaCorrente").required = true;
		}else{
			document.getElementById("filialTomadorFrete.idFilial").required = false;
			document.getElementById("nrContaCorrente").required = false;
			setDisabled("filialTomadorFrete.idFilial", true);
			setDisabled("nrContaCorrente", true);
		}
		
		setSiglaObrigatoria();
	}
	
	function setSiglaObrigatoria(){
		if(document.getElementById("tpEmpresa").value == "C"){
			document.getElementById("sgEmpresa").required = true;
		}else{
			document.getElementById("sgEmpresa").required = false;
		}
	}

</script>
<adsm:window service="lms.municipios.empresaService" onPageLoadCallBack="pageLoadCustom" onPageLoad="pageLoadEmpresa">
	<adsm:i18nLabels>
		<adsm:include key="empresa"/>
	</adsm:i18nLabels>

	<adsm:form idProperty="idEmpresa" action="/municipios/manterEmpresas"
			onDataLoadCallBack="empresaLoad" service="lms.municipios.empresaService.findByIdDetalhamento" >
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		<adsm:hidden property="labelPessoa"/>
		
		<adsm:complement label="identificacao" labelWidth="18%" width="82%" required="true" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" onchange="return changeTpIdentificacao(this);" />
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" 
					service="lms.municipios.empresaService.validateIdentificacao"
					onDataLoadCallBack="pessoaCallback"
					onchange="return changeNrIdentificacao(this);" />
		</adsm:complement>

		<adsm:textbox
			label="razaoSocial"
			property="pessoa.nmPessoa"
			dataType="text"
			maxLength="50"
			size="35"
			required="true"
			labelWidth="18%"
			width="32%"
			depends="pessoa.nrIdentificacao"
		/>

		<adsm:textbox
			label="sigla"
			property="sgEmpresa"
			dataType="text"
			onchange="return onChangeSgEmpresa(this);"
			maxLength="3"
			size="2"
			labelWidth="18%"
			width="25%"
		/>

		<adsm:textbox
			dataType="text"
			property="pessoa.nmFantasia"
			label="nomeFantasia"
			maxLength="60"
			size="35"
			required="true"
			labelWidth="18%"
			width="82%"
			depends="pessoa.nrIdentificacao"
		/>

		<adsm:combobox property="tpEmpresa" label="tipoEmpresa" domain="DM_TIPO_EMPRESA" required="true" labelWidth="18%" width="32%" onchange="verificaTipoEmpresa();" />
		<adsm:textbox dataType="text" property="dsHomePage" label="homepage" maxLength="60" size="35" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email" maxLength="60" size="35" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="decimal" property="pcTaxaCombustivel" label="taxaDeCombustivel" mask="##0.00" size="11" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="integer" property="nrDac" label="DAC" maxLength="11" size="11" labelWidth="18%" width="32%" />
	 	<adsm:textbox dataType="integer" property="cdIATA" label="IATA" maxLength="4" size="4" labelWidth="18%" width="32%" />
	 	<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" width="82%" required="true" />
	 	
	 	<adsm:checkbox property="blPodeAgendar" serializable="true" 
					label="podeAgendarEntrega" labelWidth="18%" width="82%"
					cellStyle="vertical-align:bottom" />
					
        <adsm:lookup property="filialTomadorFrete" idProperty="idFilial" criteriaProperty="sgFilial" 
				dataType="text" size="3" maxLength="3" 
				labelWidth="18%"
				service="lms.municipios.consultarRotasViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialTomadorFrete" width="82%"
				exactMatch="true">
			<adsm:propertyMapping relatedProperty="filialTomadorFrete.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialTomadorFrete.nmFilial" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
		<adsm:textbox dataType="integer" property="nrContaCorrente" label="contaCiaAerea" maxLength="11" size="11" labelWidth="18%" width="82%" />
	 	
		<adsm:buttonBar>
			<adsm:button caption="filiais" boxWidth="60" onclick="openFiliais()" id="filiais" >
			</adsm:button>
			<adsm:button caption="inscricoesEstaduais" id="buttonInscricaoEstadual" action="/configuracoes/manterInscricoesEstaduais" cmd="main" boxWidth="120">
				<adsm:linkProperty src="idEmpresa" target="pessoa.idPessoa"/>			
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa"/>
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>						
			</adsm:button>
			<adsm:button caption="enderecos" action="configuracoes/manterEnderecoPessoa" cmd="main" boxWidth="75">
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="contatos" action="configuracoes/manterContatos" cmd="main" >
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp" />
			</adsm:button>
			<adsm:button caption="dadosBancarios" action="configuracoes/manterDadosBancariosPessoa" boxWidth="105" cmd="main" >
				<adsm:linkProperty src="idEmpresa" target="empresa.pessoa.idEmpresa" />
				<adsm:linkProperty src="pessoa.idPessoa" target="pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="pessoa.nmPessoa" />
				<adsm:linkProperty src="labelPessoa" target="labelPessoaTemp"/>	
			</adsm:button>
			<adsm:button caption="permissos" action="municipios/manterPermissosEmpresaPaises" cmd="main" >
				<adsm:linkProperty src="idEmpresa" target="empresa.idEmpresa" />
				<adsm:linkProperty src="pessoa.idPessoa" target="empresa.pessoa.idPessoa" />
				<adsm:linkProperty src="pessoa.nrIdentificacao" target="empresa.pessoa.nrIdentificacao" />
				<adsm:linkProperty src="pessoa.nmPessoa" target="empresa.pessoa.nmPessoa" />
			</adsm:button>
			<adsm:button caption="salvar" buttonType="storeButton" service="lms.municipios.manterEmpresasAction.store" callbackProperty="customStore" />
			<adsm:newButton />
			<adsm:removeButton service="lms.municipios.empresaService.removeEmpresaById"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
	document.getElementById("labelPessoa").masterLink = "true";
	document.getElementById("pessoa.nrIdentificacao").serializable = true;
	var labelPessoaTemp = getI18nMessage("empresa");

	function pageLoadCustom_cb(data, error) {
		onPageLoad_cb(data, error);
		
		setElementValue("labelPessoa", labelPessoaTemp);
		changeTypePessoaWidget(
				{tpTipoElement : getElement("pessoa.tpPessoa"),
				 tpIdentificacaoElement : getElement('pessoa.tpIdentificacao'),
				 numberElement : getElement('pessoa.nrIdentificacao'),tabCmd:'cad'});
	}

	/**
	 * Ao detalhar um registro deve desabilitar tipo e número de identificação.
	 */
	function initWindow(eventObj) {
		var blDetalhamento = (eventObj.name == "gridRow_click" || eventObj.name == "storeButton");

		setDisabled(document.getElementById("pessoa.tpIdentificacao"),blDetalhamento);			
		setDisabled(document.getElementById("tpEmpresa"),blDetalhamento);
		setDisabled(document.getElementById("pessoa.nrIdentificacao"),true);
		setFocusOnFirstFocusableField(document);
		verificaTipoEmpresa();
	} 

	function pessoaCallback_cb(data,error) {
		if (error != undefined) {
			alert(error);
			resetValue("pessoa.nrIdentificacao");
			setFocus(document.getElementById('pessoa.nrIdentificacao'));
			return false;
		}
		if (data != undefined && data.length == 1)
			setElementValue("pessoa.nmFantasia",data[0].nmFantasia);
		return pessoa_nrIdentificacao_exactMatch_cb(data);
	}

	function buildLinkPropertiesQueryString_filiais() {
		var qs = "";
		qs += "&empresa.idEmpresa=" + document.getElementById("pessoa.idPessoa").value;
		qs += "&empresa.pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;
		qs += "&empresa.pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").value;
		qs += "&empresa.tpEmpresa=" + document.getElementById("tpEmpresa").value;

		return qs;
	}

	function buildLinkPropertiesQueryString_filiaisCia() {
		var qs = "";
		qs += "&empresa.idEmpresa=" + document.getElementById("pessoa.idPessoa").value;
		qs += "&idEmpresaTemp=" + document.getElementById("pessoa.idPessoa").value;
		qs += "&empresa.pessoa.nrIdentificacao=" + document.getElementById("pessoa.nrIdentificacao").value;
		qs += "&empresa.pessoa.nmPessoa=" + document.getElementById("pessoa.nmPessoa").value;
		qs += "&empresa.tpEmpresa=" + document.getElementById("tpEmpresa").value;

		return qs;
	}

	function openFiliais() {
		if (document.forms[0].elements["tpEmpresa"].value == "C")
			parent.parent.redirectPage('municipios/manterFiliaisCiaAerea.do?cmd=main' + buildLinkPropertiesQueryString_filiaisCia());
		else
			parent.parent.redirectPage('municipios/manterFiliais.do?cmd=main' + buildLinkPropertiesQueryString_filiais());
	}

	function customStore_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			return false;
		}

		store_cb(data,exception);

		if (data != undefined) {
			setElementValue('idEmpresa',getNestedBeanPropertyValue(data,'idEmpresa'));
			setElementValue('pessoa.idPessoa',getNestedBeanPropertyValue(data,'idEmpresa'));
			setDisabled("filiais",(getElementValue("tpEmpresa") == "S" || getElementValue("tpEmpresa") == "F"));
		}

		setFocusOnNewButton();
	}

	function changeTpIdentificacao(elem) {
		if (elem.value == "") {
			resetValue("pessoa.nmFantasia");
		}
		return changeIdentificationTypePessoaWidget({tpIdentificacaoElement:elem, numberElement:document.getElementById('pessoa.idPessoa'), tabCmd:'cad'});
	}
	
	function changeNrIdentificacao(elem) {
		if (elem.value == "") {
			resetValue("pessoa.nmFantasia");
		}
		return pessoa_nrIdentificacaoOnChangeHandler();
	}

</script>