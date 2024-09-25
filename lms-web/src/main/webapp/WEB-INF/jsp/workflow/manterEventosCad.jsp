<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.manterEventosAction">
	<adsm:form action="/workflow/manterEventos" idProperty="idEventoWorkflow">
	
		<adsm:hidden property="statusAtivo" serializable="false" value="A"/>
	
		<adsm:lookup action="/workflow/manterComites" dataType="text" service="lms.workflow.manterComitesAction.findLookup" 
			exactMatch="false"
			property="comite" idProperty="idComite" criteriaProperty="nmComite" label="comite" labelWidth="17%" width="83%" required="true"
			size="60" maxLength="60" minLengthForAutoPopUpSearch="1"/>

		<adsm:lookup action="/workflow/manterTiposEventos" service="lms.workflow.manterTiposEventosAction.findLookup" dataType="integer" 
			property="tipoEvento" idProperty="idTipoEvento" criteriaProperty="nrTipoEvento" label="tipoEvento" 
			size="4" maxLength="4" labelWidth="17%" width="83%" required="true" exactMatch="true">
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" />
			<adsm:propertyMapping relatedProperty="tipoEvento.dsTipoEvento" modelProperty="dsTipoEvento" />
			<adsm:textbox dataType="text" property="tipoEvento.dsTipoEvento" size="60" maxLength="60" disabled="true"/>
			
		</adsm:lookup>

		<adsm:combobox property="tpAcaoAutomatica" label="acaoAutomatica" domain="DM_ACAO_AUTOMATICA_WORKFLOW" required="true" 
			labelWidth="17%" width="33%" onlyActiveValues="true"/>
		
		<adsm:textbox dataType="JTTime" property="hrAcaoAutomatica" label="tempoAcaoAutomatica" labelWidth="23%" width="27%" 
			biggerThan="00:00" smallerThan="23:59"
			required="true"/>
		
		<adsm:combobox property="tpAlerta" label="tipoAlerta" domain="DM_TIPO_ALERTA_WORKFLOW" required="true" onlyActiveValues="true"
			labelWidth="17%" width="33%"/>

		<adsm:checkbox  property="blRequerAprovacao" label="requerAprovacao" labelWidth="23%" width="27%"/>
		
		<adsm:textbox dataType="text" property="nmClasseVisualizacao" label="classeVisualizacao" maxLength="500" 
			size="80" labelWidth="17%" width="83%"/>
			
		<adsm:textbox dataType="text" property="nmClasseAcao" label="classeAcao" maxLength="500" size="80" labelWidth="17%" width="83%"/>

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="17%" width="83%" 
			required="true" onlyActiveValues="true"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>


<script type="text/javascript">
	
	function initWindow(eventObj){  
    	if (eventObj.name == "storeButton" || eventObj.name == "gridRow_click") {
	    	setDisabled("tipoEvento.idTipoEvento",true);
	    } else { 
	 		setDisabled("tipoEvento.idTipoEvento",false);
	    }
	} 

</script>