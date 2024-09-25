<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarServicosMeioTransporte" service="lms.contratacaoveiculos.meioTranspRodoPermissoService">
	<adsm:form action="/contratacaoVeiculos/manterPaisPermissionadoMeiosTransporte" idProperty="idMeioTranspRodoPermisso">
	    
	    <adsm:lookup 
		    picker="false"
		    serializable="false"
			property="meioTransporteRodoviario2" 
			criteriaProperty="meioTransporte.nrFrota" 
			idProperty="idMeioTransporte" 
			service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" 
			dataType="text" size="8" maxLength="6" 
			width="82%" 
			cmd="rodo"
			label="meioTransporte" labelWidth="24%"
			action="/contratacaoVeiculos/manterMeiosTransporte" >
			<adsm:propertyMapping 
			         criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
                     modelProperty="meioTransporte.nrIdentificador" />

            <adsm:propertyMapping 
                      relatedProperty="meioTransporteRodoviario.idMeioTransporte"
                      modelProperty="idMeioTransporte" />      
            <adsm:propertyMapping 
            		  relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
                      modelProperty="meioTransporte.nrIdentificador" />
		   	<adsm:lookup 
		   		picker="true"
				property="meioTransporteRodoviario" 
				criteriaProperty="meioTransporte.nrIdentificador" 
				idProperty="idMeioTransporte" 
				service="lms.contratacaoveiculos.meioTransporteRodoviarioService.findLookup" 
				dataType="text" 
				size="20" maxLength="25" 
				cmd="rodo"
				exactMatch="false"
				minLengthForAutoPopUpSearch="3"
				action="/contratacaoVeiculos/manterMeiosTransporte">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" /> 
	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:lookup property="pais" criteriaProperty="nmPais" idProperty="idPais" service="lms.municipios.paisService.findLookup" dataType="text" label="pais" size="30" maxLength="60" labelWidth="24%" width="76%" action="/municipios/manterPaises" exactMatch="false" minLengthForAutoPopUpSearch="3"/>

		<adsm:textbox dataType="integer" property="nrPermisso" label="numeroPermisso" maxLength="10" size="20" labelWidth="24%" width="60%"/>
		
		<adsm:range label="vigencia" width="60%" labelWidth="24%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="meioTranspRodoPermisso"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="meioTranspRodoPermisso" idProperty="idMeioTranspRodoPermisso" gridHeight="200" unique="true" rows="11"
			defaultOrder="meioTransporteRodoviario_meioTransporte_.nrIdentificador,pais_.nmPais:asc,dtVigenciaInicial:desc">
		<adsm:gridColumn title="meioTransporte" property="meioTransporteRodoviario.meioTransporte.nrFrota" width="50" />
		<adsm:gridColumn title="" property="meioTransporteRodoviario.meioTransporte.nrIdentificador" width="100" align="left" />
		<adsm:gridColumn title="pais" property="pais.nmPais" width="150" />
		<adsm:gridColumn title="numeroPermisso" property="nrPermisso" width="150" dataType="integer"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="100" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="100" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>