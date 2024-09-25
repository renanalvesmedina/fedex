<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.envioSMSAction" onPageLoadCallBack="myPageLoad">
	<adsm:form  action="/vol/envioSMS">
			<adsm:hidden property="tpAcesso" serializable="false" value="F"/>
            <adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		    <adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
			<adsm:lookup label="filial" labelWidth="15%" width="85%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.envioSMSAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             required="true"
		             minLengthForAutoPopUpSearch="3">
		    <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
				
		<adsm:lookup label="grupoFrota" labelWidth="15%" width="35%"
		        property="grupo"
		        idProperty="idGrupoFrota"
		        criteriaProperty="dsNome"
		        action="/vol/manterGruposFrotas"
		        service="lms.vol.envioSMSAction.findLookupGruposFrotas"
		        dataType="text"  
				size="20" 
				maxLength="20"
				exactMatch="false" 
				minLengthForAutoPopUpSearch="3"
				afterPopupSetValue="buscaDadosFilial">
				
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
     
    		<adsm:propertyMapping relatedProperty="filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false" />
     
		</adsm:lookup>
		
		<adsm:lookup label="meioTransporte" labelWidth="15%" width="35%" picker="false"
		             property="meioTransporte"
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
		             action="/contratacaoVeiculos/manterMeiosTransporte"
		             service="lms.vol.envioSMSAction.findLookupMeioTransporte" 
		             dataType="text"
		             size="8" 
		             maxLength="6"
		             exactMatch="true"
		             
		             >
		           <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			
		   <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador"
                       modelProperty="nrIdentificador" />
        
             <!--  Criteria por nrIdentificador -->        
             <adsm:lookup 
		             property="meioTransporte2"
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrIdentificador"
		             action="/contratacaoVeiculos/manterMeiosTransporte"
		             service="lms.vol.envioSMSAction.findLookupMeioTransporte" 
		             dataType="text"
		             size="30" 
		             maxLength="25"
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="5"
		             >
		             
		           <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			
		         <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota"
                        modelProperty="nrFrota" />
                 <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
                            modelProperty="idMeioTransporte" />      
                        
         	 </adsm:lookup>
        </adsm:lookup>
	
	    
	    <adsm:section caption="dadosEnvio" width="100%"/>
		<adsm:combobox property="tpEvento" label="evento" domain="DM_TP_EVENTO_CEL"
					   autoLoad="true" onlyActiveValues="true" width="35%"/>		
		
		<adsm:textarea label="mensagem" maxLength="250" property="mensagem" rows="2" columns="90" width="100%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="envioSMS"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid property="envioSMS" idProperty="idEquipamento" selectionMode="check"  onDataLoadCallBack="dataLoadCallBack"
			   rows="9" unique="true" onRowClick="myRowClick" service="lms.vol.envioSMSAction.findPaginatedMeioTransporte"
			   rowCountService="lms.vol.envioSMSAction.getRowCountMeioTransporte" >
		<adsm:gridColumn property="sgFilial" title="filial" width="80" />
		<adsm:gridColumn property="dsNome" title="grupo" width="300" />
        <adsm:gridColumn title="meioTransporte"  property="nrFrota" width="80" />
		<adsm:gridColumn title="" property="nrIdentificador" width="150" align="left" />
		<adsm:gridColumn property="dsNumero" title="fone" width="120" dataType="text" />
		
		<adsm:buttonBar> 
			<adsm:button caption="transmitir" id="btnTransmitir" disabled="false" onclick="transmitir();"/>
			<adsm:button caption="transmitirTodos" id="btnTransmitirAll" disabled="false" onclick="transmitir('all');"/>
		</adsm:buttonBar>
	</adsm:grid>
    <adsm:i18nLabels>
		<adsm:include key="LMS-41016"/>
		<adsm:include key="LMS-41008"/>
		<adsm:include key="LMS-41018"/>
	</adsm:i18nLabels>
</adsm:window>

<script>
	function dataLoadCallBack_cb(data, errorMesage){
		if( data.list == undefined) {
			setDisabled("btnTransmitir",true);
       		setDisabled("btnTransmitirAll",true); 
		}
	}


    function initWindow(eventObj) {
       setDisabled("btnTransmitir",true);
       setDisabled("btnTransmitirAll",true); 
       
       if (eventObj.name == "cleanButton_click") {
			fillDataUsuario();
			filial_sgFilialOnChangeHandler();	
			setFocus(document.getElementById("filial.sgFilial"));
		}
    }
    function myRowClick(data) {
	    var tabGroup = getTabGroup(this.document);
	    var tabHistorico = tabGroup.getTab("historico");
	    
	    setFormPropertyValue(tabHistorico.tabOwnerFrame, "idEquipamento",data);
	    
		tabGroup.selectTab("historico");
		return false;
	}
    
	function transmitir(tipo) {
		if (confirm(i18NLabel.getLabel("LMS-41016"))) {
		   var idsMap = envioSMSGridDef.getSelectedIds();
		   
		   if (idsMap.ids.length ==0 && tipo == undefined) {
		      alert(i18NLabel.getLabel("LMS-41008"));
		   } else { 
		     var lista = new Array()
             for (i = 0; i < idsMap.ids.length; i++) {
                var data = new Array();
                data = idsMap.ids[i];
			    lista.push(data);
	         }
	       
	         var formData = buildFormBeanFromForm(this.document.forms[0]);
	         var tela = new Array();
	     	 tela.push(formData);
			 
             var sdo = createServiceDataObject("lms.vol.envioSMSAction.executeEnvioSms","executeEnvioSms",{idEquipamentoList:lista,filtroTela:tela,tipo:tipo});
		     xmit({serviceDataObjects:[sdo]});
    	   }   
		}
		
	}
	
	function executeEnvioSms_cb(data,error) {
	   if (error != undefined) {
	     alert(error);
	   } else {
	     showMessage(i18NLabel.getLabel("LMS-41018"), null, true);
	   }
	}
	
	function buscaDadosFilial(data) {
	    setElementValue("filial.sgFilial",data.sgFilial);
	    var dados = new Array();
	    
		setNestedBeanPropertyValue(dados,"sgFilial",data.sgFilial);
		var sdo = createServiceDataObject ("lms.vol.envioSMSAction.findLookupFilial", "populaLookupFilial", dados);
        xmit({serviceDataObjects:[sdo]});
        return 
	}
	
	function populaLookupFilial_cb(data,error) {
	   if (error != undefined) {
	      alert(error)
	   } else {
	      setElementValue("filial.idFilial",getNestedBeanPropertyValue(data[0], "idFilial"));
		  setElementValue("filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data[0],"pessoa.nmFantasia"));
	   }
	}
	
	function myPageLoad_cb(data,error) {
		if (isLookup()) {
			onPageLoad_cb(data,error);
	    } else {
	    	var data = new Array();
			var sdo = createServiceDataObject("lms.vol.envioSMSAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
    		xmit({serviceDataObjects:[sdo]});
	    }
	}
	
	/**
	 * Carrega os dados de filial do usuario logado
	 */
	var dataUsuario;
	function loadFilialUsuario_cb(data, error) {
		dataUsuario = data;
		fillDataUsuario();
		onPageLoad_cb(data,error);
	}
	
	/**
	 * Faz o callBack do carregamento da pagina
	 */
	function loadPage_cb(data, error) {
		setDisabled("filial.idFilial", false);
		document.getElementById("filial.sgFilial").disabled=false;
		document.getElementById("filial.sgFilial").focus;
	}
	
	/**
	 * Retorna o parametro 'mode' que contem o modo em que a tela esta sendo utilizada.
	 * Caso mode seja igual a 'lookup' significa que a tela esta sendo aberta por uma lookup.
	 */
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}
	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
		}
	}
    
</script>