<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.vol.emitirMetricasAtividadesOperacionaisAction.findDataSession","dataSession",data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Carrega os dados de filial do usuario logado
	 */
	var dataUsuario;
	function dataSession_cb(data, error) {
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
	* preenche a lookup filial com a filial do grupo frota escolhido na popup manter grupos frota
	*/
	
	function buscaDadosFilial(data) {
		setElementValue("filial.idFilial",'');
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.nmFantasia);
		
		document.getElementById("filial.sgFilial").serializable = true;
		document.getElementById("filial.pessoa.nmFantasia").serializable = true;
	}

	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
			setElementValue("dtMesAno", setFormat(document.getElementById("dtMesAno"), dataUsuario.mesAno));
			
			document.getElementById("filial.sgFilial").serializable = true;
			document.getElementById("filial.pessoa.nmFantasia").serializable = true;
		}
	}
	
	
</script>
<adsm:window service="lms.vol.emitirMetricasAtividadesOperacionaisAction" onPageLoadCallBack="pageLoad">
	<adsm:form  action="/vol/metricasAtividadesOperacionais">	
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/>	
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		
		<adsm:lookup label="filial" width="85%" labelWidth="15%" 
				     property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.emitirMetricasAtividadesOperacionaisAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3" 
		             required="true">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        
        <adsm:lookup label="grupoFrota" labelWidth="15%" width="30%"
		        property="grupo"
		        idProperty="idGrupoFrota"
		        criteriaProperty="dsNome"
		        action="/vol/manterGruposFrotas"
		        service="lms.vol.emitirMetricasAtividadesOperacionaisAction.findLookupGrupo"
		        dataType="text"  
				size="20" 
				maxLength="20"
				exactMatch="false" 
				minLengthForAutoPopUpSearch="5"
				afterPopupSetValue="buscaDadosFilial">
				
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />

    		<adsm:propertyMapping relatedProperty="filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false" />
     
		</adsm:lookup>
            
	    <adsm:textbox label="competencia" 
					  dataType="monthYear" 
					  property="dtMesAno" 
					  required="true"
		/> 	
        <adsm:buttonBar>
			<adsm:reportViewerButton 
				caption="visualizar" 
				service="lms.vol.emitirMetricasAtividadesOperacionaisAction"
			/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
	</adsm:form>	
</adsm:window>
