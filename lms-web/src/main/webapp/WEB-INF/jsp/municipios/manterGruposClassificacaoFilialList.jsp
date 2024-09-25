<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
//função chamada pelo proprio combo quando o onchange da combo "grupo classificação" é chamado"
//comportamento:com essa função a combo é populada(divisaoGrupoClassificacao_idDivisaoGrupoClassificacao_cb(data)) 
//e depois é setado o valor do idDivisaoGrupoClassificacao vindos do linkProperty da tela "Divisão Grupo de classificação"
 function populaDivisaoGrupoClassificacao_cb(data) {
	divisaoGrupoClassificacao_idDivisaoGrupoClassificacao_cb(data);
	if (this.document.getElementById("idDivisaoGrupoClassificacaoTemp").value != ""){
		document.getElementById("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao").value = document.getElementById("idDivisaoGrupoClassificacaoTemp").value;
		document.getElementById("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao").masterLink = "true";
		document.getElementById("divisaoGrupoClassificacao.idDivisaoGrupoClassificacao").disabled = true;
	}
	
}	
  //recebe os parametros vindos do button  - chamada pelo callBack da window	

  function habilitaDesabilitaComboDivisao_cb(dado,erro){
	onPageLoad_cb(dado,erro);
	if(document.getElementById("idDivisaoGrupoClassificacaoTemp").value != null
         || document.getElementById("idDivisaoGrupoClassificacaoTemp").value != '' ){
		
		document.getElementById("divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao").onchange();
	}
 }
</script>
<adsm:window service="lms.municipios.grupoClassificacaoFilialService" onPageLoadCallBack="habilitaDesabilitaComboDivisao">
	  <adsm:form action="/municipios/manterGruposClassificacaoFilial" idProperty="idGrupoClassificacaoFilial">
		
		<adsm:hidden property="idDivisaoGrupoClassificacaoTemp" serializable="false"/>
		<adsm:hidden property="dsDivisaoGrupoClassificacaoTemp" serializable="false"/>

		<adsm:combobox property="divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao" label="grupoClassificacao" boxWidth="220"  service="lms.municipios.grupoClassificacaoService.find" optionLabelProperty="dsGrupoClassificacao" optionProperty="idGrupoClassificacao" labelWidth="20%" width="80%"/>

		<adsm:combobox onDataLoadCallBack="populaDivisaoGrupoClassificacao" boxWidth="220" property="divisaoGrupoClassificacao.idDivisaoGrupoClassificacao" label="divisaoGrupo" service="lms.municipios.divisaoGrupoClassificacaoService.find" optionLabelProperty="dsDivisaoGrupoClassificacao" optionProperty="idDivisaoGrupoClassificacao" required="false" labelWidth="20%" width="80%" >
			<adsm:propertyMapping criteriaProperty="divisaoGrupoClassificacao.grupoClassificacao.idGrupoClassificacao" modelProperty="grupoClassificacao.idGrupoClassificacao"/>
		</adsm:combobox>
		

		<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" property="filial" criteriaProperty="sgFilial" 
					 idProperty="idFilial" label="filial" size="3" maxLength="3" 
					 labelWidth="20%" width="75%" action="/municipios/manterFiliais">
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
	        <adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
	  
		<adsm:range label="vigencia" width="80%" labelWidth="20%" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="grupoClassificacaoFilial"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idGrupoClassificacaoFilial" property="grupoClassificacaoFilial" selectionMode="check" gridHeight="200" unique="true" scrollBars="horizontal" rows="10" defaultOrder="divisaoGrupoClassificacao_grupoClassificacao_.dsGrupoClassificacao,divisaoGrupoClassificacao_.dsDivisaoGrupoClassificacao,filial_.sgFilial,dtVigenciaInicial">
		<adsm:gridColumn title="grupoClassificacao" property="divisaoGrupoClassificacao.grupoClassificacao.dsGrupoClassificacao" width="150" />	
		<adsm:gridColumn title="situacao" property="divisaoGrupoClassificacao.grupoClassificacao.tpSituacao" isDomain="true" width="80" />	
		<adsm:gridColumn title="divisaoGrupo" property="divisaoGrupoClassificacao.dsDivisaoGrupoClassificacao" width="150"  />	
		<adsm:gridColumn title="situacao" property="divisaoGrupoClassificacao.tpSituacao" isDomain="true" width="80"  />	
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="siglaFilial" property="filial.sgFilial" width="50"/>	
			<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="150"/>	
		</adsm:gridColumnGroup>
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="100" dataType="JTDate"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="100" dataType="JTDate"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

</script>