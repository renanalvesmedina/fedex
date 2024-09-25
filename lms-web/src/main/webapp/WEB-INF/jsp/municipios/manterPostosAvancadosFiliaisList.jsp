<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarPostosAvancadosFiliais" service="lms.municipios.postoAvancadoService">
	<adsm:form action="/municipios/manterPostosAvancadosFiliais">

	 	<adsm:lookup label="filial" labelWidth="25%" dataType="text" size="3" maxLength="3" width="75%"
				     service="lms.municipios.filialService.findLookup" property="filial" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
				  <adsm:textbox dataType="text" serializable="false" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
      	
		<adsm:textbox dataType="text" label="descricaoPostoAvancado" property="dsPostoAvancado" maxLength="60" size="30" labelWidth="25%" width="75%" />

		<adsm:lookup property="usuario" label="encarregado" idProperty="idUsuario" criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView" dataType="text" size="10" maxLength="16" labelWidth="25%" width="77%"
		 			 service="lms.municipios.manterPostosAvancadosFiliaisAction.findLookupUsuario">
			<adsm:propertyMapping modelProperty="nmUsuario"    relatedProperty="usuario.nmUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" disabled="true" serializable="false"/>
		</adsm:lookup> 	
		 
		<adsm:lookup  service="lms.municipios.postoAvancadoService.findLookupCliente" dataType="text" property="cliente" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" idProperty="idCliente" label="localizacao" action="vendas/manterDadosIdentificacao" size="20" maxLength="20" labelWidth="25%" width="19%">
			<adsm:propertyMapping relatedProperty="nomeCliente" modelProperty="pessoa.nmFantasia"/>
        </adsm:lookup>
        <adsm:textbox dataType="text" property="nomeCliente" size="30" disabled="true" width="56%" serializable="false"/>

		<adsm:range label="vigencia" labelWidth="25%" width="75%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="PostoAvancado"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid  selectionMode="check" gridHeight="200" unique="true" rows="9" idProperty="idPostoAvancado" property="PostoAvancado" defaultOrder="filial_.sgFilial,cliente_pessoa_.nmFantasia,dsPostoAvancado">
		<adsm:gridColumn title="filial" property="filial.sgFilial" width="6%"/>
		<adsm:gridColumn title="descricaoPostoAvancado" property="dsPostoAvancado" />
		<adsm:gridColumn title="encarregado" property="usuario.nmUsuario" />
		<adsm:gridColumn title="localizacao" property="cliente.pessoa.nmPessoa" />
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="13%" />
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="12%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
