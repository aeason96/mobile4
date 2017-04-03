package cs3714.hw4.data;

/**
 * Created by Andrey on 3/2/2017.
 */



    public class Team {

        private String teamname;
        private int rank_today;
        private long steps;
        private long steps_total;
        private int rank_total;
        private long stepgoal;

        public Team(String teamname, int rank_today, long steps, long steps_total, int rank_total, long stepgoal) {
            this.teamname = teamname;
            this.rank_today = rank_today;
            this.steps = steps;
            this.steps_total = steps_total;
            this.rank_total = rank_total;
            this.stepgoal = stepgoal;
        }

        public String getTeamname() {
            return teamname;
        }

        public int getRank_today() {
            return rank_today;
        }

        public long getSteps() {
            return steps;
        }

        public long getSteps_total() {
            return steps_total;
        }

        public int getRank_total() {
            return rank_total;
        }

        public long getStepgoal() {
            return stepgoal;
        }
    }


