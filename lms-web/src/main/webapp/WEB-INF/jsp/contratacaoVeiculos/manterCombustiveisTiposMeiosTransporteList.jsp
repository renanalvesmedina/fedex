<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="manterCombustiveisTiposMeiosTransporte" service="lms.contratacaoveiculos.tpCombustTpMeioTranspService">
	<adsm:form action="/contratacaoVeiculos/manterCombustiveisTiposMeiosTransporte" idProperty="idTpCombustTpMeioTransp">
		
		<adsm:hidden property="tipoMeioTransporte.idTipoMeioTransporte"/>
		<adsm:textbox dataType="text" property="dsMeioTransporte" label="modalidade" maxLength="50" labelWidth="20%" size="25" width="30%" disabled="true" serializable="false"/>
		
		<adsm:textbox dataType="text" property="tipoMeioTransporte.dsTipoMeioTransporte" label="tipoMeioTransporte" maxLength="50" labelWidth="20%" size="25" width="30%" disabled="true" serializable="false"/>
		
		<adsm:combobox property="tipoCombustivel.idTipoCombustivel" label="tipoCombustivel" service="lms.fretecarreteiroviagem.tipoCombustivelService.find" optionLabelProperty="dsTipoCombustivel" optionProperty="idTipoCombustivel" labelWidth="20%" width="30%" boxWidth="200">
		</adsm:combobox>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="20%" width="30%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tpCombustTpMeioTransp"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="tpCombustTpMeioTransp" idProperty="idTpCombustTpMeioTransp" unique="true" defaultOrder="tipoCombustivel_.dsTipoCombustivel" rows="13">
		<adsm:gridColumn width="45%" title="tipoCombustivel" property="tipoCombustivel.dsTipoCombustivel" />
		<adsm:gridColumn width="45%" title="consumo" property="qtConsumo" dataType="currency" mask="#,##0.00"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid> 
</adsm:window>
