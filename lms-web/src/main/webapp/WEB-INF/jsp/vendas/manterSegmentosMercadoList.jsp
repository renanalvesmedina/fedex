<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.segmentoMercadoService">
	<adsm:form action="/vendas/manterSegmentosMercado" >
		<adsm:textbox maxLength="60" dataType="text" property="dsSegmentoMercado" label="segmentoMercado" size="60" width="80%" labelWidth="20%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="80%" labelWidth="20%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="segmentoMercado"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid selectionMode="check" idProperty="idSegmentoMercado" defaultOrder="dsSegmentoMercado" property="segmentoMercado" gridHeight="200" unique="true" rows="13">
		<adsm:gridColumn title="segmentoMercado" property="dsSegmentoMercado" width="80%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="20%" isDomain="true"/>
		<adsm:buttonBar >
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
