<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<script type="text/javascript">
	function onChangeLookupCliente() {
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}
</script>

<adsm:window
	service="lms.vendas.manterUsuariosResponsaveisClienteAction">
	<adsm:form idProperty="idUsuariosResponsaveisCliente"
		action="/vendas/manterUsuariosResponsaveisCliente">

		<!-- Lookup Clientes -->
		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterUsuariosResponsaveisClienteAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao" exactMatch="false"
			onchange="return onChangeLookupCliente()" label="cliente" size="20"
			maxLength="20" serializable="true" labelWidth="17%" width="83%">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa"
				modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa"
				size="90" maxLength="90" disabled="true" serializable="true" />
		</adsm:lookup>
		<!-- Lookup de usuarios responsaveis -->
		<adsm:lookup dataType="text" property="usuarioResponsavel"
			idProperty="idUsuario" criteriaProperty="nrMatricula"
			service="lms.vendas.manterUsuariosResponsaveisClienteAction.findLookupUsuarioFuncionario"
			action="/configuracoes/consultarFuncionariosView" exactMatch="true"
			label="usuarioResponsavel" size="20" maxLength="20"
			serializable="true" labelWidth="17%" width="83%">
			<adsm:propertyMapping relatedProperty="usuarioResponsavel.nmUsuario"
				modelProperty="nmUsuario" />
			<adsm:textbox dataType="text" property="usuarioResponsavel.nmUsuario"
				size="50" maxLength="50" serializable="true" />
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="17%" width="83%">
			<adsm:textbox dataType="JTDate"
				property="usuarioResponsavel.dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate"
				property="usuarioResponsavel.dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="usuarioResponsavel" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idUsuarioClienteResponsavel" property="usuarioResponsavel"
		selectionMode="none" unique="true" rows="10" scrollBars="horizontal"
		width="900" showPagging="true" showTotalPageCount="true">
		<adsm:gridColumn title="identificacao" dataType="text"
			property="nrIdentificacao" width="12%" />
		<adsm:gridColumn title="cliente" dataType="text"
			property="nmPessoa" width="20%" />
		<adsm:gridColumn title="matricula" dataType="text"
			property="nrMatricula" width="8%" />
		<adsm:gridColumn title="usuario" dataType="text"
			property="usuarioLogin" width="10%" />
		<adsm:gridColumn title="nome" dataType="text"
			property="nmUsuarioResponsavel" width="20%" />
		<adsm:gridColumn title="siglaFilial" dataType="text"
			property="sgFilial" width="5%" />
		<adsm:gridColumn title="dtVigenciaInicial" dataType="JTDate"
			property="dtVigenciaInicial" width="12%" />
		<adsm:gridColumn title="dtVigenciaFinal" dataType="JTDate"
			property="dtVigenciaFinal" width="12%" />
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>