<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window onPageLoadCallBack="pageLoadCustom">
	
		<adsm:i18nLabels>
			<adsm:include  key="LMS-09086"/>		
		</adsm:i18nLabels>

	
	<adsm:form action="entrega/cancelarEntregaAlterarOcorrencia">
	
		<adsm:lookup property="filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3"
				service="lms.entrega.cancelarEntregaAlterarOcorrenciaAction.findFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="19%" width="70%" minLengthForAutoPopUpSearch="3"
				exactMatch="true" 				style="width:45px" 				disabled="false"
				onchange="return filialChange(this)"
				onDataLoadCallBack="filialDataLoad"
				onPopupSetValue="filialPopup">
				
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />		
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>						
		</adsm:lookup>
				
	  <adsm:textbox label="controleCargas" dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" size="3" labelWidth="19%" width="70%" disabled="true" serializable="false">		
		  <adsm:textbox dataType="integer" mask="00000000" property="controleCarga.nrControleCarga"  size="8" disabled="true" serializable="false"/>
      </adsm:textbox>
	  	
	   <adsm:textbox label="manifestoEntrega" dataType="text" property="manifestoEntrega.filial.sgFilial" size="3" labelWidth="19%" width="70%" disabled="true" serializable="false">		  
		  <adsm:textbox dataType="integer" mask="00000000" property="manifestoEntrega.nrManifestoEntrega" size="8" disabled="true" serializable="false"/>
      </adsm:textbox>
      	    		 
	  	<adsm:hidden property="controleCarga.idControleCarga"/>
   		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial"/>
   		<adsm:hidden property="manifestoEntrega.idManifestoEntrega"/>
	  	<adsm:hidden property="manifestoEntregaDocumento.idManifestoEntregaDocumento"/>
	    <adsm:hidden property="manifestoEntrega.filial.idFilial"/> 
	    
   		<adsm:hidden property="idDoctoServico" />
   		
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" labelWidth="19%" width="31%" 
					   service="lms.entrega.cancelarEntregaAlterarOcorrenciaAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"					  
					   onchange="var args = {
									   documentTypeElement:this, 
									   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
									   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
									   documentGroup:'DOCTOSERVICE',
									   parentQualifier:'doctoServico',
									   actionService:'lms.entrega.cancelarEntregaAlterarOcorrenciaAction'
									};
								var r = changeDocumentWidgetType(args); 				   
								resetDadosOcorrencia(this);
								return r;">
			
			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true" popupLabel="pesquisarDocumentoServico"
						 action="" 
						 size="3" maxLength="3" picker="false" 
						 onchange="var r = changeDocumentWidgetFilial({
											 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
											 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
										  	}); 
								  	resetDadosOcorrencia(this); 
								  	return r;"/>
						 											
			<adsm:hidden property="blBloqueado" value="N"/>
			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 onDataLoadCallBack="doctoServiceDataLoad"
						 onPopupSetValue="doctoServicePopup"
						 onchange="return nrDoctoServicoChange(this)"
						 service="" 
						 action="" 						 
						 size="12" maxLength="8" mask="00000000" serializable="true" disabled="true" popupLabel="pesquisarDocumentoServico">
				
				<adsm:propertyMapping criteriaProperty="blBloqueado" modelProperty="blBloqueado"/>					
			</adsm:lookup>		 
		</adsm:combobox> 

		<adsm:hidden property="doctoServico.clienteByIdClienteDestinatario.idCliente" />
		<adsm:hidden property="doctoServico.clienteByIdClienteConsignatario.idCliente" />
		<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" />
				
	
		<adsm:section caption="informacoesBaixa"/>			

			<adsm:textbox dataType="integer"  mask="000" property="cdOcorrenciaEntrega" size="6" width="41%" label="ocorrencia" labelWidth="19%" disabled="true">
				<adsm:textbox dataType="text" property="dsOcorrenciaEntrega" size="45"  disabled="true"/>	
			</adsm:textbox>
		 
			<adsm:hidden property="idOcorrenciaEntrega"/>			
			
			<adsm:textbox dataType="text" property="tpEntregaParcialDesc" size="16"  width="14%" label="entrega" labelWidth="12%" required="false" disabled="true"/>

			<adsm:textbox dataType="text" property="nmRecebedor" size="21"  width="31%" label="recebedor" labelWidth="19%" required="false" disabled="true"/>
									
			<adsm:textbox dataType="JTDateTimeZone" picker="false" property="dhBaixa" size="31"   width="27%" label="dataHoraOcorrencia" labelWidth="22%" disabled="true"/>
									
			<adsm:textarea property="obManifestoEntregaDocumento" label="observacao" maxLength="120" rows="2" columns="97"  labelWidth="19%" width="81%" disabled="true"/>
			
			<adsm:textarea property="obAlteracao" label="historico" maxLength="120" rows="4" columns="97"  labelWidth="19%" width="81%" disabled="true"/>
			
		<adsm:section caption="informacoesAlteracaoCancelamento"/>
									
			<adsm:lookup action="/entrega/manterOcorrenciasEntrega" 
						 service="lms.entrega.cancelarEntregaAlterarOcorrenciaAction.findOcorrenciaEntrega" 
						 dataType="integer" exactMatch="true" property="ocorrenciaEntrega" required="true"
						 onchange="return ocorrenciaChanged(this)"
						 idProperty="idOcorrenciaEntrega" criteriaProperty="cdOcorrenciaEntrega" criteriaSerializable="true"
						 size="3" mask="000" width="70%" label="ocorrencia" labelWidth="19%" picker="true">
						 
					<adsm:propertyMapping relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega" modelProperty="dsOcorrenciaEntrega"/>
					<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
					<adsm:textbox dataType="text" property="ocorrenciaEntrega.dsOcorrenciaEntrega" size="45" disabled="true" serializable="false"/>						
			</adsm:lookup>
			<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
			 
			<adsm:combobox property="complementoBaixaSPP" label="complementoBaixa" labelWidth="19%" width="41%" service="lms.expedicao.dadosComplementoService.findBaixaSPP" optionProperty="value" optionLabelProperty="description"/>				 
			 
			<adsm:combobox property="tipoEntregaParcial" label="entrega" labelWidth="14%" width="23%" domain="DM_TIPO_ENTREGA_PARCIAL" renderOptions="true"/>
			 
			<adsm:textbox dataType="text" property="nmRecebedor2" size="19"  width="60%" label="recebedor" labelWidth="19%" />
			
			<adsm:textarea property="obMotivoAlteracao" label="motivo" maxLength="120" rows="2" columns="97" labelWidth="19%" width="81%" required="true"/>
			
			<adsm:textbox dataType="text" property="nmUsuario" size="19" width="31%" label="usuario" disabled="true"  labelWidth="19%"/>
			
		<adsm:buttonBar>
			<adsm:button id="btOcorrenciaPorNota" caption="btOcorrenciaPorNota" onclick="abrirOcorrenciaPorNota('T')"/>
			<adsm:button id="confirmar" caption="confirmar" disabled="false" callbackProperty="confirmar" service="lms.entrega.cancelarEntregaAlterarOcorrenciaAction.executeCancelar"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
		<script>
			var notasMessage = '<adsm:label key="LMS-09127"/>';
		</script>
		
	</adsm:form>
</adsm:window>

<script>

	document.getElementById("tpSituacao").masterLink = "true";
	
	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;
	var nmUsuario;

	function initWindow(eventObj){
		setDisabled("confirmar", false);
		setDisabled("btOcorrenciaPorNota", true);
		if (eventObj.name == "cleanButton_click") {
			setaValoresFilial();		
			setDisabled("nmRecebedor", true);	
			setDisabled("doctoServico.idDoctoServico", true);	
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);	
			setDisabled("tipoEntregaParcial", true);
		}
	}
	
	function resetDoctoServico(){
		resetValue("doctoServico.tpDocumentoServico");
		resetValue("doctoServico.filialByIdFilialOrigem.idFilial");
		resetValue("doctoServico.idDoctoServico");
		
		setDisabled("doctoServico.tpDocumentoServico", true);
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial", true);
		setDisabled("doctoServico.idDoctoServico", true);
	}
	
	function confirmar_cb(data, error){
		if (error != undefined){
			alert(error);
			return;
		}
		
		if (getElementValue("ocorrenciaEntrega.cdOcorrenciaEntrega") == '102' && getElementValue("cdOcorrenciaEntrega") != '102' ) {
			abrirOcorrenciaPorNota('F');
		}
		
		cleanButtonScript(this.document);
		showSuccessMessage();
		
	}
	
	function ocorrenciaChanged(obj){
		if (obj.value == '1' || obj.value == '001' || obj.value == '01'){
			setDisabled("tipoEntregaParcial", false);
		}else{
			setDisabled("tipoEntregaParcial", true);	
		}
	}
	
	function pageLoadCustom_cb(){
		onPageLoad_cb();
		getFilialUsuario();
	}
		
	
	function filialChange(obj){
		var retorno = filial_sgFilialOnChangeHandler();
		
		if (obj.value == ''){		
			resetDoctoServico();
			resetDadosOcorrencia();
		}
			
		return retorno;
	}
	
	function filialDataLoad_cb(data){
		
		if (data != undefined && data.length == 1){
			resetDoctoServico();
			resetDadosOcorrencia();
			setDisabled("doctoServico.tpDocumentoServico", false);
		}
				
		filial_sgFilial_exactMatch_cb(data);				
	}
	
	
	function filialPopup(data){
		
		if (data != undefined){
			resetDoctoServico();
			resetDadosOcorrencia();
			setDisabled("doctoServico.tpDocumentoServico", false);
		}
		
		return true;
	}
		
	function setaValoresFilial() {
		
		setElementValue("filial.idFilial", idFilialLogado);
		setElementValue("filial.sgFilial", sgFilialLogado);
		setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);				
		setElementValue("nmUsuario", nmUsuario);
	}
	
	
	function getFilialUsuario() {
		var sdo = createServiceDataObject("lms.entrega.cancelarEntregaAlterarOcorrenciaAction.findFilialUsuarioLogado","getFilialCallBack",null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function getFilialCallBack_cb(data,error) {

		if (error != undefined) {
			alert(error);
			return false;
		}
		
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			nmUsuario = getNestedBeanPropertyValue(data,"nmUsuario");

			setDisabled("filial.idFilial", !eval(data.isUsuarioDIVOP));
			setDisabled("nmUsuario", !eval(data.isUsuarioDIVOP));
			
			setaValoresFilial();
		}
	}
	
	function doctoServiceDataLoad_cb(data){
		doctoServico_nrDoctoServico_exactMatch_cb(data); 

		if (data != undefined && data.length == 1){		
			resetDadosOcorrencia();
			findDadosDoctoServico(data[0].idDoctoServico);
		}
	}
	
	function doctoServicePopup(data){
		if (data != undefined){
			resetDadosOcorrencia();
			if (getElementValue("doctoServico.tpDocumentoServico") == 'CRT')
				property = 'idCtoInternacional';
			else
				property = 'idDoctoServico';

			findDadosDoctoServico(getNestedBeanPropertyValue(data, property));
		}
		
		return true;
	}
	
	
	function findDadosDoctoServico(idDoctoServico){
		var data = new Array();
		
		setNestedBeanPropertyValue(data, "doctoServico.idDoctoServico", idDoctoServico);
		setElementValue("idDoctoServico", idDoctoServico);
		
		var sdo = createServiceDataObject("lms.entrega.cancelarEntregaAlterarOcorrenciaAction.findLastManifestoEntrega","findDadosDoctoServico",data);
		xmit({serviceDataObjects:[sdo]});
	}	
	
	function nrDoctoServicoChange(obj){
		var retorno = doctoServico_nrDoctoServicoOnChangeHandler();
		
		if (obj.value == ''){
			resetDadosOcorrencia();
		}
		
		return retorno;
	}
	
	function findDadosDoctoServico_cb(data, error){
		if (error != undefined){
			alert(error);
			resetValue("doctoServico.idDoctoServico");
			setFocus("doctoServico.nrDoctoServico");
			return;
		}
		
		if (data.idFilialOrigemManifesto == null || 
				data.idFilialOrigemManifesto != data.idFilialSessao){
			alert("LMS-09086: " + i18NLabel.getLabel("LMS-09086"));
			resetValue("doctoServico.idDoctoServico");
			setFocus("doctoServico.nrDoctoServico");
			return;
		}
		
		if(data.hasNota == "true"){
			
			var option = window.confirm("LMS-09127: " + notasMessage);
			if(option){
				if (data != undefined){
					setElementValue("manifestoEntregaDocumento.idManifestoEntregaDocumento", data.idManifestoEntregaDocumento);				
					setElementValue("manifestoEntrega.nrManifestoEntrega", setFormat(document.getElementById("manifestoEntrega.nrManifestoEntrega"), data.nrManifestoEntrega));				
					setElementValue("manifestoEntrega.idManifestoEntrega", data.idManifestoEntrega);				
					setElementValue("manifestoEntrega.filial.sgFilial", data.sgFilial);			
					setElementValue("manifestoEntrega.filial.idFilial", data.idFilial);
					
					setElementValue("controleCarga.idControleCarga", data.idControleCarga);			
					setElementValue("controleCarga.nrControleCarga", setFormat(document.getElementById("controleCarga.nrControleCarga"),data.nrControleCarga));			
					setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data.sgFilialControleCarga);		
					setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data.idFilialControleCarga);	
					
					setElementValue("cdOcorrenciaEntrega", setFormat(document.getElementById("cdOcorrenciaEntrega"),data.cdOcorrenciaEntrega));
					setElementValue("dsOcorrenciaEntrega", data.dsOcorrenciaEntrega);
					setElementValue("idOcorrenciaEntrega", data.idOcorrenciaEntrega);			
					setElementValue("dhBaixa", setFormat(document.getElementById("dhBaixa"), data.dhBaixa));
					setElementValue("nmRecebedor", data.nmRecebedor);
					setElementValue("nmRecebedor2", data.nmRecebedor);
					setElementValue("obManifestoEntregaDocumento", data.obManifestoEntregaDocumento);
					setElementValue("obAlteracao", data.obAlteracao);	
					setElementValue("tpEntregaParcialDesc", data.tpEntregaParcialDesc);
				}
			}
			
		}else{
			if (data != undefined){
				setElementValue("manifestoEntregaDocumento.idManifestoEntregaDocumento", data.idManifestoEntregaDocumento);				
				setElementValue("manifestoEntrega.nrManifestoEntrega", setFormat(document.getElementById("manifestoEntrega.nrManifestoEntrega"), data.nrManifestoEntrega));				
				setElementValue("manifestoEntrega.idManifestoEntrega", data.idManifestoEntrega);				
				setElementValue("manifestoEntrega.filial.sgFilial", data.sgFilial);			
				setElementValue("manifestoEntrega.filial.idFilial", data.idFilial);
				
				setElementValue("controleCarga.idControleCarga", data.idControleCarga);			
				setElementValue("controleCarga.nrControleCarga", setFormat(document.getElementById("controleCarga.nrControleCarga"),data.nrControleCarga));			
				setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", data.sgFilialControleCarga);		
				setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", data.idFilialControleCarga);	
				
				setElementValue("cdOcorrenciaEntrega", setFormat(document.getElementById("cdOcorrenciaEntrega"),data.cdOcorrenciaEntrega));
				setElementValue("dsOcorrenciaEntrega", data.dsOcorrenciaEntrega);
				setElementValue("idOcorrenciaEntrega", data.idOcorrenciaEntrega);			
				setElementValue("dhBaixa", setFormat(document.getElementById("dhBaixa"), data.dhBaixa));
				setElementValue("nmRecebedor", data.nmRecebedor);
				setElementValue("nmRecebedor2", data.nmRecebedor);
				setElementValue("obManifestoEntregaDocumento", data.obManifestoEntregaDocumento);
				setElementValue("obAlteracao", data.obAlteracao);	
				setElementValue("tpEntregaParcialDesc", data.tpEntregaParcialDesc);
			}
		}	
		
		if (getElementValue("cdOcorrenciaEntrega") == '102') {
			setDisabled("btOcorrenciaPorNota", false);
		} else {
			setDisabled("btOcorrenciaPorNota", true);
		}
		
	}
	
	function abrirOcorrenciaPorNota(alteracao) {
		var idDoctoServico = getElementValue("doctoServico.idDoctoServico");
		var idManifesto    = getElementValue("manifestoEntrega.idManifestoEntrega");
		
		var url = 'view/swt/index#/app/entrega/registrarBaixaEntregaPorNotaFiscal/detalhe/'+ idDoctoServico + '&' + idManifesto + '&E&' + alteracao;
		var wProperties = 'unardorned:no;scroll:yes;resizable:yes;status:no;center=yes;help:no;dialogWidth:1500px;dialogHeight:780px;';
		showModalDialog(url, window, wProperties);
	}
	
	function resetDadosOcorrencia(){
		
		resetValue("manifestoEntregaDocumento.idManifestoEntregaDocumento");				
		resetValue("manifestoEntrega.nrManifestoEntrega");				
		resetValue("manifestoEntrega.idManifestoEntrega");				
		resetValue("manifestoEntrega.filial.sgFilial");			
		resetValue("manifestoEntrega.filial.idFilial");
			
		resetValue("controleCarga.idControleCarga");			
		resetValue("controleCarga.nrControleCarga");			
		resetValue("controleCarga.filialByIdFilialOrigem.sgFilial");		
		resetValue("controleCarga.filialByIdFilialOrigem.idFilial");	
			
		resetValue("cdOcorrenciaEntrega");
		resetValue("dsOcorrenciaEntrega");
		resetValue("idOcorrenciaEntrega");			
		resetValue("dhBaixa");
		resetValue("nmRecebedor");
		resetValue("nmRecebedor2");
		resetValue("tpEntregaParcialDesc");
		resetValue("obManifestoEntregaDocumento");
		resetValue("obAlteracao");	

		resetValue("ocorrenciaEntrega.idOcorrenciaEntrega");
		resetValue("obMotivoAlteracao");
		
		resetValue("idDoctoServico");

		setDisabled("nmRecebedor", true);
	}
		
	
</script>