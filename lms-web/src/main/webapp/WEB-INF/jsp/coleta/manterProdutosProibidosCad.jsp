<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.produtoProibidoService">
	<adsm:form action="/coleta/manterProdutosProibidos" idProperty="idProdutoProibido">
		<adsm:textbox property="dsProduto" label="descricao" dataType="text" size="70" width="85%" maxLength="100" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>