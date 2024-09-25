<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.produtoProibidoService">
	<adsm:form action="/coleta/manterProdutosProibidos">
		<adsm:textbox property="dsProduto" label="descricao" dataType="text" size="70" width="85%" maxLength="100"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="produtoProibido"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="produtoProibido" idProperty="idProdutoProibido" 
			   selectionMode="check" gridHeight="200" unique="true" rows="14" defaultOrder="dsProduto:asc">
		<adsm:gridColumn title="descricao" property="dsProduto" width="100%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
