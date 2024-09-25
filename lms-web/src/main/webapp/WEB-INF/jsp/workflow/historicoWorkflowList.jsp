<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.workflow.historicoWorkflowAction">
	<adsm:form action="/workflow/historicoWorkflow">
		<adsm:hidden property="idProcesso"/>
		<adsm:hidden property="nmTabela"/>

		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente" disabled="true"
				 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				 service="lms.vendas.manterTrtClienteAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
				 label="cliente" size="20" maxLength="20" serializable="true" labelWidth="15%" width="82%" required="false">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="53" maxLength="53" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="dsDivisaoCliente" label="divisao" maxLength="60" labelWidth="15%" size="40" width="35%" disabled="true"	/>

		<adsm:lookup
			action="/tabelaPrecos/manterTabelasPreco"
			criteriaProperty="tabelaPrecoString"
			dataType="text"
			exactMatch="true"
			idProperty="idTabelaPreco"
			label="tabela"
			property="tabelaPreco"
			service="lms.vendas.manterTabelasDivisaoAction.findLookupTabelaPreco"
			size="8"
			maxLength="7"
			disabled="true"
			labelWidth="15%"
			width="35%">
			<adsm:textbox
				dataType="text"
				disabled="true"
				property="tabelaPreco.dsDescricao"
				size="31" />
		</adsm:lookup>

		<adsm:section caption="filtros"/>
		<adsm:lookup property="aprovadorAcao"
        			 idProperty="idUsuario"
        			 criteriaProperty="nrMatricula"
        			 serializable="true"
                     dataType="text"
                     label="aprovadorAcao"
                     size="15"
                     maxLength="20"
                     labelWidth="15%"
                     width="35%"
                     service="lms.workflow.historicoWorkflowAction.findLookupUsuarioFuncionario"
                     action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="nmUsuarioAcao" modelProperty="nmUsuario"/>
                <adsm:textbox dataType="text" property="nmUsuarioAcao" size="24" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:lookup property="solicitante"
        			 idProperty="idUsuario"
        			 criteriaProperty="nrMatricula"
        			 serializable="true"
                     dataType="text"
                     label="solicitante"
                     size="15"
                     maxLength="20"
                     labelWidth="15%"
                     width="35%"
                     service="lms.workflow.historicoWorkflowAction.findLookupUsuarioFuncionario"
                     action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="nmUsuarioSolicitante" modelProperty="nmUsuario"/>
                <adsm:textbox dataType="text" property="nmUsuarioSolicitante" size="24" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:range label="periodoAcao" width="35%" labelWidth="15%">
			<adsm:textbox dataType="JTDate" property="dtAcaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtAcaoFinal"/>
		</adsm:range>

		<adsm:range label="periodoSolicitacao" width="35%" labelWidth="15%">
			<adsm:textbox dataType="JTDate" property="dtSolicitacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtSolicitacaoFinal"/>
		</adsm:range>

		<adsm:combobox label="acao" domain="DM_STATUS_ACAO_WORKFLOW" property="tpSituacaoAcao" width="35%" labelWidth="15%"/>

		<adsm:combobox label="campoAlterado" domain="DM_TIPO_CAMPO_WORKFLOW" property="tpCampoWorkflow" width="35%" labelWidth="15%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="historicos" />
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid selectionMode="none" idProperty="idHistoricoWorkflow" property="historicos" gridHeight="230" unique="true" onRowClick="rowClick" scrollBars="horizontal"
		service="lms.workflow.historicoWorkflowAction.findPaginated" rowCountService="lms.workflow.historicoWorkflowAction.getRowCount" rows="11">
		<adsm:gridColumn title="solicitante" property="solicitante" width="120"/>
		<adsm:gridColumn title="dataSolicitacao" property="dataSolicitacao" width="100" align="center"/>
		<adsm:gridColumn title="situacao" property="situacao" width="90"/>
		<adsm:gridColumn title="ordemAprovacao" property="nrOrdemAprovacao" width="70" align="center"/>
		<adsm:gridColumn title="aprovadorAcao" property="aprovadorAcao" width="120"/>
		<adsm:gridColumn title="acao" property="acao" width="90"/>
		<adsm:gridColumn title="dataAcao" property="dataAcao" width="100" align="center"/>
		<adsm:gridColumn title="dsPendencia" property="dsPendencia" width="150"/>
		<adsm:gridColumn title="campoAlterado" property="campoAlterado" width="150"/>
		<adsm:gridColumn title="conteudoOriginal" property="conteudoOriginal" width="150"/>
		<adsm:gridColumn title="conteudoSolicitado" property="conteudoSolicitado" width="150"/>
		<adsm:gridColumn title="dataLiberacao" property="dataLiberacao" width="100" align="center"/>
		<adsm:gridColumn title="observacaoAcao" property="observacaoAcao" width="140"/>
		<adsm:gridColumn title="aprovadorFinal" property="aprovadorFinal" width="120"/>
		<adsm:gridColumn title="dataEncerramento" property="dataEncerramento" width="100" align="center"/>
	</adsm:grid>

	<adsm:buttonBar>
		<adsm:button caption="fechar" onclick="javascript:window.close();" disabled="false"/>
	</adsm:buttonBar>
</adsm:window>
<script>
	function rowClick(){
		return false;
	}
</script>
