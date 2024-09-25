<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.entrega.emitirTentativasAgendamentoAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/entrega/emitirTentativasAgendamento">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-09070"/>
			<adsm:include key="LMS-09074"/>
		</adsm:i18nLabels>
		 
		<!-- Este campo não faz parte desta tela, somente está aqui para compatibilidade com a lookup de documento de serviço -->
		<adsm:hidden property="clienteByIdClienteConsignatario.idCliente" serializable="false"/>
	
		<adsm:lookup label="filialOrigem" width="78%" labelWidth="20%"
		             property="filialByIdFilialOrigem"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.entrega.emitirTentativasAgendamentoAction.findLookupFilial" 		             
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilialOrigem" modelProperty="sgFilial" />        	
            <adsm:textbox dataType="text" 
            			  property="filialByIdFilialOrigem.pessoa.nmFantasia" 
            			  serializable="true" 
            			  size="50" 
            			  maxLength="60" 
            			  disabled="true"/>
        </adsm:lookup>        
        <adsm:hidden property="sgFilialOrigem" serializable="true"/>
        
        <adsm:lookup label="filialDestino" width="78%" labelWidth="20%"
		             property="filialByIdFilialDestino"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.entrega.emitirTentativasAgendamentoAction.findLookupFilial" 		             
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilialDestino" modelProperty="sgFilial" />        	
            <adsm:textbox dataType="text" 
            			  property="filialByIdFilialDestino.pessoa.nmFantasia" 
            			  serializable="true" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        <adsm:hidden property="sgFilialDestino" serializable="true"/>
	
		<adsm:lookup label="filialAgendamento" labelWidth="20%" width="78%"
		             property="filialAgendamento"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.entrega.emitirTentativasAgendamentoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="filialAgendamento.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	       	<adsm:propertyMapping relatedProperty="sgFilialAgendamento" modelProperty="sgFilial" />        	
            <adsm:textbox dataType="text" 
            			  property="filialAgendamento.pessoa.nmFantasia" 
            			  serializable="true" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        <adsm:hidden property="sgFilialAgendamento" serializable="true"/>
			
		<adsm:lookup label="remetente" 
			         property="remetente" 
        			 idProperty="idCliente"                      
        			 action="/vendas/manterDadosIdentificacao" 
                     criteriaProperty="pessoa.nrIdentificacao" 
                     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
                     dataType="text" 
                     exactMatch="true"                      
                     maxLength="20" 
                     service="lms.entrega.emitirTentativasAgendamentoAction.findLookupCliente" 
                     size="20"
                     labelWidth="20%" 
                     width="78%">			
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>	
			<adsm:propertyMapping relatedProperty="remetenteNrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
			<adsm:hidden property="remetenteNrIdentificacao" serializable="true"/>		
			<adsm:textbox dataType="text" 
						  disabled="true" 
						  property="remetente.pessoa.nmPessoa" 
						  serializable="true"
						  size="30"
						  maxLength="60"/>
		</adsm:lookup>	
		
		<adsm:lookup label="destinatario" 
			         property="destinatario"  
        			 idProperty="idCliente"                      
        			 action="/vendas/manterDadosIdentificacao" 
                     criteriaProperty="pessoa.nrIdentificacao" 
                     relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
                     dataType="text" 
                     exactMatch="true"                      
                     maxLength="20" 
                     service="lms.entrega.emitirTentativasAgendamentoAction.findLookupCliente" 
                     size="20" 
                     labelWidth="20%" 
                     width="78%">			
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>	
			<adsm:propertyMapping relatedProperty="destinatarioNrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
			<adsm:hidden property="destinatarioNrIdentificacao" serializable="true"/>		
			<adsm:textbox dataType="text" 
						  disabled="true" 
						  property="destinatario.pessoa.nmPessoa" 
						  serializable="true"
						  size="30"
						  maxLength="60"/>
		</adsm:lookup> 
		
		<!-- ---------------------------------------- -->
		<adsm:hidden property="idDoctoServico" serializable="true"/>
		<adsm:hidden property="dsTpDocumentoServico" serializable="true"/>
		<adsm:hidden property="sgFilialOrigemDoctoServico" serializable="true"/>
		<%--<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia" serializable="true"/>--%>
		
		<adsm:hidden property="nrDoctoServico" serializable="true"/>
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   label="documentoServico" labelWidth="20%" width="78%" 
					   service="lms.entrega.emitirTentativasAgendamentoAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return tpDocumentoServicoOnChange(this)">
 
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true"
						 action="" 
						 criteriaSerializable="true"
						 size="3" maxLength="3" picker="false" 
						 onchange="var r = changeDocumentWidgetFilial({
										   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
										   documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
										  	}); 							
								  	return r;">
				<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="sgFilialOrigemDoctoServico"/>
			</adsm:lookup>				  
			 
			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 						 
						 onDataLoadCallBack="retornoDocumentoServico"
						 onPopupSetValue="retornoDocumentoServicoPopup"
						 size="15" maxLength="8" mask="00000000" serializable="true" disabled="true"
						 popupLabel="pesquisarDocumentoServico">
			</adsm:lookup>						
			
		</adsm:combobox>		
		<!-- ---------------------------------------- -->
			
		<adsm:range label="periodoTentativaAgendamento" labelWidth="20%" width="78%" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="lancamentoInicial" cellStyle="vertical-align:bottom;"/>
			<adsm:textbox dataType="JTDate" property="lancamentoFinal" cellStyle="vertical-align:bottom;"/>
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" 
		       label="formatoRelatorio"
		       labelWidth="20%" 
		       width="80%"
			   domain="DM_FORMATO_RELATORIO"
		       required="true" 
		       defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="btnVisualizar" buttonType="reportButton" onclick="visualizarOnClick(this)" disabled="false"/>
			<adsm:button caption="limpar" id="btnLimpar" buttonType="cleanButton" disabled="false" onclick="myCleanButton(this)"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>
<script>

	/**
	*	Método sobrescrito para poder setar a filial do usuário após o click no botão Limpar
	*/
	function myCleanButton(elem){
		cleanButtonScript(elem.document);
		tpDocumentoServicoOnChange(getElement('doctoServico.tpDocumentoServico'));
		buscaFilialUsuario();				
	}

	/**
	*	Após carregar os dados da tela, busca a filial do usuário logado
	*
	*/
	function myOnPageLoadCallBack_cb(data,erro){
		onPageLoad_cb(data,erro);
		buscaFilialUsuario();			
	}
	
	/**
	*	Busca a filial do usuário logado
	*/
	function buscaFilialUsuario(){
		
		var dados = new Array();
        
        var sdo = createServiceDataObject("lms.entrega.emitirTentativasAgendamentoAction.findFilialUsuario",
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
			setFocus(getElement('filial.sgFilial'));
			return false;
		}
		
		fillFormWithFormBeanData(0, data);
		
		setElementValue('sgFilialAgendamento',getElementValue('filialAgendamento.sgFilial'));
		
		setFocusOnFirstFocusableField(document);			
		
	}
	
	/**
	*	Método chamado ao se clicar no botão Visualizar
	*/
	function visualizarOnClick(elem){
		
		var idDoctoServico 		= getElementValue('idDoctoServico');
		
		var doctoServico        = getElement('doctoServico.idDoctoServico');
		
		var idFilialOrigem 		= getElementValue('filialByIdFilialOrigem.idFilial');		
		var idFilialDestino 	= getElementValue('filialByIdFilialDestino.idFilial');		
		var idFilialAgendamento = getElementValue('filialAgendamento.idFilial');
		var idRemetente         = getElementValue('remetente.idCliente');
		var idDestinatario      = getElementValue('destinatario.idCliente');
		
		var dataAgendamentoIni  = getElement('lancamentoInicial');
		var dataAgendamentoFin  = getElement('lancamentoFinal');
		
		var dtInicial           = getElementValue('lancamentoInicial');
		var dtFinal             = getElementValue('lancamentoFinal');
		var idFilialOrigemDocto = getElementValue('doctoServico.filialByIdFilialOrigem.idFilial');		
		
		var filialOrigemDocto   = getElementValue('doctoServico.filialByIdFilialOrigem.sgFilial');
		var tpDocumento         = getElementValue('doctoServico.tpDocumentoServico');
		
		var nrDoctoServico      = getElementValue('doctoServico.nrDoctoServico');
		
		if( nrDoctoServico == '' ){
			resetValue('idDoctoServico');
		}
		//Regra 1.8
		if( (idDoctoServico == undefined || idDoctoServico == '') ){
		
		
			dataAgendamentoIni.required = 'true';
			dataAgendamentoFin.required = 'true';
			
			if(((idFilialOrigem 		== undefined || idFilialOrigem == '') &&
			    (idFilialDestino 		== undefined || idFilialDestino == '') &&
			    (idFilialAgendamento 	== undefined || idFilialAgendamento == '') &&
			    (idRemetente 			== undefined || idRemetente == '') &&
			    (idDestinatario 		== undefined || idDestinatario == '')) ||
			    ((dtInicial == undefined || dtInicial == '') ||
			     (dtFinal 	== undefined || dtFinal == ''))){
				alert(i18NLabel.getLabel('LMS-09070'));
				setFocusOnFirstFocusableField(document);
				return false;		     
			}
			
		} else {
			dataAgendamentoIni.required = 'false';
			dataAgendamentoFin.required = 'false';
		}
		
		if( ((idFilialOrigem != '') && idFilialOrigemDocto != '') && (idFilialOrigem) != (idFilialOrigemDocto) ){
		
		
			alert(i18NLabel.getLabel('LMS-09074'));
			setFocusOnFirstFocusableField(document);
			return false;		     
		}
		
		verificaAcessoAsFiliais(elem, idFilialOrigem, idFilialDestino, idFilialAgendamento);		
		
	}
	 
	/**
	*	Verifica se o usuário possui permissões de acesso as filiais de Origem, Destino e Agendamento informadas
	*/
	function verificaAcessoAsFiliais(elem, idFilialOrigem, idFilialDestino, idFilialAgendamento){
	
		var dados = new Array();
         
        setNestedBeanPropertyValue(dados, "idFilialOrigem", idFilialOrigem);
        setNestedBeanPropertyValue(dados, "idFilialDestino", idFilialDestino);
        setNestedBeanPropertyValue(dados, "idFilialAgendamento", idFilialAgendamento);        
         
        var sdo = createServiceDataObject("lms.entrega.emitirTentativasAgendamentoAction.findPermissaoUsuario",
                                          "retornoPermissaoUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
		
	}
	
	/**
	*	Método de retorno da permissão de usuário sobre as filiais
	*/
	function retornoPermissaoUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocusOnFirstFocusableField(document);
			return false;	
		}
				
		reportButtonScript('lms.entrega.emitirTentativasAgendamentoAction', 'openPdf', document.forms[0]);		
		
	}
	
	/**
	*	Chamado ao trocar o tipo de documento na combo de tipos de documento de serviço
	*/
	function tpDocumentoServicoOnChange(field){
	    
		var tpDocumentoServico = getElement('doctoServico.tpDocumentoServico');

		var flag = changeDocumentWidgetType({
				   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"), 
				   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
				   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
				   documentGroup:'DOCTOSERVICE',
				   parentQualifier:'doctoServico',
				   actionService:'lms.entrega.emitirTentativasAgendamentoAction'});
	
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};		
	
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", 				criteriaProperty:"remetente.idCliente"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", 	criteriaProperty:"remetente.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", 		criteriaProperty:"remetente.pessoa.nmPessoa"};
		
		if (tpDocumentoServico.value != 'RRE'){
			pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", 			 		criteriaProperty:"filialByIdFilialDestino.idFilial"};
			pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", 					criteriaProperty:"filialByIdFilialDestino.sgFilial"};
			pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", 		 	criteriaProperty:"filialByIdFilialDestino.pessoa.nmFantasia"};
		} else {
			pms[pms.length] = {modelProperty:"filialDestino.idFilial", 			 					criteriaProperty:"filialByIdFilialDestino.idFilial"};
			pms[pms.length] = {modelProperty:"filialDestino.sgFilial", 								criteriaProperty:"filialByIdFilialDestino.sgFilial"};
			pms[pms.length] = {modelProperty:"filialDestino.pessoa.nmFantasia", 		 			criteriaProperty:"filialByIdFilialDestino.pessoa.nmFantasia"};
		}

		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.idCliente", 			  criteriaProperty:"destinatario.idCliente"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"destinatario.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", 		  criteriaProperty:"destinatario.pessoa.nmPessoa"};
		
		
	    var indice = tpDocumentoServico.selectedIndex;
	    
	    resetValue('idDoctoServico'); 
    	resetValue('dsTpDocumentoServico');
    	resetValue('sgFilialOrigemDoctoServico');
    	resetValue('nrDoctoServico');
	     
	    if( indice != 0 ){
	    	setFocus(getElement('doctoServico.filialByIdFilialOrigem.sgFilial'));
	    	setElementValue('dsTpDocumentoServico',tpDocumentoServico.options[indice].text);
	    } else {
	    	setDisabled(getElement('doctoServico.filialByIdFilialOrigem.idFilial'),true);
	    	setFocus(field);	    	
	    }
		
		if (field.value != '') {		
			changeDocumentWidgetFilial({
									 	filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
									 	documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
								  		});
		}
		
		return flag;
	}
	
	/////////////////////////////////////////////////////////////
	/**
	*	Chamado no retorno da busca do documento de serviço informado
	*/
	function retornoDocumentoServico_cb(data) {		
	    var r = doctoServico_nrDoctoServico_exactMatch_cb(data);
	    
	    setElementValue('nrDoctoServico',getElementValue('doctoServico.nrDoctoServico'));
   	    setElementValue('idDoctoServico',getElementValue('doctoServico.idDoctoServico'));
   	    setElementValue('filialByIdFilialOrigem.pessoa.nmFantasia',getElementValue('filialByIdFilialOrigem.pessoa.nmFantasia'));
	    
		return r;
	}
	
	function retornoDocumentoServicoPopup(data){
   	    if (data != undefined) {
   	    	if (data.idCtoInternacional != undefined)  {
   	    		setElementValue('idDoctoServico', getNestedBeanPropertyValue(data,"idCtoInternacional"));
   	    		setElementValue('nrDoctoServico', getNestedBeanPropertyValue(data, 'nrCrt'));
   	    		
   	    	} else {
   	    		setElementValue('idDoctoServico', getNestedBeanPropertyValue(data,"idDoctoServico"));   	    		
   	    		setElementValue('nrDoctoServico', getNestedBeanPropertyValue(data, 'nrDoctoServico'));
   	    	}	
   	    	
		}
	}
	
	
	/**
	*	Chamado ao se alterar a filial origem do documento de serviço
	*/
	function filialOrigemOnChange(elem){
	
		var retorno = changeDocumentWidgetFilial({
						   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						   });
						   
		var doctoServico = getElement('doctoServico.idDoctoServico');						   
						   
		//TODO este if deveria ser funcionalidade da tag de documento de serviço
		if( retorno == true || retorno == 'true' ){
			setDisabled(doctoServico, true, getElement('doctoServico.idDoctoServico').document, true);
			setFocus(getElement('lancamentoInicial'));
		}
						   
		return retorno;						   
	}
	
	/**
	*	Chamado no retorno dos dados da tela 
	*/
	function myOnDataLoadFilial_cb(data,erro){
		
		var retorno = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		
		if( retorno == true ){
			setElementValue('sgFilialOrigemDoctoServico',data[0].sgFilial);
			setElementValue('filialByIdFilialOrigem.pessoa.nmFantasia',data[0].pessoa.nmFantasia);
		} else {
			resetValue('sgFilialOrigemDoctoServico');
			resetValue('filialByIdFilialOrigem.pessoa.nmFantasia');
		}
		
	}
	
</script>