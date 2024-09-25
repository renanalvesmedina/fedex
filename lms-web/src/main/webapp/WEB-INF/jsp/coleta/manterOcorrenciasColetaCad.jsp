<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.coleta.ocorrenciaColetaService" >
	<adsm:form action="/coleta/manterOcorrenciasColeta" idProperty="idOcorrenciaColeta" >
		<adsm:combobox property="tpEventoColeta" label="evento" domain="DM_TIPO_EVENTO_COLETA" width="85%" required="true" renderOptions="true"/>
		<adsm:textbox dataType="integer" property="codigo" label="codigo" size="4" maxLength="3" mask="000" width="55%" required="true"/>
		<adsm:textbox dataType="text" property="dsDescricaoResumida" label="ocorrenciaResumida" size="50" maxLength="60" width="55%" required="true"/>
		<adsm:combobox property="blIneficienciaFrota" label="ineficienciaFrota" domain="DM_SIM_NAO" width="15%" renderOptions="true"/>
		<adsm:textarea property="dsDescricaoCompleta" label="ocorrencia" maxLength="200" rows="3" columns="70" width="85%" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton service="lms.coleta.ocorrenciaColetaService.storeOcorrenciasColeta"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>