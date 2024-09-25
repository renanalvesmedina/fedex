<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.metricasRealizacoesAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form  action="/vol/metricasRealizacoes">
	
	<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
	<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
	<adsm:hidden property="tpAcesso" serializable="false" value="F"/>

    <adsm:lookup label="filial" width="85%" labelWidth="15%" 
				     property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.metricasMediaOcupacionalAction.findLookupFilialByUsuarioLogado"  
		             dataType="text"
		             size="3" 
		             maxLength="3" 
		             required="true" >
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

		<adsm:lookup label="grupoFrota" labelWidth="15%" width="85%"
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
		
		<adsm:range label="periodo" width="85%" labelWidth="15%" required="true">
			<adsm:textbox dataType="JTDate" property="dataInicial" />
			<adsm:textbox dataType="JTDate" property="dataFinal" />
		</adsm:range>
		
		
		<adsm:buttonBar>
			<adsm:button caption="visualizar" onclick="visualizarClick();" buttonType="reportButton" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
				
	</adsm:form>
	
</adsm:window>
<script>
   
//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	 function myOnPageLoadCallBack_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.vol.metricasRealizacoesAction.findDadosDefault","dataSession",data);
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
	* preenche a lookup filial com a filial do grupo frota escolhido na popup Manter Grupos Frota
	*/
	function buscaDadosFilial(data) {
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.pessoa.nmFantasia", data.nmFantasia);	
	}

	/**
	 * Preenche os campos relacionados com o usuario.
	 */
   function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
			setElementValue("dataInicial", setFormat(document.getElementById("dataInicial"), dataUsuario.dataPrimeiroDiaMes));
			setElementValue("dataFinal", setFormat(document.getElementById("dataFinal"), dataUsuario.dataAtual));
		}
	}
      
	
	function visualizarClick() {
	    var ret = validateTabScript(document.forms) 
	    if (ret == false) {
	       return false;
	    }
	    var dados = buildFormBeanFromForm(document.forms[0])
        dados.filial.sgFilial = getElementValue("filial.sgFilial")
        dados.filial.nmFantasia = getElementValue("filial.pessoa.nmFantasia")
        var sdo = createServiceDataObject("lms.vol.metricasRealizacoesAction.execute",
                                          "openPdf",
                                          dados);
        executeReportWindowed(sdo, 'pdf');
	}
</script>	