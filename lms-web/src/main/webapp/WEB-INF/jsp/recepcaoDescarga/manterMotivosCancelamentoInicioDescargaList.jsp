<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.motivoCancelDescargaService">
	<adsm:form action="/recepcaoDescarga/manterMotivosCancelamentoInicioDescarga">
		<adsm:textbox property="dsMotivo" label="descricao" dataType="text" size="65%" width="85%" maxLength="60"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="15%" width="85%" renderOptions="true"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivoCancelDescarga"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idMotivoCancelDescarga" property="motivoCancelDescarga" selectionMode="checkbox" gridHeight="200" unique="true" defaultOrder="dsMotivo:asc" rows="13">
		<adsm:gridColumn property="dsMotivo" title="descricao" width="80%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" width="20%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>