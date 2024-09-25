<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	service="lms.expedicao.manterCRTServicosAdicionaisAction">
	<adsm:form
		action="/expedicao/manterCRTServicosAdicionais">

		<adsm:section
			caption="servicosAdicionaisTitulo"
			width="88"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:combobox
			property="servicoAdicional"
			label="servicoAdicional"
			optionLabelProperty="label"
			optionProperty="1"
			service=""
			width="73%"
			labelWidth="26%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />

		<adsm:textbox
			dataType="integer"
			property="quantidadeDias"
			label="qtdeDias"
			size="10"
			labelWidth="26%"
			width="19%"/>

		<adsm:textbox
			dataType="integer"
			property="quantidadeColetas"
			label="quantidadeColetas"
			size="10"
			labelWidth="21%"
			width="33%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textbox
			dataType="decimal"
			property="quilometragemRodada"
			label="quilometragemRodada"
			size="10"
			labelWidth="26%"
			width="19%"/>

		<adsm:textbox
			dataType="integer"
			property="quantidadePaletes"
			label="quantidadePaletes"
			size="10"
			labelWidth="21%"
			width="33%"/>

		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>

		<adsm:textbox
			dataType="integer"
			property="quantidadeSegurancasAdicionais"
			label="quantidadeSegurancasAdicionais"
			size="10"
			labelWidth="26%"
			width="19%"/>

		<adsm:buttonBar
			freeLayout="true">
			<adsm:button
				id="btnNovo"
				caption="novo"/>
			<adsm:button
				id="btnSalvar"
				caption="salvar"/>
			<adsm:button
				id="btnFechar"
				caption="fechar"/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid
		property="servicoAdicional"
		idProperty="idServicoAdicional"
		gridHeight="115"
		unique="true"
		showPaging="true">

		<adsm:gridColumn
			title="servicoSelecionado"
			property="dsServicoAdicional"
			width="100%"/>

		<adsm:buttonBar>
			<adsm:button
				id="btnExcluir"
				caption="excluir"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	
</script>