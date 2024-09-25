<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction"  onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form action="/indenizacoes/emitirRelatorioIndenizacoesExcel" height="390">
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="tpFormatoRelatorio" value="xls" serializable="true"/>		
		
		
		<adsm:section caption="filtrosRims"/>
		
		<adsm:hidden property="siglaDescricaoRegionalHidden"/>
		<adsm:combobox property="regional.idRegionalFilial" 
					   optionProperty="idRegional" 
					   optionLabelProperty="siglaDescricao"
					   label="regional" 
					   width="33%" 
					   labelWidth="17%"
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findComboRegional"
					   onchange="onChangeRegional()"/>

		<adsm:range label="dataEmissao" width="33%" labelWidth="17%" required="true">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" picker="true"/>
		</adsm:range>

		<adsm:hidden property="siglaFilialHidden"/>
		<adsm:lookup label="filial" labelWidth="17%" width="33%" 
             		 property="filial"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialHidden" modelProperty="sgFilial" />
          	<adsm:textbox dataType="text" 
          			property="filial.pessoa.nmFantasia" 
          			size="30" 
          			maxLength="30" 
          			disabled="true"/>
        </adsm:lookup>

		<adsm:range label="dataLiberacaoPgto" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dataLiberacaoPgtoInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataLiberacaoPgtoFinal" picker="true"/>
		</adsm:range>

		<adsm:range label="numero" width="33%" labelWidth="17%">
			<adsm:textbox dataType="integer" property="numeroInicial" size="16" />
			<adsm:textbox dataType="integer" property="numeroFinal" size="16"  />
		</adsm:range>

		<adsm:range label="dataProgramadaPagamento" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dataProgramadaPagamentoInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataProgramadaPagamentoFinal" picker="true"/>
		</adsm:range>

		<adsm:hidden property="tpIndenizacaoHidden"/>
		<adsm:combobox property="tpIndenizacao" 
					   label="tipoIndenizacao" 
					   domain="DM_TIPO_INDENIZACAO" 
					   width="33%" renderOptions="true"
					   labelWidth="17%"
					   onchange="onChangeIndenizacao()"/>		

		<adsm:range label="dataPagamento" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dataPagamentoInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataPagamentoFinal" picker="true"/>
		</adsm:range>

		<adsm:hidden property="tpStatusIndenizacaoHidden"/>
		<adsm:combobox label="status" 
					   property="tpStatusIndenizacao"  
					   domain="DM_STATUS_INDENIZACAO" 
					   width="33%" renderOptions="true"
					   labelWidth="17%"
					   onchange="onChangeStatus()"/>		

		<adsm:range label="dataAnaliseNc" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dataAnaliseNcInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataAnaliseNcFinal" picker="true"/>
		</adsm:range>

		<adsm:hidden property="situacaoAprovacaoHidden"/>
		<adsm:combobox label="situacaoAprovacao" 
					   property="situacaoAprovacao"  
					   domain="DM_STATUS_WORKFLOW" 
					   width="33%" 
					   renderOptions="true"
					   labelWidth="17%"
					   onchange="onChangeSituacaoAprovacao()"/>	

        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="usuarioAnaliseNc"
			         labelWidth="17%"
					 property="usuario" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text"					  
					 size="10" 
					 maxLength="16"	
					 width="33%"		 					 
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" disabled="true" maxLength="60" size="23"/>
		</adsm:lookup>	

   		<adsm:hidden property="naturezaProduto.dsNaturezaProdutoHidden"/>
		<adsm:combobox label="naturezaProduto" 
					property="naturezaProduto.idNaturezaProduto"
				    optionProperty="idNaturezaProduto" 
				    optionLabelProperty="dsNaturezaProduto" 
				    service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findNaturezaProduto" 
				    width="33%"
				    labelWidth="17%" 
				    onchange="onChangeNaturezaProduto()"/>

		<adsm:range label="dataLote" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dataLoteInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataLoteFinal" picker="true"/>
		</adsm:range>

		<adsm:hidden property="formaPagamentoHidden"/>
		<adsm:combobox label="formaPagamento" 
					   property="formaPagamento"  
					   domain="DM_FORMA_PAGAMENTO_INDENIZACAO" 
					   width="33%" 
					   renderOptions="true"
					   labelWidth="17%"
					   onchange="onChangeFormaPagamento()"/>	
 		
 		<adsm:textbox dataType="integer" 
 					property="nrLoteJdeRim" 
 					width="33%" 
 					maxLength="10" 
 					size="12"
 					labelWidth="17%" 
 					label="numeroLote" />
 		
		<adsm:lookup label="numeroProcesso" 
					property="processoSinistro" 
					idProperty="idProcessoSinistro" 
					criteriaProperty="nrProcessoSinistro"
					picker="true" 	
					popupLabel="pesquisarProcessoSinistro"				
					dataType="text"
					action="/seguros/manterProcessosSinistro" 
					cmd="list"
					labelWidth="17%"
					width="33%"  
					service="lms.seguros.emitirCartaOcorrenciaAction.findLookupProcessoSinistro">
					
					<adsm:propertyMapping relatedProperty="nrProcessoSinistro" modelProperty="nrProcessoSinistro"/>
					<adsm:hidden property="nrProcessoSinistro"/>
		</adsm:lookup>			
		
		<adsm:hidden property="siglaFilialRncHidden"/>
		<adsm:lookup label="filialRnc" labelWidth="17%" width="33%" 
             		 property="filialRnc"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filialRnc.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialRncHidden" modelProperty="sgFilial" />
          	<adsm:textbox dataType="text" 
          			property="filialRnc.pessoa.nmFantasia" 
          			size="30" 
          			maxLength="30" 
          			disabled="true"/>
        </adsm:lookup>
		
		<adsm:hidden property="sgTipoSeguroHidden"/> 
		<adsm:combobox property="tipoSeguro.idTipoSeguro" 
				   optionLabelProperty="sgTipo" 
				   optionProperty="idTipoSeguro" 
				   service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findComboTipoSeguro"  
				   label="tipoSeguro" 
				   width="33%" 
				   labelWidth="17%"
				   onchange="onChangeTipoSeguro()"/>
		
		
		<adsm:range label="numeroRNC" width="33%" labelWidth="17%">
			<adsm:textbox dataType="integer" property="numeroRncInicial" size="16" />
			<adsm:textbox dataType="integer" property="numeroRncFinal" size="16"  />
		</adsm:range>
		
		<adsm:hidden property="dsTipoSinistroHidden"/>
		<adsm:combobox property="tipoSinistro.idTipo" 
					   optionLabelProperty="dsTipo" 
					   optionProperty="idTipoSinistro" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findComboTipoSinistro" 
					   label="tipoSinistro" 
					   width="33%" 
					   labelWidth="17%"
					   onchange="onChangeTipoSinistro()"/>
		
		<adsm:range label="dataEmissaoRnc" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dataEmissaoRncInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dataEmissaoRncFinal" picker="true"/>
		</adsm:range>
		
		<adsm:hidden property="blSalvadosHidden"/>
		<adsm:combobox property="blSalvados" 
					   label="salvados" 
					   domain="DM_SIM_NAO" 
					   width="33%" renderOptions="true"
					   labelWidth="17%"
					   onchange="onChangeSalvados()"/>	
		
		<adsm:hidden property="dsMotivoAberturaHidden"/>			   
		<adsm:combobox label="motivoAbertura" 
					   labelWidth="17%" 
					   width="33%"
					   property="motivoAbertura.idMotivoAberturaNc" 
					   optionProperty="idMotivoAberturaNc" 
					   optionLabelProperty="dsMotivoAbertura" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findComboMotivoAberturaNc"
					   onchange="onChangeMotivoAbertura()"/>

		<adsm:range label="valorIndenizacao" width="33%" labelWidth="17%">
			<adsm:textbox dataType="currency" property="valorIndenizacaoInicial" size="16" />
			<adsm:textbox dataType="currency" property="valorIndenizacaoFinal" size="16"  />
		</adsm:range>

		<adsm:hidden property="siglaFilialOcorrencia1Hidden"/>
		<adsm:lookup label="filialOcorrencia1" labelWidth="17%" width="33%" 
             		 property="filialOcorrencia1"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filialOcorrencia1.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialOcorrencia1Hidden" modelProperty="sgFilial" />
          	<adsm:textbox dataType="text" 
          			property="filialOcorrencia1.pessoa.nmFantasia" 
          			size="30" 
          			maxLength="30" 
          			disabled="true"/>
        </adsm:lookup>

		<adsm:hidden property="siglaFilialDebitadaHidden"/>
		<adsm:lookup label="filialDebitada" labelWidth="17%" width="33%" 
             		 property="filialDebitada"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filialDebitada.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialDebitadaHidden" modelProperty="sgFilial" />
          	<adsm:textbox dataType="text" 
          			property="filialDebitada.pessoa.nmFantasia" 
          			size="30" 
          			maxLength="30" 
          			disabled="true"/>
        </adsm:lookup>

		<adsm:hidden property="siglaFilialOcorrencia2Hidden"/>
		<adsm:lookup label="filialOcorrencia2" labelWidth="17%" width="33%" 
             		 property="filialOcorrencia2"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
   			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filialOcorrencia2.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:propertyMapping relatedProperty="siglaFilialOcorrencia2Hidden" modelProperty="sgFilial" />
          	<adsm:textbox dataType="text" 
          			property="filialOcorrencia2.pessoa.nmFantasia" 
          			size="30" 
          			maxLength="30" 
          			disabled="true"/>
        </adsm:lookup>

		<adsm:hidden property="beneficiario.nrIdentificacaoHidden"/>
		<adsm:lookup label="beneficiario" 
					 dataType="text" 
					 property="beneficiario" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="17%" 
					 width="83%">
					 
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
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="17%" 
					 width="83%">
					 
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


		<adsm:section caption="filtrosDocumentosServico"/>

		<adsm:hidden property="doctoServico.tpDocumentoHidden"/>
		<adsm:hidden property="doctoServico.sgFilialOrigemHidden"/>
		<adsm:hidden property="doctoServico.nrDoctoServicoHidden"/>
		<adsm:combobox property="doctoServico.tpDocumentoServico" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findTipoDocumentoServico"
					   optionProperty="value" optionLabelProperty="description"
					   label="documentoServico" labelWidth="17%" width="33%"
					   onchange="onChangeTpDocumentoServico();return changeDocumentWidgetType({
					   		documentTypeElement:this, 
					   		filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					   		documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					   		documentGroup:'SERVICE',
					   		actionService:'lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction'
					   	});" onchangeAfterValueChanged="true">

			<adsm:lookup dataType="text"
						 property="doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 action="" 
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false"
 						 onchange="return changeDocumentWidgetFilial({
							 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
							 documentNumberElement:document.getElementById('doctoServico.idDoctoServico')
						  }); "/>

			<adsm:lookup dataType="integer"
						 property="doctoServico" 
						 idProperty="idDoctoServico" 
						 criteriaProperty="nrDoctoServico" 
						 service="" 
						 action="" 
						 mask="00000000"
						 afterPopupSetValue="onDoctoServicoAfterPopupSetValue"
						 onDataLoadCallBack="onDoctoServicoDataCallback"
						 onchange="return doctoServicoNrDoctoServico_OnChange();"
						 size="10" serializable="true" disabled="true" />
		</adsm:combobox>

		<adsm:range label="dataEmissao" width="33%" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoDocInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dtEmissaoDocFinal" picker="true"/>
		</adsm:range>

		<adsm:hidden property="remetente.nrIdentificacaoHidden"/>
		<adsm:lookup label="remetente" 
					 dataType="text" 
					 property="remetente" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="17%" 
					 width="83%">
					 
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="remetente.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="remetente.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="nrIdentificacaoFormatado" relatedProperty="remetente.nrIdentificacaoHidden" />
			
			<adsm:textbox dataType="text" 
						  property="remetente.nmPessoa" 
						  size="60" 
						  maxLength="60" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="destinatario.nrIdentificacaoHidden"/>
		<adsm:lookup label="destinatario" 
					 dataType="text" 
					 property="destinatario" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="17%" 
					 width="83%">
					 
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="destinatario.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="destinatario.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="nrIdentificacaoFormatado" relatedProperty="destinatario.nrIdentificacaoHidden" />
			
			<adsm:textbox dataType="text" 
						  property="destinatario.nmPessoa" 
						  size="60" 
						  maxLength="60" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="devedor.nrIdentificacaoHidden"/>
		<adsm:lookup label="devedor" 
					 dataType="text" 
					 property="devedor" 
					 idProperty="idPessoa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="nrIdentificacaoFormatado" 
					 disabled="false"
					 service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findLookupPessoa" 
					 action="/configuracoes/manterPessoas"					 
					 size="20"
					 maxLength="20" 
					 picker="true"
					 serializable="true" 
					 required="false" 
					 labelWidth="17%" 
					 width="83%">
					 
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="devedor.nmPessoa" />
			<adsm:propertyMapping modelProperty="nmPessoa" criteriaProperty="devedor.nmPessoa" disable="false" />
			<adsm:propertyMapping modelProperty="nrIdentificacaoFormatado" relatedProperty="devedor.nrIdentificacaoHidden" />
			
			<adsm:textbox dataType="text" 
						  property="devedor.nmPessoa" 
						  size="60" 
						  maxLength="60" 
						  disabled="true" 
						  serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="modalHidden"/>
		<adsm:combobox property="modal" 
					   label="modal" 
					   domain="DM_MODAL" 
					   width="16%" renderOptions="true"
					   labelWidth="17%"
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
					   	service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findComboServico"
					   	onchange="onChangeServico()"/>
 
		<adsm:buttonBar>
			<adsm:reportViewerButton  service="lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript" >
function initWindow(eventObj) {
	if (eventObj.name == "cleanButton_click"){
		retornoCarregaPagina_cb(null, null);
	}
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	
	if(error != undefined) {
		alert(error);
	} else {
		var sdo = createServiceDataObject("lms.indenizacoes.emitirRelatorioIndenizacoesExcelAction.findDefaultValues", "findDefaultValues");
		xmit({serviceDataObjects:[sdo]});
	}
}

function findDefaultValues_cb(data, errorMsg, errorKey) {
	if(errorMsg) {
		alert(errorMsg);
		return;
	}
	
	if(data) {
		setElementValue("dtEmissaoInicial", data.dtEmissaoInicial);
		setElementValue("dtEmissaoFinal", data.dtEmissaoFinal);
	}
}

function onChangeRegional() {
    var combo = document.getElementById("regional.idRegionalFilial");
    setElementValue('siglaDescricaoRegionalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeIndenizacao() {
    var combo = document.getElementById("tpIndenizacao");
    setElementValue('tpIndenizacaoHidden', combo.options[combo.selectedIndex].text);
}

function onChangeStatus() {
    var combo = document.getElementById("tpStatusIndenizacao");
    setElementValue('tpStatusIndenizacaoHidden', combo.options[combo.selectedIndex].text);
}

function onChangeSituacaoAprovacao(){
	var combo = document.getElementById("situacaoAprovacao");
    setElementValue('situacaoAprovacaoHidden', combo.options[combo.selectedIndex].text);
}

function onChangeNaturezaProduto(){
	var combo = document.getElementById("naturezaProduto.idNaturezaProduto");
    setElementValue('naturezaProduto.dsNaturezaProdutoHidden', combo.options[combo.selectedIndex].text);
}

function onChangeFormaPagamento(){
	var combo = document.getElementById("formaPagamento");
    setElementValue('formaPagamentoHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTipoSeguro() {
    var combo = document.getElementById("tipoSeguro.idTipoSeguro");
    setElementValue('sgTipoSeguroHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTipoSinistro() {
    var combo = document.getElementById("tipoSinistro.idTipo");
    setElementValue('dsTipoSinistroHidden', combo.options[combo.selectedIndex].text);
}

function onChangeSalvados() {
    var combo = document.getElementById("blSalvados");
    setElementValue('blSalvadosHidden', combo.options[combo.selectedIndex].text);
}

function onChangeMotivoAbertura(){
	var combo = document.getElementById("motivoAbertura.idMotivoAberturaNc");
    setElementValue('dsMotivoAberturaHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTpDocumentoServico(){
	var combo = document.getElementById("doctoServico.tpDocumentoServico");
    setElementValue('doctoServico.tpDocumentoHidden', combo.options[combo.selectedIndex].text);
    resetValue('doctoServico.filialByIdFilialOrigem.sgFilial');
}

function doctoServicoNrDoctoServico_OnChange() {
	if ( getElementValue('doctoServico.nrDoctoServico') == '' ) {
		resetValue("doctoServico.idDoctoServico");
	}
	return doctoServico_nrDoctoServicoOnChangeHandler();
}

function onDoctoServicoAfterPopupSetValue(data) {
	var criteria = new Array();
    setNestedBeanPropertyValue(criteria, "idDoctoServico", getNestedBeanPropertyValue(data, 'idDoctoServico'));
	setNestedBeanPropertyValue(criteria, "tpDocumentoServico", getElementValue('doctoServico.tpDocumentoServico')); 	    
	setNestedBeanPropertyValue(criteria, "filialByIdFilialOrigem.idFilial", getElementValue('doctoServico.filialByIdFilialOrigem.idFilial')); 	    
    document.getElementById("doctoServico.nrDoctoServico").previousValue = undefined;
    lookupChange({e:document.getElementById("doctoServico.idDoctoServico"), forceChange:true});
    return false;
}

function onDoctoServicoDataCallback_cb(data, error) {
	var result = doctoServico_nrDoctoServico_exactMatch_cb(data);
	if (result == true) {
		var idDoctoServico = getNestedBeanPropertyValue(data, ":0.doctoServico.idDoctoServico");
		var nrDoctoServico = getNestedBeanPropertyValue(data, ":0.doctoServico.nrDoctoServico");
		var idFilial = getNestedBeanPropertyValue(data, ":0.doctoServico.filialByIdFilialOrigem.idFilial");
		var sgFilial = getNestedBeanPropertyValue(data, ":0.doctoServico.filialByIdFilialOrigem.sgFilial");

		setElementValue('doctoServico.idDoctoServico', idDoctoServico);
		setElementValue('doctoServico.nrDoctoServico', nrDoctoServico);
		setElementValue('doctoServico.filialByIdFilialOrigem.idFilial', idFilial);
		setElementValue('doctoServico.filialByIdFilialOrigem.sgFilial',	sgFilial);
		
		setElementValue('doctoServico.sgFilialOrigemHidden', sgFilial);
		setElementValue('doctoServico.nrDoctoServicoHidden', nrDoctoServico);
		
		format(document.getElementById('doctoServico.nrDoctoServico'));

		setDisabled('doctoServico.nrDoctoServico', false);
	} else {
		if (document.getElementById('doctoServico.nrDoctoServico').disabled == false) {
			setFocus("doctoServico.nrDoctoServico");
		} else {
			resetValue('doctoServico.idDoctoServico');
			setFocus("doctoServico.filialByIdFilialOrigem.sgFilial");
		}
	}
	
	return result;
}

function onChangeModal(){
	var combo = document.getElementById("modal");
    setElementValue('modalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeAbrangencia(){
	var combo = document.getElementById("abrangencia");
    setElementValue('abrangenciaHidden', combo.options[combo.selectedIndex].text);
}

function onChangeServico(){
	var combo = document.getElementById("servico.idServico");
    setElementValue('dsServicoHidden', combo.options[combo.selectedIndex].text);
}
</script>