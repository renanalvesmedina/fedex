<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarMCDAction">
	<adsm:form action="/municipios/consultarMCD" idProperty="idMcdMunicipioFilial" >

		<adsm:hidden property="flag" value="01" serializable="false" />
		<adsm:combobox property="tpEmissao" label="tipoEmissao" domain="DM_TIPO_EMISSAO_MCD" required="true" defaultValue="D" />
		<adsm:combobox property="servico.idServico" optionLabelProperty="dsServico" optionProperty="idServico"
				service="lms.municipios.consultarMCDAction.findComboServico"
				label="servico" boxWidth="220" />
		 
		<adsm:section caption="origem" />
		<adsm:lookup dataType="text" idProperty="idMunicipioFilial" criteriaProperty="municipio.nmMunicipio"
				service="lms.municipios.consultarMCDAction.findLookupMunicipio" property="municipioFilialByIdMunicipioFilialOrigem"
				label="municipio" size="35" maxLength="50" width="35%" serializable="false"
				action="/municipios/manterMunicipiosAtendidos" minLengthForAutoPopUpSearch="3" exactMatch="false" >
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.filial.idFilial" 
					modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.filial.sgFilial" 
					modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia" 
					modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" addChangeListener="false" inlineQuery="false"/>					
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.pais.nmPais" 
					modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
   		</adsm:lookup>
   		<adsm:textbox property="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa" 
   				dataType="text" label="uf" serializable="false" labelWidth="8%" width="8%" size="4" disabled="true"/>
		<adsm:textbox property="municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.pais.nmPais" 
				dataType="text" label="pais" serializable="false" width="24%" labelWidth="8%" size="30" disabled="true"/>  
		<adsm:hidden property="municipioFilialByIdMunicipioFilialOrigem.municipio.idMunicipio"/>
        
        <adsm:lookup dataType="text" property="municipioFilialByIdMunicipioFilialOrigem.filial" idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.municipios.consultarMCDAction.findLookupFilial" action="/municipios/manterFiliais"
    			label="filialResponsavel" exactMatch="true" size="3" maxLength="3" width="35%" >
         	<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="municipioFilialByIdMunicipioFilialOrigem.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
	    </adsm:lookup>
		<adsm:combobox label="regional" 
				property="idRegionalOrigem" 
				service="lms.municipios.consultarMCDAction.findRegionaisValidas" 
				optionProperty="idRegional" 
				optionLabelProperty="siglaDescricao"
				serializable="true"
				width="35%"/>
        
		
	 	<adsm:section caption="destino" />
		<adsm:lookup dataType="text" idProperty="idMunicipioFilial" criteriaProperty="municipio.nmMunicipio"
				service="lms.municipios.consultarMCDAction.findLookupMunicipio" property="municipioFilialByIdMunicipioFilialDestino"
				label="municipio" size="35" maxLength="50" width="35%" serializable="false"
				action="/municipios/manterMunicipiosAtendidos" minLengthForAutoPopUpSearch="3" exactMatch="false" >
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.filial.idFilial" 
					modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.filial.sgFilial" 
					modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia" 
					modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" addChangeListener="false" inlineQuery="false"/>					
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa" 
					modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa"/>
			<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.pais.nmPais" 
					modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
   		</adsm:lookup>
   		<adsm:textbox property="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa" 
   				dataType="text" label="uf" serializable="false" labelWidth="8%" width="8%" size="4" disabled="true"/>
		<adsm:textbox property="municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.pais.nmPais" 
				dataType="text" label="pais" serializable="false" width="24%" labelWidth="8%" size="30" disabled="true"/>  
		<adsm:hidden property="municipioFilialByIdMunicipioFilialDestino.municipio.idMunicipio"/>
		
		<adsm:lookup dataType="text" property="municipioFilialByIdMunicipioFilialDestino.filial" idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.municipios.consultarMCDAction.findLookupFilial" action="/municipios/manterFiliais"
    			label="filialResponsavel" exactMatch="true" size="3" maxLength="3" width="35%" >	
         	<adsm:propertyMapping relatedProperty="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="municipioFilialByIdMunicipioFilialDestino.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
	    </adsm:lookup>
	    <adsm:combobox label="regional" 
					property="idRegionalDestino" 
					service="lms.municipios.consultarMCDAction.findRegionaisValidas" 
					optionProperty="idRegional" 
					optionLabelProperty="siglaDescricao"
					serializable="true"
					width="35%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="mcdMunicipioFilial" />
			<adsm:resetButton />
		</adsm:buttonBar> 
		<adsm:i18nLabels>
			<adsm:include key="LMS-46006"/>
		</adsm:i18nLabels>
		
	</adsm:form>
	<adsm:grid property="mcdMunicipioFilial" idProperty="idMcdMunicipioFilial" selectionMode="none"
			gridHeight="142" unique="true" rows="7" scrollBars="horizontal" onRowClick="onRowClickCustom"
			service="lms.municipios.consultarMCDAction.findPaginatedCustom"
			rowCountService="lms.municipios.consultarMCDAction.getRowCountCustom" >
		<adsm:gridColumn title="servico" property="servico_dsServico" width="220" />
		
		<adsm:gridColumn title="municipioOrigem" property="municipioFilialByIdMunicipioFilialOrigem_nmMunicipio" width="220" />
		<adsm:gridColumn title="codIBGE" property="municipioFilialByIdMunicipioFilialOrigem_cdIBGE" width="140" />
		<adsm:gridColumn title="indDistrito" property="blDistritoOrigem" width="140" renderMode="image-check" />
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialResponsavel" property="sgFilialOrigem" width="80" />
			<adsm:gridColumn title="" property="nmFilialOrigem" width="70" />
		</adsm:gridColumnGroup> 
 
		<adsm:gridColumn title="dom" property="blDomingoOrigem" width="30" renderMode="image-check" />
		<adsm:gridColumn title="seg" property="blSegundaOrigem" width="30" renderMode="image-check" />
		<adsm:gridColumn title="ter" property="blTercaOrigem" width="30" renderMode="image-check" />
		<adsm:gridColumn title="qua" property="blQuartaOrigem" width="30" renderMode="image-check" />
		<adsm:gridColumn title="qui" property="blQuintaOrigem" width="30" renderMode="image-check" />
		<adsm:gridColumn title="sex" property="blSextaOrigem" width="30" renderMode="image-check" />
		<adsm:gridColumn title="sab" property="blSabadoOrigem" width="30" renderMode="image-check" />
		
		<adsm:gridColumn title="municipioDestino" property="municipioFilialByIdMunicipioFilialDestino_nmMunicipio" width="220" />
		<adsm:gridColumn title="codIBGE" property="municipioFilialByIdMunicipioFilialDestino_cdIBGE" width="140" />
		<adsm:gridColumn title="indDistrito" property="blDistritoDestino" width="140" renderMode="image-check" />
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialResponsavel" property="sgFilialDestino" width="80" />
			<adsm:gridColumn title="" property="nmFilialDestino" width="70" />
		</adsm:gridColumnGroup>
			
		<adsm:gridColumn title="dom" property="blDomingoDestino" width="30" renderMode="image-check" />
		<adsm:gridColumn title="seg" property="blSegundaDestino" width="30" renderMode="image-check" />
		<adsm:gridColumn title="ter" property="blTercaDestino" width="30" renderMode="image-check" />
		<adsm:gridColumn title="qua" property="blQuartaDestino" width="30" renderMode="image-check" />
		<adsm:gridColumn title="qui" property="blQuintaDestino" width="30" renderMode="image-check" />
		<adsm:gridColumn title="sex" property="blSextaDestino" width="30" renderMode="image-check" />
		<adsm:gridColumn title="sab" property="blSabadoDestino" width="30" renderMode="image-check" />

		<adsm:gridColumn title="tarifaAtual"  property="tarifa_cdTarifa_atual"  width="80" />
		<adsm:gridColumn title="tarifaAntiga" property="tarifa_cdTarifa" width="80" />
		
		<adsm:gridColumn title="postosPassagem" property="qtPedagio" width="130" dataType="integer" />
		<adsm:gridColumn title="ppe" property="nrPpe" width="50" dataType="integer"/>
		
		<adsm:buttonBar/>
	</adsm:grid>
</adsm:window> 
<script>

	function validateTab() {
		if (validateTabScript(document.forms)){
			if(getElementValue("municipioFilialByIdMunicipioFilialOrigem.filial.idFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialOrigem.idMunicipioFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.sgUnidadeFederativa") == "" &&
					getElementValue("idRegionalOrigem") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialOrigem.municipio.unidadeFederativa.pais.nmPais") == ""){
				alert(i18NLabel.getLabel("LMS-46006"));
				return false;
			}					
			if(getElementValue("municipioFilialByIdMunicipioFilialDestino.filial.idFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialDestino.idMunicipioFilial") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.sgUnidadeFederativa") == "" &&
					getElementValue("idRegionalDestino") == "" &&
					getElementValue("municipioFilialByIdMunicipioFilialDestino.municipio.unidadeFederativa.pais.nmPais") == ""){
				alert(i18NLabel.getLabel("LMS-46006"));
				 	return false;
				}		
					return true;
				}	 		
			}

	function onRowClickCustom() {
		return false;
	}
	
</script>