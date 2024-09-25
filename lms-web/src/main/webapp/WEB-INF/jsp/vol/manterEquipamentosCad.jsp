<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>		
		  
<adsm:window service="lms.vol.manterEquipamentosAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vol/manterEquipamentos" idProperty="idEquipamento">
		<adsm:hidden property="tpAcessoFilial" serializable="false" value="F"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:lookup label="filial" width="8%" labelWidth="15%" 
				     property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.manterEquipamentosAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3" required="true">
            <adsm:propertyMapping criteriaProperty="tpAcessoFilial" modelProperty="tpAcesso" /> 
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			
			
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true" width="50%"/>
        </adsm:lookup>

		
		<adsm:lookup label="meioTransporte" labelWidth="15%" width="80%" picker="false"
                      property="meioTransporte"
                      idProperty="idMeioTransporte"
                      criteriaProperty="nrFrota"
                      action="/contratacaoVeiculos/manterMeiosTransporte"
                      service="lms.vol.manterEquipamentosAction.findLookupMeioTransporte" 
                      dataType="text"
                      size="8" 
                      maxLength="6"
                      exactMatch="true"
                      required="false"
          >
              <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" /> 
              <!--  Criteria por nrIdentificador -->        
              <adsm:lookup 
                         property="meioTransporte2"
                         idProperty="idMeioTransporte"
                         criteriaProperty="nrIdentificador"
                         action="/contratacaoVeiculos/manterMeiosTransporte"
                         service="lms.vol.manterEquipamentosAction.findLookupMeioTransporte" 
                         dataType="text"
                         size="30" 
                         maxLength="25"
                         exactMatch="false"
                         serializable="false"
                         minLengthForAutoPopUpSearch="5"
              >
                  <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota" />
                  <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />      
             </adsm:lookup>
        </adsm:lookup>	
				
		<adsm:lookup service="lms.vol.manterEquipamentosAction.findLookupModelo" dataType="text" 
				property="volModeloseqp" idProperty="idModeloeqp"
				criteriaProperty="dsNome" label="modelo" size="20" maxLength="20" required="true"
				action="/vol/manterModelos" width="35%" exactMatch="true"/>

		<adsm:textbox property="dsNumero" label="numero" dataType="text" maxLength="15" size="16" width="35%" required="true" />				
		<adsm:textbox property="dsIccid" label="ICCID" dataType="text" maxLength="30" size="30" width="35%" required="true"/>				

		<adsm:textbox property="dsImei" label="IMEI" dataType="text" maxLength="30" size="30" width="35%" required="true"/>							
		<adsm:textbox property="nmPin" label="PIN" dataType="integer" maxLength="4" size="4" width="35%"/>				

		<adsm:textbox property="nmPin2" label="PIN2" dataType="integer" maxLength="4" size="4" width="35%"/>				
		
		<adsm:lookup service="lms.vol.manterEquipamentosAction.findLookupOperadora" dataType="text" property="volOperadorasTelefonia" idProperty="idOperadora"
				criteriaProperty="pessoa.nmPessoa" label="operadora" size="20" maxLength="30" required="true"
				action="/vol/manterOperadoras" width="35%" exactMatch="false" minLengthForAutoPopUpSearch="5" />

		<adsm:lookup service="lms.vol.manterEquipamentosAction.findLookupUso" dataType="text" property="volTiposUso" idProperty="idTiposUso"
				criteriaProperty="dsNome" label="uso" size="20" maxLength="20" required="true"
				action="/vol/manterTiposUso" width="35%" exactMatch="true"/>

					   
		<adsm:checkbox property="blHabilitado" label="habilitado" width="35%"/>
		<%--
		<adsm:checkbox property="homologado" label="homologado" width="35%"/>		
		--%>
		<adsm:combobox property="tpTarifa" label="tpTarifa" width="70%" domain="DM_TP_TARIFA"
						required="true" autoLoad="true" onlyActiveValues="false"/>				

		<adsm:textarea property="obObservacao" columns="50" rows="4" width="35%" maxLength="255" label="observacoes"/>

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">

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
			var sdo = createServiceDataObject("lms.vol.manterEquipamentosAction.findDataSession","loadFilialUsuario",data);
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
	

</script>
