<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarAtendimentosEspecificosClientes" service="lms.municipios.atendimentoClienteService">
	<adsm:form action="/municipios/manterAtendimentosEspecificosClientes" height="162" idProperty="idAtendimentoCliente">
	
		<adsm:hidden property="operacaoServicoLocaliza.idOperacaoServicoLocaliza" serializable="true"/>
	
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.nmMunicipio" label="municipio" maxLength="30" size="30" labelWidth="16%" width="84%" serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.siglaDescricao" label="uf" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:checkbox disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.blDistrito" label="indDistrito" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.municipioFilial.filial.siglaNomeFilial" label="filial" maxLength="30" size="30" labelWidth="16%" width="84%"  serializable="false"/>
		
		<adsm:hidden property="operacaoServicoLocaliza.municipioFilial.idMunicipioFilial" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.municipioFilial.municipio.idMunicipio" serializable="true"/>
		
		<adsm:hidden property="operacaoServicoLocaliza.blDomingo" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blSegunda" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blTerca" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blQuarta" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blQuinta" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blSexta" serializable="true"/>
		<adsm:hidden property="operacaoServicoLocaliza.blSabado" serializable="true"/>
		

		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.tpOperacao" label="tipoOperacao" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.servico.dsServico" label="servico" maxLength="30" size="30" labelWidth="16%" width="34%"  serializable="false"/>

		<adsm:textbox dataType="text" disabled="true" property="operacaoServicoLocaliza.tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio" label="tipoLocalizacao" maxLength="30" size="30" labelWidth="16%" width="84%"  serializable="false"/>

		<adsm:lookup property="cliente" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" service="lms.vendas.clienteService.findLookup" dataType="text"  label="cliente" size="20" maxLength="20" labelWidth="16%" width="84%" action="/vendas/manterDadosIdentificacao" exactMatch="false" required="false" onDataLoadCallBack="dataLoadC" onPopupSetValue="popUpSetC">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />			
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>
			
			<adsm:propertyMapping relatedProperty="cliente.pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao" />
			<adsm:hidden property="cliente.pessoa.tpIdentificacao" serializable="false"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="16%" width="84%">
			<adsm:textbox dataType="JTDate" required="false" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="atendimentoCliente"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idAtendimentoCliente" property="atendimentoCliente" selectionMode="check" gridHeight="160" unique="true" defaultOrder="cliente_pessoa_.nmPessoa, dtVigenciaInicial" rows="7">
		<adsm:gridColumn title="identificacao" property="cliente.pessoa.tpIdentificacao" width="60" isDomain="true"/>
		<adsm:gridColumn title="" property="cliente.pessoa.nrIdentificacaoFormatado" width="130" align="right"/>
		<adsm:gridColumn title="razaoSocial" property="cliente.pessoa.nmPessoa"  />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="90" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<Script>
    function popUpSetC(data) {
		var nrFormatado = getNestedBeanPropertyValue(data, ":pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, ":pessoa.nrIdentificacao", nrFormatado);
		return true;	
	}

	function dataLoadC_cb(data,exception) {
		if (data != undefined && data.length == 1) {
			var nrFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrFormatado);
		}
		return cliente_pessoa_nrIdentificacao_likeEndMatch_cb(data);
	}
</script>