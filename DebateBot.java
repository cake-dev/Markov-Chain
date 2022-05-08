import java.util.concurrent.TimeUnit;

public class DebateBot {

    // change to true to have the state of each reply start with the speakers name, as to encourage more accurate sentence generation
    final static boolean MAKE_ACCURATE = false;

    public static int char_variance(int c_count) {
        double x = c_count * 0.2;
        double randval = Math.random();
        if (randval > 0.5) {
            return c_count + (int) x;
        }
        return c_count - (int) x;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to the 2020 Presidential Bot Debate!\n");
        System.out.println("Order # of markov chain (num > 0): ");
        int order = StdIn.readInt();

        // # words to print
        System.out.print("How many characters to print? (#): ");
        int c_count = StdIn.readInt();

        // approx 20 chars read per second + 1 second baseline
        int delay = (c_count / 20) + 1;

        String f_i = "2.";
        String t = "TRUMP: ";
        String b = "BIDEN: ";
        String w = "WELKER: ";
        String t_file = "Data/debate/trump.txt";
        String b_file = "Data/debate/biden.txt";
        String w_file = "Data/debate/welker.txt";
        String t_start = "";
        String b_start = "";
        String w_start = "";
        String t_prefix = t;
        String b_prefix = b;
        String w_prefix = w;

        // uses the text files with line by line statements with the names as the start of each statement
        // (will generate text closer to the original sentence structure)
        if (MAKE_ACCURATE) {
            t_file = t_file.replace(".", f_i);
            b_file = b_file.replace(".", f_i);
            w_file = w_file.replace(".", f_i);
            t_start = t.substring(t.length() - order);
            b_start = b.substring(b.length() - order);
            w_start = w.substring(w.length() - order);

            t_prefix = t.substring(0, t.length() - order);
            b_prefix = b.substring(0, b.length() - order);
            w_prefix = w.substring(0, w.length() - order);
        }
        MarkovChain2 trump_bot = new MarkovChain2(order, t_file);
        MarkovChain2 biden_bot = new MarkovChain2(order, b_file);
        MarkovChain2 welker_bot = new MarkovChain2(order, w_file);


        while (true) {
            trump_bot.generate(char_variance(c_count), t_start);
            biden_bot.generate(char_variance(c_count), b_start);
            welker_bot.generate(char_variance(c_count), w_start);

            System.out.println("\n" + w_prefix + welker_bot + "?");
            TimeUnit.SECONDS.sleep(delay);
            System.out.println("\n" + t_prefix + trump_bot);
            TimeUnit.SECONDS.sleep(delay);
            System.out.println("\n" + b_prefix + biden_bot);
            TimeUnit.SECONDS.sleep(delay);

        }

    }

}
