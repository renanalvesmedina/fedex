<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterFilialResponsavelMonitoramentoAction">
	<adsm:form action="/sgr/manterFilialResponsavelMonitoramento" idProperty="idFilialMonitoramento">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		
		<adsm:lookup dataType="text" property="filialByIdFilialResponsavel" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.municipios.filialService.findLookup" 
					 action="/municipios/manterFiliais" 
					 label="filialResponsavel" size="3" maxLength="3" width="85%" required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialResponsavel.pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:textbox dataType="text" property="filialByIdFilialResponsavel.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup dataType="text" property="filialByIdFilialMonitorada" 
					 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.municipios.filialService.findLookup" 
					 action="/municipios/manterFiliais" 
					 label="filialMonitorada" size="3" maxLength="3" width="85%" required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialMonitorada.pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:textbox dataType="text" property="filialByIdFilialMonitorada.pessoa.nmFantasia" size="50" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpTipoOperacao" domain="DM_TIPO_OPERACAO_GER_RISCO" label="tipoOperacao" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
