<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		findButtonScript('dicionarioTags', document.forms[0]);
	}
}

function grid_OnClick(id) {
	return false;
}

</script>

<adsm:window title="dicionario" service="lms.tabelaprecos.tagsTabelaPrecoAction" 
			 onPageLoadCallBack="retornoCarregaPagina">
			 
	<adsm:form action="/tabelaPrecos/consultarTagsTabelaPreco">
	</adsm:form> 
			 
	<adsm:grid idProperty="idDicionarioTags" 
			   property="dicionarioTags" 
			   gridHeight="340" 
			   title="dicionario" 
			   unique="true" rows="14"
			   selectionMode="none"
			   onRowClick="grid_OnClick"
			   defaultOrder="observacoes"
			   service="lms.tabelaprecos.tagsTabelaPrecoAction.findDicionarioTags"
			   rowCountService="lms.tabelaprecos.tagsTabelaPrecoAction.getRowCountDicionarioTags"
			   >
		<adsm:gridColumn title="codigo" 	property="parcelaPreco" width="8%"  align="left" />
		<adsm:gridColumn title="parcela"	property="descricao"	width="42%" />
		<adsm:editColumn title="tag" 		property="tag" 		    width="20%" field="TextBox" dataType="text" disabled="true" />
		<adsm:gridColumn title="observacao" property="observacoes"  width="30%" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

