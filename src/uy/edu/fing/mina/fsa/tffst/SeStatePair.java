/*
 * Created on 27-Sep-2004
 *
 * Copyright (C) 2004 Javier Baliosian
 * All rights reserved.
 * */

package uy.edu.fing.mina.fsa.tffst;

import uy.edu.fing.mina.fsa.tf.TfString;

/**
 * @author Javier Baliosian &lt; <a
 *         href="mailto:jbaliosian@tsc.upc.es">jbaliosian@tsc.upc.es </a>&gt;
 */

public class SeStatePair {

  public State state;

  public TfString se;

  /**
   * @param state
   * @param se
   */
  public SeStatePair(State state, TfString se) {
    this.state = state;
    this.se = se;
  }

}
