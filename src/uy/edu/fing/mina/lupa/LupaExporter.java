package uy.edu.fing.mina.lupa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uy.edu.fing.mina.fsa.tf.CompositeTf;
import uy.edu.fing.mina.fsa.tf.Operator;
import uy.edu.fing.mina.fsa.tf.TfI;
import uy.edu.fing.mina.fsa.tf.TfString;
import uy.edu.fing.mina.fsa.tffst.State;
import uy.edu.fing.mina.fsa.tffst.Tffst;
import uy.edu.fing.mina.fsa.tffst.Transition;
import uy.edu.fing.mina.fsa.utils.Utils;
import uy.edu.fing.mina.lupa.exceptions.UnsupportedTFFSTException;
import uy.edu.fing.mina.lupa.tf.ActionTf;
import uy.edu.fing.mina.lupa.tf.EventTf;
import uy.edu.fing.mina.lupa.tf.Sentence;

/**
 * Contains all the functions necessary to export a TFFST into LUA code to work with LUPA.
 * @author Jorge Visca
 * @author Juan Saavedra
 *
 */
public class LupaExporter {
	/**
	 * This is used to name the functions.
	 * Functions use number as names to avoid conflicts with LUA's syntax.
	 */
	private static Integer functionsCounter;
	
	/**
	 * A map is maintained in order to access the generated function name for a given TF. 
	 * The original name of the TF should be included as a comment on top of the function.
	 */
	private static Map<TfI, String> functionNames;
	
	/**
	 * These sets will be completed when all the TF functions are sorted by loadActionsAndEvents.
	 * @see loadActionsAndEvents
	 */
	private static Map<String, TfI> mainEvents;
	private static Map<String, TfI> mainActions;
	private static Map<String, TfI> compositeEvents;
	private static Map<String, TfI> compositeActions;
	
	/**
	 * Reads the specified file and returns its contents as a string.
	 * @param file The file to read.
	 * @return The contents of the file in a String object.
	 */
	private static String readFile(File file) {
		if(file.exists()){
			StringBuilder contents = new StringBuilder();
			try {
				BufferedReader input = new BufferedReader(new FileReader(file));
				try {
					String line = null; // not declared within while loop
					while ((line = input.readLine()) != null) {
						contents.append(line);
						contents.append(System.getProperty("line.separator"));
					}
				} catch (IOException ex){
					System.err.println("ERROR while reading a file.");
					System.err.println(ex.getMessage());
					
				} finally {
					input.close();
				}
			} catch (IOException ex) {
				System.err.println("ERROR while creating a FileReader or a BufferedReader.");
				System.err.println(ex.getMessage());
			}
			return contents.toString();
		}
		else{
			return "";
		}

	}
	
	/**
	 * Writes the desired output into the file. 
	 * <b> Be careful, since it overwrites the destination file </b>.
	 * @param text Text to print in the file.
	 * @param file Destination write file.
	 */
	private static void printFile(String text, File file) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			
			bw.write(text);
		} catch (IOException e) {
			System.err.println("ERROR when writing to a file.");
			System.err.println(e.getMessage());
		} finally {
			try{
				bw.close();
			} catch (IOException e) {
				System.err.println("ERROR when closing a file writer.");
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Returns the function name to use in LUA.
	 * If it is composite, a number as a name will be assigned.
	 * Otherwise, the label will be included.
	 * @param function The Tautness Function to translate. 
	 * @return The function name in LUA code.
	 */
	private static String functionName(TfI function) {
		String storedName = functionNames.get(function);
		if(storedName != null){
			return storedName;
		}
		else{
			String newName;
			String label = function.getName();
			label = label.replace(" ", "_");
			label = label.replace("!", "not_");
			if( function.isNot() || function instanceof CompositeTf){
				newName = "f" + functionsCounter.toString();
				functionsCounter++;
			}
			else if(function instanceof ActionTf){			
				newName = "action_" + function.getName();
			}
			else if(function instanceof EventTf){
				newName = "event_" + function.getName();
			}
			else{
				newName = "There is an error!! A TF function was detected that is not an Action, an Event nor a Composite function.";
			}
			functionNames.put(function, newName);
			return newName;
		}
	}
	
	/**
	 * Returns the header for the LUA function.
	 * Weather it is a local function or a function inside a table, such must be append at the header's beginning. 
	 * @param function Function's name
	 * @return The complete header.
	 */
	private static String functionHeader(String function){
		return "function " + function + "(e)";
	}
	
	private static void loadActions(TfI tfi) throws UnsupportedTFFSTException{
		String functionName = functionName(tfi);
		if(tfi instanceof CompositeTf){
			compositeActions.put(functionName, tfi);
			loadActions(((CompositeTf) tfi).left);
			loadActions(((CompositeTf) tfi).right);
			
		}
		else if(tfi instanceof ActionTf){
			if(tfi.isNot()){
				compositeActions.put(functionName,tfi);
				tfi = tfi.not();
				functionName = functionName(tfi);
				mainActions.put(functionName, tfi);
			}
			else{
				mainActions.put(functionName, tfi);
			}
		}
		else{
			throw new UnsupportedTFFSTException("A problematic TF (" + functionName +") function was detected. Not an action or composite TF when uploading actions.");
		}
	}
	
	private static void loadEvents(TfI tfi) throws UnsupportedTFFSTException{
		String functionName = functionName(tfi);
		if(tfi instanceof CompositeTf){
			compositeEvents.put(functionName, tfi);
			loadEvents(((CompositeTf) tfi).left);
			loadEvents(((CompositeTf) tfi).right);
		}
		else if(tfi instanceof EventTf){
			if(tfi.isNot()){
				compositeEvents.put(functionName,tfi);
				tfi = tfi.not();
				functionName = functionName(tfi);
				mainEvents.put(functionName, tfi);
			}
			else{
				mainEvents.put(functionName, tfi);
			}
		}
		else{
			throw new UnsupportedTFFSTException("A problematic TF (" + functionName +") function was detected. Not an eveny or composite TF when uploading events.");
		}
	}
	
	private static void loadActionsAndEvents(Tffst tffst) throws UnsupportedTFFSTException{
//		TODO Why are we working only with the first element of the LabelIn?
		for(Transition t : tffst.getTransitions()){
			if(!t.getLabelIn().isEpsilon()){
				loadEvents(t.getLabelIn().get(0));
			}
			else{
				throw new UnsupportedTFFSTException("A problematic Transition (" + t.getFrom() + t.toString() +") was detected. Not an eveny or composite TF when uploading events.");
			}
			
			if(!t.getLabelOut().isEpsilon()){
				loadActions(t.getLabelOut().get(0));
				
			}
		}
	}

	private static String initialstateToLupa(Tffst tffst) {
		State s = tffst.getInitialState();
		String out = "\"" + String.valueOf(s.getNumber()) + "\"";
		
		return out;
	}

	private static String initToLupa(Tffst tffst) {
		//TODO replicar para setear init_subs
		Iterator<Transition> transIt = tffst.getTransitions().iterator();
		StringBuffer out_notifs = new StringBuffer("local initialization_notifs = {\n");
		StringBuffer out_subs = new StringBuffer("local initialization_subs = {\n");

		LinkedList<TfI> workingList = new LinkedList<TfI>();
		Set<String> ev_generated = new HashSet<String>();
		Set<String> init_notifs_generated = new HashSet<String>();
		Set<String> init_subs_generated = new HashSet<String>();

		// tfi from all the transitions
		while (transIt.hasNext()) {
			Transition t = transIt.next();
			if (!t.getLabelIn().isEpsilon()) {
				String functionname = functionName(t.getLabelIn().get(0));
				if (!ev_generated.contains(functionname)) {
					ev_generated.add(functionname);
					workingList.add(t.getLabelIn().get(0));
				}
			}
		}

		// iterate all the tfs, in dept
		ev_generated.clear();
		while (!workingList.isEmpty()) {
			TfI tfi = workingList.removeFirst();
			String functionname = functionName(tfi);
			if (!ev_generated.contains(functionname)) {
				ev_generated.add(functionname);
				if (tfi instanceof EventTf) {
					// creating code for a new event
					EventTf e = (EventTf) tfi;
					Iterator<String> inits_notifs = e.getInitNotifStrings().iterator();
					while (inits_notifs.hasNext()) {
						String init = inits_notifs.next();
						if (!init_notifs_generated.contains(init)) {
							init_notifs_generated.add(init);
							out_notifs.append("  ").append(init).append(",\n");
						}
					}

					Iterator<String> inits_subs = e.getInitSubsStrings().iterator();

					while (inits_subs.hasNext()) {
						String init = inits_subs.next();
						if (!init_subs_generated.contains(init)) {
							init_subs_generated.add(init);
							out_subs.append("  ").append(init).append(",\n");
						}
					}
				
				}
				if (tfi instanceof CompositeTf) {
					CompositeTf c = (CompositeTf) tfi;
					TfI leftTf = c.left;
					TfI rightTf = c.right;
					workingList.add(leftTf);
					workingList.add(rightTf);
				}
			}

		}
		out_notifs.append("}\n");
		out_subs.append("}\n");
		return out_notifs.toString() + out_subs.toString();
	}
	
	/**
	 * Updates the manual LUA event TF functions to include new ones.
	 * THIS FUNCTION DOES NOT ERASE DATA. Have this in mind if you are frequently renaming labels.
	 * @param old A String object with the LUA functions.
	 * @param tffst The TFFST to export.
	 * @return The updated String objects with the new function bodies that it may have.
	 */
	private static String updateMainEventsInLupa(String old, Tffst tffst) {
//		Iterator<Transition> transIt = tffst.getTransitions().iterator();
		String out = new String(old);
		
//		Set<String> alreadyGenerated = new HashSet<String>();
////		We take only those functions that are Events.
//		List<TfI> workingList = new LinkedList<TfI>();
////		TODO Why are we working only with the first element of the LabelIn?
//		TfI firstElementOfTfString;
//		String functionName;
//		for(Transition t : tffst.getTransitions()){
//			
//			if(!t.getLabelIn().isEpsilon()){
//				firstElementOfTfString = t.getLabelIn().get(0);
//				if(!firstElementOfTfString.isNot() && (firstElementOfTfString instanceof EventTf)){
//					workingList.add(firstElementOfTfString);
//				}
//			}
//		}
		
		String functionHeader;
		for(String functionName : mainEvents.keySet()){
			functionHeader = functionHeader(functionName);
//			There are no duplicates in a Map.
			if(! out.contains(functionHeader)){
				out = out.concat("\t-- " + functionHeader + "\n");
				out = out.concat("events." + functionName + " = function(e) \n");
//				TODO Mejorar!!
				//out = out.concat("\tshared[\"incomming_event\"] = nil\n");
				out = out.concat("\tshared[\"incomming_event\"] = e\n"); 
				out = out.concat("\t-----------------------------------------------\n");
				out = out.concat("\t-- TODO: Complete this with your event code. --\n");
				out = out.concat("\t-----------------------------------------------\n");
				out = out.concat("end\n\n");
			}
			
		}
		return out;
	}

	/**
	 * Updates the manual LUA functions to include new ones.
	 * THIS FUNCTION DOES NOT ERASE DATA. Have this in mind if you are frequently renaming labels.
	 * @param old A String object with the lua functions.
	 * @param tffst The TFFST to export.
	 * @return The updated String objects with the new function bodies that it may have.
	 */
	private static String updateMainActionsInLupa(String old, Tffst tffst) {
//		Iterator<Transition> transIt = tffst.getTransitions().iterator();
		String out = new String(old);
		
//		Set<String> alreadyGenerated = new HashSet<String>();
////		We take only those functions that are Events.
//		List<TfI> workingList = new LinkedList<TfI>();
////		TODO Why are we working only with the first element of the LabelIn?
//		TfI firstElementOfTfString;
//		String functionName;
//		for(Transition t : tffst.getTransitions()){
//			
//			if(!t.getLabelIn().isEpsilon()){
//				firstElementOfTfString = t.getLabelIn().get(0);
//				if(!firstElementOfTfString.isNot() && (firstElementOfTfString instanceof EventTf)){
//					workingList.add(firstElementOfTfString);
//				}
//			}
//		}
		
		String functionHeader;
		for(String functionName : mainActions.keySet()){
			functionHeader = functionHeader(functionName);
//			There are no duplicates in a Map.
			if(! out.contains(functionHeader)){
				out = out.concat("\t-- " + functionHeader + "\n");
				out = out.concat("actions." + functionName + " = function(e)\n");
//				TODO Mejorar!!
				
				out = out.concat("\te = shared[\"incomming_event\"]\n");
				out = out.concat("\t------------------------------------------------\n");
				out = out.concat("\t-- TODO: Complete this with your action code. --\n");
				out = out.concat("\t------------------------------------------------\n");
				out = out.concat("end\n\n");
			}
			
		}
		return out;
	}

	/**
	 * Creates a String with event functions that are CompositeTf or Nots.
	 * @param tffst The TFFST to export.
	 * @return The updated String objects with the new function bodies that it may have.
	 * @throws UnsupportedTFFSTException 
	 */
	private static String compositeEventsToLupa(Tffst tffst) throws UnsupportedTFFSTException {
		String out = new String();
		
		out = out.concat("-----------------------------------------------\n");
		out = out.concat("--          BEGIN COMPOSITE EVENTS           --\n");
		out = out.concat("-----------------------------------------------\n");
		CompositeTf comp;
		TfI leftTf;
		TfI rightTf;
		String functionName;
		for(TfI function : compositeEvents.values()){
			functionName = functionName(function);
			if(function instanceof CompositeTf){
				comp = (CompositeTf) function;
				leftTf = comp.left;
				rightTf = comp.right;
				out = out.concat("-- " + function.getName() + "\n");
				out = out.concat("events." + functionName + " = function(e) \n");
//				out = out.concat(functionHeader(functionName) + "\n");
				out = out.concat("\tlocal left= events." + functionName(leftTf) + "(e)\n");
				out = out.concat("\tlocal right= events."	+ functionName(rightTf) + "(e)\n");
				if (comp.op == Operator.AND) {
					out = out.concat("\tif left < right then return left else return right end\n");
				} else if (comp.op == Operator.OR) {
					out = out.concat("\tif left > right then return left else return right end\n");

				} 

				out = out.concat("end\n");
			}
			else if (function.isNot()) {
				out = out.concat("-- " + function.getName() + "\n");
				out = out.concat("events." + functionName + " = function(e) \n");
				TfI nonneg = function.not();
				out = out.concat("\tlocal nonneg = events." + functionName(nonneg) + "(e)\n");
				out = out.concat("\treturn 1-nonneg\n");
				out = out.concat("end\n");
			}
			else{
				throw new UnsupportedTFFSTException("A problematic function (" + function.getName() +") was detected. A generated EVENT function should always be composite or not.");
			}
		}
		
		
		out = out.concat("-----------------------------------------------\n");
		out = out.concat("--           END COMPOSITE EVENTS            --\n");
		out = out.concat("-----------------------------------------------\n");
		
		return out;
	}


  /**
   * Creates a String with actions functions that are CompositeTf or Nots.
   * 
   * @param tffst
   *          The TFFST to export.
   * @return The updated String objects with the new function bodies that it may
   *         have.
   * @throws UnsupportedTFFSTException
   */
  private static String compositeActionsToLupa(Tffst tffst) throws UnsupportedTFFSTException {
    String out = new String();

    out = out.concat("-----------------------------------------------\n");
    out = out.concat("--          BEGIN COMPOSITE ACTIONS          --\n");
    out = out.concat("-----------------------------------------------\n");
    CompositeTf comp;
    TfI leftTf;
    TfI rightTf;
    String functionName;
    for (TfI function : compositeActions.values()) {
      functionName = functionName(function);
      if (function instanceof CompositeTf) {
        comp = (CompositeTf) function;
        leftTf = comp.left;
        rightTf = comp.right;
        out = out.concat("-- " + function.getName() + "\n");
        // out = out.concat(functionHeader(functionName) + "\n");
        out = out.concat("actions." + functionName + " = function(e)\n");
        out = out.concat("\tlocal left= actions." + functionName(leftTf) + "(e)\n");
        out = out.concat("\tlocal right= actions." + functionName(rightTf) + "(e)\n");
        if (comp.op == Operator.AND) {
          out = out.concat("\tif left < right then return left else return right end\n");
        } else if (comp.op == Operator.OR) {
          out = out.concat("\tif left > right then return left else return right end\n");
        } 
        out = out.concat("end\n");
      } else if (function.isNot()) {
        out = out.concat("-- " + function.getName() + "\n");
        out = out.concat("actions." + functionName + " = function(e)\n");
        TfI nonneg = function.not();
        out = out.concat("\tlocal nonneg = actions." + functionName(nonneg) + "(e)\n");
        out = out.concat("\treturn -nonneg\n");
        out = out.concat("end\n");
      } else {
        throw new UnsupportedTFFSTException("A problematic function (" + function.getName()
            + ") was detected. A generated ACTION function should always be composite or not.");
      }
    }

    out = out.concat("-----------------------------------------------\n");
    out = out.concat("--           END COMPOSITE ACTIONS           --\n");
    out = out.concat("-----------------------------------------------\n");

    return out;
  }

	

//	private static String actionsToLupa(Tffst tffst) {
//
//		Iterator<Transition> transIt = tffst.getTransitions().iterator();
//		StringBuffer out = new StringBuffer("");
//
//		LinkedList<TfI> workingList = new LinkedList<TfI>();
//		Set<String> ev_generated = new HashSet<String>();
//
//		// tfi from all the transitions
//		while (transIt.hasNext()) {
//			Transition t = transIt.next();
//			if (!t.getLabelOut().isEpsilon()) {
//				String functionname = functionName(t.getLabelOut().get(0));
//				if (!ev_generated.contains(functionname)) {
//					ev_generated.add(functionname);
//					workingList.add(t.getLabelOut().get(0));
//				}
//			}
//		}
//
//		// iterate all the tfs, in dept
//		ev_generated.clear();
//		while (!workingList.isEmpty()) {
//			TfI tfi = workingList.removeFirst();
//			String functionname = functionName(tfi);
//			if (!ev_generated.contains(functionname)) {
//				ev_generated.add(functionname);
//				if (tfi instanceof ActionTf) {
//					// creating code for a new action
//					ActionTf a = (ActionTf) tfi;
//					out.append("-- " + tfi.getSLabel() + "\n");
//					out.append("local function " + functionname + "(e)\n");
//					/*
//					 * Esto ya no se utiliza más.
//					 * Ahora la tabla shared es global.
//					 *
//					 *
//					if (a.getRefersTo() != null) {
//						out.append("  local shared = shared_"
//								+ a.getRefersTo().getId() + "\n");
//					} else {
//						out.append("  local shared = shared_" + a.getId()
//								+ "\n");
//					}
//					*/
//					if (a.getLuaCode() != null) {
//						out.append(a.getLuaCode() + "\n");
//					}
//					if (a.getIdentity() == 1) {
//						out.append("  return shared[\"incomming_event\"]\n");
//					}
//					out.append("end\n");
//				}
//				if (tfi instanceof CompositeTf) {
//					CompositeTf c = (CompositeTf) tfi;
//					TfI leftTf = c.leftTf;
//					TfI rightTf = c.rightTf;
//					workingList.add(leftTf);
//					workingList.add(rightTf);
//					out.append("-- " + tfi.getSLabel() + "\n");
//					out.append("local function " + functionname + "(e)\n");
//					out
//							.append("  local left=" + functionName(leftTf)
//									+ "(e)\n");
//					out.append("  local right=" + functionName(rightTf)
//							+ "(e)\n");
//					out.append("  --return left " + c.operator + " right\n");
//					out.append("end\n");
//				}
//			}
//
//		}
//		return out.toString();
//	}

	private static String sharesToLupa(Tffst tffst) {

		Iterator<Transition> transIt = tffst.getTransitions().iterator();
		StringBuffer out = new StringBuffer("");

		LinkedList<TfI> workingList = new LinkedList<TfI>();
		Set<Integer> ev_generated = new HashSet<Integer>();

		// tfi from all the transitions
		while (transIt.hasNext()) {
			Transition t = transIt.next();
			if (!t.getLabelIn().isEpsilon()) {
				Integer id = t.getLabelIn().get(0).getId();
				if (!ev_generated.contains(id)) {
					ev_generated.add(id);
					workingList.add(t.getLabelIn().get(0));
				}
			}
			if (!t.getLabelOut().isEpsilon()) {
				Integer id = t.getLabelOut().get(0).getId();
				if (!ev_generated.contains(id)) {
					ev_generated.add(id);
					workingList.add(t.getLabelOut().get(0));
				}
			}
		}

		// iterate all the tfs, in depth
		ev_generated.clear();
		while (!workingList.isEmpty()) {
			TfI tfi = workingList.removeFirst();
			Integer id = tfi.getId();
			if (!ev_generated.contains(id)) {
				ev_generated.add(id);
				if (tfi instanceof EventTf || tfi instanceof ActionTf) {
					// creating code for a new share
					out.append("local shared_" + tfi.getId() + " = {}\n");
				} else if (tfi instanceof CompositeTf) {
					CompositeTf c = (CompositeTf) tfi;
					TfI leftTf = c.left;
					TfI rightTf = c.right;
					workingList.add(leftTf);
					workingList.add(rightTf);
				}
			}

		}
		return out.toString();
	}

	private static String transitionsToLupa(Tffst tffst) {

		Set<State> states = tffst.getStates();

		Iterator<State> stetesIt = states.iterator();

		StringBuffer out = new StringBuffer("{\n");

		while (stetesIt.hasNext()) {
			State s = stetesIt.next();

			Iterator<Transition> transitionsIt = s.getTransitionsIterator();
			while (transitionsIt.hasNext()) {
				Transition t = transitionsIt.next();
				State s2 = t.getDest();

				out.append("  {");
				out.append("\"").append(s.getNumber()).append("\"");

				if (t.getLabelIn().size() == 0) {
					out.append(", nil");
				} else {
					out.append(", events.")
							.append(functionName(t.getLabelIn().get(0)));
				}
				out.append(", \"").append(s2.getNumber()).append("\"");
				if (t.getLabelOut().size() == 0) {
					out.append(", nil");
				} else {
					out.append(", {");
					LinkedList<TfI> actionsList = t.getLabelOut().getListOfTfs();
					Iterator<TfI> actionsIterator = actionsList.iterator();
					while (actionsIterator.hasNext()) {
						TfI action = actionsIterator.next();
						out.append("actions.").append(
								functionName(action));
						out.append(", ");
					}
					out.append("}");

				}
				out.append("},\n");
			}
		}
		out.append("}\n");

		return out.toString();
	}

	private static String finalsToLupa(Tffst tffst) {

		Set<State> states = tffst.getStates();

		Iterator<State> stetesIt = states.iterator();

		StringBuffer out = new StringBuffer("  {\n");

		while (stetesIt.hasNext()) {
			State s = stetesIt.next();
			if (s.isAccept()) {
				out.append("['").append(s.getNumber()).append("']=true,\n");
			}
		}
		out.append("}\n");

		return out.toString();
	}
	
	private static void writeGeneratedLupa(Tffst tffst, String templateFileName, String destinationFileName, String manualFunctions) throws UnsupportedTFFSTException {
		File inputFile = new File(templateFileName);
		File outputFile = new File(destinationFileName + ".lua");
		
		String original = readFile(inputFile);
		String salida = new String();
		
		salida = salida.concat("\n--initialization\n");
		salida = salida.concat(initToLupa(tffst));
		
		salida = salida.concat("\n-------------------------------\n");
		salida = salida.concat("\n-- Non-Generated functions ----\n");
		salida = salida.concat("\n-- Obtained from:          ----\n");
		salida = salida.concat("\n-- " + destinationFileName + "_manual.lua  --\n");
		salida = salida.concat("\n-------------------------------\n");
		salida = salida.concat(manualFunctions);
		salida = salida.concat("\n-------------------------------\n");
		salida = salida.concat("\n-- End of functions        ----\n");
		salida = salida.concat("\n-------------------------------\n");
		
		salida = salida.concat("\nlocal init_state=");
		salida = salida.concat(initialstateToLupa(tffst));
		salida = salida.concat("\n--shares\n");
		salida = salida.concat(sharesToLupa(tffst));
		salida = salida.concat("\n--events\n");
		salida = salida.concat(compositeEventsToLupa(tffst));
		salida = salida.concat("\n--actions\n");
		salida = salida.concat(compositeActionsToLupa(tffst));
		salida = salida.concat("\n--transitions\nlocal fsm = FSM");
		salida = salida.concat(transitionsToLupa(tffst));
		salida = salida.concat("\n--final states\nlocal is_accept = ");
		salida = salida.concat(finalsToLupa(tffst));
		
		salida = original.replace("--datahere--", salida);
		
		printFile(salida, outputFile);

	}
	
	private static String updateManualLupa(Tffst tffst, String destinationFileName) {
		File inputFile = new File(destinationFileName + "_manual.lua");
		File outputFile = inputFile;
		
		String original = readFile(inputFile);
		
		original = updateMainActionsInLupa(original, tffst);
		original = updateMainEventsInLupa(original, tffst);
		
		
		printFile(original, outputFile);
		return original;
//		
//		functionsCounter = new Integer(0);
//		functionNames = new HashMap<TfI, String>();
//		Tffst proctffst = tffst.toSimpleTransitions();
////		Utils.showDot(proctffst.toDot(""));
//
//		String initToLupa = initToLupa(proctffst);
//		String initialstate = initialstateToLupa(proctffst);
//		String transitions = transitionsToLupa(proctffst);
//		String events = eventsToLupa(proctffst);
//		String shares = sharesToLupa(proctffst);
//		String actions = actionsToLupa(proctffst);
//
//		StringBuffer out = new StringBuffer("");
//
//		out.append("\n--initialization\n").append(initToLupa);
//		out.append("local init_state=").append(initialstate).append("\n");
//		out.append("\n--shares\n").append(shares);
//		out.append("\n--events\n").append(events);
//		out.append("\n--actions\n").append(actions);
//		// out.append("\n--shared states\n").append(shared);
//		out.append("\n--transitions\nlocal fsm = FSM").append(transitions);
//
//		String template = readFile(template_file);
//		String lupaout = template.replace("--datahere--", out);
//
//		return lupaout.toString();
	}
	
	/** 
	 * Generates the outcoming files for the given TFFST.
	 * The resulting files are :
	 * <ol>
	 * <li> [filename].lua : Contains the generated LUA code for LUPA.
	 * <li> [filename]_manual.lua : Contains the headers for the LUA TF functions needed for the TFFST to work.
	 * </ol>
	 * 
	 * In the "manual" file, any already existing function WILL NOT be overwritten.
	 * If a function definition doesn't exists, a header for such will be introduced.
	 * 
	 * In the "general" file, no code editing should be necessary.
	 * 
	 * @param fst The TFFST to generate the LUA files.
	 * @param filename The prefix for the destination files.
	 * @throws UnsupportedTFFSTException 
	 */
  public static void generateLupaFiles(Tffst fst, String templateFilename, String filename) throws UnsupportedTFFSTException {
	try {
	  functionsCounter = new Integer(0);
	  functionNames = new HashMap<TfI, String>();
	  mainEvents = new HashMap<String, TfI>();
	  mainActions = new HashMap<String, TfI>();
	  compositeEvents = new HashMap<String, TfI>();
	  compositeActions = new HashMap<String, TfI>();
	  // Tffst proctffst = fst.toSimpleTransitions();
	  Tffst proctffst;
	  proctffst = fst.clone();

	  loadActionsAndEvents(proctffst);

	  String manualFunctions = updateManualLupa(proctffst, filename);

	  writeGeneratedLupa(proctffst, templateFilename, filename, manualFunctions);
	} catch (CloneNotSupportedException e) {
	  e.printStackTrace();
	}
  }

}


