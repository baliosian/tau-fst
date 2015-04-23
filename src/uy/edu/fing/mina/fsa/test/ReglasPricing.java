package uy.edu.fing.mina.fsa.test;

import java.util.HashSet;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.SimpleTf;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.lupa.tf.ActionTf;

public class ReglasPricing {
    private static ReglasPricing instancia;
    public Set<Tffst> reglas = new HashSet<Tffst>();
    
    public static ReglasPricing getInstance(){
        if(instancia == null){
            instancia = new ReglasPricing();
        }
        return instancia;
    }

    
    public Set<Tffst> getReglas(){
        return reglas;
    }
    
    public void construirReglas(){
        
        ActionTf epsilon = new ActionTf();
        epsilon.setEpsilon();
        
        State estado1, estado2, estado3;
        Tffst iterador;
        
//      REGLAS DE DEMANDA
//      REGLAS EN CASO DE POCOS CLIENTES
        
//      Regla 1 : if few users and users increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("fu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uif"), new SimpleTf("ipf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 2 : if few users and users increasing slow then increase price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("fu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uis"), new SimpleTf("ips"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 3 : if few users and users steady then decrease price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("fu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("us"), new SimpleTf("dps"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 4 : if few users and users decreasing slow then decrease price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("fu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uds"), new SimpleTf("dps"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 5 : if few users and users decreasing fast then decrease price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("fu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("udf"), new SimpleTf("dpf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      FIN DE LAS REGLAS PARA EL CASO DE POCOS CLIENTES
        
//      REGLAS PARA EL CASO DE UNA CANTIDAD MEDIA DE CLIENTES
        
//      Regla 6 : if mid users and users increasing fast then increase price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("mu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uif"), new SimpleTf("ips"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 7 : if mid users and users increasing slow then keep price
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("mu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uis"), new SimpleTf("kp"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 8 : if mid users and users steady then increase price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("mu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("us"), new SimpleTf("ips"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 9 : if mid users and users decreasing slow then decrease price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("mu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uds"), new SimpleTf("dps"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 10 : if mid users and users decreasing fast then decrease price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("mu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("udf"), new SimpleTf("dpf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      FIN DE REGLAS PARA EL CASO DE CANTIDAD MEDIA DE CLIENTES
        
//      REGLAS PARA EL CASO DE MUCHOS CLIENTES
        
//      Regla 11 : if lots of users and users increasing fast then increase price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("lu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uif"), new SimpleTf("ips"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 12 : if lots of users and users increasing slow then increase price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("lu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uis"), new SimpleTf("ips"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 13 : if lots of users and users steady then keep price
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("lu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("us"), new SimpleTf("kp"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 14 : if lots of users and users decreasing slow then decrease price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("lu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("uds"), new SimpleTf("dps"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 15 : if lots of users and users decreasing fast then decrease price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("lu"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("udf"), new SimpleTf("dpf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      FIN DE REGLAS PARA EL CASO DE MUCHOS CLIENTES
        
//      FIN DE REGLAS PARA LA DEMANDA
        
//      REGLAS PARA LA COMPETENCIA
        
//      REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS ALTOS
        
//      Regla 16 : if competitor price higher and competitor price increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cph"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cpif"), new SimpleTf("ipf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 17 : if competitor price higher and competitor price increasing slow then increase price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cph"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cpis"), new SimpleTf("ipf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 18 : if competitor price higher and competitor price steady then increase price slow
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cph"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cps"), new SimpleTf("ips"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 19 : if competitor price higher and competitor price increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cph"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cpdf"), new SimpleTf("dps"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      FIN DE REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS ALTOS
        
//      REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS BAJOS
        
//      Regla 20 : if competitor price higher and competitor price increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cpl"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cpif"), new SimpleTf("dpf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 21 : if competitor price higher and competitor price increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cpl"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cpis"), new SimpleTf("dps"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 22 : if competitor price higher and competitor price increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cpl"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cps"), new SimpleTf("dpf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
        
//      Regla 23 : if competitor price higher and competitor price increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cpl"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cpds"), new SimpleTf("dps"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);

//      Regla 24 : if competitor price higher and competitor price increasing fast then increase price fast
        estado1 = new State();
        estado2 = new State();
        estado3 = new State();
        estado3.setAccept(true);
        
        estado1.addOutTran(new Transition(new SimpleTf("cpl"), epsilon, estado2));
        estado2.addOutTran(new Transition(new SimpleTf("cpdf"), new SimpleTf("dpf"), estado3));
        
        iterador = new Tffst();
        iterador.setInitialState(estado1);
        reglas.add(iterador);
    
//      FIN DE REGLAS PARA EL CASO DE QUE LA COMPETENCIA OFREZCA PRECIOS MÁS ALTOS
        
//      FIN DE REGLAS PARA LA COMPETENCIA
        
        
    }
    
    public static void main(String[] args) {
    
      ReglasPricing rp = new ReglasPricing();
      rp.construirReglas();
      
      Tffst test = Tffst.makeEmpty();
      
      for (Tffst r : rp.reglas) {
        test = test.union(r);
      }

      Utils.showDot(test.toDot(""));
      
      Tffst testkleene = test.kleene();
      
      testkleene.setDeterministic(false);
      testkleene.determinize();
      
      Utils.showDot(testkleene.toDot("deterministic"));
      
      
    }

}
