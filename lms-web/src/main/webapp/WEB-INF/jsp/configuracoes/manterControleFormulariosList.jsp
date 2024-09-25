<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
   
    /** 
      * Função chamada no início da página
      */
	function myOnPageLoad(){
		onPageLoad();
		setDisabled("controleFormulario.controleForm",true);
		
		// Seta a sigla da filial do usário para masterLink
		getElement("sgFilialUsuario").masterLink = "true";
		findFilialSessao();
	}
</script>
<adsm:window service="lms.configuracoes.controleFormularioService" onPageLoad="myOnPageLoad" onPageLoadCallBack="myPageLoadCallBack">
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-27073"/>
	</adsm:i18nLabels>
	
	<adsm:form action="/configuracoes/manterControleFormularios">

		<adsm:hidden property="sgFilialUsuario" serializable="true"/>
		
	   	<adsm:lookup label="estoqueOrigem" property="controleFormulario" service="lms.configuracoes.controleFormularioService.findLookup" 
			idProperty="idControleFormulario" criteriaProperty="controleForm"
			dataType="text" action="/configuracoes/manterControleFormularios" 
			width="27%"/>

        <adsm:lookup label="empresa" property="empresa" dataType="text"  service="lms.municipios.empresaService.findLookup" maxLength="20"
			criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" idProperty="idEmpresa" action="/municipios/manterEmpresas"
			labelWidth="18%" width="40%">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="empresa.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="20" disabled="true" serializable="false"/>
		</adsm:lookup>

     	<adsm:lookup label="filial" property="filial" dataType="text"  service="lms.municipios.filialService.findLookup" exactMatch="false"
			idProperty="idFilial" criteriaProperty="sgFilial" size="5" maxLength="3" width="10%" action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="14" disabled="true" width="17%" serializable="false"/>

		<adsm:range label="dataRecebimento" labelWidth="18%" width="32%">
			<adsm:textbox property="dtRecebimentoInicial" dataType="JTDate" width="16%"/>
			<adsm:textbox property="dtRecebimentoFinal" dataType="JTDate" width="16%"/>
		</adsm:range>

		<adsm:combobox label="tipoDocumento" property="tpFormulario" domain="DM_TIPO_FORMULARIO" width="27%"/>

		<adsm:textbox label="formulario" property="nrFormulario" dataType="integer" size="10" maxLength="10" labelWidth="18%" width="32%"/>

		<adsm:combobox label="situacao" property="tpSituacaoFormulario" domain="DM_SITUACAO_FORMULARIO" width="27%"/>		

		<adsm:textbox label="seloFiscal" property="nrSeloFiscal" dataType="integer" maxLength="15" size="15" labelWidth="18%" width="32%" />

		<adsm:textbox label="serie" property="cdSerie" dataType="text" maxLength="5" width="27%"/>

		<adsm:textbox label="aidf" property="nrAidf" dataType="text" maxLength="20" labelWidth="18%" width="32%" size="20"/>

		<adsm:hidden property="controleForm" serializable="false"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" onclick="consultar(this.form);" disabled="false" buttonType="findButton"/>
			<adsm:button caption="limpar" onclick="myLimpar(this)" disabled="false" buttonType="resetButton"/>			
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid idProperty="idControleFormulario" property="formularios" gridHeight="200" 
		defaultOrder="empresa_pessoa_.nmPessoa, filial_.sgFilial, tpFormulario, tpSituacaoFormulario, nrFormularioInicial, nrFormularioFinal"
		rows="9">
		<adsm:gridColumn width="17%" title="empresa" property="empresa.pessoa.nmPessoa"/>
		<adsm:gridColumn width="4%" title="filial" property="filial.sgFilial"/>
		<adsm:gridColumn width="16%" title="tipoDocumento" property="tpFormulario" isDomain="true"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacaoFormulario" isDomain="true"/>
		<adsm:gridColumn width="10%" title="formularioInicial" property="nrFormularioInicial" dataType="integer"/>
		<adsm:gridColumn width="10%" title="formularioFinal" property="nrFormularioFinal" dataType="integer"/>
		<adsm:gridColumn width="12%" title="recebimento" property="dtRecebimento" dataType="JTDate"/>
		<adsm:gridColumn width="21%" title="estoqueOrigem" property="controleFormOrigem" />
        <adsm:buttonBar>
		   <adsm:removeButton/>
    	</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	
	/**
      * Função para validação do intervalo de datas de recebimento, chamada da função consultar
      */
	function verificaIntervaloDataRecebimento(){
		var dtRecebimentoInicial = getElementValue("dtRecebimentoInicial");
		var dtRecebimentoFinal = getElementValue("dtRecebimentoFinal");
		if (dtRecebimentoInicial == "" && dtRecebimentoFinal != ""){
			alert(parent.i18NLabel.getLabel("LMS-27034"));
			return false;
		}
		return true;
	}
	
	/**
      * Função chamda no clic do botão consultar
      */
	function consultar(eForm){
		if (verificaIntervaloDataRecebimento()){
			findButtonScript("formularios", eForm);
			//var fb = buildFormBeanFromForm(eForm, 'LIKE_END'); 
			//formularios_cb(fb);
		}else{
			setFocus(document.getElementById("dtRecebimentoInicial"));
		}
	}
	
	/**
      * Função chamada no início da tela
      */
	function initWindow(eventObj){
		setFocus(document.getElementById("controleFormulario_lupa"), false);
	}
	
	/** 
	  * Busca a filial do usuario da sessão 
	  */
	function findFilialSessao(){
		_serviceDataObjects = new Array();
	
		addServiceDataObject(createServiceDataObject("lms.configuracoes.manterVinculoFormulariosImpressorasAction.findFilialUsuario",
			"setFilialUsuario", 
			new Array()));
	
		xmit(false);	
	}
	
	/**
      * Seta o hidden com a filial do usuário 
      */
	function setFilialUsuario_cb(data, error){
		if(error != undefined){
			alert(error);
		}
		setFilial(data);
	}
	
	/**
      * Seta os dados da filial que retornaram do xmit
      */
	function setFilial(data){
		setElementValue("sgFilialUsuario", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.pessoa.nmFantasia);
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.idFilial", data.idFilial);
	}
	
	
	
	/**
      * Função chamada no callBack da página
      */
	function myPageLoadCallBack_cb(data, error){
		onPageLoad_cb(data, error);
		
	}
	
	function myLimpar(elem){
		cleanButtonScript(elem.document);
		// Seta a sigla da filial do usário para masterLink
		getElement("sgFilialUsuario").masterLink = "true";
		findFilialSessao();
	}
</script>