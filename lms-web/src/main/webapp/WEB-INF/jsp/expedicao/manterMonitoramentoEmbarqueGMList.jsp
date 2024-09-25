<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myOnPageLoad() {
		onPageLoad();
		//var sdo = createServiceDataObject("lms.gm.monitoramentoEmbarqueService.findPageLoad",null,null);
		//xmit({serviceDataObjects:[sdo]});
		
		findButtonScript("carregamento",document.forms[0]);
		setTimeout("findButtonScript('carregamento',document.forms[0]);","60000");
		setInterval("findButtonScript('carregamento',document.forms[0]);","60000");
		
	}

	//function loadPageMonitoramento_cb(data, exception){
		//setElementValue("dataDisponivel", setFormat("dataDisponivel", data.dataDisponibilizada));
	//} 
</script>
<adsm:window service="lms.gm.monitoramentoEmbarqueGMService" onPageLoad="myOnPageLoad">
	<adsm:form action="/expedicao/manterMonitoramentoEmbarqueGM">
		<adsm:textbox
			property="rotaCarregamento"
			dataType="text"
			label="rota"
			size="5"
			serializable="true"
			maxLength="5"
			labelWidth="10%"
			width="40%"
		/>
			
        <adsm:hidden property="idMeioTransporte" serializable="true"  />
        <adsm:lookup label="meioTransporte" labelWidth="10%" width="5%" maxLength="6" size="6"
			dataType="text"
		             property="meioTransporte" 
			idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
					 service="lms.gm.monitoramentoEmbarqueGMService.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 serializable="false">
			<adsm:propertyMapping relatedProperty="idMeioTransporte" modelProperty="nrFrota" />
			<adsm:propertyMapping relatedProperty="placa" modelProperty="nrIdentificador" />
            <adsm:textbox
				property="placa"
            	dataType="text"
				disabled="true" 
				size="15"
            	serializable="false"
				labelWidth="10%"
				width="20%"
			/>
        </adsm:lookup>

		<adsm:textbox
			property="mapaCarregamento"
			dataType="integer"
			label="mapaCarregamento"
			size="11"
			serializable="true"
			maxLength="10"
			labelWidth="10%"
			width="35%"
		/>
			
		<adsm:textbox 
			property="dataDisponivel"
			dataType="JTDate" 
			label="dataDisponibilizada" 
			labelWidth="15%" 
			width="20%"
		/>	
			
		<adsm:combobox 
			onlyActiveValues="true"
			label="status" 
			property="statusCarregamento" 
			domain="DM_STATUS_CARREGAMENTO" 
			labelWidth="10%" 
			width="38%" 
		/>	
		
		
		<adsm:textbox
			property="codigoVolume"
			dataType="text"
			label="codigoVolume"
			size="10"
			serializable="true"
			maxLength="10"
			labelWidth="12%"
			width="20%"
			
			
		/>
		<adsm:hidden property="matriculaChefia" serializable="true"  />
		<adsm:lookup label="usuario"
			         labelWidth="10%"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
			dataType="text"
					 size="15" 
					 maxLength="15"	
					 width="15%"		 					 
					 service="lms.gm.monitoramentoEmbarqueGMService.findLookupUsuarioFuncionarioMonitoramento"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping relatedProperty="matriculaChefia" modelProperty="idUsuario" />
			<adsm:textbox 
				dataType="text" 
				property="usuario.nmUsuario" 
				disabled="true" 
				serializable="false" 
				maxLength="60" 
				size="40" 
				width="35%"/>
		</adsm:lookup>

		<adsm:buttonBar
			freeLayout="true">
			<adsm:findButton callbackProperty="carregamento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
		<script>
			var lms_04310 = 'LMS-04310 - '+'<adsm:label key="LMS-04310"/>';
		</script>
	</adsm:form>
	
	<adsm:grid
		idProperty="idCarregamento"
		property="carregamento"
		scrollBars="horizontal" 
		gridHeight="190"
		defaultOrder="dataDisponivel"
		unique="false"
		disableMarkAll="true"
		onDataLoadCallBack="gridCarregamentoCallback"
		selectionMode="radio"
		onRowClick="rowClick"
		rows="8">
		
		<adsm:editColumn field="hidden" property="placaVeiculo" title=""/>
		
		<adsm:gridColumn
			title="veiculo"
			property="frotaVeiculo"
			width="45" />

		<adsm:gridColumn
			title="totalVolumes"
			property="totalVolume"
			width="50"
			align="right" />

		<adsm:gridColumn
			title="totalPeso"
			property="totalPeso"
			width="40"
			align="right" />	
		
		<adsm:gridColumnGroup customSeparator=" ">
		<adsm:gridColumn
				title="destino"
				property="rotaCarregamento"
				width="50" />
				
			<adsm:gridColumn
				title=""
				property="tipoCarregamento"
				width="30" />	
		</adsm:gridColumnGroup>		

		<adsm:gridColumn
			title="status"
			isDomain="true"
			property="codigoStatus"
			width="60" />
			
		<adsm:gridColumn
			title="dataInicio"
			property="dtInicio"
			dataType="JTDateTimeZone"
			mask="dd/MM/yyyy HH:mm"
			width="90" />
		
		<adsm:gridColumn
			title="dataFim"
			property="dtFim"
			dataType="JTDateTimeZone"
			mask="dd/MM/yyyy HH:mm"
			width="90" />	
		
		<adsm:gridColumn
			title="volumes"
			property="qtdVolumes"
			width="50"
			align="right" />	
			
		<adsm:gridColumn
			title="responsavel"
			property="responsavel"
			width="80" />			
			
		<adsm:gridColumn
			title="horarioCorte"
			property="horarioCorte"
			dataType="JTTime"
			width="60" />
	</adsm:grid>
</adsm:window>
<script>		
	function gridCarregamentoCallback_cb(data, error){
		if (error != undefined){
			alert(error);
		}

		
	}

	function initWindow(eventObj) {
		setDisabled("imprimirCarregamento", false);
		
	    var tabGroup = getTabGroup(this.document);
		tabGroup.getTab("cad").setDisabled(true);
	}

	/**
	* Function executada ao se clicar em um registro na grid 
	* LMS-2781
	*/
	function rowClick(id) {
		var data = new Object();
		
		var dataSelectedRow = carregamentoGridDef.findById(id);
		//Se o status do carregamento iguale "Aberto" ou "Cancelado" 
		if(dataSelectedRow.codigoStatus.value == "1" || dataSelectedRow.codigoStatus.value == "5") {
			//Apresentar a mensagem LMS-04310
			alert(lms_04310);
			return false;
		}
		data.idCarregamento = dataSelectedRow.idCarregamento;
		data.destino = dataSelectedRow.rotaCarregamento + " " + dataSelectedRow.tipoCarregamento;
		data.codigoStatus = dataSelectedRow.codigoStatus;
		data.dtInicio = dataSelectedRow.dtInicio;
		data.dtFim = dataSelectedRow.dtFim;
		data.horarioCorte = dataSelectedRow.horarioCorte;
		data.frotaVeiculo = dataSelectedRow.frotaVeiculo;
		data.placaVeiculo = dataSelectedRow.placaVeiculo;
		
		//Habilita a tab de detalhamento
		var tabGroup = getTabGroup(this.document);
		tabGroup.getTab("cad").setDisabled(false);
		
		carregamentoGridDef.detailGridRow("onDetailDataLoad", data);

		
		return false;
	}

</script>	