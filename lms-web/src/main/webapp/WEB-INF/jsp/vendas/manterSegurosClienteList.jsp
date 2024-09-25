<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.seguroClienteService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterSegurosCliente">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>

		<adsm:lookup
			label="cliente"
			property="cliente"
			idProperty="nrIdentificacao"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterSegurosClienteAction.findClienteLookup"
			action="/vendas/manterDadosIdentificacao"
			dataType="text"
			exactMatch="true"
			labelWidth="13%"
			maxLength="20"
			required="false"
			size="20"
			width="65%"
		>
			<adsm:propertyMapping 
				relatedProperty="cliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa" 
			/>
			<adsm:propertyMapping 
				relatedProperty="cliente.idCliente" 
				modelProperty="idCliente" 
			/>
			<adsm:propertyMapping 
				relatedProperty="cliente.identificacao" 
				modelProperty="pessoa.nrIdentificacao" 
			/>
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="cliente.pessoa.nmPessoa" 
				size="60" 
			/>
		</adsm:lookup>
		<adsm:hidden property="cliente.identificacao" />

		<adsm:combobox
			label="modal"
			property="tpModal"
			domain="DM_MODAL"
			onchange="setaDescricaoModal(this);validateCombos();"
			labelWidth="13%"
			width="30%"/>
		<adsm:hidden property="dsModal" serializable="true"/>

		<adsm:combobox
			label="abrangencia"
			property="tpAbrangencia"
			domain="DM_ABRANGENCIA"
			onchange="setaDescricaoAbrangencia(this);validateCombos();"
			labelWidth="13%"
			width="37%"/>
		<adsm:hidden property="dsAbrangencia" serializable="true"/>

		<adsm:combobox
			label="tipoSeguro"
			property="tipoSeguro.idTipoSeguro"
			style="width:100px"
			autoLoad="false"
			optionLabelProperty="sgTipo"
			optionProperty="idTipoSeguro"
			labelWidth="13%"
			width="30%">
			<adsm:propertyMapping modelProperty="sgTipo" relatedProperty="tipoSeguro.sgTipo"/>
		</adsm:combobox>
		<adsm:hidden property="tipoSeguro.sgTipo" />

		<!-- LMS-7285 -->
		<adsm:textbox
			dataType="JTDate"
			label="vigencia"
			labelWidth="13%"
			picker="true"
			property="dtVigencia"
			width="37%"
		/>

		<!-- LMS 6148 -->
		<adsm:combobox property="seguradora.idSeguradora"
			optionProperty="seguradora.idSeguradora"
			optionLabelProperty="seguradora.pessoa.nmPessoa"
			service="lms.vendas.manterSegurosClienteAction.findSeguradoraOrderByNmPessoa"
			label="seguradora"
			style="width:170px"
			labelWidth="13%" width="30%">
			<adsm:propertyMapping modelProperty="seguradora.pessoa.nmPessoa" relatedProperty="seguradora.pessoa.nmPessoa"/>
		</adsm:combobox>
		<adsm:hidden property="seguradora.pessoa.nmPessoa" />

		<adsm:combobox property="reguladoraSeguro.idReguladora"
			optionProperty="pessoa.idPessoa"
			optionLabelProperty="pessoa.nmPessoa"
			service="lms.seguros.manterApolicesSeguroAction.findReguladoraOrderByNmPessoa"
			label="reguladora"
			labelWidth="13%"
			width="20%">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="reguladoraSeguro.pessoa.nmPessoa"/>
		</adsm:combobox>
		<adsm:hidden property="reguladoraSeguro.pessoa.nmPessoa" />
		<adsm:buttonBar freeLayout="true">
			<adsm:button id="exportarExcel" caption="exportarExcel" onclick="emitirReport();"/>
			<adsm:findButton callbackProperty="seguros"/>
			<adsm:button buttonType="resetButton" id="btnLimpar" caption="limpar" disabled="false" onclick="resetForm(this.document)"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idSeguroCliente" property="seguros" gridHeight="200" unique="true" 
		onSelectAll="myOnSelectAll" scrollBars="horizontal" onSelectRow="myOnSelectRow" rows="10">
		<adsm:gridColumn title="cliente" property="identClienteFormatado" width="100"/>
		<adsm:gridColumn title="" property="infCliente" width="150"/>
		<adsm:gridColumn title="modal" property="tpModal" width="70" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" width="90" isDomain="true"/>
		<adsm:gridColumn title="tipoSeguro" property="sgTipo" width="60"/>
		<adsm:gridColumn title="reguladora" property="nmPessoa" width="120"/>
		<adsm:gridColumn title="seguradora" property="infSeguradora" width="120"/>
		<adsm:gridColumn title="apolice" property="dsApolice" width="100"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="80" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="80" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	document.getElementById("permissao").masterLink="true";
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	
	function initWindow(eventObj) {
		setDisabled("exportarExcel", false);
	}
	
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		
		if(getElementValue("cliente.idCliente") != null && getElementValue("cliente.idCliente") != "") {
			setDisabled(document.getElementById("cliente.nrIdentificacao"), true);
			document.getElementById("tpModal").focus();
		}
		
		var idFilial = getElementValue("idFilial");
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		
		var sdo = createServiceDataObject("lms.vendas.seguroClienteService.validatePermissaoUsuarioLogado", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});	
		
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {			
			setDisabled("removeButton", true);
		}
	}
	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){

		var permissao = document.getElementById("permissao");
		
		if( permissao.value != "true" ){
			setDisabled("removeButton",true);
			return false;
		}
	}
	
	/**
	* Esta função deve executar exatamente a mesma tarefa que a função myOnSelectRow.
	*/
	function myOnSelectAll(){
		return myOnSelectRow();
	}
	
	/** Função que valida se as combos de modal e abrangência estão preenchidas, para preencher a combo de tipo de seguro */
	function validateCombos(){

		document.getElementById("tipoSeguro.idTipoSeguro").length = 1;
		
		if(getElementValue("tpModal") != '' &&
				getElementValue("tpAbrangencia") != ''){
			var sgModal = getElementValue("tpModal");
			var sgAbrangencia = getElementValue("tpAbrangencia");
			var data = new Array();
			
			/** Insere os dois campos com suas chaves e valores no Array que posteriormente será transformado em um Map pela XMLBroker */
			setNestedBeanPropertyValue(data, "sgModal", sgModal);
			setNestedBeanPropertyValue(data, "sgAbrangencia", sgAbrangencia);
			
			/** Invoca o método da service, especifica a função JS que será executada no callBack e passa os dados para serem usados como filtro */
			var sdo = createServiceDataObject("lms.seguros.tipoSeguroService.findComboByTipoSeguro", "validateCombos", data);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	/** Função que popula a combo tipo seguro no callBack */
	function validateCombos_cb(data, error, erromsg){
		if(error == null){
			/** Função gerada pelo framework para popular o combo */
			tipoSeguro_idTipoSeguro_cb(data);	
		}
	}
	
	/** Função responsável por limpar os campos da tela e colocar o focus no primeiro campo */
	function resetForm(doc){
		/** Retira todos optioms da Select deixando apenas o primeiro */
		document.getElementById("tipoSeguro.idTipoSeguro").length = 1;
		
		/** Reseta todos campos da tela */
		cleanButtonScript(doc);
		
		/** Coloca o focus no primeiro campo da tela */
		setFocusOnFirstFocusableField(doc);
		
		/** Habilita o botão */
		setDisabled("btnLimpar", false);
	}
	
	function emitirReport() {
		executeReportWithCallback('lms.vendas.manterSegurosClienteAction.executeReport', 'openReportWithLocator', document.forms[0]);
	}
	
	/**
	* Seta a descrição Modal para ser usada no cabeçalho do relatório
	*/
	function setaDescricaoModal(obj){
    	setElementValue("dsModal",obj.options[obj.selectedIndex].text);    
	}
	
	/**
	* Seta a descrição Abrangencia para ser usada no cabeçalho do relatório
	*/
	function setaDescricaoAbrangencia(obj){
    	setElementValue("dsAbrangencia",obj.options[obj.selectedIndex].text);    
	}
</script>