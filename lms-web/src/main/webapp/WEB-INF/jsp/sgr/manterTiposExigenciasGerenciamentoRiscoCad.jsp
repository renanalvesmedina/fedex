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
				required="true"
		/>
		<adsm:combobox
				property="tpSituacao"
				label="situacao"
				domain="DM_STATUS"
				labelWidth="20%"
				width="80%"
				required="true"
				renderOptions="true"
		/>
		<adsm:combobox
				property="tpExigencia"
				label="tipoExigencia"
				domain="DM_TIPO_EXIGENCIA_GER_RISC"
				labelWidth="20%"
				width="80%"
				required="true"
				renderOptions="true"
		/>
		<adsm:checkbox property="blRestrito" label="restrito" labelWidth="20%" width="80%" />
		<adsm:checkbox property="blExigeQuantidade" label="exigeQuantidade" labelWidth="20%" width="80%" />
		<adsm:checkbox property="blControleNivel" label="controlaNivel" labelWidth="20%" width="80%" />
		<adsm:checkbox property="blTravaSistema" label="travaSistema" labelWidth="20%" width="80%" />
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>