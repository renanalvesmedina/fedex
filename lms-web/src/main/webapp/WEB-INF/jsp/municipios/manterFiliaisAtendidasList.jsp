<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarFiliaisAtendidas" service="lms.municipios.manterFiliaisAtendidasAction">
	<adsm:form action="/municipios/manterFiliaisAtendidas" idProperty="idMunicipioFilialFilOrigem">
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		<adsm:hidden property="municipioFilial.filial.empresa.idEmpresa" />
		<adsm:hidden property="municipioFilial.filial.empresa.pessoa.nmPessoa" serializable="false" />
		<adsm:hidden property="municipioFilial.filial.empresa.pessoa.nrIdentificacao" serializable="false" />

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

		<adsm:lookup
			label="filialAtendida"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.municipios.manterFiliaisAtendidasAction.findLookupFilial"
			action="/municipios/manterFiliais"
			dataType="text"
			size="3"
			maxLength="3"
			exactMatch="true"
			labelWidth="17%"
			width="33%"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="municipioFilial.filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:propertyMapping criteriaProperty="municipioFilial.filial.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="municipioFilial.filial.empresa.pessoa.nrIdentificacao" modelProperty="empresa.pessoa.nrIdentificacao" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" disabled="true"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="17%" width="33%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipioFilialFilOrigem"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idMunicipioFilialFilOrigem" property="municipioFilialFilOrigem" selectionMode="check" rows="10" unique="true" defaultOrder="filial_.sgFilial, dtVigenciaInicial">
		<adsm:gridColumn title="filialAtendida" property="filial.siglaNomeFilial" width="50%" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="25%" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="25%" dataType="JTDate"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>