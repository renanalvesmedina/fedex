<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.workflow.manterIntegrantesComiteAction">
	<adsm:form action="/workflow/manterIntegrantesComite">
		<adsm:lookup action="/workflow/manterComites" dataType="text" service="lms.workflow.comiteService.findLookup" 
			property="comite" idProperty="idComite" criteriaProperty="nmComite" label="comite" 
			minLengthForAutoPopUpSearch="1" exactMatch="false"
			labelWidth="20%" width="80%" maxLength="60" size="60"/>
        <adsm:lookup service="adsm.security.perfilService.findLookup" dataType="text" 
        	property="perfil" criteriaProperty="dsPerfil" idProperty="idPerfil"
        	label="perfil" minLengthForAutoPopUpSearch="1" exactMatch="false"
        	size="60" maxLength="60" labelWidth="20%" width="80%" 
        	action="/seguranca/manterPerfil"/>
        	
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
        	
		<adsm:textbox dataType="integer" property="nrOrdemAprovacao"  label="ordemAprovacao" maxLength="2" size="5" labelWidth="20%" width="20%"/>
		<adsm:combobox property="tpSituacao" domain="DM_SITUACAO" label="situacao" labelWidth="20%" width="30%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="integrante"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
	</adsm:form>
	<adsm:grid idProperty="idIntegrante" property="integrante">
		<adsm:gridColumn width="25%" title="comite" property="comite.nmComite"/>
		<adsm:gridColumn width="25%" title="perfil" property="perfil.dsPerfil"/>
		<adsm:gridColumn width="25%" title="usuario" property="usuario.nmUsuario"/>
		<adsm:gridColumn width="15%" title="ordemAprovacao" property="nrOrdemAprovacao" dataType="integer"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>			
	</adsm:grid>	
</adsm:window>
<script>
	document.getElementById("usuario.tpCategoriaUsuario").masterLink = "true";
</script>