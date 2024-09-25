<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.pendencia.consultarMDAAction">
	<adsm:form action="/pendencia/consultarMDA" idProperty="idDoctoServico" height="370" onDataLoadCallBack="verificaStatus">

		<adsm:hidden property="origem" value="consultarMda"/>

		<adsm:hidden property="siglaNrDoctoServico"/>
		<adsm:hidden property="filialByIdFilialOrigem.idFilial"/>
		<adsm:textbox property="filialByIdFilialOrigem.sgFilial"
					  label="mda" dataType="text" labelWidth="19%" width="20%"
					  size="5" maxLength="3" disabled="true">
			<adsm:textbox property="nrDoctoServico" dataType="integer" mask="00000000"
						  size="10" maxLength="10" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="localizacaoMercadoria.dsLocalizacaoMercadoria" label="localizacao" 
					  dataType="text" size="60" maxLength="60" labelWidth="10%" width="51%" disabled="true"/>
		
		<adsm:textbox property="tpStatusMda.description" label="status" dataType="text" size="19" labelWidth="19%" width="81%" disabled="true"/>
		<adsm:hidden property="tpStatusMda.value" />
					  		
		<adsm:textbox property="clienteByIdClienteConsignatario.pessoa.nrIdentificacaoFormatado"
					  label="consignatario" dataType="text" labelWidth="19%" width="81%" 
					  size="19" maxLength="18" disabled="true">
			<adsm:textbox property="clienteByIdClienteConsignatario.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox property="moeda1.siglaSimbolo" label="valorFrete" labelWidth="19%" 
					  width="81%" dataType="text" size="8" maxLength="8" disabled="true">
			<adsm:textbox property="vlTotalDocServico" dataType="currency" 
					 	  mask="#,###,###,###,###,##0.00" size="16" disabled="true" />
		</adsm:textbox>

		<adsm:textbox property="moeda2.siglaSimbolo" label="valorMercadoria" labelWidth="19%"
					  width="81%" dataType="text" size="8" maxLength="8" disabled="true">
			<adsm:textbox property="vlMercadoria" dataType="currency" 
					 	  mask="#,###,###,###,###,##0.00" size="16" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox property="dataEntrega" label="dataEntrega" dataType="JTDateTimeZone" picker="false"
					  labelWidth="19%" width="81%" disabled="true"/>

		<adsm:textarea property="obMda" label="observacoes" maxLength="200" columns="80" 
					   rows="3" labelWidth="19%" width="81%" disabled="true"/>

		<adsm:label key="espacoBranco" width="100%" style="border:none"/>
		<adsm:section caption="remetente" />

		<adsm:textbox property="tpRemetenteMda" label="tipoRemetente" labelWidth="19%" width="81%" 
					  dataType="text" size="8" maxLength="8" disabled="true"/>

		<adsm:textbox property="clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado"
					  label="remetente" dataType="text" labelWidth="19%" width="81%" 
					  size="20" maxLength="20" disabled="true">
			<adsm:textbox property="clienteByIdClienteRemetente.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:textarea property="enderecoRemetente.enderecoCompleto" label="endereco" maxLength="300" 
					   columns="90" rows="3" labelWidth="19%" width="81%" disabled="true" />

		<adsm:textbox property="enderecoRemetente.municipio.nmMunicipio" label="municipio" dataType="text" 
					  size="35" maxLength="50" labelWidth="19%" width="30%" disabled="true"/>					  
		<adsm:textbox label="uf" property="enderecoRemetente.municipio.unidadeFederativa.sgUnidadeFederativa" 
					  dataType="text" size="3" labelWidth="10%" disabled="true" />	

		<adsm:label key="espacoBranco" width="100%" style="border:none"/>
		<adsm:section caption="destinatario" />		

		<adsm:textbox property="tpDestinatarioMda" label="tipoDestinatario" labelWidth="19%" width="81%" 
					   dataType="text" size="8" maxLength="8" disabled="true"/>

		<adsm:textbox property="filialByIdFilialDestino.sgFilial"
					  label="filialDestino" dataType="text" labelWidth="19%" width="81%"
					  size="5" maxLength="3" disabled="true">
			<adsm:textbox property="filialByIdFilialDestino.pessoa.nmFantasia" 
						  dataType="text" size="50" maxLength="50" disabled="true" />
		</adsm:textbox>

		<adsm:textbox property="setor.dsSetor" label="setorDestino" dataType="text" 
					  size="35" maxLength="60" labelWidth="19%" width="81%" disabled="true" />	

		<adsm:textbox property="usuarioByIdUsuarioDestino.nrMatricula"
					  label="funcionarioDestinatario" dataType="text" labelWidth="19%" width="81%" 
					  size="16" maxLength="16" disabled="true">
			<adsm:textbox property="usuarioByIdUsuarioDestino.nmUsuario" 
						  dataType="text" size="50" maxLength="50" disabled="true" />
		</adsm:textbox>

		<adsm:textbox property="clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado"
					  label="destinatario" dataType="text" labelWidth="19%" width="81%" 
					  size="20" maxLength="20" disabled="true">
			<adsm:textbox property="clienteByIdClienteDestinatario.pessoa.nmPessoa" 
						  dataType="text" size="50" maxLength="50" disabled="true" />
		</adsm:textbox>

		<adsm:textarea property="enderecoDestinatario.enderecoCompleto" label="endereco" maxLength="300" 
					   columns="90" rows="3" labelWidth="19%" width="81%" disabled="true" />

		<adsm:textbox property="enderecoDestinatario.municipio.nmMunicipio" label="municipio" dataType="text" 
					  size="35" maxLength="50" labelWidth="19%" width="30%" disabled="true"/>					  
		<adsm:textbox label="uf" property="enderecoDestinatario.municipio.unidadeFederativa.sgUnidadeFederativa"
					  dataType="text" size="3" labelWidth="10%" disabled="true" />	

		<adsm:buttonBar>
			<adsm:button caption="emitirMda" id="emitir" action="/pendencia/emitirMDA" cmd="main">
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="idDoctoServico" target="idDoctoServico" />
				<adsm:linkProperty src="nrDoctoServico" target="nrDoctoServico" />
				<adsm:linkProperty src="origem" target="origem"/>
			</adsm:button>
			<adsm:button caption="registrarRecebimentoMDA" id="registrar" action="/pendencia/registrarRecebimentoMDA" cmd="main">
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="filialByIdFilialOrigem.idFilial"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="filialByIdFilialOrigem.sgFilial"/>
				<adsm:linkProperty src="idDoctoServico" target="idDoctoServico"/>
				<adsm:linkProperty src="nrDoctoServico" target="nrDoctoServico"/>
				<adsm:linkProperty src="origem" target="origem"/>
			</adsm:button>
			
			<adsm:button caption="cancelarMDA" id="cancelar" action="/pendencia/cancelarMDA" cmd="main">
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="filialByIdFilialOrigem.idFilial" disabled="false"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="filialByIdFilialOrigem.sgFilial" disabled="false"/>
				<adsm:linkProperty src="idDoctoServico" target="idDoctoServico" disabled="false"/>
				<adsm:linkProperty src="nrDoctoServico" target="nrDoctoServico" />
				<adsm:linkProperty src="origem" target="origem"/>
			</adsm:button>						
			
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script type="text/javascript">

	/**
	 * Função chamada quando a tela é inicializada
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click"){
			// Se MDA está cancelado
			if (getElementValue("tpStatusMda.value")=="C"){
				desabilitaBotoes(true);
			} else {
				desabilitaBotoes(false);
			}
		}
	}
	
	function verificaStatus_cb(data, error){
		onDataLoad_cb(data, error);
		if (error){
			alert(error);
			return false;
		}
		// Se MDA está cancelado
		if (data.tpStatusMda.value=="C"){
			desabilitaBotoes(true);
		} else {
			desabilitaBotoes(false);
		}
	}
	
	function desabilitaBotoes(bool){
		setDisabled("emitir", bool);
		setDisabled("registrar", bool);
		setDisabled("cancelar", bool);		
	}
	
</script>