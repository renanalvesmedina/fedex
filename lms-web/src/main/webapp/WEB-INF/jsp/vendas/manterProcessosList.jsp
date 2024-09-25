<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterProcessosAction">
	<adsm:form action="/vendas/manterProcessos">
		<adsm:textbox maxLength="10" dataType="integer" property="cdProcessoPce" label="codigo" size="10"/> 
		<adsm:textbox maxLength="60" dataType="text" property="dsProcessoPce" label="processo" size="40"/>
		<adsm:combobox label="modal" property="tpModal" domain="DM_MODAL" />
		<adsm:combobox property="tpAbrangencia" domain="DM_ABRANGENCIA" label="abrangencia"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="processoPce"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="processoPce" idProperty="idProcessoPce" gridHeight="200" rows="12" unique="true" defaultOrder="tpModal,tpAbrangencia,dsProcessoPce">
		<adsm:gridColumn title="codigo" property="cdProcessoPce" width="15%" dataType="integer"/>
		<adsm:gridColumn title="processo" property="dsProcessoPce" width="30%"/>
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="20%"/>
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="20%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="15%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
