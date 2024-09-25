<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.exigenciaGerRiscoService">
	<adsm:form action="/sgr/manterExigenciasGerenciamentoRisco">
		<adsm:combobox
				label="tipoExigencia"
				labelWidth="20%"
				width="30%"
				property="tipoExigenciaGerRisco.idTipoExigenciaGerRisco"
				optionLabelProperty="dsTipoExigenciaGerRisco"
				optionProperty="idTipoExigenciaGerRisco"
				service="lms.sgr.tipoExigenciaGerRiscoService.findOrdenadoPorDescricao"
		/>
		<adsm:combobox
				property="tpSituacao"
				label="situacao"
				labelWidth="20%"
				width="30%"
				domain="DM_STATUS"
				renderOptions="true"
		/>
		<adsm:combobox
				property="tpCriterioAgrupamento"
				label="criterioAgrupamento"
				labelWidth="20%"
				width="30%"
				domain="DM_TIPO_CRITERIO_AGRUPAMENTO"
				renderOptions="true"
		/>
		<adsm:combobox
				property="blAreaRisco"
				label="utilizaAreaRisco"
				labelWidth="20%"
				width="30%"
				domain="DM_SIM_NAO"
				renderOptions="true"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="exigenciaGerRisco" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
			property="exigenciaGerRisco"
			idProperty="idExigenciaGerRisco"
			defaultOrder="tipoExigenciaGerRisco_.dsTipoExigenciaGerRisco, dsResumida:asc"
			rows="12">
		<adsm:gridColumn title="tipoExigencia" property="tipoExigenciaGerRisco.dsTipoExigenciaGerRisco" width="14%" />
		<adsm:gridColumn title="descricaoResumida" property="dsResumida" width="36%" />
		<adsm:gridColumn title="nivel" property="nrNivel" width="6%" align="right" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="8%" isDomain="true" />
		<adsm:gridColumn title="criterioAgrupamento" property="tpCriterioAgrupamento" isDomain="true" width="12%" />
		<adsm:gridColumn title="utilizaAreaRisco" property="blAreaRisco" renderMode="image-check" width="10%" />
		<adsm:gridColumn title="identificador" property="cdExigenciaGerRisco" width="14%" />

		<adsm:buttonBar>
			<adsm:removeButton service="lms.sgr.exigenciaGerRiscoService.removeByIdsWithOrdering" />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
