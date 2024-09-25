<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.observacaoCiaAereaService">
	<adsm:form action="/municipios/manterObservacoesCiasAereas" idProperty="idObservacaoCiaAerea">
		<adsm:textbox dataType="text" property="dsObservacaoCiaAerea" label="descricao" maxLength="60" size="60" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton  callbackProperty="observacaoCiaAerea"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idObservacaoCiaAerea" property="observacaoCiaAerea" selectionMode="check" gridHeight="200" unique="true" defaultOrder="dsObservacaoCiaAerea" rows="13">
		<adsm:gridColumn title="descricao" property="dsObservacaoCiaAerea" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

