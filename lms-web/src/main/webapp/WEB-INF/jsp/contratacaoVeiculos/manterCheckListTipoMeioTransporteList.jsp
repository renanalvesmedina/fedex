<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterCheckListTipoMeioTransporte" service="lms.contratacaoveiculos.manterCheckListTipoMeioTransporteAction">
	<adsm:form action="/contratacaoVeiculos/manterCheckListTipoMeioTransporte">
		<adsm:combobox property="tipoMeioTransporte.tpMeioTransporte"   label="modalidade" labelWidth="20%" width="30%" domain="DM_TIPO_MEIO_TRANSPORTE" boxWidth="190"/>
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" label="tipoMeioTransporte" service="lms.contratacaoveiculos.manterCheckListTipoMeioTransporteAction.findLookupTipoMeioTransporte" boxWidth="190"
						optionLabelProperty="dsTipoMeioTransporte" optionProperty="idTipoMeioTransporte" labelWidth="20%" width="30%">
			<adsm:propertyMapping modelProperty="tpMeioTransporte" criteriaProperty="tipoMeioTransporte.tpMeioTransporte"/>
		</adsm:combobox>
		<adsm:combobox property="itemCheckList.idItemCheckList" label="itemCheckList" service="lms.contratacaoveiculos.manterCheckListTipoMeioTransporteAction.findComboitemCheckList" boxWidth="190"
						optionLabelProperty="dsItemCheckList" optionProperty="idItemCheckList" labelWidth="20%" width="30%">
			<adsm:propertyMapping modelProperty="tpMeioTransporte" criteriaProperty="tipoMeioTransporte.tpMeioTransporte"/>
		</adsm:combobox>
		
		<adsm:combobox property="tpItChecklistTpMeioTransp" label="tipoItem" domain="DM_TIPO_ITEM_CHECK_LIST_VEICULO" labelWidth="20%" width="30%" boxWidth="190"/>
		<adsm:combobox property="blObrigatorioAprovacao" label="obrigatorioAprovacao" domain="DM_SIM_NAO"  labelWidth="20%" width="30%"/>

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ItChecklistTpMeioTransp"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idItChecklistTpMeioTransp" property="ItChecklistTpMeioTransp" unique="true" 
		       scrollBars="horizontal" rows="10" gridHeight="200" 
		       defaultOrder="tipoMeioTransporte_.tpMeioTransporte,tipoMeioTransporte_.dsTipoMeioTransporte,tpItChecklistTpMeioTransp,itemCheckList_.dsItemCheckList">
		<adsm:gridColumn width="150" title="modalidade" property="tipoMeioTransporte.tpMeioTransporte" isDomain="true"/>
		<adsm:gridColumn width="150" title="tipoMeioTransporte" property="tipoMeioTransporte.dsTipoMeioTransporte"/>
		<adsm:gridColumn width="180" title="itemCheckList" property="itemCheckList.dsItemCheckList" />
		<adsm:gridColumn width="180" title="tipoItem" property="tpItChecklistTpMeioTransp" isDomain="true"/>
		<adsm:gridColumn width="180" title="obrigatorioAprovacao" property="blObrigatorioAprovacao" renderMode="image-check" />
		<adsm:gridColumn width="100" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn width="100" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
