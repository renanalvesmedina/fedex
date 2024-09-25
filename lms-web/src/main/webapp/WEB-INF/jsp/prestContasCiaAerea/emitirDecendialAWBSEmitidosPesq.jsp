<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/prestContasCiaAerea/emitirDecendialAWBSEmitidos">
	
		<adsm:hidden property="tpEmpresa" serializable="false" value="C"/>

		<adsm:combobox label="companhiaAerea" 
					   autoLoad="false"
					   property="empresa.idEmpresa"
					   optionLabelProperty="pessoa.nmPessoa"
					   optionProperty="idEmpresa"
					   boxWidth="235"
					   labelWidth="18%"
					   width="32%"
					   service="lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction.findComboEmpresas"
					   required="true"
					   onchange="myCompanhiaAereaOnChange(this)"> 
			<adsm:propertyMapping relatedProperty="nmCompanhiaAerea" modelProperty="pessoa.nmPessoa"/>
		</adsm:combobox>			   
		<adsm:hidden property="nmCompanhiaAerea" serializable="true"/>
			
		<adsm:combobox label="tipoEmissao" 
					   domain="DM_OPCAO_IMPRESSAO_PC"
					   property="opcaoImpressao"   
					   required="true"
					   width="35%"
					   onchange="opcaoImpressaoOnChange(this)"/>
		<adsm:hidden property="dsOpcaoImpressao" serializable="true"/>
					   
		<adsm:lookup label="filialPrestadoraContas"
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 					  
					 size="3" 
					 maxLength="3" 
					 width="83%"
					 labelWidth="18%"					 
					 exactMatch="true"
		             onPopupSetValue="filialPopup" 
		             onDataLoadCallBack="filialDataLoad" 
		             onchange="return filialChange(this);">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="nmFilialPrestadoraContas"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial"/>			
			<adsm:textbox dataType="text" property="nmFilialPrestadoraContas" size="31" maxLength="30" disabled="true" serializable="true"/>			
		</adsm:lookup>			 
		<adsm:hidden property="siglaFilial" serializable="true"/>
		
		<adsm:textbox property="dtInicial" label="periodoVendas" labelWidth="18%" dataType="JTDate" disabled="true" size="10" width="15%"  onchange="return dtInicialOnChange();" required="true"/> 
		<adsm:label key="ate" width="4%" style="border:none;"/>
	   	<adsm:textbox property="dtFinal" dataType="JTDate" size="10" width="17%" disabled="true"/>  		
			
		<adsm:lookup label="remetenteAWB"					 
					 service="lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction.findLookupCliente" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 exactMatch="true" 
					 size="20"
					 width="83%"
					 labelWidth="18%"					 
					 maxLength="20"
					 popupLabel="pesquisarRemetenteAwb" 
					 action="/vendas/manterDadosIdentificacao"
					 disabled="true">
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacaoFormatado" formProperty="nrIdentificacao"/>
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="nmRemetente"/>
			<adsm:textbox dataType="text" property="nmRemetente" disabled="true" size="40"/>
		</adsm:lookup>	
		<adsm:hidden property="nrIdentificacao" serializable="true"/>
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" required="true" labelWidth="18%"/>

		<adsm:buttonBar>
			<adsm:button buttonType="reportButton" disabled="false" caption="visualizar" onclick="myOnClickButtonReport(this)"/>
			<adsm:button buttonType="resetButton" disabled="false" caption="limpar" onclick="myOnClickLimpar(this);"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script type="text/javascript">   

	function enablePeriodoVenda(){
		var documento = getCurrentDocument("dtInicial", null);
	    var objdtInicial = getElement("dtInicial", documento);
	    var objdtFinal = getElement("dtFinal", documento);
	
		resetValue("dtInicial");
		resetValue("dtFinal");
	    
		if(getElementValue("filial.idFilial") &&  getElementValue("empresa.idEmpresa")){
	        setDisabledCalendar(objdtInicial, false, documento);
	        setDisabledCalendar(objdtFinal, true, documento);
		} else {
			if(getElementValue("filial.idFilial")){
		        setDisabledCalendar(objdtInicial, true, documento);
		        setDisabledCalendar(objdtFinal, true, documento);
			} else {
		        setDisabledCalendar(objdtInicial, false, documento);
		        setDisabledCalendar(objdtFinal, false, documento);
		        objdtFinal.required = "true";
			}
		}
	}

	/**
	*  Funções que capturam a troca de filial 
	*/
	
	function changeFilial(){
		enablePeriodoVenda();
	}
	
	function filialPopup(data){
		if (data != undefined){
			setFilial_cb(data);
		}

		changeFilial();
		return true;
	}


	function filialChange(obj){
		var retorno = filial_sgFilialOnChangeHandler();
		
		changeFilial();
			
		return retorno;
	}
	
	function filialDataLoad_cb(data){
		filial_sgFilial_exactMatch_cb(data);
		changeFilial();				
	}

	/**
	*  Fim Funções que capturam a troca de filial 
	*/

	function myOnClickButtonReport(elemento){
	
		if( document.getElementById("opcaoImpressao").options[document.getElementById("opcaoImpressao").selectedIndex].value == 'R' && 
			getElementValue("cliente.idCliente") == "" ){
			document.getElementById("cliente.idCliente").required = 'true';
			setFocus(document.getElementById('cliente.pessoa.nrIdentificacao'));

			
		}
	
		reportButtonScript('lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction', 'openPdf', elemento.form);
		
	}

	/**
	* Criado para colocar o campo de remetente AWB como disabilitado e não obrigatório
	*/	
	function myOnClickLimpar(elemento){
	
		cleanButtonScript(elemento.document);
		setDisabled("cliente.idCliente",true);
		document.getElementById("cliente.idCliente").required = 'false';
		setTipoRelatorio();	
		resetPeriodoVendas();
		setFocusOnFirstFocusableField();
		enablePeriodoVenda();
		
	}

	/**
	* Acionado ao mudar os valores na combo de companhia aérea
	* Utilizada para setar o nome da companhia aérea num campo hiden para ester ser usado nos parâmetros de pesquisa do relatório
	*/
	function myCompanhiaAereaOnChange(elemento){
	
		comboboxChange({e:elemento});
	
		if( elemento.selectedIndex != 0 ){
			setElementValue("nmCompanhiaAerea",elemento.options[elemento.selectedIndex].text);		
		} else {		
	        resetValue("nmCompanhiaAerea");
		}
	
		enablePeriodoVenda();
	
	}
	
	/**
	* Método utilizado para que ao carregar os dados da tela a combo de Companhia aérea possa ser preenchida
	*
	*/
    function myPageLoadCallBack_cb(data,erro){
	
       onPageLoad_cb(data,erro);
       
       setTipoRelatorio();
      
       var tpEmpresa = getElementValue("tpEmpresa");
         
       var dados = new Array();
         
       setNestedBeanPropertyValue(dados, "empresa.tpEmpresa", tpEmpresa);
         
       var sdo = createServiceDataObject("lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction.findComboEmpresas",
                                         "empresa_idEmpresa",
                                         dados);
       xmit({serviceDataObjects:[sdo]});
      
       //carrega lookup com o usuario logado

		var sdo2 = createServiceDataObject("lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction.findFilialUsuarioLogado",
											"setFilial", 
											new Array());
	
		xmit({serviceDataObjects:[sdo2]});
      
    }
    
	// seta a filial
	function setFilial_cb(data, error) {
		
		if (data != null) {
			setElementValue("filial.idFilial", data.idFilial);
			setElementValue("filial.sgFilial", data.sgFilial);
			setElementValue("nmFilialPrestadoraContas", data.pessoa.nmFantasia);
		}
		enablePeriodoVenda();
	}

	
    function setTipoRelatorio(){
    	setElementValue("tpFormatoRelatorio","pdf");
    }
    
    /**
    * Método chamado ao modificar os dados de período de vendas    
    */  
	function periodoVendasOnChange(eThis){
		var documento = getCurrentDocument("dtInicial", null);
	    var obj = getElement("dtInicial", documento);
		

		if (eThis.selectedIndex == 0){
			resetPeriodoVendas();
		}else {
			setDisabled("dtInicial", false);
			
			//habilitando o calendário
    	   	setDisabledCalendar(obj, false, documento);
    	   	
    	   	if (getElementValue("dtInicial") != ""){
				dtInicialOnChange();
			}
		}
	}


	
	/**
	* Acionado ao modificar os dados do campo data inicial do período de vendas
	* Busca a data final de acordo com os dados do período de vendas e da data inicial
	*/
	function dtInicialOnChange(){
		
		if(!validate(document.getElementById("dtInicial"))){
			resetValue("dtInicial");
			document.getElementById("dtInicial").focus();
		}
		
		if(!getElementValue("filial.idFilial")){
			return true;
		}
		
		if (getElementValue("dtInicial") == ""){
			resetPeriodoVendas();
			return true;
		}
		
		var dtInicial = getElementValue("dtInicial");
		var data = new Array();
		setNestedBeanPropertyValue(data, "dtInicial", dtInicial);
		setNestedBeanPropertyValue(data, "empresa.idEmpresa", getElementValue("empresa.idEmpresa"));
		setNestedBeanPropertyValue(data, "filial.idFilial", getElementValue("filial.idFilial"));

		var sdo = createServiceDataObject("lms.prestcontasciaaerea.emitirDecendialAWBSEmitidosAction.findDataFinal",
			"dtInicial",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	* Método de retorno do onchange da data inicial do periódo de vendas
	* Seta a data final
	*/
	function dtInicial_cb(data, error){
		if (error != undefined){
			alert(''+error);
			return;
		}

		if (data == undefined) return;
		if (data._value == undefined) return;
		
		setElementValue("dtFinal", data._value);
		
		return true;
	}
	
	/**
	* Verifica se a o opção de impressão está setada para <code>Por remetente</code>
	* Caso esteja, habilita o campo 'Remetente do AWB' e torna o campo obrigatório, 
	* caso contrário desabilita o campo 'Remetente do AWB' e torna ele não obrigatório
	*/
	function opcaoImpressaoOnChange(elemento){
	
		var opcao = elemento.value;
		
		//Por Remetente
		if( opcao == "R" ){
		
			setDisabled("cliente.idCliente",false);
			document.getElementById("cliente.idCliente").required = 'true';						
		
		} else {
			
			setDisabled("cliente.idCliente",true);
			document.getElementById("cliente.idCliente").required = false;

			resetValue("cliente.idCliente");
			resetValue("cliente.pessoa.nrIdentificacao");
			resetValue("nmRemetente");
			resetValue("dsOpcaoImpressao");
		}
		
		if( elemento.selectedIndex != 0 ){
			setElementValue("dsOpcaoImpressao",elemento.options[elemento.selectedIndex].text);
		}
	
	}
	
		/** Função que limpa o periodo de vendas de desabilita o calendar
	*/	
	function resetPeriodoVendas(){
        resetValue("dtInicial");
		resetValue("dtFinal");
	}
	
	function retornaNada(){
		return;
	}
</script>
   
   
