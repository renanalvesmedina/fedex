<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirLocalizacaoMeioTransporte" service="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction">
	<adsm:form action="/contratacaoVeiculos/emitirLocalizacaoMeioTransporte">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="regional"/>
			<adsm:include key="filial"/>
			<adsm:include key="meioTransporte"/>
			<adsm:include key="evento"/>
		</adsm:i18nLabels>
	
		<adsm:hidden property="filial.empresa.tpEmpresa" value="M" serializable="false"/>
		<adsm:combobox property="regional.id" label="regional" service="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction.findComboRegional"
					optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="17%" width="83%" boxWidth="200" onchange="changeCombo(this);">		
					<adsm:propertyMapping relatedProperty="regional.desc" modelProperty="siglaDescricao"/>
		</adsm:combobox>
		<adsm:hidden property="regional.desc"/>
		
		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox size="12" property="dtVigenciaInicial" dataType="JTDate" picker="false" disabled="true"/>
			<adsm:textbox size="12" property="dtVigenciaFinal" dataType="JTDate" picker="false" disabled="true"/>
		</adsm:range>
		<adsm:lookup service="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction.findLookupFilial" dataType="text"
				 property="filial" labelWidth="17%"  criteriaProperty="sgFilial" label="filial" size="3" maxLength="3"
				 width="80%" action="/municipios/manterFiliais" idProperty="idFilial">
	      	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
	      	<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
	       	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="40" disabled="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpVinculo.value" label="tipoVinculo" domain="DM_TIPO_VINCULO_VEICULO" labelWidth="17%" width="83%">
					<adsm:propertyMapping relatedProperty="tpVinculo.desc" modelProperty="description"/>
		</adsm:combobox>
 
		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte"
				service="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction.findLookupMeioTransp" picker="false" 
				label="meioTransporte" labelWidth="17%" width="82%" size="8" maxLength="6"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota">
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
		
		<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
				service="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction.findLookupMeioTransp"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrIdentificador"
				size="20" maxLength="25" picker="true">
			<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota"
					modelProperty="nrFrota" />
			<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
					modelProperty="idMeioTransporte" />	
			<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" 
					modelProperty="nrFrota" />	
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:combobox property="tpEvento.value" label="evento" domain="DM_EVENTO_MEIO_TRANSPORTE" labelWidth="17%" width="83%">
			<adsm:propertyMapping relatedProperty="tpEvento.desc" modelProperty="description"/>
		</adsm:combobox>
		<adsm:hidden property="tpEvento.desc"/>
		
		<adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio"
				labelWidth="17%" width="83%" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
				
		<adsm:hidden property="tpVinculo.desc"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contratacaoveiculos.emitirLocalizacaoMeioTransporteAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--
	document.getElementById("filial.sgFilial").serializable = "true";
	document.getElementById("meioTransporte2.nrFrota").serializable = "true";
	document.getElementById("meioTransporte.nrIdentificador").serializable = "true";
	
	function changeCombo(field) {
		comboboxChange({e:field});
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaInicial",setFormat(document.getElementById("dtVigenciaInicial"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaFinal",setFormat(document.getElementById("dtVigenciaFinal"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaInicial");
			resetValue("dtVigenciaFinal");
		}
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)){
			if (getElementValue("regional.id") != "" ||
					getElementValue("filial.idFilial") != "" ||
					getElementValue("meioTransporte.idMeioTransporte") != "" || 
					getElementValue("tpEvento.value") != "") {
				return true;	
			} else {
				alert(i18NLabel.getLabel("LMS-00013") + i18NLabel.getLabel("regional") + ', ' 
							+ i18NLabel.getLabel("filial") + ', ' 
							+ i18NLabel.getLabel("meioTransporte") + ', '
							+ i18NLabel.getLabel("evento") + ".");
				setFocusOnFirstFocusableField(document);
			}
		}
		
		return false;
	}
//-->
</script>
