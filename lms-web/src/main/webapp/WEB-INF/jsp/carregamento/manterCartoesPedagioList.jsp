<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterCartoesPedagioAction">
	<adsm:form action="/carregamento/manterCartoesPedagio" idProperty="idCartaoPedagio">
		<adsm:hidden property="tpIdentificacao" serializable="false" />
		
		<adsm:lookup idProperty="idOperadoraCartaoPedagio" 
					 label="operadoraCartaoPedagio" dataType="text" property="operadoraCartaoPedagio" 
					 criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" 
					 service="lms.carregamento.manterCartoesPedagioAction.findLookupOperadoraCartaoPedagio" 
					 action="/carregamento/manterOperadorasCartaoPedagio" size="18" maxLength="18" labelWidth="24%" width="76%">
        	<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="operadoraCartaoPedagio.pessoa.nmPessoa" />
        	<adsm:propertyMapping modelProperty="pessoa.tpIdentificacao.value" relatedProperty="tpIdentificacao" />
        	<adsm:propertyMapping modelProperty="nrIdentificacao" criteriaProperty="operadoraCartaoPedagio.pessoa.nrIdentificacao" inlineQuery="false"/>
        	<adsm:propertyMapping modelProperty="tpIdentificacao" criteriaProperty="tpIdentificacao" inlineQuery="false"/>

            <adsm:textbox dataType="text" property="operadoraCartaoPedagio.pessoa.nmPessoa" size="40" maxLength="50" disabled="true"/>
        </adsm:lookup>

		<adsm:textbox dataType="integer" property="nrCartao" label="numeroCartaoPedagio" maxLength="16" size="18" labelWidth="24%" width="76%"/>
		<adsm:textbox dataType="JTDate" property="dtValidade" label="validade" labelWidth="24%" width="76%" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="manterCartoesPedagio"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid property="manterCartoesPedagio" idProperty="idCartaoPedagio" selectionMode="check" gridHeight="200" unique="true" rows="12"
			   defaultOrder="operadoraCartaoPedagio_pessoa_.nmPessoa:asc,nrCartao:asc" >
		<adsm:gridColumn title="operadoraCartaoPedagio" property="operadoraCartaoPedagio.pessoa.nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="" 						property="operadoraCartaoPedagio.pessoa.nmPessoa" width="300" />
		<adsm:gridColumn title="numeroCartaoPedagio" 	property="nrCartao" align="right" />
		<adsm:gridColumn title="validade" 				property="dtValidade" dataType="JTDate" width="15%" align="center" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
