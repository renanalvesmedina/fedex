<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoveiculos.manterItensCheckListMeioTransporteAction">
	<adsm:form action="/contratacaoVeiculos/manterItensCheckListMeioTransporte" idProperty="idItemCheckList">
		<adsm:textbox dataType="text" property="dsItemCheckList" required="true" label="descricaoItem" maxLength="60" labelWidth="20%" size="50" width="60%"/>
		<adsm:combobox property="tpMeioTransporte" onlyActiveValues="true" domain="DM_TIPO_MEIO_TRANSPORTE" required="true" label="modalidade" labelWidth="20%" width="50%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="20%" width="50%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   