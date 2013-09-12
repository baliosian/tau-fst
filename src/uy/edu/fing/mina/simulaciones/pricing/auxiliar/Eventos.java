package uy.edu.fing.mina.simulaciones.pricing.auxiliar;

import java.util.HashMap;
import java.util.Map;

import uy.edu.fing.mina.lupa.tf.EventTf;

public class Eventos {
	private Map<String, EventTf> conjuntoEventos;
	private static Eventos instancia;
	
	private Eventos(){
		super();
		this.conjuntoEventos = new HashMap<String,EventTf>();
		this.popular();
	}
	
	public static Eventos getInstance(){
		if(instancia == null){
			instancia = new Eventos();
		}
		return instancia;
	}
	
	private void popular(){
		EventTf nuevo = new EventTf();
		nuevo.setName("localPriceInformation");
		
//		Just for demonstration.
//		One subscription to local price:
		
		nuevo.addInitNotifString("{target_service=\"/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"localPrice\", watcher_id=\"localPrice\", timeout=\"10\"}");
//		Add 6 subscription per competitor:
//		- price value
		nuevo.addInitNotifString("{target_service=\"COMP0/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"COMP0-Price\", watcher_id=\"COMP0Price\", timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"COMP1/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"COMP1-Price\", watcher_id=\"COMP1Price\", timeout=\"10\"}");
		
//		- price delta_e steady
//		- price delta_e is
//		- price delta_e if
//		- price delta_e ds
//		- price delta_e df
		
//		Subscriptions to remote deltas are disabled. We pretend to calculate deltas locally at the arrival of the localPrice event.
//		For such we keep in the shared table the last timestamp as well as the last price.
//		nuevo.addInitString("{target_service=\"COMP0/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\">\", delta_e=\"0.5\", notification_id=\"COMP0-PriceIS\", watcher_id=\"stateLocalCOMP0IS\"}");
//		nuevo.addInitString("{target_service=\"COMP0/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\">\", delta_e=\"2\", notification_id=\"COMP0-PriceIF\", watcher_id=\"stateLocalCOMP0IF\"}");
//		nuevo.addInitString("{target_service=\"COMP0/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\"<\", delta_e=\"-0.5\", notification_id=\"COMP0-PriceDS\", watcher_id=\"stateLocalCOMP0DS\"}");
//		nuevo.addInitString("{target_service=\"COMP0/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\"<\", delta_e=\"-1\", notification_id=\"COMP0-PriceIS\", watcher_id=\"stateLocalCOMP0DF\"}");
//		nuevo.addInitString("{target_service=\"COMP0/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\"=\", delta_e=\"0\", notification_id=\"COMP0-PriceDS\", watcher_id=\"stateLocalCOMP0ST\"}");
//		
//		nuevo.addInitString("{target_service=\"COMP1/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\">\", delta_e=\"0.5\", notification_id=\"COMP1-PriceIS\", watcher_id=\"stateLocalCOMP1IS\"}");
//		nuevo.addInitString("{target_service=\"COMP1/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\">\", delta_e=\"2\", notification_id=\"COMP1-PriceIF\", watcher_id=\"stateLocalCOMP1IF\"}");
//		nuevo.addInitString("{target_service=\"COMP1/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\"<\", delta_e=\"-0.5\", notification_id=\"COMP1-PriceDS\", watcher_id=\"stateLocalCOMP1DS\"}");
//		nuevo.addInitString("{target_service=\"COMP1/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\"<\", delta_e=\"-1\", notification_id=\"COMP1-PriceIS\", watcher_id=\"stateLocalCOMP1DF\"}");
//		nuevo.addInitString("{target_service=\"COMP1/lupa/rmoon\", target_host=\"127.0.0.1\", command=\"watch_mib\", mib=\"price\", op=\"=\", delta_e=\"0\", notification_id=\"COMP1-PriceDS\", watcher_id=\"stateLocalCOMP1ST\"}");
		
		conjuntoEventos.put(nuevo.getName(), nuevo);
		
		
		
	}
	
	public EventTf getEvento(String name){
		return conjuntoEventos.get(name);
	}
}
