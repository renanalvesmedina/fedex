<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.workflow.emitirAprovacoesSolicitanteAction">
	<adsm:form action="/workflow/manterSubstitutos">
		
        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuarioSolicitante"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"				
					 required="true"	  
					 size="16" 
					 width="85%"
					 maxLength="16"		 					 
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" serializable="true" maxLength="60" size="40"/>
		</adsm:lookup>			

		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS_WORKFLOW_REL" width="35%" defaultValue="E" onchange="setaDescricaoSituacao(this);"/>
		
		<adsm:hidden property="dsSituacao" serializable="true"/>
		
		<adsm:range label="dataSolicitacao" width="35%">
			<adsm:textbox dataType="JTDate" property="dtSolicitacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtSolicitacaoFinal"/>
		</adsm:range>

		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.workflow.emitirAprovacoesSolicitanteAction" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script>
	/**
	* Seta a descrição da Situação para ser usada no cabeçalho do relatório
	*/
	function setaDescricaoSituacao(obj){
    	setElementValue("dsSituacao",obj.options[obj.selectedIndex].text);    
	}
</script>