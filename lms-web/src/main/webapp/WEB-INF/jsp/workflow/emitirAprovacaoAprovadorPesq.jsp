<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/workflow/emitirAprovacaoAprovador">
        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="aprovador"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 width="85%"
					 maxLength="16"	
					 required="true"	 					 
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" serializable="true" maxLength="60" size="40"/>
		</adsm:lookup>	        
        
		<adsm:combobox property="tpSituacao" label="situacao" labelWidth="15%" width="35%" domain="DM_STATUS_WORKFLOW" 
			onchange="situacaoChange(this)"/>
		<adsm:hidden property="dsSituacao" serializable="true"/>
		
        <adsm:range label="dataLiberacao" labelWidth="15%" width="85%" >
			<adsm:textbox dataType="JTDateTimeZone" property="dhLiberacaoInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhLiberacaoFinal"/> 
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.workflow.emitirAprovacaoAprovadorAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function situacaoChange(obj){
		setElementValue(document.getElementById("dsSituacao"), obj.options[obj.selectedIndex].text);
	}
</script>