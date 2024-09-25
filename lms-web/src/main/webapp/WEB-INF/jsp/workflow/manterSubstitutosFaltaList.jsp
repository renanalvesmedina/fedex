<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.workflow.manterSubstitutosFaltaAction">
	<adsm:form action="/workflow/manterSubstitutos">
	
		<adsm:textbox size="60" label="comite" dataType="text" property="integrante.comite.nmComite" disabled="true" serializable="false"/>
		<adsm:hidden property="integrante.comite.idComite" serializable="true"/>
		<adsm:hidden property="integrante.idIntegrante" serializable="true"/>

		<adsm:textbox label="integrante" 
					  property="nmIntegrante" 
					  dataType="text"					  
					  size="60" 
					  maxLength="45"					  
					  width="80%" 
					  serializable="false"/>
					  
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
					   domain="DM_STATUS"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="substitutoFalta"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	
	<adsm:grid idProperty="idSubstitutoFalta" property="substitutoFalta" gridHeight="200">
		<adsm:gridColumn title="usuarioSubstituto" property="usuario.nmUsuario"/>
		<adsm:gridColumn title="perfilSubstituto" property="perfil.dsPerfil"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
