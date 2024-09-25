<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.municipioFilialUFOrigemService">
	<adsm:form action="/municipios/manterUnidadesFederativasAtendidas" idProperty="idMunicipioFilialUFOrigem">

		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>

		<adsm:textbox
			label="filial"
			property="municipioFilial.filial.sgFilial"
			dataType="text"
			labelWidth="17%"
			width="83%"
			size="3"
			disabled="true"
			serializable="false"
		>
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:textbox>

		<adsm:textbox
			label="municipio"
			property="municipioFilial.municipio.nmMunicipio"
			dataType="text"
			labelWidth="17%"
			width="83%"
			size="30"
			disabled="true"
			serializable="false"
		/>

		<adsm:textbox
			label="uf"
			property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa"
			dataType="text"
			labelWidth="17%"
			width="33%"
			disabled="true"
			size="3"
			maxLength="3"
			serializable="false"
		>
			<adsm:textbox
				dataType="text"
				property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa"
				size="30"
				disabled="true"
				serializable="false"
			/>
		</adsm:textbox>

		<adsm:textbox
			label="pais"
			property="municipioFilial.municipio.unidadeFederativa.pais.nmPais"
			dataType="text"
			labelWidth="17%"
			width="33%"
			size="30"
			disabled="true"
			serializable="false"
		/>

		<adsm:checkbox property="municipioFilial.municipio.blDistrito" label="indDistrito" width="33%" labelWidth="17%" disabled="true" serializable="false"/>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false"/>

		<adsm:lookup
			label="ufAtendida"
			idProperty="idUnidadeFederativa"
			property="unidadeFederativa"
			criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findLookup"
			action="/municipios/manterUnidadesFederativas"
			dataType="text"
			labelWidth="17%"
			width="9%"
			size="5"
			maxLength="3"
		>
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="28" width="24%" disabled="true"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipioFilialUFOrigem"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="municipioFilialUFOrigem" idProperty="idMunicipioFilialUFOrigem" selectionMode="check" 
		unique="true" rows="10" defaultOrder="unidadeFederativa_.sgUnidadeFederativa,dtVigenciaInicial">
		<adsm:gridColumn title="ufAtendida" property="unidadeFederativa.siglaDescricao" width="60%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="20%" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="20%" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>