<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.grupoClassificacaoService">
	<adsm:form action="/municipios/manterGruposClassificacao" idProperty="idGrupoClassificacao">
		<adsm:textbox dataType="text" property="dsGrupoClassificacao" label="descricao" maxLength="60" size="60" labelWidth="15%" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="15%" width="85%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="grupoClassificacao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idGrupoClassificacao" property="grupoClassificacao" selectionMode="check" gridHeight="200" unique="true" defaultOrder="dsGrupoClassificacao" rows="13">
		<adsm:gridColumn title="descricao" property="dsGrupoClassificacao" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
