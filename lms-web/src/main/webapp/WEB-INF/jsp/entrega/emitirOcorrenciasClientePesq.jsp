<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.emitirOcorrenciasClienteAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/entrega/emitirOcorrenciasCliente">
		<adsm:i18nLabels>
			<adsm:include key="LMS-09026"/>
		</adsm:i18nLabels>
		<adsm:hidden property="idFilialSessao" serializable="false" />
		<adsm:hidden property="tpFilialSessao" serializable="false" />
		
		<adsm:lookup label="filialOrigem" width="85%"
		             property="filialOrigem"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.entrega.emitirOcorrenciasClienteAction.findLookupFilial" 		             
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="nmFilialOrigem" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilialOrigem" modelProperty="sgFilial" />        	
            <adsm:textbox dataType="text" 
            			  property="nmFilialOrigem" 
            			  serializable="true" 
            			  size="50" 
            			  maxLength="60"  
            			  disabled="true"/>
        </adsm:lookup>
         
        <adsm:hidden property="sgFilialOrigem" serializable="true"/>
        
        <adsm:lookup label="remetente" 
			         property="cliente" 
        			 idProperty="idCliente"                      
        			 action="/vendas/manterDadosIdentificacao" 
                     criteriaProperty="pessoa.nrIdentificacao" 
                     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
                     dataType="text" 
                     exactMatch="true"                      
                     maxLength="20" 
                     service="lms.entrega.emitirOcorrenciasClienteAction.findLookupCliente" 
                     size="20" 
                     width="85%"
                     required="true">			
			<adsm:propertyMapping relatedProperty="nmRemetente" modelProperty="pessoa.nmPessoa"/>			
			<adsm:propertyMapping relatedProperty="nrIdentificacaoFormatado" modelProperty="pessoa.nrIdentificacaoFormatado"/>			
			<adsm:textbox dataType="text" 
						  disabled="true" 
						  property="nmRemetente" 
						  serializable="true"
						  size="30"
						  maxLength="60"/>
		</adsm:lookup>
		
		<adsm:hidden property="nrIdentificacaoFormatado" serializable="true"/>
		
		<adsm:lookup label="filialDestino" width="85%"
		             property="filialDestino"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.entrega.emitirOcorrenciasClienteAction.findLookupFilial" 		             
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="nmFilialDestino" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilialDestino" modelProperty="sgFilial" />        	
            <adsm:textbox dataType="text" 
            			  property="nmFilialDestino" 
            			  serializable="true" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>		
        
        <adsm:hidden property="sgFilialDestino" serializable="true"/>
        
   		<adsm:combobox label="ocasionadaPor" 
   		               property="ocasionadaPor" 
   		               boxWidth="85"
   		               domain="DM_OCORRENCIA_OCASIONADA_POR"
   		               required="true"
   		               onchange="return myOcasionadaPorOnChange(this)"   		               
   		               defaultValue="A"/>
   		               
		<adsm:hidden property="dsOcasionadaPor" serializable="true" />
		
		<adsm:range label="periodoOcorrencia" width="85%" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="lancamentoInicial" required="true"/> 
			<adsm:textbox dataType="JTDate" property="lancamentoFinal" required="true"/>
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" 
		               label="formatoRelatorio" 
		               domain="DM_FORMATO_RELATORIO" 
		               required="true" 
		               defaultValue="pdf"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.entrega.emitirOcorrenciasClienteAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>	
	
</adsm:window>
<script>

	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("tpFilialSessao").masterLink = true;
	/**
	*	Seta a descrição do campo Ocasionada por no campo hidden dsOcasionadaPor
	*   para ser utilizada no cabeçalho do relatório
	*/
	function myOcasionadaPorOnChange(elemento){		
		
		var retorno = comboboxChange({e:elemento});
		
		var ocasionadaPor = getElement('ocasionadaPor');
		
		setElementValue('dsOcasionadaPor',ocasionadaPor.options[ocasionadaPor.selectedIndex].text);
		
		return retorno;
		
	}
	
	/**
	*   Chamada no pageLoad para pegar a descrição do valor default do campo Ocasionada
	*/
	function myOnPageLoadCallBack_cb(data,erro){
		onPageLoad_cb(data,erro);
		buscaFilialUsuario();
		setaValor();
	}
	
	/**
	*  Chamada na initWindow para pegar a descrição do valor default do campo Ocasionada
	*/
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			setaValor();
			buscaFilialUsuario();	
		} 
	}
	
	/**
	*  Função que seta a descrição do hidden dsOcasionadaPor
	*/
	function setaValor(){
		setElementValue('dsOcasionadaPor',getElement('ocasionadaPor').options[getElement('ocasionadaPor').selectedIndex].text);
	}
	
	
	/**
	*	Busca a filial do usuário logado
	*/
	function buscaFilialUsuario(){
	
		var dados = new Array();
        
        var sdo = createServiceDataObject("lms.entrega.emitirOcorrenciasClienteAction.findFilialUsuario",
                                          "retornoFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	*	Retorno da busca da filial do usuário.
	*   Seta a filial da seção na lookup de filial
	*/
	function retornoFilialUsuario_cb(data,erro){
		 
		if( erro != undefined ){
			alert(erro);
			setFocus(getElement('filialDestino.sgFilial'));
			return false;
		}
		
		fillFormWithFormBeanData(0, data);
		setElementValue("idFilialSessao",data.filialDestino.filial.idFilial);
		setElementValue('filialDestino.idFilial',data.filialDestino.filial.idFilial);
		setElementValue('filialDestino.sgFilial',data.filialDestino.filial.sgFilial);
		setElementValue('nmFilialDestino',data.filialDestino.filial.pessoa.nmFantasia);
		setElementValue('sgFilialDestino',data.filialDestino.filial.sgFilial);
		setElementValue('tpFilialSessao',data.tpFilialSessao);
		setFocusOnFirstFocusableField(document);			
		
	}
	
	function validateTab() {
		if (validateTabScript(document.forms)){
			var blFilialMatriz = (getElementValue("tpFilialSessao") == "MA");
			
			if (!blFilialMatriz) {
				var idOrigem = getElementValue("filialOrigem.idFilial");
				var idDestino = getElementValue("filialDestino.idFilial");
				var idSessao = getElementValue("idFilialSessao");
				if (idOrigem != idSessao && idDestino != idSessao) {
					alert(i18NLabel.getLabel("LMS-09026"));
					return false;
				}
			}	
			return true;
		} 
		return false;
	}
	
</script>