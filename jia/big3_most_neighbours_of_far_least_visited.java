package jia;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.logging.Level;

//import arch.Big3LocalWorldModel;
//import arch.Big3MinerArch;
import arch.LocalWorldModel;
import arch.MinerArch;

import java.util.ArrayList;

import env.WorldModel;


/**
 * Gets the far least visited location.
 * Its is based on the agent's model of the world.
 *
 * @author jomi
 *
 */



public class big3_most_neighbours_of_far_least_visited extends DefaultInternalAction {

    LocalWorldModel model;

    /*
    class Big3LocalWorldModel extends LocalWorldModel{

        public Big3LocalWorldModel(int w, int h, int nbAg) {
            super(w, h, nbAg);
        }
        
    }
    */

    public Location bestOne(ArrayList<Location> l){
        Location best = null;
        double bestUtility = 0;
        for (Location loc : l){
            int visitsCount = 0;
            int neighboursCount = 0;
            if (loc.x - 1 >= 0) visitsCount += visitsCount + model.getVisited(new Location(loc.x - 1, loc.y)); neighboursCount++;
            if (loc.x + 1 < model.getWidth()) visitsCount += visitsCount + model.getVisited(new Location(loc.x + 1, loc.y)); neighboursCount++;
            if (loc.y - 1 >= 0) visitsCount += visitsCount + model.getVisited(new Location(loc.x, loc.y - 1)); neighboursCount++;
            if (loc.y + 1 < model.getHeight()) visitsCount += visitsCount + model.getVisited(new Location(loc.x, loc.y + 1)); neighboursCount++;
            double utility = visitsCount / neighboursCount;
            if (utility >= bestUtility) bestUtility = utility; best = loc;
        }
        return best;
    }

    public Location getMostNeighboursOfFarLeastVisited(int agx, int agy) {
        //int distanceToBorder = (agx < getWidth()/2 ? agx : getWidth() - agx) - 1;
        Location agloc = new Location(agx,agy);
        /*
        logger.info("------");
        for (int i = 0; i < getWidth(); i++) {
            String line = "";
            for (int j = 0; j < getHeight(); j++) {
                line += visited[j][i] + " ";
            }
            logger.info(line);
        }
        */

        //int visitedTarget = 0;
        int farMinVisited = 0;
        while (true) {
            
            int x = agx;
            int y = agy;
            int w = model.getWidth() - 1;
            int dx = w;
            int dy = w;
            int stage = 1;//(x % 2 == 0 ? 1 : 2);
            Location better = null;
            ArrayList<Location> goodOnes = new ArrayList();

            while (w > 0) { //( (w/2+distanceToBorder) < getWidth()) {
                switch (stage) {
                case 1:
                    if (dx > 0) {
                        dx--;
                        break;
                    } else {
                        stage = 2;//(x % 2 == 0) ? 2 : 3;
                    }
                case 2:
                    if (dy > 0) {
                        dy--;
                        break;
                    } else {
                        stage = 3;//(x % 2 == 0) ? 3 : 1;
                    }
                case 3:
                    if (dx < w) {
                        dx++;
                        break;
                    } else {
                        stage = 4;
                    }
                case 4:
                    if (dy < w) {
                        dy++;
                        break;
                    } else {
                        stage = 1;
                        x--;
                        y--;
                        w -= 2;
                    }
                }

                Location l = new Location(x+dx,y+dy);
                if (model.isFree(l) && !l.equals(agloc)) {
                    if (model.getVisited(l) <= farMinVisited) { // a place better then minVisited! go there
                        goodOnes.add(l);
                    }
                }
            } // end while

            
            if (!goodOnes.isEmpty()){
                Location best = bestOne(goodOnes);
                return best;
            }
		
            farMinVisited++;
        }
    }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
        try {
            model = ((MinerArch)ts.getUserAgArch()).getModel();
            //Big3LocalWorldModel model = ((Big3MinerArch)ts.getUserAgArch()).getModel();
            if (model == null) {
                ts.getLogger().log(Level.SEVERE, "no model to get far_least_visited!");
            } else {
                NumberTerm agx = (NumberTerm)terms[0];
                NumberTerm agy = (NumberTerm)terms[1];
                Location n = getMostNeighboursOfFarLeastVisited((int)agx.solve(), (int)agy.solve());
                un.unifies(terms[2], new NumberTermImpl(n.x));
                un.unifies(terms[3], new NumberTermImpl(n.y));
                //ts.getLogger().info("at "+agx+","+agy+" to "+n.x+","+n.y);
                return true;
            }
        } catch (Throwable e) {
            ts.getLogger().log(Level.SEVERE, "far_least_visited error: "+e, e);
        }
        return false;
    }
}


