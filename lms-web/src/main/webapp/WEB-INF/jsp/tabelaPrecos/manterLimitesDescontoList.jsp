<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.limiteDescontoService">
	<adsm:form action="/tabelaPrecos/manterLimitesDesconto" idProperty="idLimiteDesconto">
		<adsm:combobox property="divisaoGrupoClassificacao.idDivisaoGrupoClassificacao" label="divisaoGrupo" service="lms.municipios.divisaoGrupoClassificacaoService.findByGrupoClassificacao" optionLabelProperty="dsGrupoDivisao" optionProperty="idDivisaoGrupoClassificacao" boxWidth="200" labelWidth="14%" width="40%"/>
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.municipios.filialService.findLookup" dataType="text" label="filial" size="3" action="/municipios/manterFiliais" minLengthForAutoPopUpSearch="3" exactMatch="true" maxLength="3" labelWidth="14%" width="32%">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup property="funcionario"
			label="funcionario"
			idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			service="lms.tabelaprecos.manterLimitesDescontoAction.findLookupUsuarioFuncionario"
			dataType="text"
			serializable="false"
			size="16"
			maxLength="16"
			labelWidth="14%"
			width="40%"
			action="/configuracoes/consultarFuncionariosView"
			exactMatch="true">
			<adsm:propertyMapping relatedProperty="funcionario.codPessoa.nome" modelProperty="nmUsuario"/>
			<adsm:propertyMapping relatedProperty="usuario.idUsuario" modelProperty="idUsuario"/>
		<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" size="27" disabled="true" serializable="false"/>
		</adsm:lookup>
		<adsm:hidden property="usuario.idUsuario"/>

		<adsm:combobox property="parcelaPreco.idParcelaPreco" optionLabelProperty="nmParcelaPreco" optionProperty="idParcelaPreco" boxWidth="220" service="lms.tabelaprecos.parcelaPrecoService.find" label="parcela" labelWidth="14%" width="32%"/>

		<adsm:combobox
			label="tipoTabela"
			property="tpTipoTabelaPreco"
			domain="DM_TIPO_TABELA_PRECO"
			labelWidth="14%"
			width="40%"/>

		<adsm:combobox
			label="subtipoTabela"
			property="subtipoTabelaPreco.idSubtipoTabelaPreco"
			optionLabelProperty="tpSubtipoTabelaPreco"
			optionProperty="idSubtipoTabelaPreco"
			service="lms.tabelaprecos.subtipoTabelaPrecoService.findByTipoSelecionadoOuTipoNulo"
			labelWidth="14%"
			width="32%">
			<adsm:propertyMapping criteriaProperty="tpTipoTabelaPreco" modelProperty="tpTipoTabelaPreco"/>
		</adsm:combobox>

		<adsm:combobox property="tpIndicadorDesconto" domain="DM_INDICADOR_LIMITE_DESCONTO" label="localDesconto" labelWidth="14%" width="40%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="14%" width="32%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="limiteDesconto"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idLimiteDesconto" property="limiteDesconto" gridHeight="200" unique="true" >
		<adsm:gridColumn title="divisaoGrupo" property="dsGrupoDivisao" width="22%" />
		<adsm:gridColumn title="filial" property="sgFilial" width="7%"/>
		<adsm:gridColumn title="funcionario" property="nmUsuario" width="22%" />
		<adsm:gridColumn title="parcela" property="nmParcelaPreco" width="12%" />
		<adsm:gridColumn title="subtipoTabela" property="tpSubtipoTabelaPreco" width="8%"/>
		<adsm:gridColumn title="limiteDesconto" property="pcLimiteDesconto" dataType="decimal" width="12%" unit="percent"/>
		<adsm:gridColumn title="localDesconto" property="tpIndicadorDesconto" isDomain="true" width="17%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>