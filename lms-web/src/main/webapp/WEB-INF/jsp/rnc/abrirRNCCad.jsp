<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.abrirRNCAction" onPageLoadCallBack="retornoCarregaPagina" >
	<adsm:form action="/rnc/abrirRNC" height="390" idProperty="idOcorrenciaNaoConformidade" >

		<adsm:i18nLabels>
			<adsm:include key="LMS-09122"/>
			<adsm:include key="LMS-12021"/>
			<adsm:include key="LMS-12023"/>
			<adsm:include key="LMS-12026"/>
			<adsm:include key="LMS-12027"/>
			<adsm:include key="LMS-12031"/>
		</adsm:i18nLabels>

		<adsm:hidden property="naoConformidade.idNaoConformidade" serializable="false" />
		<adsm:hidden property="naoConformidade.filial.idFilial" serializable="false" />
		<adsm:hidden property="naoConformidade.clienteByIdClienteRemetente.blClienteCCT" serializable="false" />
		<adsm:hidden property="isClienteObrigaBO" serializable="false" />
		<adsm:hidden property="existFotoOcorrenciaTpAnexoBO" serializable="false" />

		<%-- Esse valor vem da tela de registro --%>		
		<adsm:hidden property="filialByIdFilialResponsavel.idFilial" serializable="true" />
		<adsm:hidden property="filialByIdFilialResponsavel.sgFilial" serializable="false" />
		<adsm:hidden property="filialByIdFilialResponsavel.pessoa.nmFantasia" serializable="false" />
		

		<adsm:textbox dataType="text" property="naoConformidade.filial.sgFilial" size="3" maxLength="3" label="naoConformidade" 
					  labelWidth="23%" width="25%" disabled="true" serializable="false" >
			<adsm:textbox dataType="integer" property="naoConformidade.nrNaoConformidade" size="9" maxLength="8" mask="00000000" 
						  disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:combobox property="motivoAberturaNc.idMotivoAberturaNc" 
			optionProperty="idMotivoAberturaNc"
			optionLabelProperty="dsMotivoAbertura"
					   service="lms.rnc.abrirRNCAction.findMotivoAberturaNc" 
			onchange="return motivoAberturaNc_OnChange(this, true)"
			label="motivoNaoConformidade" labelWidth="21%" width="31%"
			required="true">
			<adsm:propertyMapping modelProperty="blExigeDocServico"
				relatedProperty="blExigeDocServico" />
			<adsm:propertyMapping modelProperty="blExigeValor"
				relatedProperty="blExigeValor" />
			<adsm:propertyMapping modelProperty="blExigeQtdVolumes"
				relatedProperty="blExigeQtdVolumes" />
		</adsm:combobox>
		<adsm:hidden property="blExigeDocServico" serializable="false" />
		<adsm:hidden property="blExigeValor" serializable="false" />
		<adsm:hidden property="blExigeQtdVolumes" serializable="false" />
		

		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false" />
		<adsm:lookup dataType="text" label="controleCargas"
					 property="controleCarga.filialByIdFilialOrigem" 
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="lms.rnc.abrirRNCAction.findLookupFilialByControleCarga" 
					 action="/municipios/manterFiliais" 
					 onchange="return controleCargaFilial_OnChange()"
					 popupLabel="pesquisarFilial"
					 onPopupSetValue="popupControleCarga" 
					 onDataLoadCallBack="retornoControleCargaFilial"
					 size="3" maxLength="3" labelWidth="23%" width="77%" picker="false" serializable="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
					 
	 		<adsm:lookup dataType="integer" property="controleCarga" 
	 					 idProperty="idControleCarga" criteriaProperty="nrControleCarga" 
						 service="lms.rnc.abrirRNCAction.findLookupControleCarga" 
						 action="/carregamento/manterControleCargas" 
						 onPopupSetValue="popupControleCarga" popupLabel="pesquisarControleCarga"
						 size="9" maxLength="8" mask="00000000" serializable="true" >
 						 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.idFilial" disable="false" />
 						 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" criteriaProperty="controleCarga.filialByIdFilialOrigem.sgFilial" disable="false" />
 						 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" criteriaProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" />
 						 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.idFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.idFilial" blankFill="false" />
 						 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.sgFilial" relatedProperty="controleCarga.filialByIdFilialOrigem.sgFilial" blankFill="false" />
 						 <adsm:propertyMapping modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" relatedProperty="controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
						 <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrFrota" relatedProperty="controleCarga.meioTransporteByIdTransportado.nrFrota" />
						 <adsm:propertyMapping modelProperty="meioTransporteByIdTransportado.nrIdentificador" relatedProperty="controleCarga.meioTransporteByIdTransportado.nrIdentificador" />
						 <adsm:propertyMapping modelProperty="meioTransporteByIdSemiRebocado.nrFrota" relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrFrota" />
						 <adsm:propertyMapping modelProperty="meioTransporteByIdSemiRebocado.nrIdentificador" relatedProperty="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador" />
			</adsm:lookup>
		</adsm:lookup>

		<adsm:section caption="documentoServico" />
		<adsm:combobox property="naoConformidade.doctoServico.tpDocumentoServico" 
					   label="documentoServico" labelWidth="23%" width="30%" 
					   service="lms.rnc.abrirRNCAction.findTipoDocumentoServico" 
					   optionProperty="value" optionLabelProperty="description"
					   onchange="documentoServico_OnChange(); 
						   var args = {
							   documentTypeElement:this, 
							   filialElement:document.getElementById('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial'), 
							   documentNumberElement:document.getElementById('naoConformidade.doctoServico.idDoctoServico'), 
							   parentQualifier:'', 
							   documentGroup:'SERVICE',
							   actionService:'lms.rnc.abrirRNCAction'
						   };
					   	   var r=changeDocumentWidgetType(args); afterChangeDocumentType(args); return r;">

			<adsm:lookup dataType="text"
						 property="naoConformidade.doctoServico.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" 
						 disabled="true"
						 action="" popupLabel="pesquisarFilial"
						 size="3" maxLength="3" picker="false" onDataLoadCallBack="enableNaoConformidadeDoctoServico"
						 onchange="doctoServicoSgFilial_OnChange(); return changeDocumentWidgetFilial({
						   filialElement:document.getElementById('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('naoConformidade.doctoServico.idDoctoServico')
						   });" />
			
			<adsm:lookup dataType="integer" 
						 property="naoConformidade.doctoServico" 
						 idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service=""
						 action=""
						 popupLabel="pesquisarDocumentoServico"
						 onDataLoadCallBack="retornoDocumentoServico"
						 onchange="return doctoServicoNrDoctoServico_OnChange();"
						 onPopupSetValue="popupDoctoServicoNrDoctoServico"
						 size="12" maxLength="8" mask="00000000" disabled="true" />
		</adsm:combobox>
		<adsm:hidden property="cdLocalizacaoMercadoria" serializable="false" />
		<adsm:hidden property="naoConformidade.dsMotivoAbertura" serializable="true" />

		<adsm:combobox property="naoConformidade.tpModal" label="modal" domain="DM_MODAL"
				labelWidth="11%" width="31%" required="false" disabled="true"/>
		
		<adsm:combobox property="naoConformidade.awb" 
					   optionProperty="idAwb" 
					   optionLabelProperty="dsAwbFormatado"
					   labelWidth="23%" 
					   width="30%"
					   onlyActiveValues="true" 
					   label="awb" 
					   boxWidth="135"
					   style="vertical-align:top" disabled="true"/>
					   
		<adsm:combobox label="atribuirAWB" property="tpStatusAwb"
					   domain="DM_LOOKUP_AWB" 
					   labelWidth="11%" width="31%"
					   defaultValue="E" renderOptions="true" disabled="true">
        
			<adsm:lookup property="ciaFilialMercurio.empresa" 
						 idProperty="idEmpresa"
						 dataType="text"
						 criteriaProperty="sgEmpresa"
				 		 criteriaSerializable="true"
						 service="lms.rnc.abrirRNCAction.findLookupSgCiaAerea"
						 action="" 	
						 size="3" maxLength="3"						 
					 	picker="false">
			</adsm:lookup>

	        <adsm:lookup dataType="integer" size="13" maxLength="11" 
	        	property="awb"
	        	idProperty="idAwb"
	        	criteriaProperty="nrAwb"
	        	criteriaSerializable="true"
	 			service="lms.rnc.abrirRNCAction.findLookupAwb"
				action="expedicao/consultarAWBs"
				onchange="return limparDadosAwb(this.value)"
				onPopupSetValue="findAwb_cb">
				
				<adsm:propertyMapping modelProperty="tpStatusAwb" criteriaProperty="tpStatusAwb" disable="true" />
				<adsm:propertyMapping modelProperty="ciaFilialMercurio.empresa.idEmpresa" criteriaProperty="ciaFilialMercurio.empresa.idEmpresa" disable="true" />
				
	        </adsm:lookup>
	    </adsm:combobox>


		<adsm:textbox dataType="JTDateTimeZone" property="naoConformidade.doctoServico.dhEmissao" 
					  label="dataEmissao" picker="false" size="20" labelWidth="23%" width="30%" 
					  disabled="true" serializable="false" />
					  
		<adsm:combobox property="naoConformidade.causadorRnc" label="causador" domain="DM_LOCAL_CAUSADOR"
					  labelWidth="11%" width="31%" renderOptions="true" defaultValue="F" />
					  
		<adsm:lookup dataType="text" property="naoConformidade.clienteByIdClienteRemetente" 
 					 idProperty="idCliente" criteriaProperty="pessoa.nrIdentificacao" 
					 service="lms.rnc.abrirRNCAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao" 
					 label="remetente" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 size="20" maxLength="20" labelWidth="23%" width="77%" serializable="true" disabled="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="naoConformidade.clienteByIdClienteRemetente.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="naoConformidade.clienteByIdClienteRemetente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:lookup dataType="text" property="naoConformidade.clienteByIdClienteDestinatario" 
 					 idProperty="idCliente" criteriaProperty="pessoa.nrIdentificacao" 
					 service="lms.rnc.abrirRNCAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao" 
					 label="destinatario" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 size="20" maxLength="20" labelWidth="23%" width="77%" serializable="true" disabled="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="naoConformidade.clienteByIdClienteDestinatario.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="naoConformidade.clienteByIdClienteDestinatario.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>
		
		<adsm:lookup dataType="text" property="naoConformidade.clienteByIdClienteTomador" 
 					 idProperty="idCliente" criteriaProperty="pessoa.nrIdentificacao" 
					 service="lms.rnc.abrirRNCAction.findLookupCliente" 
					 action="/vendas/manterDadosIdentificacao" 
					 label="tomadorFrete" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 size="20" maxLength="20" labelWidth="23%" width="77%" serializable="true" disabled="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="naoConformidade.clienteByIdClienteTomador.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="naoConformidade.clienteByIdClienteTomador.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:textbox dataType="text" property="destinoDoctoServico" label="destino" 
					  size="10" maxLength="3" labelWidth="23%" width="30%" disabled="true" serializable="false" />

		<adsm:combobox property="manifesto.tpManifesto" 
					   label="manifesto" labelWidth="11%" width="31%"
					   service="lms.rnc.abrirRNCAction.findTipoManifesto" 
					   optionProperty="value" optionLabelProperty="description"
					   serializable="false" 
					   onchange="manifesto_OnChange();" >
			<adsm:lookup dataType="text"
						 property="manifesto.filialByIdFilialOrigem"
					 	 idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="lms.rnc.abrirRNCAction.findLookupFilialByManifesto"
						 action="" popupLabel="pesquisarFilial"
						 size="3" maxLength="3" picker="false" disabled="true" serializable="false" 
						 onDataLoadCallBack="enableManifestoManifestoViagemNacioal"
 						 >			 

			 	<adsm:lookup dataType="integer" 
						 onDataLoadCallBack="retornoManifesto"
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service="lms.rnc.abrirRNCAction.findLookupManifesto"
						 action="" 
						 onchange="return manifestoNrManifestoOrigem_OnChange();"
						 picker="false"
						 size="10" maxLength="8" mask="00000000" disabled="true" serializable="false" >
						 
				<adsm:propertyMapping modelProperty="manifesto.tpManifesto" criteriaProperty="manifesto.tpManifesto" disable="false" />
				<adsm:propertyMapping modelProperty="manifesto.filialByIdFilialOrigem.idFilial" criteriaProperty="manifesto.filialByIdFilialOrigem.idFilial" disable="false" />
						 
			</adsm:lookup>
			</adsm:lookup>
		</adsm:combobox>
		<adsm:hidden property="manifesto.nrNumero" serializable="true" />
		<adsm:hidden property="manifesto.idManifesto" serializable="true"/>
		<adsm:hidden property="manifesto.tpStatusManifesto" serializable="false"/>
		<adsm:hidden property="manifesto.tpStatusManifestoEntrega" value="" serializable="false" />
		<adsm:hidden property="manifesto.tpManifestoEntrega" value="EN" serializable="false"/>
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
		<adsm:hidden property="blManifestoPreenchidoManualmente" serializable="false"/>

		<adsm:textbox property="naoConformidade.doctoServico.conhecimento.qtVolumes" label="quantidadeVolumes" dataType="integer" 
					  size="6" maxLength="8" labelWidth="23%" width="30%" disabled="true" serializable="false" />
		
		<adsm:hidden property="naoConformidade.doctoServico.moeda.idMoeda" serializable="false" />
		<adsm:textbox label="valor" dataType="text" property="moedaVlTotalDocServico" size="8" labelWidth="11%" 
					  width="31%" serializable="false" disabled="true" >
			<adsm:textbox dataType="currency" property="naoConformidade.doctoServico.vlMercadoria" 
						  mask="###,###,###,###,##0.00" size="18" serializable="false" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdTransportado.nrFrota" label="veiculo" 
					  size="6" maxLength="6" labelWidth="23%" width="77%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdTransportado.nrIdentificador" 
						  size="20" maxLength="25" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado.nrFrota" label="semiReboque" 
					  size="6" maxLength="6" labelWidth="23%" width="77%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador" 
						  size="20" maxLength="25" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:pairedListbox property="notaOcorrenciaNcs" 
							size="6" boxWidth="90" label="notasFiscais" labelWidth="23%" width="32%" 
							optionProperty="idNotaOcorrenciaNc" optionLabelProperty="nrNotaFiscal" 
							sourceOptionProperty="notaFiscalConhecimento.idNotaFiscalConhecimento" />

		<adsm:listbox property="notaOcorrenciaNcs2" 
					  optionProperty="idNotaOcorrenciaNc" optionLabelProperty="nrNotaFiscal" 
					  size="4" boxWidth="90" width="45%" serializable="true" >
			<adsm:textbox property="nrNotaFiscal" dataType="integer" maxLength="9" size="14" serializable="true" disabled="true" />
		</adsm:listbox>	



		<adsm:section caption="branco" />

 		<adsm:lookup dataType="text" property="empresa" 
 					 idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao" 
					 service="lms.rnc.abrirRNCAction.findLookupEmpresa" 
					 action="/municipios/manterEmpresas" 
					 label="companhiaAerea" size="18" maxLength="20" labelWidth="23%" width="77%" 
					 serializable="true" >
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:complement label="descricaoNaoConformidade" width="77%" labelWidth="23%" separator="branco" >
			<adsm:combobox property="descricaoPadraoNc.idDescricaoPadraoNc" 
						   optionProperty="idDescricaoPadraoNc" optionLabelProperty="dsPadraoNc" 
						   service="lms.rnc.abrirRNCAction.findDescricaoPadraoNc" 
						   boxWidth="446" onlyActiveValues="true" required="true" style="vertical-align:top" />
			<adsm:textarea property="dsOcorrenciaNc" maxLength="1000" columns="85" rows="3" required="true" />
		</adsm:complement>

		<adsm:checkbox property="blCaixaReaproveitada" label="caixaReaproveitada" onclick="checkCaixaReaproveitada_OnClick()" labelWidth="23%" width="77%" />
		<adsm:textbox label="clienteCaixa" dataType="text" property="dsCaixaReaproveitada" size="60" maxLength="80" 
					  labelWidth="23%" width="77%" serializable="true" />

		<adsm:complement label="causaNaoConformidade" width="77%" labelWidth="23%" separator="branco" >
			<adsm:combobox property="causaNaoConformidade.idCausaNaoConformidade" 
						   optionProperty="idCausaNaoConformidade" optionLabelProperty="dsCausaNaoConformidade"
						   service="lms.rnc.abrirRNCAction.findCausaNaoConformidade"
						   boxWidth="446" onlyActiveValues="true" required="true" style="vertical-align:top" />
			<adsm:textarea property="dsCausaNc" maxLength="200" columns="85" />
		</adsm:complement>

		<adsm:combobox property="moeda.idMoeda" label="valorNaoConformidade"
					   service="lms.rnc.abrirRNCAction.findMoeda" 
					   optionProperty="idMoeda" optionLabelProperty="siglaSimbolo" 
					   onchange="return moeda_OnChange(this)"
					   width="77%" labelWidth="23%" >
			<adsm:textbox dataType="currency" 
						  property="vlOcorrenciaNc" 
						  mask="###,###,###,###,##0.00" minValue="0.01"
						  size="18" maxLength="18" disabled="true" required="true" />
		</adsm:combobox>

		<adsm:textbox property="qtVolumes" label="quantidadeVolumes" dataType="integer" size="6" maxLength="6" labelWidth="23%" width="77%" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="emitirRNC" id="botaoEmitirRNC" buttonType="reportViewerButton" onclick="imprimeRelatorio();" disabled="true" />
			<adsm:button caption="salvar" id="storeButton" disabled="false" onclick="salvarRnc(this.form)" />
			<adsm:newButton id="btnLimpar"/>
		</adsm:buttonBar>

		<script>
			var lms_12003 = '<adsm:label key="LMS-12003"/>';
			var lms_12022 = '<adsm:label key="LMS-12022"/>';
			var lms_12004 = '<adsm:label key="LMS-12004"/>';
			var labelCausaNaoConformidade = '<adsm:label key="causaNaoConformidade"/>';
			var labelClienteCaixa = '<adsm:label key="clienteCaixa"/>';
			var labelDescricaoNaoConformidade = '<adsm:label key="descricaoNaoConformidade"/>';
			var labelDoctoServico = '<adsm:label key="documentoServico"/>';
			var labelFilialResponsavel = '<adsm:label key="filialResponsavel" />';
			var labelManifesto = '<adsm:label key="manifesto" />';
			var labelValorNaoConformidade = '<adsm:label key="valorNaoConformidade"/>';
		</script>

	</adsm:form>
</adsm:window>

<script>

function limparDadosAwb(value){
	if(value == null || value == ""){
		setElementValue("awb.idAwb", "");
	}
	return awb_nrAwbOnChangeHandler();
}

function findAwb_cb(data, error){
	if(data != null){
		data = data[0] != null ? data[0] : data;
		setElementValue("awb.idAwb", data.idAwb);
		setElementValue("ciaFilialMercurio.empresa.idEmpresa", data.ciaFilialMercurio.empresa.idEmpresa);
		setElementValue("ciaFilialMercurio.empresa.sgEmpresa", data.ciaFilialMercurio.empresa.sgEmpresa);
	}
}

function afterChangeDocumentType(args) {
	if ( getElementValue("naoConformidade.doctoServico.tpDocumentoServico") != "NFT" )
		return;

	var documentTypeElement = args.documentTypeElement;
	var documentNumberElement = args.documentNumberElement;
	documentNumberElement.propertyMappings[documentNumberElement.propertyMappings.length] = 
	{ 
		modelProperty:"tpDocumentoServico", 
		criteriaProperty:"naoConformidade.doctoServico.tpDocumentoServico",
		inlineQuery:true
	};
}


document.getElementById("dsOcorrenciaNc").label = labelDescricaoNaoConformidade;
document.getElementById("dsCausaNc").label = labelCausaNaoConformidade;

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	loadDataUsuario();
	desabilitaTodosCampos();

	// configura parametros adicionais para o comobo de manifesto:
	EN_MANIFESTO_DOCUMENT_WIDGET_DEFINITION.documentNumber.propertyMappings.push(
			{
				modelProperty:"doctoServico.tpDocumentoServico", 
				criteriaProperty:"naoConformidade.doctoServico.tpDocumentoServico",
				inlineQuery:true
			},
			{
				modelProperty:"idDoctoServico", 
				criteriaProperty:"naoConformidade.doctoServico.idDoctoServico"	
			},
			{
				modelProperty:"doctoServico.idDoctoServico", 
				criteriaProperty:"naoConformidade.doctoServico.idDoctoServico",
				inlineQuery:true
			},
			{
				modelProperty:"doctoServico.filialByIdFilialOrigem.sgFilial", 
				criteriaProperty:"naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial",
				inlineQuery:true
			},
			{
				modelProperty:"doctoServico.nrDoctoServico", 
				criteriaProperty:"naoConformidade.doctoServico.nrDoctoServico",
				inlineQuery:true
}
	);

	VN_MANIFESTO_DOCUMENT_WIDGET_DEFINITION.documentNumber.propertyMappings.push(
			{
				modelProperty:"idDoctoServico", 
				criteriaProperty:"naoConformidade.doctoServico.idDoctoServico",
				inlineQuery:true
			}
	);
}


function initWindow(eventObj) {
	desabilitaTodosCampos();
	
	if (eventObj.name == "tab_load") {
		setFocusOnFirstFocusableField();
		setDisabled('storeButton', false);
		setDisabled('botaoEmitirRNC', true);
		
	} else if (eventObj.name == "newButton_click") {
		desabilitaTab("item", true);
		setDisabled('motivoAberturaNc.idMotivoAberturaNc', false);
		desabilitaTab("caracteristicas", true);
		desabilitaTab("fotos", true);
		limpaPaired();
		checkCaixaReaproveitada_OnClick();
		setFocusOnFirstFocusableField();
		limparTabItem();
		setDisabled('storeButton', false);
		setDisabled('botaoEmitirRNC', true);
		
	} else if (eventObj.name == "storeButton"){
		setFocusOnNewButton();
		
	} else if(eventObj.name == "tab_click"){
		motivoAberturaNc_OnChange(getElementValue('motivoAberturaNc.idMotivoAberturaNc'), false);
		loadWindowByTabItem();
		
		var idOcorrenciaNaoConformidade = getElementValue('idOcorrenciaNaoConformidade'); 
		if(idOcorrenciaNaoConformidade != undefined && idOcorrenciaNaoConformidade != null && idOcorrenciaNaoConformidade != ''){
			setDisabled('storeButton', true);
			setDisabled('botaoEmitirRNC', false);
		} else {
			setDisabled('storeButton', false);
			setDisabled('botaoEmitirRNC', true);
		}
	}
}

var qtdInformadaGlobal;
function loadWindowByTabItem(){	
		var tabItem = getTabGroup(this.document).getTab("item");
		
		if(tabItem.getElementById("pessoa.remetente.cpfCnpj").value != undefined && tabItem.getElementById("pessoa.remetente.cpfCnpj").value != ""){
			var cpfCnpj = tabItem.getElementById("pessoa.remetente.cpfCnpj").value;
			var nmPessoa = tabItem.getElementById("pessoa.remetente.nmPessoa").value;
			
			setElementValue("naoConformidade.clienteByIdClienteRemetente.pessoa.nrIdentificacao", cpfCnpj);
			setElementValue("naoConformidade.clienteByIdClienteRemetente.pessoa.nmPessoa", nmPessoa);
		}	
		else{
			setElementValue("naoConformidade.clienteByIdClienteRemetente.pessoa.nrIdentificacao", "");
			setElementValue("naoConformidade.clienteByIdClienteRemetente.pessoa.nmPessoa", "");
		}
		
		var gridDef = tabItem.getElementById("itensNFe.dataTable").gridDefinition;
	 	var qtdInformada = 0;
	 	var qtdAnterior = 0;
	 	var valor = 0;
	 	var divValorQtdAnterior = 0;
		var sumValorNaoConformidade;
		
		qtdInformadaGlobal = 0;
		for(var i = 0; i < gridDef.currentRowCount; i++) {
			qtdInformada = tabItem.getElementById("itensNFe:" + i + ".itensNFe.qtdInformada").value;
			qtdAnterior = gridDef.gridState["data"][i].itensNFe.qtdeAnterior;
			valor = gridDef.gridState["data"][i].itensNFe.valor;
	
			if(qtdInformada == "" || qtdInformada == 0){
				continue;
			}
			qtdInformadaGlobal = parseFloat(qtdInformadaGlobal) + parseFloat(qtdInformada);
			
			divValorQtdAnterior = valor / qtdAnterior;
	
			if(sumValorNaoConformidade == undefined){
				sumValorNaoConformidade = 0;
			}
			
			sumValorNaoConformidade = parseFloat(sumValorNaoConformidade) + (divValorQtdAnterior * qtdInformada);
			sumValorNaoConformidade = sumValorNaoConformidade.toFixed(2);
		} 
		
		if(sumValorNaoConformidade != undefined){
			setElementValue("vlOcorrenciaNc", sumValorNaoConformidade.replace(".", ","));
			
			var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findMoedaUsuario", "setMoedaUsuario", new Array());
		   	xmit({serviceDataObjects:[sdo]});
		}
}

function setMoedaUsuario_cb(data, error){
	setElementValue("moeda.idMoeda", data.idMoeda);
	setDisabled('vlOcorrenciaNc', false);
}
 
/**
 * Desabilita todos os campos da tela com exceção do "Motivo não conformidade"
 */
function desabilitaTodosCampos() {
	setDisabled('notaOcorrenciaNcs', true);
	setDisabled('notaOcorrenciaNcs2', true);
	setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', true);
	setDisabled('naoConformidade.filial.sgFilial', true);
	setDisabled('naoConformidade.nrNaoConformidade', true);
	desabilitaControleCarga(true);
	desabilitaTagDocumento(true);
	setDisabled('manifesto.tpManifesto', true);
	setDisabled('manifesto.manifestoViagemNacional.nrManifestoOrigem', true);
	setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
	setDisabled('naoConformidade.clienteByIdClienteRemetente.idCliente', true);
	setDisabled('naoConformidade.clienteByIdClienteDestinatario.idCliente', true);
	setDisabled('naoConformidade.clienteByIdClienteTomador.idCliente', true);
	setDisabled('empresa.idEmpresa', true);
	setDisabled('descricaoPadraoNc.idDescricaoPadraoNc', true);
	setDisabled('dsOcorrenciaNc', true);
	setDisabled('blCaixaReaproveitada', true);
	setDisabled('dsCaixaReaproveitada', true);
	setDisabled('causaNaoConformidade.idCausaNaoConformidade', true);
	setDisabled('dsCausaNc', true);
	setDisabled('moeda.idMoeda', true);
	setDisabled('vlOcorrenciaNc', true);
	setDisabled('qtVolumes', true);
	setDisabled('naoConformidade.tpModal', true);
	setDisabled('naoConformidade.causadorRnc', true);
	setDisabled('naoConformidade.awb', true);
	setLookupAwbEnabled(false);
	
}

/**
 * Responsável por habilitar/desabilitar uma tab
 */
function desabilitaTab(aba, disabled) {
	var tabGroup = getTabGroup(this.document);
	tabGroup.setDisabledTab(aba, disabled);
}

/**
 * Carrega os dados do usuario (Usuario, Filial).
 */
function loadDataUsuario() {
   	var data = new Array();
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.getDataUsuario", "resultado_loadDataUsuario", data);
   	xmit({serviceDataObjects:[sdo]});
}

var dataUsuario;
function resultado_loadDataUsuario_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	dataUsuario = data;
}

/**
 * Chamada quando o campo 'Caixa reaproveitada' for marcado/desmarcado. Conforme a escolha, deve habilitar/desabilitar
 * o campo Cliente (caixa).
 */
function checkCaixaReaproveitada_OnClick() {
	if ( getElementValue('blCaixaReaproveitada') == false ) {
		resetValue('dsCaixaReaproveitada');
		setDisabled('dsCaixaReaproveitada', true);
		document.getElementById("dsCaixaReaproveitada").required = "false";
	}
	else {
		setDisabled('dsCaixaReaproveitada', false);
		document.getElementById("dsCaixaReaproveitada").required = "true";
		document.getElementById("dsCaixaReaproveitada").label = labelClienteCaixa;
	}
}

/**
 * Realiza a pesquisa para buscar os dados da combo "Descrição não conformidade" de acordo com o "Motivo não conformidade" 
 * selecionado.
 */
function loadDataDescricaoPadraoNc() {
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findDescricaoPadraoNc", "resultado_loadDataDescricaoPadraoNc", 
		{motivoAberturaNc:{idMotivoAberturaNc:getElementValue('motivoAberturaNc.idMotivoAberturaNc')}, tpSituacao:"A"} );
   	xmit({serviceDataObjects:[sdo]});
}

/**
 * Povoa a combo "Descrição não conformidade".
 */
function resultado_loadDataDescricaoPadraoNc_cb(data, error){
	if (error != undefined) {
		alert(error);
		return false;
	}
	descricaoPadraoNc_idDescricaoPadraoNc_cb(data);
	// se for retornado 1 registro, ele deve ser selecionado
	if (document.getElementById('descricaoPadraoNc.idDescricaoPadraoNc').length == 2) {
		setElementValue('descricaoPadraoNc.idDescricaoPadraoNc', document.getElementById('descricaoPadraoNc.idDescricaoPadraoNc').options[1].value);
	}
}


function moeda_OnChange(combo){
	if (getElementValue("moeda.idMoeda") == "" ) {
		resetValue("vlOcorrenciaNc");
		setDisabled("vlOcorrenciaNc", true);
	}
	else
		setDisabled("vlOcorrenciaNc", false);
	return comboboxChange({e:combo});
}


function povoaDadosForm() {
	buscarManifestoControleCargas( getElementValue('naoConformidade.doctoServico.idDoctoServico') );
	buscarDadosDoctoServico( getElementValue('naoConformidade.doctoServico.idDoctoServico') );
	
	buscarAwbsDoctoServico(getElementValue('naoConformidade.doctoServico.idDoctoServico'));
	
	var tpDocumentoServico = getElementValue('naoConformidade.doctoServico.tpDocumentoServico');
	
	if (inAny(tpDocumentoServico, ['CTR', 'NFT', 'CTE', 'NTE'])) {
		buscarNotasFiscaisConhecimento(getElementValue('naoConformidade.doctoServico.idDoctoServico'));
	} else {
		if (tpDocumentoServico == 'MDA') {
			buscarNotasFiscaisMda(getElementValue('naoConformidade.doctoServico.idDoctoServico'));
		} else {
			limpaPaired();
			setDisabled('notaOcorrenciaNcs', true);
		}
	}
}

/**
 * Realiza pesquisa por awbs que estejam vinculados ao documento de serviço
 */
function buscarAwbsDoctoServico(idDoctoServico){
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findAwbsDoctoServico", "buscarAwbsDoctoServico", 
			{conhecimento:{idDoctoServico:idDoctoServico}});
	xmit({serviceDataObjects:[sdo]});
}

function buscarAwbsDoctoServico_cb(data, error){
	if(error != undefined){
		alert(error);
		return false;
	}
	
	
	if(data.length < 1 ){
		document.getElementById("naoConformidade.awb").required = "false";
		setDisabled("naoConformidade.awb", true);
		document.getElementById("naoConformidade.awb").selectedIndex = 0;
		setLookupAwbEnabled(true);
	}
	else{
		document.getElementById("naoConformidade.awb").required = "true";
		setDisabled("naoConformidade.awb", false);
		naoConformidade_awb_cb(data);
		if(data.length == 1){
			document.getElementById("naoConformidade.awb").selectedIndex = 1;
			setDisabled("naoConformidade.awb", true);
		}
		
		setLookupAwbEnabled(false);
	}
}

function setLookupAwbEnabled(enabled){
	setDisabled("awb.idAwb", !enabled);
	setDisabled("awb.nrAwb", !enabled);
	setDisabled("ciaFilialMercurio.empresa.idEmpresa", !enabled);
	setDisabled("ciaFilialMercurio.empresa.sgEmpresa", !enabled);
}

/**
 * Realiza pesquisa em NotasFiscais de acordo com o docto de servico informado (CTRC).
 */
function buscarNotasFiscaisConhecimento(idDoctoServico) {
	setDisabled('notaOcorrenciaNcs', false);
	limpaPaired();
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findNotaFiscalConhecimento", "resultado_buscarNotasFiscais", 
			{conhecimento:{idDoctoServico:idDoctoServico}});
	xmit({serviceDataObjects:[sdo]});
}

/**
 * Realiza pesquisa em NotasFiscais de acordo com o docto de servico informado (MDA).
 */
function buscarNotasFiscaisMda(idDoctoServico) {
	setDisabled('notaOcorrenciaNcs', false);
	limpaPaired();
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findNotaFiscalMda", "resultado_buscarNotasFiscais", 
			{mda:{idDoctoServico:idDoctoServico}});
	xmit({serviceDataObjects:[sdo]});
}

/**
 * Retorno da pesquisa em Notas Fiscais.
 */
function resultado_buscarNotasFiscais_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	notaOcorrenciaNcs_source_cb(data, error);
}


function limpaPaired() {
	resetValue(document.getElementById("notaOcorrenciaNcs"));
	notaOcorrenciaNcsListboxDef.clearSourceOptions();
}   

	//#####################################################
	// Inicio da validacao do pce
	//#####################################################
	
	/**
	 * Faz a validacao da tela
	 *
	 * @param data = parametro da consulta.
	 */
	function validatePCE(data) {	
		
		var data = new Object();
		data.remetente = getElementValue("naoConformidade.clienteByIdClienteRemetente.idCliente");
		data.destinatario = getElementValue("naoConformidade.clienteByIdClienteDestinatario.idCliente");
		
		var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.validatePCE", "validatePCE", data);
		xmit({serviceDataObjects:[sdo]});
	}
		
	/**
	 * Callback da chamada de validacao do PCE, chama a popUp de alert com os dados do
	 * PCE caso necessario.
	 *
	 * @param data
	 * @param error
	 */
	function validatePCE_cb(data, error) {
		if (data._exception==undefined) {
			if (data.destinatario!=undefined) {
				if (data.destinatario.codigo!=undefined) {
					showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.destinatario.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
				}
			}
			if (data.remetente!=undefined) {
				if (data.remetente.codigo!=undefined) {
					showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + data.remetente.codigo + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
				}
			}
		} else {
			alert(error);
		}
	}
	
	/**
	 * Este callback existe decorrente de uma necessidade da popUp de alert.
	 */
	function alertPCE_cb() {
		//Empty...
	}	
	
	//#####################################################
	// Fim da validacao do pce
	//#####################################################

var formRnc;
/**
 * Função chamada quando no eveto onclick do botão salvar.
 */
function salvarRnc(form) {
	if (getElementValue('blExigeDocServico') == "false") {
	        if (getElementValue('controleCarga.idControleCarga') == "") {
	    	alert("LMS-09122 - " + i18NLabel.getLabel("LMS-09122"));
		    return false;   
		    
		}
	}

	var tabItem = getTabGroup(this.document).getTab("item");
    if(getElementValue('naoConformidade.clienteByIdClienteRemetente.blClienteCCT') == 'true' && tabItem.properties.disabled == false && (qtdInformadaGlobal == undefined || qtdInformadaGlobal == 0)){
		alert("LMS-12023 - " + i18NLabel.getLabel("LMS-12023"));
		return;
    }
    
    if (getElementValue('blExigeDocServico') == "false" && 	(getElementValue('motivoAberturaNc.idMotivoAberturaNc') == "14" || getElementValue('motivoAberturaNc.idMotivoAberturaNc') == "24"))
   	{
   		document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").required = "false";
   	}
   	else if (getElementValue('controleCarga.idControleCarga') != "" && getElementValue('motivoAberturaNc.idMotivoAberturaNc') != "24") {
		document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").required = "true";
		document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").label = labelManifesto;
	}
	else {
		document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem").required = "false";
	}
	if (!validateForm(form)) {
		return false;
	}
	
	formRnc = form;
	validaPreCondicoes();
}

function montaParametrosAbaItems(){
	var tabItem = getTabGroup(this.document).getTab("item");
	var gridDef = tabItem.getElementById("itensNFe.dataTable").gridDefinition

	for(var i = 0; i < gridDef.currentRowCount; i++) {
		gridDef.gridState["data"][i].itensNFe.qtdInformada = tabItem.getElementById("itensNFe:" + i + ".itensNFe.qtdInformada").value;
	}
	
	return gridDef.gridState["data"];
}
 
/**
 * Valida a quantidade e o valor que foi informado, com os respectivos valores do docto de serviço.
 */
function validaPreCondicoes() {
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.validatePreConditions", "resultado_validaPreCondicoes", 
		 {idDoctoServico:getElementValue('naoConformidade.doctoServico.idDoctoServico'), 
		 qtVolumes:getElementValue('qtVolumes'),
		 vlOcorrenciaNc:getElementValue('vlOcorrenciaNc'),
		 idManifesto:getElementValue('manifesto.idManifesto'),
		 idMotivoAberturaNc:getElementValue('motivoAberturaNc.idMotivoAberturaNc'),
		 nrManifesto:getElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem')
	});
   	xmit({serviceDataObjects:[sdo]});
} 

function resultado_validaPreCondicoes_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setFocusOnFirstFocusableField();
		return false;
	}
	
	if('1'.equals(getElementValue('cdLocalizacaoMercadoria'))){
		alert(lms_12022);
		var data = showModalDialog('/rnc/abrirRNC.do?cmd=motivoabertura',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:460px;dialogHeight:200px;');

		if(!data) {
			return false;
		}
		setElementValue('naoConformidade.dsMotivoAbertura', data );
	}
	
	idDoctoServico = getElementValue("naoConformidade.doctoServico.idDoctoServico");
	existDoctoServico = (idDoctoServico != null && idDoctoServico != '');
	var campo = document.getElementById("notaOcorrenciaNcs");
	if( existDoctoServico && campo.options.length == 0){
		alert("LMS-12031 - " + i18NLabel.getLabel("LMS-12031"));			
		setFocus(campo);
		return false;
	} 
	
	validateInsercaoBoObrigatorio();
	validateAwbRessalvado();
	submeterFormulario();
}

function validateAwbRessalvado() {
	tpLocalCausador = getElementValue("naoConformidade.causadorRnc");
	
	if(tpLocalCausador !== null && tpLocalCausador === 'A'){
		alert("LMS-12027 - " + i18NLabel.getLabel("LMS-12027"));
	}
}

function validateInsercaoBoObrigatorio() {
	idDoctoServico = getElementValue("naoConformidade.doctoServico.idDoctoServico");
	isClienteObrigaBO = getElementValue("isClienteObrigaBO");
		
	idNaoConformidade = getElementValue("naoConformidade.idNaoConformidade");
	existFotoOcorrenciaTpAnexoBO = getElementValue("existFotoOcorrenciaTpAnexoBO");
	
	existDoctoServico = (idDoctoServico != null || idDoctoServico != '');
	isNovoNaoConformidade = (idNaoConformidade == null || idNaoConformidade == '');

	if(existDoctoServico && isClienteObrigaBO  == 'true' && (isNovoNaoConformidade || existFotoOcorrenciaTpAnexoBO == 'false')){
		alert("LMS-12026 - " + i18NLabel.getLabel("LMS-12026"));
	}
}

function submeterFormulario() {
	showModalDialog('rnc/registrarFilialResponsavelNaoConformidade.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:550px;dialogHeight:120px;');
	
	if (getElementValue('filialByIdFilialResponsavel.idFilial') == undefined || getElementValue('filialByIdFilialResponsavel.idFilial') == "") {
		return false;
	}
	
	formBean = buildFormBeanFromForm(formRnc);
	formBean.itensGrid = montaParametrosAbaItems();
	
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.storeRNC", "store", formBean);
	xmit({serviceDataObjects:[sdo]});
}

function store_cb(data, error) {
	if (error != undefined) {
		alert(error);
		setFocusOnFirstFocusableField();
		return false;
	}
	setElementValue('idOcorrenciaNaoConformidade', getNestedBeanPropertyValue(data, "idOcorrenciaNaoConformidade"));
	setElementValue('naoConformidade.idNaoConformidade', getNestedBeanPropertyValue(data, "naoConformidade.idNaoConformidade"));
	setElementValue('naoConformidade.nrNaoConformidade', getFormattedValue("integer",  getNestedBeanPropertyValue(data, "naoConformidade.nrNaoConformidade"), "00000000", true));
	setElementValue('naoConformidade.filial.sgFilial', getNestedBeanPropertyValue(data, "naoConformidade.filial.sgFilial"));
	setElementValue('naoConformidade.filial.idFilial', getNestedBeanPropertyValue(data, "naoConformidade.filial.idFilial"));
	desabilitaTab("caracteristicas", false);
	desabilitaTab("fotos", false);

	alert(labelFilialResponsavel + ': ' + getElementValue('filialByIdFilialResponsavel.sgFilial'));
	setDisabled('motivoAberturaNc.idMotivoAberturaNc', true);
	setDisabled('storeButton', true);
	setDisabled('botaoEmitirRNC', false);
	desabilitaTodosCampos();
	
	// seta o flag q indica se houve mudança no estado da tela para falso.
    // evitar que seja perguntado se deseja limpar
    var tab = getTab(this.document);
	tab.setChanged(false);
	tab.itemTabChanged = false;
	document.getElementById("btnLimpar").focus();
	showSuccessMessage();
	validatePCE();

	if(getNestedBeanPropertyValue(data, "lmsMensagem") != null){
		alert("LMS-12021 - " + i18NLabel.getLabel("LMS-12021"));
	}	
			
}


function imprimeRelatorio() {
	var data = new Array();
	setNestedBeanPropertyValue(data, "tpFormatoRelatorio", "pdf");
	setNestedBeanPropertyValue(data, "naoConformidade.filial.sgFilial", getElementValue("naoConformidade.filial.sgFilial"));
	setNestedBeanPropertyValue(data, "naoConformidade.idNaoConformidade", getElementValue("naoConformidade.idNaoConformidade"));
	setNestedBeanPropertyValue(data, "naoConformidade.nrNaoConformidade", getElementValue("naoConformidade.nrNaoConformidade"));
	var sdo = createServiceDataObject('lms.rnc.emitirRNCAction', 'retornoRelatorio', data); 
	executeReportWindowed(sdo, 'pdf');
}


/************************* INICIO - CONTROLE CARGA *************************/

function resetaControleCarga() {
	resetValue('controleCarga.idControleCarga');
	resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
	limparDadosFrota();
	desabilitaControleCarga(false);
}

/**
 * Limpa os dados informados no campo veículo e semi-reboque
 */
function limparDadosFrota() {
	resetValue('controleCarga.meioTransporteByIdTransportado.nrFrota');
	resetValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador');
	resetValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota');
	resetValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador');
}

function desabilitaControleCarga(valor) {
	setDisabled('controleCarga.filialByIdFilialOrigem.idFilial', valor);
	if (valor == true)
		setDisabled('controleCarga.idControleCarga', true);
	else {
		setDisabled('controleCarga.idControleCarga', false);
		if (getElementValue('controleCarga.nrControleCarga') == "")
			setDisabled('controleCarga.nrControleCarga', true);
	}
}

function controleCargaFilial_OnChange() {
	var r = controleCarga_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	if (getElementValue('controleCarga.filialByIdFilialOrigem.sgFilial') == "") {
		setDisabled('controleCarga.idControleCarga', true);
		resetaControleCarga();
	}
	return r;
}

function retornoControleCargaFilial_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = controleCarga_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data)
	if (r == true) {
		setDisabled('controleCarga.idControleCarga', false);
		setFocus(document.getElementById("controleCarga.nrControleCarga"));
	}
}

function popupControleCarga(data) {
	setDisabled('controleCarga.idControleCarga', false);
}
/************************* FIM - CONTROLE CARGA *************************/


/************************* INICIO - MOTIVO ABERTURA NC *************************/

/**
 * Chamada a cada alteração do valor da combo "Motivo não conformidade". Chama a função para povoar a "Descrição não conformidade"
 * de acordo com o "Motivo não conformidade" selecionado e habilita alguns campos da tela.
 */
function motivoAberturaNc_OnChange(combo, isLimparTabItem) {
	setFocusOnNewButton(); 
	limparCamposRelacionadosMotivoAbertura();
	var r = comboboxChange({e:combo})
	
	if (getElementValue('motivoAberturaNc.idMotivoAberturaNc') != "") {
		loadDataDescricaoPadraoNc();
		setaDadosByMotivoAberturaNC();
		habilitaCamposConformeMotivoAberturaNc();
		//desabilitaTab("item", true);
	
	}else {
		desabilitaTodosCampos();
		desabilitaTab("item", true);
	}
	
	return r;
}

function limparTabItem(){
	var tabItem = getTabGroup(this.document).getTab("item");
	resetValue(tabItem.getElementById("pessoa.remetente.cpfCnpj"));
	resetValue(tabItem.getElementById("pessoa.remetente.nmPessoa"));
	resetValue(tabItem.getElementById("nrChaveNfe_nrChave"));
	resetValue(tabItem.getElementById("nrChaveNfe"));
	tabItem.getElementById("itensNFe.dataTable").gridDefinition.resetGrid();
}

function limparCamposRelacionadosMotivoAbertura() {
	if (getElementValue('naoConformidade.doctoServico.moeda.idMoeda') == "")
		resetValue('moeda.idMoeda');
}

/**
 * Responsável por habilitar campos quando o 'Motivo não conformidade' for informado.
 */
function habilitaCamposConformeMotivoAberturaNc() {
	desabilitaTagDocumento(false);
	setDisabled('descricaoPadraoNc.idDescricaoPadraoNc', false);
	setDisabled('dsOcorrenciaNc', false);
	setDisabled('blCaixaReaproveitada', false);
	setDisabled('dsCaixaReaproveitada', true);
	setDisabled('causaNaoConformidade.idCausaNaoConformidade', false);
	setDisabled('dsCausaNc', false);
	setDisabled('qtVolumes', false);
	setDisabled('naoConformidade.causadorRnc', false);
	if (getElementValue('naoConformidade.doctoServico.moeda.idMoeda') == "")
		setDisabled('moeda.idMoeda', false);
	else
		setDisabled('vlOcorrenciaNc', false);
}

function habilitaCamposConformeIn() {
	 // Descrição da Não Conformidade (combo e textArea)
	setDisabled('descricaoPadraoNc.idDescricaoPadraoNc', false);
	setDisabled('dsOcorrenciaNc', false);
	
	// Caixa Reaproveitada (check)
	setDisabled('blCaixaReaproveitada', false);
	
	// Causa Não Conformidade (combo e textArea)
	setDisabled('causaNaoConformidade.idCausaNaoConformidade', false);
	setDisabled('dsCausaNc', false);
	
	// Valor não conformidade
	if (getElementValue('naoConformidade.doctoServico.moeda.idMoeda') == "")
		setDisabled('moeda.idMoeda', false);
	else
		setDisabled('vlOcorrenciaNc', false);
}

function verificaExigenciaDoctoServico() {
	
	if (getElementValue('blExigeDocServico') == "true") {
		limparCamposRelacionadosManifesto();
		buscarManifestoControleCargas(getElementValue('naoConformidade.doctoServico.idDoctoServico'));
		document.getElementById("naoConformidade.doctoServico.idDoctoServico").required = "true";
		document.getElementById("naoConformidade.doctoServico.idDoctoServico").label = labelDoctoServico;
		setDisabled('naoConformidade.clienteByIdClienteRemetente.idCliente', true);
		setDisabled('naoConformidade.clienteByIdClienteDestinatario.idCliente', true);
		setDisabled('naoConformidade.clienteByIdClienteTomador.idCliente', true);
	
		if(getElementValue('motivoAberturaNc.idMotivoAberturaNc') == "24"){
			desabilitaControleCarga(false);
			desabilitaTagManifesto(false);
		}else{
		desabilitaControleCarga(true);
		desabilitaTagManifesto(true);
		}
		
		setDisabled('notaOcorrenciaNcs2', true);
		setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', true);
		resetValue(document.getElementById('notaOcorrenciaNcs2'));
		resetValue('notaOcorrenciaNcs2_nrNotaFiscal');
		return true;
	}
	else {
		document.getElementById("naoConformidade.doctoServico.idDoctoServico").required = "false";
		if (getElementValue('blManifestoPreenchidoManualmente') != "true") {
			desabilitaControleCarga(false);
		}
		if(getElementValue('motivoAberturaNc.idMotivoAberturaNc') == "24" 
				|| getElementValue('motivoAberturaNc.idMotivoAberturaNc') == "14") {
			//setDisabled('naoConformidade.tpModal', false);
			setElementValue('naoConformidade.tpModal', 'R');
		}
		desabilitaTagManifesto(false);
		if (getElementValue('naoConformidade.doctoServico.nrDoctoServico') != "") {
			setDisabled('naoConformidade.clienteByIdClienteRemetente.idCliente', true);
			setDisabled('naoConformidade.clienteByIdClienteDestinatario.idCliente', true);
			setDisabled('naoConformidade.clienteByIdClienteTomador.idCliente', true);
			setDisabled('notaOcorrenciaNcs2', true);
			setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', true);
			resetValue(document.getElementById('notaOcorrenciaNcs2'));
			resetValue('notaOcorrenciaNcs2_nrNotaFiscal');
		}
		else {
			setDisabled('naoConformidade.clienteByIdClienteRemetente.idCliente', false);
			setDisabled('naoConformidade.clienteByIdClienteDestinatario.idCliente', false);
			if (document.getElementById('_notaOcorrenciaNcs_source').length == 0) {
				setDisabled('notaOcorrenciaNcs2', false);
				setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', false);
			}
			else {
				setDisabled('notaOcorrenciaNcs2', true);
				setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', true);
			}
		}
		return false;
	}
}

/**
 * Alteração da combo de "Motivo abertura não conformidade".
 */
function setaDadosByMotivoAberturaNC(){
	verificaExigenciaDoctoServico();

	if (getElementValue("naoConformidade.doctoServico.idDoctoServico") == "") {
		if (getElementValue('motivoAberturaNc.idMotivoAberturaNc') != "") {
			setElementValue("naoConformidade.doctoServico.tpDocumentoServico", "CTE");
			changeDocumentWidgetType({
			   documentTypeElement:document.getElementById("naoConformidade.doctoServico.tpDocumentoServico"), 
			   filialElement:document.getElementById('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial'), 
			   documentNumberElement:document.getElementById('naoConformidade.doctoServico.idDoctoServico'), 
			   parentQualifier:'', 
			   documentGroup:'SERVICE',
			   actionService:'lms.rnc.abrirRNCAction'
			});
		}
		else {
			resetValue('naoConformidade.doctoServico.tpDocumentoServico');
			setDisabled("naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial", true);
		}
	}

	if (getElementValue("blExigeValor") == 'true') {
		document.getElementById("moeda.idMoeda").required = "true";
		document.getElementById("vlOcorrenciaNc").required = "true";
		document.getElementById("vlOcorrenciaNc").label = labelValorNaoConformidade;
	}
	else {
		document.getElementById("moeda.idMoeda").required = "false";
		document.getElementById("vlOcorrenciaNc").required = "false";
	}

	if (getElementValue("blExigeQtdVolumes") == 'true')
		document.getElementById("qtVolumes").required = "true";
	else
		document.getElementById("qtVolumes").required = "false";
}
/************************* FIM - MOTIVO ABERTURA NC *************************/


/************************* INICIO - DOCTO SERVICO *************************/

/**
 * Responsável por habilitar/desabilitar os campos da tag documento de serviço
 */
function desabilitaTagDocumento(valor) {
	setDisabled('naoConformidade.doctoServico.tpDocumentoServico', valor); 
	if (valor == true) {
		setDisabled('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial', true);
		setDisabled('naoConformidade.doctoServico.idDoctoServico', true);
	}
	else {
		if (getElementValue('naoConformidade.doctoServico.idDoctoServico') != "" || getElementValue('naoConformidade.doctoServico.tpDocumentoServico') != "")
			setDisabled('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial', false);
		if (getElementValue('naoConformidade.doctoServico.idDoctoServico') != "" || getElementValue('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial') != "")
			setDisabled('naoConformidade.doctoServico.idDoctoServico', false);
	}
}

/**
 * Limpa os dados informados na tag documento de serviço
 */
function limparTagDocumento() {
	resetValue("naoConformidade.doctoServico.idDoctoServico");
	resetValue("naoConformidade.doctoServico.tpDocumentoServico");
	resetValue("naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial");
	resetValue("naoConformidade.doctoServico.nrdoctoServico");
	limparCamposRelacionadosDoctoServico();
}

function enableNaoConformidadeDoctoServico_cb(data) {
   var r = naoConformidade_doctoServico_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      //setDisabled("naoConformidade.doctoServico.idDoctoServico", false);
      setFocus(document.getElementById("naoConformidade.doctoServico.nrDoctoServico"));
      documentoServico_OnChange();
   }
}

function documentoServico_OnChange() {
	verificaManifestoPreenchido();
	limparCamposRelacionadosDoctoServico();
	if (getElementValue('blExigeDocServico') == "true"){
		getElementValue('motivoAberturaNc.idMotivoAberturaNc')
		
		if(getElementValue('motivoAberturaNc.idMotivoAberturaNc') != "24"){
			limparCamposRelacionadosManifesto();
		}
	}
	if(document.getElementById("naoConformidade.doctoServico.idDoctoServico") == null){
		desabilitaTab("item", true);
	}
}

function doctoServicoSgFilial_OnChange() {
	verificaManifestoPreenchido();
	if ( getElementValue('naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial') == '' ) {
		documentoServico_OnChange();
	}
	return naoConformidade_doctoServico_nrDoctoServicoOnChangeHandler();
}

function doctoServicoNrDoctoServico_OnChange() {
	if ( getElementValue('naoConformidade.doctoServico.nrDoctoServico') == '' ) {
		documentoServico_OnChange();
	}
	return naoConformidade_doctoServico_nrDoctoServicoOnChangeHandler();
}

function resultado_findDoctoServicoMCCT_cb(data, error){
	
	 if(data._value == "true"){
	 	desabilitaTab("item", false);
	}else{
		desabilitaTab("item", true);
	}

	
}

function verificaManifestoPreenchido() {
	if (getElementValue("naoConformidade.doctoServico.nrDoctoServico") == "") {
		resetValue('blManifestoPreenchidoManualmente');
		if (getElementValue('blExigeDocServico') == "true") {
			desabilitaControleCarga(true);
			desabilitaTagManifesto(true);
		}
		else {
			desabilitaControleCarga(false);
			desabilitaTagManifesto(false);
		}
	}
}

function limparCamposRelacionadosDoctoServico() {
	resetValue('naoConformidade.clienteByIdClienteRemetente.idCliente');
	resetValue('naoConformidade.clienteByIdClienteDestinatario.idCliente');
	resetValue('naoConformidade.clienteByIdClienteTomador.idCliente');
	resetValue('naoConformidade.doctoServico.conhecimento.qtVolumes');
	resetValue('naoConformidade.doctoServico.vlMercadoria');
	resetValue('moedaVlTotalDocServico');
	resetValue('destinoDoctoServico');
	resetValue('naoConformidade.doctoServico.dhEmissao');
	resetValue('naoConformidade.clienteByIdClienteRemetente.blClienteCCT');
	resetValue('naoConformidade.tpModal');
	resetValue('naoConformidade.awb');
	limpaPaired();
	setDisabled('empresa.idEmpresa', true);
	setDisabled('notaOcorrenciaNcs', true);
	setDisabled('moeda.idMoeda', false);
	if (getElementValue('moeda.idMoeda') == "")
		setDisabled('vlOcorrenciaNc', true);
	else
		setDisabled('vlOcorrenciaNc', false);
	
	if (getElementValue('blExigeDocServico') == "false") {
		setDisabled('notaOcorrenciaNcs2', false);
		setDisabled('notaOcorrenciaNcs2_nrNotaFiscal', false);
	}
}

/**
 * Busca os dados relacionados ao documento de serviço.
 */
function buscarDadosDoctoServico(idDoctoServico) {
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findDataDoctoServico", "resultado_buscarDadosDoctoServico", 
				{idDoctoServico:idDoctoServico});
    xmit({serviceDataObjects:[sdo]});
}

/**
 * Povoa os campos com os dados retornados da busca em documento de serviço
 */
function resultado_buscarDadosDoctoServico_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	var awb = "";
	var cdLocalizacaoMercadoria = "";
	var tpDocumentoServico = getElementValue('naoConformidade.doctoServico.tpDocumentoServico');
	
	if (data != undefined) {
		setDisabled('naoConformidade.clienteByIdClienteRemetente.idCliente', true);
		setElementValue('naoConformidade.clienteByIdClienteRemetente.idCliente', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteRemetente.idCliente"));
		setElementValue('naoConformidade.clienteByIdClienteRemetente.pessoa.nrIdentificacao', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado"));
		setElementValue('naoConformidade.clienteByIdClienteRemetente.pessoa.nmPessoa', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteRemetente.pessoa.nmPessoa"));
		setElementValue('naoConformidade.clienteByIdClienteRemetente.blClienteCCT', 
				getNestedBeanPropertyValue(data,"clienteByIdClienteRemetente.blClienteCCT"));
		
		setElementValue('isClienteObrigaBO', 
				getNestedBeanPropertyValue(data,"isClienteObrigaBO"));
		
		setElementValue('existFotoOcorrenciaTpAnexoBO', 
				getNestedBeanPropertyValue(data,"existFotoOcorrenciaTpAnexoBO"));
		
		setDisabled('naoConformidade.clienteByIdClienteDestinatario.idCliente', true);
		setElementValue('naoConformidade.clienteByIdClienteDestinatario.idCliente', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteDestinatario.idCliente"));
		setElementValue('naoConformidade.clienteByIdClienteDestinatario.pessoa.nrIdentificacao', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado"));
		setElementValue('naoConformidade.clienteByIdClienteDestinatario.pessoa.nmPessoa', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteDestinatario.pessoa.nmPessoa"));
		
		setDisabled('naoConformidade.clienteByIdClienteTomador.idCliente', true);
		setElementValue('naoConformidade.clienteByIdClienteTomador.idCliente', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteTomador.idCliente"));
		setElementValue('naoConformidade.clienteByIdClienteTomador.pessoa.nrIdentificacao', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteTomador.pessoa.nrIdentificacaoFormatado"));
		setElementValue('naoConformidade.clienteByIdClienteTomador.pessoa.nmPessoa', 
						getNestedBeanPropertyValue(data,"clienteByIdClienteTomador.pessoa.nmPessoa"));

		setElementValue('naoConformidade.doctoServico.conhecimento.qtVolumes', getNestedBeanPropertyValue(data,"qtVolumes"));
		setElementValue('naoConformidade.doctoServico.vlMercadoria', setFormat(document.getElementById("naoConformidade.doctoServico.vlMercadoria"), 
						getNestedBeanPropertyValue(data,"vlTotalDocServico")) );
		setElementValue('moedaVlTotalDocServico', getNestedBeanPropertyValue(data,"moeda.dsSimbolo"));
		setElementValue('naoConformidade.doctoServico.moeda.idMoeda', getNestedBeanPropertyValue(data,"moeda.idMoeda"));
		setElementValue('destinoDoctoServico', getNestedBeanPropertyValue(data,"filialByIdFilialDestino.sgFilial"));

		setElementValue('naoConformidade.doctoServico.dhEmissao', setFormat(document.getElementById("naoConformidade.doctoServico.dhEmissao"),
		 				getNestedBeanPropertyValue(data,"dhEmissao")) );

		setElementValue('moeda.idMoeda', getNestedBeanPropertyValue(data,"moeda.idMoeda"));
		setDisabled('moeda.idMoeda', true);
		setDisabled('vlOcorrenciaNc', false);

		awb = getNestedBeanPropertyValue(data,"awb");
		cdLocalizacaoMercadoria = getNestedBeanPropertyValue(data,"localizacaoMercadoria.cdLocalizacaoMercadoria");
		setElementValue('cdLocalizacaoMercadoria', cdLocalizacaoMercadoria);
		
		tpModal = getNestedBeanPropertyValue(data,"servico.tpModal");
		setElementValue('naoConformidade.tpModal', tpModal);
	}
	
	if (awb != undefined && awb != "" && inAny(tpDocumentoServico, ['CTR', 'NFT', 'CTE', 'NTE'])) {
		setDisabled('empresa.idEmpresa', false);
	} else {
		setDisabled('empresa.idEmpresa', true);
	}
	
}

function popupDoctoServicoNrDoctoServico(data) {
	callBackDocumentoServico( getNestedBeanPropertyValue(data,"idDoctoServico") );
}

/**
 * Quando o "Número do documento" for informado
 */
function retornoDocumentoServico_cb(data) {
	var r = naoConformidade_doctoServico_nrDoctoServico_exactMatch_cb(data);
   	if (r == true) {
		callBackDocumentoServico( getNestedBeanPropertyValue(data,":0.idDoctoServico") );
	}
	return r;
}

function callBackDocumentoServico(idDoctoServico) {
	setElementValue('naoConformidade.doctoServico.idDoctoServico', idDoctoServico);
	if (idDoctoServico != undefined && idDoctoServico != ''){
		buscarNaoConformidade(idDoctoServico);

		var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findDoctoServicoMCCT", "resultado_findDoctoServicoMCCT", 
				{idDoctoServico:idDoctoServico});
		xmit({serviceDataObjects:[sdo]});
	}
}

/**
 * Realiza pesquisa em NaoConformidade a fim de verificar se existe algum registro associado ao documento de serviço informado.
 */
function buscarNaoConformidade(idDoctoServico) {
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findNaoConformidade", "resultado_buscarNaoConformidade", 
			{doctoServico:{idDoctoServico:idDoctoServico}});
	xmit({serviceDataObjects:[sdo]});
}

/**
 * Retorno da pesquisa em NaoConformidade.
 */
function resultado_buscarNaoConformidade_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (data != undefined && data.length > 0 && getNestedBeanPropertyValue(data,":0.nrNaoConformidade") != undefined) {
		var parametros = "";
		if (confirm(lms_12003, focusSim)) {
			var selectedIndex = document.getElementById('naoConformidade.doctoServico.tpDocumentoServico').selectedIndex; 
			var tpDocumentoServicoDescription = document.getElementById('naoConformidade.doctoServico.tpDocumentoServico').options[selectedIndex].text;
			
			// Formata o número da não-conformidade
			var nrNaoConformidade = getFormattedValue("integer",  getNestedBeanPropertyValue(data,":0.nrNaoConformidade"), "00000000", true);
			parametros = 
				'&naoConformidade.filial.idFilial=' + getNestedBeanPropertyValue(data,":0.filial.idFilial") +
				'&naoConformidade.filial.sgFilial=' + getNestedBeanPropertyValue(data,":0.filial.sgFilial") +
				'&naoConformidade.nrNaoConformidade=' + nrNaoConformidade +
				'&naoConformidade.idNaoConformidade=' + getNestedBeanPropertyValue(data,":0.idNaoConformidade") +
				'&naoConformidade.doctoServico.idDoctoServico=' + getElementValue("naoConformidade.doctoServico.idDoctoServico") +
				'&naoConformidade.doctoServico.tpDocumentoServico.description=' + tpDocumentoServicoDescription +
				'&naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial=' + getElementValue("naoConformidade.doctoServico.filialByIdFilialOrigem.sgFilial") + 
				'&naoConformidade.doctoServico.nrDoctoServico=' + getFormattedValue("integer",  getElementValue("naoConformidade.doctoServico.nrDoctoServico"), "00000000", true) +
				'&naoConformidade.doctoServico.moeda.idMoeda=' + getNestedBeanPropertyValue(data,":0.doctoServico.moeda.idMoeda");
		}
		limparTagDocumento();
		desabilitaTab("item", true);	
		//setDisabled('naoConformidade.doctoServico.filialByIdFilialOrigem.idFilial', true);
		//setDisabled('naoConformidade.doctoServico.idDoctoServico', true);
		setFocus(document.getElementById("naoConformidade.doctoServico.tpDocumentoServico"));
		
		if (parametros != "") {
			parent.parent.redirectPage("rnc/manterOcorrenciasNaoConformidade.do?cmd=main" + parametros);
		}
	}
	else 
		povoaDadosForm();
}
/************************* FIM - DOCTO SERVICO *************************/


/************************* INICIO - MANIFESTO *************************/

/**
 * Limpa os campos relacionados ao manifesto
 */
function limparCamposRelacionadosManifesto() {
	resetValue('controleCarga.idControleCarga');
	resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
	limparTagManifesto();
	limparDadosFrota();
}

/**
 * Limpa os dados informados na tag manifesto
 */
function limparTagManifesto() {
	resetValue("manifesto.tpManifesto");
	resetValue("manifesto.idManifesto");
	resetValue("manifesto.manifestoViagemNacional.nrManifestoOrigem");
	resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
}

/**
 * Responsável por habilitar/desabilitar os campos da tag manifesto
 */
function desabilitaTagManifesto(valor) {

	var ID_MOTIVO_ABERTURA_NC_SOBRA = "14";
	
	if(valor) {
		valor = getElementValue('motivoAberturaNc.idMotivoAberturaNc') != ID_MOTIVO_ABERTURA_NC_SOBRA;
	}
	
	setDisabled('manifesto.tpManifesto', valor);
	setDisabled('manifesto.manifestoViagemNacional.nrManifestoOrigem', valor);
	setDisabled('manifesto.filialByIdFilialOrigem.idFilial', valor);
	
}

function enableManifestoManifestoViagemNacioal_cb(data) {
   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
   if (r == true) {
      setDisabled("manifesto.manifestoViagemNacional.nrManifestoOrigem", false);
      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
   }
}

function manifesto_OnChange() {
	if (getElementValue('manifesto.idManifesto') != "") {
		desabilitaControleCarga(false);
		resetValue('controleCarga.idControleCarga');
		resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
		limparDadosFrota();
	}
}

function manifestoSgFilial_OnChange() {
	var r = manifesto_filialByIdFilialOrigem_sgFilialOnChangeHandler();
	if (r == true && getElementValue('manifesto.filialByIdFilialOrigem.sgFilial') == "") {
		verificaExistenciaControleCarga();
	}
	return r;
}

function manifestoNrManifestoOrigem_OnChange() {
	var r = manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
	if (r == true && getElementValue("manifesto.manifestoViagemNacional.nrManifestoOrigem") == "") {
		verificaExistenciaControleCarga();
	}
	setElementValue('manifesto.nrNumero', getElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem'));
	
	return r;
}

function verificaExistenciaControleCarga() {
	resetValue('manifesto.idManifesto');
	resetValue('controleCarga.idControleCarga');
	resetValue('controleCarga.filialByIdFilialOrigem.idFilial');
	if (getElementValue('blExigeDocServico') == "false") 
		desabilitaControleCarga(false);
}

/**
 * Busca os dados relacionados ao manifesto.
 */
function buscarManifesto(manifesto) {
	var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findManifestoByRNC", "resultado_buscarManifesto", {idManifesto:manifesto.idManifesto});
    xmit({serviceDataObjects:[sdo]});
    if(manifesto.nrPreManifesto){
    	setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getElementValue('manifesto.nrNumero'));
	    manifesto_manifestoViagemNacional_nrManifestoOrigemOnChangeHandler();
}
}

/**
 * Povoa os campos com os dados retornados da busca em manifesto
 */
function resultado_buscarManifesto_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (data != undefined) {
		var idFilialUsuario = getNestedBeanPropertyValue(dataUsuario, "filial.idFilial");
		var idFilialOrigem = getNestedBeanPropertyValue(data,"0:filialByIdFilialDestino.idFilial");
		var idFilialDestino = getNestedBeanPropertyValue(data,"0:filialByIdFilialOrigem.idFilial");
		var tpEmpresaFilialDestino = getNestedBeanPropertyValue(data,"0:filialByIdFilialDestino.empresa.tpEmpresa.value");

		if (tpEmpresaFilialDestino=="P"){
			if(idFilialUsuario != idFilialOrigem){
				alert(lms_12004);
				resetValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional');
				setFocus(document.getElementById('manifesto.manifestoViagemNacional.nrManifestoOrigem'));
				return false;
			}
		} else {
			if (idFilialUsuario != idFilialOrigem && idFilialUsuario != idFilialDestino) {
				alert(lms_12004);
				//resetValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional');
				setFocus(document.getElementById('manifesto.manifestoViagemNacional.nrManifestoOrigem'));
				return false;
			}
		}
		setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.idControleCarga"));
		setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data,"0:controleCarga.nrControleCarga"));
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data,"0:controleCarga.filialByIdFilialOrigem.sgFilial"));

		setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdTransportado.nrFrota"));
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdTransportado.nrIdentificador"));

		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdSemiRebocado.nrFrota"));
		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador', getNestedBeanPropertyValue(data,"0:controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"));

		format(document.getElementById("controleCarga.nrControleCarga"));
		desabilitaControleCarga(true);
	}
}

function popupManifestoNrManifestoOrigem(data) {
	callBackManifesto();
}

/**
 * Quando o "Manifesto" for informado
 */
function retornoManifesto_cb(data) {
	var r = manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
	if (r == true) {
		callBackManifesto(data[0]);
	}
	return false;
}

function callBackManifesto(manifesto) {
	if(!manifesto){
		manifesto ={idManifesto:getElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional')}; 
	}

	if(manifesto.idManifesto == undefined){
		manifesto.idManifesto = getElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional');
	}
	
	setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', manifesto.idManifesto);
	setElementValue('manifesto.idManifesto', manifesto.idManifesto);
	setElementValue('blManifestoPreenchidoManualmente', "true");
	buscarManifesto(manifesto);
}

/**
 * Busca os dados relacionados ao Manifesto/Controle de cargas
 */
function buscarManifestoControleCargas(idDoctoServico) {
	if (idDoctoServico != undefined && idDoctoServico != "") {
		if(getElementValue('motivoAberturaNc.idMotivoAberturaNc') != "24"){
		limparCamposRelacionadosManifesto();
		var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findManifestoComControleCargas", 
				"resultado_buscarManifestoControleCargas", {idDoctoServico:idDoctoServico});
	    xmit({serviceDataObjects:[sdo]});
		}else{
			desabilitaTagManifesto(false);
			desabilitaControleCarga(false);
	}
}
}

/**
 * Povoa os campos com os dados retornados da busca em Manifesto/Controle de cargas
 */
function resultado_buscarManifestoControleCargas_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}

	if (data != undefined) {
		resetValue('blManifestoPreenchidoManualmente');
		setElementValue('controleCarga.idControleCarga', getNestedBeanPropertyValue(data,"idControleCarga"));
		setElementValue('controleCarga.nrControleCarga', getNestedBeanPropertyValue(data,"nrControleCarga")); 
		setElementValue('controleCarga.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data,"sgFilialControleCarga")); 

		setElementValue('controleCarga.meioTransporteByIdTransportado.nrFrota', getNestedBeanPropertyValue(data,"veiculoFrota"));
		setElementValue('controleCarga.meioTransporteByIdTransportado.nrIdentificador', getNestedBeanPropertyValue(data,"veiculoPlaca"));

		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrFrota', getNestedBeanPropertyValue(data,"semiReboqueFrota"));
		setElementValue('controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador', getNestedBeanPropertyValue(data,"semiReboquePlaca"));
		
		desabilitaTagManifesto(getNestedBeanPropertyValue(data,"manifestoCount") == 0);
		
		if (getNestedBeanPropertyValue(data,"idManifesto") == undefined || getNestedBeanPropertyValue(data,"idManifesto") == '') {
			desabilitaTagManifesto(true);
			desabilitaControleCarga(false);
		}
		else {
			setElementValue('manifesto.idManifesto', getNestedBeanPropertyValue(data,"idManifesto"));
			setElementValue('manifesto.filialByIdFilialOrigem.sgFilial', getNestedBeanPropertyValue(data,"sgFilialManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional', getNestedBeanPropertyValue(data,"idManifesto"));
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem', getNestedBeanPropertyValue(data,"nrManifesto"));
			setElementValue('manifesto.tpManifesto', getNestedBeanPropertyValue(data,"tpManifesto"));
			if (getElementValue('blExigeDocServico') == "true") {
				desabilitaControleCarga(true);
			}
			else {
				desabilitaControleCarga(false);
			}
			desabilitaTagManifesto(true);
		}
		format(document.getElementById("controleCarga.nrControleCarga"));
		format(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	}
}

/************************* FIM - MANIFESTO *************************/

-->
</script>

