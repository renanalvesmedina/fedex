<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.paisService">
	<adsm:form action="/municipios/manterPaises" idProperty="idPais">
		<adsm:hidden property="unidadeFederativas.municipios.idMunicipio"/>
		<adsm:textbox dataType="text" property="sgPais" onchange="validaSigla(this)" label="sigla" size="3" labelWidth="15%" width="35%" maxLength="3"/>
		<adsm:textbox dataType="text" property="sgResumida" onchange="validaSigla(this)" label="siglaResumida" size="2" labelWidth="15%" width="35%" maxLength="2"/>
		<adsm:textbox dataType="text" property="nmPais" label="nome" size="30" labelWidth="15%" width="35%" maxLength="60"/>
		<adsm:textbox dataType="integer" property="cdIso" label="numeroPais"  size="3" labelWidth="15%" width="35%" maxLength="3"/>
		<adsm:combobox property="zona.idZona" label="zona" service="lms.municipios.zonaService.find" optionLabelProperty="dsZona" optionProperty="idZona" boxWidth="200" labelWidth="15%" width="35%" onlyActiveValues="false"/> 
		<adsm:hidden property="unidadeFederativas.idUnidadeFederativa"/>
		<adsm:combobox property="tpBuscaEndereco" label="buscaEndereco" domain="DM_TIPO_BUSCA_ENDERECO" labelWidth="15%" width="35%" boxWidth="140"/> 
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="15%" width="35%" boxWidth="80"/> 
		
		<adsm:hidden property="moedaPais.moeda.idMoeda"/> 
				
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="pais" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid defaultOrder="nmPais" idProperty="idPais" property="pais" selectionMode="check" unique="true" rows="11">
		<adsm:gridColumn title="nome" property="nmPais" width="30%" />
		<adsm:gridColumn title="sigla" property="sgPais" width="10%" />
		<adsm:gridColumn title="numeroPais" property="cdIso" width="20%" dataType="integer" />
		<adsm:gridColumn title="zona" property="zona.dsZona" width="30%" />		
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true"/>	
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	function validaSigla(obj) {
		var sigla = obj;
		sigla.value = sigla.value.toUpperCase();
	}
</script>