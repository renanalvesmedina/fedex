<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.tarifaColetaService">
	<adsm:form action="/municipios/manterTarifasColetas" idProperty="idTarifaColeta" service="lms.municipios.tarifaColetaService.findByIdDetalhamento" onDataLoadCallBack="pageLoad">
			
			<adsm:hidden property="empresa.tpEmpresa" serializable="false" value="M"/>
			<adsm:hidden property="flag" value="01" serializable="false"/>
			 

	 	<adsm:lookup 
		 	label="filialResponsavelPeloMunicipio" 
		 	dataType="text" size="4" 
		 	maxLength="3" width="35%" required="true"
			service="lms.municipios.filialService.findLookup" 
			property="filialByIdFilial" idProperty="idFilial" onchange="return lookupChaMunicipio();"
			criteriaProperty="sgFilial" 
			action="/municipios/manterFiliais" 
			cellStyle="vertical-align:bottom;"  
			onPopupSetValue="implLookupFilial1PopUp" 
			onDataLoadCallBack="implLookupFilial1DataLoad">
                  
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilial.pessoa.nmFantasia"/>
	      	
	      	      <adsm:textbox dataType="text" serializable="false" property="filialByIdFilial.pessoa.nmFantasia" size="30" disabled="true" cellStyle="vertical-align:bottom;"/>
        </adsm:lookup>
		
		<adsm:lookup 
					service="lms.municipios.municipioFilialService.findLookup" 
					dataType="text" property="municipioFilial" serializable="false"
					criteriaProperty="municipio.nmMunicipio" 
					idProperty="idMunicipioFilial" 
					label="municipio" size="35" maxLength="50" width="35%"
					action="/municipios/manterMunicipiosAtendidos" minLengthForAutoPopUpSearch="2" exactMatch="false" cellStyle="vertical-align:bottom;" required="true">
					<adsm:propertyMapping criteriaProperty="filialByIdFilial.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
					<adsm:propertyMapping criteriaProperty="filialByIdFilial.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="filialByIdFilial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
					<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
					<adsm:propertyMapping relatedProperty="filialByIdFilial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
					<adsm:propertyMapping relatedProperty="municipio.idMunicipio" modelProperty="municipio.idMunicipio"/>
					<adsm:propertyMapping relatedProperty="filialByIdFilial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
					<adsm:propertyMapping relatedProperty="filialByIdFilial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>
		</adsm:lookup>
		
   		<adsm:hidden property="municipio.idMunicipio"/>
   		
		<adsm:lookup label="filialColeta" dataType="text" size="4" maxLength="3" width="35%" required="true"
					 onPopupSetValue="implLookupFilial2PopUp" onDataLoadCallBack="implLookupFilial2DataLoad"
				     service="lms.municipios.filialService.findLookup" property="filialByIdFilialColeta" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
	                 <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialColeta.pessoa.nmFantasia"/>
	                 <adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
		        	 <adsm:textbox dataType="text" serializable="false" property="filialByIdFilialColeta.pessoa.nmFantasia" size="30" disabled="true"/>
	    </adsm:lookup>
	
		<adsm:lookup dataType="text" idProperty="idTarifaPreco" 
			property="tarifaPreco" service="lms.tabelaprecos.tarifaPrecoService.findLookup" 
			size="15" maxLength="5" width="35%" 
			criteriaProperty="cdTarifaPreco"  
			action="/tabelaPrecos/manterTarifasPreco" 
			label="tarifa" required="true">
			<adsm:propertyMapping criteriaProperty="tarifaPreco.tpSituacao" modelProperty="tpSituacao" />
		</adsm:lookup>
		
		<adsm:hidden property="tarifaPreco.tpSituacao" value="A" />

		<adsm:range label="vigencia" labelWidth="15%" width="85%">
	             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
	             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
	    </adsm:range>			
		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.tarifaColetaService.storeMap" />
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		<Script>
		<!--
			var msgLMS29048 = "<adsm:label key="LMS-29048"/>";
		//-->
		</Script>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
document.getElementById("filialByIdFilial.sgFilial").serializable = true;
document.getElementById("filialByIdFilialColeta.sgFilial").serializable = true;
<!--
	function pageLoad_cb(data,error) {
		habilitaCampo();
		onDataLoad_cb(data,error);
		acaoVigencia(data);
	}
	
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  habilitaCampo();
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.storeButton",false);
		      setDisabled("__buttonBar:0.newButton",false);
		      setDisabled("dtVigenciaFinal",false);
		      setFocus("dtVigenciaFinal");
		} else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.newButton",false);
		      setFocus(document.getElementById("__buttonBar:0.newButton"),false);
		}
	}
	
	function afterStore_cb(data,exception) {
		store_cb(data,exception);
		if (exception == undefined) {
			acaoVigencia(data);
			setFocus(document.getElementById("__buttonBar:0.newButton"),false);
		}
	}
	function habilitaCampo() {
		setDisabled("filialByIdFilial.idFilial",false);
		setDisabled("municipioFilial.idMunicipioFilial",false);
		setDisabled("municipioFilial.municipio.nmMunicipio",false);
		setDisabled("filialByIdFilialColeta.idFilial",false);
		setDisabled("tarifaPreco.idTarifaPreco",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click"))
			habilitaCampo();
	}
	

	
		//Funções desenvolvidas para impedir dados inconsistentes entre filial e municipio
	function lookupPopMunicipio(data) {
		if (getNestedBeanPropertyValue(data, "idFilial") != getElementValue("filialByIdFilial.idFilial")) {
			resetMunicipio();	
		}
		return true;
	}
	function lookupChaMunicipio() {
		return filialByIdFilial_sgFilialOnChangeHandler();
	}

	function resetMunicipio() {
		document.getElementById("municipioFilial.municipio.nmMunicipio").value = "";
		document.getElementById("municipioFilial.idMunicipioFilial").value = "";
		document.getElementById("municipio.idMunicipio").value = "";	
	}
			
	function implLookupFilial2DataLoad_cb(data) {
		if (!verificaFilial("filialByIdFilialColeta",getNestedBeanPropertyValue(data,":0.idFilial")))
			return false;
		return filialByIdFilialColeta_sgFilial_exactMatch_cb(data);
	}
	
	function implLookupFilial2PopUp(data, dialogWindow) {
		 var flag = verificaFilial("filialByIdFilialColeta",getNestedBeanPropertyValue(data,"idFilial"),dialogWindow);
		 if(flag == false)
		   setFocus(document.getElementById("filialByIdFilialColeta.sgFilial"));
		 return flag;
		
	}
	
	//filialByIdFilialOrigem	
	function implLookupFilial1DataLoad_cb(data) {
		if (!verificaFilial("filialByIdFilial",getNestedBeanPropertyValue(data,":0.idFilial")))
			return false;
		var firstValue = getElementValue("filialByIdFilial.idFilial");
		filialByIdFilial_sgFilial_exactMatch_cb(data);
		if (firstValue != getElementValue("filialByIdFilial.idFilial"))
			resetMunicipio();
	}

	function implLookupFilial1PopUp(data, dialogWindow) {
        var flag = verificaFilial("filialByIdFilial",getNestedBeanPropertyValue(data,"idFilial"),dialogWindow);
        if (flag) {
           if (getNestedBeanPropertyValue(data, "idFilial") != getElementValue("filialByIdFilial.idFilial"))
              resetMunicipio(); 
        }else{
        	setElementValue(document.getElementById("filialByIdFilial.idFilial"),"");
        	setElementValue(document.getElementById("filialByIdFilial.sgFilial"),"");
        	setElementValue(document.getElementById("filialByIdFilial.pessoa.nmFantasia"),"");
        	setFocus(document.getElementById("filialByIdFilial.sgFilial"));
        }
        return flag;
    }
	
	function verificaFilial(fieldName,fieldValue,dialogWindow) {
		var filiais = new Array(2);
		filiais[0] = "filialByIdFilial";
		filiais[1] = "filialByIdFilialColeta";
		for(x = 0; x < filiais.length; x++) {
			if (filiais[x] != fieldName) {
				if (getElementValue(filiais[x] + ".idFilial") == fieldValue && fieldValue != "") {
				    if(dialogWindow != undefined)
						dialogWindow.close();
					alert(msgLMS29048);
					return false;
				}
			}
		}
		return true;
	}
	
//-->
</script>