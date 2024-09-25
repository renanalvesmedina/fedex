<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.gerencialAction" onPageLoadCallBack="myPageLoad">
	<adsm:form  action="/vol/gerencial">
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/>	
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:lookup label="filial" width="85%" labelWidth="15%" 
				     property="filial"
		             idProperty="idFilial" 
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.gerencialAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3" required="true" disabled="true">
		             
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
		        service="lms.vol.controleEquipamentosAction.findLookupGruposFrotas"
		        dataType="text"  
				size="20" 
				maxLength="20"
				exactMatch="false" 
				minLengthForAutoPopUpSearch="5"
				afterPopupSetValue="buscaDadosFilial">
				
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
     	
		</adsm:lookup>
		
		<adsm:combobox property="tipoFrota" label="situacaoFrota" labelWidth="15%" width="35%" domain="DM_SIT_FROTA"/>
		<adsm:combobox property="tipoEntrega"  label="entrega" labelWidth="15%" width="35%" domain="DM_SIT_ENTREGA"/>
		<adsm:combobox property="tipoColeta" label="coleta" labelWidth="15%" width="35%" domain="DM_SIT_COLETA"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" onclick="pesquisa();" buttonType="outros" disabled="false" />
			<adsm:button caption="acaoImediata" onclick="filtrosDefault();" buttonType="outros" disabled="false"/>
			<adsm:button caption="todos" onclick="clean();" buttonType="outros" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>						
	</adsm:form>		
	<adsm:form  action="/vol/gerencial">
		<adsm:label key="espacoBranco" width="12%" style="border:none;"/>
		<adsm:label key="legenda" width="80%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="espacoBranco" width="100%" style="border:none;"/>
		<adsm:label key="espacoBranco" width="12%" style="border:none;"/>
		<adsm:label key="legendaGerencialFrota" width="40%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>
		<adsm:label key="legendaGerencialColEnt" width="40%" style="border:none; vertical-align: text-top; margin-top: 4px; padding-top:3px"/>

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
			var sdo = createServiceDataObject("lms.vol.gerencialAction.findFilialUsuarioLogado", "loadFilialUsuario", data);
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
		
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("det", true);
	}

	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
			
			//LMS-3468
			if(dataUsuario.filial.sgFilial == "MTZ") {
				setDisabled('filial.idFilial',false);
			}
		}
	}
    
	function buscaDadosFilial(data) {
	    setElementValue("filial.sgFilial",data.sgFilial);
	    var dados = new Array();
	    
		setNestedBeanPropertyValue(dados,"sgFilial",data.sgFilial);
		var sdo = createServiceDataObject ("lms.vol.gerencialAction.findLookupFilialByEmpresa", "populaLookupFilial", dados);
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
	
	function pesquisa() {
	  if (validateTabScript(document.forms)) {
	     var tabGroup = getTabGroup(this.document);
	     tabGroup.selectTab('list', {name:'tab_click'});
	  }   
	}
	
	function clean() {
		setElementValue('tipoFrota','');		
		setElementValue('tipoEntrega','');		
		setElementValue('tipoColeta','');
		pesquisa();
	}
	function filtrosDefault() {
		setElementValue('tipoFrota','CHS');		
		setElementValue('tipoEntrega','');		
		setElementValue('tipoColeta','NR');
		pesquisa();
	}
	
	function tabHideFiltros() {
	  return validateTabScript(document.forms);
	}	
</script>