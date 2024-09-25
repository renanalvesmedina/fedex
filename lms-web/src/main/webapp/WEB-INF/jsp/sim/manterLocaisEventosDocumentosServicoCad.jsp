<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterLocaisEventosDocumentosServicoAction">
<adsm:form action="/sim/manterLocaisEventosDocumentosServico" idProperty="idLocalEvento">
	<adsm:textbox dataType="integer" property="cdLocalEvento" label="codigo" size="5" maxLength="3" minValue="0" maxValue="999" required="true" labelWidth="18%" width="82%"/>
	<adsm:textbox dataType="text" property="dsLocalEvento" label="localEvento" size="40" maxLength="60" required="true" labelWidth="18%" width="82%"/>
	<adsm:combobox property="tpModal" domain="DM_MODAL" label="modal" required="true" labelWidth="18%" width="35%" />
	<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" required="true" labelWidth="16%" width="29%"/>
	<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" labelWidth="18%" width="35%" />
	<adsm:buttonBar>
		<adsm:storeButton />
		<adsm:newButton />
		<adsm:removeButton />
	</adsm:buttonBar>
</adsm:form>
</adsm:window>