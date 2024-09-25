<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.substAtendimentoFilialService">
	<adsm:form action="/municipios/manterSubstituicaoTemporariaDestino" idProperty="idSubstAtendimentoFilial" onDataLoadCallBack="substLoad" service="lms.municipios.substAtendimentoFilialService.findByIdDetalhamento">
		<adsm:hidden property="tpEmpresa" serializable="false" value="M"/>
		<adsm:hidden property="filialFiltro.idFilial" serializable="false" value=""/>
		<adsm:hidden property="tpSituacaoAtivo" serializable="false" value="A"/>
		
		<adsm:combobox labelWidth="15%" width="70%" property="tpDesvioCarga" label="tpDesvioCarga" domain="DM_TIPO_DESVIO_CARGA" disabled="false"/>		
		
	 	<adsm:lookup label="filialDestino" labelWidth="15%" dataType="text" size="2" maxLength="3" width="35%" onPopupSetValue="implLookupPais1PopUp" onDataLoadCallBack="implLookupPais1DataLoad"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilialDestino" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
                  <adsm:textbox dataType="text" serializable="false"  property="filialByIdFilialDestino.pessoa.nmFantasia" size="28" disabled="true" />
        </adsm:lookup>
				      	
	 	<adsm:lookup label="substituirPelaFilial" dataType="text" size="2" maxLength="3" width="35%"  onPopupSetValue="implLookupPais2PopUp" onDataLoadCallBack="implLookupPais2DataLoad"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilialDestinoSubstituta" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestinoSubstituta.pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
                  <adsm:textbox dataType="text" serializable="false" property="filialByIdFilialDestinoSubstituta.pessoa.nmFantasia" size="28" disabled="true" />
        </adsm:lookup>
		      
		<adsm:section caption="configuracaoOperacional" />
		<adsm:lookup property="unidadeFederativa"  
					idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" onPopupSetValue="implLookupUfPopUp" onDataLoadCallBack="implLookupUfDataLoad" onchange="return implLookupUfChange()"
				 	service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					width="7%" label="ufOrigem" size="2" maxLength="10" labelWidth="15%"  
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="68%" size="28" serializable="false" disabled="true"/>

	   	<adsm:combobox label="regionalOrigem" property="regional.idRegional" optionLabelProperty="siglaDescricao" optionProperty="idRegional"
	   			service="lms.municipios.regionalService.findRegionaisVigentes" labelWidth="15%" width="35%"
	   			boxWidth="215"
	   			onchange="implRgChange(this);" />

      	<adsm:range label="vigencia" labelWidth="15%">
			<adsm:textbox size="12" property="dtVigenciaRegionalInicial" dataType="JTDate" picker="false" disabled="true"/>
			<adsm:textbox size="12" property="dtVigenciaRegionalFinal" dataType="JTDate" picker="false" disabled="true"/>
		</adsm:range>

		<adsm:lookup label="filialOrigem" labelWidth="15%" dataType="text" size="2" maxLength="3" width="7%"
				onPopupSetValue="implLookupOrigemPopUp" onDataLoadCallBack="implLookupOrigemDataLoad" onchange="return implLookupOrigemChange();"
				service="lms.municipios.filialService.findLookup" property="filialByIdFilialOrigem" idProperty="idFilial"
				criteriaProperty="sgFilial" action="/municipios/manterFiliais">
	        <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
	        <adsm:propertyMapping modelProperty="idFilial" relatedProperty="filtroCliente"/>
	        <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
        </adsm:lookup>
      	<adsm:textbox dataType="text" serializable="false" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="28" disabled="true" width="68%"/>
      	
		<adsm:lookup dataType="text" property="municipio.municipioFiliais" idProperty="municipio.idMunicipio" criteriaProperty="municipio.nmMunicipio"
	             action="/municipios/manterMunicipiosAtendidos" service="lms.municipios.municipioFilialService.findLookup"
                 maxLength="30" size="38" minLengthForAutoPopUpSearch="3" exactMatch="false"
                 label="municipioDestino" labelWidth="15%" width="35%" serializable="false">
                 <adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial"/>
                 <adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial"/>
                 <adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
                 <adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
        </adsm:lookup>
        <adsm:hidden property="municipio.idMunicipio"/>
        

		<adsm:combobox property="servico.idServico" label="servico" service="lms.configuracoes.servicoService.find"
				optionLabelProperty="dsServico" optionProperty="idServico" boxWidth="200" onlyActiveValues="true" >
			<%--adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filialServicos.filial.idFilial"/--%>
		</adsm:combobox>
		<adsm:combobox property="naturezaProduto.idNaturezaProduto" label="natureza"
				service="lms.expedicao.naturezaProdutoService.find" onlyActiveValues="true" optionLabelProperty="dsNaturezaProduto"
				optionProperty="idNaturezaProduto" labelWidth="15%" width="80%" />

		<adsm:lookup service="lms.vendas.clienteService.findLookup" onDataLoadCallBack="clienteExactMatch"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" property="cliente" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="clienteRemetente" action="/vendas/manterDadosIdentificacao"
			size="20" maxLength="20" labelWidth="15%" width="19%">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo" modelProperty="tpSituacao"/>
        </adsm:lookup>
        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="35" disabled="true" width="62%" serializable="false"/>
 
 
		<adsm:range label="vigencia" labelWidth="15%" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        <adsm:hidden property="filtroCliente" serializable="false"/>
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.substAtendimentoFilialService.storeMap" />
			<adsm:newButton id="botaoLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		<Script>
		<!--
			var msgLMS29040 = "<adsm:label key="LMS-29040"/>";
			var msgLMS29046 = "<adsm:label key="LMS-29046"/>";
			var msgLMS29047 = "<adsm:label key="LMS-29047"/>";
		//-->
		</Script>
	</adsm:form>
</adsm:window>  
<Script>
<!-- 
	// funções responsaveis por interpretar o retorno do campo UF
	function implLookupUfDataLoad_cb(data) {
		unidadeFederativa_sgUnidadeFederativa_exactMatch_cb(data);
		implReturnUF(getElementValue("unidadeFederativa.idUnidadeFederativa"));
		return true;
	}
	function implLookupUfChange() {
		var flag = unidadeFederativa_sgUnidadeFederativaOnChangeHandler();
		if (getElementValue("unidadeFederativa.sgUnidadeFederativa") == "")
			implReturnUF();
		return flag;
	}
	function implLookupUfPopUp(data) {
		if (getElementValue("unidadeFederativa.idUnidadeFederativa") != 
			getNestedBeanPropertyValue(data,"idUnidadeFederativa"))
			implReturnUF(getNestedBeanPropertyValue(data,"idUnidadeFederativa"));
		return true;
	}
	//função responsavel  por limpar o campo filial de origem e desabilitar a regional
	function implReturnUF(value) {
		if (value != undefined && value != "") {
			//document.getElementById("regional.idRegional").selectedIndex = 0;
			resetValue("regional.idRegional");
			setElementValue("filialFiltro.idFilial",getElementValue("regional.idRegional"));
			clearFilial();
		}
	}
	
	function clienteExactMatch_cb(data, error){
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data, error);

		if (data != undefined){
			var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			if (nrIdentificacaoFormatado != undefined)
				document.getElementById("cliente.pessoa.nrIdentificacao", nrIdentificacaoFormatado).value = nrIdentificacaoFormatado;
		}
		
	}
	
	//Função limpa filial origem
	function clearFilial() {	
		resetValue("filialByIdFilialOrigem.idFilial");
		resetValue("filialByIdFilialOrigem.sgFilial");
		resetValue("filialByIdFilialOrigem.pessoa.nmFantasia");
	}
	
	// Função que limpa Unidade Federativa de Origem
	function clearUf() {
		resetValue("unidadeFederativa.idUnidadeFederativa");
		resetValue("unidadeFederativa.sgUnidadeFederativa");
		resetValue("unidadeFederativa.nmUnidadeFederativa");
	}
		
	//função responsavel por habilitar os campos
	
	function implRgChange(field) {
		comboboxChange({e:field});
		if (field.selectedIndex > 0) {
			clearUf();
			setElementValue("filialFiltro.idFilial",getElementValue("regional.idRegional"));
			clearFilial();
		}else{
			setDisabled("unidadeFederativa.idUnidadeFederativa",false);
			setElementValue("filialFiltro.idFilial","");
		}
		if (getElementValue("filialByIdFilialOrigem.idFilial") == "")
			document.getElementById("filtroCliente").value = getElementValue("regional.idRegional");
		loadVigenciaRegional(field);
	}
	
	function loadVigenciaRegional(field) {
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaRegionalInicial",setFormat(document.getElementById("dtVigenciaRegionalInicial"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaRegionalFinal",setFormat(document.getElementById("dtVigenciaRegionalFinal"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaRegionalInicial");
			resetValue("dtVigenciaRegionalFinal");
		}
	}
	
	function clickPickerCliente() {
		updateFilialFiltro();
		lookupClickPicker({e:document.forms[0].elements['cliente.idCliente']});	
	}
	function changeCliente() {
		updateFilialFiltro();
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function updateFilialFiltro() {
		var id = "";
		if (getElementValue("filialByIdFilialOrigem.idFilial") != "") 
			id = getElementValue("filialByIdFilialOrigem.idFilial");
		else if (getElementValue("regional.idRegional") != "") 
			id = getElementValue("regional.idRegional");
		setElementValue("filialFiltro.idFilial",id);
	}
	
	/*
	 *
	 *
	 */
	function substLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		acaoVigencia(data);
		loadVigenciaRegional(document.getElementById("regional.idRegional"));
	}
	
	function afterStore_cb(data,exception) {
		store_cb(data,exception);
		if (exception == undefined) {
			acaoVigencia(data);
			setFocus(document.getElementById("botaoLimpar"),false);
		}
	}
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			enabledFields();
			setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 1) {
		    setDisabled(document,true);
		    setDisabled("__buttonBar:0.storeButton",false);
		    setDisabled("botaoLimpar",false);
		    setDisabled("dtVigenciaFinal",false);
		    setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 2) {
		    setDisabled(document,true);
		    setDisabled("botaoLimpar",false);
		    setFocus(document.getElementById("botaoLimpar"),false);
		}
	}
	
// função executa em cada acesso da pagina...
	function initWindow(eventObj) {
		if ((eventObj.name != "storeButton") && (eventObj.name == "gridRow_click")) {
			enabledFields();
			setDisabled("municipio.municipioFiliais.municipio.idMunicipio",false);
		}
		if (eventObj.name == "newButton_click" || eventObj.name == "tab_click")
			enabledFields();
	}   
	
	function enabledFields() {

		setDisabled("tpDesvioCarga",false);
		setDisabled("botaoLimpar",false);
		setDisabled("__buttonBar:0.storeButton",false);
		setDisabled("regional.idRegional",false);
		setDisabled("unidadeFederativa.idUnidadeFederativa",false);
		setDisabled("filialByIdFilialDestino.idFilial",false);
		setDisabled("filialByIdFilialDestinoSubstituta.idFilial",false);
		setDisabled("unidadeFederativa.idUnidadeFederativa",false);
		setDisabled("regional.idRegional",false);
		setDisabled("filialByIdFilialOrigem.idFilial",false);
		setDisabled("municipio.idMunicipio",false);
		setDisabled("servico.idServico",false);
		setDisabled("naturezaProduto.idNaturezaProduto",false);
		setDisabled("cliente.idCliente",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setDisabled("municipio.municipioFiliais.municipio.idMunicipio",false);
		setFocusOnFirstFocusableField();
	}
	
	//FUNÇÕES POR VERIFICAR SE A FILIAIS DUPLICADAS	
	//filialByIdFilialDestino	
	function implLookupPais1DataLoad_cb(data) {
		if (!verificaPais("filialByIdFilialDestino",getNestedBeanPropertyValue(data,":0.idFilial")))
			return false;
		return filialByIdFilialDestino_sgFilial_exactMatch_cb(data);
	}
	function implLookupPais1PopUp(data) {
		return verificaPais("filialByIdFilialDestino",getNestedBeanPropertyValue(data,"idFilial"));
	}
	//filialByIdFilialDestinoSubstituta
	function implLookupPais2DataLoad_cb(data) {
		if (!verificaPais("filialByIdFilialDestinoSubstituta",getNestedBeanPropertyValue(data,":0.idFilial")))
			return false;
		return filialByIdFilialDestinoSubstituta_sgFilial_exactMatch_cb(data);

	}
	function implLookupPais2PopUp(data) {
		return verificaPais("filialByIdFilialDestinoSubstituta",getNestedBeanPropertyValue(data,"idFilial"));
	}
	//filialByIdFilialOrigem	
	function implLookupOrigemDataLoad_cb(data) {
		implReturnOrigem(getNestedBeanPropertyValue(data,":0.idFilial"));
		return filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	}
	
	function implLookupOrigemPopUp(data) {
		if (getElementValue("filialByIdFilialOrigem.idFilial") != 
			getNestedBeanPropertyValue(data,"idFilial"))
			implReturnOrigem(getNestedBeanPropertyValue(data,"idFilial"));
		return verificaPais("filialByIdFilialOrigem",getNestedBeanPropertyValue(data,"idFilial"));
	}
	
	function implLookupOrigemChange() {
		if (getElementValue("filialByIdFilialOrigem.sgFilial") == "") {
			setElementValue("filtroCliente",getElementValue("regional.idRegional"));
			implReturnOrigem();
		}
		return filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	//função responsavel  por limpar o campo filial de origem e desabilitar a regional
	function implReturnOrigem(value) {
		if (value != undefined && value != "") {
			resetValue("regional.idRegional");
			resetValue("dtVigenciaRegionalInicial");
			resetValue("dtVigenciaRegionalFinal");
			clearUf()
		}
	}

	function verificaPais(fieldName,fieldValue) {
		var filiais = new Array(3);
		filiais[0] = "filialByIdFilialDestino";
		filiais[1] = "filialByIdFilialDestinoSubstituta";
		filiais[2] = "filialByIdFilialOrigem";
		for(x = 0; x < filiais.length; x++) {
			if (filiais[x] != fieldName) {
				if (getElementValue(filiais[x] + ".idFilial") == fieldValue && fieldValue != "") {
					if (fieldName == "filialByIdFilialOrigem")
						alert(msgLMS29047);
					else if (fieldName == "filialByIdFilialDestinoSubstituta")
						alert(msgLMS29046);
					else
						alert(msgLMS29040);
					return false;
				}
			}
		}
		return true;
	}
//-->
</Script>