<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirRelacaoRotas" service="lms.municipios.emitirRelacaoRotasAction" onPageLoadCallBack="pageLoadCustom">
	<adsm:form action="/municipios/emitirRelacaoRotas">
	
		 <adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
			<adsm:include key="filialOrigem"/>
			<adsm:include key="filialDestino"/>
			<adsm:include key="filialIntegranteRota"/>
			<adsm:include key="rotaViagem"/>
		</adsm:i18nLabels> 	
	
		<adsm:combobox property="rotaViagem.tpRota" label="tipoRota" labelWidth="17%" width="83%" domain="DM_TIPO_ROTA_VIAGEM" >
			<adsm:propertyMapping relatedProperty="tpRotaDescription" modelProperty="description" />
		</adsm:combobox>
		<adsm:hidden property="tpRotaDescription" />
			
		<adsm:hidden property="tpEmpresaFilial" value="M" serializable="false"/>

		<adsm:lookup property="filialOrigem" idProperty="idFilial" required="false"
				criteriaProperty="sgFilial" maxLength="3" service="lms.municipios.emitirRelacaoRotasAction.findLookupFilial" 
				dataType="text" label="filialOrigem" size="3" action="/municipios/manterFiliais"
				criteriaSerializable="true"
				labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="false" >
			<adsm:propertyMapping relatedProperty="filialOrigem.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="tpEmpresaFilial" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filialOrigem.nmFantasia" size="30" disabled="true" />
		</adsm:lookup>

		<adsm:lookup property="filialDestino" idProperty="idFilial" required="false" 
				criteriaProperty="sgFilial" maxLength="3" service="lms.municipios.emitirRelacaoRotasAction.findLookupFilial" 
				dataType="text" label="filialDestino" size="3" action="/municipios/manterFiliais"
				criteriaSerializable="true"
				labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="3" exactMatch="false" disabled="false" >
			<adsm:propertyMapping relatedProperty="filialDestino.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="tpEmpresaFilial" modelProperty="empresa.tpEmpresa"/>			
			<adsm:textbox dataType="text" property="filialDestino.nmFantasia" size="30" disabled="true" />	
		</adsm:lookup>        

		<adsm:lookup property="filialIntegrante" idProperty="idFilial" required="false"
				criteriaProperty="sgFilial" maxLength="3" service="lms.municipios.emitirRelacaoRotasAction.findLookupFilial" 
				dataType="text" label="filialIntegranteRota" size="3" action="/municipios/manterFiliais" 
				criteriaSerializable="true"
				labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="false" >
			<adsm:propertyMapping relatedProperty="filialIntegrante.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="tpEmpresaFilial" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filialIntegrante.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:lookup>

		<adsm:lookup service="lms.municipios.emitirRelacaoRotasAction.findLookupRotasViagem" dataType="integer" 
					 property="rotaIdaVolta" idProperty="idRotaIdaVolta" 
					 criteriaProperty="nrRota"
					 size="4" label="rotaViagem" 
					 maxLength="4" exactMatch="false"					 
					 labelWidth="17%" cellStyle="vertical-align:bottom;"
					 mask="0000" width="35%" criteriaSerializable="true"
					 action="/municipios/consultarRotas" cmd="idaVolta" disabled="false" >
			
			<adsm:propertyMapping relatedProperty="rotaViagem.dsRota" modelProperty="rota.dsRota"/>
			
			<adsm:propertyMapping criteriaProperty="rotaViagem.tpRota" modelProperty="tpRota"/>
			
			<adsm:propertyMapping criteriaProperty="filialOrigem.idFilial" modelProperty="filialOrigem.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filialOrigem.sgFilial" modelProperty="filialOrigem.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filialOrigem.nmFantasia" modelProperty="filialOrigem.nmFilial"/>

			<adsm:propertyMapping criteriaProperty="filialDestino.idFilial" modelProperty="filialDestino.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filialDestino.sgFilial" modelProperty="filialDestino.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filialDestino.nmFantasia" modelProperty="filialDestino.nmFilial"/>

			<adsm:propertyMapping criteriaProperty="filialIntegrante.idFilial" modelProperty="filialIntermediaria.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filialIntegrante.sgFilial" modelProperty="filialIntermediaria.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filialIntegrante.pessoa.nmFantasia" modelProperty="filialIntermediaria.nmFilial"/>
			
			<adsm:textbox dataType="text" property="rotaViagem.dsRota" size="30" cellStyle="vertical-align:bottom;" disabled="true" serializable="true"/>
			
		</adsm:lookup>				

		<adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" defaultValue="S" labelWidth="17%" width="83%" />

		<adsm:combobox property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO"
				label="formatoRelatorio" labelWidth="17%" width="83%"
				defaultValue="pdf" required="true"/>
		
		<adsm:combobox property="moedaPais.idMoedaPais" autoLoad="true"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo"
				service="lms.entrega.emitirEficienciaEntregasAction.findComboMoedaPais"
				label="converterParaMoeda" labelWidth="17%" width="83%" required="true" >
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.idMoeda" modelProperty="moeda.idMoeda" />
			<adsm:propertyMapping relatedProperty="moedaPais.pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo" />
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.dsSimbolo" modelProperty="moeda.dsSimbolo" />
		</adsm:combobox>
        <adsm:hidden property="moedaPais.moeda.idMoeda" />
        <adsm:hidden property="moedaPais.pais.idPais" />
        <adsm:hidden property="moedaPais.moeda.siglaSimbolo" />
        <adsm:hidden property="moedaPais.moeda.dsSimbolo" />
        
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.municipios.emitirRelacaoRotasAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--

	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		findInfoUsuarioLogado();
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateInfoUsuarioLogado();
		}
	}

	var infoUsuario = undefined;
	
	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.municipios.emitirRelacaoRotasAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		if (error != undefined) {
			alert(error);
		} else {
			infoUsuario = data;
			populateInfoUsuarioLogado();
		}
	}

	function populateInfoUsuarioLogado() {
		fillFormWithFormBeanData(document.forms[0].tabIndex, infoUsuario);
		comboboxChange({e:document.getElementById("moedaPais.idMoedaPais")});
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)){
			if (getElementValue("filialOrigem.idFilial") != "" ||
					getElementValue("filialDestino.idFilial") != "" ||
					getElementValue("filialIntegrante.idFilial") != "" || 
					getElementValue("rotaIdaVolta.idRotaIdaVolta") != "") {
				return true;	
			} else {
				alert(i18NLabel.getLabel("LMS-00013") + i18NLabel.getLabel("filialOrigem") + ', ' 
							+ i18NLabel.getLabel("filialDestino") + ', ' 
							+ i18NLabel.getLabel("filialIntegranteRota") + ', '
							+ i18NLabel.getLabel("rotaViagem") + ".");
				
			}
		}
		
		return false;
	}
	
//-->
</script>



