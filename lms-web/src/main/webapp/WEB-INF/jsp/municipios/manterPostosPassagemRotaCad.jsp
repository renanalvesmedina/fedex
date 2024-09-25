<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<%--adsm:window service="lms.municipios.postoPassagemRotaColEntService"--%>
<adsm:window service="lms.municipios.manterPostosPassagemRotaAction">
	<adsm:form action="/municipios/manterPostosPassagemRota" idProperty="idPostoPassagemRotaColEnt" service="lms.municipios.manterPostosPassagemRotaAction.findByIdDetalhamento" onDataLoadCallBack="dataLoad">	

		<adsm:hidden property="tpEmpresaMercurioValue" value="M" />

		<adsm:lookup dataType="text" property="rotaColetaEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.municipios.manterPostosPassagemRotaAction.findLookupFilial" action="/municipios/manterFiliais" required="true" disabled="true"
    			size="3" maxLength="3" label="filial" labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="3" exactMatch="true" >
    		<adsm:propertyMapping criteriaProperty="tpEmpresaMercurioValue" modelProperty="empresa.tpEmpresa" />
         	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="rotaColetaEntrega.filial.pessoa.nmFantasia" size="32" disabled="true" serializable="false" required="false" />
	    </adsm:lookup>

		<adsm:lookup service="lms.municipios.manterPostosPassagemRotaAction.findLookupRotaColetaEntrega" property="rotaColetaEntrega" idProperty="idRotaColetaEntrega" criteriaProperty="nrRota" dataType="integer" labelWidth="17%" label="numeroRota" size="5" maxLength="5" width="33%" action="/municipios/manterRotaColetaEntrega" exactMatch="true" required="true">
        	<adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" />
			<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>			
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="rotaColetaEntrega.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false"/>			
        </adsm:lookup>

		<adsm:section caption="informacaoPostoPassagem"/>

	    <adsm:lookup dataType="text" property="postoPassagem"  idProperty="idPostoPassagem" criteriaProperty="tpPostoPassagem.description" onchange="return changeLookup();"
        		action="/municipios/manterPostosPassagem" service="lms.municipios.manterPostosPassagemRotaAction.findLookupByFormaCobranca" onPopupSetValue="popUpLookup"
        		size="30" minLengthForAutoPopUpSearch="3" onDataLoadCallBack="dataLoadLookup" disabled="true"
        		label="postoPassagem" labelWidth="17%" width="33%" required="true">
        	<adsm:propertyMapping relatedProperty="postoPassagem.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.tpPostoPassagem.description" modelProperty="tpPostoPassagem.description" />        	
        	<adsm:propertyMapping relatedProperty="postoPassagem.tpSentidoCobranca.description" modelProperty="tpSentidoCobranca.description" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.rodovia.sgRodovia" modelProperty="rodovia.sgRodovia" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.nrKm" modelProperty="nrKm" />
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" modelProperty="concessionaria.pessoa.nrIdentificacaoFormatado"/>
        	<adsm:propertyMapping relatedProperty="postoPassagem.concessionaria.pessoa.nmPessoa" modelProperty="concessionaria.pessoa.nmPessoa"/>        	
	    </adsm:lookup>

	    <adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado" size="18" label="concessionaria" labelWidth="17%"  disabled="true" width="15%" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.concessionaria.pessoa.nmPessoa" size="21" disabled="true" width="15%" serializable="false"/>
	    
		<adsm:textbox dataType="text" property="postoPassagem.municipio.nmMunicipio"         label="localizacao"     size="30" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.tpSentidoCobranca.description" label="sentido"         size="30" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.rodovia.sgRodovia"           label="rodovia"           size="30" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" property="postoPassagem.nrKm"                        label="km"                size="10" labelWidth="17%" width="33%" disabled="true" serializable="false"/>
		
		<adsm:listbox optionLabelProperty="text" optionProperty="" property="valor" serializable="false" size="7" boxWidth="575" labelWidth="17%" width="83%" label="valorPedagio"/>
		
		
		<adsm:range label="vigencia" labelWidth="17%" width="83%">
             <adsm:textbox dataType="JTDate" required="true" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
		<adsm:buttonBar>
				<adsm:button id="reportButton" caption="emitirPostosPassagemRota" action="/municipios/emitirPostosPassagemRotaColetaEntrega" cmd="main">
					<adsm:linkProperty src="rotaColetaEntrega.filial.idFilial" target="filial.idFilial"/>
					<adsm:linkProperty src="rotaColetaEntrega.filial.sgFilial" target="filial.sgFilial"/>
					<adsm:linkProperty src="rotaColetaEntrega.filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia"/>
					<adsm:linkProperty src="rotaColetaEntrega.idRotaColetaEntrega" target="rotaColetaEntrega.idRotaColetaEntrega"/>
					<adsm:linkProperty src="rotaColetaEntrega.nrRota" target="rotaColetaEntrega.nrRota"/>
					<adsm:linkProperty src="rotaColetaEntrega.dsRota" target="rotaColetaEntrega.dsRota"/>
				</adsm:button>	
					
				<adsm:storeButton callbackProperty="afterStore" service="lms.municipios.manterPostosPassagemRotaAction.storeMap" />				
				<adsm:newButton />
				<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
<script>

	function enabledFields() {
		//setDisabled(document,false);
		setDisabled("postoPassagem.municipio.nmMunicipio",true);
		setDisabled("rotaColetaEntrega.idRotaColetaEntrega",document.getElementById("rotaColetaEntrega.idRotaColetaEntrega").masterLink == "true");
		setDisabled("rotaColetaEntrega.dsRota",true);
		//setDisabled("rotaColetaEntrega.filial.idFilial",document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink == "true");
		setDisabled("rotaColetaEntrega.filial.pessoa.nmFantasia",true);
		setDisabled("postoPassagem.tpSentidoCobranca.description",true);
		setDisabled("postoPassagem.rodovia.sgRodovia",true);
		setDisabled("postoPassagem.nrKm",true);
		setDisabled("postoPassagem.concessionaria.pessoa.nrIdentificacaoFormatado",true);
		setDisabled("postoPassagem.concessionaria.pessoa.nmPessoa",true);
		setDisabled("postoPassagem.idPostoPassagem",false);
		setDisabled("valor",false);
				
		document.getElementById("postoPassagem.tpPostoPassagem.description").disabled = true;
		setFocusOnFirstFocusableField();
	}
	
	function populateFilial() {
		var tabGroup = getTabGroup(this.document);
		var tabPesq = tabGroup.getTab("pesq");
		setElementValue("rotaColetaEntrega.filial.idFilial",tabPesq.getFormProperty("idFilialSessao"));
		setElementValue("rotaColetaEntrega.filial.sgFilial",tabPesq.getFormProperty("sgFilialSessao"));
		setElementValue("rotaColetaEntrega.filial.pessoa.nmFantasia",tabPesq.getFormProperty("nmFilialSessao"));
	}
	
	function initWindow(eventObj) {	
		var blDetalhamento = (eventObj.name == "storeButton" || eventObj.name == "gridRow_click");
				
		if (!blDetalhamento) {
			if (!document.getElementById("rotaColetaEntrega.filial.idFilial").masterLink) {
				populateFilial();
			}
			
			enabledFields();
			setDisabled("removeButton",true);
			setDisabled("reportButton",true);		
		}
	}
	

	function dataLoad_cb(data,error) {
		enabledFields();
		onDataLoad_cb(data,error);
		setDisabled("valor",false);
		loadList();
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		validaAcaoVigencia(acaoVigenciaAtual, null);
		
		
	}
	
	function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
			  setDisabled("reportButton",false);
			  if(tipoEvento == "" ||  tipoEvento == null)
		     	setFocusOnFirstFocusableField(document);
     		  else
       		    setFocusOnNewButton(document);					      			  
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.storeButton",false);
		      setDisabled("__buttonBar:0.removeButton",true);
		      setDisabled("__buttonBar:0.newButton",false);
		      setDisabled("dtVigenciaFinal",false);
			  setDisabled("reportButton",false);				      
		      if(tipoEvento == "" ||  tipoEvento == null)
		     	setFocus(document.getElementById("dtVigenciaFinal"));
     		  else
       		    setFocusOnNewButton(document);
		} else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("__buttonBar:0.newButton",false);
			  setDisabled("reportButton",false);
              setFocusOnNewButton(document);
		}
	}

	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
    	if (exception == undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  			var store = "true";
  			validaAcaoVigencia(acaoVigenciaAtual, store);
		}
		
    }

	///DAQUI PRA BAIXO É SO PARA A LIST
		function loadList() {
			var sdo = createServiceDataObject("lms.municipios.manterPostosPassagemRotaAction.findValoresPostosPassagemList","resultLoad",{idPostoPassagem:getElementValue("postoPassagem.idPostoPassagem")});
			xmit({serviceDataObjects:[sdo]});										
		}
		function clearList() {
			for(x = document.getElementById("valor").options.length; x >= 0; x--)
				document.getElementById("valor").options[x] = null;
		}
		function resultLoad_cb(data,exception) {
			if (exception != undefined) {
				clearList();
				return;
			}
			clearList();
			for(x = 0; x < data.length; x++)
				document.getElementById("valor").options[document.getElementById("valor").options.length] = 
					new Option(data[x].text,"",true);
			//valor_cb(data,exception);
		}
		//change lookup
		function changeLookup() {
			if (getElementValue("postoPassagem.tpPostoPassagem.description") == "")
				clearList();
			return postoPassagem_tpPostoPassagem_descriptionOnChangeHandler();
		}
		//CallBack
		function dataLoadLookup_cb(data,exception) {
			var temp = getElementValue("postoPassagem.idPostoPassagem");
			var flag = postoPassagem_tpPostoPassagem_description_exactMatch_cb(data);
			if (getElementValue("postoPassagem.idPostoPassagem") != temp)
				loadList();
		}
		//PopUp
		function popUpLookup(data) {
			if (getElementValue("postoPassagem.idPostoPassagem") != data.idPostoPassagem) {
				setElementValue("postoPassagem.idPostoPassagem",data.idPostoPassagem);
				loadList();
			}
			return true;
		}
</script>
