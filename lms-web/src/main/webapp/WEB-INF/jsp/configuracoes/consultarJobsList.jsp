<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.consultarJobsAction">
	<adsm:form action="/configuracoes/consultarJobs">

		<adsm:combobox label="grupoExecucao" 
			property="jobGroup"
			required="true"
			service="lms.configuracoes.consultarJobsAction.findJobGroups"
			optionLabelProperty="jobGroupName" 
			
			onchangeAfterValueChanged="enableSchedulableUnit(this);"
			optionProperty="jobGroupId">
		</adsm:combobox>

		<adsm:combobox property="schedulableUnit" service="lms.configuracoes.manterJobAction.findAllSchedulableUnit" 
					label="processo" width="80%"
					optionProperty="schedulableUnitId" optionLabelProperty="schedulableUnitName">
			<adsm:propertyMapping criteriaProperty="jobGroup"
				modelProperty="schedulableUnitId" />
		</adsm:combobox>
					
		<adsm:textbox property="login" dataType="text" width="85%"
					  label="usuario" size="40" maxLength="50"/>
		
		<adsm:textbox property="idJob" dataType="integer" label="codigo" size="15" width="80%"/>

	<%-- 
		<adsm:lookup dataType="text" property="grupoJob" idProperty="idGrupoJob" criteriaProperty="nmGrupo"
					 service="lms.configuracoes.manterJobAction.findLookupGrupoJob" action="/configuracoes/manterGrupoJob"
					 label="grupoJob" size="40" maxLength="80"/>	
	--%>
								 
		<adsm:range label="disparoJob" labelWidth="15%" required="true" width="80%">
			<adsm:textbox dataType="JTDate" property="firedFrom" />
			<adsm:textbox dataType="JTDate" property="firedUpon"/> 
		</adsm:range>
		
		<adsm:combobox label="statusAgendamento" property="statusAgendamento" domain="DM_TRIGGER_STATUS" width="80%"/>
	
		<adsm:combobox label="statusJob" property="statusJob" domain="DM_JOB_STATUS" width="80%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridJobs" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idStatusJob" property="gridJobs" onRowClick="loadDetail"
			   paginatedService="lms.configuracoes.consultarJobsAction.findJobsExecutions" 
			   scrollBars="horizontal" rows="8" selectionMode="none" emptyLabel="naoAgendamentoGrupo" gridHeight="162">
			    
		<adsm:gridColumn property="jobGroup" title="grupoExecucao" dataType="text" width="140"/>
		<%--
		<adsm:gridColumn property="grupoJob" title="grupoJob" dataType="text" width="140"/>
		--%>
		<adsm:gridColumn property="description" title="descricaoJob" dataType="text" width="140"/>
		
		<adsm:gridColumn property="fireTime" title="disparoJob" dataType="JTDateTimeZone" width="120"/>		
		<adsm:gridColumn property="statusJob" title="statusJob" dataType="text" width="100"/>
		
		<adsm:gridColumn property="name" title="processo" dataType="text" width="500"/>
		
		<adsm:gridColumn property="parametersa" title="parametros" align="center" image="/images/popup.gif" link="javascript:abrirPopUpParametros" linkIdProperty="id" popupDimension="600,200" width="75"/>
		
		<adsm:gridColumn property="cronExpression" title="expressaoCron" dataType="text" width="100"/>
		<adsm:gridColumn property="nextFireTime" title="proximasExecucoes" dataType="JTDateTimeZone" width="150"/>

		<adsm:buttonBar/>
	</adsm:grid>
	
</adsm:window>


<script type="text/javascript">

	function enableSchedulableUnit(combobox) {
		setDisabled("schedulableUnit", (combobox.index > -1));
	}

	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") { 
			setDisableTab(true);
		} 
	}
	
	//############################################################
	// Funções apartir de objetos
	//############################################################
	
  	/**
	 * Retorno data funcao de populateGrupoExecucao;
	 *
	 * @param data
	 * @param error
	 */
	function populateGrupoExecucao_cb(data, error) {
		if (error==undefined) {
			setElementValue("grupoExecucao", data.grupoExecucao);
		} else {
			alert(error);
		}
	}
	
	//############################################################
	// Funções da grid
	//############################################################
	
	/**
	 * Carrega a tela de details a partir do clique na grid.
	 *
	 * @param id
	 */
	function loadDetail(id) {
	
		setDisableTab(false);
		
		gridJobsGridDef.detailGridRow("onDataLoad", {value:id});
		
		return false;
	}
	
	/**
       * Abre uma popUp contendo os parametros de chamada deste job
       *
       * @param id 
       */

      function abrirPopUpParametros(id){
		var dadosURL = "&idStatusJob="+id;
		showModalDialog('/configuracoes/consultarJobs.do?cmd=popUp'+dadosURL, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:400px;');
		event.cancelBubble=true;
		return false;
      }
	
	//############################################################
	// Funções basicas
	//############################################################
	
	/**
	 * Faz a pesquisa para determinar o grupo de execucao do metodo 
	 * em questao.
	 *
	 * @param
	 */
	function populateGrupoExecucao(id) {
		var data = new Object();
		data.idMetodo = id;
		
		var sdo = createServiceDataObject("lms.configuracoes.manterJobAction.validateJob", "populateGrupoExecucao", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Desabilita a aba de detalhamento
	 *
	 * @param disableTab aba gerenciamento
	 */
	function setDisableTab(disableTab) {
		var tabGroup = getTabGroup(this.document);
		if (tabGroup!=null) {
			if (disableTab!=null) tabGroup._tabsIndex[1].setDisabled(disableTab);
		}
	}

</script>

