<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script>
	function myPageLoadCallBack_cb(dados,errors){
		onPageLoad_cb(dados,errors);
		populateUserSession();
	}
	
</script>

<adsm:window service="lms.contasreceber.inclusaoCobrancaInadimplentesAction" onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/inclusaoCobrancaInadimplentes">
        
        <adsm:i18nLabels>
			<adsm:include key="LMS-00055"/>
		</adsm:i18nLabels>
        
        <adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			dataType="text" 
			exactMatch="true" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			idProperty="idCliente" 
			label="cliente" 
			labelWidth="20%"
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.emitirDocumentosFaturarAction.findClienteLookup" 
			size="20" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="clienteByIdCliente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
				
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="clienteByIdCliente.pessoa.nmPessoa" 
				serializable="false"
				size="60"
				maxLength="50"/>
				
		</adsm:lookup>
        

		<adsm:lookup property="usuario" 
        			 idProperty="idUsuario" 
        			 criteriaProperty="nrMatricula" 
        			 serializable="true"
                     dataType="text" 
                     label="usuario" 
                     size="20" 
                     maxLength="20" 
                     labelWidth="20%" 
                     width="80%" 
                     service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupUsuario" 
                     action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario"/>
                <adsm:textbox dataType="text" property="nmUsuario" size="60" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>

        <adsm:combobox property="blCobrancaEncerrada" domain="DM_SIM_NAO" label="cobrancaEncerrada" labelWidth="20%" width="80%" disabled="false"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				disabled="false"
				buttonType="findButton" 
				caption="consultar" 
				onclick="validateFields(this);"/>
				
			<adsm:button caption="limpar" onclick="myReset(this.document);" buttonType="resetButton"/>

		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid selectionMode="check" 
		idProperty="idCobrancaInadimplencia" 
		property="cobrancaInadimplencia" 
		service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findPaginatedByCobrancaInadimplencia"
		rowCountService="lms.contasreceber.inclusaoCobrancaInadimplentesAction.getRowCountByCobrancaInadimplencia"
		gridHeight="200" 
		unique="true" 
		rows="12">

		<adsm:gridColumn title="cliente" property="nmPessoa" />

		<adsm:gridColumn title="responsavel" property="nmUsuario" />

        <adsm:gridColumn title="cobrancaEncerrada" property="blCobrancaEncerrada" renderMode="image-check"/>
				
	</adsm:grid>

</adsm:window>

<script>

/** Função chamada na inicialização da tela */ 
function initWindow(eventObj) {
	var event = eventObj.name;
	if (event == "removeButton" || event == "newButton_click") {
		desabilitaTabFatura(true);
	}else if (event == "gridRow_click" || event == "storeButton") { 
		desabilitaTabFatura(false);
	}
}

function carregarUsuarioSession_cb(data,erro){
		setElementValue("usuario.nrMatricula", data.nrMatricula);
		setElementValue("nmUsuario", data.nmUsuario);
		setElementValue("usuario.idUsuario", data.idUsuario);
}

function populateUserSession(){
	var sdo = createServiceDataObject("lms.contasreceber.inclusaoCobrancaInadimplentesAction.getUsuarioSession",
			"carregarUsuarioSession",new Array());
	xmit({serviceDataObjects:[sdo]});
}
	
function desabilitaTabFatura(disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab("item", disabled);
}

function myReset(documento){
	cleanButtonScript(this.document);
	populateUserSession();
}

function validateFields() {
	if (getElementValue("cliente.idCliente") == ''
		&& getElementValue("usuario.idUsuario") == ''
		&& getElementValue("blCobrancaEncerrada") == '') {
			alert(i18NLabel.getLabel("LMS-00055"));
			return false;
	}
	findButtonScript("cobrancaInadimplencia", this.document.forms[0]);
}

</script>
