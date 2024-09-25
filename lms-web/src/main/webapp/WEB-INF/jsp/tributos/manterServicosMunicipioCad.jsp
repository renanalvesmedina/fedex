<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterServicosMunicipioAction">
	<adsm:form action="/tributos/manterServicosISSMunicipio" idProperty="idServicoMunicipio" >
	    <adsm:hidden property="municipio.tpSituacao" value="A" serializable="false"/>
	    <adsm:lookup property="municipio" 
		             criteriaProperty="nmMunicipio" 
		             idProperty="idMunicipio" 
		             service="lms.municipios.municipioService.findMunicipioLookup" 
		             required="true" 
		             dataType="text" 
		             disabled="false" 
		             label="municipio" 
		             size="30" 
		             maxLength="60" 
		             action="/municipios/manterMunicipios" 
		             width="85%" 
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="3">
		    <adsm:propertyMapping criteriaProperty="municipio.tpSituacao" modelProperty="tpSituacao"/>
		</adsm:lookup>
		             
        <adsm:textbox dataType="text" property="nrServicoMunicipio" label="numero" size="10" maxLength="10" width="85%" minValue="0" required="true"/>
        <adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" />
        <adsm:textarea label="descricao" width="85%" columns="60" rows="3" maxLength="500" property="dsServicoMunicipio" required="true" />
		<adsm:buttonBar>
	    	<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>