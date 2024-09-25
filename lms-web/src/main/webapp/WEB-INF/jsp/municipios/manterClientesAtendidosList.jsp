<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.municipioFilialCliOrigemService">
	<adsm:form action="/municipios/manterClientesAtendidos" idProperty="idMunicipioFilialCliOrigem">
		
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>		
		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>
		
		<adsm:textbox dataType="text" property="municipioFilial.filial.sgFilial"
				label="filial" labelWidth="17%" width="83%" size="3" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.nmMunicipio" label="municipio" labelWidth="17%" width="83%" size="30" disabled="true" serializable="false"/>
 
		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="17%" width="33%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" label="pais" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>

		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" width="33%" labelWidth="17%" disabled="true" serializable="false"/>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>

		<adsm:textbox label="tipoIdentificacao" dataType="text" serializable="false" property="cliente.pessoa.tpIdentificacao" labelWidth="17%" width="60%" disabled="true"/>	

		<adsm:hidden property="cliente.tpCliente" value="S"/>
		<adsm:lookup 
			service="lms.vendas.clienteService.findLookup" dataType="text" onDataLoadCallBack="clienteAtend_dataLoad"
			property="cliente" idProperty="idCliente" criteriaProperty="pessoa.nrIdentificacao" onPopupSetValue="clienteAtend_onPopup"
			label="cliente" size="17" maxLength="20" labelWidth="17%" width="17%" 
			action="/vendas/manterDadosIdentificacao" exactMatch="true" >
			<adsm:propertyMapping criteriaProperty="cliente.tpCliente" modelProperty="tpCliente" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="cliente.pessoa.tpIdentificacao" modelProperty="pessoa.tpIdentificacao.description"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" width="50%"/>

		<adsm:range label="vigencia" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipioFilialCliOrigem"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idMunicipioFilialCliOrigem" property="municipioFilialCliOrigem" selectionMode="check" rows="8" unique="true" defaultOrder="cliente_pessoa_.nmPessoa, dtVigenciaInicial" gridHeight="220">
		<adsm:gridColumn title="identificacao" property="cliente.pessoa.tpIdentificacao" isDomain="true" width="60" />
		<adsm:gridColumn title="" property="cliente.pessoa.nrIdentificacaoFormatado" width="130" align="right"/>
		<adsm:gridColumn title="cliente" property="cliente.pessoa.nmPessoa" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="90" dataType="JTDate"/>

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	function clienteAtend_dataLoad_cb(data){
		if (data != undefined && data.length >= 1){
			var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
		}
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data)
	}

	function clienteAtend_onPopup(data){
		var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
		return true;
	}
</script>
