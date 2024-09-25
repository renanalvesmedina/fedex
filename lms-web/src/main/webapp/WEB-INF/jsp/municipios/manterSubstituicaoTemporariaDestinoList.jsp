<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarSubstituicaoTemporariaDestino" service="lms.municipios.substAtendimentoFilialService">
	<adsm:form action="/municipios/manterSubstituicaoTemporariaDestino" height="100">
		<adsm:hidden property="tpEmpresa" serializable="false" value="M"/>
	 	<adsm:lookup label="filialDestino" labelWidth="15%" dataType="text" size="2" maxLength="3" width="7%"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilialDestino" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
        </adsm:lookup> 
      	<adsm:textbox dataType="text" serializable="false" property="filialByIdFilialDestino.pessoa.nmFantasia" size="28" disabled="true" width="28%"/>

	 	<adsm:lookup label="substituirPelaFilial" dataType="text" size="2" maxLength="3" width="7%"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilialDestinoSubstituta" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestinoSubstituta.pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
        </adsm:lookup>
      	<adsm:textbox dataType="text" serializable="false" property="filialByIdFilialDestinoSubstituta.pessoa.nmFantasia" size="28" disabled="true" width="28%"/>

		<adsm:section caption="configuracaoOperacional" />
		<adsm:lookup property="unidadeFederativa"  
					idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				 	service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					width="7%" label="ufOrigem" size="2" maxLength="10" labelWidth="15%"  
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
		</adsm:lookup>
		<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" width="68%" size="28" serializable="false" disabled="true"/>

	   <adsm:combobox label="regionalOrigem" 
	   		property="regional.idRegional" 
	   		optionLabelProperty="siglaDescricao" 
	   		optionProperty="idRegional" 
	   		service="lms.municipios.regionalService.findRegional" 
	   		labelWidth="15%" width="35%"
	   		boxWidth="215"
	   		onchange="regionalOrigemChange(this);" />

      	<adsm:range label="vigencia" labelWidth="15%">
			<adsm:textbox size="12" property="dtVigenciaRegionalInicial" dataType="JTDate" picker="false" disabled="true"/>
			<adsm:textbox size="12" property="dtVigenciaRegionalFinal" dataType="JTDate" picker="false" disabled="true"/>
		</adsm:range>

		<adsm:lookup label="filialOrigem" labelWidth="15%" dataType="text" size="2" maxLength="3" width="7%"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilialOrigem" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa"/>
                  <adsm:propertyMapping criteriaProperty="regional.idRegional" modelProperty="regionalFiliais.regional.idRegional"/>
                  <adsm:propertyMapping criteriaProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="pessoa.enderecoPessoas.municipio.unidadeFederativa.idUnidadeFederativa"/>
        </adsm:lookup>
      	<adsm:textbox dataType="text" serializable="false" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="28" disabled="true" width="68%"/>
      	
		<adsm:lookup dataType="text" property="municipio.municipioFiliais" idProperty="municipio.idMunicipio" criteriaProperty="municipio.nmMunicipio"
	             action="/municipios/manterMunicipiosAtendidos" service="lms.municipios.municipioFilialService.findLookup"
                 maxLength="30" size="38" minLengthForAutoPopUpSearch="3" exactMatch="false"
                 label="municipioDestino" labelWidth="15%" width="35%" serializable="false">
                 <adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
        </adsm:lookup>
        <adsm:hidden property="municipio.idMunicipio"/>
        
		<adsm:combobox property="servico.idServico" label="servico" service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" boxWidth="200"/>
		<adsm:combobox property="naturezaProduto.idNaturezaProduto" label="natureza" service="lms.expedicao.naturezaProdutoService.find" optionLabelProperty="dsNaturezaProduto" optionProperty="idNaturezaProduto" labelWidth="15%" width="80%"/>

		<adsm:lookup  service="lms.vendas.clienteService.findLookup" dataType="text" onDataLoadCallBack="clienteExactMatch" property="cliente" criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" idProperty="idCliente" label="clienteRemetente" action="vendas/manterDadosIdentificacao" size="20" maxLength="20" labelWidth="15%" width="19%">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
        </adsm:lookup>
        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="35" disabled="true" width="62%" serializable="false"/>

		<adsm:range label="vigencia" labelWidth="15%" width="45%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="SubstAtendimentoFilial"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idSubstAtendimentoFilial" property="SubstAtendimentoFilial" selectionMode="check" 
			   gridHeight="180" unique="true" rows="9" scrollBars="horizontal" defaultOrder="filialByIdFilialDestino_.sgFilial,dtVigenciaInicial" >
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="filialByIdFilialDestino.sgFilial"   width="100"  title="filialDestino"/>
			<adsm:gridColumn property="filialByIdFilialDestino.pessoa.nmFantasia" width="100" title=""/>
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="filialByIdFilialDestinoSubstituta.sgFilial"   width="100"  title="filialSubstituta"/>
			<adsm:gridColumn property="filialByIdFilialDestinoSubstituta.pessoa.nmFantasia" width="100" title=""/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="ufOrigem" property="unidadeFederativa.sgUnidadeFederativa" width="80"/>
		<adsm:gridColumn title="regionalOrigem" property="regional.dsRegional" width="130"/>
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn property="filialByIdFilialOrigem.sgFilial"   width="100"  title="filialOrigem"/>
			<adsm:gridColumn property="filialByIdFilialOrigem.pessoa.nmFantasia" width="100" title=""/>
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="municipioDestino" property="municipio.nmMunicipio" width="130" />
		<adsm:gridColumn title="servico" property="servico.dsServico" width="180" />		 
		<adsm:gridColumn title="natureza" property="naturezaProduto.dsNaturezaProduto" width="100"/>
		<adsm:gridColumn title="clienteRemetente" property="cliente.pessoa.nmPessoa" width="180" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="93"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="93"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--

	function clienteExactMatch_cb(data, error){
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data, error);

		if (data != undefined){
			var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			if (nrIdentificacaoFormatado != undefined)
				document.getElementById("cliente.pessoa.nrIdentificacao", nrIdentificacaoFormatado).value = nrIdentificacaoFormatado;
		}
		
	}
	
	function regionalOrigemChange(field) {
		comboboxChange({e:field});

		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaRegionalInicial",setFormat(document.getElementById("dtVigenciaRegionalInicial"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaRegionalFinal",setFormat(document.getElementById("dtVigenciaRegionalFinal"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaRegionalInicial");
			resetValue("dtVigenciaRegionalFinal");
		}
	}
	
	
//-->
</script>