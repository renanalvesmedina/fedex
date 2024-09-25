<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterServicosMunicipioAction">
	<adsm:form action="/tributos/manterServicosISSMunicipio">
        <adsm:lookup property="municipio" 
		             criteriaProperty="nmMunicipio" 
		             idProperty="idMunicipio" 
		             service="lms.municipios.municipioService.findMunicipioLookup" 
		             dataType="text" 
		             disabled="false" 
		             label="municipio" 
		             size="30" 
		             maxLength="60" 
		             action="/municipios/manterMunicipios" 
		             width="85%" 
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="3"/>		             
        <adsm:textbox dataType="text" property="nrServicoMunicipio" label="numero" minValue="0" size="10" maxLength="10" width="85%" />
        <adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" />
        <adsm:textarea label="descricao" width="85%" columns="60" rows="3" maxLength="500" property="dsServicoMunicipio" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicosISSMunicipio"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idServicoMunicipio" property="servicosISSMunicipio" defaultOrder="municipio_.nmMunicipio,nrServicoMunicipio" selectionMode="check" gridHeight="200" rows="9" unique="true">
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="20%" dataType="text" />
        <adsm:gridColumn title="numero" property="nrServicoMunicipio"  width="20%" dataType="text" />
        <adsm:gridColumn title="descricao" property="dsServicoMunicipio" width="40%" dataType="text"  />
        <adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="20%" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
	    </adsm:buttonBar>
	</adsm:grid>
</adsm:window>
