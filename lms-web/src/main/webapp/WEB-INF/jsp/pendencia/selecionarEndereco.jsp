<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="selecionarEnderecos">
	<adsm:form action="/pendencia/abrirMDA">
		<adsm:grid idProperty="id" property="id" selectionMode="none" rows="5" gridHeight="124" unique="false" title="enderecos" scrollBars="horizontal">
			<adsm:gridColumn title="endereco" property="endereco" width="250" />
			<adsm:gridColumn title="numero" property="numero" width="80" align="right" />
			<adsm:gridColumn title="complemento" property="complemento"	width="100" />
			<adsm:gridColumn title="bairro" property="bairro" width="130" />
			<adsm:gridColumn title="cep" property="cep" width="80" align="right" />
			<adsm:gridColumn title="municipio" property="municipio" width="180" />
			<adsm:gridColumn title="uf" property="uf" width="50" />
			<adsm:gridColumn title="alterar" property="alterar" width="50" image="popup.gif" link="pendencia/abrirMDA.do?cmd=cadastrarEndereco" popupDimension="770,270" />
			<adsm:buttonBar>
				<adsm:button caption="novo"	onclick="showModalDialog('pendencia/abrirMDA.do?cmd=cadastrarEndereco',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:270px;');" />
				<adsm:button caption="fechar" />
			</adsm:buttonBar>

		</adsm:grid>

	</adsm:form>
</adsm:window>