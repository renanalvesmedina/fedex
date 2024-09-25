<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarRotasViagemAction" >
	<adsm:form action="/municipios/consultarRotas" idProperty="idRotaIdaVolta" height="390" onDataLoadCallBack="rotaLoad"
			service="lms.municipios.consultarRotasViagemAction.findByIdRotaIda" >
		<adsm:hidden property="idRotaViagem" />
		<adsm:hidden property="hiddenTpSistemaRota" />
		<adsm:textbox dataType="text" label="tipoRota" property="dsTpRota" disabled="true" size="18" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" label="sistemaRota" property="tpSistemaRota" disabled="true" size="18" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" label="tipoMeioTransporte" property="dsTipoMeioTransporte" disabled="true" size="18" labelWidth="18%" width="75%" />
		
		<adsm:textbox dataType="integer" label="rotaIda" property="nrRota" disabled="true" mask="0000" size="5" labelWidth="18%" width="32%">
			<adsm:textbox dataType="text" property="dsRota" disabled="true" size="30" />
		</adsm:textbox>
		<adsm:textbox dataType="text" label="moeda" property="dsMoeda" disabled="true" size="18" labelWidth="18%" width="32%" />

		<adsm:textbox dataType="currency" label="premioCumprimento" property="vlPremio" disabled="true" size="18"
				labelWidth="18%" width="32%" />
		
		<adsm:textbox dataType="currency" property="vlPedagio" label="valorPostoPassagem" serializable="false"
				labelWidth="18%" width="32%" size="18" cellStyle="vertical-align:bottom;" disabled="true"
				mask="#,###,###,###,###,##0.00" />
				
		<adsm:textbox dataType="currency" label="freteKm" property="vlFreteKm" disabled="true" size="18"
				labelWidth="18%" width="32%" mask="#,###,###,###,###,##0.000"/>
		<adsm:textbox dataType="integer" label="distancia" property="nrDistancia" disabled="true"
				size="18" mask="###,###" unit="km2" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="currency" label="freteCarreteiro" property="vlFreteCarreteiro"
				size="18" labelWidth="18%" width="32%" disabled="true"/>
				
		<adsm:textbox dataType="text" property="nrTempoViagem" label="tempoViagem" serializable="false"
				labelWidth="18%" width="32%" size="18" cellStyle="vertical-align:bottom;" unit="h" disabled="true"/>
				
		<adsm:textarea property="obItinerario" label="itinerario" disabled="true"
				rows="2" columns="75" labelWidth="18%" width="82%" maxLength="1000" />
		<adsm:textarea property="obRotaIdaVolta" label="observacao" disabled="true"
				rows="2" columns="75" labelWidth="18%" width="82%" maxLength="1000" />
			
					
		
			
    	<adsm:section caption="trechosRota" />    	
		<adsm:grid property="trechoRotaIdaVolta" idProperty="idTrechoRotaIdaVolta" showPagging="false" scrollBars="vertical"
				service="lms.municipios.consultarRotasViagemAction.findTrechosRota" autoSearch="false" onPopulateRow="populateRowTrecho"
				gridHeight="100" selectionMode="none" unique="false" rows="5" onRowClick="onRowClickTrecho" >
				
			<adsm:gridColumnGroup separatorType="FILIAL" >
				<adsm:gridColumn title="filialOrigem" property="sgFilialOrigem" width="60" />
				<adsm:gridColumn title="" property="nmFilialOrigem" width="60" />
			</adsm:gridColumnGroup> 
			<adsm:gridColumnGroup separatorType="FILIAL" >
				<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="60" /> 
				<adsm:gridColumn title="" property="nmFilialDestino" width="60" />
			</adsm:gridColumnGroup>
			
			<adsm:gridColumn title="horarioSaida" property="hrSaida" dataType="JTTime" />		
			<adsm:gridColumn title="distancia" property="nrDistancia" dataType="decimal" mask="###,###,###" unit="km2" width="60" />
			<adsm:gridColumn title="tempoViagem" property="nrTempoViagem" dataType="text" unit="h" width="70" align="center"/>
			<adsm:gridColumn title="tempoOperacao" property="nrTempoOperacao" dataType="text" unit="h" width="80" align="center"/>
			<adsm:gridColumn title="dom" property="blDomingo" width="30" renderMode="image-check" />
			<adsm:gridColumn title="seg" property="blSegunda" width="30" renderMode="image-check" />
			<adsm:gridColumn title="ter" property="blTerca" width="30" renderMode="image-check" />
			<adsm:gridColumn title="qua" property="blQuarta" width="30" renderMode="image-check" />
			<adsm:gridColumn title="qui" property="blQuinta" width="30" renderMode="image-check" />
			<adsm:gridColumn title="sex" property="blSexta" width="30" renderMode="image-check" />
			<adsm:gridColumn title="sab" property="blSabado" width="30" renderMode="image-check" />
		</adsm:grid>
		
    	<adsm:section caption="pontosParadaTrecho" />
    	<adsm:grid property="pontoParadaTrecho" idProperty="idPontoParadaTrecho" showPagging="false"
    			service="lms.municipios.consultarRotasViagemAction.findPontosParadaTrecho" autoSearch="false" onPopulateRow="populateRowPonto"
    			gridHeight="100" selectionMode="none" unique="false" rows="5" scrollBars="vertical" onRowClick="onRowClickParada" >
    		<adsm:gridColumn title="ordem" property="nrOrdem" dataType="integer" width="45"/>
			<adsm:gridColumn title="local" property="dsLocalidade" />
			<adsm:gridColumn title="municipio" property="nmMunicipio" width="100" />
			<adsm:gridColumn title="uf" property="sgUnidadeFederativa" width="20" />
			<adsm:gridColumn title="pais" property="nmPais" width="100" />
			<adsm:gridColumnGroup customSeparator=" - " >
				<adsm:gridColumn title="rodovia" property="rodovia_sgRodovia" width="50" />
				<adsm:gridColumn title="" property="rodovia_dsRodovia" width="50" />
			</adsm:gridColumnGroup>
			<adsm:gridColumn title="km" property="pontoParada_nrKm" dataType="decimal" mask="###,###,###" width="40" />
			<adsm:gridColumn title="tempoParada" property="nrTempoParada" align="center" unit="h" width="70" />
			<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="60" />
			<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="60" />
		</adsm:grid>
		
		<adsm:section caption="motivosParadaTrecho" />
		<adsm:grid property="motivoParadaPontoTrecho" idProperty="idMotivoParadaPontoTrecho" showPagging="false" scrollBars="vertical"
				service="lms.municipios.consultarRotasViagemAction.findMotivosPontosParadaTrecho" autoSearch="false"
    			gridHeight="100" selectionMode="none" unique="false" rows="5" onRowClick="onRowClickMotivos" >
			<adsm:gridColumn title="motivo" property="dsMotivo" width="60%" />
			<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="20%" />
			<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="20%" />
		</adsm:grid>
		
		<adsm:buttonBar/>
	</adsm:form>	
		
</adsm:window>
<script>
	// Flags que evitam reconsulta desnecessárias.
	var idTrechoOld = 0;
	var idParadaOld = 0;
	var detalhando = -1;
	
	function rotaLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		
		var tpRota = getNestedBeanPropertyValue(data,"tpRota");
		var tpSistemaRota = getNestedBeanPropertyValue(data,"hiddenTpSistemaRota");
		var tabGroup = getTabGroup(this.document);
		
		// Habilita e desabilita algumas abas.
		tabGroup.setDisabledTab("rtIda", false );
		if (tpSistemaRota != "S")
			tabGroup.setDisabledTab("rtVolta", false );
		tabGroup.setDisabledTab("expr", false );
		tabGroup.setDisabledTab("mot", false );
		
		idTrechoOld = 0;
		
		var idRotaIdaVolta = getNestedBeanPropertyValue(data,"idRotaIdaVolta");
		
		// Carrega o grid de Trechos da rota Ida Volta
		detalhando = 0;
		carregaTrechoRotaIdaVoltaGrid(idRotaIdaVolta);
		
		limpaGridApresentaMsg("pontoParadaTrecho");
		limpaGridApresentaMsg("motivoParadaPontoTrecho");
	}
	
	function carregaTrechoRotaIdaVoltaGrid(idRotaIdaVolta) {
		if (idRotaIdaVolta != undefined && idRotaIdaVolta != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "idRotaIdaVolta", idRotaIdaVolta);
			trechoRotaIdaVoltaGridDef.executeSearch(data);
		} else
			limpaGridApresentaMsg("trechoRotaIdaVolta");
	}
	
	function carregaPontoParadaTrechoGrid(idTrechoRotaIdaVolta) {
		if (idTrechoRotaIdaVolta != undefined && idTrechoRotaIdaVolta != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "idTrechoRotaIdaVolta", idTrechoRotaIdaVolta);
			pontoParadaTrechoGridDef.executeSearch(data);
		} else
			limpaGridApresentaMsg("pontoParadaTrecho");
	}
	
	function carregaMotivoParadaPontoTrechoGrid(idPontoParadaTrecho) {
		if (idPontoParadaTrecho != undefined && idPontoParadaTrecho != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "idPontoParadaTrecho", idPontoParadaTrecho);
			motivoParadaPontoTrechoGridDef.executeSearch(data);
		} else
			limpaGridApresentaMsg("motivoParadaPontoTrecho");
	}
	
	function onRowClickTrecho(id) {
		if (id != idTrechoOld) {
			idTrechoOld = id;
			idParadaOld = 0;
			switchForeColor("trechoRotaIdaVolta",id);
			carregaPontoParadaTrechoGrid(id);
			limpaGridApresentaMsg("motivoParadaPontoTrecho");
		}	
		return false;
	}
		
	function onRowClickParada(id) {
		if (id != idParadaOld) {
			idParadaOld = id;
			switchForeColor("pontoParadaTrecho",id);
			carregaMotivoParadaPontoTrechoGrid(id);
		}
		return false;
	}
	
	function onRowClickMotivos() {
		return false;
	}
	
	function switchForeColor(gridProperty,rowId) {
		var griddef = document.getElementById(gridProperty + ".dataTable");
		var i = 0;
		for (i = 0; i < griddef.rows.length; i++) {
			if (griddef.rows[i].pkValue == rowId)
				griddef.rows[i].style.color = "#0055FF";
			else
				griddef.rows[i].style.color = "";
		}
	}
	
	function populateRowTrecho(row,data) {
		if (row.rowIndex == 0) {
			var id = getNestedBeanPropertyValue(data,"idTrechoRotaIdaVolta");
			carregaPontoParadaTrechoGrid(id);
			row.style.color = "#0055FF";
		}
		return true;
	}
	
	function populateRowPonto(row,data) {
		if (row.rowIndex == 0) {
			var id = getNestedBeanPropertyValue(data,"idPontoParadaTrecho");
			carregaMotivoParadaPontoTrechoGrid(id);
			row.style.color = "#0055FF";
		}
		return true;
	}
	
	function limpaGridApresentaMsg(gridProperty) {
		eval(gridProperty + "GridDef.resetGrid();");
		var msgSpan = document.getElementById(gridProperty+".noResultMessage");
		msgSpan.style.display = 'inline';
	}
		
</script>