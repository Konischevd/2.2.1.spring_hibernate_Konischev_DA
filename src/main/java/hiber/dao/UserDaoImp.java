package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User getUserByCar(String model, int series) {

      // очень тупой способ:
//      Car car = new Car(model, series);
//      TypedQuery<Long> q_car = sessionFactory.getCurrentSession().createQuery(
//              "select c.id from Car c where c.model = :model and c.series = :series");
//      q_car.setParameter("model", model);
//      q_car.setParameter("series", series);
//      long carId = q_car.getSingleResult();
//
//      TypedQuery<User> q_user = sessionFactory.getCurrentSession().createQuery("from User where id = :id");
//      q_user.setParameter("id", carId);
//
//      return q_user.getSingleResult();

      return (User) sessionFactory.getCurrentSession()
              .createQuery("select u from User u where u.car.model = :model and u.car.series = :series")
              .setParameter("model", model)
              .setParameter("series", series)
              .getSingleResult();
   }
}
