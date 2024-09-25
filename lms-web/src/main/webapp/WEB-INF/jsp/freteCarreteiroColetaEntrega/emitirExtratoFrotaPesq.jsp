<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="emitirExtratoFrota" onPageLoadCallBack="findFilialAcessoUsuario">
	<adsm:form action="/freteCarreteiroColetaEntrega/emitirExtratoFrota" >
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-00013"/>
		</adsm:i18nLabels>
	
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" label="filial" 
	    		size="3" maxLength="3" width="71%" action="/municipios/manterFiliais" 
	    		service="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findLookupFilial" style="width:45px" labelWidth="25%">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="filialSigla" modelProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
			<adsm:hidden property="filialSigla" serializable="true"/>
        </adsm:lookup>
        
		                	
		<adsm:combobox required="true" property="tpSelecao" label="tipoSelecao" width="71%" domain="DM_TIPO_SELECAO_EXTRATO" labelWidth="25%" onchange="changeTpSelecao(this)"/>
		<adsm:hidden property="dsSelecao" serializable="true"/>
		
		<adsm:lookup service="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findLookupProprietario" idProperty="idProprietario" 
                     property="proprietario" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
                     label="proprietario" size="21" maxLength="20" 
					 width="71%" labelWidth="25%" action="/contratacaoVeiculos/manterProprietarios" dataType="text" required="false">
	         <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	         <adsm:propertyMapping relatedProperty="proprietarioNrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
	         <adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" maxLength="60" disabled="true"/>
	         <adsm:hidden property="proprietarioNrIdentificacao" serializable="true"/>
	    </adsm:lookup>
        
		<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" 
				idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrFrota"
				service="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findLookupMeioTransporteRodoviario" 
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" 
				label="meioTransporte" labelWidth="25%" width="75%" size="8" maxLength="6"
				cellStyle="vertical-Align:bottom" picker="false" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
					
			<adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />		
			
			
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
					
			 <adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
			 
		
		
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2"
				idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrIdentificador"
				service="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findLookupMeioTransporteRodoviario"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo"
				serializable="false" 
				size="20" maxLength="25" picker="true" cellStyle="vertical-Align:bottom">
				
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />		
			<adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
            
            <adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
			
		</adsm:lookup>
		</adsm:lookup>
		<adsm:hidden property="identificacaoMeioTransporte" serializable="true"/>
		<adsm:hidden property="meioTransporteNrFrota" serializable="true"/>		
		<adsm:hidden property="meioTransporteNrFrota" serializable="true"/>	
		<adsm:hidden property="identificacaoMeioTransporte" serializable="true"/>	
		
		<adsm:combobox onDataLoadCallBack="loadMoedaPadrao" required="true" property="moedaPais.idMoedaPais" optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" label="converterParaMoeda" service="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findMoedaPais" labelWidth="25%" width="75%" >
			<adsm:propertyMapping relatedProperty="descricaoMoeda" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="idMoedaDestino" modelProperty="moeda.idMoeda"/>
			<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="pais.idPais"/>
		</adsm:combobox>
		
		<adsm:hidden property="descricaoMoeda" serializable="true"/>	
		<adsm:hidden property="idPaisDestino" serializable="true"/>
		<adsm:hidden property="idMoedaDestino" serializable="true"/>	
		<adsm:hidden property="dsSimbolo" serializable="true"/>
        
		<adsm:range label="periodoEmissao" width="71%" labelWidth="25%" required="true" maxInterval="30">
             <adsm:textbox dataType="JTDate" property="periodoGeracaoInicial"/>
             <adsm:textbox dataType="JTDate" property="periodoGeracaoFinal"/>
        </adsm:range>
        
        <adsm:combobox label="formatoRelatorio" labelWidth="25%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
        
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>
function initWindow(eventObj){
		if(eventObj.name == 'cleanButton_click')
			findFilialAcessoUsuario();
			loadMoedaPadrao();
}

	function findFilialAcessoUsuario_cb(data,exception){
		onPageLoad_cb(data,exception);
		findFilialAcessoUsuario();
	}
	
	function findFilialAcessoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findFilialUsuarioLogado", "setaInformacoesFilial", ""));
	  	xmit();
	}
	
	function setaInformacoesFilial_cb(data, exception){
		if(data != undefined){
			setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filialSigla", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
			setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
		}
	}


function changeTpSelecao(field){
	comboboxChange({e:field});
		var e = document.getElementById("tpSelecao");
		if(e.value != ""){
			var dsSelecao = e.options[e.selectedIndex].text;
			setElementValue("dsSelecao", dsSelecao);
		}else if(e.value == "")
			setElementValue("dsSelecao", "");
}

function loadMoedaPadrao_cb(data){
		moedaPais_idMoedaPais_cb(data);
		loadMoedaPadrao();
	}

	function loadMoedaPadrao(){

		var data = new Array();
		
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.emitirExtratoFrotaAction.findMoedaUsuario",
				"setaMoeda",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function setaMoeda_cb(data){
		if (data != undefined) {
			setElementValue("moedaPais.idMoedaPais", data.idMoedaPais);
			setElementValue("idMoedaDestino", data.idMoeda);
			setElementValue("dsSimbolo", data.dsSimbolo);
			setElementValue("descricaoMoeda", data.siglaSimbolo);
			setElementValue("idPaisDestino", data.idPaisDestino);
		}
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)) {
			if (getElementValue("proprietario.idProprietario") != "" ||
					getElementValue("meioTransporteRodoviario.idMeioTransporte") != "") {
				return true;
			} else {
				alert(i18NLabel.getLabel("LMS-00013")
						+ document.getElementById("proprietario.idProprietario").label + ', '
						+ document.getElementById("meioTransporteRodoviario.idMeioTransporte").label + ".");
			}
		}
		return false;
	}
	
</script>