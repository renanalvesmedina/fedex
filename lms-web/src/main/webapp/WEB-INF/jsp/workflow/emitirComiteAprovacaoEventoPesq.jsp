<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.emitirComiteAprovacaoEventoService">
	<adsm:form action="/workflow/emitirComiteAprovacaoEvento">
		<adsm:lookup action="/workflow/manterTiposEventos"
		       service="lms.workflow.tipoEventoService.findLookup" 
		       dataType="integer" 
		       property="tipoEvento" 
		       criteriaProperty="nrTipoEvento" 
		       idProperty="idTipoEvento"
			   label="evento" 
			   size="5" 
			   maxLength="4"
			   width="85%" >
			   <adsm:propertyMapping modelProperty="dsTipoEvento" relatedProperty="dsTipoEvento"/>
			   <adsm:propertyMapping modelProperty="nrTipoEvento" relatedProperty="nrTipoEvento"/>
			   <adsm:textbox dataType="text" property="dsTipoEvento" size="60" maxLength="60" disabled="true"/>
		</adsm:lookup>
        <adsm:hidden property="nrTipoEvento" serializable="true"/>
        <adsm:hidden property="dsModal" serializable="true"/>
        <adsm:hidden property="dsAbrangencia" serializable="true"/>
        
		<adsm:combobox label="modal" property="modal" domain="DM_MODAL" onchange="setDescricaoModal(this)" />
		<adsm:combobox property="abrangencia" label="abrangencia" domain="DM_ABRANGENCIA" onchange="setDescricaoAbrangencia(this)" />

		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.workflow.emitirComiteAprovacaoEventoService"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
		<script type="text/javascript">
		       function setDescricaoModal(obj) {
		           setElementValue("dsModal",obj.options[obj.selectedIndex].text);    
		       }  
		       
		       function setDescricaoAbrangencia(obj) {
		           setElementValue("dsAbrangencia",obj.options[obj.selectedIndex].text);    
		       }  
		</script>	
	</adsm:form>
</adsm:window>