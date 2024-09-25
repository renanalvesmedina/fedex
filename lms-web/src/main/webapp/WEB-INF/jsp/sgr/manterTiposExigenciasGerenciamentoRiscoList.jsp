<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.sgr.tipoExigenciaGerRiscoService">
	<adsm:form action="/sgr/manterTiposExigenciasGerenciamentoRisco" idProperty="idTipoExigenciaGerRisco">
		<adsm:textbox
				dataType="text"
				property="dsTipoExigenciaGerRisco"
				label="descricao"
				maxLength="30"
				size="40"
				labelWidth="20%"
				width="80%"
		/>
		<adsm:combobox
				property="tpSituacao"
				label="situacao"
				domain="DM_STATUS"
				labelWidth="20%"
				width="30%"
				renderOptions="true"
		/>
		<adsm:combobox
				property="tpExigencia"
				label="tipoExigencia"
				domain="DM_TIPO_EXIGENCIA_GER_RISC"
				labelWidth="20%"
				width="30%"
				renderOptions="true"
		/>
		<adsm:combobox
				property="blRestrito"
				label="restrito"
				domain="DM_SIM_NAO"
				labelWidth="20%"
				width="30%"
				renderOptions="true"
		/>
		<adsm:combobox
				property="blExigeQuantidade"
				label="exigeQuantidade"
				domain="DM_SIM_NAO"
				labelWidth="20%"
				width="30%"
				renderOptions="true"
		/>
		<adsm:combobox
				property="blControleNivel"
				label="controlaNivel"
				domain="DM_SIM_NAO"
				labelWidth="20%"
				width="30%"
				renderOptions="true"
		/>
		<adsm:combobox
				property="blTravaSistema"
				label="travaSistema"
				domain="DM_SIM_NAO"
				labelWidth="20%"
				width="30%"
				renderOptions="true"
		/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipos" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
			property="tipos"
			idProperty="idTipoExigenciaGerRisco"
			defaultOrder="dsTipoExigenciaGerRisco:asc"
			selectionMode="check"
			rows="10">
		<adsm:gridColumn property="dsTipoExigenciaGerRisco" title="descricao" width="35%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:gridColumn property="tpExigencia" title="tipoExigencia" isDomain="true" width="15%" />
		<adsm:gridColumn property="blRestrito" title="restrito" renderMode="image-check" width="10%" />
		<adsm:gridColumn property="blExigeQuantidade" title="exigeQuantidade" renderMode="image-check" width="10%" />
		<adsm:gridColumn property="blControleNivel" title="controlaNivel" renderMode="image-check" width="10%" />
		<adsm:gridColumn property="blTravaSistema" title="travaSistema" renderMode="image-check" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
