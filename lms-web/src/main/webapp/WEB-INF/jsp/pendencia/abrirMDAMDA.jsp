<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	/**
	 * Carrega Master na sessão
	 */	
	function carregaPagina() {
		var objTextAreaEnderecoRemetente = document.getElementById("enderecoRemetente");
		objTextAreaEnderecoRemetente.readOnly="true";
		
		var objTextAreaEnderecoDestinatario = document.getElementById("enderecoDestinatario");
		objTextAreaEnderecoDestinatario.readOnly="true";
		
	
	    var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.getDadosSessao", "buscarDadosSessao");
	    var sdo2 = createServiceDataObject("lms.pendencia.abrirMDAAction.newMaster");
		xmit({serviceDataObjects:[sdo, sdo2]});	
	}
	
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	var dadosSessao;
	function buscarDadosSessao_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		dadosSessao = data;	
		setaDadosSessao();
		onPageLoad();	
	}
	
</script>
<adsm:window service="lms.pendencia.abrirMDAAction" onPageLoad="carregaPagina">
	<adsm:form action="/pendencia/abrirMDA" idProperty="idDoctoServico" height="370" >
	
		<adsm:hidden property="origem" value="abrirMda"/>
		<adsm:hidden property="tipoSelecionado"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>		
		<!-- Campo para setar os dados do MDA na barra verde ao salvar. -->
		<adsm:hidden property="mdaConcatenado" serializable="false" />
		<adsm:hidden property="tpRemetenteMda" value="F"/>
		<adsm:hidden property="tpDestinatarioMda" value="F"/>

		<adsm:textbox property="sgFilialMda" label="mda" dataType="text" 
					  labelWidth="19%" width="81%" size="5" maxLength="3" disabled="true">
			<adsm:textbox property="nrDoctoServico" dataType="integer" mask="00000000"
						  size="10" maxLength="10" disabled="true"/>
		</adsm:textbox>

		<adsm:label key="espacoBranco" width="100%" style="border:none"/>	
		<adsm:section caption="remetente" />

		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial"
					 criteriaProperty="sgFilial" 
				 	 service="lms.pendencia.abrirMDAAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="filialOrigem" dataType="text" size="5" maxLength="3" 
					 labelWidth="19%" width="81%"
					 onPopupSetValue="carregaDadosFilialOrigem"
					 onDataLoadCallBack="carregaDadosFilialOrigem"
					 onchange="return limpaFilialOrigem();">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" 
						  size="30" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup property="usuarioByIdUsuarioOrigem"
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView"
					 service="lms.pendencia.abrirMDAAction.findLookupUsuarioFuncionario" 
					 dataType="integer" label="funcionarioOrigem" size="10" 
					 maxLength="9" labelWidth="19%" width="81%"
					 disabled="true">
 			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioOrigem.nmUsuario"
								  modelProperty="nmUsuario" />
		<adsm:textbox property="usuarioByIdUsuarioOrigem.nmUsuario" dataType="text" 
						  size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:hidden property="tipoEnderecoRemetente" serializable="false" />
		<adsm:textarea label="endereco" property="enderecoRemetente" maxLength="300" 
					   columns="90" rows="3" labelWidth="19%" width="81%" serializable="false"  >						
			<adsm:lookup style="visibility: hidden;font-size: 1px" size="1" maxLength="1" dataType="text" 
			 			 property="enderecoPessoaRemetente" 
						 idProperty="idEnderecoPessoa" 						 
						 action="/pendencia/abrirMDA"
						 cmd="selecionarEndereco" disabled="true"
						 service="lms.pendencia.abrirMDAAction.findLookupEnderecoPessoa"
						 criteriaProperty="pessoa.idPessoa" 
						 onPopupSetValue="concatenaEndereco" 
						 onclickPicker="onclickPickerLookupEnderecoRemetente()">
				<adsm:propertyMapping relatedProperty="municipioRemetente.nmMunicipio" modelProperty="municipio.nmMunicipio" />
				<adsm:propertyMapping relatedProperty="ufRemetente" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
			</adsm:lookup>												
		</adsm:textarea>

		<adsm:textbox property="municipioRemetente.nmMunicipio" label="municipio" dataType="text" 
					  size="35" maxLength="50" labelWidth="19%" width="35%" disabled="true" serializable="false"/>
					  
		<adsm:textbox label="uf" property="ufRemetente" dataType="text" 
					  size="3" disabled="true" labelWidth="10%" serializable="false" />					  
		
		<adsm:label key="espacoBranco" width="100%" style="border:none"/>
		<adsm:section caption="destinatario" />

		<adsm:lookup property="filialByIdFilialDestino" idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
				 	 service="lms.pendencia.abrirMDAAction.findLookupFilial"
				 	 action="/municipios/manterFiliais" 
					 label="filialDestino" dataType="text" size="5" maxLength="3" 
					 labelWidth="19%" width="81%"
					 onPopupSetValue="carregaDadosFilialDestino"
					 onDataLoadCallBack="carregaDadosFilialDestino"
					 onchange="return limpaFilialDestino();">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" 
						  size="30" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup property="usuarioByIdUsuarioDestino"
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView"
					 service="lms.pendencia.abrirMDAAction.findLookupUsuarioFuncionario" 
					 dataType="integer" label="funcionarioDestinatario" size="10" 
					 maxLength="9" labelWidth="19%" width="81%" exactMatch="true"
					 onchange="return limpaCamposFuncionario();">
 			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioDestino.nmUsuario"
								  modelProperty="nmUsuario" />
									  
			<adsm:textbox property="usuarioByIdUsuarioDestino.nmUsuario" dataType="text" 
						  size="60" maxLength="60" disabled="true" serializable="false"/>
		</adsm:lookup>
	
		<adsm:combobox label="setor" labelWidth="19%" width="81%" property="setor.idSetor" optionLabelProperty="dsSetor" optionProperty="idSetor" service="lms.pendencia.abrirMDAAction.findSetor" disabled="true" />

		<adsm:hidden property="clienteByIdClienteDestinatario.tpSituacao" value="A" serializable="false"/>
 		<adsm:lookup property="clienteByIdClienteDestinatario" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.pendencia.abrirMDAAction.findLookupCliente" 
					 action="/pendencia/abrirMDA" 
					 cmd="selecionarCliente"
					 label="destinatario" dataType="text"
					 size="20" maxLength="20" labelWidth="19%" width="81%" 					  
					 onPopupSetValue="carregaDadosClienteDestinatario"
					 onDataLoadCallBack="carregaDadosClienteDestinatario" 
					 onchange="return limpaClienteDestinatario();"
					 onclickPicker="onclickPickerLookupClienteDestinatario()">
					 
			<adsm:propertyMapping criteriaProperty="clienteByIdClienteDestinatario.tpSituacao" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao.value" />
			<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			
			<adsm:hidden property="clienteByIdClienteDestinatario.pessoa.tpIdentificacao" serializable="false"/>
			<adsm:textbox dataType="text" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  size="50" maxLength="50" disabled="true" serializable="false" />			  
		</adsm:lookup>
		
		<adsm:hidden property="tipoEnderecoDestinatario" serializable="false" />
		<adsm:textarea label="endereco" property="enderecoDestinatario" maxLength="300" 
					   columns="90" rows="3" labelWidth="19%" width="81%" serializable="false" >						
			<adsm:lookup style="visibility: hidden;font-size: 1px" size="1" maxLength="1" dataType="text" 
			 			 property="enderecoPessoaDestinatario" 
						 idProperty="idEnderecoPessoa" 						 
						 action="/pendencia/abrirMDA"
						 cmd="selecionarEndereco"
						 service="lms.pendencia.abrirMDAAction.findLookupEnderecoPessoa"
						 criteriaProperty="pessoa.idPessoa" 
						 onPopupSetValue="concatenaEndereco" 
						 onclickPicker="onclickPickerLookupEnderecoDestinatario()">
				<adsm:propertyMapping criteriaProperty="clienteByIdClienteDestinatario.idCliente" modelProperty="pessoa.idPessoa" />
				<adsm:propertyMapping criteriaProperty="clienteByIdClienteDestinatario.pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao" />
				<adsm:propertyMapping criteriaProperty="clienteByIdClienteDestinatario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacao" />
				<adsm:propertyMapping criteriaProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
				<adsm:propertyMapping criteriaProperty="tipoEnderecoDestinatario" modelProperty="tipoEnderecoPessoas.tpEndereco" />
				
				<adsm:propertyMapping relatedProperty="municipioDestinatario.idMunicipio" modelProperty="municipio.idMunicipio" />
				<adsm:propertyMapping relatedProperty="municipioDestinatario.nmMunicipio" modelProperty="municipio.nmMunicipio" />
				<adsm:propertyMapping relatedProperty="ufDestinatario" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
			</adsm:lookup>												
		</adsm:textarea>
	
		<adsm:hidden property="municipioDestinatario.idMunicipio"/> 
		<adsm:textbox property="municipioDestinatario.nmMunicipio" label="municipio" dataType="text" 
					  size="35" maxLength="50" labelWidth="19%" width="35%" disabled="true" serializable="false"/>

		<adsm:textbox label="uf" property="ufDestinatario" dataType="text" 
					  size="3" disabled="true" labelWidth="10%" serializable="false" />						  
		
		<adsm:label key="espacoBranco" width="100%" style="border:none"/>
		
		<adsm:buttonBar>
			<adsm:button caption="emitirMda" id="emitir" action="/pendencia/emitirMDA" cmd="main">
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="idDoctoServico" target="idDoctoServico"/>
				<adsm:linkProperty src="nrDoctoServico" target="nrDoctoServico"/>
				<adsm:linkProperty src="origem" target="origem"/>
			</adsm:button>
			<adsm:storeButton id="storeButton" callbackProperty="salvarRegistro" caption="salvar"/>		
			<adsm:button id="newButton" buttonType="newButton" caption="novo" onclick="novoRegistro()"/>			
		</adsm:buttonBar>
	</adsm:form>
	
</adsm:window>

<script type="text/javascript">
	document.getElementById("usuarioByIdUsuarioDestino.nrMatricula").style.textAlign = "right";

	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		if(getElementValue("idDoctoServico") == "") {
			setDisabled("storeButton", false);
			setDisabled("emitir", true);
		} else {
			setDisabled("storeButton", true);
			setDisabled("emitir", false);
			//Adicionado try catch, pois nem sempre o foco estará na primeira aba.
			try{
				document.getElementById('newButton').focus();
			}catch(err){
			}
		}
		setDisabled("newButton", false);		
		
		if(getElementValue("tpRemetenteMda") == "") {
			if(getElementValue("enderecoPessoaRemetente.idEnderecoPessoa") == "") {
				setDisabled("enderecoPessoaRemetente.idEnderecoPessoa", true);
			}
		}
		
		if(getElementValue("tpDestinatarioMda") == "") {
			if(getElementValue("filialByIdFilialDestino.idFilial") == "") {
				setDisabled("filialByIdFilialDestino.idFilial", true);
			}
			if(getElementValue("usuarioByIdUsuarioDestino.idUsuario") == "") {
				setDisabled("usuarioByIdUsuarioDestino.idUsuario", true);
			}
			if(getElementValue("clienteByIdClienteDestinatario.idCliente") == "") {
				setDisabled("clienteByIdClienteDestinatario.idCliente", true);
			}
			if(getElementValue("enderecoPessoaDestinatario.idEnderecoPessoa") == "") {
				setDisabled("enderecoPessoaDestinatario.idEnderecoPessoa", true);
			}
		}
		} 
	
	/**
	 * Retorno do Salvamento do registro de Pedido Coleta
	 */
	function salvarRegistro_cb(data, error, errorMsg, eventObj) {
		//Adicionado um return na linha seguinte.
		//Não é necessário dar um alert(error) aqui, pois o store_cb já faz isso.
		store_cb(data, error, errorMsg, eventObj);
		if (error){
			return false;
		}
				
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("itens");			
		setDisabledDocument(tabDet.getDocument(), true);
	
		setDisabledDocument(this.document, true);
		setDisabled("emitir", false);
		setDisabled("newButton", false);
		
		setElementValue("idDoctoServico", data.idDoctoServico);
		setElementValue("sgFilialMda", data.sgFilial);			
		setElementValue("nrDoctoServico", setFormat(document.getElementById("nrDoctoServico"), data.nrDoctoServico));			

		//Seta o número do MDA na barra verde.
		var mdaConcatenado = data.sgFilial + " " + setFormat(document.getElementById("nrDoctoServico"), data.nrDoctoServico);		
		setElementValue("mdaConcatenado", mdaConcatenado);
		setElementValue(tabDet.getElementById("_mdaConcatenado"), mdaConcatenado);
		
   		setElementValue("filialByIdFilialDestino.idFilial", data.filialByIdFilialDestino.idFilial);
   		setElementValue("filialByIdFilialDestino.sgFilial", data.filialByIdFilialDestino.sgFilial);
   		setElementValue("filialByIdFilialDestino.pessoa.nmFantasia", data.filialByIdFilialDestino.pessoa.nmFantasia);			
		//Adicionado try catch, pois nem sempre o foco estará na primeira aba.
		try{
			document.getElementById('newButton').focus();
		}catch(err){
		}
	}	
	
	/**
	 * Limpa a tela e carrega os dados necessarios novamente.
	 */
	function novoRegistro() {
		var tabGroup = getTabGroup(this.document);
		tabGroup.document.parentWindow.navigate(window.location.pathname + "?cmd=main");
	}	
	
	/**
	 * Seta os Dados referente ao usuário da sessão.
	 */
	function setaDadosSessao() {
		setElementValue("usuarioByIdUsuarioOrigem.nrMatricula", dadosSessao.nrMatricula);
		setElementValue("usuarioByIdUsuarioOrigem.nmUsuario", dadosSessao.nmUsuario);
		setElementValue("usuarioByIdUsuarioOrigem.idUsuario", dadosSessao.idUsuario);
		
		if(dadosSessao.blMatriz == "true") {
			setDisabled("filialByIdFilialOrigem.idFilial", false);
			document.getElementById("filialByIdFilialOrigem.sgFilial").required = "true";
		} else {
			setElementValue("filialByIdFilialOrigem.idFilial", dadosSessao.idFilialSessao);
			setElementValue("filialByIdFilialOrigem.sgFilial", dadosSessao.sgFilialSessao);
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", dadosSessao.nmFilialSessao);			
			setDisabled("filialByIdFilialOrigem.idFilial", true);
			if(getElementValue("tpRemetenteMda") == "F") {
				setElementValue("tipoSelecionado", "remetente");
				buscarEnderecoFilialOrigem(dadosSessao);
			}
		}		
	
	}
	
	/**
	 * ###############################################################################
	 * # Inicio das funções para lookup de Cliente Remetente e Endereço do Remetente #
	 * ###############################################################################
	 */

	function habilitaCamposRemetente() {
		if(getElementValue("tpRemetenteMda") == "F") {
			document.getElementById("enderecoRemetente").required = "true";
			setDisabled("enderecoPessoaRemetente.idEnderecoPessoa", true);
		}
		return true;
	}

	function fRemetente(fn){
			document.getElementById("enderecoRemetente").required = "false";
			resetValue("enderecoPessoaRemetente.idEnderecoPessoa");
			setDisabled("enderecoPessoaRemetente.idEnderecoPessoa", true);
			setElementValue("enderecoRemetente", "");
		fn();
		}
		
	function carregaDadosFilialOrigem(data) {		
		setElementValue("tipoSelecionado", "remetente");		
		buscarEnderecoFilialOrigem(data);
	}
	
	function carregaDadosFilialOrigem_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		setElementValue("tipoSelecionado", "remetente");
		filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if(data[0] != undefined) {
			buscarEnderecoFilialOrigem(data[0]);
		}	
	}	
	
	/**
	 * Busca os Dados referente a filial destinatário.
	 */	
	function buscarEnderecoFilialOrigem(data) {
		var mapCriteria = new Array();	   
		if(data.pessoa){
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.idPessoa", data.pessoa.idPessoa);
		}else{
		   	setNestedBeanPropertyValue(mapCriteria, "pessoa.idPessoa", data.idFilial);
		}
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.tpIdentificacao", data.pessoa.tpIdentificacao.value);
	   	setNestedBeanPropertyValue(mapCriteria, "tipoSelecionado", getElementValue("tipoSelecionado"));
	
		var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.getEnderecoPessoa", "buscarEnderecoFilialOrigem", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da pesquisa de dados da filial destinatário em getEnderecoFilial().
	 */
	function buscarEnderecoFilialOrigem_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		setElementValue("enderecoPessoaRemetente.idEnderecoPessoa", data.idEnderecoPessoa);
		concatenaEndereco(data);
		setElementValue("municipioRemetente.nmMunicipio", data.municipio.nmMunicipio);
		setElementValue("ufRemetente", data.municipio.unidadeFederativa.sgUnidadeFederativa);
	}
			
	function carregaDadosClienteRemetente(data) {
		setElementValue("tipoSelecionado", "remetente");
		setDisabled("enderecoPessoaRemetente.idEnderecoPessoa", false);
		buscarEnderecoRemetente(data);
	}
	
	function carregaDadosClienteRemetente_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		setElementValue("tipoSelecionado", "remetente");
		clienteByIdClienteRemetente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if(data[0] != undefined) {
			setDisabled("enderecoPessoaRemetente.idEnderecoPessoa", false);
			buscarEnderecoRemetente(data[0]);
		}	
	}
	
	/**
	 * Limpa os campos do form caso não exista filial
	 */
	function limpaFilialOrigem() {				
		if(getElementValue("filialByIdFilialOrigem.sgFilial") == "" && 
		   getElementValue("filialByIdFilialOrigem.idFilial") != "") {
			resetValue("filialByIdFilialOrigem.idFilial");			
			resetValue("enderecoPessoaRemetente.idEnderecoPessoa");
			setElementValue("enderecoRemetente", "");
			setElementValue("municipioRemetente.nmMunicipio", "");
			setElementValue("ufRemetente", "");
			setDisabled("enderecoPessoaRemetente.idEnderecoPessoa", true);
		}
		return filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}	
	
	
	/**
	 * Função que especifica que o cliente em questão é 'Remetente' ao 
	 * chamar a pop-up de cliente remetente
	 */
	function onclickPickerLookupClienteRemetente() {
		setElementValue("tipoSelecionado", "remetente");
		lookupClickPicker({e:document.getElementById("clienteByIdClienteRemetente.idCliente")});
	}	
	
	/**
	 * Função que especifica que o cliente em questão é 'Remetente' ao 
	 * chamar a pop-up de endereço remetente
	 */
	function onclickPickerLookupEnderecoRemetente() {
		setElementValue("tipoSelecionado", "remetente");
		lookupClickPicker({e:document.getElementById("enderecoPessoaRemetente.idEnderecoPessoa")});
	}		
	
	/**
	 * Busca EnderecoPessoa de acordo com o ID co Cliente
	 */
	function buscarEnderecoRemetente(data) {
		var mapCriteria = new Array();	   
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.idPessoa", data.idCliente);
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.tpIdentificacao", data.pessoa.tpIdentificacao.value);
	   	setNestedBeanPropertyValue(mapCriteria, "tipoSelecionado", getElementValue("tipoSelecionado"));
	   
	    var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.getEnderecoPessoa", "buscarEnderecoRemetente", mapCriteria);
		xmit({serviceDataObjects:[sdo]});	
	}
	
	/**
	 * Retorno da pesquisa de EnderecoPessoa em getEnderecoPessoa().
	 */
	function buscarEnderecoRemetente_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
		if(data.idEnderecoPessoa != undefined) {				
			concatenaEndereco(data);
			setElementValue("enderecoPessoaRemetente.idEnderecoPessoa", data.idEnderecoPessoa);
			setElementValue("municipioRemetente.nmMunicipio", data.municipio.nmMunicipio);
			setElementValue("ufRemetente", data.municipio.unidadeFederativa.sgUnidadeFederativa);			
		} else {
			setElementValue("tipoSelecionado", "remetente");
			lookupClickPicker({e:document.forms[0].elements['enderecoPessoaRemetente.idEnderecoPessoa']});
		}
	}	
		
	/**
	 * ############################################################################
	 * # Fim das funções para lookup de Cliente Remetente e Endereço do Remetente #
	 * ############################################################################
	 */	
	 
	 
	 
	/**
	 * #####################################################################################
	 * # Inicio das funções para lookup de Cliente Destinatário e Endereço do Destinatário #
	 * #####################################################################################
	 */
	 
	function habilitaCamposDestinatario() {
		if(getElementValue("tpDestinatarioMda") == "F") {
			setDisabled("filialByIdFilialDestino.idFilial", false);
			document.getElementById("filialByIdFilialDestino.sgFilial").required = "true";			
			setDisabled("usuarioByIdUsuarioDestino.idUsuario", false);
			document.getElementById("usuarioByIdUsuarioDestino.nrMatricula").required = "true";
			setDisabled("setor.idSetor", false);
			resetValue("clienteByIdClienteDestinatario.idCliente");
			setDisabled("clienteByIdClienteDestinatario.idCliente", true);
			document.getElementById("clienteByIdClienteDestinatario.pessoa.nrIdentificacao").required = "false";
			setElementValue("enderecoDestinatario", "");
			document.getElementById("enderecoDestinatario").required = "true";
			resetValue("enderecoPessoaDestinatario.idEnderecoPessoa");			
			setDisabled("enderecoPessoaDestinatario.idEnderecoPessoa", true);
		}
	}

	function fDestinatario(fn){
			resetValue("filialByIdFilialDestino.idFilial");
			document.getElementById("filialByIdFilialDestino.sgFilial").required = "false";
			setDisabled("filialByIdFilialDestino.idFilial", true);
			resetValue("usuarioByIdUsuarioDestino.idUsuario");
			setDisabled("usuarioByIdUsuarioDestino.idUsuario", true);
			document.getElementById("usuarioByIdUsuarioDestino.nrMatricula").required = "false";
			resetValue("clienteByIdClienteDestinatario.idCliente");
			setDisabled("clienteByIdClienteDestinatario.idCliente", true);
			document.getElementById("clienteByIdClienteDestinatario.pessoa.nrIdentificacao").required = "false";
			setElementValue("enderecoDestinatario", "");
			document.getElementById("enderecoDestinatario").required = "false";
			resetValue("enderecoPessoaDestinatario.idEnderecoPessoa");			
			setDisabled("enderecoPessoaDestinatario.idEnderecoPessoa", true);
			resetValue("setor.idSetor");
			setDisabled("setor.idSetor", true);
		fn();
		}
	
	function carregaDadosFilialDestino(data) {
		setElementValue("tipoSelecionado", "destinatario");	
		buscarEnderecoFilialDestino(data);
	}
	
	function carregaDadosFilialDestino_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		setElementValue("tipoSelecionado", "destinatario");
		filialByIdFilialDestino_sgFilial_exactMatch_cb(data);
		if(data[0] != undefined) {
			buscarEnderecoFilialDestino(data[0]);
		}	
	}	
	
	/**
	 * Busca os Dados referente a filial destinatário.
	 */	
	function buscarEnderecoFilialDestino(data) {
		var mapCriteria = new Array();	   
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.idPessoa", data.idFilial);
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.tpIdentificacao", data.pessoa.tpIdentificacao.value);
	   	setNestedBeanPropertyValue(mapCriteria, "tipoSelecionado", getElementValue("tipoSelecionado"));
	
		var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.getEnderecoPessoa", "buscarEnderecoFilialDestino", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da pesquisa de dados da filial destinatário em getEnderecoFilial().
	 */
	function buscarEnderecoFilialDestino_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		setElementValue("enderecoPessoaDestinatario.idEnderecoPessoa", data.idEnderecoPessoa);
		concatenaEndereco(data);
		setElementValue("municipioDestinatario.nmMunicipio", data.municipio.nmMunicipio);
		setElementValue("ufDestinatario", data.municipio.unidadeFederativa.sgUnidadeFederativa);
	}

	function carregaDadosClienteDestinatario(data) {
		setElementValue("tipoSelecionado", "destinatario");
		setDisabled("enderecoPessoaDestinatario.idEnderecoPessoa", false);
		buscarEnderecoDestinatario(data);
	}
	
	function carregaDadosClienteDestinatario_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		setElementValue("tipoSelecionado", "destinatario");
		clienteByIdClienteDestinatario_pessoa_nrIdentificacao_exactMatch_cb(data);
		if(data[0] != undefined) {
			setDisabled("enderecoPessoaDestinatario.idEnderecoPessoa", false);
			buscarEnderecoDestinatario(data[0]);
		}	
	}
	
	/**
	 * Limpa os campos do form caso não exista filial
	 */
	function limpaFilialDestino() {				
		if(getElementValue("filialByIdFilialDestino.sgFilial") == "" && 
		   getElementValue("filialByIdFilialDestino.idFilial") != "") {
			resetValue("filialByIdFilialDestino.idFilial");			
			resetValue("enderecoPessoaDestinatario.idEnderecoPessoa");
			setElementValue("enderecoDestinatario", "");
			setElementValue("municipioDestinatario.nmMunicipio", "");
			setElementValue("ufDestinatario", "");
			setDisabled("enderecoPessoaDestinatario.idEnderecoPessoa", true);
		}
		return filialByIdFilialDestino_sgFilialOnChangeHandler();
	}	
	
	/**
	 * Limpa os campos do form caso não exista cliente
	 */
	function limpaClienteDestinatario() {				
		if(getElementValue("clienteByIdClienteDestinatario.pessoa.nrIdentificacao") == "" && 
		   getElementValue("clienteByIdClienteDestinatario.idCliente") != "") {
			resetValue("clienteByIdClienteDestinatario.idCliente");			
			resetValue("enderecoPessoaDestinatario.idEnderecoPessoa");
			setElementValue("enderecoDestinatario", "");
			setElementValue("municipioDestinatario.nmMunicipio", "");
			setElementValue("ufDestinatario", "");
			setDisabled("enderecoPessoaDestinatario.idEnderecoPessoa", true);
		}
		return clienteByIdClienteDestinatario_pessoa_nrIdentificacaoOnChangeHandler();
	}	

	/**
	 * Função que especifica que o cliente em questão é 'Destinatario' ao 
	 * chamar a pop-up de cliente destinatario
	 */
	function onclickPickerLookupClienteDestinatario() {
		setElementValue("tipoSelecionado", "destinatario");
		lookupClickPicker({e:document.getElementById("clienteByIdClienteDestinatario.idCliente")});
	}

	/**
	 * Função que especifica que o cliente em questão é 'Destinatario' ao 
	 * chamar a pop-up de endereço destinatario
	 */
	function onclickPickerLookupEnderecoDestinatario() {
		setElementValue("tipoSelecionado", "destinatario");
		lookupClickPicker({e:document.getElementById("enderecoPessoaDestinatario.idEnderecoPessoa")});
	}		
	
	/**
	 * Busca EnderecoPessoa de acordo com o ID co Cliente
	 */
	function buscarEnderecoDestinatario(data) {
		var mapCriteria = new Array();	   
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.idPessoa", data.idCliente);
	   	setNestedBeanPropertyValue(mapCriteria, "pessoa.tpIdentificacao", data.pessoa.tpIdentificacao.value);
	   	setNestedBeanPropertyValue(mapCriteria, "tipoSelecionado", getElementValue("tipoSelecionado"));
	   
	    var sdo = createServiceDataObject("lms.pendencia.abrirMDAAction.getEnderecoPessoa", "buscarEnderecoDestinatario", mapCriteria);
		xmit({serviceDataObjects:[sdo]});	
	}
	
	/**
	 * Retorno da pesquisa de EnderecoPessoa em getEnderecoPessoa().
	 */
	function buscarEnderecoDestinatario_cb(data, error) {
		if(error){
			alert(error);
			return false;
		}
	
		if(data.idEnderecoPessoa != undefined) {				
			concatenaEndereco(data);
			setElementValue("enderecoPessoaDestinatario.idEnderecoPessoa", data.idEnderecoPessoa);
			setElementValue("municipioDestinatario.nmMunicipio", data.municipio.nmMunicipio);
			setElementValue("ufDestinatario", data.municipio.unidadeFederativa.sgUnidadeFederativa);			
		} else {
			setElementValue("tipoSelecionado", "destinatario");
			lookupClickPicker({e:document.forms[0].elements['enderecoPessoaDestinatario.idEnderecoPessoa']});
		}
	}	
	
	/**
	 * ##################################################################################
	 * # Fim das funções para lookup de Cliente Destinatário e Endereço do Destinatário #
	 * ##################################################################################
	 */		 
	 
	/**
	 * Seta o Número Identificação vindo do Cadastro de Cliente.
	 */
	function setaNumeroIdentificacao(nrIdentificacao) {
		if(getElementValue("tipoSelecionado") == "remetente") {
			var identificacao = document.getElementById("clienteByIdClienteRemetente.pessoa.nrIdentificacao");
			identificacao.value = nrIdentificacao;
			
			clienteByIdClienteRemetente_pessoa_nrIdentificacaoOnChangeHandler();
		} else if(getElementValue("tipoSelecionado") == "destinatario") {
			var identificacao = document.getElementById("clienteByIdClienteDestinatario.pessoa.nrIdentificacao");
			identificacao.value = nrIdentificacao;
			
			clienteByIdClienteDestinatario_pessoa_nrIdentificacaoOnChangeHandler();
		}		
	}
	
	/**
	 * Seta os dados do Endereço na tela pai após salvar o registro.
	 */
	function setaDadosEndereco(mapEndereco) {
		if(getElementValue("tipoSelecionado") == "remetente") {
			setElementValue("enderecoPessoaRemetente.idEnderecoPessoa", mapEndereco.idEnderecoPessoa);
			setElementValue("municipioRemetente.nmMunicipio", mapEndereco.municipio.nmMunicipio);
			setElementValue("ufRemetente", mapEndereco.municipio.unidadeFederativa.sgUnidadeFederativa);
		} else if(getElementValue("tipoSelecionado") == "destinatario") {
			setElementValue("enderecoPessoaDestinatario.idEnderecoPessoa", mapEndereco.idEnderecoPessoa);
			setElementValue("municipioDestinatario.nmMunicipio", mapEndereco.municipio.nmMunicipio);
			setElementValue("ufDestinatario", mapEndereco.municipio.unidadeFederativa.sgUnidadeFederativa);
		}		
					
		concatenaEndereco(mapEndereco);
	}

	/**
	 * Concatena a descrição e o numero do EnderecoPessoa
	 */
	 //FIXME Internacionalizar
	function concatenaEndereco(data) {
		var strEndereco = data.tipoLogradouro.dsTipoLogradouro + " " + data.dsEndereco + ", nº.: " + data.nrEndereco;
		if(data.dsComplemento != undefined && data.dsComplemento != "") {
			strEndereco = strEndereco + " / compl.: " + data.dsComplemento;
		}
		if(data.dsBairro != undefined && data.dsBairro != "") {
			strEndereco = strEndereco + "\nBairro: " + data.dsBairro;
		}		
		strEndereco = strEndereco + "\nCEP: " + data.nrCep;
		
		if(getElementValue("tipoSelecionado") == "remetente") {
			setElementValue("enderecoRemetente", strEndereco);
		} else if(getElementValue("tipoSelecionado") == "destinatario") {
			setElementValue("enderecoDestinatario", strEndereco);
		}		
	}
	
	/**
	 * Limpa os campos relacionados a funcionario destino
	 */
	function limpaCamposFuncionario() {				
		if(getElementValue("usuarioByIdUsuarioDestino.nrMatricula") == "" && 
		   getElementValue("usuarioByIdUsuarioDestino.nmUsuario") != "") {
			resetValue("usuarioByIdUsuarioDestino.idUsuario");			
		}
		return usuarioByIdUsuarioDestino_nrMatriculaOnChangeHandler();
	}	
	
	fDestinatario(habilitaCamposDestinatario)
	fRemetente(habilitaCamposRemetente)
	setDisabled("usuarioByIdUsuarioOrigem.idUsuario", true);
</script>