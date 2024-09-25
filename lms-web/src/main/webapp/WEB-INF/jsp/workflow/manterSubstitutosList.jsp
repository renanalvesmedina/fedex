<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.manterSubstitutosAction">

	<adsm:form action="/workflow/manterSubstitutos">	

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
					 service="lms.workflow.manterSubstitutosAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioSubstituto.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioSubstituto.nmUsuario" disabled="true" serializable="false" size="40"/>
		</adsm:lookup>			
        
        <adsm:textbox label="periodoSubstituicao"
        			  dataType="JTDate" 
                      property="dtSubstituicao" 
                      size="10"
                      labelWidth="20%"
                      width="80%"/>
                      
        <adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="substituto"/>
			<adsm:resetButton/>
		</adsm:buttonBar>              
        
	</adsm:form>	
	
	<adsm:grid idProperty="idSubstituto" property="substituto" 
			   defaultOrder="perfilSubstituido_.dsPerfil, usuarioByIdUsuarioSubstituido_funcionario_codPessoa_.nome, usuarioByIdUsuarioSubstituto_funcionario_codPessoa_.nome, dtSubstituicaoInicial"
	           gridHeight="200">
        <adsm:gridColumn width="36%" title="perfilSubstituido"  property="perfilSubstituido.dsPerfil" dataType="text"/>
		<adsm:gridColumn width="20%" title="usuarioSubstituido" property="usuarioByIdUsuarioSubstituido.nmUsuario" dataType="text"/>
		<adsm:gridColumn width="20%" title="usuarioSubstituto"  property="usuarioByIdUsuarioSubstituto.nmUsuario" dataType="text"/>
		<adsm:gridColumn width="12%" title="periodoInicial"     property="dtSubstituicaoInicial" dataType="JTDate"/>
		<adsm:gridColumn width="12%" title="periodoFinal"       property="dtSubstituicaoFinal" dataType="JTDate"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>

</adsm:window>