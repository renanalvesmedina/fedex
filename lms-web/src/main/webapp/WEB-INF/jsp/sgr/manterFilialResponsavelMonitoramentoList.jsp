<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterFilialResponsavelMonitoramentoAction">
	<adsm:form action="/sgr/manterFilialResponsavelMonitoramento" idProperty="idFilialMonitoramento">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>

		<adsm:lookup dataType="text" property="filialByIdFilialResponsavel" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.municipios.filialService.findLookup" 
					 action="/municipios/manterFiliais" 
					 label="filialResponsavel" size="3" maxLength="3" width="85%">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialResponsavel.pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:textbox dataType="text" property="filialByIdFilialResponsavel.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
	
		<adsm:lookup dataType="text" property="filialByIdFilialMonitorada" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.municipios.filialService.findLookup" 
					 action="/municipios/manterFiliais" 
					 label="filialMonitorada" size="3" maxLength="3" width="85%">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialMonitorada.pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:textbox dataType="text" property="filialByIdFilialMonitorada.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpTipoOperacao" domain="DM_TIPO_OPERACAO_GER_RISCO" label="tipoOperacao" width="85%" renderOptions="true" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="descricaoFiliais"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="descricaoFiliais" idProperty="idFilialMonitoramento" 
			   defaultOrder="filialByIdFilialResponsavel_pessoa_.nmPessoa:asc,filialByIdFilialMonitorada_pessoa_.nmFantasia:asc,tpTipoOperacao:asc" 
			   rows="12" gridHeight="200" unique="true" 
			   service="lms.sgr.manterFilialResponsavelMonitoramentoAction.findPaginatedGrid" 
			   rowCountService="lms.sgr.manterFilialResponsavelMonitoramentoAction.getRowCount" >
		<adsm:gridColumnGroup separatorType="FILIAL">			   
			<adsm:gridColumn title="filialResponsavel" 	property="filialByIdFilialResponsavel.sgFilial" width="72" />
			<adsm:gridColumn title="" property="filialByIdFilialResponsavel.pessoa.nmFantasia" width="200" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="FILIAL">			   
			<adsm:gridColumn title="filialMonitorada" 	property="filialByIdFilialMonitorada.sgFilial" width="72" />
			<adsm:gridColumn title="" property="filialByIdFilialMonitorada.pessoa.nmFantasia" width="200" />		
		</adsm:gridColumnGroup>			
			<adsm:gridColumn title="tipoOperacao" 		property="tpTipoOperacao" isDomain="true"  />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
