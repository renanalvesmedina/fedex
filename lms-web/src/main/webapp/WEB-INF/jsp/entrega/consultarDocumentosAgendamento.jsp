<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.entrega.manterAgendamentosAction" onPageLoadCallBack="consultaDocumentoAgendamento" >
	<adsm:form action="/entrega/manterAgendamentos" >
	
		<adsm:i18nLabels>
                <adsm:include key="LMS-09068"/>
                <adsm:include key="LMS-09110"/>
    	</adsm:i18nLabels>
	
	
		<adsm:section caption="listagemDocumentosServicoAgendamento"/>
		<adsm:label key="branco" width="100%" style="height:25px;border:none"/>
		
		<adsm:hidden property="tpSituacaoAtivo" value="A"/>
		<adsm:hidden property="filialAgendamento.idFilial" value=""/>
		
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
        			 width="80%">  
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>


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
         	
         	<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />         
         	<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
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
    				 
         	<adsm:propertyMapping relatedProperty="nmFilialDestino" modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping relatedProperty="sgFilialDestino" modelProperty="sgFilial" />        	
         	<adsm:textbox dataType="text" property="nmFilialDestino" size="25" disabled="true" serializable="false" />
	    </adsm:lookup>
        <adsm:hidden property="sgFilialDestino" serializable="true"/>
	    
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
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			
			<adsm:textbox dataType="text" disabled="true" 
					property="remetente.pessoa.nmPessoa" serializable="false" size="58"/>
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

		<!-- ---------------------------------------- -->
		<adsm:hidden property="idDoctoServico" serializable="true"/>
		<adsm:hidden property="dsTpDocumentoServico" serializable="true"/>
		<adsm:hidden property="sgFilialOrigemDoctoServico" serializable="true"/>
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		<adsm:hidden property="nrDoctoServico" serializable="true"/>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico" serializable="true" 
					   label="documentoServico" labelWidth="20%" width="30%" 
					   service="lms.entrega.manterAgendamentosAction.findTpDoctoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeTpDoctoServico(this);">

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
		<!-- ---------------------------------------- -->
				        
		<adsm:lookup property="localizacaoMercadoria" idProperty="idLocalizacaoMercadoria" 
					 criteriaProperty="cdLocalizacaoMercadoria" 
					 service="lms.entrega.manterAgendamentosAction.findLookupLocalizacaoMercadoria" 
					 action="/sim/manterLocalizacoesMercadoria"
					 label="localizacaoMercadoria" dataType="integer" size="4" maxLength="3" 
					 labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="localizacaoMercadoria.dsLocalizacaoMercadoria" modelProperty="dsLocalizacaoMercadoria" />
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacaoAtivo" />
			<adsm:textbox property="localizacaoMercadoria.dsLocalizacaoMercadoria" dataType="text" 
						  size="60" maxLength="60" disabled="true"/>
		</adsm:lookup>
				        
		<adsm:combobox property="blAgendamento" 
					   label="servicoAgendamento" 
					   domain="DM_SIM_NAO" 
					   labelWidth="20%" 					   
					   width="30%" />			       
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="agendamentoEntrega" />
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid property="agendamentoEntrega" 
			   idProperty="idDoctoServico"
			   service="lms.entrega.manterAgendamentosAction.findPaginatedDoctoServico"
			   showPagging="false" 
			   unique="true" 
			   scrollBars="both"
			   gridHeight="180"
			   onRowClick="gridClick" >
		
			<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" width="40"/>
				<adsm:gridColumnGroup separatorType="DOCTO_SERVICO"> 			
				<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="60" />
				<adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="60" align="right" dataType="integer" mask="00000000" />
			</adsm:gridColumnGroup> 
		
	 	 	<adsm:gridColumn width="120" title="notaFiscal" property="nrNotaFiscal" dataType="integer" mask="000000"/>
		
			<adsm:gridColumnGroup separatorType="FILIAL" >
				<adsm:gridColumn title="filialDestino" property="doctoServico.filialDestinoOperacional.sgFilial" width="90" />
				<adsm:gridColumn title="" property="doctoServico.filialDestinoOperacional.pessoa.nmFantasia" width="90" />
			</adsm:gridColumnGroup>			   			
			
			<adsm:gridColumn title="identificacao" property="doctoServico.clienteByIdClienteRemetente.pessoa.tpIdentificacao" width="50" />
			<adsm:gridColumn title="" property="doctoServico.clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado" align="right" width="120"/>
			<adsm:gridColumn title="remetente" property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" width="140" />

			<adsm:gridColumn title="identificacao" property="doctoServico.clienteByIdClienteDestinatario.pessoa.tpIdentificacao" width="50" />
			<adsm:gridColumn title="" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado" align="right" width="120"/>
			<adsm:gridColumn title="destinatario" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" width="140" />
			
			<adsm:gridColumn title="dataPrevistaEntrega" dataType="JTDate" property="doctoServico.dtPrevEntrega" width="150" />
			
			<adsm:gridColumn title="localizacaoMercadoria" property="lome.dsLocalizacaoMercadoria" width="200" />
			
		<adsm:buttonBar>
			<adsm:button caption="confirmar" id="btnConfirmar" onclick="onConfirmarClick();" />
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" />
		</adsm:buttonBar>
		
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">

	document.getElementById("filialAgendamento.idFilial").masterLink = "true";
	 
	document.getElementById("destinatario.idCliente").masterLink = "true";
	document.getElementById("destinatario.pessoa.nrIdentificacao").masterLink = "true";
	document.getElementById("destinatario.pessoa.nmPessoa").masterLink = "true";			

	document.getElementById("filial.idFilial").masterLink = "true";
	document.getElementById("filial.sgFilial").masterLink = "true";
	document.getElementById("filial.pessoa.nmFantasia").masterLink = "true";			

	function initWindow(eventObj) {
		setDisabled("btnConfirmar",false);
		setDisabled("btnFechar",false);
		
		lookupChange({e:document.getElementById("filialOrigem.idFilial"), forceChange:true});						
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);
			
		setFocusOnFirstFocusableField(document);
		
	}
	
	function onConfirmarClick() {

		var array = new Array();
		var list = agendamentoEntregaGridDef.getSelectedIds();	
		var idFilialDestinoOperacionalAnterior = 0;
		var podeFechar = 1;
		for (var i=0; i < list.ids.length; i++)	{

			var line = agendamentoEntregaGridDef.findById(list.ids[i]);
		
			if ( idFilialDestinoOperacionalAnterior != 0 && idFilialDestinoOperacionalAnterior != line.idFilialDestinoOperacional ) {
				podeFechar = 0;
				alert(i18NLabel.getLabel("LMS-09068"));
				break;
			}
		
			array[i] = {idDoctoServico:line.idDoctoServico,
						nrDoctoServico:line.doctoServico.nrDoctoServico,
						tpDocumentoServicoValue:line.doctoServico.tpDocumentoServicoValue,
						tpDocumentoServico:line.doctoServico.tpDocumentoServico,
						idFilial:line.doctoServico.filialByIdFilialOrigem.idFilial,
						sgFilial:line.doctoServico.filialByIdFilialOrigem.sgFilial,
						idClienteRemetente:line.idClienteRemetente,
						idClienteDestinatario:line.idClienteDestinatario,
						idFilialDestinoOperacional:line.idFilialDestinoOperacional,
						sgFilialDestinoOperacional:line.doctoServico.filialDestinoOperacional.sgFilial,
						nmFilialDestinoOperacional:line.doctoServico.filialDestinoOperacional.pessoa.nmFantasia,
						nrNotaFiscal:line.nrNotaFiscal};
						
			idFilialDestinoOperacionalAnterior = line.idFilialDestinoOperacional;
			
		}
		
		if (podeFechar == 1) {
			if (window.dialogArguments.validateDocumentosRepitidos(array)) {
				window.dialogArguments.setArrayDocumentoServico(array);	
				self.close();
			} else {
				alert(i18NLabel.getLabel("LMS-09110"));
			}
		}
	}

	function gridClick() {
		return false;
	}

	/**
	*	Chamado no retorno dos dados da tela
	*/
	function myOnDataLoadFilial_cb(data,erro){
		
		var retorno = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		
		if( retorno == true ){
			setElementValue('sgFilialOrigemDoctoServico',data[0].sgFilial);
			setElementValue('doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia',data[0].pessoa.nmFantasia);
		} else {
			resetValue('sgFilialOrigemDoctoServico');
			resetValue('doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia');
		}
		
	}
	
	function consultaDocumentoAgendamento_cb() {
		onPageLoad_cb();   
		setElementValue("blAgendamento", "S");
		setElementValue("filialAgendamento.idFilial", getElementValue(window.dialogArguments.document.forms[0].elements["filial.idFilial"]));
		setElementValue("destinatario.idCliente", getElementValue(window.dialogArguments.document.forms[0].elements["destinatario.idCliente"]));
		setElementValue("destinatario.pessoa.nrIdentificacao", getElementValue(window.dialogArguments.document.forms[0].elements["destinatario.pessoa.nrIdentificacao"]));
		setElementValue("destinatario.pessoa.nmPessoa", getElementValue(window.dialogArguments.document.forms[0].elements["destinatario.pessoa.nmPessoa"]));
		setDisabled("destinatario.idCliente", true);
		
		setElementValue("filial.idFilial", getElementValue(window.dialogArguments.document.forms[0].elements["filial.idFilial"]));
		setElementValue("filial.sgFilial", getElementValue(window.dialogArguments.document.forms[0].elements["filial.sgFilial"]));
		setElementValue("filial.pessoa.nmFantasia", getElementValue(window.dialogArguments.document.forms[0].elements["filial.pessoa.nmFantasia"]));
		setDisabled("filial.idFilial", true);
		
		setDisabled("btnFechar",false);		
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
				
		if (getElementValue("filialOrigem.idFilial") != ''){
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", false);
		}
		
		return flag;
	}
	
	function filialPopup(data){
		if (data != undefined){
			setaEstadoDoctoServico();
			preencheFilialRelated(data);
		}
		return true;
	}

	function filialDataLoad_cb(data){
		var retorno = filialOrigem_sgFilial_exactMatch_cb(data);

		if (data != undefined && data.length == 1){
			setaEstadoDoctoServico();
			preencheFilialRelated(data[0]);
		}
		return retorno;
	}

	function filialChange(obj){
		var retorno = filialOrigem_sgFilialOnChangeHandler();

		if (obj.value == ''){
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

	function preencheFilialRelated(data){
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
        setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
        setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
	}

	function setaEstadoDoctoServico(){
		resetValue("doctoServico.idDoctoServico");
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		
		if (getElementValue("doctoServico.tpDocumentoServico") != '')
			setDisabled("doctoServico.idDoctoServico", false);
	
	}

</script>