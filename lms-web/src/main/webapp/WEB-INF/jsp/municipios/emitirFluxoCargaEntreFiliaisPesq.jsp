<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="fluxoCargaEntreFiliais" service="lms.municipios.emitirFluxoCargaEntreFiliaisAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/municipios/emitirFluxoCargaEntreFiliais">
	
		 <adsm:i18nLabels> 
			<adsm:include key="LMS-00013"/>
			    <adsm:include key="filialOrigem"/>
                <adsm:include key="ufOrigem2"/>
                <adsm:include key="filialDestino"/>
                <adsm:include key="ufDestino2"/>
                <adsm:include key="ou"/>
		 </adsm:i18nLabels>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>

		<adsm:combobox labelWidth="20%" property="tpEmissao" label="tipoEmissao" domain="DM_TIPO_EMISSAO_MCD" width="35%" required="true" />
		<adsm:combobox labelWidth="20%" width="80%" label="servico" property="servico.idServico" boxWidth="250"
						optionLabelProperty="dsServico" optionProperty="idServico"
						service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findComboServico">
			<adsm:propertyMapping relatedProperty="servico.dsServico" modelProperty="dsServico"/>
		</adsm:combobox>
		<adsm:hidden property="servico.dsServico"/>
		
		<adsm:combobox width="80%" label="regionalOrigem2" property="regionalO.idRegional" optionLabelProperty="siglaDescricao" labelWidth="20%" onchange="chgRgO(this);"  
						optionProperty="idRegional" service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findComboRegional" boxWidth="250">
			 <adsm:propertyMapping relatedProperty="regionalO.nmRegional" modelProperty="siglaDescricao"/>
		</adsm:combobox>
		<adsm:hidden property="regionalO.nmRegional"/>
		
		<adsm:range label="vigencia" labelWidth="20%">
			<adsm:textbox size="12" property="dtVigenciaInicialO" dataType="JTDate" picker="false" disabled="true"/>
			<adsm:textbox size="12" property="dtVigenciaFinalO" dataType="JTDate" picker="false" disabled="true"/>
		</adsm:range>
		
		<adsm:lookup label="filialOrigem" labelWidth="20%" dataType="text" size="3" maxLength="3" width="80%"
			     service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findLookupFilial" property="filialO" idProperty="idFilial"
			     onPopupSetValue="popFlO" onDataLoadCallBack="cbFlO"
				 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
		    <adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialO.pessoa.nmFantasia"/>
			<%--adsm:propertyMapping modelProperty="lastRegional.idRegional" relatedProperty="regionalO.idRegional" blankFill="false"/>
			<adsm:propertyMapping modelProperty="lastRegional.sgNmFull" relatedProperty="regionalO.nmRegional" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="regionalO.idRegional" modelProperty="regionalFiliais.regional.idRegional"/--%>
			<adsm:textbox dataType="text" property="filialO.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup>

		<adsm:lookup property="unidadeFederativaO"  idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findLookupUf" dataType="text"
					 labelWidth="20%" width="80%" label="ufOrigem2" size="3" maxLength="2" 
					 onPopupSetValue="popUfO" onDataLoadCallBack="cbUfO"
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping relatedProperty="unidadeFederativaO.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox dataType="text" property="unidadeFederativaO.nmUnidadeFederativa" size="35" disabled="true" />
		</adsm:lookup>		
		
		<adsm:combobox width="80%" label="regionalDestino2" property="regionalD.idRegional" optionLabelProperty="siglaDescricao" labelWidth="20%" onchange="chgRgD(this);"
						optionProperty="idRegional" service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findComboRegional" boxWidth="250">
			 <adsm:propertyMapping relatedProperty="regionalD.nmRegional" modelProperty="siglaDescricao"/>
		</adsm:combobox>
		<adsm:hidden property="regionalD.nmRegional"/>

		<adsm:range label="vigencia" labelWidth="20%">
			<adsm:textbox size="12" property="dtVigenciaInicialD" dataType="JTDate" picker="false" disabled="true"/>
			<adsm:textbox size="12" property="dtVigenciaFinalD" dataType="JTDate" picker="false" disabled="true"/>
		</adsm:range>

		<adsm:lookup label="filialDestino" labelWidth="20%" dataType="text" size="3" maxLength="3" width="80%"
			     service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findLookupFilial" property="filialD" idProperty="idFilial"
			     onPopupSetValue="popFlD" onDataLoadCallBack="cbFlD"
				 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialD.pessoa.nmFantasia"/>
			<%--adsm:propertyMapping modelProperty="lastRegional.idRegional" relatedProperty="regionalD.idRegional" blankFill="false"/>
			<adsm:propertyMapping modelProperty="lastRegional.sgNmFull" relatedProperty="regionalD.nmRegional" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="regionalD.idRegional" modelProperty="regionalFiliais.regional.idRegional"/--%>
			<adsm:textbox dataType="text" property="filialD.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup>
		

		<adsm:lookup property="unidadeFederativaD"  idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findLookupUf" dataType="text"
					 labelWidth="20%" width="80%" label="ufDestino2" size="3" maxLength="2" 
					 onPopupSetValue="popUfD" onDataLoadCallBack="cbUfD"
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2">
			<adsm:propertyMapping relatedProperty="unidadeFederativaD.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox dataType="text" property="unidadeFederativaD.nmUnidadeFederativa" size="35" disabled="true" />
		</adsm:lookup>		


		<adsm:lookup label="filialReembarcadora" labelWidth="20%" dataType="text" size="3" maxLength="3" width="80%"
				     service="lms.municipios.emitirFluxoCargaEntreFiliaisAction.findLookupFilial" property="filialR" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
  		            <adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialR.pessoa.nmFantasia"/>
					<adsm:textbox dataType="text" property="filialR.pessoa.nmFantasia" size="35" disabled="true"/> 
		</adsm:lookup> 
		<adsm:combobox label="formatoRelatorio" labelWidth="20%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirFluxoCargaEntreFiliaisAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
   
	function validateTab() {
			if (validateTabScript(document.forms)) {
				if (
				     getElementValue("unidadeFederativaD.idUnidadeFederativa") != "" || 
				     getElementValue("unidadeFederativaO.idUnidadeFederativa") != "" ||
					 getElementValue("filialO.idFilial") != "" || 
					 getElementValue("filialD.idFilial") != "") 
					return true;
				else
					alert(i18NLabel.getLabel("LMS-00013")
					+ i18NLabel.getLabel("filialOrigem")+" "+i18NLabel.getLabel("ou")+" "  
					+ i18NLabel.getLabel("ufOrigem2") + " "+i18NLabel.getLabel("ou")+" "
					+ i18NLabel.getLabel("filialDestino")+ " "+i18NLabel.getLabel("ou")+" "
					+ i18NLabel.getLabel("ufDestino2")+ ".");
			}
			return false;			
	}

	function popUfD(data) {
		resetValue(document.getElementById("filialD.idFilial"));
		resetValue(document.getElementById("regionalD.idRegional"));
		resetValue("dtVigenciaInicialD");
		resetValue("dtVigenciaFinalD");
		return true;	
	}
	function cbUfD_cb(data) {
		var flag = unidadeFederativaD_sgUnidadeFederativa_exactMatch_cb(data);
		if (flag) {
			resetValue(document.getElementById("filialD.idFilial"));
			resetValue(document.getElementById("regionalD.idRegional"));
			resetValue("dtVigenciaInicialD");
			resetValue("dtVigenciaFinalD");
		}
		return flag;
	}
	
	function popUfO(data) {
		resetValue(document.getElementById("filialO.idFilial"));
		resetValue(document.getElementById("regionalO.idRegional"));
		resetValue("dtVigenciaInicialO");
		resetValue("dtVigenciaFinalO");
		return true;	
	}
	function cbUfO_cb(data) {
		var flag = unidadeFederativaO_sgUnidadeFederativa_exactMatch_cb(data);
		if (flag) {
			resetValue(document.getElementById("filialO.idFilial"));
			resetValue(document.getElementById("regionalO.idRegional"));
			resetValue("dtVigenciaInicialO");
			resetValue("dtVigenciaFinalO");
		}
		return flag;
	}

	function popFlD(data) {
		resetValue(document.getElementById("unidadeFederativaO.idUnidadeFederativa"));
		return true;	
	}
	function cbFlD_cb(data) {
		var flag = filialD_sgFilial_exactMatch_cb(data);
		if (flag)
			resetValue(document.getElementById("unidadeFederativaD.idUnidadeFederativa"));
		return flag;
	}
	
	function popFlO(data) {
		resetValue(document.getElementById("unidadeFederativaO.idUnidadeFederativa"));
		return true;	
	}
	function cbFlO_cb(data) {
		var flag = filialO_sgFilial_exactMatch_cb(data);
		if (flag)
			resetValue(document.getElementById("unidadeFederativaO.idUnidadeFederativa"));
		return flag;
	}
	
	function chgRgD(field) {
		resetValue(document.getElementById("unidadeFederativaD.idUnidadeFederativa"));
		comboboxChange({e:field});

		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaInicialD",setFormat(document.getElementById("dtVigenciaInicialO"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaFinalD",setFormat(document.getElementById("dtVigenciaFinalO"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaInicialD");
			resetValue("dtVigenciaFinalD");
		}
	}
	function chgRgO(field) {
		resetValue(document.getElementById("unidadeFederativaO.idUnidadeFederativa"));
		comboboxChange({e:field});

		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaInicialO",setFormat(document.getElementById("dtVigenciaInicialO"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaFinalO",setFormat(document.getElementById("dtVigenciaFinalO"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaInicialO");
			resetValue("dtVigenciaFinalO");
		}
	}
	
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		document.getElementById("unidadeFederativaD.sgUnidadeFederativa").serializable = "true";
		document.getElementById("unidadeFederativaO.sgUnidadeFederativa").serializable = "true";
		document.getElementById("filialO.sgFilial").serializable = "true";
		document.getElementById("filialD.sgFilial").serializable = "true";
		document.getElementById("filialR.sgFilial").serializable = "true";
		preencheFilial();
	}
	
	function preencheFilial() {
		var sdoUsuario = createServiceDataObject("lms.municipios.emitirFluxoCargaEntreFiliaisAction.findFilialUsuarioLogado",
			"preencheFilial",undefined);
		xmit({serviceDataObjects:[sdoUsuario]});
	}
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheFilial_cb(data, exception){
		if (exception == null){
			setElementValue("filialO.idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));
			setElementValue("filialO.sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));
			setElementValue("filialO.pessoa.nmFantasia",getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));	
			setElementValue("tpEmissao","D");
		}
	}
	
	function initWindow(obj) {
		if (obj.name == 'cleanButton_click')
			preencheFilial();
	}
	
//-->
</script>
