<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.observacaoCiaAereaService">
	<adsm:form action="/municipios/manterObservacoesCiasAereas" idProperty="idObservacaoCiaAerea">
		<adsm:textbox dataType="text" property="dsObservacaoCiaAerea" label="descricao" required="true" size="60" maxLength="60" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton caption="limpar" />
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
  