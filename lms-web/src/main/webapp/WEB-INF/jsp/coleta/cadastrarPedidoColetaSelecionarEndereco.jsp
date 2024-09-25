<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
	function carregaPagina_cb() {
		onPageLoad_cb();
		
		// Busca a data atual.
		var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaSelecionarEnderecoAction.getDataAtual", "dataAtual");
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da pesquisa de data atual em getDataAtual().
	 */
	function dataAtual_cb(data, error) {
		setElementValue("dtVigencia", data._value);
		carregaGrid();
	}
</script>

<adsm:window service="lms.coleta.cadastrarPedidoColetaSelecionarEnderecoAction" onPageLoadCallBack="carregaPagina" >
	<adsm:form action="/coleta/cadastrarPedidoColeta">
		<adsm:hidden property="pessoa.idPessoa" />
		<adsm:hidden property="pessoa.tpIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nrIdentificacao" serializable="false"/>
		<adsm:hidden property="pessoa.nmPessoa" serializable="false"/>
		<adsm:hidden property="tipoEnderecoPessoas.tpEndereco" />
		<adsm:hidden property="dtVigencia" />
	</adsm:form>
	
	<adsm:grid idProperty="idEnderecoPessoa" property="enderecoPessoa" 
			   selectionMode="none" rows="15" gridHeight="300" defaultOrder="dsEndereco:asc"
			   service="lms.coleta.cadastrarPedidoColetaSelecionarEnderecoAction.findPaginatedEnderecos"
			   rowCountService="lms.coleta.cadastrarPedidoColetaSelecionarEnderecoAction.getRowCount"
			   unique="true" scrollBars="horizontal" autoSearch="false" >
		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="endereco" property="dsTipoLogradouro" width="60" />
			<adsm:gridColumn title="" property="dsEndereco" width="250" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="numero" property="nrEndereco" width="80" align="right" />
		<adsm:gridColumn title="complemento" property="dsComplemento" width="100" />
		<adsm:gridColumn title="bairro" property="dsBairro" width="130" />
		<adsm:gridColumn title="cep" property="nrCep" width="80" align="right" />
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="180" />
		<adsm:gridColumn title="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="100"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="100"/>
	
		<adsm:buttonBar>
			<adsm:button caption="novoEndereco" disabled="false" onclick="showModalDialog('coleta/cadastrarPedidoColeta.do?cmd=cadastrarEndereco',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:250px;');" />
			<adsm:button caption="fechar" disabled="false" onclick="window.close()"/>			
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script type="text/javascript">
function carregaGrid() {	
	var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
	enderecoPessoa_cb(fb);	
}
</script>