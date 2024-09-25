<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.workflow.manterSubstitutosFaltaAction">
	<adsm:form action="/workflow/manterSubstitutosFalta" idProperty="idSubstitutoFalta">
	
		<adsm:textbox size="60" label="comite" dataType="text" property="integrante.comite.nmComite" disabled="true" serializable="false"/>
		<adsm:hidden property="integrante.comite.idComite" serializable="true"/>
		<adsm:hidden property="integrante.idIntegrante" serializable="true"/>

		<adsm:textbox label="integrante" 
					  property="nmIntegrante" 
					  dataType="text"					  
					  size="60" 
					  maxLength="45" 
					  width="80%" 
					  required="true"/>
					  
        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuarioSubstituto"
			         width="80%"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 maxLength="16"		 					 
					 service="lms.workflow.manterSubstitutosFaltaAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" serializable="false" size="40"/>
		</adsm:lookup>		  
        
        <adsm:lookup service="adsm.security.perfilService.findLookup" dataType="text" 
        	property="perfil" criteriaProperty="dsPerfil" idProperty="idPerfil"
        	label="perfilSubstituto" minLengthForAutoPopUpSearch="1" exactMatch="false"
        	size="60" maxLength="60" width="80%" 
        	action="/seguranca/manterPerfil"/>  

		<adsm:combobox label="situacao"
					   property="tpSituacao" 
					   domain="DM_STATUS"
					   required="true"/>
					   
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="storeButton" onclick="verificaCampos(this)" buttonType="storeButton" disabled="false"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script>

	/**
	* Verifica a exclusividade do preenchimento dos campos perfilSubstituto e usuarioSubstituto
	* @param elem Botão Salvar
	* @return false se houver erro e true se bem sucedido
	* @exception LMS-39003 O usuário substituto ou perfil substituto deve estar preenchido.
	*/
	function verificaCampos(elemento){

		var perfilSubstituto   = getElementValue("perfil.idPerfil");
		var usuarioSubstituto  = getElementValue("usuario.idUsuario");
		
		if( (perfilSubstituto != "" && usuarioSubstituto != "") ||
		    (perfilSubstituto == "" && usuarioSubstituto == "") ){
			alert(parent.i18NLabel.getLabel("LMS-39003"));
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		storeButtonScript('lms.workflow.manterSubstitutosFaltaAction.store', 'store', elemento.form);
		return true;
		 
	}
	
</script>