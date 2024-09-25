<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterMeuPerfilAction">
	<adsm:form action="/seguranca/consultarFiliaisByUsuarioLogado">
		
		<adsm:combobox property="empresa.idEmpresa" optionProperty="idEmpresa" optionLabelProperty="pessoa.nmPessoa" label="empresa" service="lms.seguranca.manterMeuPerfilAction.findEmpresasByUsuarioLogado" width="85%"/>
		
		<adsm:textbox dataType="text" label="sigla" property="sgFilial" size="3" maxLength="3"/>
		
		<adsm:buttonBar freeLayout="true">
				<adsm:findButton callbackProperty="filiaisUsuario"/>
				<adsm:resetButton/>
		</adsm:buttonBar>
		</adsm:form>
		
		<adsm:grid idProperty="idFilial" property="filiaisUsuario" gridHeight="200" unique="true" rows="12" service="lms.seguranca.manterMeuPerfilAction.findPaginatedFiliaisByEmpresaUsuarioLogado" rowCountService="lms.seguranca.manterMeuPerfilAction.getRowCountFiliaisByEmpresaUsuarioLogado">
			<%-- adsm:gridColumn title="identificacao" property="empresa.pessoa.tpIdentificacao" isDomain="true" width="60" align="left"/ --%>
			<adsm:gridColumn title="identificacao" property="empresa.pessoa.nrIdentificacao" width="250" dataType="text" />					
			<adsm:gridColumn title="filial" property="filialSgNm"/>
			<adsm:buttonBar>
				<adsm:removeButton/>
			</adsm:buttonBar>
		</adsm:grid>
</adsm:window>