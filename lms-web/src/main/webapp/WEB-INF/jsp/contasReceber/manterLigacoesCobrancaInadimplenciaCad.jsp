<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 

<adsm:window service="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction" 
	onPageLoad="myPageLoad"
	onPageLoadCallBack="myPageLoad">
	<adsm:form action="/contasReceber/manterLigacoesCobrancaInadimplencia" 
			idProperty="idLigacaoCobranca" 
			service="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.findByIdMap">

		<adsm:hidden property="idCliente"/>
		<adsm:textbox label="cliente" property="cliente.nrIdentificacao" dataType="text" size="20" disabled="true" labelWidth="22%" width="16%"/>
		<adsm:textbox property="cliente" dataType="text" size="60" disabled="true" width="62%"/>

		<adsm:textbox label="responsavelCobranca" property="responsavel.nrIdentificacao" dataType="text" size="9" disabled="true" labelWidth="22%" width="9%"/>
		<adsm:textbox property="responsavel" dataType="text" size="60" disabled="true" width="69%"/>

		<adsm:textbox label="descricao" property="cobrancaInadimplencia.dsCobrancaInadimplencia" dataType="text" disabled="true" size="85" labelWidth="22%" width="78%"/>

		<adsm:hidden property="usuario.idUsuario"/>
		<adsm:textbox label="usuario" property="usuario.nrMatricula" dataType="text" size="9" disabled="true" labelWidth="22%" width="9%"/>
		<adsm:textbox property="usuario.nmUsuario" dataType="text" size="60" disabled="true" width="69%"/>
		
		<adsm:combobox property="telefoneContato.idTelefoneContato"
					   optionLabelProperty="contato" 
					   optionProperty="idTelefoneContato" 
					   boxWidth="390"
					   service="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.findComboContatos" 
					   required="true"
					   label="contato" 
					   labelWidth="22%" 
					   width="78%">
			   <adsm:propertyMapping criteriaProperty="idCliente" modelProperty="idPessoa"/>
		</adsm:combobox>			
			
		<adsm:textarea label="descricao" property="dsLigacaoCobranca" maxLength="255" rows="6" columns="41" required="true" labelWidth="22%" width="35%"/>

		<adsm:hidden property="cobrancaInadimplencia.idCobrancaInadimplencia"/>
		<adsm:pairedListbox label="faturas"
							property="itemLigacoes" 
							optionProperty="idItemCobranca" 
							optionLabelProperty="nrFatura" 
							size="6" 
							boxWidth="110" 
							required="true" 
							labelWidth="11%" 
							width="31%"/>

		<adsm:textbox label="dataLigacao" dataType="JTDateTimeZone" property="dhLigacaoCobranca" required="true" labelWidth="22%" width="35%"/>

		<adsm:buttonBar>
            <adsm:button caption="agenda" action="contasReceber/manterAgendaCobrancaInadimplencia" cmd="main">
   				<adsm:linkProperty src="idCliente" target="cobrancaInadimplencia.cliente.idCliente"/>
				<adsm:linkProperty src="cliente.nrIdentificacao" target="cobrancaInadimplencia.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="cliente" target="cobrancaInadimplencia.cliente.pessoa.nmPessoa"/>
				
				<adsm:linkProperty src="responsavel.nrIdentificacao" target="cobrancaInadimplencia.usuario.nrMatricula"/>
				<adsm:linkProperty src="usuario.nmUsuario" target="cobrancaInadimplencia.usuario.nmUsuario"/>
				<adsm:linkProperty src="usuario.idUsuario" target="cobrancaInadimplencia.usuario.idUsuario"/>
				
				<adsm:linkProperty src="cobrancaInadimplencia.dsCobrancaInadimplencia" target="cobrancaInadimplencia.dsCobrancaInadimplencia"/>
				<adsm:linkProperty src="cobrancaInadimplencia.idCobrancaInadimplencia" target="cobrancaInadimplencia.idCobrancaInadimplencia"/>
            </adsm:button>

			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	var nmUsuario;
	var nrMatricula;
	var idUsuario;

	function carregarUsuarioSessao_cb(data, erro){
		nmUsuario = data.nmUsuario;
		nrMatricula = data.nrMatricula;
		idUsuario = data.idUsuario;
	}

	function initWindow(eventObj){
		setDisabled("dhLigacaoCobranca", true);

		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton") {
		
			setElementValue("usuario.idUsuario", idUsuario);
			setElementValue("usuario.nrMatricula", nrMatricula);
			setElementValue("usuario.nmUsuario", nmUsuario);

			var sdo = createServiceDataObject("lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.getDataHora",
					"setDataHora", new Array() );
			xmit({serviceDataObjects:[sdo]});
			
			document.getElementById("telefoneContato.idTelefoneContato").selectedIndex = 0;

		}
		
	}
	
	function setDataHora_cb(data, error){
		setElementValue("dhLigacaoCobranca", setFormat(getElement("dhLigacaoCobranca"), data.dataHora));
		setDisabled("dhLigacaoCobranca", false);
	}

	function myPageLoad(){
		onPageLoad();
		document.getElementById("telefoneContato.idTelefoneContato").masterLink = "true";
	}
	
	function myPageLoad_cb(dados,errors){
		onPageLoad_cb(dados,errors);
		
		notifyElementListeners({e:document.getElementById("idCliente")});
		notifyElementListeners({e:document.getElementById("cobrancaInadimplencia.idCobrancaInadimplencia")});

		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.getUsuarioSessao",
			"carregarUsuarioSessao", 
			new Array()));

	    addServiceDataObject(createServiceDataObject("lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.findFaturasByCobrancaInadimplencia", 
	    	"itemLigacoes_source", 
	    	{idCob:getElementValue("cobrancaInadimplencia.idCobrancaInadimplencia")}));

        xmit(false);      

	}
</script>
