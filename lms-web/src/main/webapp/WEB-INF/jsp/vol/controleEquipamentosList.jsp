 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.controleEquipamentosAction" onPageLoadCallBack="myPageLoad">
	<adsm:form  action="/vol/controleEquipamentos"  >
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/>

		<adsm:hidden property="nrIdentificacao" serializable="true" />
		<adsm:hidden property="nmPessoa" serializable="true" />

		<adsm:lookup label="filial" labelWidth="20%" width="80%"
		             property="filial" 
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.controleEquipamentosAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             required="true"
		             minLengthForAutoPopUpSearch="3" disabled="true">
		    <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
				
		<adsm:lookup label="grupoFrota" labelWidth="20%" width="30%"
		        property="grupo"
		        idProperty="idGrupoFrota"
		        criteriaProperty="dsNome"
		        action="/vol/manterGruposFrotas"
		        service="lms.vol.controleEquipamentosAction.findLookupGruposFrotas"
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
		             service="lms.vol.controleEquipamentosAction.findLookupMeioTransporte" 
		             dataType="text"
		             size="8" 
		             maxLength="6"
		             exactMatch="true"
		             
		             >
		           <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			
		   <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" />
        
             <!--  Criteria por nrIdentificador -->        
             <adsm:lookup 
		             property="meioTransporte2"
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrIdentificador"
		             action="/contratacaoVeiculos/manterMeiosTransporte"
		             service="lms.vol.controleEquipamentosAction.findLookupMeioTransporte" 
		             dataType="text"
		             size="30" 
		             maxLength="25"
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="5"
		             >
		             
		           <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			
		         <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota" />
                 <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />      
                        
         	 </adsm:lookup>
        </adsm:lookup>
				
        <adsm:range label="periodoRetirada" labelWidth="20%" width="30%" >
            <adsm:textbox dataType="JTDate" property="dhRetiradaInicial"  />
            <adsm:textbox dataType="JTDate" property="dhRetiradaFinal" />
        </adsm:range>
         
        <adsm:combobox property="tipoStatus" label="tipoStatRet"  labelWidth="15%" width="30%" 
					   domain="DM_STATUS_RET_EQP" required="false" 
					   autoLoad="true" onlyActiveValues="false" defaultValue="R">
	    </adsm:combobox>
	     
			  
        <adsm:hidden property="pessoa.tpPessoa" value="F" />
      
		<adsm:lookup label="motorista" dataType="text" size="20" maxLength="20" labelWidth="20%" width="75%"
					 idProperty="idMotorista"
					 property="motorista" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/contratacaoVeiculos/manterMotoristas" 
					 service="lms.vol.controleEquipamentosAction.findLookupMotorista" 
					 exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" >
			<adsm:propertyMapping modelProperty="pessoa.idPessoa" relatedProperty="pessoa.idPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" relatedProperty="pessoa.nrIdentificacao" />
			
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="motorista.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.tpPessoa" criteriaProperty="pessoa.tpPessoa" />
			<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" disabled="true" serializable="false" />
			<adsm:hidden property="pessoa.nrIdentificacao" />
			<adsm:hidden property="pessoa.idPessoa" />
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="equipamentos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-42043"></adsm:include>
		</adsm:i18nLabels>	
		
	</adsm:form>

	<adsm:grid property="equipamentos" idProperty="idRetiradaEqp" selectionMode="check" 
			   rows="10"  unique="true" scrollBars="horizontal" onRowClick="cancelClick" 
			   onSelectRow="trataEmitirTermoCompromisso" >
		<adsm:gridColumn property="sgFilial" title="filial" width="60" />
		<adsm:gridColumn property="dsNome" title="grupo" width="200" />
        <adsm:gridColumn title="meioTransporte"  property="nrFrota"  width="60" />
		<adsm:gridColumn title="" property="nrIdentificador" width="80" align="left" />
		<adsm:gridColumn property="dsModelo" title="modelo" width="200"/>
		<adsm:gridColumn property="dsNumero" title="numero" width="100" dataType="text"/>
		<adsm:gridColumn property="nrIdentificacao" align="right" title="motorista" width="100" />				
		<adsm:gridColumn property="nmPessoa" title="" width="200" />
		<adsm:gridColumn property="dhRetirada" title="retirada" dataType="JTDateTimeZone" width="150"/>
		<adsm:gridColumn property="dhDevolucao" title="devolucao" dataType="JTDateTimeZone" width="150" />		
	    <adsm:gridColumn property="assinouTermo" title="assinouTermo" width="100" renderMode="image-check" />						
	
		<adsm:buttonBar> 
			<adsm:button caption="termoCompromisso" id="termoCompromisso" onclick="emitirTermoComp()" />
			<adsm:button buttonType="removeButton" caption="devolucaoEquipamento" onclick="javascript:devolverEquipamento();" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>	


	function trataEmitirTermoCompromisso(){
		var ids = equipamentosGridDef.getSelectedIds().ids;
	
		if( ids.length > 1 ){
			alert(i18NLabel.getLabel('LMS-42043'));
			document.getElementById("termoCompromisso").disabled = true;
		}else {
			document.getElementById("termoCompromisso").disabled = false;
		}		 
	}

	function emitirTermoComp(rowRef){
		var gridFormElems = document.getElementById("equipamentos.form").elements;
	
		 var ids = equipamentosGridDef.getSelectedIds().ids;
		
		 for(var i = 0; i < ids.length;i++) {
			if (gridFormElems[i].type == "checkbox") {
				var myRow = equipamentosGridDef.getDataRowById(ids[i]);
				setElementValue("nrIdentificacao",myRow.nrIdentificacao);
				setElementValue("nmPessoa",myRow.nmPessoa);
				reportButtonScript("lms.vol.emitirTermoCompromissoAction", "openPdf", this.document.forms[0]);
		}
		}
	}



    function cancelClick() {
        return false;
	}

	
    function devolverEquipamento() {
       var mapIds = equipamentosGridDef.getSelectedIds();
	   if (mapIds.ids.length>0) { 
		  var sdo = createServiceDataObject("lms.vol.controleEquipamentosAction.storeDevolucao","devoverEquipamento",mapIds);
	    		xmit({serviceDataObjects:[sdo]});
		}
    }
    
    function devoverEquipamento_cb(data,error) {
       if (error != undefined)
          alert(error);
       findButtonScript('equipamentos', document.forms[0]);
    }
    
	function buscaDadosFilial(data) {
	    setElementValue("filial.sgFilial",data.sgFilial);
	    var dados = new Array();
	    
		setNestedBeanPropertyValue(dados,"sgFilial",data.sgFilial);
		var sdo = createServiceDataObject ("lms.vol.controleEquipamentosAction.findLookupFilial", "populaLookupFilial", dados);
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
	
    /**
	 * Verifica se algum parametro foi enviado para a tela.
	 * Caso a tenha sido enviado significa que a tela esta sendo usada como tela de consulta e sua grid
	 * estara com o click habilitado.
	 */
	function myPageLoad_cb(data,error) {
		if (isLookup()) {
			onPageLoad_cb(data,error);
	    } else {
	    	var data = new Array();
			var sdo = createServiceDataObject("lms.vol.controleEquipamentosAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
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

	function initWindow(eventObj) {
		
		if (eventObj.name == "cleanButton_click") {
			fillDataUsuario();
			
			filial_sgFilialOnChangeHandler();	
			
			setFocus(document.getElementById("filial.sgFilial"));
		}
		 
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