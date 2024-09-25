<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.workflow.acaoService" onPageLoadCallBack="historico">
	<adsm:form action="/workflow/listarHistoricoPendencia">
		<adsm:hidden property="pendencia.idPendencia"/>
	</adsm:form>
	
	<adsm:section caption="consultarHistoricoAprocacao"/>
	
	<adsm:grid  selectionMode="none" idProperty="idAcao" property="pendencias" gridHeight="200" unique="true" onRowClick="rowClick" 
		        service="lms.workflow.listarHistoricoPendenciaAction.findPaginatedPendencia" 
		        rowCountService="lms.workflow.listarHistoricoPendenciaAction.getRowCountPendencia">
		<adsm:gridColumn title="aprovador" property="integrante" width="20%" />
		<adsm:gridColumn title="dataLiberacao" property="dhLiberacao" width="17%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="dataAcao" property="dhAcao" width="17%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="situacao" property="tpSituacaoAcao" width="10%" isDomain="true"/>
		<adsm:gridColumn title="observacao" property="obAcao" width="36%" />
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" onclick="javascript:window.close();" disabled="false"/>
	</adsm:buttonBar>
</adsm:window>
<script>
	function rowClick(){
		return false;
	}
	

	function historico_cb(){
		onPageLoad_cb();
		findPendencias();
	}

	/**
	 * Chamar no onDataLoadCallBack, para listar
	 */
	function findPendencias(){
		findButtonScript('pendencias', document.forms[0]);
	}
</script>
