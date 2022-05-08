import java.util.ArrayList;

public class MarkovChain2 {

    // forces random state to start with capital letter if true
    final boolean START_UPPERCASE = true;

    public ST<String, ArrayList<Character>> st;
    public int order;
    public int transitions;
    public String state;
    public String output = "";
    public String full_text;

    // use this to clean up certain characters using the regex charset
    public static String cleanText(String str) {
        // replaces any occurrence of the characters in the given set with empty char
        str = str.replaceAll("[\n]", "");
        str = str.replaceAll("[\r]", " ");
        // return string
        return str;
    }

    // constructor
    public MarkovChain2(int order, String filename) {
        if (order < 1) throw new IllegalArgumentException("order must be > 0");
        this.st = new ST<String, ArrayList<Character>>();
        this.order = order;
        this.transitions = 0;
        In file = new In(filename);
        this.full_text = file.readAll();
        // uncomment the line below to use cleaned text instead of original
        //this.full_text = cleanText(this.full_text);
        // append substring of length order to end of text sample for cyclical behavior when creating transitions
        this.full_text += this.full_text.substring(0, this.order);
        // create transitions with order K
        for (int i = 0; i < this.full_text.length() - this.order; i++) {
            String k_gram = this.full_text.substring(i, i + this.order);
            addTransition(k_gram, this.full_text.charAt(i + order));
        }
    }

    // add a transition from state v to state c
    public void addTransition(String v, Character c) {
        if (!this.st.contains(v)) {
            this.st.put(v, new ArrayList<>());
        }
        this.st.get(v).add(c);
        this.transitions++;
    }

    // pick a transition leaving state v at random, and return the resulting state
    public void next(String v) {
        // create list of possible transitions (needed for chatbot)
        ArrayList<Character> choices = st.get(v);
        // stop if there are no possible transitions from current state (needed for chatbot)
        if (choices == null) {
            return;
        }
        char next_char = choices.get((int) (Math.random() * choices.size()));
        // add character to output sequence
        this.output += next_char;
        int sub_len = output.length();
        // set current state to substring of current output with respect to order
        this.state = output.substring(sub_len - this.order, sub_len);
    }

    // generates a markov chain sequence of length N
    public void generate(int c_count, String start_override) {
        // set state to random key
        String[] keys = new String[this.st.size()];
        int j = 0;
        for (String key : this.st.keys()) {
            keys[j] = key;
            j++;
        }
        String start = keys[(int) (Math.random() * keys.length)];
        if (START_UPPERCASE) {
            while (!Character.isUpperCase(start.charAt(0))) {
                start = keys[(int) (Math.random() * keys.length)];
            }
        }

        this.state = start;
        // override starting n-gram usin constructor, else starting n-gram is random key
        if (start_override.length() > 0) {
            this.state = start_override;
        }
        this.output = this.state;
        for (int i = 0; i < c_count - this.order; i++) {
            this.next(this.state);
        }
    }

    // return a string representation of the generated Markov chain sequence.
    public String toString() {
        return this.output;
    }

}
