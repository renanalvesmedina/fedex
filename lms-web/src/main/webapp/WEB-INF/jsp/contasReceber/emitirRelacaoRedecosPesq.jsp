<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirRelacaoRedecosAction"  onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirRelacaoRedecos" >
	
		<adsm:lookup label="filialCobranca" labelWidth="18%" width="30%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.contasreceber.emitirRelacaoRedecosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3"
		             serializable="true">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="true" 
            			  size="25" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>

	 	<adsm:hidden property="siglaFilial" serializable="true"/>

		<adsm:combobox 
			property="tpFinalidade" label="finalidade" 
			domain="DM_FINALIDADE_REDECO"  labelWidth="18%" width="10%"/>		

		<adsm:combobox 
			label="situacao" property="tpSituacao" 
			domain="DM_STATUS_REDECO" labelWidth="18%" width="30%" boxWidth="180"/>
		

		<adsm:range label="periodoEmissao" labelWidth="18%" width="32%">
			<adsm:textbox property="periodoEmissaoInicial" dataType="JTDate" />
			<adsm:textbox property="periodoEmissaoFim" dataType="JTDate" />
		</adsm:range>


	 <adsm:hidden property="nmPessoa"/>
		<adsm:combobox 
			property="empresaCobranca.idEmpresaCobranca" onlyActiveValues="true" 
			optionLabelProperty="pessoa.nmPessoa" optionProperty="idEmpresaCobranca" 
			service="lms.contasreceber.manterMovimentoChequesPreDatadosAction.findEmpresaCobranca" 
			label="empresaCobranca" labelWidth="18%" 
			width="30%" boxWidth="180" >
				<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="nmPessoa"/>
			</adsm:combobox>


 	 <adsm:hidden property="siglaMoeda"/>

			<adsm:combobox 
				onchange="setSigla()"
				property="moeda.idMoeda" label="moedaExibicao" optionProperty="idMoeda" 
				optionLabelProperty="siglaSimbolo"
				service="lms.contasreceber.emitirRelacaoContaFreteAction.findMoedaPaisCombo" 
				labelWidth="18%"
				required="true"
				width="20%" boxWidth="100" serializable="true">
			</adsm:combobox>	
			
			
			<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"
    				   labelWidth="18%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton caption="visualizar"
				 service="lms.contasreceber.emitirRelacaoRedecosAction" disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function setSigla(){

		var moeda = document.getElementById('moeda.idMoeda');
			
		if( moeda.selectedIndex != 0 ){
			setElementValue("siglaMoeda", moeda.options[moeda.selectedIndex].text); 
		}
	}

	/** Busca a moeda do usuário que está na sessão  */
    function findMoedaUsuario(reinicializa, realizaXmit){
    
		if( reinicializa == true ){
			_serviceDataObjects = new Array(); 
		}
            
        addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacaoRedecosAction.findMoedaSessao",
                                                     "findMoedaSessao",
                                                     new Array()));
                  
		if( realizaXmit == true ){
			xmit(false);
		}
		
    }

    /**  CallBack da função findMoedaUsuario        */
    function findMoedaSessao_cb(data, error){
          setElementValue("moeda.idMoeda", data.idMoeda);
          setSigla();
    }      
  
  	/** PageLoadCallBack personalizado */
	function myPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data,erro);
		findMoedaUsuario(true,false);
		buscaFilialUsuario(false,true);
	}

	/** Chamado na inicialização da tela **/
	function initWindow(eventObj){
		if (eventObj.name == "cleanButton_click" ){
			findMoedaUsuario(true,false);
			buscaFilialUsuario(false,true);					
		}
	}
	
		/**
	*	Busca a filial do usuário logado
	*/
	function buscaFilialUsuario(reinicializa, realizaXmit){
	
		if( reinicializa == true ){
			_serviceDataObjects = new Array();
		}
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirRelacaoRedecosAction.findFilialUsuario",
                                                     "retornoFilialUsuario",
                                                     new Array()));
                  
		if( realizaXmit == true ){
			xmit(false);
		}
	    		
	}
	
	/**
	*	Retorno da busca da filial do usuário.
	*   Seta a filial da seção na lookup de filial
	*/
	function retornoFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(getElement('filial.sgFilial'));
			return false;
		}
		
		fillFormWithFormBeanData(0, data);				
		
		setElementValue('siglaFilial',data.filial.sgFilial);
		
		setFocusOnFirstFocusableField(document);			
		
	}	
	
		
	

</script>