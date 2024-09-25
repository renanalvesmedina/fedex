<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.estoqueDispositivoQtdeService">
	<adsm:form action="/carregamento/manterEstoqueDispositivosUnitizacao">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="true" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="F" serializable="true"/>
		
		<adsm:lookup label="filial" dataType="text" size="3" maxLength="3" labelWidth="18%" width="82%" required="true"
					 property="filial" exactMatch="true" onPopupSetValue="validaAcessoFilial"
					 service="lms.carregamento.estoqueDispositivoQtdeService.findLookupFiliaisPorUsuario" 
                     action="/municipios/manterFiliais"
                     idProperty="idFilial"
                     criteriaProperty="sgFilial">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup label="empresaProprietaria" dataType="text" size="20" maxLength="20" labelWidth="18%" width="82%"
					 property="empresa"
					 service="lms.municipios.empresaService.findLookup" 
                     action="/municipios/manterEmpresas"  
                     idProperty="idEmpresa" 
                     criteriaProperty="pessoa.nrIdentificacao"
                     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
            <adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
            <adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" serializable="false" size="30" maxLength="30" disabled="true"/>
        </adsm:lookup>

		<adsm:combobox label="tipoDispositivo" labelWidth="18%" width="82%"
					   property="tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao"
					   service="lms.carregamento.tipoDispositivoUnitizacaoService.findTipoDispositivoByQuantidade"
					   optionProperty="idTipoDispositivoUnitizacao"
					   optionLabelProperty="dsTipoDispositivoUnitizacao"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="estoqueDispIdentificado"  />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="estoqueDispIdentificado" idProperty="idEstoqueDispositivoQtde" selectionMode="check" gridHeight="200" unique="true" rows="12"
			   defaultOrder="filial_pessoa_.nmFantasia, empresa_pessoa_.nmPessoa, tipoDispositivoUnitizacao_.dsTipoDispositivoUnitizacao:asc"
			   service="lms.carregamento.estoqueDispositivoQtdeService.findPaginatedWithDateRestriction" 
			   rowCountService="lms.carregamento.estoqueDispositivoQtdeService.getRowCountWithDateRestriction">
		<adsm:gridColumn property="filial.pessoa.nmFantasia" title="filial" width="20%" />
		<adsm:gridColumn property="empresa.pessoa.nmPessoa" title="empresaProprietaria" width="35%" />
		<adsm:gridColumn property="tipoDispositivoUnitizacao.dsTipoDispositivoUnitizacao" title="tipoDispositivo" width="30%" />
		<adsm:gridColumn property="qtEstoque" title="quantidade" width="15%" align="right" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript">

/**
 * Verifica se o usuario tem acesso a filial selecionada na popup de filial.
 * Função necessária pois quando é selecionado um item na popup não é chamado
 * o serviço definido na lookup.
 */
 function validaAcessoFilial(data) {
	var criteria = new Array();
    // Monta um map
    setNestedBeanPropertyValue(criteria, "idFilial", data.idFilial);
    setNestedBeanPropertyValue(criteria, "sgFilial", data.sgFilial);
	
    var sdo = createServiceDataObject("lms.carregamento.estoqueDispositivoQtdeService.findLookupFiliaisPorUsuario", "resultadoLookup", criteria);
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Função que trata o retorno da função validaAcessoFilial.
 */
 function resultadoLookup_cb(data, error) {

	if (error != undefined) {
		alert(error);
	    resetValue("filial.idFilial");
	    setFocus(document.getElementById("filial.sgFilial"));	    
		return false;
	} else {
		filial_sgFilial_exactMatch_cb(data, error);
	}
}

/**
 * Verifica se o usuario tem acesso a mais de uma filial. Caso tenha acesso
 * a apenas uma, a lookup deve vir preenchida.
 */
 function initWindow(eventObj) {
	var criteria = new Array();
    var sdo = createServiceDataObject("lms.carregamento.estoqueDispositivoQtdeService.verificaAcessoFilial", "resultadoBusca", criteria);
    xmit({serviceDataObjects:[sdo]});
}

function resultadoBusca_cb(data, error) {
	if(data.filial==undefined) {
		return false;
	} else {
		setElementValue('filial.idFilial', getNestedBeanPropertyValue(data,"filial.idFilial"));
		setElementValue('filial.sgFilial', getNestedBeanPropertyValue(data,"filial.sgFilial"));
		setElementValue('filial.pessoa.nmFantasia', getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
		
	}
}

</script>