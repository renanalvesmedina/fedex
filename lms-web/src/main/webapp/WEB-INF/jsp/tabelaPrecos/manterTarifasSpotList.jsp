<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.tarifaSpotService">
	<adsm:form action="/tabelaPrecos/manterTarifasSpot" idProperty="idTarifaSpot">
		<adsm:hidden property="empresa.tpEmpresa" value="C" serializable="false"/>

		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.tabelaprecos.manterTarifasSpotAction.findLookupFilial"
			action="/municipios/manterFiliais"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="15%"
			width="85%"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		 	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" serializable="false" disabled="true"/>
		</adsm:lookup>

		<adsm:lookup
			label="ciaAerea"
			property="empresa"
			idProperty="idEmpresa"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.municipios.empresaService.findLookup"
			action="/municipios/manterEmpresas"
			popupLabel="pesqCiaAerea"
			dataType="text"
			size="18"
			maxLength="18"
			labelWidth="15%"
			width="18%"
		>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" serializable="false" size="30" width="60%" disabled="true"/>
		</adsm:lookup>

		<adsm:textbox
			label="dataLiberacao"
			property="dtLiberacao"
			dataType="JTDate"
			labelWidth="15%"
			width="85%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tarifaSpot"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTarifaSpot" property="tarifaSpot" gridHeight="200" unique="true"
		defaultOrder="filial_.sgFilial,empresa_pessoa_.nmPessoa,aeroportoByIdAeroportoOrigem_.sgAeroporto,aeroportoByIdAeroportoDestino_.sgAeroporto" >

		<adsm:gridColumn title="filialSolicitante" property="filial.sgFilial" width="15%" />
		<adsm:gridColumn title="ciaAerea" property="empresa.pessoa.nmPessoa" width="20%" />
		<adsm:gridColumn title="aeroportoDeOrigem" property="aeroportoByIdAeroportoOrigem.sgAeroporto" width="18%" />
		<adsm:gridColumn title="aeroportoDeDestino" property="aeroportoByIdAeroportoDestino.sgAeroporto" width="18%" />

		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn property="moeda.sgMoeda" dataType="text" title="valorTarifa" width="30"/>
			<adsm:gridColumn property="moeda.dsSimbolo" dataType="text" title="" width="30"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="" dataType="currency" property="vlTarifaSpot" width="80" align="right"/>

		<adsm:gridColumn title="dataLiberacao" dataType="JTDate" property="dtLiberacao" width="17%" align="center"/>

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>