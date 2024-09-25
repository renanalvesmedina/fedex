<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.metricasMediaOcupacionalAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form  action="/vol/metricasMediaOcupacional">
	<adsm:hidden property="tpAcesso" serializable="false" value="F"/>
	<adsm:lookup label="filial" width="8%" labelWidth="10%" 
			     property="filial"
	             idProperty="idFilial"
	             criteriaProperty="sgFilial"
	             action="/municipios/manterFiliais" 
	             service="lms.vol.metricasMediaOcupacionalAction.findLookupFilialByUsuarioLogado" 
	             dataType="text"
	             size="3" 
	             required="true"
	             maxLength="3">
       	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        <adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
        <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true" width="50%"/>
    </adsm:lookup>


		<adsm:range label="periodo" width="70%" labelWidth="10%" required="true">
			<adsm:textbox dataType="JTDate" property="dataInicial" />
			<adsm:textbox dataType="JTDate" property="dataFinal" />
		</adsm:range>
		
		
		<adsm:buttonBar >
			<adsm:button caption="visualizar" onclick="visualizarClick();" buttonType="reportButton" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
				
	</adsm:form>
	<adsm:i18nLabels>
		<adsm:include key="LMS-41020"/>
	</adsm:i18nLabels>
</adsm:window>
<script>
   //SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function myOnPageLoadCallBack_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.vol.metricasMediaOcupacionalAction.findDadosDefault","dataSession",data);
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
	    var objIni = document.getElementById("dataInicial");
	    var objFim = document.getElementById("dataFinal");
	    var dtIni = stringToDate(objIni.value,objIni.mask);
	    var dtFim = stringToDate(objFim.value,objFim.mask);
	    
	    if ((dtFim.getMonth() - dtIni.getMonth()) > 2) {
	      alert(i18NLabel.getLabel("LMS-41020"));
	      return false;
	    }
	    var dados = buildFormBeanFromForm(document.forms[0])
        dados.filial.sgFilial = getElementValue("filial.sgFilial") 
        var sdo = createServiceDataObject("lms.vol.metricasMediaOcupacionalAction.execute",
                                          "openPdf",
                                          dados);
        executeReportWindowed(sdo, 'pdf');
	}
	
</script>
