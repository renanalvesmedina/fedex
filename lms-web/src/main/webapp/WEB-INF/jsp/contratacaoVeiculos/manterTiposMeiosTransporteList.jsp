<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterTiposMeiosTransporte" service="lms.contratacaoveiculos.tipoMeioTransporteService">
	<adsm:form action="/contratacaoVeiculos/manterTiposMeiosTransporte" idProperty="idTipoMeioTransporte">
		<adsm:combobox boxWidth="150" property="tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE" label="modalidade" labelWidth="23%" width="27%" cellStyle="vertical-align:bottom"/>
		
		<adsm:textbox dataType="text" property="dsTipoMeioTransporte" label="tipoMeioTransporte" maxLength="60" labelWidth="23%" size="35" width="27%" cellStyle="vertical-align:bottom"/>
		
		<adsm:combobox boxWidth="150" property="tpCategoria" domain="DM_CATEGORIA_VEICULO"  label="categoria" labelWidth="23%" width="27%"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="23%" width="77%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoMeioTransporte"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
	 
	<adsm:grid property="tipoMeioTransporte" idProperty="idTipoMeioTransporte" 
			   defaultOrder="tpMeioTransporte,tpCategoria,dsTipoMeioTransporte" scrollBars="horizontal" 
			   gridHeight="220" unique="true" rows="11">
		<adsm:gridColumn width="130" title="modalidade" property="tpMeioTransporte" isDomain="true" />
		<adsm:gridColumn width="180" title="tipoMeioTransporte" property="dsTipoMeioTransporte" />
		<adsm:gridColumn width="130" title="categoria" property="tpCategoria" isDomain="true"/>
		<adsm:gridColumn width="130" title="compostoPor" property="tipoMeioTransporte.dsTipoMeioTransporte" />
		<adsm:gridColumn width="180" title="capacidadePesoInicial" unit="kg" mask="###,###" property="nrCapacidadePesoInicial" dataType="decimal"/>
		<adsm:gridColumn width="180" title="capacidadePesoFinal" unit="kg" mask="###,###" property="nrCapacidadePesoFinal" dataType="decimal"/>
		<adsm:gridColumn width="80" title="situacao" property="tpSituacao" isDomain="true"/>

		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid> 
</adsm:window>
