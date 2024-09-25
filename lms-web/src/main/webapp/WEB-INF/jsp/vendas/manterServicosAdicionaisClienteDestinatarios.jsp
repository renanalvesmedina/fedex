<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterServicosAdicionaisClienteAction">
	<adsm:form 
		action="/vendas/manterServicosAdicionaisCliente" 
		service="lms.vendas.manterServicosAdicionaisClienteAction.findByIdDestinatario"
		idProperty="idServicoAdicionalClienteDestinatario" >								
	
		<adsm:masterLink showSaveAll="true" idProperty="idServicoAdicionalCliente" />		
	
		<adsm:lookup 
				dataType="text" 
				label="destinatario" 
				property="clienteDestinatario" 
	 			idProperty="idCliente" 
	 			criteriaProperty="pessoa.nrIdentificacao" 
	 			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.vendas.manterServicosAdicionaisClienteAction.findLookupCliente" 
				action="/vendas/manterDadosIdentificacao" 
				size="20" 
				maxLength="20" 
				width="85%" 
				serializable="true"
				required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="clienteDestinatario.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="clienteDestinatario.pessoa.nmPessoa" size="50" disabled="true" />
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">		
			<adsm:storeButton caption="adicionar" service="lms.vendas.manterServicosAdicionaisClienteAction.storeDestinatario"/>						 
			<adsm:newButton/>
		</adsm:buttonBar>
	</adsm:form>
		
	<adsm:grid property="destinatarios" idProperty="idServicoAdicionalClienteDestinatario" selectionMode="check" 
			   rows="11" gridHeight="150" onRowClick="disableClick"
			   service="lms.vendas.manterServicosAdicionaisClienteAction.findPaginatedDestinatarios"
			   rowCountService="lms.vendas.manterServicosAdicionaisClienteAction.getRowCountDestinatarios"
			   unique="true"
			   autoSearch="false" showGotoBox="true" showPagging="true" detailFrameName="destinatarios">
<%--
		<adsm:gridColumn property="usuario.login" title="login" width="60" />
	--%>	
		
		<adsm:gridColumn property="pessoa.nrIdentificacao" title="identificacao"  />	
		<adsm:gridColumn property="pessoa.nmPessoa" title="nome" />		
			
		<adsm:buttonBar> 
			<adsm:removeButton service="lms.vendas.manterServicosAdicionaisClienteAction.removeByIdsDestinatarios"/>
		</adsm:buttonBar>
	</adsm:grid>
		
</adsm:window>

<script>
	function disableClick() {
		return false;
	}
</script>