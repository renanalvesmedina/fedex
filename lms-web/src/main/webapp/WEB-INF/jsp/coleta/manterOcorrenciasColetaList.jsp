<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.ocorrenciaColetaService" >
	<adsm:form action="/coleta/manterOcorrenciasColeta" idProperty="idOcorrenciaColeta" >
		<adsm:combobox property="tpEventoColeta" label="evento" domain="DM_TIPO_EVENTO_COLETA" width="55%" renderOptions="true" />
		<adsm:textbox dataType="integer" property="codigo" label="codigo" size="4" maxLength="3" mask="000" width="15%" />
		<adsm:textbox dataType="text" property="dsDescricaoResumida" label="ocorrenciaResumida" size="50" maxLength="60" width="55%" />
		<adsm:combobox property="blIneficienciaFrota" label="ineficienciaFrota" domain="DM_SIM_NAO" width="15%" renderOptions="true" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ocorrencias"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="ocorrencias" idProperty="idOcorrenciaColeta" defaultOrder="tpEventoColeta:asc,dsDescricaoResumida:asc" 
		selectionMode="check" gridHeight="200" unique="true" rows="12">
		<adsm:gridColumn property="tpEventoColeta" title="evento" isDomain="true" width="15%" />
		<adsm:gridColumn property="dsDescricaoResumida" title="ocorrenciaResumida" width="30%" />
		<adsm:gridColumn property="codigo" title="codigo" align="right" dataType="integer" mask="000" width="7%" />
		<adsm:gridColumn property="dsDescricaoCompleta" title="ocorrencia" width="38%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>