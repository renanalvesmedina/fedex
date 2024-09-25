<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.municipios.intervaloCepService">
	<adsm:form action="/municipios/manterMunicipiosCEP" idProperty="idIntervaloCep">

		<adsm:textbox property="municipio.nmMunicipio" dataType="text" label="municipio" size="30" labelWidth="18%" width="82%" disabled="true" serializable="false"/>
		<adsm:hidden property="municipio.idMunicipio" />
		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="18%" width="32%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		<adsm:textbox  property="municipio.unidadeFederativa.pais.nmPais" dataType="text" label="pais" size="30" labelWidth="18%" width="32%" disabled="true" serializable="false"/>
		<adsm:checkbox property="municipio.blDistrito" label="indDistrito" labelWidth="18%" width="32%" disabled="true" serializable="false"/>		
		<adsm:textbox property="municipio.municipioDistrito.nmMunicipio" dataType="text" label="municDistrito" size="30" labelWidth="18%" width="32%" disabled="true" serializable="false"/>
		<adsm:textbox property="nrCepInicial" dataType="text" onchange="return validaIntervaloCeps(this)" label="cepInicial"  size="8" labelWidth="18%" width="32%" maxLength="8" required="true" />
		<adsm:textbox property="nrCepFinal" dataType="text" onchange="return validaIntervaloCeps(this)" label="cepFinal"  size="8" labelWidth="18%" width="32%" maxLength="8" required="true"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" required="true" label="situacao" labelWidth="18%" width="82%"/>	
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	<script>
		function LMS_29013() {
			alert('<adsm:label key="LMS-29013" />');
		}
	</script>
	</adsm:form>
</adsm:window>

<script>
	function validaIntervaloCeps(campo){
		var msg = "";
		var cepInicial = document.getElementById('nrCepInicial').value;
		var cepFinal = document.getElementById('nrCepFinal').value;	
		
		if ((cepInicial != '' && cepFinal != '') &&
			cepInicial > cepFinal){
			LMS_29013();
			setFocus(campo);
			return false;			 					
		} else return true;
	}
</script>
