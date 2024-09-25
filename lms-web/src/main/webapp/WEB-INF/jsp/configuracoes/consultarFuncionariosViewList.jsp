 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

function pageLoadCustom_cb(data,error) {
		
		changeTypePessoaWidget(
				{tpTipoElement:document.getElementById("pessoa.tpPessoa"),
				 tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'),
				 numberElement:document.getElementById('pessoa.nrIdentificacao'),tabCmd:'list'});
				 
		onPageLoad_cb(data,error);
		
		if (getElementValue("pessoa.tpIdentificacao") == '')
			setElementValue("pessoa.tpIdentificacao","CPF");
}
	 
function pageLoadFuncionario() {
   		onPageLoad();
   		document.getElementById("filial.sgFilial").serializable = true;
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
   				   ,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
      			   ,numberElement:document.getElementById("pessoa.idPessoa")});
}
	
</script>
<adsm:window service="lms.configuracoes.FuncionarioService" onPageLoadCallBack="pageLoadCustom" onPageLoad="pageLoadFuncionario">
	<adsm:form action="/configuracoes/consultarFuncionariosView">
        <adsm:hidden property="tpSituacaoFuncionario"/>
        <adsm:hidden property="codigoCargoMotorista"/>
        <adsm:hidden property="codSetor.codigo"/>

		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.municipios.filialService.findLookup" dataType="text"  label="filial" size="3" 
			action="/municipios/manterFiliais" labelWidth="15%" width="7%" minLengthForAutoPopUpSearch="3" exactMatch="false" maxLength="3" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" width="28%" size="30" serializable="false" disabled="true" />
		</adsm:lookup>
		
		<adsm:hidden property="pessoa.tpPessoa" value="F" />
		<adsm:complement label="identificacao" labelWidth="15%" width="32%">
            <adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" 
            			   />
            <adsm:textbox definition="IDENTIFICACAO_PESSOA"/> 
		</adsm:complement>
		
		<adsm:textbox dataType="text" property="nrMatricula" label="matricula" maxLength="16" labelWidth="15%" width="35%" size="16" disabled="false"/>

		<adsm:textbox dataType="text" property="nmUsuario" label="nome" labelWidth="15%" width="35%" maxLength="50" size="44"/>
		
		<adsm:combobox property="codFuncao.cargo.codigo" label="cargo" labelWidth="15%" width="35%" 
						service="lms.configuracoes.manterUsuarioFuncionarioAction.findCargo" 
						optionLabelProperty="nome" optionProperty="codigo" boxWidth="240" disabled="false"/>
		
		<adsm:lookup property="codFuncao" idProperty="codigo" criteriaProperty="idCodigo" service="lms.configuracoes.RHFuncaoService.findLookup" dataType="text"  label="funcao" size="3" action="/configuracoes/consultarCargos" labelWidth="15%" width="9%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px" maxLength="6">
			<adsm:propertyMapping relatedProperty="codFuncao.nome" modelProperty="nome"/>
			<adsm:textbox dataType="text" property="codFuncao.nome" width="26%" size="30" serializable="false" maxLength="60" disabled="true"/>
		</adsm:lookup>
	 			
		<adsm:buttonBar freeLayout="true">
				<adsm:findButton callbackProperty="usuario"/> 
				<adsm:resetButton/>
		</adsm:buttonBar>
		</adsm:form> 
		
		<adsm:grid idProperty="idUsuario" property="usuario" gridHeight="200" unique="true" rows="12"
				   service="lms.configuracoes.manterUsuarioFuncionarioAction.findPaginatedCustom" 
		       	   rowCountService="lms.configuracoes.manterUsuarioFuncionarioAction.getRowCountCustom">
			<adsm:gridColumn title="filial" property="nmFantasia" width="30%"/>
			<adsm:gridColumn title="matricula" property="nrMatricula" width="10%" align="right"/>
			<adsm:gridColumn title="nome" property="nmUsuario" width="30%"/>
			<adsm:gridColumn title="cargo" property="dsFuncao" width="20%"/>
			<adsm:gridColumn title="situacao" property="dsSituacao" width="10%" />
			<adsm:buttonBar>
				<adsm:removeButton/> 
			</adsm:buttonBar>
		</adsm:grid>
</adsm:window>	