<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAtributosModelosTiposMeioTransporte" service="lms.contratacaoveiculos.modeloMeioTransporteService">
	<adsm:form action="/contratacaoVeiculos/manterModelosMeiosTransporte" idProperty="idModeloMeioTransporte">
		
		<adsm:combobox property="tipoMeioTransporte.tpMeioTransporte" label="modalidade" domain="DM_TIPO_MEIO_TRANSPORTE" labelWidth="23%" width="27%" boxWidth="150"/>
			
		
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" label="tipoMeioTransporte" service="lms.contratacaoveiculos.modeloMeioTransporteService.findTipoMeioTranspByMeio" onlyActiveValues="false" optionLabelProperty="dsTipoMeioTransporte" optionProperty="idTipoMeioTransporte" labelWidth="23%" width="27%" boxWidth="150">
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.tpMeioTransporte" modelProperty="tpMeioTransporte" />
		</adsm:combobox>
		
		<adsm:combobox property="marcaMeioTransporte.idMarcaMeioTransporte" label="marcaMeioTransporte" service="lms.contratacaoveiculos.modeloMeioTransporteService.findMarcaMeioTranspByMeio" optionLabelProperty="dsMarcaMeioTransporte" optionProperty="idMarcaMeioTransporte" labelWidth="23%" width="77%" boxWidth="150">
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.tpMeioTransporte" modelProperty="tpMeioTransporte"/>
		</adsm:combobox>
		
		<adsm:textbox dataType="text" property="dsModeloMeioTransporte" label="descricaoModelo" maxLength="60" size="50" labelWidth="23%" width="77%"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="23%" width="27%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="modeloMeioTransporte"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="modeloMeioTransporte" idProperty="idModeloMeioTransporte" unique="true" defaultOrder="tipoMeioTransporte_.tpMeioTransporte,tipoMeioTransporte_.dsTipoMeioTransporte,marcaMeioTransporte_.dsMarcaMeioTransporte,dsModeloMeioTransporte">
		<adsm:gridColumn width="25%" title="modalidade" property="tipoMeioTransporte.tpMeioTransporte" isDomain="true" />
		<adsm:gridColumn width="25%" title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte" />
		<adsm:gridColumn width="20%" title="marcaMeioTransporte" property="marcaMeioTransporte.dsMarcaMeioTransporte" />
		<adsm:gridColumn width="20%" title="descricaoModelo" property="dsModeloMeioTransporte"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
