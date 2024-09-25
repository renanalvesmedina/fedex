<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.emitirOciosidadeRotaViagemAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/municipios/emitirOciosidadeRota" >
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
		
		<adsm:range label="periodoEmissaoCc" labelWidth="20%" width="78%" required="true" maxInterval="30" >
			<adsm:textbox dataType="JTDate" property="rotaViagem.dtVigenciaInicial"/> 
			<adsm:textbox dataType="JTDate" property="rotaViagem.dtVigenciaFinal"/>
		</adsm:range>
		
        <adsm:lookup property="filialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" criteriaSerializable="true"
				dataType="text" size="3" maxLength="3" 
				service="lms.municipios.emitirOciosidadeRotaViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialOrigem" width="32%" labelWidth="20%"
				exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialOrigem.nmFilial" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialOrigem.nmFilial" disabled="true" serializable="true" size="30" />
		</adsm:lookup>
		
		<adsm:lookup property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" criteriaSerializable="true"
				dataType="text" size="3" maxLength="3" 
				service="lms.municipios.emitirOciosidadeRotaViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialDestino" width="32%" labelWidth="20%"
				exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialDestino.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.nmFilial" disabled="true" serializable="true" size="30" />
		</adsm:lookup>
		
		<adsm:lookup property="filialIntermediaria" idProperty="idFilial" criteriaProperty="sgFilial" criteriaSerializable="true"
				dataType="text" size="3" maxLength="3" 
				service="lms.municipios.emitirOciosidadeRotaViagemAction.findLookupFilial" action="/municipios/manterFiliais"
				label="filialIntegranteRota" width="32%" labelWidth="20%"
				exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialIntermediaria.nmFilial" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialIntermediaria.nmFilial" disabled="true" serializable="true" size="30" />
		</adsm:lookup>		
		
		<adsm:lookup service="lms.municipios.emitirOciosidadeRotaViagemAction.findLookupRotasViagem" dataType="integer" 
					 property="rotaIdaVolta" idProperty="idRotaIdaVolta" 
					 criteriaProperty="nrRota"
					 size="4" label="rotaViagem" 
					 maxLength="4" exactMatch="false"					 
					 labelWidth="20%" cellStyle="vertical-align:bottom;"
					 mask="0000" width="35%"
					 action="/municipios/consultarRotas" cmd="idaVolta" disabled="false" >
			
			<adsm:propertyMapping relatedProperty="rotaViagem.dsRota" modelProperty="rota.dsRota"/>
			
			<adsm:propertyMapping criteriaProperty="filialOrigem.idFilial" modelProperty="filialOrigem.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filialOrigem.sgFilial" modelProperty="filialOrigem.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filialOrigem.nmFilial" modelProperty="filialOrigem.nmFilial"/>

			<adsm:propertyMapping criteriaProperty="filialDestino.idFilial" modelProperty="filialDestino.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filialDestino.sgFilial" modelProperty="filialDestino.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filialDestino.nmFilial" modelProperty="filialDestino.nmFilial"/>

			<adsm:propertyMapping criteriaProperty="filialIntermediaria.idFilial" modelProperty="filialIntermediaria.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filialIntermediaria.sgFilial" modelProperty="filialIntermediaria.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filialIntermediaria.nmFilial" modelProperty="filialIntermediaria.nmFilial"/>
			
			<adsm:textbox dataType="text" property="rotaViagem.dsRota" size="30" cellStyle="vertical-align:bottom;" disabled="true" serializable="true"/>
			
		</adsm:lookup>				
		
		<adsm:combobox property="rotaViagem.tpRota" label="tipoRota" labelWidth="20%" width="28%" domain="DM_TIPO_ROTA_VIAGEM"/>		
		
		<adsm:combobox property="moedaPais.idMoedaPais" autoLoad="true"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo"
				service="lms.municipios.emitirOciosidadeRotaViagemAction.findMoedaByPais"
				label="converterParaMoeda" labelWidth="20%" width="78%" boxWidth="85" required="true" > 
			<adsm:propertyMapping relatedProperty="moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="moeda.dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="idMoedaDestino" modelProperty="moeda.idMoeda"/>
			<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="pais.idPais"/>
		</adsm:combobox>
		<adsm:hidden property="moeda.siglaSimbolo"/>
		<adsm:hidden property="moeda.dsSimbolo"/> 
		<adsm:hidden property="idPaisDestino"/>
		<adsm:hidden property="idMoedaDestino"/> 
		
		<adsm:combobox label="formatoRelatorio" labelWidth="20%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirOciosidadeRotaViagemAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--
	
	var idMoedaPaisLogado;
	
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		carregaMoedaUsuLogado();
	}
	
	function carregaMoedaUsuLogado() {
		var sdo = createServiceDataObject("lms.municipios.emitirOciosidadeRotaViagemAction.findMoedaPadrao",
				"findMoedaPadrao",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findMoedaPadrao_cb(data,error) {
		idMoedaPaisLogado = getNestedBeanPropertyValue(data,"idMoedaPais");
		populateMoedaPais();
	}

	function populateMoedaPais() {
		setElementValue("moedaPais.idMoedaPais",idMoedaPaisLogado);
		comboboxChange({e:document.getElementById("moedaPais.idMoedaPais")});
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateMoedaPais();
		}
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("filialOrigem.idFilial") != "" ||
					getElementValue("filialDestino.idFilial") != "" ||
					getElementValue("filialIntermediaria.idFilial") != "" ||
					getElementValue("rotaIdaVolta.idRotaIdaVolta") != "") {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013")
						+ document.getElementById("filialOrigem.idFilial").label + ', ' 
						+ document.getElementById("filialDestino.idFilial").label + ', ' 
						+ document.getElementById("filialIntermediaria.idFilial").label + ', ' 
						+ document.getElementById("rotaIdaVolta.idRotaIdaVolta").label + '.');
			}
		}
		return false;
	}
	
//-->
</script>
 