<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.integracao.municipioCorporativoService">
	<adsm:form action="/integracao/manterMunicipiosCorporativo" idProperty="idMunicipio"> 
		
		
		<adsm:textbox dataType="text" property="nmMunicipio" 
					  label="municipio" maxLength="50" size="35" 
					  labelWidth="18%" width="32%" />
					  
		<adsm:textbox dataType="text" property="nrCep" 
					  label="cep" size="8" maxLength="8" 
					  labelWidth="18%" width="32%"/>

		<adsm:textbox dataType="text" property="sgUnidadeFederativa"
					  label="uf" size="3" maxLength="2" onchange="uper();" 
					  labelWidth="18%" width="32%"/>
		
		<adsm:lookup label="pais" 
					 property="pais"
					 idProperty="idPais"
					 dataType="text"
					 criteriaProperty="nmPais"
					 service="lms.integracao.manterMunicipiosCorporativoAction.findLookupPais" 
                     action="/integracao/manterPaisesCorporativo"
					 size="25"
					 maxLength="50"
					 labelWidth="18%" width="32%"
			        >   	
        </adsm:lookup>
		
		<adsm:textbox dataType="integer" property="cdIbge" 
					  label="codIBGE" size="8" maxLength="8" 
					  labelWidth="18%" width="32%"/>
					  
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="municipioCorporativo"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idMunicipio" property="municipioCorporativo" rows="12" selectionMode="check" gridHeight="200" unique="true" defaultOrder="nmMunicipio">
		<adsm:gridColumn title="municipio" property="nmMunicipio" width="35%" />
		<adsm:gridColumn title="cep" property="nrCep" align="center" width="15%" />
		<adsm:gridColumn title="codIBGE" property="cdIbge" align="center" width="15%" />
		<adsm:gridColumn title="uf" property="sgUnidadeFederativa" align="center" width="10%" />
		<adsm:gridColumn title="pais" property="pais.nmPais" width="25%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
	function uper(){
		setElementValue("sgUnidadeFederativa", getElementValue("sgUnidadeFederativa").toUpperCase());
	}
</script>