<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.contaBancariaService" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/configuracoes/manterDadosBancariosPessoa" idProperty="idContaBancaria" onDataLoadCallBack="myOnDataLoad">
		<adsm:i18nLabels>
			<adsm:include key="LMS-27108"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="pessoa.idPessoa"/>
		<adsm:label key="branco" style="width:0"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>		
        <td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>
		<adsm:textbox dataType="text" property="pessoa.nrIdentificacao" size="20" maxLength="20" width="13%" disabled="true" serializable="false">	
			<adsm:textbox dataType="text" property="pessoa.nmPessoa" size="60" maxLength="60" width="74%" disabled="true" serializable="false"/>		
		</adsm:textbox>
		
		<adsm:hidden value="A" property="statusAtivo"/>
		
		<adsm:lookup dataType="integer" 
					 property="agenciaBancaria.banco" 
					 idProperty="idBanco"
					 service="lms.configuracoes.bancoService.findLookup" 
					 label="banco" size="5" maxLength="3"
					 criteriaProperty="nrBanco" 
					 onchange="return bancoChange(this);"
					 onPopupSetValue="resetValueAgencia"
					 action="/configuracoes/manterBancos" 					 
					 required="true">
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" inlineQuery="true"/>		 		 
			<adsm:propertyMapping modelProperty="nmBanco" formProperty="agenciaBancaria.banco.nmBanco"/> 
			<adsm:textbox property="agenciaBancaria.banco.nmBanco" dataType="text" size="25" maxLength="30" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup dataType="integer" 
					 property="agenciaBancaria" 
					 idProperty="idAgenciaBancaria"
					 service="lms.configuracoes.agenciaBancariaService.findLookup" 
					 onDataLoadCallBack="setaBanco"
					 onPopupSetValue="myOnPopUpSetValue"
					 label="agencia" maxLength="4" size="7" 
					 criteriaProperty="nrAgenciaBancaria" 
					 action="/configuracoes/manterAgencias" 
					 required="true">			
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" inlineQuery="true"/>		 		 					 
			<adsm:propertyMapping criteriaProperty="agenciaBancaria.banco.idBanco" modelProperty="banco.idBanco" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="agenciaBancaria.banco.nmBanco" modelProperty="banco.nmBanco" inlineQuery="false" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="agenciaBancaria.banco.nrBanco" modelProperty="banco.nrBanco" inlineQuery="false" addChangeListener="false"/>
			<adsm:propertyMapping modelProperty="nmAgenciaBancaria" formProperty="agenciaBancaria.nmAgenciaBancaria"/> 			
			<adsm:textbox property="agenciaBancaria.nmAgenciaBancaria" dataType="text" size="25" maxLength="30" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:complement label="numeroConta" width="35%">
			<adsm:textbox label="numeroConta" dataType="text" property="nrContaBancaria" maxLength="15" size="14" required="true" />
			<adsm:textbox dataType="text" property="dvContaBancaria" maxLength="2"  style="width:30px;" required="true"/>
		</adsm:complement>

		<adsm:combobox property="tpConta" label="tipoConta" domain="DM_TIPO_CONTA" required="true"/>
		
		<adsm:range label="vigencia" width="35%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:buttonBar>
			<adsm:button caption="salvar" buttonType="storeButton" id="storeButton" service="lms.configuracoes.manterDadosBancariosPessoaAction.store" callbackProperty="myStore"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function bancoChange(e){
		if (e.value == e.previousValue)
		   return true;
			resetValueAgencia();
		return agenciaBancaria_banco_nrBancoOnChangeHandler();
	}
	
	function resetValueAgencia(){
		resetValue(document.getElementById("agenciaBancaria.idAgenciaBancaria"));	
	}

	/**
	* Ao iniciar verifica se veio para esta aba através da seleção de um item da listagem
	* caso afirmativo desabilita alguns campos.
	*/
	function initWindow(eventObj) {
		if(eventObj.name == 'gridRow_click' || eventObj.name == 'storeButton'){
			setDisabled(getElement("__buttonBar:0.removeButton"), false);		
			if(eventObj.name == 'storeButton'){
				setFocus(document.getElementById("newButton"));
			} else {
				setFocusOnFirstFocusableField(document);
			}								
		} else {
			enableDisableElements(false);
			setFocusOnFirstFocusableField(document);
			findDataAtual();
		}
		
    }

	function enableDisableElements(value) {
		setDisabled("agenciaBancaria.banco.idBanco", value);
		setDisabled("agenciaBancaria.idAgenciaBancaria", value);
		setDisabled("nrContaBancaria", value);
		setDisabled("dvContaBancaria", value);
		setDisabled("tpConta", value);
		setDisabled("dtVigenciaInicial", value);
	}
	
	/**
	* Chama o método onPageLoad_cb padrão, após seta o label dinâmico de pessoa
	* @param data Dados carregados
	* @param erro Lista de erros ocorridos 
	*/
	function myOnPageLoad_cb(data, erro){
	      onPageLoad_cb(data,erro);
	      document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
	}	
	
	/**
	*	Seta dados do banco após a pesquisa da agência.
	*
	*/
	function setaBanco_cb(data,erro){
	
		var retorno = agenciaBancaria_nrAgenciaBancaria_exactMatch_cb(data);
		
		if( retorno == true ){
		
			if( data != undefined && data[0] != undefined && data[0].banco != undefined ){
				setElementValue("agenciaBancaria.banco.idBanco",data[0].banco.idBanco);
				setElementValue("agenciaBancaria.banco.nrBanco",data[0].banco.nrBanco);
				setElementValue("agenciaBancaria.banco.nmBanco",data[0].banco.nmBanco);
			}
		}
			
	}
	
	/**
	*	Seta dados do banco após a pesquisa da agência pela lookup
	*
	*/
	function myOnPopUpSetValue(data){
	
		if( data != undefined && data.banco != undefined ){
			setElementValue("agenciaBancaria.banco.idBanco",data.banco.idBanco);
			setElementValue("agenciaBancaria.banco.nrBanco",data.banco.nrBanco);
			setElementValue("agenciaBancaria.banco.nmBanco",data.banco.nmBanco);
		}	
	
	}
	
	function setaDados(data){
		resetValue(document);
		myOnDataLoad_cb(data);
	}
	
	function findDataAtual(){
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.configuracoes.manterDadosBancariosPessoaAction.findDataAtual", "setData")); 
		xmit(false);
	}
	
	function setData_cb(d,e,c){
		if (e == undefined){
			setElementValue("dtVigenciaInicial", setFormat("dtVigenciaInicial",d._value));
		}				
	}
	
	function myOnDataLoad_cb(data, error){
		onDataLoad_cb(data, error);
		//Se tem que desabilitar os campos por é um dado anterior a hoje
		enableDisableElements(data.blDisabled == "true");
		setFocusOnFirstFocusableField();
	}
	var isInsert = false;
	function myStore_cb(data, error, errorMsg, eventObj){
		isInsert = getElementValue("idContaBancaria") == "";
	
		store_cb(data, error);

		if (error == undefined){
			_serviceDataObjects = new Array();
			addServiceDataObject(createServiceDataObject("lms.contasreceber.contaBancariaService.findIsContaBancariaProprietarioOuPostoConveniado", "findIsContaBancariaProprietarioOuPostoConveniado",data )); 
			xmit(true);

			enableDisableElements(data.blDisabled == "true");
		}	
	}

	function findIsContaBancariaProprietarioOuPostoConveniado_cb(data, error, errorMsg, eventObj){
		if(data["_value"] == "true" && !isInsert){
			alert(i18NLabel.getLabel("LMS-27108"));
		}
	}

</script>