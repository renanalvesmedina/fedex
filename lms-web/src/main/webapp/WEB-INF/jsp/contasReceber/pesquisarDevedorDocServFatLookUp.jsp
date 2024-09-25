<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.pesquisarDevedorDocServFatLookUpAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/contasReceber/pesquisarDevedorDocServFatLookUp"  idProperty="idDevedorDocServFat"
			service="lms.contasreceber.pesquisarDevedorDocServFatLookUpAction">

		<adsm:hidden property="nrDocumento" serializable="true"/>
		<adsm:hidden property="tpDocumentoHidden"/>
		<adsm:hidden property="tpDocumentoServicoHidden"/>

		<adsm:hidden property="idServico" serializable="true"/>
		<adsm:hidden property="tpModal" serializable="true"/>
		<adsm:hidden property="tpFrete" serializable="true"/>
		<adsm:hidden property="tpAbrangencia" serializable="true"/>
		<adsm:hidden property="idMoeda" serializable="true"/>
		<adsm:hidden property="idDivisaoCliente" serializable="true"/>
		<adsm:hidden property="tpSituacaoDevedorDocServFatValido" serializable="true"/>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-36213"/>
		</adsm:i18nLabels>
		
		<adsm:combobox property="doctoServico.tpDocumentoServico"
					   label="documentoServico" 
					   width="40%"
					   service="lms.contasreceber.pesquisarDevedorDocServFatLookUpAction.findTipoDocumentoServico"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   onchange="return onChangeTpDocumentoServico();"> 

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service=""
						 disabled="true"
						 action=""
						 size="3" 
						 maxLength="3" 
						 picker="false" 
						 exactMatch="true"
						 popupLabel="pesquisarFilial"
						 onDataLoadCallBack="enableDevedorDocServFatDoctoServico"						 
						 onchange="return onChangeFilial();"/>										 
			
			<adsm:lookup dataType="integer"
						 property="doctoServico"
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 size="10" 
						 onPopupSetValue="myOnPopupDoctoServico"
						 popupLabel="pesquisarDocumentoServico"
						 maxLength="8" 
						 serializable="true" 
						 disabled="true">
			</adsm:lookup>			 
		</adsm:combobox>
		
		<adsm:combobox property="tpSituacaoCobranca" 
					   label="estadoCobranca" 
					   labelWidth="15%"
  					   width="30%" 
					   domain="DM_STATUS_COBRANCA_DOCTO_SERVICO"/>	

		<adsm:lookup property="cliente" 
		             idProperty="idCliente" 
		             dataType="text"
		             service="lms.contasreceber.pesquisarDevedorDocServFatLookUpAction.findLookupCliente" 
		             action="/vendas/manterDadosIdentificacao"
		             criteriaProperty="pessoa.nrIdentificacao" 
		             relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		             label="cliente" 
		             size="17" 
		             maxLength="20" 
		             exactMatch="true" 
		             labelWidth="15%" 
		             width="40%">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" serializable="false"/>			
		</adsm:lookup>
		
		<adsm:range label="dataEmissao" width="30%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" size="10" maxLength="20"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" size="10" maxLength="20"/>
		</adsm:range>
		
		

 		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="btnConsultar" buttonType="findButton" onclick="verificaCampos(this)" disabled="false"/>
			<adsm:button buttonType="cleanButton" caption="limpar" onclick="myCleanButtonScript(this);" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idDevedorDocServFat" property="notas" rows="13"
			service="lms.contasreceber.pesquisarDevedorDocServFatLookUpAction.findPaginatedDevedorDocServFat"
			rowCountService="lms.contasreceber.pesquisarDevedorDocServFatLookUpAction.getRowCountDevedorDocServFat">
			
		<adsm:gridColumn width="30" title="documentoServico" isDomain="true" property="tpDocumentoServico"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="105" title="" property="sgFilialOrigem"/>	
			<adsm:gridColumn width="0" title="" property="doctoServico.nrDoctoServico"/>
		</adsm:gridColumnGroup>	
			
		<adsm:gridColumn title="filialCobranca" property="sgFilial" width="125" dataType="text" />
		<adsm:gridColumn title="cliente" property="nrIdentRespAntNmResAnt"  dataType="text" />
		
				
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="5" title="valorDevido" property="sgMoeda"/>	
			<adsm:gridColumn width="40" title="" property="dsSimbolo" />
		</adsm:gridColumnGroup>							
		
		<adsm:gridColumn title="" property="vlDevido" width="60" dataType="currency" align="right"/>	
			
		<adsm:gridColumn title="situacaoCobranca" property="tpSituacaoCobranca" width="130" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
<script src="../lib/utils/tagDevedor.js" type="text/javascript"></script>
<script src="/<%=request.getContextPath().substring(1)%>/lib/formatNrDocumento.js" type="text/javascript"></script>
<script>

	getElement('tpSituacaoDevedorDocServFatValido').masterLink = 'true';
	getElement("doctoServico.nrDoctoServico").serializable = 'true';	

	function myCleanButtonScript(elem){
	
		//getElement("doctoServico.idDoctoServico").masterLink = 'false';
		//getElement("doctoServico.nrDoctoServico").masterLink = 'false';

		cleanButtonScript(elem.document, true);		
		resetDoctoServico();	
		setFocusOnFirstFocusableField(document);
		
	}

	function myOnPageLoad_cb(d,e,c,x){
		onPageLoad_cb(d,e,c,x);

		if( getElementValue('doctoServico.nrDoctoServico') != "" ){		
			getElement("doctoServico.idDoctoServico").masterLink = 'true';
			getElement("doctoServico.nrDoctoServico").masterLink = 'true';
		} else {
			getElement("doctoServico.idDoctoServico").masterLink = 'false';
			getElement("doctoServico.nrDoctoServico").masterLink = 'false';
		}
		
		desabilitaDoctoServico(false);	
		
		onChangeTpDocumentoServico();
		onChangeFilial();

		//setDisabledLookUp(getElement("doctoServico.idDoctoServico"), false, document);
		
		if (d != undefined){		
			setElementValue("doctoServico.nrDoctoServico",d);
		}
		
		setFocusOnFirstFocusableField(document);
		
	}

	/**
	*	Após a busca padrão do combo de Tipo de Documento de Serviço seta a descrição do tipo no campo hidden tpDocumentoHidden
	*
	*/
	function onChangeTpDocumentoServico() {
		var comboTpDocumentoServico = document.getElementById("doctoServico.tpDocumentoServico");
		setElementValue('tpDocumentoHidden', comboTpDocumentoServico.options[comboTpDocumentoServico.selectedIndex].text);
		var retorno = changeDocumentWidgetType({
									   documentTypeElement:document.getElementById("doctoServico.tpDocumentoServico"),
									   filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
									   documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
									   parentQualifier:'',
									   documentGroup:'SERVICE',
									   actionService:'lms.contasreceber.pesquisarDevedorDocServFatLookUpAction'
					   });		

		resetValue('doctoServico.filialByIdFilialOrigem.idFilial');			
		onChangeFilial()
		setMaskNrDocumento("doctoServico.nrDoctoServico", getElementValue("doctoServico.tpDocumentoServico"));					   
		
		return retorno;				   
	}
	
	function myOnPopupDoctoServico(d,e){
		if (d != undefined){
			setDisabled("doctoServico.idDoctoServico", false);
			setDisabled("doctoServico.nrDoctoServico", false);
		}
		return true;
	}
	
	function onChangeFilial(){
	
		var siglaFilial = getElementValue('doctoServico.filialByIdFilialOrigem.sgFilial');
		var siglaAnterior = getElement('doctoServico.filialByIdFilialOrigem.sgFilial').previousValue;
	
		var retorno = changeDocumentWidgetFilial({
										 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
										 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')									 
										 });	
										 
		resetDoctoServico();	
		
		if (siglaAnterior != '' && siglaFilial == '' ){
			habilitaLupa();		
		}
												 
		return retorno;										 
	}
	
		/**
	*	Busca a filial origem e habilita a lookup de documento serviço
	*
	*/	
	function enableDevedorDocServFatDoctoServico_cb(data) {
	   var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}
	
	function verificaCampos(elem){
	
		var doctoServico = getElementValue('doctoServico.idDoctoServico');
		var cliente = getElementValue('cliente.idCliente');
		
		if( doctoServico == '' ){
			doctoServico = getElementValue("doctoServico.nrDoctoServico");
		}
		
		if( doctoServico == '' && cliente == '' ){
			alert(i18NLabel.getLabel('LMS-36213'));
			setFocusOnFirstFocusableField(document);
			return false;
		}		
	
		findButtonScript('notas', elem.form);
	}	


	function resetDoctoServico(){
	
		resetValue("doctoServico.idDoctoServico");
		resetValue("doctoServico.nrDoctoServico");

		if (getElementValue("doctoServico.tpDocumentoServico") == "") {
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
			setDisabled("doctoServico.idDoctoServico",true);	
		} else {
			setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",false);
			habilitaLupa();
			if (getElementValue("doctoServico.filialByIdFilialOrigem.sgFilial") != ""){
				setDisabled("doctoServico.idDoctoServico",false);	
			}
		}		

	}
	
	/*
		Habilita a lupa de documento de serviço mesmo com o número do documento desabilitado
	*/
	function habilitaLupa() {
		setDisabled("doctoServico.idDoctoServico",false);	
		setDisabled("doctoServico.nrDoctoServico", true);
	}
	
	/**
	*	Habilita/Desabilita tag Documento de serviço
	*/
	function desabilitaDoctoServico(desabilitar){
	
		if( desabilitar == true || desabilitar == false ){
	
			setDisabled(getElement('doctoServico.tpDocumentoServico'),desabilitar);			
			
			if( desabilitar == true ){			
				setDisabled(getElement('doctoServico.filialByIdFilialOrigem.idFilial'),desabilitar);
				setDisabled(getElement('doctoServico.idDoctoServico'),desabilitar);
			}
		}
	}

</script>