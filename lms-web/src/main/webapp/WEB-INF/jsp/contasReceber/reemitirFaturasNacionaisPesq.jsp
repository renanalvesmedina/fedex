<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window>

	<adsm:form action="/contasReceber/reemitirFaturasNacionais">

		<adsm:hidden property="nrIdentificacao" serializable="true"/>
		<adsm:lookup label="cliente"
					 service="lms.contasreceber.reemitirFaturasNacionaisAction.findLookupClientes" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 onDataLoadCallBack="verificaCliente"
					 size="20"
					 maxLength="20" 
					 width="80%"
					 serializable="true"
					 required="true"
					 labelWidth="20%"
					 onPopupSetValue="setValueCliente"
					 onchange="return trocaCliente()"
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" formProperty="nrIdentificacao" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="60" maxLength="60" />
		</adsm:lookup>
		
		<adsm:hidden property="filial.pessoa.nmFantasia"/>
		<adsm:hidden property="tpSituacaoFaturaValido" value="4"/>		
		
		<adsm:lookup label="fatura"
					 popupLabel="pesquisarFilial"
					 action="/municipios/manterFiliais" 
					 service="lms.contasreceber.reemitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filialByIdFilial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 picker="false"
					 size="3" 					 
					 maxLength="3" 
					 labelWidth="20%"
					 width="5%"
					 disabled="true"
					 onDataLoadCallBack="verificaFilial"
					 onchange="return trocaFilial()"
					 onPopupSetValue="liberaNrFatura"
					 exactMatch="true">

			<adsm:propertyMapping   relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			
			<adsm:lookup serializable="true"
						 popupLabel="pesquisarFatura"
   					 	 service="lms.contasreceber.reemitirFaturasNacionaisAction.findLookupFatura" 
   					 	 dataType="integer" 
	   					 property="fatura" 
   	 					 idProperty="idFatura"
   	 					 criteriaProperty="nrFatura" 
   	 					 onPopupSetValue="fatura_cb"
   						 required="true"
    					 size="20"
   						 maxLength="10"
   						 mask="0000000000"
   					 	 width="75%"
   					 	 disabled="true"
   					  	 action="/contasReceber/manterFaturas">
	            
	            <adsm:propertyMapping criteriaProperty="tpSituacaoFaturaValido" modelProperty="tpSituacaoFaturaValido" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.idFilial" modelProperty="filialByIdFilial.idFilial"/> 
   				<adsm:propertyMapping criteriaProperty="filialByIdFilial.sgFilial" modelProperty="filialByIdFilial.sgFilial" inlineQuery="true"/> 
   				<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filialByIdFilial.pessoa.nmFantasia" inlineQuery="true"/> 
   				
   				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nmPessoa" modelProperty="cliente.pessoa.nmPessoa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nrIdentificacao" modelProperty="cliente.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="cliente.idCliente" modelProperty="cliente.idCliente"/>

	         </adsm:lookup>
    
        </adsm:lookup>
        
    	<adsm:combobox  property="tpFormatoRelatorio" 
						required="true"
						label="formatoRelatorio" 
						defaultValue="pdf"
						domain="DM_FORMATO_RELATORIO"
						labelWidth="20%" width="80%"/>		

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.reemitirFaturasNacionaisAction" disabled="false"/>
			<adsm:button caption="limpar" disabled="false"  id="btnLimpar" onclick="limpaCampos()"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
	
	getElement("filialByIdFilial.sgFilial").required = "true";

	function fatura_cb(data, errors){
	 	if (getElementValue("nrIdentificacao") != "" ){
			setElementValue("filialByIdFilial.sgFilial", data.filialByIdFilial.sgFilial);
			setElementValue("filialByIdFilial.idFilial", data.filialByIdFilial.idFilial);
			setElementValue("filial.pessoa.nmFantasia", data.filialByIdFilial.pessoa.nmFantasia);
			setDisabled("fatura.nrFatura",false);
		}
	}
	
	
	
	
	
	/* limpa os campos da tela e desabilita a fatura */
	function  limpaCampos(){
		cleanButtonScript();
		desabilitaFatura();
	}

	/* Limpa e Desabilita os campos de fatura*/
	function desabilitaFatura(){
		resetValue("filialByIdFilial.idFilial");
		setDisabled("filialByIdFilial.idFilial",true);
		setDisabled("fatura.idFatura",true);
	
	}
	
	

	/* Executado ao entrar na tela */
	function initWindow(){
		setDisabled("btnLimpar", false);
	}
	
	/* chama o onChange padrao e desabilita fatura caso o cliente tenha sido deixado em branco*/
	function trocaCliente(){
		resetValue("cliente.pessoa.nmPessoa");
		desabilitaFatura();
		return lookupChange({e:document.getElementById("cliente.idCliente")});
	}

	/* Verifica se possui um cliente válido, se sim libera a fatura */
    function verificaCliente_cb(data, errors) {
    	var r = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if (r == true) { liberaFatura(); }
		else{ setFocus("cliente.pessoa.nrIdentificacao");  }
		return r;
    }
    
    
    
    
	/* chama o onChange padrao e desabilita nrFatura caso a filial tenha sido deixado em branco*/
	function trocaFilial(){


	
		setElementValue("fatura.idFatura","");
		setElementValue("fatura.nrFatura","");
		
	
		setDisabled("filialByIdFilial.idFilial",false);

		setDisabled("fatura.idFatura", false);
		setDisabled("fatura.nrFatura", true);

		var siglaFilial = getElement('filialByIdFilial.sgFilial');
		var siglaAnterior = siglaFilial.previousValue;
		
		var retorno = filialByIdFilial_sgFilialOnChangeHandler();		
		
		if( siglaAnterior != '' && siglaFilial.value == '' ){
			setFocus('fatura_lupa',false);		
		}

		return retorno;
			
	}

    
	/** De acordo com a ação libera ou protege o campo nrFatura
	*/
	function liberaNrFatura(){
	
		if (getElementValue("filialByIdFilial.sgFilial") != "" ){
			setDisabled("fatura.nrFatura",false);	
			setFocus("fatura.nrFatura");	
		}else{
			setDisabled("fatura.nrFatura",true);
			resetValue( getElement("fatura.idFatura") );
			setFocus("filialByIdFilial.sgFilial");
		}
	}
	
	
	
	function setValueCliente(){
		resetValue( getElement("filialByIdFilial.idFilial") );
		liberaFatura();
	}
	
	
	/* Verifica se possui uma filial válido, se sim libera a NrFatura */
    function verificaFilial_cb(data, errors) {
    	var r = filialByIdFilial_sgFilial_exactMatch_cb(data);
    	
    	if ( data != undefined && data[0] != undefined ){
			setElementValue("filialByIdFilial.sgFilial",data[0].sgFilial);
			setElementValue("filial.pessoa.nmFantasia",data[0].pessoa.nmFantasia);
    	}
    	
		if (r == true) { liberaNrFatura(); }
		else{ setFocus("filialByIdFilial.sgFilial");  }

		return r;
    }
	
	/* Libera o campo da fatura e seta o campo para a sg filial */
	function liberaFatura(){
		setDisabled("filialByIdFilial.idFilial",false);
		setDisabled("fatura.idFatura",false);
		setFocus("filialByIdFilial.sgFilial");
		
		//so libera o numero se for preenchido a filial
		setDisabled("fatura.nrFatura",true);	
	}
	
	
	
</script>