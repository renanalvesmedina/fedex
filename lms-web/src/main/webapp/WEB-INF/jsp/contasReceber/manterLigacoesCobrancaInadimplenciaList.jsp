<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myPageLoad_cb(data){
		onPageLoad_cb(data);
		
		notifyElementListeners({e:document.getElementById("idCliente")});
		notifyElementListeners({e:document.getElementById("cobrancaInadimplencia.idCobrancaInadimplencia")});
	}
</script>
<adsm:window service="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/contasReceber/manterLigacoesCobrancaInadimplencia">

		<adsm:hidden property="idCliente"/>
		<adsm:textbox label="cliente" property="cliente.nrIdentificacao" dataType="text" size="20" disabled="true" labelWidth="22%" width="16%"/>
		<adsm:textbox property="cliente" dataType="text" size="60" disabled="true" width="62%"/>

		<adsm:textbox label="responsavelCobranca" property="responsavel.nrIdentificacao" dataType="text" size="9" disabled="true" labelWidth="22%" width="9%"/>
		<adsm:textbox property="responsavel" dataType="text" size="60" disabled="true" width="69%"/>

		<adsm:textbox label="descricao" property="cobrancaInadimplencia.dsCobrancaInadimplencia" dataType="text" disabled="true" size="85" labelWidth="22%" width="78%"/>

		
		
		
		<adsm:lookup property="usuario" 
        			 idProperty="idUsuario" 
        			 criteriaProperty="nrMatricula" 
        			 serializable="true"
                     dataType="text" 
                     label="usuario" 
                     size="9" 
                     maxLength="20" 
                     labelWidth="22%"
					 width="12%"
                     service="lms.municipios.manterRegionaisAction.findLookupUsuarioFuncionario" 
                     action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario"/>
                <adsm:textbox dataType="text" property="nmUsuario" width="26%" size="30" maxLength="45" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:combobox property="telefoneContato.idTelefoneContato"
					   optionLabelProperty="contato" 
					   optionProperty="idTelefoneContato" 
					   boxWidth="230"
					   service="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.findComboContatos" 
					   label="contato" 
					   autoLoad="true"
					   labelWidth="10%" 
					   width="22%">
			   <adsm:propertyMapping criteriaProperty="idCliente" modelProperty="idPessoa"/>
		</adsm:combobox>

		<adsm:range label="dataLigacao" labelWidth="22%" width="38%">
			<adsm:textbox property="dhLigacaoInicial" dataType="JTDateTimeZone"/>
			<adsm:textbox property="dhLigacaoFinal" dataType="JTDateTimeZone"/>
		</adsm:range>

		<adsm:hidden property="cobrancaInadimplencia.idCobrancaInadimplencia"/>
		<adsm:combobox label="fatura" 
					   property="idFatura" 
					   optionLabelProperty="descricao" 
					   optionProperty="idFatura" 
					   autoLoad="true"
					   service="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.findComboFaturasInadimplencia" 
					   labelWidth="10%" 
					   width="30%">
				<adsm:propertyMapping criteriaProperty="cobrancaInadimplencia.idCobrancaInadimplencia" modelProperty="idCobrancaInadimplencia"/>					   
		</adsm:combobox>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ligacaoCobranca"/>
			<adsm:button buttonType="reset" caption="limpar" onclick="limpaCombos();"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 
		selectionMode="check" 
		idProperty="idLigacaoCobranca"
		property="ligacaoCobranca" 
		gridHeight="200"
		service="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.findPaginatedByLigacaoCobranca"
		rowCountService="lms.contasreceber.manterLigacoesCobrancaInadimplenciaAction.getRowCountByLigacaoCobranca">
		
		<adsm:gridColumn width="15%" title="dataLigacao" property="ligacaoCobranca" dataType="JTDateTimeZone" align="center"/>
		<adsm:gridColumn width="45%" title="contato" property="nmContato"/>
		<adsm:gridColumn width="40%" title="usuario" property="nmFuncionario"/>

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>
	getElement("telefoneContato.idTelefoneContato").masterLink = true;
	getElement("idFatura").masterLink = true;
  
	function limpaCombos() {
		cleanButtonScript(this.document);
		getElement("telefoneContato.idTelefoneContato").selectedIndex = 0;
		getElement("idFatura").selectedIndex = 0;	
	}
  
</script>