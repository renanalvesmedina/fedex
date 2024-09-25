<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRotas" service="lms.municipios.consultarRotasIdaVoltaAction" >
	<adsm:form action="/municipios/consultarRotas" idProperty="idRotaIdaVolta" >
        <adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="filialOrigem"/>
			<adsm:include key="filialDestino"/>
			<adsm:include key="filialIntegranteRota"/>
			<adsm:include key="numeroRota"/>
		</adsm:i18nLabels>
		
		
		<adsm:combobox property="regionalOrigem.idRegional" label="regionalOrigem" onchange="changeCombo(this,true)"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboRegional"
				optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="20%" width="30%" boxWidth="200" />

		<adsm:range label="vigencia" labelWidth="20%" width="30%">
			<adsm:textbox size="12" property="dtVigenciaInicialRO" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRO" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>
		
        <adsm:lookup property="filialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				dataType="text" size="3" maxLength="3" 
				labelWidth="20%"
				service="lms.municipios.consultarRotasViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialOrigem" width="35%"
				exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialOrigem.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialOrigem.nmFilial" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
		
		<adsm:combobox property="regionalDestino.idRegional" label="regionalDestino" onchange="changeCombo(this,false)"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboRegional"
				optionLabelProperty="siglaDescricao" optionProperty="idRegional" labelWidth="20%" width="30%" boxWidth="200" />

		<adsm:range label="vigencia" labelWidth="20%" width="30%">
			<adsm:textbox size="12" property="dtVigenciaInicialRD" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRD" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>
		
		<adsm:lookup property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" labelWidth="20%"
				dataType="text" size="3" maxLength="3" 
				service="lms.municipios.consultarRotasViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialDestino" width="35%"
				exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialDestino.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.nmFilial" disabled="true" serializable="false" size="30" />
		</adsm:lookup>

		<adsm:combobox property="tpRota" label="tipoRota" domain="DM_TIPO_ROTA_VIAGEM" labelWidth="20%" width="32%"/>

		<adsm:lookup dataType="text" property="proprietario" idProperty="idProprietario" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				service="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction.findLookupProprietario" width="82%" size="20" maxLength="20"
				action="/contratacaoVeiculos/manterProprietarios" criteriaProperty="pessoa.nrIdentificacao" label="proprietario" labelWidth="20%">
			<adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<%--
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			--%>
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" serializable="false" size="35" disabled="true"/>
		</adsm:lookup>

		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" boxWidth="180" cellStyle="vertical-align:bottom;" labelWidth="20%"
			optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte" label="tipoMeioTransporte" width="32%"
			service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findComboTipoMeioTransporte" onlyActiveValues="true">		
				<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="tpMeioTransporte.value"/>
		</adsm:combobox>
		<adsm:hidden property="tpMeioTransporte" serializable="false"/>

		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;" criteriaProperty="nrFrota"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupMeioTransporte" picker="false" maxLength="6" size="7"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" label="meioTransporte" labelWidth="20%" width="8%" serializable="false">
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte"/>		
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador" modelProperty="nrIdentificador"/>
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/>
			<%--
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			--%>
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value" inlineQuery="false"/>
		</adsm:lookup>   
		
		<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte" criteriaProperty="nrIdentificador" labelWidth="20%"
				service="lms.fretecarreteiroviagem.simularEfetivarReajustesTabelasFreteAction.findLookupMeioTransporte" picker="true" maxLength="25"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" width="24%" size="20" cellStyle="vertical-align:bottom;">
			<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota" modelProperty="nrFrota" />
			<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte" modelProperty="idMeioTransporte"/>	
			<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota" modelProperty="nrFrota"/>	
			<adsm:propertyMapping criteriaProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"/>
			<adsm:propertyMapping relatedProperty="tipoMeioTransporte.idTipoMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" blankFill="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario2.pessoa.nmPessoa" inlineQuery="false"/>
			<%--
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			--%>
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="tpMeioTransporte" modelProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value" inlineQuery="false"/>
		</adsm:lookup>

		
		<adsm:lookup property="filialIntermediaria" idProperty="idFilial" criteriaProperty="sgFilial" 
				dataType="text" size="3" maxLength="3" labelWidth="20%"
				service="lms.municipios.consultarRotasViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialIntegranteRota" width="85%"
				exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialIntermediaria.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialIntermediaria.nmFilial" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
        
        <%--adsm:lookup dataType="text" property="rota" idProperty="idRota" criteriaProperty="dsRota"
				service="lms.municipios.consultarRotasViagemAction.findRotaLookup"
				action="/municipios/manterPostosPassagemRotasViagem" cmd="list"
				label="rota" size="30" maxLength="120" width="35%" /--%> 
 
		<adsm:textbox dataType="integer" property="nrRota"
				label="numeroRota" size="3" labelWidth="20%" maxLength="4" width="35%" mask="0000"/>

  		<adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" labelWidth="20%" width="32%" />
  
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaIdaVolta"/>
			<adsm:resetButton/> 
		</adsm:buttonBar> 
	</adsm:form>
	<adsm:grid property="rotaIdaVolta" idProperty="idRotaIdaVolta" 
			service="lms.municipios.consultarRotasViagemAction.findPaginatedRotaIdaVolta"
			rowCountService="lms.municipios.consultarRotasViagemAction.getRowCountRotaIdaVolta"
			selectionMode="none" unique="true" rows="5" >
			
		<adsm:gridColumn title="numero" property="nrRota" dataType="integer" width="15%" mask="0000"/>
		<adsm:gridColumn title="rota" property="rota.dsRota" width="25%" />
		<adsm:gridColumn title="sentido" property="tpRotaIdaVolta" width="10%" isDomain="true" />
		<adsm:gridColumn title="horarioSaida" property="hrSaida" dataType="JTTime" width="15%"/>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="15%" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="15%" />
		
		<adsm:buttonBar>
			<adsm:removeButton />
			<%--adsm:button caption="fechar" onclick="self.close();" /--%>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("filialOrigem.idFilial") != "" ||
					getElementValue("filialDestino.idFilial") != "" ||
					getElementValue("filialIntermediaria.idFilial") != "" || 
					getElementValue("nrRota") != "") {
					return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013") + ' ' + i18NLabel.getLabel("filialOrigem") + ', ' 
							+ i18NLabel.getLabel("filialDestino") + ', ' 
							+ i18NLabel.getLabel("filialIntegranteRota") + ', '
							+ i18NLabel.getLabel("numeroRota") + ".");
				return false;
			}
		}else
			return false;
	}
	
	function changeCombo(field,isRegionalOrigem) {
		if (field != undefined)
			comboboxChange({e:field});
		else
			field = document.getElementById(((isRegionalOrigem) ? "regionalOrigem.idRegional" : "regionalDestino.idRegional"));
		var dtVigenciaI = document.getElementById("dtVigenciaInicialR" + ((isRegionalOrigem) ? "O" : "D"));
		var dtVigenciaF = document.getElementById("dtVigenciaFinalR" + ((isRegionalOrigem) ? "O" : "D"));
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue(dtVigenciaI,setFormat(dtVigenciaI,getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue(dtVigenciaF,setFormat(dtVigenciaF,getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue(dtVigenciaI);
			resetValue(dtVigenciaF);
		}		
	}
	
	function filialDChange() {
		var flag = filialDestino_sgFilialOnChangeHandler();
		changeCombo(undefined,false);
		return flag;
	}

	function filialDCallBack_cb(data) {
		filialDestino_sgFilial_exactMatch_cb(data);
		changeCombo(undefined,false);
	}
	function filialDPopUp(data) {
		setElementValue("regionalDestino.idRegional",getNestedBeanPropertyValue(data,"lastRegional.idRegional"));
		changeCombo(undefined,false);
		return true;
	}

	function filialOChange() {
		var flag = filialOrigem_sgFilialOnChangeHandler();
		changeCombo(undefined,true);
		return flag;
	}
	function filialOCallBack_cb(data) {
		filialOrigem_sgFilial_exactMatch_cb(data);
		changeCombo(undefined,true);
	}
	function filialOPopUp(data) {
		setElementValue("regionalOrigem.idRegional",getNestedBeanPropertyValue(data,"lastRegional.idRegional"));
		changeCombo(undefined,true);
		return true;
	}
	
</script>	