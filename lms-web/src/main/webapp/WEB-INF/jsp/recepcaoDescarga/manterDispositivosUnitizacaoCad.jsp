<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.dispositivoUnitizacaoService">
	<adsm:form action="/recepcaoDescarga/manterDispositivosUnitizacao" idProperty="idDispositivoUnitizacao">

		<adsm:lookup label="empresa" size="20" maxLength="20" labelWidth="22%" width="78%"
					 action="/municipios/manterEmpresas" required="true"
					 service="lms.municipios.empresaService.findLookup" dataType="text" 
					 property="empresa" 
					 idProperty="idEmpresa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
		   	<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
		   	<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="35" maxLength="30" disabled="true" serializable="false"/>
        </adsm:lookup>

		<adsm:combobox label="tipoDispositivo" required="true" 
					   property="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao" 
					   service="lms.carregamento.tipoDispositivoUnitizacaoService.findTipoDispositivoByIdentificacao" 
					   optionProperty="idTipoDispositivoUnitizacao" 
					   optionLabelProperty="dsTipoDispositivoUnitizacao" labelWidth="22%" width="78%"
					   onlyActiveValues="true"
		/>
					   
        <adsm:textbox property="nrIdentificacao" dataType="text" size="15" maxLength="12" label="numeroIdentificacao" labelWidth="22%" width="78%" required="true"/>
		
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" labelWidth="22%" width="78%" required="true" renderOptions="true"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>