<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window
	service="lms.vendas.manterUsuariosResponsaveisClienteAction">

	<adsm:i18nLabels>
		<adsm:include key="LMS-00006"/>
		<adsm:include key="LMS-00008"/>
	</adsm:i18nLabels>	
	
	<adsm:form action="/vendas/manterUsuariosResponsaveisCliente"
		idProperty="idUsuarioResponsavel"
		service="lms.vendas.manterUsuariosResponsaveisClienteAction.findByIdDetalhado"
 		onDataLoadCallBack="formLoad">
	
		
		<!-- Lookup Clientes -->
<%-- 		<adsm:hidden property="cliente.idCliente" /> --%>
		<adsm:lookup dataType="text" property="cliente" idProperty="idCliente"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.vendas.manterUsuariosResponsaveisClienteAction.findLookupCliente"
			action="/vendas/manterDadosIdentificacao" exactMatch="true"
			onPopupSetValue ="lookupClienteSetValue" 
			onDataLoadCallBack = "lookupClienteDataLoadCallback" label="cliente" size="20"
			maxLength="20" required="true" serializable="true" labelWidth="17%"
			width="53%"
			onchange="return onChangeCliente()">
 			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa"
 				modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa"
				size="40" maxLength="40" disabled="true" serializable="true" />
		</adsm:lookup>

		<!-- Filial -->
		<adsm:textbox dataType="text" property="filialAntendenteComercial.sgFilial" label="filial"
			size="10" maxLength="10" disabled="true" labelWidth="5%" width="10%">
			<adsm:textbox dataType="text" property="filialAntendenteComercial.nmFilial" size="20"
				maxLength="20" disabled="true" width="15%" />
 		</adsm:textbox> 

		<!-- Lookup de usuarios responsaveis -->
		<adsm:lookup dataType="text" property="usuarioResponsavel"
			idProperty="idUsuario" criteriaProperty="nrMatricula"
			service="lms.vendas.manterUsuariosResponsaveisClienteAction.findLookupUsuarioFuncionario"
			action="/configuracoes/consultarFuncionariosView" exactMatch="true"
			label="usuarioResponsavel" size="20" maxLength="20" required="true"
			serializable="true" labelWidth="17%" width="83%">
			<adsm:propertyMapping relatedProperty="usuarioResponsavel.nmUsuario"
				modelProperty="nmUsuario" />
			<adsm:propertyMapping relatedProperty="usuarioResponsavel.idUsuario"
				modelProperty="idUsuario" />				
			<adsm:textbox dataType="text" property="usuarioResponsavel.nmUsuario"
				size="40" maxLength="40" serializable="true" />
		</adsm:lookup>

		<!-- Data Inicio período -->
		<adsm:range label="vigencia" labelWidth="17%" width="83%">
			<adsm:textbox dataType="JTDate"
				required="true"
				property="dtVigenciaInicial"
				onchange="return dtVigenciaInicial_change(this);" />
			<adsm:textbox dataType="JTDate"
				property="dtVigenciaFinal" 
				onchange="return dtVigenciaFinal_change(this);" />
		</adsm:range>


		<!-- Botões -->
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:button caption="limpar" id="btnLimpar" buttonType="reset" callbackProperty="return reloadPage();"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
	function initWindow(eventObj){
		if (eventObj.name.equals("tab_click") && document.getElementById("cliente.pessoa.nrIdentificacao").disabled){
			data = new Object();
			data.idCliente = getElementValue("cliente.idCliente")
			findFilialCliente(data);
		}
	}	
	
	function onChangeDataInicio() {
		setElementValue("dtVigenciaFinal", "");
	}
	
	function onChangeCliente(){
		var retorno = cliente_pessoa_nrIdentificacaoOnChangeHandler();
		var cliente = getElement("cliente.pessoa.nrIdentificacao");
		
		
		if (getElementValue("cliente.pessoa.nrIdentificacao") == "" || isElementChanged(cliente) ){
			setElementValue("filialAntendenteComercial.sgFilial", "");
			setElementValue("filialAntendenteComercial.nmFilial", "");
		}
		
		return retorno;	
	}
	
	function lookupClienteDataLoadCallback_cb(data,error){
		if (data != undefined && data.length > 0){
			findFilialCliente(data[0]);	
		}
		
		return lookupExactMatch({e:document.getElementById("cliente.idCliente"), data:data})
	}
	
	function lookupClienteSetValue(data){
		setElementValue("cliente.pessoa.nmPessoa", data.pessoa.nmPessoa);
		setElementValue("cliente.idCliente", data.idCliente);
		
		dataFilial = new Object();
		dataFilial.idCliente = data.idCliente;
		findFilialCliente(dataFilial);
	}
	
	function findFilialCliente(data){
		var dataNew = new Object();
		
		setNestedBeanPropertyValue(dataNew, "idCliente", data.idCliente);
		var sdo = createServiceDataObject("lms.vendas.manterUsuariosResponsaveisClienteAction.findFilialByCliente", 
		 		"findFilialCliente", dataNew);
		 	xmit({serviceDataObjects:[sdo]});
		 	
		setFocus("usuarioResponsavel.nrMatricula", true);		 	
	}
	
	function findFilialCliente_cb(data,error){
		if (error) {
			alert(error);
			setFocusOnFirstFocusableField();
			return false;
		}
		
		
		setElementValue("filialAntendenteComercial.sgFilial", data.filial.sgFilial);
		setElementValue("filialAntendenteComercial.nmFilial", data.filial.nmFilial);
		
	}
	
	function dtVigenciaInicial_change(obj) {
		if (obj.value != "") {
			var dtVigenciaInicial = stringToDate(obj.value, obj.mask);
			if (dtVigenciaInicial != "0") {
				var currentDate = new Date();
				var time1 = LZ(dtVigenciaInicial.getYear())+LZ(dtVigenciaInicial.getMonth())+LZ(dtVigenciaInicial.getDate());
				var time2 = LZ(currentDate.getFullYear())+LZ(currentDate.getMonth())+LZ(currentDate.getDate());
		
				if (time1 < time2) {
					alertI18nMessage("LMS-00006");
					obj.value = "";
					return false;
				}
			}
			
			var dtVigenciaFinal = getElement("dtVigenciaFinal");
			if(dtVigenciaFinal.value != "") {
				dtVigenciaFinal_change(dtVigenciaFinal);
			}
		}
		return true;
	}

	function dtVigenciaFinal_change(obj) {
		if (obj.value != "") {
			var dtVigenciaInicial = stringToDate(getElement("dtVigenciaInicial").value, 
					getElement("dtVigenciaInicial").mask);
			
			var dtVigenciaFinal = stringToDate(obj.value, obj.mask);
			if (dtVigenciaInicial != "0" && dtVigenciaFinal != "0") {
				var currentDate = new Date();
				var timeFinal = LZ(dtVigenciaFinal.getYear())+LZ(dtVigenciaFinal.getMonth())+LZ(dtVigenciaFinal.getDate());
				var timeInicial = LZ(dtVigenciaInicial.getYear())+LZ(dtVigenciaInicial.getMonth())+LZ(dtVigenciaInicial.getDate());
		
				if (timeFinal < timeInicial) {
					alertI18nMessage("LMS-00008");
					obj.value = "";
					return false;
				}
			}
		}
		return true;
	}	
	
	function formLoad_cb(data,error){
		if (error != undefined){
			alert(error);
		}
		setElementValue("cliente.idCliente", data.cliente.idCliente);
		onDataLoad_cb(data,error);
	}	
	
	function reloadPage(){
		if(document.getElementById("cliente.pessoa.nrIdentificacao").disabled){
			resetValue("cliente.pessoa.nrIdentificacao");
			resetValue("usuarioResponsavel.nmUsuario");
			resetValue("usuarioResponsavel.idUsuario");
			resetValue("usuarioResponsavel.nrMatricula");
			resetValue("dtVigenciaInicial");		
			resetValue("dtVigenciaFinal");
			setFocus("usuarioResponsavel.nrMatricula");
		} else {
			resetValue(this.document);	
			setFocus("cliente.pessoa.nrIdentificacao");
		}
		
		setDisabled("filialAntendenteComercial.sgFilial", true);
		setDisabled("filialAntendenteComercial.nmFilial", true);
	}
	
</script>