<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.inclusaoCobrancaInadimplentesAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/contasReceber/inclusaoCobrancaInadimplentes" idProperty="idCobrancaInadimplencia" 
				newService="lms.contasreceber.inclusaoCobrancaInadimplentesAction.newMaster" onDataLoadCallBack="myDataLoadCallBack">

		<adsm:hidden property="statusAtivo" value="A"/>

		
		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			criteriaProperty="pessoa.nrIdentificacao" 
			dataType="text" 
			exactMatch="true" 
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			idProperty="idCliente" 
			required="true"
			label="cliente" 
			labelWidth="20%"
			maxLength="20" 
			property="cliente" 
			service="lms.contasreceber.emitirDocumentosFaturarAction.findClienteLookup" 
			size="20" 
			width="80%">
			
			<adsm:propertyMapping 
				relatedProperty="nmPessoa" 
				modelProperty="pessoa.nmPessoa"/>
				
			<adsm:propertyMapping 
				criteriaProperty="statusAtivo" 
				modelProperty="tpSituacao" />
				
			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				property="nmPessoa" 
				serializable="false"
				size="60"
				maxLength="50"/>
				
		</adsm:lookup>
        
		
        
        <adsm:lookup property="usuario" 
        			 idProperty="idUsuario" 
        			 criteriaProperty="nrMatricula" 
        			 serializable="true"
                     dataType="text" 
                     label="usuario" 
                     required="true"
                     size="20" 
                     maxLength="20" 
                     labelWidth="20%" 
                     width="80%" 
                     service="lms.contasreceber.inclusaoCobrancaInadimplentesAction.findLookupUsuario" 
                     action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario"/>
                <adsm:textbox dataType="text" property="nmUsuario" size="60" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="dsCobrancaInadimplencia" required="true" size="70" maxLength="60" label="descricao" labelWidth="20%" width="80%"/>
		
		<adsm:checkbox property="blCobrancaEncerrada" disabled="true" label="cobrancaEncerrada" labelWidth="20%"  width="80%" />
		

		<adsm:buttonBar>

			<adsm:button caption="agenda" action="/contasReceber/manterAgendaCobrancaInadimplencia.do" cmd="main">
				<adsm:linkProperty src="cliente.idCliente" target="cobrancaInadimplencia.cliente.idCliente"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="cobrancaInadimplencia.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="nmPessoa" target="cobrancaInadimplencia.cliente.pessoa.nmPessoa"/>
				
				<adsm:linkProperty src="usuario.nrMatricula" target="cobrancaInadimplencia.usuario.nrMatricula"/>
				<adsm:linkProperty src="nmUsuario" target="cobrancaInadimplencia.usuario.nmUsuario"/>
				<adsm:linkProperty src="usuario.idUsuario" target="cobrancaInadimplencia.usuario.idUsuario"/>
				
				<adsm:linkProperty src="dsCobrancaInadimplencia" target="cobrancaInadimplencia.dsCobrancaInadimplencia"/>
				
				<adsm:linkProperty src="idCobrancaInadimplencia" target="cobrancaInadimplencia.idCobrancaInadimplencia"/>
			</adsm:button>

			<adsm:button caption="ligacoes" action="/contasReceber/manterLigacoesCobrancaInadimplencia.do" cmd="main">
				<adsm:linkProperty src="cliente.idCliente" target="idCliente"/>
				<adsm:linkProperty src="cliente.pessoa.nrIdentificacao" target="cliente.nrIdentificacao"/>
				<adsm:linkProperty src="nmPessoa" target="cliente"/>
				
				<adsm:linkProperty src="usuario.nrMatricula" target="responsavel.nrIdentificacao"/>
				<adsm:linkProperty src="nmUsuario" target="responsavel"/>
				
				
				<adsm:linkProperty src="dsCobrancaInadimplencia" target="cobrancaInadimplencia.dsCobrancaInadimplencia"/>
				
				<adsm:linkProperty src="idCobrancaInadimplencia" target="cobrancaInadimplencia.idCobrancaInadimplencia"/>
			</adsm:button>

			<adsm:storeButton id="storeButton"/>
			
			<adsm:newButton/>
			

		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>

/** Função chamada no callBack do form para popular o mesmo */
function myDataLoadCallBack_cb(data, errors, o){
	onDataLoad_cb(data);
	
	/** Seta os Elementos da lookup cliente manualmente */
	setaLookupCliente(data.cliente.pessoa.nrIdentificacaoFormatado, data.cliente.pessoa.nmPessoa);
	
	/** Seta os Elementos da lookup usuario manualmente */
	setaLookupUsuario(data.usuario.nrMatricula, data.usuario.vfuncionario.nmFuncionario);
}

function setaLookupCliente(nrIdentificacao, nmPessoa){
	/** Seta os Elementos da lookup cliente manualmente */
	setElementValue("cliente.pessoa.nrIdentificacao", nrIdentificacao);
	setElementValue("nmPessoa", nmPessoa);
}

function setaLookupUsuario(nrMatricula, nmUsuario){
	/** Seta os Elementos da lookup usuario manualmente */
	setElementValue("usuario.nrMatricula", nrMatricula);
	setElementValue("nmUsuario", nmUsuario);
}

function initWindow(eventObj){
	if (eventObj.name == "newButton_click" || eventObj.name == "removeButton"){	
		findUsuarioLogado();
	} else if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq") {
		findUsuarioLogado();
	}
}

function myOnPageLoad_cb(d,e,c,x){
	document.getElementById('blCobrancaEncerrada').checked = true;
	
	onPageLoad_cb(d,e,c,x);
	findUsuarioLogado();
}


function findUsuarioLogado(){
	var sdo = createServiceDataObject("lms.contasreceber.inclusaoCobrancaInadimplentesAction.getUsuarioSession",
			"carregarUsuarioSession",new Array());
		xmit({serviceDataObjects:[sdo]});
}

function carregarUsuarioSession_cb(data,erro){
		setElementValue("usuario.nrMatricula", data.nrMatricula);
		setElementValue("nmUsuario", data.nmUsuario);
		setElementValue("usuario.idUsuario", data.idUsuario);
}
</script>