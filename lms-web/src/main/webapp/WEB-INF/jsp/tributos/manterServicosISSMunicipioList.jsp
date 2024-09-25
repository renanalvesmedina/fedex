<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.manterServicosISSMunicipioAction">

	<adsm:form action="/tributos/manterServicosISSMunicipio">

		<adsm:lookup label="municipio" 
					 property="municipio" 
					 idProperty="idMunicipio"
					 criteriaProperty="nmMunicipio"					 
					 service="lms.tributos.manterServicosISSMunicipioAction.findMunicipiosLookup"
					 dataType="text" 
					 size="35" 
					 maxLength="60"
					 action="/municipios/manterMunicipios"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"/>		

		<adsm:combobox label="servicoMunicipio" 
					   property="servicoMunicipio.idServicoMunicipio"
					   optionLabelProperty="nrServicoDsServicoMunicipio" 
					   optionProperty="idServicoMunicipio"
					   service="lms.tributos.manterServicosISSMunicipioAction.findServicosMunicipioCombo" 
					   boxWidth="230"
					   labelWidth="17%" 
					   width="32%">
			<adsm:propertyMapping criteriaProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
		</adsm:combobox>			   
		
		<adsm:combobox label="servicoAdicional"
					   property="servicoAdicional.idServicoAdicional"  					   
					   service="lms.tributos.manterServicosISSMunicipioAction.findServicosAdicionaisListCombo" 
					   optionLabelProperty="dsServicoAdicional" 
					   optionProperty="idServicoAdicional" 					   
					   boxWidth="244"/>
					   
		<adsm:combobox label="outroServico" 
					   property="servicoTributo.idServicoTributo"
					   optionLabelProperty="dsServicoTributo" 
					   optionProperty="idServicoTributo" 
					   service="lms.tributos.manterServicosISSMunicipioAction.findServicosTributosCombo" 
					   boxWidth="230"
					   labelWidth="17%" 
					   width="32%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="issMunicipioServico"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid idProperty="idIssMunicipioServico" property="issMunicipioServico" 	           
	            rows="13" unique="true">
	           
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="20%"/>
		<adsm:gridColumn title="servicoMunicipio" property="servicoMunicipio.dsServicoMunicipio" width="25%"/>
		<adsm:gridColumn title="servicoAdicional" property="servicoAdicional.dsServicoAdicional" width="25%"/>
		<adsm:gridColumn title="outroServico" property="servicoTributo.dsServicoTributo" width="30%"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>
	
</adsm:window>
