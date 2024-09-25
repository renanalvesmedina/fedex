<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarServicosMeioTransporte" service="lms.portaria.manterOrdensSaidaAction">

	<adsm:form action="/contratacaoVeiculos/manterPaisPermissionadoMeiosTransporte" height="118">
	
		<adsm:lookup property="filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.portaria.manterOrdensSaidaAction.findFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" width="80%" minLengthForAutoPopUpSearch="3" labelWidth="17%"
				exactMatch="false" style="width:45px" disabled="true" required="true">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia" size="30" disabled="true" />	
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa" />
			<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>		
		</adsm:lookup>
			
		<!-- Lookup de identificacao do meio-transporte -->
		<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdMeioTransporte2" idProperty="idMeioTransporte"
					 service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="false" cellStyle="vertical-align=bottom;"
					 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
					 label="meioTransporte" width="33%" size="8" serializable="false" maxLength="6" labelWidth="17%">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte"
								  modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />	
								  
	  		<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdMeioTransporte" idProperty="idMeioTransporte" cellStyle="vertical-align=bottom;"
						 service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="true" maxLength="25" 
						 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
						 size="20">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdMeioTransporte2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte2.idMeioTransporte"
									  modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdMeioTransporte2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />				
			</adsm:lookup>
		</adsm:lookup>

				
		<!-- Lookup de identificacao do semi-reboque -->
		<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdSemiReboque2" idProperty="idMeioTransporte"
				service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="false" labelWidth="17%"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="semiReboque" width="33%" size="8" serializable="false" maxLength="6" cellStyle="vertical-align=bottom;">
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdSemiReboque.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte"
								  modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque.meioTransporte.nrIdentificador"
								  modelProperty="meioTransporte.nrIdentificador" />	
			
			<adsm:lookup dataType="text" property="meioTransporteRodoviarioByIdSemiReboque" idProperty="idMeioTransporte"
						 service="lms.portaria.manterOrdensSaidaAction.findMeioTransporteRodoviario" picker="true" maxLength="25" cellStyle="vertical-align=bottom;"
						 action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
						 size="20">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviarioByIdSemiReboque2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque2.idMeioTransporte"
									  modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviarioByIdSemiReboque2.meioTransporte.nrFrota"
									  modelProperty="meioTransporte.nrFrota" />		
			</adsm:lookup>
		
		</adsm:lookup>  
		
	
		 
		 <adsm:hidden property="tpSituacao" value="A" serializable="false"/>
 		<!-- FIM Lookup de identificacao do semi-reboque -->
		 
		  
		<adsm:lookup dataType="text" property="motorista" idProperty="idMotorista" criteriaProperty="pessoa.nrIdentificacao"
				service="lms.portaria.manterOrdensSaidaAction.findMotorista" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
				label="motorista" action="/contratacaoVeiculos/manterMotoristas" size="15" maxLength="20" width="80%" labelWidth="17%"
				exactMatch="false" minLengthForAutoPopUpSearch="5">
        	<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
        	<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="30" disabled="true"/>
        	<adsm:hidden property="motorista.showFilialUsuarioLogado" value="false"/>
        </adsm:lookup> 
        
		<adsm:lookup property="usuario" idProperty="idUsuario" criteriaProperty="nrMatricula"
					 dataType="text" label="responsavel" size="10" maxLength="16" labelWidth="17%" width="12%" 
					 service="lms.portaria.manterOrdensSaidaAction.findResponsavel" 
					 action="/configuracoes/consultarFuncionariosView" >	
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial" />
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false" />
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
			
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" maxLength="45" width="37%" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:range label="periodoRegistroLiberacao" labelWidth="17%" width="83%">
             <adsm:textbox dataType="JTDate" property="dtRegistroInicial" cellStyle="vertical-align=bottom;"/>
             <adsm:textbox dataType="JTDate" property="dtRegistroFinal" cellStyle="vertical-align=bottom;"/>
        </adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="ordemSaida"/>
			<adsm:resetButton/>
		</adsm:buttonBar> 
	</adsm:form>
	
	
	<adsm:grid idProperty="idOrdemSaida"
			service="lms.portaria.manterOrdensSaidaAction.findPaginatedCustom"
			rowCountService="lms.portaria.manterOrdensSaidaAction.getRowCountCustom"
			gridHeight="150" property="ordemSaida" scrollBars="horizontal" unique="true" rows="7">
	
		<adsm:gridColumn title="meioTransporte" property="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrFrota" width="105" />
		<adsm:gridColumn title="" property="meioTransporteRodoviarioByIdMeioTransporte.meioTransporte.nrIdentificador" width="105" align="left"/>
		
		<adsm:gridColumn title="semiReboque" property="meioTransporteRodoviarioByIdSemiReboque.meioTransporte.nrFrota" width="90"/>		
		<adsm:gridColumn title="" property="meioTransporteRodoviarioByIdSemiReboque.meioTransporte.nrIdentificador" width="90" align="left"/>	
			
		<adsm:gridColumn title="identificacao" property="motorista.pessoa.tpIdentificacao" width="50"/>		
		<adsm:gridColumn title="" property="motorista.pessoa.nrIdentificacao" width="100" align="right"/>		
		
		<adsm:gridColumn title="motorista" property="motorista.pessoa.nmPessoa" width="180"/>
		
		<adsm:gridColumn title="responsavel" property="usuario.vfuncionario.nmFuncionario" width="180"/>
		<adsm:gridColumn title="dataRegistroLiberacao" property="dhRegistro" dataType="JTDateTimeZone" align="center" width="175" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar> 
	</adsm:grid>
</adsm:window>
<script>

	function initWindow(evt){

		if(evt.name == "tab_load" || evt.name == 'cleanButton_click'){
			loadDadosSessao();
		} 
	}

	//Chama o servico que retorna os dados do usuario logado 
	function loadDadosSessao(){

		var data = new Array();
		var sdo = createServiceDataObject("lms.portaria.manterOrdensSaidaAction.findDadosSessao",
					"preencheDadosSessao",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosSessao_cb(data, exception){
		if (exception == null){
			setElementValue("filialByIdFilialOrigem.idFilial", getNestedBeanPropertyValue(data, "idFilial"));
			setElementValue("filialByIdFilialOrigem.sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "nmFantasia"));		
		}
	}


</script>