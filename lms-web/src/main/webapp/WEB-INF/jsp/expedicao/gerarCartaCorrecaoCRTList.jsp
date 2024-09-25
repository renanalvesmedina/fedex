<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.gerarCartaCorrecaoCRTAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-00055"/>
	</adsm:i18nLabels>
	<adsm:form action="/expedicao/gerarCartaCorrecaoCRT">
		<adsm:lookup dataType="text" size="5" maxLength="3" labelWidth="15%" width="9%"
			service="lms.expedicao.gerarCartaCorrecaoCRTAction.findFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialOrigem" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			label="filial">
			<adsm:propertyMapping 
				modelProperty="pessoa.nmFantasia"
				relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
				
	     	<adsm:textbox dataType="text" width="76%" size="30"
        		property="filialByIdFilialOrigem.pessoa.nmFantasia" 
	        	serializable="false" 
    	    	disabled="true"/>
        </adsm:lookup>

		<adsm:textbox 
			label="numeroCRT"
			dataType="text" size="1"
			maxLength="2" labelWidth="15%"
			onchange="return sgPaisChange(this);"
			property="ctoInternacional.sgPais" serializable="false">
			<adsm:lookup dataType="integer" 
				property="ctoInternacional" 
				idProperty="idDoctoServico"
				criteriaProperty="nrCrt"
				service="lms.expedicao.gerarCartaCorrecaoCRTAction.findCtoInternacional"
				action="/expedicao/manterCRT" 
				onPopupSetValue="findLookupCRT"
				size="6" mask="000000" maxLength="6" >
				<adsm:propertyMapping 
					criteriaProperty="ctoInternacional.sgPais"
					modelProperty="sgPais"/>

				<adsm:propertyMapping 
					criteriaProperty="filialByIdFilialOrigem.idFilial"
					modelProperty="filialByIdFilialOrigem.idFilial"/>
				<adsm:propertyMapping 
					criteriaProperty="filialByIdFilialOrigem.sgFilial"
					modelProperty="filialByIdFilialOrigem.sgFilial"/>
				<adsm:propertyMapping 
					criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
					modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			</adsm:lookup>
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="cartaCorrecao"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
		idProperty="idCartaCorrecao" 
		property="cartaCorrecao" 
		service="lms.expedicao.gerarCartaCorrecaoCRTAction.findPaginatedCartaCorrecao"
		rowCountService="lms.expedicao.gerarCartaCorrecaoCRTAction.getRowCountCartaCorrecao"
		rows="12" gridHeight="250" unique="true">
		<adsm:gridColumnGroup customSeparator=" - " >
			<adsm:gridColumn title="filial" property="sgFilial" width="65"/>
			<adsm:gridColumn title="" property="pessoa.nmFantasia" width="65"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="numeroCRT" property="nrCrt" width="25%" />
		<adsm:gridColumn title="data" dataType="JTDate" property="dtEmissao" width="25%"/>
		<adsm:gridColumn title="campoAlterado" property="nrCampo" isDomain="true" width="25%"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
	function sgPaisChange(campo) {
	 	var nrValue = getElementValue(campo);
	 	resetValue("ctoInternacional.idDoctoServico");
	 	if(nrValue != "") {
	 		setFocus(document.getElementById("ctoInternacional.nrCrt"), false);
	 		setElementValue(campo, nrValue.toUpperCase());
	 	}
	 	return true;
	}
	
	function findLookupCRT(data, error) {
		setElementValue("ctoInternacional.sgPais", data.sgPais);
		setElementValue("ctoInternacional.nrCrt", data.nrCrt);
		return lookupChange({e:document.getElementById("ctoInternacional.idDoctoServico"), forceChange:true});
	}
	
	function validateTab(){
		if (getElementValue("ctoInternacional.idDoctoServico") != "" || 
		    getElementValue("filialByIdFilialOrigem.idFilial") != "") {
			return true;
		}
		alert(i18NLabel.getLabel("LMS-00055"));
		setFocusOnFirstFocusableField();
		return false;
	}
//-->
</script>
