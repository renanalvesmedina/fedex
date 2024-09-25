<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterNotasDebitoInternacionaisAction">
	<adsm:form action="/contasReceber/manterNotasDebitoInternacionais"  idProperty="idNotasDebitoInternacional"
			service="lms.contasreceber.manterNotasDebitoInternacionaisAction">

		<adsm:hidden property="desconto.tpSituacaoAprovacao" value="E"/>

		<adsm:combobox property="tpDocumentoServico"
			   label="documentoServico"
			   domain="DM_TIPO_DOCUMENTO_SERVICO">
			   
			<adsm:lookup dataType="text"
						 property="filial"
					 	 idProperty="idFilial" 
					 	 criteriaProperty="sgFilial"
						 service="lms.contasreceber.manterNotasDebitoInternacionaisAction"
						 action="/municipios/manterFiliais"
						 size="3" 
						 maxLength="3" 
						 picker="false" />
			<adsm:textbox property="nrDocumento" dataType="integer" size="10" maxLength="8" mask="00000000"/>
			
		</adsm:combobox>

		<adsm:combobox property="tpSituacaoCobranca"
			   label="documentoServico"
			   domain="DM_STATUS_COBRANCA_DOCTO_SERVICO"/>

		<adsm:lookup property="cliente" idProperty="idCliente" dataType="text"
				service="lms.vendas.clienteService.findLookup" action="/vendas/manterDadosIdentificacao"
				criteriaProperty="pessoa.nrIdentificacao" label="cliente" size="17" maxLength="20" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
		</adsm:lookup>
		<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" width="50%" serializable="false"/>

 		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="notas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idNotasDebitoInternacional" property="notas"
			service="lms.contasreceber.manterNotasDebitoInternacionaisAction.findPaginatedDevedorDocServFat"
			rowCountService="lms.contasreceber.manterNotasDebitoInternacionaisAction.getRowCountDevedorDocServFat">
		<adsm:gridColumn title="documentoServico" property="nrDocumento" width="20%" dataType="text"/>
		<adsm:gridColumn title="clienteDevedor" property="cliente.nmCliente" width="30%" dataType="text"/>
		<adsm:gridColumn title="valorDevido" property="vlDevido" width="15%" dataType="currency"/>
		<adsm:gridColumn title="valorDesconto" property="vlDesconto" width="15%" dataType="currency"/>
		<adsm:gridColumn title="situacaoCobranca" property="tpSituacaoCobranca" width="20%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>