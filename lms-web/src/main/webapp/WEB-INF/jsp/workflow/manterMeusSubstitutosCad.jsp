<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script>

	function manterMeusSubstitutosPageLoad_cb(dados,errors){
		onPageLoad_cb(dados,errors);
		var sdo = createServiceDataObject("lms.workflow.manterSubstitutosAction.getUsuarioSession",
			"carregarUsuarioSession",new Array());
		xmit({serviceDataObjects:[sdo]});
	
	}
	
</script>

<adsm:window service="lms.workflow.manterSubstitutosAction" onPageLoadCallBack="manterMeusSubstitutosPageLoad">

	<adsm:form action="/workflow/manterMeusSubstitutos" idProperty="idSubstituto">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-39002"/>
			<adsm:include key="LMS-39006"/>
		</adsm:i18nLabels>
	   
        <adsm:lookup label="usuarioSubstituido"
			         labelWidth="20%"
			         width="80%"
					 property="usuarioByIdUsuarioSubstituido" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 maxLength="16"	
					 disabled="true"
					 picker="false"					 	 					 
					 service="lms.workflow.manterSubstitutosAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioSubstituido.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioSubstituido.nmUsuario" disabled="true" serializable="false" size="40"/>
		</adsm:lookup>	   
       

		<adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuarioSubstituto"
			         labelWidth="20%"
			         width="80%"
					 property="usuarioByIdUsuarioSubstituto" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 maxLength="16"		 					 
					 service="lms.workflow.manterSubstitutosAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS"
					 required="true">
			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioSubstituto.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioSubstituto.nmUsuario" disabled="true" serializable="false" size="40"/>
		</adsm:lookup>			
		              
        <adsm:range label="periodoSubstituicao" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtSubstituicaoInicial" size="10" required="true"/>
	    	<adsm:textbox dataType="JTDate" property="dtSubstituicaoFinal"   size="10" required="true"/>
	    </adsm:range>	  
                      
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="storeButton" onclick="verificaCampos(this)" buttonType="storeButton" disabled="false"/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
		
		<script>
			var lms39002 = i18NLabel.getLabel('LMS-39002');
			var lms39006 = i18NLabel.getLabel('LMS-39006');
		</script>
		
	</adsm:form>
</adsm:window>
<script>

	var MATRICULA_USUARIO = undefined;
	var NOME_USUARIO = undefined;
	var ID_USUARIO = undefined;

	function carregarUsuarioSession_cb(data,erro){
		MATRICULA_USUARIO = data.nrMatricula;
		NOME_USUARIO = data.nmUsuario;
		ID_USUARIO = data.idUsuario;
	}

	function initWindow(eventObj){
		if (eventObj.name != "gridRow_click"){
			setElementValue("usuarioByIdUsuarioSubstituido.nrMatricula", MATRICULA_USUARIO);
			setElementValue("usuarioByIdUsuarioSubstituido.nmUsuario", NOME_USUARIO);
			setElementValue("usuarioByIdUsuarioSubstituido.idUsuario", ID_USUARIO);

		}
	}

	/**
	* Verifica a exclusividade do preenchimento dos campos perfilSubstituido e usuarioSubstituido
	* Verifica também se o usuário substituido não é o mesmo usuário substituto
	* @param elem Botão Salvar
	* @return false se houver erro e true se bem sucedido
	* @exception LMS-39002 Deve ser informado perfil ou usuário.
	* @exception LMS-39006 O usuário substituto não pode ser igual ao usuário substituído
	*/
	function verificaCampos(elemento){
	
		var usuarioSubstituido = getElementValue("usuarioByIdUsuarioSubstituido.idUsuario");
		var usuarioSubstituto  = getElementValue("usuarioByIdUsuarioSubstituto.idUsuario");
		
		if( (usuarioSubstituido != "" && usuarioSubstituto != "") && (usuarioSubstituido == usuarioSubstituto) ){
			alert(lms39006);
			return false;
		}
	
		storeButtonScript('lms.workflow.manterSubstitutosAction.store', 'store', elemento.form);
		return true;
		 
	}
	
</script>