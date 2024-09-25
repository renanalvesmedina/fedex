<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	
	function gridRedecoLogRowClick(id){
	
	    var data = null;
	    for( var i = 0; i < gridLogGridDef.gridState.data.length; i++ ){
			if( gridLogGridDef.gridState.data[i].idGeral == id ) {
				data = gridLogGridDef.gridState.data[i];
			}
		}
		
		if( data == null ){
			return false;
		}
	
		gridLogGridDef.executeSearch({redeco:{idRedeco:data.redeco.idRedeco}});

		return false;
	}

</script>
<adsm:window service="lms.contasreceber.consultarRedecoLogAction">
	<adsm:form action="/contasreceber/consultarRedecoLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idRedecoLog"
			rows="6" 
			service="lms.contasreceber.consultarRedecoLogAction.findPaginated"
			rowCountService="lms.contasreceber.consultarRedecoLogAction.getRowCount"
			selectionMode="none"
			onRowClick="gridRedecoLogRowClick"
			width="10000"
			gridHeight="100"
			scrollBars="horizontal"
			unique="false"
  	>
		<adsm:gridColumn property="redeco.idRedeco" title="redeco.idRedeco" dataType="integer"/>
		<adsm:gridColumn property="filial.idFilial" title="filial.idFilial" dataType="integer"/>
		<adsm:gridColumn property="nrRedeco" title="nrRedeco" dataType="integer"/>
		<adsm:gridColumn property="vlDiferencaCambialCotacao" title="vlDiferencaCambialCotacao" dataType="currency"/>
		<adsm:gridColumn property="dtEmissao" title="dtEmissao" dataType="JTDate"/>
		<adsm:gridColumn property="tpSituacaoRedeco" title="tpSituacaoRedeco" isDomain="true"/>
		<adsm:gridColumn property="tpFinalidade" title="tpFinalidade" isDomain="true"/>
		<adsm:gridColumn property="nmResponsavelCobranca" title="nmResponsavelCobranca"/>
		<adsm:gridColumn property="tpRecebimento" title="tpRecebimento" isDomain="true"/>
		<adsm:gridColumn property="tpSituacaoWorkflow" title="tpSituacaoWorkflow" isDomain="true"/>
		<adsm:gridColumn property="empresaCobranca.idEmpresaCobranca" title="empresaCobranca.idEmpresaCobranca" dataType="integer"/>
		<adsm:gridColumn property="dtLiquidacao" title="dtLiquidacao" dataType="JTDate"/>
		<adsm:gridColumn property="dtRecebimento" title="dtRecebimento" dataType="JTDate"/>
		<adsm:gridColumn property="dhTransmissao" title="dhTransmissao" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="obRedeco" title="obRedeco"/>
		<adsm:gridColumn property="tpAbrangencia" title="tpAbrangencia" isDomain="true"/>
		<adsm:gridColumn property="nrVersao" title="nrVersao" dataType="integer"/>
		<adsm:gridColumn property="pendenciaDesconto.idPendencia" title="pendenciaDesconto.idPendencia" dataType="integer"/>
		<adsm:gridColumn property="pendenciaLucrosPerdas.idPendencia" title="pendenciaLucrosPerdas.idPendencia" dataType="integer"/>
		<adsm:gridColumn property="pendenciaRecebimento.idPendencia" title="pendenciaRecebimento.idPendencia" dataType="integer"/>
		<adsm:gridColumn property="moeda.idMoeda" title="moeda.idMoeda" dataType="integer"/>
		<adsm:gridColumn property="dhTransmissaoTzr" title="dhTransmissaoTzr"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	<adsm:grid 
			property="gridItemLog" 
			idProperty="idItemRedecoLog"
			service="lms.contasreceber.consultarItemRedecoLogAction.findPaginated"
			rowCountService="lms.contasreceber.consultarItemRedecoLogAction.getRowCount"	
			rows="3" 
			selectionMode="none"
			scrollBars="horizontal"
			autoSearch="false"
			gridHeight="60"
			width="3000"
			unique="false"
  	>
		<adsm:gridColumn property="itemRedeco.idItemRedeco" title="itemRedeco.idItemRedeco" dataType="integer"/>
		<adsm:gridColumn property="redeco.idRedeco" title="redeco.idRedeco" dataType="integer"/>
		<adsm:gridColumn property="fatura.idFatura" title="fatura.idFatura" dataType="integer"/>
		<adsm:gridColumn property="vlTarifa" title="vlTarifa" dataType="currency"/>
		<adsm:gridColumn property="vlJuros" title="vlJuros" dataType="currency"/>
		<adsm:gridColumn property="recibo.idRecibo" title="recibo.idRecibo" dataType="integer"/>
		<adsm:gridColumn property="obItemRedeco" title="obItemRedeco"/>
		<adsm:gridColumn property="nrVersao" title="nrVersao" dataType="integer"/>
		<adsm:gridColumn property="vlDiferencaCambialCotacao" title="vlDiferencaCambialCotacao" dataType="currency"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>

	<adsm:buttonBar/>
</adsm:window>