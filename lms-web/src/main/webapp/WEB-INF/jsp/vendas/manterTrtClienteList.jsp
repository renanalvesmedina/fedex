<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
		
	var idTabelaPreco = window.document.getElementById('idTabelaPreco').value;	
	setDisabled("cliente.idCliente", (idTabelaPreco != undefined && idTabelaPreco !=''));	
}

function onChangeLookupCliente(){
    return cliente_pessoa_nrIdentificacaoOnChangeHandler();
}
</script>

<adsm:window service="lms.vendas.manterTrtClienteAction" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form idProperty="idTrtCliente" action="/vendas/manterTrtCliente" >
	
		<adsm:hidden property="idTabelaPreco"/>
		
		<adsm:textbox dataType="text" label="tabelaPrecos" labelWidth="17%" width="83%" property="tabelaPrecoDesc" size="50" maxLength="50" disabled="true" serializable="true"/>
	
		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.vendas.manterTrtClienteAction.findLookupCliente" action="/vendas/manterDadosIdentificacao" exactMatch="false"
					 onchange="return onChangeLookupCliente()" label="cliente" size="20" maxLength="20" serializable="true" labelWidth="17%" width="83%">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="17%" width="83%">
			<adsm:textbox dataType="JTDate" property="trtCliente.dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="trtCliente.dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="trtCliente" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTrtCliente" property="trtCliente" selectionMode="none" unique="true" rows="13">
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="15%"/>
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="15%"/>
		<adsm:gridColumn title="vigenciaInicialSolicitada" dataType="JTDate" property="dtVigenciaInicialSolicitada" width="20%"/>
		<adsm:gridColumn title="vigenciaFinalSolicitada" dataType="JTDate" property="dtVigenciaFinalSolicitada" width="20%"/>
		<adsm:gridColumn title="situacaoAprovacao" property="tpSituacaoAprovacao" isDomain="true" width="25%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>