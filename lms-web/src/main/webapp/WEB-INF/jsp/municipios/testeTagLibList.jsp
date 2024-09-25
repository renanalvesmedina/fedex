<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.testeTagLibService">
	<adsm:form idProperty="idTesteTagLib" action="/municipios/testeTagLib">
		<adsm:lookup property="pais" service="lms.municipios.paisService.findLookup" dataType="text" 
			criteriaProperty="dsPais" label="pais" size="30" minLengthForAutoPopUpSearch="3" maxLength="60"  exactMatch="false"
			action="/municipios/manterPaises" idProperty="idPais"/>
		<adsm:combobox property="unidadeFederativa.idUnidadeFederativa" label="uf" service="lms.municipios.unidadeFederativaService.find" optionLabelProperty="nmUnidadeFederativa" optionProperty="idUnidadeFederativa"/>		
		<adsm:lookup service="lms.municipios.municipioService.findLookup" dataType="text" 
			property="municipio" criteriaProperty="nrCep" label="municipio" size="9" maxLength="8"
			action="/municipios/manterMunicipios" width="13%" idProperty="idMunicipio">
			<adsm:propertyMapping relatedProperty="municipio.dsMunicipio" modelProperty="dsMunicipio" />
		</adsm:lookup>
		<adsm:textbox dataType="text" property="municipio.dsMunicipio" width="22%" size="15" disabled="true"/>
		<adsm:range label="vigencia"> 
			<adsm:textbox dataType="date" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="date" property="dtVigenciaFinal"/>
		</adsm:range>	
		<adsm:combobox property="tpSituacao" domain="TP_SITUACAO" label="situacao" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="testeTagLib" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTesteTagLib" property="testeTagLib" selectionMode="check" gridHeight="200" unique="true">
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="date" width="50%"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="date" width="35%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="15%"/>
		<adsm:buttonBar>
			<adsm:button caption="excluir" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

