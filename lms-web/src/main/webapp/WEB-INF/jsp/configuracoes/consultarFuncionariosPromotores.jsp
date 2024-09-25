 <%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function serializableObjects(){
	 onPageLoad();
	 //usado para pesquisar pela "sigla da filial" no metodo findPaginated	
	 document.getElementById("funcionario.codFilial.sgFilial").serializable = true;	 
		 
	}
</script>
<adsm:window service="lms.configuracoes.RHFuncionarioService" onPageLoad="serializableObjects">

	<adsm:form action="/configuracoes/consultarFuncionarios">
	
		<adsm:hidden property="tpSituacaoFuncionario"/>
	
		<adsm:hidden property="idUsuario" serializable="false"/>

		<adsm:lookup property="funcionario.codFilial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.municipios.filialService.findLookup" 
					 dataType="text"  
					 label="filial" 
					 size="3" 
					 action="/municipios/manterFiliais" 
					 labelWidth="15%" 
					 width="9%" 
					 minLengthForAutoPopUpSearch="3" 
					 exactMatch="false" 
					 style="width:45px" 
					 maxLength="3">
			<adsm:propertyMapping relatedProperty="funcionario.codFilial.pessoa.nmPessoa" modelProperty="pessoa.nmFantasia" />
			
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="funcionario.codFilial.pessoa.nmPessoa" width="76%" size="30" serializable="false" disabled="true"/>
				
		<adsm:textbox dataType="text" property="nrMatricula" label="matricula" maxLength="16" labelWidth="15%" width="35%" />
		
		<adsm:textbox dataType="text" property="funcionario.codPessoa.nome" label="nome" labelWidth="15%" width="35%"/>
		
		<adsm:combobox property="funcionario.codFuncao.cargo.codigo" 
					   label="cargo" labelWidth="15%" width="35%" 
					   service="lms.configuracoes.RHCargoService.find" 
					   optionLabelProperty="nome" optionProperty="codigo"/>
		
		<adsm:lookup property="funcionario.codFuncao" 
					 idProperty="codigo" 
					 criteriaProperty="idCodigo" 
					 service="lms.configuracoes.RHFuncaoService.findLookup" 
					 dataType="text"  
					 label="funcao" 
					 size="3" 
					 action="/configuracoes/consultarCargos" 
					 labelWidth="15%" 
					 width="9%" 
					 minLengthForAutoPopUpSearch="3" 
					 exactMatch="false" 
					 style="width:45px">
			<adsm:propertyMapping relatedProperty="funcionario.codFuncao.nome" modelProperty="nome"/>
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="funcionario.codFuncao.nome" width="26%" size="30" disabled="true" serializable="false" />
		
		
				
		<adsm:buttonBar freeLayout="true">
				<adsm:findButton callbackProperty="usuario"/>
				<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
		
	<adsm:grid idProperty="idUsuario" property="usuario" unique="true" 
			   service="lms.vendas.manterPromotoresClienteAction.findPaginatedPromotores" 
			   rowCountService="lms.vendas.manterPromotoresClienteAction.getRowCountPromotores">
		<adsm:gridColumn title="filial"    property="vfuncionario.filial.sgFilial"         width="140"/>
		<adsm:gridColumn title="matricula" property="nrMatricula"               width="80" />
		<adsm:gridColumn title="nome"      property="nmUsuario"        width="180"/>
		<adsm:gridColumn title="cargo" 	   property="cfuncionario.dsCargo"        width="100"/>
		<adsm:gridColumn title="situacao"  property="vfuncionario.dsSituacao" width="50" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>	