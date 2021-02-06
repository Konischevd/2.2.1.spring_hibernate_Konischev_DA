package hiber.config;

import hiber.model.Car;
import hiber.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.net.CacheRequest;
import java.util.Properties;


@Configuration // файл конфигурации спринг приложения, здесь мы пеерчисляем бины,
               // или указываем где их искат

@PropertySource("classpath:db.properties") // файл из которого берём значения перечисленных полей

@EnableTransactionManagement // включить поддержку транзакций (поддержку аннотаций @Transactional)

@ComponentScan(value = "hiber") // где искать бины


public class AppConfig {

   // это я не понимаю, зачем мы подтягиваем сюда бин и откуда. Ведь в обычной настройке hibernate
   // мы просто клали переменные по типу: Environment.DRIVER в объект Properties
   @Autowired
   private Environment env;

   // здесь, в объект DataSource кладём настройки драйвера для РСУБД
   // настройки берём из файла db.properties
   // по сути это настройки базы данных
   @Bean
   public DataSource getDataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(env.getProperty("db.driver"));
      dataSource.setUrl(env.getProperty("db.url"));
      dataSource.setUsername(env.getProperty("db.username"));
      dataSource.setPassword(env.getProperty("db.password"));
      return dataSource;
   }

   // здесь создаём LocalSessionFactoryBean - т.е. SessionFactory
   //  - у него с момощью методов:
   //    - .setDataSource              (настройки БД сверху)
   //    - и .setHibernateProperties   (настройки Hibernate из файла)
   //    добавляем все необходимые настройки БД и Hibernate
   //
   // Далее - указываем классы бинов
   @Bean
   public LocalSessionFactoryBean getSessionFactory() {
      // создаём LocalSessionFactoryBean - т.е. SessionFactory
      LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

      // в LocalSessionFactoryBean кладём настройки БД
      //  - вызываем метод сверху, получаем DataSource с настрйоками БД
      factoryBean.setDataSource(getDataSource());

      // в объект Properties кладём настройки Hibernate
      Properties props = new Properties();
      props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
      props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
      props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));

      // применяем настройки Hibernate к LocalSessionFactoryBean
      factoryBean.setHibernateProperties(props);

      // указываем аннотируемый класс(ы), т.е. класы, которые будут
      // являться сущностями (настройка Hibernate)
      factoryBean.setAnnotatedClasses(User.class, Car.class);    // сущности hibernate


      return factoryBean;
   }


   // бин для автоматического открытия и закрытия транзакции
   @Bean
   public HibernateTransactionManager getTransactionManager() {
      HibernateTransactionManager transactionManager = new HibernateTransactionManager();
      transactionManager.setSessionFactory(getSessionFactory().getObject());
      return transactionManager;
   }
}
