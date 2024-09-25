<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.consultarAgendamentosAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/entrega/consultarAgendamentos" idProperty="idAgendamentoDoctoServico" service="lms.entrega.consultarAgendamentosAction.findByIdCustom" height="390" onDataLoadCallBack="formLoad">

		<adsm:hidden property="idDoctoServico" />
		<adsm:hidden property="tpDocumentoServicoValue" />
		<adsm:hidden property="idAgendamentoEntrega" />

		<adsm:textbox dataType="text" label="filialAgendamento" size="5" property="filialAgendamento.sgFilial" maxLength="3" disabled="true" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="text"  property="filialAgendamento.pessoa.nmFantasia" size="22" maxLength="50" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="text" label="filialDestino" size="5" property="filialDestino.sgFilial" maxLength="3" disabled="true" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="text"  property="filialDestino.pessoa.nmFantasia" size="22" maxLength="50" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="text" label="documentoServico" property="tpDocumentoServico" maxLength="5" size="5" disabled="true" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="text" property="sgFilialOrigem" size="3" maxLength="3" disabled="true" />
			<adsm:textbox dataType="integer" property="nrDoctoServico" mask="00000000" size="10" maxLength="10" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="integer" mask="000000" label="notaFiscal" size="10" property="nrNotaFiscal" maxLength="30" disabled="true" labelWidth="20%" width="30%" />

		<adsm:textbox dataType="text" label="controleCargas" property="sgFilialControleCarga" size="3" maxLength="3" disabled="true" labelWidth="20%" width="30%">
			<adsm:textbox dataType="integer" mask="00000000" size="10" property="controleCarga" maxLength="10" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" label="manifestoEntrega"  property="sgFilialManifestoEntrega" size="3" maxLength="3" disabled="true" labelWidth="20%" width="30%">
			<adsm:textbox dataType="integer" mask="00000000" size="16" property="manifestoEntrega" maxLength="16" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" label="tipoServico" size="40" property="servico.dsServico" maxLength="60" disabled="true" labelWidth="20%" width="30%" />

		<adsm:textbox dataType="text" label="remetente" property="remetente.pessoa.nrIdentificacaoFormatado" labelWidth="20%" maxLength="20" size="20" width="80%" disabled="true" >	
			<adsm:textbox dataType="text" property="remetente.pessoa.nmPessoa" size="58" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" label="destinatario" property="destinatario.pessoa.nrIdentificacaoFormatado" labelWidth="20%" maxLength="20" size="20" width="80%" disabled="true">
			<adsm:textbox dataType="text" property="destinatario.pessoa.nmPessoa" size="58" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox dataType="JTDate" label="dpe" property="DPE" maxLength="50" disabled="true" labelWidth="20%" width="30%"  />	
		
		<adsm:section caption="agendamento"/>
		<adsm:textbox dataType="text" label="dataHoraContato" property="dhContato" size="24" disabled="true" labelWidth="20%" width="30%"  />
		<adsm:textbox dataType="text" label="contato" property="nmContato" maxLength="50" disabled="true" labelWidth="20%" width="30%"  />	
		<adsm:textbox dataType="text" label="telefone" property="nrTelefone" maxLength="50" disabled="true" labelWidth="20%" width="30%"  />	
		<adsm:textbox dataType="text" label="ramal" property="nrRamal" maxLength="50" disabled="true" labelWidth="20%" width="30%"  />	
		<adsm:textbox dataType="text" label="agendadoPor" property="usuarioByIdUsuarioCriacao.nmUsuario" size="35" maxLength="60" disabled="true" labelWidth="20%" width="30%"  />	
		<adsm:textbox dataType="JTDate" label="dataAgendamento" property="dtAgendamento" maxLength="50" disabled="true" labelWidth="20%" width="30%"  />
		<adsm:textbox dataType="text" label="diaAgendamento" property="diaAgendamento" maxLength="50" disabled="true" labelWidth="20%" width="30%"  />	
		<adsm:textbox dataType="text" label="turno" property="turno.dsTurno" maxLength="50" disabled="true" labelWidth="20%" width="30%"  />	
		<adsm:range label="preferencia" labelWidth="20%" width="30%" >
			<adsm:textbox dataType="JTTime" property="hrPreferenciaInicial" style="width:51px" disabled="true"/>
			<adsm:textbox dataType="JTTime" property="hrPreferenciaFinal" style="width:51px" disabled="true"/>
		</adsm:range>
		<adsm:checkbox label="cartaoCredito" property="blCartao" disabled="true" labelWidth="20%" width="30%" />
		
		<adsm:textbox dataType="text" label="situacao" property="tpSituacaoAgendamento" maxLength="50" disabled="true" labelWidth="20%" width="80%"  />	
		
		<adsm:textarea label="observacao" property="obAgendamentoEntrega" maxLength="50" style="width:510px" disabled="true" required="false" labelWidth="20%" width="80%" />
		<adsm:section caption="entrega"/>
			<adsm:textbox dataType="text" label="dataHora" property="dataHora" disabled="true" labelWidth="20%" width="80%" size="24" />
		<adsm:buttonBar>
			<adsm:button disabled="false" id="informacoesDocumentoServicoButton" caption="informacoesDocumentoServico" action="/sim/consultarLocalizacoesMercadorias" cmd="main">
				<adsm:linkProperty src="nrDoctoServico" target="doctoServico.nrDoctoServico" />
				<adsm:linkProperty src="tpDocumentoServicoValue" target="doctoServico.tpDocumentoServico" />
				<adsm:linkProperty src="sgFilialOrigem" target="doctoServico.filialByIdFilialOrigem.sgFilial" />
                <adsm:linkProperty src="idDoctoServico" target="idDoctoServicoReembolsado"/>
			</adsm:button>			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>  

<script>

		function pageLoad_cb(data) {
			onPageLoad_cb(data);			
		}

		function formLoad_cb(data) {
			onDataLoad_cb(data);
			var tabGroup = getTabGroup(this.document);
			var tabDoc = tabGroup.getTab("doc");
			if (tabDoc != null) {
				tabDoc.setDisabled(false);
			}
		}

		function initWindow(eventObj) { 
			setDisabled("informacoesDocumentoServicoButton",false);
			if (getElementValue("idAgendamentoEntrega") == "") {
				var tabGroup = getTabGroup(this.document);
				var tabDoc = tabGroup.getTab("doc");
				if (tabDoc != null) {
					tabDoc.setDisabled(true);
				}
			}
		}
		
</script>

