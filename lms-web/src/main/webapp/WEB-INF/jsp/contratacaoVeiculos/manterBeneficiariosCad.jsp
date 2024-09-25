<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/contratacaoVeiculos/manterBeneficiarios">
		<adsm:complement width="100%" label="identificacao">
            <adsm:combobox property="identificacao" optionLabelProperty="a" optionProperty="0" service="" prototypeValue="CPF/CNPJ|CUIT/DNI|RUT|RUC" />
            <adsm:textbox dataType="text" property="numeroIdentificacao" size="61"/>
        </adsm:complement>
		<adsm:textbox dataType="text" property="pessoa.nome" label="nome" maxLength="50" required="true" size="80" width="85%"/>
		<adsm:textbox dataType="text" property="pessoa.correioEletronico" label="email" maxLength="50"  size="40" width="35%"/>
		<adsm:combobox property="pessoa.status" label="situacao" prototypeValue="Ativo|Inativo" service="" optionLabelProperty="" optionProperty="" width="35%" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="enderecos" action="configuracoes/manterEnderecoPessoa.do" cmd="main" />
			<adsm:button caption="contatos" action="configuracoes/manterContatos.do" cmd="main" />
			<adsm:button caption="dadosBancarios" action="configuracoes/manterDadosBancariosPessoa.do" cmd="main" />
			<adsm:button caption="inscricoesEstaduais" action="configuracoes/manterInscricoesEstaduais.do" cmd="main" />
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   