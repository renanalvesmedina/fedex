<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterLocalizacoesCliente" service="lms.sim.manterDescricaoEventoAction">
	<adsm:form action="/sim/manterLocalizacoesCliente" idProperty="idDescricaoEvento">
		<adsm:textbox dataType="integer" property="cdDescricaoEvento" label="codigo" maxLength="3" size="5" minValue="0" maxValue="999" width="82%" labelWidth="18%" required="true"/>
		<adsm:textbox dataType="text" property="dsDescricaoEvento" label="descricaoEvento" maxLength="60" size="40" width="82%" labelWidth="18%" required="true"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="82%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>