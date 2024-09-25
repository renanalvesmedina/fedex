<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="390" idProperty="idDoctoServico" onDataLoadCallBack="callBackForm">
		<adsm:i18nLabels>
                <adsm:include key="LMS-10047"/>
       	</adsm:i18nLabels>
        
		<adsm:hidden property="idDoctoServicoConsulta"/>
		<adsm:hidden property="idAgendamentoEntrega"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="false"/>
		
		<adsm:hidden property="nomeEmpresa"/>
		<adsm:hidden property="nrIdentificacaoEmpresa"/>
		<adsm:hidden property="empresa.idEmpresa"/>
			
		<adsm:combobox property="modal" domain="DM_MODAL" label="modal" labelWidth="22%" width="28%" />
		
		<adsm:combobox property="abrangencia" domain="DM_ABRANGENCIA" label="abrangencia" labelWidth="22%" width="28%" />
		
		<adsm:combobox property="tipoServico.idTipoServico" optionLabelProperty="dsTipoServico" optionProperty="idTipoServico" service="lms.sim.consultarLocalizacoesMercadoriasAction.findComboTipoServico" label="tipoServico" labelWidth="22%" width="28%" boxWidth="130"/>
		
		<%--LOOKUP FILIAL ORIGEM--%>
		<adsm:lookup dataType="text" onchange="return onFilialOrigemChange();" property="filialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial" label="filialOrigem2" labelWidth="22%" width="78%" size="3" exactMatch="false" minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" maxLength="3">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping criteriaProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="nomeEmpresa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="nrIdentificacaoEmpresa" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" disabled="true" size="30"/>
		</adsm:lookup>
		
		<%--LOOKUP FILIAL DESTINO--%>
		<adsm:lookup dataType="text" onchange="return onFilialDestinoChange();" property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial" service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial" label="filialDestino2" labelWidth="22%" width="78%" size="3" exactMatch="false" minLengthForAutoPopUpSearch="3" action="/municipios/manterFiliais" maxLength="3">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso"/>
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" disabled="true" size="30"/>
		</adsm:lookup>
		
		<adsm:combobox property="finalidade" domain="DM_TIPO_CONHECIMENTO" label="finalidade" labelWidth="22%" width="23%" />
		
		<%--DOCTO SERVIÇO--%>
		 <adsm:hidden property="idDoctoServicoReembolsado"/>
		 <adsm:hidden property="tpDocumentoServico"/>
		 <adsm:combobox property="doctoServico.tpDocumentoServico" 
					   label="documentoServico" labelWidth="22%" width="33%" 
					   service="lms.sim.consultarLocalizacoesMercadoriasAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="return changeComboDocumentoServicoType(this);" >

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true"
						 action="" 
						 size="3" maxLength="3" picker="false" 
						 onDataLoadCallBack="filialDoctoServico"
						 onchange="return changeComboDocumentoServicoFilial();" popupLabel="pesquisarFilial">
			</adsm:lookup>				  
			
			<adsm:lookup dataType="integer" 
						 property="doctoServico" 
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 onDataLoadCallBack="doctoServicoCb"
						 onchange="return changeDocumentoServico();"
						 onPopupSetValue="retornoPopPupDoctoServico"
						 size="20" maxLength="10" mask="0000000000" serializable="true" disabled="true" popupLabel="pesquisarDocumentoServico">
			</adsm:lookup>
			
			<adsm:hidden property="filialByIdFilialOrigem.pessoa.nmFantasia"/>
			
		</adsm:combobox> 
		<%--FIM DOCTO SERVICO--%> 
		
		<adsm:lookup
			service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial"
			action="/municipios/manterFiliais"
			property="filial"
			idProperty="idFilial"
			onchange="return changeFilialCotacao(this);"
			onDataLoadCallBack="filial"
			criteriaProperty="sgFilial"
			label="filialNumeroCotacao"
			dataType="text"
			size="3"
			maxLength="3"
			labelWidth="22%"
			width="9%" popupLabel="pesquisarFilial">
			<adsm:propertyMapping criteriaProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" inlineQuery="true"/>
			<adsm:propertyMapping criteriaProperty="nomeEmpresa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="nrIdentificacaoEmpresa" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>	
			<adsm:textbox
				property="nrCotacao"
				dataType="integer"
				size="8"
				maxLength="8"
				width="14%" disabled="true"/>
		</adsm:lookup>
		
		<%--LOOKUP PEDIDO COLETA--%>
		<adsm:hidden property="pedidoColeta.filialByIdFilialResponsavel.pessoa.nmFantasia"/>
		<adsm:lookup dataType="text" 
					 property="pedidoColeta.filialByIdFilialResponsavel"  
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial" 
					 action="/municipios/manterFiliais"  
					 label="pedidoColeta" size="3" maxLength="3" labelWidth="22%" width="23%" picker="false" 
					 serializable="true" onchange="return onFilialPedColChange(this);" popupLabel="pesquisarFilial">
					 <adsm:propertyMapping  relatedProperty="pedidoColeta.filialByIdFilialResponsavel.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia"/>
					 <adsm:propertyMapping criteriaProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" inlineQuery="true"/>
					 <adsm:propertyMapping criteriaProperty="nomeEmpresa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
					 <adsm:propertyMapping criteriaProperty="nrIdentificacaoEmpresa" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>	
			<adsm:lookup dataType="integer" 
						 property="pedidoColeta" idProperty="idPedidoColeta" criteriaProperty="nrColeta"
						 action="/coleta/consultarColetas" cmd="pesq" 
						 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupPedidoColeta"
						 mask="00000000" 
						 exactMatch="false" 
						 size="10" maxLength="8" onPopupSetValue="retornoPopPup_pedidoColeta" disabled="true"> 
						 <adsm:propertyMapping criteriaProperty="pedidoColeta.filialByIdFilialResponsavel.sgFilial" modelProperty="filialByIdFilialResponsavel.sgFilial" disable="true" />
						 <adsm:propertyMapping criteriaProperty="pedidoColeta.filialByIdFilialResponsavel.idFilial" modelProperty="filialByIdFilialResponsavel.idFilial" disable="true"/>
						 <adsm:propertyMapping criteriaProperty="pedidoColeta.filialByIdFilialResponsavel.pessoa.nmFantasia" modelProperty="filialByIdFilialResponsavel.pessoa.nmFantasia"/>
						 <adsm:propertyMapping relatedProperty="periodoInicial" modelProperty=""/>
						 <adsm:propertyMapping relatedProperty="periodoFinal" modelProperty=""/>
			</adsm:lookup>
		</adsm:lookup>
		<%--FIM PEDIDO COLETA--%>

		<adsm:textbox dataType="integer" property="nfCliente" size="28" width="23%" label="notaFiscalCliente" labelWidth="22%"/>
		
		<adsm:range label="periodoEmissao" labelWidth="22%" width="28%">
			<adsm:textbox dataType="JTDate" property="periodoInicial" />
			<adsm:textbox dataType="JTDate" property="periodoFinal" />
		</adsm:range>
		
		<%--------------------------------------------REMENTENTE--------------------------------------------------%>
		<adsm:section caption="remetente"/>
		
		<%--LOOKUP REMETENTE--%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" onchange="return onRemetenteChange();" dataType="text" property="remetente" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="remetente" size="20" maxLength="20" labelWidth="22%" width="78%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="remetente.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        
           	<adsm:propertyMapping relatedProperty="remetenteFantasia.nmFantasia" modelProperty="pessoa.nmFantasia"/> 
           	<adsm:propertyMapping relatedProperty="remetenteFantasia.idCliente" modelProperty="idCliente"/> 
         	<adsm:propertyMapping relatedProperty="remetenteConta.nrConta" modelProperty="nrConta"/> 
         	<adsm:propertyMapping relatedProperty="remetenteConta.idCliente" modelProperty="idCliente"/> 
        	        	        	
        	<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" />
        </adsm:lookup>
        
		<%--LOOKUP NOME FANTASIA REMETENTE--%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" onchange="return onRemetenteFantasiaChange();" dataType="text" property="remetenteFantasia" criteriaProperty="nmFantasia" idProperty="idCliente" label="nomeFantasia" size="30" maxLength="50" labelWidth="22%" width="28%" action="/vendas/manterDadosIdentificacao" cmd="list"  >
			<adsm:propertyMapping relatedProperty="remetenteFantasia.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/>
			<adsm:propertyMapping relatedProperty="remetente.idCliente" modelProperty="idCliente"/>
			<adsm:propertyMapping relatedProperty="remetenteConta.nrConta" modelProperty="nrConta"/>
			<adsm:propertyMapping relatedProperty="remetenteConta.idCliente" modelProperty="idCliente"/>
		
		</adsm:lookup>
		
		<%--LOOKUP CONTA REMETENTE--%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" dataType="integer" onchange="return onRemetenteContaChange();" property="remetenteConta" criteriaProperty="nrConta" idProperty="idCliente" label="numeroContaRemetente" size="20" maxLength="50" labelWidth="22%" width="28%" action="/vendas/manterDadosIdentificacao" cmd="list">
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/>
			<adsm:propertyMapping relatedProperty="remetente.idCliente" modelProperty="idCliente"/>
			<adsm:propertyMapping relatedProperty="remetenteFantasia.nmFantasia" modelProperty="pessoa.nmFantasia"/> 
           	<adsm:propertyMapping relatedProperty="remetenteFantasia.idCliente" modelProperty="idCliente"/> 
       </adsm:lookup>
              
        <adsm:combobox onchange="onChangeDocumentoCliente(this);" disabled="true" onDataLoadCallBack="setaComboDoctoCliente" service="lms.sim.consultarLocalizacoesMercadoriasAction.findComboDoctoCliente" property="informacaoDoctoCliente.idInformacaoDoctoCliente" optionLabelProperty="dsCampo" optionProperty="idInformacaoDoctoCliente" label="documentoCliente" labelWidth="22%" width="28%" cellStyle="vertical-align:bottom;">
        	<adsm:propertyMapping criteriaProperty="remetente.idCliente" modelProperty="cliente.idCliente"/>
        	<adsm:propertyMapping criteriaProperty="remetenteFantasia.idCliente" modelProperty="cliente.idCliente"/>
        	<adsm:propertyMapping criteriaProperty="remetenteConta.idCliente" modelProperty="cliente.idCliente"/>
        	<adsm:propertyMapping criteriaProperty="modal" modelProperty="tpModal" />
        	<adsm:propertyMapping criteriaProperty="abrangencia" modelProperty="tpAbrangencia" />
        	<adsm:propertyMapping relatedProperty="nrDocumentoCliente" modelProperty=""/>
        	<adsm:propertyMapping relatedProperty="infDocCliFormat" modelProperty="dsFormatacao"/>
        	<adsm:propertyMapping relatedProperty="infDocCliTpCampo" modelProperty="tpCampo"/>
        </adsm:combobox>
        <adsm:hidden property="infDocCliFormat"/>
        <adsm:hidden property="infDocCliTpCampo"/>
		
		<adsm:textbox dataType="text" property="nrDocumentoCliente" size="28" width="28%" label="numeroDocumento" labelWidth="22%" required="false" disabled="true"/>

		<%--------------------------------------------DESTINATARIO--------------------------------------------------%>
		<adsm:section caption="destinatario"/>
		<%--LOOKUP DESTINATARIO--%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" onchange="return onDestinatarioChange();" dataType="text" property="destinatario" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="destinatario" size="20" maxLength="20" labelWidth="22%" width="78%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="destinatario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	
        	<adsm:propertyMapping relatedProperty="destinatarioFantasia.nmFantasia" modelProperty="pessoa.nmFantasia"/> 
        	<adsm:propertyMapping relatedProperty="destinatarioFantasia.idCliente" modelProperty="idCliente"/>
        	
        	<adsm:propertyMapping relatedProperty="destinatarioConta.nrConta" modelProperty="nrConta"/> 
        	<adsm:propertyMapping relatedProperty="destinatarioConta.idCliente" modelProperty="idCliente"/>
        	
        	<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" />
        </adsm:lookup>
        
		<%--LOOKUP NOME FANTASIA DESTINATARIO--%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" dataType="text" property="destinatarioFantasia" criteriaProperty="nmFantasia" idProperty="idCliente" label="nomeFantasia" size="30" maxLength="50" labelWidth="22%" width="28%" action="/vendas/manterDadosIdentificacao" cmd="list" exactMatch="false" minLengthForAutoPopUpSearch="4">
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="destinatarioFantasia.nmFantasia" modelProperty="pessoa.nmFantasia"/>	 
        	<adsm:propertyMapping relatedProperty="destinatario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	<adsm:propertyMapping relatedProperty="destinatario.idCliente" modelProperty="idCliente"/> 
        	<adsm:propertyMapping relatedProperty="destinatarioConta.nrConta" modelProperty="nrConta"/> 
        	<adsm:propertyMapping relatedProperty="destinatarioConta.idCliente" modelProperty="idCliente"/>
		</adsm:lookup>
		
		<%--LOOKUP CONTA DESTINATARIO--%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" dataType="integer" property="destinatarioConta" criteriaProperty="nrConta" idProperty="idCliente" label="numeroContaDestinatario" size="20" maxLength="50" labelWidth="22%" width="28%" action="/vendas/manterDadosIdentificacao" cmd="list">
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="destinatario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	<adsm:propertyMapping relatedProperty="destinatario.idCliente" modelProperty="idCliente"/> 
        	<adsm:propertyMapping relatedProperty="destinatarioFantasia.nmFantasia" modelProperty="pessoa.nmFantasia"/> 
        	<adsm:propertyMapping relatedProperty="destinatarioFantasia.idCliente" modelProperty="idCliente"/>
        </adsm:lookup>
        	
        	
		<%--------------------------------------------CRITERIOS COMPLEMENTARES--------------------------------------------------%>
		<adsm:section caption="criteriosComplementares"/>
		
		<%--LOOKUP CONSIGNATARIO --%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" onchange="return onConsignatarioChange();" dataType="text" property="consignatario" relatedCriteriaProperty="nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="consignatario" size="20" maxLength="20" labelWidth="22%" width="78%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping relatedProperty="consignatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="consignatario.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	<adsm:textbox dataType="text" property="consignatario.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" />
        </adsm:lookup>
        
        <%--LOOKUP REDESPACHO --%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" onchange="return onRedespachoChange();" dataType="text" property="redespacho" relatedCriteriaProperty="nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="redespacho" size="20" maxLength="20" labelWidth="22%" width="78%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping relatedProperty="redespacho.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="redespacho.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	<adsm:textbox dataType="text" property="redespacho.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" />
        </adsm:lookup>
        
        <%--LOOKUP RESPONSÁVEL PELO FRETE--%>
		<adsm:lookup service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupCliente" onchange="return onResponsavelFreteChange();" dataType="text" property="responsavelFrete" relatedCriteriaProperty="nrIdentificacaoFormatado" criteriaProperty="pessoa.nrIdentificacao" idProperty="idCliente" label="responsavelFrete" size="20" maxLength="20" labelWidth="22%" width="78%" action="/vendas/manterDadosIdentificacao" cmd="list">
        	<adsm:propertyMapping relatedProperty="responsavelFrete.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/> 
        	<adsm:propertyMapping relatedProperty="responsavelFrete.pessoa.nrIdentificacao" modelProperty="pessoa.nrIdentificacaoFormatado"/> 
        	<adsm:textbox dataType="text" property="responsavelFrete.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" />
        </adsm:lookup>
        
        <%--LOOKUP CONTROLE DE CARGA--%>
        <adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia"/>
        <adsm:lookup dataType="text" popupLabel="pesquisarFilial"
				property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
				service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial"
				label="controleCarga" size="3" maxLength="3" width="28%" labelWidth="22%" picker="false" serializable="true"
				action="/municipios/manterFiliais" onchange="return onFilialControleCargaChange(this);">
				<adsm:propertyMapping relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
				<adsm:propertyMapping criteriaProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="nomeEmpresa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="nrIdentificacaoEmpresa" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>	
			<adsm:lookup dataType="integer" popupLabel="pesquisarControleCarga"
					property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga"
					service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupControleCarga"
					action="carregamento/manterControleCargas" size="8" mask="00000000" disabled="true" >
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial" />
				<adsm:propertyMapping criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" />
				<adsm:propertyMapping relatedProperty="periodoInicial" modelProperty=""/>
				<adsm:propertyMapping relatedProperty="periodoFinal" modelProperty=""/>
	        </adsm:lookup>
	     </adsm:lookup>
		
		<%--LOOKUP MANIFESTO DE COLETA--%>	
		<adsm:hidden property="manifestoColeta.filial.pessoa.nmFantasia"/>	
		<adsm:lookup popupLabel="pesquisarFilial" dataType="text" property="manifestoColeta.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial" action="/municipios/manterFiliais" 
					 label="manifestoColeta" labelWidth="22%" width="28%" size="3" maxLength="3" picker="false" serializable="false" onchange="return onFilialManifestoColChange(this);">
				<adsm:propertyMapping relatedProperty="manifestoColeta.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>	 
				<adsm:propertyMapping criteriaProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" inlineQuery="true"/>
				<adsm:propertyMapping criteriaProperty="nomeEmpresa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="nrIdentificacaoEmpresa" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>		 
			<adsm:lookup dataType="integer" property="manifestoColeta" idProperty="idManifestoColeta" criteriaProperty="nrManifesto"
						 action="/coleta/consultarManifestosColeta" cmd="pesq" service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupManifestoColeta" 
						 exactMatch="false" size="15" maxLength="8" mask="00000000" disabled="true" popupLabel="pesquisarManifestoColeta"> 
						 <adsm:propertyMapping criteriaProperty="manifestoColeta.filial.idFilial" modelProperty="filial.idFilial"/>
 						 <adsm:propertyMapping criteriaProperty="manifestoColeta.filial.sgFilial" modelProperty="filial.sgFilial"/>
 						 <adsm:propertyMapping criteriaProperty="manifestoColeta.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
 						 <adsm:propertyMapping relatedProperty="periodoInicial" modelProperty=""/>
						 <adsm:propertyMapping relatedProperty="periodoFinal" modelProperty=""/>
			</adsm:lookup>
		</adsm:lookup>
		
		
		
		<%--LOOKUP MANIFESTO DE VIAGEM--%>
		<adsm:combobox label="manifestoViagem" labelWidth="22%" width="78%" serializable="true" 
		               property="manifesto.tpManifesto" 
					   service="lms.sim.consultarLocalizacoesMercadoriasAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="resetValue('periodoInicial');resetValue('periodoFinal');changeDocumentWidgetType({
						   documentTypeElement:this, 
						   filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
						   documentGroup:'MANIFESTO',
						   actionService:'lms.sim.consultarLocalizacoesMercadoriasAction'
						   }); " >
						  

			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem" popupLabel="pesquisarFilial"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false" 
						 onDataLoadCallBack="enableManifestoManifestoViagemNacional"
						 onchange="return changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	}); "/>
						 

			<adsm:lookup dataType="integer" onDataLoadCallBack="retornoManifesto"
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" 
						 onPopupSetValue="manifestoNrManifestoOrigem_retornoPopup"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="true" popupLabel="pesquisarManifestoViagem"/>
		</adsm:combobox>
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.tpStatusManifesto" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" />
		
		
		
			
		<%--LOOKUP MANIFESTO DE ENTREGA--%>		
		<adsm:hidden property="manifesto.filial.pessoa.nmFantasia"/>
		<adsm:lookup label="manifestoEntrega" popupLabel="pesquisarFilial"
					 serializable="false"
					 action="/municipios/manterFiliais" 
					 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial" 
					 dataType="text" 
					 property="manifesto.filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 picker="false"
					 size="3" 
					 maxLength="3" 
					 labelWidth="22%"
					 width="78%"
					 exactMatch="true" onchange="return onFilialManifestoEntChange(this);">
					 <adsm:propertyMapping relatedProperty="manifesto.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
					 <adsm:propertyMapping criteriaProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" inlineQuery="true"/>
					 <adsm:propertyMapping criteriaProperty="nomeEmpresa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
					 <adsm:propertyMapping criteriaProperty="nrIdentificacaoEmpresa" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>	
			<adsm:lookup serializable="true"
   					 	 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupManifestoEntrega" 
   					 	 dataType="integer" 
   					 	 property="manifestoEntrega" 
   	 					 idProperty="idManifestoEntrega"
   						 criteriaProperty="nrManifestoEntrega" 
   						 size="20"
   						 maxLength="16"
   					 	 action="/entrega/consultarManifestosEntrega" cmd="lookup" mask="00000000" disabled="true" popupLabel="pesquisarManifestoEntrega">
   				<adsm:propertyMapping criteriaProperty="controleCarga.idControleCarga" modelProperty="manifesto.controleCarga.idControleCarga"/>	  	 
   				<adsm:propertyMapping criteriaProperty="manifesto.filial.idFilial" modelProperty="filial.idFilial"/> 	  	 
   				<adsm:propertyMapping criteriaProperty="manifesto.filial.sgFilial" modelProperty="filial.sgFilial"/> 	  	 
   				<adsm:propertyMapping criteriaProperty="manifesto.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/> 	  	 
   				<adsm:propertyMapping relatedProperty="periodoInicial" modelProperty=""/>
				<adsm:propertyMapping relatedProperty="periodoFinal" modelProperty=""/>	 
   		   </adsm:lookup>
    
        </adsm:lookup> 
        
        <%--LOOKUP MIR--%>	
        <adsm:hidden property="mir.filialByIdFilialOrigem.pessoa.nmFantasia"/>	
        <adsm:hidden property="tpDocumentoMir"/>	
        <adsm:lookup label="MIR" popupLabel="pesquisarFilial"
					 serializable="false"
					 action="/municipios/manterFiliais"
					 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupFilial" 
					 dataType="text" 
					 property="mir.filialByIdFilialOrigem" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 picker="false"
					 size="3" 
					 maxLength="3" 
					 labelWidth="22%"
					 width="28%"
					 exactMatch="true" onchange="return onFilialMirChange(this);">
					 <adsm:propertyMapping relatedProperty="mir.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
					 <adsm:propertyMapping criteriaProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa" inlineQuery="true"/>
					 <adsm:propertyMapping criteriaProperty="nomeEmpresa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
					 <adsm:propertyMapping criteriaProperty="nrIdentificacaoEmpresa" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>	
			<adsm:lookup serializable="true"
   					 	 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupMir" 
   					 	 dataType="integer" 
   					 	 property="mir" 
   	 					 idProperty="idMir"
   						 criteriaProperty="nrMir" 
   						 size="20"
   						 maxLength="16"
   					 	 action="/entrega/manterMemorandosInternosResposta" mask="00000000" disabled="true">
   				<adsm:propertyMapping criteriaProperty="mir.filialByIdFilialOrigem.idFilial" modelProperty="filialByIdFilialOrigem.idFilial"/> 	  	 
   				<adsm:propertyMapping criteriaProperty="mir.filialByIdFilialOrigem.sgFilial" modelProperty="filialByIdFilialOrigem.sgFilial"/> 	  	 
   				<adsm:propertyMapping criteriaProperty="mir.filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/> 	  	 
   				<adsm:propertyMapping relatedProperty="tpDocumentoMir" modelProperty="tpDocumentoMir.value"/>
   				<adsm:propertyMapping relatedProperty="periodoInicial" modelProperty=""/>
				<adsm:propertyMapping relatedProperty="periodoFinal" modelProperty=""/>	 
   		  	</adsm:lookup>
    
        </adsm:lookup>
		
		<%--LOOKUP AWB--%>		
		<adsm:lookup idProperty="idAwb" property="awb"
				 criteriaProperty="nrAwb"
				 action="/expedicao/consultarAWBs"
				 service="lms.sim.consultarLocalizacoesMercadoriasAction.findLookupAwb"
				 label="AWB" dataType="integer" maxLength="10" width="28%" labelWidth="22%" mask="0000000">
				 <adsm:propertyMapping relatedProperty="periodoInicial" modelProperty=""/>
				 <adsm:propertyMapping relatedProperty="periodoFinal" modelProperty=""/>	 
		</adsm:lookup>		 
		
		
		<adsm:buttonBar>
			<adsm:button caption="consultar" id="botaoConsultar" disabled="false" onclick="consultar();" buttonType="findButton" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

function callBackForm_cb(data, exception){
	onDataLoad_cb(data, exception);
	if(getNestedBeanPropertyValue(data,"pedidoColeta.idPedidoColeta")!= undefined)	
      	setDisabled("pedidoColeta.idPedidoColeta", false);
}

/************************************************************\
	* numero cotacao
	\************************************************************/
	function changeFilialCotacao(obj){
		var flag = filial_sgFilialOnChangeHandler();
		if(getElementValue(obj) == '') {
			setElementValue('nrCotacao', '');
			setDisabled('nrCotacao', true);
		}else{
			setDisabled('nrCotacao', false);
		}
		
		return flag;
	}
	
	function filial_cb(data, error){
		if(data && data != undefined && data.length > 0){
			setDisabled('nrCotacao', false);
		} else {
			setDisabled('nrCotacao', true);
		}
		filial_sgFilial_exactMatch_cb(data);
	}
	

function doctoServicoCb_cb(data, exception){	
   var r = doctoServico_nrDoctoServico_exactMatch_cb(data);
   if (r == true) {
   	 if(getNestedBeanPropertyValue(data[0],"pedidoColeta.idPedidoColeta")!= undefined)	
      	setDisabled("pedidoColeta.idPedidoColeta", false);
   }
}

function initWindow(eventObj){
		setDisabled("botaoConsultar",false);
		if(eventObj.name=='tab_click'){
		  var tabGroup = getTabGroup(this.document);	
		  tabGroup.setDisabledTab("list", true);
		  tabGroup.setDisabledTab("cad", true);
		}
		if(eventObj.name=='cleanButton_click'){
			setDisabled("manifesto.filialByIdFilialOrigem.idFilial",true);
			setDisabled("mir.idMir",true);
			setDisabled("manifestoEntrega.idManifestoEntrega",true);
			setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional",true);
			setDisabled("manifestoColeta.idManifestoColeta",true);
			setDisabled("controleCarga.idControleCarga",true);
			setDisabled("doctoServico.idDoctoServico",true);
			setDisabled("doctoServico.filialByIdFilialOrigem.sgFilial",true);
			setDisabled("pedidoColeta.idPedidoColeta",true);
			setDisabled("nrCotacao",true);
			setDisabled("informacaoDoctoCliente.idInformacaoDoctoCliente",true);
			document.getElementById("doctoServico.idDoctoServico").propertyMappings=null;
			atualizaMaxInterval();
			findEmpresaUsuario();
		}
}
//CONSULTA


	
function consultar() {
	var ret = validateTab();
	if(ret == true){
	    var tabGroup = getTabGroup(this.document);
	    tabGroup.setDisabledTab("list", false);
	    tabGroup.selectTab("list",{name:'tab_click'});
    }
    return false;
}

function validateTab() {
	
	if (validateTabScript(document.forms)) {
		if(getElementValue("idAgendamentoEntrega") == ""){
		
			if(  getElementValue("doctoServico.idDoctoServico")!= "" ||
				 getElementValue("idDoctoServico")!= "" ||
				 getElementValue("pedidoColeta.idPedidoColeta")!= "" ||
	 		 	 getElementValue("controleCarga.idControleCarga")!= "" ||
	 		 	 getElementValue("manifestoColeta.idManifestoColeta")!= "" ||
	 		 	 getElementValue("manifestoEntrega.idManifestoEntrega")!= "" ||
	 		 	 getElementValue("mir.idMir")!= "" ||
	 		 	 getElementValue("nrCotacao")!= "" ||
	 		 	 getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional")!= "" ||
	 		 	 getElementValue("awb.idAwb")!= "" ){
	 		 	return true;
			}else if(
				((getElementValue("remetente.idCliente")!= "" && getElementValue("destinatario.idCliente")!= "") || 
				(getElementValue("destinatario.idCliente")!= "" && getElementValue("consignatario.idCliente")!= "") || 
				(getElementValue("destinatario.idCliente")!= "" && getElementValue("responsavelFrete.idCliente")!= "") || 
				(getElementValue("consignatario.idCliente")!= "" && getElementValue("remetente.idCliente")!= "") ||
		    	(getElementValue("consignatario.idCliente")!= "" && getElementValue("responsavelFrete.idCliente")!= "") ||
		    	(getElementValue("remetente.idCliente")!= "" && getElementValue("responsavelFrete.idCliente")!= "")) ){
					if(document.getElementById("periodoInicial").value != "" && document.getElementById("periodoFinal").value != ""){
						document.getElementById("periodoInicial").maxInterval = 15;
						document.getElementById("periodoFinal").maxInterval = 15;
						if(!validate(document.getElementById("periodoInicial"))){
							return false;
						}
					}
					return true;
			}else if(getElementValue("filialOrigem.idFilial")!= "" && getElementValue("filialDestino.idFilial")!= "" ){
				if(document.getElementById("periodoInicial").value != "" && document.getElementById("periodoFinal").value != ""){
					document.getElementById("periodoInicial").maxInterval = 1;
					document.getElementById("periodoFinal").maxInterval = 1;
					if(!validate(document.getElementById("periodoInicial"))){
						return false;
					}
				}
				return true;	
			}else if((getElementValue("remetente.idCliente")!= "" || getElementValue("destinatario.idCliente")!= "" || getElementValue("consignatario.idCliente")!= "" || getElementValue("responsavelFrete.idCliente")!= "" || getElementValue("redespacho.idCliente")!= "") && (getElementValue("periodoInicial")!= "" && getElementValue("periodoFinal")!= "") && (getElementValue("nfCliente")== "") ){		
				document.getElementById("periodoInicial").maxInterval = 7;
				document.getElementById("periodoFinal").maxInterval = 7;
				if(!validate(document.getElementById("periodoInicial"))){
					return false;

				}
				return true;
			}else if(getElementValue("nfCliente")!= "" && (getElementValue("remetente.idCliente")!= "" || getElementValue("destinatario.idCliente")!= "" || getElementValue("consignatario.idCliente")!= "" || getElementValue("responsavelFrete.idCliente")!= "" || getElementValue("redespacho.idCliente")!= "") ){	
				document.getElementById("periodoInicial").maxInterval = 15;
				document.getElementById("periodoFinal").maxInterval = 15;
				if(!validate(document.getElementById("periodoInicial"))){
					return false;

				}
				return true;
			
			}else {
					alert(i18NLabel.getLabel("LMS-10047"));
					return false;
	               }
	               
       }else{
       		return true;
       }
      }
      
	return false;
}

function atualizaMaxInterval(){
	document.getElementById("periodoInicial").maxInterval = null;
	document.getElementById("periodoFinal").maxInterval = null;
}
//onchange filial Origem, filial Destino, remetente, destinatario, consignatario, responsavel frete
function onFilialOrigemChange() {
		atualizaMaxInterval();
		return filialOrigem_sgFilialOnChangeHandler();
} 
function onFilialDestinoChange() {
		atualizaMaxInterval();
		return filialDestino_sgFilialOnChangeHandler();
}

function limpaComboInformacaoDoctoCliente(){
	setElementValue("informacaoDoctoCliente.idInformacaoDoctoCliente","");
	setElementValue("nrDocumentoCliente","");
	setDisabled("informacaoDoctoCliente.idInformacaoDoctoCliente",true);
}

function onRemetenteChange() {
		atualizaMaxInterval();
		limpaComboInformacaoDoctoCliente();
		return remetente_pessoa_nrIdentificacaoOnChangeHandler();
}
function onRemetenteFantasiaChange() {
		limpaComboInformacaoDoctoCliente();		
		return remetenteFantasia_nmFantasiaOnChangeHandler();
}
function onRemetenteContaChange() {
		limpaComboInformacaoDoctoCliente();
		return remetenteConta_nrContaOnChangeHandler();
}
function onDestinatarioChange() {
		atualizaMaxInterval();
		return destinatario_pessoa_nrIdentificacaoOnChangeHandler();
}	
function onConsignatarioChange() {
		atualizaMaxInterval();
		return consignatario_pessoa_nrIdentificacaoOnChangeHandler();
}
function onRedespachoChange() {
		atualizaMaxInterval();
		return redespacho_pessoa_nrIdentificacaoOnChangeHandler();
}
function onResponsavelFreteChange() {
		atualizaMaxInterval();
		return responsavelFrete_pessoa_nrIdentificacaoOnChangeHandler();
}	

//DOCTO SERVICO
	function changeComboDocumentoServicoType(field) {
		resetValue('doctoServico.idDoctoServico');
		setDisabled("pedidoColeta.idPedidoColeta", true);
		setElementValue("tpDocumentoServico", getElementValue("doctoServico.tpDocumentoServico"));
		var flag = changeDocumentWidgetType({documentTypeElement:field,
                             filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'),
                             documentNumberElement:document.getElementById('doctoServico.idDoctoServico'),
                             documentGroup:'DOCTOSERVICE',actionService:'lms.sim.consultarLocalizacoesMercadoriasAction'});
       
		return flag;
	}
	
	function changeDocumentoServico() {
		setElementValue("tpDocumentoServico", getElementValue("doctoServico.tpDocumentoServico"));
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
        if(getElementValue("doctoServico.tpDocumentoServico")!='RRE'){
			pms[pms.length] = { modelProperty:"servico.tpModal", criteriaProperty:"modal", inlineQuery:true};
			pms[pms.length] = { modelProperty:"servico.tpAbrangencia", criteriaProperty:"abrangencia", inlineQuery:true };
			pms[pms.length] = { modelProperty:"servico.tipoServico.idTipoServico", criteriaProperty:"tipoServico.idTipoServico", inlineQuery:true};
			pms[pms.length] = { modelProperty:"filialByIdFilialOrigem.idFilial",criteriaProperty:"filialOrigem.idFilial", inlineQuery:true};
			pms[pms.length] = { modelProperty:"filialByIdFilialDestino.idFilial",criteriaProperty:"filialDestino.idFilial", inlineQuery:true};
			pms[pms.length] = { modelProperty:"filialByIdFilialDestino.sgFilial",criteriaProperty:"filialDestino.sgFilial", inlineQuery:false};
			pms[pms.length] = { modelProperty:"filialByIdFilialDestino.pessoa.nmFantasia",criteriaProperty:"filialDestino.pessoa.nmFantasia", inlineQuery:false};
			pms[pms.length] = { modelProperty:"finalidade", criteriaProperty:"finalidade", inlineQuery:true };
		}
		
		if(getElementValue("doctoServico.tpDocumentoServico") =='CTR')	{
			pms[pms.length] = { modelProperty:"nfCliente", relatedProperty:"nfCliente" };
			pms[pms.length] = { modelProperty:"finalidade", relatedProperty:"finalidade" };
		}
		
		pms[pms.length] = { modelProperty:"tpDocumentoServico", criteriaProperty:"tpDocumentoServico", inlineQuery:true };
		
		pms[pms.length] = { modelProperty:"idFilialDoctoSer", criteriaProperty:"doctoServico.filialByIdFilialOrigem.idFilial", inlineQuery:true };
		
		pms[pms.length] = { modelProperty:"doctoServico.idDoctoServico", relatedProperty:"idDoctoServico" };
		pms[pms.length] = { modelProperty:"dhEmissao", relatedProperty:"periodoInicial"};
		pms[pms.length] = { modelProperty:"dhEmissao", relatedProperty:"periodoFinal" };
		
				
		pms[pms.length] = { modelProperty:"modal", relatedProperty:"modal" };
		pms[pms.length] = { modelProperty:"abrangencia", relatedProperty:"abrangencia" };
		pms[pms.length] = { modelProperty:"tipoServico.idTipoServico", relatedProperty:"tipoServico.idTipoServico" };
		
		pms[pms.length] = { modelProperty:"filialOrigem.idFilial", relatedProperty:"filialOrigem.idFilial" };
		pms[pms.length] = { modelProperty:"filialOrigem.sgFilial", relatedProperty:"filialOrigem.sgFilial" };
		pms[pms.length] = { modelProperty:"filialOrigem.pessoa.nmFantasia", relatedProperty:"filialOrigem.pessoa.nmFantasia" };
		
		pms[pms.length] = { modelProperty:"filialDestino.idFilial", relatedProperty:"filialDestino.idFilial" };
		pms[pms.length] = { modelProperty:"filialDestino.sgFilial", relatedProperty:"filialDestino.sgFilial" };
		pms[pms.length] = { modelProperty:"filialDestino.pessoa.nmFantasia", relatedProperty:"filialDestino.pessoa.nmFantasia" };
		
		pms[pms.length] = { modelProperty:"pedidoColeta.filialByIdFilialResponsavel.sgFilial", relatedProperty:"pedidoColeta.filialByIdFilialResponsavel.sgFilial" };
		pms[pms.length] = { modelProperty:"pedidoColeta.filialByIdFilialResponsavel.idFilial", relatedProperty:"pedidoColeta.filialByIdFilialResponsavel.idFilial" };
		pms[pms.length] = { modelProperty:"pedidoColeta.nrColeta", relatedProperty:"pedidoColeta.nrColeta" };
		pms[pms.length] = { modelProperty:"pedidoColeta.idPedidoColeta", relatedProperty:"pedidoColeta.idPedidoColeta" };
		
				
		pms[pms.length] = { modelProperty:"remetente.idCliente", relatedProperty:"remetente.idCliente" };
		pms[pms.length] = { modelProperty:"remetente.pessoa.nmPessoa", relatedProperty:"remetente.pessoa.nmPessoa" };
		pms[pms.length] = { modelProperty:"remetente.pessoa.nrIdentificacao", relatedProperty:"remetente.pessoa.nrIdentificacao" };
		pms[pms.length] = { modelProperty:"remetenteFantasia.pessoa.nmFantasia", relatedProperty:"remetenteFantasia.nmFantasia" };
		pms[pms.length] = { modelProperty:"remetenteFantasia.idCliente", relatedProperty:"remetenteFantasia.idCliente" };
		pms[pms.length] = { modelProperty:"remetenteConta.nrConta", relatedProperty:"remetenteConta.nrConta" };
		pms[pms.length] = { modelProperty:"remetenteConta.idCliente", relatedProperty:"remetenteConta.idCliente" };
		
		pms[pms.length] = { modelProperty:"destinatario.idCliente", relatedProperty:"destinatario.idCliente" };
		pms[pms.length] = { modelProperty:"destinatario.pessoa.nmPessoa", relatedProperty:"destinatario.pessoa.nmPessoa" };
		pms[pms.length] = { modelProperty:"destinatario.pessoa.nrIdentificacao", relatedProperty:"destinatario.pessoa.nrIdentificacao" };
		pms[pms.length] = { modelProperty:"destinatarioConta.idCliente", relatedProperty:"destinatarioConta.idCliente" };
		pms[pms.length] = { modelProperty:"destinatarioConta.nrConta", relatedProperty:"destinatarioConta.nrConta" };
		pms[pms.length] = { modelProperty:"destinatariofantasia.pessoa.nmFantasia", relatedProperty:"destinatariofantasia.nmFantasia" };
		pms[pms.length] = { modelProperty:"destinatariofantasia.idCliente", relatedProperty:"destinatariofantasia.idCliente" };
		pms[pms.length] = { modelProperty:"idDoctoServicoReembolsado", relatedProperty:"idDoctoServicoReembolsado"};
	
		return doctoServico_nrDoctoServicoOnChangeHandler();
	}
	
	//filial do documento de servico	
	function changeComboDocumentoServicoFilial() {
		resetValue('doctoServico.idDoctoServico');
		                        
        var pmsF = document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").propertyMappings; 
   		pmsF[pmsF.length] = { modelProperty:"empresa.idEmpresa", criteriaProperty:"empresa.idEmpresa", inlineQuery:true}; 
   		pmsF[pmsF.length] = { modelProperty:"empresa.pessoa.nrIdentificacao", criteriaProperty:"nrIdentificacaoEmpresa", inlineQuery:false}; 
   		pmsF[pmsF.length] = { modelProperty:"empresa.pessoa.nmPessoa", criteriaProperty:"nomeEmpresa", inlineQuery:false}; 
   		
   		
   		if(getElementValue("doctoServico.filialByIdFilialOrigem.sgFilial")== ""){
   			setDisabled("doctoServico.nrDoctoServico", true);
   		}
        return doctoServico_filialByIdFilialOrigem_sgFilialOnChangeHandler();                  
	}
	
	function filialDoctoServico_cb(data){
		var r = doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("doctoServico.idDoctoServico", false);
	      setFocus(document.getElementById("doctoServico.nrDoctoServico"));
	   }
	}
	
	
 //manifesto de viagem
 function enableManifestoManifestoViagemNacional_cb(data) {
   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
   }
 }

function manifestoNrManifestoOrigem_retornoPopup(data) {
	if (data != undefined) {
		var nr = getNestedBeanPropertyValue(data,"nrManifesto");
		var idManifesto = getNestedBeanPropertyValue(data,"idManifestoViagemNacional");
		
		setElementValue('manifesto.idManifesto', idManifesto);
		setElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem", nr);
		
		if (getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
			resetValue('manifesto.idManifesto');
		}
	}
	
}

function retornoManifesto_cb(data) {
	var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
	if (r == true) {
		var idManifesto = getElementValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		setElementValue('manifesto.idManifesto', idManifesto);

	}
}
 
	
function retornoPopPupDoctoServico(data){
	if(data != undefined){
	   	var tpDocumento = getElementValue("doctoServico.tpDocumentoServico");
	   	if(tpDocumento != 'RRE'){
	   		var idDoctoServico = getNestedBeanPropertyValue(data,"idDoctoServico");
			
			var criteria = new Array();
			setNestedBeanPropertyValue(criteria,"idDoctoServico",idDoctoServico);
			
			
			_serviceDataObjects = new Array();
	   		addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findRetornoPopPupDoctoServico", "callBackForm", criteria));
	  		xmit();
	  		 
  		}else if(tpDocumento == 'RRE'){
  			setElementValue("filialByIdFilialOrigem.sgFilial",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.sgFilial"));
  			setElementValue("filialByIdFilialOrigem.idFilial",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.idFilial"));
  			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia",getNestedBeanPropertyValue(data,"filialByIdFilialOrigem.pessoa.nmFantasia"));
  			setElementValue("idDoctoServico",getNestedBeanPropertyValue(data,"idDoctoServico"));
  			
  		}
  		
	}
}	
	

//documento do cliente
function onChangeDocumentoCliente(field){
	comboboxChange({e:field});
	var doctoCliente =  getElementValue("informacaoDoctoCliente.idInformacaoDoctoCliente");
	if(doctoCliente == ''){
		setDisabled("nrDocumentoCliente",true);
	}else{
		setElementValue("nrDocumentoCliente","");
		setDisabled("nrDocumentoCliente",false);
		
		if(getElementValue("infDocCliTpCampo") == 'A')
			document.getElementById("nrDocumentoCliente").mask = "";
		else{
			if(getElementValue("infDocCliFormat")!= "")
				document.getElementById("nrDocumentoCliente").mask = getElementValue("infDocCliFormat");	
			else
			   	document.getElementById("nrDocumentoCliente").mask = undefined;
		}	
		if(getElementValue("infDocCliTpCampo") == 'A')
			document.getElementById("nrDocumentoCliente").dataType = "text";
			
		else if(getElementValue("infDocCliTpCampo") == 'D')
			document.getElementById("nrDocumentoCliente").dataType = "JTDate";	
			
		else if(getElementValue("infDocCliTpCampo") == 'N'){
			var formato = getElementValue("infDocCliFormat");
			if(formato.indexOf(separator) > -1 || formato.indexOf(decimal) > -1)
				document.getElementById("nrDocumentoCliente").dataType = "decimal";
			else		
				document.getElementById("nrDocumentoCliente").dataType = "integer";	
		}	
	}
}
function setaComboDoctoCliente_cb(data){
	informacaoDoctoCliente_idInformacaoDoctoCliente_cb(data);
	if (data.length > 0 && getElementValue("remetente.idCliente")!= ""){
		setDisabled("informacaoDoctoCliente.idInformacaoDoctoCliente",false);
	}else{
		setElementValue("informacaoDoctoCliente.idInformacaoDoctoCliente","");
		setDisabled("informacaoDoctoCliente.idInformacaoDoctoCliente",true);
	}
	
}

//Empresa do usuário logado
function pageLoad_cb(data,exception){
		onPageLoad_cb(data,exception);
		setMasterLink(document, true);
		if(getElementValue("idDoctoServicoConsulta")!= ""){
			getTabGroup(this.document).setDisabledTab("cad",false);
			getTabGroup(this.document).selectTab("cad");
			getTabGroup(this.document).setDisabledTab("doc",true);
		}
		if(getElementValue("idAgendamentoEntrega")!= ""){
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("doc", true);
		    tabGroup.setDisabledTab("list", false);
		    tabGroup.selectTab("list",{name:'tab_click'});
		}else{
			findEmpresaUsuario();
			setDisabled("botaoConsultar",false);
		}
}
	
function findEmpresaUsuario(){
	_serviceDataObjects = new Array();
   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findEmpresaUsuarioLogado", "setaInformacoesEmpresa", new Array()));
  	xmit();
}

function setaInformacoesEmpresa_cb(data, exception){
	setElementValue("empresa.idEmpresa",getNestedBeanPropertyValue(data,"idEmpresa"));
	setElementValue("nomeEmpresa",getNestedBeanPropertyValue(data,"nomeEmpresa"));
	setElementValue("nrIdentificacaoEmpresa",getNestedBeanPropertyValue(data,"nrIdentificacaoEmpresa"));
	setElementValue("periodoInicial", getNestedBeanPropertyValue(data,"dtInicial"));
	setElementValue("periodoFinal", getNestedBeanPropertyValue(data,"dtFinal"));
}




//pedido de coleta
	function sgFilialOnChangeHandler() {
		if (getElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial")=='') {
			setElementValue("pedidoColeta.nrColeta","");		
			setDisabled('pedidoColeta.nrColeta', true);
		} else {
			setDisabled('pedidoColeta.nrColeta', false);
		}
		return lookupChange({e:document.forms[0].elements["pedidoColeta.filialByIdFilialResponsavel.idFilial"]});
	}
	
	function loadFilial_cb(data, error) {
		setElementValue("pedidoColeta.nrColeta","");		
		setDisabled('pedidoColeta.nrColeta', false);	
		return pedidoColeta_filialByIdFilialResponsavel_sgFilial_exactMatch_cb(data);
	}
		
	function enableColeta(data){
		var idPedidoColeta = getNestedBeanPropertyValue(data, "idPedidoColeta");
		var idPedidoColetaForm = getElementValue("pedidoColeta.idPedidoColeta");
		var nrColeta = getElementValue("pedidoColeta.nrColeta");
		
		var filial = getNestedBeanPropertyValue(data, "sgFilial");
		var idFilial = getNestedBeanPropertyValue(data, "idFilial");

	    setElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial",filial);
	    setElementValue("pedidoColeta.nrColeta",nrColeta);
	    setElementValue("pedidoColeta.idPedidoColeta",idPedidoColeta);
	    setElementValue("pedidoColeta.filialByIdFilialResponsavel.idFilial",idFilial);
	    
		setDisabled('pedidoColeta.nrColeta', false);		
					
	}
	function retornoPopPup_pedidoColeta(data){
		if(data != undefined)
		  setElementValue("pedidoColeta.filialByIdFilialResponsavel.sgFilial", getNestedBeanPropertyValue(data,"sgFilial"));
	}
	
	function onFilialPedColChange(elem){
		setDisabled("pedidoColeta.idPedidoColeta",elem.value == "");
		return pedidoColeta_filialByIdFilialResponsavel_sgFilialOnChangeHandler();
	}
	
	//controle carga
	function onFilialControleCargaChange(elem) {
		setDisabled("controleCarga.idControleCarga",elem.value == "");
		return controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	//manifesto coleta
	function onFilialManifestoColChange(elem) {
		setDisabled("manifestoColeta.idManifestoColeta",elem.value == "");
		return manifestoColeta_filial_sgFilialOnChangeHandler();
	}
	
	//manifesto entrega
	function onFilialManifestoEntChange(elem) {
		setDisabled("manifestoEntrega.idManifestoEntrega",elem.value == "");
		return manifesto_filial_sgFilialOnChangeHandler();
	}
	
	//mir
	function onFilialMirChange(elem) {
		setDisabled("mir.idMir",elem.value == "");
		return mir_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	}
	
	

	
	
	
	
</script>