<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/municipios/manterFornecedores">
		<adsm:complement labelWidth="18%" width="82%" label="identificacao" required="true" >
                  <adsm:combobox property="identificacao" width="15%" optionLabelProperty="a" optionProperty="0" service="" prototypeValue="CPF/CNPJ|CUIT/DNI|RUT|RUC" />
                  <adsm:textbox dataType="text" property="numeroIdentificacao" width="80%" size="75%"/>
        </adsm:complement>

		<adsm:textbox dataType="text" property="pessoa.Nome" label="razaoSocial" maxLength="50" size="50" labelWidth="18%" width="60%" required="true" />
		<adsm:textbox dataType="text" property="homepage" label="homepage" maxLength="120" size="25" labelWidth="18%" width="32%" />
		<adsm:textbox dataType="text" property="pessoa.email" label="email" maxLength="50" size="25" />
		<adsm:combobox property="situacao" label="situacao" service="" optionLabelProperty="" optionProperty="" prototypeValue="Ativo|Inativo" labelWidth="18%" width="60%" />
	<adsm:buttonBar>
			<adsm:button caption="enderecos" action="configuracoes/manterEnderecoPessoa" cmd="main"/>
			<adsm:button caption="contatos" action="configuracoes/manterContatos" cmd="main"/>
			<adsm:button caption="dadosBancarios" action="configuracoes/manterDadosBancariosPessoa" boxWidth="100" cmd="main"/>
			<adsm:button caption="inscricoesEstaduais" action="configuracoes/manterInscricoesEstaduais" boxWidth="125" cmd="main"/>
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   