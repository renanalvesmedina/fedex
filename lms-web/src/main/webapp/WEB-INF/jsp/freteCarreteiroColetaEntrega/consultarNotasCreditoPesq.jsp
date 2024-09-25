<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarNotasCredito" onPageLoadCallBack="findFilialAcessoUsuario">
	<adsm:form action="/freteCarreteiroColetaEntrega/consultarNotasCredito">
	
	     <adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial" label="filial" 
	    			size="3" maxLength="3" width="70%" action="/municipios/manterFiliais" 
	    			service="lms.fretecarreteirocoletaentrega.consultarNotasCreditoAction.findLookupFilial" style="width:45px" labelWidth="30%" required="true" disabled="true">
                  <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
                  <adsm:propertyMapping relatedProperty="filialSigla" modelProperty="sgFilial"/>
        		  <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        		  <adsm:hidden property="filialSigla" serializable="true"/>
        </adsm:lookup>
        
        <adsm:lookup service="lms.fretecarreteirocoletaentrega.consultarNotasCreditoAction.findLookupProprietario" idProperty="idProprietario" 
                     property="proprietario" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
                     label="proprietario" size="21" maxLength="20" 
					 width="70%" labelWidth="30%" action="/contratacaoVeiculos/manterProprietarios" dataType="text" required="false">
	         <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	         <adsm:propertyMapping relatedProperty="proprietarioNrIdentificacao" modelProperty="pessoa.nrIdentificacao" />
	         <adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30" maxLength="60" disabled="true" />
	         <adsm:hidden property="proprietarioNrIdentificacao" serializable="true"/>
	    </adsm:lookup>


	    	    
    	<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" 
				idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrFrota"
				service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupMeioTransporte" 
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" 
				label="meioTransporte" labelWidth="30%" width="8%" size="8" maxLength="6"
				cellStyle="vertical-Align:bottom" picker="false" >
			
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte" modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />		
			<adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
			
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
	        <adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="true" />
	        
	        <adsm:hidden property="identificacaoMeioTransporte" serializable="true"/>
		    <adsm:hidden property="meioTransporteNrFrota" serializable="true"/>		
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2"
				idProperty="idMeioTransporte" criteriaProperty="meioTransporte.nrIdentificador"
				service="lms.fretecarreteiroviagem.manterRecibosAction.findLookupMeioTransporte"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo"
				serializable="false" 
				width="50%" size="20" maxLength="25" picker="true" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping criteriaProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="tpMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" inlineQuery="true" />					
			
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />		
			<adsm:hidden property="identificacaoMeioTransporte" serializable="true"/>
			<adsm:hidden property="meioTransporteNrFrota" serializable="true"/>		
		</adsm:lookup>
		<adsm:hidden property="tpMeioTransporte" value="R" serializable="false"/>
		<%--Lookup Meio de Transporte------------------------------------------------------------------------------------------------------------%>
		
		<adsm:combobox property="moedaPais.idMoedaPais" onDataLoadCallBack="loadMoedaPadrao" optionLabelProperty="moeda.siglaSimbolo" optionProperty="idMoedaPais" label="converterParaMoeda" service="lms.fretecarreteirocoletaentrega.consultarNotasCreditoAction.findMoedaPais" labelWidth="30%" width="70%" onchange="onChangeMoeda(this);" required="true">
			<adsm:propertyMapping relatedProperty="descricaoMoeda" modelProperty="moeda.siglaSimbolo"/>
			<adsm:propertyMapping relatedProperty="dsSimbolo" modelProperty="moeda.dsSimbolo"/>
			<adsm:propertyMapping relatedProperty="idMoedaDestino" modelProperty="moeda.idMoeda"/>
			<adsm:propertyMapping relatedProperty="idPaisDestino" modelProperty="pais.idPais"/>
		</adsm:combobox>
		
		<adsm:hidden property="descricaoMoeda" serializable="true"/>	
		<adsm:hidden property="idPaisDestino" serializable="true"/>
		<adsm:hidden property="idMoedaDestino" serializable="true"/>	
		<adsm:hidden property="dsSimbolo" serializable="true"/>
        
   		<adsm:range label="periodoEmissaoNotaCredito" labelWidth="30%">
			<adsm:textbox dataType="JTDate" property="dataEmissaoNotaCreditoInicial"  cellStyle="vertical:bottom" required="true" />
			<adsm:textbox dataType="JTDate" property="dataEmissaoNotaCreditoFinal"  cellStyle="vertical:bottom" required="true" />
		</adsm:range>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="30%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.fretecarreteirocoletaentrega.consultarNotasCreditoAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>
	function initWindow(eventObj){
		if(eventObj.name == 'cleanButton_click'){
			findFilialAcessoUsuario();
			loadMoedaPadrao();
		}	
	}

	function findFilialAcessoUsuario_cb(data,exception){
		onPageLoad_cb(data,exception);
		findFilialAcessoUsuario();
	}
	
	function findFilialAcessoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.fretecarreteirocoletaentrega.consultarNotasCreditoAction.findFilialUsuarioLogado", "setaInformacoesFilial", new Array()));
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
	
	function onChangeMoeda(elem) {
		comboboxChange({e:elem});
	}	
	
	function loadMoedaPadrao_cb(data){
		moedaPais_idMoedaPais_cb(data);
		loadMoedaPadrao();
	}

	function loadMoedaPadrao(){

		var data = new Array();
		
		var sdo = createServiceDataObject("lms.fretecarreteirocoletaentrega.consultarNotasCreditoAction.findMoedaUsuario",
				"setaMoeda",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function setaMoeda_cb(data){
		if (data != undefined) {
			setElementValue("moedaPais.idMoedaPais", data.idMoedaPais);
			setElementValue("idMoedaDestino", data.idMoeda);
			setElementValue("dsSimbolo", data.dsSimbolo);
			setElementValue("descricaoMoeda", data.siglaSimbolo);
			setElementValue("idPaisDestino", data.idPais);
		}
	}

		
</script>
