<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>

	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'list'});
	} 
	
	function pageLoadAeroporto() {
   		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.nrIdentificacao")});
	} 
</script>
<adsm:window service="lms.municipios.manterAeroportosAction" onPageLoadCallBack="pageLoadCustom" onPageLoad="pageLoadAeroporto">
	<adsm:form action="/municipios/manterAeroportos" idProperty="idAeroporto" >
		<adsm:hidden property="pessoa.tpPessoa" value="J" />
		
		<adsm:complement label="identificacao" labelWidth="15%" width="35%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list"/>
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/>
		</adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" size="35" maxLength="50" />
		
		<adsm:textbox dataType="text" property="sgAeroporto" label="sigla" size="3" maxLength="3" onchange="validaSigla();"/>
		<adsm:textbox dataType="text" property="cdCidade" label="codigoCidade" size="3" maxLength="3" />
		

		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="true"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="true"/>
		<adsm:lookup service="lms.municipios.manterAeroportosAction.findLookupFilial" dataType="text" property="filial" idProperty="idFilial"
				criteriaProperty="sgFilial" label="filialResponsavel" size="3" maxLength="3"
	 			action="/municipios/manterFiliais" width="8%" exactMatch="true" >
         	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"  />
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"  />
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"  />
         	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" 
         			size="30" disabled="true" width="27%" serializable="false" />

	    </adsm:lookup>
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="aeroporto" />
			<adsm:resetButton />
		</adsm:buttonBar>  
	</adsm:form>
	<adsm:grid property="aeroporto" idProperty="idAeroporto" rows="11" unique="true" gridHeight="220"
			scrollBars="horizontal"
			service="lms.municipios.manterAeroportosAction.findPaginatedCustom"
			rowCountService="lms.municipios.manterAeroportosAction.getRowCountCustom" >
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="45" align="left" />
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="110" align="right" />
		<adsm:gridColumn title="nome" property="pessoa.nmPessoa" width="200" />
		<adsm:gridColumn title="sigla" property="sgAeroporto" width="40" />		
		<adsm:gridColumn title="municipio" property="municUltimoEndereco" width="150" />
		<adsm:gridColumn title="codigoCidade" property="cdCidade" width="115" />
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialResponsavel" property="filial.sgFilial" width="30" />
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="120" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="60" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function validaSigla(){
		var sigla = document.getElementById("sgAeroporto")
		setElementValue(sigla, sigla.value.toUpperCase());
	}
	
</script>