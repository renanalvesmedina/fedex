<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.tipoDispositivoUnitizacaoService">
	<adsm:form action="/recepcaoDescarga/manterTiposDispositivosUnitizacao" idProperty="idTipoDispositivoUnitizacao">
		<adsm:textbox property="dsTipoDispositivoUnitizacao" label="descricao" dataType="text" size="35" width="85%" maxLength="30" required="true"/>
		<adsm:combobox property="tpControleDispositivo" label="tipoControle" width="85%" domain="DM_TIPO_CONTROLE_DISPOSITIVO" required="true" renderOptions="true"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="15%" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>