<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/manterContatosRecebemInformacoesEventos">
		<adsm:hidden property="cliente.idCliente" />
		<adsm:hidden property="idConfiguracaoComunicacao" />
		<adsm:hidden property="tpIdentificacao" />
		
		<adsm:textbox dataType="text" property="pessoa.nrIdentificacao"  label="cliente" labelWidth="18%" width="18%" size="23" disabled="true"/>
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" width="64%" size="60" disabled="true"/>
	
		<adsm:combobox property="servico" optionLabelProperty="dsServico" optionProperty="idServico"  labelWidth="18%" width="82%" disabled="true"
			service="lms.configuracoes.servicoService.find" label="servico" boxWidth="300"/>
			
		<adsm:combobox property="meioTransmissao"  domain="DM_MEIO_COMUNICACAO" label="meioTransmissao" labelWidth="18%" width="82%"  disabled="true"/>
	
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="contatoComunicacao"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid 	idProperty="idContatoComunicacao"  property="contatoComunicacao"
				service="lms.sim.manterContatosRecebemInformacoesEventosAction.findPaginatedCustom"
				rowCountService="lms.sim.manterContatosRecebemInformacoesEventosAction.getRowCount"
				 rows="8">
		<adsm:gridColumn property="contato" title="contato" width="28%" dataType="text"/>
		<adsm:gridColumn property="mail" title="email" width="30%" dataType="text"/>
		<adsm:gridColumn property="telefone" title="telefone" width="18%" />
		<adsm:gridColumn property="vigenciaInicial" title="vigenciaInicial" width="12%"  dataType="JTDate" />
		<adsm:gridColumn property="vigenciaFinal" title="vigenciaFinal" width="12%"  dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
    </adsm:grid>
</adsm:window>

<script>
	document.getElementById("pessoa.nrIdentificacao").masterLink = "true";
	document.getElementById("tpIdentificacao").masterLink = "true";
	document.getElementById("cliente.idCliente").masterLink = "true";
	document.getElementById("idConfiguracaoComunicacao").masterLink = "true";
			
	


</script>
