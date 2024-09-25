<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAuditoriasEspecificas" service="lms.portaria.manterAuditoriasEspecificasAction">

	<adsm:form action="/portaria/manterAuditoriasEspecificas" idProperty="idConfiguracaoAuditoriaFil" height="103" >

		<adsm:lookup property="filial" onchange="return resetFilial();" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3" service="lms.portaria.manterAuditoriasEspecificasAction.findLookupFilial" dataType="text" label="filial" size="3" action="/municipios/manterFiliais" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpOperacao" labelWidth="20%" label="tipoOperacao" domain="DM_TIPO_OPERACAO_AUDITORIA" width="80%" required="false" />
			
        <adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
				service="lms.portaria.manterAuditoriasEspecificasAction.findLookupMeioTransporteRodoviario" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="20%" width="8%" size="8" serializable="false" maxLength="6" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />				
		
		
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" cellStyle="vertical-align:bottom;"
					service="lms.portaria.manterAuditoriasEspecificasAction.findLookupMeioTransporteRodoviario" picker="true" maxLength="25"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					width="69%" size="20" required="false" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
			</adsm:lookup>
			
        </adsm:lookup>
        

		<adsm:lookup service="lms.portaria.manterAuditoriasEspecificasAction.findLookupRotasViagem" dataType="integer" 
					 property="rotaIdaVolta" idProperty="idRotaIdaVolta" 
					 criteriaProperty="nrRota"
					 size="4" label="rotaViagem" 
					 maxLength="4" exactMatch="false"					 
					 labelWidth="20%"
					 width="80%" mask="0000" cellStyle="vertical-align:bottom;"
					 action="/municipios/consultarRotas" cmd="idaVolta" disabled="false" >
			
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filialIntermediaria.idFilial" disable="false"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filialIntermediaria.sgFilial" inlineQuery="false" disable="false"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialIntermediaria.nmFilial" inlineQuery="false" disable="false"/>
			
			<adsm:propertyMapping relatedProperty="rotaViagem.dsRota" modelProperty="rota.dsRota"/>		
			
			<adsm:textbox dataType="text" property="rotaViagem.dsRota" size="30" cellStyle="vertical-align:bottom;" disabled="true" serializable="false"/>
			
			
		</adsm:lookup>
		
		<adsm:combobox label="rotaColetaEntrega" property="rotaColetaEntrega.idRotaColetaEntrega" optionLabelProperty="nrDsRota" onchange="changeComboRota(this)"
				optionProperty="idRotaColetaEntrega" service="lms.portaria.manterAuditoriasEspecificasAction.findComboRotaColetaEntrega" labelWidth="20%" width="80%" boxWidth="250">
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
		</adsm:combobox>
		
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox size="12" property="dtVigenciaInicialRota" dataType="JTDate" picker="false" disabled="true" serializable="false"/>
			<adsm:textbox size="12" property="dtVigenciaFinalRota"   dataType="JTDate" picker="false" disabled="true" serializable="false"/>
		</adsm:range>
		
		
		<adsm:range label="horarioAuditoria" labelWidth="20%" width="80%" required="false">
             <adsm:textbox dataType="JTTime" property="hrAuditoriaInicial" />
             <adsm:textbox dataType="JTTime" property="hrAuditoriaFinal"   />
        </adsm:range>

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
 
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="configuracaoAuditoriaFil"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
			
	</adsm:form>

	<adsm:grid property="configuracaoAuditoriaFil" idProperty="idConfiguracaoAuditoriaFil"  
	selectionMode="check" scrollBars="horizontal" unique="true" rows="8" gridHeight="170"  
	rowCountService="lms.portaria.manterAuditoriasEspecificasAction.getRowCountCustom">
	
		<adsm:gridColumn width="110" title="tipoOperacao" property="tpOperacao" />
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="65"/>
		<adsm:gridColumn title="" property="nrIdentificador" align="left" width="75" />
		<adsm:gridColumn title="rotaViagem" property="nrRota" width="60" align="right" mask="0000" dataType="integer"/>
		<adsm:gridColumn title="" property="dsRotaIdaVolta" width="175"/>
		<adsm:gridColumn title="sentido" property="tpRotaIdaVolta" width="60"/>
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="rotaColetaEntrega" property="nrRotaColetaEntrega" width="30" align="left"/>
			<adsm:gridColumn title="" property="dsSRotaColetaEntrega" width="170"/>
		</adsm:gridColumnGroup> 
		<adsm:gridColumn width="100" title="horarioInicial" property="hrAuditoriaInicial" align="center" dataType="JTTime" />
		<adsm:gridColumn width="100" title="horarioFinal" property="hrAuditoriaFinal" align="center" dataType="JTTime" />
		<adsm:gridColumn width="90" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" align="center"/>		
		<adsm:gridColumn width="90" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" align="center"/>
		<adsm:buttonBar>
			<adsm:removeButton caption="excluir" /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--

	function changeComboRota(field) {
		comboboxChange({e:field});
		if (field.selectedIndex > 0) {
			var data = field.data[field.selectedIndex - 1];
			setElementValue("dtVigenciaInicialRota",setFormat(document.getElementById("dtVigenciaInicialRota"),getNestedBeanPropertyValue(data,"dtVigenciaInicial")));
			setElementValue("dtVigenciaFinalRota",setFormat(document.getElementById("dtVigenciaFinalRota"),getNestedBeanPropertyValue(data,"dtVigenciaFinal")));
		}else{
			resetValue("dtVigenciaInicialRota");
			resetValue("dtVigenciaFinalRota");
		}
	}
	
	
	
	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;
	
	function initWindow(evt){
		if (evt.name == "cleanButton_click" || evt.name == "tab_load"){
			if (idFilialLogado != undefined && idFilialLogado != "") {
				setaValoresFilial();
			} else {
				getFilialUsuario();
			}
		}
	}
	
	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.portaria.manterAuditoriasEspecificasAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	function getFilialCallBack_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setaValoresFilial();
		}
	}	
	
	function setaValoresFilial() {
		setElementValue("filial.idFilial", idFilialLogado);
		setElementValue("filial.sgFilial", sgFilialLogado);
		setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);
		notifyElementListeners({e:document.getElementById("filial.idFilial")});
	}
	
	function resetFilial() {
		if (document.getElementById("filial.sgFilial").value == "" ) {
			setElementValue("filial.pessoa.nmFantasia","");
			setElementValue("filial.idFilial","");
		}
		return filial_sgFilialOnChangeHandler();
	}
 
//-->
</script>
