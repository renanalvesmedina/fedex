<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.consultarJobsAction">
	<adsm:form id="form1" action="/configuracoes/consultarJobs" idProperty="idStatusJob" onDataLoadCallBack="onDetailDataLoad">

		<adsm:textbox property="idJob" dataType="text" 
				      label="codigoJob" width="85%" disabled="true"/> 
						  
		<adsm:textbox property="jobGroup" dataType="text" 
					  label="grupoExecucao" disabled="true" size="40"/>

		<%-- 
		<adsm:hidden property="idGrupoJob" serializable="true"/>
		<adsm:textbox property="grupoJob" dataType="text" 
					  label="grupoJob" disabled="true" size="40"/>
		--%>		

		<adsm:textbox label="processo" property="schedulableUnit" dataType="text" width="85%" size="80" disabled="true"/>
					 
		<adsm:textbox property="description" dataType="text" 
					  label="descricaoJob" width="85%" size="80" disabled="true"/>
		
		<adsm:textbox property="fireTime" dataType="JTDateTimeZone" label="disparoJob" disabled="true" picker="false"/>
		
		<adsm:textbox property="login" label="usuario" dataType="text" disabled="true"/>

		<adsm:textbox property="statusAgendamento" label="statusAgendamento" dataType="text" disabled="true"/>
	
		<adsm:textbox property="statusJob" label="statusJob" dataType="text" disabled="true"/>			  
			
		<adsm:textbox property="cronExpression" label="expressaoCron" dataType="text" disabled="true"/>
	
	</adsm:form>
	
	<adsm:grid idProperty="idJob" property="gridLogs"  title="log" autoSearch="false"
		   paginatedService="lms.configuracoes.consultarJobsAction.findPaginatedLogs"
		   rows="6" onRowClick="noAction" width="880" scrollBars="horizontal" selectionMode="none" 
		   onDataLoadCallBack="onPopulateGrid" gridHeight="140">
			   
		<%-- 
   		<adsm:gridColumn property="firedTime" title="disparoJob" dataType="JTDateTimeZone" width="120" />
   		--%>
		<adsm:gridColumn property="messageTime" title="dataHoraLog" dataType="JTDateTimeZone" width="120"/>
   		<adsm:gridColumn property="level" title="nivel" dataType="text" width="60" />
   		<adsm:editColumn property="message" title="mensagem" dataType="text" field="textbox" width="350"/>
   		<adsm:editColumn property="className" title="nomeClasse" dataType="text" field="textbox" width="350"/>
   		
		<adsm:buttonBar>
			<adsm:button id="btnReExecutado" caption="reExecucutarJob" onclick="validateExecutarJob();"/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			setDisabled("btnReExecutado", true);
		} 
	}
	
	/**
	 * Callback da rotina de 'onDetailDataLoad'.
	 *
	 * @param data
	 * @param error
	 */
	function onDetailDataLoad_cb(data, error) {

		onDataLoad_cb(data, error);
		gridLogsGridDef.resetGrid();
	
		/*
	
		setElementValue("idStatusJob", data.idStatusJob);
	
		setElementValue("schedulableUnit", data.schedulableUnit);
		setElementValue("idJob", data.idJob);
		setElementValue("jobGroup", data.jobGroup);
		
		//setElementValue("idGrupoJob", data.idGrupoJob);
		//setElementValue("grupoJob", data.grupoJob);
		
		setElementValue("description", data.description);
		setElementValue("fireTime", setFormat(document.getElementById("fireTime"), data.fireTime));
		setElementValue("login", data.login);
		setElementValue("statusAgendamento", data.statusAgendamento);
		setElementValue("statusJob", data.statusJob);
		setElementValue("cronExpression", data.cronExpression);
		*/
		populaGrid();
	}

	function populaGrid() {
		findButtonScript("gridLogs", getElement("form1"));
	}

	function noAction(){
		return false;
	}
	
	function onPopulateGrid_cb(data,erro){
		for(var i = 0; i < gridLogsGridDef.getRowCount(); i++) {
			getElement("gridLogs:"+i+".message").readOnly = true;
			getElement("gridLogs:"+i+".className").readOnly = true;
		}
	}
	
	/**
	 * Faz a validacao da re-execucao do job corrente.
	 */
	function validateExecutarJob() {
		var sdo = createServiceDataObject("lms.configuracoes.consultarJobsAction.validateReExecutarJob", "validateExecutarJob", {value:getElementValue("idStatusJob")});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateExecutarJob_cb(data, error) {
			
		if (data._exception==undefined) {
			reExecutarJob();
		} else {
			if (confirm(error)) {
				reExecutarJob();
			}
		}	
	}
	
	function reExecutarJob() {
		var sdo = createServiceDataObject("lms.configuracoes.consultarJobsAction.generateReExecutarJob", "reExecutarJob", {idStatusJob:getElementValue("idStatusJob")});
		xmit({serviceDataObjects:[sdo]});
	}
	
	
	function reExecutarJob_cb(data, error)  {
		if (data._exception==undefined) {
			showSuccessMessage();
		} else {
			alert(error);
		}
		
		populaGrid();
	}
</script>		
		