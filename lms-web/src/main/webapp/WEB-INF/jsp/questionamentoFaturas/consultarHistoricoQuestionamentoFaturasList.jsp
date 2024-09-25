<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.questionamentoFaturas.manterHistoricoQuestionamentoFaturasAction" onPageLoadCallBack="findDadosQuestionamentoFatura" >
	<adsm:form action="/questionamentoFaturas/consultarHistoricoQuestionamentoFaturas">
		<adsm:hidden property="idQuestionamentoFatura"/>
		
		<adsm:section caption="historicoQuestionamentoFaturas"/>
				
		<adsm:combobox label="tipoDocumento" property="tpDocumento" domain="DM_TIPO_DOC_QUEST_FATURAS"
				renderOptions="true" labelWidth="18%" width="37%" disabled="true" serializable="false">
			<adsm:textbox property="unidSigla" dataType="text" size="3" disabled="true" serializable="false" />
			<adsm:textbox property="numero" dataType="integer" size="10" maxLength="10" mask="0000000000" disabled="true" serializable="false" />
		</adsm:combobox>

	</adsm:form>
	<adsm:grid  selectionMode="none" idProperty="idHistoricoQuestionamentoFatura" property="historicoQuestionamentoFaturas" gridHeight="200" unique="true" onRowClick="rowClick"
			service="lms.questionamentoFaturas.manterHistoricoQuestionamentoFaturasAction.findPaginatedHistoricoQuestionamentoFaturas"
			rowCountService="lms.questionamentoFaturas.manterHistoricoQuestionamentoFaturasAction.getRowCount"
			defaultOrder="dhHistorico:desc">
		<adsm:gridColumn title="descricao" property="tpHistorico" isDomain="true" />
		<adsm:gridColumn title="usuario" property="nmUsuario" width="17%" />
		<adsm:gridColumn title="dataHora" property="dhHistorico" width="17%" dataType="JTDateTimeZone"/>
		<adsm:gridColumn title="observacao" property="obHistorico" width="36%" />
	</adsm:grid>
	<adsm:buttonBar>
		<adsm:button caption="fechar" onclick="javascript:window.close();" disabled="false"/>
	</adsm:buttonBar>
</adsm:window>
<script>
	function rowClick(){
		return false;
	}

	function findDadosQuestionamentoFatura_cb() {
		onPageLoad_cb();
		var idQuestionamentoFatura = getElementValue("idQuestionamentoFatura");
		var sdo = createServiceDataObject("lms.questionamentoFaturas.manterHistoricoQuestionamentoFaturasAction.findQuestionamentoFaturaById", "dadosQuestionamentoFatura", {idQuestionamentoFatura:idQuestionamentoFatura});
		xmit({serviceDataObjects:[sdo]});
	}

	function dadosQuestionamentoFatura_cb(data, error) {
		if (error) {
			alert(error);
			return false;
		}
		if (data) {
			setElementValue("tpDocumento", getNestedBeanPropertyValue(data, "tpDocumento"));
			setElementValue("unidSigla", getNestedBeanPropertyValue(data, "unidSigla"));
			setElementValue("numero", getNestedBeanPropertyValue(data, "numero"));
			setElementValue("numero", im_mask(document.getElementById("numero")));
		}
		findHistoricoQuestionamentoFaturas();
	}

	function findHistoricoQuestionamentoFaturas() {
		var filtro = new Array();
		// Monta um map com o campo para sser realizado o filtro
		setNestedBeanPropertyValue(filtro, "idQuestionamentoFatura", getElementValue("idQuestionamentoFatura"));
		//chama a pesquisa da grid 
		historicoQuestionamentoFaturasGridDef.executeSearch(filtro);
	}

</script>
