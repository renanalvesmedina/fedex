<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.workflow.manterEventosAction" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/workflow/manterEventos">

		<adsm:lookup action="/workflow/manterComites" dataType="text" service="lms.workflow.manterComitesAction.findLookup" 
			property="comite" idProperty="idComite" criteriaProperty="nmComite" size="60" maxLength="60" label="comite" 
			labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="1" exactMatch="false"/>

		<adsm:lookup action="/workflow/manterTiposEventos" 
			service="lms.workflow.tipoEventoService.findLookup" 
			dataType="integer" 
			property="tipoEvento" 
			idProperty="idTipoEvento" 
			criteriaProperty="nrTipoEvento" 
			label="tipoEvento" 
			size="4"
			maxLength="4" 
			labelWidth="17%" 
			criteriaSerializable="true"
			width="83%" 
			exactMatch="true">
			<adsm:propertyMapping relatedProperty="tipoEvento.dsTipoEvento" modelProperty="dsTipoEvento"/>
			<adsm:textbox dataType="text" property="tipoEvento.dsTipoEvento" serializable="false" size="60" maxLength="60" disabled="true"/>
		</adsm:lookup>	

		<adsm:combobox property="tpAcaoAutomatica" label="acaoAutomatica" domain="DM_ACAO_AUTOMATICA_WORKFLOW" labelWidth="17%" width="33%"/>

		<adsm:textbox dataType="JTTime" property="hrAcaoAutomatica" label="tempoAcaoAutomatica" labelWidth="23%" width="27%"/>
		
		<adsm:combobox property="tpAlerta" label="tipoAlerta" domain="DM_TIPO_ALERTA_WORKFLOW" labelWidth="17%" width="33%"/>

		<adsm:combobox property="blRequerAprovacao" domain="DM_SIM_NAO" label="requerAprovacao" labelWidth="23%" width="27%"/>

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="17%" width="83%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="eventos"/>
			<adsm:button caption="limpar"  id='btnLimpar' disabled="false" onclick="limpar()" />
		</adsm:buttonBar>
		
	</adsm:form>
	<adsm:grid idProperty="idEventoWorkflow" property="eventos" rows="9">
		<adsm:gridColumn width="20%" title="comite" property="comite.nmComite"/>
		<adsm:gridColumn width="20%" title="tipoEvento" property="tipoEvento.codigoDescricao"/>
		<adsm:gridColumn width="15%" title="tempoAcaoAutomatica" property="hrAcaoAutomatica" align="center" dataType="JTTime"/>
		<adsm:gridColumn width="15%" title="acaoAutomatica" property="tpAcaoAutomatica" isDomain="true"/>
		<adsm:gridColumn width="10%" title="tipoAlerta" property="tpAlerta" isDomain="true"/>
		<adsm:gridColumn width="10%" title="requerAprovacao" property="blRequerAprovacao" renderMode="image-check"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function limpar(){
	cleanButtonScript(this.document, true);
	setElementValue('tipoEvento.dsTipoEvento','');
	return true;
}


function initWindow(eventObj){
	setDisabled('btnLimpar',false);
}


function myPageLoadCallBack_cb(data, error){  
	onPageLoad_cb(data, error);
	/**
	 * Foi comentado para poder navegar a partir da tela de tipo de evento. 
	 */
	/*
	setElementValue("tipoEvento.nrTipoEvento", "");
	setElementValue("tipoEvento.dsTipoEvento", "");
	*/
}

</script>