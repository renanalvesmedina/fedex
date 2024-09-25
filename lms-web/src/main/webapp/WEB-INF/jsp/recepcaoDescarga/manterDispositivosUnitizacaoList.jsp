<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.recepcaodescarga.manterDispositivosUnitizacaoAction">
	<adsm:form action="/recepcaoDescarga/manterDispositivosUnitizacao">

		<adsm:lookup label="empresa" size="20" maxLength="20" labelWidth="22%" width="78%"
				  	 action="/municipios/manterEmpresas" 
					 service="lms.municipios.empresaService.findLookup" dataType="text" 
					 property="empresa" 
					 idProperty="idEmpresa" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
		   	<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
		   	<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="35" maxLength="30" disabled="true" serializable="false"/>
        </adsm:lookup>

		<adsm:combobox label="tipoDispositivo" labelWidth="22%" width="78%"
					   property="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" 
					   service="lms.carregamento.tipoDispositivoUnitizacaoService.findTipoDispositivoByIdentificacao"
					   optionProperty="idTipoDispositivoUnitizacao" 
					   optionLabelProperty="dsTipoDispositivoUnitizacao"
		/>
					   
        <adsm:textbox property="nrIdentificacao" dataType="text" size="15" maxLength="10" label="numeroIdentificacao" labelWidth="22%" width="78%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="22%" width="78%" renderOptions="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="dispositivoUnitizacao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="dispositivoUnitizacao" idProperty="idDispositivoUnitizacao" selectionMode="checkbox" defaultOrder="tipoDispositivoUnitizacao, nrIdentificacao:asc"
			gridHeight="200" unique="true" service="lms.recepcaodescarga.manterDispositivosUnitizacaoAction.findPaginatedDispositivosUnitizacao" rowCountService="lms.recepcaodescarga.manterDispositivosUnitizacaoAction.getRowCount">
		<adsm:gridColumn title="tipoDispositivo" property="tipoDispositivoUnitizacao.dsTipoDispositivoUnitizacao" width="15%"/>
		<adsm:gridColumn title="numeroIdentificacao" property="nrIdentificacao" width="25%" align="right"/>
		<adsm:gridColumn title="empresa" property="empresa.pessoa.nmPessoa" width="50%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>