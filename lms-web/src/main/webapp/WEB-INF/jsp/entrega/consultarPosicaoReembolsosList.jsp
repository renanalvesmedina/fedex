<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarPosicaoReembolsos" service="lms.entrega.consultarPosicaoReembolsosAction" onPageLoadCallBack="pageLoad">

	<adsm:form action="/entrega/consultarPosicaoReembolsos" height="106" >
			<adsm:lookup label="filialOrigemReembolso" labelWidth="20%" width="31%" property="filialByIdFilialOrigem"
		             idProperty="idFilial" criteriaProperty="sgFilial" action="/municipios/manterFiliais" 
		             service="lms.entrega.consultarPosicaoReembolsosAction.findLookupFilial" 
		             dataType="text" size="3" maxLength="3" exactMatch="true"  minLengthForAutoPopUpSearch="3"
		             onPopupSetValue="filialPopup" onDataLoadCallBack="filialDataLoad" onchange="return filialChange(this);">
		             
        	<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
        	 
        	<adsm:propertyMapping relatedProperty="reciboReembolso.filialByIdFilialOrigem.idFilial" modelProperty="idFilial" />
        	<adsm:propertyMapping relatedProperty="reciboReembolso.filialByIdFilialOrigem.sgFilial" modelProperty="sgFilial" />        	
        	<adsm:propertyMapping relatedProperty="reciboReembolso.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />        	
     
        	<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />        						  
            <adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" size="30" maxLength="50" disabled="true"/>
        </adsm:lookup>
        <adsm:hidden property="empresa.tpEmpresa" value="M"/>
        
        <adsm:lookup label="filialDestinoReembolso" labelWidth="16%" width="33%" property="filialByIdFilialDestino"
		             idProperty="idFilial"  criteriaProperty="sgFilial" action="/municipios/manterFiliais" 
		             service="lms.entrega.consultarPosicaoReembolsosAction.findLookupFilial" 
		             dataType="text" size="3" maxLength="3" exactMatch="true" minLengthForAutoPopUpSearch="3">
		             
        	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />   
        	<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />      	
            <adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia" serializable="false" size="30" maxLength="50" disabled="true"/>
        </adsm:lookup>
        		
		<adsm:lookup action="/vendas/manterDadosIdentificacao" property="clienteByIdClienteRemetente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 dataType="text" exactMatch="true" idProperty="idCliente" label="remetente" maxLength="20" 
					 service="lms.entrega.consultarPosicaoReembolsosAction.findLookupCliente" size="20" 	labelWidth="20%" width="80%">
			
			<adsm:propertyMapping relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>			
			<adsm:textbox dataType="text" disabled="true" property="clienteByIdClienteRemetente.pessoa.nmPessoa" serializable="false" size="30"/>
		</adsm:lookup>
		
		<adsm:lookup action="/vendas/manterDadosIdentificacao" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				dataType="text" exactMatch="true" idProperty="idCliente" label="destinatario" maxLength="20" property="clienteByIdClienteDestinatario" 
				service="lms.entrega.consultarPosicaoReembolsosAction.findLookupCliente" size="20" 	labelWidth="20%" 	width="80%">
			
			<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>			
			<adsm:textbox dataType="text" disabled="true" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" serializable="false" size="30"/>
		</adsm:lookup>
	
	<%-- DOCUMENTO DE SERVICO ----------------------------------------------------------%>

		<adsm:combobox property="doctoServico.tpDocumentoServico" serializable="true"
					   label="documentoServico" labelWidth="20%" width="30%" 
					   service="lms.entrega.consultarPosicaoReembolsosAction.findTipoDocumentoServico" 
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
						 property="doctoServico" popupLabel="pesquisarDocumentoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 											 
						 service="" 
						 action="" 						 
						 size="12" maxLength="8" mask="00000000" serializable="true" disabled="true" >					
			</adsm:lookup>		 
		</adsm:combobox>
		
		<adsm:hidden property="idDoctoServico"/>
		<adsm:hidden property="doctoServico.clienteByIdClienteRemetente.idCliente" serializable="false"/>
		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.idCliente" serializable="false"/>
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.idCliente" serializable="false"/>
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
	<%-- FIM DOCUMENTO DE SERVICO ----------------------------------------------------------%>
		
	<%-- RECIBO DE REEMBOLSO----- ----------------------------------------------------------%>
		<adsm:combobox property="reciboReembolso.tpDocumentoServico" 
					   label="reciboReembolso" labelWidth="20%" width="30%" 
					   service="lms.entrega.consultarPosicaoReembolsosAction.findTipoDocumentoServicoRRE" 
					   optionProperty="value" 
					   optionLabelProperty="description"		
					   defaultValue="RRE"	  
					   disabled="true">
			
			<adsm:lookup dataType="text"
						 property="reciboReembolso.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="lms.entrega.consultarPosicaoReembolsosAction.findLookupServiceDocumentFilialRRE" 
						 disabled="true"
						 onDataLoadCallBack="filialRREDataLoad" onPopupSetValue="filialRREPopup" onchange="return filialRREOnChange(this)"
						 action="/municipios/manterFiliais" 
						 size="3" maxLength="3" picker="false">
				 <adsm:propertyMapping relatedProperty="reciboReembolso.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" inlineQuery="false"/>							 
			</adsm:lookup>
						 											
			<adsm:hidden property="blBloqueado" value="N"/>
			<adsm:lookup dataType="integer" 
						 property="reciboReembolso" 
						 idProperty="idDoctoServico" criteriaProperty="nrReciboReembolso" 											 
						 service="lms.entrega.consultarPosicaoReembolsosAction.findLookupServiceDocumentNumberRRE" 
						 action="/entrega/manterReembolsos" popupLabel="pesquisarReciboReembolso"			 
						 size="12" maxLength="8" mask="00000000" serializable="true" disabled="false" onPopupSetValue="retornoPopPupRecibo">
				<adsm:propertyMapping criteriaProperty="blBloqueado" modelProperty="blBloqueado"/>	
										
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/>	
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/>	
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>

				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filialDestino.idFilial"/>			
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filialByIdFilialDestino.idFilial"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" modelProperty="filialDestino.sgFilial"/>
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filialDestino.pessoa.nmFantasia"/>

				<adsm:propertyMapping criteriaProperty="doctoServico.tpDocumentoServico" modelProperty="doctoServicoByIdDoctoServReembolsado.tpDocumentoServico" inlineQuery="false" addChangeListener="false"/>			
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.sgFilial" modelProperty="doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial" inlineQuery="false" addChangeListener="false"/>			
				<adsm:propertyMapping criteriaProperty="doctoServico.filialByIdFilialOrigem.idFilial" modelProperty="doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.idFilial" addChangeListener="false"/>
				
				<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="doctoServicoByIdDoctoServReembolsado.idDoctoServico" addChangeListener="false"/>				
				<adsm:propertyMapping criteriaProperty="doctoServico.idDoctoServico" modelProperty="idDoctoReembolso" addChangeListener="false"/>				
				<adsm:propertyMapping criteriaProperty="doctoServico.nrDoctoServico" modelProperty="doctoServicoByIdDoctoServReembolsado.nrDoctoServico" inlineQuery="false" addChangeListener="false"/>
			</adsm:lookup>		 
		</adsm:combobox>
		<adsm:hidden property="reciboReembolso.filialByIdFilialOrigem.pessoa.nmFantasia" />
		
	<%-- FIM RECIBO DE REEMBOLSO----- ----------------------------------------------------------%>
		
		<adsm:range label="periodoEmissaoReembolso" labelWidth="20%" width="30%" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" cellStyle="vertical-align:bottom;" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" cellStyle="vertical-align:bottom;" />
		</adsm:range>
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS_POSICAO_REEMBOLSO" labelWidth="20%" width="30%" renderOptions="true"/>
		
		<adsm:buttonBar freeLayout="true" >
			<adsm:button onclick="pesquisar();" caption="consultar" buttonType="findButton" disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid  idProperty="idReciboReembolso" property="reciboReembolso" rows="8" unique="true" scrollBars="horizontal" 
				service="lms.entrega.consultarPosicaoReembolsosAction.findGridPosicaoReembolso" gridHeight="175" onRowClick="gridClick"
				rowCountService="lms.entrega.consultarPosicaoReembolsosAction.getRowCountGridPosicaoReembolso" selectionMode="none">
				
		<adsm:gridColumn width="40" title="documentoServico" property="tpDocumentoServico" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilial" width="60" />
			<adsm:gridColumn title="" property="nrDoctoServico" width="50" align="right" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		 
		<adsm:gridColumn width="40" title="reembolso" property="tpDocumentoServicoRR" />
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn title="" property="sgFilialRR" width="60" />
			<adsm:gridColumn title="" property="nrDoctoServicoRR" width="50" align="right" dataType="integer" mask="00000000"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn width="150" title="dataHoraDeEmissao" property="dhEmissao" align="center" dataType="JTDateTimeZone"/>

		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialOrigemReembolso" property="sgFilialOrigem" width="80" />
			<adsm:gridColumn title="" property="nmFilialOrigem" width="70" align="right" />
		</adsm:gridColumnGroup>

		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filialDestinoReembolso" property="sgFilialDestino" width="80" />
			<adsm:gridColumn title="" property="nmFilialDestino" width="70" align="right" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn width="50"  title="identificacao" property="tpIdentificacaoRemetente"/>
		<adsm:gridColumn width="125" title="" property="nrIdentificacaoRemetente" align="right"/>		
		<adsm:gridColumn width="180" title="remetente" property="nmRemetente"/>
		
		<adsm:gridColumn width="50"  title="identificacao" property="tpIdentificacaoDestinatario"/>
		<adsm:gridColumn width="125" title="" property="nrIdentificacaoDestinatario" align="right"/>
		<adsm:gridColumn width="180" title="destinatario" property="nmDestinatario"/>		
			
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="valor" property="sgMoeda" width="50" />
			<adsm:gridColumn title="" property="dsSimbolo" width="20" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="90"  title="" property="vlReembolso" align="right" />
		
		<adsm:gridColumn width="230" title="situacao" property="tpSituacao" />
			
		<adsm:buttonBar/>
		
	</adsm:grid>
	
</adsm:window>
<script type="text/javascript">
	
	var idFilial;
	var sgFilial;
	var nmFilial;
	
	function retornoPopPupRecibo(data){
		if(data != undefined){
		   		setElementValue("reciboReembolso.filialByIdFilialOrigem.sgFilial",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.sgFilial"));
	  			setElementValue("reciboReembolso.filialByIdFilialOrigem.idFilial",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.idFilial"));
	  			setElementValue("reciboReembolso.filialByIdFilialOrigem.pessoa.nmFantasia",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.pessoa.nmFantasia"));
	  			setDisabled("reciboReembolso.idDoctoServico", false);
		}
	}	
	
	function pageLoad_cb(){
		onPageLoad_cb();
		findFilialSessao();
		document.getElementById("empresa.tpEmpresa").masterLink = "true";
	}
	
	function findFilialSessao(){
		var sdo = createServiceDataObject("lms.entrega.consultarPosicaoReembolsosAction.findFilialSessao","findFilialSessao");
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFilialSessao_cb(data){
		if (data != undefined){
			idFilial = data.idFilial;
			sgFilial = data.sgFilial;
			nmFilial = data.pessoa.nmFantasia;
			preencheFilialSessao();
			preencheFilialReciboReembolso();
			preencheFilialDoctoServico();
		}
	}
		
	function preencheFilialSessao(){
		setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", nmFilial);
	}
	
	function filialChange(obj){
		var retorno = filialByIdFilialOrigem_sgFilialOnChangeHandler();
		
		if (obj.value == ''){
			resetValue("doctoServico.idDoctoServico");
			resetValue("reciboReembolso.idDoctoServico");
			setDisabled("doctoServico.idDoctoServico", true);
			setDisabled("reciboReembolso.idDoctoServico", true, null, false);
			resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
			
			if (getElementValue("doctoServico.tpDocumentoServico") != '') {
				setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", false);
				setDisabled("doctoServico.idDoctoServico", false);
				setDisabled("doctoServico.nrDoctoServico", true);
			}
			
			setDisabled("reciboReembolso.filialByIdFilialOrigem.idFilial", false);
			
		}
		
		return retorno;
	}
	
	function filialPopup(data){
		if (data != undefined){
			setaEstadoReciboReembolso();
			setaEstadoDoctoServico();
			preencheFilialRelated(data);
		}
		
		return true;
	}
	
	function filialDataLoad_cb(data){
		var retorno = filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);

		if (data != undefined && data.length == 1){
			setaEstadoReciboReembolso();
			setaEstadoDoctoServico();
			preencheFilialRelated(data[0]);
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
	
	function setaEstadoReciboReembolso(){
		resetValue("reciboReembolso.idDoctoServico");
		setDisabled("reciboReembolso.idDoctoServico", false);
		setDisabled("reciboReembolso.filialByIdFilialOrigem.idFilial", true);
		
	}
	
	function gridClick(){
		//ref para tabgroup    	
		var tabGroup = getTabGroup(this.document);
		if (tabGroup) {
			//ref para tab cad		
			var tabCad = tabGroup.getTab("cad");
			tabCad.setDisabled(false);
		}	
	}
	
	function pesquisar(){
		if ((getElementValue("idDoctoServico") == '' && getElementValue("reciboReembolso.idDoctoServico") == '' &&
				(getElementValue("dtEmissaoInicial") == '' || getElementValue("dtEmissaoFinal") == '')) ||
			(getElementValue("filialByIdFilialDestino.idFilial") == '' && getElementValue("filialByIdFilialOrigem.idFilial") == '')){
		
				alert(parent.i18NLabel.getLabel("LMS-09084"));
				return;
		} else {
			findButtonScript('reciboReembolso', document.forms[0]);
		}		
	}
	
	function filialRREDataLoad_cb(data){
		reciboReembolso_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		
		if (data != undefined && data.length == 1){
			setDisabled("reciboReembolso.idDoctoServico", false);
			setFocus("reciboReembolso.nrReciboReembolso");
		}
	}
	
	function filialRREPopup(data){
		if (data != undefined){
			setDisabled("reciboReembolso.idDoctoServico", false);
			setFocus("reciboReembolso.nrReciboReembolso");
		}
		
		return true;
	}
	
	
	function filialRREOnChange(obj){
		var retorno = reciboReembolso_filialByIdFilialOrigem_sgFilialOnChangeHandler();
		if (obj.value == "") {
			setDisabled("reciboReembolso.idDoctoServico", true);
		}
		return retorno;
	}

	function initWindow(evt){
 
		if (evt.name == 'cleanButton_click'){
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("reciboReembolso.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", true);
			setDisabled("reciboReembolso.idDoctoServico", false);			
			preencheFilialSessao();
			preencheFilialReciboReembolso();
			preencheFilialDoctoServico();
		} 
	}
	
	
	function tabShow(){
		var tabGroup = getTabGroup(this.document);
		if (tabGroup) {
			//ref para tab cad		
			var tabCad = tabGroup.getTab("cad");
			tabCad.setDisabled(true);
		}
	}
	
	function preencheFilialDoctoServico(){
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", nmFilial);
	}
	
	function preencheFilialReciboReembolso(){
		setElementValue("reciboReembolso.filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("reciboReembolso.filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("reciboReembolso.filialByIdFilialOrigem.pessoa.nmFantasia", nmFilial);
	}
	
	function changeTpDoctoServico(field) {
		var flag = changeDocumentWidgetType({
							   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"), 
							   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
							   documentGroup:'DOCTOSERVICE',
							   parentQualifier:'doctoServico',
							   actionService:'lms.entrega.consultarPosicaoReembolsosAction'});
	
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};		
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.idCliente", 				criteriaProperty:"clienteByIdClienteRemetente.idCliente" , inlineQuery:true};
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", 	criteriaProperty:"clienteByIdClienteRemetente.pessoa.nrIdentificacao", inlineQuery:false };
		pms[pms.length] = {modelProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa", 		criteriaProperty:"clienteByIdClienteRemetente.pessoa.nmPessoa" , inlineQuery:false };
		
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.idFilial", 			 		criteriaProperty:"filialByIdFilialDestino.idFilial" , inlineQuery:true};
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.sgFilial", 					criteriaProperty:"filialByIdFilialDestino.sgFilial" , inlineQuery:false };
		pms[pms.length] = {modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia", 		 	criteriaProperty:"filialByIdFilialDestino.pessoa.nmFantasia" , inlineQuery:false };
		
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.idCliente", 			  criteriaProperty:"clienteByIdClienteDestinatario.idCliente" , inlineQuery:true};
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao", criteriaProperty:"clienteByIdClienteDestinatario.pessoa.nrIdentificacao" , inlineQuery:false };
		pms[pms.length] = {modelProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa", 		  criteriaProperty:"clienteByIdClienteDestinatario.pessoa.nmPessoa" , inlineQuery:false };

		resetValue('idDoctoServico');
		
		changeDocumentWidgetFilial({filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
									 	documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
								  		});

		
		setElementValue("doctoServico.filialByIdFilialOrigem.idFilial", getElementValue("filialByIdFilialOrigem.idFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.sgFilial", getElementValue("filialByIdFilialOrigem.sgFilial"));
		setElementValue("doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia", getElementValue("filialByIdFilialOrigem.pessoa.nmFantasia"));
				
		if (getElementValue("filialByIdFilialOrigem.idFilial") != ''){
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
			setDisabled("doctoServico.idDoctoServico", (getElementValue(field) == ""));
		}else{
			setDisabled("doctoServico.idDoctoServico", (getElementValue(field) == ""));
			setDisabled("doctoServico.nrDoctoServico", true);
		}
		
		return flag;
	}

//-->
</script>