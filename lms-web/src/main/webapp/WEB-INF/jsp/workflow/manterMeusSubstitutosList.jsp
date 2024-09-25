<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script>

	function manterMeusSubstitutosPageLoad_cb(dados,errors){
		onPageLoad_cb(dados,errors);
		var sdo = createServiceDataObject("lms.workflow.manterSubstitutosAction.getUsuarioSession",
			"carregarUsuarioSession",new Array());
		xmit({serviceDataObjects:[sdo]});
	
	}
	
</script>

<adsm:window service="lms.workflow.manterSubstitutosAction" onPageLoadCallBack="manterMeusSubstitutosPageLoad">

	<adsm:form action="/workflow/manterMeusSubstitutos">

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
					 disabled="true"
					 picker="false"					 	 					 
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
	           gridHeight="200"  
	           rows="12">
        <adsm:gridColumn width="46%" title="usuarioSubstituto"  property="usuarioByIdUsuarioSubstituto.nmUsuario" dataType="text"/>
		<adsm:gridColumn width="27%" title="periodoInicial"     property="dtSubstituicaoInicial" dataType="JTDate"/>
		<adsm:gridColumn width="27%" title="periodoFinal"       property="dtSubstituicaoFinal" dataType="JTDate"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>

</adsm:window>

<script>
	document.getElementById("usuarioByIdUsuarioSubstituido.idUsuario").masterLink = "true";
	document.getElementById("usuarioByIdUsuarioSubstituido.nrMatricula").masterLink = "true";
	document.getElementById("usuarioByIdUsuarioSubstituido.nmUsuario").masterLink = "true";	

	var MATRICULA_USUARIO = undefined;
	var NOME_USUARIO = undefined;
	var ID_USUARIO = undefined;

	function carregarUsuarioSession_cb(data,erro){
		MATRICULA_USUARIO = data.nrMatricula;
		NOME_USUARIO = data.nmUsuario;
		ID_USUARIO = data.idUsuario;
		
		setElementValue("usuarioByIdUsuarioSubstituido.nrMatricula", MATRICULA_USUARIO);
		setElementValue("usuarioByIdUsuarioSubstituido.nmUsuario", NOME_USUARIO);
		setElementValue("usuarioByIdUsuarioSubstituido.idUsuario", ID_USUARIO);
	}

</script>