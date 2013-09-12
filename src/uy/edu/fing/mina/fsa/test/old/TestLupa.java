package uy.edu.fing.mina.fsa.test.old;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.lupa.LupaExporter;
import uy.edu.fing.mina.lupa.exceptions.UnsupportedTFFSTException;
import uy.edu.fing.mina.lupa.tf.ActionTf;
import uy.edu.fing.mina.lupa.tf.EventTf;
import uy.edu.fing.mina.lupa.tf.Sentence;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */
public class TestLupa {

    // public static void setSharedId(Tf tf1, Tf tf2, String id) {
    // tf1.setSharedId(id);
    // tf2.setSharedId(id);
    // }
    // public static void setSharedId(Transition t, String id) {
    // ((Tf)t.getLabelIn().get(0)).setSharedId(id);
    // ((Tf)t.getLabelOut().get(0)).setSharedId(id);
    // }


    public static void main(String[] args) {

        Tffst.setMinimizeAlways(false);

        Tffst tffst = new Tffst();

        State s0 = new State();
        tffst.setInitialState(s0);

        State s1 = new State();

        State s2 = new State();

        String init_e1_string = "{target=\"127.0.0.1/lupa/rmoon\", command=\"watch_mib\", mib=\"random\", "
        +"op=\">\", value=\"0.5\", notification_id=\"n1\", watcher_id=\"e1\"}";
        String init_e2_string = "{target=\"127.0.0.1/lupa/rmoon\", command=\"watch_mib\", mib=\"random\", "
            +"op=\">\", value=\"0.5\", notification_id=\"n2\", watcher_id=\"e2\"}";
        
        //s2.setAccept(true);

        EventTf etf_E1 = new EventTf();
        etf_E1.setName("E1");
        etf_E1.getFilter().add(new Sentence("message_type", "==", "\"trap\""));
        etf_E1.getFilter().add(new Sentence("watcher_id", "==", "\"e1\""));
        etf_E1.getFilter().add(new Sentence("source", "==", "\"127.0.0.1/lupa/rmoon\""));
        etf_E1.getLuaPredicate().luaText = "return 1";
        etf_E1.getInitSubsStrings().add(init_e1_string);

        EventTf etf_E2 = new EventTf();
        etf_E2.setName("E2");
        etf_E2.getFilter().add(new Sentence("message_type", "==", "\"trap\""));
        etf_E2.getFilter().add(new Sentence("watcher_id", "==", "\"e2\""));
        etf_E2.getFilter().add(new Sentence("source", "==", "\"127.0.0.1/lupa/rmoon\""));
        etf_E2.getLuaPredicate().luaText = "return 1";
        etf_E2.getInitSubsStrings().add(init_e2_string);

        ActionTf atf_S = new ActionTf();
        atf_S.setName("S");
        atf_S.setLuaCode("  local e=shared[\"incomming_event\"] --retrieve event\n"
                +"  print (\"S! value:\", e.value)\n"
                +"  local ret = {}\n --generate a copy of event..."
                +"  ret.source=e.source\n"
                +"  ret.timestamp=e.timestamp\n"
                +"  ret.notification_id=e.notification_id\n"
                +"  ret.message_type=\"trap\"\n"
                +"  ret.watcher_id=e.watcher_id\n"
                +"  ret.mib=e.mib\n"
                +"  ret.value=e.value\n"
                +"  ret.target=\"test_tffst\" --...with new target\n"
                +"  return {ret}");

        ActionTf atf_Eps = new ActionTf();
        atf_Eps.setName("Eps");
        atf_Eps.setEpsilon();

        Transition trans0 = new Transition(etf_E1, atf_Eps, s1);
        Transition trans1 = new Transition(etf_E1, atf_Eps, s1);
        Transition trans2 = new Transition(etf_E2, atf_S, s2);
        atf_S.setRefersTo(etf_E2);
        Transition trans3 = new Transition(etf_E2, atf_Eps, s2);
        Transition trans4 = new Transition(etf_E1, atf_Eps, s1);

        s0.addOutTran(trans0);
        s1.addOutTran(trans1);
        s1.addOutTran(trans2);
        s2.addOutTran(trans3);
        s2.addOutTran(trans4);


    try {
      
      LupaExporter.generateLupaFiles(tffst, "src/fsm_template.lua", "/tmp/test_fsm");
      
      tffst.setDeterministic(false);
      tffst.determinize();

      LupaExporter.generateLupaFiles(tffst, "src/fsm_template.lua", "/tmp/test_fsm_determinized");
      
    } catch (UnsupportedTFFSTException e) {
      e.printStackTrace();
    }       

    }

}