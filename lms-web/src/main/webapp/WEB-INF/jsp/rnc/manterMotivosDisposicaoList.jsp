<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.motivoDisposicaoService" >
	<adsm:form action="/rnc/manterMotivosDisposicao">
		<adsm:textbox dataType="text" property="dsMotivo" label="descricao" maxLength="50" size="50" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="motivos" idProperty="idMotivoDisposicao" selectionMode="check" unique="true" defaultOrder="dsMotivo:asc" rows="13">
		<adsm:gridColumn property="dsMotivo" title="descricao" width="40%" />
		<adsm:gridColumn property="blReverteRespFilAbertura" title="reverteResponsavelFilialAbertura" renderMode="image-check" width="30%" />
		<adsm:gridColumn property="blSomenteAutomatico" title="somenteAutomatico" renderMode="image-check" width="20%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
