<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.emitirRelatorioSegurosAction ">
	
	<adsm:form action="/seguros/emitirRelatorioSeguros" height="385" id="formSeguros">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-00055"/>
			<adsm:include key="LMS-37024"/>
		</adsm:i18nLabels>
	
		<%-- Filtros de Processo de Sinistro --%>
		<adsm:section caption="filtrosProcessoSinistro"/>
		
		<adsm:textbox property="nrProcessoSinistro" label="processoSinistro" dataType="text" width="40%" maxLength="30"/>
		
		<adsm:combobox property="situacaoProcessoSinistro" 
					   label="situacao"
					   domain="DM_SIT_PROCESSO_SINISTRO"  
					   labelWidth="15%" 
					   width="30%" >
			<adsm:propertyMapping relatedProperty="dsSituacaoProcessoSinistro" modelProperty="description" />
			<adsm:hidden property="dsSituacaoProcessoSinistro"/>
	   </adsm:combobox>
		
		<adsm:hidden property="manifesto.idManifesto"/>
		<adsm:hidden property="manifesto.nrManifesto"/>
		<adsm:hidden property="manifesto.tpStatusManifesto" serializable="false"/>
		<adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" serializable="false" />
		<adsm:hidden property="manifesto.tpManifestoEntrega" serializable="false"/>
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.sgFilialHidden" />
		<adsm:hidden property="manifesto.tpManifestoHidden"/>	
		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" labelWidth="15%" width="40%" serializable="false" 
					   service="lms.seguros.emitirRelatorioSegurosAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="limpaManifesto(); getTpManifesto();
					   			 return changeDocumentWidgetType({
					   		documentTypeElement:this, 
					   		filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
					   		documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
					   		documentGroup:'MANIFESTO',
					   		actionService:'lms.seguros.emitirRelatorioSegurosAction'}); " >

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="true"
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
						 onchange="limpaManifesto();
						 		   return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	});" >
			</adsm:lookup>

			<adsm:lookup dataType="integer" onDataLoadCallBack="retornoManifesto"
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" popupLabel="pesquisarManifesto"
						 afterPopupSetValue="manifestoAfterPopupSetValue"
						 onchange="return manifestoNrManifestoOrigem_OnChange();"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" >
			</adsm:lookup>
		</adsm:combobox>	
		
		<adsm:hidden property="filialByIdFilialOrigem.nmFilial"/>
		<adsm:hidden property="controleCarga.sgFilialOrigemHidden"/>
		<adsm:hidden property="controleCarga.nrControleCargaHidden"/>
		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem"  idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.seguros.emitirRelatorioSegurosAction.findLookupFilial" action="/municipios/manterFiliais" popupLabel="pesquisarFilial"
					 onchange="return onFilialControleCargaChange();" onDataLoadCallBack="disableNrControleCarga"
					 label="controleCargas" labelWidth="15%" width="20%" size="3" maxLength="3" picker="false" serializable="false">
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.nmFilial" modelProperty="pessoa.nmFantasia" />
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.seguros.emitirRelatorioSegurosAction.findLookupControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 onPopupSetValue="onControleCargaPopupSetValue" 
						 popupLabel="pesquisarControleCarga"
						 maxLength="8" size="8" mask="00000000" disabled="false">

				<adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
				<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false" inlineQuery="false"/>				
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:range label="dataSinistro" width="40%" >
			<adsm:textbox dataType="JTDate" property="dhSinistroInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dhSinistroFinal" picker="true" />
		</adsm:range>
		
		<adsm:lookup label="meioTransporte" labelWidth="15%" width="25%" maxLength="6" size="6"
		             dataType="text" 
		             picker="false"
		             property="meioTransporteRodoviario2" 
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
					 service="lms.seguros.emitirRelatorioSegurosAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onchange="meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler()"> 

			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporteRodoviario.idMeioTransporte"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  relatedProperty="meioTransporteRodoviario.nrIdentificador"/>
			<adsm:propertyMapping modelProperty="nrIdentificador"  criteriaProperty="meioTransporteRodoviario.nrIdentificador" disable="false"/>
			<adsm:propertyMapping relatedProperty="nrIdentificadorHidden"	modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="nrFrotaHidden"	modelProperty="nrFrota" /> 
			<adsm:hidden property="nrIdentificadorHidden" serializable="true" />
			
			<adsm:lookup dataType="text" 
			             property="meioTransporteRodoviario" 
			             idProperty="idMeioTransporte" 
			             criteriaProperty="nrIdentificador"
						 service="lms.seguros.emitirRelatorioSegurosAction.findLookupMeioTransporte" 
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="10" picker="true">
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteRodoviario2.nrFrota"/>
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteRodoviario2.nrFrota" disable="false"/>
				<adsm:propertyMapping relatedProperty="nrIdentificadorHidden"	modelProperty="nrIdentificador" />
				<adsm:propertyMapping relatedProperty="nrFrotaHidden"	modelProperty="nrFrota" />
				<adsm:hidden property="nrFrotaHidden" serializable="true" />
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:hidden property="dsTipoSinistro" />
		<adsm:combobox property="tipoSinistro.idTipoSinistro" label="tipoSinistro" width="30%" 
					optionLabelProperty="dsTipo" optionProperty="idTipoSinistro" 
					service="lms.seguros.emitirRelatorioSegurosAction.findComboTipoProcessoSinistro">
			<adsm:propertyMapping modelProperty="dsTipo" relatedProperty="dsTipoSinistro"/> 
		</adsm:combobox>

		<adsm:combobox property="localSinistro" label="localSinistro" labelWidth="10%" 
					width="15%" domain="DM_LOCAL_SINISTRO" renderOptions="true" onchange="onChangeLocalSinistro()" >
			<adsm:hidden property="dsLocalSinistro" serializable="true" />
		</adsm:combobox>
		
		<adsm:combobox property="tipoSeguro.idTipoSeguro" label="tipoSeguro" optionLabelProperty="sgTipo"
					optionProperty="idTipoSeguro" service="lms.seguros.emitirRelatorioSegurosAction.findComboTipoSeguro" 
					labelWidth="15%" width="15%" >
			<adsm:propertyMapping modelProperty="sgTipo" relatedProperty="tipoSeguro.dsTipoSeguro"/>
			<adsm:hidden property="tipoSeguro.dsTipoSeguro" serializable="true" />
		</adsm:combobox>
		
		<adsm:lookup service="lms.seguros.emitirRelatorioSegurosAction.findLookupRodovia" dataType="text" property="rodovia" idProperty="idRodovia" criteriaProperty="sgRodovia" label = "rodovia" size="10" maxLength="10" width="40%" action="/municipios/manterRodovias" >
			<adsm:propertyMapping relatedProperty="rodovia.dsRodovia" modelProperty="dsRodovia" />
			<adsm:propertyMapping relatedProperty="sgRodoviaHidden" modelProperty="sgRodovia" />
			<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="23" disabled="true" serializable="true" />
			<adsm:hidden property="sgRodoviaHidden" serializable="true" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio"
	             action="/municipios/manterMunicipios" service="lms.seguros.emitirRelatorioSegurosAction.findLookupMunicipio"
                 maxLength="60" size="30" minLengthForAutoPopUpSearch="3" exactMatch="false" 
                 label="municipio" labelWidth="15%" width="30%" required="false">
                <adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativaHidden" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativaHidden" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="municipio.nmMunicipioHidden" modelProperty="nmMunicipio" />
                <adsm:hidden property="municipio.nmMunicipioHidden" serializable="true" />
        </adsm:lookup>
		
		<adsm:lookup property="filialSinistro" idProperty="idFilial" required="false" criteriaProperty="sgFilial" maxLength="3"
			 service="lms.seguros.emitirRelatorioSegurosAction.findLookupFilial" dataType="text" label="filialSinistro" size="3" action="/municipios/manterFiliais"  width="40%" minLengthForAutoPopUpSearch="3" exactMatch="true" disabled="false" >
			<adsm:propertyMapping relatedProperty="filialSinistro.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialSinistro.pessoa.nmFantasia" size="30" disabled="true" serializable="true"/>
			<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado"  serializable="false"/>
			<adsm:hidden property="flagBuscaEmpresaUsuarioLogado"  serializable="false"/>
			<adsm:hidden property="sgFilialSinisitro"  serializable="true"/>
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping relatedProperty="sgFilialSinisitro" modelProperty="sgFilial"/>
		</adsm:lookup>
		
		<adsm:lookup property="unidadeFederativa"  idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
					 service="lms.seguros.emitirRelatorioSegurosAction.findLookupUnidadeFederativa" dataType="text" required="false"
					 labelWidth="15%" width="30%" label="uf" size="3" maxLength="3" 
					 action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="false">
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa"/>
			<adsm:textbox dataType="text" property="unidadeFederativa.nmUnidadeFederativa" size="25" serializable="false" disabled="true" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.sgUnidadeFederativaHidden" modelProperty="sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativaHidden" modelProperty="nmUnidadeFederativa" />
            <adsm:hidden property="unidadeFederativa.sgUnidadeFederativaHidden" serializable="true" />
            <adsm:hidden property="unidadeFederativa.nmUnidadeFederativaHidden" serializable="true" />
		</adsm:lookup>			

		<adsm:lookup label="aeroporto" service="lms.seguros.emitirRelatorioSegurosAction.findLookupAeroporto" action="municipios/manterAeroportos" 
			 	 	 dataType="text" property="aeroporto" idProperty="idAeroporto" labelWidth="15%" width="40%" size="3" 
			 	 	 maxLength="3" criteriaProperty="sgAeroporto" required="false">
 	 	    <adsm:propertyMapping relatedProperty="aeroporto.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
 	 	    <adsm:propertyMapping relatedProperty="sgAeroportoHidden" modelProperty="sgAeroporto" />
			<adsm:textbox property="aeroporto.pessoa.nmPessoa" dataType="text" size="30" maxLength="30" disabled="true" serializable="true"/>	 	
			<adsm:hidden property="sgAeroportoHidden"  serializable="true"/>    	      
	 	</adsm:lookup>

		<adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuarioResponsavel"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="10" 
					 maxLength="20"	
					 width="30%"		 					 
					 service="lms.seguros.emitirRelatorioSegurosAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="nmUsuario" disabled="true" maxLength="45" size="20"/>
			<adsm:propertyMapping relatedProperty="usuarioResponsavel.nrMatriculaHidden" modelProperty="nrMatricula" />
			<adsm:propertyMapping relatedProperty="usuarioResponsavel.nmUsuarioHidden" modelProperty="nmUsuario" />
			<adsm:hidden property="usuarioResponsavel.nrMatriculaHidden"  serializable="true"/>
			<adsm:hidden property="usuarioResponsavel.nmUsuarioHidden"  serializable="true"/>
		</adsm:lookup>		
		<%--Fim Filtros de Processo de Sinistro --%>
		
		
		<%-- Filtros de RIMs --%>
		<adsm:section caption="filtrosRims"/>
		
		<adsm:lookup label="filial" labelWidth="15%" width="40%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.seguros.emitirRelatorioSegurosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" serializable="false" size="30" maxLength="50" disabled="true"/>
            <adsm:propertyMapping relatedProperty="filialRIM.sgFilial" modelProperty="sgFilial" />
            <adsm:propertyMapping relatedProperty="filialRIM.nmFilial" modelProperty="pessoa.nmFantasia" />
            <adsm:hidden property="filialRIM.sgFilial"/>
            <adsm:hidden property="filialRIM.nmFilial"/>
        </adsm:lookup>
        
		<adsm:range label="dataEmissao" width="30%" labelWidth="15%">
			<adsm:textbox property="dataInicial" dataType="JTDate" />
			<adsm:textbox property="dataFinal" dataType="JTDate"/>
		</adsm:range>
	
		<adsm:range label="numero" width="40%" labelWidth="15%">
			<adsm:textbox property="nrReciboIndenizacaoInicial" dataType="integer" size="15"/>
			<adsm:textbox property="nrReciboIndenizacaoFinal" dataType="integer"   size="15"/>
		</adsm:range>
		
		<adsm:range label="dataLiberacaoPgto" width="30%" labelWidth="15%">
			<adsm:textbox property="dataLiberacaoPgtoInicial" dataType="JTDate" />
			<adsm:textbox property="dataLiberacaoPgtoFinal" dataType="JTDate"/>
		</adsm:range>
		
		<adsm:combobox label="tipoIndenizacao" property="tpIndenizacao" 
					domain="DM_TIPO_INDENIZACAO" required="false" labelWidth="15%" width="40%" 
					renderOptions="true" onchange="onChangeTipoIndenizacao()">
            <adsm:hidden property="dsTipoIndenizacao"/>
		</adsm:combobox>
		
		<adsm:range label="dataProgramadaPagamento" width="30%" labelWidth="15%">
			<adsm:textbox property="dataProgramadaPagamentoInicial" dataType="JTDate" />
			<adsm:textbox property="dataProgramadaPagamentoFinal" dataType="JTDate"/>
		</adsm:range>

		<adsm:combobox property="tpStatusIndenizacao" 
					   label="status"
					   domain="DM_STATUS_INDENIZACAO"  
					   labelWidth="15%" 
					   width="40%" >
			<adsm:propertyMapping relatedProperty="dsTpStatusIndenizacao" modelProperty="description" />
            <adsm:hidden property="dsTpStatusIndenizacao"/>
		</adsm:combobox>
					   
		<adsm:range label="dataPagamento" width="30%" labelWidth="15%">
			<adsm:textbox property="dataPagtoInicial" dataType="JTDate" />
			<adsm:textbox property="dataPagtoFinal" dataType="JTDate"/>
		</adsm:range>			   
					   
		<adsm:combobox property="tpSituacaoWorkFlow" 
					   label="situacaoAprovacao"
					   domain="DM_STATUS_WORKFLOW"  
					   labelWidth="15%" 
					   width="40%" >
			<adsm:propertyMapping relatedProperty="dsTpSituacaoWorkFlow" modelProperty="description" />
            <adsm:hidden property="dsTpSituacaoWorkFlow"/>
		</adsm:combobox>					   
					   
		<adsm:range label="dataLote" width="30%" labelWidth="15%">
			<adsm:textbox dataType="JTDate" property="dataLoteInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataLoteFinal" picker="true"/>
		</adsm:range>
		
   		<adsm:hidden property="naturezaProduto.dsNaturezaProdutoHidden"/>
		<adsm:combobox label="naturezaProduto" 
					property="naturezaProduto.idNaturezaProduto"
				    optionProperty="idNaturezaProduto" 
				    optionLabelProperty="dsNaturezaProduto" 
				    service="lms.seguros.emitirRelatorioSegurosAction.findNaturezaProduto" 
				    width="40%"
				    labelWidth="15%" 
				    onchange="onChangeNaturezaProduto()"/>

 		<adsm:textbox dataType="integer" 
 					property="nrLote" 
 					width="30%" 
 					maxLength="10" 
 					size="12"
 					labelWidth="15%" 
 					label="numeroLote" />

		<adsm:combobox property="formaPagto" 
					   label="formaPagamento"
					   domain="DM_FORMA_PAGAMENTO_INDENIZACAO"  
					   labelWidth="15%" 
					   width="40%" >
			<adsm:propertyMapping relatedProperty="dsFormaPagto" modelProperty="description" />
            <adsm:hidden property="dsFormaPagto"/>
		</adsm:combobox>

		<adsm:hidden property="siglaFilialRncHidden"/>
		<adsm:lookup label="filialRnc" labelWidth="15%" width="30%" 
             		 property="filialRnc"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.seguros.emitirRelatorioSegurosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filialRnc.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialRncHidden" modelProperty="sgFilial" />
          	<adsm:textbox dataType="text" property="filialRnc.pessoa.nmFantasia" size="25" maxLength="30" disabled="true" serializable="true"/>
        </adsm:lookup>

		<adsm:range label="valorIndenizacao" width="40%" labelWidth="15%">
			<adsm:textbox dataType="currency" property="valorIndenizacaoInicial" size="15" />
			<adsm:textbox dataType="currency" property="valorIndenizacaoFinal" size="15"  />
		</adsm:range>
		
		<adsm:range label="numeroRNC" width="30%" labelWidth="15%">
			<adsm:textbox dataType="integer" property="numeroRncInicial" size="13" />
			<adsm:textbox dataType="integer" property="numeroRncFinal" size="13"  />
		</adsm:range>		
		
        <adsm:lookup label="filialDebitada" labelWidth="15%" width="40%"
		             property="filialDebitada"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.seguros.emitirRelatorioSegurosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>					      
        	<adsm:propertyMapping relatedProperty="filialDebitada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filialDebitada.pessoa.nmFantasia" serializable="false" 
            			  size="30" maxLength="50" disabled="true"/>
            <adsm:propertyMapping relatedProperty="filialDebitada.sgFilialHidden" modelProperty="sgFilial" />
            <adsm:propertyMapping relatedProperty="filialDebitada.nmFilialHidden" modelProperty="pessoa.nmFantasia" />			  
        	<adsm:hidden property="filialDebitada.sgFilialHidden"/>
        	<adsm:hidden property="filialDebitada.nmFilialHidden"/>
        </adsm:lookup>

		<adsm:range label="dataEmissaoRnc" width="30%" labelWidth="15%">
			<adsm:textbox dataType="JTDate" property="dataEmissaoRncInicial" picker="true" size="18"/>
			<adsm:textbox dataType="JTDate" property="dataEmissaoRncFinal" picker="true" size="18"/>
		</adsm:range>
		
		<adsm:hidden property="siglaFilialOcorrencia1Hidden"/>
		<adsm:hidden property="nomeFilialOcorrencia1Hidden"/>
		<adsm:lookup label="filialOcorrencia1" labelWidth="15%" width="40%" 
             		 property="filialOcorrencia1"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.seguros.emitirRelatorioSegurosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filialOcorrencia1.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialOcorrencia1Hidden" modelProperty="sgFilial" />
      		<adsm:propertyMapping relatedProperty="nomeFilialOcorrencia1Hidden" modelProperty="pessoa.nmFantasia" />
          	<adsm:textbox dataType="text" 
          			property="filialOcorrencia1.pessoa.nmFantasia" 
          			size="30" 
          			maxLength="30" 
          			disabled="true"/>
        </adsm:lookup>	
		
		<adsm:hidden property="dsMotivoAberturaHidden"/>		
		<adsm:combobox label="motivoAbertura" 
					   labelWidth="15%" 
					   width="30%"
					   property="motivoAbertura.idMotivoAberturaNc" 
					   optionProperty="idMotivoAberturaNc" 
					   optionLabelProperty="dsMotivoAbertura" 
					   service="lms.seguros.emitirRelatorioSegurosAction.findComboMotivoAberturaNc"
					   onchange="onChangeMotivoAbertura()"/>

		<adsm:hidden property="siglaFilialOcorrencia2Hidden"/>
		<adsm:hidden property="nomeFilialOcorrencia2Hidden"/>
		<adsm:lookup label="filialOcorrencia2" labelWidth="15%" width="40%" 
             		 property="filialOcorrencia2"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.seguros.emitirRelatorioSegurosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filialOcorrencia2.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialOcorrencia2Hidden" modelProperty="sgFilial" />
      		<adsm:propertyMapping relatedProperty="nomeFilialOcorrencia2Hidden" modelProperty="pessoa.nmFantasia" />
          	<adsm:textbox dataType="text" 
          			property="filialOcorrencia2.pessoa.nmFantasia" 
          			size="30" 
          			maxLength="30" 
          			disabled="true"/>
        </adsm:lookup>
        
        <adsm:hidden property="blSalvadosHidden"/>
		<adsm:combobox property="blSalvados" 
					   label="salvados" 
					   domain="DM_SIM_NAO" 
					   width="30%" renderOptions="true"
					   labelWidth="15%"
					   onchange="onChangeSalvados()"/>	        

		<adsm:hidden property="beneficiario.nrIdentificacaoHidden"/>
		<adsm:lookup label="beneficiario" 
					 dataType="text" 
					 property="beneficiario" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.seguros.emitirRelatorioSegurosAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="15%" 
					 width="80%">
					 
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="beneficiario.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="beneficiario.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="nrIdentificacaoFormatado" relatedProperty="beneficiario.nrIdentificacaoHidden" />
			<adsm:textbox dataType="text" 
						  property="beneficiario.nmPessoa" 
						  size="60" 
						  maxLength="60" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>
        
		<adsm:hidden property="favorecido.nrIdentificacaoHidden"/>
		<adsm:lookup label="favorecido" 
					 dataType="text" 
					 property="favorecido" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.seguros.emitirRelatorioSegurosAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="15%" 
					 width="80%">
					 
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="favorecido.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="favorecido.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="nrIdentificacaoFormatado" relatedProperty="favorecido.nrIdentificacaoHidden" />
			
			<adsm:textbox dataType="text" 
						  property="favorecido.nmPessoa" 
						  size="60" 
						  maxLength="60" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>

		<%-- Filtros de Docto de Serviço --%>
		<adsm:section caption="filtrosDoctoServico"/>

		<adsm:combobox property="doctoServico.tpDocumentoServico" 
						label="documentoServico" labelWidth="15%" width="40%"
						service="lms.seguros.emitirRelatorioSegurosAction.findTpDoctoServico"
						optionProperty="value" optionLabelProperty="description"
						onchange="getTpDoctoServico(); return changeDocumentWidgetType({
						 documentTypeElement:this, 
						 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
						 parentQualifier:'',
						 documentGroup:'SERVICE',
						 actionService:'lms.seguros.emitirRelatorioSegurosAction'
						});">
			<adsm:hidden property="doctoServico.dsTpDocumentoServico"/>

			<adsm:lookup dataType="text"
						property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial"
						criteriaProperty="sgFilial" service="" disabled="true" action=""
						size="3" maxLength="3" picker="false"
						onDataLoadCallBack="enableDoctoServico"
						onchange="return changeDocumentWidgetFilial({
						filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
						documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						});" >
				<adsm:propertyMapping relatedProperty="doctoServico.sgFilialOrigem" modelProperty="sgFilial" />
				<adsm:hidden property="doctoServico.sgFilialOrigem"/>
			</adsm:lookup>

			<adsm:lookup dataType="integer" property="doctoServico"
						idProperty="idDoctoServico" criteriaProperty="nrDoctoServico"
						service="" action="" onDataLoadCallBack="retornoDocumentoServico"
						popupLabel="pesquisarDocumentoServico" size="10" maxLength="8"
						serializable="true" disabled="true" mask="00000000" >
				<adsm:hidden property="doctoServico.nrDocto"/>
			</adsm:lookup>

		</adsm:combobox>
		
		<adsm:range label="dataEmissao" width="30%" labelWidth="15%">
			<adsm:textbox property="dataEmissaoDocInicial" dataType="JTDate" />
			<adsm:textbox property="dataEmissaoDocFinal" dataType="JTDate"/>
		</adsm:range>		
		
		<adsm:lookup dataType="text" property="doctoServico.clienteByIdClienteRemetente" idProperty="idCliente" 
 					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 action="/vendas/manterDadosIdentificacao" service="lms.seguros.emitirRelatorioSegurosAction.findClienteLookup"  
 					 label="remetente" width="85%" maxLength="20" size="20">
 			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacaoFormatado" relatedProperty="remetente.nrIdentificacaoHidden" />
			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" /> 
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/> 
 			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa" size="60" disabled="true" serializable="true"/>
 			<adsm:hidden property="remetente.nrIdentificacaoHidden"/>
 		</adsm:lookup>		 
		
		<adsm:lookup dataType="text" property="doctoServico.clienteByIdClienteDestinatario" idProperty="idCliente" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 action="/vendas/manterDadosIdentificacao" service="lms.seguros.emitirRelatorioSegurosAction.findClienteLookup"  
 					 label="destinatario" width="85%" maxLength="20" size="20">
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacaoFormatado" relatedProperty="destinatario.nrIdentificacaoHidden" />
 			<adsm:propertyMapping relatedProperty="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" /> 
 			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/> 
 			<adsm:textbox dataType="text" property="doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa" size="60" disabled="true" serializable="true"/>
 			<adsm:hidden property="destinatario.nrIdentificacaoHidden"/>
 		</adsm:lookup>		 
		
		<adsm:hidden property="tpSituacao" />
		<adsm:hidden property="devedor.nrIdentificacaoHidden"/>
		<adsm:lookup label="devedor" 
					 dataType="text" 
					 property="devedor" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.seguros.emitirRelatorioSegurosAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="15%" 
					 width="83%">
					 
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="devedor.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="devedor.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="nrIdentificacaoFormatado" relatedProperty="devedor.nrIdentificacaoHidden" />
			
			<adsm:textbox dataType="text" property="devedor.nmPessoa" size="60" maxLength="60" disabled="true" serializable="true"/>
		</adsm:lookup>		
		
		<adsm:hidden property="modalHidden"/>
		<adsm:combobox property="modal" 
					   label="modal" 
					   domain="DM_MODAL" 
					   width="16%" renderOptions="true"
					   labelWidth="15%"
					   onchange="onChangeModal()"/>	

		<adsm:hidden property="abrangenciaHidden"/>
		<adsm:combobox property="abrangencia" 
					   label="abrangencia" 
					   domain="DM_ABRANGENCIA" 
					   width="16%" renderOptions="true"
					   labelWidth="10%"
					   onchange="onChangeAbrangencia()"/>	
 
		<adsm:hidden property="dsServicoHidden" />
		<adsm:combobox label="servico" 
						property="servico.idServico" 
						width="34%"
						labelWidth="7%" 
					   	optionProperty="idServico" 
					   	optionLabelProperty="dsServico" 
					   	service="lms.seguros.emitirRelatorioSegurosAction.findComboServico"
					   	onchange="onChangeServico()"/>
		
		<%-- Opções de exibição --%>
		<adsm:section caption="opcoesExibicao"/>
		
		<adsm:multicheckbox property="checkProcessosSinistro|checkRims|checkCtes|checkNfs|" 
							texts="listarProcessosSinistro|listarRims|listarCtes|listarNfs" 
							width="90%" labelWidth="30%"/>
	
		<adsm:buttonBar freeLayout="bottom">
			<adsm:button id="visualizarRelatorio" caption="visualizar" onclick="emitirReport()" disabled="false"/>
			<adsm:resetButton id="newButton" caption="limpar" />
		</adsm:buttonBar>
	
	</adsm:form>
	
</adsm:window>

<script>

	function initWindow(eventObj){
		setDisabled("visualizarRelatorio", false);
		
		if (eventObj.name == 'cleanButton_click') {
			limparTagManifesto();
		}
		disableControleCarga(true);
	}

    function emitirReport() {
    	
    	// Verifica se pelo menos um campo de filtro está preenchido
        if (!validaFiltroInformado()) {
            alert("LMS-00055- " + i18NLabel.getLabel("LMS-00055"));
            return false;
        }
        
        // Valida se pelo menos uma das checkbox foram marcadas na tela
        if (validaCheckboxInformado()) {
        	alert("LMS-37024- " + i18NLabel.getLabel("LMS-37024"));
			return false;
        }

        executeReportWithCallback('lms.seguros.emitirRelatorioSegurosAction.executeReport', 'openCSVReportWithLocator', document.forms[0]);
    }
    
	function openCSVReportWithLocator_cb(strFile, error) {
		if(error == undefined) {
			reportUrl = contextRoot+"/viewBatchReport?open=false&"+strFile._value;
			location.href(reportUrl);
		} else {
			alert(error);
		}
	}

	// Função usada para validar os filtros da tela, verifica se pelo menos um dos filtros está preenchido
	function validaFiltroInformado() {
		// Pega os elementos do formulario
		var formElems = this.document.forms[0];

		// Percorre todos os elementos do formulario
		for (var i = 0; i < formElems.length; i++) {
			// Pega o elemento atual
			var currentElem = formElems[i];

			// Descarta os filtros do tipo checkbox e os botões da tela
			if (currentElem.type != 'checkbox' && currentElem.type != 'button') {
				
				// Usado em alguns casos especificos, como nas lookups    
				// deverá ser validado apenas o id e não as outras propriedades
				if (!currentElem.name.equals("manifesto.tpManifesto")
						&& !currentElem.name.equals("manifesto.filialByIdFilialOrigem.idFilial")
						&& !currentElem.name.equals("manifesto.filialByIdFilialOrigem.pessoa.nmFantasia")
						&& !currentElem.name.equals("manifesto.filialByIdFilialOrigem.sgFilial")
						&& !currentElem.name.equals("manifesto.filialByIdFilialOrigem.pessoa.sgFilialHidden")
						&& !currentElem.name.equals("manifesto.tpManifestoHidden")
						&& !currentElem.name.equals("controleCarga.filialByIdFilialOrigem.idFilial")
						&& !currentElem.name.equals("controleCarga.filialByIdFilialOrigem.sgFilial")
						&& !currentElem.name.equals("filialByIdFilialOrigem.nmFilial")
						&& !currentElem.name.equals("usuario.tpCategoriaUsuario")
						&& !currentElem.name.equals("doctoServico.tpDocumentoServico")
						&& !currentElem.name.equals(".doctoServico.filialByIdFilialOrigem.idFilial")
						&& !currentElem.name.equals("doctoServico.dsTpDocumentoServico")
						&& !currentElem.name.equals("doctoServico.sgFilialOrigem")
						&& !currentElem.name.equals(".doctoServico.filialByIdFilialOrigem.sgFilial")) {
					
					// Verifica se o elemento não está vazio, se encontrar pelo menos um preenchido então retorna 'true'
					if (trim(currentElem.value.toString().split('\r\n').join('')).length > 0) {
						return true;
					}
				}
			}

		}

		// Caso nenhum campo esteja preenchido retorna 'false'		
		return false;
	}


	// Valida se pelo menos uma checkbox foi marcada nas opções de exibição da tela
	function validaCheckboxInformado() {

		// Se nenhuma checkbox estiver marcada então retorna 'true'
		if (getElementValue("checkProcessosSinistro") == false
				&& getElementValue("checkRims") == false
				&& getElementValue("checkCtes") == false
				&& getElementValue("checkNfs") == false) {
				return true;
		}

		return false;
	}

	function manifestoNrManifestoOrigem_OnChange() {
		var r = manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
		if (getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
			resetaControleCarga();
			resetValue('manifesto.idManifesto');
		} else {
			setElementValue('manifesto.nrManifesto',
				getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
		return r;
	}

	function resetaControleCarga() {
		resetValue('manifesto.idManifesto');
		resetValue('controleCarga.idControleCarga');
		resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
		limparDadosFrota();
	}

	function limparDadosFrota() {
		resetValue('meioTransporteRodoviario2.nrFrota');
		resetValue('meioTransporteRodoviario.nrIdentificador');
		resetValue('meioTransporteRodoviario.idMeioTransporte');
	}

	function limpaManifesto() {
		resetValue('manifesto.idManifesto');
		disableControleCarga(true);
	}

	/**
	 * Limpa os dados informados na tag manifesto
	 */
	function limparTagManifesto() {
		resetValue("manifesto.tpManifesto");
		resetValue("manifesto.idManifesto");
		resetValue("manifesto.filialByIdFilialOrigem.idFilial");
		resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
		desabilitaTagManifesto(true);
		disableControleCarga(true);
	}

	function desabilitaTagManifesto(valor) {
		if (valor == true) {
			setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
			setDisabled(
					'manifesto.manifestoViagemNacional.idManifestoViagemNacional',
					true);
		} else {
			if (getElementValue('manifesto.idManifesto') != ""
					|| getElementValue('manifesto.tpManifesto') != "")
				setDisabled('manifesto.filialByIdFilialOrigem.idFilial', false);
			if (getElementValue('manifesto.idManifesto') != ""
					|| getElementValue('manifesto.filialByIdFilialOrigem.idFilial') != "")
				setDisabled(
						'manifesto.manifestoViagemNacional.idManifestoViagemNacional',
						false);
		}
	}

	/**
	 * Quando o "Manifesto" for informado
	 */
	function retornoManifesto_cb(data) {
		var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
		if (r == true) {
			var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
			setElementValue('manifesto.idManifesto', idManifesto);
			buscarManifesto(idManifesto);
		}
	}

	function manifestoAfterPopupSetValue(data) {
		setDisabled('manifesto.manifestoViagemNacional.nrManifestoOrigem',
				false);
		var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		setElementValue('manifesto.idManifesto', idManifesto);
		buscarManifesto(idManifesto);
	}

	/**
	 * Busca os dados relacionados ao manifesto.
	 */
	function buscarManifesto(idManifesto) {
		var sdo = createServiceDataObject(
				"lms.seguros.emitirRelatorioSegurosAction.findManifestoByRNC",
				"resultado_buscarManifesto", {
					idManifesto : idManifesto
				});
		xmit({
			serviceDataObjects : [ sdo ]
		});
	}

	/**
	 * Povoa os campos com os dados retornados da busca em manifesto
	 */
	function resultado_buscarManifesto_cb(data, error) {
		if (data != undefined) {
			setElementValue('controleCarga.idControleCarga',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.idControleCarga"));
			setElementValue('controleCarga.nrControleCarga',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.nrControleCarga"));
			setElementValue('controleCarga.nrControleCargaHidden',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.nrControleCarga"));
			setElementValue('controleCarga.filialByIdFilialOrigem.idFilial',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.filialByIdFilialOrigem.idFilial"));
			setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.filialByIdFilialOrigem.sgFilial"));
			setElementValue('controleCarga.sgFilialOrigemHidden',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.filialByIdFilialOrigem.sgFilial"));				
			setElementValue(
					'meioTransporteRodoviario.idMeioTransporte',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.meioTransporteByIdTransportado.idMeioTransporte"));
			setElementValue(
					'meioTransporteRodoviario2.nrFrota',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.meioTransporteByIdTransportado.nrFrota"));
			setElementValue(
					'nrFrotaHidden',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.meioTransporteByIdTransportado.nrFrota"));
			setElementValue(
					'meioTransporteRodoviario.nrIdentificador',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
			setElementValue(
					'nrIdentificadorHidden',
					getNestedBeanPropertyValue(data,
							"0:controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
							
			format(document.getElementById("controleCarga.nrControleCarga"));
			disableControleCarga(false);
		}
	}

	function enableManifestoManifestoViagemNacioal_cb(data) {
		var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if (r == true) {
		
			setElementValue(
					'manifesto.filialByIdFilialOrigem.pessoa.sgFilialHidden',
					getNestedBeanPropertyValue(data,"0:sgFilial"));
		
			setDisabled(
					"manifesto.manifestoViagemNacional.idManifestoViagemNacional",
					false);
			setFocus(document
					.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
	}

	function disableControleCarga(disable) {
		setDisabled('controleCarga.nrControleCarga', disable);
		if (disable == true) {
			resetValue('controleCarga.idControleCarga');
			resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
		}
	}

	function onFilialControleCargaChange() {
		if (getElementValue("controleCarga.filialByIdFilialOrigem.sgFilial") == "") {
			disableNrControleCarga(true);
			resetValue("controleCarga.idControleCarga");
			return true;

		} else {
			disableNrControleCarga(false);
			return lookupChange({
				e : document.forms[0].elements["controleCarga.filialByIdFilialOrigem.idFilial"]
			});
		}
	}

	function onControleCargaPopupSetValue(data) {
		setDisabled('controleCarga.idControleCarga', false);
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial',
				data.filialByIdFilialOrigem.sgFilial);
	}

	function disableNrControleCarga(disable) {
		setDisabled("controleCarga.nrControleCarga", disable);
	}

	function disableNrControleCarga_cb(data, error) {
		if (data.length == 0)
			disableNrControleCarga(false);
		return lookupExactMatch({
			e : document
					.getElementById("controleCarga.filialByIdFilialOrigem.idFilial"),
			data : data
		});
	}

	function meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler() {

		meioTransporteRodoviario2_nrFrotaOnChangeHandler();

		if (document.getElementById("meioTransporteRodoviario2.nrFrota").value == "") {
			document
					.getElementById("meioTransporteRodoviario.idMeioTransporte").value = "";
			resetValue(document
					.getElementById("meioTransporteRodoviario.idMeioTransporte"));
			// document.getElementById("meioTransporteByIdMeioTransporte.nrIdentificador").value="";
		}
	}

	function onChangeNaturezaProduto() {
		var combo = document
				.getElementById("naturezaProduto.idNaturezaProduto");
		setElementValue('naturezaProduto.dsNaturezaProdutoHidden',
				combo.options[combo.selectedIndex].text);
	}

	function onChangeMotivoAbertura() {
		var combo = document
				.getElementById("motivoAbertura.idMotivoAberturaNc");
		setElementValue('dsMotivoAberturaHidden',
				combo.options[combo.selectedIndex].text);
	}

	function onChangeSalvados() {
		var combo = document.getElementById("blSalvados");
		setElementValue('blSalvadosHidden',
				combo.options[combo.selectedIndex].text);
	}

	function enableDoctoServico_cb(data) {
		var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		if (r == true) {
			setDisabled("doctoServico.idDoctoServico", false);
			setFocus(document.getElementById("doctoServico.nrDoctoServico"));
		}
	}

	function retornoDocumentoServico_cb(data) {
		setElementValue('doctoServico.nrDocto', getNestedBeanPropertyValue(data,
							"0:nrDoctoServico"));
		doctoServico_nrDoctoServico_exactMatch_cb(data);
	}

	function onChangeModal() {
		var combo = document.getElementById("modal");
		setElementValue('modalHidden', combo.options[combo.selectedIndex].text);
	}
	
	function onChangeLocalSinistro() {
		var combo = document.getElementById("localSinistro");
		setElementValue('dsLocalSinistro', combo.options[combo.selectedIndex].text);
	}

	function onChangeAbrangencia() {
		var combo = document.getElementById("abrangencia");
		setElementValue('abrangenciaHidden',
				combo.options[combo.selectedIndex].text);
	}

	function onChangeServico() {
		var combo = document.getElementById("servico.idServico");
		setElementValue('dsServicoHidden',
				combo.options[combo.selectedIndex].text);
	}
	
	function onChangeTipoIndenizacao() {
		var combo = document.getElementById("tpIndenizacao");
		setElementValue('dsTipoIndenizacao',
				combo.options[combo.selectedIndex].text);
	}
	
	function getTpManifesto() {
		var combo = document.getElementById("manifesto.tpManifesto");
		setElementValue('manifesto.tpManifestoHidden',
				combo.options[combo.selectedIndex].text);
	}
	
	function getTpDoctoServico() {
		var combo = document.getElementById("doctoServico.tpDocumentoServico");
		setElementValue('doctoServico.dsTpDocumentoServico',
				combo.options[combo.selectedIndex].text);
	}
	
</script>