<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.manterGruposFrotasAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vol/manterGruposFrotas" idProperty="idGrupoFrota" 
	          newService="lms.vol.manterGruposFrotasAction.newMaster" onDataLoadCallBack="myOnDataLoad">
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:lookup label="filial" labelWidth="15%" width="85%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.manterGruposFrotasAction.findLookupFilialByUsuarioLogado" 
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

		<adsm:textbox property="dsNome" dataType="text" label="grupo" maxLength="30" size="35" width="35%" labelWidth="15%" required="true" />
		
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

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
			var sdo = createServiceDataObject("lms.vol.manterGruposFrotasAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
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
		//setDisabled("filial.idFilial", false);
		//document.getElementById("filial.sgFilial").disabled=false;
		//document.getElementById("filial.sgFilial").focus;
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
		
		if ((eventObj.name == "cleanButton_click") || (eventObj.name == "tab_click") || (eventObj.name == "newButton_click")) {
			fillDataUsuario();
			
			filial_sgFilialOnChangeHandler();	
			
			//setFocus(document.getElementById("filial.sgFilial"));
			
			/* habilita filial se ela veio desabilitada do click da grid*/
			//setDisabled("filial.idFilial",false);	     
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
	
	function limpaTela() {
		//cleanButtonScript(this.document);
		onPageLoad();
	}
	function desabilitaTab(blFlag) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("frotas",  blFlag);
		tabGroup.setDisabledTab("usuarios", blFlag);
	}
	
    /* Se grupo frota já possui frotas associadas ao grupo, não poderá ser alterado a filial  */
	function myOnDataLoad_cb(data, errorMessage, errorCode, eventObj){
	   onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	   
	   var sdo = createServiceDataObject("lms.vol.manterGruposFrotasAction.getRowCountGruposFrotaMeiosTransporte", "habilitarFilial", {idGrupoFrota:getElementValue("idGrupoFrota")});
       xmit({serviceDataObjects:[sdo]});
	
    }
	function habilitarFilial_cb(data,error) {
	  if (error == undefined) {
        //setDisabled("filial.idFilial",data._value > 0);	     
	  } else {
	    alert(error);
	  }
	}
</script>