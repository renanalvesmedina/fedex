<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterReembolsos" service="lms.entrega.manterReembolsosAction" onPageLoadCallBack="findFilialUsuarioLogado">
	<adsm:form action="/entrega/manterReembolsos" idProperty="idDoctoServico" height="103" >
		<adsm:i18nLabels>
                <adsm:include key="LMS-09084"/>
                <adsm:include key="LMS-09085"/>
                <adsm:include key="filialOrigem"/>
                <adsm:include key="filialDestino"/>
                <adsm:include key="numeroReembolso"/>
                <adsm:include key="documentoServico"/>
                <adsm:include key="periodoEmissao"/>
        </adsm:i18nLabels>
		 
		<adsm:hidden property="idFilialSessao"/>
		<adsm:hidden property="sgFilialSessao"/>		
		<adsm:hidden property="tpEmpresa" value="M"/>
		<adsm:hidden property="tpDocumentoServico" serializable="false"/>
		<adsm:hidden property="idFilialDoctoSer" serializable="false"/>
		<adsm:hidden property="filialByIdFilialDestino.idFilial"/>
		
		<adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filialByIdFilialOrigem" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.entrega.manterReembolsosAction.findLookupFilial" 
	        label="filialOrigem" labelWidth="19%" size="3" width="31%" required="false" maxLength="3">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" disable="false"/>
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
              
        <adsm:lookup 
	        action="/municipios/manterFiliais" 
	        dataType="text" 
	        property="filialDestino" 
	        idProperty="idFilial" 
	        criteriaProperty="sgFilial" 
	        service="lms.entrega.manterReembolsosAction.findLookupFilial" 
	        label="filialDestino" labelWidth="19%" size="3" width="31%" required="false" maxLength="3">
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" disable="false"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
		<adsm:textbox dataType="integer" property="nrReciboReembolso" mask="00000000" label="numeroReembolso" maxLength="8" size="16" labelWidth="19%" width="31%"/>
		
		<adsm:range label="periodoEmissao" labelWidth="19%" width="81%" required="false" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="periodoEmissaoInicial"/>
			<adsm:textbox dataType="JTDate" property="periodoEmissaoFinal"/>
		</adsm:range>
		
		<adsm:hidden property="idDoctoReembolso" serializable="true"/>
		<adsm:hidden property="doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.pessoa.nmFantasia" />			
		<adsm:combobox property="doctoServicoByIdDoctoServReembolsado.tpDocumentoServico" 
					   label="documentoServico" labelWidth="19%" width="81%" 
					   service="lms.entrega.manterReembolsosAction.findTipoDocumentoServico" 
					   renderOptions="true"
					   optionProperty="value" optionLabelProperty="description"
					   onchange="resetValue('idDoctoReembolso'); return changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('doctoServicoByIdDoctoServReembolsado.idDoctoServico'), 
						   parentQualifier:'doctoServicoByIdDoctoServReembolsado', 
						   documentGroup:'DOCTOSERVICE',
						   actionService:'lms.entrega.manterReembolsosAction'
					   });">
					   
			<adsm:lookup dataType="text"
						 property="doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true"
						 action="" 
						 size="3" maxLength="3" picker="false" 
						 onchange="return changeDocumentWidgetFilial({
						   filialElement:document.getElementById('doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('doctoServicoByIdDoctoServReembolsado.idDoctoServico')
						   });">
			</adsm:lookup>					   

			<adsm:lookup dataType="integer" 
						 property="doctoServicoByIdDoctoServReembolsado" 
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 onDataLoadCallBack="retornoDocumentoServico"
						 size="12" maxLength="8" mask="00000000" serializable="true" disabled="true" popupLabel="pesquisarDocumentoServico"/>
		</adsm:combobox>
		
		 <adsm:hidden property="doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.pessoa.nmFantasia"/>
        
		<adsm:lookup service="lms.entrega.manterReembolsosAction.findLookupCLiente" dataType="text" property="clienteByIdClienteRemetente" relatedCriteriaProperty="nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="remetente" size="20" maxLength="20" labelWidth="19%" width="81%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping relatedProperty="clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="clienteByIdClienteRemetente.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	<adsm:textbox dataType="text" property="clienteByIdClienteRemetente.pessoa.nmPessoa" size="30" maxLength="50" disabled="true" />
        </adsm:lookup>
        
               
       <adsm:lookup service="lms.entrega.manterReembolsosAction.findLookupCLiente" dataType="text" property="clienteByIdClienteDestinatario" relatedCriteriaProperty="nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="destinatario" size="20" maxLength="20" labelWidth="19%" width="81%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="clienteByIdClienteDestinatario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	<adsm:textbox dataType="text" property="clienteByIdClienteDestinatario.pessoa.nmPessoa" size="30" maxLength="50" disabled="true" />
        </adsm:lookup>
  		
		<adsm:combobox label="situacaoRecibo" property="tpSituacaoRecibo" 
		               domain="DM_STATUS_RECIBO_REEMBOLSO" labelWidth="19%" width="81%"
		               renderOptions="true"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="__buttonBar:0.findButton" disabled="false" onclick="validateDados();" buttonType="findButton"/>
		<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="reciboReembolso" idProperty="idDoctoServico" scrollBars="horizontal" onRowClick="habilitaAbaDetalhe" 
	           unique="true" rows="9" gridHeight="180"  selectionMode="none">
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="numeroReembolso" property="filialByIdFilialOrigem.sgFilial" width="70" />
			<adsm:gridColumn title="" property="nrDoctoServico" dataType="integer" mask="00000000" width="65" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn width="100" dataType="JTDate" title="dataEmissao" property="dhEmissao" />
		
		
		 <adsm:gridColumn title="documentoServico" property="tpDocumentoServico" width="50" isDomain="true"/>
		 <adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="" property="sgFilialDoc" width="50" />
			<adsm:gridColumn title="" property="nrDoctoServicoRembolsado" dataType="integer" mask="00000000" width="50" />
		</adsm:gridColumnGroup>	
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn property="sgFilialOper" width="50" title="filialDestino"/>	
			<adsm:gridColumn property="nmPessoaDestFilOper" width="135" title=""/>	
		</adsm:gridColumnGroup> 
		
		<adsm:gridColumn width="50"  title="identificacao" property="tpIdentificacaoRemetente" isDomain="true" />
		<adsm:gridColumn width="150" title="" property="nrIdentificacaoRemetente" align="right"/>
		<adsm:gridColumn width="200" title="remetente" property="nmPessoaRemetente" />
		<adsm:gridColumn width="50"  title="identificacao" property="tpIdentificacaoDestinatario" isDomain="true"/>
		<adsm:gridColumn width="150" title="" property="nrIdentificacaoDestinatario" align="right"/>
		<adsm:gridColumn width="200" title="destinatario" property="nmPessoaDestinatario" />
		<adsm:gridColumn width="150" title="situacaoRecibo" property="tpSituacaoRecibo" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="btnFechar" onclick="self.close();" buttonType="closeButton" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window> 
<script>

	isLookup = window.dialogArguments && window.dialogArguments.window;
	if (isLookup) {
		document.getElementById('btnFechar').property = ".closeButton";
		setDisabled('btnFechar',false);
	} else {
		setVisibility('btnFechar', false);
	}

	
document.getElementById("idFilialSessao").masterLink="true";

function findFilialUsuarioLogado_cb(data,exception){
		onPageLoad_cb(data,exception);
		findFilialUsuario();
} 
function findFilialUsuario(){
		_serviceDataObjects = new Array();
	   	addServiceDataObject(createServiceDataObject("lms.entrega.manterReembolsosAction.findFilialUsuarioLogado", "onDataLoad", new Array()));
		xmit();
}
 
function onDataLoad_cb(data,erro){
	if (!isLookup) { 
		fillFormWithFormBeanData(0, data);
	}else {
		setElementValue('idFilialSessao',getNestedBeanPropertyValue(data,"idFilialSessao"));
	}			
}
	
	 
function habilitaAbaDetalhe(id){
	var isLookup = window.dialogArguments && window.dialogArguments.window;
    if (!isLookup) {
		var tabGroup = getTabGroup(document);
		tabGroup.setDisabledTab("cad", false);
		tabGroup.selectTab("cad",{name:'tab_click'},true);
	}	
	
	return true; 
}
/**
 * Quando o "Número do recibo" for informado
 */
function retornoDocumentoServico_cb(data) {
    var r = doctoServicoByIdDoctoServReembolsado_nrDoctoServico_exactMatch_cb(data);
   	if (r == true) {
		var idDoctoServico = getNestedBeanPropertyValue(data,":0.idDoctoServico");
		setElementValue('idDoctoReembolso', idDoctoServico);
	}
	return r;
}

function doctoServicoByIdDoctoServReembolsado_OnChange() {
	
	if ( getElementValue('doctoServicoByIdDoctoServReembolsado.nrDoctoServico') == '' ) {
		resetValue("doctoServicoByIdDoctoServReembolsado.idDoctoServico");
	}
	return doctoServicoByIdDoctoServReembolsado_nrDoctoServicoOnChangeHandler();
}

function doctoServicoNrReciboReembolso_retornoPopup(data) {
	return true;
}

//lookup de filial
function doctoServicoByIdDoctoServReembolsado_cb(data) {
   var r = doctoServicoByIdDoctoServReembolsado_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      setDisabled("doctoServicoByIdDoctoServReembolsado.idDoctoServico", false);
      setFocus(document.getElementById("doctoServicoByIdDoctoServReembolsado.nrDoctoServico"));
   }
} 

function initWindow(eventObj){
	if(eventObj.name=="tab_click"){
		var tabGroup = getTabGroup(this.document);	
		tabGroup.setDisabledTab('cad',true);
		
		
		tabGroup.getTab('cheq').getElementById("chequeReembolso"+".form").disabled = false;
		tabGroup.getTab('cheq').getElementById("chequeReembolso"+".chkSelectAll").disabled = false;
		
		tabGroup.setDisabledTab('cheq',true);
	}
	
	if(eventObj.name=="cleanButton_click"){
		setDisabled("doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.idFilial","true");
		setDisabled("doctoServicoByIdDoctoServReembolsado.idDoctoServico","true");
		findFilialUsuario();
	}
}

function validateDados() {
		var ret = validateTab();
		if(ret == true)
			findButtonScript('reciboReembolso', document.forms[0]);
		return false;
}

function validateTab() {
		if (validateTabScript(document.forms)) {
			 if (
					((getElementValue("filialByIdFilialOrigem.idFilial") != "" || getElementValue("filialDestino.idFilial") != "") && getElementValue("nrReciboReembolso") != "") ||
					((getElementValue("filialByIdFilialOrigem.idFilial") != "" || getElementValue("filialDestino.idFilial") != "") && getElementValue("doctoServicoByIdDoctoServReembolsado.idDoctoServico") != "")||
					((getElementValue("filialByIdFilialOrigem.idFilial") != "" || getElementValue("filialDestino.idFilial") != "") && ((getElementValue("periodoEmissaoInicial") != "" ) || (getElementValue("periodoEmissaoFinal") != "" ))))
					
				{
					if(getElementValue("sgFilialSessao")!= 'MTZ'){
						if (getElementValue("filialByIdFilialOrigem.idFilial") != "" && getElementValue("filialDestino.idFilial") != ""){
						    
						    if(getElementValue("filialByIdFilialOrigem.idFilial")!= getElementValue("idFilialSessao") && getElementValue("filialDestino.idFilial") != getElementValue("idFilialSessao")){
								alert(i18NLabel.getLabel("LMS-09085"));
							} else 
								return true;
								
						}else if(getElementValue("filialByIdFilialOrigem.idFilial") != "" && getElementValue("filialByIdFilialOrigem.idFilial")!= getElementValue("idFilialSessao")){
								alert(i18NLabel.getLabel("LMS-09085"));
								
						}else if(getElementValue("filialDestino.idFilial") != "" && getElementValue("filialDestino.idFilial") != getElementValue("idFilialSessao")){		
							    alert(i18NLabel.getLabel("LMS-09085"));
							    	
						}else {
							return true;
						}
					}else
						return true;	
				} else {
					alert(i18NLabel.getLabel("LMS-09084"));
					return false;
                }
        }
		return false;
}	

</script>