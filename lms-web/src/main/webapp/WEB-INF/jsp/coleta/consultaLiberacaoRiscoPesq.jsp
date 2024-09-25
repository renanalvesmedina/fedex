<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.consultaLiberacaoRiscoAction">
	<adsm:form action="/coleta/consultaLiberacaoRisco">
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false" />
		
		<adsm:range label="periodo" width="85%" required="true">
			<adsm:textbox dataType="JTDate" property="dataInicial" picker="true" required="true" />
			<adsm:textbox dataType="JTDate" property="dataFinal" picker="true" required="true" />
		</adsm:range>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportado2" 
				     idProperty="idMeioTransporte"
					 criteriaProperty="nrFrota"
					 service="lms.coleta.consultaLiberacaoRiscoAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 picker="false" label="meioTransporte" size="6" maxLength="6" labelWidth="15%" width="7%" serializable="false" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrIdentificador" criteriaProperty="meioTransporteByIdTransportado.nrIdentificador" disable="false" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />		
			<adsm:propertyMapping modelProperty="nrIdentificador" relatedProperty="meioTransporteByIdTransportado.nrIdentificador" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteByIdTransportado" 
					 idProperty="idMeioTransporte"
					 criteriaProperty="nrIdentificador"
					 service="lms.coleta.consultaLiberacaoRiscoAction.findLookupMeioTransporteTransportado" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 picker="true" size="24" maxLength="25" width="78%" >
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao" />
			<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteByIdTransportado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado2.idMeioTransporte"	/>	
			<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteByIdTransportado2.nrFrota" />
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteByIdTransportado.idMeioTransporte" />		
		</adsm:lookup>		
					 
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="frotas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="frotas" idProperty="idEventoControleCarga" gridHeight="240" onRowClick="disableRowClick" rows="12"
			   selectionMode="none" scrollBars="horizontal" unique="true" defaultOrder="dhEvento">
		<adsm:gridColumn title="meioTransporte" property="meioTransporteByIdTransportado.nrFrota" width="60" />
		<adsm:gridColumn title="" property="meioTransporteByIdTransportado.nrIdentificador" width="110" />
		<adsm:gridColumn property="dhEvento" title="dataHoraLiberacao" width="150" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="siglaSimbolo" title="valor" width="60"/>
		<adsm:gridColumn property="vlLiberacao" title="" width="80" dataType="currency"/>
		<adsm:gridColumn property="usuario.nmUsuario" title="liberadoPor" width="170" />
		<adsm:gridColumn property="dsEvento" title="observacao" width="250" />
		<adsm:gridColumn property="equipe" title="equipe" width="80" image="/images/popup.gif" openPopup="true" link="coleta/consultaLiberacaoRiscoDadosEquipe.do?cmd=main" align="center" linkIdProperty="idEventoControleCarga"/>
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
/**
 * Função para não chamar nada no onclick da row da grid
 */
function disableRowClick() {
	return false;
}
</script>