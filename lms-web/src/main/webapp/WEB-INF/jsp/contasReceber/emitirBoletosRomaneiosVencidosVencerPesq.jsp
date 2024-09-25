<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction" onPageLoadCallBack="myOnPageLoadCallBack">


	<adsm:form action="/contasReceber/emitirBoletosRomaneiosVencidosVencer" >

        <adsm:combobox property="situacao" domain="DM_RELATORIO_VENCIDOS_VENCER" defaultValue="VV" onchange="obrigaPeriodo()" label="situacao" labelWidth="20%" width="22%" required="true"/> 

        <adsm:range label="periodo" labelWidth="24%" width="34%" required="true" >
			<adsm:textbox dataType="JTDate" property="periodoInicial" picker="true"/>		
			<adsm:textbox dataType="JTDate" property="periodoFinal" picker="true" />		
		</adsm:range>

        <adsm:combobox property="modal" domain="DM_MODAL" label="modal" labelWidth="20%" width="22%"/>

        <adsm:combobox property="abrangencia" domain="DM_ABRANGENCIA" label="abrangencia" labelWidth="24%" width="34%"/>

		<adsm:combobox 
			service="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findComboCedentes" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			label="banco"
			labelWidth="20%"
			width="22%"
			boxWidth="150"
			required="false">
			
			<adsm:propertyMapping relatedProperty="cedente.dsCedente" modelProperty="comboText" /> 
		</adsm:combobox>
			<adsm:hidden property="cedente.dsCedente" />

        <adsm:checkbox label="filiaisComFatCentralizado" property="filiaisComFatCentralizado" onclick="obrigaFilial()" labelWidth="24%" width="34%" />

        <adsm:combobox 
        label="tipoFilial" 
        property="tipoFilial" 
        domain="DM_TIPO_FILIAL" 
        labelWidth="20%" 
        width="22%" 
        boxWidth="150" 
        onchange="limpaFilial(this)">
        
        	<adsm:propertyMapping relatedProperty="dsTipoFilial" modelProperty="description" /> 
		</adsm:combobox>
			<adsm:hidden property="dsTipoFilial" />
        

		<adsm:hidden property="sgFilial"/>

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialCobranca" 
					 size="3" 
					 maxLength="3" 
					 width="34%"
					 labelWidth="24%"
					 required="false"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="sgFilial"/>
			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="20" maxLength="20" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="sgDsRegional" />

	    <adsm:combobox 
	    	label="regional" 
	    	property="regional.idRegional"
	    	optionLabelProperty="siglaDescricao" 
	    	optionProperty="idRegional" 
	    	service="lms.municipios.regionalService.findRegionaisVigentes" 
	    	labelWidth="20%" 
	    	width="22%" boxWidth="150" 
	    	required="false" 
	    	onchange="limpaFilial(this)">
	    	
	         <adsm:propertyMapping relatedProperty="sgDsRegional" modelProperty="siglaDescricao" />
        </adsm:combobox>

		<adsm:hidden property="moeda.dsSimbolo" serializable="true"/>

		<adsm:combobox property="moeda.idMoeda" label="moedaExibicao" optionProperty="idMoeda" 
			optionLabelProperty="siglaSimbolo"
			service="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findMoedaPaisCombo" 
			labelWidth="24%"
			width="34%" required="true" serializable="true">
			<adsm:propertyMapping relatedProperty="moeda.dsSimbolo" modelProperty="siglaSimbolo"/>			
		</adsm:combobox>

		<adsm:textbox label="emissaoAte" property="dtEmissaoAte" dataType="JTDate" labelWidth="20%" 
    				  width="22%"/>
		
		<adsm:combobox property="tpDocumentoServico"
			  label="tipoDocumentoServico"
			  domain="DM_TIPO_DOCUMENTO_SERVICO"
			  onchange="tpDocumentoServicoOnChange(this)"
			  labelWidth="24%" width="34%"/>

        <adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   labelWidth="20%" 
    				   width="22%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:section caption="dadosDoCliente" />

        <adsm:combobox label="tipoIdentificacao" property="tpIdentificacao" domain="DM_TIPO_IDENTIFICACAO_PESSOA" labelWidth="20%" width="22%"/>
        
        <adsm:textbox dataType="text" property="nrIdentificacao"  label="identificacaoCliente" maxLength="30" size="15" labelWidth="24%" width="34%"/>
        
        <adsm:hidden property="tpIdentificacaoCliente"/>
        <adsm:hidden property="nrIdentificacaoCliente"/>
        
		<adsm:lookup label="cliente"
					 service="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findLookupClientes" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 exactMatch="true" 
					 size="18"
					 maxLength="20" 
					 width="80%"
					 serializable="true"
					 onDataLoadCallBack="cedenteDataLoadCallBack"
					 labelWidth="20%"
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="cliente.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacaoFormatado" relatedProperty="cliente.pessoa.nrIdentificacao" />
			<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao" formProperty="tpIdentificacaoCliente" />
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" formProperty="nrIdentificacaoCliente" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="50" maxLength="50" />
		</adsm:lookup>

		<adsm:buttonBar> 
			<adsm:reportViewerButton service="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>	

	</adsm:form>
	
</adsm:window>

<script>

	// ao limpar, seta os campos com os valores padrão
	function initWindow(eventObj) {
    	if (eventObj.name == "cleanButton_click") {
	    	setaDefault();
	    }
	}

	// ao alterar a regional ou o tipo de filial, limpa a filial
	function limpaFilial(campo){

		if (campo.value!="") {
			setElementValue('filial.idFilial', "");
			setElementValue('filial.sgFilial', "");
			setElementValue('sgFilial', "");
			setElementValue('filial.pessoa.nmFantasia', "");

			if (campo.id == "tipoFilial"){
				setElementValue('dsTipoFilial', campo.options[campo.selectedIndex].text);
			}else if (campo.id == "regional.idRegional"){
				setElementValue('sgDsRegional', campo.options[campo.selectedIndex].text);
			}
			
			
			
			
			
		}
	}

	function obrigaPeriodo() {
		var req = "true";
	
		if (getElementValue("situacao") == "EP") {
			req = "false";
		}
		
		document.getElementById("periodoInicial").required = req;
		document.getElementById("periodoFinal").required = req;
	}
	
	function obrigaFilial() {
		var req = "false";
	
		if (getElementValue("filiaisComFatCentralizado")) {
			req = "true";
		}

		document.getElementById("filial.sgFilial").required = req;
	}
	
	function cedenteDataLoadCallBack_cb(data, error) {
	
		var retorno = cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
	
		if ( retorno == true && data != null) {
			setElementValue("cliente.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
			setElementValue("cliente.pessoa.nmPessoa", data[0].pessoa.nmPessoa);
		}
		
		return retorno;
		
	}
	
	function setaDefault() {
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findDatas",
			"setDatas", 
			new Array()));

		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findFilialUsuario",
			"setFilialUsuario", 
			new Array()));

		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findMoedaUsuario",
			"setMoedaUsuario", 
			new Array()));
			
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction.findUserMatriz",
			"findUserMatriz", 
			new Array()));		
		
        xmit(false);	
	}

    function myOnPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data, erro);

		setaDefault();		
	}

	function setDatas_cb(data, erro){
		//setando as datas
		setElementValue("periodoInicial",setFormat(getElement("periodoInicial"),data.periodoInicial));
		setElementValue("periodoFinal",setFormat(getElement("periodoFinal"),data.periodoFinal));
	}
		
	// seta na filial, a filial do usuário da sessão
	function setFilialUsuario_cb(data, error) {
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue('filial.sgFilial', data.sgFilial);
		setElementValue('sgFilial', data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.pessoa.nmFantasia);
	}

	// seta na moeda, a moeda da sessão	
	function setMoedaUsuario_cb(data, error) {
		setElementValue('moeda.idMoeda', data.idMoeda);
		setElementValue('moeda.dsSimbolo', data.siglaSimbolo);
	}
	
	/**
	  * Caso a filial do usuário não seja matriz, desabilita alguns campos
	  */	
	function findUserMatriz_cb(data, error){
	
		if( error != undefined ){
			alert(error);
		}
		
		// Caso a filial da sessão do usuário não seja matriz, proteger os campos filialCobranca, regional e tipoFilial
		if(data.filialUserIsMatriz == "false"){
			setDisabled("regional.idRegional", true);
			setDisabled("filial.idFilial", true);
			setDisabled("tipoFilial", true);
		}
		
	}
	
</script>