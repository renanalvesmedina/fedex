<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoad_cb(data) {
		//
		if (window.dialogArguments && window.dialogArguments.window) {
			var lookupOpenerElements = window.dialogArguments.window.document.forms[0].elements;
			var lookupElement = lookupOpenerElements["ciaFilialMercurio.idCiaFilialMercurio"];
			lookupFillCriteriaOnPopUp({e:lookupElement, opener:window.dialogArguments.window});
			setMasterLink(this.document);
			if (getElementValue("flag") == "01") {
				lookupChange({e:document.forms[0].elements["filial.idFilial"],forceChange:true});
			}
		}else
			onPageLoad_cb(data);
	}
	
	function implLookupFilialCalBack_cb(data) {
		filial_sgFilial_exactMatch_cb(data);
		if (getElementValue("flag") == "01") {
			document.getElementById("__buttonBar:0.findButton").click();
			setElementValue("flag","");
		}
	}
		
//--> 
</script>
<adsm:window service="lms.municipios.ciaFilialMercurioService" onPageLoadCallBack="pageLoad">
	<adsm:form action="/municipios/manterCiasAereasFiliais" idProperty="idCiaFilialMercurio" >
		<adsm:hidden property="flag" serializable="false"/>
		<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>
		<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" property="filial" idProperty="idFilial"
				criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				action="/municipios/manterFiliais" labelWidth="21%" width="8%" exactMatch="false" minLengthForAutoPopUpSearch="3" onDataLoadCallBack="implLookupFilialCalBack">
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
         	<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
	    </adsm:lookup>
		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" width="71%" serializable="false" />

		<adsm:combobox property="empresa.idEmpresa" optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresa"
				labelWidth="21%" width="79%" label="ciaAerea" service="lms.municipios.empresaService.findCiaAerea" boxWidth="205" >
		</adsm:combobox>	
		<adsm:hidden property="empresa.tpSituacao"/>
		
        <adsm:combobox property="tpUso" label="status" domain="DM_STATUS_AEROPORTO" labelWidth="21%" width="79%"/>
        		
        <adsm:range label="vigencia" labelWidth="21%" width="79%" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ciaFilialMercurio" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="ciaFilialMercurio" idProperty="idCiaFilialMercurio" unique="true" rows="11"
				defaultOrder="filial_pessoa_.nmPessoa,empresa_pessoa_.nmPessoa,dtVigenciaInicial" >
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filial" property="filial.sgFilial" width="20" />
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="220" />
		</adsm:gridColumnGroup>	
		
		<adsm:gridColumn title="ciaAerea" property="empresa.pessoa.nmPessoa" />
		<adsm:gridColumn title="status" property="tpUso" isDomain="true" width="100"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="80" dataType="JTDate" />
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
