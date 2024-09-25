<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterMonitoramentoEmbarqueGMAction">

	<adsm:form action="/expedicao/manterMonitoramentoEmbarqueGM" idProperty="idCarregamento" >
		
		<adsm:hidden property="idCarregamento" />
		
		<adsm:textbox dataType="text" property="frotaVeiculo" 
					  label="meioTransporte" labelWidth="10%" width="32%" size="6" serializable="false" disabled="true" >
			<adsm:textbox dataType="text" property="placaVeiculo" size="9" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:textbox label="horarioCorte" property="horarioCorte" dataType="JTTime" labelWidth="12%" disabled="true" size="9" width="20%"/>
		
		<adsm:textbox label="destino" property="destino" dataType="text" labelWidth="10%" width="29%" disabled="true"/>
		
		<adsm:textbox label="status" property="codigoStatus" dataType="text" labelWidth="10%" width="90%" disabled="true"/>
		
		<adsm:textbox label="dataInicio" property="dtInicio" dataType="JTDateTimeZone" width="90%" labelWidth="10%" mask="dd/MM/yyyy HH:mm" picker="false" disabled="true"/>		
		
		<adsm:textbox label="dataFim" property="dtFim" dataType="JTDateTimeZone" width="15%" labelWidth="10%" mask="dd/MM/yyyy HH:mm" picker="false" disabled="true"/>
		
	</adsm:form>
	<adsm:grid
		idProperty="idCarregamento"
		property="carregamento"
		scrollBars="horizontal" 
		gridHeight="190"
		defaultOrder="dataDisponivel"
		selectionMode="none"
		disableMarkAll="true"
		onDataLoadCallBack="gridCarregamentoCallback"
		service="lms.expedicao.manterMonitoramentoEmbarqueGMAction.findPaginatedMpc"
		rowCountService="lms.expedicao.manterMonitoramentoEmbarqueGMAction.getRowCountMpc"
		onRowClick="rowClick"
		autoSearch="false"
		rows="10">
		
		<adsm:gridColumn
			title="mapaCarregamento"
			property="mapaCarregamento"
			align="right"
			width="10%" />
			
		<adsm:gridColumn
			title="dataCriacao"
			property="dataCriacao"
			dataType="JTDateTimeZone"
			mask="dd/MM/yyyy HH:mm"
			align="center"
			width="20%" />
		
		<adsm:gridColumn
			title="dataEfetivacao"
			property="dataDisponivel"
			dataType="JTDateTimeZone"
			mask="dd/MM/yyyy HH:mm"
			align="center"
			width="20%" />

		<adsm:gridColumn
			title="totalVolumes"
			property="totalVolume"
			align="right"
			width="15%" />

		<adsm:gridColumn
			title="totalPeso"
			property="totalPeso"
			align="right"
			width="15%" />
		
		<adsm:gridColumn
			title="totalVolumesCarregados"
			property="qtdVolumes"
			align="right"
			width="20%" />
	</adsm:grid>
	
		

</adsm:window>

<script>		
	function gridCarregamentoCallback_cb(data, error){
		if (error != undefined){
			alert(error);
		}
	}
	
	/**
	 * Substitui a funcao padrao de 'onDataLoad'. 
	 * LMS-2781
	 *
	 * @param idDoctoServico
	 * @param idManifesto
	 */
	function onDetailDataLoad(idCarregamento, destino, codigoStatus, dtInicio, dtFim, horarioCorte, frotaVeiculo, placaVeiculo) {
		
		setElementValue("idCarregamento", idCarregamento);
		setElementValue("destino", destino);
		setElementValue("codigoStatus", codigoStatus.description);
		setElementValue("dtInicio", setFormat(document.getElementById("dtInicio"), dtInicio));
		
		setElementValue("dtFim", setFormat(document.getElementById("dtFim"), dtFim));
		setElementValue("horarioCorte", horarioCorte);
		
		setElementValue("frotaVeiculo", frotaVeiculo);
		setElementValue("placaVeiculo", placaVeiculo);
		
		var formData = new Object();
		setNestedBeanPropertyValue(formData, "idCarregamento", idCarregamento);
		
		carregamentoGridDef.executeSearch(formData, true);
	}
	
	/**
	 * soberscrito o rowclick para não disparar nenhuma ação
	 *
	 * @param data
	 * @param error
	 */
	function rowClick(id) {
		return false;
	}
</script>