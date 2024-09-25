<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.portaria.manterOrdensSaidaAction" onPageLoadCallBack="ordensSaidaPageLoad">
	<adsm:form action="portaria/manterOrdensSaida" idProperty="idOrdemSaida" service="lms.portaria.manterOrdensSaidaAction.findById" onDataLoadCallBack="ordemSaidaDataLoad">
	
		<adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		
		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3" 
				service="lms.portaria.manterOrdensSaidaAction.findFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" width="80%" minLengthForAutoPopUpSearch="3" labelWidth="17%"
				exactMatch="false" style="width:45px" onchange="return filialOnchange(this)" required="true" disabled="true">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="30" disabled="true" />	
			<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		</adsm:lookup>
			
		<!-- Lookup de identificacao do meio-transporte -->
		<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdMeioTransporte2" idProperty="idMeioTransporte" labelWidth="17%"
					 service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="false" cellStyle="vertical-align=bottom;"
					 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
					 label="meioTransporte" width="33%" size="8" serializable="false" maxLength="6" 
					 onDataLoadCallBack="meioTransporte2DataLoad" 
					 onPopupSetValue="meioTransportePopup"
					 onchange="return meioTransporteOnChange(this)" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte"
								  modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />	
  			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
  			
  					<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdMeioTransporte" idProperty="idMeioTransporte" cellStyle="vertical-align=bottom;"
						         service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="true" maxLength="25"
								 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
								 size="20" required="true"
								 onDataLoadCallBack="meioTransporteDataLoad" 
								 onPopupSetValue="meioTransportePopup"
								 onchange="return meioTransportePlacaOnChange(this)">
							<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdMeioTransporte2.meioTransporte.nrFrota"
												  modelProperty="meioTransporte.nrFrota" />
							<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte2.idMeioTransporte"
												  modelProperty="idMeioTransporte" />	
							<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte2.meioTransporte.nrFrota"
												  modelProperty="meioTransporte.nrFrota" />		
						  <adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>		
          				  <adsm:propertyMapping criteriaProperty="setaTipoMeioTransporte"  modelProperty="setaTipoMeioTransporte" />	
                   </adsm:lookup>
                   <adsm:hidden property="setaTipoMeioTransporte" serializable="false" value="N"/>
		</adsm:lookup>
		
		 
		 <adsm:hidden property="tpSituacao" value="A" serializable="false"/>
 		<!-- FIM Lookup de identificacao do meio-transporte -->
				
		<!-- Lookup de identificacao do semi-reboque -->
		 <adsm:hidden property="idTipoMeioTransporte"  serializable="false"/>
		<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdSemiReboque2" idProperty="idMeioTransporte"
				service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="false" labelWidth="17%"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota" disabled="true"
				label="semiReboque" width="33%" size="8" serializable="false" maxLength="6" cellStyle="vertical-align=bottom;">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdSemiReboque.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
								  
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" 
		  						  modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			  
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte"
								  modelProperty="idMeioTransporte" />		
								  
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />	
  			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
  			
  					<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdSemiReboque" idProperty="idMeioTransporte" disabled="true"
								 service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="true" maxLength="25" cellStyle="vertical-align=bottom;"
								 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
								 size="20">
							<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdSemiReboque2.meioTransporte.nrFrota"
												  modelProperty="meioTransporte.nrFrota" />
				
							<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" 
						  						  modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
																  						  
							<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte"			
												  modelProperty="idMeioTransporte" />	
												  
							<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque2.meioTransporte.nrFrota"
												  modelProperty="meioTransporte.nrFrota" />		
						  <adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="meioTransporte.tpSituacao"/>		
						  						  	
				   </adsm:lookup>		
		</adsm:lookup>
		 		
 		<!-- FIM Lookup de identificacao do semi-reboque -->
		 
		 
		<adsm:lookup dataType="text" property="motorista" idProperty="idMotorista" criteriaProperty="pessoa.nrIdentificacao"
				service="lms.portaria.manterOrdensSaidaAction.findMotorista" labelWidth="17%" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				label="motorista" action="/contratacaoVeiculos/manterMotoristas" size="15" maxLength="20" width="80%" required="true"
				exactMatch="false" minLengthForAutoPopUpSearch="5">
        	<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
        	<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
        	<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" disabled="true"/>
        	<adsm:hidden property="motorista.showFilialUsuarioLogado" value="false"/>
        </adsm:lookup> 
        
		<adsm:complement width="90%" labelWidth="17%" label="responsavel" required="true">
             <adsm:textbox  dataType="text" property="usuario.nrMatricula" width="20%" size="10" disabled="true"/>
             <adsm:textbox  dataType="text"  property="usuario.nmUsuario" size="50" width="80%"  disabled="true"/>
        </adsm:complement>

		<adsm:hidden property="usuario.idUsuario"/>

		<adsm:textbox dataType="JTDateTimeZone" property="dhRegistro" label="dataRegistroLiberacao" labelWidth="17%" width="33%" cellStyle="vertical-align=bottom;" disabled="true"/>
		
		<adsm:textbox dataType="JTDateTimeZone" property="dhSaida" label="dataSaidaPortaria"  labelWidth="17%" width="33%" disabled="true" cellStyle="vertical-align=bottom;"/>
		
		<adsm:checkbox property="blSemRetorno" label="semRetorno" labelWidth="17%" cellStyle="vertical-align=bottom;"  width="33%"/>

		<adsm:textbox dataType="JTDateTimeZone" property="dhChegada" label="dataChegadaPortaria" size="22" disabled="true" width="33%" cellStyle="vertical-align=bottom;" labelWidth="17%"/>
				
		<adsm:textarea maxLength="500" property="obMotivo" label="motivo" rows="3" columns="101"  labelWidth="17%" width="83%" required="true"/>
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton id="botaoNovo"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	
	document.getElementById("setaTipoMeioTransporte").masterLink = "true";
	
	function meioTransporte2DataLoad_cb(data){
		var retorno = meioTransporteRodoviarioByIdMeioTransporte2_meioTransporte_nrFrota_exactMatch_cb(data);
		if (retorno != undefined && data != undefined){
			var idTipoMeioComposto = getNestedBeanPropertyValue(data,":0.idTipoMeioComposto");	
	 		setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", idTipoMeioComposto);
	 		habilitaDesabilitaSemiReboque(idTipoMeioComposto);
		}
		
		return retorno;
	}
	

				
	function meioTransporteDataLoad_cb(data){
		var retorno = meioTransporteRodoviarioByIdMeioTransporte_meioTransporte_nrIdentificador_exactMatch_cb(data);
		if (retorno != undefined && data != undefined){
			var idTipoMeioComposto = getNestedBeanPropertyValue(data,":0.idTipoMeioComposto");	
	 		setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", idTipoMeioComposto);
	 		habilitaDesabilitaSemiReboque(idTipoMeioComposto);
		}
		
		return retorno;
	}
	
	function meioTransportePopup(data){
		if (data != undefined){
			  var idTipoMeioComposto = getNestedBeanPropertyValue(data,"idTipoMeioComposto");	
   			  setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte", idTipoMeioComposto);
			  habilitaDesabilitaSemiReboque(idTipoMeioComposto);	
		}
		
		return true;
	}
	
	function habilitaDesabilitaSemiReboque(data){
	   	 if(data!= null){
	     	setDisabled(document.getElementById("meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte"), false);
	     	setDisabled(document.getElementById("meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte"), false);
	    }else { 
	    	setDisabled(document.getElementById("meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte"), true);	 	
	    	setDisabled(document.getElementById("meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte"), true);
	    }		
	}


	function meioTransporteOnChange(obj){
		var retorno = meioTransporteRodoviarioByIdMeioTransporte2_meioTransporte_nrFrotaOnChangeHandler();
		if (obj.value == "") {
			limpaSemiReboque();
			resetValue("setaTipoMeioTransporte");
		}
		return retorno;
	}

	function meioTransportePlacaOnChange(obj){
		var retorno = meioTransporteRodoviarioByIdMeioTransporte_meioTransporte_nrIdentificadorOnChangeHandler();
		if (obj.value == ""){
			limpaSemiReboque();
			resetValue("setaTipoMeioTransporte");
		}
		return retorno;
	}

	function limpaSemiReboque(){
		setElementValue("meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte", "");
		setElementValue("meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte", "");
		setElementValue("meioTransporteRodoviarioByIdSemiReboque2.meioTransporte.nrFrota", "");
		setElementValue("meioTransporteRodoviarioByIdSemiReboque.meioTransporte.nrIdentificador", "");
		setDisabled("meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte", true);
		setDisabled("meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte", true);
	}
	
	function validateTab(eventObj) {
	 
	    var valid = true;
	    // adicionar validações personalizadas
	    if (getElementValue("meioTransporteRodoviarioByIdMeioTransporte2.idMeioTransporte") == "") {
	        valid = false;
	        alert(getMessage(erRequired, new Array(document.getElementById("meioTransporteRodoviarioByIdMeioTransporte2.idMeioTransporte").label)));
	        setFocus("meioTransporteRodoviarioByIdMeioTransporte2.meioTransporte.nrFrota");
	    }
	 
	    // script padrao de validacao da tela
	    if (valid)
		    valid = validateTabScript(document.forms); 
	
	    return valid;
	 
	}
	
	function ordensSaidaPageLoad_cb(){
		onPageLoad_cb();
		document.getElementById("meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador").required = false;	
	}

	function initWindow(evt){
		if(evt.name != "gridRow_click" && evt.name != 'storeButton'){
			loadDadosSessao();
			estadoNovo();
			setFocusOnFirstFocusableField();
		}
	}
	
	function estadoNovo(){
		//setDisabled("filialByIdFilialOrigem.idFilial", false);
		setDisabled("meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte", false);
		setDisabled("meioTransporteRodoviarioByIdMeioTransporte2.idMeioTransporte", false);
		setDisabled("meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte", true);
		setDisabled("meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte", true);
		setDisabled("motorista.idMotorista", false);
		setDisabled("obMotivo", false);
		setDisabled("blSemRetorno", false);
	}
		
	//Chama o servico que retorna os dados do usuario logado 
	function loadDadosSessao(){

		var data = new Array();
		var sdo = createServiceDataObject("lms.portaria.manterOrdensSaidaAction.findDadosSessao",
					"preencheDadosSessao",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosSessao_cb(data, exception){
		if (exception == null){
			setElementValue("filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
			setElementValue("filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFantasia"));
			setElementValue("usuario.idUsuario",getNestedBeanPropertyValue(data,"idUsuario"));
			setElementValue("usuario.nrMatricula",getNestedBeanPropertyValue(data,"nrMatricula"));
			setElementValue("usuario.nmUsuario", getNestedBeanPropertyValue(data, "nome"));						
		}
	}
	
	function ordemSaidaDataLoad_cb(data, error){
		onDataLoad_cb(data, error);

		if (data != undefined) {
					
			var dhSaida = getNestedBeanPropertyValue(data,"dhSaida");
			if (dhSaida != undefined) {
				setDisabled(document, true);
				setDisabled("botaoNovo", false);
				setFocusOnNewButton();
			} else {
				estadoNovo();
				var idTipoMeioTransporte = getNestedBeanPropertyValue(data,"idTipoMeioTransporte");
				if (idTipoMeioTransporte != '') {
					setDisabled("meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte", false);
					setDisabled("meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte", false);
				}
				setFocusOnFirstFocusableField();
			}			
		}
	}
	
	function filialOnchange(obj){
		var retorno = filialByIdFilialOrigem_sgFilialOnChangeHandler();
		if (obj.value == '') {
			setElementValue("filialByIdFilialOrigem.idFilial", "");
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", "");
		}
		
		return retorno;
	}
	
</script>