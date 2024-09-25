<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAgendamentos" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/entrega/manterAgendamentos" height="140">

		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M"/>


		<adsm:i18nLabels>
                <adsm:include key="LMS-00013"/>
                <adsm:include key="LMS-04400"/>                                                
                <adsm:include key="filialOrigem"/>
                <adsm:include key="filialDestino"/>
                <adsm:include key="filialAgendamento"/>
                <adsm:include key="periodoAgendamento"/>
                <adsm:include key="documentoServico"/>                                
                <adsm:include key="periodoContato"/>                                                
                <adsm:include key="chaveNfe"/>                                                
    	</adsm:i18nLabels> 

		<adsm:lookup dataType="text"
					 property="filialOrigem"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.entrega.manterAgendamentosAction.findLookupFilial"
    				 label="filialOrigem" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="20%" 
    				 width="30%"  
    				 exactMatch="true" 
    				 action="/municipios/manterFiliais"
		             onPopupSetValue="filialPopup" 
		             onDataLoadCallBack="filialDataLoad" 
		             onchange="return filialChange(this);">
         	
			<%--adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
         	<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />         
         	<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="25" disabled="true" serializable="true" />
	    </adsm:lookup>

	    <adsm:lookup dataType="text" 
	    			 property="filialDestino"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.entrega.manterAgendamentosAction.findLookupFilial"
    				 label="filialDestino" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="20%"
    				 width="30%" 
    				 exactMatch="true"
    				 action="/municipios/manterFiliais">
    				 
			<%--adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
         	<adsm:propertyMapping relatedProperty="nmFilialDestino" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilialDestino" modelProperty="sgFilial" />        	
         	<adsm:textbox dataType="text" property="nmFilialDestino" size="25" disabled="true" serializable="false" />
	    </adsm:lookup>
        <adsm:hidden property="sgFilialDestino" serializable="true"/>
	    
	
        <adsm:lookup property="filial"  
        			 idProperty="idFilial" 
        			 criteriaProperty="sgFilial" 
        			 service="lms.entrega.manterAgendamentosAction.findLookupFilial" 
        			 dataType="text"  
        			 label="filialAgendamento" 
        			 size="3" 
        			 action="/municipios/manterFiliais" 
        			 minLengthForAutoPopUpSearch="3" 
        			 exactMatch="true" 
        			 maxLength="3" 
        			 labelWidth="20%" 
        			 width="30%">
			<%--adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" /--%>
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" disabled="true"/>
        </adsm:lookup>
        
		<adsm:combobox property="idTipoServico" 
					   label="tipoServico" 
					   optionLabelProperty="dsTipoServico" 
					   optionProperty="idTipoServico" 
					   service="lms.configuracoes.tipoServicoService.find" 
					   required="false" 
					   labelWidth="20%"
					   width="30%"
					   boxWidth="150"
					   onlyActiveValues="false"/>

		<adsm:combobox labelWidth="20%" 
					   width="60%" 
					   required="false" 
					   property="tpDocumento" 
					   label="tipoDocumento" 
					   boxWidth="200"
					   domain="DM_TP_DOCTO" 
					   renderOptions="true"
					   onchange="return changeTpDocumento(this);" /> 
					   
		<adsm:textbox dataType="text" property="nrChaveNFe"	size="50" maxLength="50" disabled="false" label="chaveNfe" width="50%" labelWidth="20%" onchange="validateFieldChaveNFE();"/>

		<!-- ---------------------------------------- -->
		<adsm:hidden property="idDoctoServico" serializable="true"/>
		<adsm:hidden property="dsTpDocumentoServico" serializable="true"/>
		<adsm:hidden property="sgFilialOrigemDoctoServico" serializable="true"/>
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		<adsm:hidden property="nrDoctoServico" serializable="true"/>
		
		<div id="agrupamentoDocto" style="display:;border:none;">
		<adsm:combobox property="doctoServico.tpDocumentoServico" serializable="true" 
					   label="documentoServico" labelWidth="20%" width="30%" 
					   service="lms.entrega.manterAgendamentosAction.findTpDoctoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeTpDoctoServico(this);"
					   renderOptions="true">

			<adsm:lookup dataType="text" serializable="true"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 popupLabel="pesquisarFilial"
						 disabled="true"
						 action="/municipios/manterFiliais" 
						 size="3" maxLength="3" picker="false" 
						 onchange="var r = changeDocumentWidgetFilial({
											 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
											 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
										  	}); 							
								  	return r;"/>
			<adsm:hidden property="blBloqueado" value="N"/>
			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 popupLabel="pesquisarDocumentoServico"
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 						 
						 size="12" 
						 maxLength="8" 
						 mask="00000000" 
						 serializable="true" 
						 disabled="true">
			</adsm:lookup>						
			
		</adsm:combobox>		
		<!-- ---------------------------------------- -->		
		
		<adsm:lookup label="notaFiscal" 
					 action="/expedicao/consultarNotaFiscalCliente" 
					 service="lms.entrega.manterAgendamentosAction.findLookupNotaFiscalCliente"
					 dataType="integer" 
					 property="notaFiscalCliente"  
					 idProperty="idNotaFiscalConhecimento" 
					 labelWidth="20%" 
					 width="30%" 
					 criteriaProperty="nrNotaFiscal" 
					 mask="000000000" 
					 maxLength="30" 
					 size="45" 
					 exactMatch="false" 
					 minLengthForAutoPopUpSearch="3"
					 popupLabel="pesquisarNotaFiscal"
					 onPopupSetValue="notaFiscalPopup" >

				<adsm:propertyMapping criteriaProperty="remetente.idCliente" modelProperty="remetente.idCliente" />
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nrIdentificacao" modelProperty="remetente.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nmPessoa" modelProperty="remetente.pessoa.nmPessoa" inlineQuery="false"/>
			
				<adsm:propertyMapping criteriaProperty="destinatario.idCliente" modelProperty="destinatario.idCliente" />
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nrIdentificacao" modelProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nmPessoa" modelProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>

				<adsm:propertyMapping criteriaProperty="filialOrigem.idFilial" modelProperty="filialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialOrigem.sgFilial" modelProperty="filialOrigem.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filialOrigem.pessoa.nmFantasia" modelProperty="filialOrigem.pessoa.nmFantasia" inlineQuery="false"/>

				<adsm:propertyMapping criteriaProperty="filialDestino.idFilial" modelProperty="filialDestino.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialDestino.sgFilial" modelProperty="filialDestino.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="nmFilialDestino" modelProperty="filialDestino.pessoa.nmFantasia" inlineQuery="false"/>

				<adsm:propertyMapping relatedProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.nrDoctoServico" modelProperty="nrDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.tpDocumentoServico" modelProperty="tpDocumentoServico.value" blankFill="false"/>
				
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="idFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="nmFantasiaOrigem" blankFill="false"/>

		</adsm:lookup>
		</div>
		
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="remetente" 
			maxLength="20" 
			property="remetente" 
			service="lms.entrega.manterAgendamentosAction.findLookupRemetente" 
			size="20" 
			labelWidth="20%" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="remetente.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="remetente.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
		</adsm:lookup>

		<adsm:lookup label="destinatario" 
					 idProperty="idCliente" 
					 property="destinatario" 
					 criteriaProperty="pessoa.nrIdentificacao"
			 		 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 service="lms.entrega.manterAgendamentosAction.findLookupDestinatario"
					 action="/vendas/manterDadosIdentificacao" 
					 dataType="text" 
					 size="20" 
					 maxLength="20" 
					 labelWidth="20%" 
					 width="80%" >			
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="58" disabled="true" />
			<adsm:propertyMapping relatedProperty="destinatarioNrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado" />
			<adsm:hidden property="destinatarioNrIdentificacao" serializable="true"/>		
		</adsm:lookup>

		<adsm:combobox labelWidth="20%" 
					   width="30%" 
					   required="false" 
					   property="tpAgendamento" 
					   label="tipoAgendamento" 
					   boxWidth="200"
					   domain="DM_TIPO_AGENDAMENTO"
					   renderOptions="true"/>
					   
		<adsm:combobox labelWidth="20%" 
					   width="30%" 
					   required="false" 
					   property="tpSituacaoAgendamento" 
					   boxWidth="150"
					   label="situacao" 
					   domain="DM_SITUACAO_AGENDA"
					   renderOptions="true" />
		
        <adsm:range label="periodoContato" labelWidth="20%" width="30%" maxInterval="60">
			<adsm:textbox dataType="JTDate" property="periodoContatoInicial"/>
			<adsm:textbox dataType="JTDate" property="periodoContatoFinal"/>
		</adsm:range>

        <adsm:range label="periodoAgendamento" labelWidth="20%" width="30%" maxInterval="60">
			<adsm:textbox dataType="JTDate" property="periodoAgendamentoInicial"/>
			<adsm:textbox dataType="JTDate" property="periodoAgendamentoFinal"/>
		</adsm:range>
		
		<adsm:combobox labelWidth="20%" 
					   width="30%" 
					   boxWidth="100"
					   required="false" 
					   property="blCartao" 
					   label="cartaoCredito" 
					   domain="DM_SIM_NAO"
					   renderOptions="true" />

		<adsm:combobox service="lms.entrega.manterAgendamentosAction.findTurnoByIdFilial" 
					   labelWidth="20%" 
					   width="30%" 
					   boxWidth="200"
					   required="false" 
					   optionProperty="idTurno" 
					   optionLabelProperty="dsTurno" 
					   property="turno" 					   
					   label="turno">
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
		</adsm:combobox>

		<adsm:lookup 
					 action="/configuracoes/consultarFuncionariosView" 					
					 service="lms.entrega.manterAgendamentosAction.findLookupFuncionario" 
					 dataType="text" 
					 property="usuario"
					 idProperty="idUsuario" 
					 criteriaProperty="nrMatricula" 
					 label="registradoPor" 
					 size="8" 
					 maxLength="16" 
					 labelWidth="20%" 
					 exactMatch="true"
					 width="11%">					 
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="20" maxLength="50" width="19%" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridAgendamento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid 	idProperty="idAgendamentoEntrega"  
				property="gridAgendamento" 				
				disableMarkAll="true"
				selectionMode="none"				
				rows="7" 
				gridHeight="140"
				defaultOrder="dtAgendamento"
 			   	service="lms.entrega.manterAgendamentosAction.findPaginatedAgendamentoEntregaDoctoServico"
			   	rowCountService="lms.entrega.manterAgendamentosAction.getRowCountAgendamentoEntregaDoctoServico"
				scrollBars="horizontal">
			   	
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialAgendamento" property="sgFilial" width="70" />
			<adsm:gridColumn title="" property="nmFantasia" width="70" />
		</adsm:gridColumnGroup>			   	
			   	
		<adsm:gridColumn width="120" title="tipoServico" property="dsTipoServico" />		
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="40"/>
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialOrigem" width="120" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="40" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>		

 	 	<adsm:gridColumn width="120" title="notaFiscal" property="nrNotaFiscal" dataType="integer" mask="000000"/>
 	 	<adsm:gridColumn width="250" title="chaveNfe" property="nrChave" />
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="90" />
			<adsm:gridColumn title="" property="nmFantasiaDestino" width="90" />
		</adsm:gridColumnGroup>			   	
		
		<adsm:gridColumn title="identificacao" property="tpIdentificacaoRemetente" width="50" />
		<adsm:gridColumn title="" property="nrIdentificacaoRemetente" align="right" width="120"/>
		<adsm:gridColumn title="remetente" property="remetente" width="140" />
		<adsm:gridColumn title="identificacao" property="tpIdentificacaoDestinatario" width="50" />
		<adsm:gridColumn title="" property="nrIdentificacaoDestinatario" align="right" width="120"/>
		<adsm:gridColumn title="destinatario" property="destinatario" width="140" />
		<adsm:gridColumn width="180" title="tipoAgendamento" property="tpAgendamento" />
		<adsm:gridColumn width="140" dataType="JTDate" title="dataAgendamento" property="dtAgendamento"/>
		<adsm:gridColumn width="140" dataType="JTDateTimeZone" title="contato" property="dhContato"/>
		<adsm:gridColumn width="100" title="turno" property="dsTurno"/>
		<adsm:gridColumn width="90" dataType="JTTime" title="horarioInicial" property="horarioInicial" align="center" />
		<adsm:gridColumn width="90" dataType="JTTime" title="horarioFinal" property="horarioFinal" align="center" />				
		<adsm:gridColumn width="120" title="situacao" property="tpSituacaoAgendamento"/>		
	 	<adsm:gridColumn width="160" title="registradoPor" property="agendadoPor" />	 	
	 	
	 	<adsm:buttonBar></adsm:buttonBar>
	 	
	</adsm:grid>
</adsm:window>


<script>

//FUNÇÔES RELACIONADAS AO DOCTO SERVICO

  	  function initWindow(eventObj) {
	  	if (eventObj.name == "cleanButton_click" ) {
			writeDataSession();
			
			<%--
			preencheFilialDoctoServico();
			--%>

			lookupChange({e:document.getElementById("filialOrigem.idFilial"), forceChange:true});
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);			
			
			setFocusOnFirstFocusableField(document);			
	  	}
	  }

	function preencheFilialDoctoServico(){
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", nmFilial);
		
		if (idFilial != null && idFilial != undefined) {
			setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", false);
		}
		
	}

	// LMS-3252
	function changeTpDocumento(field){
		var tpDocumentoValue = field.value;
		
   	    switch (tpDocumentoValue){
	    	case "NFE" :
	    		resetValue("doctoServico.tpDocumentoServico");
	    		resetValue("doctoServico.filialByIdFilialOrigem.sgFilial");
	    		resetValue("doctoServico.nrDoctoServico");
	    		resetValue("notaFiscalCliente.idNotaFiscalConhecimento");
	    		
	    		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
	    		setDisabled("doctoServico.filialByIdFilialOrigem.sgFilial", true);
	    		setDisabled("doctoServico.tpDocumentoServico", true);
	    		setDisabled("doctoServico.nrDoctoServico", true);
	    		setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", true);
	    		setDisabled("doctoServico.idDoctoServico", true);
	    		setDisabled("nrChaveNFe", false);
	    		
    			break;
	    	case "CTRC" :
	    		resetValue("nrChaveNFe");
	    		
	    		setDisabled("doctoServico.tpDocumentoServico", false);
	    		setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", false);
	    		setDisabled("nrChaveNFe", true);
	    		
	    		break;
	     	default:
   	    }
	}
	
	function changeTpDoctoServico(field) {
	
		var flag = changeDocumentWidgetType({
							   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"), 
							   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
							   documentGroup:'DOCTOSERVICE',
							   parentQualifier:'doctoServico',
							   actionService:'lms.entrega.manterAgendamentosAction'});
	
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};		
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", 				criteriaProperty:"remetente.idCliente"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", 	criteriaProperty:"remetente.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", 		criteriaProperty:"remetente.pessoa.nmPessoa"};
		
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", 			 		criteriaProperty:"filialDestino.idFilial"};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", 					criteriaProperty:"filialDestino.sgFilial"};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", 		 	criteriaProperty:"nmFilialDestino"};
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.idCliente", 			  criteriaProperty:"destinatario.idCliente"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"destinatario.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", 		  criteriaProperty:"destinatario.pessoa.nmPessoa"};

		resetValue('idDoctoServico');
		
		if (field.value != '') {		
			changeDocumentWidgetFilial({
									 	filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
									 	documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
								  		});
		}
		
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", getElementValue("filialOrigem.idFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", getElementValue("filialOrigem.sgFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", getElementValue("filialOrigem.pessoa.nmFantasia"));
		
		
		if (getElementValue("filialOrigem.idFilial") != '') {
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			
			if (getElementValue(field)) {
				setDisabled("doctoServico.idDoctoServico", false);
			}
		}
		
		return flag;
	}

	  function validateTab() {
	  	if (validateTabScript(document.forms)) {
			if ( (getElementValue("filialOrigem.idFilial") == null || getElementValue("filialOrigem.idFilial") == "") && 
				 (getElementValue("filialDestino.idFilial") == null || getElementValue("filialDestino.idFilial") == "") && 
				 (getElementValue("filial.idFilial") == null || getElementValue("filial.idFilial") == "") ) {
				 
				 alert(i18NLabel.getLabel("LMS-00013")
								+ i18NLabel.getLabel("filialOrigem") + " ou "
								+ i18NLabel.getLabel("filialDestino")+ " ou "
								+ i18NLabel.getLabel("filialAgendamento")+ "." );

		    	 return false;
			}
			
			if(getElementValue("tpDocumento") == "NFE"){
			if ( (getElementValue("periodoAgendamentoInicial") == null || getElementValue("periodoAgendamentoInicial") == "") && 
						(getElementValue("nrChaveNFe") == "") && 
						(getElementValue("periodoContatoInicial") == null || getElementValue("periodoContatoInicial") == "")) {
					alert(i18NLabel.getLabel("LMS-00013")
						+ i18NLabel.getLabel("chaveNfe") + " ou "
						+ i18NLabel.getLabel("periodoAgendamento")+ " ou "
						+ i18NLabel.getLabel("periodoContato")+ "." );
					
					return false;
				}
			}
			else{
				if((getElementValue("periodoAgendamentoInicial") == null || getElementValue("periodoAgendamentoInicial") == "") && 
				 (getElementValue("doctoServico.idDoctoServico") == null || getElementValue("doctoServico.idDoctoServico") == "") && 
				 (getElementValue("periodoContatoInicial") == null || getElementValue("periodoContatoInicial") == "")) {
				 alert(i18NLabel.getLabel("LMS-00013")
								+ i18NLabel.getLabel("documentoServico") + " ou "
								+ i18NLabel.getLabel("periodoAgendamento")+ " ou "
								+ i18NLabel.getLabel("periodoContato")+ "." );

		    	 return false;
			}
			
			}
			
			return true;
    	} 
	  	else {
    		return false;
    	}
	  }	

		function pageLoad_cb(data) {
			onPageLoad_cb(data);
			if (getElementValue("idProcessoWorkflow") == "") {
				var sdo = createServiceDataObject("lms.entrega.manterAgendamentosAction.findDataSession","dataSession",null);
				xmit({serviceDataObjects:[sdo]});
			}
		}

		var idFilial = null;
		var sgFilial = null;
		var nmFilial = null;
		
		<%--
		var usuarioByIdUsuarioCriacao_nmUsuario = null;
		var usuarioByIdUsuarioCriacao_idUsuario = null;
		var usuarioByIdUsuarioCriacao_nrMatricula = null;
	    --%>
	    
		function dataSession_cb(data) {
			idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
			sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
			nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			
			<%--
			usuarioByIdUsuarioCriacao_nmUsuario = getNestedBeanPropertyValue(data,"usuarioByIdUsuarioCriacao.nmUsuario");
			usuarioByIdUsuarioCriacao_idUsuario = getNestedBeanPropertyValue(data,"usuarioByIdUsuarioCriacao.idUsuario");
			usuarioByIdUsuarioCriacao_nrMatricula = getNestedBeanPropertyValue(data,"usuarioByIdUsuarioCriacao.nrMatricula");
			--%>

			setElementValue("tpDocumento", "CTRC");
			changeTpDocumento(document.getElementById("tpDocumento"));
			
			writeDataSession();
		}

		function writeDataSession() {
			if (idFilial != null &&
				sgFilial != null &&
				nmFilial != null) {
				setElementValue("filial.idFilial",idFilial);
				setElementValue("filial.sgFilial",sgFilial);
				setElementValue("filial.pessoa.nmFantasia",nmFilial);
<%--
				lookupChange({e:document.getElementById("filial.idFilial"), forceChange:true});
--%>				
			    notifyElementListeners({e:document.getElementById("filial.idFilial")});
			}
			
			<%--
			if (usuarioByIdUsuarioCriacao_idUsuario != null) {
				setElementValue("usuario.idUsuario",usuarioByIdUsuarioCriacao_idUsuario);
			}
			if (usuarioByIdUsuarioCriacao_nrMatricula != null) {
				setElementValue("usuario.nrMatricula",usuarioByIdUsuarioCriacao_nrMatricula);
			}
			if (usuarioByIdUsuarioCriacao_nmUsuario != null) {
				setElementValue("usuario.nmUsuario",usuarioByIdUsuarioCriacao_nmUsuario);
			}
			--%>
			
			setFocusOnFirstFocusableField(document);
			
		}

	function setValuesDoctoServico(data) {
			setElementValue("doctoServico.tpDocumentoServico",getNestedBeanPropertyValue(data,"tpDocumentoServico.value"));
			changeTpDoctoServico(document.getElementById("doctoServico.tpDocumentoServico"));
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",false);
			setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.sgFilial"));
			setElementValue("doctoServico.filialByIdFilialOrigem.idFilial",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.idFilial"));
			setDisabled("doctoServico.idDoctoServico",false);
			setElementValue("doctoServico.nrDoctoServico",setFormat(document.getElementById("doctoServico.nrDoctoServico"),getNestedBeanPropertyValue(data,"nrDoctoServico")));
			setElementValue("doctoServico.idDoctoServico",getNestedBeanPropertyValue(data,"idDoctoServico"));
			setElementValue("idDoctoServico",getNestedBeanPropertyValue(data,"idDoctoServico"));
	}

	function setPopUp(data) {
		setValuesDoctoServico(data);
		return true;
	}

	function filialPopup(data){
		if (data != undefined){
			setaEstadoDoctoServico();
			preencheFilialRelated(data);
		}
		return true;
	}
	
	function dataLoadNF_cb(data) {
		notaFiscalCliente_nrNotaFiscal_exactMatch_cb(data);
		if (data != undefined && data.length == 1)
			setValuesDoctoServico(data[0]);
	}
	
	function setaEstadoDoctoServico(){
		resetValue("doctoServico.idDoctoServico");
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		
		if (getElementValue("doctoServico.tpDocumentoServico") != '')
			setDisabled("doctoServico.idDoctoServico", false);
	
	}

	function preencheFilialRelated(data){
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
        setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
        setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
	}
	
	function filialDataLoad_cb(data){
		var retorno = filialOrigem_sgFilial_exactMatch_cb(data);

		if (data != undefined && data.length == 1){
			setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", false);
			setaEstadoDoctoServico();
			preencheFilialRelated(data[0]);
		}
		return retorno;
	}
	
	function filialChange(obj){
		var retorno = filialOrigem_sgFilialOnChangeHandler();

		if (obj.value == ''){
			setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", true);
			resetValue("doctoServico.idDoctoServico");
			setDisabled("doctoServico.idDoctoServico", true);
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			
			if (getElementValue("doctoServico.tpDocumentoServico") != '') {
				setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
				setDisabled("doctoServico.idDoctoServico", true, null, false);
			}
			
		}
		return retorno;
	}
	
	/*
	 * Após selecionar algum item na popup
	 */
	function notaFiscalPopup(data) {
		// Desabilita campos de Documento de serviço
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
		setDisabled("doctoServico.idDoctoServico", false);
	}
	
	function validateFieldChaveNFE(){
		var chaveNfe = getElementValue("nrChaveNFe");
		
		if(chaveNfe != ""){
			if(!validateChaveNfe(chaveNfe)){
				setElementValue("nrChaveNFe", "");
				setFocus(document.getElementById("nrChaveNFe"));
			}
		}
	}
	
	function validateChaveNfe(chaveNfe) {
		if(chaveNfe.length >= 44){
			if(!validateDigitoVerificadorNfe(chaveNfe)){
				return false;
			}
		}else{
			alert("LMS-04400 - " + i18NLabel.getLabel("LMS-04400"));
			return false;
		}
		
		return true;
	}
  	
	/**
	 * Valida o digito verificador da Chave Nfe
	 */
	function validateDigitoVerificadorNfe(chaveNfe){
		var dvChaveNfe = chaveNfe.substring(chaveNfe.length - 1, chaveNfe.length);
		var chave = chaveNfe.substring(0, chaveNfe.length - 1);	
		var calculoChaveNfe = modulo11(chave);
		
		if(dvChaveNfe != (calculoChaveNfe)){
			alert("LMS-04400 - " + i18NLabel.getLabel("LMS-04400"));
			return false;
		}
		
		return true;
	}
	
	function modulo11(chave){
		var n = new Array();
		var peso = 2;
		var soma = 0;

		n = chave.split('');
		
		for (var i = n.length-1; i >= 0; i--) {
			var value = n[i];
			soma = soma + value * peso++;
			if(peso == 10){
				peso = 2;
			}
		}
		
		var mod = soma % 11;
		var dv;
		
		if(mod == 0 || mod == 1){
			dv = 0;
		} else {
			dv = 11 - mod;
		}
		
		return dv
	}
	
</script>