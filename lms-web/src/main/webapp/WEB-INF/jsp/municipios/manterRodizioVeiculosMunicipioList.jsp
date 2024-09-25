<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.rodizioVeiculoMunicipioService" >
	<adsm:form action="/municipios/manterRodizioVeiculosMunicipio" idProperty="idRodizioVeiculoMunicipio" >
		<adsm:lookup 
			service="lms.municipios.municipioService.findLookup" 
			dataType="text" 
			property="municipio"
			criteriaProperty="nmMunicipio" 
			idProperty="idMunicipio" 
			label="municipio" 
			size="30" 
			maxLength="30"  
			width="85%"
			action="/municipios/manterMunicipios" 
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false" >
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais"/>
		</adsm:lookup>
		
		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.pais.nmPais" label="pais" required="false" disabled="true" serializable="false" />

		<adsm:combobox property="diaSemana" label="diaSemana" domain="DM_DIAS_SEMANA" />
		<adsm:textbox dataType="integer" property="nrFinalPlaca" label="finalPlaca" maxLength="1" size="3"/>

		<adsm:range label="horario">
			<adsm:textbox dataType="JTTime" property="hrRodizioInicial" />
			<adsm:textbox dataType="JTTime" property="hrRodizioFinal" />
		</adsm:range>

		<adsm:range label="vigencia" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rodizioVeiculoMunicipio" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idRodizioVeiculoMunicipio" property="rodizioVeiculoMunicipio"  selectionMode="check" rows="11" unique="true">			
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" />
		<adsm:gridColumn title="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa" width="4%" />
		<adsm:gridColumn title="pais" property="municipio.unidadeFederativa.pais.nmPais" width="15%" />
		<adsm:gridColumn title="diaSemana" property="diaSemana" width="12%" isDomain="true" />
		<adsm:gridColumn title="finalPlaca" property="nrFinalPlaca" width="11%" dataType="integer"/>
		<adsm:gridColumn title="horaInicial" property="hrRodizioInicial" width="10%" dataType="JTTime"/>
		<adsm:gridColumn title="horaFinal" property="hrRodizioFinal" width="9%" dataType="JTTime" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="12%" dataType="JTDate" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="11%" dataType="JTDate" />

		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>