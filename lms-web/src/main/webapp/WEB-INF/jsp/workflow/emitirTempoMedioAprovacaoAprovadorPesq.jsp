<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.workflow.emitirTempoMedioAprovacaoAprovadorAction">
	<adsm:form action="/workflow/emitirTempoMedioAprovacaoAprovador">
	
		<adsm:hidden property="sgFilial"/>
	
		<adsm:lookup service="lms.workflow.emitirTempoMedioAprovacaoAprovadorAction.findLookupFilial" 
					 dataType="text" 
					 property="filial"
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filial" 
					 size="3"
					 maxLength="3" 
					 width="85%" 
					 action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="nmFilial"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="nmFilial" size="30" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" width="35%" onchange="setaDescricaoModal(this)"/>
		<adsm:hidden property="dsModal" serializable="true"/>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" width="35%" onchange="setaDescricaoAbrangencia(this)"/>
		<adsm:hidden property="dsAbrangencia" serializable="true"/>		
		
        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="aprovador"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="16" 
					 width="85%"
					 maxLength="16"		 					 
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" serializable="true" maxLength="60" size="40"/>
		</adsm:lookup>			
			
		<adsm:range label="periodo" width="35%">
			<adsm:textbox dataType="JTDate" property="dtInicial"  required="true"/>
			<adsm:textbox dataType="JTDate" property="dtFinal"  required="true"/>
		</adsm:range>
		
   		<adsm:combobox property="tpFormatoRelatorio" 
					   label="formatoRelatorio" 
					   required="true"
					   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>		
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.workflow.emitirTempoMedioAprovacaoAprovadorAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	/**
	* Seta a descrição Modal para ser usada no cabeçalho do relatório
	*/
	function setaDescricaoModal(obj){
    	setElementValue("dsModal",obj.options[obj.selectedIndex].text);    
	}
	
	/**
	* Seta a descrição Abrangencia para ser usada no cabeçalho do relatório
	*/
	function setaDescricaoAbrangencia(obj){
    	setElementValue("dsAbrangencia",obj.options[obj.selectedIndex].text);    
	}
</script>
