<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterLocalizacoesCliente" service="lms.sim.manterDescricaoEventoAction">
	<adsm:form action="/sim/manterLocalizacoesCliente" idProperty="idDescricaoEvento">
		<adsm:textbox dataType="integer" property="cdDescricaoEvento" label="codigo" maxLength="3" size="5" minValue="0" maxValue="999" width="82%" labelWidth="18%"/>
		<adsm:textbox dataType="text" property="dsDescricaoEvento" label="descricaoEvento" maxLength="60" size="40" width="82%" labelWidth="18%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="82%" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="descricaoEvento" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDescricaoEvento" property="descricaoEvento" defaultOrder="cdDescricaoEvento,tpSituacao" rows="12">
		<adsm:gridColumn property="cdDescricaoEvento" title="codigo" dataType="integer" width="15%" />
		<adsm:gridColumn property="dsDescricaoEvento" title="descricaoEvento" width="70%"/>
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="15%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
   	</adsm:grid>	

</adsm:window>



