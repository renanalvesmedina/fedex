<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterTiposClassificacaoClienteAction">
	<adsm:form action="/vendas/manterTiposClassificacaoCliente" idProperty="idDescClassificacaoCliente"
		service="lms.vendas.manterTiposClassificacaoClienteAction.findByIdDescClassificacaoCliente">
		<adsm:masterLink showSaveAll="true" idProperty="idTipoClassificacaoCliente">
			<adsm:masterLinkItem property="dsTipoClassificacaoCliente" label="tipoClassificacao" />
		</adsm:masterLink>

        <adsm:textbox dataType="text" property="dsDescClassificacaoCliente" label="descricao" maxLength="60" size="53" width="45%" required="true"/>
		<adsm:checkbox property="blPadrao" label="opcaoPadrao" width="10%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="45%" required="true"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarClassificacao" service="lms.vendas.manterTiposClassificacaoClienteAction.saveDescClassificacaoCliente"/>		
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDescClassificacaoCliente" property="descClassificacaoCliente" unique="true"
			autoSearch="false" rows="10" showGotoBox="true" showPagging="true" detailFrameName="classificacoes"
			service="lms.vendas.manterTiposClassificacaoClienteAction.findPaginatedDescClassificacaoCliente"
			rowCountService="lms.vendas.manterTiposClassificacaoClienteAction.getRowCountDescClassificacaoCliente">
		<adsm:gridColumn title="descricao" property="dsDescClassificacaoCliente" width="70%" />
		<adsm:gridColumn title="opcaoPadrao" property="blPadrao" renderMode="image-check" width="30%" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirClassificacao"
			service="lms.vendas.manterTiposClassificacaoClienteAction.removeByIdsDescClassificacaoCliente"
			/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>