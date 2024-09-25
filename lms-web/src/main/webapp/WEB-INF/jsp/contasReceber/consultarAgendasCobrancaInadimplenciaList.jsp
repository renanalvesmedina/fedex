<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction">

	<adsm:form action="/contasReceber/consultarAgendasCobrancaInadimplencia">

		<adsm:lookup action="/vendas/manterDadosIdentificacao" 
					 service="lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction.findCliente" 
					 dataType="text" property="cobrancaInadimplencia.cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao"
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 label="cliente" size="20" maxLength="20"
   					 labelWidth="20%" 
   					 width="80%" >
		   	<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="clienteByIdCliente.pessoa.nmPessoa"/>
            <adsm:textbox dataType="text" 
            			  property="clienteByIdCliente.pessoa.nmPessoa" 
            			  size="50" 
            			  maxLength="30" 
            			  disabled="true" 
            			  serializable="false"/>
        </adsm:lookup>

		<adsm:hidden property="idUsuario" serializable="true"/>
		<adsm:lookup property="usuario" 
        			 idProperty="idUsuario" 
        			 criteriaProperty="nrMatricula" 
        			 serializable="false"
                     dataType="text" 
                     label="usuario" 
                     size="20" 
                     maxLength="20" 
                     labelWidth="20%" 
                     width="80%" 
                     service="lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction.findLookupUsuarioFuncionario" 
                     action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
                <adsm:propertyMapping modelProperty="idUsuario" formProperty="idUsuario"/>   
                <adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>
        

		<adsm:range label="dataAgenda" labelWidth="20%" width="85%">
			<adsm:textbox dataType="JTDateTimeZone" property="dhAgendaCobrancaInicial" size="10" maxLength="20" />
			<adsm:textbox dataType="JTDateTimeZone" property="dhAgendaCobrancaFinal" size="10" maxLength="20" />
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="findButton" caption="consultar" onclick="validateAgendaCobranca(this.form);"/>
			<adsm:resetButton/>
		</adsm:buttonBar>    
		
		<script>
			var LMS_36012 = "<adsm:label key="LMS-36012"/>";
		</script>

	</adsm:form>

	<adsm:grid idProperty="idAgendaCobranca" property="agendaCobranca" 
			   service="lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction.findPaginatedByAgendaCobranca"
			   rowCountService="lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction.getRowCountByAgendaCobranca"
			   onRowClick="returnFalse();"
			   selectionMode="none"
			   gridHeight="200"  
	           rows="12">
	           
		<adsm:gridColumn title="dataAgenda" property="dhAgendaCobranca" dataType="JTDateTimeZone" width="15%"/>
		<adsm:gridColumn title="cliente" property="nmPessoa" align="left" width="20%"/>
		<adsm:gridColumn title="contato" property="nmContato" align="left" width="20%"/>
		<adsm:gridColumn title="dataLigacao" property="dhLigacaoCobranca" dataType="JTDateTimeZone" width="15%"/>
	    <adsm:gridColumn title="usuario" property="nmUsuario" align="left" width="20%"/>
	    <adsm:gridColumn 
	    		title="descricao" 
	    		property="descricao" 
	    		image="/images/popup.gif" 
	    		openPopup="true" 
	    		link="/contasReceber/consultarDescricaoAgendasCobrancaInadimplencia.do?cmd=main" 
	    		popupDimension="500, 170" 
	    		width="100" 
	    		align="center" 
	    		linkIdProperty="idAgendaCobranca"/>
		
		<adsm:buttonBar>
		</adsm:buttonBar>
		
	</adsm:grid>

</adsm:window>

<script>

function returnFalse(){
	return false;
}

function validateAgendaCobranca(formulario){
	if(getElementValue("cobrancaInadimplencia.cliente.pessoa.nrIdentificacao") == "" && getElementValue("usuario.nrMatricula") == ""){
		alert(LMS_36012	);
		setFocusOnFirstFocusableField(document);
	}else{
		findButtonScript('agendaCobranca', formulario);
	}
	
}

/*  Executado ao entrar na tela */
function initWindow(){

	buscaUsuarioSessao();
}

/*  busca o usuario da sessao */
function buscaUsuarioSessao(){
	                                                                                    
//	        xmit(false);	
		var dados = new Array();
    	_serviceDataObjects = new Array();
    
	    addServiceDataObject(createServiceDataObject("lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction.findUsuarioSessao",
                                          "setaUsuarioSessao",
                                          dados));
  	
  		addServiceDataObject(createServiceDataObject("lms.contasreceber.consultarAgendasCobrancaInadimplenciaAction.findDateTime",
	                                      "findDateTime",
	                                      dados));                                        
	
        xmit(false);		
		
}
	
/**
*	Seta o usuario da sessao
*/
function setaUsuarioSessao_cb(data,erro){
	if ( erro != undefined ) {
		alert(erro);
		return false;		
	}
	
	setElementValue('usuario.idUsuario',data.idUsuario);
	setElementValue('usuario.nrMatricula',data.nrMatricula);
	setElementValue('usuario.nmUsuario',data.nmUsuario);
	setElementValue('idUsuario',data.idUsuario);
				
	setFocusOnFirstFocusableField(document);		
}

function findDateTime_cb(data, erro){
	if ( erro != undefined ) {
		alert(erro);
		return false;		
	}
	
	setElementValue('dhAgendaCobrancaInicial', 
			setFormat(document.getElementById('dhAgendaCobrancaInicial'), data.dhAgendaCobrancaInicial));
	setElementValue('dhAgendaCobrancaFinal', 
			setFormat(document.getElementById('dhAgendaCobrancaFinal'), data.dhAgendaCobrancaFinal));
}	

</script>