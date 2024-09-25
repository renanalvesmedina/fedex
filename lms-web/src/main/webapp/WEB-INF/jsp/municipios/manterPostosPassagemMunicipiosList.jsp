<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function desabilitaLookupPostoPassagem() {
   onPageLoad();
   setDisabled("postoPassagem.idPostoPassagem", false);
   setDisabled("postoPassagem.tpPostoPassagem.description", true);
} 
</script> 
<adsm:window service="lms.municipios.postoPassagemMunicipioService" onPageLoad="desabilitaLookupPostoPassagem" >
	<adsm:form action="/municipios/manterPostosPassagemMunicipios" idProperty="idPostoPassagemMunicipio" >
	
	    
		
		<adsm:lookup dataType="text" property="municipioFilial.filial" idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.municipios.filialService.findLookup" action="/municipios/manterFiliais"
    			size="3" maxLength="3" 
				label="filialResponsavel" labelWidth="17%" width="8%" minLengthForAutoPopUpSearch="3" exactMatch="true" >
         	<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	    </adsm:lookup>
		<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia"
				size="30" maxLength="50" disabled="true" width="75%" serializable="false" />
        
		<adsm:lookup service="lms.municipios.municipioFilialService.findLookup" labelWidth="17%" dataType="text" property="municipioFilial"
					criteriaProperty="municipio.nmMunicipio" idProperty="idMunicipioFilial" label="municipioAtendido" size="35" maxLength="50" width="33%" serializable="false"
					action="/municipios/manterMunicipiosAtendidos" minLengthForAutoPopUpSearch="2" exactMatch="false" cellStyle="vertical-align:bottom;"
					required="true" >
					<adsm:propertyMapping criteriaProperty="municipioFilial.filial.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
					<adsm:propertyMapping criteriaProperty="municipioFilial.filial.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" addChangeListener="false" inlineQuery="false"/>					
					<adsm:propertyMapping relatedProperty="municipioFilial.filial.idFilial" modelProperty="filial.idFilial"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.filial.sgFilial" modelProperty="filial.sgFilial"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
					<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
   		</adsm:lookup>
   		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" serializable="false" labelWidth="8%" width="16%" disabled="true"/>
		<adsm:textbox property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" dataType="text" label="pais" serializable="false" width="16%" labelWidth="8%" disabled="true"/>  
		
   		<adsm:hidden property="municipioFilial.municipio.idMunicipio"/>
   		<adsm:hidden property="flag" value="01"/>
		
		<adsm:lookup dataType="text" property="postoPassagem" idProperty="idPostoPassagem" criteriaProperty="tpPostoPassagem.description"
        		action="/municipios/manterPostosPassagem" cellStyle="vertical-align:bottom;" service="lms.municipios.postoPassagemService.findLookupByFormaCobranca"
        		size="30" minLengthForAutoPopUpSearch="3" exactMatch="false" 
        		label="tipoPostoPassagem" labelWidth="17%" width="83%">
        	<adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.tpSentidoCobranca" modelProperty="tpSentidoCobranca.description" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
	    </adsm:lookup>
		<adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio" serializable="false" label="localizacao" size="30" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca" serializable="false" label="sentido" size="30" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia" serializable="false" label="rodovia" size="15" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>
		<adsm:textbox dataType="text" property="postoPassagem.nrKm" label="km" serializable="false" size="15" maxLength="35" labelWidth="17%" width="33%" disabled="true"/>

        <adsm:range label="vigencia" labelWidth="17%" width="83%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true"> 
			<adsm:findButton callbackProperty="postoPassagemMunicipio" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="postoPassagemMunicipio" idProperty="idPostoPassagemMunicipio"
			defaultOrder="municipioFilial_municipio_.nmMunicipio,municipioFilial_filial_.sgFilial,postoPassagem_rodovia_.dsRodovia,dtVigenciaInicial"
			rows="8" unique="true" >
		<adsm:gridColumnGroup separatorType="FILIAL">
				<adsm:gridColumn property="municipioFilial.filial.sgFilial"   width="44"  title="filial"/>
				<adsm:gridColumn property="municipioFilial.filial.pessoa.nmFantasia" width="100" title=""/>
			</adsm:gridColumnGroup>
		<adsm:gridColumn title="municipio" property="municipioFilial.municipio.nmMunicipio" width="144"/>
		<adsm:gridColumn title="postoPassagem" property="postoPassagem.tpPostoPassagem" isDomain="true" width="130"/>
		<adsm:gridColumn title="rodovia" property="postoPassagem.rodovia.sgRodovia" width="83"/>
		<adsm:gridColumn title="km" dataType="integer" property="postoPassagem.nrKm" width="56"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="86" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="76" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>