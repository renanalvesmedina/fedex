<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.tipoDispositivoUnitizacaoService">
	<adsm:form action="/recepcaoDescarga/manterTiposDispositivosUnitizacao">
		<adsm:textbox property="dsTipoDispositivoUnitizacao" label="descricao" dataType="text" size="35" width="85%" maxLength="30" />
		<adsm:combobox property="tpControleDispositivo" label="tipoControle" width="85%" domain="DM_TIPO_CONTROLE_DISPOSITIVO" renderOptions="true" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="15%" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoDispositivoUnitizacao" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="tipoDispositivoUnitizacao" idProperty="idTipoDispositivoUnitizacao" selectionMode="checkbox" gridHeight="200" unique="true" defaultOrder="dsTipoDispositivoUnitizacao:asc" rows="12">
		<adsm:gridColumn property="dsTipoDispositivoUnitizacao" title="descricao" width="50%" />
		<adsm:gridColumn property="tpControleDispositivo" title="tipoControle" width="30%" isDomain="true" />
		<adsm:gridColumn property="tpSituacao" title="situacao" width="20%" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
