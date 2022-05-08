import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TextGen2 {

    public static void main(String[] args) throws IOException {

        // https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
        List<File> file_list = Files.walk(Paths.get("Data/Text Samples"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        System.out.println("Select a file # to read from: ");
        for (int i = 0; i < file_list.size(); i++) {
            System.out.println(i + ": " + file_list.get(i).getName());
        }
        int file_num = StdIn.readInt();
        System.out.println("Order # of markov chain (num > 0): ");
        int order = StdIn.readInt();
        String file_name = "Data/Text Samples/" + file_list.get(file_num).getName();
        MarkovChain2 chain = new MarkovChain2(order, file_name);

        // # words to print
        System.out.print("How many characters to print? (#): ");
        int c_count = StdIn.readInt();

        // generate a markov chain text sequence of length N (chars)
        chain.generate(c_count, "");
        System.out.println(chain);


    }

}
