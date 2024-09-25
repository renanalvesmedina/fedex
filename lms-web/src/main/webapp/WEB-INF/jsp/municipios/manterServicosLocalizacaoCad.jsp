<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarServicosLocalizacao" service="lms.municipios.manterServicosLocalizacaoAction">
	<adsm:form action="/municipios/manterServicosLocalizacao" idProperty="idOperacaoServicoLocaliza" 
			   service="lms.municipios.manterServicosLocalizacaoAction.findByIdDetalhado" onDataLoadCallBack="pageLoad">
        
        <adsm:hidden property="flag" serializable="false" value="01"/>
		<adsm:hidden property="tpSituacaoAtiva" value="A" />
        <adsm:lookup property="filial.empresa" idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacao" onDataLoadCallBack="empresa_dataLoad"
				service="lms.municipios.manterServicosLocalizacaoAction.findLookupEmpresa" dataType="text" label="empresa" size="18" action="/municipios/manterEmpresas" onPopupSetValue="empresa_onPopup"
				labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="3" exactMatch="true" maxLength="18" disabled="false" serializable="false">
			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtiva" modelProperty="tpSituacao" />
			<adsm:textbox property="filial.empresa.pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
        
        <adsm:lookup property="municipioFilial" 
				service="lms.municipios.manterServicosLocalizacaoAction.findLookupMunicipioFilial" 
				dataType="text" 
				disabled="false" 
				criteriaProperty="municipio.nmMunicipio" 
				label="municipio" size="41" maxLength="30" 
				action="/municipios/manterMunicipiosAtendidos" 
				labelWidth="17%" width="83%" 
				idProperty="idMunicipioFilial" 
				exactMatch="false" minLengthForAutoPopUpSearch="3" required="true" serializable="true"> 
        	<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false" addChangeListener="false"/>
        	<adsm:propertyMapping criteriaProperty="filial.empresa.idEmpresa" modelProperty="filial.empresa.idEmpresa" />
 			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nmPessoa" modelProperty="filial.empresa.pessoa.nmPessoa" inlineQuery="false"/>
 			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="filial.empresa.pessoa.nrIdentificacao" inlineQuery="false"/>
 			<adsm:propertyMapping relatedProperty="filial.empresa.idEmpresa" modelProperty="filial.empresa.idEmpresa" blankFill="false" />
 			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="filial.empresa.pessoa.nmPessoa" blankFill="false" />
 			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="filial.empresa.pessoa.nrIdentificacao" blankFill="false" />
			<%--adsm:propertyMapping criteriaProperty="municipioFilial.municipio.tpSituacao" modelProperty="municipio.tpSituacao" /--%>					
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />			
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.siglaDescricao" modelProperty="municipio.unidadeFederativa.siglaDescricao" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.blDistrito" modelProperty="municipio.blDistrito" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.municipioDistrito.nmMunicipio" modelProperty="municipio.municipioDistrito.nmMunicipio" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.idMunicipio" modelProperty="municipio.idMunicipio" />
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.siglaNomeFilial" modelProperty="filial.siglaNomeFilial"/>
			<adsm:hidden property="municipioFilial.filial.siglaNomeFilial" serializable="false"/>
			<adsm:hidden property="municipioFilial.municipio.idMunicipio" serializable="true"/>
		</adsm:lookup> 
    
    	<adsm:lookup label="uf" property="municipioFilial.municipio.unidadeFederativa" 
				action="/municipios/manterUnidadesFederativas" idProperty="idUnidadeFederativa" 
				service="lms.municipios.manterServicosLocalizacaoAction.findLookupUnidadeFederativa" 
				dataType="text" criteriaProperty="sgUnidadeFederativa" 
				size="3" maxLength="3" exactMatch="true" labelWidth="17%" width="33%" 
				minLengthForAutoPopUpSearch="2" serializable="false" picker="false" disabled="true">
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="34" maxLength="30" disabled="true" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilial.municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais"/>
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" />
			<adsm:hidden property="municipioFilial.municipio.unidadeFederativa.siglaDescricao" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup service="lms.municipios.manterServicosLocalizacaoAction.findLookupPais" 
					 dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais" 
					 criteriaProperty="nmPais" idProperty="idPais" label="pais" size="37" 
					 action="/municipios/manterPaises" labelWidth="17%" width="33%"
					 serializable="false" disabled="true" picker="false">
		</adsm:lookup>
		
		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		
		<adsm:lookup property="municipioFilial.municipio.municipioDistrito" label="municDistrito" 
					 service="lms.municipios.manterServicosLocalizacaoAction.findLookupMunicipio" 
					 dataType="text" disabled="true" picker="false" 
					 criteriaProperty="nmMunicipio" size="37" maxLength="30" action="/municipios/manterMunicipios"  
					 idProperty="idMunicipio" exactMatch="false" minLengthForAutoPopUpSearch="3" 
					 labelWidth="17%" width="33%" serializable="false">
			<adsm:hidden property="indicadorDistrito" value="N" serializable="false"/>
			<adsm:propertyMapping criteriaProperty="indicadorDistrito" modelProperty="blDistrito" />
		</adsm:lookup>

		<adsm:hidden property="municipioFilial.filial.idFilial"/>
		<adsm:textbox property="municipioFilial.filial.sgFilial" dataType="text" serializable="false" label="filial" size="3" maxLength="3" labelWidth="17%" width="5%" disabled="true"/>
		<adsm:textbox property="municipioFilial.filial.pessoa.nmFantasia" dataType="text" disabled="true"  size="29" serializable="false" width="60%"/>
		     		
		<adsm:combobox property="tpOperacao" label="tipoOperacao" domain="DM_TIPO_OPERACAO_COLETA_ENTREGA" width="33%" labelWidth="17%" required="true"/>
		<adsm:hidden property="tpOperacao.descricao"/>
		
		<adsm:combobox property="servico.idServico" optionProperty="idServico" optionLabelProperty="dsServico" label="servico" 
					   service="lms.municipios.manterServicosLocalizacaoAction.findServico" 
					   onlyActiveValues="true" width="33%" labelWidth="17%" boxWidth="230" />
		<adsm:hidden property="servico.dsServico" serializable="false"/>
		
		<adsm:combobox property="tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio" label="tipoLocalizacao"  
					   service="lms.municipios.manterServicosLocalizacaoAction.findTipoLocalizacaoMunicipio" 
					   onlyActiveValues="true" optionLabelProperty="dsTipoLocalizacaoMunicipio" optionProperty="idTipoLocalizacaoMunicipio" width="83%" labelWidth="17%" boxWidth="200" required="true" />
		<adsm:hidden property="tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio" serializable="false"/>
		
		<adsm:combobox property="tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio" label="tipoLocalizacaoFob"  
					   service="lms.municipios.manterServicosLocalizacaoAction.findTipoLocalizacaoMunicipioFob" 
					   onlyActiveValues="true" optionLabelProperty="dsTipoLocalizacaoMunicipio" optionProperty="idTipoLocalizacaoMunicipio" width="83%" labelWidth="17%" boxWidth="200" required="true" />
		<adsm:hidden property="tipoLocalizacaoMunicipioFob.dsTipoLocalizacaoMunicipio" serializable="false"/>

		<adsm:textbox dataType="integer" property="nrTempoColeta" label="tempoColeta" maxLength="3" size="4" width="33%" labelWidth="17%" unit="horas2" />
		<adsm:textbox dataType="integer" property="nrTempoEntrega" label="tempoEntrega" maxLength="3" size="4" width="33%" labelWidth="17%" unit="horas2" />

		<adsm:checkbox property="blAtendimentoGeral" label="atendimentoGeral" width="33%" labelWidth="17%"/>

   		<adsm:multicheckbox texts="dom|seg|ter|qua|qui|sex|sab|" property="blDomingo|blSegunda|blTerca|blQuarta|blQuinta|blSexta|blSabado|" 
                            align="top" label="frequencia" width="83%" labelWidth="17%" />

		<adsm:checkbox property="blCobraTaxaFluvial" label="indTaxaFluvial" labelWidth="17%" width="33%"/>
		<adsm:checkbox property="blAceitaFreteFob" label="aceitaFreteFOB" labelWidth="17%" width="33%"/>
 
		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:button id="botaoAtendimentosClientes" caption="atendimentosClientes" onclick="parent.parent.redirectPage('municipios/manterAtendimentosEspecificosClientes.do?cmd=main' + buildLinkPropertiesQueryString_atendimentosClientes());" boxWidth="155" breakBefore="false">
			</adsm:button>
			<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.manterServicosLocalizacaoAction.storeCustom" />			
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function empresa_dataLoad_cb(data){
	
		if (data != undefined && data.length >= 1){
			var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
		}
		
		filial_empresa_pessoa_nrIdentificacao_exactMatch_cb(data);
		
	}
	
	function empresa_onPopup(data){
	
		var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
			
		return true;
		
	}
	function implLookupMunicipioChange() {
		var flag = municipioFilial_municipio_nmMunicipioOnChangeHandler();
	
		if (getElementValue("municipioFilial.municipio.nmMunicipio") == "") {
			disabledFilial();
		} else {
			setDisabled("municipioFilial.idMunicipioFilial", false);
		}
		return flag;
	}	
		
	function implLookupMunicipioPopUp(data) {
		if (getElementValue("municipioFilial.municipio.nmMunicipio") == "") {
			setDisabled("municipioFilial.idMunicipioFilial", false);
		}
		
		return true;
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
    	
		if (exception == undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");
			var store= "true";
		    comportamentoDetalhe(acaoVigenciaAtual, store);
		    if (document.getElementById("blAtendimentoGeral").checked == true)
		    	setDisabled("botaoAtendimentosClientes", true);
		   }
	}
	
	function implLookupMunicipioCallBack_cb(data,exception) {
		var vlrIni = getElementValue("municipioFilial.municipio.idMunicipio");
		var flag = municipioFilial_municipio_nmMunicipio_exactMatch_cb(data)
		
		if (flag && getElementValue("municipioFilial.municipio.idMunicipio") != vlrIni)
			disabledFilial();
		return flag;
	}

	
	
	function preencheHiddenServico() {
		if (document.getElementById("servico.idServico").selectedIndex > 0){
			var valor = document.getElementById("servico.idServico").options[document.getElementById("servico.idServico").selectedIndex].text;
			document.getElementById("servico.dsServico").value = valor;
		}
	}
	
	function preencheHiddenTipoLocalizacao() {
		var valor = getElement("tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio").options[getElement("tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio").selectedIndex].text;
		setElementValue("tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio", valor);
	}
	
	function preencheHiddenTipoLocalizacaoFob() {
		var valor = getElement("tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio").options[getElement("tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio").selectedIndex].text;
		setElementValue("tipoLocalizacaoMunicipioFob.dsTipoLocalizacaoMunicipio", valor);
	}
		
	function preencheHiddenTipoOperacao() {
	   if (document.getElementById("tpOperacao")!= ''){
         var e = document.getElementById("tpOperacao");
         var dsTipo = e.options[e.selectedIndex].text;
         setElementValue("tpOperacao.descricao", dsTipo); 
   		}
	}
	
	
	
	// ####################################################################################
	// Só habilita a filial (municipioFilial) quando estiver preenchido o municipio
	// ####################################################################################
	function disabledFilial() {
		document.getElementById("municipioFilial.filial.sgFilial").value = ''; 
		document.getElementById("municipioFilial.idMunicipioFilial").value = ''; 
		document.getElementById("municipioFilial.filial.pessoa.nmPessoa").value = '';
		setDisabled("municipioFilial.idMunicipioFilial",getElementValue("municipioFilial.municipio.idMunicipio") == "")
	}
	
	// #####################################################################
	// tratamento do comportamento padrão da vigência
	// #####################################################################
	function pageLoad_cb(data, exception) {

		if (exception != undefined) {
			alert(exception);
			return;
		}
		onDataLoad_cb(data, exception); 
		
		preencheHiddenServico();
		preencheHiddenTipoLocalizacao();
		preencheHiddenTipoLocalizacaoFob();
		preencheHiddenTipoOperacao();
				
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");		
		comportamentoDetalhe(acaoVigenciaAtual, null);		
		
		if (getElementValue("idOperacaoServicoLocaliza") != "" && !document.getElementById("blAtendimentoGeral").checked) {
			setDisabled("__buttonBar:0_1", false);
		} else {
			setDisabled("__buttonBar:0_1", true);
		}
	
	}
	
	function comportamentoDetalhe(acaoVigenciaAtual, tipoEvento) {
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("municipioFilial.idMunicipioFilial",false); 
			document.getElementById("municipioFilial.municipio.nmMunicipio").previousValue = document.getElementById("municipioFilial.municipio.nmMunicipio").value;
			if(tipoEvento == "" ||  tipoEvento == null)
		    	setFocusOnFirstFocusableField(document);
		    else
		       setFocusOnNewButton(document);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("__buttonBar:0.storeButton", false);
			setDisabled("__buttonBar:0.removeButton", true);
			setDisabled("__buttonBar:0_1", false); 
			setDisabled("dtVigenciaFinal", false);
			if(tipoEvento == "" ||  tipoEvento == null)
		    	setFocusOnFirstFocusableField(document);
		    else
		       setFocusOnNewButton(document);
			
		} else {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setFocusOnNewButton(document);
		} 
	}
	
	function estadoNovo() {
		setDisabled("municipioFilial.municipio.idMunicipio",document.getElementById("municipioFilial.municipio.idMunicipio").masterLink == "true"); 
		setDisabled("municipioFilial.idMunicipioFilial",document.getElementById("municipioFilial.idMunicipioFilial").masterLink == "true"); 
		setDisabled("tpOperacao", false); 
		setDisabled("servico.idServico", false); 
		setDisabled("tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio", false); 
		setDisabled("tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio", false);
		setDisabled("nrTempoColeta", false);
		setDisabled("nrTempoEntrega", false); 		
		setDisabled("blAtendimentoGeral", false); 		
		setDisabled("servico.idServico", false); 		
		
		setDisabled("blDomingo", false);
		setDisabled("blSegunda", false);
		setDisabled("blTerca", false);
		setDisabled("blQuarta", false);
		setDisabled("blQuinta", false);
		setDisabled("blSexta", false);
		setDisabled("blSabado", false);
		
		setDisabled("blCobraTaxaFluvial", false); 		
		setDisabled("blAceitaFreteFob", false); 		
		setDisabled("dtVigenciaInicial", false); 		
		setDisabled("dtVigenciaFinal", false); 		
		
		setDisabled("filial.empresa.idEmpresa", false); 

		setFocusOnFirstFocusableField();
	}
	
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {	
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click")) { 
			estadoNovo();			
			document.getElementById('blAtendimentoGeral').checked=true; 
		} else if (eventObj.name == "storeButton") {
			if (getElementValue("idOperacaoServicoLocaliza") != "" && !document.getElementById("blAtendimentoGeral").checked) {
				setDisabled("__buttonBar:0_1", false);
			} else {
				setDisabled("__buttonBar:0_1", true);
			}
		}
    }
    
    
    
    function buildLinkPropertiesQueryString_atendimentosClientes() {

	var qs = "";
	if (document.getElementById("idOperacaoServicoLocaliza").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.idOperacaoServicoLocaliza=" + document.getElementById("idOperacaoServicoLocaliza").checked;
	} else {
		qs += "&operacaoServicoLocaliza.idOperacaoServicoLocaliza=" + document.getElementById("idOperacaoServicoLocaliza").value;
	}
	if (document.getElementById("municipioFilial.idMunicipioFilial").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.idMunicipioFilial=" + document.getElementById("municipioFilial.idMunicipioFilial").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.idMunicipioFilial=" + document.getElementById("municipioFilial.idMunicipioFilial").value;
	}
	if (document.getElementById("municipioFilial.municipio.idMunicipio").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.idMunicipio=" + document.getElementById("municipioFilial.municipio.idMunicipio").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.idMunicipio=" + document.getElementById("municipioFilial.municipio.idMunicipio").value;
	}
	if (document.getElementById("municipioFilial.municipio.nmMunicipio").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.nmMunicipio=" + document.getElementById("municipioFilial.municipio.nmMunicipio").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.nmMunicipio=" + document.getElementById("municipioFilial.municipio.nmMunicipio").value;
	}
	if (document.getElementById("municipioFilial.municipio.unidadeFederativa.siglaDescricao").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.siglaDescricao=" + document.getElementById("municipioFilial.municipio.unidadeFederativa.siglaDescricao").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.siglaDescricao=" + 
			  document.getElementById("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa").value +
			  " - " +
			  document.getElementById("municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa").value;
	}
	if (document.getElementById("municipioFilial.municipio.unidadeFederativa.pais.nmPais").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.pais.nmPais=" + document.getElementById("municipioFilial.municipio.unidadeFederativa.pais.nmPais").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.pais.nmPais=" + document.getElementById("municipioFilial.municipio.unidadeFederativa.pais.nmPais").value;
	}
	if (document.getElementById("municipioFilial.municipio.blDistrito").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.blDistrito=" + document.getElementById("municipioFilial.municipio.blDistrito").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.blDistrito=" + document.getElementById("municipioFilial.municipio.blDistrito").value;
	}
	if (document.getElementById("municipioFilial.municipio.municipioDistrito.nmMunicipio").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.municipioDistrito.nmMunicipio=" + document.getElementById("municipioFilial.municipio.municipioDistrito.nmMunicipio").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.municipio.municipioDistrito.nmMunicipio=" + document.getElementById("municipioFilial.municipio.municipioDistrito.nmMunicipio").value;
	}
	if (document.getElementById("municipioFilial.filial.siglaNomeFilial").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.filial.siglaNomeFilial=" + document.getElementById("municipioFilial.filial.siglaNomeFilial").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.filial.siglaNomeFilial=" + 
				document.getElementById("municipioFilial.filial.sgFilial").value
				+ " - " +
				document.getElementById("municipioFilial.filial.pessoa.nmFantasia").value;
	}
	if (document.getElementById("municipioFilial.idMunicipioFilial").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.municipioFilial.idMunicipioFilial=" + document.getElementById("municipioFilial.idMunicipioFilial").checked;
	} else {
		qs += "&operacaoServicoLocaliza.municipioFilial.idMunicipioFilial=" + document.getElementById("municipioFilial.idMunicipioFilial").value;
	}
	if (document.getElementById("blDomingo").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.blDomingo=" + document.getElementById("blDomingo").checked;
	} else {
		qs += "&operacaoServicoLocaliza.blDomingo=" + document.getElementById("blDomingo").value;
	}
	if (document.getElementById("blSegunda").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.blSegunda=" + document.getElementById("blSegunda").checked;
	} else {
		qs += "&operacaoServicoLocaliza.blSegunda=" + document.getElementById("blSegunda").value;
	}
	if (document.getElementById("blTerca").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.blTerca=" + document.getElementById("blTerca").checked;
	} else {
		qs += "&operacaoServicoLocaliza.blTerca=" + document.getElementById("blTerca").value;
	}
	if (document.getElementById("blQuarta").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.blQuarta=" + document.getElementById("blQuarta").checked;
	} else {
		qs += "&operacaoServicoLocaliza.blQuarta=" + document.getElementById("blQuarta").value;
	}
	if (document.getElementById("blQuinta").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.blQuinta=" + document.getElementById("blQuinta").checked;
	} else {
		qs += "&operacaoServicoLocaliza.blQuinta=" + document.getElementById("blQuinta").value;
	}
	if (document.getElementById("blSexta").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.blSexta=" + document.getElementById("blSexta").checked;
	} else {
		qs += "&operacaoServicoLocaliza.blSexta=" + document.getElementById("blSexta").value;
	}
	if (document.getElementById("blSabado").type == 'checkbox') {
		qs += "&operacaoServicoLocaliza.blSabado=" + document.getElementById("blSabado").checked;
	} else {
		qs += "&operacaoServicoLocaliza.blSabado=" + document.getElementById("blSabado").value;
	}
	qs += "&operacaoServicoLocaliza.tpOperacao=" + document.getElementById("tpOperacao")[document.getElementById("tpOperacao").selectedIndex].text;
	qs += "&operacaoServicoLocaliza.servico.dsServico=" + document.getElementById("servico.dsServico").value;
	qs += "&operacaoServicoLocaliza.tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio=" + document.getElementById("tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio")[document.getElementById("tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio").selectedIndex].text
	qs += "&operacaoServicoLocaliza.tipoLocalizacaoMunicipioFob.dsTipoLocalizacaoMunicipio=" + document.getElementById("tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio")[document.getElementById("tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio").selectedIndex].text;
	return qs;
}

</script>