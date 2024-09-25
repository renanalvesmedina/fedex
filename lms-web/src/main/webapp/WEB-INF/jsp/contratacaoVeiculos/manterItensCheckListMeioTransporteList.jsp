<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterItensCheckListMeioTransporte" service="lms.contratacaoveiculos.manterItensCheckListMeioTransporteAction">
	<adsm:form action="/contratacaoVeiculos/manterItensCheckListMeioTransporte">
		<adsm:textbox dataType="text" property="dsItemCheckList" label="descricaoItem" maxLength="60" labelWidth="20%" size="50" width="60%"/>
		<adsm:combobox property="tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE" label="modalidade" labelWidth="20%" width="50%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="20%" width="50%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ItensCheckList"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idItemCheckList" property="ItensCheckList" selectionMode="check" unique="true" defaultOrder="tpMeioTransporte,dsItemCheckList" rows="12">
		<adsm:gridColumn width="70%" title="descricaoItem" property="dsItemCheckList"/>
		<adsm:gridColumn width="20%" title="modalidade" property="tpMeioTransporte" isDomain="true"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
