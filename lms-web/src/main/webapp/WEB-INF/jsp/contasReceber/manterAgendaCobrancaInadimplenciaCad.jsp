<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterAgendaCobrancaInadimplenciaAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/manterAgendaCobrancaInadimplencia" idProperty="idAgendaCobranca" onDataLoadCallBack="myOnDataLoadCallBack">
	
		<adsm:hidden property="cobrancaInadimplencia.idCobrancaInadimplencia" serializable="true"/>
		<adsm:hidden property="cobrancaInadimplencia.cliente.idCliente" serializable="true"/>
		<adsm:hidden property="cobrancaInadimplencia.usuario.idUsuario" serializable="true"/>
	
		<adsm:textbox label="cliente" dataType="text" property="cobrancaInadimplencia.cliente.pessoa.nrIdentificacao" size="20" labelWidth="20%" width="80%" serializable="false">
			<adsm:textbox dataType="text" property="cobrancaInadimplencia.cliente.pessoa.nmPessoa" size="60" serializable="false"/>
		</adsm:textbox>	
		
		<adsm:textbox label="responsavelCobranca" dataType="text" property="cobrancaInadimplencia.usuario.nrMatricula" size="20" labelWidth="20%" width="80%" serializable="false">
			<adsm:textbox dataType="text" property="cobrancaInadimplencia.usuario.nmUsuario"  size="60" serializable="false"/>
		</adsm:textbox>	
		
		<adsm:textbox label="descricaoCobranca" dataType="text" property="cobrancaInadimplencia.dsCobrancaInadimplencia" size="60" labelWidth="20%" width="80%"/>




        <adsm:section caption="dadosUltimaLigacao"/>
        
        <adsm:hidden property="ligacaoCobranca.idLigacaoCobranca" serializable="true"/>
        
     	<adsm:textbox label="usuario" 
     				  dataType="text" 
     				  property="ligacaoCobranca.usuario.nrMatricula" 
     				  size="20" 
     				  labelWidth="20%" 
     				  width="80%"  
     				  disabled="true" 
     				  serializable="false">
			<adsm:textbox dataType="text" 
						  property="ligacaoCobranca.usuario.nmUsuario"  
						  size="60" 						  
						  maxLength="30" 
						  disabled="true" 
						  serializable="false"/>
		</adsm:textbox>        
        <adsm:hidden property="ligacaoCobranca.usuario.idUsuario" serializable="false"/>

		<adsm:textbox label="contato" 
					  property="ligacaoCobranca.telefoneContato.contato.nmContato" 
					  dataType="text" 
					  disabled="true" 
					  serializable="false" 
					  labelWidth="20%" 
					  width="80%"
					  size="60"/>
		<adsm:hidden property="ligacaoCobranca.telefoneContato.contato.idContato"/>

		<adsm:textarea label="descricao" property="ligacaoCobranca.dsLigacaoCobranca" disabled="true" maxLength="255" rows="6" columns="35" useRowspan="false" labelWidth="20%" width="40%" serializable="false" />

        <adsm:listbox label="faturas"        			  
					  property="ligacaoCobranca.itemLigacoes"
					  size="6"
					  optionProperty="idFatura"
					  optionLabelProperty="descricao"
					  width="30%" 
					  showOrderControls="false" 
					  boxWidth="110"
					  labelWidth="10%"
					  showIndex="false"
					  serializable="false" 
					  allowMultiple="false">		
		</adsm:listbox>

        <adsm:textbox label="dataLigacao" size="10" dataType="JTDateTimeZone" property="ligacaoCobranca.dhLigacaoCobranca" labelWidth="20%" width="80%" disabled="true" serializable="false" picker="false"/>
        
        <adsm:section caption="dadosAgenda"/>
        
		<adsm:hidden property="usuario.idUsuario" serializable="true"/>
        <adsm:textbox label="usuario" labelWidth="20%" property="usuario.nrMatricula" width="80%" size="20" disabled="true" dataType="text">
        	<adsm:textbox property="usuario.nmUsuario" size="60" dataType="text" disabled="true"/>
        </adsm:textbox>
        
		<adsm:combobox label="contato" 
					   property="contato.idContato" 
					   optionLabelProperty="nmContato" 
					   optionProperty="idContato" 
					   service="lms.contasreceber.manterAgendaCobrancaInadimplenciaAction.findComboContatos" 					   
					   labelWidth="20%"
					   width="80%"
					   boxWidth="250"
					   autoLoad="false"/>

		<adsm:textbox label="dataAgenda" dataType="JTDateTimeZone" property="dhAgendaCobranca" required="true" labelWidth="20%" width="35%"/>

        <adsm:textarea label="descricaoAgenda" property="dsAgendaCobranca" columns="100" rows="3" maxLength="500" labelWidth="20%" width="80%" required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	function initWindow(eventObj){
	
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click") || (eventObj.name == "removeButton") ) {
			buscaDadosUltimaLigacao();
		}
	
	}

	/**
	*	Busca os dados de contatos com o critério do idCliente
	*	OnPageLoad_cb padrão e busca de dados da última ligação
	*/
	function myOnPageLoadCallBack_cb(data,erro){
	
		onPageLoad_cb(data,erro);
                                          
		buscaDadosUltimaLigacao();

		var dados = new Array();
		
		_serviceDataObjects = new Array();
         
        setNestedBeanPropertyValue(dados, "pessoa.idPessoa", getElementValue("cobrancaInadimplencia.cliente.idCliente"));
         
        addServiceDataObject(createServiceDataObject("lms.contasreceber.manterAgendaCobrancaInadimplenciaAction.findComboContatos",
                                                     "contato_idContato",
                                                     dados));
                                                                                    
        xmit(false);
	}
	
	/**
	*	OnDataLoad_cb padrão e busca de dados da última ligação
	*/
	function myOnDataLoadCallBack_cb(data,erro){
	
		if( data[0] != undefined ){
			onDataLoad_cb(data[0],erro);		
		} else if( data != undefined ){
			onDataLoad_cb(data,erro);		
		}
		
		buscaDadosUltimaLigacao();
		
		if( getElementValue("idAgendaCobranca") != "" ){
			buscaFaturasInadimplencia();
		}

		setFocusOnFirstFocusableField(document);
	}
	
	/**
	*	Seta dados da sessão "Dados da última ligação"
	*
	*/
	function buscaDadosUltimaLigacao(){
	
		if( getElementValue("ligacaoCobranca.idLigacaoCobranca") == "" && getElementValue("idAgendaCobranca") == "" ){

			var dados = new Array();

			_serviceDataObjects = new Array();

	        setNestedBeanPropertyValue(dados, "cobrancaInadimplencia.idCobrancaInadimplencia", getElementValue("cobrancaInadimplencia.idCobrancaInadimplencia"));		                                          

			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterAgendaCobrancaInadimplenciaAction.findDadosLigacaoCobranca",
	                                                     "retornoDadosLigacaoCobranca",
	                                                     dados));
	                                                     
			addServiceDataObject(createServiceDataObject("lms.contasreceber.manterAgendaCobrancaInadimplenciaAction.findUsuarioLogado",
                                                         "retornoFindUsuarioLogado",
                                                         dados));	                                                     
                                            
	        xmit(false);	
	    }
	}
	
	/**
	*	Seta os dados da sessão "Dados da última ligação"
	*
	*/
	function retornoDadosLigacaoCobranca_cb(data,erro){
	
		fillFormWithFormBeanData(0, data);		
		document.getElementById("ligacaoCobranca.dsLigacaoCobranca").readOnly = true;
	}
	
	/**
	*	Método de retorno da busca do usuário logado para setar
	*	na lookup de usuário da seção dados da agenda
	*/
	function retornoFindUsuarioLogado_cb(data,erro){
	
		fillFormWithFormBeanData(0, data);
	
		if( data != undefined && getElementValue("usuario.idUsuario") != "" ){
			setDisabled("usuario.nrMatricula",true);
		}
		
	}
	
	/**
	*	Busca as faturas Inadimplência
	*/
	function buscaFaturasInadimplencia(){
	
		var dados = new Array();
	         
        setNestedBeanPropertyValue(dados, "ligacaoCobranca.idLigacaoCobranca", getElementValue("ligacaoCobranca.idLigacaoCobranca"));
        setNestedBeanPropertyValue(dados, "viaIdLigacaoCobranca", 'true');
        
        var sdo = createServiceDataObject("lms.contasreceber.manterAgendaCobrancaInadimplenciaAction.findComboFaturasInadimplencia",
                                          "retornoFaturasInadimplência",
                                          dados);
                                          
		                                          
        xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	*	Seta dados das faturas inadimplência
	*/
	function retornoFaturasInadimplência_cb(data, erro){
		fillFormWithFormBeanData(0, data[0]);
	}

</script>