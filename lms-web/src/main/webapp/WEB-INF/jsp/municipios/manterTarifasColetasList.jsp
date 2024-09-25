<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarTarifasColetas" service="lms.municipios.tarifaColetaService">
	<adsm:form action="/municipios/manterTarifasColetas">
		<adsm:hidden property="empresa.tpEmpresa" serializable="false" value="M"/>
		<adsm:hidden property="municipio.idMunicipio"/>
		<adsm:hidden property="flag" value="01" serializable="false"/>
	 	<adsm:lookup label="filialResponsavelPeloMunicipio" dataType="text" size="4" maxLength="3" width="35%"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilial" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" cellStyle="vertical-align:bottom;" onPopupSetValue="lookupPopMunicipio" onDataLoadCallBack="lookupCalMunicipio">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilial.pessoa.nmFantasia"/>
      			  <adsm:textbox dataType="text" serializable="false" property="filialByIdFilial.pessoa.nmFantasia" size="30" disabled="true" cellStyle="vertical-align:bottom;"/>
        </adsm:lookup>

		<adsm:lookup 
		service="lms.municipios.municipioFilialService.findLookup" 
		dataType="text" property="municipioFilial" serializable="false"
					criteriaProperty="municipio.nmMunicipio" 
					idProperty="idMunicipioFilial" 
					label="municipio" size="35" maxLength="50" width="35%"
					action="/municipios/manterMunicipiosAtendidos" 
					minLengthForAutoPopUpSearch="2" exactMatch="false" cellStyle="vertical-align:bottom;">
					<adsm:propertyMapping criteriaProperty="filialByIdFilial.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
					<adsm:propertyMapping criteriaProperty="filialByIdFilial.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="filialByIdFilial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
					<adsm:propertyMapping relatedProperty="filialByIdFilial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
					<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
					<adsm:propertyMapping relatedProperty="filialByIdFilial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
					<adsm:propertyMapping relatedProperty="filialByIdFilial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
   		</adsm:lookup>

	 	<adsm:lookup label="filialColeta" dataType="text" size="4" maxLength="3" width="35%"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilialColeta" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialColeta.pessoa.nmFantasia"/>
                  <adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
				  <adsm:textbox dataType="text" serializable="false" property="filialByIdFilialColeta.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>

		<adsm:lookup dataType="text" idProperty="idTarifaPreco" property="tarifaPreco" service="lms.tabelaprecos.tarifaPrecoService.findLookup" size="15" maxLength="5" width="35%" criteriaProperty="cdTarifaPreco"  action="/tabelaPrecos/manterTarifasPreco" label="tarifa"/>
		<adsm:range label="vigencia" labelWidth="15%" width="85%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="TarifaColeta"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTarifaColeta" property="TarifaColeta" selectionMode="check" gridHeight="200" unique="true" rows="11" defaultOrder="filialByIdFilial_.sgFilial,municipio_.nmMunicipio,dtVigenciaInicial">
		<adsm:gridColumnGroup separatorType="FILIAL">
				<adsm:gridColumn property="filialByIdFilial.sgFilial"   width="35"  title="filialResponsavelPeloMunicipio"/>
				<adsm:gridColumn property="filialByIdFilial.pessoa.nmFantasia" width="110" title=""/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="municipio" property="municipio.nmMunicipio" width="" />
		<adsm:gridColumnGroup separatorType="FILIAL">
				<adsm:gridColumn property="filialByIdFilialColeta.sgFilial"   width="35"  title="filialColeta"/>
				<adsm:gridColumn property="filialByIdFilialColeta.pessoa.nmFantasia" width="110" title=""/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="tarifa" property="tarifaPreco.cdTarifaPreco" width="50"/>
		<adsm:gridColumnGroup separatorType="FILIAL">
				<adsm:gridColumn property="tarifaPreco.nrKmInicial"   width="35"  title="km"/>
				<adsm:gridColumn property="tarifaPreco.nrKmFinal" width="35" title=""/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="vigenciaInicial" dataType="JTDate" property="dtVigenciaInicial" width="80" />
		<adsm:gridColumn title="vigenciaFinal" dataType="JTDate" property="dtVigenciaFinal" width="70" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<Script>
<!--
	//Funções desenvolvidas para impedir dados inconsistentes entre filial e municipio
	function lookupPopMunicipio(data) {
		if (getNestedBeanPropertyValue(data, "idFilial") != getElementValue("filialByIdFilial.idFilial")) {
			resetMunicipio();	
		}
		return true;
	}
	function lookupChaMunicipio() {
		if (getElementValue("filialByIdFilial.sgFilial") == "")
			resetMunicipio();
		return filialByIdFilial_sgFilialOnChangeHandler();
	}
	function lookupCalMunicipio_cb(data) {
		var firstValue = getElementValue("filialByIdFilial.idFilial");
		var flag = filialByIdFilial_sgFilial_exactMatch_cb(data);
		if (firstValue != getElementValue("filialByIdFilial.idFilial"))
			resetMunicipio();
		return flag;
	}
	function resetMunicipio() {
		resetValue("municipioFilial.municipio.nmMunicipio");
		resetValue("municipioFilial.idMunicipioFilial");
		resetValue("municipio.idMunicipio");	
	}
//-->
</Script>