<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterAgendamentos" onPageLoadCallBack="pageLoad">
	<adsm:form action="/entrega/consultarAgendamentos" height="165">

		<adsm:hidden property="idFilialUsuarioLogado" />
 
		<adsm:i18nLabels>
                <adsm:include key="LMS-00013"/>
                <adsm:include key="LMS-09111"/>
                <adsm:include key="LMS-09069"/>
                <adsm:include key="filialOrigem"/>
                <adsm:include key="filialDestino"/>
                <adsm:include key="filialAgendamento"/>
                <adsm:include key="periodoAgendamento"/>
                <adsm:include key="documentoServico"/>
                <adsm:include key="notaFiscal"/>                
    	</adsm:i18nLabels>

		<adsm:hidden property="empresa.tpEmpresa" value="M"/>

		<adsm:lookup dataType="text"
					 property="filialByIdFilialOrigem"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.entrega.consultarAgendamentosAction.findLookupFilial"
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
         	
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
         	<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />         
         	<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
	    </adsm:lookup>

	    <adsm:lookup dataType="text" 
	    			 property="filialByIdFilialDestino"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.entrega.consultarAgendamentosAction.findLookupFilial"
    				 label="filialDestino" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="20%"
    				 width="30%" 
    				 exactMatch="true"
    				 action="/municipios/manterFiliais">
    				 
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
         	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
	    </adsm:lookup>
        
		<adsm:lookup property="filial"
				     idProperty="idFilial" 
		             criteriaProperty="sgFilial" 
		             service="lms.entrega.consultarAgendamentosAction.findLookupFilial" 
		             dataType="text" 
					 label="filialAgendamento" 
		             size="3" 
		             action="/municipios/manterFiliais" 
		             minLengthForAutoPopUpSearch="3"
		             exactMatch="true"  
		             maxLength="3" 
				     labelWidth="20%" 
				     width="30%">
        	<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" disabled="true" serializable="false" maxLength="50" />
        </adsm:lookup>
        
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
			service="lms.entrega.consultarAgendamentosAction.findLookupRemetente" 
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


		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			dataType="text" 
			exactMatch="true" 
			idProperty="idCliente" 
			label="destinatario" 
			maxLength="20" 
			property="destinatario" 
			service="lms.entrega.consultarAgendamentosAction.findLookupDestinatario" 
			size="20" 
			labelWidth="20%" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="destinatario.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
			
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="destinatario.pessoa.nmPessoa" 
				serializable="false"
				size="58"/>
		</adsm:lookup>
		
		<adsm:combobox labelWidth="20%" 
					   width="30%" 
					   required="false" 
					   property="tpDestinatario" 
					   label="tipoDestinatario" 
					   domain="DM_TIPO_PESSOA" renderOptions="true"/>					   
		
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

	<%-- DOCUMENTO DE SERVICO ----------------------------------------------------------%>

		<adsm:combobox property="doctoServico.tpDocumentoServico" serializable="true"
					   label="documentoServico" labelWidth="20%" width="30%" 
					   service="lms.entrega.consultarAgendamentosAction.findTpDoctoServico" 
					   optionProperty="value" optionLabelProperty="description"						   			  
					   onchange="return changeTpDoctoServico(this);" renderOptions="true">
			
			<adsm:lookup dataType="text" serializable="true"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 					 	 
						 service="" 
						 popupLabel="pesquisarDocumentoServico"
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
		
		<adsm:hidden property="idDoctoServico"/>
<%--
		<adsm:hidden property="doctoServico.clienteByIdClienteRemetente.idCliente" serializable="false"/>
		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.idCliente" serializable="false"/>
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.idCliente" serializable="false"/>
	--%>
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>

	<%-- FIM DOCUMENTO DE SERVICO ----------------------------------------------------------%>
        


		<adsm:lookup label="notaFiscal" 
					 action="/expedicao/consultarNotaFiscalCliente" 
					 service="lms.entrega.consultarAgendamentosAction.findLookupNotaFiscalCliente"
					 dataType="integer" 
					 property="notaFiscalCliente"  
					 idProperty="idNotaFiscalConhecimento" 
					 labelWidth="20%" 
					 width="30%" 
					 criteriaProperty="nrNotaFiscal" 
					 mask="000000000" 
					 size="45" 
					 maxLength="30" 
					 exactMatch="false" 
					 onchange="return changeResetDoctoServico(this);"
					 minLengthForAutoPopUpSearch="3" onclickPicker="notaFiscalPickerClick();">

				<adsm:propertyMapping criteriaProperty="remetente.idCliente" modelProperty="remetente.idCliente" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nrIdentificacao" modelProperty="remetente.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="remetente.pessoa.nmPessoa" modelProperty="remetente.pessoa.nmPessoa" inlineQuery="false"/>
			
				<adsm:propertyMapping criteriaProperty="destinatario.idCliente" modelProperty="destinatario.idCliente" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nrIdentificacao" modelProperty="destinatario.pessoa.nrIdentificacao" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="destinatario.pessoa.nmPessoa" modelProperty="destinatario.pessoa.nmPessoa" inlineQuery="false"/>

				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filialOrigem.idFilial" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialOrigem.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filialOrigem.pessoa.nmFantasia" inlineQuery="false"/>

				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filialDestino.idFilial" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" modelProperty="filialDestino.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filialDestino.pessoa.nmFantasia" inlineQuery="false"/>

				<adsm:propertyMapping relatedProperty="doctoServico.idDoctoServico" modelProperty="idDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.nrDoctoServico" modelProperty="nrDoctoServico" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.tpDocumentoServico" modelProperty="tpDocumentoServico.value" blankFill="false"/>
				
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="idFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilialOrigem" blankFill="false"/>
				<adsm:propertyMapping relatedProperty="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="nmFantasiaOrigem" blankFill="false"/>
				
		</adsm:lookup>		

		<adsm:combobox property="formaConsulta" 
					   label="formaConsulta" 
					   domain="DM_FORMA_CONSULTA_AGENDAMENTOS"
					   required="false" 
					   labelWidth="20%"
					   onchange="validaFormaConsulta()"
					   width="30%" 
					   renderOptions="true"/>

		<adsm:combobox property="entregas" 
					   label="entregas" 
					   domain="DM_STATUS_REALIZACAO_ENTREGA"
					   required="false" 
					   labelWidth="20%"
					   disabled="true"
					   width="30%"
					   renderOptions="true"/>
 

        <adsm:range label="periodoDPE" labelWidth="20%" width="30%">
			<adsm:textbox dataType="JTDate" property="periodoDPEInicial"/>
			<adsm:textbox dataType="JTDate" property="periodoDPEFinal"/>
		</adsm:range>

        <adsm:range label="periodoAgendamento" labelWidth="20%" width="30%" maxInterval="30" >
			<adsm:textbox dataType="JTDate" property="periodoAgendamentoInicial"/>
			<adsm:textbox dataType="JTDate" property="periodoAgendamentoFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridAgendamento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>				
		
	</adsm:form>
	
	
	<adsm:grid 	idProperty="idAgendamentoDoctoServico"
				detailFrameName="det"
				property="gridAgendamento"
				disableMarkAll="true"
				selectionMode="none"		
				rows="5" 
				gridHeight="100"
				defaultOrder="dtAgendamento"
 			   	service="lms.entrega.consultarAgendamentosAction.findPaginatedConsultaAgendamentoEntregaDoctoServico"
			   	rowCountService="lms.entrega.consultarAgendamentosAction.getRowCountConsultaAgendamentoEntregaDoctoServico"
			   	onRowClick="gridClick"
				scrollBars="horizontal">
	
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialAgendamento" property="sgFilial" width="90" />
			<adsm:gridColumn title="" property="nmFantasia" width="90" />
		</adsm:gridColumnGroup>			   	

		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialDestino" property="sgFilialDestino" width="90" />
			<adsm:gridColumn title="" property="nmFantasiaDestino" width="90" />
		</adsm:gridColumnGroup>			   	

		<adsm:gridColumn width="150" title="tipoServico" property="dsTipoServico" />		
		
		<adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialOrigem" width="120" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>		

 	 	<adsm:gridColumn width="120" title="notaFiscal" mask="000000" dataType="integer" property="nrNotaFiscal" />
		<adsm:gridColumn width="70" dataType="JTDate" title="dpe" property="dtPrevEntrega"/>

		<adsm:gridColumn title="identificacao" 
				property="tpIdentificacaoRemetente"
				width="50" />
		<adsm:gridColumn title="" property="nrIdentificacaoRemetente" 
				align="right" width="120"/>
		<adsm:gridColumn title="remetente" property="remetente" width="140" />
						
		<adsm:gridColumn title="identificacao" 
				property="tpIdentificacaoDestinatario"
				width="50" />
		<adsm:gridColumn title="" 
				property="nrIdentificacaoDestinatario" 
				align="right" width="120"/>
		<adsm:gridColumn title="destinatario" 
				property="destinatario" 
				width="140" />

		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn title="controleCargas" property="sgFilialControleCarga" width="100" />
			<adsm:gridColumn title="" property="controleCarga" dataType="integer" mask="00000000" width="10" />
		</adsm:gridColumnGroup>	

		<adsm:gridColumnGroup customSeparator=" ">
			<adsm:gridColumn title="manifestoEntrega" property="sgFilialManifestoEntrega" width="80" />
			<adsm:gridColumn title="" property="manifestoEntrega" dataType="integer" mask="00000000" width="70" />
		</adsm:gridColumnGroup>	
		
		<adsm:gridColumn width="70" dataType="JTDate" title="agendadoPara" property="dtAgendamento"/>		
		<adsm:gridColumn width="150" title="" property="dsTurno"/>
		<adsm:gridColumn width="50" title="" dataType="JTTime" property="horarioInicial"/>
		<adsm:gridColumn width="50" title="" dataType="JTTime" property="horarioFinal"/>
		
		<adsm:gridColumn width="105" title="cartaoCredito" property="blCartao" renderMode="image-check" />
		
		<adsm:gridColumn width="60" title="meioTransporte" property="meioTransporteNrFrota"/>		
		<adsm:gridColumn width="80" title="" property="meioTransporteNrIdentificador"/>		

		<adsm:gridColumn width="60" title="semiReboque" property="semiReboqueNrFrota"/>		
		<adsm:gridColumn width="80" title="" property="semiReboqueNrIdentificador"/>		

		<adsm:gridColumn width="70" title="dataEntrega" property="dataEntrega"/>
		<adsm:gridColumn width="40" title="" dataType="JTTime" property="horaEntrega"/>

		<adsm:gridColumn width="220" title="observacao" property="obAgendamentoEntrega"/>		

		<adsm:gridColumn width="120" title="situacao" property="tpSituacaoAgendamento" />
	</adsm:grid>
	<adsm:buttonBar>
	</adsm:buttonBar> 
</adsm:window>

<script>

	 function changeResetDoctoServico(obj){
		var retorno = notaFiscalCliente_nrNotaFiscalOnChangeHandler();		
		if (obj.value == ''){
			resetValue("doctoServico.idDoctoServico");
			resetValue("doctoServico.tpDocumentoServico");
			setDisabled("doctoServico.idDoctoServico", true);
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		}	
		return retorno;
	 }
	  		
	 function notaFiscalPickerClick(){
	 	if(getElementValue("remetente.idCliente")==""){
	 		alert(i18NLabel.getLabel("LMS-09111"));
	 	}else{
	 		lookupClickPicker({e:document.getElementById('notaFiscalCliente.idNotaFiscalConhecimento')});	
	 	}	
	 }

  	  function initWindow(eventObj) {
	  	if (eventObj.name == "cleanButton_click" ) {
			writeDataSession();

			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);
			preencheFilialDoctoServico();
			
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

	function changeTpDoctoServico(field) {
	
		var flag = changeDocumentWidgetType({
							   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"), 
							   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
							   documentGroup:'DOCTOSERVICE',
							   parentQualifier:'doctoServico',
							   actionService:'lms.entrega.consultarAgendamentosAction'});
	
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};		
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", 				criteriaProperty:"remetente.idCliente"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", 	criteriaProperty:"remetente.pessoa.nrIdentificacao"};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", 		criteriaProperty:"remetente.pessoa.nmPessoa"};
		
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", 			 		criteriaProperty:"filialByIdFilialDestino.idFilial"};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", 					criteriaProperty:"filialByIdFilialDestino.sgFilial"};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", 		 	criteriaProperty:"filialByIdFilialDestino.pessoa.nmFantasia"};
		
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
		
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", getElementValue("filialByIdFilialOrigem.idFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", getElementValue("filialByIdFilialOrigem.sgFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", getElementValue("filialByIdFilialOrigem.pessoa.nmFantasia"));
				
		if (getElementValue("filialByIdFilialOrigem.idFilial") != ''){
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			if (field.value != '') {
				setDisabled("doctoServico.idDoctoServico", false);
			}	
		}
		
		return flag;
	}
      	
		function pageLoad_cb(data) {
			onPageLoad_cb(data);			
			
			var sdo = createServiceDataObject("lms.entrega.consultarAgendamentosAction.findDataSession","dataSession",null);
			xmit({serviceDataObjects:[sdo]});
			
		}

		var idFilial = null;
		var sgFilial = null;
		var nmFilial = null;
		var blMatriz = null;
	
		function dataSession_cb(data) {
			idFilial = getNestedBeanPropertyValue(data,"filial.idFilial");
			sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
			nmFilial = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			blMatriz = getNestedBeanPropertyValue(data,"blMatriz");
			writeDataSession();
		}

		function writeDataSession() {
			if (idFilial != null &&
				sgFilial != null &&
				nmFilial != null) {
				
				setElementValue("filialByIdFilialOrigem.idFilial",idFilial);
				setElementValue("filialByIdFilialOrigem.sgFilial",sgFilial);
				setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia",nmFilial);
				
				setElementValue("idFilialUsuarioLogado",idFilial);
				
				preencheFilialDoctoServico();				
				
			}
		}

	  	function validateTab() {
	
	  		if (validateTabScript(document.forms)) {
				if ( (getElementValue("filialByIdFilialOrigem.idFilial") == null || getElementValue("filialByIdFilialOrigem.idFilial") == "") && 
					 (getElementValue("filialByIdFilialDestino.idFilial") == null || getElementValue("filialByIdFilialDestino.idFilial") == "") && 
					 (getElementValue("notaFiscalCliente.idNotaFiscalConhecimento") == null || getElementValue("notaFiscalCliente.idNotaFiscalConhecimento") == "") && 
					 (getElementValue("filial.idFilial") == null || getElementValue("filial.idFilial") == "") ) {
				 
					 alert(i18NLabel.getLabel("LMS-00013")
									+ i18NLabel.getLabel("filialOrigem") + ", "
									+ i18NLabel.getLabel("filialDestino")+ ", "
									+ i18NLabel.getLabel("filialAgendamento")+ " ou "
									+ i18NLabel.getLabel("notaFiscal")+ "." );
			    	 return false;
				}
			
				if ( (getElementValue("periodoAgendamentoInicial") == "" || getElementValue("periodoAgendamentoFinal") == "") && 
					 (getElementValue("doctoServico.idDoctoServico") == "") ) {
				 
					 alert(i18NLabel.getLabel("LMS-00013")
									+ i18NLabel.getLabel("periodoAgendamento") + " ou "
									+ i18NLabel.getLabel("documentoServico")+  "." );

			    	 return false;
				}
				
				if (getElementValue("idFilialUsuarioLogado") != null && blMatriz != "true") {
					if ( (getElementValue("filialByIdFilialOrigem.idFilial") != null && getElementValue("filialByIdFilialOrigem.idFilial") != getElementValue("idFilialUsuarioLogado") ) && 
						 (getElementValue("filialByIdFilialDestino.idFilial") != null && getElementValue("filialByIdFilialDestino.idFilial") != getElementValue("idFilialUsuarioLogado")) && 
						 (getElementValue("filial.idFilial") != null && getElementValue("filial.idFilial") != getElementValue("idFilialUsuarioLogado")) ) {
				 
						 alert(i18NLabel.getLabel("LMS-09069"));

				    	 return false;
					}
				}
				
				return true;
	    	} else {
    			return false;
    		}
		}	

		function validaFormaConsulta() {
			if (getElementValue("formaConsulta") == "E") {
				setDisabled("entregas", false);
			} else {
				setElementValue("entregas", "");
				setDisabled("entregas", true);
			}
		}

		function tabShow(){
			var tabGroup = getTabGroup(this.document);
			if (tabGroup) {
				//ref para tab cad		
				var tabCad = tabGroup.getTab("det");
				tabCad.setDisabled(true);
				
				var tabDoc = tabGroup.getTab("doc");
				tabDoc.setDisabled(true);
				
			}
		}

		function gridClick(){
			//ref para tabgroup    	
			var tabGroup = getTabGroup(this.document);
			if (tabGroup) {
				//ref para tab cad		
				var tabCad = tabGroup.getTab("det");
				tabCad.setDisabled(false);
				
				var tabDoc = tabGroup.getTab("doc");
				tabDoc.setDisabled(false);
			}	
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

		function dataLoadNF_cb(data) {
			notaFiscalCliente_nrNotaFiscal_exactMatch_cb(data);
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

	function filialDataLoad_cb(data){
		var retorno = filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);

		if (data != undefined && data.length == 1){
			setDisabled("notaFiscalCliente.idNotaFiscalConhecimento", false);
			setaEstadoDoctoServico();
			preencheFilialRelated(data[0]);
		}
		
		return retorno;
	}

	function filialChange(obj){
		var retorno = filialByIdFilialOrigem_sgFilialOnChangeHandler();

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

</script>