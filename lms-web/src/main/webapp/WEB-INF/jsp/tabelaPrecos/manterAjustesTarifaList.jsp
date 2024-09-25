<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>


<adsm:window service="lms.tabelaprecos.manterAjustesTarifaAction">
	<adsm:form action="/tabelaPrecos/manterAjustesTarifa" height="140" idProperty="idAjustetarifa" >
		<!-- Indicador: Filtro de busca para registros ativos  -->
		<adsm:hidden property="geral.tpSituacao" 	 value="A" serializable="false" />
		
		<!-- Indicador: Filtro de busca para lookup filial trazer o endereco -->
		<adsm:hidden property="findEndereco" value="S" serializable="false" />

		<adsm:lookup
			service="lms.tabelaprecos.manterAjustesTarifaAction.findTabelaPreco" 
			action="tabelaPrecos/manterTabelasPreco" 
			label="tabela" 
			property="tabelaPrecoByIdTabelaPreco" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"
			dataType="text" 
			size="11" 
			maxLength="15" 
			labelWidth="6%" 
			width="40%">
			<adsm:propertyMapping relatedProperty="tabelaPrecoByIdTabelaPreco.dsDescricao" 		modelProperty="dsDescricao"/>
			<adsm:propertyMapping relatedProperty="tabelaPrecoByIdTabelaPreco.idTabelaPreco" 	modelProperty="tabelaPreco.idTabelaPreco" />	

			<adsm:textbox
			 	property="tabelaPrecoByIdTabelaPreco.dsDescricao"
			 	dataType="text"
			 	size="35"
			 	maxLength="60"/>
		</adsm:lookup>

		<adsm:section caption="origem"/>
		
		<adsm:lookup
			label="uf"
			property="unidadeFederativaByIdUfOrigem"
			idProperty="idUnidadeFederativa"
			criteriaProperty="sgUnidadeFederativa"
			service="lms.tabelaprecos.manterAjustesTarifaAction.findUnidadeFederativa"
			action="/municipios/manterUnidadesFederativas"
			dataType="text"
			labelWidth="6%"
			width="10%"
			size="3"
			maxLength="3">
			<adsm:propertyMapping criteriaProperty="geral.tpSituacao" modelProperty="tpSituacao"/>
			
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.siglaDescricao" modelProperty="siglaDescricao" />
	
			<adsm:textbox dataType="text" property="unidadeFederativaByIdUfOrigem.nmUnidadeFederativa" width="15%" size="16" serializable="false" disabled="true" />

		</adsm:lookup>
		<adsm:hidden property="unidadeFederativaByIdUfOrigem.siglaDescricao" serializable="false"/>	

		<adsm:lookup 
			service="lms.tabelaprecos.manterAjustesTarifaAction.findFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialOrigem" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"			
			label="filial" 
			dataType="text" size="5" maxLength="3" labelWidth="6%" width="30%">
			<adsm:propertyMapping criteriaProperty="findEndereco" modelProperty="findEndereco" addChangeListener="false"/>

			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" modelProperty="endereco.municipio.unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.nmUnidadeFederativa" modelProperty="endereco.municipio.unidadeFederativa.nmUnidadeFederativa" />
			
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" size="25" maxLength="30" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup 
			service="lms.tabelaprecos.manterAjustesTarifaAction.findMunicipio" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioByIdMunicipioOrigem" 
			idProperty="idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			dataType="text" labelWidth="6%" width="25%" maxLength="60" size="32">
			
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			
			<adsm:propertyMapping relatedProperty="municipioByIdMunicipioOrigem.idMunicipio" 			modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" 	modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" 	modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.nmUnidadeFederativa" 	modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" 					modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" 					modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" 			modelProperty="filial.pessoa.nmFantasia"/>

		</adsm:lookup>

		<adsm:section caption="destino"/>
		
		<adsm:lookup
			label="uf"
			property="unidadeFederativaByIdUfDestino"
			idProperty="idUnidadeFederativa"
			criteriaProperty="sgUnidadeFederativa"
			service="lms.tabelaprecos.manterAjustesTarifaAction.findUnidadeFederativa"
			action="/municipios/manterUnidadesFederativas"
			dataType="text"
			labelWidth="6%"
			width="10%"
			size="3"
			maxLength="3">
			<adsm:propertyMapping criteriaProperty="geral.tpSituacao" modelProperty="tpSituacao"/>
			
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.siglaDescricao" modelProperty="siglaDescricao" />
	
			<adsm:textbox dataType="text" property="unidadeFederativaByIdUfDestino.nmUnidadeFederativa" width="15%" size="16" serializable="false" disabled="true" />
		</adsm:lookup>
		<adsm:hidden property="unidadeFederativaByIdUfDestino.siglaDescricao" serializable="false"/>	

		<adsm:lookup 
			service="lms.tabelaprecos.manterAjustesTarifaAction.findFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialDestino" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"			
			label="filial" 
			dataType="text" size="5" maxLength="3" labelWidth="6%" width="30%">
			<adsm:propertyMapping criteriaProperty="findEndereco" modelProperty="findEndereco" addChangeListener="false"/>
			
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.idUnidadeFederativa" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" modelProperty="endereco.municipio.unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.nmUnidadeFederativa" modelProperty="endereco.municipio.unidadeFederativa.nmUnidadeFederativa" />
			
			<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" serializable="false" size="25" maxLength="30" disabled="true" />
		</adsm:lookup>

		<adsm:lookup 
			service="lms.tabelaprecos.manterAjustesTarifaAction.findMunicipio" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioByIdMunicipioDestino" 
			idProperty="idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			dataType="text" labelWidth="6%" width="25%" maxLength="60" size="32">
			
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfDestino.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			
			<adsm:propertyMapping relatedProperty="municipioByIdMunicipioDestino.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>

		</adsm:lookup>

		<adsm:section caption="fretePeso"/>
		
		<adsm:combobox 
			domain="DM_ACRESCIMO_DESCONTO"
			label="tpAjuste"
			labelWidth="10%"
			property="tpAjusteFretePeso" 
			width="23%">
			
		</adsm:combobox>

		<adsm:combobox 
			domain="DM_TIPO_VALOR"
			label="tpValor"
			property="tpValorFretePreso"  
			labelWidth="10%"
			width="23%">
		</adsm:combobox>

		<adsm:section caption="freteValor"/>
		
		<adsm:combobox 
			domain="DM_ACRESCIMO_DESCONTO"
			label="tpAjuste"
			labelWidth="10%"
			property="tpAjusteFreteValor"  
			width="56%">
			
		</adsm:combobox>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ajusteTarifa"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
		idProperty="idAjusteTarifa" property="ajusteTarifa" width="1000" gridHeight="125" unique="true" scrollBars="horizontal" rows="6" >

		<adsm:editColumn title="hidden"				property="tabelaPrecoByIdTabelaPreco.idTabelaPreco"				field="hidden"  />
		<adsm:editColumn title="hidden" 			property="tpAjusteFretePeso" 									field="hidden" />
		<adsm:editColumn title="hidden" 			property="tpAjusteFreteValor" 									field="hidden" />
		<adsm:editColumn title="hidden" 			property="tpValorFretePreso" 									field="hidden" />
		<adsm:editColumn title="hidden" 			property="vlFretePeso" 											field="hidden" />
		<adsm:editColumn title="hidden" 			property="vlFreteValor" 										field="hidden" />

		<adsm:gridColumn title="tabela" 			property="tabelaPrecoByIdTabelaPreco.dsDescricao" 				width="100" />

		<adsm:gridColumn title="ufOrigem2" 			property="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" 	width="60" />
		<adsm:gridColumn title="filialOrigem" 		property="filialByIdFilialOrigem.sgFilial" 						width="70" />
		<adsm:gridColumn title="municipioOrigem" 	property="municipioByIdMunicipioOrigem.nmMunicipio" 			width="120" />

		<adsm:gridColumn title="ufDestino2" 		property="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" 	width="60" />
		<adsm:gridColumn title="filialDestino" 		property="filialByIdFilialDestino.sgFilial" 					width="70" />
		<adsm:gridColumn title="municipioDestino" 	property="municipioByIdMunicipioDestino.nmMunicipio" 			width="120" />

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>