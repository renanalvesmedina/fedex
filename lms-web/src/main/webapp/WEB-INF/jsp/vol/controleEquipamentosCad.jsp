<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.controleEquipamentosAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vol/controleEquipamentos" idProperty="idRetiradaEqp">
        <adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/>
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
		             minLengthForAutoPopUpSearch="3">
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
        
      	<adsm:lookup label="etiquetaEquipamento" labelWidth="20%" width="80%"
		     property="volEquipamentos" 
		     idProperty="idEquipamento"
		     criteriaProperty="nmEtiqueta"
		     action="/vol/manterEquipamentos" 
		     service="lms.vol.controleEquipamentosAction.findLookupEquipamento" 
		     dataType="integer"  
			 size="10" 
			 maxLength="10"
			 exactMatch="true"
			 onDataLoadCallBack="trataEtiqueta"
			 afterPopupSetValue="trataEtiquetaAfterPopup">
			 <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			 <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
             <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
 		</adsm:lookup>
        
        <adsm:hidden   property="tpSituacao" value="A"  />
		<adsm:lookup label="meioTransporte" labelWidth="20%" width="80%" picker="false"
		             property="meioTransporte"
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
		             action="/contratacaoVeiculos/manterMeiosTransporte"
		             service="lms.vol.controleEquipamentosAction.findLookupMeioTransporte" 
		             dataType="text"
		             size="8" 
		             maxLength="6"
		             exactMatch="true"
		             required="true"
		             onDataLoadCallBack="trataMeioTransporte"
		             afterPopupSetValue="trataMeioTransporteAfterPopup">
		           <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
			       <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			       <adsm:propertyMapping criteriaProperty="tpSituacao"  formProperty="tpSituacao"/> 
			
		   <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" blankFill="false" />
        
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
		             onDataLoadCallBack="trataMeioTransporte"
		             afterPopupSetValue="trataMeioTransporteAfterPopup">
		             
		           <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			       <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			
		           <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota"  blankFill="false"/>
                   <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" blankFill="false" />      
                   <adsm:propertyMapping criteriaProperty="tpSituacao"  formProperty="tpSituacao"/> 
                        
         	 </adsm:lookup>
        </adsm:lookup>
		
		<adsm:hidden property="blTermoComp"/>
		<adsm:hidden property="tpSituacaoFuncionario" value="A"/>
			
		<adsm:lookup label="funcionario" labelWidth="20%" width="80%"
		      property="usuario" 
		      idProperty="idUsuario"
		      criteriaProperty="nrMatricula" 
		      action="/configuracoes/consultarFuncionariosView" 
		      service="lms.vol.controleEquipamentosAction.findLookupUsuarioFuncionario" 
		      dataType="integer"
		      size="20" 
		      maxLength="20"
		      exactMatch="true"
		      afterPopupSetValue="popupFuncionario"
			  onDataLoadCallBack="retornoFuncionario">
			  
			  <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			  <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			  <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			    
			 <adsm:propertyMapping criteriaProperty="tpSituacaoFuncionario" modelProperty="tpSituacaoFuncionario"/>
	         <adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario" />
	         
			 <adsm:textbox size="50" maxLength="50" dataType="text"  property="usuario.nmUsuario" disabled="true"/>
						  					  
		</adsm:lookup>
 
		<adsm:hidden property="pessoa.tpPessoa" value="F" />
      
		<adsm:lookup label="terceiro" dataType="text" size="20" maxLength="20" width="80%" labelWidth="20%"
					 idProperty="pessoa.idPessoa"
					 property="pessoa" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/carregamento/manterPrestadoresServico" 
					 service="lms.vol.controleEquipamentosAction.findLookupIntegrante" 
					 onDataLoadCallBack="retornoIntegrante"
					 onPopupSetValue="popupIntegrante">
			<adsm:propertyMapping modelProperty="pessoa.idPessoa" relatedProperty="pessoa.idPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" relatedProperty="pessoa.nrIdentificacao" />
			
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.tpPessoa" criteriaProperty="pessoa.tpPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" criteriaProperty="pessoa.nmPessoa" disable="false" />
			<adsm:textbox dataType="text" property="pessoa.nmPessoa" size="50" maxLength="50" disabled="true" />
			<adsm:hidden property="pessoa.nrIdentificacao" />
			<adsm:hidden property="pessoa.idPessoa" />
            <adsm:propertyMapping criteriaProperty="tpSituacao"  formProperty="tpSituacao"/> 
		</adsm:lookup>
		

		<adsm:buttonBar>
			<adsm:button caption="termoCompromisso" id="termoCompromisso" onclick="emitirTermoComp()"/>
			<adsm:button caption="retirar" buttonType="storeButton" onclick="retiraEquipamento()" />
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-41015"/>
		<adsm:include key="LMS-41019"/>
		<adsm:include key="LMS-41007"/>
		<adsm:include key="LMS-41045"/>
	</adsm:i18nLabels>

	
</adsm:window>
<script>
	function cancelClick() {
		return false;
	}
	
	
     /**
    * chama a função retornaBlTermoComp(data, error), passando como paramametro o id do funcionario
    * essa função retorna o status do blTermoComp e também as informações da filal do usuário para preencher os campos da filial 
	* no JSP, quando a matricula do funcionario é digitada no textBox
    */
    
    document.getElementById("usuario.nrMatricula").serializable = true;
    function retornoFuncionario_cb(data) {
	   var r = usuario_nrMatricula_exactMatch_cb(data);
	   if (r == true) {
	   	   if (data[0].tpsituacoes == 'A') {	
			  setElementValue("usuario.nmUsuario", data[0].nmUsuario);
			  retornaBlTermoComp(data[0].idUsuario, null, 'funcionario');   
			} else {
				alert(i18NLabel.getLabel("LMS-41045"));
				setElementValue("usuario.nmUsuario", '');
				setElementValue("usuario.nrMatricula", ''); 
				setFocus('usuario.nrMatricula',true,true);
			} 		 	  
	   }
	   return r;
    }
    
    /**
    * chama a função retornaBlTermoComp(data, error), passando como paramametro o id do funcionario
    * essa função retorna o status do blTermoComp e também as informações da filal do usuário para preencher os campos da filial 
	* no JSP quando o usuario é selecionado da lookpup "pesquisar funcionario"
    */
    function popupFuncionario(data){  
    	    retornaBlTermoComp(data.idUsuario, null, 'funcionario');
    }
    
    /**
    * retorna o status do blTermoComp do usuario e os dados da filial caso quem retire o equipamento seja funcionario
    */
    function retornaBlTermoComp(data, error, tipoPessoa){
    	if (error != undefined) {
			  alert(error);
			  return false;
	   }else{	  
	  		 dados = new Array();
	  		 if(tipoPessoa == 'funcionario'){
		  		 dados.idUsuario = data
		  	 }else{
		  	 	 dados.idTerceiro = data
		  	 }
	  		 var sdo = createServiceDataObject("lms.vol.controleEquipamentosAction.findUsuario","setaBlTermoComp",dados);
	   		 xmit({serviceDataObjects:[sdo]});
	   		 
  		     setDisabled("termoCompromisso", false);
  		     
	   }
    }
     /**
     * seta o campo "blTermoComp" e os campos da filial com o resultado da consulta da função retornaBlTermoComp(data, error)
     */
    function setaBlTermoComp_cb(data){
		setElementValue("blTermoComp", data[0].blTermoComp);
		if (data[0].idFilial == ""){
			setElementValue("filial.idFilial", data[0].idFilial);
			setElementValue("filial.sgFilial", data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia", data[0].nmFilial);
		}	
    }
    
    /**
    * verifica o valor do campo "blTermoComp" do usuario e emite o termo de compromisso
    * quando é clicado o botão Termo Compromisso 
    */
    function emitirTermoComp(data, error){
    	if (validateTabScript(this.document.forms[0])){
			if (error != undefined) {
				alert(error);
	            return false;
	 		}else {
		   	      var dataObject = new Object();
			      dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);
			      if((dataObject.formBean.usuario.nmUsuario == "") && (dataObject.formBean.pessoa.nmPessoa == "")){	
			   			alert(i18NLabel.getLabel("LMS-41007"));
			      }else {
	   			        if (dataObject.formBean.blTermoComp == "S"){
	  	 			   		  alert(i18NLabel.getLabel("LMS-41019")); 
	   			        }		
	 	 	      }
	 	 	      reportButtonScript("lms.vol.emitirTermoCompromissoAction", "openPdf", this.document.forms[0]); 				
	      }	
      }
     
  }
  
  /**
    * verifica o valor do campo "blTermoComp" do usuario e emite o termo de compromisso,
    * essa função é chamda no callBack do botao Retirar (função retira equipamento)
    */ 
   function emitirTermoCompStoreButton_cb(data, error){
  	  if (error != undefined) {
			  alert(error);
	  } else {
		   store_cb(data,error);
	   	   var dataObject = new Object();
		   dataObject = buildFormBeanFromForm(this.document.forms[0]);
   		   if (	dataObject.blTermoComp == "N" || dataObject.blTermoComp == ""){
	   		   	if (confirm(i18NLabel.getLabel("LMS-41015"))){
			   	   		reportButtonScript("lms.vol.emitirTermoCompromissoAction", "openPdf", this.document.forms[0]);
	   		   	}	  
   		   }  		
	  }
	  
	  //se a retirada for realizada com sucesso limpa os campos
	  if ( error == undefined ) {
		  	limpaCampos();
		  	setDisabled("termoCompromisso", true);
	        setFocus(document.getElementById("volEquipamentos.nmEtiqueta")); 		
	  } else {
	  		setFocus(document.getElementById("volEquipamentos.nmEtiqueta")); 		
	  }
	  
  }
    	
  	
  	/**
  	* Função que que realiza a retirada de equipamentos, é necessária pois, deve ser chamada a function
  	* emitirTermoComp() ao clicar no botão Retirar
  	*/
	function retiraEquipamento(){ 
		if (validateTabScript(this.document.forms[0])){
				var dataObject = new Object();
				dataObject = buildFormBeanFromForm(this.document.forms[0]);
				if( ( (dataObject.usuario.nmUsuario == "") && (dataObject.pessoa.nmPessoa == "") ) || ((dataObject.usuario.nmUsuario != "") && (dataObject.pessoa.nmPessoa != "")) ){
					   alert(i18NLabel.getLabel("LMS-41007"));
					   setFocus(document.getElementById("usuario.nrMatricula"));
				} else {
						var sdo = createServiceDataObject("lms.vol.controleEquipamentosAction.store", "emitirTermoCompStoreButton", dataObject);
			    		xmit({serviceDataObjects:[sdo]});
				}
		}
		
	}
	
	function limpaCampos() {
	    resetValue(this.document);
        fillDataUsuario();
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

		if (eventObj.name == "cleanButton_click" || eventObj.name == "tab_click" || eventObj.name == "newButton_click") {
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
	
	document.getElementById("pessoa.pessoa.nrIdentificacao").serializable = true;
    function retornoIntegrante_cb(data, error) { 
	   if (error != undefined) {
		  alert(error);
		  return false;
	   }
	   var r = pessoa_pessoa_nrIdentificacao_exactMatch_cb(data)
	   if (r == true) {
	   	  setElementValue("pessoa.idPessoa", data[0].pessoa.idPessoa);
	   	  setElementValue("pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
	   	  retornaBlTermoComp(data[0].pessoa.idPessoa, error, 'terceiro');
	   }  
    }

    function popupIntegrante(data) {
	   setElementValue("pessoa.nmPessoa", getNestedBeanPropertyValue(data,"nmPessoa"));
	   setElementValue("pessoa.idPessoa", getNestedBeanPropertyValue(data,"idPessoa"));
	   setElementValue("pessoa.nrIdentificacao", getNestedBeanPropertyValue(data,"nrIdentificacaoFormatado"));
	   retornaBlTermoComp(data.pessoa.idPessoa, null, 'terceiro');
    }
    
    /*
	* retorna o meio de transporte de acordo com a etiqueta
	*/
	function trataEtiqueta_cb(data,error){
	
		var r = volEquipamentos_nmEtiqueta_exactMatch_cb(data);
		if (r == true){
		   if (error != undefined) {
		      alert(error)
	   	   } else { 
	   	   if (data[0] != undefined) {	
	   	  	 if (getElementValue("meioTransporte2.nrIdentificador") == "") {
	             setElementValue("meioTransporte2.nrIdentificador", data[0].meioTransporte2.nrIdentificador);
		         setElementValue("meioTransporte.nrFrota", data[0].meioTransporte.nrFrota);
        	     setElementValue("meioTransporte.idMeioTransporte", data[0].meioTransporte.idMeioTransporte); 
         	 }
          }else {
            alert(lookup_noRecordFound);
          }    
      }    
	}		
	return r;
 }
	
	function trataEtiquetaAfterPopup(data){
		var nmEtiqueta = data.nmEtiqueta;     
        var dados = new Array();
        dados.nmEtiqueta = nmEtiqueta
    	var sdo = createServiceDataObject("lms.vol.controleEquipamentosAction.findLookupEquipamento","trataEtiqueta",dados);
		xmit({serviceDataObjects:[sdo]});
	}
	
	
	    
    /*
    * retorna o número da etiqueta de acordo com o meio de transporte (nrFrota ou nrIdentificador)
    */
    
    function trataMeioTransporte_cb(data, error) {
    
    	var r = meioTransporte_nrFrota_exactMatch_cb(data);
    	var r1 = meioTransporte2_nrIdentificador_exactMatch_cb(data);
		if ((r == true) || (r1 == true)){
    		if (error != undefined) {
			  alert(error);
	 	  }else{
	   			if (data[0] != undefined) {
    				setElementValue("meioTransporte.idMeioTransporte", data[0].idMeioTransporte);
    				setElementValue("meioTransporte.nrFrota", data[0].nrFrota);
    				setElementValue("meioTransporte2.nrIdentificador", data[0].nrIdentificador);
    			
    			 	var idMeioTransporte = data[0].idMeioTransporte;      
     		 	    var dados = new Array();
     		   		dados.idMeioTransporte = idMeioTransporte
    			    var sdo = createServiceDataObject("lms.vol.controleEquipamentosAction.findEquipamentoByIdMeioTransporte","findEquipamento",dados);
				    xmit({serviceDataObjects:[sdo]});
    			}else {
           		   alert(lookup_noRecordFound);
         		}  
    	}
    	return r1;
     }
  }
    
    function findEquipamento_cb(data, error) {
    	if (error != undefined) {
		  alert(error);
		  return false;
	   }else{
	   		if (data[0] != undefined) {
	   		   if (getElementValue("volEquipamentos.nmEtiqueta") == ""){
    			    	setElementValue("volEquipamentos.nmEtiqueta", data[0].nmEtiqueta);
    			    	setElementValue("volEquipamentos.idEquipamento", data[0].idEquipamento);
    			}
    		}
    	}
	}
	
	function trataMeioTransporteAfterPopup(data){
		var idMeioTransporte = data.idMeioTransporte;      
        var dados = new Array();
        dados.idMeioTransporte = idMeioTransporte
    	var sdo = createServiceDataObject("lms.vol.controleEquipamentosAction.findEquipamentoByIdMeioTransporte","findEquipamento",dados);
		xmit({serviceDataObjects:[sdo]});
	}
	
	
		
</script>