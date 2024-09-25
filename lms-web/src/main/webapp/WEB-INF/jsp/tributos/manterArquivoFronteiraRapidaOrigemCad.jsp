<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.tributos.manterArquivoFronteiraRapidaOrigemAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:i18nLabels>
		<adsm:include key="LMS-23009"/>
		<adsm:include key="LMS-23027"/>
	</adsm:i18nLabels>

	<adsm:form action="/tributos/manterArquivoFronteiraRapidaOrigem" id="manifestoForm">

		<adsm:section caption="manifestoViagem" width="100%" />
		
		<adsm:hidden property="naoConformidade.doctoServico.idDoctoServico" />
		
		<adsm:hidden property="manifesto.tpManifesto" value="VN"/>		
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia"/>
		<adsm:lookup label="manifesto"
					 labelWidth="20%"
					 dataType="text"
					 property="manifesto.filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="" 
					 action="" 
					 size="3" maxLength="3" picker="false" 
					 disabled="true" 
					 serializable="false" 
					 required="true" 
					 onDataLoadCallBack="enableManifestoViagemNacional"
					 onchange="return myFilialOnChange();">
			<adsm:propertyMapping relatedProperty="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" 
			                      modelProperty="pessoa.nmFantasia"/>		 
			<adsm:lookup dataType="integer" 
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" 
						 criteriaSerializable="true"
						 size="10" 
						 popupLabel="pesquisarManifestoViagem"
						 maxLength="8" 
						 mask="00000000" 
						 disabled="false" 
						 onchange="return myNrManifestoOnChange(this)"
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadCallBackManifestoViagemNacional"
						 serializable="true"/>				
		</adsm:lookup>		

		<adsm:textbox label="dataEmissao"
			labelWidth="20%"
			dataType="JTDate"
			picker="false"
			property="dtEmissao"
			width="30%"
			disabled="true"/>

		<adsm:textbox label="situacao"
			labelWidth="20%" 
			dataType="text"
			property="dstpStatusManifesto" 
			width="30%"
			disabled="true"/>
		<adsm:hidden property="tpStatusManifesto" serializable="true" />

		<adsm:hidden property="naoConformidade.conhecimento.tpDocumentoServico" value="CTR" serializable="true"/>			
		<adsm:section caption="conhecimento" width="100%" />

		<!-- TAG CONHECIMENTO - DOCTO_SERVICO -->
		<adsm:lookup label="conhecimento" labelWidth="20%" width="30%"
					 dataType="text"
					 property="naoConformidade.doctoServico.filialByIdFilialOrigem"
					 idProperty="idFilial" criteriaProperty="sgFilial"
					 service=""
					 popupLabel="pesquisarFilial"
					 disabled="true"
					 action=""
					 size="3" 
					 maxLength="3" 
					 picker="false" 
					 onDataLoadCallBack="enableDoctoServico"
					 onchange="return filialDoctoServicoOnChange()">
			<adsm:propertyMapping relatedProperty="naoConformidade.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" 
			                      modelProperty="pessoa.nmFantasia"/>	
			<adsm:lookup dataType="integer"
						 property="naoConformidade.doctoServico"
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						 service=""
						 action=""
						 onDataLoadCallBack=""
						 onPopupSetValue="myOnPopupSetValueDoctoServico"
						 popupLabel="pesquisarConhecimento"
						 size="10" 
						 maxLength="8" 
						 serializable="true" 
						 disabled="true" 
						 mask="00000000">
			</adsm:lookup>	          
		</adsm:lookup>	
		<adsm:hidden property="naoConformidade.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia"/>

		<adsm:combobox label="incluidoFronteiraRapida" 
			labelWidth="20%"
			property="incluidoFronteiraRapida" 
			domain="DM_SIM_NAO" 
			width="30%" />
	
		<adsm:buttonBar freeLayout="true">
		
			<adsm:button caption="consultar" onclick="validateConhecimento(this.form)" 
				buttonType="findButton" disabled="false" />
	
			<adsm:resetButton />
		
		</adsm:buttonBar>
		
	</adsm:form>
			
	<adsm:grid selectionMode="check" 
		idProperty="idManifestoNacionalCto" 
		property="manifestos" 
		onRowClick="returnFalse();"
		onSelectRow="habilitaFronteiraRapida"
		onSelectAll="habilitaFronteiraRapida"
		service="lms.tributos.manterArquivoFronteiraRapidaOrigemAction.findPaginatedByManifestoViagemNacional"
		rowCountService="lms.tributos.manterArquivoFronteiraRapidaOrigemAction.getRowCountByManifestoViagemNacional"
		gridHeight="200" 
		unique="true" 
		rows="9">

		<adsm:gridColumn title="responsavel" 
			property="responsavel"
			width="30%" />
			
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="conhecimento" property="sgFilial"/>	
			<adsm:gridColumn width="130" title="" property="nrConhecimento" />
		</adsm:gridColumnGroup>		

		<adsm:gridColumn title="dataEmissao" 
			property="data_emissao" 
			dataType="JTDateTimeZone"
			width="15%" />

		<adsm:gridColumn title="valor" 
			property="valor" 
			dataType="currency"
			width="15%" />

		<adsm:gridColumn title="incluidoFronteiraRapida" 
			property="situacao"
			width="20%"
			renderMode="image-check"/>


		<adsm:buttonBar>
			<adsm:button caption="incExcFronteiraRapida" id="gridStoreButton" onclick="incluirFR();" disabled="true" buttonType="removeButton"/>
		</adsm:buttonBar>

	</adsm:grid>

</adsm:window>

<script>

	// Seta o campo nrManifesto como campo obrigatório
	document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").required = 'true';
	
	/**
	*	Habilita/desabilita o botão de Incluir no arquivo Fronteira Rápida
	*
	*/
	function habilitaFronteiraRapida(todos){
		
		var ids = manifestosGridDef.getSelectedIds();
		var status = false;
		
		if( todos == true || todos == false ) {
			status = todos;
		} else {
			status = (ids.ids.length > 0);
		}
		
		if( status == true ){
			setDisabled('gridStoreButton',false);
		} else {
			setDisabled('gridStoreButton',true);
		}
				
	}
	
	/** Função responsavel por realizar update nos registros selcionados na grid */
	function incluirFR() {
	
		// Caso tpStatusManifesto seja CA, exibir a mensagem LMS-23027
		if(getElementValue("tpStatusManifesto") == "CA"){
			alert(i18NLabel.getLabel("LMS-23027"));
			return false;
		}
		
		var mapIds = manifestosGridDef.getSelectedIds();
	
		var remoteCall = {serviceDataObjects:new Array()};
		var gridCall = createServiceDataObject("lms.tributos.manterArquivoFronteiraRapidaOrigemAction.storeConhecimentosFronteiraRapida", "incluirFR", mapIds);
		remoteCall.serviceDataObjects.push(gridCall);
		xmit(remoteCall);
	}
	
	
	/** Função responsável por realizar a atualização dos dados no callBack */
	function incluirFR_cb(data, error, erromsg) {
		if (error == null) {
			showSuccessMessage();
			manifestosGridDef.executeLastSearch();			
		}
	}
	
	/** Função responsável por validar se os campos de conhecimento ou o campo incluído na fronteira rápida está preenchedo */
	function validateConhecimento(form){
	
		/** Valida os campos obrigatórios da Tab para depois fazer a minha validação */
		if ( validateTabScript(form) == false ){
			
			if(getElementValue("manifesto.filialByIdFilialOrigem.sgFilial") == ""){
				setFocus("manifesto.filialByIdFilialOrigem.sgFilial");
			}else{	
				setFocus("manifesto.manifestoViagemNacional.nrManifestoOrigem");
			}
			
			return false;
		}
		
		/** Valida os campos de conhecimento ou o de inclui´do na fronteira rápida estão preenchidos */
		if((getElementValue("naoConformidade.doctoServico.idDoctoServico") != "" ) ||
						document.getElementById("incluidoFronteiraRapida").selectedIndex != 0 ){
			findButtonScript('manifestos', form);
		}else{
			alert(i18NLabel.getLabel("LMS-23009"));
			setFocusOnFirstFocusableField(document);
		}	
	}
	
	/** Função para retornar false no click da gridColumm*/
	function returnFalse(){
		return false;
	}
	
	function initWindow(evento){
		
		if( evento.name = "cleanButton_click" ){	
			onChangeTpManifesto();
			onChangeTpDocumentoServico();
		}
		
		// Caso não esteja carregando a tab, nem esteja incluindo em fronteira rápida, busca a filial da sessão
		if(evento.name != "removeButton" && evento.name != "tab_load"){
			findFilialUsuarioLogado();
		}
	}
	
	//-------------------------------- Códigos para a lookup de manifesto -----------------------------------
	
	/**
	*	Realiza o onchange da filial
	*   Chamada por : lookup de manifestos de viagem 
	*/
	function myFilialOnChange(){
		
		var retorno = changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	}); 
						 	
		
		
		//limpa o campo dataEmissao e situacao
		setElementValue("dtEmissao", "");
		setElementValue("dstpStatusManifesto", "");		
		
		return retorno;
		
		
	}	
	
	/**
	*	No OnPageLoad_cb realiza o onchange do tipo de manifesto para poder setar
	*   o manifesto de viagem nacional como padrão
	*/
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		onChangeTpManifesto();
		onChangeTpDocumentoServico();
		findFilialUsuarioLogado();
	}
	
	function onChangeTpManifesto(){
		changeDocumentWidgetType({
						   documentTypeElement:getElement('manifesto.tpManifesto'), 
						   filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
						   documentGroup:'MANIFESTO',
						   actionService:'lms.tributos.manterArquivoFronteiraRapidaOrigemAction'
						   });
		
		setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
		setDisabled('manifesto.manifestoViagemNacional.idManifestoViagemNacional', false);
		
		setFocusOnFirstFocusableField(document);
		
	}
	
	/**
	*	Retorno da busca da lookup de filial do manifesto viagem via digitação
	*
	*/
	function enableManifestoViagemNacional_cb(data) {
	   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
	      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	   }
	}
	
	/**
	*	Retorno da busca da lookup de manifesto viagem via tela da lookup
	*
	*/
	function myOnPopupSetValue(data,error){
		if( data != undefined ){
		
			if( data.nrManifestoOrigem != undefined ){
			
				var nrManifestoOrigem;
				
				if( !isNaN(parseInt(data.nrManifestoOrigem,10)) ){
					nrManifestoOrigem = data.nrManifestoOrigem;	
				}
				else {	
					nrManifestoOrigem = data.nrManifestoOrigem.substr(4);
				}			
				
			}
			
			data.nrManifestoOrigem = nrManifestoOrigem;
		
			setElementValue('manifesto.filialByIdFilialOrigem.pessoa.nmFantasia',data.pessoa.nmFantasia);
			setElementValue('manifesto.filialByIdFilialOrigem.idFilial',data.idFilialOrigem);
			setElementValue('manifesto.filialByIdFilialOrigem.sgFilial',data.sgFilialOrigem);
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem',data.nrManifestoOrigem);
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional',data.idManifestoViagemNacional);
			
			setElementValue('dtEmissao', setFormat('dtEmissao',data.dhEmissaoManifesto.substr(0,10)));			
			setElementValue('dstpStatusManifesto', data.tpStatusManifesto.description );
			setElementValue('tpStatusManifesto', data.tpStatusManifesto.value );					
			
			setFocus(document.getElementById("ocorrenciaDoctoServico.doctoServico.nrDoctoServico"));
			
		} else {
			setFocus('manifesto.manifestoViagemNacional.nrManifestoOrigem');
		}
	}
	
	/**
	*	Retorno da busca da lookup de manifesto viagem nacional via digitação
	*
	*/
	function myOnDataLoadCallBackManifestoViagemNacional_cb(data,error){
		var retorno = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
		
		if( retorno == true && data[0] != undefined ){
		
			setElementValue('dtEmissao', setFormat('dtEmissao',data[0].dataEmissao));			
			setElementValue('dstpStatusManifesto', data[0].tpStatusManifesto.description );
			setElementValue('tpStatusManifesto', data[0].tpStatusManifesto.value );		
			
		}
		
		return retorno;
	}
	
	//-------------------------Código para a Lookup de Documento de serviço - Conhecimentos -------------------------
	
	function onChangeTpDocumentoServico(){
	
		var args = {documentTypeElement:getElement('naoConformidade.conhecimento.tpDocumentoServico'),
								  filialElement:document.getElementById('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial'),
								  documentNumberElement:document.getElementById('naoConformidade.doctoServico.idDoctoServico'),
								  documentGroup:'SERVICE',
								  actionService:'lms.tributos.manterArquivoFronteiraRapidaOrigemAction'};
	
		changeDocumentWidgetType(args);
		afterChangeDocumentType(args);
		
		setDisabled('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial',false);							 								  
	}	
	
	function afterChangeDocumentType(args) {

		var documentTypeElement = args.documentTypeElement;
		var documentNumberElement = args.documentNumberElement;
		
		documentNumberElement.propertyMappings[documentNumberElement.propertyMappings.length] = 		
		{ 
			modelProperty:"tpDocumentoServico",  			  
			criteriaProperty:"naoConformidade.conhecimento.tpDocumentoServico",
			inlineQuery:true
		};
		
		documentNumberElement.propertyMappings[documentNumberElement.propertyMappings.length] = 		
		{ 
			modelProperty:"filialByIdFilialOrigem.pessoa.nmFantasia",  			  
			criteriaProperty:"naoConformidade.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia",
			inlineQuery:false
		};

	}
	
	function filialDoctoServicoOnChange(){
		var retorno = changeDocumentWidgetFilial({
									 filialElement:document.getElementById('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial'),
									 documentNumberElement:document.getElementById('naoConformidade.doctoServico.idDoctoServico')
									 });
		return retorno;							 
	}

	/**
	 * Controla as tags aninhadas para habilitar/desabilitar
	 */
	 function enableDoctoServico_cb(data) {
	   var r = naoConformidade_doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("naoConformidade.doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("ocorrenciaDoctoServico.doctoServico.nrDoctoServico"));	      
	   }
	}
	
	function retornoDocumentoServico_cb(data) {
		var r = naoConformidade_doctoServico_nrDoctoServico_exactMatch_cb(data);
	
		if (r == true && (data != undefined && data.length > 0)) {
			var idDoctoServico = getElementValue("naoConformidade.doctoServico.idDoctoServico");
			setElementValue('naoConformidade.doctoServico.idDoctoServico', idDoctoServico);
		}
	}
	
	function myOnPopupSetValueDoctoServico(data){
		if( data != undefined ){
			
			setElementValue('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial',data.idFilialOrigem);			
			setElementValue('naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial',data.sgFilialOrigem);
			setElementValue('naoConformidade.doctoServico.idDoctoServico',data.idDoctoServico);
			setElementValue('naoConformidade.doctoServico.nrDoctoServico',data.nrDoctoServico);
		
		}

	}
	
	function myNrManifestoOnChange(elem){
		var retorno = manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
		
		if( elem.value == '' ){		
			setElementValue("dtEmissao", "");
			setElementValue("dstpStatusManifesto", "");		
		}
		
		return retorno;
	}
	
	function findFilialUsuarioLogado(){
		_serviceDataObjects = new Array();
		var dados = new Array();	
        addServiceDataObject(createServiceDataObject("lms.tributos.manterArquivoFronteiraRapidaOrigemAction.findFilialUsuarioLogado",
			"retornoFindFilialUsuarioLogado", 
			dados));

        xmit(false);
	}
	
	function retornoFindFilialUsuarioLogado_cb(data,error){
		
		if (error != undefined){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		setElementValue("manifesto.filialByIdFilialOrigem.idFilial", data.filial.idFilial);
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.filial.sgFilial);
		setElementValue("manifesto.filialByIdFilialOrigem.pessoa.nmFantasia", data.filial.pessoa.nmFantasia)
		
	}		
	
</script>
