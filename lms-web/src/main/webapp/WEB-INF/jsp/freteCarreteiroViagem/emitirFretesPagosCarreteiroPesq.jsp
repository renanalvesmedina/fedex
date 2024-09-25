<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction" onPageLoadCallBack="findFilialAcessoUsuario">
	<adsm:form action="/freteCarreteiroViagem/emitirFretesPagosCarreteiro">
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-24033"/>
		</adsm:i18nLabels>
		
		<adsm:lookup 
		dataType="text" 
		label="filial"
		service="lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction.findLookupFilial" 
		property="filial" 
		idProperty="idFilial" 
		criteriaProperty="sgFilial" serializable="true" action="/municipios/manterFiliais" maxLength="3" size="3" labelWidth="20%" width="80%" required="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox property="filial.pessoa.nmFantasia" dataType="text" disabled="true" size="30"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpCarreteiro" label="tipoCarreteiro" optionLabelProperty="description" optionProperty="value" service="lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction.findTipoCarreteiro" labelWidth="20%" width="80%" onchange="deletaProprietario(this);" />
		<adsm:hidden property="descricaoTpCarreteiro" serializable="true"/>
		
		<adsm:lookup 
		   onDataLoadCallBack="desabilitaTipoCarreteiro"
		   service="lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction.findLookupProprietario" 
		   dataType="text" 
		   property="proprietario" 
		   idProperty="idProprietario"
		   criteriaProperty="pessoa.nrIdentificacao" 
		   relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		   label="proprietario" size="30" maxLength="20" 
		   action="/contratacaoVeiculos/manterProprietarios" onPopupSetValue="retornoPopPupProprietario" labelWidth="20%" width="80%" onchange="return habilitaComboTipoCarreteiro(this);">
		   		  <adsm:propertyMapping criteriaProperty="tpCarreteiro" modelProperty="tpProprietario"/>
		   		  <adsm:propertyMapping relatedProperty="tpCarreteiro" modelProperty="tpProprietario.value"/>	
		   		  <adsm:propertyMapping relatedProperty="descricaoTpCarreteiro" modelProperty="tpProprietario.description"/>
                  <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
                  <adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="60" disabled="true" />
         </adsm:lookup>
        
		<adsm:combobox 
		      property="tpReciboFreteCarreteiro" 
		      label="situacaoRecibo" 
		      domain="DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE" 
		      labelWidth="20%" 
		      width="80%" 
		      onchange="setaDescricaoTpRecibo(this)"/>
		<adsm:hidden property="descricaoTpRecibo" serializable="true"/>
				
		<adsm:combobox 
		      onDataLoadCallBack="loadMoedaPadrao" 
		      property="moedaPais.idMoedaPais" 
		      required="true" 
		      optionLabelProperty="moeda.siglaSimbolo" 
		      optionProperty="idMoedaPais" 
		      label="converterParaMoeda" 
		      service="lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction.findMoedaPais" 
		      labelWidth="20%" 
		      width="80%" 
		      onchange="return setaDescricaoMoeda(this);">
		</adsm:combobox>
		
		<adsm:hidden property="descricaoMoeda" serializable="true"/>	
		<adsm:hidden property="idPaisDestino" serializable="true"/>
		<adsm:hidden property="idMoedaDestino" serializable="true"/>
		<adsm:hidden property="dsSimbolo" serializable="true"/>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="20%" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf" required="true"/>
		
		<adsm:range label="periodoEmissao" labelWidth="20%" width="80%" required="true" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="periodoEmissaoInicial"/> 
			<adsm:textbox dataType="JTDate" property="periodoEmissaoFinal"/>
		</adsm:range>
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

  
	document.getElementById("filial.sgFilial").serializable= true;
	document.getElementById("proprietario.pessoa.nrIdentificacao").serializable= true;
	
	function habilitaComboTipoCarreteiro(elem){
		setDisabled("tpCarreteiro", false);
		return proprietario_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function initWindow(eventObj){
		if(eventObj.name == 'cleanButton_click'){
			setDisabled("tpCarreteiro",false);
			findFilialAcessoUsuario();
			loadMoedaPadrao();
		}
		
	}
	
	function setaDescricaoTpRecibo(field){
		if(getElementValue("tpReciboFreteCarreteiro") != "" && getElementValue("tpReciboFreteCarreteiro") != null){
        	var descricao = document.getElementById("tpReciboFreteCarreteiro").options[document.getElementById("tpReciboFreteCarreteiro").selectedIndex].text;
        	setElementValue("descricaoTpRecibo",descricao);
		}
		
	}
	
	function setaDescricaoMoeda(field){
		
		if(getElementValue("moedaPais.idMoedaPais")!= "" && getElementValue("moedaPais.idMoedaPais")!= null){
			
			var descricao = document.getElementById("moedaPais.idMoedaPais").options[document.getElementById("moedaPais.idMoedaPais").selectedIndex].text;
			setElementValue("descricaoMoeda",descricao);
			
			var data = document.getElementById("moedaPais.idMoedaPais").data[document.getElementById("moedaPais.idMoedaPais").selectedIndex - 1];
	        var idPais = getNestedBeanPropertyValue(data,"pais.idPais");
	        var idMoeda = getNestedBeanPropertyValue(data,"moeda.idMoeda");
	        setElementValue("idPaisDestino",idPais);
	        setElementValue("idMoedaDestino",idMoeda);
       }
	}
	
	function deletaProprietario(field){
		setElementValue("proprietario.idProprietario","");
		setElementValue("proprietario.pessoa.nrIdentificacao","");
		setElementValue("proprietario.pessoa.nmPessoa","");
		//seta a descricao do dominio
		if(getElementValue("tpCarreteiro") != "" && getElementValue("tpCarreteiro") != null){
			var descricao = document.getElementById("tpCarreteiro").options[document.getElementById("tpCarreteiro").selectedIndex].text;
			setElementValue("descricaoTpCarreteiro",descricao);
		}
	}
	
	function findFilialAcessoUsuario_cb(data,exception){
		onPageLoad_cb(data,exception);
		findFilialAcessoUsuario();
	}
	
	function findFilialAcessoUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction.findFilialUsuarioLogado", "setaInformacoesFilial", new Array()));
	  	xmit();
	}
	
	function setaInformacoesFilial_cb(data, exception){
		if(data != undefined){
			setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
			setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
			setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
		}
	}
	
	function desabilitaTipoCarreteiro_cb(data){
		if (data != undefined ) {
			var tpProprietario = getNestedBeanPropertyValue(data, ":0.tpProprietario.value");
			if (validaTpCarreteiro(tpProprietario)) {
				resetValue("proprietario.pessoa.nrIdentificacao");
				setFocus("proprietario.pessoa.nrIdentificacao");
				return false;
			} else {
				setDisabled("tpCarreteiro",true);
				return proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
			}	
		} else {
			setDisabled("tpCarreteiro",false);
			return true;	
		}	
	}
	
	function loadMoedaPadrao_cb(data){
		moedaPais_idMoedaPais_cb(data);
		loadMoedaPadrao();
	}

	function loadMoedaPadrao(){

		var data = new Array();
		
		var sdo = createServiceDataObject("lms.fretecarreteiroviagem.emitirFretesPagosCarreteiroAction.findMoedaUsuario",
				"setaMoeda",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function setaMoeda_cb(data){
		if (data != undefined) {
			setElementValue("moedaPais.idMoedaPais", data.idMoedaPais);
			setElementValue("idMoedaDestino", data.idMoeda);
			setElementValue("idPaisDestino", data.idPais);
			setElementValue("dsSimbolo", data.dsSimbolo);
			setElementValue("descricaoMoeda", data.siglaSimbolo);
		}
	}
	
	function retornoPopPupProprietario(data,dialogWindow){	
		if (data != undefined) {
			var tpProprietario = getNestedBeanPropertyValue(data, "tpProprietario.value");
			var flag = validaTpCarreteiro(tpProprietario,dialogWindow);
			if(flag == true)
				setFocus(document.getElementById("proprietario.pessoa.nrIdentificacao"));
			else setDisabled("tpCarreteiro",true);	
			return (!flag);
		} else {
			setDisabled("tpCarreteiro",false)
			return true;
		}	
	}
	
	function validaTpCarreteiro(field, dialogWindow){
		if (field == 'P'){
			if(dialogWindow != undefined)
				dialogWindow.close();
			alert(i18NLabel.getLabel("LMS-24033"));
			return true;				
		} else return false;
	}
</script>