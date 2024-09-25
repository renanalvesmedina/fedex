<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterEmbarquesProibidosAction" onPageLoad="myOnPageLoad">
	<adsm:form action="/vendas/manterEmbarquesProibidos" idProperty="idProibidoEmbarque" onDataLoadCallBack="embarqueProibidos">
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="motivoUsuarioSemTributacaoMTZ" serializable="true"/>
		<adsm:hidden property="cliente.idCliente"/>
		<%-- Objetos para controle de sessão --%>
		<adsm:hidden property="session.nrMatricula" serializable="false"/>
		<adsm:hidden property="session.nmUsuario" serializable="false"/>
		<adsm:hidden property="session.idUsuario" serializable="false"/>
		<adsm:hidden property="session.dtToday" serializable="false"/>

		<adsm:textbox
			property="cliente.pessoa.nrIdentificacao"
			dataType="text"
			size="20"
			labelWidth="15%"
			width="16%"
			maxLength="20"
			disabled="true"
			serializable="false"
			label="cliente">
			<adsm:textbox
				property="cliente.pessoa.nmPessoa"
				dataType="text"
				size="30"
				disabled="true"
				serializable="false" 
				width="50%"
				maxLength="60"/>
		</adsm:textbox>

		<adsm:hidden property="dtBloqueioCarregada" serializable="true"/>
		<adsm:textbox
			property="dtBloqueio"
			label="dataBloqueio"
			dataType="JTDate" 
			labelWidth="15%"
			width="32%"
			required="true"/>

        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup
        	label="responsavel"
			property="usuarioBloqueio" 
			idProperty="idUsuario"
			criteriaProperty="nrMatricula" 
			dataType="text"					  
			size="16" required="true"
			maxLength="16"	
			onchange="return usuarioBloqueioOnChange(this);"
			onDataLoadCallBack="funcionarioBloqueio"
			onPopupSetValue="funcionarioBloqueioOnPopupSetValues"
			service="lms.vendas.manterEmbarquesProibidosAction.findLookupUsuarioFuncionario"
			action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping
				relatedProperty="usuarioBloqueio.nmUsuario"
				modelProperty="nmUsuario" />
			<adsm:propertyMapping
				criteriaProperty="usuario.tpCategoriaUsuario"
				modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox
				dataType="text"
				property="usuarioBloqueio.nmUsuario"
				disabled="true"
				serializable="false"/>
		</adsm:lookup>

		<adsm:combobox
			property="motivoProibidoEmbarque.idMotivoProibidoEmbarque" 
			label="motivoBloqueio" 
			optionLabelProperty="dsMotivoProibidoEmbarque" 
			optionProperty="idMotivoProibidoEmbarque" 
			onlyActiveValues="true" 
			service="lms.vendas.motivoProibidoEmbarqueService.find" 
			labelWidth="15%" 
			width="82%" 
			required="true"					   
			onDataLoadCallBack="myOnDataLoadCallBackMotivoProibido"/>

		<adsm:textarea
			property="dsBloqueio"
			label="descricao"
			columns="116"
			rows="3"
			maxLength="500"
			labelWidth="15%"
			width="82%"
			required="true"/>

		<adsm:textbox
			property="dtDesbloqueio"
			label="dataDesbloqueio"
			dataType="JTDate" 
			labelWidth="15%"
			width="32%"/>

        <adsm:lookup
        	label="responsavel"
			property="usuarioDesbloqueio" 
			idProperty="idUsuario"
			criteriaProperty="nrMatricula" 
			dataType="text"					  
			size="16" 
			maxLength="16"	
			onchange="return usuarioDesbloqueioOnChange(this);"
			onDataLoadCallBack="funcionarioDesbloqueio"
			onPopupSetValue="funcionarioDesbloqueioOnPopupSetValues"
			service="lms.vendas.manterEmbarquesProibidosAction.findLookupUsuarioFuncionario"
			action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping
				relatedProperty="usuarioDesbloqueio.nmUsuario"
				modelProperty="nmUsuario" />
			<adsm:propertyMapping
				criteriaProperty="usuario.tpCategoriaUsuario"
				modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox
				dataType="text"
				property="usuarioDesbloqueio.nmUsuario"
				disabled="true"
				serializable="false"/>
		</adsm:lookup>

		<adsm:textarea
			property="dsDesbloqueio"
			label="descricao"
			columns="116"
			rows="3"
			maxLength="500"
			labelWidth="15%"
			width="82%"/>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" disabled="false" callbackProperty="afterStore"/>
			<adsm:newButton id="newButton" caption="limpar" disabled="false"/>
			<adsm:removeButton id="removeButton" caption="excluir" onclick="return myRemoveButtonOnClick()" disabled="true"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	getElement("session.nrMatricula").masterLink = "true";
	getElement("session.nmUsuario").masterLink = "true";
	getElement("session.idUsuario").masterLink = "true";
	getElement("session.dtToday").masterLink = "true";

	function myOnPageLoad() {
		onPageLoad();
		var sdo = createServiceDataObject("lms.vendas.manterEmbarquesProibidosAction.findSessionData", "sessionData");
		xmit({serviceDataObjects:[sdo]});
	}
	function sessionData_cb(data,error){
		if(error != undefined) {
			alert(error);
			return;
		}
		setElementValue("session.nrMatricula", data.nrMatricula);
		setElementValue("session.nmUsuario", data.nmUsuario);
		setElementValue("session.idUsuario", data.idUsuario);
		setElementValue("session.dtToday", data.dtToday);
	}

	function embarqueProibidos_cb(data,erro){
		onDataLoad_cb(data,erro);
		/*
		*  Proteger os botões ?Salvar?, ?Limpar? e ?Excluir?, caso a data do desbloqueio esteja informada ou o usuário 
		*  logado não tenha permissão de acesso à filial responsável comercialmente pelo cliente (ID_FILIAL_ATENDE_COMERCIAL). 
		*/
		setElementValue("dtBloqueioCarregada",data.dtBloqueio);

		var parameters = new Array();
		setNestedBeanPropertyValue(parameters, "idFilial", getElementValue("idFilial"));	
		setNestedBeanPropertyValue(parameters, "idProibidoEmbarque", getElementValue("idProibidoEmbarque"));		
		var sdo = createServiceDataObject("lms.vendas.manterEmbarquesProibidosAction.disabilitaBotoesDetalhamento", "disabilitaBotoes", parameters);
		xmit({serviceDataObjects:[sdo]});	
	}

	function disabilitaBotoes_cb(data,error){
		var disabilitarBotoes = data.disabilitarBotoes;		

		if(data.motivoUsuarioSemTributacaoMTZ != undefined){
			setElementValue("motivoUsuarioSemTributacaoMTZ",data.motivoUsuarioSemTributacaoMTZ);	
		}
		
		if(getElementValue("dtDesbloqueio") != "" || disabilitarBotoes == "true"){
			
			var ex = data.errorMessage;
			if(ex != undefined){
				alert(ex);
			}
						
			setDisabled("storeButton", true);
			setDisabled("removeButton", true);								
		}

		var disabilitarLimpar = data.disabilitarLimpar;
		if(disabilitarLimpar != undefined && disabilitarLimpar == "true"){
			setDisabled("newButton", true);	
		}				
	}

	
	function usuarioBloqueioOnChange(eThis){
		if (!validatePermission() && eThis.value != ""){
			alert(getTabGroup(document).getTab("pesq").getElementById("msgErroPermissao").value);
			resetValue(document.getElementById("usuarioBloqueio.idUsuario"));
			return false;
		}
		return usuarioBloqueio_nrMatriculaOnChangeHandler();
	}

	function funcionarioBloqueio_cb(data, erro){
		if (erro != undefined) {
	      	alert(erro);
		}

		var result = usuarioBloqueio_nrMatricula_exactMatch_cb(data);
	   	if (result) {
	   		var idUsuario = getNestedBeanPropertyValue(data[0], "idUsuario");
	   		validatePermissaoFuncionario(idUsuario, "Bloqueio");
	   	} else {
	       	setFocus("usuarioBloqueio.nrMatricula");
	       	resetValue("usuarioBloqueio.idUsuario");
		}
		return result;
	}

	function funcionarioBloqueioOnPopupSetValues(data,erro){
		var idUsuario = data.idUsuario;
		validatePermissaoFuncionario(idUsuario, "Bloqueio");
		return true;
	}

	function validateFuncionarioBloqueio_cb(data, erro){
		if (erro != undefined){
			alert(erro);
			resetValue("usuarioBloqueio.idUsuario");
			resetValue("usuarioBloqueio.nmUsuario");
			setFocus("usuarioBloqueio.nrMatricula");
		}
	}

	/** METODO GENERICO PARA VALIDACAO DE FUNCIONARIOS */
	function validatePermissaoFuncionario(idUsuario, type) {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", getElementValue("idFilial"));
		setNestedBeanPropertyValue(data, "idUsuario", idUsuario);
		var sdo = createServiceDataObject("lms.vendas.proibidoEmbarqueService.validatePermissaoFuncionario",
										  "validateFuncionario"+type, 
										  data);
		xmit({serviceDataObjects:[sdo]});
	}

	function usuarioDesbloqueioOnChange(eThis){
		if (!validatePermission() && eThis.value != ""){
			alert(getTabGroup(document).getTab("pesq").getElementById("msgErroPermissao").value);
			resetValue(document.getElementById("usuarioDesbloqueio.idUsuario"));
			return false;
		}
		return usuarioDesbloqueio_nrMatriculaOnChangeHandler();
	}

	function funcionarioDesbloqueio_cb(data, erro){
		if (erro != undefined) { 
	    	alert(erro);
	   	}
	   	var result = usuarioDesbloqueio_nrMatricula_exactMatch_cb(data);
	   	if (result) {
		 	var idUsuario = getNestedBeanPropertyValue(data[0], "idUsuario");
		 	validatePermissaoFuncionario(idUsuario, "Desbloqueio");
	   	} else {
	       	setFocus("usuarioDesbloqueio.nrMatricula");
	       	resetValue("usuarioDesbloqueio.idUsuario");
	   	}
	   	return result;
	}

	function funcionarioDesbloqueioOnPopupSetValues(data,erro){
		var idUsuario = data.idUsuario;
		validatePermissaoFuncionario(idUsuario, "Desbloqueio");
		return true;
	}

	function validateFuncionarioDesbloqueio_cb(data, erro){
		if (erro != undefined){
			alert(erro);
			resetValue("usuarioDesbloqueio.idUsuario");
			resetValue("usuarioDesbloqueio.nmUsuario");
			setFocus("usuarioDesbloqueio.nrMatricula");
		}
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function initWindow(eventObj){
		if (eventObj != undefined && eventObj.name != "gridRow_click" && eventObj.name != "storeButton"){
			setElementValue("dtBloqueio",getElementValue("session.dtToday"));
			if (validatePermission()){
				setElementValue("usuarioBloqueio.nrMatricula", getElementValue("session.nrMatricula"));
				setElementValue("usuarioBloqueio.nmUsuario", getElementValue("session.nmUsuario"));
				setElementValue("usuarioBloqueio.idUsuario", getElementValue("session.idUsuario"));
			}
		}

		/*
		*  Proteger os botões ?Salvar?, ?Limpar? e ?Excluir?, caso a data do desbloqueio esteja informada ou o usuário 
		*  logado não tenha permissão de acesso à filial responsável comercialmente pelo cliente (ID_FILIAL_ATENDE_COMERCIAL). 
		*/
		var blDisabled = (!validatePermission() || getElementValue("dtDesbloqueio") != "");
		setDisabled("storeButton", blDisabled);
		setDisabled("newButton", blDisabled);
		if (eventObj.name != "newButton_click" && eventObj.name != "tab_click") {
			setDisabled("removeButton", blDisabled);
		}
	}

	function validatePermission() {
		var permission = getTabGroup(document).getTab("pesq").getElementById("permissao").value;
		return (permission == "true" || permission == true);
	}

	/**
	* Método utilizado para setar a informação de motivo Financeiro a descrição do motivo	
	* a ser mostrado no combo de motivos de bloqueio
	*/
	function myOnDataLoadCallBackMotivoProibido_cb(data){
		var blFinanceiro, x;
		for( x = 0; x < data.length; x++ ){
			blFinanceiro = getNestedBeanPropertyValue(data[x],"blFinanceiro");
			if( blFinanceiro == "true" ){
				data[x].dsMotivoProibidoEmbarque += " - Financeiro";
			}
		}
		motivoProibidoEmbarque_idMotivoProibidoEmbarque_cb(data);
	}
	
	/**
	* Este método destina-se a verificar se o usuário logado tem permissão
	* de acesso à filial responsável comercialmente pelo cliente antes
	* de excluir os registros selecionados.
	*/
	function myRemoveButtonOnClick(){
		var idFilial = getElementValue("idFilial");
		var sdo = createServiceDataObject("lms.vendas.proibidoEmbarqueService.validatePermissaoUsuarioAcessoFilial", 
										  "removeItens", 
										  {idFilial:idFilial});
		xmit({serviceDataObjects:[sdo]});
		return true;
	}

	/**
	* Método responsável pela exclusão dos registros selecionados na grid após verificações
	* de regras de negócio. 
	* Caso os testes das regras de negócio retornem OK, exclui os itens selecionados, caso contrário
	* informa o erro ocorrido.
	*/
	function removeItens_cb(data,error){
		var valido = (error == undefined);
		if( valido ){
			return removeButtonScript('lms.vendas.proibidoEmbarqueService.removeById', 'removeById', 'idProibidoEmbarque', this.document);
		} else {
			alert(error);
			return false;
		}
	}

	function afterStore_cb(data,erro) {

		if(erro != undefined) {
			alert(erro);
			return false;
		}	
		
		store_cb(data,erro);
		setDisabled("newButton", false);
		setFocus("newButton",false);
	}
</script>