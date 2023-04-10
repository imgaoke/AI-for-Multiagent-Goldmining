package jia;

import java.util.logging.Level;

import env.*;
import arch.MinerArch;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.grid.Location;

/** gets the max simulation steps */
public class big3_max_step extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
        try {
            WorldModel model = ((MinerArch)ts.getUserAgArch()).getModel();

            int l = model.getMaxSteps();
            return un.unifies(terms[0], new NumberTermImpl(l));
        } catch (Throwable e) {
            ts.getLogger().log(Level.SEVERE, "max_step error: "+e, e);
        }
        return false;
    }
}

