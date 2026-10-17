import java.util.HashSet;
import java.util.Set;


public class EqualsOnlyBug {
    static final class Email{
        private final String value;

        Email(String value) {this.value = value;}

        public boolean equals(Object o){
            return o instanceof Email other && value.equals(other.value);
        }
    }

    public static void main(String[] args){
        Email a = new Email("asha@gmail.com");
        Email b = new Email("asha@gmail.com");

        System.out.println(a.equals(b));

        Set<Email> unique = new HashSet<>();

        unique.add(a);
        unique.add(b);
        System.out.println(unique.size());
    }
}