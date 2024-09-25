<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarMunicipiosAction" >
	<adsm:form action="/municipios/consultarMunicipios" idProperty="idPostoPassagemMunicipio" service="lms.municipios.consultarMunicipiosAction.findByIdPostoPassagemMunicipio">

		<adsm:textbox property="sgFilial" dataType="text" label="filial" labelWidth="22%" width="78%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="nmFantasia" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
	
		<adsm:textbox dataType="text" property="municipioFilial.municipio.nmMunicipio" label="municipio" labelWidth="22%" maxLength="30" size="37" width="78%" disabled="true"/>

		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="22%" width="28%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.pais.dsPais" label="pais" labelWidth="22%" maxLength="30" size="37" width="28%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.tpPostoPassagem.description.description" label="postoPassagem" maxLength="10" size="10" labelWidth="22%" width="28%" disabled="true"/>
 
		<adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio" label="localizacao" labelWidth="22%" width="28%" disabled="true" size="37"/>
		<adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nmPessoa" label="concessionaria" size="60" labelWidth="22%" width="75%" disabled="true"/>		
		
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca.description.description" label="sentido" maxLength="30" size="37" labelWidth="22%" width="28%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia" label="rodovia" maxLength="10" size="10" labelWidth="22%" width="28%" disabled="true"/>
		<adsm:textbox dataType="integer" property="postoPassagem.nrKm" label="km" mask="##,###.#" labelWidth="22%" width="28%" maxLength="2" size="10" disabled="true"/>
		<adsm:listbox serializable="false" width="82%" property="listaValores" size="7" label="valor" labelWidth="22%" style="width:205px"  optionLabelProperty="text" optionProperty="" boxWidth="575" />
		<adsm:buttonBar />
	</adsm:form>
</adsm:window>
<script>
getTab(document).properties.ignoreChangedState=true;
</script>