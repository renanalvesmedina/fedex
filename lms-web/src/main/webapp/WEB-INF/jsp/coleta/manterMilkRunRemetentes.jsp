<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.coleta.manterMilkRunAction">

	<adsm:form action="/coleta/manterMilkRun" idProperty="idMilkRemetente"
				service="lms.coleta.manterMilkRunAction.findByIdMilkRemetente" height="240">
				
		<adsm:masterLink showSaveAll="true" idProperty="idMilkRun">
			<adsm:masterLinkItem property="cliente.pessoa.nmPessoa" label="destinatario" />
		</adsm:masterLink>
		
		<adsm:lookup label="remetente" width="80%" size="20" maxLength="20"  serializable="true"
					 service="lms.coleta.manterMilkRunAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao"
					 dataType="text" 
					 property="cliente" 
					 idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="false"
					 disabled="false" 
					 required="true"
 					 onPopupSetValue="carregaDadosCliente"
					 onDataLoadCallBack="carregaDadosCliente">					 	

			<adsm:propertyMapping relatedProperty="tipoIdentificacao" modelProperty="pessoa.tpIdentificacao.value" />
			<adsm:propertyMapping relatedProperty="numeroIdentificacao" modelProperty="pessoa.nrIdentificacao" />
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="idClientePessoa" modelProperty="idCliente" />			

			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" 
						  maxLength="50" disabled="true" />
			<adsm:hidden property="idClientePessoa" />
			<adsm:hidden property="tipoIdentificacao" serializable="false" />
			<adsm:hidden property="numeroIdentificacao" serializable="false" />
		</adsm:lookup>
	
		<adsm:hidden property="enderecoPessoa.dsEndereco" />
		<adsm:hidden property="enderecoPessoa.nrEndereco" />
		<adsm:hidden property="enderecoPessoa.dsComplemento" />
		<adsm:hidden property="enderecoPessoa.dsBairro" />
		<adsm:hidden property="enderecoPessoa.nrCep" />
		<adsm:textarea  label="enderecoColeta" property="endereco" maxLength="300" 
						columns="90" rows="3"  width="80%" 
						disabled="true" serializable="false" required="true">			

			<adsm:lookup style="visibility: hidden;font-size: 1px" size="1" maxLength="1" dataType="text" 
						 idProperty="idEnderecoPessoa" 
						 property="enderecoPessoa" 
						 action="/coleta/cadastrarPedidoColeta"
						 cmd="selecionarEndereco"
						 service="lms.coleta.manterMilkRunAction.findLookupEnderecoPessoa"
						 criteriaProperty="pessoa.idPessoa" 
						 onPopupSetValue="concatenaEndereco" >
						 
				<adsm:propertyMapping criteriaProperty="idClientePessoa" modelProperty="pessoa.idPessoa" />	
				<adsm:propertyMapping criteriaProperty="tipoIdentificacao" modelProperty="pessoa.tpIdentificacao" />
				<adsm:propertyMapping criteriaProperty="numeroIdentificacao" modelProperty="pessoa.nrIdentificacao" />	
				<adsm:propertyMapping criteriaProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />	
				
				<adsm:propertyMapping relatedProperty="enderecoPessoa.municipio.idMunicipio" modelProperty="municipio.idMunicipio" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" 
									  modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.dsEndereco" modelProperty="dsEndereco" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.nrEndereco" modelProperty="nrEndereco" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.dsComplemento" modelProperty="dsComplemento" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.dsBairro" modelProperty="dsBairro" />
				<adsm:propertyMapping relatedProperty="enderecoPessoa.nrCep" modelProperty="nrCep" />

			</adsm:lookup>
									
		</adsm:textarea>		
		
		<adsm:hidden property="enderecoPessoa.municipio.idMunicipio" />
		<adsm:textbox label="municipio" property="enderecoPessoa.municipio.nmMunicipio" dataType="text" 
					  size="30" width="80%" disabled="true"/>
		
		<adsm:textbox label="uf" property="enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text"
					  size="3" width="80%" disabled="true"/>
					  
		<adsm:combobox label="servico" property="servico.idServico"
					   optionProperty="idServico" optionLabelProperty="dsServico"
					   service="lms.coleta.manterMilkRunAction.findServico" 
					   width="80%" boxWidth="230" required="true" onlyActiveValues="true"/>
		
		<adsm:combobox label="natureza" property="naturezaProduto.idNaturezaProduto" 
					   optionProperty="idNaturezaProduto" optionLabelProperty="dsNaturezaProduto" 
					   service="lms.coleta.manterMilkRunAction.findNaturezaProduto" 
					   width="80%" required="true" onlyActiveValues="true"/>

		<adsm:label key="espacoBranco" width="100%" style="border:none"/>

		<adsm:label key="espacoBranco" width="18%" style="border:none"/>
        <adsm:range width="21%">
			<adsm:label key="1Semana" style="border:none"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:label key="2Semana" style="border:none"/>
	    </adsm:range>
        <adsm:range width="20%">
        	<adsm:label key="3Semana" style="border:none"/>
	    </adsm:range>
        <adsm:range width="10%">
        	<adsm:label key="4Semana" style="border:none"/>
	    </adsm:range>
		
        <adsm:range label="dom" width="20%">
        	<adsm:hidden property="nrDomSemana1" />
			<adsm:textbox dataType="JTTime" property="domInicioSemana1" size="4%"/>
			<adsm:textbox dataType="JTTime" property="domFinalSemana1" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrDomSemana2" />
			<adsm:textbox dataType="JTTime" property="domInicioSemana2" size="4%"/>
			<adsm:textbox dataType="JTTime" property="domFinalSemana2" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
	        <adsm:hidden property="nrDomSemana3" />
			<adsm:textbox dataType="JTTime" property="domInicioSemana3" size="4%"/>
			<adsm:textbox dataType="JTTime" property="domFinalSemana3" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrDomSemana4" />
			<adsm:textbox dataType="JTTime" property="domInicioSemana4" size="4%"/>
			<adsm:textbox dataType="JTTime" property="domFinalSemana4" size="4%"/>
        </adsm:range>  		
		
        <adsm:range label="seg" width="20%">
        	<adsm:hidden property="nrSegSemana1" />
			<adsm:textbox dataType="JTTime" property="segInicioSemana1" size="4%"/>
			<adsm:textbox dataType="JTTime" property="segFinalSemana1" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
	        <adsm:hidden property="nrSegSemana2" />
			<adsm:textbox dataType="JTTime" property="segInicioSemana2" size="4%"/>
			<adsm:textbox dataType="JTTime" property="segFinalSemana2" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSegSemana3" />
			<adsm:textbox dataType="JTTime" property="segInicioSemana3" size="4%"/>
			<adsm:textbox dataType="JTTime" property="segFinalSemana3" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSegSemana4" />
			<adsm:textbox dataType="JTTime" property="segInicioSemana4" size="4%"/>
			<adsm:textbox dataType="JTTime" property="segFinalSemana4" size="4%"/>
        </adsm:range>

        <adsm:range label="ter" width="20%">
        	<adsm:hidden property="nrTerSemana1" />
			<adsm:textbox dataType="JTTime" property="terInicioSemana1" size="4%"/>
			<adsm:textbox dataType="JTTime" property="terFinalSemana1" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrTerSemana2" />
			<adsm:textbox dataType="JTTime" property="terInicioSemana2" size="4%"/>
			<adsm:textbox dataType="JTTime" property="terFinalSemana2" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrTerSemana3" />
			<adsm:textbox dataType="JTTime" property="terInicioSemana3" size="4%"/>
			<adsm:textbox dataType="JTTime" property="terFinalSemana3" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrTerSemana4" />
			<adsm:textbox dataType="JTTime" property="terInicioSemana4" size="4%"/>
			<adsm:textbox dataType="JTTime" property="terFinalSemana4" size="4%"/>
        </adsm:range>

        <adsm:range label="qua" width="20%">
        	<adsm:hidden property="nrQuaSemana1" />
			<adsm:textbox dataType="JTTime" property="quaInicioSemana1" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quaFinalSemana1" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrQuaSemana2" />
			<adsm:textbox dataType="JTTime" property="quaInicioSemana2" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quaFinalSemana2" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
	        <adsm:hidden property="nrQuaSemana3" />
			<adsm:textbox dataType="JTTime" property="quaInicioSemana3" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quaFinalSemana3" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrQuaSemana4" />
			<adsm:textbox dataType="JTTime" property="quaInicioSemana4" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quaFinalSemana4" size="4%"/>
        </adsm:range>

        <adsm:range label="qui" width="20%">
        	<adsm:hidden property="nrQuiSemana1" />
			<adsm:textbox dataType="JTTime" property="quiInicioSemana1" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quiFinalSemana1" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrQuiSemana2" />
			<adsm:textbox dataType="JTTime" property="quiInicioSemana2" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quiFinalSemana2" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrQuiSemana3" />
			<adsm:textbox dataType="JTTime" property="quiInicioSemana3" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quiFinalSemana3" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrQuiSemana4" />
			<adsm:textbox dataType="JTTime" property="quiInicioSemana4" size="4%"/>
			<adsm:textbox dataType="JTTime" property="quiFinalSemana4" size="4%"/>
        </adsm:range>

        <adsm:range label="sex" width="20%">
        	<adsm:hidden property="nrSexSemana1" />
			<adsm:textbox dataType="JTTime" property="sexInicioSemana1" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sexFinalSemana1" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSexSemana2" />
			<adsm:textbox dataType="JTTime" property="sexInicioSemana2" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sexFinalSemana2" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSexSemana3" />
			<adsm:textbox dataType="JTTime" property="sexInicioSemana3" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sexFinalSemana3" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSexSemana4" />
			<adsm:textbox dataType="JTTime" property="sexInicioSemana4" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sexFinalSemana4" size="4%"/>
        </adsm:range>
        
        <adsm:range label="sab" width="20%">
        	<adsm:hidden property="nrSabSemana1" />
			<adsm:textbox dataType="JTTime" property="sabInicioSemana1" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sabFinalSemana1" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSabSemana2" />
			<adsm:textbox dataType="JTTime" property="sabInicioSemana2" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sabFinalSemana2" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSabSemana3" />
			<adsm:textbox dataType="JTTime" property="sabInicioSemana3" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sabFinalSemana3" size="4%"/>
        </adsm:range>
        <adsm:range width="20%">
        	<adsm:hidden property="nrSabSemana4" />
			<adsm:textbox dataType="JTTime" property="sabInicioSemana4" size="4%"/>
			<adsm:textbox dataType="JTTime" property="sabFinalSemana4" size="4%"/>
        </adsm:range>    

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarRemetente" service="lms.coleta.manterMilkRunAction.saveMilkRemetente" />
			<adsm:newButton caption="limpar"/>			
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="milkRemetente" idProperty="idMilkRemetente" detailFrameName="remetentes"
			   autoSearch="false" rows="3" selectionMode="check" showPagging="true" 
			   showGotoBox="true" unique="true" 
			   service="lms.coleta.manterMilkRunAction.findPaginatedMilkRemetente"
			   rowCountService="lms.coleta.manterMilkRunAction.getRowCountMilkRemetente" 
			   defaultOrder="cliente_pessoa_.nmPessoa">

		<adsm:gridColumn width="80" title="identificacao" property="cliente.pessoa.tpIdentificacao" isDomain="true" align="left" />
		<adsm:gridColumn width="150" title="" property="cliente.pessoa.nrIdentificacao" align="right" />
		<adsm:gridColumn title="remetente" property="cliente.pessoa.nmPessoa" />		
	
		<adsm:buttonBar> 
			<adsm:removeButton caption="excluirRemetente" service="lms.coleta.manterMilkRunAction.removeByIdsMilkRemetente"/>
		</adsm:buttonBar>		
	</adsm:grid>
	
</adsm:window>

<script type="text/javascript">

	function initWindow(eventObj) {
		if(getElementValue("idClientePessoa") == null || getElementValue("idClientePessoa") == "") { 		
			setDisabled("enderecoPessoa.idEnderecoPessoa", true);
		}
	}

	function carregaDadosCliente(data) {	
		buscarEnderecoPessoa(data);	
		setDisabled("enderecoPessoa.idEnderecoPessoa", false);
	}
	
	function carregaDadosCliente_cb(data) {	
		cliente_pessoa_nrIdentificacao_exactMatch_cb(data);
		if(data[0] != undefined) {
			buscarEnderecoPessoa(data[0]);
			setDisabled("enderecoPessoa.idEnderecoPessoa", false);
		}	
	}	

	/**
	 * Busca EnderecoPessoa de acordo com o ID co Cliente
	 */
	function buscarEnderecoPessoa(data) {
		var mapCriteria = new Array();
	   
	   	setNestedBeanPropertyValue(mapCriteria, "idCliente", data.idCliente);
	   
	    var sdo = createServiceDataObject("lms.coleta.manterMilkRunAction.getEnderecoPessoa", "resultado_buscarEnderecoPessoa", mapCriteria);
		xmit({serviceDataObjects:[sdo]});	
	}
	
	/**
	 * Retorno da pesquisa de EnderecoPessoa em getEnderecoPessoa().
	 */
	function resultado_buscarEnderecoPessoa_cb(data, error) {
	
		if(data.idEnderecoPessoa != undefined) {				
			setElementValue("enderecoPessoa.idEnderecoPessoa", data.idEnderecoPessoa);
			setElementValue("enderecoPessoa.dsEndereco", data.dsEndereco);
			setElementValue("enderecoPessoa.nrEndereco", data.nrEndereco);
			setElementValue("enderecoPessoa.dsComplemento", data.dsComplemento);
			setElementValue("enderecoPessoa.dsBairro", data.dsBairro);
			setElementValue("enderecoPessoa.nrCep", data.nrCep);
			setElementValue("enderecoPessoa.municipio.idMunicipio", data.municipio.idMunicipio);
			setElementValue("enderecoPessoa.municipio.nmMunicipio", data.municipio.nmMunicipio);
			setElementValue("enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa", data.municipio.unidadeFederativa.sgUnidadeFederativa);
			
			concatenaEndereco(data);
		} else {
			lookupClickPicker({e:document.forms[0].elements['enderecoPessoa.idEnderecoPessoa']});
		}
	}
	
	/**
	 * Concatena a descrição e o numero do EnderecoPessoa
	 */
	function concatenaEndereco(data) {
		var strEndereco = data.dsEndereco + ", nº: " + data.nrEndereco;
		if(data.dsComplemento != null) {
			strEndereco = strEndereco + " / compl.: " + data.dsComplemento;
		}
		strEndereco = strEndereco + "\nBairro: " + data.dsBairro + "\nCEP: " + data.nrCep;
		
		setElementValue("endereco", strEndereco);				
	}	
	
	
	/**
	 * Seta os dados do Endereço na tela pai após salvar o registro.
	 */
	function setaDadosEndereco(mapEndereco) {
		setElementValue("enderecoPessoa.idEnderecoPessoa", mapEndereco.idEnderecoPessoa);
		setElementValue("enderecoPessoa.dsEndereco", mapEndereco.dsEndereco);
		setElementValue("enderecoPessoa.nrEndereco", mapEndereco.nrEndereco);
		setElementValue("enderecoPessoa.dsComplemento", mapEndereco.dsComplemento);
		setElementValue("enderecoPessoa.dsBairro", mapEndereco.dsBairro);
		setElementValue("enderecoPessoa.nrCep", mapEndereco.nrCep);
		setElementValue("enderecoPessoa.municipio.idMunicipio", mapEndereco.municipio.idMunicipio);
		setElementValue("enderecoPessoa.municipio.nmMunicipio", mapEndereco.municipio.nmMunicipio);
		setElementValue("enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa", mapEndereco.municipio.unidadeFederativa.sgUnidadeFederativa);
					
		concatenaEndereco(mapEndereco);
	}

</script>