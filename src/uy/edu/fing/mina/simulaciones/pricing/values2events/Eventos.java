package uy.edu.fing.mina.simulaciones.pricing.values2events;

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

		nuevo.addInitNotifString("{target_service=\"AP0VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP0-Price\" .. math.random(2^30), watcher_id=\"AP0Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP1VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP1-Price\" .. math.random(2^30), watcher_id=\"AP1Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP2VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP2-Price\" .. math.random(2^30), watcher_id=\"AP2Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP3VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP3-Price\" .. math.random(2^30), watcher_id=\"AP3Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP4VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP4-Price\" .. math.random(2^30), watcher_id=\"AP4Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP5VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP5-Price\" .. math.random(2^30), watcher_id=\"AP5Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP6VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP6-Price\" .. math.random(2^30), watcher_id=\"AP6Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP7VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP7-Price\" .. math.random(2^30), watcher_id=\"AP7Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=\"AP8VE/lupa/rmoon\", command=\"watch_mib\", mib=\"price\", op=\">\", value=\"0\", notification_id=\"AP8-Price\" .. math.random(2^30), watcher_id=\"AP8Price\" .. configuration.my_name_pdp, timeout=\"10\"}");
		nuevo.addInitNotifString("{target_service=configuration.my_name_rmoon, command=\"watch_mib\", mib=\"number_of_clients\", op=\">\", value=\"-1\", notification_id=configuration.my_name_rmoon .. \"Clients\" .. math.random(2^30), watcher_id=\"MyClients\" .. configuration.my_name_pdp, timeout=\"10\"}");
		
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
		
		nuevo = new EventTf();
		nuevo.setName("localClientsInformation");
		conjuntoEventos.put(nuevo.getName(), nuevo);
		
	}
	
	public EventTf getEvento(String name){
		return conjuntoEventos.get(name);
	}
}
