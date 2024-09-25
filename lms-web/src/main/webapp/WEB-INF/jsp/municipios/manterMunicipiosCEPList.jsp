<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="municipios.manterMunicipiosCEP" service="lms.municipios.intervaloCepService">
	<adsm:form action="/municipios/manterMunicipiosCEP" idProperty="idIntervaloCep">
		
		<adsm:textbox property="municipio.nmMunicipio" dataType="text" label="municipio" size="30" labelWidth="18%" width="82%" disabled="true" serializable="false"/> 
		<adsm:hidden property="municipio.idMunicipio" />
		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="18%" width="32%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
 		<adsm:textbox property="municipio.unidadeFederativa.pais.nmPais" dataType="text" label="pais" size="30" labelWidth="18%" width="32%" disabled="true" serializable="false"/>
		<adsm:checkbox property="municipio.blDistrito" label="indDistrito" labelWidth="18%" width="32%" disabled="true" serializable="false"/>		
		<adsm:textbox property="municipio.municipioDistrito.nmMunicipio" dataType="text" label="municDistrito" size="30" labelWidth="18%" width="32%" disabled="true" serializable="false"/>
		<adsm:range label="cep" labelWidth="18%" width="32%">
			<adsm:textbox property="nrCepInicial" onchange="return validaIntervaloCeps(this)" dataType="text" maxLength="8"  size="10"/>
			<adsm:textbox property="nrCepFinal" onchange="return validaIntervaloCeps(this)" dataType="text" maxLength="8" size="10"/>		
		</adsm:range>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="18%" width="32%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="intervaloCep"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid idProperty="idIntervaloCep" property="intervaloCep" defaultOrder="nrCepInicial" selectionMode="check" gridHeight="200" unique="true" rows="11">
		<adsm:gridColumn title="cepInicial" property="nrCepInicial" width="40%" />
		<adsm:gridColumn title="cepFinal" property="nrCepFinal" width="40%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="20%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function validaIntervaloCeps(campo){
		var msg = "";
		var cepInicial = document.getElementById('nrCepInicial').value;
		var cepFinal = document.getElementById('nrCepFinal').value;	
		
		if ((cepInicial != '' && cepFinal != '') &&
			cepInicial > cepFinal){
			alert('Valor final menor que valor inicial!');
			setFocus(campo);
			return false;								
		} else return true;
	}
	
</script>
