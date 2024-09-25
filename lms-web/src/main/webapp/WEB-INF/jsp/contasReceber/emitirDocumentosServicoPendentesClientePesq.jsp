<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirDocumentosServicoPendentesClienteAction">

	<adsm:form action="/contasReceber/emitirDocumentosServicoPendentesCliente">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-36102"/>
			<adsm:include key="LMS-36257"/>
			<adsm:include key="LMS-36107"/>
			<adsm:include key="LMS-36230"/>
		</adsm:i18nLabels>

		<adsm:hidden property="siglaNomeFilial" serializable="true" />

		<adsm:lookup  dataType="text" 
				      property="filial" 
				      idProperty="idFilial" 
					  criteriaProperty="sgFilial" 
					  service="lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.findLookupFilial" 					  
					  onDataLoadCallBack="setaNomeFilial"
					  action="/municipios/manterFiliais" 
					  label="filialCobranca" 
					  size="3" 
					  onchange="limpaSgFilialNomeFilial()"					  
					  maxLength="3" 
					  width="35%"
					  exactMatch="true">
				<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
				<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial"/>			
				<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="31" maxLength="30" disabled="true"/>
			</adsm:lookup>
	    <adsm:hidden property="siglaFilial" serializable="true"/>   

		<adsm:hidden property="dsTipoFilial" serializable="true"/>
		<adsm:combobox
			property="tpFilial"
			label="tipoFilial" width="35%" boxWidth="130"
			service="lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.findTipoFilial"
			optionProperty="value" optionLabelProperty="description" 
			onchange="tipoFilialOnChange(this)"/>		
			
		<adsm:lookup label="cliente"
					 service="lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.findLookupCliente" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 width="85%" 
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="cliente.pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="30" />	
		</adsm:lookup>
		
		<adsm:textbox label="identificacaoParcial" property="identificacaoParcial" dataType="text" size="10" maxLength="8" width="35%" minLength="8"/>
		
		<adsm:hidden property="dsTipoCliente" serializable="true"/>
		<adsm:combobox label="tipoCliente" property="tpCliente" width="35%" boxWidth="130" domain="DM_TIPO_CLIENTE" onchange="tipoClienteOnChange(this)"/>

		<adsm:textbox label="emissaoAte" property="dtEmissaoAte" dataType="JTDate"  width="35%"/>

		<adsm:hidden property="dsSituacaoCobranca" serializable="true"/>
		<adsm:combobox property="tpSituacaoCobranca"
					   label="situacaoCobranca" 
					   width="35%"
					   boxWidth="130"
					   service="lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.findSituacaoCobranca"
					   optionProperty="value" 
					   optionLabelProperty="description"
					   defaultValue="C"
					   onchange="return situacaoCobrancaOnChange(this);"/> 			
		
		<adsm:hidden property="dsModal" serializable="true"/>
		<adsm:combobox label="modal" property="tpModal"  width="35%" domain="DM_MODAL" onchange="modalOnChange(this)"/>

		<adsm:hidden property="dsAbrangencia" serializable="true"/>
		<adsm:combobox label="abrangencia" property="tpAbrangencia" width="35%" boxWidth="130" domain="DM_ABRANGENCIA" onchange="abrangenciaOnChange(this)"/>

		<adsm:textbox label="diasAtraso" property="diasAtraso" dataType="integer" maxLength="5"  size="5"/>
		
		<adsm:combobox label="moeda"
					   property="moeda.idMoeda"
					   optionProperty="idMoeda"
					   optionLabelProperty="descricao"
					   required="true"
					   width="35%"
					   boxWidth="130"
					   onchange="moedaOnChange(this)"
					   service="lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.findMoedasByCombo">		
			<adsm:propertyMapping modelProperty="dsMoeda" formProperty="moeda.dsMoeda"/>
		</adsm:combobox>			   
		<adsm:hidden property="dsSiglaSimbolo" serializable="true"/>
		<adsm:hidden property="moeda.dsMoeda" serializable="true"/>
		
		<adsm:combobox label="tipoFrete" property="tpFrete" domain="DM_TIPO_FRETE" width="35%" renderOptions="true"/>
		
		<adsm:checkbox label="blCortesia" property="blCortesia" width="35%" serializable="true"/>  

		<adsm:hidden property="dsTpDocumentoServico" serializable="true"/>
		<adsm:combobox property="tpDocumentoServico"
			  label="tipoDocumentoServico"
			  domain="DM_TIPO_DOCUMENTO_SERVICO"
			  onchange="tpDocumentoServicoOnChange(this)" />
			   
		<adsm:hidden property="dsTpSituacaoRPS" serializable="true"/>
		<adsm:combobox property="tpSituacaoRPS"
			  label="situacaoRPS"
			  domain="DM_SITUACAO_RPS"
			  onchange="tpSituacaoRPSOnChange(this)"/>

		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" required="true" width="85%" defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="btnVisualizar" disabled="false" buttonType="reportButton" onclick="visualizarOnClick(this)"/>
			<adsm:resetButton/> 
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script>
		
	function initWindow(eventObj){
		buscaDadosPadroes();

		// Seta o valor default do campo dias de atraso
		setElementValue("diasAtraso", 30);
	}
	
	/**
	*	Busca dados da filial e moeda padrão do usuário logado
	*/
	function buscaDadosPadroes(){
	
		var dados = new Array();
         
        _serviceDataObjects = new Array();
            
        addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.findFilialUsuario",
                                                     "retornoBuscaFilialUsuario",
                                                     dados));
                                                     
        addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.findMoedaPadrao",
                                                     "retornoBuscaMoedaPadrao",
                                                     dados));                                                     
                                                                                       
        xmit(false);
	}
	
	/**
	*	Seta a filial do usuário como padrão. 
	* 	Se filial usuario for 'MTZ' pesquisa somente filiais ativas
	*/
	function retornoBuscaFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('filial.sgFilial'));
			return false;		
		}
		
		// Caso a filial da sessão do usuário não seja matriz, proteger os campos filial e tipoFilial
		if(data.filialUserIsMatriz == "false"){
			setDisabled("filial.idFilial", true);
			setDisabled("tpFilial", true);
		}
			
		setElementValue('filial.idFilial',data.filial.idFilial);
		setElementValue('filial.sgFilial',data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		
		if( data.siglaNomeFilial == undefined || data.siglaNomeFilial == '' ){
			setElementValue('siglaNomeFilial',data.filial.sgFilial + ' - ' + data.filial.pessoa.nmFantasia);
		} else {
			setElementValue('siglaNomeFilial',data.siglaNomeFilial);
		}
		
	}
	
	/**
	*	Seta a moeda padrão do País do usuário
	*/
	function retornoBuscaMoedaPadrao_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('moeda.dsMoeda'));
			return false;		
		}
		
		setElementValue('moeda.idMoeda',data.idMoeda);
		setElementValue('dsSiglaSimbolo',data.siglaSimbolo);
		
	}
	
	function limpaSgFilialNomeFilial(){
		filial_sgFilialOnChangeHandler();
		resetValue('siglaNomeFilial');
	}
	
	function tipoFilialOnChange(elem){	
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsTipoFilial', elem.options[elem.selectedIndex].text);		
		}else{
		    setElementValue('dsTipoFilial','');
		}
	}
	
	function tipoClienteOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsTipoCliente', elem.options[elem.selectedIndex].text);		
		}else{
		    setElementValue('dsTipoCliente','');
		}
	}
	
	function situacaoCobrancaOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsSituacaoCobranca', elem.options[elem.selectedIndex].text);		
		}else{
            setElementValue('dsSituacaoCobranca','');
        }
	}
	
	function modalOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsModal', elem.options[elem.selectedIndex].text);		
		}else{
		    setElementValue('dsModal','');
		}
	}
	
	function abrangenciaOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsAbrangencia', elem.options[elem.selectedIndex].text);		
		}else{
		  setElementValue('dsAbrangencia','');
		}
		
	}
	
	function tpDocumentoServicoOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsTpDocumentoServico', elem.options[elem.selectedIndex].text);		
		}else{
		  setElementValue('dsTpDocumentoServico','');
		}
	}
	
	function tpSituacaoRPSOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsTpSituacaoRPS', elem.options[elem.selectedIndex].text);		
		}else{
		  setElementValue('dsTpSituacaoRPS','');
		}
	}
	
	function moedaOnChange(elem){
		comboboxChange({e:elem});
		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsSiglaSimbolo', elem.options[elem.selectedIndex].text);		
		}else{
		    setElementValue('dsSiglaSimbolo','');  
		}
	}
	
	function visualizarOnClick(elem){
		
		var cliente = getElementValue('cliente.idCliente');
		var identParcial = getElementValue('identificacaoParcial');
		var filial = getElementValue("filial.idFilial");
		var tpFilial = getElementValue("tpFilial");
		
		// Deve ser informado cliente ou número de identificação parcial ou filial ou tipo de filial
		if(cliente == '' && identParcial == '' && filial == '' && tpFilial == ''){
			alert(i18NLabel.getLabel('LMS-36230'));
			return false;
		}
		
		// Não adeve  informar cliente e identificação parcial
		if( cliente != '' && identParcial != '' ){
			alert(i18NLabel.getLabel('LMS-36102'));
			setFocus(document.getElementById("cliente.idCliente"));
			return false;
		} 
		
		// Identificação parcial não deve ter menos de 8 caracteres
		if( identParcial != '' && cliente == '' && identParcial.length < 8 ){
			alert(i18NLabel.getLabel('LMS-36107'));
			setFocus(document.getElementById("identificacaoParcial"));
			return false;
		}
	
		var elem = getElement("tpSituacaoCobranca");		
		if( elem.selectedIndex != 0 ){
			setElementValue('dsSituacaoCobranca', elem.options[elem.selectedIndex].text);
		}else{
		  	setElementValue('dsSituacaoCobranca','');	
		}
		alert(i18NLabel.getLabel('LMS-36257'));
		var data = buildFormBeanFromForm(elem.form);
		var sdo = createServiceDataObject("lms.contasreceber.emitirDocumentosServicoPendentesClienteAction.executeAssynchronousReport", "empty", data);
		xmit({serviceDataObjects:[sdo]});		
	}
	
	function empty_cb(){};
	
	function setaNomeFilial_cb(data, erro){
		
		var retorno = filial_sgFilial_exactMatch_cb(data);
		
		if( retorno != undefined && retorno == true ){
		
			if( data.siglaNomeFilial == undefined || data.siglaNomeFilial == '' ){
				setElementValue('siglaNomeFilial',data[0].sgFilial + ' - ' + data[0].pessoa.nmFantasia);
			} else {
				setElementValue('siglaNomeFilial',data[0].siglaNomeFilial);
			}
			
		}
	}
</script>