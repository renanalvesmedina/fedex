<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.municipioFilialIntervCepService">
	<adsm:form action="/municipios/manterIntervalosCEPAtendidos" height="122" idProperty="idMunicipioFilialIntervCep">
		<adsm:hidden property="municipioFilial.idMunicipioFilial"/>
		<adsm:hidden property="pais.idPais" serializable="false"/>
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
		<adsm:textbox dataType="text" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="17%" width="33%" size="30" disabled="true" serializable="false" />

		<adsm:textbox dataType="text" property="nrCepInicial" label="cepInicial" required="false" maxLength="8" size="12" labelWidth="17%" width="33%" />
		<adsm:textbox dataType="text" property="nrCepFinal" label="cepFinal" required="false" maxLength="8" size="12" labelWidth="17%" width="33%" />
		

		<adsm:range label="vigencia" labelWidth="17%" width="83%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipioFilialIntervCep"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="municipioFilialIntervCep" idProperty="idMunicipioFilialIntervCep" selectionMode="check" rows="9" unique="true" defaultOrder="nrCepInicial, dtVigenciaInicial"> 
		<adsm:gridColumn title="cepInicial" property="nrCepInicial" width="25%" dataType="text"/>
		<adsm:gridColumn title="cepFinal" property="nrCepFinal" width="25%" dataType="text"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="25%" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="25%" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>