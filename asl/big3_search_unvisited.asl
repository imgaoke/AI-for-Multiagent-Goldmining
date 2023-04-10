// find gold by going to the near least unvisited location


+!search_gold(near_unvisited)
   :  pos(X,Y,S) & free & jia.big3_max_step(N) & (S < N div 3 | S >= N div 3 * 2) &
      jia.near_least_visited(X,Y,ToX,ToY)
   <- !pos(ToX,ToY);
      .print("search near unvisited");
      !!search_gold(near_unvisited).

+!search_gold(near_unvisited)
   :  pos(X,Y,S) & free & jia.big3_max_step(N) & S >= N div 3 & S < N div 3 * 2
   <- -search_gold_strategy(near_unvisited);
      +search_gold_strategy(far_unvisited);
      !!search_gold(far_unvisited).

+!search_gold(far_unvisited)
   :  pos(X,Y,S) & free & jia.big3_max_step(N) & S >= N div 3 * 2
   <- -search_gold_strategy(far_unvisited);
      +search_gold_strategy(near_unvisited);
      !!search_gold(near_unvisited).

+!search_gold(near_unvisited) : free
   <- !!search_gold(near_unvisited).

-!search_gold(near_unvisited)
   <- !!choose_goal.


+!search_gold(far_unvisited)
   :  pos(X,Y,_) & free & 
      jia.big3_most_neighbours_of_far_least_visited(X,Y,ToX,ToY)
   <- !pos(ToX,ToY);
      .print("search far unvisited");
      !!search_gold(far_unvisited).

/*
+!search_gold(far_unvisited)
   :  pos(X,Y,S) & free & S >= 200
   <- -search_gold_strategy(far_unvisited);
      +search_gold_strategy(near_unvisited);
      !!search_gold(near_unvisited).
*/

+!search_gold(far_unvisited) : free
   <- !!search_gold(far_unvisited).

-!search_gold(far_unvisited)
   <- !!choose_goal.





@lfg[atomic]
-free
  :  .desire(search_gold(_))
  <- .print("Dropping search_gold desires and intentions to handle gold");
     .drop_desire(search_gold(_)).

