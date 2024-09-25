<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.manterIntegrantesComiteAction">
	<adsm:form action="/workflow/manterIntegrantesComite" idProperty="idIntegrante" onDataLoadCallBack="myOnDataLoad">
		<adsm:hidden property="nmIntegrante" serializable="false"/>
		
		<adsm:lookup action="/workflow/manterComites" dataType="text" 
			service="lms.workflow.comiteService.findLookup"
			minLengthForAutoPopUpSearch="1" exactMatch="false" 
			property="comite" idProperty="idComite" criteriaProperty="nmComite" 
			label="comite" labelWidth="20%" width="80%" maxLength="60" size="60" required="true"/>
        <adsm:lookup service="adsm.security.perfilService.findLookup" dataType="text" 
        	property="perfil" criteriaProperty="dsPerfil" idProperty="idPerfil"
        	label="perfil" minLengthForAutoPopUpSearch="1" exactMatch="false"
        	size="60" maxLength="60" labelWidth="20%" width="80%" 
        	action="/seguranca/manterPerfil">
        	<adsm:propertyMapping relatedProperty="nmIntegrante" modelProperty="dsPerfil"/>
        </adsm:lookup>
        
        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuario"
			         labelWidth="20%"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 maxLength="16"	
					 width="16%"		 					 
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" serializable="false" maxLength="60" size="40" width="35%"/>
		</adsm:lookup>		
        
		<adsm:textbox dataType="integer" property="nrOrdemAprovacao"  label="ordemAprovacao" maxLength="2" size="5" labelWidth="20%" width="80%"  required="true"/>
		<adsm:combobox property="tpSituacao" domain="DM_SITUACAO" label="situacao" labelWidth="20%" width="30%" required="true"/>
		<adsm:buttonBar>
            <adsm:button caption="substitutoPorFalta" action="/workflow/manterSubstitutosFalta" cmd="main">
            	<adsm:linkProperty src="comite.idComite" target="integrante.comite.idComite"/>
            	<adsm:linkProperty src="comite.nmComite" target="integrante.comite.nmComite"/>
            	<adsm:linkProperty src="idIntegrante" target="integrante.idIntegrante"/>
            	<adsm:linkProperty src="nmIntegrante" target="nmIntegrante"/>
            </adsm:button>
			<adsm:button caption="substituto" action="/workflow/manterSubstitutos" cmd="main">
            	<adsm:linkProperty src="usuario.idUsuario" target="usuarioByIdUsuarioSubstituido.idUsuario"/>                	
            	<adsm:linkProperty src="usuario.nmUsuario" target="usuarioByIdUsuarioSubstituido.nmUsuario"/>
            	<adsm:linkProperty src="usuario.nrMatricula" target="usuarioByIdUsuarioSubstituido.nrMatricula"/>
            	<adsm:linkProperty src="perfil.idPerfil" target="perfilSubstituido.idPerfil"/>
            	<adsm:linkProperty src="perfil.dsPerfil" target="perfilSubstituido.dsPerfil"/>
            </adsm:button>			
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function myOnDataLoad_cb(d,e,m,o){
		onDataLoad_cb(d,e,m,o);	
		setaDadosIntegrante();
	}

	function setaDadosIntegrante(){
		if (getElementValue('usuario.idUsuario') != ""){
			setElementValue('nmIntegrante',getElementValue('usuario.nmUsuario'));
		} else {
			setElementValue('nmIntegrante',getElementValue('perfil.dsPerfil'));	
		}
	}

	document.getElementById("usuario.tpCategoriaUsuario").masterLink = "true";

	function initWindow(eventObj){
		if( eventObj.name == 'storeButton' ){
			setaDadosIntegrante();	
		}
	}
	
</script>