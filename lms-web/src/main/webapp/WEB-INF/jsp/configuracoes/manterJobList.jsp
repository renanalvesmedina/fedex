<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.manterJobAction">
	<adsm:form action="/configuracoes/manterJob" id="formJobList">

		<adsm:combobox label="grupoExecucao" 
			property="jobGroup"
			required="true"
			service="lms.configuracoes.manterJobAction.findJobGroups"
			optionLabelProperty="jobGroupName" 
			width="80%"
			optionProperty="jobGroupId"/>

		<adsm:combobox property="schedulableUnit" service="lms.configuracoes.manterJobAction.findAllSchedulableUnit" 
					label="processo"
					optionProperty="schedulableUnitId" optionLabelProperty="schedulableUnitName">
			<adsm:propertyMapping criteriaProperty="jobGroup"
				modelProperty="jobGroupId" />
		</adsm:combobox>
	
		<adsm:textbox property="login" dataType="text" width="85%"
					  label="usuario" size="40" maxLength="50"/>
		
		<adsm:textbox property="idJob" dataType="integer" label="codigo" size="15"/>

		<%-- 
		<adsm:lookup dataType="text" property="grupoJob" idProperty="idGrupoJob" criteriaProperty="nmGrupo"
					 service="lms.configuracoes.manterJobAction.findLookupGrupoJob" action="/configuracoes/manterGrupoJob"
					 label="grupoJob" size="40" maxLength="80"/>	
		--%>					  
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="job" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="job" idProperty="id" gridHeight="220" 
				service="lms.configuracoes.manterJobAction.findJobs"
			   showPagging="false" showTotalPageCount="false" scrollBars="vertical" onDataLoadCallBack="checkErrorReLoad">
		<adsm:gridColumn property="jobGroup" title="grupoExecucao" width="15%"/>
		<adsm:gridColumn property="name" title="processo" width="50%"/>
		<adsm:gridColumn property="description" title="descricao" width="15%"/>
		<adsm:gridColumn property="cronExpression" title="expressaoCron" width="15%"/>
		<adsm:buttonBar>
			<adsm:removeButton id="btnExcluir" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

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
	
	/**
	 * Callback da grid
	 *
	 * @param data
	 * @param error
	 */
	function checkErrorReLoad_cb(data, error) {
		if (error!=undefined) {
			alert(error);
			findButtonScript('job', document.getElementById("formJobList"));
		} 
	}

</script>