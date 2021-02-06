package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MainApp {
   public static void main(String[] args) {

      AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

      UserService userService = context.getBean(UserService.class);

      // Сохраняем пользователей в БД
      User u1 = new User("Dimas", "Konischev", "fimka41@mail.ru");
      u1.setCar(new Car("Skoda", 7));
      userService.add(u1);

      User u2 = new User("Ilya", "Trifon", "trifon@mail.ru");
      u2.setCar(new Car("BMW", 3));
      userService.add(u2);

      User u3 = new User("Antonio", "Votin", "votin@mail.ru");
      u3.setCar(new Car("Wolksvagen", 666));
      userService.add(u3);

      User u4 = new User("Putin", "Vor", "vor@mail.ru");
      u4.setCar(new Car("Shuttle", 777));
      userService.add(u4);


      // Считываем пользователей из БД
      List<User> list = userService.listUsers();
      for (User user : list) {
         System.out.println(user.toString());
      }

      // Достаём User по модели и серии машины
      System.out.println(userService.getUserByCar("BMW", 3));

      context.close();
   }
}
