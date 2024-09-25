<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterAgendaCobrancaInadimplenciaAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/manterAgendaCobrancaInadimplencia">
	
		<adsm:hidden property="cobrancaInadimplencia.idCobrancaInadimplencia" serializable="true"/>
		<adsm:hidden property="cobrancaInadimplencia.cliente.idCliente" serializable="true"/>
		<adsm:hidden property="cobrancaInadimplencia.usuario.idUsuario" serializable="true"/>
	
		<adsm:textbox label="cliente" dataType="text" property="cobrancaInadimplencia.cliente.pessoa.nrIdentificacao" size="20" labelWidth="20%" width="80%" serializable="false">
			<adsm:textbox dataType="text" property="cobrancaInadimplencia.cliente.pessoa.nmPessoa" size="60" serializable="false"/>
		</adsm:textbox>	
		
		<adsm:textbox label="responsavelCobranca" dataType="text" property="cobrancaInadimplencia.usuario.nrMatricula" size="20" labelWidth="20%" width="80%" serializable="false">
			<adsm:textbox dataType="text" property="cobrancaInadimplencia.usuario.nmUsuario" size="60" serializable="false"/>
		</adsm:textbox>	
		
		<adsm:textbox label="descricaoCobranca" dataType="text" property="cobrancaInadimplencia.dsCobrancaInadimplencia" size="60" labelWidth="20%" width="80%" serializable="false"/>
	
		 <adsm:lookup property="usuario" 
        			 idProperty="idUsuario" 
        			 criteriaProperty="nrMatricula" 
        			 serializable="true"
                     dataType="text" 
                     label="usuario" 
                     size="20" 
                     maxLength="20" 
                     labelWidth="20%" 
                     width="80%" 
                     service="lms.municipios.manterRegionaisAction.findLookupUsuarioFuncionario" 
                     action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario"/>
                <adsm:textbox dataType="text" property="nmUsuario" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox label="contato" 
					   property="contato.idContato" 
					   optionLabelProperty="nmContato" 
					   optionProperty="idContato" 
					   service="lms.contasreceber.manterAgendaCobrancaInadimplenciaAction.findComboContatos" 					   
					   labelWidth="20%"
					   width="80%"
					   boxWidth="250"
					   autoLoad="false"/>
	
		<adsm:range label="dataAgenda" width="80%" labelWidth="20%">
			<adsm:textbox dataType="JTDateTimeZone" property="dhAgendaCobrancaInicial"/>
			<adsm:textbox dataType="JTDateTimeZone" property="dhAgendaCobrancaFinal"/> 
		</adsm:range>
	
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="agendaCobranca"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idAgendaCobranca" property="agendaCobranca" gridHeight="200" 
	           defaultOrder="dhAgendaCobranca, contato_.nmContato, usuario_.nmUsuario" rows="9">
	           
		<adsm:gridColumn width="20%" title="dataAgenda" property="dhAgendaCobranca" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="40%" title="contato" property="nmContato" dataType="text"/>
		<adsm:gridColumn width="40%" title="usuario" property="nmUsuario" dataType="text"/>
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
		
	</adsm:grid>	

</adsm:window>
<script>

	/**
	*	Busca os dados de contatos com o critério do idCliente
	*
	*/
	function myOnPageLoadCallBack_cb(data,erro){
	
		onPageLoad_cb(data,erro);
		
		var idCliente = getElementValue("cobrancaInadimplencia.cliente.idCliente");
		
		var dados = new Array();
         
        setNestedBeanPropertyValue(dados, "pessoa.idPessoa", idCliente);
         
        var sdo = createServiceDataObject("lms.contasreceber.manterAgendaCobrancaInadimplenciaAction.findComboContatos",
                                          "contato_idContato",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
	
	}

</script>