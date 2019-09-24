package ascendcorp.com.order.ulti;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {

  public static void main(String[] args) {
    String salt = BCrypt.gensalt();
    System.out.println(BCrypt.hashpw("password",salt));
  }
}
