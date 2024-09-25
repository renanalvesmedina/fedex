package com.mercurio.lms.tabelaprecos.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.lms.tabelaprecos.model.ReajusteClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.dao.ReajusteTabelaPrecoDAO;

public class ReajusteParametroClienteDispatcherService {

	private ReajusteParametroClienteService reajusteParametroClienteService;
	private ReajusteTabelaPrecoDAO reajusteTabelaPrecoDAO;
	
	private static final Log LOG = LogFactory.getLog(ReajusteParametroClienteDispatcherService.class);
	private static List<Long> idsProcessados = new ArrayList<Long>();
	private static final int NUM_THREADS = 10;
	
	public Boolean reajustarClientesAutomaticosNovoReajuste() throws InterruptedException, ExecutionException{
		List<Long> tabelasDivisaoClienteAutomaticosParaReajustar = reajusteTabelaPrecoDAO.findTabelasDivisaoClienteAutomaticosParaReajustar();
		
		return reajustarDivisoesClientesAutomaticosNovoReajuste(tabelasDivisaoClienteAutomaticosParaReajustar);
	}
	
	public Boolean reajustarDivisoesClientesAutomaticosNovoReajuste(List<Long> tabelasDivisaoClienteAutomaticosParaReajustar) throws InterruptedException, ExecutionException {
		
		List<Long> idsProcessadosNovoReajuste = new ArrayList<Long>();

		List<List<Long>> listasThreads = new ArrayList<List<Long>>();
		for (int i = 0; i < NUM_THREADS; i++) {
			listasThreads.add(new ArrayList<Long>());
		}

		int i = 0;
		for (Long tabelaDivisao : tabelasDivisaoClienteAutomaticosParaReajustar) {
			if (!idsProcessadosNovoReajuste.contains(tabelaDivisao)) {
				listasThreads.get(i % NUM_THREADS).add(tabelaDivisao);
				idsProcessadosNovoReajuste.add(tabelaDivisao);
				i++;
			}
		}

		ExecutorService threadpool = Executors.newFixedThreadPool(NUM_THREADS);
		
		System.out.println("Processando a tarefa ...");
	    Future<Boolean> futureT0 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(0)));
	    Future<Boolean> futureT1 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(1)));
	    Future<Boolean> futureT2 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(2)));
	    Future<Boolean> futureT3 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(3)));
	    Future<Boolean> futureT4 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(4)));
	    Future<Boolean> futureT5 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(5)));
	    Future<Boolean> futureT6 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(6)));
	    Future<Boolean> futureT7 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(7)));
	    Future<Boolean> futureT8 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(8)));
	    Future<Boolean> futureT9 = threadpool.submit(new ProcessoReajusteTesteNovoReajuste(listasThreads.get(9)));

		while (!futureT0.isDone() || !futureT1.isDone() || !futureT2.isDone()
				|| !futureT3.isDone() || !futureT4.isDone()
				|| !futureT5.isDone() || !futureT6.isDone()
				|| !futureT7.isDone() || !futureT8.isDone()
				|| !futureT9.isDone()) {

//			System.out.println("As tarefas ainda não foram processadas!");
			Thread.sleep(1); // sleep for 1 millisecond
		}
//		System.out.println("Tarefa completa!");

		Boolean reajustouTudo = futureT0.get() && futureT1.get()
				&& futureT2.get() && futureT3.get() && futureT4.get()
				&& futureT5.get() && futureT6.get() && futureT7.get()
				&& futureT8.get() && futureT9.get();
		threadpool.shutdown();

		return reajustouTudo;
	}
	
	
	class ProcessoReajusteTesteNovoReajuste implements Callable<Boolean>{
		List<Long> ids;
		public ProcessoReajusteTesteNovoReajuste(List<Long> ids){
			this.ids = ids;
		}

		@Override
		public Boolean call() throws Exception {
			
			for(Long id : ids){
				ReajusteClienteAutomaticoDTO reajuste = new ReajusteClienteAutomaticoDTO();
				reajuste.setIdTabelaDivisaoCliente(id);
				reajuste.setTipo("A");
				
				try{
					if(!reajusteParametroClienteService.executeReajustarClienteAutomatico(reajuste, true)) {
						return false;
					}
				} catch(Exception e){
					reajusteParametroClienteService.saveHistorico(id);
					LOG.error("Erro em executeReajustarClienteAutomatico, tabela divisao: " + id + " idThread: " + Thread.currentThread().getId(), e);
					return false;
				}
			}
			
			return true;
		}
	}
	
	public Boolean reajustarClientesAutomaticos(){
		List<Long> tabelasDivisaoClienteAutomaticosParaReajustar = reajusteTabelaPrecoDAO.findTabelasDivisaoClienteAutomaticosParaReajustar();
		
		reajustarDivisoesClientesAutomaticos(tabelasDivisaoClienteAutomaticosParaReajustar);
		
		return true;
	}

	public Boolean reajustarDivisoesClientesAutomaticos(List<Long> tabelasDivisaoClienteAutomaticosParaReajustar) {
		Boolean encontrouTabela = false;
		
		List<List<Long>> listasThreads = new ArrayList<List<Long>>();
		for(int i=0; i<NUM_THREADS;i++){
			listasThreads.add(new ArrayList<Long>());
		}
		
		int i=0;
		for(Long tabelaDivisao : tabelasDivisaoClienteAutomaticosParaReajustar){
			if(!isProcessado(tabelaDivisao)){
				listasThreads.get(i % NUM_THREADS).add(tabelaDivisao);
				insertProcessado(tabelaDivisao);
				encontrouTabela = true;
				i++;
			}
		 }
		
		Thread t0 = new Thread(new ProcessoReajuste(listasThreads.get(0)));
		Thread t1 = new Thread(new ProcessoReajuste(listasThreads.get(1)));
		Thread t2 = new Thread(new ProcessoReajuste(listasThreads.get(2)));
		Thread t3 = new Thread(new ProcessoReajuste(listasThreads.get(3)));
		Thread t4 = new Thread(new ProcessoReajuste(listasThreads.get(4)));
		Thread t5 = new Thread(new ProcessoReajuste(listasThreads.get(5)));
		Thread t6 = new Thread(new ProcessoReajuste(listasThreads.get(6)));
		Thread t7 = new Thread(new ProcessoReajuste(listasThreads.get(7)));
		Thread t8 = new Thread(new ProcessoReajuste(listasThreads.get(8)));
		Thread t9 = new Thread(new ProcessoReajuste(listasThreads.get(9)));
		
		t0.start();
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		
		try {
			t0.join();
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
			t9.join();
			
		} catch (InterruptedException e) {
			LOG.error("Thread erro", e); 
		}
		 
		 return encontrouTabela;
	}
	
	
	class ProcessoReajuste implements Runnable{
		List<Long> ids;
		public ProcessoReajuste(List<Long> ids){
			this.ids = ids;
		}

		@Override
		public void run() {
			long tempoInicial = System.currentTimeMillis();
		
			for(Long id : ids){
				ReajusteClienteAutomaticoDTO reajuste = new ReajusteClienteAutomaticoDTO();
				reajuste.setIdTabelaDivisaoCliente(id);
				reajuste.setTipo("A");
				
				try{
					reajusteParametroClienteService.executeReajustarClienteAutomatico(reajuste, false);
				} catch(Exception e){
					reajusteParametroClienteService.saveHistorico(id);
					LOG.error("Erro em executeReajustarClienteAutomatico, tabela divisao: " + id + " idThread: " + Thread.currentThread().getId(), e);
				}
			}
			
			long tempoFinal = System.currentTimeMillis();
		}
	}
	
	private static synchronized void insertProcessado(Long tabelaDivisao) {
		idsProcessados.add(tabelaDivisao);
	}

	private static synchronized boolean isProcessado(Long tabelaDivisao) {
		return idsProcessados.contains(tabelaDivisao);
	}
	
	public void setReajusteTabelaPrecoDAO(ReajusteTabelaPrecoDAO reajusteTabelaPrecoDAO) {
		this.reajusteTabelaPrecoDAO = reajusteTabelaPrecoDAO;
	}
	
	public void setReajusteParametroClienteService(ReajusteParametroClienteService reajusteParametroClienteService) {
		this.reajusteParametroClienteService = reajusteParametroClienteService;
	}
	
}
