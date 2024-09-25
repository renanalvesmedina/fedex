<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterLocaisEventosDocumentosServicoAction">
	<adsm:form action="/sim/manterLocaisEventosDocumentosServico" idProperty="idLocalEvento">
		<adsm:textbox dataType="integer" property="cdLocalEvento" label="codigo" size="5" maxLength="3" minValue="0" maxValue="999" labelWidth="18%" width="82%"/>
		<adsm:textbox dataType="text" property="dsLocalEvento" label="localEvento" size="40" maxLength="60" labelWidth="18%" width="82%"/>
		<adsm:combobox property="tpModal" domain="DM_MODAL" label="modal" labelWidth="18%" width="35%"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="16%" width="29%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="35%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="localEvento" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idLocalEvento" property="localEvento" defaultOrder="cdLocalEvento, tpSituacao" unique="true">
		<adsm:gridColumn property="cdLocalEvento" title="codigo" dataType="integer" width="15%"/>
		<adsm:gridColumn property="dsLocalEvento" title="localEvento" width="40%"/>
		<adsm:gridColumn property="tpModal" title="modal" isDomain="true" width="15%"/>
		<adsm:gridColumn property="tpAbrangencia" title="abrangencia" isDomain="true" width="15%"/>
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="15%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
