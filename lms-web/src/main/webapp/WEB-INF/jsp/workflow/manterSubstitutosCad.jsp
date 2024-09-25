<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.workflow.manterSubstitutosAction">

	<adsm:form action="/workflow/manterSubstitutos" idProperty="idSubstituto">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-39002"/>
			<adsm:include key="LMS-39006"/>
		</adsm:i18nLabels>


        <adsm:lookup service="adsm.security.perfilService.findLookup" dataType="text" 
        	property="perfilSubstituido" criteriaProperty="dsPerfil" idProperty="idPerfil"
        	label="perfilSubstituido" minLengthForAutoPopUpSearch="1" exactMatch="false"
        	size="60" maxLength="60" width="80%" labelWidth="20%" 
        	action="/seguranca/manterPerfil"/>     					 
   					 
        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuarioSubstituido"
			         labelWidth="20%"
			         width="80%"
					 property="usuarioByIdUsuarioSubstituido" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 maxLength="16"		 					 
					 service="lms.workflow.manterSubstitutosAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioSubstituido.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioSubstituido.nmUsuario" disabled="true" serializable="false" size="40"/>
		</adsm:lookup>					 
        
        <adsm:lookup label="usuarioSubstituto"
			         labelWidth="20%"
			         width="80%"
					 property="usuarioByIdUsuarioSubstituto" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 maxLength="16"	
					 required="true"	 					 
					 service="lms.workflow.manterSubstitutosAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
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

	/**
	* Verifica a exclusividade do preenchimento dos campos perfilSubstituido e usuarioSubstituido
	* Verifica também se o usuário substituido não é o mesmo usuário substituto
	* @param elem Botão Salvar
	* @return false se houver erro e true se bem sucedido
	* @exception LMS-39002 Deve ser informado perfil ou usuário.
	* @exception LMS-39006 O usuário substituto não pode ser igual ao usuário substituído
	*/
	function verificaCampos(elemento){
	
		var perfilSubstituido  = getElementValue("perfilSubstituido.idPerfil");
		var usuarioSubstituido = getElementValue("usuarioByIdUsuarioSubstituido.idUsuario");
		var usuarioSubstituto  = getElementValue("usuarioByIdUsuarioSubstituto.idUsuario");
		
		if( (perfilSubstituido != "" && usuarioSubstituido != "") ||
		    (perfilSubstituido == "" && usuarioSubstituido == "") ){
			alert(lms39002);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		if( (usuarioSubstituido != "" && usuarioSubstituto != "") && (usuarioSubstituido == usuarioSubstituto) ){
			alert(lms39006);
			setFocusOnFirstFocusableField(document);
			return false;
		}
	
		storeButtonScript('lms.workflow.manterSubstitutosAction.store', 'store', elemento.form);
		return true;
		 
	}
	
</script>