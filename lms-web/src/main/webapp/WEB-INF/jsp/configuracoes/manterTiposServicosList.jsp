<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.tipoServicoService">
	<adsm:form action="/configuracoes/manterTiposServicos" idProperty="idTipoServico">
		<adsm:textbox dataType="text" property="dsTipoServico" label="descricao" size="40" maxLength="30"/>
		<adsm:combobox domain="DM_SIM_NAO" property="blPriorizar" label="priorizar"/>
		<adsm:combobox domain="DM_STATUS" property="tpSituacao" label="situacao" width="85%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tiposServicos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="tiposServicos" idProperty="idTipoServico" gridHeight="200" defaultOrder="dsTipoServico" rows="12">
		<adsm:gridColumn width="60%" title="descricao" property="dsTipoServico" />
		<adsm:gridColumn width="20%" title="priorizar" property="blPriorizar" renderMode="image-check"/>
		<adsm:gridColumn width="20%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
